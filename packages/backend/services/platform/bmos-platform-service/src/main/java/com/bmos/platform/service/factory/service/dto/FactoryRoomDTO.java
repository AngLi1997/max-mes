package com.bmos.platform.service.factory.service.dto;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.bmos.mybatis.dataobject.BaseDO;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 房间(BpFactoryRoom)实体类
 *
 * @author makejava
 * @since 2024-05-21 10:15:54
 */
@Getter
@Setter
public class FactoryRoomDTO {
    private Long id;

    /**
     * 房间编码
     */
    private String code;
    /**
     * 房间名称
     */
    private String name;
    /**
     * 房间状态
     */
    private Integer status;
    /**
     * 清洁时限(单位秒)
     */
    private Long timeLimit;
    /**
     * 被多少个业务配置绑定
     */
    private Integer useCount;
    /**
     * 房间清洁效期
     */
    @TableField(updateStrategy = FieldStrategy.IGNORED)
    private LocalDateTime expireTime;
    /**
     * 房间描述
     */
    private String description;
    /**
     * 所属房间模型id
     */
    private Long moduleId;

    /**
     * 是否启用
     */
    private Boolean enable;

    /**
     * 操作人id
     */
    private String operateId;

    /**
     * 操作人名称 login_name-user_name
     */
    private String operator;

    /**
     * 操作时间
     */
    private LocalDateTime operateTime;


    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    private String createBy;

    private String updateBy;

    private Boolean deleted;


    private List<RoomEnvPropertyWithAcquitPointDTO> roomEnvPropertyDTOList;

    private String moduleName;

    private String floorName;

    private String tenementName;

    private String cleanLevel;


    /**
     * 3D模型id
     */
    private String threeDModelId;

}

