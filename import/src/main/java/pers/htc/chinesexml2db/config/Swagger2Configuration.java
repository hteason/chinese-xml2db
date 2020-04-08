package pers.htc.chinesexml2db.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;
/**
 * 整合swagger文档接口框架
 */
@Configuration
@EnableSwagger2
public class Swagger2Configuration {

    private final String PACKAGE = "pers.htc.chinesexml2db";
    private final String VERSION = "1.0.0";

    @Bean
    public Docket createRestApi(){
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage(PACKAGE))
                .paths(PathSelectors.any())
                .build();
    }


    private ApiInfo apiInfo(){
        return new ApiInfoBuilder()
                .title("xml格式文件导入功能")
                .description("xml格式文件导入 api 接口文档")
                .termsOfServiceUrl("https://github.com/hteason")
                .version(VERSION)
                .build();
    }
}
