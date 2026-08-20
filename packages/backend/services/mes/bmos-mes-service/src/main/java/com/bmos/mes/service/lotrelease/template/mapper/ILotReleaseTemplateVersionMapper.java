package com.bmos.mes.service.lotrelease.template.mapper;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.bmos.mes.service.lotrelease.template.dto.LotReleaseTemplateVersionPageQuery;
import com.bmos.mes.service.lotrelease.template.enums.LotReleaseTemplateVersionStatus;
import com.bmos.mes.service.lotrelease.template.model.LotReleaseTemplateVersion;
import com.bmos.mybatis.mapper.BaseMapperX;
import org.apache.ibatis.annotations.Mapper;

import java.util.ArrayList;
import java.util.List;

/**
 * @author liang
 * @version 1.0.0
 * @date 2024/8/26 18:28
 */
@Mapper
public interface ILotReleaseTemplateVersionMapper extends BaseMapperX<LotReleaseTemplateVersion> {

    default LotReleaseTemplateVersion selectByTemplateIdAndVersion(Long templateId, String version) {
        return selectOne(new LambdaQueryWrapper<LotReleaseTemplateVersion>()
                .eq(LotReleaseTemplateVersion::getTemplateId, templateId)
                .eq(LotReleaseTemplateVersion::getVersion, version)
        );
    }

    default LotReleaseTemplateVersion selectDefaultByTemplateId(Long templateId){
        if (templateId == null){
            return null;
        }
        return selectOne(new LambdaQueryWrapper<LotReleaseTemplateVersion>()
                .eq(LotReleaseTemplateVersion::getTemplateId, templateId)
                .eq(LotReleaseTemplateVersion::getIsDefault, true)
        );
    }

    default List<LotReleaseTemplateVersion> queryVersionPage(LotReleaseTemplateVersionPageQuery pageQuery){
        return selectList(new LambdaQueryWrapper<LotReleaseTemplateVersion>()
                .eq(LotReleaseTemplateVersion::getTemplateId, pageQuery.getLogReleaseTemplateId())
                .orderByDesc(LotReleaseTemplateVersion::getCreateTime)
        );
    }

    default List<LotReleaseTemplateVersion> selectByTemplateIds(List<Long> templateIds){
        if (CollectionUtil.isEmpty(templateIds)){
            return new ArrayList<>();
        }
        return selectList(new LambdaQueryWrapper<LotReleaseTemplateVersion>()
                .in(LotReleaseTemplateVersion::getTemplateId, templateIds)
                .eq(LotReleaseTemplateVersion::getStatus, LotReleaseTemplateVersionStatus.MAKE_SURE)
        );
    }

    default List<LotReleaseTemplateVersion> selectByTemplateId(Long templateId){
        return selectList(new LambdaQueryWrapper<LotReleaseTemplateVersion>()
                .eq(LotReleaseTemplateVersion::getTemplateId, templateId)
                .eq(LotReleaseTemplateVersion::getStatus, LotReleaseTemplateVersionStatus.MAKE_SURE)
        );
    }
}
