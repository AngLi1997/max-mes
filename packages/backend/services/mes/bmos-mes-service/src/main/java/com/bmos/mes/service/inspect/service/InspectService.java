package com.bmos.mes.service.inspect.service;

import com.bmos.mes.inspect.dto.InspectRejectDTO;
import com.bmos.mes.inspect.dto.InspectResultCallBackDTO;
import com.bmos.mes.service.inspect.controller.vo.InspectDetailVO;
import com.bmos.mes.service.inspect.controller.vo.InspectPageVO;
import com.bmos.mes.service.inspect.controller.vo.InspectResultVO;
import com.bmos.mes.service.inspect.service.dto.InitiateInspectDTO;
import com.bmos.mes.service.inspect.service.dto.InitiateRetryInspectDTO;
import com.bmos.mes.service.inspect.service.dto.InspectPageDTO;
import com.bmos.mybatis.page.CommonPage;

import java.util.List;

public interface InspectService {
    /**
     * 发起请验
     * @param dto
     */
    void initiateInspect(InitiateInspectDTO dto);

    /**
     * 分页查询请验单
     * @return
     */
    CommonPage<InspectPageVO> queryPage(InspectPageDTO dto);

    /**
     * 获取请验单详情
     * @param id
     * @return
     */
    InspectDetailVO queryDetail(Long id);

    /**
     * 重新发起请验
     * @param dto
     */
    void retryInitiateInspect(InitiateRetryInspectDTO dto);

    /**
     * 请验单退回
     * @param dtoList
     */
    void rejectInspect(List<InspectRejectDTO> dtoList);

    /**
     * 检验结果回传
     * @param inspectResultCallBackDTO
     */
    void inspectCallback(InspectResultCallBackDTO inspectResultCallBackDTO);

    /**
     * 获取请验结果
     * @param id
     * @return
     */
    InspectResultVO queryInspectResult(Long id);

    /**
     * 根据物料批号和物料id查询请验结果
     * @param materialBatchNo
     * @param materialId
     * @return
     */
    InspectResultVO queryInspectResultByMaterialBatchNoAndMaterialId(String materialBatchNo, Long materialId);

    /**
     * 根据配方物料id查询检验方案（供前端选择，自研LIMS路径）
     * @param formulaMaterialId
     * @return
     */
    java.util.List<com.bmos.mes.service.inspect.controller.vo.InspectSchemeVO> querySchemes(Long formulaMaterialId);
}
