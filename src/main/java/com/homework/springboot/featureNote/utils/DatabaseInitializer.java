//package com.homework.springboot.featureNote.utils;
//
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.boot.jdbc.DataSourceBuilder;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.context.annotation.DependsOn;
//import org.springframework.context.annotation.Profile;
//import org.springframework.core.io.ClassPathResource;
//import org.springframework.jdbc.datasource.init.DataSourceInitializer;
//import org.springframework.jdbc.datasource.init.DatabasePopulator;
//import org.springframework.jdbc.datasource.init.DatabasePopulatorUtils;
//import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
//
//import javax.sql.DataSource;
//
//@Configuration
//@Profile("!test") // Exclude from tests
//public class DatabaseInitializer {
//
//    @Value("${spring.datasource.url}")
//    private String dataSourceUrl;
//
//    @Value("${spring.datasource.username}")
//    private String dataSourceUsername;
//
//    @Value("${spring.datasource.password}")
//    private String dataSourcePassword;
//
//    @Bean
//    @DependsOn("entityManagerFactory")
//    public DataSourceInitializer dataSourceInitializer() {
//        DataSource dataSource = DataSourceBuilder
//                .create()
//                .url(dataSourceUrl)
//                .username(dataSourceUsername)
//                .password(dataSourcePassword)
//                .build();
//
//        ResourceDatabasePopulator databasePopulator = new ResourceDatabasePopulator();
//        databasePopulator.addScript(new ClassPathResource("create-database.sql")); // Add your SQL script to create the database
//
//        DataSourceInitializer initializer = new DataSourceInitializer();
//        initializer.setDataSource(dataSource);
//        initializer.setDatabasePopulator(databasePopulator);
//        return initializer;
//    }
//}
