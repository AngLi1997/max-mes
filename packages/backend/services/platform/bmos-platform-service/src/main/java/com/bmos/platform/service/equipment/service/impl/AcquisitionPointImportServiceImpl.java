package com.bmos.platform.service.equipment.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.alibaba.nacos.client.naming.utils.CollectionUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bmos.common.exception.BmosException;
import com.bmos.common.exporter.ExcelReaderUtils;
import com.bmos.common.exporter.ExcelWriterUtils;
import com.bmos.common.exporter.bo.OptionBo;
import com.bmos.common.exporter.bo.SheetDataBo;
import com.bmos.common.exporter.util.ExcelI18nUtil;
import com.bmos.common.util.i18n.I18nUtils;
import com.bmos.common.util.id.IdUtils;
import com.bmos.platform.common.exception.PlatformResponseCode;
import com.bmos.platform.service.equipment.enums.AcquisitionPlatformEnum;
import com.bmos.platform.service.equipment.mapper.AcquisitionPointMapper;
import com.bmos.platform.service.equipment.model.AcquisitionPoint;
import com.bmos.platform.service.equipment.service.AcquisitionPointImportService;
import com.bmos.platform.service.equipment.service.dto.AcquisitionPointDTO;
import com.bmos.platform.service.equipment.service.dto.AcquisitionPointExportDTO;
import com.bmos.platform.service.equipment.service.dto.AcquisitionPointImportErrorDTO;
import com.bmos.platform.service.equipment.service.enums.AcquisitionPointDataTypeEnum;
import com.bmos.platform.service.equipment.service.enums.AcquisitionPointStatusEnum;
import com.bmos.platform.service.equipment.service.enums.AcquisitionPointTypeEnum;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 导入服务实现
 * <p>
 *
 * @author yigaohui
 * @date 2024/4/22
 **/
@Service
public class AcquisitionPointImportServiceImpl extends ServiceImpl<AcquisitionPointMapper, AcquisitionPoint> implements AcquisitionPointImportService {

    private static final String IMPORT_TEMPLATE_SHEET_NAME = "采集点";

    private static final String IMPORT_TEMPLATE_SHEET_OPTION_COLUMN_TYPE = "采集点类型(必填)";
    private static final String IMPORT_TEMPLATE_SHEET_OPTION_COLUMN_DATA_TYPE = "采集点数据类型(必填)";
    private static final String IMPORT_TEMPLATE_SHEET_OPTION_COLUMN_ACQUISITION_PLATFORM = "采集点数采平台(必填)";

    private static final String IMPORT_TEMPLATE_NAME = "采集点数据_导入模板";
    private static final String EXPORT_FILE_NAME = "采集点数据_导出";
    private static final String EXPORT_ERROR_FILE_NAME = "采集点数据_导入失败错误信息";


    @Override
    public void getImportTemplate(HttpServletResponse response) {
        SheetDataBo sheetDataBo = new SheetDataBo(ExcelI18nUtil.getI18n(IMPORT_TEMPLATE_SHEET_NAME), AcquisitionPointDTO.class, null,
                getOptions());
        try {
            ExcelWriterUtils.write(IMPORT_TEMPLATE_NAME, response, Lists.newArrayList(sheetDataBo));
        } catch (Exception e) {
            log.error("生成模板出错", e);
        }
    }

    private List<OptionBo> getOptions() {
        List<String> typeOptions =
                Arrays.stream(AcquisitionPointTypeEnum.values()).map(AcquisitionPointTypeEnum::getName).collect(Collectors.toList());
        OptionBo typeOption = new OptionBo(ExcelI18nUtil.getI18n(IMPORT_TEMPLATE_SHEET_OPTION_COLUMN_TYPE), 3,
                typeOptions.stream().map(ExcelI18nUtil::getI18n).collect(Collectors.toList()));
        List<String> dataTypeOptions =
                Arrays.stream(AcquisitionPointDataTypeEnum.values()).map(AcquisitionPointDataTypeEnum::getName).collect(Collectors.toList());
        OptionBo dataTypeOption = new OptionBo(ExcelI18nUtil.getI18n(IMPORT_TEMPLATE_SHEET_OPTION_COLUMN_DATA_TYPE), 4,
                dataTypeOptions.stream().map(ExcelI18nUtil::getI18n).collect(Collectors.toList()));

        List<String> acquisitionPlatformOptions =
                Arrays.stream(AcquisitionPlatformEnum.values()).map(AcquisitionPlatformEnum::getName).collect(Collectors.toList());
        OptionBo acquisitionPlatformOption = new OptionBo(ExcelI18nUtil.getI18n(IMPORT_TEMPLATE_SHEET_OPTION_COLUMN_ACQUISITION_PLATFORM), 5,
                acquisitionPlatformOptions.stream().map(ExcelI18nUtil::getI18n).collect(Collectors.toList()));
        return Lists.newArrayList(typeOption, dataTypeOption,acquisitionPlatformOption);
    }

