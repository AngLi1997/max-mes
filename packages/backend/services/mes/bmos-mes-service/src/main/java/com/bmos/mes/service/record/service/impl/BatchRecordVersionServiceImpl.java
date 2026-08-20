package com.bmos.mes.service.record.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.LocalDateTimeUtil;
import cn.hutool.core.thread.ThreadFactoryBuilder;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.bmos.audit.engine.core.query.resp.PageQueryResp;
import com.bmos.audit.engine.core.query.resp.TaskListResp;
import com.bmos.common.exception.BmosException;
import com.bmos.common.tree.TreeUtil;
import com.bmos.common.util.collection.CollectionUtils;
import com.bmos.common.util.i18n.I18nUtils;
import com.bmos.common.util.json.JsonUtils;
import com.bmos.expression.pojo.KeyValue;
import com.bmos.logging.annotation.OperationLog;
import com.bmos.mes.common.constant.RecordConstant;
import com.bmos.mes.common.enums.audit.AuditCategoryCodeEnum;
import com.bmos.mes.common.enums.record.BasicComponentTypeEnum;
import com.bmos.mes.common.exception.MesResponseCode;
import com.bmos.mes.common.model.execute.ExecuteFormDataBaseExtInfo;
import com.bmos.mes.service.audit.dto.FlowAuditTaskDTO;
import com.bmos.mes.service.audit.dto.FlowStartDTO;
import com.bmos.mes.service.audit.service.FlowAuditService;
import com.bmos.mes.service.operation.history.annotation.OperationHistory;
import com.bmos.mes.service.operation.history.aspect.OperationHistoryContext;
import com.bmos.mes.service.operation.history.enums.BusinessModule;
import com.bmos.mes.service.operation.history.enums.OperationType;
import com.bmos.mes.service.operation.history.model.OperationLogModel;
import com.bmos.mes.service.operation.history.service.OperationHistoryService;
import com.bmos.mes.service.platform.FeignUtils;
import com.bmos.mes.service.platform.expression.PlatformExpressionFeignClient;
import com.bmos.mes.service.platform.expression.vo.ExpressionTreeNodeVO;
import com.bmos.mes.service.platform.parameter.impl.PlatformParameterClientImpl;
import com.bmos.mes.service.process.model.ProcessBatchRecordRelation;
import com.bmos.mes.service.process.service.ProcessBatchRecordRelationService;
import com.bmos.mes.service.process.vo.ProcessRecordVO;
import com.bmos.mes.service.record.convert.RecordVersionConvert;
import com.bmos.mes.service.record.dto.*;
import com.bmos.mes.service.record.enums.ComponentFormulaTypeEnum;
import com.bmos.mes.service.record.enums.RecordStateEnum;
import com.bmos.mes.service.record.mapper.BatchRecordExpressionMapper;
import com.bmos.mes.service.record.mapper.BatchRecordVersionMapper;
import com.bmos.mes.service.record.model.BatchRecordExpression;
import com.bmos.mes.service.record.model.BatchRecordItem;
import com.bmos.mes.service.record.model.BatchRecordParse;
import com.bmos.mes.service.record.model.BatchRecordVersion;
import com.bmos.mes.service.record.model.formula.AssociationPatternConfig;
import com.bmos.mes.service.record.service.BatchRecordComponentService;
import com.bmos.mes.service.record.service.BatchRecordItemService;
import com.bmos.mes.service.record.service.BatchRecordParseService;
import com.bmos.mes.service.record.service.BatchRecordVersionService;
import com.bmos.mes.service.record.vo.*;
import com.bmos.mybatis.CustomIdGenerator;
import com.bmos.mybatis.page.CommonPage;
import com.bmos.platform.facade.system.execute.parameter.constants.BusinessParameterCodeConstants;
import com.google.common.collect.Sets;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.ZoneId;
import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;

@Service
public class BatchRecordVersionServiceImpl implements BatchRecordVersionService {

    @Autowired
    private BatchRecordVersionMapper mapper;

    private static final Executor copyThreadPool=new ThreadPoolExecutor(2, 10, 30, TimeUnit.SECONDS, new ArrayBlockingQueue<>(100),
            new ThreadFactoryBuilder().setNamePrefix("version-copy-thread").build(), new ThreadPoolExecutor.AbortPolicy());

    @Autowired
    private BatchRecordItemService itemService;

    @Autowired
    private BatchRecordComponentService componentService;

    @Autowired
    private OperationHistoryService logService;

