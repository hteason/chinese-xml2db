package pers.htc.chinesexml2db.bean;

import bean.Importable;
import handler.anno.XMLAttr;
import handler.anno.NodeName;
import lombok.Data;

import java.util.UUID;

@Data
@NodeName("班级")
public class Classes implements Importable {
    String id = UUID.randomUUID().toString();
    @XMLAttr("年级")
    String grade;
    @XMLAttr("专业")
    String major;
}
