package com.bmos.mes.service.plan.document.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.codec.Base64;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.BooleanUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.aspose.words.Shape;
import com.aspose.words.*;
import com.bmos.common.exception.BmosException;
import com.bmos.common.util.collection.CollectionUtils;
import com.bmos.common.util.json.JsonUtils;
import com.bmos.file.docx.dto.ComponentDTO;
import com.bmos.file.docx.dto.FieldValueDTO;
import com.bmos.file.docx.dto.ImageVO;
import com.bmos.file.docx.model.DocxFooter;
import com.bmos.file.docx.model.DocxHeader;
import com.bmos.file.docx.model.DocxHeaderFooterItem;
import com.bmos.file.docx.util.HtmlUtil;
import com.bmos.file.docx.util.WordUtil;
import com.bmos.mes.common.enums.plan.ProductPlanStartEnum;
import com.bmos.mes.common.enums.record.BasicComponentTypeEnum;
import com.bmos.mes.common.enums.record.BusinessComponentTypeEnum;
import com.bmos.mes.common.exception.MesResponseCode;
import com.bmos.mes.common.model.execute.AcquisitionPictureExtInfo;
import com.bmos.mes.service.config.async.AsyncTaskComponent;
import com.bmos.mes.service.config.minio.MinioProperties;
import com.bmos.mes.service.execute.dto.IntactMergeListQueryDTO;
import com.bmos.mes.service.execute.mapper.ExecuteSubsidiaryRecordMapper;
import com.bmos.mes.service.execute.model.ExecuteAttachment;
import com.bmos.mes.service.execute.model.ExecuteFormData;
import com.bmos.mes.service.execute.model.ExecuteRecordCopy;
import com.bmos.mes.service.execute.model.ExecuteSubsidiaryRecord;
import com.bmos.mes.service.execute.service.ExecuteAttachmentService;
import com.bmos.mes.service.execute.service.ExecuteFormDataService;
import com.bmos.mes.service.execute.service.ExecuteRecordCopyService;
import com.bmos.mes.service.execute.vo.IntactFormDataVO;
import com.bmos.mes.service.plan.document.convert.BatchRecordArchiveConverter;
import com.bmos.mes.service.plan.document.service.ArchivedRecordItem;
import com.bmos.mes.service.plan.document.service.IPlanArchiveService;
import com.bmos.mes.service.plan.document.service.ProcessStepArchiveContext;
import com.bmos.mes.service.plan.info.model.Plan;
import com.bmos.mes.service.plan.info.service.PlanService;
import com.bmos.mes.service.process.constant.ProcessConstant;
import com.bmos.mes.service.process.model.ProcedureModel;
import com.bmos.mes.service.process.model.ProcessRecordOrder;
import com.bmos.mes.service.process.service.ProcedureModelService;
import com.bmos.mes.service.process.service.ProcedureStepModelService;
import com.bmos.mes.service.process.service.ProcessRecordOrderService;
import com.bmos.mes.service.record.dto.RecordStyleDTO;
import com.bmos.mes.service.record.model.BatchRecordComponent;
import com.bmos.mes.service.record.model.BatchRecordItem;
import com.bmos.mes.service.record.service.BatchRecordComponentService;
import com.bmos.mes.service.record.service.BatchRecordItemService;
import com.bmos.mes.service.utils.PlanArchivePathUtil;
import com.bmos.mes.service.utils.UserUtils;
import com.google.common.collect.Lists;
import io.minio.GetObjectArgs;
import io.minio.GetObjectResponse;
import io.minio.MinioClient;
import io.minio.UploadObjectArgs;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.awt.*;
import java.io.*;
import java.util.List;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 计划归档服务实现
 *
 * @author yigaohui
 * @date 2024/6/6
 **/
@Service
@Slf4j
public class PlanArchiveServiceImpl implements IPlanArchiveService {


    private static final String PNG_BASE64_PREFIX = "data:image/png;base64,";

    private static final String WATER_MARK_DISCARD = "已作废";

    private static final String WATER_MARK_COPY = "副本";

    private static final Integer DEFAULT_PAGE_NUMBER_STYLE = 0;

    private static final Integer DEFAULT_PAGE_START_NUMBER = 1;

    @Value("${minio.appDownloadUrl}")
    private String downloadUrl;

    @Resource
    private PlanService planService;

    @Resource
    private ProcedureStepModelService procedureStepModelService;

    @Resource
    private ProcedureModelService procedureModelService;

    @Resource
    private BatchRecordItemService recordItemService;

    @Resource
    private ProcessRecordOrderService processRecordOrderService;

    @Resource
    private AsyncTaskComponent asyncTaskComponent;

    @Resource
    private ExecuteFormDataService formDataService;

    @Autowired
    private ExecuteAttachmentService executeAttachmentService;

    @Autowired
    private MinioClient minioClient;

    @Autowired
    private MinioProperties minioProperties;

    @Autowired
    private BatchRecordComponentService batchRecordComponentService;

    @Autowired
    private ExecuteSubsidiaryRecordMapper executeSubsidiaryRecordMapper;

    @Autowired
    private ExecuteRecordCopyService executeRecordCopyService;

//    @Value("${debug}")
    private boolean debug;


