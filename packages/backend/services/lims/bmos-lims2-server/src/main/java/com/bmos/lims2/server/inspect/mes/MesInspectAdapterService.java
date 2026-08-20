package com.bmos.lims2.server.inspect.mes;

import com.bmos.lims2.feign.mes.dto.MesDocumentConfigFeignVO;
import com.bmos.lims2.feign.mes.dto.MesInitiateInspectFeignDTO;
import com.bmos.lims2.feign.mes.dto.MesRetryInspectFeignDTO;
import com.bmos.lims2.feign.mes.dto.MesSchemeFeignVO;

import java.util.List;

public interface MesInspectAdapterService {

    List<MesDocumentConfigFeignVO> queryDocumentConfig(Long platformMaterialId);

    List<MesSchemeFeignVO> querySchemes(Long platformMaterialId);

    /** 建单+确认，返回 orderNo */
    String createInspectOrder(MesInitiateInspectFeignDTO dto);

    /** 作废原单+新建确认，返回新 orderNo */
    String retryInspectOrder(MesRetryInspectFeignDTO dto);
}
