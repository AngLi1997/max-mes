package com.bmos.mes.material.feign;

import com.bmos.common.response.ResponseInfo;
import com.bmos.mes.material.vo.MaterialFieldInfoFeignVO;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * 生产管理的物料基础信息
 */
@FeignClient(name = "bmos-mes-service", contextId = "mes-material")
public interface MaterialFeign {

    /**
     * 【生产物料】根据生产物料的物料id查询自定义字段信息
     * @param materialId: 生产物料的物料id
     * @return
     */
    @GetMapping("/api/app/mes/feign/material/field/info")
    ResponseInfo<List<MaterialFieldInfoFeignVO>> getMaterialFieldFeignInfo(@RequestParam("materialId") Long materialId);

}
