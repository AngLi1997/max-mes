package com.bmos.lims2.feign.mes.dto;

import lombok.Data;
import java.util.List;

@Data
public class MesDocumentConfigFeignVO {
    /** 请验单Id（LIMS DocumentConfig.id，MES 作 templateId / inspectConfigId） */
    private Long id;
    private String name;
    private String remark;
    private List<MesDocumentConfigFieldFeignVO> dataList;
}
