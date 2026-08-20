package com.bmos.mes.service.plan.document.mapper;

import cn.hutool.core.util.StrUtil;
import com.bmos.mes.common.enums.plan.BatchRecordArchiveStatusEnum;
import com.bmos.mes.service.plan.document.controller.vo.RecordArchiveTemplateVersionVO;
import com.bmos.mes.service.plan.document.mapper.param.ArchiveParam;
import com.bmos.mes.service.plan.document.model.BatchRecordArchive;
import com.bmos.mes.service.plan.document.service.dto.BatchRecordArchiveQueryDTO;
import com.bmos.mes.service.plan.document.service.dto.RecordArchivePageDTO;
import com.bmos.mybatis.mapper.BaseMapperX;
import com.bmos.mybatis.query.LambdaQueryWrapperX;
import org.apache.ibatis.annotations.Mapper;

import java.util.Collection;
import java.util.List;
import java.util.Objects;

/**
 * 归档生成的批记录档案(BmBatchRecordArchive)表数据库访问层
 *
 * @author makejava
 * @since 2024-08-21 11:28:02
 */
@Mapper
public interface BatchRecordArchiveMapper extends BaseMapperX<BatchRecordArchive> {

    default List<BatchRecordArchive> selectByPlanIdAndTemplateId(Long planId, Long templateInfoId, Integer status){
        LambdaQueryWrapperX<BatchRecordArchive> wrapperX = new LambdaQueryWrapperX<BatchRecordArchive>()
                .eq(BatchRecordArchive::getPlanId, planId)
                .eq(BatchRecordArchive::getBatchTemplateInfoId, templateInfoId);
        if (Objects.nonNull(status)){
            wrapperX.eq(BatchRecordArchive::getStatus, status);
        }
        return selectList(wrapperX);
    }

    default List<BatchRecordArchive> selectByInstanceIdList(List<String> processInstanceIds){
        return selectList(new LambdaQueryWrapperX<BatchRecordArchive>()
                .in(BatchRecordArchive::getInstanceId, processInstanceIds));
    }

    default List<BatchRecordArchive> selectByParam(ArchiveParam param){
        LambdaQueryWrapperX<BatchRecordArchive> wrapperX = new LambdaQueryWrapperX<BatchRecordArchive>();
        if (StrUtil.isNotEmpty(param.getBatchNo())){
            wrapperX.likeIfPresent(BatchRecordArchive::getBatchNo, param.getBatchNo());
        }
        if (StrUtil.isNotEmpty(param.getTemplateName())){
            wrapperX.likeIfPresent(BatchRecordArchive::getTemplateName, param.getTemplateName());
        }
        if (StrUtil.isNotEmpty(param.getProductName())){
            wrapperX.likeIfPresent(BatchRecordArchive::getProductName, param.getProductName());
        }
        if (param.getStatus() != null){
            wrapperX.eq(BatchRecordArchive::getStatus, param.getStatus());
        }
        return selectList(wrapperX);
    }

    default List<BatchRecordArchive> selectPlanArchiveRecord(RecordArchivePageDTO dto, List<Long> templateVersionIdList){
        return selectList(new LambdaQueryWrapperX<BatchRecordArchive>()
                .eq(BatchRecordArchive::getPlanId, dto.getPlanId())
                .in(BatchRecordArchive::getBatchTemplateVersionId, templateVersionIdList)
                .orderByDesc(StrUtil.isEmpty(dto.getOrderBy()), BatchRecordArchive::getArchiveTime));
    }

    default List<BatchRecordArchive> selectEffectiveByTemplateInfoAndPlanId(List<Long> templateInfoIds, Long planId){
        return selectList(new LambdaQueryWrapperX<BatchRecordArchive>()
                .in(BatchRecordArchive::getBatchTemplateInfoId, templateInfoIds)
                .eq(BatchRecordArchive::getPlanId, planId)
                .eq(BatchRecordArchive::getStatus, BatchRecordArchiveStatusEnum.EFFECTIVE.getValue()));
    }

    default List<BatchRecordArchive> selectByTemplateInfoIdList(Collection<Long> templateInfoIdList){
        return selectList(new LambdaQueryWrapperX<BatchRecordArchive>()
                .in(BatchRecordArchive::getBatchTemplateInfoId, templateInfoIdList)
                .isNotNull(BatchRecordArchive::getInstanceId));
    }

    List<RecordArchiveTemplateVersionVO> queryBatchArchivePage(BatchRecordArchiveQueryDTO archiveQueryDTO);

}

