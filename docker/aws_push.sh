#登录aws
AWS_REGION="eu-west-1"
ACCOUNT_ID=$(aws sts get-caller-identity --query Account --output text)
aws ecr get-login-password --region $AWS_REGION | docker login --username AWS --password-stdin $ACCOUNT_ID.dkr.ecr.$AWS_REGION.amazonaws.com

#构建整个项目，或者单独构建common项目,避免依赖未被构建上去
cd ../xdclass-common
mvn install


#构建网关
cd ../xdclass-gateway
mvn package docker:build
IMAGE_NAME="xdclass-gateway"
REPO_NAME="xdclass-gateway"

#aws ecr describe-repositories --repository-names xdclass-gateway --region eu-west-1 || aws ecr create-repository --repository-name $REPO_NAME --region $AWS_REGION

docker tag $IMAGE_NAME:latest $ACCOUNT_ID.dkr.ecr.$AWS_REGION.amazonaws.com/$REPO_NAME:latest
docker push $ACCOUNT_ID.dkr.ecr.$AWS_REGION.amazonaws.com/$REPO_NAME:latest
echo "网关构建推送成功"


#用户服务
cd ../xdclass-user-service
mvn package docker:build
IMAGE_NAME="xdclass-user-service"
REPO_NAME="xdclass-user-service"



docker tag $IMAGE_NAME:latest $ACCOUNT_ID.dkr.ecr.$AWS_REGION.amazonaws.com/$REPO_NAME:latest
docker push $ACCOUNT_ID.dkr.ecr.$AWS_REGION.amazonaws.com/$REPO_NAME:latest
echo "用户服务构建推送成功"


#商品服务
cd ../xdclass-product-service
mvn package docker:build
IMAGE_NAME="xdclass-product-service"
REPO_NAME="xdclass-product-service"



docker tag $IMAGE_NAME:latest $ACCOUNT_ID.dkr.ecr.$AWS_REGION.amazonaws.com/$REPO_NAME:latest
docker push $ACCOUNT_ID.dkr.ecr.$AWS_REGION.amazonaws.com/$REPO_NAME:latest
echo "商品服务构建推送成功"



#订单服务
cd ../xdclass-order-service
mvn package docker:build
IMAGE_NAME="xdclass-order-service"
REPO_NAME="xdclass-order-service"



docker tag $IMAGE_NAME:latest $ACCOUNT_ID.dkr.ecr.$AWS_REGION.amazonaws.com/$REPO_NAME:latest
docker push $ACCOUNT_ID.dkr.ecr.$AWS_REGION.amazonaws.com/$REPO_NAME:latest
echo "订单服务构建推送成功"


#优惠券服务
cd ../xdclass-coupon-service
mvn package docker:build
IMAGE_NAME="xdclass-coupon-service"
REPO_NAME="xdclass-coupon-service"



docker tag xdclass-coupon-service:latest 767722431933.dkr.ecr.eu-west-1.amazonaws.com/xdclass-coupon-service:latest
docker push 767722431933.dkr.ecr.eu-west-1.amazonaws.com/xdclass-coupon-service:latest
echo "优惠券服务构建推送成功"


echo "=======构建脚本执行完毕====="