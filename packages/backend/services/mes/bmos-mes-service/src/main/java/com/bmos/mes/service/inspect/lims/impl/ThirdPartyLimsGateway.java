package com.bmos.mes.service.inspect.lims.impl;

import com.alibaba.fastjson.JSON;
import com.bmos.api.feign.lims.IHlInspectFeign;
import com.bmos.mes.common.enums.inpspect.InspectConfigDataCodeEnum;
import com.bmos.mes.service.inspect.controller.vo.InspectConfigDetailVO;
import com.bmos.mes.service.inspect.controller.vo.InspectSchemeVO;
import com.bmos.mes.service.inspect.lims.InitiateInspectContext;
import com.bmos.mes.service.inspect.lims.LimsInspectGateway;
import com.bmos.mes.service.inspect.lims.LimsType;
import com.bmos.mes.service.inspect.lims.RetryInspectContext;
import com.bmos.mes.service.inspect.model.InspectInfo;
import com.bmos.mes.service.platform.FeignUtils;
import com.bmos.unit.service.UnitCache;
import cn.hutool.core.collection.CollUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/** 三方 LIMS 网关：保留原有 api 中转下发逻辑，行为不变。 */
@Component
public class ThirdPartyLimsGateway implements LimsInspectGateway {

    @Autowired
    private IHlInspectFeign iHlInspectFeign;

    @Autowired
    private UnitCache unitCache;

    @Override
    public LimsType type() {
        return LimsType.THIRD_PARTY;
    }

    @Override
    public List<InspectConfigDetailVO> queryConfig(Long platformMaterialId) {
        // 三方路径：请验单配置走 MES 本地逻辑，返回空列表由调用方走本地查询
        return Collections.emptyList();
    }

    @Override
    public List<InspectSchemeVO> querySchemes(Long materialId) {
        // 三方无检验方案概念
        return Collections.emptyList();
    }

    @Override
    public String initiate(InitiateInspectContext ctx) {
        List<InspectInfo> inspectInfos = ctx.getInspectInfos();
        if (CollUtil.isEmpty(inspectInfos)) {
            return null;
        }
        List<Map<String, String>> syncLimsParams = new ArrayList<>();
        Map<String, String> codeMap = new HashMap<>();
        for (InspectInfo inspectInfo : inspectInfos) {
            if (InspectConfigDataCodeEnum.SAMPLE_QUANTITY_UNIT.getValue().equals(inspectInfo.getCode())) {
                codeMap.put("sampleQuantityUnitName", unitCache.getGlobalUnitName(Long.valueOf(inspectInfo.getValue())));
            }
            codeMap.put(inspectInfo.getCode(), inspectInfo.getValue());
        }
        syncLimsParams.add(codeMap);
        FeignUtils.handleRequest(data -> iHlInspectFeign.sendInspectOrder(data), JSON.toJSONString(syncLimsParams));
        // 三方路径检验单号沿用 MES 既有 inspectNo，返回 null
        return null;
    }

    @Override
    public String retry(RetryInspectContext ctx) {
        // 三方路径重发维持现状（原方法体不下发），不做额外处理
        return null;
    }
}
