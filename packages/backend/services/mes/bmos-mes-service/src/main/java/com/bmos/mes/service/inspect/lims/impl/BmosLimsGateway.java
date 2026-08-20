package com.bmos.mes.service.inspect.lims.impl;

import cn.hutool.core.collection.CollUtil;
import com.bmos.common.response.ResponseInfo;
import com.bmos.lims2.feign.mes.MesInspectFeign;
import com.bmos.lims2.feign.mes.dto.MesDocumentConfigFeignVO;
import com.bmos.lims2.feign.mes.dto.MesDocumentConfigFieldFeignVO;
import com.bmos.lims2.feign.mes.dto.MesInitiateInspectFeignDTO;
import com.bmos.lims2.feign.mes.dto.MesRetryInspectFeignDTO;
import com.bmos.lims2.feign.mes.dto.MesSchemeFeignVO;
import com.bmos.mes.service.inspect.controller.vo.InspectConfigDataVO;
import com.bmos.mes.service.inspect.controller.vo.InspectConfigDetailVO;
import com.bmos.mes.service.inspect.controller.vo.InspectSchemeVO;
import com.bmos.mes.service.inspect.lims.InitiateInspectContext;
import com.bmos.mes.service.inspect.lims.LimsInspectGateway;
import com.bmos.mes.service.inspect.lims.LimsType;
import com.bmos.mes.service.inspect.lims.RetryInspectContext;
import com.bmos.mes.service.inspect.model.InspectInfo;
import com.bmos.mes.service.platform.FeignUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 自研 LIMS 网关：通过 bmos-lims2 feign 直连交互。
 */
@Component
public class BmosLimsGateway implements LimsInspectGateway {

    @Autowired
    private MesInspectFeign mesInspectFeign;

    @Override
    public LimsType type() {
        return LimsType.BMOS;
    }

    @Override
    public List<InspectConfigDetailVO> queryConfig(Long platformMaterialId) {
        List<MesDocumentConfigFeignVO> cfgList = FeignUtils.handleRequest(
                data -> mesInspectFeign.queryDocumentConfig(data), platformMaterialId).getData();
        if (CollUtil.isEmpty(cfgList)) {
            return java.util.Collections.emptyList();
        }
        List<InspectConfigDetailVO> result = new ArrayList<>(cfgList.size());
        for (MesDocumentConfigFeignVO cfg : cfgList) {
            result.add(toMesVO(cfg));
        }
        return result;
    }

    private InspectConfigDetailVO toMesVO(MesDocumentConfigFeignVO cfg) {
        InspectConfigDetailVO vo = new InspectConfigDetailVO();
        vo.setId(cfg.getId());
        vo.setName(cfg.getName());
        vo.setRemark(cfg.getRemark());
        List<InspectConfigDataVO> dataList = new ArrayList<>();
        if (CollUtil.isNotEmpty(cfg.getDataList())) {
            for (MesDocumentConfigFieldFeignVO f : cfg.getDataList()) {
                InspectConfigDataVO d = new InspectConfigDataVO();
                d.setCode(f.getCode());
                d.setId(f.getId());
                d.setShowName(f.getShowName());
                d.setDataName(f.getDataName());
                d.setRequired(f.getRequired());
                d.setDefaultValue(f.getDefaultValue());
                d.setSort(f.getSort());
                dataList.add(d);
            }
        }
        vo.setDataList(dataList);
        return vo;
    }

    @Override
    public List<InspectSchemeVO> querySchemes(Long materialId) {
        List<MesSchemeFeignVO> list = FeignUtils.handleRequest(
                data -> mesInspectFeign.querySchemes(data), materialId).getData();
        List<InspectSchemeVO> result = new ArrayList<>();
        if (CollUtil.isNotEmpty(list)) {
            for (MesSchemeFeignVO s : list) {
                InspectSchemeVO vo = new InspectSchemeVO();
                vo.setSchemeId(s.getSchemeId());
                vo.setSchemeVersionId(s.getSchemeVersionId());
                vo.setName(s.getName());
                vo.setDisplayName(s.getDisplayName());
                result.add(vo);
            }
        }
        return result;
    }

    @Override
    public String initiate(InitiateInspectContext ctx) {
        MesInitiateInspectFeignDTO dto = new MesInitiateInspectFeignDTO();
        dto.setPlatformMaterialId(ctx.getPlatformMaterialId());
        dto.setInspectConfigId(ctx.getInspectConfigId());
        dto.setSchemeId(ctx.getSchemeId());
        dto.setSchemeVersionId(ctx.getSchemeVersionId());
        dto.setMaterialBatchNo(ctx.getMaterialBatchNo());
        dto.setSourceSystem("MES");
        fillFieldMaps(ctx.getInspectInfos(), dto::setFields, dto::setFieldNames, dto::setFieldRequired);
        ResponseInfo<String> resp = FeignUtils.handleRequest(data -> mesInspectFeign.createInspectOrder(data), dto);
        return resp.getData();
    }

    @Override
    public String retry(RetryInspectContext ctx) {
        MesRetryInspectFeignDTO dto = new MesRetryInspectFeignDTO();
        dto.setOriginOrderNo(ctx.getOriginInspectNo());
        dto.setPlatformMaterialId(ctx.getPlatformMaterialId());
        dto.setInspectConfigId(ctx.getInspectConfigId());
        dto.setSchemeId(ctx.getSchemeId());
        dto.setSchemeVersionId(ctx.getSchemeVersionId());
        dto.setMaterialBatchNo(ctx.getMaterialBatchNo());
        dto.setSourceSystem("MES");
        fillFieldMaps(ctx.getInspectInfos(), dto::setFields, dto::setFieldNames, dto::setFieldRequired);
        ResponseInfo<String> resp = FeignUtils.handleRequest(data -> mesInspectFeign.retryInspectOrder(data), dto);
        return resp.getData();
    }

    /**
     * 把请验单信息列表拆成 fields / fieldNames / fieldRequired 三个 map。
     */
    private void fillFieldMaps(List<InspectInfo> infos,
                               java.util.function.Consumer<Map<String, String>> fieldsSetter,
                               java.util.function.Consumer<Map<String, String>> namesSetter,
                               java.util.function.Consumer<Map<String, Boolean>> requiredSetter) {
        Map<String, String> fields = new HashMap<>();
        Map<String, String> names = new HashMap<>();
        Map<String, Boolean> required = new HashMap<>();
        if (CollUtil.isNotEmpty(infos)) {
            for (InspectInfo i : infos) {
                fields.put(i.getCode(), i.getValue());
                names.put(i.getCode(), i.getShowName());
                required.put(i.getCode(), i.getRequired());
            }
        }
        fieldsSetter.accept(fields);
        namesSetter.accept(names);
        requiredSetter.accept(required);
    }
}
