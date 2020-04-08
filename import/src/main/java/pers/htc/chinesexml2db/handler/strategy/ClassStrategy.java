package pers.htc.chinesexml2db.handler.strategy;

import bean.Importable;
import handler.AbstractImportStrategyTemplate;
import handler.anno.Strategy;
import pers.htc.chinesexml2db.bean.Classes;

import java.util.Map;

@Strategy(forClass = Classes.class)
public class ClassStrategy extends AbstractImportStrategyTemplate {

    @Override
    protected void doHandle(Map<String, Object> objMap, Class<? extends Importable> nodeClass) {
//        Classes classes = trans2class(objMap, nodeClass);
//        List<Classes> classList = (List<Classes>) dataMap.getOrDefault(KEY,new ArrayList<>());
//        classList.add(classes);
//        dataMap.put(KEY,classList);
//        return classes.getId();
    }
}
