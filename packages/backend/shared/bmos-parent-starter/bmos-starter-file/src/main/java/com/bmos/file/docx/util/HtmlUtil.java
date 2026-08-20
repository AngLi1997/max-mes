package com.bmos.file.docx.util;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.BooleanUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONUtil;
import com.bmos.common.util.json.JsonUtils;
import com.bmos.file.docx.constant.ComponentConstant;
import com.bmos.file.docx.dto.ComponentDTO;
import com.bmos.file.docx.dto.FieldValueDTO;
import com.bmos.file.docx.dto.ImageVO;
import com.bmos.file.docx.model.ExpandTableInfo;
import com.bmos.file.docx.model.ExtendTableValueMap;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * html 工具类
 *
 * @author yigaohui
 * @date 2024/6/7
 **/
public final class HtmlUtil {
    public static String mergeHtml(String html, List<ComponentDTO> components, List<FieldValueDTO> data,
                                   List<ImageVO> imageBase64List) {
        Document doc = Jsoup.parse(html);
        doc.charset(StandardCharsets.UTF_8);
        Map<Long, FieldValueDTO> dataMap =
                data.stream().collect(Collectors.toMap(FieldValueDTO::getFieldId, Function.identity()));
        components.forEach(component -> {
            Element element = doc.getElementById(component.getFieldId() + "");
            // 普通的值填充
            FieldValueDTO fieldValueDTO = dataMap.get(component.getFieldId());
            if (element == null) {
                return;
            }
            // 当前的style ：style="height: 20px; vertical-align: top; position: relative; top: -2px; text-indent: 2px; font-family: SimSun; font-size: 16px; max-width: 98%; overflow: hidden; width: 125px; border-radius: 2px; border: 1px solid rgb(144, 147, 152);"
            // 如果textarea标签设置了border，去掉
            if (element.tagName().equals("textarea") && StringUtils.isNotBlank(element.attr("style")) && element.attr("style").contains("border")) {
                // 去掉style中设置的边框，保留其他的属性
                element.attr("style", element.attr("style").replaceAll("\\s*border\\s*:\\s*[^;]+;?", ""));
            }


            if (fieldValueDTO != null && StringUtils.isNotEmpty(fieldValueDTO.getValue())) {
                //处理拍照上传组件以及签名组件值
                if (ComponentConstant.PIC_COMPONENT.contains(fieldValueDTO.getComponentType()) && CollectionUtil.isNotEmpty(fieldValueDTO.getImgs())) {
                    element.val("");
                    handlePhotoAndSignature(fieldValueDTO.getImgs(), element);
                } else if (ComponentConstant.EXTEND_TABLE.contains(fieldValueDTO.getComponentType())) {
                    handleExtendTable(fieldValueDTO.getValue(), element, component.getComponentDetail());
                } else if (ComponentConstant.AUTO_ADAPT_SIZE_PIC_COMPONENT.contains(fieldValueDTO.getComponentType())) {
                    handleAutoAdaptSizePicComponent(fieldValueDTO.getImgs(), element);
                } else {
                    element.val(fieldValueDTO.getValue());
                }
            } else {
                element.val(" ");
                return;
            }
            // 如果是单选或者复选框，要通过名称获取
            Elements elements = doc.getElementsByAttributeValue("name", component.getFieldId() + "");
            elements.forEach(e -> {
                // 多选需要将值转换成json数组
                if (ComponentConstant.CHECKBOX.equals(fieldValueDTO.getComponentType())) {
                    handleCheckBox(fieldValueDTO, e);
                } else if (ComponentConstant.RADIO.equals(fieldValueDTO.getComponentType())) {
                    handleRadio(fieldValueDTO, e);
                }
                // LIMS结论组件需要单独处理
                else if (ComponentConstant.CONCLUSION.equals(fieldValueDTO.getComponentType())) {
                    handleConclusion(fieldValueDTO, e);
                }

            });
        });
        // 将textarea 换成text
        doc.select("textarea").forEach(e -> e.tagName("text"));
        if (CollectionUtil.isNotEmpty(imageBase64List)) {
            imageBase64List.forEach(image -> {
                Element body = doc.body();
                body.appendElement("img").attr("src", image.getValue());
                if (StrUtil.isNotBlank(image.getEvidenceName())) {
                    body.appendElement("br");
                    body.appendText(image.getEvidenceName());
                }
                if (StrUtil.isNotBlank(image.getEvidenceTime())) {
                    body.appendElement("br");
                    body.appendText(image.getEvidenceTime());
                }
            });
        }
        // 处理掉单选多选的边框
        doc.select("input").forEach(e -> {
            if (StringUtils.equalsIgnoreCase(ComponentConstant.RADIO, e.attr("type")) || StringUtils.equalsIgnoreCase(ComponentConstant.CHECKBOX, e.attr("type"))) {
                e.parent().parent().attr("style", "border:none");
            }
        });
        // 添加table的样式
        //<table style="margin-bottom: 10px;border-collapse: collapse;">
        doc.select("table").forEach(e -> {
            StringBuilder sb = new StringBuilder("margin-bottom: 10px;border-collapse: collapse;");
            if (StringUtils.isNotBlank(e.attr("style"))) {
                sb.append(e.attr("style"));
            }
            e.attr("style", sb.toString());
        });
        // 修改td的样式
        //<td style="padding: 5px 10px;border: 1px solid #000;"
        doc.select("td").forEach(e -> {
            StringBuilder sb = new StringBuilder("border: 1px solid #000;");
            if (StringUtils.isNotBlank(e.attr("style"))) {
                sb.append(e.attr("style"));
            }
            e.attr("style", sb.toString());
        });
        return doc.html();
    }

