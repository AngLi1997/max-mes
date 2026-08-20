package com.bmos.mes.service.plan.document.mapper;

import com.bmos.mes.service.plan.document.mapper.param.TemplateInfoParam;
import com.bmos.mes.service.plan.document.model.BatchTemplateInfo;
import com.bmos.mybatis.mapper.BaseMapperX;
import com.bmos.mybatis.query.LambdaQueryWrapperX;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 批记录模板信息(BmBatchTemplateInfo)表数据库访问层
 *
 * @author makejava
 * @since 2024-08-19 11:06:02
 */
@Mapper
public interface BatchTemplateInfoMapper extends BaseMapperX<BatchTemplateInfo> {

    /**
     * 根据参数查询批记录模板信息
     * @param param
     * @return
     */
    List<BatchTemplateInfo> selectByParam(@Param("param") TemplateInfoParam param);


    default boolean existsByName(String name){
        return exists(new LambdaQueryWrapperX<BatchTemplateInfo>()
                .eq(BatchTemplateInfo::getName, name));
    }

    List<BatchTemplateInfo> selectAuthByIdList(@Param("templateInfoIdList") List<Long> templateInfoIdList,
                                               @Param("deptIds") List<Long> deptIds);

    default boolean existByCategoryId(Long id){
        return exists(new LambdaQueryWrapperX<BatchTemplateInfo>()
                .eq(BatchTemplateInfo::getCategoryId, id));
    }
}

