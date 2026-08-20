package com.bmos.mes.service.lotrelease.manage.mapper;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.bmos.mes.service.lotrelease.manage.dto.LotReleasePageQuery;
import com.bmos.mes.service.lotrelease.manage.dto.LotReleaseVersionPageQuery;
import com.bmos.mes.service.lotrelease.manage.enums.LotReleaseStatus;
import com.bmos.mes.service.lotrelease.manage.model.LotRelease;
import com.bmos.mes.service.lotrelease.manage.vo.LotReleaseGeneratePreviewVO;
import com.bmos.mes.service.lotrelease.manage.vo.LotReleasePageVO;
import com.bmos.mes.service.lotrelease.manage.vo.LotReleaseVersionPageVO;
import com.bmos.mybatis.mapper.BaseMapperX;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author liang
 * @version 1.0.0
 * @date 2024/8/27 18:43
 */
@Mapper
public interface ILotReleaseMapper extends BaseMapperX<LotRelease> {


    List<LotReleasePageVO> selectTemplateIdsByProcessId(@Param("pageQuery") LotReleasePageQuery pageQuery, @Param("processId") Long processId, @Param("planId") Long planId);

    List<LotReleaseVersionPageVO> selectListByTemplateId(@Param("pageQuery") LotReleaseVersionPageQuery pageQuery);

    default List<LotRelease> selectListWithCondition(Long productId, String templateName, String batchNo){
        return selectList(new LambdaQueryWrapper<LotRelease>()
                .eq(productId != null, LotRelease::getProductId, productId)
                .like(StrUtil.isNotBlank(templateName), LotRelease::getName, templateName)
                .like(StrUtil.isNotBlank(batchNo), LotRelease::getBatchNo, batchNo)
        );
    }

    default List<LotRelease> selectAuditList(List<Long> ids){
        return selectList(new LambdaQueryWrapper<LotRelease>()
                .in(LotRelease::getId, ids)
                .orderByDesc(LotRelease::getSubmitterTime)
        );
    }

    List<LotReleaseGeneratePreviewVO> queryGeneratePreviewListByProcessId(@Param("processId") Long processId);

    default LotRelease selectEditByTemplateIdAndVersion(Long lotReleaseTemplateId, String lotReleaseVersion, Long planId){
        return selectOne(new LambdaQueryWrapper<LotRelease>()
                .eq(LotRelease::getTemplateId, lotReleaseTemplateId)
                .eq(LotRelease::getTemplateVersion, lotReleaseVersion)
                .eq(LotRelease::getPlanId, planId)
                .eq(LotRelease::getStatus, LotReleaseStatus.EDIT)
        );
    }

    default LotRelease selectByUrl(String url){
        return selectOne(new LambdaQueryWrapper<LotRelease>()
                .eq(LotRelease::getFileUrl, url)
                .last("limit 1")
        );
    }

    default void updateByTemplateIdAndVersion(Long templateId, Long planId, LotReleaseStatus status){
        update(null, new LambdaUpdateWrapper<LotRelease>()
                .eq(LotRelease::getTemplateId, templateId)
                .eq(LotRelease::getPlanId, planId)
                .eq(LotRelease::getStatus, LotReleaseStatus.EFFECTIVE)
                .set(LotRelease::getStatus, status)
        );
    }

    default List<LotRelease> selectByTemplateIdList(List<Long> templateIdList){
        return selectList(new LambdaQueryWrapper<LotRelease>()
                .in(LotRelease::getTemplateId, templateIdList)
        );
    }
}
