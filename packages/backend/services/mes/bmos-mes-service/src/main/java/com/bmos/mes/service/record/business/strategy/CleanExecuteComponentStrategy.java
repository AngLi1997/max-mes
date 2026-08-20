package com.bmos.mes.service.record.business.strategy;

import cn.hutool.core.date.LocalDateTimeUtil;
import cn.hutool.core.util.BooleanUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.bmos.common.util.json.JsonUtils;
import com.bmos.mes.common.enums.record.BusinessComponentTypeEnum;
import com.bmos.mes.common.model.execute.ExecuteFormDataBaseExtInfo;
import com.bmos.mes.common.utils.TimeUtil;
import com.bmos.mes.service.execute.convert.ExecuteFormDataConverter;
import com.bmos.mes.service.execute.model.ExecuteFormData;
import com.bmos.mes.service.execute.vo.BusinessComponentConfigDetailVO;
import com.bmos.mes.service.facotry.service.data.FactoryRoomInfo;
import com.bmos.mes.service.record.business.BusinessComponentStrategy;
import com.bmos.mes.service.record.business.model.ProductionDetailInfo;
import com.bmos.mes.service.record.vo.ComponentListVO;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 清场信息组件
 */
@Component("CLEAN_IMPLEMENT")
public class CleanExecuteComponentStrategy implements BusinessComponentStrategy {
    @Override
    public void handleBusinessComponent(List<ExecuteFormData> results, ComponentListVO component,
                                        ProductionDetailInfo info, Map<Long, BusinessComponentConfigDetailVO> configMap,
                                        Integer index) {
        FactoryRoomInfo factoryRoomInfo = info.getFactoryRoomInfo();
        List<ComponentListVO> children = component.getChildren();
        List<ComponentListVO> lastLevelComponent = new ArrayList<>();
        for (ComponentListVO child : children) {
            lastLevelComponent.addAll(child.getChildren());
        }
        results.addAll(lastLevelComponent.stream()
                .filter(e -> BooleanUtil.isTrue(e.getUsed()))
                .map(componentListVO-> {
                    ExecuteFormData formData = ExecuteFormDataConverter.INSTANCE.convert(factoryRoomInfo);
                    formData.setFieldId(componentListVO.getFieldId());
                    formData.setComponentType(componentListVO.getComponentType());
                    formData.setDiscard(false);
                    formData.setValue(this.getValueByType(componentListVO.getComponentType(), factoryRoomInfo));
                    formData.setExtInfo(this.buildExtInfo(componentListVO.getComponentType(), factoryRoomInfo));
                    return formData;
                }).collect(Collectors.toList()));
    }

    private String getValueByType(String type, FactoryRoomInfo factoryRoomInfo) {
        // 生成取值
        BusinessComponentTypeEnum enumByValue = BusinessComponentTypeEnum.getEnumByValue(type);
        switch (enumByValue){
            case CLEAN_IMPLEMENT_BATCHNO:
                return factoryRoomInfo.getBatchNo();
            case CLEAN_IMPLEMENT_PRODUCT_NAME:
                return factoryRoomInfo.getProductName();
            case CLEAN_IMPLEMENT_PRODUCT_NO:
                return factoryRoomInfo.getProductNo();
            case CLEAN_IMPLEMENT_PROCEDURE:
                return factoryRoomInfo.getProcedureName();
            case CLEAN_IMPLEMENT_ROOM_NAME:
                return factoryRoomInfo.getRoomName();
            case CLEAN_IMPLEMENT_ROOM_CODE:
                return factoryRoomInfo.getRoomCode();
            case CLEAN_IMPLEMENT_START_TIME:
                if (StrUtil.isNotEmpty(factoryRoomInfo.getBeginTime())){
                    return LocalDateTimeUtil.format(LocalDateTime.parse(factoryRoomInfo.getBeginTime(), DateTimeFormatter.ofPattern(TimeUtil.F_DATETIME)), TimeUtil.F_DATETIME_MINUTE);
                }
                return factoryRoomInfo.getBeginTime();
            case CLEAN_IMPLEMENT_END_TIME:
                if (StrUtil.isNotEmpty(factoryRoomInfo.getEndTime())){
                    return LocalDateTimeUtil.format(LocalDateTime.parse(factoryRoomInfo.getEndTime(), DateTimeFormatter.ofPattern(TimeUtil.F_DATETIME)), TimeUtil.F_DATETIME_MINUTE);
                }
                return factoryRoomInfo.getEndTime();
            case CLEAN_IMPLEMENT_CLEAN_DATE:
                return factoryRoomInfo.getCleanDate();
            case CLEAN_IMPLEMENT_EXPIRATION_DATE:
                if (StrUtil.isNotEmpty(factoryRoomInfo.getExpireDate())){
                    return LocalDateTimeUtil.format(LocalDateTime.parse(factoryRoomInfo.getExpireDate(), DateTimeFormatter.ofPattern(TimeUtil.F_DATETIME)), TimeUtil.F_DATETIME_MINUTE);
                }
                return factoryRoomInfo.getExpireDate();
            case CLEAN_IMPLEMENT_CLEAN_PERSON:
                return factoryRoomInfo.getOperator();
            case CLEAN_IMPLEMENT_QUALITY_INSPECTION_PERSON:
                return factoryRoomInfo.getVerifier();
            case CLEAN_IMPLEMENT_ROOM_QUALITY_INSPECTION_DATE:
                return factoryRoomInfo.getVerifyDate();
        }
        return null;
    }

