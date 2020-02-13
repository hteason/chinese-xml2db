package pers.htc.chinesexml2db.handler.strategy;

import pers.htc.chinesexml2db.bean.Classes;
import pers.htc.chinesexml2db.handler.AbstractImportStrategyTemplate;
import pers.htc.chinesexml2db.handler.anno.Strategy;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Strategy(ForClass = Classes.class)
public class ClassStrategy extends AbstractImportStrategyTemplate {
    private final String KEY = "ClassList";

    @Override
    protected String doHandle(Map<String, Object> objMap, Class<?> nodeClass, Map<String, Object> dataMap) {
        Classes classes = trans2class(objMap, nodeClass);
        List<Classes> classList = (List<Classes>) dataMap.get(KEY);
        if (Objects.isNull(classList)) {
            classList = new ArrayList<>();
            dataMap.put(KEY, classList);
        }
        classList.add(classes);
        return classes.getId();
    }
}
