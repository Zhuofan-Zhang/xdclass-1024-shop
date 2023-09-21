CREATE TABLE `product_order` (
                                 `id` bigint(11) NOT NULL AUTO_INCREMENT,
                                 `out_trade_no` varchar(64) DEFAULT NULL COMMENT '订单唯一标识',
                                 `state` varchar(11) DEFAULT NULL COMMENT 'NEW 未支付订单,PAY已经支付订单,CANCEL超时取消订单',
                                 `create_time` datetime DEFAULT NULL COMMENT '订单生成时间',
                                 `total_amount` decimal(16,2) DEFAULT NULL COMMENT '订单总金额',
                                 `pay_amount` decimal(16,2) DEFAULT NULL COMMENT '订单实际支付价格',
                                 `pay_type` varchar(64) DEFAULT NULL COMMENT '支付类型，微信-银行-支付宝',
                                 `nickname` varchar(64) DEFAULT NULL COMMENT '昵称',
                                 `head_img` varchar(524) DEFAULT NULL COMMENT '头像',
                                 `user_id` int(11) DEFAULT NULL COMMENT '用户id',
                                 `del` int(5) DEFAULT '0' COMMENT '0表示未删除，1表示已经删除',
                                 `update_time` datetime DEFAULT NULL COMMENT '更新时间',
                                 `order_type` varchar(32) DEFAULT NULL COMMENT '订单类型 DAILY普通单，PROMOTION促销订单',
                                 `receiver_address` varchar(1024) DEFAULT NULL COMMENT '收货地址 json存储',
                                 PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2439 DEFAULT CHARSET=utf8mb4;