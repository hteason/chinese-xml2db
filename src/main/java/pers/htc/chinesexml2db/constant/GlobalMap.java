package pers.htc.chinesexml2db.constant;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * 映射类，主要是XML内容和数据库的映射
 */
@Component
@PropertySource(value = "classpath:/import-maps.yml", encoding = "utf-8")
public class GlobalMap {
    private GlobalMap() {
    }

    @Value("${package-prefix}")//实体类的包名
    private String packagePrefix;

    public static Map<String, String> CLASS_NAME = new HashMap<>();
    public static Map<String, Integer> STR_TO_INT = new HashMap<>();
    public static Map<String, String> PROPERTIES = new HashMap<>();

    @Value("#{${CLASS_NAME}}")
    public void setClassName(Map<String, String> className) {
        for (Map.Entry<String, String> entry : className.entrySet()) {
            CLASS_NAME.put(entry.getKey(), packagePrefix + entry.getValue());
        }
    }

    @Value("#{${STR-TO-INT}}")
    public void setSTR_TO_INT(Map<String, Integer> STR_TO_INT) {
        GlobalMap.STR_TO_INT = STR_TO_INT;
    }

    @Value("#{${PROPERTIES}}")
    public void setPROPERTIES(Map<String, String> PROPERTIES) {
        GlobalMap.PROPERTIES = PROPERTIES;
    }

//    static {
//        //标记名和类名映射
//        CLASS_NAME.put("学生", Student.class);
//        CLASS_NAME.put("班级", Classes.class);
//        CLASS_NAME.put("家庭成员", Family.class);
//    }


//    static {
//        //字符串和枚举/整数类型映射
//        STR_TO_INT.put("父亲",1);
//        STR_TO_INT.put("母亲",2);
//    }

//    static {
////        属性名和字段名映射
//
//        PROPERTIES.put("姓名","name");
//        PROPERTIES.put("年龄","age");
//
//        PROPERTIES.put("年级","grade");
//        PROPERTIES.put("专业","major");
//
//        PROPERTIES.put("关系","relation");
//
//    }

}
