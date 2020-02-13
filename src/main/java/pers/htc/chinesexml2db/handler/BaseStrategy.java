package pers.htc.chinesexml2db.handler;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.util.Map;

/**
 * 所有策略类的基类
 * 主要用于将Map键值对数据转换为相应的实体类对象
 */
public abstract class BaseStrategy {
    //即Object To Class
    protected <T> T o2c(Map<String, Object> objMap, Class<?> nodeClass) {
        return (T) (JSON.toJavaObject((JSON) JSONObject.toJSON(objMap), nodeClass));
    }
}
