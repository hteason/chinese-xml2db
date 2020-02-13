package pers.htc.chinesexml2db.api;

import io.swagger.annotations.Api;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import pers.htc.chinesexml2db.constant.GlobalMap;
import pers.htc.chinesexml2db.handler.AbstractImportStrategyTemplate;
import pers.htc.chinesexml2db.handler.factory.ImportStrategyFactory;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Api("导入xml格式文件")
@RestController
public class ImportXmlApi {

    @PostMapping("/upload/file")
    public Object upload(@RequestParam("file") MultipartFile file) {
        //解析、封装到实体类、插入数据库
        try {
            Map<String, Object> ret = readXml(file.getInputStream());
            ret.remove("CURRENT_NODE");
            return ret;
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    /**
     * 读取xml
     * @param xmlFile xml文件流数据
     * @return
     * @throws Exception
     */
    private Map<String, Object> readXml(InputStream xmlFile) throws Exception {

        Map<String, Object> dataMap = new HashMap<>();//存储转换为实体类的结果

        SAXReader sax = new SAXReader();//创建一个SAXReader对象
        Document document = sax.read(xmlFile);//获取document对象,如果文档无节点，则会抛出Exception提前结束
        Element root = document.getRootElement();//获取根节点
        getNodes(root, dataMap);//从根节点开始遍历所有节点

        printData(dataMap);
        xmlFile.close();
        return dataMap;
    }

    /**
     * 打印实体类数据
     */
    private void printData(Map<String, Object> dataMap) {
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
     * @param node 当前所在节点
     * @param dataMap 节点对应的实体类映射数据
     * @throws Exception
     */
    private void getNodes(Element node, Map<String, Object> dataMap) throws Exception {
        dataMap.put("CURRENT_NODE", node);
//        System.out.println("--------------------");
        String nodeName = node.getName().trim();//当前节点的名称
//        System.out.println("当前节点名称：" + nodeName);
        String claName = GlobalMap.CLASS_NAME.get(nodeName);
        if (claName != null && !"".equals(claName)) {
            Class<?> nodeClass = Class.forName(GlobalMap.CLASS_NAME.get(nodeName));
//            Object obj = nodeClass.newInstance();
//            System.out.println("当前节点的内容：" + node.getTextTrim());
            List<Attribute> listAttr = node.attributes();//当前节点的所有属性的list
            Map<String, Object> map = new HashMap<>();
            for (Attribute attr : listAttr) {//遍历当前节点的所有属性
                String attrName = attr.getName();//属性名称
                String attrValue = attr.getValue();//属性的值
//                System.out.println(attrName + "  -  " + ("".equals(attrValue) ? "没有值" : attrValue));
                //反射37k：首次718ms 第二次356ms  第三次225ms 第四次237ms
                //10M：首次72846ms 第二次68342ms  第三次59512ms
//                InvokeKit.setter(obj, attrName, attrValue);

                //STR_TO_INT:如果可以转整型则存储整型值，不能转则存原来的字符串值
                Integer intVal = GlobalMap.STR_TO_INT.get(attrValue);
                map.put(GlobalMap.PROPERTIES.get(attrName), intVal != null ? intVal : attrValue);
            }
            //内省机制37k:首次769ms  第二次298ms   225ms   265ms  256ms
//            BeanUtils.populate(obj,map);
            trans2Class(map, nodeClass, dataMap);
        }

        //递归遍历当前节点所有的子节点
        List<Element> listElement = node.elements();//所有一级子节点的list
        for (Element e : listElement) {//遍历所有一级子节点
            getNodes(e, dataMap);//递归
        }
    }

    /**
     * 获取相应实体类
     */
    private void trans2Class(Map<String, Object> objMap, Class<?> nodeClass, Map<String, Object> dataMap) {
        //通过节点对应的策略类获取相应策略类对象
        AbstractImportStrategyTemplate abstractImportStrategyTemplate = ImportStrategyFactory.getInstance(nodeClass);
        if (abstractImportStrategyTemplate == null) {
            //可能没有实现的策略类则跳过，避免NPE
            return;
        }
        abstractImportStrategyTemplate.handler(objMap, nodeClass, dataMap);
    }

}
