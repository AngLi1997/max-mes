package com.bmos.wms.service.inspect.convert;

import com.bmos.wms.service.inspect.controller.vo.InspectDetailVO;
import com.bmos.wms.service.inspect.controller.vo.InspectInfoVO;
import com.bmos.wms.service.inspect.controller.vo.InspectPageVO;
import com.bmos.wms.service.inspect.controller.vo.InspectProgramResultVO;
import com.bmos.wms.service.inspect.model.Inspect;
import com.bmos.wms.service.inspect.model.InspectInfo;
import com.bmos.wms.service.inspect.model.InspectResult;
import com.bmos.wms.service.inspect.service.dto.InitiateInspectInfoDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * WMS 检验单领域对象转换。
 */
@Mapper
public interface InspectConvert {

    InspectConvert INSTANCE = Mappers.getMapper(InspectConvert.class);

    // ---- entity → vo ----

    InspectPageVO toPageVO(Inspect inspect);

    List<InspectPageVO> toPageVO(List<Inspect> inspects);

    @Mapping(target = "inspectInfoVOList", ignore = true)
    @Mapping(target = "inspectProgramResultVOList", ignore = true)
    InspectDetailVO toDetailVO(Inspect inspect);

    InspectInfoVO toInfoVO(InspectInfo info);

    List<InspectInfoVO> toInfoVOList(List<InspectInfo> list);

    @Mapping(target = "inspectConclusion",
            expression = "java(result.getInspectConclusion() == null ? null : "
                    + "com.bmos.wms.common.enums.inspect.InspectProgramResultEnum.valueOf(result.getInspectConclusion()))")
    InspectProgramResultVO toProgramVO(InspectResult result);

    List<InspectProgramResultVO> toProgramVOList(List<InspectResult> list);

    // ---- dto → entity ----

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "inspectId", ignore = true)
    InspectInfo toInspectInfo(InitiateInspectInfoDTO dto);

    List<InspectInfo> toInspectInfoList(List<InitiateInspectInfoDTO> list);
}
