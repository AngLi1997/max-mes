package com.bmos.lims2.server.recordprint.mapper;

import com.bmos.lims2.server.recordprint.dto.PrintableAnalysisItemDTO;
import com.bmos.lims2.server.recordprint.dto.RecordItemAssetsDTO;
import com.bmos.lims2.server.inspect.order.dto.InspectionOrderDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * @Description: 记录打印相关查询Mapper
 * @Author: yigaohui
 * @Date: 2025/11/25 10:20
 */
@Mapper
public interface RecordPrintMapper {

    /**
     * 分页查询可打印的检验单（已确认 + 存在复核通过任务）
     * @param orderNo 检验单号
     * @param batchNo 批号
     * @param applyTimeStart 请验时间开始
     * @param applyTimeEnd 请验时间结束
     * @return 可打印检验单列表
     */
    List<InspectionOrderDTO> selectPrintableInspectionOrders(@Param("orderNo") String orderNo,
                                                             @Param("batchNo") String batchNo,
                                                             @Param("materialId") Long materialId,
                                                             @Param("materialIds") List<Long> materialIds,
                                                             @Param("inspectionRequestTimeStart") LocalDateTime inspectionRequestTimeStart,
                                                             @Param("inspectionRequestTimeEnd") LocalDateTime inspectionRequestTimeEnd);

    /**
     * 查询某个检验单下可打印（复核通过）的分析项
     * @param inspectionId 检验单ID
     * @return 可打印分析项列表
     */
    List<PrintableAnalysisItemDTO> listPrintableAnalysisItems(@Param("inspectionId") Long inspectionId);

    /**
     * 查询某个检验单下可打印（复核通过）的分析项（稳定性方案）
     * @param inspectionId 检验单ID
     * @return 可打印分析项列表
     */
    List<PrintableAnalysisItemDTO> listPrintableAnalysisItemsForStability(@Param("inspectionId") Long inspectionId);

    /**
     * 根据分析项记录绑定ID查询HTML模板内容
     * @param parameterRecordId 分析项记录绑定ID
     * @return HTML模板内容
     */
    String selectHtmlTemplateByParameterRecordId(@Param("parameterRecordId") Long parameterRecordId);

    /**
     * 根据任务ID查询HTML模板内容（限定任务记录版本与记录项）
     * @param taskId 任务ID
     * @return HTML模板内容
     */
    String selectHtmlTemplateByTaskId(@Param("taskId") Long taskId);

    /**
     * 根据任务ID查询记录项模板与页眉页脚配置
     * @param taskId 任务ID
     * @return 模板与页眉页脚资源
     */
    RecordItemAssetsDTO selectRecordItemAssetsByTaskId(@Param("taskId") Long taskId);

    /**
     * 根据任务ID查询执行表单数据，返回为Map（JSON->Map）
     * @param taskId 任务ID
     * @return 表单数据Map
     */
    Map<String, Object> selectExecuteFormDataMapByTaskId(@Param("taskId") Long taskId);

    /**
     * 记录操作日志
     * @param fields 字段Map
     * @return 影响条数
     */
    int insertOperationLog(@Param("fields") Map<String, Object> fields);
}


