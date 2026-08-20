package com.bmos.lims2.server.eln.record.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.LocalDateTimeUtil;
import cn.hutool.core.thread.ThreadFactoryBuilder;
import cn.hutool.core.util.ObjectUtil;
import com.bmos.audit.engine.core.query.resp.PageQueryResp;
import com.bmos.audit.engine.core.query.resp.TaskListResp;
import com.bmos.common.exception.BmosException;
import com.bmos.common.tree.TreeUtil;
import com.bmos.common.util.collection.CollectionUtils;
import com.bmos.common.util.i18n.I18nUtils;
import com.bmos.common.util.json.JsonUtils;
import com.bmos.expression.pojo.KeyValue;
import com.bmos.lims2.common.constants.RecordConstant;
import com.bmos.lims2.common.enums.AuditCategoryCodeEnum;
import com.bmos.lims2.common.enums.BasicComponentTypeEnum;
import com.bmos.lims2.common.i18n.LimsResponseCode;
import com.bmos.lims2.common.model.execute.ExecuteFormDataBaseExtInfo;
import com.bmos.lims2.server.audit.FlowAuditService;
import com.bmos.lims2.server.audit.dto.FlowAuditTaskDTO;
import com.bmos.lims2.server.audit.dto.FlowStartDTO;
import com.bmos.lims2.server.audit.operationlog.entity.AuditOperationLogEntity;
import com.bmos.lims2.server.audit.operationlog.service.AuditOperationLogService;
import com.bmos.lims2.server.eln.record.convert.RecordVersionConvert;
import com.bmos.lims2.server.eln.record.dto.*;
import com.bmos.lims2.server.eln.record.entity.BatchRecordExpression;
import com.bmos.lims2.server.eln.record.entity.BatchRecordItem;
import com.bmos.lims2.server.eln.record.entity.BatchRecordParse;
import com.bmos.lims2.server.eln.record.entity.BatchRecordVersion;
import com.bmos.lims2.server.eln.record.entity.formula.AssociationPatternConfig;
import com.bmos.lims2.server.eln.record.enums.ComponentFormulaTypeEnum;
import com.bmos.lims2.server.eln.record.enums.RecordStateEnum;
import com.bmos.lims2.server.eln.record.mapper.BatchRecordExpressionMapper;
import com.bmos.lims2.server.eln.record.mapper.BatchRecordVersionMapper;
import com.bmos.lims2.server.eln.record.service.BatchRecordComponentService;
import com.bmos.lims2.server.eln.record.service.BatchRecordItemService;
import com.bmos.lims2.server.eln.record.service.BatchRecordParseService;
import com.bmos.lims2.server.eln.record.service.BatchRecordVersionService;
import com.bmos.lims2.server.eln.record.vo.*;
import com.bmos.lims2.server.inspect.parameter.entity.InspectMethod;
import com.bmos.lims2.server.inspect.parameter.mapper.InspectMethodMapper;
import com.bmos.lims2.server.platform.expression.PlatformExpressionFeignClient;
import com.bmos.lims2.server.platform.expression.vo.ExpressionTreeNodeVO;
import com.bmos.lims2.server.platform.parameter.impl.PlatformParameterClientImpl;
import com.bmos.lims2.server.platform.util.FeignUtils;
import com.bmos.logging.annotation.OperationLog;
import com.bmos.mybatis.CustomIdGenerator;
import com.bmos.mybatis.page.CommonPage;
import com.bmos.platform.facade.system.execute.parameter.constants.BusinessParameterCodeConstants;
import com.bmos.common.holder.SysUserHolder;
import com.bmos.lims2.common.enums.AuditBusinessModule;
import com.bmos.lims2.common.enums.OperationType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDateTime;
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

//    @Autowired
//    private OperationHistoryService logService;

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
    private PlatformParameterClientImpl platformParameterClient;

    @Autowired
    private InspectMethodMapper inspectMethodMapper;

    @Resource
    private AuditOperationLogService auditOperationLogService;

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
            throw new BmosException(LimsResponseCode.RECORD_VERSION_SAVE_ERROR);
        }
    }

    @Override
    public Boolean updateVersion(BatchRecordVersion convertCopyToDo) {
        return mapper.updateVersion(convertCopyToDo);
    }

    @Override
