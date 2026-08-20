package com.bmos.platform.facade.factory.vo;

import com.bmos.platform.facade.factory.enums.RoomStatusEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * 移动端获取房间信息VO
 */
@Getter
@Setter
@ApiModel("房间信息VO")
public class RoomInfoMobileFeignVO {

    /**
     * 房间ID
     */
    private Long id;

    /**
     * 房间编号
     */
    private String code;

    /**
     * 房间名称
     */
    private String name;

    /**
     * 房间状态
     * {@link com.bmos.platform.facade.factory.enums.RoomStatusEnum}
     */
    private Integer status;

    /**
     * 产品名称
     */
    private String productName;

    /**
     * 生产批号
     */
    private String batchNo;

    /**
     * 工序名称
     */
    private String procedureName;

    /**
     * 有效期至
     */
    private LocalDateTime expireTime;

}
