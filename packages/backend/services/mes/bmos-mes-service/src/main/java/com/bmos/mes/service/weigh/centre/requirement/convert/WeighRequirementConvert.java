package com.bmos.mes.service.weigh.centre.requirement.convert;

import com.bmos.mes.service.weigh.centre.execute.vo.WeighExecutePendingRequirementSimpleVO;
import com.bmos.mes.service.weigh.centre.requirement.model.WeighRequirement;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * @author liang
 * @version 1.0.0
 * @date 2024/7/10 17:55
 */
@Mapper
public interface WeighRequirementConvert {

    WeighRequirementConvert INSTANCE = Mappers.getMapper(WeighRequirementConvert.class);

    WeighExecutePendingRequirementSimpleVO convertToRequirementSimpleVO(WeighRequirement weighRequirement);

    List<WeighExecutePendingRequirementSimpleVO> convertToRequirementSimpleVO(List<WeighRequirement> list);
}
