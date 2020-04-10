package pers.htc.chinesexml2db.handler.strategy;

import bean.Importable;
import pers.htc.chinesexml2db.bean.Academy;
import pers.htc.chinesexml2db.bean.Family;
import strategy.AbstractImportStrategyTemplate;
import strategy.anno.Strategy;

import java.util.Map;

@Strategy(forClass = Academy.class)
public class AcademyStrategy extends AbstractImportStrategyTemplate {

}
