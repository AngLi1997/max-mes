package com.bmos.lims2.server.recordprint.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.codec.Base64;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.StrUtil;
import com.aspose.words.Document;
import com.aspose.words.HeaderFooter;
import com.aspose.words.HeaderFooterType;
import com.aspose.words.HtmlLoadOptions;
import com.aspose.words.ImportFormatMode;
import com.aspose.words.Node;
import com.aspose.words.NodeCollection;
import com.aspose.words.NodeType;
import com.aspose.words.DocumentBuilder;
import com.aspose.words.FieldType;
import com.aspose.words.Paragraph;
import com.aspose.words.ParagraphAlignment;
import com.aspose.words.PageSetup;
import com.aspose.words.Orientation;
import com.aspose.words.PaperSize;
import com.bmos.common.util.collection.CollectionUtils;
import com.bmos.file.docx.dto.ComponentDTO;
import com.bmos.file.docx.dto.FieldValueDTO;
import com.bmos.file.docx.dto.ImageVO;
import com.bmos.file.docx.model.DocxFooter;
import com.bmos.file.docx.model.DocxHeader;
import com.bmos.file.docx.util.HtmlUtil;
import com.bmos.file.docx.util.WordUtil;
import com.bmos.lims2.common.enums.BasicComponentTypeEnum;
import com.bmos.lims2.common.enums.BusinessComponentTypeEnum;
import com.bmos.lims2.server.eln.entry.converter.ExecuteFormDataConverter;
import com.bmos.lims2.server.eln.entry.entity.ExecuteAttachment;
import com.bmos.lims2.server.eln.entry.enums.AttachmentTypeEnum;
import com.bmos.lims2.server.platform.util.UserUtils;
import com.bmos.lims2.server.recordprint.dto.PrintItemReqDTO;
import com.bmos.lims2.server.recordprint.dto.PrintableAnalysisItemDTO;
import com.bmos.lims2.server.recordprint.dto.RecordItemAssetsDTO;
import com.bmos.lims2.server.recordprint.dto.RecordPrintPageReqDTO;
import com.bmos.lims2.server.recordprint.mapper.RecordPrintMapper;
import com.bmos.lims2.server.recordprint.service.RecordPrintService;
import com.bmos.lims2.server.recordprint.util.WordPdfUtil;
import com.bmos.lims2.server.inspect.order.dto.InspectionOrderDTO;
import com.bmos.lims2.server.eln.entry.entity.ExecuteFormData;
import com.bmos.lims2.server.eln.entry.mapper.ExecuteFormDataMapper;
import com.bmos.lims2.server.eln.record.entity.BatchRecordComponent;
import com.bmos.lims2.server.eln.record.service.BatchRecordComponentService;
import com.bmos.lims2.server.task.entity.Task;
import com.bmos.lims2.server.task.mapper.TaskMapper;
import com.bmos.lims2.server.inspect.order.entity.InspectionOrder;
import com.bmos.lims2.server.inspect.order.mapper.InspectionOrderMapper;
import com.bmos.lims2.server.stability.scheme.mapper.StabilitySchemeVersionMapper;
import com.bmos.mybatis.page.CommonPage;
import cn.hutool.json.JSONUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import io.minio.GetObjectArgs;
import io.minio.GetObjectResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Description: 记录打印服务实现
 * @Author: yigaohui
 * @Date: 2025/11/25 10:20
 */
@Service
@Slf4j
public class RecordPrintServiceImpl implements RecordPrintService {

    @Autowired
    private RecordPrintMapper recordPrintMapper;
    @Autowired
    private ExecuteFormDataMapper executeFormDataMapper;
    @Autowired
    private com.bmos.lims2.server.eln.entry.service.ExecuteAttachmentService executeAttachmentService;
    @Autowired
    private com.bmos.lims2.server.config.minio.MinioProperties minioProperties;
    @Autowired
    private io.minio.MinioClient minioClient;
    @Autowired
    private BatchRecordComponentService batchRecordComponentService;
    @Autowired
    private TaskMapper taskMapper;
    @Autowired
    private InspectionOrderMapper inspectionOrderMapper;
    @Autowired
    private StabilitySchemeVersionMapper stabilitySchemeVersionMapper;
    private static final String PNG_BASE64_PREFIX = "data:image/png;base64,";

