package com.bmos.platform.service.dict.model;

import com.baomidou.mybatisplus.annotation.TableName;
import com.bmos.mybatis.dataobject.BaseDO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@TableName(value = "bp_dict_dept")
public class DictDept extends BaseDO {

    @ApiModelProperty(value = "字典表id")
    private Long dictId;

    @ApiModelProperty(value = "部门id")
    private Long deptId;
}
