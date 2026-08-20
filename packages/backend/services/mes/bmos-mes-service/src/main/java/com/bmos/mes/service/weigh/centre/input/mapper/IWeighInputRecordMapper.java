package com.bmos.mes.service.weigh.centre.input.mapper;

import com.bmos.mes.service.components.comps.MaterialInputComponentsFromDataOPT;
import com.bmos.mes.service.components.model.BusinessComponentInstance;
import com.bmos.mes.service.weigh.centre.input.model.WeighInputRecord;
import com.bmos.mes.service.weigh.centre.input.vo.WeighInputRecordVO;
import com.bmos.mybatis.mapper.BaseMapperX;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.ArrayList;
import java.util.List;

/**
 * @author liang
 * @version 1.0.0
 * @date 2024/7/18 16:07
 */
@Mapper
public interface IWeighInputRecordMapper extends BaseMapperX<WeighInputRecord> {

    /**
     * 根据生产计划id和需求key查询待投料列表
     * @param productPlanId 生产计划id
     * @param requirementIds 需求id list
     * @return
     */
    List<WeighInputRecordVO> getInputList(@Param("productPlanId") Long productPlanId,
                                          @Param("requirementIds") List<Long> requirementIds);

    /**
     * 根据生产计划id和需求key查询待投料列表(组件视图)
     * @param productPlanId 生产计划id
     * @param requirementIds 需求id list
     * @return
     */
    List<MaterialInputComponentsFromDataOPT> getComponentsViewList(@Param("productPlanId") Long productPlanId,
                                                                   @Param("requirementIds") List<Long> requirementIds);

    default List<WeighInputRecord> selectByComponentInstanceId(BusinessComponentInstance componentInstance){
        if (componentInstance == null){
            return new ArrayList<>();
        }
        return selectList(WeighInputRecord::getInputComponentInstanceId, componentInstance.getId());
    }
}
