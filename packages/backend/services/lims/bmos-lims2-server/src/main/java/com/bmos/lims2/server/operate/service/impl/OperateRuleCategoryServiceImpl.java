package com.bmos.lims2.server.operate.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.bmos.adaptor.platform.PlatformApiAdaptor;
import com.bmos.common.exception.BmosException;
import com.bmos.common.tree.TreeUtil;
import com.bmos.common.util.collection.CollectionUtils;
import com.bmos.common.util.id.IdUtils;
import com.bmos.lims2.common.i18n.LimsResponseCode;
import com.bmos.lims2.server.operate.convert.OperateRuleCategoryConvert;
import com.bmos.lims2.server.operate.dto.SaveCategoryDTO;
import com.bmos.lims2.server.operate.dto.UpdateCategoryDTO;
import com.bmos.lims2.server.operate.enums.OperateRuleVersionStateEnum;
import com.bmos.lims2.server.operate.mapper.OperateRuleCategoryMapper;
import com.bmos.lims2.server.operate.model.OperateRule;
import com.bmos.lims2.server.operate.model.OperateRuleCategory;
import com.bmos.lims2.server.operate.service.OperateRuleCategoryService;
import com.bmos.lims2.server.operate.service.OperateRuleService;
import com.bmos.lims2.server.operate.service.OperateRuleVersionService;
import com.bmos.lims2.server.operate.vo.OperateRuleCategoryVO;
import com.bmos.lims2.server.operate.vo.OperateRuleSopVO;
import com.bmos.mybatis.query.LambdaQueryWrapperX;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author renjinguang
 */
@Service
public class OperateRuleCategoryServiceImpl implements OperateRuleCategoryService {

    @Autowired
    private OperateRuleCategoryMapper mapper;

    @Autowired
    private OperateRuleService ruleService;

    @Autowired
    private OperateRuleVersionService versionService;

    @Autowired
    private PlatformApiAdaptor platformApiAdaptor;


    @Override
    public List<OperateRuleCategoryVO> getListCategory() {
        List<OperateRuleCategory> listCategory = mapper.getListCategory();
        if (CollUtil.isEmpty(listCategory)) {
            return Collections.emptyList();
        }
        return TreeUtil.buildTree(OperateRuleCategoryConvert.INSTANCE.convertToVoList(listCategory), false);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveCategory(SaveCategoryDTO dto) {
        List<OperateRuleCategory> list = mapper.getListCategory();
        isNameRepetition(dto.getName(), null);
        OperateRuleCategory category = OperateRuleCategoryConvert.INSTANCE.convertToSave(dto);
        Long categoryId = IdUtils.getSnowflake();
        category.setId(categoryId);
        category.setCode(dto.getParentId() == 0 ? String.valueOf(categoryId) : list.stream().filter(item ->
                item.getId().equals(dto.getParentId())).findFirst().get().getCode() + StrUtil.COMMA + categoryId);
        mapper.saveOrUpdateCategory(category);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateCategory(UpdateCategoryDTO dto) {
        isNameRepetition(dto.getName(), dto.getId());
        mapper.saveOrUpdateCategory(OperateRuleCategoryConvert.INSTANCE.convertToUpdate(dto));
    }

    @Override
    public void deleteCategory(Long id) {
        List<OperateRuleCategory> list = mapper.selectListByParentId(id);
        if (CollUtil.isNotEmpty(list)){
            throw new BmosException(LimsResponseCode.CATEGORY_PARENT_ERROR);
        }
        List<OperateRule> operateRuleList = ruleService.getListByCategoryId(id);
        if (CollUtil.isNotEmpty(operateRuleList)){
            throw new BmosException(LimsResponseCode.CATEGORY_OPERATE_ERROR);
        }
        mapper.deleteCategory(id);
    }

    @Override
    public List<OperateRuleSopVO> listSop() {
        List<OperateRuleCategoryVO> listCategory = getListCategory();
        if (CollUtil.isEmpty(listCategory)){
            return Collections.emptyList();
        }

        List<OperateRuleSopVO> voList = BeanUtil.copyToList(listCategory, OperateRuleSopVO.class);
        List<Long> deptIds = platformApiAdaptor.deptIds();
        if (CollUtil.isEmpty(deptIds)) {
            return Collections.emptyList();
        }
        List<OperateRule> rules = versionService.selectByListByState(OperateRuleVersionStateEnum.VALID.getCode(),deptIds);
        if (CollUtil.isEmpty(rules)){
            return Collections.emptyList();
        }
        Map<Long, List<OperateRule>> ruleMap = CollectionUtils.convertMultiMap(rules, OperateRule::getCategoryId);
        handelSopTreeNode(voList,ruleMap);
        return removeEmptyCategoryNodes(voList);
    }

    public void handelSopTreeNode(List<OperateRuleSopVO> voList,Map<Long, List<OperateRule>> ruleMap) {
        voList.forEach(item -> {
            List<OperateRule> versions = ruleMap.get(item.getId());
            if (CollUtil.isNotEmpty(versions)) {
                List<OperateRuleSopVO> children = item.getChildren();
                versions.forEach(info -> {
                    OperateRuleSopVO sopVO = BeanUtil.toBean(info, OperateRuleSopVO.class);
                    sopVO.setName(info.getCode() + StrUtil.DASHED +info.getName());
                    sopVO.setFlag(true);
                    children.add(sopVO);
                });
            }
            if (CollUtil.isNotEmpty(item.getChildren())) {
                handelSopTreeNode(item.getChildren(),ruleMap);
            }
        });
    }

    private List<OperateRuleSopVO> removeEmptyCategoryNodes(List<OperateRuleSopVO> voList) {
        if (CollUtil.isEmpty(voList)) {
            return Collections.emptyList();
        }
        return voList.stream()
                .map(node -> {
                    List<OperateRuleSopVO> children = removeEmptyCategoryNodes(node.getChildren());
                    node.setChildren(children);
                    return node;
                })
                .filter(node -> Boolean.TRUE.equals(node.getFlag()) || CollUtil.isNotEmpty(node.getChildren()))
                .collect(Collectors.toList());
    }



    private void isNameRepetition(String name, Long id) {
        List<OperateRuleCategory> nameList = mapper.selectList(new LambdaQueryWrapperX<OperateRuleCategory>()
                .eq(OperateRuleCategory::getName, name));
        if (CollUtil.isEmpty(nameList)) {
            return;
        }
        if (ObjectUtil.isNull(id)) {
            throw new BmosException(LimsResponseCode.CATEGORY_NAME_ERROR);
        }
        boolean sameRecordExist = nameList.stream().anyMatch(category -> category.getId().equals(id));
        if (!sameRecordExist) {
            throw new BmosException(LimsResponseCode.CATEGORY_NAME_ERROR);
        }
    }
}
