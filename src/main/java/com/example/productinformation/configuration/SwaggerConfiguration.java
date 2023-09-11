package com.example.productinformation.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.RequestHandler;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

@Configuration
public class SwaggerConfiguration {

  @Bean
  public Docket api() {
    return new Docket(DocumentationType.OAS_30)
        .useDefaultResponseMessages(true)
        .select()
        .apis(RequestHandlerSelectors.basePackage("com.example.productinformation.controller"))
        .paths(PathSelectors.any())
        .build()
        .apiInfo(apiInfo());
  }

  private ApiInfo apiInfo() {
    return new ApiInfoBuilder()
        .title("상품 등록, 조회, 수정, 삭제 API")
        .description("주어진 csv 파일로부터 추출한 데이터를 기반으로 상품을 CRUD 하는 API")
        .version("1.0.0")
        .termsOfServiceUrl("")
        .license("")
        .licenseUrl("")
        .build();
  }

}
