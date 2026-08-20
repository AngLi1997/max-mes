package com.bmos.lims2.server.audit.operationlog.entity;


import com.baomidou.mybatisplus.annotation.TableName;
import com.bmos.mybatis.dataobject.BaseDO;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import lombok.experimental.Tolerate;

@TableName("bm_log_operation")
@Getter
@Setter
@ToString
@SuperBuilder(toBuilder = true)
public class AuditOperationLogEntity extends BaseDO {

    @Tolerate
    public AuditOperationLogEntity(){}

    private Long businessId;

    private String module;

    private String operationType;

    private String remark;

    private String nodeName;

    private String comment;

    /**
     * 详细信息json字符串
     */
    private String detail;
}
