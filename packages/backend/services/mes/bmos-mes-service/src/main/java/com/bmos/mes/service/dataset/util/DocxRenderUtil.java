package com.bmos.mes.service.dataset.util;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.aspose.words.*;
import com.bmos.common.util.json.JsonUtils;
import com.bmos.file.docx.util.SplitDocxUtil;
import com.bmos.mes.service.dataset.common.DatasetTrans;
import com.bmos.mes.service.dataset.common.DatasetTransValueData;
import com.bmos.mes.service.dataset.common.enums.DatasetTransValueDataType;
import com.bmos.mes.service.dataset.util.options.*;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.List;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * docx渲染工具
 *
 * @author liang
 * @version 1.0.0
 * @date 2024/9/2 10:23
 */
public class DocxRenderUtil {

    private static final Logger log = LoggerFactory.getLogger(DocxRenderUtil.class);

    private static final String LOG_PREFIX = "[Docx4jUtil]";

    private static final String[] DOCX_SUFFIXES = {"doc", "docx"};

    /**
     * 扫描占位符
     *
     * @param file docx文件
     * @return 占位符列表
     * @throws Exception
     */
    public static List<String> scanPlaceHolder(File file) throws Exception {
        List<String> list = new ArrayList<>();
        log.info("{} 开始扫描word文档,解析所有占位符", LOG_PREFIX);
        Document document = SplitDocxUtil.loadDocx(file);
        NodeCollection childNodes = document.getChildNodes(NodeType.RUN, true);
        String[] str = new String[childNodes.getCount()];
        String allText = StrUtil.concat(true, Arrays.stream(childNodes.toArray())
                .map(node -> (Run) node)
                .map(Run::getText)
                .collect(Collectors.toList())
                .toArray(str));
        if (StrUtil.isNotBlank(allText)) {
            Matcher matcher = PlaceholderConstants.PATTERN.matcher(allText);
            while (matcher.find()) {
                String group = matcher.group(0);
                String[] split = group.split("\\$");
                if (split.length == 2){
                    list.add(group);
                }
            }
        }
        NodeCollection shapes = document.getChildNodes(NodeType.SHAPE, true);
        // 获取alt中的占位符
        for (Shape shape : (Iterable<Shape>) shapes) {
            if (shape.hasImage()) {
                // 获取图片的Alt文本
                String altTextTitle = shape.getAlternativeText();
                if (StrUtil.isNotBlank(altTextTitle)){
                    Matcher matcher = PlaceholderConstants.PATTERN.matcher(altTextTitle);
                    while (matcher.find()) {
                        String group = matcher.group(0);
                        String[] split = group.split("\\$");
                        if (split.length == 2){
                            list.add(group);
                        }
                    }
                }
            }
        }
        return list;
    }

