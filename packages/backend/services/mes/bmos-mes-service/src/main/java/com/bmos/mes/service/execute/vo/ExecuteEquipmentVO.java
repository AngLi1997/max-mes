package com.bmos.mes.service.execute.vo;

import com.bmos.platform.facade.equipment.enums.AcquisitionPlatformEnum;
import com.bmos.platform.facade.equipment.vo.EquipmentPropertyAcquisitionPointFeignVO;
import com.bmos.platform.facade.equipment.vo.EquipmentPropertyFeignVO;
import com.bmos.platform.facade.equipment.vo.EquipmentStatusFeignVO;
import com.bmos.platform.facade.equipment.vo.TagFeignVO;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class ExecuteEquipmentVO {

    private Long id;
    private String code;
    private String name;
    @JsonFormat(
            pattern = "yyyy-MM-dd HH:mm:ss"
    )
    private LocalDate expireDate;
    private Integer status;
    private List<String> tagNames;
    private List<TagFeignVO> equipmentTagDataList;
    private List<Long> stationIdList;
    private List<EquipmentStatusFeignVO> statusPropertyList;
    private List<EquipmentPropertyFeignVO> infoPropertyList;
    private List<EquipmentPropertyAcquisitionPointFeignVO> dataPropertyList;
    @ApiModelProperty("数采平台")
    private AcquisitionPlatformEnum acquisitionPlatform;

}
