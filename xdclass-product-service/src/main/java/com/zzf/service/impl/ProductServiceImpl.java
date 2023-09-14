package com.zzf.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zzf.dto.ProductDTO;
import com.zzf.model.ProductDO;
import com.zzf.mapper.ProductMapper;
import com.zzf.service.ProductService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author zzf
 * @since 2023-09-12
 */
@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductMapper productMapper;

    @Override
    public Map<String, Object> page(int page, int size) {

        Page<ProductDO> pageInfo = new Page<>(page, size);
        IPage<ProductDO> productDOIPage = productMapper.selectPage(pageInfo, null);
        Map<String, Object> productResultMap = new HashMap<>(3);
        productResultMap.put("total_record", productDOIPage.getTotal());
        productResultMap.put("total_page", productDOIPage.getPages());
        productResultMap.put("current_data", productDOIPage.getRecords().stream().map(this::mapProductDTO).collect(Collectors.toList()));

        return productResultMap;
    }

    @Override
    public ProductDTO findDetailById(long id) {
        ProductDO productDO = productMapper.selectById(id);

        return mapProductDTO(productDO);
    }

    @Override
    public List<ProductDTO> findProductsByIdBatch(List<Long> productIdList) {
        List<ProductDO> products = productMapper.selectList(new QueryWrapper<ProductDO>().in("id", productIdList));
        List<ProductDTO> productDTOS = products.stream().map(this::mapProductDTO).collect(Collectors.toList());
        return productDTOS;
    }

    private ProductDTO mapProductDTO(ProductDO productDO) {
        ProductDTO productDTO = new ProductDTO();
        BeanUtils.copyProperties(productDO, productDTO);
        productDTO.setStock(productDO.getInventory() - productDO.getLockedInventory());
        return productDTO;
    }
}
