package pers.htc.chinesexml2db.handler;

import org.dom4j.Element;
import pers.htc.chinesexml2db.handler.anno.Strategy;

import java.util.Map;

/**
 * 抽象策略模式的模板方法类
 */
public abstract class AbstractImportStrategyTemplate extends BaseStrategy {
    protected final String CURRENT_NODE = "CURRENT_NODE";
    protected final String ID = "ID";
    protected final String DEFAULT_ID = "0";//若拿不到父级id，则可能是顶层id，默认为0

    /**
     * 模板方法：
     * 1.获取父节点的id
     * 2.转换为相应示例类对象，并存入dataMap（钩子方法）
     * 3.将当前节点的id新增到xml新属性中
     *
     * @param objMap    键值对对象
     * @param nodeClass 当前节点对应的class类
     * @param dataMap   存放所有转换成功后的实例对象
     */
    public void handler(Map<String, Object> objMap, Class<?> nodeClass, Map<String, Object> dataMap) {
//        String parentId = getParentId(dataMap);   //取不取parentId暂由用户决定
//        IStrategyBean strategyBean = trans2class(objMap, nodeClass);
        String currentId = doHandle(objMap, nodeClass, dataMap);
        setId(currentId, dataMap);
    }

    /**
     * 获取当前节点的父节点id
     *
     * @param dataMap
     * @return 父节点id
     */
    protected String getParentId(Map<String, Object> dataMap) {
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
        return o2c(objMap, nodeClass);
    }

    /**
     * 钩子方法，具体逻辑留给用户在派生类实现
     *
     * @param objMap
     * @param nodeClass
     * @param dataMap
     * @return 返回对象id
     */
    protected abstract String doHandle(Map<String, Object> objMap, Class<?> nodeClass, Map<String, Object> dataMap);

    /**
     * 给当前的节点新增id属性，若有子节点，则子节点能取其作为父节点，即：parentId
     *
     * @param currentId 当前节点的id
     * @param dataMap
     */
    protected void setId(String currentId, Map<String, Object> dataMap) {
        Element currentNode = (Element) dataMap.get(CURRENT_NODE);
        currentNode.addAttribute(ID, currentId == null ? DEFAULT_ID : currentId);
    }

}
