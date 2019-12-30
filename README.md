# Demonstration Client for secure-aws-config
This project demonstrates the use of the annotations implemented in the secure-aws-config module.

It shows the steps required to allow a Spring Boot application to retrieve database credentials 
from either AWS Parameter Store or AWS Secrets Manager, and then use those credentials to configure 
a datasource used by the application. In addition, we demonstrate using that same datasource to
run Liquibase update changesets on application startup. 

## Spring Profiles
### Credential Storage Profiles
To determine whether our application will use AWS Parameter Store or AWS Secrets Manager for the 
storage of the database credentials, we make use of two Spring profiles: _awsParameterConfig_ 
and _awsSecretConfig_.  One or the other must be specified - not both.
#### _awsParameterConfig_

 
#### _awsSecretConfig_


### Environment Profiles 
#### _local_

 
#### _prod_

 
## Getting Started I

### Database
This sample project has been tested using MySQL 5.7, however other relational databases 
will no doubt work just as well.

For each environment (_local_ and _prod_), create a new database named _sampledb_.
Create a new user named 'sample_user', with password 'password123'

Because the application will use this user's credentials to execute the Liquibase 
database migration scripts on startup, this user will need to have permissions to execute 
the corresponding DDL statements. 

Use the following commands to achieve this:
```sql
create database sampledb;

create user 'sample_user'@'%' identified by 'password123';

GRANT ALL ON sampledb.* TO 'sample_user'@'%';
```

### AWS Parameter Store Configuration
This step is only required when using the `@AwsParameter` annotation to retrieve our 
database credentials from the AWS Parameter Store.

Login to the AWS Console, navigate to the Parameter Store and create four new parameters 
with the following attributes:
* Path: _/sampleapi/dev/db/username_    
Value: _sample_user_ 

* Path: _/sampleapi/dev/db/password_    
Value: _password123_

* Path: _/sampleapi/prod/db/username_    
Value: _sample_user_

* Path: _/sampleapi/prod/db/password_    
Value: _password123_
 
### AWS Secrets Manager Configuration
This step is only required when using the `@AwsSecret` annotation to retrieve our 
database credentials from the AWS Secrets Manager.

Login to the AWS Console, navigate to the AWS Secrets Manager and create two new secrets: 
* sampleapi/dev
* sampleapi/prod

For each secret, add the following entries:
* Secret Key: _mysql-username_    
Secret Value: _sample_user_

* Secret Key: _mysql-password_    
Secret Value: _password123_

### Local Development
#### Application Configuration
In application.yml, `spring.profiles.active` contains the list of Spring profiles which will 
be used to select the configurations to bei used at runtime. Ensure this list includes 'local'
and not 'prod'.

The following configurations will then be found in application-local.yml.  

Configure `spring.datasource.jdbc-url` as required, depending on the location, type and name 
of your database.

Set `secure-aws-config.awsProfile` to the name of your AWS profile used for local development.
This profile must represent credentials which will give the application permissions to read 
from the AWS Parameter Store or AWS Secrets Manager, as required.

Set `secure-aws-config.parameters.region` or `secure-aws-config.secrets.region` to the region in 
which your account created the corresponding parameters/secrets.

#### AWS IAM Configuration
TODO

### AWS Elastic Beanstalk Deployment
#### Application Configuration
In application.yml, `spring.profiles.active` contains the list of Spring profiles which will 
be used to select the configurations to be used at runtime. Ensure this list includes 'prod'
and not 'local'.

The following configurations will then be found in application-prod.yml.  

Configure `spring.datasource.jdbc-url` as required, depending on the location, type and name 
of your database.

Set `secure-aws-config.parameters.region` or `secure-aws-config.secrets.region` to the region in 
which your account created the corresponding parameters/secrets.

#### AWS IAM Configuration
TODO
