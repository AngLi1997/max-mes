package com.bmos.mes.service.product.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@TableName(value = "bm_material_log", autoResultMap = true)
public class MaterialLog {

    /**
     * id
     */
    private Long id;

    /**
     * 操作时间
     */
    private LocalDateTime operationTime;

    /**
     * 操作类型
     */
    private Integer operationType;

    /**
     * 具体操作类型
     */
    private Integer specificOperationType;

    /**
     * 物料类型
     */
    private Integer categoryType;

    /**
     * 操作人员id
     */
    private String userId;

    /**
     * 操作人员 userName
     */
    private String userName;

    /**
     * 操作人员登录账户
     */
    private String loginName;

    /**
     * 物料名称
     */
    private String materialName;

    /**
     * 物料id
     */
    private Long materialId;

    /**
     * 物料编码(合并)
     */
    private String materialCode;

    /**
     * 物料批次id
     */
    private Long materialBatchId;

    /**
     * 物料批号
     */
    private String materialBatchNo;

    /**
     * 物料件id
     */
    private Long storageMaterialId;

    /**
     * 物料件号
     */
    private String materialNo;

    /**
     * 预定量
     */
    private String scheduled;

    /**
     * 可用量
     */
    private String available;

    /**
     * 皮重
     */
    private String tareWeight;

    /**
     * 毛重
     */
    private String grossWeight;

    /**
     * 单位id
     */
    @TableField(exist = false)
    private Long unitId;

    /**
     * 单位
     */
    private String unitName;

    /**
     * 有效期
     */
    private String expirationTime;

    /**
     * 产品名称
     */
    private String productName;

    /**
     * 产品id
     */
    private Long productId;

    /**
     * 产品编码(合并)
     */
    private String productMergeCode;

    /**
     * 生产批号
     */
    private String batchNo;

    /**
     * 暂存货位
     */
    private String materialPositionName;

    /**
     * 货位编码
     */
    private String materialPositionCode;

    /**
     * 货位id
     */
    private Long materialPositionId;

    /**
     * 暂存间id
     */
    private Long storageId;

    /**
     * 所属区域
     */
    private String materialPositionPath;

    /**
     * 检验id 预留字段 查找检验信息使用
     */
    private Long inspectId;

    /**
     * 物料拓展信息
     */
    @TableField(value = "expand_info", typeHandler = JacksonTypeHandler.class)
    private MaterialExpandInfo expandInfo;

    /**
     * 原始编码
     */
    private String originalCode;

    /**
     * 原厂编号
     */
    private String originalNo;


    /**
     * 请验单号
     */
    private String requestVerifyNo;

    /**
     * 报告单号
     */
    private String reportNo;

    /**
     * 物料状态
     */
    private Boolean enable;

    /**
     * 备注
     */
    private String remark;

    /**
     * 供应商
     */
    private String supplier;

    /**
     * 生产商
     */
    private String producer;

    @ApiModelProperty("物料批次状态")
    private String qualityStatus;
}