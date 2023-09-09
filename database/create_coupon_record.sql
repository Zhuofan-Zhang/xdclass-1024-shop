CREATE TABLE `coupon_record` (
                                 `id` bigint(11) unsigned NOT NULL AUTO_INCREMENT,
                                 `coupon_id` bigint(11) DEFAULT NULL,
                                 `created_at` datetime DEFAULT NULL,
                                 `usage_state` varchar(32) DEFAULT NULL COMMENT 'NEW,USED,EXPIRED',
                                 `user_id` bigint(11) DEFAULT NULL,
                                 `user_name` varchar(128) DEFAULT NULL,
                                 `coupon_title` varchar(128) DEFAULT NULL,
                                 `valid_from` datetime DEFAULT NULL,
                                 `valid_until` datetime DEFAULT NULL,
                                 `order_id` bigint(11) DEFAULT NULL,
                                 `price_deducted` decimal(16,2) DEFAULT NULL,
                                 `condition_price` decimal(16,2) DEFAULT NULL COMMENT 'can only be used after spent certain amount',
                                 PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=141 DEFAULT CHARSET=utf8mb4;