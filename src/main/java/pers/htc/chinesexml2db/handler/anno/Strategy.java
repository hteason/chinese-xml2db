package pers.htc.chinesexml2db.handler.anno;

import org.springframework.stereotype.Component;

import java.lang.annotation.*;

/**
 * 策略类注解：带有此注解的类都将被识别为xml各标签的处理类，并加入bean容器
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Component
public @interface Strategy {
    Class<?> ForClass();
}
