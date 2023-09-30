package com.zzf.service;

import com.zzf.dto.ProductDTO;
import com.zzf.model.ProductDO;
import com.zzf.model.ProductMessage;
import com.zzf.request.LockProductRequest;
import com.zzf.util.JsonData;

import java.util.List;
import java.util.Map;


public interface ProductService {

    Map<String, Object> page(int page, int size);

    ProductDTO findDetailById(long id);

    List<ProductDTO> findProductsByIdBatch(List<Long> productIdList);

    JsonData lockProductStock(LockProductRequest lockProductRequest);

    boolean releaseProductStock(ProductMessage productMessage);
}
