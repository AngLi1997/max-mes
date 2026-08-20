package com.bmos.mes.service.plan.document.mapper;

import com.bmos.mes.common.enums.plan.TemplateVersionStatusEnum;
import com.bmos.mes.service.plan.document.model.BatchTemplateVersion;
import com.bmos.mybatis.mapper.BaseMapperX;
import com.bmos.mybatis.query.LambdaQueryWrapperX;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 批记录模板版本(BmBatchTemplateVersion)表数据库访问层
 *
 * @author makejava
 * @since 2024-08-19 11:07:01
 */
@Mapper
public interface BatchTemplateVersionMapper extends BaseMapperX<BatchTemplateVersion> {


    default boolean existsByVersion(String version, Long templateInfoId){
        return exists(new LambdaQueryWrapperX<BatchTemplateVersion>()
                .eq(BatchTemplateVersion::getVersion, version)
                .eq(BatchTemplateVersion::getBatchTemplateInfoId, templateInfoId));
    }

    default List<BatchTemplateVersion> selectByTemplateInfoId(Long templateInfoId){
        return selectList(new LambdaQueryWrapperX<BatchTemplateVersion>()
                .eq(BatchTemplateVersion::getBatchTemplateInfoId, templateInfoId)
                .orderByDesc(BatchTemplateVersion::getCreateTime)
        );
    }

    /**
     * 根据版本信息id查询有效的批记录模板信息
     * @param templateInfoId
     * @param normal
     * @return
     */
    default BatchTemplateVersion selectEffectiveByVersionId(Long templateInfoId, Boolean normal){
        return selectOne(new LambdaQueryWrapperX<BatchTemplateVersion>()
                .eq(BatchTemplateVersion::getBatchTemplateInfoId, templateInfoId)
                .eq(BatchTemplateVersion::getNormal, normal)
                .orderByDesc(BatchTemplateVersion::getCreateTime)
                .last(" limit 1"));
    }

    /**
     * 查询默认模板版本
     * @param templateInfoIdList
     * @return
     */
    default List<BatchTemplateVersion> selectNormalByTemplateInfoIdList(List<Long> templateInfoIdList){
        return selectList(new LambdaQueryWrapperX<BatchTemplateVersion>()
                .in(BatchTemplateVersion::getBatchTemplateInfoId, templateInfoIdList)
                .eq(BatchTemplateVersion::getNormal, true));
    }

    default List<BatchTemplateVersion> selectByConfirmTemplateInfoId(Long templateInfoId){
        return selectList(new LambdaQueryWrapperX<BatchTemplateVersion>()
                .eq(BatchTemplateVersion::getBatchTemplateInfoId, templateInfoId)
                .eq(BatchTemplateVersion::getStatus, TemplateVersionStatusEnum.CONFIRM.getValue()));
    }
}

