package com.bmos.mes.service.lotrelease.template.convert;

import com.bmos.mes.service.lotrelease.template.model.LotReleaseTemplate;
import com.bmos.mes.service.lotrelease.template.model.LotReleaseTemplateCategory;
import com.bmos.mes.service.lotrelease.template.model.LotReleaseTemplateVersion;
import com.bmos.mes.service.lotrelease.template.vo.LotReleaseTemplateCategoryVO;
import com.bmos.mes.service.lotrelease.template.vo.LotReleaseTemplatePageVO;
import com.bmos.mes.service.lotrelease.template.vo.LotReleaseTemplateVersionPageVO;
import com.bmos.mybatis.page.CommonPage;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * @author liang
 * @version 1.0.0
 * @date 2024/8/26 18:44
 */
@Mapper
public interface LotReleaseTemplateCategoryConvert {

    LotReleaseTemplateCategoryConvert INSTANCE = Mappers.getMapper(LotReleaseTemplateCategoryConvert.class);

    CommonPage<LotReleaseTemplatePageVO> convertToVO(CommonPage<LotReleaseTemplate> page);

    List<LotReleaseTemplateCategoryVO> convertToVO(List<LotReleaseTemplateCategory> lotReleaseTemplateCategories);

    CommonPage<LotReleaseTemplateVersionPageVO> convertToPageVO(CommonPage<LotReleaseTemplateVersion> lotReleaseTemplateVersions);

    @Mapping(target = "templateName", source = "name")
    LotReleaseTemplateVersionPageVO convertToPageVO(LotReleaseTemplateVersion lotReleaseTemplateVersion);
}
