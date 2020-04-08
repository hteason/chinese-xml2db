package handler.factory;

import constant.GlobalMap;
import handler.AbstractImportStrategyTemplate;
import handler.anno.Strategy;
import org.reflections.Reflections;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 单例工厂
 * 导入功能策略模式的工厂类，用于在启动时在指定package下获取贴有@Strategy注解的bean并放入Map中，在运行时动态获取实例对象（单例）
 */
public class ImportStrategyFactory {

    private static Map<Class<?>, AbstractImportStrategyTemplate> strategyMap = GlobalMap.getStrategyMap();

    public static AbstractImportStrategyTemplate getInstance(Class<?> clazz) {
        return strategyMap.get(clazz);
    }
}
