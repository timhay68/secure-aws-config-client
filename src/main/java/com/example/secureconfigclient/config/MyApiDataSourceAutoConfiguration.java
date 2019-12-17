package com.example.secureconfigclient.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.boot.orm.jpa.hibernate.SpringImplicitNamingStrategy;
import org.springframework.boot.orm.jpa.hibernate.SpringPhysicalNamingStrategy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableTransactionManagement
@EnableAutoConfiguration
@EnableJpaRepositories(
        basePackages = {"com.example.secureconfigclient.repository"},
        entityManagerFactoryRef = "entityManager",
        transactionManagerRef = "transactionManager"
)
@DependsOn(value = {"dbCredentials"})
public class MyApiDataSourceAutoConfiguration {

    private static final Logger LOG = LoggerFactory.getLogger(MyApiDataSourceAutoConfiguration.class);

    @Autowired
    private Environment environment;

    @Autowired
    private DbCredentials dbCredentials;

    @Value("${spring.datasource.jdbc-url}")
    private String jdbcUrl;

    @Bean(name = "dataSource")
    // username/password used to come from @ConfigurationProperties.
    // @ConfigurationProperties(prefix = "spring.datasource")
    public DataSource dataSource() {
        final DataSourceBuilder dataSourceBuilder = DataSourceBuilder.create();
        dataSourceBuilder.username(dbCredentials.getUsername());
        dataSourceBuilder.password(dbCredentials.getPassword());
        dataSourceBuilder.url(jdbcUrl);

        return dataSourceBuilder.build();
    }

    @Bean(name = "entityManager")
    public LocalContainerEntityManagerFactoryBean myApiEntityManager(final EntityManagerFactoryBuilder builder,
                                                                     final DataSource dataSource) {
        return builder
                .dataSource(dataSource)
                .packages("com.example.secureconfigclient.repository.model")
                .persistenceUnit("myApi")
                .properties(jpaProperties())
                .build();
    }

    @Bean(name = "transactionManager")
    public JpaTransactionManager myApiTransactionManager(@Qualifier("entityManager") final EntityManagerFactory emf) {
        return new JpaTransactionManager(emf);
    }

    private Map<String, Object> jpaProperties() {
        Map<String, Object> props = new HashMap<>();
        props.put("hibernate.physical_naming_strategy", SpringPhysicalNamingStrategy.class.getName());
        props.put("hibernate.implicit_naming_strategy", SpringImplicitNamingStrategy.class.getName());
        props.put("hibernate.show_sql", environment.getProperty("spring.jpa.show-sql"));
        props.put("hibernate.dialect", environment.getProperty("spring.jpa.hibernate.dialect"));
        props.put("hibernate.id.new_generator_mappings", "false");
        props.put("javax.persistence.validation.mode", environment.getProperty("spring.jpa.properties.validation_mode"));
        return props;
    }

}
