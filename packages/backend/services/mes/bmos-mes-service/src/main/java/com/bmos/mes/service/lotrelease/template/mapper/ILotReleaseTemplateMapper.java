package com.bmos.mes.service.lotrelease.template.mapper;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.bmos.mes.service.lotrelease.template.dto.LotReleaseTemplatePageQuery;
import com.bmos.mes.service.lotrelease.template.model.LotReleaseTemplate;
import com.bmos.mybatis.mapper.BaseMapperX;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author liang
 * @version 1.0.0
 * @date 2024/8/26 18:28
 */
@Mapper
public interface ILotReleaseTemplateMapper extends BaseMapperX<LotReleaseTemplate> {

    /**
     * 根据分类查询批签发模板列表
     *
     * @param lotReleaseTemplateCategoryId 批签发模板分类id
     * @return 批签发模板列表
     */
    default List<LotReleaseTemplate> listByCategoryId(Long lotReleaseTemplateCategoryId) {
        return selectList(new LambdaQueryWrapper<LotReleaseTemplate>()
                .eq(LotReleaseTemplate::getCategoryId, lotReleaseTemplateCategoryId)
        );
    }

    default LotReleaseTemplate selectByName(String name){
        if(StrUtil.isBlank(name)){
            return null;
        }
        return selectOne(new LambdaQueryWrapper<LotReleaseTemplate>()
                .eq(LotReleaseTemplate::getName, name)
        );
    }

    List<LotReleaseTemplate> queryPage(@Param("pageQuery") LotReleaseTemplatePageQuery pageQuery,
                                       @Param("categoryIds") List<Long> categoryIds,
                                       @Param("deptIds") List<Long> deptIds);

    List<Long> selectAuthByDeptIdList(@Param("deptIdList") List<Long> deptIdList);

    default void clearEffectiveByLotReleaseId(Long lotReleaseId){
        if (lotReleaseId == null){
            return;
        }
        update(null, new LambdaUpdateWrapper<LotReleaseTemplate>()
                .eq(LotReleaseTemplate::getEffectiveLotReleaseId, lotReleaseId)
                .set(LotReleaseTemplate::getEffectiveLotReleaseId, null)
        );
    }
}
