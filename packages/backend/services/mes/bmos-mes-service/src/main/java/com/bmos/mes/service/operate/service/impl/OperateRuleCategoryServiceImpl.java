package com.bmos.mes.service.operate.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.bmos.adaptor.platform.PlatformApiAdaptor;
import com.bmos.common.exception.BmosException;
import com.bmos.common.tree.TreeUtil;
import com.bmos.common.util.collection.CollectionUtils;
import com.bmos.common.util.id.IdUtils;
import com.bmos.mes.common.enums.operate.OperateRuleVersionStateEnum;
import com.bmos.mes.common.exception.MesResponseCode;
import com.bmos.mes.service.operate.convert.OperateRuleCategoryConvert;
import com.bmos.mes.service.operate.dto.SaveCategoryDTO;
import com.bmos.mes.service.operate.dto.UpdateCategoryDTO;
import com.bmos.mes.service.operate.mapper.OperateRuleCategoryMapper;
import com.bmos.mes.service.operate.model.OperateRule;
import com.bmos.mes.service.operate.model.OperateRuleCategory;
import com.bmos.mes.service.operate.service.OperateRuleCategoryService;
import com.bmos.mes.service.operate.service.OperateRuleService;
import com.bmos.mes.service.operate.service.OperateRuleVersionService;
import com.bmos.mes.service.operate.vo.OperateRuleCategoryVO;
import com.bmos.mes.service.operate.vo.OperateRuleSopVO;
import com.bmos.mes.service.process.service.ProcedureStepModelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;
import java.util.Map;

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

    @Resource
    @Lazy
    private ProcedureStepModelService stepModelService;

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
        isNameRepetition(dto.getParentId(), dto.getName(),null);
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
        isNameRepetition(dto.getParentId(), dto.getName(),dto.getId());
        mapper.saveOrUpdateCategory(OperateRuleCategoryConvert.INSTANCE.convertToUpdate(dto));
    }

    @Override
    public void deleteCategory(Long id) {
        List<OperateRuleCategory> list = mapper.selectListByParentId(id);
        if (CollUtil.isNotEmpty(list)){
            throw new BmosException(MesResponseCode.CATEGORY_PARENT_ERROR);
        }
        List<OperateRule> operateRuleList = ruleService.getListByCategoryId(id);
        if (CollUtil.isNotEmpty(operateRuleList)){
            throw new BmosException(MesResponseCode.CATEGORY_OPERATE_ERROR);
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
           return voList;
        }
        List<OperateRule> rules = versionService.selectByListByState(OperateRuleVersionStateEnum.VALID.getCode(),deptIds);
        if (CollUtil.isNotEmpty(rules)){
            Map<Long, List<OperateRule>> ruleMap = CollectionUtils.convertMultiMap(rules, OperateRule::getCategoryId);
            handelSopTreeNode(voList,ruleMap);
        }
        return voList;
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



    private void isNameRepetition(Long parentId, String name,Long id) {
        List<OperateRuleCategory> list = mapper.selectListByParentId(parentId);
        List<OperateRuleCategory> nameList = CollectionUtils.filterList(list,
                item -> StrUtil.equals(item.getName(), name));
        if (CollUtil.isNotEmpty(nameList)) {
            if (ObjectUtil.isNotNull(id)){
                List<OperateRuleCategory> categories = CollectionUtils.filterList(nameList, category -> category.getId().equals(id));
                if (CollUtil.isEmpty(categories)){
                    throw new BmosException(MesResponseCode.CATEGORY_NAME_ERROR);
                }
            }else {
                throw new BmosException(MesResponseCode.CATEGORY_NAME_ERROR);
            }
        }
    }
}
