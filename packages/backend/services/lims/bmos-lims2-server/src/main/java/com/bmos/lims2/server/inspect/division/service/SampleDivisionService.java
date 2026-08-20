package com.bmos.lims2.server.inspect.division.service;

import com.bmos.lims2.server.inspect.division.dto.SampleDivisionRemainDTO;
import com.bmos.lims2.server.inspect.sample.dto.SampleDTO;
import com.bmos.lims2.server.inspect.division.dto.SampleDivisionDTO;

import java.math.BigDecimal;
import java.util.List;

/**
 * @Description: 分样服务接口
 * @Author: yigaohui
 * @Date: 2025/01/29 16:45
 */
public interface SampleDivisionService {

    /**
     * 计算分样剩余量
     * @param originalSampleId 原样品ID
     * @param divisionResults 当前分样结果
     * @return 剩余量信息
     */
    SampleDivisionRemainDTO calculateRemaining(Long originalSampleId, List<SampleDivisionDTO.DivisionResultDTO> divisionResults);

    /**
     * 保存分样结果
     * @param divisionDTO 分样数据
     */
    List<SampleDTO> saveSampleDivision(SampleDivisionDTO divisionDTO);
}
