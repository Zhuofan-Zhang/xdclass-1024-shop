package com.zzf.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel
@Data
public class CartItemRequest {

    @ApiModelProperty(value = "product_id",example = "11")
    @JsonProperty("product_id")
    private long productId;

    @JsonProperty("purchase_num")
    private int purchaseNum;
}
