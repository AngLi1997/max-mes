package com.bmos.mes.service.record.model;

import com.baomidou.mybatisplus.annotation.TableName;
import com.bmos.mybatis.dataobject.BaseDO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@TableName(value = "bm_batch_record_product")
public class BatchRecordProduct extends BaseDO {

    @ApiModelProperty(value = "批记录id")
    private Long recordId;

    @ApiModelProperty(value = "产品id")
    private Long productId;
}
