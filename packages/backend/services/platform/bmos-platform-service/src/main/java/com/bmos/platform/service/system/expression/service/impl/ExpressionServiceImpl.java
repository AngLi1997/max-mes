package com.bmos.platform.service.system.expression.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.BooleanUtil;
import cn.hutool.core.util.StrUtil;
import com.bmos.common.exception.BmosException;
import com.bmos.common.holder.SysUserHolder;
import com.bmos.common.response.ResponseInfo;
import com.bmos.common.tree.TreeUtil;
import com.bmos.common.util.collection.CollectionUtils;
import com.bmos.expression.bmos.ExpressionCalculator;
import com.bmos.expression.enums.RoundingEnum;
import com.bmos.platform.common.enums.expression.ExpressionStatusEnum;
import com.bmos.platform.common.exception.PlatformResponseCode;
import com.bmos.platform.service.feign.FeignUtils;
import com.bmos.platform.service.feign.MesFeignClient;
import com.bmos.platform.service.system.expression.convert.ExpressionConverter;
import com.bmos.platform.service.system.expression.dto.*;
import com.bmos.platform.service.system.expression.mapper.ExpressionMapper;
import com.bmos.platform.service.system.expression.model.Expression;
import com.bmos.platform.service.system.expression.model.ExpressionCategory;
import com.bmos.platform.service.system.expression.service.ExpressionCategoryService;
import com.bmos.platform.service.system.expression.service.ExpressionService;
import com.bmos.platform.service.system.expression.vo.ExpressionPageVO;
import com.bmos.platform.service.system.expression.vo.ExpressionTreeNodeVO;
import com.bmos.platform.service.system.expression.vo.MesRecordTreeNodeVO;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ExpressionServiceImpl implements ExpressionService {

    @Autowired
    private ExpressionCategoryService expressionCategoryService;

    @Autowired
    private ExpressionMapper expressionMapper;

    @Autowired
    private ExpressionCalculator expressionCalculator;

    @Resource
    private MesFeignClient mesFeignClient;

    @Override
    public List<ExpressionPageVO> page(ExpressionPageDTO dto) {
        if (Objects.nonNull(dto.getExpressionCategoryId())) {
            List<Long> expressionCategoryIds = expressionCategoryService.selectChildrenById(dto.getExpressionCategoryId())
                    .stream()
                    .map(ExpressionCategory::getId)
                    .collect(Collectors.toList());
            expressionCategoryIds.add(dto.getExpressionCategoryId());
            dto.setExpressionCategoryIds(expressionCategoryIds);
        }
        return expressionMapper.page(dto);
    }

    @Override
    public List<ExpressionPageVO> list() {
        return expressionMapper.list();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void save(ExpressionSaveDTO dto) {
        if (expressionMapper.existsName(null, dto.getName())) {
            throw new BmosException(PlatformResponseCode.EXPRESSION_NAME_EXIST);
        }
        expressionCategoryService.existsCategory(dto.getExpressionCategoryId());
        Expression expression = ExpressionConverter.INSTANCE.convetDO(dto);
        expression.setConfirmStatus(ExpressionStatusEnum.EDIT.getValue());
        expressionMapper.insert(expression);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(ExpressionUpdateDTO dto) {
        if (expressionMapper.existsName(dto.getId(), dto.getName())) {
            throw new BmosException(PlatformResponseCode.EXPRESSION_NAME_EXIST);
        }
        Expression expression = expressionMapper.selectById(dto.getId());
        if (Objects.isNull(expression)) {
            throw new BmosException(PlatformResponseCode.EXPRESSION_UPDATE_NOT_EXIST);
        }
        if (Objects.equals(ExpressionStatusEnum.CONFIRMED.getValue(), expression.getConfirmStatus())) {
            throw new BmosException(PlatformResponseCode.EXPRESSION_CONFIRM_STATUS_VALIDATED);
        }
        Expression update = ExpressionConverter.INSTANCE.convetDO(dto);
        update.setConfirmStatus(ExpressionStatusEnum.EDIT.getValue());
        expressionMapper.updateById(update);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void confirm(Long id) {
        Expression expression = expressionMapper.selectById(id);
        if (Objects.isNull(expression)) {
            throw new BmosException(PlatformResponseCode.EXPRESSION_UPDATE_NOT_EXIST);
        }
        if (Objects.equals(ExpressionStatusEnum.CONFIRMED.getValue(), expression.getConfirmStatus())) {
            throw new BmosException(PlatformResponseCode.EXPRESSION_CONFIRM_STATUS_VALIDATED);
        }
        if (!Objects.equals(ExpressionStatusEnum.VERIFIED.getValue(), expression.getConfirmStatus())) {
            throw new BmosException(PlatformResponseCode.EXPRESSION_NOT_VERIFIED);
        }
        expressionMapper.confirm(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        Expression expression = expressionMapper.selectById(id);
        if (expression == null) {
            throw new BmosException(PlatformResponseCode.EXPRESSION_UPDATE_NOT_EXIST);
        }
        if (Objects.equals(expression.getConfirmStatus(), ExpressionStatusEnum.CONFIRMED.getValue())) {
            throw new BmosException(PlatformResponseCode.EXPRESSION_CONFIRM_STATUS_VALIDATED);
        }
        expressionMapper.delete(id, SysUserHolder.getUser().getUserId());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteCategory(Long id) {
        if (expressionMapper.existsCategoryId(id)) {
            throw new BmosException(PlatformResponseCode.EXPRESSION_EXISTS_CATEGORY_NOT_DELETE);
        }
        expressionCategoryService.delete(id);
    }

    @Override
    public Set<String> parse(String expression) {
        return expressionCalculator.parseParams(expression);
    }

    @Override
    public List<ExpressionTreeNodeVO> getFullExpressionAndCategoryList(Boolean tree) {
        List<ExpressionTreeNodeVO> categories = expressionCategoryService.getFullTreeNodeVOList();
        List<ExpressionTreeNodeVO> expressionList = expressionMapper.selectFullTreeNodeVOList();
        expressionList.forEach(e->e.setCategoryFlag(false));
        if (CollUtil.isNotEmpty(categories)) {
            categories.addAll(expressionList);
        }
        if(BooleanUtil.isTrue(tree)){
            return TreeUtil.buildTree(categories, false);
        }else{
            return categories;
        }
    }

    private final Integer defaultScale = 15;

    @Override
    public String calculateExpression(ExpressionCalculateDTO dto) {
        String roundingCode = dto.getRoundingCode();
        RoundingMode roundingMode = StrUtil.isBlank(roundingCode) ? RoundingMode.HALF_EVEN :
                RoundingEnum.getEnumByCode(roundingCode).getMapping();
        List<String> keys = CollectionUtils.convertList(dto.getKeyValueList(), ExpressionCalculateDTO.kvDTO::getKey);
        List<String> values = CollectionUtils.convertList(dto.getKeyValueList(),
                ExpressionCalculateDTO.kvDTO::getValue);
        int initScale = dto.getScale() == null ? defaultScale : dto.getScale();
        BigDecimal result = expressionCalculator.evaluate(dto.getExpression(), keys, values);
        return dto.getScale() == null ?
                result.setScale(initScale, roundingMode).stripTrailingZeros().toPlainString() :
                result.setScale(initScale, roundingMode).toPlainString();
    }

    @Override
    public void verify(Long id) {
        Expression expression = expressionMapper.selectById(id);
        if (expression == null) {
            throw new BmosException(PlatformResponseCode.EXPRESSION_UPDATE_NOT_EXIST);
        }
        if (Objects.equals(expression.getConfirmStatus(), ExpressionStatusEnum.CONFIRMED.getValue())) {
            throw new BmosException(PlatformResponseCode.EXPRESSION_CONFIRM_STATUS_VALIDATED);
        }
        expressionMapper.verify(id);
    }

    @Override
    public List<MesRecordTreeNodeVO> getRecordBindTree(Long id) {
        ResponseInfo<List<MesRecordTreeNodeVO>> listResponseInfo =
                FeignUtils.handleRequest(mesFeignClient::getExpressionBindTree, id);
        return listResponseInfo.getData();
    }

    @Override
    public void bindBatchRecord(ExpressionBindRecordDTO dto) {
        FeignUtils.handleRequest(mesFeignClient::expressionBindBatchRecord, dto);
    }

    @Override
    public List<Long> getBoundRecordIdList(Long id) {
        return FeignUtils.handleRequest(mesFeignClient::getBoundRecordIdList, id).getData();
    }
}