    private static void handleAutoAdaptSizePicComponent(List<ImageVO> imgs, Element element) {
        imgs.forEach(item -> {
            element.appendElement("img")
                    .attr("src", item.getValue())
                    .attr("style", getImgSizeStyle(element));
            if (StrUtil.isNotEmpty(item.getImageCaption())) {
                element.appendElement("br");
                element.appendText(item.getImageCaption());
            }
        });
    }

    private static String getImgSizeStyle(Element element) {
        String style = element.attr("style");
        return adaptStylePxToPdf(style);
    }

    /**
     * 直接按原大小会超出页面大小
     *
     * @param style
     * @return
     */
    private static String adaptStylePxToPdf(String style) {
        StringBuilder adaptedStyle = new StringBuilder();
        String[] styles = style.split(";");
        for (String s : styles) {
            String[] keyValue = s.split(":");
            if (keyValue.length == 2) {
                String property = keyValue[0].trim();
                String value = keyValue[1].trim();

                if (value.endsWith("px")) {
                    // 转换 px
                    try {
                        int px = Integer.parseInt(value.replace("px", "").trim());
                        int pt = (int) (px * 0.75); // 转换公式
                        adaptedStyle.append(property).append(": ").append(pt).append("px; ");
                    } catch (NumberFormatException e) {
                        adaptedStyle.append(property).append(": ").append(value).append("; ");
                    }
                } else {
                    adaptedStyle.append(property).append(": ").append(value).append("; ");
                }
            }
        }
        return adaptedStyle.toString().trim();
    }


    /**
     * 处理拓展表格
     *
     * @param value           组件值
     * @param table
     * @param componentDetail 组件详情 含有表头信息
     */
    private static void handleExtendTable(String value, Element table, String componentDetail) {
        List<ExtendTableValueMap> values = JsonUtils.parseArray(value, ExtendTableValueMap.class);
        ExpandTableInfo tableConfig = JsonUtils.parseObject(componentDetail, ExpandTableInfo.class);
        if (tableConfig == null || CollUtil.isEmpty(tableConfig.getTableList())) {
            return;
        }
        Elements tbodys = table.getElementsByTag("tbody");
        Element tbody = tbodys.get(0);
        // 表格是否需要扩展
        int expandSize = values.size() - tableConfig.getRowNum();
        while (expandSize-- > 0) {
            tbody.appendElement("tr");
        }
        Elements allLines = table.getElementsByTag("tr");
        for (int i = 0; i < values.size(); i++) {
            Element tr = allLines.get(i + 1);
            tr.empty();
            for (ExpandTableInfo.ExpandTableColumn column : tableConfig.getTableList()) {
                tr.appendElement("td").text(values.get(i).get(column.getColData()));
            }
        }
    }

