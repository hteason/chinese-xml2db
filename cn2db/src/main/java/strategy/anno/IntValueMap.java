package strategy.anno;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * xml属性值和数据库字段代表值的映射，字符串数组类型，如:
 * {"父亲=1","母亲=2"}，等于号分隔
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface IntValueMap {
    String[] value();
}
