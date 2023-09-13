CREATE TABLE `product` (
                           `id` bigint(11) unsigned NOT NULL AUTO_INCREMENT,
                           `title` varchar(128) DEFAULT NULL,
                           `cover_img` varchar(128) DEFAULT NULL,
                           `detail` varchar(256) DEFAULT '',
                           `old_amount` decimal(16,2) DEFAULT NULL,
                           `price` decimal(16,2) DEFAULT NULL,
                           `inventory` int(11) DEFAULT NULL,
                           `created_at` datetime DEFAULT NULL,
                           `locked_inventory` int(11) DEFAULT '0',
                           PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4;


INSERT INTO `product` (`id`, `title`, `cover_img`, `detail`, `old_amount`, `price`, `inventory`, `created_at`, `locked_inventory`)
VALUES
    (1, '小滴课堂抱枕', 'https://file.xdclass.net/video/2020/alibabacloud/zt-alibabacloud.png', 'https://file.xdclass.net/video/2021/60-MLS/summary.jpeg', 32.00, 213.00, 100, '2021-09-12 00:00:00', 31),
    (2, '技术人的杯子Linux', 'https://file.xdclass.net/video/2020/alibabacloud/zt-alibabacloud.png', 'https://file.xdclass.net/video/2021/59-Postman/summary.jpeg', 432.00, 42.00, 20, '2021-03-12 00:00:00', 2),
    (3, '技术人的杯子docker', 'https://file.xdclass.net/video/2020/alibabacloud/zt-alibabacloud.png', 'https://file.xdclass.net/video/2021/60-MLS/summary.jpeg', 35.00, 12.00, 20, '2022-09-22 00:00:00', 13),
    (4, '技术人的杯子git', 'https://file.xdclass.net/video/2020/alibabacloud/zt-alibabacloud.png', 'https://file.xdclass.net/video/2021/60-MLS/summary.jpeg', 12.00, 14.00, 20, '2022-11-12 00:00:00', 2);