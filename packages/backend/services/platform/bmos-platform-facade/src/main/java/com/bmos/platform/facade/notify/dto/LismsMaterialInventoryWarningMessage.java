package com.bmos.platform.facade.notify.dto;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author zht
 * @date 2025-01-25
 */
@Data
@ApiModel("集中化物料最低库存预警消息")
public class LismsMaterialInventoryWarningMessage {

    private LocalDateTime time;

    private List<String> materialNameList;
}
