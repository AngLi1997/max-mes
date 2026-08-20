package com.bmos.wms.service.inspect.model;

import com.baomidou.mybatisplus.annotation.TableName;
import com.bmos.mybatis.dataobject.BaseDO;
import lombok.Getter;
import lombok.Setter;

/**
 * WMS 检验结论（mirror MES bm_inspect_result）
 *
 * <p>结构沿用 MES 写法但 inspectConclusion 用 String（避免拉 MES 的
 * InspectProgramResultEnum 跨仓依赖）；取值与 MES 同：合格 / 不合格。
 */
@TableName("bw_inspect_result")
@Getter
@Setter
public class InspectResult extends BaseDO {

    /** 请验单主键id（bw_inspect.id） */
    private Long inspectId;
    /** 检验项代码（LIMS 分析项 code） */
    private String inspectProgramNo;
    /** 字典对应的检验项目 code */
    private String inspectDictNo;
    /** 检验项名称 */
    private String inspectProgramName;
    /** 检验项结果（原始值） */
    private String inspectResult;
    /** 检验结论（值与 MES InspectProgramResultEnum.code 对齐） */
    private String inspectConclusion;
}
