package com.bmos.platform.service.unit.dto;

import com.bmos.platform.service.unit.vo.UnitExtendVO;
import com.bmos.platform.service.unit.vo.UnitVO;
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


