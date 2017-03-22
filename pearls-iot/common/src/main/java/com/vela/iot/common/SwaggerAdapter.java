package com.vela.iot.common;

import io.swagger.config.ScannerFactory;
import io.swagger.jaxrs.config.ReflectiveJaxrsScanner;
import io.swagger.models.Contact;
import io.swagger.models.ExternalDocs;
import io.swagger.models.Info;
import io.swagger.models.License;
import io.swagger.models.Swagger;
import io.swagger.models.Tag;


public class SwaggerAdapter {
	public void init(){
		ReflectiveJaxrsScanner scanner = new ReflectiveJaxrsScanner();
	    scanner.setResourcePackage("com.vela.iot.active.netty.http");
	    ScannerFactory.setScanner(scanner);
	    
	    Info info = new Info()
	      .title("Swagger iot")
	      .version("1.0.0")
	      .description("This is a Swagger iot server.")
	      .termsOfService("http://swagger.io/terms/")
	      .contact(new Contact()
	        .email("pqb@swagger.io"))
	      .license(new License()
	        .name("Apache 2.0")
	        .url("http://www.apache.org/licenses/LICENSE-2.0.html"));
	    Swagger swagger = new Swagger()
	      .info(info)
	      .host("localhost:8002")
	      .basePath("/api");
	    
	    swagger.tag(new Tag()
	      .name("active")
	      .description("Everything about your active"));
	}
}
