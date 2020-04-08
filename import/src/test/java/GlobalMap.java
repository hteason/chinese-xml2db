import handler.AbstractImportStrategyTemplate;
import handler.anno.NodeName;
import handler.anno.Strategy;
import handler.anno.XMLAttr;
import lombok.Data;
import org.reflections.Reflections;
import org.yaml.snakeyaml.Yaml;

import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 映射类，主要是XML内容和数据库的映射
 */
@Data
public class GlobalMap {

    public Map<String, String> CLASS_NAME;
    public Map<String, Integer> STR_TO_INT;
    public Map<String, String> CLASS_FIELDS;
//    private static Map<Class<?>, AbstractImportStrategyTemplate> STRATEGY_MAP = new ConcurrentHashMap<>();
    /*
    以pers.htc.chinesexml2db.bean.Student为例
    学生：
        姓名:name
        年龄:age
     */
//    public static Map<String, Map<String, String>> CLASS_FIELDS = new HashMap<>();
}