    /**
     * 填值 渲染docx
     *
     * @param file              模版文件
     * @param params            渲染数据
     * @param placeHolders      占位符
     * @param emptyPlaceholder  空值占位符
     * @param evidencePhotoList 拍照取证列表
     * @return
     * @throws Exception
     */
    public static Document fillValue(File file,
                                     Map<String, DatasetTransValueData> params,
                                     List<String> placeHolders,
                                     String emptyPlaceholder, String recordEmpty,
                                     List<DocxTakePhotoLegendReplaceOption.TakePhotoData> evidencePhotoList,
                                     String patternEvidencePlaceHolder,
                                     String patternTakePhotoPlaceHolder

    ) throws Exception {
        Document document = SplitDocxUtil.loadDocx(file);
        // 处理图片
        Set<String> urls = params.values()
                .stream()
                .filter(item -> Objects.equals(item.getType(), DatasetTransValueDataType.IMAGE))
                .map(DatasetTransValueData::getValue)
                .flatMap(item -> Arrays.stream(item.split(",")))
                .collect(Collectors.toSet());
        // 处理拍照组件
        urls.addAll(params.values()
                .stream()
                .filter(item -> Objects.equals(item.getType(), DatasetTransValueDataType.TAKE_PHOTO))
                .map(DatasetTransValueData::getValue)
                .map(item -> {
                    try {
                        return JSON.parseArray(item, DocxTakePhotoLegendReplaceOption.TakePhotoData.class);
                    } catch (Exception e) {
                        log.error("{} 拍照组件数据格式错误,无法转换为数组", item);
                        return new ArrayList<DocxTakePhotoLegendReplaceOption.TakePhotoData>();
                    }
                })
                .flatMap(Collection::stream)
                .map(DocxTakePhotoLegendReplaceOption.TakePhotoData::getImageUrl)
                .collect(Collectors.toSet()));
        // 拍照取证组件
        urls.addAll(evidencePhotoList
                .stream()
                .map(DocxTakePhotoLegendReplaceOption.TakePhotoData::getImageUrl)
                .collect(Collectors.toSet()));
        // 含有图片标注的组件
        urls.addAll(params
                .values()
                .stream()
                .filter(item -> Objects.equals(item.getType(), DatasetTransValueDataType.IMAGE_CAPTION) && !item.isEmpty())
                .map(e-> Optional.ofNullable(JsonUtils.parseObject(e.getValue(),
                        DocxImageCaptionReplaceOption.ImageCaptionData.class))
                        .orElse(new DocxImageCaptionReplaceOption.ImageCaptionData())
                        .getImageUrl())
                .collect(Collectors.toList()));
        Map<String, byte[]> imageMap = ImageUtil.downloadAllPictures(urls);
        DocumentBuilder builder = new DocumentBuilder(document);
        // 扫描word中所有的带有alt占位符的图片并替换
        solvePicPlaceholderWithAlt(params, document, builder, imageMap, emptyPlaceholder, recordEmpty);
        // 处理文本占位符 或者没有图片alt的占位符
        Range range = document.getRange();
        AtomicInteger takePhotoCount = new AtomicInteger(0);
        LinkedHashMap<String, DocxTakePhotoLegendReplaceOption.TakePhotoData> takePhotoMap = new LinkedHashMap<>();
        for (String placeHolder : placeHolders) {
            range.replace(placeHolder, placeHolder);
            DatasetTransValueData data = params.get(placeHolder);
            if (data == null) {
                DatasetTrans.DatasetTransExpression expression = new DatasetTrans.DatasetTransExpression(placeHolder);
                if (!expression.getIsList()) {
                    // 文本类型
                    range.replace(placeHolder, emptyPlaceholder, new DocxTextReplaceOption(builder, null));
                } else {
                    // 数组类型
                    range.replace(placeHolder, "", new DocxCheckboxReplaceOption(builder, null, placeHolder));
                }
            } else {
                if (data.getValue() == null){
                    range.replace(placeHolder, recordEmpty, new DocxTextReplaceOption(builder, null));
                }
                if (Objects.equals(data.getType(), DatasetTransValueDataType.TEXT)) {
                    // 处理文本
                    range.replace(placeHolder, data.getValue(), new DocxTextReplaceOption(builder, data));
                } else if (Objects.equals(data.getType(), DatasetTransValueDataType.IMAGE)) {
                    // 处理图片
                    range.replace(placeHolder, data.getValue(), new DocxImageReplaceOption(builder, data, imageMap, recordEmpty));
                } else if (Objects.equals(data.getType(), DatasetTransValueDataType.CHECKBOX)) {
                    // 选择框
                    range.replace(placeHolder, data.getValue(), new DocxCheckboxReplaceOption(builder, data, placeHolder));
                } else if (Objects.equals(data.getType(), DatasetTransValueDataType.TAKE_PHOTO)) {
                    // 拍照组件
                    range.replace(placeHolder, data.getExtStringValue(), new DocxTakePhotoLegendReplaceOption(builder, takePhotoCount, takePhotoMap));
                } else if (Objects.equals(data.getType(), DatasetTransValueDataType.IMAGE_CAPTION)) {
                    range.replace(placeHolder, data.getValue(), new DocxImageCaptionReplaceOption(builder, data, imageMap));
                } else {
                    // 错误类型
                    range.replace(placeHolder, placeHolder, new DocxErrorReplaceOption(builder));
                }
            }
        }
        // 先replace一下 将word中的文本节点打碎 变成只包含满足占位符的node（否则可能会将包含占位符前后的文字）
        extractRun(range);

        // 处理拍照取证的图片 渲染到占位符位置
        range.replace(DocxRenderConstants.EVIDENCE_PLACEHOLDER, "", new DocxEvidencePhotoReplaceOption(builder, imageMap, evidencePhotoList));
        // 处理照相组件的图片 渲染到占位符位置
        range.replace(DocxRenderConstants.TAKE_PHOTO_PLACEHOLDER, "", new DocxTakePhotoPicReplaceOption(builder, imageMap, takePhotoMap));
        // 处理拍照取证的图片 渲染到占位符位置（按照工艺/工序过滤）
        range.replace(Pattern.compile(patternEvidencePlaceHolder), "", new DocxPatternEvidencePhotoReplaceOption(builder, imageMap, evidencePhotoList, patternEvidencePlaceHolder));
        // 处理照相组件的图片 渲染到占位符位置（按照工艺/工序过滤）
        range.replace(Pattern.compile(patternTakePhotoPlaceHolder), "", new DocxPatternTakePhotoPicReplaceOption(builder, imageMap, takePhotoMap, patternTakePhotoPlaceHolder));
        // 分组渲染
        return document;
    }

