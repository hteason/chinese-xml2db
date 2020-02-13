package pers.htc.chinesexml2db.handler.strategy;

import pers.htc.chinesexml2db.bean.Student;
import pers.htc.chinesexml2db.handler.AbstractImportStrategyTemplate;
import pers.htc.chinesexml2db.handler.anno.Strategy;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Strategy(ForClass = Student.class)
public class StudentStrategy extends AbstractImportStrategyTemplate {
    private final String KEY = "StudentList";

    @Override
    protected String doHandle(Map<String, Object> objMap, Class<?> nodeClass, Map<String, Object> dataMap) {
        Student student = trans2class(objMap, nodeClass);

        String classId = getParentId(dataMap);
        student.setClassId(classId);

        //获取已存在的学生列表数据
        List<Student> studentList = (List<Student>) dataMap.get(KEY);
        if (Objects.isNull(studentList)) {
            studentList = new ArrayList<>();
            dataMap.put(KEY, studentList);
        }
        //将新的学生数据添加到列表里
        studentList.add(student);

        return student.getId();
    }

//    private void checkListEmpty( Map<String, Object> dataMap){
//        List<Student> studentList = (List<Student>) dataMap.get(KEY);
//        if (Objects.isNull(studentList)) {
//            studentList = new ArrayList<>();
//            dataMap.put(KEY, studentList);
//        }
//    }
}
