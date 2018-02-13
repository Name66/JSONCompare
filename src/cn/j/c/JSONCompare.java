package cn.proj;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.json.JSONTokener;

public class JSONCompare {
    static String jsonString1 = "";
    static String jsonString2 = "";
    public static void main(String[] args) {
        JSONCompare comp = new JSONCompare();
        try {
            comp.mJsonCompareAll(jsonString1, jsonString2);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * JSON校验入口
     * @param jsonString1
     * @param jsonString2
     * @throws Exception
     */
    public void mJsonCompareAll(String jsonString1, String jsonString2) throws Exception{
        if (this.isJSONArray(jsonString1) && this.isJSONArray(jsonString2)) {
            if (mJsonArrayCompare(JSONArray.parseArray(jsonString1), JSONArray.parseArray(jsonString2))) {
                System.out.println("JSON全量校验成功!");
            } else {
                System.out.println("JSON全量校验失败!");
            }
        } else if (this.isJSONObject(jsonString1) && this.isJSONObject(jsonString2)) {
            if (mJsonArrayCompare(JSONArray.parseArray(jsonString1), JSONArray.parseArray(jsonString2))) {
                System.out.println("JSON全量校验成功!");
            } else {
                System.out.println("JSON全量校验失败!");
            }
        } else {
            throw new RuntimeException("数据不是有效的JSON格式");
        }
    }

    private boolean mJsonObjectCompare(JSONObject obj1, JSONObject obj2) throws Exception {
        if (obj1.size() == obj2.size()) {
            for (String key : obj1.keySet()) {
                if (obj2.containsKey(key)) {
                    if (obj1.get(key) instanceof JSONArray && obj2.get(key) instanceof JSONArray) {
                        if (!mJsonArrayCompare(obj1.getJSONArray(key), obj2.getJSONArray(key))) {
                            return false;
                        }
                    } else if (obj1.get(key) instanceof JSONObject && obj2.get(key) instanceof JSONObject) {
                        if (!mJsonObjectCompare(obj1.getJSONObject(key), obj2.getJSONObject(key))) {
                            return false;
                        }
                    } else if (obj1.getString(key) != null && obj2.getString(key) != null) {
                        if (!obj1.getString(key).equals(obj2.getString(key))) {
                            System.out.println(key + ": " + obj1.getString(key) + ";" + key + ": " + obj2.getString(key) + ";同键不同值!");
                            return false;
                        }
                    }  else {
                        return false;
                    }
                } else {
                    return false;
                }
            }
        } else {
            return false;
        }
        return true;
    }

    private boolean mJsonArrayCompare(JSONArray array1, JSONArray array2) throws Exception {
        if (array1.size() == array2.size()) {
            for (int i = 0; i < array1.size(); i++) {
                for (int j = 0; j < array2.size(); j++) {
                    if (array1.get(i) instanceof String && array2.get(j) instanceof String) {
                        if (!array1.getString(i).equals(array2.getString(j))) {
                            if (j != array2.size() - 1) {
                                System.out.println(array1.getString(i) + "和" + array2.getString(j) + "不同，继续校验!");
                            } else {
                                System.out.println(array1.getString(i) + "和" + array2.getString(j) + "不同，已完成全部内容校验!");
                                throw new Exception(array1.toString() + "和" + array2.toString() + "大循环校验完毕，校验失败!");
                            }
                        } else {
                            break;
                        }
                        if (j == array2.size() - 1) {
                            return false;
                        }
                    } else if (!mJsonObjectCompare(array1.getJSONObject(i), array2.getJSONObject(j))) {
                        if (j != array2.size() - 1) {
                            System.out.println(array1.getJSONObject(i) + "和" + array2.getJSONObject(j) + "不同，继续校验!");
                        } else {
                            System.out.println(array1.getJSONObject(i) + "和" + array2.getJSONObject(j) + "不同, 已完成全部内容校验!");
                            throw new Exception(array1.getJSONObject(i) + "和" + array2.getJSONObject(j) + "大循环校验完毕，校验失败!");
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
            return false;
        }
        return true;
    }

    /**
     * 判断字符串是否为JSONArray类型
     * @param stringValue
     * @return
     */
    public boolean isJSONArray(String stringValue){
        return new JSONTokener(stringValue).nextValue() instanceof org.json.JSONArray;
    }

    /**
     * 判断字符串是否是JSONObject类型
     * @param stringValue
     * @return
     */
    public boolean isJSONObject(String stringValue) {
        return new JSONTokener(stringValue).nextValue() instanceof org.json.JSONObject;
    }

    /**
     * 判断字符串是否符合JSON格式
     * @param stringValue
     * @return
     */
    public boolean isJSONString(String stringValue) {
        return this.isJSONObject(stringValue) || this.isJSONArray(stringValue);
    }
}
