package com.github.dqqzj.springboot.selector;

import com.github.dqqzj.springboot.selector.EnableServer;
import com.github.dqqzj.springboot.selector.FtpServer;
import com.github.dqqzj.springboot.selector.HttpServer;
import com.github.dqqzj.springboot.selector.Server;
import org.springframework.context.annotation.ImportSelector;
import org.springframework.core.type.AnnotationMetadata;

import java.util.Map;

/**
 * @author qinzhongjian
 * @date created in 2019-07-28 20:37
 * @description: TODO
 * @since JDK 1.8.0_212-b10
 */
public class ServerImportSelector implements ImportSelector {
    @Override
    public String[] selectImports(AnnotationMetadata annotationMetadata) {
        Map<String, Object> attributesMap = annotationMetadata.getAnnotationAttributes(EnableServer.class.getName());
        Server.Type type = (Server.Type) attributesMap.get("type");
        String []importClasses = new String[0];
        switch (type) {
            case FTP:
                importClasses = new String[] {FtpServer.class.getName()};
                break;
            case HTTP:
                importClasses = new String[] {HttpServer.class.getName()};
                break;
        }
        return importClasses;
    }
}
