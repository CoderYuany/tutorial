package com.github.dqqzj.springboot.filter;

import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.core.type.filter.TypeFilter;

import java.io.IOException;

/**
 * @author qinzhongjian
 * @date created in 2019-07-30 19:13
 * @description: TODO
 * @since JDK 1.8.0_212-b10
 */
public class MyTypeFilter implements TypeFilter {
    @Override
    public boolean match(MetadataReader metadataReader, MetadataReaderFactory metadataReaderFactory) throws IOException {
        if (metadataReader.getAnnotationMetadata().isAnnotated(Dqqzj.class.getName())) {
            return true;
        }
        return false;
    }
}
