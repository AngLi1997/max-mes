package com.bmos.lims2.server.operate.mapper;

import com.baomidou.mybatisplus.extension.toolkit.Db;
import com.bmos.lims2.server.operate.model.OperateRuleCategory;
import com.bmos.mybatis.mapper.BaseMapperX;
import com.bmos.mybatis.query.LambdaQueryWrapperX;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;


/**
 * @author renjinguang
 */
@Mapper
public interface OperateRuleCategoryMapper extends BaseMapperX<OperateRuleCategory> {


    default List<OperateRuleCategory> getListCategory() {
        return selectList(new LambdaQueryWrapperX<>());
    }

    default List<OperateRuleCategory> selectListByParentId(Long parentId) {
        return selectList(new LambdaQueryWrapperX<OperateRuleCategory>()
                .eq(OperateRuleCategory::getParentId, parentId));
    }

    default Boolean saveOrUpdateCategory(OperateRuleCategory category){
        return Db.saveOrUpdate(category);
    }

    default Boolean deleteCategory(Long id){
        return Db.removeById(id,OperateRuleCategory.class);
    }
}
