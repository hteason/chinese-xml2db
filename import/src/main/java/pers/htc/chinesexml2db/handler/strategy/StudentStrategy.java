package pers.htc.chinesexml2db.handler.strategy;

import bean.Importable;
import strategy.AbstractImportStrategyTemplate;
import strategy.anno.Strategy;
import pers.htc.chinesexml2db.bean.Student;

import java.util.Map;

@Strategy(forClass = Student.class)
public class StudentStrategy extends AbstractImportStrategyTemplate {

    @Override
    protected void doHandle(Map<String, Object> objMap, Class<? extends Importable> nodeClass) {
//        Student student = trans2class(objMap, nodeClass);
//        String classId = getParentId();
//        student.setClassId(classId);
//        //获取已存在的学生列表数据
//        List<Student> studentList = (List<Student>) dataMap.getOrDefault(KEY,new ArrayList<>());
//        //将新的学生数据添加到列表里
//        studentList.add(student);
//        dataMap.put(KEY,studentList);

        ((Student) importable).setClassId(getParentId());
    }
}
