cd ..
echo "\nspring.redis.host: ${ secrets.REDIS_HOST }" >> ./src/main/resources/application.yml
echo "\nspring.redis.port: ${ secrets.REDIS_PORT }" >> ./src/main/resources/application.yml
echo "\nspring.redis.password: ${ secrets.REDIS_PASSWORD }" >> ./src/main/resources/application.yml
echo  "\nspring.datasource.driver-class-name: ${ env.DATABASE_DRIVER }" >> ./src/main/resources/application.yml
echo "\nspring.datasource.url: ${ secrets.SQL_URL }" >> ./src/main/resources/application.yml
echo  "\nspring.datasource.username: ${ secrets.DB_USERNAME }" >> ./src/main/resources/application.yml
echo  "\nspring.datasource.password: ${ secrets.REDIS_PASSWORD }" >> ./src/main/resources/application.yml
