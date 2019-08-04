package com.github.dqqzj.seckill.api;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;
/**
 * @author qinzhongjian
 * @date created in 2019-08-04 09:58
 * @description: TODO
 * @since JDK 1.8.0_212-b10
 */
@Configuration
@EnableSwagger2
public class SwaggerConfig {
	@Bean
	public Docket userApi() {
		return new Docket(DocumentationType.SWAGGER_2).groupName("秒杀案例").apiInfo(apiInfo()).select()
				.apis(RequestHandlerSelectors.basePackage("com.github.dqqzj.seckill.controller")).paths(PathSelectors.any()).build();
	}

	/**
	 * @author qinzhongjian
	 * @date 2019-08-04
	 * @param:
	 * @return springfox.documentation.service.ApiInfo
	 * @description: 预览地址:swagger-ui.html
	 */
	private ApiInfo apiInfo() {

		return new ApiInfoBuilder().title("Spring 中使用Swagger2构建文档").termsOfServiceUrl("https://github.com/dqqzj")
				.contact(new Contact("dqqzj ", "https://github.com/dqqzj", "798078824@qq.com")).version("1.1").build();
	}
}
