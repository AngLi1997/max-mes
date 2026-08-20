package com.bmos.wms.service.inspect.lims;

import com.bmos.wms.service.inspect.controller.vo.InspectConfigDetailVO;
import com.bmos.wms.service.inspect.controller.vo.InspectSchemeVO;

import java.util.List;

/**
 * LIMS 对接网关（按平台参数开关选择 三方 / 自研 实现）。
 *
 * <p>WMS 当前只实现 BMOS 一种；THIRD_PARTY 在 selector 层显式拒绝。
 */
public interface LimsInspectGateway {

    /** 适用类型 */
    LimsType type();

    /**
     * 查请验单配置（一物料可能对应多个请验单，由前端选择）。
     * @param platformMaterialId 平台物料 id（不是 WMS cargoId）
     */
    List<InspectConfigDetailVO> queryConfig(Long platformMaterialId);

    /**
     * 查检验方案（仅 BMOS 路径有意义）。
     * @param platformMaterialId 平台物料 id
     */
    List<InspectSchemeVO> querySchemes(Long platformMaterialId);

    /** 发起请验，返回 LIMS 检验单号。 */
    String initiate(InitiateInspectContext ctx);

    /** 重新发起请验，返回新的 LIMS 检验单号。 */
    String retry(RetryInspectContext ctx);
}