    /**
     * 计划归档
     *
     * @param planId 计划id
     * @return 归档是否成功
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean archive(Long planId) {
        Plan plan = planService.getById(planId);
        if (plan == null) {
            throw new BmosException(MesResponseCode.PRODUCT_PLAN_NOT_EXISTS);
        }
        if (plan.getStart() != ProductPlanStartEnum.END && plan.getStart() != ProductPlanStartEnum.TERMINATION) {
            throw new BmosException(MesResponseCode.PLAN_ARCHIVE_NOT_EXIST);
        }
        IntactMergeListQueryDTO queryDTO = new IntactMergeListQueryDTO();
        queryDTO.setProductPlanId(planId);
        queryDTO.setProcessId(plan.getProcessId());
        queryDTO.setProcessVersion(plan.getProcessVersion());
        // 找到计划使用的记录项
        // 排除功能为辅助记录的步骤 辅助记录需要单独归档
        List<IntactFormDataVO> records = procedureStepModelService.getRecordContents(queryDTO);
        List<ExecuteSubsidiaryRecord> subsidiaryRecords = executeSubsidiaryRecordMapper.selectByProductPlanId(planId);
        Set<Long> versionIds = CollectionUtils.convertSet(records, IntactFormDataVO::getRecordVersionId);
        versionIds.addAll(CollectionUtils.convertSet(subsidiaryRecords, ExecuteSubsidiaryRecord::getRecordVersionId));
        List<ArchivedRecordItem> archivedRecordItems;
        try {
            ProcessStepArchiveContext context = getContext(versionIds, plan);
            archivedRecordItems = asyncTaskComponent.listPartition(records, 20,
                    context, this::generateArchiveWord);
            this.assembleCompletePdf(context, archivedRecordItems);
            this.handleSubRecordArchived(planId, subsidiaryRecords, context);
        } catch (Exception e) {
            log.error("归档失败", e);
            throw new BmosException(MesResponseCode.PLAN_ARCHIVE_ERROR);
        } finally {
            String filePath = PlanArchivePathUtil.getTempFilePath(plan.getId());
            FileUtil.del(filePath);
        }
        return true;
    }

    /**
     * 处理辅助记录归档
     *
     * @param planId
     * @param subsidiaryRecords
     * @param context
     */
    private void handleSubRecordArchived(Long planId, List<ExecuteSubsidiaryRecord> subsidiaryRecords,
                                         ProcessStepArchiveContext context) {
        if (CollUtil.isNotEmpty(subsidiaryRecords)) {
            List<BatchRecordItem> items =
                    recordItemService.queryByItemIdsAndVersionIds(CollectionUtils.convertList(subsidiaryRecords,
                            ExecuteSubsidiaryRecord::getRecordItemId), CollectionUtils.convertList(subsidiaryRecords,
                            ExecuteSubsidiaryRecord::getRecordVersionId));
            Map<Long, BatchRecordItem> itemMap = CollectionUtils.convertMap(items, BatchRecordItem::getItemId);
            List<ExecuteRecordCopy> listByRecordItemIds = executeRecordCopyService.getListByRecordItemIds(planId,
                    CollectionUtils.convertList(subsidiaryRecords, ExecuteSubsidiaryRecord::getRecordItemId));
            Map<Long, Map<Long, Map<Integer, Map<Integer, List<ExecuteRecordCopy>>>>> copyRecordMap =
                    listByRecordItemIds.stream()
                            .collect(Collectors.groupingBy(ExecuteRecordCopy::getRecordItemId,
                                    Collectors.groupingBy(ExecuteRecordCopy::getProcedureStepId,
                                            Collectors.groupingBy(ExecuteRecordCopy::getProcessChangeNumber,
                                                    Collectors.groupingBy(ExecuteRecordCopy::getProcedureChangeNumber)))));
            subsidiaryRecords.parallelStream().forEach(record -> {
                List<ExecuteRecordCopy> list = copyRecordMap.getOrDefault(record.getRecordItemId(), new HashMap<>())
                        .getOrDefault(record.getReuse() ? ProcessConstant.REUSE_PROCEDURE_STEP_ID : record.getProcedureStepId(), new HashMap<>())
                        .getOrDefault(record.getProcessChangeNumber(), new HashMap<>())
                        .getOrDefault(record.getProcedureChangeNumber(), new ArrayList<>());
                if (CollUtil.isNotEmpty(list)) {
                    List<IntactFormDataVO> intactFormDataVOS = BatchRecordArchiveConverter.INSTANCE.convert2IntactFormDataVO(list, itemMap, record);
                    try {
                        List<ArchivedRecordItem> subArchivedItems = generateArchiveWord(intactFormDataVOS, context);
                        assembleSubRecordCompletePdf(context, subArchivedItems, record);
                    } catch (Exception e) {
                        log.error("计划【{}】辅助记录【{}】归档失败", context.getPlan().getId(), record.getId(), e);
                    }
                }
            });
        }
    }

    /**
     * - 复用的记录与班次的关系是
     * - 假设有A、B、C三个记录，1，2，3三个班次，记录A复用
     * - bm_execute_record_copy表中的记录是
     * - 班次1如果进入了A，会有一条记录；班次2如果进入了A，则不会有记录，会将班次1记录的A返回
     * - 实际归档的要求是
     * - 复用的记录只归档一份，那么对于A记录，只在最早进入该记录的班次的位置归档一份。归档的记录A中的数据可能是班次1填的也可能是班次2填的
     * - 记录拷贝
     * - 不论是在哪个班次进行拷贝，归档的记录一定是记录第一次出现的班次中
     * - bm_execute_record_copy表中的记录是
     * - 班次1进行复制，会有一条记录，班次2进行复制也会有一条记录
     * - 总结
     * - 不论哪个班次首先开始做复用的记录，也不管哪个班次进行了复制，所有班次看到都能看到该记录的所有复制，归档时将所有的复制归档在第一次进入该记录的班次
     * - 不复用的记录与班次的关系是
     * - 假设有A、B、C三个记录，1，2，3三个班次，记录B复用
     * - bm_execute_record_copy表中的记录是
     * - 如果班次1没有进入A进行作业，班次2进入了A进行了作业，那么班次1不会有记录，班次2会有一条记录
     * - 如果班次1进入了A进行作业，班次2也进入了A进行作业，那么班次1和班次2都会有一条记录
     * - 实际的归档要求是
     * - 不复用的记录每个进行了作业的班次都进行归档
     * - 记录拷贝
     * - 在哪个班次进行拷贝的，归档的记录就跟在哪个班次
     * - 在bm_execute_record_copy表中，version是单个计划、单个记录全局递增的
     * - 对于复用的记录，根据该表拿出的数据，只需要关心version最小值是哪个班次的，然后将该记录的所有复制归档到该班次
     * - 对于不复用的记录，根据该表拿出的数据，需要按照班次进行分组，将属于同一班次的记录和复制归档在该班次
     * <p>
     * <p>
     * <p>
     * - form_data表的处理方式
     * - 记录项在bm_execute_record_copy表中
     * - form_data的查询只关心copyversion的值，同一个计划中的同一个field_id的值用这个字段的值来区分数据应该属于哪个班次哪次复制
     * <p>
     * <p>
     * 批记录的归档顺序：工艺班次>工序班次>归档顺序>记录复制，都是按升序排序，记录复用的情况按记录首个班次的进行排序
     *
     * @param context
     * @param archivedRecordItems
     * @throws Exception
     */
    private void assembleCompletePdf(ProcessStepArchiveContext context, List<ArchivedRecordItem>
            archivedRecordItems) throws Exception {
        List<ArchivedRecordItem> recordItems = this.sortArchiveRecordItems(context, archivedRecordItems);
        Document mergeDocument = new Document();
        mergeDocument.getFirstSection().remove();
        for (ArchivedRecordItem item : recordItems) {
            Document document = item.getDocument();
            this.addWaterMark(item);
            mergeDocument.appendDocument(document, ImportFormatMode.KEEP_SOURCE_FORMATTING);
        }
        File file = new File(PlanArchivePathUtil.getTempFilePath(context.getPlan().getId()),
                PlanArchivePathUtil.getPlanArchiveFileName(context.getPlan().getId()));

        File file_doc = new File(PlanArchivePathUtil.getTempFilePath(context.getPlan().getId()),
                PlanArchivePathUtil.getPlanArchiveDoxFileName(context.getPlan().getId()));
        try (OutputStream outputStream =
                     new FileOutputStream(file)  ;OutputStream outputStream_doc =new FileOutputStream(file_doc)) {
            mergeDocument.save(outputStream_doc, SaveFormat.DOCX);
//            mergeDocument.save(outputStream, SaveFormat.PDF);
            // 重新读一下保存的doc文件进行pdf转换
            mergeDocument = new Document(file_doc.getAbsolutePath());
            mergeDocument.save(outputStream, SaveFormat.PDF);
            this.save2Minio(PlanArchivePathUtil.getRelativePath(context.getPlan().getId()), file_doc);
            this.save2Minio(PlanArchivePathUtil.getRelativePath(context.getPlan().getId()), file);
        } catch (Exception e) {
            log.error("计划【{}】归档失败", context.getPlan().getId(), e);
            throw new BmosException(MesResponseCode.PLAN_ARCHIVE_ERROR);
        }
    }

