package com.bmos.lims2.web.inspect.sample.converter;

import com.bmos.lims2.server.inspect.sample.dto.SampleBatchCollectionDTO;
import com.bmos.lims2.server.inspect.sample.dto.SampleCollectionListDTO;
import com.bmos.lims2.server.inspect.sample.dto.SampleCollectionPageQueryDTO;
import com.bmos.lims2.web.inspect.sample.vo.req.SampleBatchCollectionReqVO;
import com.bmos.lims2.web.inspect.sample.vo.req.SampleCollectionPageReqVO;
import com.bmos.lims2.web.inspect.sample.vo.resp.SampleCollectionListRespVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * @Description: 样品领取Web层对象转换器
 * @Author: yigaohui
 * @Date: 2025/01/29 17:30
 */
@Mapper(componentModel = "spring")
public interface SampleCollectionWebConverter {

    SampleCollectionWebConverter INSTANCE = Mappers.getMapper(SampleCollectionWebConverter.class);

    /**
     * PageReqVO转PageQueryDTO
     * @param reqVO 分页查询请求VO
     * @return 分页查询DTO
     */
    SampleCollectionPageQueryDTO voToPageQueryDTO(SampleCollectionPageReqVO reqVO);

    /**
     * BatchCollectionReqVO转BatchCollectionDTO
     * @param reqVO 批量领取请求VO
     * @return 批量领取DTO
     */
    SampleBatchCollectionDTO voToBatchCollectionDTO(SampleBatchCollectionReqVO reqVO);

    /**
     * CollectionListDTO转CollectionListRespVO
     * @param dto 领取列表DTO
     * @return 领取列表响应VO
     */
    SampleCollectionListRespVO dtoToRespVO(SampleCollectionListDTO dto);

    /**
     * CollectionListDTO列表转CollectionListRespVO列表
     * @param dtoList 领取列表DTO列表
     * @return 领取列表响应VO列表
     */
    List<SampleCollectionListRespVO> dtoListToRespVOList(List<SampleCollectionListDTO> dtoList);

}
