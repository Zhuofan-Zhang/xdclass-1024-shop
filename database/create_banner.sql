CREATE TABLE `banner` (
                          `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
                          `banner_img` varchar(524) DEFAULT NULL,
                          `redirect_url` varchar(524) DEFAULT NULL,
                          `weight` int(11) DEFAULT NULL,
                          PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4;

INSERT INTO `banner` (`id`, `banner_img`, `redirect_url`, `weight`)
VALUES
    (1, 'https://file.xdclass.net/video/2020/alibabacloud/zx-lbt.jpeg', 'https://m.xdclass.net/#/member', 1),
    (2, 'https://file.xdclass.net/video/%E5%AE%98%E7%BD%91%E8%BD%AE%E6%92%AD%E5%9B%BE/20%E5%B9%B4%E5%8F%8C11%E9%98%BF%E9%87%8C%E4%BA%91/fc-lbt.jpeg', 'https://www.aliyun.com/1111/pintuan-share?ptCode=MTcwMTY3MzEyMjc5MDU2MHx8MTE0fDE%3D&userCode=r5saexap', 3),
    (3, 'https://file.xdclass.net/video/%E5%AE%98%E7%BD%91%E8%BD%AE%E6%92%AD%E5%9B%BE/20%E5%B9%B4%E5%8F%8C11%E9%98%BF%E9%87%8C%E4%BA%91/FAN-lbu-vip.jpeg', 'https://file.xdclass.net/video/%E5%AE%98%E7%BD%91%E8%BD%AE%E6%92%AD%E5%9B%BE/Nginx.jpeg', 2);