package pl.hycom.training.reservation.config;

import java.util.Properties;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.support.AbstractPlatformTransactionManager;

/**
 * Spring configuration for data source
 * 
 * @author Hubert Bieńkowski (hubert.bienkowski@hycom.pl) on 27/06/2016.
 */
@Configuration
@EnableTransactionManagement
@PropertySource(value = { "classpath:db/dataSource.properties" })
public class DataSourceConfiguration {
    
    /**
     * Database use name
     */
    @Value("${database.username}")
    private String databaseUserName;

    /**
     * Database user password
     */
    @Value("${database.password}")
    private String databasePassword;

    /**
     * Database URL
     */
    @Value("${database.url}")
    private String databaseUrl;

    /**
     * Database driver name
     */
    @Value("${database.driverClass}")
    private String databaseDriverClass;

    /**
     * Database dialect
     */
    @Value("${database.hibernate.dialect}")
    private String databaseHibernateDialect;

    /**
     * SQL commands should be displayed?
     */
    @Value("${database.hibernate.show_sql}")
    private boolean databaseHibernateShowSQL;

    /**
     * HBM2DLL mode
     */
    @Value("${database.hibernate.hbm2ddl.auto}")
    private String databaseHibernateHBM2DllAuto;
    
    /**
     * Bean provides transaction manager object
     * 
     * @return transaction manager
     */
    @Bean
    public AbstractPlatformTransactionManager transactionManager() {
        return new JpaTransactionManager(entityManagerFactory());
    }

    /**
     * Bean provides entity manager factory
     * 
     * @return entity manager factory
     */
    @Bean
    public EntityManagerFactory entityManagerFactory() {

        HibernateJpaVendorAdapter hibernateJpaVendorAdapter = new HibernateJpaVendorAdapter();
        hibernateJpaVendorAdapter.setShowSql(Boolean.TRUE.equals(databaseHibernateShowSQL));
        hibernateJpaVendorAdapter.setGenerateDdl(true);
        hibernateJpaVendorAdapter.setDatabasePlatform(databaseHibernateDialect);

        final LocalContainerEntityManagerFactoryBean bean = new LocalContainerEntityManagerFactoryBean();
        bean.setDataSource(dataSource());
        bean.setPackagesToScan("pl.hycom.training.reservation");
        bean.setJpaVendorAdapter(hibernateJpaVendorAdapter);
        bean.setJpaProperties(additionalProperties());
        bean.afterPropertiesSet();

        return bean.getObject();
    }

    /**
     * Bean representing data source configuration
     * 
     * @return {@link DataSource} bean
     */
    @Bean
    public DataSource dataSource() {

        DriverManagerDataSource driverManagerDataSource = new DriverManagerDataSource();

        driverManagerDataSource.setDriverClassName(databaseDriverClass);
        driverManagerDataSource.setUrl(databaseUrl);
        driverManagerDataSource.setUsername(databaseUserName);
        driverManagerDataSource.setPassword(databasePassword);

        return driverManagerDataSource;
    }
    
    /**
     * Method responsible for preparing standard properties for database connection
     * 
     * @return database connection properties
     */
    Properties additionalProperties() {
        Properties properties = new Properties();
        properties.setProperty("hibernate.hbm2ddl.auto", databaseHibernateHBM2DllAuto);
        properties.setProperty("hibernate.dialect", databaseHibernateDialect);
        properties.setProperty("hibernate.show_sql", Boolean.toString(databaseHibernateShowSQL));
        
        return properties;
    }

    /**
     * Bean responsible for providing placeholders configuration
     * 
     * @return placeholder configurer
     */
    @Bean
    public static PropertySourcesPlaceholderConfigurer propertyConfigInDev() {
        return new PropertySourcesPlaceholderConfigurer();
    }
}
