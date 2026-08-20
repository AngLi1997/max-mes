package com.bmos.mes.service.dataset.handle.data;

import lombok.*;
import lombok.experimental.Accessors;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class PlanLoadingData extends BaseLoadingData {

    /**
     * 批次id
     */
    private Long planId;

}
