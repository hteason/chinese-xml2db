package pers.htc.chinesexml2db.api;

import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import parser.XMLParser;

import java.util.Map;

@Api("导入xml格式文件")
@RestController
public class ImportXmlApi {

    @PostMapping("/upload/file")
    public Object upload(@RequestParam("file") MultipartFile file) throws Exception {
        //解析、封装到实体类、插入数据库
        Map<String, Object> ret = XMLParser.toBeanMap(file.getInputStream());
        ret.remove("CURRENT_NODE");
        return ret;
    }
}