    /**
     * * 批记录的归档顺序：工艺班次>工序顺序>工序班次>归档顺序>记录复制，都是按升序排序，记录复用的情况按记录首个班次的进行排序
     * <p>
     * 不复用
     * 记录项：	ABCDEF
     * 归档顺序：BCFADE
     * 作业顺序：CBA | FD || E
     * 换班：|工艺换班 ||工序换班
     * 最终归档：BCA|BCFAD|FD|FD|E
     * <p>
     * <p>
     * 实现：
     * 1.按照工艺换班的顺序进行第一次排序
     * 2.在同一工艺内，按照归档顺序第2次排序
     * 3.记录相同按照复制版本排序
     * 特殊情况：
     * 记录是复用且被不同工艺换班复制了，同一份记录也应该放在一起
     * <p>
     * 归档顺序在前的如果在靠后的班次做，则归档也归在后面@lilong规定的
     *
     * @param context
     * @param archivedRecordItems
     * @return
     */
    private List<ArchivedRecordItem> sortArchiveRecordItems(ProcessStepArchiveContext context, List<ArchivedRecordItem> archivedRecordItems) {
        Map<Integer, List<ArchivedRecordItem>> processChangeMap = archivedRecordItems.stream().collect(Collectors.groupingBy(ArchivedRecordItem::getProcessChangeNumber));
        Map<Long, List<ArchivedRecordItem>> recordMap = archivedRecordItems.stream().collect(Collectors.groupingBy(ArchivedRecordItem::getRecordItemId));
        // 先按照工艺换班次数进行排序
        Map<Long, List<ArchivedRecordItem>> copyVersionMap = archivedRecordItems.stream().collect(Collectors.groupingBy(ArchivedRecordItem::getCopyVersionId));
        Set<Integer> processChangeNumbers = processChangeMap.keySet();
        List<Integer> sortedProcessChangeNumbers = processChangeNumbers.stream().sorted(Comparator.comparing(Integer::intValue)).collect(Collectors.toList());
        Map<Long, ProcedureModel> procedureModelMap = context.getProcedureModelMap();
        List<ArchivedRecordItem> res = new ArrayList<>();
        for (Integer processChangeNumber : sortedProcessChangeNumbers) {
            List<ArchivedRecordItem> processChangeItems = processChangeMap.get(processChangeNumber);
            // 按照工序顺序进行遍历
            Map<Long, List<ArchivedRecordItem>> procedureMap = processChangeItems.stream().collect(Collectors.groupingBy(ArchivedRecordItem::getProcedureModelId));
            Set<Long> procedureModeId = procedureMap.keySet();
            List<Long> sortedProcedureId = procedureModeId.stream().sorted(Comparator.comparing(id -> procedureModelMap.get(id).getSort())).collect(Collectors.toList());
            sortedProcedureId.forEach(procedureModelId -> {
                List<ArchivedRecordItem> procedureItems = procedureMap.get(procedureModelId);
                // 再按照工序换班排序
                Map<Integer, List<ArchivedRecordItem>> procedureChangeMap = procedureItems.stream().collect(Collectors.groupingBy(ArchivedRecordItem::getProcedureChangeNumber));
                Set<Integer> procedureChangeNumberSet = procedureChangeMap.keySet();
                List<Integer> procedureChangeNumbers = procedureChangeNumberSet.stream().sorted(Comparator.comparing(Integer::intValue)).collect(Collectors.toList());
                for (Integer procedureChangeNumber : procedureChangeNumbers) {
                    List<ArchivedRecordItem> procedureChangeItems = procedureChangeMap.get(procedureChangeNumber);
                    procedureChangeItems.sort(Comparator.comparing(ArchivedRecordItem::getItemConfigArchiveOrder));
                    for (ArchivedRecordItem item : procedureChangeItems) {
                        // 如果该map中不存在了，则说明已经添加了，则跳过
                        if (!copyVersionMap.containsKey(item.getCopyVersionId())) {
                            continue;
                        }
                        // 如果是复用的话，需要找出整个计划的所有复制版本，放在这里
                        List<ArchivedRecordItem> recordItems = recordMap.getOrDefault(item.getRecordItemId(), new ArrayList<>());
                        if (item.getReuse()) {
                            List<ArchivedRecordItem> reuseCopy = recordItems.stream().filter(ArchivedRecordItem::getReuse)
                                    .sorted(Comparator.comparing(ArchivedRecordItem::getCopyVersion)).collect(Collectors.toList());
                            for (int i = 0; i < reuseCopy.size(); i++) {
                                ArchivedRecordItem archivedRecordItem = reuseCopy.get(i);
                                archivedRecordItem.setCopyItem(i >= 1);
                                copyVersionMap.remove(archivedRecordItem.getCopyVersionId());
                                res.add(archivedRecordItem);
                            }
                        }
                        //不复用，严格按照归档顺序排序
                        else {
                            // 找到这次换班的这个步骤的所有复制
                            List<ArchivedRecordItem> reuseCopy = recordItems.stream().filter(archivedRecordItem ->
                                    !archivedRecordItem.getReuse() && archivedRecordItem.getProcedureStepId().equals(item.getProcedureStepId())
                                            && archivedRecordItem.getProcessChangeNumber().equals(item.getProcessChangeNumber())
                                            && archivedRecordItem.getProcedureChangeNumber().equals(item.getProcedureChangeNumber()))
                                    .sorted(Comparator.comparing(ArchivedRecordItem::getCopyVersion)).collect(Collectors.toList());
                            for (int i = 0; i < reuseCopy.size(); i++) {
                                ArchivedRecordItem archivedRecordItem = reuseCopy.get(i);
                                archivedRecordItem.setCopyItem(i >= 1);
                                copyVersionMap.remove(archivedRecordItem.getCopyVersionId());
                                res.add(archivedRecordItem);
                            }
                        }
                    }
                }
            });
        }
        log.debug("一共【{}】条记录", res.size());
        log.debug(copyVersionMap.size() > 0 ? "归档没有完成" : "归档完成了");
        return res;
    }

