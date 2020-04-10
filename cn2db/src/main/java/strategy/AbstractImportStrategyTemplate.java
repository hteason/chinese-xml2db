package strategy;

import bean.Importable;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import strategy.anno.Strategy;
import org.dom4j.Element;
import util.ThreadLocalUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 抽象策略模式的模板方法类
 */
public abstract class AbstractImportStrategyTemplate {
    protected final String CURRENT_NODE = "CURRENT_NODE";
    protected final String ID = "ID";

    //若拿不到父级id，则是顶层id，默认为0
    protected final String DEFAULT_ID = "0";

    /**
     * 以下变量由{@link #preHandle}方法获取
     */
    //存放所有转换成功后的实例对象和当前访问的xml节点
    protected Map<String, Object> dataMap;
    //响应json中的key，动态默认为实体类名称
    protected String KEY;
    protected Importable importable;

    /**
     * 模板方法：
     * 1.获取父节点的id
     * 2.转换为相应示例类对象，并存入dataMap（钩子方法）
     * 3.将当前节点的id新增到xml新属性中
     *
     * @param objMap    键值对对象
     * @param nodeClass 当前节点对应的class类
     */
    public void handler(Map<String, Object> objMap, Class<? extends Importable> nodeClass) {
        preHandle(objMap, nodeClass);
        doHandle(objMap, nodeClass);//返回实体类主键id
        postHandle();
    }


    /**
     * 获取当前节点的父节点id
     *
     * @return 父节点id
     */
    protected String getParentId() {
        Element currentNode = (Element) dataMap.get(CURRENT_NODE);
        Element parent = currentNode.getParent();
        return parent.attributeValue(ID) == null ? DEFAULT_ID : parent.attributeValue(ID);
    }

    /**
     * 转为实体类：解开具体Strategy实现类对BaseStrategy的耦合
     *
     * @param objMap
     * @param nodeClass
     * @param <T>
     * @return
     */
    protected <T> T trans2class(Map<String, Object> objMap, Class<?> nodeClass) {
        return (T) (JSON.toJavaObject((JSON) JSONObject.toJSON(objMap), nodeClass));
    }

    /**
     * 钩子方法，具体逻辑留给用户在派生类实现
     *
     * @param objMap
     * @param nodeClass
     */
    protected void doHandle(Map<String, Object> objMap, Class<? extends Importable> nodeClass) {

    }


    //每次执行handle之前的操作
    private void preHandle(Map<String, Object> objMap, Class<? extends Importable> nodeClass) {
        dataMap = ThreadLocalUtil.getMap();//每次操作前都要取最新的数据
        getMapKey();
        toBean(objMap, nodeClass);
    }

    private void toBean(Map<String, Object> objMap, Class<? extends Importable> nodeClass) {
        importable = trans2class(objMap, nodeClass);
//        System.out.println(importable);
        List<? super Importable> importableList = (List<? super Importable>) dataMap.getOrDefault(KEY, new ArrayList<>());
        importableList.add(importable);
        dataMap.put(KEY, importableList);
    }

    /**
     * 获取响应结果Map的key值，默认是实体类的简单名称
     */
    private void getMapKey() {
        KEY = this.getClass().getAnnotation(Strategy.class).forClass().getSimpleName();
    }

    /**
     * 每次操作成功后都要保存id
     */
    private void postHandle() {
        setId();
        setParentId();
    }


    /**
     * 给当前的节点新增id属性，若有子节点，则子节点能取其作为父节点，即：parentId
     */
    private void setId() {
        //currentId 当前节点的id
        String currentId = importable.getId();
        Element currentNode = (Element) dataMap.get(CURRENT_NODE);
        currentNode.addAttribute(ID, currentId == null ? DEFAULT_ID : currentId);
    }

    private void setParentId() {
        importable.setParentId(getParentId());
    }

}
