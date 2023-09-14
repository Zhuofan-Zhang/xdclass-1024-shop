package com.zzf.service;

import com.zzf.dto.ProductDTO;
import com.zzf.model.ProductDO;

import java.util.List;
import java.util.Map;


public interface ProductService {

    Map<String, Object> page(int page, int size);

    ProductDTO findDetailById(long id);

    List<ProductDTO> findProductsByIdBatch(List<Long> productIdList);
}
