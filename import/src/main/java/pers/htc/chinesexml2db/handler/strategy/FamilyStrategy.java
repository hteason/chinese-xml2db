package pers.htc.chinesexml2db.handler.strategy;

import bean.Importable;
import handler.AbstractImportStrategyTemplate;
import handler.anno.Strategy;
import pers.htc.chinesexml2db.bean.Family;

import java.util.Map;

@Strategy(forClass = Family.class)
public class FamilyStrategy extends AbstractImportStrategyTemplate {

    @Override
    protected void doHandle(Map<String, Object> objMap, Class<? extends Importable> nodeClass) {
//        Family parent = trans2class(objMap, nodeClass);
//        parent.setStudentId(getParentId());
//        List<Family> parentList = (List<Family>) dataMap.getOrDefault(KEY,new ArrayList());
//        parentList.add(parent);
//        dataMap.put(KEY,parentList);

        ((Family) importable).setStudentId(getParentId());
    }

}
