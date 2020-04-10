package pers.htc.chinesexml2db.bean;

import bean.Importable;
import lombok.Data;
import strategy.anno.NodeName;
import strategy.anno.XMLAttr;

@Data
@NodeName("学院")
public class Academy implements Importable {
    private String id = uuid();
    @XMLAttr("名称")
    private String name;
    @XMLAttr("代码")
    private String code;
}
