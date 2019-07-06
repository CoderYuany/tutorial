package com.github.dqqzj.com.dynamic.service;

import com.github.dqqzj.com.dynamic.core.DynamicDataSourceContextHolder;
import com.github.dqqzj.com.dynamic.mapper.DataSourceMapper;
import com.github.dqqzj.com.dynamic.po.DataSourcePO;
import com.github.dqqzj.com.dynamic.utils.DynamicRoutingUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

/**
 * @author qinzhongjian
 * @date created in 2019-07-04 11:46
 * @description: TODD
 * @since JDK 1.8
 */
@Service
public class DataSourceServiceImpl implements DataSourceService {

    @Autowired
    DataSourceMapper dataSourceMapper;

    /**
     * @author qinzhongjian
     * @date 2019-07-04
     * @param: dataSourcePO
     * @return void
     * @description: 利用mysql的特殊表mysql进行链接，并创建其他的数据库
     */
    @Override
    public void createDatabase(DataSourcePO dataSourcePO) {
        String dbName = dataSourcePO.getName();
        String url = dataSourcePO.getUrl();
        int ipLen = dataSourcePO.getIp().length();
        int portLen = String.valueOf(dataSourcePO.getPort()).length();
        int toffset = ipLen+portLen+15;
        boolean isMysqlDatabase = url.regionMatches(toffset, "mysql", 0, 4);
        Assert.isTrue(isMysqlDatabase, "this database must be mysql and that can be caused java.sql.SQLException by connecting to database");
        DynamicRoutingUtils.addDynamicRoutingDatasource(dataSourcePO);
        DynamicDataSourceContextHolder.set(dbName);
        this.dataSourceMapper.createDatabase(dbName);
        //创建成功过后替换调缓存的datasource中的url
        StringBuilder stringBuilder = new StringBuilder(url);
        stringBuilder.replace(toffset, toffset+5, dbName);
        dataSourcePO.setUrl(stringBuilder.toString());
        DynamicRoutingUtils.addDynamicRoutingDatasource(dataSourcePO);

    }

}
