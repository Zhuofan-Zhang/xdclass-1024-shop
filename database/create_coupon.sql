CREATE TABLE `coupon` (
                          `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
                          `category` varchar(11) DEFAULT NULL COMMENT 'coupon category[NEW_USER，TASK，PROMOTION]',
                          `publish` varchar(11) DEFAULT NULL COMMENT 'PUBLISH，DRAFT，OFFLINE',
                          `coupon_img` varchar(524) DEFAULT NULL ,
                          `coupon_title` varchar(128) DEFAULT NULL ,
                          `price_deducted` decimal(16,2) DEFAULT NULL,
                          `user_limit` int(11) DEFAULT NULL,
                          `valid_from` datetime DEFAULT NULL,
                          `valid_until` datetime DEFAULT NULL,
                          `inventory` int(11) DEFAULT NULL,
                          `remaining` int(11) DEFAULT '0',
                          `created_at` datetime DEFAULT NULL,
                          `condition_price` decimal(16,2) DEFAULT NULL COMMENT 'can only be used after spent certain amount',
                          PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=23 DEFAULT CHARSET=utf8mb4;

INSERT INTO `coupon` (`id`, `category`, `publish`, `coupon_img`, `coupon_title`, `price_deducted`, `user_limit`, `valid_from`, `valid_until`, `inventory`, `remaining`, `created_at`, `condition_price`)
VALUES
    (18, 'NEW_USER', 'PUBLISH', 'https://file.xdclass.net/video/2020/alibabacloud/zt-alibabacloud.png', '永久有效-新人注册-0元满减-5元抵扣劵-限领取2张-不可叠加使用', 5.00, 2, '2000-01-01 00:00:00', '2099-01-29 00:00:00', 100000000, 99999991, '2020-12-26 16:33:02', 0.00),
    (19, 'PROMOTION', 'PUBLISH', 'https://file.xdclass.net/video/2020/alibabacloud/zt-alibabacloud.png', '有效中-21年1月到25年1月-20元满减-5元抵扣劵-限领取2张-不可叠加使用', 5.00, 2, '2000-01-29 00:00:00', '2025-01-29 00:00:00', 10, 3,  '2020-12-26 16:33:03', 20.00),
    (22, 'PROMOTION', 'PUBLISH', 'https://file.xdclass.net/video/2020/alibabacloud/zt-alibabacloud.png', '过期-20年8月到20年9月-商品id3-6元抵扣劵-限领取1张-可叠加使用', 6.00, 1, '2020-08-01 00:00:00', '2020-09-29 00:00:00', 100, 100, '2020-12-26 16:33:03', 0.00),
    (20, 'PROMOTION', 'PUBLISH', 'https://file.xdclass.net/video/2020/alibabacloud/zt-alibabacloud.png', '有效中-20年8月到21年9月-商品id1-8.8元抵扣劵-限领取2张-不可叠加使用', 8.80, 2, '2020-08-01 00:00:00', '2021-09-29 00:00:00', 100, 96, '2020-12-26 16:33:03', 0.00),
    (21, 'PROMOTION', 'PUBLISH', 'https://file.xdclass.net/video/2020/alibabacloud/zt-alibabacloud.png', '有效中-20年8月到21年9月-商品id2-9.9元抵扣劵-限领取2张-可叠加使用', 8.80, 2, '2020-08-01 00:00:00', '2021-09-29 00:00:00', 100, 96, '2020-12-26 16:33:03', 0.00);