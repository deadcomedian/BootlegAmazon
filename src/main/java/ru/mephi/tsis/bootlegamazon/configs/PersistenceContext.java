package ru.mephi.tsis.bootlegamazon.configs;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.util.Properties;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(basePackages = "ru.mephi.tsis.bootlegamazon.dao.repositories")
@EntityScan(basePackages = "ru.mephi.tsis.bootlegamazon.dao.entities")
public class PersistenceContext {
    @Bean
    DataSource dataSource(Environment env) {
        HikariConfig dataSourceConfig = new HikariConfig();
        dataSourceConfig.setDriverClassName("org.postgresql.Driver");
        dataSourceConfig.setJdbcUrl("jdbc:postgresql://localhost:5433/tsis");
        dataSourceConfig.setUsername("tsis_user");
        dataSourceConfig.setPassword("password");
        return new HikariDataSource(dataSourceConfig);
    }

    @Bean
    LocalContainerEntityManagerFactoryBean entityManagerFactory(DataSource dataSource,
                                                                Environment env) {
        LocalContainerEntityManagerFactoryBean entityManagerFactoryBean = new LocalContainerEntityManagerFactoryBean();
        entityManagerFactoryBean.setDataSource(dataSource);
        entityManagerFactoryBean.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
        entityManagerFactoryBean.setPackagesToScan("ru.mephi.tsis.bootlegamazon");

        Properties jpaProperties = new Properties();

        jpaProperties.put("hibernate.dialect", "org.hibernate.dialect.PostgreSQL9Dialect");

        jpaProperties.put("hibernate.ddl.auto", "none");

        jpaProperties.put("hibernate.show_sql", "false");

        jpaProperties.put("hibernate.format_sql", "false");

        entityManagerFactoryBean.setJpaProperties(jpaProperties);

        return entityManagerFactoryBean;
    }

    @Bean
    JpaTransactionManager transactionManager(EntityManagerFactory entityManagerFactory) {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(entityManagerFactory);
        return transactionManager;
    }
}
