package com.bmos.platform.service.system.expression.service.impl;

import cn.hutool.core.util.StrUtil;
import com.bmos.common.exception.BmosException;
import com.bmos.common.holder.SysUserHolder;
import com.bmos.common.tree.TreeUtil;
import com.bmos.common.util.id.IdUtils;
import com.bmos.platform.common.exception.PlatformResponseCode;
import com.bmos.platform.service.system.expression.constant.ExpressCategoryConstant;
import com.bmos.platform.service.system.expression.convert.ExpressionConverter;
import com.bmos.platform.service.system.expression.dto.ExpressionCategorySaveDTO;
import com.bmos.platform.service.system.expression.dto.ExpressionCategoryUpdateDTO;
import com.bmos.platform.service.system.expression.mapper.ExpressionCategoryMapper;
import com.bmos.platform.service.system.expression.model.ExpressionCategory;
import com.bmos.platform.service.system.expression.service.ExpressionCategoryService;
import com.bmos.platform.service.system.expression.vo.ExpressionCategoryTreeNodeVO;
import com.bmos.platform.service.system.expression.vo.ExpressionTreeNodeVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
public class ExpressionCategoryServiceImpl implements ExpressionCategoryService {
    @Autowired
    private ExpressionCategoryMapper expressionCategoryMapper;

    @Override
    public List<ExpressionCategoryTreeNodeVO> getCategoryTree() {
        List<ExpressionCategory> categories = expressionCategoryMapper.selectList();
        List<ExpressionCategoryTreeNodeVO> nodes = ExpressionConverter.INSTANCE.convertCategoryTreeNode(categories);
        return TreeUtil.buildTree(nodes, false);
    }

    @Override
    public List<ExpressionCategory> selectChildrenById(Long id) {
        return expressionCategoryMapper.selectChildrenById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void save(ExpressionCategorySaveDTO dto) {
        if (expressionCategoryMapper.existsNameInParentId(null, dto.getParentId(), dto.getName())) {
            throw new BmosException(PlatformResponseCode.EXPRESSION_CATEGORY_CODE_EXIST);
        }
        existsCategory(dto.getParentId());
        ExpressionCategory expressionCategory = ExpressionConverter.INSTANCE.convetDO(dto);
        expressionCategory.setId(IdUtils.getSnowflake());
        expressionCategory.setAncestors(getAncestors(dto.getParentId()));
        expressionCategory.setAncestorName(getAncestorName(dto.getParentId(), dto.getName()));
        expressionCategoryMapper.insert(expressionCategory);
    }

    private String getAncestorName(Long parentId, String name) {
        String newAncestorName;
        if (Objects.isNull(parentId) || ExpressCategoryConstant.EXPRESS_CATEGORY_ROOT.equals(parentId)) {
            newAncestorName = name;
        } else {
            ExpressionCategory parentCategory = expressionCategoryMapper.selectById(parentId);
            newAncestorName = parentCategory.getAncestorName() + StrUtil.C_SLASH + name;
        }
        return newAncestorName;
    }

    private String getAncestors(Long parentId) {
        String newAncestors;
        if (Objects.isNull(parentId) || ExpressCategoryConstant.EXPRESS_CATEGORY_ROOT.equals(parentId)) {
            newAncestors = ExpressCategoryConstant.EXPRESS_CATEGORY_ROOT.toString();
        } else {
            ExpressionCategory parentCategory = expressionCategoryMapper.selectById(parentId);
            newAncestors = parentCategory.getAncestors() + StrUtil.COMMA + parentId;
        }
        return newAncestors;
    }

    @Override
    public void existsCategory(Long id) {
        if (Objects.isNull(id)) {
            return;
        }
        if (!expressionCategoryMapper.existsId(id)) {
            throw new BmosException(PlatformResponseCode.EXPRESSION_CATEGORY_ID_NOT_EXIST);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(ExpressionCategoryUpdateDTO dto) {
        if (expressionCategoryMapper.existsNameInParentId(dto.getId(), dto.getParentId(), dto.getName())) {
            throw new BmosException(PlatformResponseCode.EXPRESSION_CATEGORY_CODE_EXIST);
        }
        existsCategory(dto.getParentId());
        expressionCategoryMapper.updateExpressionCategory(ExpressionConverter.INSTANCE.convetDO(dto));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        if (expressionCategoryMapper.existsRelationParentId(id)) {
            throw new BmosException(PlatformResponseCode.CATEGORY_EXISTS_CHILD_NODE);
        }
        expressionCategoryMapper.delete(id, SysUserHolder.getUser().getUserId());
    }

    @Override
    public List<ExpressionTreeNodeVO> getFullTreeNodeVOList() {
        List<ExpressionTreeNodeVO> list = expressionCategoryMapper.selectFullTreeNodeVOList();
        list.forEach(category->category.setCategoryFlag(true));
        return list;
    }
}
