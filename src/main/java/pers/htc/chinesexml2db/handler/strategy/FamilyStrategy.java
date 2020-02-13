package pers.htc.chinesexml2db.handler.strategy;

import pers.htc.chinesexml2db.bean.Family;
import pers.htc.chinesexml2db.handler.AbstractImportStrategyTemplate;
import pers.htc.chinesexml2db.handler.anno.Strategy;

import java.security.Key;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Strategy(ForClass = Family.class)
public class FamilyStrategy extends AbstractImportStrategyTemplate {
    private final String KEY = "FamilyList";

    @Override
    protected String doHandle(Map<String, Object> objMap, Class<?> nodeClass, Map<String, Object> dataMap) {
        Family parent = trans2class(objMap, nodeClass);
        parent.setStudentId(getParentId(dataMap));
        List<Family> parentList = (List<Family>) dataMap.get(KEY);
        if (Objects.isNull(parentList)) {
            parentList = new ArrayList<>();
            dataMap.put(KEY, parentList);
        }
        parentList.add(parent);
        return parent.getId();
    }
}
