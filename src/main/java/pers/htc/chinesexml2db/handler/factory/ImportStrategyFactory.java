package pers.htc.chinesexml2db.handler.factory;

import org.reflections.Reflections;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pers.htc.chinesexml2db.handler.AbstractImportStrategyTemplate;
import pers.htc.chinesexml2db.handler.anno.Strategy;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 导入功能策略模式的工厂类，用于在启动时在指定package下获取贴有@Strategy注解的bean并放入Map中，在运行时动态获取实例对象（单例）
 */
@Component
public class ImportStrategyFactory  /*implements ApplicationContextAware*/ {
    @Autowired
    private static Map<Class<?>, AbstractImportStrategyTemplate> strategyMap;

    static {
        strategyMap = new HashMap<>();

        Reflections reflections = new Reflections("pers.htc.chinesexml2db.handler.strategy");
        Set<Class<?>> classesList = reflections.getTypesAnnotatedWith(Strategy.class);

        // 存放url和ExecutorBean的对应关系
        for (Class<?> classes : classesList) {

            Strategy strategy = classes.getDeclaredAnnotation(Strategy.class);
            Class<?> forClass = strategy.ForClass();

            try {
                strategyMap.put(forClass, (AbstractImportStrategyTemplate) classes.newInstance());
            } catch (InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        //扫描@ImportStrategy注解的类

        //放入map中
        //example:
        //  strategyMap.put(ProjectCostDocStrategy.class,new ProjectCostDocStrategy());
    }

    public static AbstractImportStrategyTemplate getInstance(Class<?> clazz) {
        return strategyMap.get(clazz);
    }

/*

    private static ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        if(ImportStrategyFactory.applicationContext == null) {
            ImportStrategyFactory.applicationContext = applicationContext;
        }
        System.out.println("---------------------------------------------------------------------");

        System.out.println("---------------------------------------------------------------------");

        System.out.println("========ApplicationContext配置成功,在普通类可以通过调用SpringUtils.getAppContext()获取applicationContext对象,applicationContext="+ImportStrategyFactory.applicationContext+"========");

        System.out.println("---------------------------------------------------------------------");
    }

    //获取applicationContext
    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    //通过name获取 Bean.
    public static Object getBean(String name){
        return getApplicationContext().getBean(name);
    }

    //通过class获取Bean.
    public static <T> T getBean(Class clazz){
        return (T)getApplicationContext().getBean(clazz);
    }

    //通过name,以及Clazz返回指定的Bean
    public static <T> T getBean(String name,Class<T> clazz){
        return getApplicationContext().getBean(name, clazz);
    }
*/
}
