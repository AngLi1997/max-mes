package com.bmos.lims2.server.inspect.entry.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bmos.lims2.server.inspect.entry.dto.AnalysisItemEntryDTO;
import com.bmos.lims2.server.inspect.entry.dto.AnalysisItemEntryQueryDTO;
import com.bmos.lims2.server.inspect.entry.dto.EntryStatsDTO;
import com.bmos.lims2.server.inspect.entry.dto.InspectionEntryRecordDTO;
import com.bmos.lims2.server.inspect.entry.dto.InspectionOrderEntryDTO;
import com.bmos.lims2.server.inspect.entry.dto.TaskOperatorDTO;
import com.bmos.lims2.server.inspect.entry.dto.InspectionOrderEntryQueryDTO;
import com.bmos.lims2.server.inspect.entry.dto.TrendValuePointDTO;
import com.bmos.lims2.server.inspect.entry.entity.InspectionEntryRecord;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 检验录入记录Mapper
 *
 * @author system
 * @since 2025/01/30
 */
@Mapper
public interface InspectionEntryRecordMapper extends BaseMapper<InspectionEntryRecord> {

    /**
     * 查询分析项录入列表
     *
     * @param queryDTO 查询条件
     * @return 分析项录入列表
     */
    List<AnalysisItemEntryDTO> selectAnalysisItemEntryList(@Param("query") AnalysisItemEntryQueryDTO queryDTO);

    /**
     * 查询分析项-方法分组（母列表）：方法版本ID相同视为同一方法
     *
     * @param queryDTO 查询条件
     * @return 分析项-方法分组列表
     */
    List<com.bmos.lims2.server.inspect.entry.dto.AnalysisItemMethodGroupDTO> selectAnalysisItemMethodEntryList(@Param("query") AnalysisItemEntryQueryDTO queryDTO);

    /**
     * 查询检验单录入列表
     *
     * @param queryDTO 查询条件
     * @return 检验单录入列表
     */
    List<InspectionOrderEntryDTO> selectInspectionOrderEntryList(@Param("query") InspectionOrderEntryQueryDTO queryDTO);

    /**
     * 根据分析项任务ID查询数据点录入记录（常规检验方案）
     *
     * @param taskId 任务ID
     * @return 数据点录入记录列表
     */
    List<InspectionEntryRecordDTO> selectByTaskId(@Param("taskId") Long taskId);

    /**
     * 根据分析项任务ID查询数据点录入记录（稳定性方案）
     *
     * @param taskId 任务ID
     * @return 数据点录入记录列表
     */
    List<InspectionEntryRecordDTO> selectByTaskIdForStability(@Param("taskId") Long taskId);

    /**
     * 根据检验单ID和分析项ID查询数据点录入记录
     *
     * @param inspectionOrderId 检验单ID
     * @param parameterId       分析项ID
     * @return 数据点录入记录列表
     */
    List<InspectionEntryRecordDTO> selectByInspectionOrderIdAndParameterId(
            @Param("inspectionOrderId") Long inspectionOrderId,
            @Param("parameterId") Long parameterId);

    /**
     * 根据检验单ID查询所有数据点录入记录
     * @param inspectionOrderId 检验单ID
     * @return 数据点录入记录列表
     */
    List<InspectionEntryRecordDTO> selectByInspectionOrderId(@Param("inspectionOrderId") Long inspectionOrderId);

    /**
     * 根据检验单ID与检验项目ID查询数据点录入记录
     *
     * @param inspectionOrderId 检验单ID
     * @param inspectItemId     检验项目ID
     * @return 数据点录入记录列表
     */
    List<InspectionEntryRecordDTO> selectByInspectionOrderIdAndItemId(
            @Param("inspectionOrderId") Long inspectionOrderId,
            @Param("inspectItemId") Long inspectItemId);

    /**
     * 查询某检验单已录入数据的检验项目（去重）
     */
    List<com.bmos.lims2.server.inspect.entry.dto.InspectItemTabDTO> listEnteredInspectItemsByOrderId(@Param("orderId") Long orderId);

    /**
     * 统计录入未完成的分析项数量
     *
     * @param queryDTO 查询条件
     * @return 未完成数量
     */
    Long countIncompleteAnalysisItems(@Param("query") AnalysisItemEntryQueryDTO queryDTO);

    /**
     * 统计录入未完成的检验单数量
     *
     * @param queryDTO 查询条件
     * @return 未完成数量
     */
    Long countIncompleteInspectionOrders(@Param("query") InspectionOrderEntryQueryDTO queryDTO);

