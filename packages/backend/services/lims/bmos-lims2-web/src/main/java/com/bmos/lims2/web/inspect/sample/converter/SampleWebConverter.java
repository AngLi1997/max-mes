package com.bmos.lims2.web.inspect.sample.converter;

import com.bmos.lims2.server.inspect.sample.dto.SampleDTO;
import com.bmos.lims2.server.inspect.sample.dto.SampleScanResultDTO;
import com.bmos.lims2.web.inspect.sample.vo.resp.SampleListRespVO;
import com.bmos.lims2.web.inspect.sample.vo.resp.SampleScanResultRespVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * @Description: 样品Web层对象转换器
 * @Author: yigaohui
 * @Date: 2025/01/29 18:00
 */
@Mapper(componentModel = "spring")
public interface SampleWebConverter {

    SampleWebConverter INSTANCE = Mappers.getMapper(SampleWebConverter.class);

    /**
     * SampleScanResultDTO转SampleScanResultRespVO
     * @param dto 样品扫码结果DTO
     * @return 样品扫码结果响应VO
     */
    SampleScanResultRespVO scanResultDtoToRespVO(SampleScanResultDTO dto);

    /**
     * SampleScanResultDTO转SampleListRespVO（用于保持字段一致性）
     * @param dto 样品扫码结果DTO
     * @return 样品列表响应VO
     */
    SampleListRespVO scanResultDtoToListRespVO(SampleScanResultDTO dto);

    /**
     * SampleScanResultDTO列表转SampleListRespVO列表
     * @param dtoList 样品扫码结果DTO列表
     * @return 样品列表响应VO列表
     */
    List<SampleListRespVO> scanResultDtoListToListRespVOList(List<SampleScanResultDTO> dtoList);

}
