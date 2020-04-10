package pers.htc.chinesexml2db.bean;

import bean.Importable;
import strategy.anno.XMLAttr;
import strategy.anno.NodeName;
import lombok.Data;

@Data
@NodeName("班级")
public class Classes implements Importable {
    String id = uuid();
    @XMLAttr("年级")
    String grade;
    @XMLAttr("专业")
    String major;
    @XMLAttr("所属学院id")
    private String academyId;
}
