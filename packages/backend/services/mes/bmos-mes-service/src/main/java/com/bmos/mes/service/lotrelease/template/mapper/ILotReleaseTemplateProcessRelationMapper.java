package com.bmos.mes.service.lotrelease.template.mapper;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.bmos.mes.service.lotrelease.template.model.LotReleaseTemplateProcessRelation;
import com.bmos.mybatis.mapper.BaseMapperX;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author liang
 * @version 1.0.0
 * @date 2024/8/27 14:25
 */
@Mapper
public interface ILotReleaseTemplateProcessRelationMapper extends BaseMapperX<LotReleaseTemplateProcessRelation> {

    default void bindProcess(List<Long> processIds, Long templateId) {
        if (templateId == null) {
            return;
        }
        List<LotReleaseTemplateProcessRelation> relations = listByTemplateId(templateId);
        if (CollectionUtil.isNotEmpty(relations)) {
            deleteBatchIds(relations.stream().map(LotReleaseTemplateProcessRelation::getId).collect(Collectors.toList()));
        }
        if (CollectionUtil.isEmpty(processIds)) {
            return;
        }
        List<LotReleaseTemplateProcessRelation> relationList = new ArrayList<>();
        HashSet<Long> set = new HashSet<>(processIds);
        for (Long processId : set) {
            LotReleaseTemplateProcessRelation relation = new LotReleaseTemplateProcessRelation();
            relation.setProcessId(processId);
            relation.setLotReleaseTemplateId(templateId);
            relationList.add(relation);
        }
        insertBatch(relationList);
    }

    default List<LotReleaseTemplateProcessRelation> listByTemplateId(Long templateId) {
        if (templateId == null) {
            return new ArrayList<>();
        }
        return selectList(new LambdaQueryWrapper<LotReleaseTemplateProcessRelation>()
                .eq(LotReleaseTemplateProcessRelation::getLotReleaseTemplateId, templateId)
        );
    }

    List<Long> selectAllProcessIdsByTemplateId(@Param("templateId") Long templateId);

    default List<Long> selectTemplateIdsByProcessId(Long processId) {
        if (processId == null) {
            return new ArrayList<>();
        }
        return selectList(new LambdaQueryWrapper<LotReleaseTemplateProcessRelation>()
                .eq(LotReleaseTemplateProcessRelation::getProcessId, processId)
        ).stream()
                .map(LotReleaseTemplateProcessRelation::getLotReleaseTemplateId)
                .collect(Collectors.toList());
    }

    default List<Long> selectProcessIdsByTemplateId(Long templateId){
        if (templateId == null) {
            return new ArrayList<>();
        }
        return selectList(new LambdaQueryWrapper<LotReleaseTemplateProcessRelation>()
                .eq(LotReleaseTemplateProcessRelation::getLotReleaseTemplateId, templateId)
        ).stream()
                .map(LotReleaseTemplateProcessRelation::getProcessId)
                .collect(Collectors.toList());
    }
}