    /**
     * 填值 渲染docx
     *
     * @param file              模版文件
     * @param params            渲染数据
     * @param emptyPlaceholder  空值占位符
     * @param evidencePhotoList 拍照取证列表
     * @return
     * @throws Exception
     */
    public static Document fillValue(File file,
                                     Map<String, DatasetTransValueData> params,
                                     String emptyPlaceholder,
                                     String recordEmpty,
                                     List<DocxTakePhotoLegendReplaceOption.TakePhotoData> evidencePhotoList, String patternEvidencePlaceHolder, String patternTakePhotoPlaceHolder) throws Exception {
        List<String> placeHolders = scanPlaceHolder(file);
        return fillValue(file, params, placeHolders, recordEmpty, emptyPlaceholder, evidencePhotoList, patternEvidencePlaceHolder, patternTakePhotoPlaceHolder);
    }

    /**
     * 替换word中的占位图片
     *
     * @param params   替换数据
     * @param document 文档
     * @param builder  builder
     * @param imageMap 图片map
     * @throws Exception
     */
    private static void solvePicPlaceholderWithAlt(Map<String, DatasetTransValueData> params, Document document, DocumentBuilder builder, Map<String, byte[]> imageMap, String emptyPlaceHolder, String recordEmpty) throws Exception {
        for (Object shapeObj : document.getChildNodes(NodeType.SHAPE, true)) {
            Shape shape = (Shape) shapeObj;
            String alternativeText = shape.getAlternativeText();
            if (StrUtil.isBlank(alternativeText)) {
                continue;
            }
            double width = shape.getWidth();
            double height = shape.getHeight();
            if (params.containsKey(alternativeText)) {
                DatasetTransValueData datasetTransValueData = params.get(alternativeText);
                if (datasetTransValueData != null && !StrUtil.isBlank(datasetTransValueData.getValue())) {
                    // 图片格式
                    if (datasetTransValueData.getType() == DatasetTransValueDataType.IMAGE) {
                        builder.moveTo(shape);
                        String[] picUrls = datasetTransValueData.getValue()
                                .split(",");
                        for (String picUrl : picUrls) {
                            if (StrUtil.equals(recordEmpty, picUrl) || datasetTransValueData.isEmpty()){
                                builder.write(emptyPlaceHolder);
                            }else {
                                if (imageMap.get(picUrl).length != 0) {
                                    Shape node = builder.insertImage(imageMap.get(picUrl), width, height);
                                    builder.moveTo(node);
                                }
                            }
                        }
                    } else if (datasetTransValueData.getType() == DatasetTransValueDataType.TAKE_PHOTO) {
                        // 拍照组件 带文字的图片 只渲染图片
                        builder.moveTo(shape);
                        List<DocxTakePhotoLegendReplaceOption.TakePhotoData> takePhotoData = JSON.parseArray(datasetTransValueData.getValue(), DocxTakePhotoLegendReplaceOption.TakePhotoData.class);
                        for (DocxTakePhotoLegendReplaceOption.TakePhotoData item : takePhotoData) {
                            Shape node = builder.insertImage(imageMap.get(item.getImageUrl()), width, height);
                            builder.moveTo(node);
                        }
                    } else if (datasetTransValueData.getType() == DatasetTransValueDataType.IMAGE_CAPTION) {
                        // 带图注的图片
                        builder.moveTo(shape);
                        DocxImageCaptionReplaceOption.ImageCaptionData imageCaptionData = JSON.parseObject(datasetTransValueData.getValue(), DocxImageCaptionReplaceOption.ImageCaptionData.class);
                        Shape node = builder.insertImage(imageMap.get(imageCaptionData.getImageUrl()), width, height);
                        builder.moveTo(node);
                    }
                }
                shape.remove();
            }
        }
    }

    private static void extractRun(Range range) throws Exception {
        // 先replace一下 将word中的文本节点打碎 变成只包含满足占位符的node（否则可能会将包含占位符前后的文字）
        range.replace(DocxRenderConstants.EVIDENCE_PLACEHOLDER, DocxRenderConstants.EVIDENCE_PLACEHOLDER);
        range.replace(DocxRenderConstants.TAKE_PHOTO_PLACEHOLDER, DocxRenderConstants.TAKE_PHOTO_PLACEHOLDER);
    }

    /**
     * 将图片占位符
     * @param placeholder
     * @return 业务索引数组 固定五个 [工艺名称，工序名称，批次顺序，工艺换班，工序换班]
     */
    public static String[] extractImagePlaceHolderWithPattern(String placeholder){
        Pattern p = Pattern.compile("\\[(.*?)]");
        Matcher matcher = p.matcher(placeholder);
        String[] group = new String[5];
        int i = 0;
        while (matcher.find()){
            String index = matcher.group(1);
            if (i > 1 && StringUtils.equals(index, "")){
                index = "0";
            }
            group[i++] = index;
        }
        return group;
    }

}