    /**
     * 统计判定异常的分析项数量
     *
     * @param queryDTO 查询条件
     * @return 异常数量
     */
    Long countAbnormalAnalysisItems(@Param("query") AnalysisItemEntryQueryDTO queryDTO);

    /**
     * 统计判定异常的检验单数量
     *
     * @param queryDTO 查询条件
     * @return 异常数量
     */
    Long countAbnormalInspectionOrders(@Param("query") InspectionOrderEntryQueryDTO queryDTO);

    /**
     * 根据任务ID获取用户权限范围内的任务ID列表
     *
     * @param userId 用户ID
     * @return 任务ID列表
     */
    List<Long> selectAuthorizedTaskIds(@Param("userId") String userId);

    /**
     * 根据数据点配置ID查询录入记录
     *
     * @param dataPointConfigId 数据点配置ID
     * @return 录入记录列表
     */
    List<InspectionEntryRecordDTO> selectBySchemeDataPointId(@Param("dataPointConfigId") Long dataPointConfigId);

    /**
     * 根据数据点配置ID和任务ID查询录入记录
     *
     * @param dataPointConfigId 数据点配置ID
     * @param taskId           任务ID
     * @return 录入记录列表
     */
    List<InspectionEntryRecordDTO> selectBySchemeDataPointIdAndTaskId(@Param("dataPointConfigId") Long dataPointConfigId, @Param("taskId") Long taskId);

    /**
     * 查询用户所在的检验班组ID列表
     *
     * @param userId 用户ID
     * @return 班组ID列表
     */
    List<Long> selectUserInspectionTeamIds(@Param("userId") String userId);

    /**
     * 查询特定分析项的检验单任务分组（子列表）
     *
     *
     * @param inspectItemId
     * @param inspectParameterId 分析项ID
     * @param queryDTO          查询条件
     * @return 检验单录入项列表
     */
    List<AnalysisItemEntryDTO.InspectionOrderEntryItemDTO> selectInspectionOrdersByAnalysisItem(
            @Param("inspectItemId") Long inspectItemId,
            @Param("inspectParameterId") Long inspectParameterId,
            @Param("recordVersionId") Long recordVersionId,
            @Param("query") AnalysisItemEntryQueryDTO queryDTO);

    /**
     * 查询特定检验单下的任务列表（子列表）
     *
     * @param inspectionOrderId 检验单ID
     * @param queryDTO         查询条件
     * @return 分析项录入项列表
     */
    List<InspectionOrderEntryDTO.AnalysisItemEntryItemDTO> selectTasksByInspectionOrderPage(
            @Param("inspectionOrderId") Long inspectionOrderId,
            @Param("query") InspectionOrderEntryQueryDTO queryDTO);



    /**
     * 查询特定检验单下的任务列表（子列表）
     *
     * @param inspectionOrderId 检验单ID
     * @return 分析项录入项列表
     */
    List<InspectionOrderEntryDTO.AnalysisItemEntryItemDTO> selectSampleAuditTasksByInspectionOrder(
            @Param("inspectionOrderId") Long inspectionOrderId);

    /**
     * 查询检验单下已完成（COMPLETED）的任务列表 —— 用于样品审核通过后回传 MES。
     *
     * @param inspectionOrderId 检验单ID
     * @return 分析项录入项列表
     */
    List<InspectionOrderEntryDTO.AnalysisItemEntryItemDTO> selectCompletedTasksByInspectionOrder(
            @Param("inspectionOrderId") Long inspectionOrderId);

    /**
     * 合并统计 - 分析项维度
     *
     * @param queryDTO 查询条件
     * @return 统计信息
     */
    EntryStatsDTO analysisItemEntryStats(@Param("query") AnalysisItemEntryQueryDTO queryDTO);

    /**
     * 合并统计 - 检验单维度
     *
     * @param queryDTO 查询条件
     * @return 统计信息
     */
    EntryStatsDTO inspectionOrderEntryStats(@Param("query") InspectionOrderEntryQueryDTO queryDTO);

    /**
     * 批量查询任务的录入人（操作人）ID列表
     *
     * @param taskIds 任务ID列表
     * @return 任务-录入人ID列表
     */
    List<TaskOperatorDTO> selectTaskOperatorsByTaskIds(@Param("taskIds") List<Long> taskIds);

    /**
     * 检项查询-按方案版本获取分析项（一级表头）
     */
    List<com.bmos.lims2.server.inspect.entry.dto.DataPointQueryRespDTO.HeaderLevel1> listHeadersParametersByVersion(@Param("versionId") Long versionId);

