package com.zzf.config;

import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import org.springframework.beans.factory.annotation.Value;


public class AlipayConfig {

    /**
     * 支付宝网关地址  TODO
     */
    @Value("${alipay_gateway}")
    public static String PAY_GATEWAY;


    /**
     * 支付宝 APPID TODO
     */
    @Value("${alipay_appid}")
    public static String APPID;

    /**
     * 应用私钥 TODO
     */
    @Value("${alipay_pri_key}")
    public static String APP_PRI_KEY;

    /**
     * 支付宝公钥 TODO
     */
    @Value("${alipay_pub_key}")
    public static String ALIPAY_PUB_KEY;


    /**
     * 签名类型
     */
    public static String SIGN_TYPE = "RSA2";


    /**
     * 字符编码
     */
    public static String CHARSET = "UTF-8";


    /**
     * 返回参数格式
     */
    public static String FORMAT = "json";


    /**
     * 构造函数私有化
     */
    private AlipayConfig() {

    }


    private volatile static AlipayClient instance = null;


    /**
     * 单例模式获取, 双重锁校验
     *
     * @return
     */
    public static AlipayClient getInstance() {

        if (instance == null) {
            synchronized (AlipayConfig.class) {
                if (instance == null) {
                    instance = new DefaultAlipayClient(PAY_GATEWAY, APPID, APP_PRI_KEY, FORMAT, CHARSET, ALIPAY_PUB_KEY, SIGN_TYPE);
                }
            }
        }
        return instance;
    }


}
