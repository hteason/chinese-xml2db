package handler.anno;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * xml节点名/标签名，如
 * <学生 名字="张三" 年龄="20" 性别="男"></学习>
 * 中的"学生"
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface NodeName {
    String value();
}
