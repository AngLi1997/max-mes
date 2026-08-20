package com.bmos.platform.service.system.dept.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.bmos.mybatis.dataobject.BaseDO;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@TableName("bp_dept")
@Getter
@Setter
@ToString
public class Dept extends BaseDO {

    @TableId(type = IdType.INPUT)
    private Long id;
    private String code;
    private String deptName;
    private Long parentId;
    private String remark;
}
