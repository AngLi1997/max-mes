package com.bmos.mes.service.dataset.service;

import com.bmos.mes.service.dataset.dto.*;
import com.bmos.mes.service.dataset.enums.DatasetType;
import com.bmos.mes.service.dataset.model.Dataset;
import com.bmos.mes.service.dataset.model.DatasetPoint;
import com.bmos.mes.service.dataset.vo.*;
import com.bmos.mybatis.page.CommonPage;

import java.util.Collection;
import java.util.List;

/**
 * 数据集service
 * @author liang
 * @version 1.0.0
 * @date 2024/8/23 14:46
 */
public interface IDatasetService {

    /**
     * 创建数据集
     * @param dto 参数
     */
    Long createDataset(DatasetCreateDTO dto);

    /**
     * 编辑数据集
     * @param dto 参数
     */
    Long editDataset(DatasetEditDTO dto);

    /**
     * 预览数据点的值
     * @param dto
     * @return
     */
    DatasetPointDataPreviewVO previewDatasetPointData(DatasetPointDataPreviewPageQuery dto);

    DatasetPointDataPreviewListVO previewDatasetPointListData(DatasetPointDataPreviewPageQuery dto);

    /**
     * 根据字段id列表和工步id列表查询所有数据点
     * @param fieldIds 字段id列表
     * @param procedureStepIds 工步id列表
     * @return 数据点列表
     */
    List<DatasetPoint> listByFieldIdsAndProcedureStepIds(List<Long> fieldIds, List<Long> procedureStepIds);

    /**
     * 分页查询数据集
     * @param pageQuery 分页查询参数
     * @return 分页数据集
     */
    CommonPage<DatasetSimpleVO> queryDatasetPage(DatasetPageQuery pageQuery);

    /**
     * 分页查询数据点
     * @param pageQuery 分页查询参数
     * @return 分页数据点
     */
    CommonPage<DatasetPointPageVO> queryDatasetPointPage(DatasetPointPageQuery pageQuery);

    /**
     * 查询数据集详情
     * @param id 数据集id
     * @return 数据集详情
     */
    DatasetVO queryDatasetDetail(Long id);

    /**
     * 删除数据集
     * @param id
     */
    void deleteDataset(Long id);

    /**
     * 根据数据集索引获取所有的数据集信息
     * @param dataSet
     * @return
     */
    List<Dataset> queryByDataSetKeyList(Collection<String> dataSet);

    /**
     * 根据工艺id查询数据集列表
     * @param processId 工艺id
     * @param datasetType 数据集类型
     * @return 数据集列表
     */
    List<DatasetSimpleVO> queryByProcessIdAndType(Long processId, DatasetType datasetType);
}
