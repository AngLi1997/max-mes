package com.bmos.platform.service.system.code.vo.supplierno;

import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Tolerate;

import java.util.List;
import java.util.Objects;
import java.util.Set;

@Getter
@Setter
@Builder
public class CodeRuleNo {
    @Tolerate
    public CodeRuleNo() {}
    @ApiModelProperty("待确认序列号")
    private Long waitConfirmNo;
    @ApiModelProperty("待确认序列号数据")
    private Set<String> waitConfirmNos;
    @ApiModelProperty("确认序列号")
    private Long confirmNo;
    @ApiModelProperty("跳过的完整标号")
    private List<String> skipNos;

    public Long getMaxNoFromConfirmNoAndWaitConfirmNo() {
        if (Objects.isNull(waitConfirmNo)) {
            return confirmNo;
        }
        if (Objects.isNull(confirmNo)) {
            return waitConfirmNo;
        }
        return waitConfirmNo > confirmNo ? waitConfirmNo : confirmNo;
    }
}
