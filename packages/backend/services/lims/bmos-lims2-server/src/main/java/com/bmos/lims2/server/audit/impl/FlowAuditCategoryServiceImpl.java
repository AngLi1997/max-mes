package com.bmos.lims2.server.audit.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.bmos.common.util.collection.CollectionUtils;
import com.bmos.common.util.i18n.I18nUtils;
import com.bmos.lims2.common.constants.BasicConstants;
import com.bmos.lims2.server.audit.FlowAuditCategoryService;
import com.bmos.lims2.server.audit.FlowAuditConvert;
import com.bmos.lims2.server.audit.convert.FlowAuditCategoryConvert;
import com.bmos.lims2.server.audit.entity.FlowAudit;
import com.bmos.lims2.server.audit.entity.FlowAuditCategory;
import com.bmos.lims2.server.audit.mapper.FlowAuditCategoryMapper;
import com.bmos.lims2.server.audit.mapper.FlowAuditMapper;
import com.bmos.lims2.server.audit.vo.FlowAuditCategoryVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author renjinguang
 */
@Service
public class FlowAuditCategoryServiceImpl implements FlowAuditCategoryService {

    @Autowired
    private FlowAuditCategoryMapper categoryMapper;

    @Autowired
    private FlowAuditMapper auditMapper;

    @Override
    public List<FlowAuditCategoryVO> listFlowAuditCategory() {
        List<FlowAuditCategoryVO> list = FlowAuditCategoryConvert.INSTANCE.convertToCategory(categoryMapper.selectCategoryList());
        if (CollUtil.isEmpty(list)) {
            return list;
        }
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        assert attributes != null;
        HttpServletRequest request = attributes.getRequest();
        List<FlowAuditCategoryVO> newList = list.stream().peek(node -> node.setName(I18nUtils.getMenuMessage(node.getCode(), node.getName(), null, request)))
                .filter(parentNode -> parentNode.getParentId().equals(BasicConstants.PARENT_ID))
                .map(node -> getChildren(node, list)).collect(Collectors.toList());
        return newList;
    }

    @Override
    public List<FlowAuditCategoryVO> flowAuditHistoryCategory() {
        List<FlowAudit> flowAuditList = auditMapper.selectList()
                .stream().sorted(Comparator.comparing(FlowAudit::getCreateTime).reversed())
                .collect(Collectors.toList());
        List<FlowAuditCategoryVO> list = listFlowAuditCategory();

        if (CollUtil.isEmpty(flowAuditList)) {
            return list;
        }
        Map<String, List<FlowAudit>> auditMap = CollectionUtils.convertMultiMap(flowAuditList, FlowAudit::getCategoryCode);
        list.forEach(item -> {
            if (CollUtil.isNotEmpty(item.getItemList())) {
                item.getItemList().forEach(categoryList -> {
                    categoryList.setItemList(FlowAuditConvert.INSTANCE.convertToModelVo(auditMap.get(categoryList.getCode())));
                });
            }
        });
        return list;
    }

    @Override
    public List<String> queryByCode(String categoryCode) {
        FlowAuditCategory category = categoryMapper.queryByCode(categoryCode);
        if (ObjectUtil.isEmpty(category)){
            return Collections.emptyList();
        }
        return StrUtil.split(category.getTreeCode(),StrUtil.COMMA);
    }

    public FlowAuditCategoryVO getChildren(FlowAuditCategoryVO vo, List<FlowAuditCategoryVO> allList) {
        List<FlowAuditCategoryVO> treeList = allList.stream()
                .filter(subNode -> vo.getId().equals(subNode.getParentId()))
                .map(subNode -> getChildren(subNode, allList)).collect(Collectors.toList());
        vo.setItemList(treeList);
        return vo;
    }
}
