package com.zzf.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 
 * </p>
 *
 * @author 二当家小D
 * @since 2021-02-19
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class BannerVO  {


    private Integer id;

    /**
     * 图片
     */
    private String img;

    /**
     * 跳转地址
     */
    private String url;

    /**
     * 权重
     */
    private Integer weight;


}
