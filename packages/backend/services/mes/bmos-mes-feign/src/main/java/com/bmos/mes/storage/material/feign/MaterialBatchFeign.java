package com.bmos.mes.storage.material.feign;

import com.bmos.common.response.ResponseInfo;
import com.bmos.mes.storage.material.vo.MaterialBatchFieldFeignVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * 物料批次自定义字段
 */
@FeignClient(name = "bmos-mes-service", contextId = "mes-storage-material-batch")
public interface MaterialBatchFeign {

    /**
     * 【物料管理】根据生产物料的物料批次id查询自定义字段信息
     * @param id: 物料批次id
     * @return
     */
    @GetMapping("/api/app/mes/feign/material/batch/field/info")
    ResponseInfo<List<MaterialBatchFieldFeignVO>> getMaterialBatchFieldInfo(@RequestParam("id") Long id);

}
