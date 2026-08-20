package com.bmos.mes.service.process.vo;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.util.List;

@ApiModel("工序绑定房间VO")
@Data
public class ProcedureModelStationVO {

    /**
     * 工位id
     */
    private Long id;

    /**
     * 工位code，用于唯一标识工位
     */
    private String code;
    /**
     * 工位名称，对工位的描述性文字
     */
    private String name;


}