    /**
     * 处理辅助记录pdf保存
     *
     * @param context
     * @param archivedRecordItems
     * @param record
     * @throws Exception
     */
    private void assembleSubRecordCompletePdf(ProcessStepArchiveContext context, List<ArchivedRecordItem>
            archivedRecordItems, ExecuteSubsidiaryRecord record) throws Exception {
        archivedRecordItems.sort(Comparator.comparing(ArchivedRecordItem::getCopyVersion));
        Document mergeDocument = new Document();
        mergeDocument.getFirstSection().remove();
        for (ArchivedRecordItem item : archivedRecordItems) {
            Document document = item.getDocument();
            mergeDocument.appendDocument(document, ImportFormatMode.KEEP_SOURCE_FORMATTING);
        }
        File file = new File(PlanArchivePathUtil.getTempFilePath(context.getPlan().getId()),
                PlanArchivePathUtil.getPlanArchiveFileName(record.getId()));
        try (OutputStream outputStream =
                     new FileOutputStream(file)) {
            mergeDocument.save(outputStream, SaveFormat.PDF);
            this.save2Minio(PlanArchivePathUtil.getRelativePath(context.getPlan().getId()), file);
            record.setArchiveUrl(PlanArchivePathUtil.getRelativePath(context.getPlan().getId()) + StrUtil.SLASH + file.getName());
            executeSubsidiaryRecordMapper.updateById(record);
        } catch (Exception e) {
            log.error("计划【{}】辅助记录【{}】归档失败", context.getPlan().getId(), record.getId(), e);
            throw new BmosException(MesResponseCode.PLAN_ARCHIVE_ERROR);
        }

    }
    private void mergeHeaderFooter(ProcessStepArchiveContext context, Document document,
                                   BatchRecordItem recordItem, List<ExecuteFormData> data,
                                   List<ImageVO> downloadImages) {
        String headerJson = recordItem.getDocxHeader();
        String footerJson = recordItem.getDocxFooter();
        DocxHeader header = StringUtils.isEmpty(headerJson) ? null : JSONUtil.toBean(headerJson, DocxHeader.class);
        DocxFooter footer = StringUtils.isEmpty(footerJson) ? null : JSONUtil.toBean(footerJson, DocxFooter.class);
        if (header == null && footer == null) {
            return;
        }
        HeaderFooterHelper.setHeaderFooter(context, recordItem, data, downloadImages, document, header, footer);
    }

    private ProcessStepArchiveContext getContext(Set<Long> recordVersionIds, Plan plan) {
        //排序
        List<ProcessRecordOrder> orders = processRecordOrderService.getRecordItems(plan.getProcessId(),
                plan.getProcessVersion());
        //附件
        List<ExecuteAttachment> attachments = executeAttachmentService.getListByProductPlanId(plan.getId());
        //记录项信息
        List<BatchRecordItem> processRecordItemVOS =
                recordItemService.queryItemListByVersionIdList(Lists.newArrayList(recordVersionIds));
        //工序列表
        List<ProcedureModel> procedureModels = procedureModelService.getByProcessIdAndVersion(plan.getProcessId(), plan.getProcessVersion());
        return new ProcessStepArchiveContext(plan, procedureModels, processRecordItemVOS, orders, attachments);
    }


    public static void main(String[] args) throws Exception {

        FileInputStream fileInputStream = new FileInputStream("D://test/license.xml");
        License license = new License();
        license.setLicense(fileInputStream);

//        String read1 = IoUtil.read(new FileReader(new File("D://test/test.html")));
//        String read2 = IoUtil.read(new FileReader(new File("D://test/test2.html")));
//        String read3 = IoUtil.read(new FileReader(new File("D://test/test3.html")));
//        File file1 = WordUtil.convertHtml2docx("D://test/", "1.docx", read1);
//        File file2 = WordUtil.convertHtml2docx("D://test/", "2.docx", read2);
//        File file3 = WordUtil.convertHtml2docx("D://test/", "3.docx", read3);
//
//
//        Document document1 = new Document(new FileInputStream(file1));
//        Document document2 = new Document(new FileInputStream(file2));
//        Document document3 = new Document(new FileInputStream(file3));
//
//        PageSetup pageSetup1 = document1.getFirstSection().getPageSetup();
//        pageSetup1.setOrientation(Orientation.LANDSCAPE);
//        pageSetup1.setPaperSize(PaperSize.A4);
//
//        PageSetup pageSetup2 = document2.getFirstSection().getPageSetup();
//        pageSetup2.setOrientation(Orientation.LANDSCAPE);
//        pageSetup2.setPaperSize(PaperSize.A4);
//
//        PageSetup pageSetup3 = document3.getFirstSection().getPageSetup();
//        pageSetup3.setOrientation(Orientation.LANDSCAPE);
//        pageSetup3.setPaperSize(PaperSize.A4);
//        document1.save("D://test/1-c.docx", SaveFormat.DOCX);
//        document2.save("D://test/2-c.docx", SaveFormat.DOCX);
//        document3.save("D://test/3-c.docx", SaveFormat.DOCX);


//
//        File file1 = new File("D://test/1942571984508555264-0-0-0-0.docx");
//        File file2 = new File("D://test/1942571984508555265-0-0-0-0.docx");
//        File file3 = new File("D://test/1942571984508555266-0-0-0-0.docx");
//        Document document1 = new Document(new FileInputStream(file1));
//        document1.save("D://test/1.pdf", SaveFormat.PDF);
//        Document document2 = new Document(new FileInputStream(file2));
//        document2.save("D://test/2.pdf", SaveFormat.PDF);
//        Document document3 = new Document(new FileInputStream(file3));
//        document3.save("D://test/3.pdf", SaveFormat.PDF);

//        Document mergeDocument = new Document();
//        mergeDocument.getFirstSection().remove();
//        mergeDocument.appendDocument(document1, ImportFormatMode.KEEP_SOURCE_FORMATTING);
//        mergeDocument.appendDocument(document2, ImportFormatMode.KEEP_SOURCE_FORMATTING);
//        mergeDocument.appendDocument(document3, ImportFormatMode.KEEP_SOURCE_FORMATTING);
//        mergeDocument.save("D://test/4.pdf", SaveFormat.PDF);

//        File file = new File("D://test/1943513610949103616.docx");
//        Document document = new Document(new FileInputStream(file));
//        document.save("D://test/5.pdf", SaveFormat.PDF);

    }

