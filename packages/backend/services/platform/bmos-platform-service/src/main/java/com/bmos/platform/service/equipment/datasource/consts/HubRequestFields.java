package com.bmos.platform.service.equipment.datasource.consts;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

/**
 * @author yigaohui
 * @date hub 请求的字段
 **/
public final class HubRequestFields {
    /**
     * 访问token，值通过publicKey获取或者通过登录获取
     */
    public static final String HEADER_ACCESS_TOKEN = "token";
    /**
     * 租户id，对mes-hub来说，这个值应该是固定的
     */
    public static final String HEADER_TENANT_ID = "isc-tenant-id";

    /**
     * tagNames 字段，根据点位名称获取数据的字段
     */
    public static final String GET_DATA_TAG_NAMES = "tagNames";


    /**
     * id字段
     */
    public static final String GET_TAG_ID = "id";


    /**
     * 名称字段
     */
    public static final String GET_GAT_NAME = "names";


    public static final String TAG_ID="tagId";

    public static final String START_TIME="startTime";

    public static final String END_TIME="endTime";

    public static final String SIZE="size";

    public static final String CURRENT="current";

    public static final String IS_NUMBER="isNumber";

    /**
     * 数采参数配置code
     */
    public static final String PARAMETER_CODE = "platform.sys.acquisition-address";

}
