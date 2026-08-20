package com.bmos.platform.service.tag.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import com.bmos.common.exception.BmosException;
import com.bmos.mybatis.page.CommonPage;
import com.bmos.platform.common.enums.BooleanEnum;
import com.bmos.platform.common.exception.PlatformResponseCode;
import com.bmos.platform.service.adaptor.ServiceInterfaceAdapter;
import com.bmos.platform.service.tag.convert.TagDefineFieldConvert;
import com.bmos.platform.service.tag.convert.TagInstanceConvert;
import com.bmos.platform.service.tag.dto.*;
import com.bmos.platform.service.tag.enums.PrinterDpi;
import com.bmos.platform.service.tag.mapper.*;
import com.bmos.platform.service.tag.model.TagDefine;
import com.bmos.platform.service.tag.model.TagInstance;
import com.bmos.platform.service.tag.model.TagScene;
import com.bmos.platform.service.tag.model.TagSceneField;
import com.bmos.platform.service.tag.service.ITagInstanceService;
import com.bmos.platform.service.tag.util.TagUtil;
import com.bmos.platform.service.tag.util.ZplPrinterUtil;
import com.bmos.platform.service.tag.vo.TagInstanceDetailVO;
import com.bmos.platform.service.tag.vo.TagInstancePageVO;
import com.github.pagehelper.PageHelper;
import com.google.zxing.BarcodeFormat;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.jetbrains.annotations.Nullable;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ValidationException;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.List;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author liang
 * @version 1.0.0
 * @date 2024/3/7 10:32
 */
@Service
@Slf4j
public class TagInstanceServiceImpl implements ITagInstanceService {

    private static final String LOG_PREFIX = "[标签]";

    @Resource
    private ITagInstanceMapper tagInstanceMapper;

    @Resource
    private ITagTypeMapper tagTypeMapper;

    @Resource
    private ITagSceneMapper tagSceneMapper;

    @Resource
    private ITagDefineMapper tagDefineMapper;

    @Resource
    private ITagSceneFieldMapper tagDefineFieldMapper;

    @Resource
    private ServiceInterfaceAdapter serviceInterfaceAdapter;

