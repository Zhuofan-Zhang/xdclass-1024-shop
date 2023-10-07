#登录阿里云镜像仓
docker login --username=$ALICLOUD_USERNAME registry.cn-huhehaote.aliyuncs.com --password=$ALICLOUD_PASSWORD

#构建整个项目，或者单独构建common项目,避免依赖未被构建上去
cd ../xdclass-common
mvn install


#构建网关
cd ../xdclass-gateway
mvn package docker:build
docker tag xdclass-gateway:latest registry.cn-huhehaote.aliyuncs.com/zzf-cloud/xdclass-gateway:latest
docker push registry.cn-huhehaote.aliyuncs.com/zzf-cloud/xdclass-gateway:latest
echo "网关构建推送成功"


#用户服务
cd ../xdclass-user-service
mvn package docker:build
docker tag xdclass-user-service:latest registry.cn-huhehaote.aliyuncs.com/zzf-cloud/xdclass-user-service:latest
docker push registry.cn-huhehaote.aliyuncs.com/zzf-cloud/xdclass-user-service:latest
echo "用户服务构建推送成功"


#商品服务
cd ../xdclass-product-service
mvn package docker:build
docker tag xdclass-product-service:latest registry.cn-huhehaote.aliyuncs.com/zzf-cloud/xdclass-product-service:latest
docker push registry.cn-huhehaote.aliyuncs.com/zzf-cloud/xdclass-product-service:latest
echo "商品服务构建推送成功"



#订单服务
cd ../xdclass-order-service
mvn package docker:build
docker tag xdclass-order-service:latest registry.cn-huhehaote.aliyuncs.com/zzf-cloud/xdclass-order-service:latest
docker push registry.cn-huhehaote.aliyuncs.com/zzf-cloud/xdclass-order-service:latest
echo "订单服务构建推送成功"


#优惠券服务
cd ../xdclass-coupon-service
mvn package docker:build
docker tag xdclass-coupon-service:latest registry.cn-huhehaote.aliyuncs.com/zzf-cloud/xdclass-coupon-service:latest
docker push registry.cn-huhehaote.aliyuncs.com/zzf-cloud/xdclass-coupon-service:latest
echo "优惠券服务构建推送成功"


echo "=======构建脚本执行完毕====="