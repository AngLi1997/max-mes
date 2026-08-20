package com.bmos.lims2.server.eln.record.mapper;

import com.baomidou.mybatisplus.extension.toolkit.Db;
import com.bmos.lims2.server.eln.record.dto.ComponentDetailDTO;
import com.bmos.lims2.server.eln.record.entity.BatchRecordComponent;
import com.bmos.lims2.server.eln.record.vo.ComponentDetailVO;
import com.bmos.mybatis.mapper.BaseMapperX;
import com.bmos.mybatis.query.LambdaQueryWrapperX;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Collection;
import java.util.List;
import java.util.Set;


@Mapper
public interface BatchRecordComponentMapper extends BaseMapperX<BatchRecordComponent> {


    /**
     * 该查询已处理详情信息
     * @param versionId
     * @return
     */
    List<BatchRecordComponent> selectByVersionId(@Param("recordVersionId") Long versionId);

    default Boolean saveOrUpdateComponent(List<BatchRecordComponent> components) {
        return Db.saveOrUpdateBatch(components);
    }

    default Boolean saveOrUpdateFormula(BatchRecordComponent component){
        return Db.saveOrUpdate(component);
    }

    /**
     * 该查询已处理详情信息
     * @param itemId
     * @param recordVersionId
     * @return
     */
    List<BatchRecordComponent> selectComponentList(@Param("itemId") Long itemId, @Param("recordVersionId") Long recordVersionId);

    void deleteCompoenent(@Param("itemId") Long itemId,@Param("recordVersionId") Long recordVersionId);

    /**
     * 该查询已处理详情信息
     * @param recordVersionId
     * @return
     */
    List<BatchRecordComponent> selectGraphList(Long recordVersionId);

    void deleteFormula(Long componentId);

    /**
     * 该查询已处理详情信息
     * @param recordVersionId
     * @param fields
     * @param isResult
     * @return
     */
    List<BatchRecordComponent> selectByRecordVersionIdAndFields(@Param("recordVersionId") Long recordVersionId,
                                                                        @Param("fieldIds") Set<Long> fields,
                                                                        @Param("isResult") Boolean isResult);

    /**
     * 该查询已处理详情信息
     * @param longs
     * @param fieldIdList
     * @return
     */
    List<BatchRecordComponent> selectByRecordVersionIdListAndFields(@Param("versionIds") Collection<Long> longs,
                                                                                @Param("fieldIds") Collection<Long> fieldIdList);

    /**
     * 该查询已处理关联信息
     * @param recordItemIdList
     * @param recordVersionIdList
     * @param componentTypes
     * @return 查询出used为ture的数据
     */
    List<BatchRecordComponent> selectByRecordListAndTypeList(@Param("recordItemIdList") List<Long> recordItemIdList,
                                                             @Param("recordVersionIdList") List<Long> recordVersionIdList,
                                                             @Param("componentTypes") List<String> componentTypes);

    /**
     * 该查询已处理详情信息
     * @param idList
     * @return
     */
    List<BatchRecordComponent> selectByIdList(@Param("idList") List<Long> idList);

    /**
     * 根据组件ID查询（含detail表信息）
     * @param id 组件ID
     * @return 组件（含component_detail）
     */
    BatchRecordComponent selectWithDetailById(@Param("id") Long id);

    /**
     * 根据工艺版本id、记录项id、fieldId查询组件的配置信息
     * @param componentDetailDTOS
     * @return
     */
    List<ComponentDetailVO> selectByComponentDetailDTOS(@Param("detailList") List<ComponentDetailDTO> componentDetailDTOS);

    /**
     * 根据fieldId查询组件
     * @param fieldId
     * @return
     */
    Long getByFieldId(@Param("fieldId") Long fieldId);

    default List<BatchRecordComponent> selectByFieldIdList(List<Long> fieldIdList){
        return selectList(new LambdaQueryWrapperX<BatchRecordComponent>()
                .in(BatchRecordComponent::getFieldId, fieldIdList));
    }

    List<BatchRecordComponent> listByParentIds(@Param("parentIds") List<Long> parentIds);
}