    private List<ArchivedRecordItem> generateArchiveWord(List<IntactFormDataVO> ts, ProcessStepArchiveContext context) {
        // 获取form数据
        Plan plan = context.getPlan();
        Set<Long> recordItemIds = ts.stream().map(IntactFormDataVO::getRecordItemId).collect(Collectors.toSet());
        List<ExecuteFormData> dataList = formDataService.getDataByPlanAndItemIds(plan.getId(), recordItemIds);
        Map<Long, Map<Long, Map<Long, List<ExecuteAttachment>>>> attachmentMap = context.getAttachmentMap();
        Map<Long, Map<Long, Map<Long, List<ExecuteFormData>>>> dataMap =
                dataList.stream().collect(Collectors.groupingBy(ExecuteFormData::getRecordItemId,
                        Collectors.groupingBy(ExecuteFormData::getProcedureStepId,
                                Collectors.groupingBy(ExecuteFormData::getCopyVersion))));
        //填充
        List<ArchivedRecordItem> res = new ArrayList<>();
        ts.forEach(intactFormDataVO -> {
            List<ExecuteFormData> data = dataMap.getOrDefault(intactFormDataVO.getRecordItemId(), new HashMap<>())
                    .getOrDefault(intactFormDataVO.getProcedureStepId(), new HashMap<>()).getOrDefault(intactFormDataVO.getCopyVersion(),
                            new ArrayList<>());
            List<ExecuteAttachment> attachments = attachmentMap.getOrDefault(intactFormDataVO.getRecordItemId(),
                    new HashMap<>())
                    .getOrDefault(intactFormDataVO.getProcedureStepId(), new HashMap<>()).getOrDefault(intactFormDataVO.getCopyVersion(),
                            new ArrayList<>());
            //将附件下载下来，这里面包含了拍照取证的图片和拍照组件的图片
            List<ImageVO> downloadImages = this.downloadImages(attachments);
            List<FieldValueDTO> fieldValueDTOS = BeanUtil.copyToList(data,
                    FieldValueDTO.class);
            //处理手动签名数据
            this.handleSignature(fieldValueDTOS);
            // 处理数采绘图组件
            this.handleEquipmentPicture(fieldValueDTOS);
            // 需要单独处理拍照组件的图片
            this.handlePicComponent(data, fieldValueDTOS, downloadImages);
            long startMerge = System.currentTimeMillis();
            List<BatchRecordComponent> itemComponentList = this.getItemComponent(intactFormDataVO.getRecordItemId(),
                    intactFormDataVO.getRecordVersionId());
            context.putItemComponent(intactFormDataVO.getRecordItemId(), itemComponentList);
            String mergeHtml = HtmlUtil.mergeHtml(intactFormDataVO.getFileContent(),
                    BeanUtil.copyToList(itemComponentList, ComponentDTO.class),
                    fieldValueDTOS,
                    downloadImages);
            log.info("【{}】 记录项html合并数据记录耗时【{}】ms", intactFormDataVO.getRecordItemId(),
                    System.currentTimeMillis() - startMerge);
            long startConvert = System.currentTimeMillis();
            File file = WordUtil.convertHtml2docx(PlanArchivePathUtil.getTempFilePath(plan.getId()),
                    PlanArchivePathUtil.getFileName(intactFormDataVO.getRecordItemId(), intactFormDataVO.getProcedureStepId(), intactFormDataVO.getProcessChangeNumber(), intactFormDataVO.getProcedureChangeNumber(),
                            intactFormDataVO.getCopyVersion()),
                    mergeHtml);
            log.info("【{}】文件转换word耗时【{}】ms", file.getAbsolutePath(), System.currentTimeMillis() - startConvert);
            try (InputStream inputStream = new FileInputStream(file)) {
                Document document = new Document(inputStream);
                long startMergeHeaderFooter = System.currentTimeMillis();
                BatchRecordItem recordItem = context.getItemsMap().get(intactFormDataVO.getRecordItemId());
                // 图片的值不放入到html末尾，在data value中已经处理了组件的图片
                this.mergeHeaderFooter(context, document, recordItem, data, null);
                log.info("【{}】word文件合并页眉页脚耗时【{}】ms", file.getAbsolutePath(),
                        System.currentTimeMillis() - startMergeHeaderFooter);
                this.documentPageSetUp(document, recordItem);
                long startUpload = System.currentTimeMillis();
                // 再次保存一次文件，这次是添加了页面设置的文件
                document.save(file.getAbsolutePath(), SaveFormat.DOCX);
                this.save2Minio(PlanArchivePathUtil.getRelativePath(plan.getId()), file);
                log.info("【{}】word文件上传耗时【{}】ms", file.getAbsolutePath(), System.currentTimeMillis() - startUpload);
                ArchivedRecordItem archivedRecordItem = new ArchivedRecordItem()
                        .setDocument(document)
                        .setPlanId(plan.getId())
                        .setRecordItemId(intactFormDataVO.getRecordItemId())
                        .setCopyVersion(intactFormDataVO.getCopyVersion())
                        .setWordUrl(PlanArchivePathUtil.getTempFilePath(plan.getId()) + File.separator + file.getName())
                        .setProcedureStepModelId(intactFormDataVO.getProcedureStepModelId())
                        .setProcedureStepId(intactFormDataVO.getProcedureStepId())
                        .setReuse(intactFormDataVO.getReuse())
                        .setProcessChangeNumber(intactFormDataVO.getProcessChangeNumber())
                        .setProcedureChangeNumber(intactFormDataVO.getProcedureChangeNumber())
                        .setDiscard(intactFormDataVO.getDiscard())
                        .setProcedureModelId(intactFormDataVO.getProcedureModelId())
                        .setProcedureId(intactFormDataVO.getProcedureId())
                        .setCopyVersionId(intactFormDataVO.getCopyVersionId());
                Map<Long, Map<Long, Long>> orderMap = context.getOrderMap();
                Long order = orderMap.getOrDefault(intactFormDataVO.getRecordItemId(), new HashMap<>()).getOrDefault(intactFormDataVO.getReuse() ? 0L :
                        intactFormDataVO.getProcedureStepModelId(), intactFormDataVO.getProcedureStepModelId());
                archivedRecordItem.setItemConfigArchiveOrder(order);
                res.add(archivedRecordItem);
            } catch (Exception e) {
                log.error("检查项【{}】、拷贝【{}】生成归档文件失败", intactFormDataVO.getRecordItemId(),
                        intactFormDataVO.getCopyVersion(), e);
                throw new BmosException(MesResponseCode.PLAN_ARCHIVE_ERROR);
            }
        });
        return res;
    }

