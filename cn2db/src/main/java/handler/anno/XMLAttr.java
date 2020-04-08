package handler.anno;

import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
/**
 * xml属性名，如
 * <学生 名字="张三" 年龄="20" 性别="男"></学习>
 * 中的"姓名"、"年龄"、"性别"为属性名
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface XMLAttr {
    String value();
}
