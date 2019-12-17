package com.example.secureconfigclient.config.secrets;

import com.example.secureconfigclient.config.DbCredentials;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Profile({ "!awsParameterConfig & !awsSecretConfig" })
@Component
@ConfigurationProperties(
        prefix = LocalSecretsConfiguration.PREFIX
)
public class LocalSecretsConfiguration {

    static final String PREFIX = "spring.datasource";

    @Value("username")
    private transient String username;

    @Value("password")
    private transient String password;

    @Bean
    public DbCredentials dbCredentials() {
       return new DbCredentials(username, password);
    }


}
