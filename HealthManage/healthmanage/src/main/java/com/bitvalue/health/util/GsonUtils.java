package com.bitvalue.health.util;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;

import org.xutils.common.util.LogUtil;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Gson 工具类
 *
 * @author blj
 */
public class GsonUtils {
    private static Gson gson = null;

    private GsonUtils() {

    }

    public static String ModelToJson(Object obj) {
        if (null == obj) {
            return null;
        }
        return GsonUtils.getGson().toJson(obj);
    }


    /**
     * 将返回的普通model类型的Json字符串解析为model
     * 泛型使用报错  com.google.gson.internal.LinkedTreeMap cannot be cast to
     * @param <T>
     * @param json
     * @return
     */
    @SuppressWarnings("unchecked")
    public static  <T> T jsonToModel(String json, Class<T> clazz) {
        if (TextUtils.isEmpty(json) || null == clazz) {
            return null;
        }
        try {
            return gson.fromJson(json, clazz);
        }catch (Exception e){
            e.printStackTrace();
            LogUtil.e("服务端接口json数据格式异常:"+e.getMessage());
            return null;
        }

    }
    /**
     * 将返回的List类型的Json字符串解析为list
     * @param <T>
     * @param json
     * @return
     */
    public static  <T> List<T> jsonToList(String json, Class<T> clazz) {
        if (TextUtils.isEmpty(json) || null == clazz) {
            return null;
        }
        List<T> list = new ArrayList<T>();
        JsonArray array = new JsonParser().parse(json).getAsJsonArray();

        for(final JsonElement elem : array){
            list.add(getGson().fromJson(elem, clazz));
        }
        return list;

    }

    /**
     * 将model类型的list转为json数据格式的字符串
     * @return
     */
    public static  <T> String ListTojson(List<T> T) {
        String jsonStr = getGson().toJson(T);
        return jsonStr;
    }

    public static Gson getGson() {

        if(gson == null) {
            GsonBuilder builder = new GsonBuilder();

            // Register an adapter to manage the date types as long values
            builder.registerTypeAdapter(Date.class, new JsonDeserializer<Date>() {
                public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                    return new Date(json.getAsJsonPrimitive().getAsLong());
                }
            });

            gson = builder.create();
        }

        return gson;
    }
}
