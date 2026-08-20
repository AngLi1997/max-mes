package com.bmos.mes.service.weigh.centre.requirement.model;

import com.baomidou.mybatisplus.annotation.TableName;
import com.bmos.mybatis.dataobject.BaseDO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @author liang
 * @version 1.0.0
 * @date 2024/8/2 16:17
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("bm_weigh_input_process")
@AllArgsConstructor
@NoArgsConstructor
public class WeighInputProcess extends BaseDO {

    private Long componentInstanceId;

    private Boolean finished;
}
