package constant;

import handler.AbstractImportStrategyTemplate;
import handler.anno.IntValueMap;
import handler.anno.NodeName;
import handler.anno.Strategy;
import handler.anno.XMLAttr;
import org.reflections.Reflections;
import org.yaml.snakeyaml.Yaml;

import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * 映射类，主要是XML内容和数据库的映射
 */
//@Configuration
//@PropertySource(value = "classpath:/import-maps.yml", encoding = "utf-8")
public class GlobalMap {
    private GlobalMap() {
    }

    public static Map<String, String> CLASS_NAME = new HashMap<>();
    public static Map<String, Integer> STR_TO_INT = new HashMap<>();
    private static Map<Class<?>, AbstractImportStrategyTemplate> STRATEGY_MAP = new HashMap<>();
    /*
    以pers.htc.chinesexml2db.bean.Student为例
    学生：
        姓名:name
        年龄:age
     */
    public static Map<String, Map<String, String>> CLASS_FIELDS = new HashMap<>();

    static {
        //try-with-resources：自动关闭输入流
        try (InputStreamReader in = new InputStreamReader(GlobalMap.class.getResourceAsStream("/import-maps.yml"), StandardCharsets.UTF_8)) {
            initByYaml(in);
        } catch (IOException e) {
            System.err.println("在resources下找不到配置文件import-maps.yml");
            e.printStackTrace();
        }
    }

    /**************** 通过yaml方式加载 *****************/
    private static void initByYaml(InputStreamReader in) {
        Yaml yaml = new Yaml();
        Map<String, Object> map = (Map<String, Object>) yaml.load(in);
        String beanPackage = (String) map.get("bean_package");
        String strategyPackage = (String) map.get("strategy_package");//策略所在包

        initClassName(map, beanPackage);
        initStrToInt(map);
        initClassFields(map);

        initClassNameNyAnno(beanPackage);
        initClassFieldsByAnno(beanPackage);
        initStrategyMapByAnno(strategyPackage);//添加策略到工厂Map
        print();
    }


    public static void initClassName(Map<String, Object> map, String beanPackage) {
        Map<String, String> className = (Map<String, String>) map.getOrDefault("CLASS_NAME",new HashMap<>());
        for (Map.Entry<String, String> entry : className.entrySet()) {
            CLASS_NAME.put(entry.getKey(), beanPackage + "." + entry.getValue());
        }
    }

    public static void initStrToInt(Map<String, Object> map) {
        STR_TO_INT = (Map<String, Integer>) map.getOrDefault("STR_TO_INT",new HashMap<>());
    }

    public static void initClassFields(Map<String, Object> map) {
        CLASS_FIELDS = (Map<String, Map<String, String>>) map.getOrDefault("CLASS_FIELDS",new HashMap<>());
    }

    public static void setStrategyMap(Map<Class<?>, AbstractImportStrategyTemplate> strategyMap) {
        STRATEGY_MAP = strategyMap;
    }

    public static void setClassFields(Map<String, Map<String, String>> classFields) {
        CLASS_FIELDS = classFields;
    }


    /**************** 通过properties方式加载文件 *****************/
    private static void initByPro(InputStreamReader in) throws IOException {
        Properties pro = new Properties();
        pro.load(in);
        String beanPackage = pro.getProperty("bean_package");//xml对应实体类所在包
        String strategyPackage = pro.getProperty("strategy_package");//策略所在包

        initClassName(beanPackage, pro.getProperty("CLASS_NAME"));
        initStrToInt(pro.getProperty("STR_TO_INT"));
        initClassFields(pro.getProperty("CLASS_FIELDS"));

        initClassNameNyAnno(beanPackage);
        initClassFieldsByAnno(beanPackage);
        initStrategyMapByAnno(strategyPackage);//添加策略到工厂Map
    }

