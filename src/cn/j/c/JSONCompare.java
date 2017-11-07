package cn.j.c;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public class JSONCompare {

    public static void toCompareJsonString(String json1, String json2) {
        if (json1.startsWith("[") && json2.startsWith("[") && json1.endsWith("]") && json2
                .endsWith("]")) {
            if (compArray(JSONArray.parseArray(json1), JSONArray.parseArray(json2))) {
                System.out.println("JSON全量校验成功！");
            } else {
                System.out.println("Json全量校验失败！");
            }
        } else if(json1.startsWith("{") && json2.startsWith("{") && json1.endsWith("}") && json2
                .endsWith("}")){
            if (compObj(JSONObject.parseObject(json1), JSONObject.parseObject(json2))) {
                System.out.println("JSON全量校验成功！");
            } else {
                System.out.println("Json全量校验失败！");
            }
        }else {
            throw new RuntimeException("");
        }
    }

    private static boolean compObj(JSONObject obj1, JSONObject obj2) {
        if (obj1.size() == obj2.size()) {
            for (String key : obj1.keySet()) {
                if (obj2.containsKey(key)) {
                    if (obj1.get(key) instanceof JSONArray && obj2.get(key) instanceof JSONArray) {
                        if (!compArray(obj1.getJSONArray(key), obj2.getJSONArray(key))) {
                            //System.out.println(key + "内容校验失败");
                            return false;
                        }
                    } else if (obj1.getString(key) instanceof String && obj2.getString(key) instanceof String) {
                        if (!obj1.getString(key).equals(obj2.getString(key))) {
                            System.out.println(key + ": " + obj1.getString(key) + ";" + key + ": " + obj2.getString(key) + ";同键不同值！");
                            return false;
                        }
                    } else if (obj1.get(key) instanceof JSONObject && obj2.get(key) instanceof JSONObject) {
                        if (!compObj(obj1.getJSONObject(key), obj2.getJSONObject(key))) {
                            return false;
                        }
                    } else {
                        return false;
                    }
                } else {
                    System.out.println("JSON对象1中含有" + key + "，而JSON对象2中不包含" + key);
                    return false;
                }
            }
        } else {
            System.out.println("JSON全量校验失败，两个JSON数量不相同");
            return false;
        }
        return true;
    }

    private static boolean compArray(JSONArray array1, JSONArray array2) {
        if (array1.size() == array2.size()) {
            for (int i = 0; i < array1.size(); i++) {
                for (int j = 0; j < array2.size(); j++) {
                    if (!compObj(array1.getJSONObject(i), array2.getJSONObject(j))) {
                        if (j != array2.size() - 1) {
                            System.out.println(array1.getJSONObject(i) + "和" + array2.getJSONObject(j) + "不同，继续校验！");
                        } else {
                            System.out.println(array1.getJSONObject(i) + "和" + array2.getJSONObject(j) + "不同, 循环校验完毕，校验失败!");
                        }
                    } else {
                        break;
                    }
                    if (j == array2.size() - 1) {
                        return false;
                    }
                }
            }
        } else {
            //System.out.println(array1.toJSONString() + "和" + array2.toJSONString() + "的数量不同！");
            return false;
        }
        return true;
    }
}
