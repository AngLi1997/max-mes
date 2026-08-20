package com.bmos.wms.service.inspect.service;

import com.bmos.mybatis.page.CommonPage;
import com.bmos.wms.inspect.dto.InspectRejectDTO;
import com.bmos.wms.inspect.dto.InspectResultCallBackDTO;
import com.bmos.wms.service.inspect.controller.vo.InspectConfigDetailVO;
import com.bmos.wms.service.inspect.controller.vo.InspectDetailVO;
import com.bmos.wms.service.inspect.controller.vo.InspectPageVO;
import com.bmos.wms.service.inspect.controller.vo.InspectSchemeVO;
import com.bmos.wms.service.inspect.service.dto.InitiateInspectDTO;
import com.bmos.wms.service.inspect.service.dto.InitiateRetryInspectDTO;
import com.bmos.wms.service.inspect.service.dto.InspectPageDTO;

import java.util.List;

/**
 * WMS 请验主服务（mirror MES InspectService）。
 */
public interface IInspectService {

    /** 发起请验，返回 LIMS 检验单号。 */
    String initiateInspect(InitiateInspectDTO dto);

    /** 重新发起请验（方案 B：作废原单 + 新建），返回新检验单号。 */
    String retryInitiateInspect(InitiateRetryInspectDTO dto);

    /** 分页查询当前货品 / 批次的检验单列表。 */
    CommonPage<InspectPageVO> queryPage(InspectPageDTO dto);

    /** 检验单详情（含字段值 + 结论）。 */
    InspectDetailVO queryDetail(Long id);

    /** 按库存批次查询历史检验单（前端用于查看历史）。 */
    List<InspectPageVO> queryHistory(Long inventoryBatchId);

    /** 前端选择请验单：返回该库存批次对应货品的所有可用请验单配置。 */
    List<InspectConfigDetailVO> queryConfigByBatchId(Long inventoryBatchId);

    /** 前端选择检验方案：返回该库存批次对应货品的可用方案。 */
    List<InspectSchemeVO> querySchemesByBatchId(Long inventoryBatchId);

    /** 检验结果回传（被 LIMS 通过 wms-feign 反向调用）。 */
    void inspectCallback(InspectResultCallBackDTO dto);

    /** 检验单退回（被 LIMS 通过 wms-feign 反向调用）。 */
    void rejectInspect(List<InspectRejectDTO> dtoList);
}
