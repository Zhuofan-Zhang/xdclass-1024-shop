package com.zzf.service;

import com.zzf.model.ProductMessage;
import com.zzf.request.LockProductRequest;
import com.zzf.util.JsonData;
import com.zzf.vo.ProductVO;

import java.util.List;
import java.util.Map;

public interface ProductService {

    /**
     * 分页查询商品列表
     * @param page
     * @param size
     * @return
     */
    Map<String,Object> page(int page, int size);

    /**
     * 根据id找商品详情
     * @param productId
     * @return
     */
    ProductVO findDetailById(long productId);

    /**
     * 根据id批量查询商品
     * @param productIdList
     * @return
     */
    List<ProductVO> findProductsByIdBatch(List<Long> productIdList);

    /**
     * 锁定商品库存
     * @param lockProductRequest
     * @return
     */
    JsonData lockProductStock(LockProductRequest lockProductRequest);

    /**
     * 释放商品库存
     * @param productMessage
     * @return
     */
    boolean releaseProductStock(ProductMessage productMessage);
}
