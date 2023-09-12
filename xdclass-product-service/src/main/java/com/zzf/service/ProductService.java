package com.zzf.service;

import com.zzf.model.ProductDO;

import java.util.Map;


public interface ProductService {

    Map<String, Object> page(int page, int size);
}