    @Override
    public void export(HttpServletResponse response, List<AcquisitionPointDTO> records) {
        SheetDataBo sheetDataBo = new SheetDataBo(IMPORT_TEMPLATE_SHEET_NAME, AcquisitionPointExportDTO.class,
                BeanUtil.copyToList(records, AcquisitionPointExportDTO.class),
                getOptions());
        try {
            ExcelWriterUtils.write(EXPORT_FILE_NAME, response, Lists.newArrayList(sheetDataBo));
        } catch (Exception e) {
            log.error("导出数据出错", e);
            throw new BmosException(PlatformResponseCode.EXPORT_TEMPLATE_EXPORT_ERROR);
        }
    }

    /**
     * 导入扩展属性
     *
     * @param response
     * @param file     文件
     */
    @Override
    public void importAcquisitionPoint(HttpServletResponse response, MultipartFile file) {
        try (InputStream inputStream = file.getInputStream()) {
            List<AcquisitionPointDTO> extAttrBos = ExcelReaderUtils.read(inputStream, AcquisitionPointDTO.class,
                    IMPORT_TEMPLATE_SHEET_NAME);
            if (CollectionUtils.isEmpty(extAttrBos)) {
                throw new BmosException(PlatformResponseCode.EXPORT_TEMPLATE_IMPORT_FILE_ERROR);
            }
            Pair<Boolean, List<AcquisitionPointImportErrorDTO>> importErrorBoPair = this.importValidate(extAttrBos);
            if (!importErrorBoPair.getLeft()) {
                this.writeErrorExcel(response, file, importErrorBoPair.getRight());
                return;
            }
            this.saveImportData(extAttrBos);
        } catch (IOException e) {
            log.error("读取文件失败", e);
        }
    }

