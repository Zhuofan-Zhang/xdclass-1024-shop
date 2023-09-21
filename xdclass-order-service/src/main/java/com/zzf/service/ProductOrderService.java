package com.zzf.service;

import com.zzf.enums.ProductOrderPayTypeEnum;
import com.zzf.model.OrderMessage;
import com.zzf.model.ProductOrderDO;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zzf.request.ConfirmOrderRequest;
import com.zzf.request.RepayOrderRequest;
import com.zzf.util.JsonData;

import java.util.Map;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author zzf
 * @since 2023-09-16
 */
public interface ProductOrderService {
    /**
     * 创建订单
     *
     * @param orderRequest
     * @return
     */
    JsonData confirmOrder(ConfirmOrderRequest orderRequest);

    /**
     * 查询订单状态
     *
     * @param outTradeNo
     * @return
     */
    String queryProductOrderState(String outTradeNo);

    /**
     * 队列监听，定时关单
     *
     * @param orderMessage
     * @return
     */
    boolean closeProductOrder(OrderMessage orderMessage);

    /**
     * 支付结果回调通知
     *
     * @param alipay
     * @param paramsMap
     * @return
     */
    JsonData handlerOrderCallbackMsg(ProductOrderPayTypeEnum alipay, Map<String, String> paramsMap);


    /**
     * 分页查询我的订单列表
     *
     * @param page
     * @param size
     * @param state
     * @return
     */
    Map<String, Object> page(int page, int size, String state);


    /**
     * 订单二次支付
     *
     * @param repayOrderRequest
     * @return
     */
    JsonData repay(RepayOrderRequest repayOrderRequest);
}
