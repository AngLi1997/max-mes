package com.bmos.lims2.server.inspect.order.mapper;

import com.bmos.lims2.server.inspect.sample.dto.*;
import com.bmos.lims2.server.inspect.order.entity.Sample;
import com.bmos.mybatis.mapper.BaseMapperX;
import com.bmos.mybatis.query.LambdaQueryWrapperX;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 样品Mapper
 *
 * @author yigaohui
 * @since 2025/01/27 15:30
 */
@Mapper
public interface SampleMapper extends BaseMapperX<Sample> {

    /**
     * 根据检验单ID查询样品信息（包含关联信息）
     * @param inspectionOrderId 检验单ID
     * @return 样品信息列表
     */
    List<SampleDTO> selectByInspectionOrderIdWithRelation(@Param("inspectionOrderId") Long inspectionOrderId);

    /**
     * 根据检验单ID查询样品信息
     * @param inspectionOrderId 检验单ID
     * @return 样品信息列表
     */
    default List<Sample> selectByInspectionOrderId(Long inspectionOrderId) {
        return selectList(new LambdaQueryWrapperX<Sample>()
                .eq(Sample::getInspectionOrderId, inspectionOrderId));
    }

    /**
     * 根据样品编号查询样品
     * @param sampleNo 样品编号
     * @return 样品信息
     */
    default Sample selectBySampleNo(String sampleNo) {
        return selectOne(new LambdaQueryWrapperX<Sample>()
                .eq(Sample::getSampleNo, sampleNo));
    }

    /**
     * 根据父样品ID查询子样品列表
     * @param parentSampleId 父样品ID
     * @return 子样品列表
     */
    default List<Sample> selectByParentSampleId(Long parentSampleId) {
        return selectList(new LambdaQueryWrapperX<Sample>()
                .eq(Sample::getParentSampleId, parentSampleId));
    }

    /**
     * 根据检验项目ID查询样品列表
     * @param inspectItemId 检验项目ID
     * @return 样品列表
     */
    default List<Sample> selectByInspectItemId(Long inspectItemId) {
        return selectList(new LambdaQueryWrapperX<Sample>()
                .eq(Sample::getInspectItemId, inspectItemId));
    }

    /**
     * 根据取样状态查询样品列表
     * @param sampled 是否已取样
     * @return 样品列表
     */
    default List<Sample> selectBySampledStatus(Boolean sampled) {
        return selectList(new LambdaQueryWrapperX<Sample>()
                .eq(Sample::getSampled, sampled));
    }

    /**
     * 根据接收状态查询样品列表
     * @param received 是否已接收
     * @return 样品列表
     */
    default List<Sample> selectByReceivedStatus(Boolean received) {
        return selectList(new LambdaQueryWrapperX<Sample>()
                .eq(Sample::getReceived, received));
    }

    /**
     * 根据分样状态查询样品列表
     * @param divided 是否已分样
     * @return 样品列表
     */
    default List<Sample> selectByDividedStatus(Boolean divided) {
        return selectList(new LambdaQueryWrapperX<Sample>()
                .eq(Sample::getDivided, divided));
    }

    /**
     * 根据领取状态查询样品列表
     * @param collected 是否已领取
     * @return 样品列表
     */
    default List<Sample> selectByCollectedStatus(Boolean collected) {
        return selectList(new LambdaQueryWrapperX<Sample>()
                .eq(Sample::getCollected, collected));
    }

    /**
     * 根据作废状态查询样品列表
     * @param discarded 是否作废
     * @return 样品列表
     */
    default List<Sample> selectByDiscardedStatus(Boolean discarded) {
        return selectList(new LambdaQueryWrapperX<Sample>()
                .eq(Sample::getDiscarded, discarded));
    }

    /**
     * 根据检验单ID删除样品信息
     * @param inspectionOrderId 检验单ID
     * @return 删除数量
     */
    default int deleteByInspectionOrderId(Long inspectionOrderId) {
        return delete(new LambdaQueryWrapperX<Sample>()
                .eq(Sample::getInspectionOrderId, inspectionOrderId));
    }


    /**
     * 根据样品编号查询样品扫码结果
     * @param sampleNo 样品编号
     * @return 样品扫码结果信息
     */
    SampleScanResultDTO selectSampleScanResult(@Param("sampleNo") String sampleNo);


    /**
     * 根据样品编号查询样品扫码结果
     * @param sampleNo 样品编号
     * @return 样品扫码结果信息
     */
    SamplePrintTagResultDTO selectSamplePrintTagResult(@Param("sampleNo") String sampleNo);

    /**
     * 根据样品ID查询样品扫码结果（详情用，包含检验单与物料信息）
     * @param sampleId 样品ID
     * @return 扫码结果详情
     */
    SampleScanResultDTO selectSampleScanResultById(@Param("sampleId") Long sampleId);

    /**
     * 根据检验单ID查询该检验单下所有样品的扫码结果信息
     * @param inspectionOrderId 检验单ID
     * @return 样品扫码结果信息列表
     */
    List<SampleScanResultDTO> selectSampleScanResultsByOrderId(@Param("inspectionOrderId") Long inspectionOrderId);

    /**
     * 分页查询可领取的样品列表
     * @param queryDTO 查询条件
     * @return 样品领取列表
     */
    List<SampleCollectionListDTO> selectCollectionPageList(SampleCollectionPageQueryDTO queryDTO);

    /**
     * 分页查询可进行样品处理的样品列表
     * @param queryDTO 查询条件
     * @return 样品处理列表
     */
    List<SampleHandleListDTO> selectHandlePageList(SampleHandlePageQueryDTO queryDTO);

    /**
     * 统计样品处理各状态数量（待回收/待处理/已处理）
     * 忽略状态筛选，复用列表的其他筛选条件
     * @param queryDTO 查询条件
     * @return 状态数量统计
     */
    SampleHandleStatusCountDTO selectHandleStatusCount(SampleHandlePageQueryDTO queryDTO);

    /**
     * 分页查询留样接收列表
     * @param queryDTO 查询条件
     * @return 留样接收列表
     */
    List<SampleCollectionListDTO> selectRetentionReceivePageList(
            com.bmos.lims2.server.inspect.receive.dto.RetentionReceivePageQueryDTO queryDTO);

    /**
     * 分页查询留样样品管理列表
     * @param queryDTO 查询条件
     * @return 留样样品管理列表
     */
    List<com.bmos.lims2.server.inspect.retention.dto.RetentionSampleManageListDTO> selectRetentionSampleManagePageList(
            com.bmos.lims2.server.inspect.retention.dto.RetentionSampleManagePageQueryDTO queryDTO);

    /**
     * 查询留样样品打印标签信息
     * @param sampleNo 样品编号
     * @return 留样样品打印标签信息
     */
    com.bmos.lims2.server.inspect.retention.dto.RetentionSamplePrintTagResultDTO selectRetentionSamplePrintTagResult(@Param("sampleNo") String sampleNo);
}
