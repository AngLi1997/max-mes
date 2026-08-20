package com.bmos.mes.service.lotrelease.template.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.bmos.mes.service.lotrelease.template.model.LotReleaseTemplateHistory;
import com.bmos.mybatis.mapper.BaseMapperX;
import org.apache.ibatis.annotations.Mapper;

import java.util.ArrayList;
import java.util.List;

/**
 * @author liang
 * @version 1.0.0
 * @date 2024/8/27 15:07
 */
@Mapper
public interface ILotReleaseTemplateHistoryMapper extends BaseMapperX<LotReleaseTemplateHistory> {
    default List<LotReleaseTemplateHistory> queryHistoryByTemplateVersionId(Long templateVersionId){
        if (templateVersionId == null){
            return new ArrayList<>();
        }
        return selectList(new LambdaQueryWrapper<LotReleaseTemplateHistory>()
                .eq(LotReleaseTemplateHistory::getTemplateVersionId, templateVersionId)
                .orderByDesc(LotReleaseTemplateHistory::getCreateTime)
        );
    }
}