    /**
     * 构建扩展字段
     * @param componentType
     * @param factoryRoomInfo
     * @return
     */
    private String buildExtInfo(String componentType, FactoryRoomInfo factoryRoomInfo) {
        BusinessComponentTypeEnum enumByValue = BusinessComponentTypeEnum.getEnumByValue(componentType);
        ExecuteFormDataBaseExtInfo executeFormDataBaseExtInfo = new ExecuteFormDataBaseExtInfo();
        switch (enumByValue){
            case CLEAN_IMPLEMENT_START_TIME:
                if (ObjectUtil.isNull(factoryRoomInfo.getBeginTime())){
                    return null;
                }
                executeFormDataBaseExtInfo.setTimeStamp(convertToTimeStamp(factoryRoomInfo.getBeginTime()));
                return JsonUtils.toJsonString(executeFormDataBaseExtInfo);
            case CLEAN_IMPLEMENT_END_TIME:
                if (ObjectUtil.isNull(factoryRoomInfo.getEndTime())){
                    return null;
                }
                executeFormDataBaseExtInfo.setTimeStamp(convertToTimeStamp(factoryRoomInfo.getEndTime()));
                return JsonUtils.toJsonString(executeFormDataBaseExtInfo);
            case CLEAN_IMPLEMENT_EXPIRATION_DATE:
                if (ObjectUtil.isNull(factoryRoomInfo.getExpireTime())){
                    return null;
                }
                executeFormDataBaseExtInfo.setTimeStamp(convertToTimeStamp(factoryRoomInfo.getExpireTime()));
                return JsonUtils.toJsonString(executeFormDataBaseExtInfo);
            case CLEAN_IMPLEMENT_CLEAN_DATE:
                if (ObjectUtil.isNull(factoryRoomInfo.getEndTime())){
                    return null;
                }
                executeFormDataBaseExtInfo.setTimeStamp(convertToTimeStamp(factoryRoomInfo.getEndTime()));
                return JsonUtils.toJsonString(executeFormDataBaseExtInfo);
            case CLEAN_IMPLEMENT_ROOM_QUALITY_INSPECTION_DATE:
                if (ObjectUtil.isNull(factoryRoomInfo.getVerifyDate())){
                    return null;
                }
                executeFormDataBaseExtInfo.setTimeStamp(convertToTimeStamp(factoryRoomInfo.getVerifyDate()));
                return JsonUtils.toJsonString(executeFormDataBaseExtInfo);
        }
        return null;
    }

    private String convertToTimeStamp(String time) {
        LocalDateTime parse = LocalDateTimeUtil.parse(time, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        return convertToTimeStamp(parse);
    }

    private String convertToTimeStamp(LocalDateTime time) {
        // 结合系统默认时区转为ZonedDateTime
        ZonedDateTime zonedDateTime = time.atZone(ZoneId.systemDefault());
        // 将ZonedDateTime转换为时间戳
        return String.valueOf(zonedDateTime.toInstant().toEpochMilli());
    }
}