    /**
     * 将设备数采绘图组件处理成base64
     *
     * @param fieldValueDTOS
     */
    private void handleEquipmentPicture(List<FieldValueDTO> fieldValueDTOS) {
        List<FieldValueDTO> list = fieldValueDTOS.stream()
                .filter(formData -> StrUtil.equals(formData.getComponentType(), BusinessComponentTypeEnum.EQUIPMENT_DATA_DRAW.getValue()))
                .collect(Collectors.toList());
        if (CollectionUtil.isEmpty(list)) {
            return;
        }
        list.forEach(draw -> {
            try {
                AcquisitionPictureExtInfo parse = JsonUtils.parseObject(draw.getValue(), AcquisitionPictureExtInfo.class);
                if (parse != null && StrUtil.isNotEmpty(parse.getUrl())) {
                    String path = StrUtil.subAfter(parse.getUrl(), StrUtil.C_SLASH, false);
                    GetObjectResponse minioClientObject =
                            minioClient.getObject(GetObjectArgs.builder().bucket(minioProperties.getBuckets()
                                    .getProduct()).object(path).build());
                    byte[] bytes = IoUtil.readBytes(minioClientObject, true);
                    String encode = Base64.encode(bytes);
                    ImageVO imageVO = new ImageVO();
                    imageVO.setValue(PNG_BASE64_PREFIX + encode);
                    String format = "设备信息：%s    设备数据：%s    采集人：%s    采集时间：%s    ";
                    imageVO.setImageCaption(String.format(format, parse.getEquipmentInfo(), parse.getEquipmentData(), parse.getAcquisitionUser(), parse.getAcquisitionTime()));
                    draw.setImgs(Lists.newArrayList(imageVO));
                }
            } catch (Exception e) {
                log.error("从minio上获取文件【{}】失败", draw.getValue(), e);
            }
        });

    }

    /**
     * 处理拍照组件
     * <p>
     * 拍照组件既需要在当前位置显示，也需要在本节末尾显示
     *
     * @param data           数据
     * @param fieldValueDTOS
     * @param downloadImages 已经下载的图片
     */
    private void handlePicComponent(List<ExecuteFormData> data, List<FieldValueDTO> fieldValueDTOS, List<ImageVO> downloadImages) {
        List<ExecuteFormData> pictureFormData = CollectionUtils.filterList(data, item ->
                StrUtil.equals(item.getComponentType(), BasicComponentTypeEnum.PHOTO.getValue()));
        List<FieldValueDTO> res = new ArrayList<>();
        pictureFormData.forEach(formData -> {
            if (StrUtil.isNotBlank(formData.getValue())) {
                FieldValueDTO fieldValueDTO = new FieldValueDTO();
                BeanUtil.copyProperties(formData, fieldValueDTO);
                List<String> split = StrUtil.split(fieldValueDTO.getValue(), StrUtil.C_COMMA);
                List<ImageVO> imageVOS = new ArrayList<>();
                for (String s : split) {
                    Optional<ImageVO> downloadImage = downloadImages.stream().filter(item1 -> StrUtil.equals(item1.getAttachmentId() + "", s)).findFirst();
                    if (!downloadImage.isPresent()) {
                        continue;
                    }
                    ImageVO imageVO = new ImageVO();
                    imageVO.setEvidenceName(downloadImage.get().getEvidenceName());
                    imageVO.setEvidenceTime(downloadImage.get().getEvidenceTime());
                    imageVO.setValue(downloadImage.get().getValue());
                    imageVO.setImageCaption(downloadImage.get().getImageCaption());
                    imageVOS.add(imageVO);
                }
                fieldValueDTO.setImgs(imageVOS);
                res.add(fieldValueDTO);
            }
        });
        //返回时排除拍照上传组件原值
        fieldValueDTOS.removeIf(item -> CollectionUtils.convertList(pictureFormData,
                ExecuteFormData::getFieldId).contains(item.getFieldId()));
        fieldValueDTOS.addAll(res);
    }

    private void addWaterMark(ArchivedRecordItem item) {
        // 添加作废的水印
        if (BooleanUtil.isTrue(item.getDiscard())) {
            this.addWaterMark(item.getDocument(), WATER_MARK_DISCARD, 50, new Color(255, 76, 38));
        }
        // 添加副本的水印
        if (item.getCopyItem()) {
            this.addWaterMark(item.getDocument(), WATER_MARK_COPY, 175, new Color(40, 113, 255));
        }
        if (debug) {
            // 添加班次水印
            String processChangeWaterMark = "工艺班次：" + (item.getProcessChangeNumber() + 1);
            this.addWaterMark(item.getDocument(), processChangeWaterMark, 275, new Color(255, 176, 38));
            // 添加班次水印
            String procedureChangeWaterMark = "工序班次：" + (item.getProcedureChangeNumber() + 1);
            this.addWaterMark(item.getDocument(), procedureChangeWaterMark, 375, new Color(188, 176, 138));
        }
    }

    private void addWaterMark(Document document, String waterMarkDiscard, int left, Color color) {
        try {
            Shape watermarkShape = new Shape(document, ShapeType.TEXT_BOX);
            watermarkShape.setWidth(500); // 水印宽度
            watermarkShape.setHeight(100); // 水印高度
            // 创建一个段落，并设置水印文本
            Shape watermark = new Shape(document, ShapeType.TEXT_PLAIN_TEXT);
            //1水印内容
            watermark.getTextPath().setText(waterMarkDiscard);
            //2水印字体
            watermark.getTextPath().setFontFamily("宋体");
            //3水印宽度
            watermark.setWidth(100);
            //4水印高度
            watermark.setHeight(20);
            //6水印颜色 浅灰色
            watermark.getFill().setColor(color);
            watermark.setStrokeColor(color);
            //7将水印放置在页面中心
            //7.1 设置相对水平位置
            watermark.setRelativeHorizontalPosition(RelativeHorizontalPosition.RIGHT_MARGIN);
            //7.2 设置相对垂直位置
            watermark.setRelativeVerticalPosition(RelativeVerticalPosition.PAGE);
            //7.3 设置包装类型
            watermark.setWrapType(WrapType.NONE);
            //7.4 设置垂直对齐
            watermark.setVerticalAlignment(VerticalAlignment.TOP);
            watermark.setLeft(-left);
            //7.5 设置文本水平对齐方式
            // 将段落添加到文本框中（注意：Aspose.Words的TextBox可能不直接支持，这里使用文本框作为概念说明）
            // 实际上，你可能需要直接将文本或形状添加到页眉中，而不是先放入文本框
            Paragraph watermarkPara = new Paragraph(document);
            watermarkPara.appendChild(watermark);
            // 遍历文档的每个节，并在每个节的页眉中添加水印
            for (Section sect : document.getSections()) {
                insertWatermarkIntoHeader(watermarkPara, sect, HeaderFooterType.HEADER_PRIMARY);
                insertWatermarkIntoHeader(watermarkPara, sect, HeaderFooterType.HEADER_FIRST);
                insertWatermarkIntoHeader(watermarkPara, sect, HeaderFooterType.HEADER_EVEN);
            }
        } catch (Exception e) {
            log.error("添加水印失败", e);
        }

    }

