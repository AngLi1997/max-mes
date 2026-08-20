package com.bmos.mes.service.dataset.mapper;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.bmos.mes.service.dataset.dto.DatasetPageQuery;
import com.bmos.mes.service.dataset.enums.DatasetType;
import com.bmos.mes.service.dataset.model.Dataset;
import com.bmos.mybatis.mapper.BaseMapperX;
import org.apache.ibatis.annotations.Mapper;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * 数据集mapper
 *
 * @author liang
 * @version 1.0.0
 * @date 2024/8/13 14:28
 */
@Mapper
public interface IDatasetMapper extends BaseMapperX<Dataset> {

    /**
     * 根据分类查询数据集列表
     *
     * @param datasetCategoryId 数据集分类id
     * @return 数据集列表
     */
    default List<Dataset> listByCategoryId(Long datasetCategoryId) {
        return selectList(new LambdaQueryWrapper<Dataset>()
                .eq(Dataset::getDatasetCategoryId, datasetCategoryId)
        );
    }

    /**
     * 校验数据集名称是否存在
     *
     * @param name 数据集名称
     * @return 是否存在 true:存在 false:不存在
     */
    default boolean existByName(String name) {
        if (StrUtil.isBlank(name)) {
            return false;
        }
        return exists(new LambdaQueryWrapper<Dataset>()
                .eq(Dataset::getName, name)
        );
    }

    default List<Dataset> queryDatasetPage(DatasetPageQuery pageQuery, List<Long> datasetCategoryIds) {
        return selectList(new LambdaQueryWrapper<Dataset>()
                .in(Dataset::getDatasetCategoryId, datasetCategoryIds)
                .eq(StrUtil.isNotBlank(pageQuery.getDatasetType()), Dataset::getType, pageQuery.getDatasetType())
                .like(StrUtil.isNotBlank(pageQuery.getName()), Dataset::getName, pageQuery.getName())
                .orderByDesc(Dataset::getCreateTime)
        );
    }

    default List<Dataset> selectByDataSetKeyList(Collection<String> dataSet){
        return selectList(new LambdaQueryWrapper<Dataset>().in(Dataset::getDatasetKey, dataSet));
    }

    default List<Dataset> queryByProcessIdAndType(Long processId, DatasetType datasetType){
        if (processId == null){
            return new ArrayList<>();
        }
        return selectList(new LambdaQueryWrapper<Dataset>()
                .eq(Dataset::getProcessId, processId)
                .eq(datasetType != null, Dataset::getType, datasetType)
        );
    }
}
