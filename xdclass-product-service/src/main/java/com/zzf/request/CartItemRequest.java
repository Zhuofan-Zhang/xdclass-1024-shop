package com.zzf.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 小滴课堂,愿景：让技术不再难学
 *
 *
 *
 * @Description
 * @Author 二当家小D
 * @Remark 有问题直接联系我，源码-笔记-技术交流群
 * @Version 1.0
 **/

@ApiModel
@Data
public class CartItemRequest {

    @ApiModelProperty(value = "商品id",example = "11")
    @JsonProperty("product_id")
    private long productId;

    @ApiModelProperty(value = "购买数量",example = "1")
    @JsonProperty("buy_num")
    private int buyNum;
}