//    @OperationHistory(module = BusinessModule.BATCH_RECORD, operationType = OperationType.SAVE, remark = "#dto.remark", businessId = "#getId")
    @OperationLog
    public Long copyVersion(CopyVersionDTO dto) {
        List<BatchRecordVersion> recordPage = mapper.getRecordVersionList(dto.getRecordId());
        List<String> state = CollectionUtils.convertList(recordPage, BatchRecordVersion::getState);
        if (state.contains(RecordConstant.COMPLIE_CODE) || state.contains(RecordConstant.AUDIT_CODE)) {
            throw new BmosException(LimsResponseCode.RECORD_STATE_ABNORMAL);
        }
        try {
            Long versionId = CustomIdGenerator.nextId();
            dto.setId(versionId);
            CompletableFuture<Boolean> future = CompletableFuture.supplyAsync(() -> {
                return copyItem(versionId, dto);
            }, copyThreadPool);
            if (!future.get()) {
                throw new BmosException(LimsResponseCode.RECORD_COPY_ERROR);
            }
            BatchRecordVersion version = RecordVersionConvert.INSTANCE.convertCopyToDo(dto);
            mapper.updateVersion(version);
//            OperationHistoryContext.putVariable(version, BatchRecordVersion::getId);
            // 保存操作历史：复制/新增版本
            auditOperationLogService.save(AuditOperationLogEntity.builder()
                    .module(AuditBusinessModule.METHOD_AUDIT.name())
                    .businessId(versionId)
                    .operationType(OperationType.SAVE.getValue())
                    .createBy(SysUserHolder.getUser().getUserId())
                    .build());
            return versionId;
        } catch (Exception e) {
            e.printStackTrace();
            throw new BmosException(LimsResponseCode.RECORD_VERSION_SAVE_ERROR);
        }
    }

    @Override
    public List<RecordVersionVO> listVersion(Long recordId) {
        return mapper.listVersion(recordId);
    }


    @Override
    public List<RecordVersionVO> listParameterRecord(Long parameterId, Long recordId) {
        if (ObjectUtil.isNotNull(recordId)) {
            return mapper.listVersion(recordId);
        }
        List<InspectMethod> relations = inspectMethodMapper.listByParameterId(parameterId);
        List<Long> recordIds = com.bmos.common.util.collection.CollectionUtils.convertList(relations, InspectMethod::getRecordId);
        if (cn.hutool.core.collection.CollUtil.isEmpty(recordIds)) {
            return java.util.Collections.emptyList();
        }
        List<RecordVersionVO> result = new java.util.ArrayList<>();
        for (Long rid : recordIds) {
            List<RecordVersionVO> versions = mapper.listVersion(rid);
            if (versions != null) {
                result.addAll(versions);
            }
        }
        return result;
    }

    @Override
    public List<VersionLogVO> listRecordLog(Long versionId) {
        List<com.bmos.lims2.server.audit.operationlog.vo.ListLogVO> list = auditOperationLogService.listRecordLog(versionId);
        if (CollUtil.isEmpty(list)) {
            return new ArrayList<>();
        }
        List<VersionLogVO> result = new ArrayList<>(list.size());
        for (com.bmos.lims2.server.audit.operationlog.vo.ListLogVO item : list) {
            VersionLogVO vo = new VersionLogVO();
            vo.setOperationType(item.getOperationType());
            vo.setOperationTypeName(item.getOperationType() == null ? null : item.getOperationType().getName());
            vo.setCreateTime(item.getCreateTime());
            vo.setCreateBy(item.getCreateBy());
            vo.setRemark(item.getRemark());
            vo.setNodeName(item.getNodeName());
            vo.setComment(item.getComment());
            vo.setDetail(item.getDetail());
            result.add(vo);
        }
        return result;
    }

    @Override
    public Boolean checkoutSaveRecord(Long recordId) {
        List<BatchRecordVersion> recordPage = mapper.getRecordVersionList(recordId);
        List<String> state = CollectionUtils.convertList(recordPage, BatchRecordVersion::getState);
        if (state.contains(RecordConstant.COMPLIE_CODE) || state.contains(RecordConstant.AUDIT_CODE)) {
            throw new BmosException(LimsResponseCode.RECORD_STATE_ABNORMAL);
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
        List<PageRecordAuditVO> voList = mapper.queryByVersionIdList(dto.getName(),dto.getCode());
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
//    @OperationHistory(module = BusinessModule.BATCH_RECORD, operationType = OperationType.SUBMIT_AUDIT, remark = "#getRemark", businessId = "#versionId")
    public Boolean startFlow(Long versionId) {
        BatchRecordVersion version = mapper.queryById(versionId);
        List<BatchRecordVersion> versionList = mapper.getRecordVersionList(version.getRecordId());
        boolean hasAuditingVersion = versionList.stream()
                .anyMatch(item -> Objects.equals(RecordStateEnum.AUDIT.getValue(), item.getState())
                        && !Objects.equals(item.getId(), versionId));
        if (hasAuditingVersion) {
            throw new BmosException(LimsResponseCode.RECORD_VERSION_AUDIT_RUNNING);
        }
        String recordName = mapper.getRecordName(version.getRecordId());
////        OperationHistoryContext.putVariable(version, BatchRecordVersion::getRemark);
//        if (!StrUtil.equals(RecordStateEnum.EDIT.getValue(), version.getState())) {
//            throw new BmosException(LimsResponseCode.EDIT_STATUS_CAN_AUDIT);
//        }
        FlowStartDTO dto = new FlowStartDTO();
        dto.setBusinessKey(String.valueOf(versionId));
        dto.setCode(AuditCategoryCodeEnum.METHOD_AUDIT.getCode());
        dto.setCategoryCode(AuditCategoryCodeEnum.METHOD_AUDIT.getCode());
        dto.setName(recordName);
        dto.setExtField(version.getVersion());
        String instanceId = auditService.flowAuditStart(dto);
        version.setInstanceId(instanceId);
        version.setState(RecordStateEnum.AUDIT.getValue());
        boolean updated = mapper.updateVersion(version);
        if (updated) {
            // 记录提交审核日志
            saveHistoryLog(null, null, SysUserHolder.getUser().getUserId(), versionId, OperationType.SUBMIT_AUDIT, null);
        }
        return updated;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void auditRecordSuccessCallBack(String processInstanceId, String comment, String userId) {
        BatchRecordVersion version = mapper.queryByInstanceId(processInstanceId);
        if (version == null) {
            return;
        }
        // 审批通过：其他已生效版本自动失效，确保单一生效版本
        List<BatchRecordVersion> activeVersions = mapper.getRecordVersionList(version.getRecordId())
                .stream()
                .filter(v -> Objects.equals(RecordStateEnum.CERTAIN.getValue(), v.getState()))
                .filter(v -> !Objects.equals(v.getId(), version.getId()))
                .collect(Collectors.toList());
        for (BatchRecordVersion activeVersion : activeVersions) {
            activeVersion.setState(RecordStateEnum.INVALID.getValue());
            activeVersion.setEffectiveTime(null);
            mapper.updateVersion(activeVersion);
            saveHistoryLog(null, "审批通过自动失效其他版本", userId, activeVersion.getId(), OperationType.INVALID, null);
        }
        version.setState(RecordStateEnum.CERTAIN.getValue());
        version.setEffectiveTime(LocalDateTime.now());
        mapper.updateVersion(version);
        // 审核通过：记录历史
        saveHistoryLog(comment, null, userId, version.getId(), OperationType.APPROVE_AUDIT, null);
        batchRecordComponentService.getGraph(version.getId());
    }

    @Override
    public void auditRecordRejectCallBack(String processInstanceId, String comment, String remark,String nodeName, String userId) {
        BatchRecordVersion version = mapper.queryByInstanceId(processInstanceId);
        version.setState(RecordStateEnum.EDIT.getValue());
        mapper.updateVersion(version);
        // 审核不通过：记录历史
        saveHistoryLog(comment, remark, userId, version.getId(), OperationType.REJECT_AUDIT, nodeName);
    }

    @Override
    public List<SelectRecorVO> queryRecordVersionByRecordId(Long recordId) {
        List<BatchRecordVersion> recordVersionList = mapper.getRecordVersionList(recordId);
        if (CollUtil.isEmpty(recordVersionList)) {
            return Collections.emptyList();
        }
        List<BatchRecordVersion> versionList = CollectionUtils.
                filterList(recordVersionList, version -> !RecordStateEnum.CANCEL.getValue().equals(version.getState())&&
                        !RecordStateEnum.INVALID.getValue().equals(version.getState()));
        List<SelectRecorVO> voList = versionList.stream().map(item -> {
            SelectRecorVO vo = new SelectRecorVO();
            vo.setName(item.getVersion());
            vo.setValue(item.getId());
            return vo;
        }).collect(Collectors.toList());
        return voList;
    }

//    private void saveHistoryLog(String comment, String remark, String userId, BatchRecordVersion version, OperationType operationType,String nodeName) {
//        logService.save(OperationLogModel.builder()
//                .module(BusinessModule.BATCH_RECORD.name())
//                .businessId(version.getId())
//                .operationType(operationType.getValue())
//                .remark(remark)
//                .comment(comment)
//                .nodeName(nodeName)
//                .createBy(userId)
//                .build());
//    }
    private void saveHistoryLog(String comment, String remark, String userId, Long businessId, OperationType operationType, String nodeName) {
        auditOperationLogService.save(AuditOperationLogEntity.builder()
                .module(AuditBusinessModule.METHOD_AUDIT.name())
                .businessId(businessId)
                .operationType(operationType.getValue())
                .remark(remark)
                .nodeName(nodeName)
                .comment(comment)
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
        // 节点执行通过：记录历史
        saveHistoryLog(comment, remark, userId, Long.valueOf(businessKey), OperationType.APPROVE_AUDIT, nodeName);
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
            throw new BmosException(LimsResponseCode.RECORD_VERSION_NOT_EXIST);
        }
        RecordInfoItemListVO result = new RecordInfoItemListVO();
        result.setRecordName(mapper.getRecordName(batchRecordVersion.getRecordId()));
        result.setRecordCode(mapper.getRecordCode(batchRecordVersion.getRecordId()));
        List<ItemBaseInfoVO> itemBaseInfoVOS = itemService.selectItemBaseInfoList(recordVersionId);
        itemBaseInfoVOS.sort(Comparator.comparing(ItemBaseInfoVO::getSort,
                Comparator.nullsLast(Comparator.naturalOrder())));
        result.setItemList(itemBaseInfoVOS);
        return result;
    }

    @Override
    public void changeRecordItemSort(RecordItemSortUpdateDTO dto) {
        BatchRecordVersion batchRecordVersion = mapper.selectById(dto.getRecordVersionId());
        if (batchRecordVersion == null) {
            throw new BmosException(LimsResponseCode.RECORD_VERSION_NOT_EXIST);
        }
        List<BatchRecordItem> batchRecordItems = itemService.queryByRecordId(dto.getRecordVersionId());
        Map<Long, Integer> sortMap = CollectionUtils.convertMap(dto.getItemList(),
                RecordItemSortUpdateDTO.ItemSortDTO::getId, RecordItemSortUpdateDTO.ItemSortDTO::getSort);
        for (BatchRecordItem batchRecordItem : batchRecordItems) {
            Integer sort = sortMap.get(batchRecordItem.getId());
            if (sort == null) {
                throw new BmosException(LimsResponseCode.RECORD_SORT_CHANGE_ERROR);
            }
            batchRecordItem.setSort(sort);
        }
        itemService.saveOrUpdateItem(batchRecordItems);
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
//
//    private void saveRecordExecutionHistoryLog(String remark, String userId, Long businessId, OperationType operationType, String nodeName, String comment) {
//        logService.save(OperationLogModel.builder()
//                .module(BusinessModule.BATCH_RECORD.name())
//                .businessId(businessId)
//                .operationType(operationType.getValue())
//                .remark(remark)
//                .comment(comment)
//                .nodeName(nodeName)
//                .createBy(userId)
//                .build());
//    }
}
