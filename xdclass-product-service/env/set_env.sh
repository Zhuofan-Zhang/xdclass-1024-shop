#!/bin/bash

pwd

# Read the value of an environment variable
#redis_host=$REDIS_HOST
#redis_port=$REDIS_PORT
#redis_password=$REDIS_PASSWORD
#
## Use the value in your script
#echo "The value of VARIABLE_NAME is: $my_variable"


echo "spring.redis.host: $redis_host" >> xdclass-product-service/src/main/resources/application.yml
#echo "\nspring.redis.port: $redis_port" >> ./src/main/resources/application.yml
#echo "\nspring.redis.password: $redis_password" >> ./src/main/resources/application.yml
#echo  "\nspring.datasource.driver-class-name: ${ env.DATABASE_DRIVER }" >> ./src/main/resources/application.yml
#echo "\nspring.datasource.url: ${ secrets.SQL_URL }" >> ./src/main/resources/application.yml
#echo  "\nspring.datasource.username: ${ secrets.DB_USERNAME }" >> ./src/main/resources/application.yml
#echo  "\nspring.datasource.password: ${ secrets.REDIS_PASSWORD }" >> ./src/main/resources/application.yml
