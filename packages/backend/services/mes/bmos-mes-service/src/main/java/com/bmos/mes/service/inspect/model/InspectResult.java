package com.bmos.mes.service.inspect.model;

import com.baomidou.mybatisplus.annotation.TableName;
import com.bmos.mes.common.enums.inpspect.InspectProgramResultEnum;
import com.bmos.mybatis.dataobject.BaseDO;
import lombok.Getter;
import lombok.Setter;

/**
 * 检验结论表(BmInspectResult)实体类
 *
 * @author makejava
 * @since 2025-02-17 18:25:46
 */
@TableName("bm_inspect_result")
@Getter
@Setter
public class InspectResult extends BaseDO {

    /**
     * 请验单主键id bm_inspect表主键
     */
    private Long inspectId;
    /**
     * 检验项代码
     */
    private String inspectProgramNo;

    /**
     * 字典对应的检验项目code
     */
    private String inspectDictNo;
    /**
     * 检验项名称
     */
    private String inspectProgramName;
    /**
     * 检验项结果
     */
    private String inspectResult;
    /**
     * 检验结论
     */
    private InspectProgramResultEnum inspectConclusion;

}

