package com.bmos.mes.service.inspect.mapper;

import com.bmos.mes.service.inspect.model.InspectResult;
import com.bmos.mybatis.mapper.BaseMapperX;
import com.bmos.mybatis.query.LambdaQueryWrapperX;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 检验结论表(BmInspectResult)表数据库访问层
 *
 * @author makejava
 * @since 2025-02-17 18:26:52
 */
@Mapper
public interface InspectResultMapper extends BaseMapperX<InspectResult> {


    /**
     * 根据请验单id查询
     * @param inspectId
     * @return
     */
    default List<InspectResult> selectByInspectId(Long inspectId){
        return selectList(new LambdaQueryWrapperX<InspectResult>()
                .eq(InspectResult::getInspectId, inspectId));
    }

    default List<InspectResult> selectByInspectIdList(List<Long> inspectIdList){
        return selectList(new LambdaQueryWrapperX<InspectResult>()
                .in(InspectResult::getInspectId, inspectIdList));
    }
}

