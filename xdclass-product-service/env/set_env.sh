#!/bin/bash
cd ..


REDIS_HOST=$REDIS_HOST
REDIS_PORT=$REDIS_REDIS_PORT
REDIS_PASSWORD=$REDIS_PASSWORD
DATABASE_DRIVER=$DATABASE_DRIVER
DATABASE_USERNAME=$DB_USERNAME
DATABASE_URL=$DB_URL
DATABASE_PASSWORD=$DB_PASSWORD


echo "spring.redis.host: $REDIS_HOST" >> xdclass-product-service/src/main/resources/application.yml
echo "spring.redis.port: $REDIS_PART" >> xdclass-product-service/src/main/resources/application.yml
echo "spring.redis.password: $REDIS_PASSWORD" >> xdclass-product-service/src/main/resources/application.yml
echo  "spring.datasource.driver-class-name: $DATABASE_DRIVER" >> xdclass-product-service/src/main/resources/application.yml
echo "spring.datasource.url: $DATABASE_URL" >> xdclass-product-service/src/main/resources/application.yml
echo  "spring.datasource.username: $DATABASE_USERNAME" >> xdclass-product-service/src/main/resources/application.yml
echo  "spring.datasource.password: $DATABASE_PASSWORD" >> xdclass-product-service/src/main/resources/application.yml
