package com.zzf.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

import java.math.BigDecimal;


@Builder
public class CartItemDTO {


    @JsonProperty("product_id")
    private Long productId;



    @JsonProperty("purchase_num")
    private Integer purchaseNum;

    /**
     * 商品标题
     */
    @JsonProperty("product_title")
    private String productTitle;

    /**
     * 图片
     */
    @JsonProperty("product_img")
    private String productImg;

    /**
     * 商品单价
     */
    private BigDecimal amount;

    /**
     * 总价格，单价+数量
     */
    @JsonProperty("total_amount")
    private BigDecimal totalAmount;


    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public Integer getPurchaseNum() {
        return purchaseNum;
    }

    public void setPurchaseNum(Integer purchaseNum) {
        this.purchaseNum = purchaseNum;
    }

    public String getProductTitle() {
        return productTitle;
    }

    public void setProductTitle(String productTitle) {
        this.productTitle = productTitle;
    }

    public String getProductImg() {
        return productImg;
    }

    public void setProductImg(String productImg) {
        this.productImg = productImg;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    /**
     * 商品单价 * 购买数量
     * @return
     */
    public BigDecimal getTotalAmount() {

        return this.amount.multiply(new BigDecimal(this.purchaseNum));
    }

}
