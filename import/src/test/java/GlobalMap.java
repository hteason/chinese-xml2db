import lombok.Data;

import java.util.Map;

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