    @Autowired
    private FlowAuditService auditService;

    @Resource
    private BatchRecordParseService recordParseService;

    @Autowired
    private BatchRecordComponentService batchRecordComponentService;

    @Autowired
    private PlatformExpressionFeignClient platformExpressionFeignClient;

    @Resource
    private BatchRecordExpressionMapper batchRecordExpressionMapper;

    @Resource
    @Lazy
    private ProcessBatchRecordRelationService processBatchRecordRelationService;

    @Resource
    private PlatformParameterClientImpl platformParameterClient;

    @Override
    public BatchRecordVersion saveOrUpdateVersion(BatchRecordSaveDTO dto) {
        try {
            BatchRecordVersion version = new BatchRecordVersion();
            version.setRecordId(dto.getRecordId());
            version.setRemark(dto.getRemark());
            version.setVersion(dto.getVersion());
            version.setId(dto.getVersionId());
            version.setFilePath(dto.getFilePath());
            mapper.saveOrUpdateVersion(version);
            return version;
        } catch (Exception e) {
            throw new BmosException(MesResponseCode.RECORD_VERSION_SAVE_ERROR);
        }
    }

    @Override
    public Boolean updateVersion(BatchRecordVersion convertCopyToDo) {
        return mapper.updateVersion(convertCopyToDo);
    }

    @Override
    @OperationHistory(module = BusinessModule.BATCH_RECORD, operationType = OperationType.SAVE, remark = "#dto.remark", businessId = "#getId")
    @OperationLog
    public Long copyVersion(CopyVersionDTO dto) {
        List<BatchRecordVersion> recordPage = mapper.getRecordVersionList(dto.getRecordId());
        List<String> state = CollectionUtils.convertList(recordPage, BatchRecordVersion::getState);
        if (state.contains(RecordConstant.COMPLIE_CODE) || state.contains(RecordConstant.AUDIT_CODE)) {
            throw new BmosException(MesResponseCode.RECORD_STATE_ABNORMAL);
        }
        try {
            Long versionId = CustomIdGenerator.nextId();
            dto.setId(versionId);
            CompletableFuture<Boolean> future = CompletableFuture.supplyAsync(() -> {
                return copyItem(versionId, dto);
            }, copyThreadPool);
            if (!future.get()) {
                throw new BmosException(MesResponseCode.RECORD_COPY_ERROR);
            }
            BatchRecordVersion version = RecordVersionConvert.INSTANCE.convertCopyToDo(dto);
            mapper.updateVersion(version);
            OperationHistoryContext.putVariable(version, BatchRecordVersion::getId);
            return versionId;
        } catch (Exception e) {
            e.printStackTrace();
            throw new BmosException(MesResponseCode.RECORD_VERSION_SAVE_ERROR);
        }
    }

    @Override
    public List<RecordVersionVO> listVersion(Long recordId) {
        return mapper.listVersion(recordId);
    }

    @Override
    public List<RecordVersionVO> listPorductRecord(Long productId, Long recordId) {
        List<RecordVersionVO> list;
        if (ObjectUtil.isNull(recordId)) {
            list = mapper.queryByProductId(productId);
        } else {
            list = mapper.listVersion(recordId);
        }
        return list;
    }

    @Override
    public List<VersionLogVO> listRecordLog(Long versionId) {
        return logService.listRecordLog(versionId);
    }

    @Override
    public Boolean checkoutSaveRecord(Long recordId) {
        List<BatchRecordVersion> recordPage = mapper.getRecordVersionList(recordId);
        List<String> state = CollectionUtils.convertList(recordPage, BatchRecordVersion::getState);
        if (state.contains(RecordConstant.COMPLIE_CODE) || state.contains(RecordConstant.AUDIT_CODE)) {
            throw new BmosException(MesResponseCode.RECORD_STATE_ABNORMAL);
        }
        return Boolean.TRUE;
    }

    @Override
    public BatchRecordVersion queryById(Long recordVersionId) {
        return mapper.queryById(recordVersionId);
    }