    private static void insertWatermarkIntoHeader(Paragraph watermarkPara, Section sect, int headerType) {
        HeaderFooter header = sect.getHeadersFooters().getByHeaderFooterType(headerType);
        if (header == null) {
            header = new HeaderFooter(sect.getDocument(), headerType);
            sect.getHeadersFooters().add(header);
        }
        // 在头部插入一个水印的克隆
        header.appendChild(watermarkPara.deepClone(true));


    }

    private List<BatchRecordComponent> getItemComponent(Long recordItemId, Long recordVersionId) {
        return batchRecordComponentService.selectByVersionAndItem(recordVersionId, recordItemId);
    }

    /**
     * 页面设置
     *
     * @param document   文档
     * @param recordItem 记录项
     */
    private void documentPageSetUp(Document document, BatchRecordItem recordItem) {
        String pageConfig = recordItem.getPageConfig();
        RecordStyleDTO styleDTO = StringUtils.isEmpty(pageConfig) ? null : JSONUtil.toBean(pageConfig,
                RecordStyleDTO.class);
        if (styleDTO == null) {
            return;
        }
        Integer pattern = styleDTO.getPattern();
        // 横板
        if (0 == pattern) {
            // todo 暂时只设置为A4纸大小，原文档的纸张大小没有解析
            PageSetup pageSetup = document.getFirstSection().getPageSetup();
            pageSetup.setOrientation(Orientation.LANDSCAPE);
            pageSetup.setPaperSize(PaperSize.A4);
        }
        // 不允许表格自动调整
        document.getSections().forEach(section -> section.getBody().getTables().forEach(table -> {
            try {
                table.setAllowAutoFit(false);
            } catch (Exception e) {
                log.error("!!!设置表格不允许制动调整失败", e);
            }
        }));
    }

    /**
     * 处理签名组件数据转为base64
     *
     * @param fieldValueDTOS
     */
    private void handleSignature(List<FieldValueDTO> fieldValueDTOS) {
        List<FieldValueDTO> sinData = fieldValueDTOS.stream().filter(formData -> StrUtil.equals(formData.getComponentType(), BusinessComponentTypeEnum.HANDLE_REVIEW_SIGN.getValue()) ||
                StrUtil.equals(formData.getComponentType(),
                        BusinessComponentTypeEnum.HANDLE_SUBMIT_SIGN.getValue())).collect(Collectors.toList());
        if (CollectionUtil.isEmpty(sinData)) {
            return;
        }
        sinData.forEach(sin -> {
            try {
                String signBucket = minioProperties.getBuckets().getSign();
                if (sin.getValue().contains(signBucket)) {
                    String path = StrUtil.subAfter(sin.getValue(), StrUtil.C_SLASH, true);
                    GetObjectResponse minioClientObject =
                            minioClient.getObject(GetObjectArgs.builder().bucket(minioProperties.getBuckets()
                                    .getSign()).object(path).build());

                    byte[] bytes = IoUtil.readBytes(minioClientObject, true);
                    String encode = Base64.encode(bytes);
                    ImageVO imageVO = new ImageVO();
                    imageVO.setValue(PNG_BASE64_PREFIX + encode);
                    sin.setImgs(Lists.newArrayList(imageVO));
                }
            } catch (Exception e) {
                log.error("从minio上获取文件【{}】失败", sin.getValue(), e);
            }
        });
    }

    private List<ImageVO> downloadImages(List<ExecuteAttachment> attachments) {
        if (CollectionUtil.isEmpty(attachments)) {
            return new ArrayList<>();
        }
        List<ImageVO> base64Images = new ArrayList<>();
        attachments.forEach(executeAttachment -> {
            try {
                //判断附件中是否有拍照上传组件，存在进行处理
                String product = minioProperties.getBuckets().getProduct();
                if (executeAttachment.getPath().contains(product)) {
                    executeAttachment.setPath(StrUtil.subAfter(executeAttachment.getPath(), StrUtil.C_SLASH, true));
                }
                GetObjectResponse minioClientObject =
                        minioClient.getObject(GetObjectArgs.builder().bucket(minioProperties.getBuckets()
                                .getProduct()).object(executeAttachment.getPath()).build());
                byte[] bytes = IoUtil.readBytes(minioClientObject, true);
                String encode = Base64.encode(bytes);
                ImageVO vo = new ImageVO();
                vo.setAttachmentId(executeAttachment.getId());
                vo.setValue(PNG_BASE64_PREFIX + encode);
                vo.setEvidenceName(UserUtils.getUsername(executeAttachment.getCreateBy()));
                vo.setEvidenceTime(DateUtil.format(executeAttachment.getCreateTime(),
                        DatePattern.NORM_DATETIME_PATTERN));
                vo.setImageCaption(executeAttachment.getRemark());
                base64Images.add(vo);
            } catch (Exception e) {
                log.error("从minio上获取文件【{}】失败", executeAttachment.getPath(), e);
            }
        });
        return base64Images;
    }

    private void save2Minio(String filePath, File file) {
        try {
            UploadObjectArgs objectArgs =
                    UploadObjectArgs.builder().bucket(minioProperties.getBuckets().getArchive()).object(filePath +
                            "/" + file.getName()).filename(file.getAbsolutePath()).build();
            minioClient.uploadObject(objectArgs);
            log.info("【{}-{}】文件上传成功", objectArgs.bucket(), objectArgs.object());
        } catch (Exception e) {
            log.error("【{}】文件上传word文件失败", file.getAbsolutePath(), e);
            throw new BmosException(MesResponseCode.PLAN_ARCHIVE_ERROR);
        }
    }


    private static class HeaderFooterHelper {

        public static void setHeaderFooter(ProcessStepArchiveContext context, BatchRecordItem recordItem,
                                           List<ExecuteFormData> data, List<ImageVO> downloadImages, Document document,
                                           DocxHeader header,
                                           DocxFooter footer) {
            if (header != null && header.getHeaderFirst() != null && StringUtils.isNotEmpty(header.getHeaderFirst().getContent())) {
                addHead2Document(context, recordItem, data, downloadImages, document, header,
                        header.getHeaderFirst(),
                        HeaderFooterType.HEADER_FIRST);
            }
            if (header != null && header.getHeaderPrimary() != null && StringUtils.isNotEmpty(header.getHeaderPrimary().getContent())) {
                addHead2Document(context, recordItem, data, downloadImages, document, header, header.getHeaderPrimary(),
                        HeaderFooterType.HEADER_PRIMARY);
            }
            if (header != null && header.getHeaderEven() != null && StringUtils.isNotEmpty(header.getHeaderEven().getContent())) {
                addHead2Document(context, recordItem, data, downloadImages, document, header, header.getHeaderEven(),
                        HeaderFooterType.HEADER_EVEN);
            }
            if (footer != null && footer.getFooterFirst() != null && StringUtils.isNotEmpty(footer.getFooterFirst().getContent())) {
                addFooter2Document(context, recordItem, data, downloadImages, document, footer, footer.getFooterFirst(),
                        HeaderFooterType.FOOTER_FIRST);
            }
            if (footer != null && footer.getFooterPrimary() != null && StringUtils.isNotEmpty(footer.getFooterPrimary().getContent())) {
                addFooter2Document(context, recordItem, data, downloadImages, document, footer,
                        footer.getFooterPrimary(),
                        HeaderFooterType.FOOTER_PRIMARY);
            }
            if (footer != null && footer.getFooterEven() != null && StringUtils.isNotEmpty(footer.getFooterEven().getContent())) {
                addFooter2Document(context, recordItem, data, downloadImages, document, footer, footer.getFooterEven(),
                        HeaderFooterType.FOOTER_EVEN);
            }
        }

