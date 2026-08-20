package com.bmos.lims2.server.inspect.mes;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.bmos.lims2.server.inspect.entry.dto.InspectionEntryRecordDTO;
import com.bmos.lims2.server.inspect.entry.dto.InspectionOrderEntryDTO;
import com.bmos.lims2.server.inspect.entry.mapper.InspectionEntryRecordMapper;
import com.bmos.lims2.server.inspect.order.entity.InspectionOrder;
import com.bmos.mes.inspect.dto.InspectRejectDTO;
import com.bmos.mes.inspect.dto.InspectResultCallBackDTO;
import com.bmos.mes.inspect.dto.InspectResultItemDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 把 LIMS 检验单结果组装成 MES 回传契约（InspectResultCallBackDTO）。
 *
 * <p>说明（对应设计 §3.5/§3.6）：
 * <ul>
 *   <li>order.orderNo 即 MES 的 inspectNo（号由 LIMS 生成、MES 保存）。</li>
 *   <li>检项(检验程序) 对应 LIMS 的 分析项(parameter)：inspectProgramNo/alreadyConvertProgramNo
 *       均填 parameterCode（按设计 §3：LIMS 分析项 code 与 MES InspectStorageMaterialCodeEnum
 *       写死值保持一致，MES 侧不再做对照表）。</li>
 *   <li>检项结果值取该分析项任务下首个非空数据点的 valueText/valueNumber。</li>
 *   <li>汇总结论：任一分析项判定为不合格则整单 UNQUALIFIED，否则 QUALIFIED（枚举 NAME）。</li>
 * </ul>
 */
@Component
public class MesCallbackAssembler {

    private static final String QUALIFIED = "合格";
    private static final String UNQUALIFIED = "不合格";
    private static final String CLOSED_FINISHED = "FINISHED";

    @Autowired
    private InspectionEntryRecordMapper inspectionEntryRecordMapper;

    /**
     * 组装检验结果回传 DTO。
     */
    public InspectResultCallBackDTO assemble(InspectionOrder order) {
        // 回传时机：样品审核通过回调内（任务已 COMPLETED），故按 COMPLETED 状态拉取本检验单下的任务。
        List<InspectionOrderEntryDTO.AnalysisItemEntryItemDTO> tasks =
                inspectionEntryRecordMapper.selectCompletedTasksByInspectionOrder(order.getId());

        boolean allQualified = true;
        List<InspectResultItemDTO> items = new ArrayList<>();
        if (CollUtil.isNotEmpty(tasks)) {
            for (InspectionOrderEntryDTO.AnalysisItemEntryItemDTO task : tasks) {
                // 整单结论：任一分析项明确判定为不合格则整单不合格
                if (Boolean.FALSE.equals(task.getJudgedResult())) {
                    allQualified = false;
                }
                InspectResultItemDTO item = new InspectResultItemDTO();
                item.setInspectProgramNo(task.getParameterCode());
                item.setAlreadyConvertProgramNo(task.getParameterCode());
                item.setInspectProgramName(task.getParameterName());
                item.setInspectResult(resolveResultValue(task.getId()));
                item.setInspectConclusion(Boolean.FALSE.equals(task.getJudgedResult()) ? UNQUALIFIED : QUALIFIED);
                items.add(item);
            }
        }

        InspectResultCallBackDTO dto = new InspectResultCallBackDTO();
        dto.setInspectNo(order.getOrderNo());
        dto.setResult(allQualified ? QUALIFIED : UNQUALIFIED);
        dto.setClosed(CLOSED_FINISHED);
        dto.setInspectResultItemDTOS(items);
        return dto;
    }

    /**
     * 组装退回回传 DTO（按 orderNo 匹配 MES 请验单）。
     */
    public List<InspectRejectDTO> assembleReject(InspectionOrder order, String reason) {
        InspectRejectDTO dto = new InspectRejectDTO();
        dto.setInspectNo(order.getOrderNo());
        dto.setReason(reason);
        return Collections.singletonList(dto);
    }

    /**
     * 取分析项任务下首个非空数据点结果值（valueText 优先，否则 valueNumber）。
     */
    private String resolveResultValue(Long taskId) {
        List<InspectionEntryRecordDTO> records = inspectionEntryRecordMapper.selectByTaskId(taskId);
        if (CollUtil.isEmpty(records)) {
            return null;
        }
        for (InspectionEntryRecordDTO r : records) {
            if (StrUtil.isNotBlank(r.getValueText())) {
                return r.getValueText();
            }
            if (StrUtil.isNotBlank(r.getValueNumber())) {
                return r.getValueNumber();
            }
        }
        return null;
    }
}