    /**
     * 检项查询-按方案版本获取数据点名称（去重）
     */
    List<String> listHeaderPointNamesByVersion(@Param("versionId") Long versionId);

    /**
     * 检项查询-按方案获取分析项（一级表头）
     */
    List<com.bmos.lims2.server.inspect.entry.dto.DataPointQueryRespDTO.HeaderLevel1> listHeadersParametersByScheme(@Param("schemeId") Long schemeId);

    /**
     * 检项查询-按方案获取数据点名称（去重，同名合并）
     */
    List<String> listHeaderPointNamesByScheme(@Param("schemeId") Long schemeId);

    /**
     * 检项查询-按方案获取表头分组（参数-数据点），同名同类型在参数内去重、跨版本合并
     */
    List<com.bmos.lims2.server.inspect.entry.dto.HeaderGroupRowDTO> listHeaderGroupsByScheme(@Param("schemeId") Long schemeId);

    /**
     * 检项查询-按方案版本获取表头分组（参数-数据点），同名同类型在参数内去重
     */
    List<com.bmos.lims2.server.inspect.entry.dto.HeaderGroupRowDTO> listHeaderGroupsByVersion(@Param("versionId") Long versionId);

    /**
     * 检项查询-按条件分页检验单ID
     */
    List<com.bmos.lims2.server.inspect.order.dto.InspectionOrderOptionDTO> pageOrderBaseForDataPoint(@Param("materialId") Long materialId,
                                                                                                   @Param("schemeVersionId") Long schemeVersionId,
                                                                                                   @Param("requestStartTime") java.time.LocalDateTime requestStartTime,
                                                                                                   @Param("requestEndTime") java.time.LocalDateTime requestEndTime,
                                                                                                   @Param("orderIds") List<Long> orderIds);

    /**
     * 检项查询-按方案分页检验单基础（用于行分页）
     */
    List<com.bmos.lims2.server.inspect.order.dto.InspectionOrderOptionDTO> pageOrderBaseForDataPointByScheme(@Param("materialId") Long materialId,
                                                                                                            @Param("schemeId") Long schemeId,
                                                                                                            @Param("requestStartTime") java.time.LocalDateTime requestStartTime,
                                                                                                            @Param("requestEndTime") java.time.LocalDateTime requestEndTime,
                                                                                                            @Param("orderIds") List<Long> orderIds);

    /**
     * 检项查询-按稳定性方案分页检验单基础（用于行分页）
     */
    List<com.bmos.lims2.server.inspect.order.dto.InspectionOrderOptionDTO> pageOrderBaseForDataPointByStabilityScheme(@Param("materialId") Long materialId,
                                                                                                                      @Param("schemeId") Long schemeId,
                                                                                                                      @Param("requestStartTime") java.time.LocalDateTime requestStartTime,
                                                                                                                      @Param("requestEndTime") java.time.LocalDateTime requestEndTime,
                                                                                                                      @Param("orderIds") List<Long> orderIds);

    /**
     * 检项查询-查询选中检验单的数据点值（最新一条）
     */
    List<com.bmos.lims2.server.inspect.entry.dto.InspectionEntryRecordDTO> listEntryValuesByOrderIds(@Param("orderIds") List<Long> orderIds,
                                                                                                     @Param("schemeVersionId") Long schemeVersionId);

    /**
     * 检项查询-查询选中检验单的数据点值（最新一条），按方案过滤
     */
    List<com.bmos.lims2.server.inspect.entry.dto.InspectionEntryRecordDTO> listEntryValuesByOrderIdsByScheme(@Param("orderIds") List<Long> orderIds,
                                                                                                              @Param("schemeId") Long schemeId);

    /**
     * 检项查询-查询选中检验单的数据点值（最新一条），按稳定性方案过滤
     */
    List<com.bmos.lims2.server.inspect.entry.dto.InspectionEntryRecordDTO> listEntryValuesByOrderIdsByStabilityScheme(@Param("orderIds") List<Long> orderIds,
                                                                                                                       @Param("schemeId") Long schemeId);

    /**
     * 趋势查询：按检品/方案/项目/分析项/数据点名称与时间范围，返回数值型录入及检验单信息
     */
    List<TrendValuePointDTO> selectNumericTrendValues(@Param("materialId") Long materialId,
                                                      @Param("schemeId") Long schemeId,
                                                      @Param("inspectItemId") Long inspectItemId,
                                                      @Param("parameterId") Long parameterId,
                                                      @Param("dataPointName") String dataPointName,
                                                      @Param("requestStartTime") java.time.LocalDateTime requestStartTime,
                                                      @Param("requestEndTime") java.time.LocalDateTime requestEndTime);
}