    @Override
    public CommonPage<TagInstancePageVO> queryPage(TagInstancePageQuery pageQuery) {
        PageHelper.startPage(pageQuery.getPageNum(), pageQuery.getPageSize());
        List<TagInstancePageVO> tagInstancePageVOS = tagInstanceMapper.queryPage(pageQuery);
        return CommonPage.convertPage(tagInstancePageVOS);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createTagInstance(TagInstanceDTO dto) {
        log.info("{} 新增标签: {}", LOG_PREFIX, dto);
        // 参数合法性校验
        validateTagInstanceParams(dto, TagInstanceDTO.Create.class);
        TagInstance tagInstance = new TagInstance();
        tagInstance.setTagName(dto.getTagName());
        tagInstance.setTagTypeId(dto.getTagTypeId());
        tagInstance.setTagSceneId(dto.getTagSceneId());
        tagInstance.setTagDefineId(dto.getTagDefineId());
        tagInstance.setConfigFields(dto.getFields());
        // 默认禁用
        tagInstance.setEnable(BooleanEnum.FALSE);
        tagInstanceMapper.insert(tagInstance);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void editTagInstance(TagInstanceDTO dto) {
        log.info("{} 编辑标签: {}", LOG_PREFIX, dto);
        // 参数合法性校验
        TagInstance tagInstance = validateTagInstanceParams(dto, TagInstanceDTO.Update.class);
        tagInstance.setTagName(dto.getTagName());
        tagInstance.setTagTypeId(dto.getTagTypeId());
        tagInstance.setTagSceneId(dto.getTagSceneId());
        tagInstance.setTagDefineId(dto.getTagDefineId());
        tagInstance.setConfigFields(dto.getFields());
        tagInstanceMapper.updateById(tagInstance);
    }

    @Nullable
    @Override
    public TagInstanceDetailVO queryInfoById(Long id) {
        TagInstance tagInstance = tagInstanceMapper.selectById(id);
        if (tagInstance == null) {
            return null;
        }
        TagDefine tagDefine = tagDefineMapper.selectById(tagInstance.getTagDefineId());
        if (tagDefine == null) {
            return null;
        }
        TagScene tagScene = tagSceneMapper.selectById(tagInstance.getTagSceneId());
        if (tagScene == null) {
            return null;
        }
        List<TagSceneField> tagSceneFields = tagDefineFieldMapper.listTagDefineFieldsBySceneId(tagScene.getId());
        TagInstanceDetailVO result = TagInstanceConvert.INSTANCE.convertToDetailVO(tagInstance);
        result.setTagDefine(tagDefine);
        result.setTagScene(tagScene);
        result.setAvailableFields(TagDefineFieldConvert.INSTANCE.convertToVO(tagSceneFields));
        result.setPreviewHtml(TagUtil.renderHtmlBarCodeOnly(result.getPreviewHtml(), BarcodeFormat.valueOf(tagDefine.getBarcodeFormat().getValue()), "BMOS", tagDefine.getTagWidth(), tagDefine.getTagHeight()));
        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void enableTagInstance(Long id) {
        log.info("{} 启用标签实例: {}", LOG_PREFIX, id);
        TagInstance tagInstance = tagInstanceMapper.selectById(id);
        if (tagInstance == null) {
            throw new BmosException(PlatformResponseCode.TAG_INSTANCE_NOT_EXIST);
        }
        if (BooleanEnum.TRUE.equals(tagInstance.getEnable())) {
            throw new BmosException(PlatformResponseCode.TAG_INSTANCE_ENABLED);
        }
        // 已存在启用的该业务场景的标签实例
        if (tagInstanceMapper.getEnabledTagInstanceBySceneId(tagInstance.getTagSceneId()) != null) {
            throw new BmosException(PlatformResponseCode.TAG_INSTANCE_EXIST_ENABLED_INSTANCE_IN_SCENE);
        }
        tagInstance.setEnable(BooleanEnum.TRUE);
        tagInstanceMapper.updateById(tagInstance);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void disableTagInstance(Long id) {
        log.info("{} 禁用标签实例: {}", LOG_PREFIX, id);
        TagInstance tagInstance = tagInstanceMapper.selectById(id);
        if (tagInstance == null) {
            throw new BmosException(PlatformResponseCode.TAG_INSTANCE_NOT_EXIST);
        }
        if (!BooleanEnum.TRUE.equals(tagInstance.getEnable())) {
            throw new BmosException(PlatformResponseCode.TAG_INSTANCE_DISABLED);
        }
        tagInstance.setEnable(BooleanEnum.FALSE);
        tagInstanceMapper.updateById(tagInstance);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteTagInstance(Long id) {
        log.info("{} 删除标签实例: {}", LOG_PREFIX, id);
        TagInstance tagInstance = tagInstanceMapper.selectById(id);
        if (tagInstance == null) {
            throw new BmosException(PlatformResponseCode.TAG_INSTANCE_NOT_EXIST);
        }
        if (BooleanEnum.TRUE.equals(tagInstance.getEnable())) {
            throw new BmosException(PlatformResponseCode.TAG_INSTANCE_ENABLED);
        }
        tagInstanceMapper.deleteById(id);
    }

    @Override
    public void printTag(PrintCommonDTO printerDTO) {
        log.info("{}打印标签:{}", LOG_PREFIX, printerDTO);

        TagScene tagScene = tagSceneMapper.selectById(printerDTO.getSceneId());
        if (tagScene == null) {
            throw new BmosException(PlatformResponseCode.TAG_SCENE_NOT_EXIST);
        }

        TagInstance tagInstance = tagInstanceMapper.getEnabledTagInstanceBySceneId(printerDTO.getSceneId());
        if (tagInstance == null) {
            throw new BmosException(PlatformResponseCode.NO_ENABLE_INSTANCE_WITH_SCENE_ID);
        }
        TagDefine tagDefine = tagDefineMapper.selectById(tagInstance.getTagDefineId());
        if (tagDefine == null) {
            throw new BmosException(PlatformResponseCode.TAG_DEFINE_NOT_EXIST);
        }

        Map<String, Object> body = printerDTO.getBody();
        String dsServiceName = tagScene.getDataSourceServiceName();
        String dsInterface = tagScene.getDataSourceInterface();
        String printerIp = printerDTO.getPrinterIp();
        Integer printerPort = printerDTO.getPrinterPort();
        Integer dpi = printerDTO.getDpi();
        Integer dpiPoint = PrinterDpi.getDpiPoint(dpi);
        if (dpiPoint == null) {
            throw new BmosException(PlatformResponseCode.PRINTER_DPI_ILLEGAL);
        }
        String tagStyle = tagDefine.getTagStyle();
        log.info("{}打印标签实例:{}", LOG_PREFIX, tagStyle);
        log.info("{}调用服务:{}{}", LOG_PREFIX, dsServiceName, dsInterface);
        log.info("{}调用服务参数:{}", LOG_PREFIX, body);
        JSONObject invoke = serviceInterfaceAdapter.invoke(dsServiceName, dsInterface, body);
        String qrCodeField = tagScene.getQrCodeField();
        log.info("{}调用服务返回:{}", LOG_PREFIX, invoke);
        Map<String, TagInstanceField> fieldMap = tagInstance.getConfigFields()
                .stream()
                .filter(tagInstanceField -> tagInstanceField.getDataSourceField() != null)
                .collect(Collectors.toMap(TagInstanceField::getDefineField, Function.identity(), (k1, k2) -> k1));
        // 一维码不加场景id
        String qrCodePrefix = tagScene.getQrCodePrefix();
        if (tagDefine.getBarcodeFormat() == com.bmos.platform.common.enums.tag.BarcodeFormat.CODE_128) {
            qrCodePrefix = "";
        }
        // 默认使用203渲染
        File htmlFile = TagUtil.renderHtmlFile(tagDefine.getPreviewHtml(), BarcodeFormat.valueOf(tagDefine.getBarcodeFormat().getValue()), invoke, fieldMap, qrCodeField, qrCodePrefix, tagDefine.getTagWidth(), tagDefine.getTagHeight(), PrinterDpi.DPI_203);
        File previewPicture = TagUtil.getPreviewPicture(htmlFile, tagDefine.getTagWidth(), tagDefine.getTagHeight(), PrinterDpi.DPI_203);
        if (previewPicture == null) {
            return;
        }
        if (!Objects.equals(PrinterDpi.getDpiPoint(dpi), PrinterDpi.DPI_203.getDpiPoint())) {
            previewPicture = zoom(previewPicture, tagDefine, dpiPoint);
        }
        ZplPrinterUtil.sendZpl(printerIp, printerPort, ZplPrinterUtil.getZplCmdFromImage(previewPicture));
    }

    @Override
    public void printBatchTag(List<PrintCommonDTO> printerDTOList) {
        if (CollectionUtil.isEmpty(printerDTOList)) {
            return;
        }
        for (PrintCommonDTO printCommonDTO : printerDTOList) {
            printTag(printCommonDTO);
        }
    }

    @Override
    public void printBatchTags(PrintBatchDTO printBatchDTO) {
        log.info("{}打印标签:{}", LOG_PREFIX, printBatchDTO);
        TagScene tagScene = tagSceneMapper.selectById(printBatchDTO.getSceneId());
        if (tagScene == null) {
            throw new BmosException(PlatformResponseCode.TAG_SCENE_NOT_EXIST);
        }
        TagInstance tagInstance = tagInstanceMapper.getEnabledTagInstanceBySceneId(printBatchDTO.getSceneId());
        if (tagInstance == null) {
            throw new BmosException(PlatformResponseCode.NO_ENABLE_INSTANCE_WITH_SCENE_ID);
        }
        TagDefine tagDefine = tagDefineMapper.selectById(tagInstance.getTagDefineId());
        if (tagDefine == null) {
            throw new BmosException(PlatformResponseCode.TAG_DEFINE_NOT_EXIST);
        }
        Map<String, TagInstanceField> fieldMap = tagInstance.getConfigFields()
                .stream()
                .filter(tagInstanceField -> tagInstanceField.getDataSourceField() != null)
                .collect(Collectors.toMap(TagInstanceField::getDefineField, Function.identity(), (k1, k2) -> k1));
        String dsServiceName = tagScene.getDataSourceServiceName();
        String dsInterface = tagScene.getDataSourceInterface();
        String qrCodeField = tagScene.getQrCodeField();
        String printerIp = printBatchDTO.getPrinterIp();
        Integer printerPort = printBatchDTO.getPrinterPort();
        Integer dpi = printBatchDTO.getDpi();
        Integer dpiPoint = PrinterDpi.getDpiPoint(dpi);
        if (dpiPoint == null) {
            throw new BmosException(PlatformResponseCode.PRINTER_DPI_ILLEGAL);
        }
        String tagStyle = tagDefine.getTagStyle();
        for (Map<String, Object> body : printBatchDTO.getBodyList()) {
            log.info("{}打印标签实例:{}", LOG_PREFIX, tagStyle);
            log.info("{}调用服务:{}{}", LOG_PREFIX, dsServiceName, dsInterface);
            log.info("{}调用服务参数:{}", LOG_PREFIX, body);
            JSONObject invoke = serviceInterfaceAdapter.invoke(dsServiceName, dsInterface, body);
            log.info("{}调用服务返回:{}", LOG_PREFIX, invoke);
            // 一维码不加场景id
            String qrCodePrefix = tagScene.getQrCodePrefix();
            if (tagDefine.getBarcodeFormat() == com.bmos.platform.common.enums.tag.BarcodeFormat.CODE_128) {
                qrCodePrefix = "";
            }
            File htmlFile = TagUtil.renderHtmlFile(tagDefine.getPreviewHtml(), BarcodeFormat.valueOf(tagDefine.getBarcodeFormat().getValue()), invoke, fieldMap, qrCodeField, qrCodePrefix, tagDefine.getTagWidth(), tagDefine.getTagHeight(), PrinterDpi.DPI_203);
            File previewPicture = TagUtil.getPreviewPicture(htmlFile, tagDefine.getTagWidth(), tagDefine.getTagHeight(), PrinterDpi.DPI_203);
            if (previewPicture == null) {
                return;
            }
            if (!Objects.equals(PrinterDpi.getDpiPoint(dpi), PrinterDpi.DPI_203.getDpiPoint())) {
                previewPicture = zoom(previewPicture, tagDefine, dpiPoint);
            }
            ZplPrinterUtil.sendZpl(printerIp, printerPort, ZplPrinterUtil.getZplCmdFromImage(previewPicture));
        }

    }

    @Override
    public void previewTag(PrintPreviewDTO previewDTO, HttpServletResponse response) {
        log.info("{}预览标签:{}", LOG_PREFIX, previewDTO);
        TagScene tagScene = tagSceneMapper.selectById(previewDTO.getSceneId());
        if (tagScene == null) {
            throw new BmosException(PlatformResponseCode.TAG_SCENE_NOT_EXIST);
        }
        TagInstance tagInstance = tagInstanceMapper.getEnabledTagInstanceBySceneId(previewDTO.getSceneId());
        if (tagInstance == null) {
            throw new BmosException(PlatformResponseCode.NO_ENABLE_INSTANCE_WITH_SCENE_ID);
        }
        TagDefine tagDefine = tagDefineMapper.selectById(tagInstance.getTagDefineId());
        if (tagDefine == null) {
            throw new BmosException(PlatformResponseCode.TAG_DEFINE_NOT_EXIST);
        }
        Map<String, Object> body = previewDTO.getBody();
        String dsServiceName = tagScene.getDataSourceServiceName();
        String dsInterface = tagScene.getDataSourceInterface();
        String tagStyle = tagDefine.getTagStyle();
        log.info("{}打印标签实例:{}", LOG_PREFIX, tagStyle);
        log.info("{}调用服务:{}{}", LOG_PREFIX, dsServiceName, dsInterface);
        log.info("{}调用服务参数:{}", LOG_PREFIX, body);
        JSONObject invoke = serviceInterfaceAdapter.invoke(dsServiceName, dsInterface, body);
        log.info("{}调用服务返回:{}", LOG_PREFIX, invoke);
        String qrCodeField = tagScene.getQrCodeField();
        log.info("{}调用服务返回:{}", LOG_PREFIX, invoke);
        Map<String, TagInstanceField> fieldMap = tagInstance.getConfigFields()
                .stream()
                .filter(tagInstanceField -> tagInstanceField.getDataSourceField() != null)
                .collect(Collectors.toMap(TagInstanceField::getDefineField, Function.identity(), (k1, k2) -> k1));
        // 一维码不加场景id
        String qrCodePrefix = tagScene.getQrCodePrefix();
        if (tagDefine.getBarcodeFormat() == com.bmos.platform.common.enums.tag.BarcodeFormat.CODE_128) {
            qrCodePrefix = "";
        }
        File htmlFile = TagUtil.renderHtmlFile(tagDefine.getPreviewHtml(), BarcodeFormat.valueOf(tagDefine.getBarcodeFormat().getValue()), invoke, fieldMap, qrCodeField,qrCodePrefix , tagDefine.getTagWidth(), tagDefine.getTagHeight(), PrinterDpi.DPI_203);
        File previewPicture = TagUtil.getPreviewPicture(htmlFile, tagDefine.getTagWidth(), tagDefine.getTagHeight(), PrinterDpi.DPI_203);
        if (previewPicture == null) {
            return;
        }
        response.setContentType("image/png");
        // 图片缩放
        try {
            FileUtils.copyFile(previewPicture, response.getOutputStream());
        } catch (Exception e) {
            log.error("{}预览标签异常:{}", LOG_PREFIX, e);
        }
    }

    /**
     * 新增/编辑标签实例参数校验
     *
     * @param dto   标签实例参数
     * @param group 分组 TagInstanceDTO.Update.class 为更新 TagInstanceDTO.Create.class 为新增
     * @return 标签实例 用于更新时返回已存在的实例 新增则会返回null
     */
    private TagInstance validateTagInstanceParams(TagInstanceDTO dto, Class<?> group) {

        boolean validateTagName = true;

        TagInstance tagInstance = null;

        if (group == TagInstanceDTO.Update.class) {
            // 修改时校验标签实例是否存在
            tagInstance = tagInstanceMapper.selectById(dto.getId());
            if (tagInstance == null) {
                throw new BmosException(PlatformResponseCode.TAG_INSTANCE_NOT_EXIST);
            }
            // 先禁用才能修改
            if (BooleanEnum.TRUE.equals(tagInstance.getEnable())) {
                throw new BmosException(PlatformResponseCode.TAG_INSTANCE_ENABLED);
            }
            validateTagName = !StrUtil.equals(dto.getTagName(), tagInstance.getTagName());
        }
        // 判断标签名称是否存在
        if (validateTagName && tagInstanceMapper.existsTagName(dto.getTagName())) {
            throw new BmosException(PlatformResponseCode.TAG_NAME_EXIST);
        }
        // 判断标签类型是否存在
        if (tagTypeMapper.selectById(dto.getTagTypeId()) == null) {
            throw new BmosException(PlatformResponseCode.TAG_TYPE_NOT_EXIST);
        }
        // 判断标签场景是否存在
        if (tagSceneMapper.selectById(dto.getTagSceneId()) == null) {
            throw new BmosException(PlatformResponseCode.TAG_SCENE_NOT_EXIST);
        }
        // 判断标签定义是否存在
        TagDefine tagDefine = tagDefineMapper.selectById(dto.getTagDefineId());
        if (tagDefine == null) {
            throw new BmosException(PlatformResponseCode.TAG_DEFINE_NOT_EXIST);
        }
        // 校验模板参数名是否合法
        Set<String> defineFields = dto.getFields().stream()
                .map(TagInstanceField::getDefineField)
                .filter(StrUtil::isNotBlank)
                .collect(Collectors.toSet());
        if (defineFields.size() != dto.getFields().size()) {
            throw new ValidationException("标签字段配置名称重复");
        }
        // 校验数据源数是否合法
        Set<String> dateSourceFields = dto.getFields().stream()
                .map(TagInstanceField::getDataSourceField)
                .filter(StrUtil::isNotBlank)
                .collect(Collectors.toSet());

        // 从模板中校验是否包含所有的字段
        Set<String> idsFromHtml = getIdsFromHtml(tagDefine.getPreviewHtml());
        if (CollectionUtil.isNotEmpty(defineFields) && !idsFromHtml.containsAll(defineFields)) {
            throw new ValidationException("定义字段不合法");
        }

        if (CollectionUtil.isNotEmpty(dateSourceFields) && !tagDefineFieldMapper.checkIncludeFields(dto.getTagSceneId(), dateSourceFields)) {
            throw new ValidationException("数据源字段不合法");
        }

        return tagInstance;
    }

    private Set<String> getIdsFromHtml(String templateHtml) {
        Document document;
        try {
            document = Jsoup.parse(templateHtml);
        } catch (Exception e) {
            throw new RuntimeException("模板html解析失败, 格式错误");
        }
        Set<String> collect = document.getAllElements().stream()
                .filter(element -> element.hasAttr("id"))
                .map(Element::id)
                .filter(StrUtil::isNotBlank)
                .collect(Collectors.toSet());
        if (CollectionUtil.isEmpty(collect)) {
            return new HashSet<>();
        }
        return collect;
    }

    /**
     * 图片缩放
     *
     * @param previewPicture
     * @param tagDefine
     * @param dpiPoint
     */
    private static File zoom(File previewPicture, TagDefine tagDefine, Integer dpiPoint) {
        try {
            BufferedImage originalImage = ImageIO.read(previewPicture);
            int newWidth = tagDefine.getTagWidth() * dpiPoint;
            int newHeight = tagDefine.getTagHeight() * dpiPoint;
            BufferedImage scaledImage = new BufferedImage(newWidth, newHeight, originalImage.getType());
            Graphics2D g2d = scaledImage.createGraphics();
            g2d.drawImage(originalImage, 0, 0, newWidth, newHeight, null);
            g2d.dispose();
            File zoomFile = File.createTempFile(UUID.randomUUID().toString(), ".jpg");
            ImageIO.write(scaledImage, "png", zoomFile);
            return zoomFile;
        } catch (Exception e) {
            log.error("{}缩放图片失败", LOG_PREFIX, e);
            return null;
        }
    }
}
