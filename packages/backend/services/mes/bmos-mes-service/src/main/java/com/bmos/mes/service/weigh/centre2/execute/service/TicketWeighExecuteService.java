package com.bmos.mes.service.weigh.centre2.execute.service;

import com.bmos.mybatis.page.CommonPage;
import com.bmos.mes.service.weigh.centre2.execute.service.dto.*;
import com.bmos.mes.service.weigh.centre2.execute.controller.vo.*;
import java.util.List;

public interface TicketWeighExecuteService {
    /**
     * 分页查询工单
     * @param dto 分页参数及查询条件
     * @return 工单分页结果
     */
    CommonPage<WeighTicketPageVO> pageWeighTicket(WeighTicketPageDTO dto, Boolean history);

    /**
     * 根据工单id查询工单详情
     * @param ticketId 工单id
     * @return 工单详情VO
     */
    WeighTicketDetailVO getWeighTicketDetail(Long ticketId);

    /**
     * 执行称量需求（变更状态为称量中）
     * @param dto
     */
    void executeWeighRequirement(TicketRequirementBindStorageMaterialDTO dto);

    /**
     * 称量工单与操作人绑定
     *
     * @param dto
     */
    void bindOperator(TicketBindOperatorDTO dto);

    /**
     * 单次称量记录保存
     * @param dto 称量记录参数
     */
    TicketRequirementEnoughVO saveWeighRequirementRecord(WeighRequirementRecordDTO dto);

    /**
     * 余料称量记录保存
     * @param dto 余料称量记录参数
     */
    TicketRequirementEnoughVO saveOddmentWeighRecord(WeighRequirementRecordDTO dto);

    /**
     * 称量签名
     *
     * @param dto
     */
    void signWeigh(SignWeighDTO dto);

    /**
     * 异常完成称量接口
     *
     * @param dto@return
     */
    void finishWeighRequirement(FinishWeighDTO dto);

    /**
     * 根据工单ID查询所有未称量或称量中的需求
     * @param ticketId 工单ID
     * @return 未称量或称量中需求VO列表
     */
    List<WeighRequirementVO> listUnWeighedOrWeighingRequirements(Long ticketId);

    /**
     * 根据称量需求id查询称量需求详情
     * @param requirementId 需求id
     * @return 需求详情VO
     */
    WeighRequirementVO getWeighRequirementDetail(Long requirementId);

    /**
     * 根据工单id查询工单内余料信息
     */
    TicketOddmentInfoVO getOddmentInfoByTicketId(Long ticketId);

    /**
     * 根据工单id和称量类型查询工单内所有称量记录
     */
    TicketWeighRequirementRecordVO getWeighRecordsByTicketId(Long ticketId);

    /**
     * 称量工单与库存物料绑定
     *
     * @param requirementId
     * @param storageMaterialIds
     */
    void bindMaterialToRequirement(Long requirementId, List<Long> storageMaterialIds);
}