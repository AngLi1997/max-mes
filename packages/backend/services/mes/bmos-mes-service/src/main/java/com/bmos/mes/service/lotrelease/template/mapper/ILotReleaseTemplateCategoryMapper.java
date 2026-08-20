package com.bmos.mes.service.lotrelease.template.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.bmos.mes.service.lotrelease.template.model.LotReleaseTemplateCategory;
import com.bmos.mes.service.lotrelease.template.vo.LotReleaseTemplateCategoryPath;
import com.bmos.mybatis.mapper.BaseMapperX;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.ArrayList;
import java.util.List;

/**
 * @author liang
 * @version 1.0.0
 * @date 2024/8/26 18:28
 */
@Mapper
public interface ILotReleaseTemplateCategoryMapper extends BaseMapperX<LotReleaseTemplateCategory> {

    default List<LotReleaseTemplateCategory> listByNameAndParentId(Long parentId, String name){
        if (StringUtils.isBlank(name)){
            return new ArrayList<>();
        }
        return selectList(new LambdaQueryWrapper<LotReleaseTemplateCategory>()
                .isNull(parentId == null, LotReleaseTemplateCategory::getParentId)
                .eq(parentId != null, LotReleaseTemplateCategory::getParentId, parentId)
                .eq(LotReleaseTemplateCategory::getName, name)
        );
    }

    default List<LotReleaseTemplateCategory> listByParentId(Long parentId) {
        return selectList(new LambdaQueryWrapper<LotReleaseTemplateCategory>()
                .isNull(parentId == null, LotReleaseTemplateCategory::getParentId)
                .eq(parentId != null, LotReleaseTemplateCategory::getParentId, parentId)
        );
    }

    List<LotReleaseTemplateCategory> listAllChildren(@Param("parentId") Long parentId);

    List<LotReleaseTemplateCategoryPath> getNamePath(@Param("ids") List<Long> ids);
}