    @Override
    public CommonPage<InspectionOrderDTO> pagePrintableInspections(RecordPrintPageReqDTO reqDTO) {
        Integer pageNum = reqDTO.getPageNum() == null ? 1 : reqDTO.getPageNum();
        Integer pageSize = reqDTO.getPageSize() == null ? 20 : reqDTO.getPageSize();
        PageHelper.startPage(pageNum, pageSize);
        List<InspectionOrderDTO> list = recordPrintMapper.selectPrintableInspectionOrders(
                likeOrNull(reqDTO.getOrderNo()),
                likeOrNull(reqDTO.getBatchNo()),
                reqDTO.getMaterialId(),
                reqDTO.getMaterialIds(),
                reqDTO.getInspectionRequestTimeStart(),
                reqDTO.getInspectionRequestTimeEnd()
        );
        PageInfo<InspectionOrderDTO> pageInfo = new PageInfo<>(list);
        CommonPage<InspectionOrderDTO> page = new CommonPage<>();
        page.setPageNum(pageNum);
        page.setPageSize(pageSize);
        page.setTotal((int) pageInfo.getTotal());
        page.setList(list);
        return page;
    }

    @Override
    public List<PrintableAnalysisItemDTO> listPrintableAnalysisItems(Long inspectionId) {
        InspectionOrder order = inspectionOrderMapper.selectById(inspectionId);
        boolean isStability = order != null && order.getSchemeVersionId() != null
                && stabilitySchemeVersionMapper.selectById(order.getSchemeVersionId()) != null;
        return isStability
                ? recordPrintMapper.listPrintableAnalysisItemsForStability(inspectionId)
                : recordPrintMapper.listPrintableAnalysisItems(inspectionId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public byte[] previewAnalysisPdf(Long taskId) {
        // 1) 获取模板与页眉页脚资源
        RecordItemAssetsDTO assets = recordPrintMapper.selectRecordItemAssetsByTaskId(taskId);
        String templateHtml = assets == null || assets.getTemplateContent() == null ? "" : assets.getTemplateContent();
        // 2) 获取任务表单数据 + 图片并合并HTML
        List<ExecuteFormData> data = queryTaskData(taskId);
        // 附件下载（拍照取证与拍照组件的图片）
        List<ExecuteAttachment> attachments = executeAttachmentService.getListByTaskId(taskId);
        List<ImageVO> downloadImages = this.downloadImages(attachments);
        // 将表单数据转为 FieldValueDTO 并按示例处理签名/设备绘图/拍照组件
        List<FieldValueDTO> fieldValues = cn.hutool.core.bean.BeanUtil.copyToList(data, FieldValueDTO.class);
        handleSignature(fieldValues);
        handleEquipmentPicture(fieldValues);
        handlePicComponent(data, fieldValues, downloadImages);
        List<ComponentDTO> components = getComponentDTOs(assets);
        String mergedHtml = HtmlUtil.mergeHtml(templateHtml, components, fieldValues, downloadImages);
        // 3) 生成Word（先转docx再载入，保持版式一致）
        java.io.File docxFile = WordUtil.convertHtml2docx(System.getProperty("java.io.tmpdir"),
                "preview-" + taskId + ".docx", mergedHtml);
        Document doc = loadDocSafely(docxFile);
        // 4) 合并页眉页脚（按记录项配置）
        applyHeaderFooter(doc, assets, components, fieldValues, downloadImages);
        // 4.1 追加PDF类型的附件到文档末尾（保持原文档页码样式，不改动附件内部内容）
        appendPdfAttachments(doc, attachments);
        String uName = "";
        String lName = "";
        if (com.bmos.common.holder.SysUserHolder.getUser() != null) {
            try {
                uName = String.valueOf(com.bmos.common.holder.SysUserHolder.getUser().getUserName());
                lName = String.valueOf(com.bmos.common.holder.SysUserHolder.getUser().getLoginName());
            } catch (Exception ignored) {
            }
        }
        String watermark = String.format("%s-%s %s",
                uName == null ? "" : uName,
                lName == null ? "" : lName,
                java.time.LocalDateTime.now().format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
        WordPdfUtil.addDiagonalWatermark(doc, watermark);
        // 5) 导出PDF
        byte[] pdf = WordPdfUtil.toPdfBytes(doc);
        // 6) 记录操作日志
        saveOpLog("RECORD_PRINT", "PREVIEW", taskId, "预览分析项PDF");
        return pdf;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public byte[] mergePrintPdf(Long inspectionId, List<PrintItemReqDTO> items) {
        List<Document> documents = new ArrayList<>();
        for (PrintItemReqDTO item : items) {
            RecordItemAssetsDTO assets = recordPrintMapper.selectRecordItemAssetsByTaskId(item.getTaskId());
            String templateHtml = assets == null || assets.getTemplateContent() == null ? "" : assets.getTemplateContent();
            List<ExecuteFormData> data = queryTaskData(item.getTaskId());
            // 附件下载（拍照取证与拍照组件的图片）
            List<ExecuteAttachment> attachments = executeAttachmentService.getListByTaskId(item.getTaskId());
            List<ImageVO> downloadImages = this.downloadImages(attachments);
            List<FieldValueDTO> fieldValues = cn.hutool.core.bean.BeanUtil.copyToList(data, FieldValueDTO.class);
            handleSignature(fieldValues);
            handleEquipmentPicture(fieldValues);
            handlePicComponent(data, fieldValues, downloadImages);
            List<ComponentDTO> components = getComponentDTOs(assets);
            String mergedHtml = HtmlUtil.mergeHtml(templateHtml, components, fieldValues, downloadImages);
            java.io.File docxFile = WordUtil.convertHtml2docx(System.getProperty("java.io.tmpdir"),
                    "print-" + item.getTaskId() + ".docx", mergedHtml);
            Document doc = loadDocSafely(docxFile);
            applyHeaderFooter(doc, assets, components, fieldValues, downloadImages);
            // 追加当前任务的PDF类型附件
            appendPdfAttachments(doc, attachments);
            documents.add(doc);
        }
        // 合并
        Document merged = WordPdfUtil.mergeDocuments(documents);
        byte[] pdf = WordPdfUtil.toPdfBytes(merged);
        // 记录操作日志
        saveOpLog("RECORD_PRINT", "PRINT", inspectionId, "合并打印检验记录PDF");
        return pdf;
    }

    private List<ExecuteFormData> queryTaskData(Long taskId) {
        try {
            List<ExecuteFormData> formDataList = executeFormDataMapper.selectByTaskIdAndItemIds(taskId);
            return ExecuteFormDataConverter.INSTANCE.filterLatest(formDataList);
        } catch (Exception e) {
            log.warn("加载任务表单数据失败，按空数据处理. taskId={}", taskId, e);
            return new ArrayList<>();
        }
    }

    private List<ImageVO> downloadImages(List<ExecuteAttachment> attachments) {
        List<ExecuteAttachment> list = attachments.stream().filter(executeAttachment ->
                AttachmentTypeEnum.MODULE_PICTURE.getValue().equals(executeAttachment.getAttachmentType()) ||
                        AttachmentTypeEnum.EVIDENCE_PICTURE.getValue().equals(executeAttachment.getAttachmentType())).collect(Collectors.toList());
        if (CollectionUtil.isEmpty(list)) {
            return new ArrayList<>();
        }
        List<ImageVO> base64Images = new ArrayList<>();
        list.forEach(executeAttachment -> {
            try {
                //判断附件中是否有拍照上传组件，存在进行处理
                String product = minioProperties.getBuckets().getRecord();
                if (executeAttachment.getPath().contains(product)) {
                    executeAttachment.setPath(StrUtil.subAfter(executeAttachment.getPath(), StrUtil.C_SLASH, true));
                }
                GetObjectResponse minioClientObject =
                        minioClient.getObject(GetObjectArgs.builder().bucket(minioProperties.getBuckets()
                                .getRecord()).object(executeAttachment.getPath()).build());
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

    private void handleEquipmentPicture(List<FieldValueDTO> fieldValueDTOS) {
        // 可根据业务需要补充设备数采绘图图片转换逻辑
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

    private List<ComponentDTO> getComponentDTOs(RecordItemAssetsDTO assets) {
        try {
            List<BatchRecordComponent> components =
                    batchRecordComponentService.selectByVersionAndItem(assets.getRecordVersionId(), assets.getRecordItemId());
            return cn.hutool.core.bean.BeanUtil.copyToList(components, ComponentDTO.class);
        } catch (Exception e) {
            log.warn("加载组件定义失败，按空组件处理. recordVersionId={}, recordItemId={}", assets.getRecordVersionId(), assets.getRecordItemId(), e);
            return new ArrayList<>();
        }
    }

    private void applyHeaderFooter(Document document, RecordItemAssetsDTO assets, List<ComponentDTO> components, List<FieldValueDTO> fieldValues, List<ImageVO> images) {
        if (assets == null) {
            return;
        }
        try {
            // 页面设置（横版/纸张/禁用表格自动调整/页码样式与起始值）
            applyPageSetup(document, assets);

            // 页眉
            if (assets.getDocxHeader() != null && !assets.getDocxHeader().trim().isEmpty()) {
                DocxHeader header = JSONUtil.toBean(assets.getDocxHeader(), DocxHeader.class);
                if (header != null) {
                    addHeaderFooterHtml(document, HeaderFooterType.HEADER_PRIMARY,
                            header.getHeaderPrimary() == null ? null : header.getHeaderPrimary().getContent(),
                            header.getHeaderPrimary() == null ? null : header.getHeaderPrimary().getPageCodeHorizontalAlignment(),
                            components, fieldValues, images);
                    addHeaderFooterHtml(document, HeaderFooterType.HEADER_FIRST,
                            header.getHeaderFirst() == null ? null : header.getHeaderFirst().getContent(),
                            header.getHeaderFirst() == null ? null : header.getHeaderFirst().getPageCodeHorizontalAlignment(),
                            components, fieldValues, images);
                    addHeaderFooterHtml(document, HeaderFooterType.HEADER_EVEN,
                            header.getHeaderEven() == null ? null : header.getHeaderEven().getContent(),
                            header.getHeaderEven() == null ? null : header.getHeaderEven().getPageCodeHorizontalAlignment(),
                            components, fieldValues, images);
                }
            }
            // 页脚
            if (assets.getDocxFooter() != null && !assets.getDocxFooter().trim().isEmpty()) {
                DocxFooter footer = JSONUtil.toBean(assets.getDocxFooter(), DocxFooter.class);
                if (footer != null) {
                    addHeaderFooterHtml(document, HeaderFooterType.FOOTER_PRIMARY,
                            footer.getFooterPrimary() == null ? null : footer.getFooterPrimary().getContent(),
                            footer.getFooterPrimary() == null ? null : footer.getFooterPrimary().getPageCodeHorizontalAlignment(),
                            components, fieldValues, images);
                    addHeaderFooterHtml(document, HeaderFooterType.FOOTER_FIRST,
                            footer.getFooterFirst() == null ? null : footer.getFooterFirst().getContent(),
                            footer.getFooterFirst() == null ? null : footer.getFooterFirst().getPageCodeHorizontalAlignment(),
                            components, fieldValues, images);
                    addHeaderFooterHtml(document, HeaderFooterType.FOOTER_EVEN,
                            footer.getFooterEven() == null ? null : footer.getFooterEven().getContent(),
                            footer.getFooterEven() == null ? null : footer.getFooterEven().getPageCodeHorizontalAlignment(),
                            components, fieldValues, images);
                }
            }
        } catch (Exception e) {
            log.warn("合并页眉页脚失败，忽略处理", e);
        }
    }

    private void addHeaderFooterHtml(Document document, int headerFooterType, String html, Integer pageCodeHorizontalAlignment, List<ComponentDTO> components, List<FieldValueDTO> fieldValues, List<ImageVO> images) {
        if (html == null || html.trim().isEmpty()) {
            return;
        }
        try (ByteArrayInputStream in = new ByteArrayInputStream(HtmlUtil.mergeHtml(html,
                components == null ? new ArrayList<>() : components,
                fieldValues == null ? new ArrayList<>() : fieldValues,
                images == null ? new ArrayList<>() : images).getBytes())) {
            HtmlLoadOptions loadOptions = new HtmlLoadOptions();
            Document headerDoc = new Document(in, loadOptions);
            HeaderFooter headerFooter = new HeaderFooter(document, headerFooterType);
            NodeCollection nodes = headerDoc.getFirstSection().getBody().getChildNodes(NodeType.ANY, false);
            nodes.forEach(n -> {
                Node importNode = document.importNode((Node) n, true, ImportFormatMode.KEEP_SOURCE_FORMATTING);
                headerFooter.appendChild(importNode.deepClone(true));
            });
            // 替换页码占位符
            replacePageNumberPlaceholder(document, headerFooter, pageCodeHorizontalAlignment);
            document.getFirstSection().getHeadersFooters().add(headerFooter);
        } catch (Exception e) {
            log.warn("添加页眉/页脚失败", e);
        }
    }

    private void replacePageNumberPlaceholder(Document document, HeaderFooter headerFooter, Integer alignment) {
        DocumentBuilder builder = new DocumentBuilder(document);
        NodeCollection runNodes = headerFooter.getChildNodes(NodeType.RUN, true);
        runNodes.forEach(n -> {
            com.aspose.words.Run run = (com.aspose.words.Run) n;
            String text = run.getText() == null ? "" : run.getText().trim();
            if ("{@pageNumber}".equals(text)) {
                try {
                    builder.moveTo(run);
                    Paragraph para = builder.getCurrentParagraph();
                    // 插入页码域
                    builder.insertField(FieldType.FIELD_PAGE, true);
                    para.getParagraphFormat().setAlignment(mapAlignment(alignment));
                    run.remove();
                } catch (Exception e) {
                    log.warn("替换页码占位失败", e);
                }
            }
        });
    }

    private int mapAlignment(Integer alignment) {
        if (alignment == null) {
            return ParagraphAlignment.LEFT;
        }
        switch (alignment) {
            case 1:
                return ParagraphAlignment.CENTER;
            case 2:
                return ParagraphAlignment.RIGHT;
            default:
                return ParagraphAlignment.LEFT;
        }
    }

    private void applyPageSetup(Document document, RecordItemAssetsDTO assets) {
        try {
            // 横版/纸张
            if (assets.getPageConfig() != null && !assets.getPageConfig().trim().isEmpty()) {
                cn.hutool.json.JSONObject obj = JSONUtil.parseObj(assets.getPageConfig());
                Integer pattern = obj.getInt("pattern", null);
                if (pattern != null && pattern == 0) {
                    PageSetup setup = document.getFirstSection().getPageSetup();
                    setup.setOrientation(Orientation.LANDSCAPE);
                    setup.setPaperSize(PaperSize.A4);
                }
            }
            // 页码样式与起始值（样式留默认，设置起始值）
            if (assets.getPageStartingNumber() != null) {
                document.getFirstSection().getPageSetup().setPageStartingNumber(assets.getPageStartingNumber());
            } else {
                // 默认从1开始，确保每个单独文档的页码独立且清晰
                document.getFirstSection().getPageSetup().setPageStartingNumber(1);
            }
            // 禁用表格自动调整
            document.getSections().forEach(section -> section.getBody().getTables().forEach(table -> {
                try {
                    table.setAllowAutoFit(false);
                } catch (Exception e) {
                    // ignore
                }
            }));
        } catch (Exception e) {
            log.warn("应用页面设置失败，忽略", e);
        }
    }

    private String likeOrNull(String v) {
        if (v == null || v.trim().isEmpty()) {
            return null;
        }
        return v.trim();
    }

    private Document loadDocSafely(java.io.File file) {
        try (java.io.InputStream in = new java.io.FileInputStream(file)) {
            return new Document(in);
        } catch (Exception e) {
            throw new RuntimeException("加载Word文档失败", e);
        }
    }

    private void saveOpLog(String module, String action, Long targetId, String remark) {
        Map<String, Object> fields = new HashMap<>();
        fields.put("id", com.bmos.common.util.id.IdUtils.getSnowflake());
        fields.put("businessId", targetId);
        fields.put("module", module);
        fields.put("operationType", action);
        fields.put("remark", remark);
        // create_by：暂无登录上下文，暂记空（可接入后替换）
        fields.put("createBy", null);
        recordPrintMapper.insertOperationLog(fields);
    }

    /**
     * 将PDF类型的附件原样拼接到当前文档末尾
     */
    private void appendPdfAttachments(Document baseDoc, List<ExecuteAttachment> attachments) {
        if (attachments == null || attachments.isEmpty()) {
            return;
        }
        List<ExecuteAttachment> collect = attachments.stream().filter(att -> AttachmentTypeEnum.FILE.getValue().equals(att.getAttachmentType())).collect(Collectors.toList());
        if (collect.isEmpty()){
            return;
        }
        for (ExecuteAttachment executeAttachment : collect) {
            try {
                String product = minioProperties.getBuckets().getRecord();
                if (executeAttachment.getPath().contains(product)) {
                    executeAttachment.setPath(StrUtil.subAfter(executeAttachment.getPath(), StrUtil.C_SLASH, true));
                }
                try (InputStream is = minioClient.getObject(
                        GetObjectArgs.builder().bucket(minioProperties.getBuckets()
                                .getRecord()).object(executeAttachment.getPath()).build())) {
                    // 使用 Aspose.Words 读取PDF并追加到文档
                    Document pdfDoc = new Document(is);
                    baseDoc.appendDocument(pdfDoc, ImportFormatMode.KEEP_SOURCE_FORMATTING);
                }
            } catch (Exception e) {
                log.warn("拼接PDF附件失败, id={}, path={}", executeAttachment.getId(), executeAttachment.getPath(), e);
            }
        }
    }
}


