package com.bmos.lims2.web.material;

import cn.hutool.core.bean.BeanUtil;
import com.bmos.common.response.ResponseInfo;
import com.bmos.lims2.server.material.dto.MaterialFieldQueryDTO;
import com.bmos.lims2.server.material.service.MaterialFieldService;
import com.bmos.lims2.web.material.vo.resp.MaterialFieldInfoVO;
import com.bmos.lims2.web.material.vo.resp.MaterialFieldTypeVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 物料自定义字段
 */
@RestController
@RequestMapping("/material/field")
@Api(tags = "【生产物料】物料自定义字段")
public class MaterialFieldController {

    @Autowired
    private MaterialFieldService materialFieldService;


    @GetMapping("/list")
    @ApiOperation("【物料】获取物料的能够配置的自定义字段")
    public ResponseInfo<List<MaterialFieldTypeVO>> getMaterialFieldList(){
        return ResponseInfo.success(BeanUtil.copyToList(materialFieldService.getMaterialFieldList(), MaterialFieldTypeVO.class));
    }

    @GetMapping("/info/{materialId}")
    @ApiOperation("【物料】根据物料的自定义字段信息")
    public ResponseInfo<List<MaterialFieldInfoVO>> getMaterialFieldInfo(@PathVariable("materialId") Long materialId){
        return ResponseInfo.success(BeanUtil.copyToList(materialFieldService.getMaterialFieldInfo(materialId), MaterialFieldInfoVO.class));
    }

    @GetMapping("/info/list")
    @ApiOperation("【物料】获取物料的自定义字段信息")
    public ResponseInfo<List<MaterialFieldInfoVO>> getMaterialFieldInfo(@Validated MaterialFieldQueryDTO dto) {
        return ResponseInfo.success(BeanUtil.copyToList(materialFieldService.getMaterialFieldInfo(dto), MaterialFieldInfoVO.class));
    }
}
