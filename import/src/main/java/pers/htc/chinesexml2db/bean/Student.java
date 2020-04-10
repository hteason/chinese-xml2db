package pers.htc.chinesexml2db.bean;

import bean.Importable;
import strategy.anno.IntValueMap;
import strategy.anno.XMLAttr;
import strategy.anno.NodeName;
import lombok.Data;

import java.util.UUID;

@Data
@NodeName("学生")
public class Student implements Importable {
    private String id = uuid();
    @XMLAttr("姓名")
    private String name;
    @XMLAttr("年龄")
    private Integer age;
//    @XMLAttr("性别")
    @IntValueMap({"男=1", "女=2", "未知=3"})
    private String sex;
    @XMLAttr("班级")
    private String classId;
}