    /**
     * 1.code 存在则不能导入
     * 2.name 重复不能导入
     * 3.excel中不能有重复数据
     *
     * @param acquisitionPointDTOS 导入数据
     * @return 判断结果
     */
    private Pair<Boolean, List<AcquisitionPointImportErrorDTO>> importValidate(List<AcquisitionPointDTO> acquisitionPointDTOS) {
        List<AcquisitionPoint> exitsCodes = getBaseMapper().selectList(new QueryWrapper<AcquisitionPoint>().lambda()
                .in(AcquisitionPoint::getCode,
                        acquisitionPointDTOS.stream().map(AcquisitionPointDTO::getCode).collect(Collectors.toList())));
        Set<String> codeExitsSet =
                exitsCodes.stream().map(AcquisitionPoint::getCode).collect(Collectors.toSet());

        List<AcquisitionPoint> exitsNames = getBaseMapper().selectList(new QueryWrapper<AcquisitionPoint>().lambda()
                .in(AcquisitionPoint::getName,
                        acquisitionPointDTOS.stream().map(AcquisitionPointDTO::getName).collect(Collectors.toList())));
        Set<String> nameExitsSet =
                exitsNames.stream().map(AcquisitionPoint::getName).collect(Collectors.toSet());

        // code导入重复的
        Map<String, List<AcquisitionPointDTO>> importCodeRepeat =
                acquisitionPointDTOS.stream().filter(item -> StringUtils.isNotBlank(item.getCode()))
                        .collect(Collectors.groupingBy(AcquisitionPointDTO::getCode));
        Set<String> repeatCodeImport =
                importCodeRepeat.entrySet().stream().filter(entry -> entry.getValue().size() > 1).map(Map.Entry::getKey).collect(Collectors.toSet());
        // name 导入重复得
        Map<String, List<AcquisitionPointDTO>> importNameRepeat =
                acquisitionPointDTOS.stream().filter(item -> StringUtils.isNotBlank(item.getName()))
                        .collect(Collectors.groupingBy(AcquisitionPointDTO::getName));
        Set<String> repeatNameImport =
                importNameRepeat.entrySet().stream().filter(entry -> entry.getValue().size() > 1).map(Map.Entry::getKey).collect(Collectors.toSet());

        boolean notError = true;
        List<AcquisitionPointImportErrorDTO> errorBos = new ArrayList<>();
        for (AcquisitionPointDTO acquisitionPointDTO : acquisitionPointDTOS) {
            StringBuffer errorMsgBuilder = new StringBuffer();
            // 校验必填
            if (StringUtils.isEmpty(acquisitionPointDTO.getCode())) {
                errorMsgBuilder.append(I18nUtils.getResponseMessage(PlatformResponseCode.EQUIPMENT_ACQUISITION_POINT_CODE_EMPTY.getCode(), "")).append("；");
            }
            if (StringUtils.isEmpty(acquisitionPointDTO.getName())) {
                errorMsgBuilder.append(I18nUtils.getResponseMessage(PlatformResponseCode.EQUIPMENT_ACQUISITION_POINT_NAME_EMPTY.getCode(), "")).append("；");
            }
            if (StringUtils.isEmpty(acquisitionPointDTO.getDataPointName())) {
                errorMsgBuilder.append(I18nUtils.getResponseMessage(PlatformResponseCode.EQUIPMENT_ACQUISITION_POINT_DATAPOINT_NAME_EMPTY.getCode(), "")).append("；");
            }
            if (acquisitionPointDTO.getType() == null) {
                errorMsgBuilder.append(I18nUtils.getResponseMessage(PlatformResponseCode.EQUIPMENT_ACQUISITION_POINT_TYPE_EMPTY.getCode(), "")).append("；");
            }
            if (acquisitionPointDTO.getDataType() == null) {
                errorMsgBuilder.append(I18nUtils.getResponseMessage(PlatformResponseCode.EQUIPMENT_ACQUISITION_POINT_DATATYPE_EMPTY.getCode(), "")).append("；");
            }
            // 已经存在的
            if (codeExitsSet.contains(acquisitionPointDTO.getCode())) {
                errorMsgBuilder.append(I18nUtils.getResponseMessage(PlatformResponseCode.EQUIPMENT_ACQUISITION_POINT_CODE_REPEAT.getCode(), "", acquisitionPointDTO.getCode())).append("；");
            } else if (repeatCodeImport.contains(acquisitionPointDTO.getCode())) {
                errorMsgBuilder.append(I18nUtils.getResponseMessage(PlatformResponseCode.EQUIPMENT_ACQUISITION_POINT_CODE_REPEAT.getCode(), "", acquisitionPointDTO.getCode())).append("；");
            }
            if (nameExitsSet.contains(acquisitionPointDTO.getName())) {
                errorMsgBuilder.append(I18nUtils.getResponseMessage(PlatformResponseCode.EQUIPMENT_ACQUISITION_POINT_NAME_REPEAT.getCode(), "", acquisitionPointDTO.getName())).append("；");
            } else if (repeatNameImport.contains(acquisitionPointDTO.getName())) {
                errorMsgBuilder.append(I18nUtils.getResponseMessage(PlatformResponseCode.EQUIPMENT_ACQUISITION_POINT_NAME_REPEAT.getCode(), "", acquisitionPointDTO.getName())).append("；");
            }
            String errorMsg = errorMsgBuilder.toString();
            notError &= StringUtils.isEmpty(errorMsg);
            AcquisitionPointImportErrorDTO bsExtAttrImportErrorBo = BeanUtil.copyProperties(acquisitionPointDTO,
                    AcquisitionPointImportErrorDTO.class);
            bsExtAttrImportErrorBo.setErrorMsg(errorMsg);
            errorBos.add(bsExtAttrImportErrorBo);
        }
        return Pair.of(notError, errorBos);
    }

    private void saveImportData(List<AcquisitionPointDTO> acquisitionPointDTOS) {
        List<AcquisitionPoint> addList = acquisitionPointDTOS.stream().map(item -> {
            AcquisitionPoint acquisitionPoint = BeanUtil.copyProperties(item, AcquisitionPoint.class);
            acquisitionPoint.setId(IdUtils.getSnowflake());
            acquisitionPoint.setStatus(AcquisitionPointStatusEnum.DISABLE);
            return acquisitionPoint;
        }).collect(Collectors.toList());
        saveBatch(addList);
    }

    private void writeErrorExcel(HttpServletResponse response, MultipartFile file,
                                 List<AcquisitionPointImportErrorDTO> bsExtAttrImportErrorBos) {

        SheetDataBo sheetDataBo = new SheetDataBo(IMPORT_TEMPLATE_SHEET_NAME, AcquisitionPointImportErrorDTO.class,
                bsExtAttrImportErrorBos, getOptions());
        try {
            response.setHeader("error-message", URLEncoder.encode("存在错误数据请处理后重新上传", "utf-8"));
            ExcelWriterUtils.write(EXPORT_ERROR_FILE_NAME, response,
                    Lists.newArrayList(sheetDataBo));
        } catch (Exception e) {
            log.error("导出数据出错", e);
        }
    }
}