    private static void handleRadio(FieldValueDTO fieldValueDTO, Element e) {
        if (StringUtils.equals(e.tagName(), "input")) {
            e.parent().parent().attr("style", "border:none");
            // 如果是录入的空值，设置特殊符号
            if (BooleanUtil.isTrue(fieldValueDTO.getEmptyValue())) {
                e.parent().text("⊗" + e.parent().text() + " ");
                e.tagName("label");
                return;
            }
            if (StringUtils.equals(e.parent().text(), fieldValueDTO.getValue())) {
                e.parent().text("⦿" + e.parent().text() + " ");
                e.tagName("label");
            } else {
                e.tagName("label");
                e.parent().text("⭘" + e.parent().text() + " ");
            }
        }
    }


    private static void handleConclusion(FieldValueDTO fieldValueDTO, Element e) {
        if (StringUtils.equals(e.tagName(), "input")) {
            e.parent().parent().attr("style", "border:none");
            // 如果是录入的空值，设置特殊符号
            if (BooleanUtil.isTrue(fieldValueDTO.getEmptyValue())) {
                e.parent().text("⊗" + e.parent().text() + " ");
                e.tagName("label");
                return;
            }
            if (StringUtils.equals(e.parent().text(), fieldValueDTO.getValueExtension())) {
                e.parent().text("⦿" + e.parent().text() + " ");
                e.tagName("label");
            } else {
                e.tagName("label");
                e.parent().text("⭘" + e.parent().text() + " ");
            }
        }
    }

    /**
     * html转成word后，多选的选中样式默认选择框回显示一个×，现在要在这里将html中的多选组件替换成文本，选中的样式为勾勾
     *
     * @param fieldValueDTO
     * @param e
     */
    private static void handleCheckBox(FieldValueDTO fieldValueDTO, Element e) {
        if (StringUtils.equals(e.tagName(), "input")) {
            // 去除掉多选的边框
            e.parent().parent().attr("style", "border:none");
            // 将e换成一个label
            // 如果是录入的空值，设置特殊符号
            if (BooleanUtil.isTrue(fieldValueDTO.getEmptyValue())) {
                e.parent().text("⊠" + e.parent().text() + " ");
                e.tagName("label");
                return;
            }
            String value = fieldValueDTO.getValue();
            JSONArray checkArray = JSONUtil.parseArray(value);
            Set<String> checkSet = checkArray.stream().map(Object::toString).collect(Collectors.toSet());
            if (checkSet.stream().anyMatch(s -> s.equals(e.parent().text()))) {
                e.parent().text("√" + e.parent().text() + " ");
                e.tagName("label");
            } else {
                e.tagName("label");
                e.parent().text("□" + e.parent().text() + " ");
            }
        }
    }

    //处理拍照上传组件以及签名组件照片内容
    private static void handlePhotoAndSignature(List<ImageVO> images, Element element) {
        images.forEach(item -> {

            element.appendElement("img")
                    .attr("src", item.getValue())
                    .attr("style", "width:64px; height:64px;");
            if (StrUtil.isNotBlank(item.getEvidenceName()) && StrUtil.isNotBlank(item.getEvidenceTime())) {
                element.appendElement("br");
                element.appendText(item.getEvidenceName());
                element.appendElement("br");
                element.appendText(item.getEvidenceTime());
                element.appendElement("br");
            }
            // 拍照组件图片可以添加备注
            if (StrUtil.isNotBlank(item.getImageCaption())) {
                element.appendElement("br");
                element.appendText(item.getImageCaption());
            }
        });
    }
}
