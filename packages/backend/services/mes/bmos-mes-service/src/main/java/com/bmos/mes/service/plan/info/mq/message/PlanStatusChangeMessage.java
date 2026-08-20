package com.bmos.mes.service.plan.info.mq.message;

import com.bmos.mes.common.enums.plan.ProductPlanStartEnum;
import com.bmos.mes.service.plan.info.model.Plan;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @author yigaohui
 * @date 2024/7/3
 **/
@Data
@Accessors(chain = true)
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public class PlanStatusChangeMessage implements Serializable {
    private static final long serialVersionUID = -6478946606724060186L;

    private Plan plan;

    @JsonFormat(shape = JsonFormat.Shape.OBJECT)
    private ProductPlanStartEnum currentPlanStatus;
}
