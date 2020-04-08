# chinese-xml2db
纯中文xml格式节点&amp;属性名映射到英文实体类并导入到数据库，Chinese XML import to database

启动项目后访问http://localhost:9999/swagger-ui.html，可选择`/xml files/测试`目录下的文件进行测试.

# 使用步骤
1. 创建对应的数据库表，实现`Importable`接口，建立xml中文名和数据库字段的映射
2. 建立对应实体类,在`import-maps.yml`的`CLASS_NAME`中加入类名
3. 在`import-maps.yml`中的`STR-TO-INT`加入xml属性值在数据库的代表值
4. 在`import-maps.yml`中的`CLASS_FIELDS`加入中英文映射
5. 创建策略类，继承自`AbstractImportStrategyTemplate`并实现doHandle方法
6. 给策略类贴上策略注解`@Strategy`，其中`forClass`元素的值代表需要封装成哪种类型的实体类，例：
```java
@Strategy(forClass = Student.class)
class StudentStrategy extends AbstractImportStrategyTemplate{
    //doHandle
}
```
以上代码说明StudentStrategy策略类最后会将xml内容封装为Student对象。

*************************

- 实体类加映射注解
## 待完善
- 代表值在数据库中也要int类型，避免找不到对应的，需要存直接值
    - 可能的解决方案：存max value，每次遇到不存在的key就自增，并将新key放入map
    - 问题：用户怎么知道这个值对应什么？在数据库备注字段加备注？不够动态

**注**：xml中同名的中文元素属性名必须翻译为相同的英文字段名，且唯一。

## 不足
- 每个实体类需要实现Importable接口，有代码侵入