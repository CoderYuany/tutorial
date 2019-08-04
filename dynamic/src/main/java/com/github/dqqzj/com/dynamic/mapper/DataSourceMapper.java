package com.github.dqqzj.com.dynamic.mapper;

import com.github.dqqzj.com.dynamic.po.DataSourcePo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * @author qinzhongjian
 * @date created in 2019-07-02 22:46
 * @description: TODD
 * @since JDK 1.8
 */
@Mapper
public interface DataSourceMapper {

    @Select("SELECT * FROM DataSourcePO")
    List<DataSourcePo> findAll();

    @Update("CREATE database ${db}")
    void createDatabase(@Param("db") String db);

    @Update("CREATE TABLE ${tableName} (\n" +
            "  `id` int(11) NOT NULL,\n" +
            "  `active` bigint(20) DEFAULT NULL,\n" +
            "  `createTime` datetime DEFAULT CURRENT_TIMESTAMP,\n" +
            "  `name` varchar(255) NOT NULL,\n" +
            "  `initialSize` bigint(20) DEFAULT NULL,\n" +
            "  `password` varchar(255) NOT NULL,\n" +
            "  `port` int(11) NOT NULL,\n" +
            "  `url` varchar(255) NOT NULL,\n" +
            "  `username` varchar(255) NOT NULL,\n" +
            "  `driverClassName` varchar(255) NOT NULL,\n" +
            "  `ip` varchar(255) NOT NULL,\n" +
            "  PRIMARY KEY (`id`)\n" +
            ") ENGINE=InnoDB DEFAULT CHARSET=utf8")
    void createTable(@Param("tableName") String tableName);


}
