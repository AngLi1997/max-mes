package com.bmos.mes.service.plan.document.service.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 生成后的批记录进行保存
 */
@Getter
@Setter
public class GenerateBatchRecordDTO {

    /**
     * 生成的批记录相关信息
     */
    private List<BatchRecordArchiveSaveDTO> batchRecordArchiveSaveDTOList;

}
