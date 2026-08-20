package com.bmos.lims2.web.inspect.order.converter;

import com.bmos.lims2.server.inspect.order.dto.InspectionSamplingDTO;
import com.bmos.lims2.server.inspect.order.dto.InspectionSamplingSaveDTO;
import com.bmos.lims2.web.inspect.order.vo.req.InspectionSamplingSaveVO;
import com.bmos.lims2.web.inspect.order.vo.resp.InspectionSamplingRespVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * 检验取样信息Web层对象转换器
 *
 * @author yigaohui
 * @since 2025/01/27 15:30
 */
@Mapper(componentModel = "spring")
public interface InspectionSamplingWebConverter {

    InspectionSamplingWebConverter INSTANCE = Mappers.getMapper(InspectionSamplingWebConverter.class);

    /**
     * SaveVO转SaveDTO
     * @param saveVO 保存VO对象
     * @return 保存DTO对象
     */
    InspectionSamplingSaveDTO voToSaveDTO(InspectionSamplingSaveVO saveVO);

    /**
     * SaveVO列表转SaveDTO列表
     * @param saveVOList 保存VO对象列表
     * @return 保存DTO对象列表
     */
    List<InspectionSamplingSaveDTO> voListToSaveDTOList(List<InspectionSamplingSaveVO> saveVOList);

    /**
     * DTO转RespVO
     * @param dto DTO对象
     * @return 响应VO对象
     */
    InspectionSamplingRespVO dtoToRespVO(InspectionSamplingDTO dto);

    /**
     * DTO列表转RespVO列表
     * @param dtoList DTO对象列表
     * @return 响应VO对象列表
     */
    List<InspectionSamplingRespVO> dtoListToRespVOList(List<InspectionSamplingDTO> dtoList);
}