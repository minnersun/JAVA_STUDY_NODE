package com.springboot.config;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import javax.sql.DataSource;

@Configuration
@MapperScan(basePackages = "com.springboot.mapper.datasource",
        sqlSessionTemplateRef = "sqlSessionTemplatePrimary")
public class TestDataSourceConfig {

//    @Bean
//    @ConfigurationProperties(prefix = "mybatis.configuration.map-underscore-to-camel-case")
//    public org.apache.ibatis.session.Configuration configuration(){
//        return new org.apache.ibatis.session.Configuration();
//    }

    @Bean(name = "sqlSessionFactoryPrimary")
    @Primary
    public SqlSessionFactory masterSqlSessionFactory(@Qualifier("testDataSource") DataSource dataSource) throws Exception {
        SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
        bean.setDataSource(dataSource);

        //如果使用xml写SQL的话在这里配置
        bean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources("classpath:mapper/datasource/*.xml"));
        // 配置驼峰
        bean.getObject().getConfiguration().setMapUnderscoreToCamelCase(true);
        return bean.getObject();
    }

    @Bean(name = "transactionManagerPrimary")
    @Primary
    public DataSourceTransactionManager masterDataSourceTransactionManager(@Qualifier("testDataSource") DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

    @Bean(name = "sqlSessionTemplatePrimary")
    @Primary
    public SqlSessionTemplate masterSqlSessionTemplate(@Qualifier("sqlSessionFactoryPrimary") SqlSessionFactory sqlSessionFactory) {
        return new SqlSessionTemplate(sqlSessionFactory);
    }


}