    @Override
    public CommonPage<PageRecordAuditVO> pageRecordAudit(RecordAuditDTO dto) {
        FlowAuditTaskDTO taskDTO = dto.convertAuditTaskDTO();
        List<PageRecordAuditVO> voList = mapper.queryByVersionIdList(dto.getName());
        List<Long> versionId = CollectionUtils.convertList(voList, PageRecordAuditVO::getVersionId);
        if (CollUtil.isEmpty(versionId)) {
            return CommonPage.CommonPage(Collections.emptyList(), 0L, dto.convertBasePage());
        }
        taskDTO.setBusinessKeyList(CollectionUtils.convertList(versionId, String::valueOf));
        PageQueryResp<List<TaskListResp>> listPageQueryResp = auditService.queryToDoListByCategory(taskDTO);
        List<String> businessIdList = CollectionUtils.convertList(listPageQueryResp.getData(), TaskListResp::getBusinessKey);
        if (CollUtil.isEmpty(listPageQueryResp.getData()) || CollUtil.isEmpty(businessIdList)) {
            return CommonPage.CommonPage(Collections.emptyList(), 0L, dto.convertBasePage());
        }
        Map<Long, PageRecordAuditVO> map = CollectionUtils.convertMap(voList, PageRecordAuditVO::getVersionId);
        List<PageRecordAuditVO> recordAuditList = RecordVersionConvert.INSTANCE.convertList(listPageQueryResp.getData(), map);
        return CommonPage.CommonPage(recordAuditList, listPageQueryResp.getTotal(), dto.convertBasePage());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @OperationHistory(module = BusinessModule.BATCH_RECORD, operationType = OperationType.SUBMIT_AUDIT, remark = "#getRemark", businessId = "#versionId")
    public Boolean startFlow(Long versionId) {
        BatchRecordVersion version = mapper.queryById(versionId);
        String recordName = mapper.getRecordName(version.getRecordId());
        OperationHistoryContext.putVariable(version, BatchRecordVersion::getRemark);
        if (!StrUtil.equals(RecordStateEnum.EDIT.getValue(), version.getState())) {
            throw new BmosException(MesResponseCode.EDIT_STATUS_CAN_AUDIT);
        }
        FlowStartDTO dto = new FlowStartDTO();
        dto.setBusinessKey(String.valueOf(versionId));
        dto.setCode(AuditCategoryCodeEnum.RECODE.getCode());
        dto.setCategoryCode(AuditCategoryCodeEnum.RECODE.getCode());
        dto.setName(recordName);
        dto.setExtField(version.getVersion());
        String instanceId = auditService.flowAuditStart(dto);
        version.setInstanceId(instanceId);
        version.setState(RecordStateEnum.AUDIT.getValue());
        return mapper.updateVersion(version);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void auditRecordSuccessCallBack(String processInstanceId, String comment, String userId) {
        BatchRecordVersion version = mapper.queryByInstanceId(processInstanceId);
        version.setState(RecordStateEnum.CERTAIN.getValue());
        mapper.updateVersion(version);
        //saveHistoryLog(comment, userId, version, OperationType.APPROVE_AUDIT);
        batchRecordComponentService.getGraph(version.getId());
    }

    @Override
    public void auditRecordRejectCallBack(String processInstanceId, String comment, String remark,String nodeName, String userId) {
        BatchRecordVersion version = mapper.queryByInstanceId(processInstanceId);
        version.setState(RecordStateEnum.EDIT.getValue());
        mapper.updateVersion(version);
        saveHistoryLog(comment, remark, userId, version, OperationType.REJECT_AUDIT,nodeName);
    }

    @Override
    public List<SelectRecorVO> queryRecordVersionByRecordId(Long recordId) {
        List<BatchRecordVersion> recordVersionList = mapper.getRecordVersionList(recordId);
        if (CollUtil.isEmpty(recordVersionList)) {
            return Collections.emptyList();
        }
        List<BatchRecordVersion> versionList = CollectionUtils.
                filterList(recordVersionList, version -> !RecordStateEnum.CANCEL.getValue().equals(version.getState()));
        List<SelectRecorVO> voList = versionList.stream().map(item -> {
            SelectRecorVO vo = new SelectRecorVO();
            vo.setName(item.getVersion());
            vo.setValue(item.getId());
            return vo;
        }).collect(Collectors.toList());
        return voList;
    }

    private void saveHistoryLog(String comment, String remark, String userId, BatchRecordVersion version, OperationType operationType,String nodeName) {
        logService.save(OperationLogModel.builder()
                .module(BusinessModule.BATCH_RECORD.name())
                .businessId(version.getId())
                .operationType(operationType.getValue())
                .remark(remark)
                .comment(comment)
                .nodeName(nodeName)
                .createBy(userId)
                .build());
    }

    @Transactional(rollbackFor = Exception.class)
    public Boolean copyItem(Long versionId, CopyVersionDTO dto) {
        List<BatchRecordItem> items = itemService.queryByRecordId(dto.getVersionOldId());
        if (ObjectUtil.isEmpty(items)) {
            return Boolean.TRUE;
        }
        List<Long> recordItemIdList = CollectionUtils.convertList(items, BatchRecordItem::getId);
        List<BatchRecordParse> parses = recordParseService.selectByItemId(recordItemIdList);
        Map<Long, BatchRecordParse> parseMap = CollectionUtils.convertMap(parses, BatchRecordParse::getId);
        // 处理业务组件复制
        List<Long> itemIdList = CollectionUtils.convertList(items, BatchRecordItem::getItemId);
        componentService.copyComponent(dto, itemIdList, versionId);
        List<BatchRecordItem> list = new ArrayList<>();
        List<BatchRecordParse> parseList = new ArrayList<>();
        items.forEach(item -> {
            item.setRecordVersionId(versionId);
            item.setVersion(dto.getVersion());
            BatchRecordParse parse = parseMap.get(item.getId());
            Long recordItemId = CustomIdGenerator.nextId();
            item.setId(recordItemId);
            if (parse != null) {
                parse.setId(recordItemId);
                parseList.add(parse);
            }
            list.add(item);

        });
        itemService.saveOrUpdateItem(list);
        recordParseService.insertBatch(parseList);
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void auditRecordExecutionSuccessCallBack(String businessKey, String remark, String userId,String nodeName,String comment) {
        saveRecordExecutionHistoryLog(remark, userId, Long.valueOf(businessKey), OperationType.APPROVE_AUDIT,nodeName,comment);
    }

    @Override
    public List<String> getAuditBusinessKey(List<Long> deptIdList) {
        return mapper.getAuditBusinessKey(deptIdList);
    }

    @Override
    public List<BatchRecordVersion> queryVersionByRecordIdList(List<Long> recordIdList) {
        return mapper.queryVersionByRecordIdList(recordIdList);
    }

    @Override
    public List<ExpressionTreeNodeVO> queryPlatformExpressionAndBuiltInFunction(ExpressionQueryDTO dto) {
        List<ExpressionTreeNodeVO> result = new ArrayList<>();
        BasicComponentTypeEnum componentTypeEnum = BasicComponentTypeEnum.getEnumByValue(dto.getComponentType());
        List<ComponentFormulaTypeEnum> formulaTypeEnums = ComponentFormulaTypeEnum.getEnumByCalculateType(componentTypeEnum);
        List<ExpressionTreeNodeVO> formulaNodes = convertBuiltInFormulaToExpressionTreeNode(formulaTypeEnums);
        if(!Objects.equals(componentTypeEnum, BasicComponentTypeEnum.NUMBER) || dto.getRecordId() == null){
            return formulaNodes;
        }
        List<BatchRecordExpression> relations = batchRecordExpressionMapper.selectByRecordId(dto.getRecordId());
        Set<Long> expressionIds = CollectionUtils.convertSet(relations, BatchRecordExpression::getExpressionId);
        List<ExpressionTreeNodeVO> res = FeignUtils.handleRequest(data ->
                platformExpressionFeignClient.getFullExpressionAndCategoryList(data), false).getData();
        res.forEach(e->{
            e.setCancelBound(!expressionIds.contains(e.getId()) && Objects.equals(dto.getFormulaId(), e.getId()));
        });
        res.removeIf(e-> !e.getCategoryFlag() && !expressionIds.contains(e.getId()) && !e.isCancelBound());
        List<ExpressionTreeNodeVO> tree = TreeUtil.buildTree(res, false);
        cleanTreeList(tree);
        CollUtil.addAll(result, tree);
        CollUtil.addAll(result, formulaNodes);
        return result;
    }

    public static ExpressionTreeNodeVO cleanTree(ExpressionTreeNodeVO root) {
        // 如果节点为 null 或没有子节点直接返回
        if (root == null) return null;

        // 如果是分类节点，递归清理子节点
        if (root.getCategoryFlag()) {
            Iterator<ExpressionTreeNodeVO> iterator = root.getChildren().iterator();
            while (iterator.hasNext()) {
                ExpressionTreeNodeVO child = iterator.next();
                // 如果子节点被清理为 null，移除它
                if (cleanTree(child) == null) {
                    iterator.remove();
                }
            }
            // 如果清理后分类节点没有子节点，返回 null
            if (root.getChildren().isEmpty()) {
                return null;
            }
        }

        // 如果是需求节点或分类节点仍有子节点，保留当前节点
        return root;
    }

    public static void cleanTreeList(List<ExpressionTreeNodeVO> rootList) {
        // 使用迭代器清理根节点列表
        Iterator<ExpressionTreeNodeVO> iterator = rootList.iterator();
        while (iterator.hasNext()) {
            ExpressionTreeNodeVO root = iterator.next();
            // 如果根节点被清理为空，移除它
            if (cleanTree(root) == null) {
                iterator.remove();
            }
        }
    }

    @Override
    public RecordInfoItemListVO getRecordInfoAndItemList(Long recordVersionId) {
        BatchRecordVersion batchRecordVersion = mapper.selectById(recordVersionId);
        if (batchRecordVersion == null) {
            throw new BmosException(MesResponseCode.RECORD_VERSION_NOT_EXIST);
        }
        RecordInfoItemListVO result = new RecordInfoItemListVO();
        result.setRecordName(mapper.getRecordName(batchRecordVersion.getRecordId()));
        List<ItemBaseInfoVO> itemBaseInfoVOS = itemService.selectItemBaseInfoList(recordVersionId);
        itemBaseInfoVOS.sort(Comparator.comparing(ItemBaseInfoVO::getSort,
                Comparator.nullsLast(Comparator.naturalOrder())));
        result.setItemList(itemBaseInfoVOS);
        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void changeRecordItemSort(RecordItemSortUpdateDTO dto) {
        BatchRecordVersion batchRecordVersion = mapper.selectById(dto.getRecordVersionId());
        if (batchRecordVersion == null) {
            throw new BmosException(MesResponseCode.RECORD_VERSION_NOT_EXIST);
        }
        List<BatchRecordItem> batchRecordItems = itemService.queryByRecordId(dto.getRecordVersionId());
        Map<Long, Integer> sortMap = CollectionUtils.convertMap(dto.getItemList(),
                RecordItemSortUpdateDTO.ItemSortDTO::getId, RecordItemSortUpdateDTO.ItemSortDTO::getSort);
        for (BatchRecordItem batchRecordItem : batchRecordItems) {
            Integer sort = sortMap.get(batchRecordItem.getId());
            if (sort == null) {
                throw new BmosException(MesResponseCode.RECORD_SORT_CHANGE_ERROR);
            }
            batchRecordItem.setSort(sort);
        }
        itemService.saveOrUpdateItem(batchRecordItems);
    }

    @Override
    public List<SelectRecorVO> queryProcessRecordVersionByRecordId(Long recordId, Long processVersionId) {
        List<SelectRecorVO> voList = this.queryRecordVersionByRecordId(recordId);
        if (ObjectUtil.isNull(processVersionId)){
            return voList;
        }
        //批记录
        List<ProcessBatchRecordRelation> relations =
                processBatchRecordRelationService.getListByProcessVersionId(processVersionId);
        List<ProcessBatchRecordRelation> recordRelations = CollectionUtils.filterList(relations, item -> item.getBatchRecordId().equals(recordId));
        List<Long> recordVersionId = CollectionUtils.convertList(voList, SelectRecorVO::getValue);
        recordRelations.forEach(item->{
            if (CollUtil.isEmpty(voList) || !recordVersionId.contains(item.getBatchRecordVersionId())){
                SelectRecorVO vo = new SelectRecorVO();
                vo.setName(item.getBatchRecordVersion());
                vo.setValue(item.getBatchRecordVersionId());
                vo.setDisabled(true);
                voList.add(vo);
            }
        });
        return voList;
    }

    @Override
    public String getFunctionCalculatePreview(FunctionCalculatePreviewDTO dto) {
        ComponentFormulaTypeEnum formulaType = ComponentFormulaTypeEnum.getEnumByValue(dto.getFunctionValue());
        try {
            switch (formulaType) {
                case ASSOCIATED_REFERENCES:
                    boolean complete = dto.getFormulaConfig().associationPatternConfigIsComplete();
                    if (!complete) {
                        return dto.getInput();
                    }
                    AssociationPatternConfig config = dto.getFormulaConfig().getAssociationPatternConfig();
                    return config.calculateResult(config.isNumberType() ? dto.getInput() : convert2ExtInfoJsonStr(dto));
                default:
                    return null;
            }
        } catch (Exception e) {
            return platformParameterClient.getValueByCode(BusinessParameterCodeConstants.MES_RECORD_ERROR_DATA);
        }
    }

    @Override
    public BatchRecordVersion selectById(Long businessId) {
        return mapper.selectOneById(businessId);
    }

    @Override
    public List<ProcessRecordListVO> queryProcessRecordVersionByRecordIdList(ProcessRecordVersionQueryDTO dto) {
        ArrayList<ProcessRecordListVO> result = new ArrayList<>();
        List<BatchRecordVersion> batchRecordVersions = mapper.queryVersionByRecordIdList(dto.getRecordIdList());
        Map<Long, List<BatchRecordVersion>> recordMap = CollectionUtils.convertMultiMap(batchRecordVersions, BatchRecordVersion::getRecordId);
        List<ProcessBatchRecordRelation> relations =
                processBatchRecordRelationService.getListByProcessVersionId(dto.getProcessVersionId());
        for (ProcessBatchRecordRelation relation : relations) {
            List<BatchRecordVersion> versions = recordMap.get(relation.getBatchRecordId());
            ProcessRecordListVO vo = new ProcessRecordListVO();
            vo.setRecordId(relation.getBatchRecordId());
            Set<Long> versionIds = new HashSet<>();
            List<SelectRecorVO> enableList = getVersionList(relation, versions, versionIds);
            vo.setVersionList(enableList);
            result.add(vo);
        }
        return result;
    }

    private static List<SelectRecorVO> getVersionList(ProcessBatchRecordRelation relation, List<BatchRecordVersion> versions, Set<Long> versionIds) {
        // 处理正常的版本
        List<SelectRecorVO> enableList = versions.stream()
                .filter(e -> !RecordStateEnum.CANCEL.getValue().equals(e.getState()))
                .map(e -> {
                    SelectRecorVO v = new SelectRecorVO();
                    v.setName(e.getVersion());
                    v.setValue(e.getId());
                    v.setDisabled(false);
                    versionIds.add(e.getId());
                    return v;
                })
                .collect(Collectors.toList());
        // 处理被删除或者废弃的版本
        if (!versionIds.contains(relation.getBatchRecordVersionId())) {
            SelectRecorVO selectRecorVO = new SelectRecorVO();
            selectRecorVO.setName(relation.getBatchRecordVersion());
            selectRecorVO.setValue(relation.getBatchRecordVersionId());
            selectRecorVO.setDisabled(true);
            enableList.add(selectRecorVO);
        }
        return enableList;
    }

    private String convert2ExtInfoJsonStr(FunctionCalculatePreviewDTO dto) {
        ExecuteFormDataBaseExtInfo extInfo = new ExecuteFormDataBaseExtInfo();
        extInfo.setTimeStamp(String.valueOf(LocalDateTimeUtil.parse(dto.getInput(), "yyyy-MM-dd HH:mm:ss")
                .atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()));
        return JsonUtils.toJsonString(extInfo);
    }

    private List<ExpressionTreeNodeVO> convertBuiltInFormulaToExpressionTreeNode(List<ComponentFormulaTypeEnum> formulaTypeEnums) {
        if(CollUtil.isEmpty(formulaTypeEnums)){
            return new ArrayList<>();
        }
        return formulaTypeEnums.stream().map(e->{
            ExpressionTreeNodeVO node = new ExpressionTreeNodeVO();
            node.setId(Long.valueOf(e.getValue()));
            node.setCategoryFlag(false);
            node.setIndefiniteParam(e.getIndefiniteParam());
            node.setName(I18nUtils.getEnumMessage(e));
            List<ComponentFormulaTypeEnum.ParseKV> expressionParse = e.getExpressionParse();
            if(CollUtil.isNotEmpty(expressionParse)){
                List<KeyValue<String, String>> collect = expressionParse.stream().map(p -> {
                    KeyValue<String, String> kv = new KeyValue<>();
                    kv.setKey(p.getKey());
                    kv.setValue(p.getValue());
                    return kv;
                }).collect(Collectors.toList());
                node.setExpressionParse(collect);
            }
            return node;
        }).collect(Collectors.toList());
    }

    private void saveRecordExecutionHistoryLog(String remark, String userId, Long businessId, OperationType operationType,String nodeName,String comment) {
        logService.save(OperationLogModel.builder()
                .module(BusinessModule.BATCH_RECORD.name())
                .businessId(businessId)
                .operationType(operationType.getValue())
                .remark(remark)
                .comment(comment)
                .nodeName(nodeName)
                .createBy(userId)
                .build());
    }
}