    public static void initClassName(String beanPackage, String classNameStr) {
        Yaml yaml = new Yaml();
//        Map<String, String> className = (Map<String, String>) yaml.load(classNameStr);
        Map<String, String> className = (Map<String, String>) yaml.loadAs(classNameStr, HashMap.class);
        for (Map.Entry<String, String> entry : className.entrySet()) {
            CLASS_NAME.put(entry.getKey(), beanPackage + "." + entry.getValue());
        }
    }

    public static void initStrToInt(String strToInt) {
        Yaml yaml = new Yaml();
        GlobalMap.STR_TO_INT = (Map<String, Integer>) yaml.load(strToInt);
    }

    public static void initClassFields(String propertiesStr) {
        Yaml yaml = new Yaml();
//        CLASS_FIELDS = (Map<String, String>) yaml.load(propertiesStr);
    }

    public static void initClassNameNyAnno(String beanPackage) {
        Reflections reflections = new Reflections(beanPackage);
        Set<Class<?>> classesList = reflections.getTypesAnnotatedWith(NodeName.class);
        for (Class<?> clazz : classesList) {
            NodeName nodeName = clazz.getAnnotation(NodeName.class);
            CLASS_NAME.put(nodeName.value(), clazz.getName());
        }
    }

    public static void initStrategyMapByAnno(String strategyPackage) {
        Reflections reflections = new Reflections(strategyPackage);
        Set<Class<?>> classesList = reflections.getTypesAnnotatedWith(Strategy.class);

        // 存放url和ExecutorBean的对应关系
        for (Class<?> classes : classesList) {
            Strategy strategy = classes.getDeclaredAnnotation(Strategy.class);
            Class<?> forClass = strategy.forClass();
            try {
                STRATEGY_MAP.put(forClass, (AbstractImportStrategyTemplate) classes.newInstance());
            } catch (InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    public static Map<Class<?>, AbstractImportStrategyTemplate> getStrategyMap() {
        return STRATEGY_MAP;
    }

    public static void initClassFieldsByAnno(String beanPackage) {
        Reflections reflections = new Reflections(beanPackage);
        Set<Class<?>> classSet = reflections.getTypesAnnotatedWith(NodeName.class);

        for (Class<?> clazz : classSet) {
            Field[] fields = clazz.getDeclaredFields();
            NodeName nodeName = clazz.getAnnotation(NodeName.class);
            String nodeNameStr = nodeName.value();
            //查看部分映射是否已经在yml文件完成了
            //存在部分：在原来基础上做添加
            Map<String, String> filedInClassMap = CLASS_FIELDS.getOrDefault(nodeNameStr,new HashMap<>());
            for (Field field : fields) {
                XMLAttr fieldAnno = field.getAnnotation(XMLAttr.class);
                if (fieldAnno != null) {
                    String cnValue = fieldAnno.value();//对应的中文属性名
                    String fieldName = field.getName();
                    filedInClassMap.put(cnValue, fieldName);
                }

                IntValueMap intValueMap = field.getAnnotation(IntValueMap.class);
                if (intValueMap != null) {
                    initStrToIntByAnno(intValueMap);
                }
            }
            if (!filedInClassMap.isEmpty()) {
                CLASS_FIELDS.put(nodeNameStr, filedInClassMap);
            }
        }
    }

    private static void initStrToIntByAnno(IntValueMap intValueMap) {
        String[] values = intValueMap.value();
        for (String value : values) {
            if (Objects.isNull(value) || "".equals(value)) {
                continue;
            }
            String[] val = value.split("=");
            if (val.length != 2) {
                continue;
            }
            STR_TO_INT.put(val[0], Integer.parseInt(val[1].trim()));
        }
    }




    private static void print(){
        System.out.println(GlobalMap.CLASS_NAME);
        System.out.println(GlobalMap.CLASS_FIELDS);
        System.out.println(GlobalMap.STR_TO_INT);
        System.out.println(GlobalMap.STRATEGY_MAP);
    }
}
