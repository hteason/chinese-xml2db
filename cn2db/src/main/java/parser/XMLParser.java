package parser;

import constant.GlobalMap;
import strategy.AbstractImportStrategyTemplate;
import strategy.factory.ImportStrategyFactory;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.springframework.stereotype.Component;
import bean.Importable;
import util.ThreadLocalUtil;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

//解析、封装到实体类、插入数据库
@Component
public class XMLParser {
    /**
     * 读取xml
     *
     * @param xmlFile xml文件流数据
     * @return 封装到实体类后的集合
     */
    public static Map<String, Object> toBeanMap(InputStream xmlFile) throws Exception {
        SAXReader sax = new SAXReader();//创建一个SAXReader对象
        Document document = sax.read(xmlFile);//获取document对象,如果文档无节点，则会抛出Exception提前结束
        Element root = document.getRootElement();//获取根节点
        getNodes(root);//从根节点开始遍历所有节点

//        printData();
        xmlFile.close();
        return ThreadLocalUtil.getAndRemove();
    }

    /**
     * 打印实体类数据
     */
    private static void printData() {
        Map<String, Object> dataMap = ThreadLocalUtil.getMap();
        Set<Map.Entry<String, Object>> entries = dataMap.entrySet();
        for (Map.Entry<String, Object> e : entries) {
            if ("CURRENT_NODE".equals(e.getKey())) {
                continue;
            }
            System.out.println(e.getValue());
        }
    }


    /**
     * 遍历xml
     *
     * @param node 当前所在节点
     */
    private static void getNodes(Element node) throws Exception {
        ThreadLocalUtil.set("CURRENT_NODE", node);
        String nodeName = node.getName().trim();//当前节点的名称
//        System.out.println("当前节点名称：" + nodeName);
        String claName = GlobalMap.CLASS_NAME.get(nodeName);
        if (claName != null && !"".equals(claName)) {
            Class<? extends Importable> nodeClass = (Class<? extends Importable>) Class.forName(GlobalMap.CLASS_NAME.get(nodeName));
            Map<String, String> fieldsMap = GlobalMap.CLASS_FIELDS.get(nodeName);
            if (fieldsMap != null) {
//            System.out.println("当前节点的内容：" + node.getTextTrim());
                List<Attribute> listAttr = node.attributes();//当前节点的所有属性的list
                Map<String, Object> map = new HashMap<>();
                for (Attribute attr : listAttr) {//遍历当前节点的所有属性
                    String attrName = attr.getName();//属性名称
                    String attrValue = attr.getValue();//属性的值
                    //STR_TO_INT:如果可以转整型则存储整型值，不能转则存原来的字符串值
                    Integer intVal = GlobalMap.STR_TO_INT.get(attrValue);

                    String fieldName = fieldsMap.get(attrName);
                    if (fieldName != null) {
                        map.put(fieldName, intVal != null ? intVal : attrValue);
                    }
                }
                //内省机制37k:首次769ms  第二次298ms   225ms   265ms  256ms
//            BeanUtils.populate(obj,map);
                trans2Class(map, nodeClass);
            }
        }

        //递归遍历当前节点所有的子节点
        List<Element> listElement = node.elements();//所有一级子节点的list
        for (Element e : listElement) {//遍历所有一级子节点
            getNodes(e);//递归
        }
    }

    /**
     * 获取相应实体类
     */
    private static void trans2Class(Map<String, Object> objMap, Class<? extends Importable> nodeClass) {
        //通过节点对应的策略类获取相应策略类对象
        AbstractImportStrategyTemplate abstractImportStrategyTemplate = ImportStrategyFactory.getInstance(nodeClass);
        if (abstractImportStrategyTemplate == null) {
            //可能没有实现的策略类则跳过，避免NPE
            return;
        }
        abstractImportStrategyTemplate.handler(objMap, nodeClass);
    }
}

