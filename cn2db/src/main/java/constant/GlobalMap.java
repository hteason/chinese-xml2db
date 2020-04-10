package constant;

import strategy.AbstractImportStrategyTemplate;
import strategy.anno.IntValueMap;
import strategy.anno.NodeName;
import strategy.anno.Strategy;
import strategy.anno.XMLAttr;
import org.reflections.Reflections;
import org.springframework.util.StringUtils;
import org.yaml.snakeyaml.Yaml;

import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * 映射类，主要是XML内容和数据库的映射
 */
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
        if (StringUtils.isEmpty(beanPackage)){
            throw new IllegalArgumentException("必须添加bean_package属性指定实体类所在包名");
        }
        String strategyPackage = (String) map.get("strategy_package");//策略所在包
        if (StringUtils.isEmpty(beanPackage)){
            throw new IllegalArgumentException("必须添加strategy_package属性指定策略类所在包名");
        }

        //通过yml配置文件配置
        initClassName(map, beanPackage);
        initStrToInt(map);
        initClassFields(map);

        //将带有@NodeName注解的实体类添加目标到CLASS_NAME
        initClassNameNyAnno(beanPackage);
        //通过@Strategy注解添加策略到工厂STRATEGY_MAP
        initStrategyMapByAnno(strategyPackage);
        print();
    }

    /**
     * 从import-maps.yml加载元素名和实体类的映射，如：
     * "学生"对应[bean_package+CLASS_NAME]即pers.htc.chinesexml2db.bean.Student
     *
     * @param map         从配置文件读取到的Map，会存储到CLASS_NAME
     * @param beanPackage 对应目标实体类的包名
     */
    public static void initClassName(Map<String, Object> map, String beanPackage) {
        Map<String, String> className = (Map<String, String>) map.getOrDefault("CLASS_NAME", new HashMap<>());
        for (Map.Entry<String, String> entry : className.entrySet()) {
            CLASS_NAME.put(entry.getKey(), beanPackage + "." + entry.getValue());
        }
    }

    /**
     * 从import-maps.yml加载属性值和数据库代表值的映射，如：
     * 父亲对应1
     *
     * @param map 从配置文件读取到的Map，会存储到STR_TO_INT
     */
    public static void initStrToInt(Map<String, Object> map) {
        STR_TO_INT = (Map<String, Integer>) map.getOrDefault("STR_TO_INT", new HashMap<>());
    }

    /**
     * 从import-maps.yml加载属性和实体类属性的映射，如：
     * 学生下的性别对应Student类下的sex属性
     *
     * @param map 从配置文件读取到的Map，会存储到STR_TO_INT
     */
    public static void initClassFields(Map<String, Object> map) {
        CLASS_FIELDS = (Map<String, Map<String, String>>) map.getOrDefault("CLASS_FIELDS", new HashMap<>());
    }

    /**
     * 将带有@NodeName注解的实体类添加目标到CLASS_NAME
     *
     * @param beanPackage 目标实体类所在的包名
     */
    public static void initClassNameNyAnno(String beanPackage) {
        Reflections reflections = new Reflections(beanPackage);
        Set<Class<?>> classesList = reflections.getTypesAnnotatedWith(NodeName.class);
        for (Class<?> clazz : classesList) {
            NodeName nodeName = clazz.getAnnotation(NodeName.class);
            CLASS_NAME.put(nodeName.value(), clazz.getName());
            //将类下含@XMLAttr注解的属性映射加入CLASS_FIELDS
            //将类下含@IntValueMap注解的属性映射加入STR_TO_INT
            initClassFieldsByAnno(nodeName.value(), clazz.getDeclaredFields());
        }
    }

    /**
     * 将类下含@XMLAttr注解的属性映射加入CLASS_FIELDS
     * 将类下含@IntValueMap注解的属性映射加入STR_TO_INT
     *
     * @param nodeNameStr 元素名
     * @param fields      对应实体类下的所有属性
     */
    public static void initClassFieldsByAnno(String nodeNameStr, Field[] fields) {
        //查看部分映射是否已经在yml文件完成了
        //存在映射：在原来基础上做添加，同key的做覆盖，注解优先
        Map<String, String> filedInClassMap = CLASS_FIELDS.getOrDefault(nodeNameStr, new HashMap<>());
        for (Field field : fields) {
            XMLAttr fieldAnno = field.getAnnotation(XMLAttr.class);
            if (fieldAnno != null) {
                String cnValue = fieldAnno.value();//对应的中文属性名
                String fieldName = field.getName();
                filedInClassMap.put(cnValue, fieldName);
            }

            //将类下含@IntValueMap注解的属性映射加入STR_TO_INT
            IntValueMap intValueMap = field.getAnnotation(IntValueMap.class);
            if (intValueMap != null) {
                initStrToIntByAnno(intValueMap);
            }
        }
        if (!filedInClassMap.isEmpty()) {
            CLASS_FIELDS.put(nodeNameStr, filedInClassMap);
        }
    }

    /**
     * 将类下含@IntValueMap注解的属性映射加入STR_TO_INT
     *
     * @param intValueMap 映射值，字符串数组类型，如{"父亲=1","母亲=2"}，等于号分隔
     */
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

    /**
     * 通过@Strategy注解添加策略到工厂STRATEGY_MAP
     *
     * @param strategyPackage 策略类所在包名
     */
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

    /**
     * 向策略工厂{@link strategy.factory.ImportStrategyFactory}提供策略接口
     *
     * @return 管理策略Bean的Map
     */
    public static Map<Class<?>, AbstractImportStrategyTemplate> getStrategyMap() {
        return STRATEGY_MAP;
    }

    /**
     * 打印所需的初始化映射对象
     */
    private static void print() {
        System.out.println("--------------xml元素名和类名的映射--------------");
        System.out.println(GlobalMap.CLASS_NAME);
        System.out.println("--------------xml属性和类属性的映射--------------");
        System.out.println(GlobalMap.CLASS_FIELDS);
        System.out.println("--------------xml属性值和数据库字段代表值的映射--------------");
        System.out.println(GlobalMap.STR_TO_INT);
        System.out.println("--------------xml元素名和相应处理策略类的映射--------------");
        System.out.println(GlobalMap.STRATEGY_MAP);
    }
}
