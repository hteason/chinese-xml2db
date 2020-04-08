import org.junit.jupiter.api.Test;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Map;

public class TestMeYaml {
    @Test
    public void testParseMeYaml() throws FileNotFoundException {
        Yaml yaml = new Yaml();
        Me me = yaml.loadAs(new FileInputStream(new File("D:\\code\\idea project\\chinese-xml2db\\import\\src\\test\\java\\me.yml")), Me.class);
        System.out.println(me);
    }
    @Test
    public void testParseMeYaml2() throws FileNotFoundException {
        Yaml yaml = new Yaml();
        FileInputStream in = new FileInputStream(new File("D:\\code\\idea project\\chinese-xml2db\\import\\src\\test\\java\\import-maps.yml"));

        Map<String, Object> map = yaml.load(in);
        for (String str : map.keySet()) {
            System.out.println(str+" "+map.get(str));
        }

        GlobalMap me = yaml.loadAs(in, GlobalMap.class);
        System.out.println(me);
    }
}