        public static void addHead2Document(ProcessStepArchiveContext context, BatchRecordItem recordItem,
                                            List<ExecuteFormData> data, List<ImageVO> images, Document document,
                                            DocxHeader header,
                                            DocxHeaderFooterItem headerItem, int headerFooterType) {
            List<BatchRecordComponent> componentList = context.getItemComponentsMap().getOrDefault(recordItem.getId(),
                    new ArrayList<>());
            String mergeHtml = HtmlUtil.mergeHtml(markPageNumber(headerItem.getContent()),
                    BeanUtil.copyToList(componentList, ComponentDTO.class),
                    BeanUtil.copyToList(data,
                            FieldValueDTO.class),
                    images);
            try (ByteArrayInputStream inputStream =
                         new ByteArrayInputStream(mergeHtml.getBytes())) {
                HtmlLoadOptions htmlLoadOptions = new HtmlLoadOptions();
                Document headerFooterDocument = new Document(inputStream, htmlLoadOptions);
                HeaderFooter headerFooter = new HeaderFooter(document, headerFooterType);
                NodeCollection childNodes =
                        headerFooterDocument.getFirstSection().getBody().getChildNodes(NodeType.ANY, false);
                childNodes.forEach(node -> {
                    Node importNode = document.importNode((Node) node, true, ImportFormatMode.KEEP_SOURCE_FORMATTING);
                    log.info(((Node) node).getNodeType() + "");
                    log.info(importNode.getText());
                    headerFooter.appendChild(importNode.deepClone(true));
                });
                headerFooter.isLinkedToPrevious(header.getLinkToPrevious());
                // 将页眉页脚的占位符替换成回真实文档中的内容
                replacePageNumber(recordItem, document, headerFooter, headerItem.getPageCodeHorizontalAlignment());
                document.getFirstSection().getHeadersFooters().add(headerFooter);
            } catch (Exception e) {
                throw new RuntimeException("页眉添加失败", e);
            }
        }

        private static String markPageNumber(String content) {
            org.jsoup.nodes.Document doc = Jsoup.parse(content);
            Elements pageno_content = doc.getElementsByClass("pageno_content");
            if (CollectionUtil.isNotEmpty(pageno_content)) {
                pageno_content.forEach(element -> {
                    element.html("{@pageNumber}");
                });
            }
            return doc.html();
        }

        private static void replacePageNumber(BatchRecordItem recordItem, Document document, HeaderFooter headerFooter,
                                              Integer pageCodeHorizontalAlignment) {
            DocumentBuilder documentBuilder = new DocumentBuilder(document);
            document.getFirstSection().getPageSetup().setPageNumberStyle(recordItem.getPageNumberStyle() == null ? DEFAULT_PAGE_NUMBER_STYLE : recordItem.getPageNumberStyle());
            document.getFirstSection().getPageSetup().setPageStartingNumber(recordItem.getPageStartingNumber() == null ? DEFAULT_PAGE_START_NUMBER : recordItem.getPageStartingNumber());
            NodeCollection<Node> childNodes = headerFooter.getChildNodes(NodeType.ANY, true);
            childNodes.forEach(node -> {
                String text = node.getText();
                if ("{@pageNumber}".equals(StrUtil.trim(text))) {
                    try {
                        documentBuilder.moveTo(node);
                        Paragraph currentParagraph = documentBuilder.getCurrentParagraph();
                        currentParagraph.insertField(FieldType.FIELD_PAGE, true, null, true);
                        currentParagraph.getParagraphFormat().setAlignment(getAlignment(pageCodeHorizontalAlignment));
                        node.remove();
                    } catch (Exception e) {
                        throw new RuntimeException("页码添加失败", e);
                    }
                }

            });

        }

        private static int getAlignment(Integer pageCodeHorizontalAlignment) {
            switch (pageCodeHorizontalAlignment) {
                case HorizontalAlignment.RIGHT:
                    return ParagraphAlignment.RIGHT;
                case HorizontalAlignment.CENTER:
                    return ParagraphAlignment.CENTER;
                default:
                    return ParagraphAlignment.LEFT;
            }
        }

        public static void addFooter2Document(ProcessStepArchiveContext context, BatchRecordItem recordItem,
                                              List<ExecuteFormData> data,
                                              List<ImageVO> images, Document document, DocxFooter footer,
                                              DocxHeaderFooterItem footerItem, int headerFooterType) {
            List<BatchRecordComponent> componentList = context.getItemComponentsMap().getOrDefault(recordItem.getId(),
                    new ArrayList<>());
            String mergeHtml = HtmlUtil.mergeHtml(footerItem.getContent(),
                    BeanUtil.copyToList(componentList, ComponentDTO.class),
                    BeanUtil.copyToList(data,
                            FieldValueDTO.class),
                    images);
            try (InputStream inputStream = new ByteArrayInputStream(mergeHtml.getBytes())) {
                HtmlLoadOptions htmlLoadOptions = new HtmlLoadOptions();
                Document headerFooterDocument = new Document(inputStream, htmlLoadOptions);
                HeaderFooter headerFooter = new HeaderFooter(document, headerFooterType);
                NodeCollection childNodes =
                        headerFooterDocument.getFirstSection().getBody().getChildNodes(NodeType.ANY, false);
                childNodes.forEach(node -> {
                    Node importNode = document.importNode((Node) node, true, ImportFormatMode.KEEP_SOURCE_FORMATTING);
                    log.info(((Node) node).getNodeType() + "");
                    log.info(importNode.getText());
                    headerFooter.appendChild(importNode.deepClone(true));
                    headerFooter.isLinkedToPrevious(footer.getLinkToPrevious());
                });
                headerFooter.isLinkedToPrevious(footer.getLinkToPrevious());
                // 将页眉页脚的占位符替换成回真实文档中的内容
                replacePageNumber(recordItem, document, headerFooter, footerItem.getPageCodeHorizontalAlignment());
                document.getFirstSection().getHeadersFooters().add(headerFooter);
            } catch (Exception e) {
                throw new RuntimeException("页脚添加失败");
            }
        }

    }
}
