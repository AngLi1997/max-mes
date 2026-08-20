package com.bmos.mes.service.unit.dto;

import com.bmos.mes.service.unit.vo.UnitExtendVO;
import com.bmos.mes.service.unit.vo.UnitVO;
import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@ApiModel("单位及拓展单位DTO")
public class UnitAndExtendDTO {
    List<UnitVO> units;

    List<UnitExtendVO> unitExtends;

}
