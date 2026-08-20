package com.bmos.mes.service.dataset.mapper;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.bmos.mes.service.dataset.dto.DatasetPointDataPreviewPageQuery;
import com.bmos.mes.service.dataset.dto.DatasetPointPageQuery;
import com.bmos.mes.service.dataset.model.DatasetPoint;
import com.bmos.mes.service.dataset.vo.DatasetPointDataPreviewPageVO;
import com.bmos.mes.service.dataset.vo.DatasetPointDataPreviewTitleVO;
import com.bmos.mybatis.mapper.BaseMapperX;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.ArrayList;
import java.util.List;

/**
 * 数据点mapper
 * @author liang
 * @version 1.0.0
 * @date 2024/8/13 14:28
 */
@Mapper
public interface IDatasetPointMapper extends BaseMapperX<DatasetPoint> {

    /**
     * 根据数据集id查询数据点
     * @param datasetId 数据集id
     */
    default List<DatasetPoint> listByDatasetId(Long datasetId){
        if (datasetId == null){
            return new ArrayList<>();
        }
        return selectList(new LambdaQueryWrapper<DatasetPoint>()
                .eq(DatasetPoint::getDatasetId, datasetId)
        );
    }

    /**
     * 预览数据点(批记录数据)
     * @param dto
     * @return
     */
    List<DatasetPointDataPreviewPageVO> previewDatasetPointData(@Param("dto") DatasetPointDataPreviewPageQuery dto);

    /**
     * 查询数据点标题
     * @param dto
     * @return
     */
    List<DatasetPointDataPreviewTitleVO> previewDatasetPointDataTitle(@Param("dto") DatasetPointDataPreviewPageQuery dto, @Param("latestProcessVersion") String latestProcessVersion);

    /**
     * 根据字段id列表和工步id列表查询所有数据点
     * @param fieldIds 字段id列表
     * @param procedureStepIds 工步id列表
     * @return 数据点列表
     */
    default List<DatasetPoint> listByFieldIdsAndProcedureStepIds(List<Long> fieldIds, List<Long> procedureStepIds){
        if (CollectionUtil.isEmpty(fieldIds) || CollectionUtil.isEmpty(procedureStepIds)){
            return new ArrayList<>();
        }
        return selectList(new LambdaQueryWrapper<DatasetPoint>()
                .in(DatasetPoint::getFieldId, fieldIds)
                .in(DatasetPoint::getProcedureStepId, procedureStepIds)
        );
    }

    default List<DatasetPoint> queryDatasetPointPage(DatasetPointPageQuery pageQuery){
        return selectList(new LambdaQueryWrapper<DatasetPoint>()
                .eq(DatasetPoint::getDatasetId, pageQuery.getDatasetId())
                .like(StrUtil.isNotBlank(pageQuery.getName()), DatasetPoint::getName, pageQuery.getName())
        );
    }

    default List<DatasetPoint> selectByDatasetId(Long datasetId){
        if (datasetId == null){
            return new ArrayList<>();
        }
        return selectList(new LambdaQueryWrapper<DatasetPoint>()
                .eq(DatasetPoint::getDatasetId, datasetId)
        );
    }

    default List<DatasetPoint> selectByDatasetPointKeys(List<String> dpNoList){
        if (CollectionUtil.isEmpty(dpNoList)){
            return new ArrayList<>();
        }
        return selectList(new LambdaQueryWrapper<DatasetPoint>()
                .in(DatasetPoint::getDatasetPointKey, dpNoList)
        );
    }
}