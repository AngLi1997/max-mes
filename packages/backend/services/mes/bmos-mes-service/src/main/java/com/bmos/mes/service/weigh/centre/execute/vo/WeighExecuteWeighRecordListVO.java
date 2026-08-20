package com.bmos.mes.service.weigh.centre.execute.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * 称量结果列表
 * @author liang
 * @version 1.0.0
 * @date 2024/5/9 11:08
 */
@Data
@ApiModel("物料称量结果列表")
public class WeighExecuteWeighRecordListVO {

    /**
     * 物料称量列表
     */
    @ApiModelProperty(value = "物料称量列表")
    private List<WeighExecuteWeighRecordResult> mainList = new ArrayList<>();

    /**
     * 余料称量列表
     */
    @ApiModelProperty(value = "余料称量列表")
    private List<WeighExecuteWeighRecordResult> oddList = new ArrayList<>();

    /**
     * 称量人id
     */
    @ApiModelProperty(value = "称量人id", example = "1")
    private String weigherId;

    /**
     * 称量人名称
     */
    @ApiModelProperty(value = "称量人名称", example = "张三")
    private String weigherName;

    /**
     * 称量人登录名
     */
    @ApiModelProperty(value = "称量人登录名", example = "张三")
    private String weigherLoginName;

    /**
     * 复核人id
     */
    @ApiModelProperty(value = "复核人id", example = "1")
    private String reCheckerId;

    /**
     * 复核人名称
     */
    @ApiModelProperty(value = "复核人名称", example = "张三")
    private String reCheckerName;

    /**
     * 复核人登录名
     */
    @ApiModelProperty(value = "复核人登录名", example = "张三")
    private String reCheckerLoginName;

    /**
     * 工位id
     */
    @ApiModelProperty(value = "工位id", example = "1")
    private List<Long> station = new ArrayList<>();
}
