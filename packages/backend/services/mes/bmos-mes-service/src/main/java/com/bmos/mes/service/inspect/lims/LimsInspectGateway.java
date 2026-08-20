package com.bmos.mes.service.inspect.lims;

import com.bmos.mes.service.inspect.controller.vo.InspectConfigDetailVO;
import com.bmos.mes.service.inspect.controller.vo.InspectSchemeVO;

import java.util.List;

/** LIMS 对接网关（按参数开关选择三方 / 自研实现） */
public interface LimsInspectGateway {

    /** 适用类型 */
    LimsType type();

    /** 查请验单配置（一个物料可能对应多个请验单；自研路径走 LIMS；三方路径返回空列表由调用方走本地）。入参为平台物料id。 */
    List<InspectConfigDetailVO> queryConfig(Long platformMaterialId);

    /** 查检验方案（仅自研路径有意义）。入参为平台物料id。 */
    List<InspectSchemeVO> querySchemes(Long platformMaterialId);

    /**
     * 发起请验。
     * @return LIMS 生成的检验单号（三方路径返回 null，沿用入参 inspectNo 由调用方处理）
     */
    String initiate(InitiateInspectContext ctx);

    /**
     * 重新发起。
     * @return 新的检验单号（三方路径返回 null）
     */
    String retry(RetryInspectContext ctx);
}
