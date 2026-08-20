package com.bmos.mes.service.execute.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
@ApiModel("换班复制记录VO")
public class ChangeTeamRecordCopyChangeTeamVO {

    @ApiModelProperty("工艺换班次数,从0开始")
    private int processChangeNumber;

    @ApiModelProperty("工序换班列表")
    private List<ProcedureChangeVO> procedureChangeList;

    @Data
    @ApiModel("工序班次信息")
    public static class ProcedureChangeVO {

        @ApiModelProperty("工序换班次数,从0开始")
        private int procedureChangeNumber;

        @ApiModelProperty("拷贝版本列表,数值与index无关")
        private List<Long> copyVersionList;

    }


}
