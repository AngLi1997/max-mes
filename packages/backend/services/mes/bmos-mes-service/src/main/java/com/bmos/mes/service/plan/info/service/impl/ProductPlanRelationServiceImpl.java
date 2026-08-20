package com.bmos.mes.service.plan.info.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.bmos.common.exception.BmosException;
import com.bmos.common.util.collection.CollectionUtils;
import com.bmos.mes.common.enums.BooleanEnum;
import com.bmos.mes.common.enums.plan.PlanArchiveStatusEnum;
import com.bmos.mes.common.exception.MesResponseCode;
import com.bmos.mes.service.config.minio.MinioProperties;
import com.bmos.mes.service.plan.info.convert.PlanConverter;
import com.bmos.mes.service.plan.info.convert.ProductPlanRelationConverter;
import com.bmos.mes.service.plan.info.mapper.PlanMapper;
import com.bmos.mes.service.plan.info.mapper.ProductPlanRelationMapper;
import com.bmos.mes.service.plan.info.model.Plan;
import com.bmos.mes.service.plan.info.model.ProductPlanRelation;
import com.bmos.mes.service.plan.info.service.ProductPlanRelationService;
import com.bmos.mes.service.plan.info.vo.ProductPlanRelatedProcessVO;
import com.bmos.mes.service.plan.info.vo.ProductPlanRelationListVO;
import com.bmos.mes.service.plan.team.dto.InstructionTeamProductStartConfirmDTO;
import com.bmos.mes.service.process.mapper.ProcessRelationMapper;
import com.bmos.mes.service.process.vo.ProcessRelationDetailVO;
import com.bmos.mes.service.utils.PlanArchivePathUtil;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ProductPlanRelationServiceImpl implements ProductPlanRelationService {
    @Autowired
    private ProductPlanRelationMapper productPlanRelationMapper;
    @Autowired
    private PlanMapper planMapper;

    @Autowired
    private MinioProperties minioProperties;

    @Autowired
    private ProcessRelationMapper processRelationMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void save(InstructionTeamProductStartConfirmDTO dto) {
        List<ProductPlanRelation> productPlanRelations = ProductPlanRelationConverter.INSTANCE.convertList(dto);
        // 使用生产前确认最新的 删除旧的
        updateProductPlanRelation(productPlanRelations, dto.getPlanId());
    }

    private void handleRelationData(List<ProductPlanRelation> relations, Set<Long> needAddData) {
        Map<Long, List<ProductPlanRelation>> relationMap = relations.stream()
                .collect(Collectors.groupingBy(ProductPlanRelation::getProductPlanId));
        List<ProductPlanRelation> collect = relationMap.entrySet().stream().map(entry -> {
            Set<Long> existsSet = CollectionUtils.convertSet(entry.getValue(),
                    ProductPlanRelation::getRelationProductPlanId);
            return new ArrayList<>(needAddData)
                    .stream()
                    .filter(relationProductPlanId -> !existsSet.contains(relationProductPlanId))
                    .map(relationProductPlanId -> ProductPlanRelation.builder()
                            .productPlanId(entry.getKey())
                            .relationProductPlanId(relationProductPlanId)
                            .isDirectRelation(BooleanEnum.FALSE)
                            .build())
                    .collect(Collectors.toList());
        }).flatMap(List::stream).collect(Collectors.toList());
        productPlanRelationMapper.insertBatch(collect);
    }

    @Override
    public List<ProductPlanRelation> getList(Long productPlanId) {
        if (productPlanId == null){
            return Lists.newArrayList();
        }
        // 记录已经查询出的关联的批次 若有环直接return
        Set<String> productRelationPlanIdStringSet = new HashSet<>();
        List<ProductPlanRelation> res = new ArrayList<>();
        List<ProductPlanRelation> productPlanRelations;
        List<Long> collect = Lists.newArrayList(productPlanId);
        do {
            productPlanRelations = productPlanRelationMapper.selectDirectByPlanIdList(collect);
            if (CollUtil.isEmpty(productPlanRelations)){
                break ;
            }
            res.addAll(productPlanRelations);
            if (judgeExist(productPlanRelations, productRelationPlanIdStringSet)){
                break;
            }
            collect = productPlanRelations.stream().map(ProductPlanRelation::getRelationProductPlanId).collect(Collectors.toList());
            productRelationPlanIdStringSet.addAll(productPlanRelations.stream()
                    .map(item -> StrUtil.format("{}{}{}", item.getProductPlanId(), StrUtil.DASHED, item.getRelationProductPlanId()))
                    .collect(Collectors.toList()));
        } while (CollUtil.isNotEmpty(productPlanRelations));
        return res;
    }

    /**
     * 判定是否存在
     * @param productPlanRelations
     * @param productRelationPlanIdStringSet
     * @return
     */
    private boolean judgeExist(List<ProductPlanRelation> productPlanRelations, Set<String> productRelationPlanIdStringSet) {
        if (CollUtil.isEmpty(productPlanRelations)){
            return false;
        }
        return productPlanRelations.stream().anyMatch(item -> productRelationPlanIdStringSet.contains(StrUtil.format("{}{}{}", item.getProductPlanId(), StrUtil.DASHED, item.getRelationProductPlanId())));
    }

    @Override
    public List<ProductPlanRelationListVO> detail(Long productPlanId) {
        return productPlanRelationMapper.detail(productPlanId);
    }

    @Override
    public List<ProductPlanRelationListVO> detailWithSelf(Long productPlanId) {
        List<ProductPlanRelationListVO> detail = productPlanRelationMapper.detail(productPlanId);
        detail.add(PlanConverter.INSTANCE.convertVO3(planMapper.selectById(productPlanId)));
        detail.forEach(item -> {
            if (PlanArchiveStatusEnum.ARCHIVE_SUCCESS == item.getArchiveStatus()) {
                item.setArchiveFileUrl(PlanArchivePathUtil.getPlanMinioCompleteFilePath(minioProperties.getBuckets().getArchive(),
                        item.getProductPlanId()));
            }
        });
        return detail;
    }

    @Override
    public void saveProductPlanRelation(List<ProductPlanRelation> productPlanRelations, Long planId) {
        if (CollUtil.isEmpty(productPlanRelations)) {
            return;
        }
        productPlanRelationMapper.insertBatch(productPlanRelations);
        // 处理上下游间接关联关系
        saveIndirectRelation(productPlanRelations, planId);
    }

    @Override
    public void updateProductPlanRelation(List<ProductPlanRelation> productPlanRelations, Long id) {
        Set<Long> currentRelationIds = CollectionUtils.convertSet(productPlanRelations, ProductPlanRelation::getRelationProductPlanId);
        // 查询出原有的批次直接关联关系
        List<ProductPlanRelation> relations = productPlanRelationMapper.selectDirectByProductPlanId(id);
        Set<Long> oldRelationIds = CollectionUtils.convertSet(relations, ProductPlanRelation::getRelationProductPlanId);
        // 处理删除的关联关系
        handleDeleteRelation(id, oldRelationIds, currentRelationIds);
        // 新增的关联关系
        List<ProductPlanRelation> insert = productPlanRelations.stream()
                .filter(e->{
                    return !oldRelationIds.contains(e.getRelationProductPlanId());
                }).collect(Collectors.toList());
        if (CollUtil.isEmpty(insert)) {
            return;
        }
        saveProductPlanRelation(insert, id);
    }

    private void handleDeleteRelation(Long id, Set<Long> oldRelationIds, Set<Long> currentRelationIds) {
        List<Long> deleteIds = oldRelationIds.stream()
                .filter(relationProductPlanId -> !currentRelationIds.contains(relationProductPlanId)).collect(Collectors.toList());
        if (CollUtil.isEmpty(deleteIds)) {
            return;
        }
        // 删除直接关联
        productPlanRelationMapper.deleteDirectRelation(id, deleteIds);
        // 删除由上面删除的直接关联引起的间接关联
        productPlanRelationMapper.deleteBySourceIds(deleteIds, id);
        // 删除来源为当前生产指令单的间接关联
        productPlanRelationMapper.deleteIndirectRelations(deleteIds, id);
    }

    @Override
    public List<ProductPlanRelatedProcessVO> queryProductPlanRelationList(Long planId) {
        Plan plan = planMapper.selectById(planId);
        if (plan == null) {
            throw new BmosException(MesResponseCode.PRODUCT_PLAN_NOT_EXISTS);
        }
        // 查询当前生产计划使用工艺的所有关联工艺
        List<ProcessRelationDetailVO> processRelations = processRelationMapper.selectDetailListByProcessId(plan.getProcessId());
        List<ProductPlanRelationListVO> detail = productPlanRelationMapper.detail(planId);
        Map<Long, List<ProductPlanRelationListVO>> processMap = CollectionUtils.convertMultiMap(detail,
                ProductPlanRelationListVO::getProcessId);
        return processRelations.stream().map(e -> {
            ProductPlanRelatedProcessVO processVO = new ProductPlanRelatedProcessVO();
            processVO.setProcessId(e.getProcessId());
            processVO.setProcessName(e.getProcessName());
            List<ProductPlanRelationListVO> relation = processMap.getOrDefault(e.getProcessId(), new ArrayList<>());
            processVO.setRelationBatchList(relation.stream().map(batch -> {
                ProductPlanRelatedProcessVO.PlanRelatedBatchVO batchVO = new ProductPlanRelatedProcessVO.PlanRelatedBatchVO();
                batchVO.setPlanBatchNo(batch.getBatchNo());
                batchVO.setPlanId(batch.getProductPlanId());
                batchVO.setRelated(batch.getRelated());
                return batchVO;
            }).collect(Collectors.toList()));
            return processVO;
        }).collect(Collectors.toList());
    }

    private void saveIndirectRelation(List<ProductPlanRelation> productPlanRelations, Long id) {
        // 处理当前计划关联其下级的关联
        handleCurrentLowerRelation(productPlanRelations, id);
        // 处理可能存在的其他关联当前计划的关联
        handleCurrentUpperRelation(productPlanRelations, id);
    }

    private void handleCurrentUpperRelation(List<ProductPlanRelation> productPlanRelations, Long id) {
        List<ProductPlanRelation> relations = productPlanRelationMapper.selectByRelationProductPlanId(Collections.singletonList(id));
        if (CollUtil.isEmpty(relations)) {
            return;
        }
        List<ProductPlanRelation> collect = relations.stream().map(e -> {
            return productPlanRelations.stream().map(child -> {
                ProductPlanRelation relation = new ProductPlanRelation();
                relation.setProductPlanId(e.getProductPlanId());
                relation.setRelationProductPlanId(child.getRelationProductPlanId());
                relation.setIsDirectRelation(BooleanEnum.FALSE);
                relation.setSourceProductPlanId(id);
                return relation;
            }).collect(Collectors.toList());
        }).flatMap(List::stream).collect(Collectors.toList());
        productPlanRelationMapper.insertBatch(collect);
    }

    private void handleCurrentLowerRelation(List<ProductPlanRelation> productPlanRelations, Long id) {
        List<Long> productPlanIdList = CollectionUtils.convertList(productPlanRelations,
                ProductPlanRelation::getRelationProductPlanId);
        List<ProductPlanRelation> relations = productPlanRelationMapper.selectProductPlanId(productPlanIdList);
        if (CollUtil.isEmpty(relations)) {
            return;
        }
        List<ProductPlanRelation> collect = relations.stream().map(e -> {
            ProductPlanRelation relation = new ProductPlanRelation();
            relation.setProductPlanId(id);
            relation.setRelationProductPlanId(e.getRelationProductPlanId());
            relation.setIsDirectRelation(BooleanEnum.FALSE);
            relation.setSourceProductPlanId(e.getProductPlanId());
            return relation;
        }).collect(Collectors.toList());
        productPlanRelationMapper.insertBatch(collect);
    }


}
