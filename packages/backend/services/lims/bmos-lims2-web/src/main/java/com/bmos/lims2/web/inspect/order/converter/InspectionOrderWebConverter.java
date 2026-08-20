package com.bmos.lims2.web.inspect.order.converter;

import com.bmos.lims2.common.enums.InspectionOrderStatusEnum;
import com.bmos.lims2.server.inspect.order.dto.CustomFieldValueDTO;
import com.bmos.lims2.server.inspect.order.dto.InspectionOrderDTO;
import com.bmos.lims2.server.inspect.order.dto.InspectionOrderPageQueryDTO;
import com.bmos.lims2.server.inspect.order.dto.InspectionOrderSaveDTO;
import com.bmos.lims2.web.inspect.order.vo.req.CustomFieldValueVO;
import com.bmos.lims2.web.inspect.order.vo.req.InspectionOrderPageQueryVO;
import com.bmos.lims2.web.inspect.order.vo.req.InspectionOrderSaveVO;
import com.bmos.lims2.web.inspect.order.vo.resp.CustomFieldValueRespVO;
import com.bmos.lims2.web.inspect.order.vo.resp.InspectionOrderRespVO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * 检验单Web层对象转换器
 *
 * @author yigaohui
 * @since 2025/01/27 15:30
 */
@Mapper(componentModel = "spring")
public interface InspectionOrderWebConverter {

    InspectionOrderWebConverter INSTANCE = Mappers.getMapper(InspectionOrderWebConverter.class);

    /**
     * SaveVO转SaveDTO
     * @param saveVO 保存VO对象
     * @return 保存DTO对象
     */
    InspectionOrderSaveDTO voToSaveDTO(InspectionOrderSaveVO saveVO);

    /**
     * PageQueryVO转PageQueryDTO
     * @param queryVO 查询VO对象
     * @return 查询DTO对象
     */
    @Mapping(source = "requestUserName", target = "requesterName")
    InspectionOrderPageQueryDTO voToPageQueryDTO(InspectionOrderPageQueryVO queryVO);

    /**
     * DTO转RespVO
     * @param dto DTO对象
     * @return 响应VO对象
     */
    InspectionOrderRespVO dtoToRespVO(InspectionOrderDTO dto);

    /**
     * DTO列表转RespVO列表
     * @param dtoList DTO对象列表
     * @return 响应VO对象列表
     */
    List<InspectionOrderRespVO> dtoListToRespVOList(List<InspectionOrderDTO> dtoList);

    /**
     * CustomFieldValueVO转CustomFieldValueDTO
     * @param vo VO对象
     * @return DTO对象
     */
    CustomFieldValueDTO customFieldVoToDto(CustomFieldValueVO vo);

    /**
     * CustomFieldValueVO列表转CustomFieldValueDTO列表
     * @param voList VO对象列表
     * @return DTO对象列表
     */
    List<CustomFieldValueDTO> customFieldVoListToDtoList(List<CustomFieldValueVO> voList);

    /**
     * CustomFieldValueDTO转CustomFieldValueRespVO
     * @param dto DTO对象
     * @return 响应VO对象
     */
    CustomFieldValueRespVO customFieldDtoToRespVo(CustomFieldValueDTO dto);

    /**
     * CustomFieldValueDTO列表转CustomFieldValueRespVO列表
     * @param dtoList DTO对象列表
     * @return 响应VO对象列表
     */
    List<CustomFieldValueRespVO> customFieldDtoListToRespVoList(List<CustomFieldValueDTO> dtoList);

    /**
     * 单据状态枚举转描述
     * @param orderStatus 单据状态枚举
     * @return 状态描述
     */
    @Named("orderStatusToDesc")
    default String orderStatusToDesc(InspectionOrderStatusEnum orderStatus) {
        return orderStatus != null ? orderStatus.getName() : null;
    }
}