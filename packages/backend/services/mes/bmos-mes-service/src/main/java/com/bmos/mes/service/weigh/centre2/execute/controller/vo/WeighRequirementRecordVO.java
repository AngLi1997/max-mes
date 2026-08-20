package com.bmos.mes.service.weigh.centre2.execute.controller.vo;

import com.bmos.mes.common.enums.weigh.centre2.SignStatusEnum;
import lombok.Getter;
import lombok.Setter;
import java.util.Date;

@Getter
@Setter
public class WeighRequirementRecordVO {
    private Long id;
    private Double netWeight;
    private Double tareWeight;
    private Double grossWeight;
    private String unitName;
    private String weighUserId;
    private Date weighTime;
    private String deviceName;
    private String deviceCode;
    private String signUser;
    private Date signTime;
    private SignStatusEnum signStatus;
    private Long storageId;
    private String storageName;
    private String storageCode;
} 