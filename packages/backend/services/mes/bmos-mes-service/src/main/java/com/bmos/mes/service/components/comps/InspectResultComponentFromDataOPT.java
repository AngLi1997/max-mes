package com.bmos.mes.service.components.comps;

import com.bmos.mes.service.components.annotations.BmosComponentDetail;
import lombok.Data;

import static com.bmos.mes.common.enums.record.BusinessComponentTypeEnum.*;

/**
 * 请验结果组件
 */
@Data
public class InspectResultComponentFromDataOPT {

    @BmosComponentDetail(INSPECT_PLEASE_CHECK_NO)
    private String inspectNo;

    @BmosComponentDetail(INSPECT_MATERIAL_NAME)
    private String materialName;

    @BmosComponentDetail(INSPECT_MATERIAL_CODE)
    private String materialCode;

    @BmosComponentDetail(INSPECT_MATERIAL_SPECIFICATION)
    private String materialSpecification;

    @BmosComponentDetail(INSPECT_MATERIAL_BATCH_NUMBER)
    private String materialBatchNo;

    @BmosComponentDetail(INSPECT_VERIFY_USER)
    private String verifyUser;

    @BmosComponentDetail(INSPECT_VERIFY_DATE)
    private String verifyDate;

}
