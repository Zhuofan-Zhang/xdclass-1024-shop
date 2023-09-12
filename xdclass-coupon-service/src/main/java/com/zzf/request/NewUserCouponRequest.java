package com.zzf.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.zzf.util.JsonData;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.Data;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@ApiModel
@Data
public class NewUserCouponRequest {


    @ApiModelProperty(value = "user_id",example = "19")
    @JsonProperty("user_id")
    private long userId;


    @ApiModelProperty(value = "name",example = "Anna")
    @JsonProperty("name")
    private String name;


}
