package pers.htc.chinesexml2db.bean;

import java.util.UUID;

public class Family {
    private String id = UUID.randomUUID().toString();
    private String studentId;
    private Integer relation;
    private String name;
    private Integer age;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public Integer getRelation() {
        return relation;
    }

    public void setRelation(Integer relation) {
        this.relation = relation;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    @Override
    public String toString() {
        return "Parent{" +
                "id='" + id + '\'' +
                ", studentId='" + studentId + '\'' +
                ", relation=" + relation +
                ", name='" + name + '\'' +
                ", age=" + age +
                '}';
    }
}
