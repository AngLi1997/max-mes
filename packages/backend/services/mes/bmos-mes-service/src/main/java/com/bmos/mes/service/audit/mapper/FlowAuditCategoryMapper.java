package com.bmos.mes.service.audit.mapper;

import com.bmos.mes.service.audit.model.FlowAuditCategory;
import com.bmos.mybatis.mapper.BaseMapperX;
import com.bmos.mybatis.query.LambdaQueryWrapperX;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;


/**
 * @author renjinguang
 */
@Mapper
public interface FlowAuditCategoryMapper extends BaseMapperX<FlowAuditCategory> {


    default List<FlowAuditCategory> selectCategoryList() {
        return selectList(new LambdaQueryWrapperX<FlowAuditCategory>());
    }

    default FlowAuditCategory queryByCode(String categoryCode){
        return selectOne(new LambdaQueryWrapperX<FlowAuditCategory>()
                .eq(FlowAuditCategory::getCode,categoryCode));
    }
}
