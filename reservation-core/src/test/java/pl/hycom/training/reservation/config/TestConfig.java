package pl.hycom.training.reservation.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.support.AbstractPlatformTransactionManager;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.util.Properties;

/**
 * Spring configuration for database
 * 
 * @author Hubert Bieńkowski (hubert.bienkowski@hycom.pl) on 28/06/2016.
 */
@Configuration
@ComponentScan(basePackages = { "pl.hycom.training.reservation" })
public class TestConfig {

    @Bean
    public AbstractPlatformTransactionManager transactionManager() {
        return new JpaTransactionManager(entityManagerFactory());
    }

    @Bean
    public EntityManagerFactory entityManagerFactory() {

        HibernateJpaVendorAdapter hibernateJpaVendorAdapter = new HibernateJpaVendorAdapter();
        hibernateJpaVendorAdapter.setShowSql(true);
        hibernateJpaVendorAdapter.setGenerateDdl(true);
        hibernateJpaVendorAdapter.setDatabasePlatform("org.hibernate.dialect.HSQLDialect");

        final LocalContainerEntityManagerFactoryBean bean = new LocalContainerEntityManagerFactoryBean();
        bean.setDataSource(dataSource());
        bean.setPackagesToScan("pl.hycom.training.reservation");
        bean.setJpaVendorAdapter(hibernateJpaVendorAdapter);
        bean.setJpaProperties(additionalProperties());
        bean.afterPropertiesSet();

        return bean.getObject();
    }

    @Bean
    public DataSource dataSource() {

        DriverManagerDataSource driverManagerDataSource = new DriverManagerDataSource();

        driverManagerDataSource.setDriverClassName("org.hsqldb.jdbcDriver");
        driverManagerDataSource.setUrl("jdbc:hsqldb:mem:.");
        driverManagerDataSource.setUsername("sa");
        driverManagerDataSource.setPassword("");

        return driverManagerDataSource;
    }

    Properties additionalProperties() {
        Properties properties = new Properties();
        properties.setProperty("hibernate.hbm2ddl.auto", "create-drop");
        properties.setProperty("hibernate.dialect", "org.hibernate.dialect.HSQLDialect");
        properties.setProperty("hibernate.show_sql", "true");

        return properties;
    }

}
