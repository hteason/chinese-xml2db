package pers.htc.chinesexml2db;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
//@ComponentScan({"pers.htc.chinesexml2db","constant"})
public class ChineseXml2dbApplication {
    public static void main(String[] args) {
        SpringApplication.run(ChineseXml2dbApplication.class, args);
    }
}
