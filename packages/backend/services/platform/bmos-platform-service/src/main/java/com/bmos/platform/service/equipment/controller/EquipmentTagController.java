package com.bmos.platform.service.equipment.controller;


import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import com.bmos.common.response.ResponseInfo;
import com.bmos.common.tree.TreeUtil;
import com.bmos.platform.service.equipment.controller.vo.EquipmentTagAddVO;
import com.bmos.platform.service.equipment.controller.vo.EquipmentTagTreeNodeVO;
import com.bmos.platform.service.equipment.controller.vo.EquipmentTagUpdateVO;
import com.bmos.platform.service.equipment.service.EquipmentTagService;
import com.bmos.platform.service.equipment.service.dto.EquipmentTagDTO;
import com.bmos.web.validation.InsertValidation;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 设备tag接口
 *
 * @author yigaohui
 * @date 2024/8/12
 **/
@RestController
@RequestMapping("/equipment/tag")
@Validated
@Api(tags = "设备类型接口")
public class EquipmentTagController {

    @Autowired
    private EquipmentTagService equipmentTagService;

    @PostMapping
    @ApiOperation("新增设备类型")
    public ResponseInfo<Void> addTag(@RequestBody @Validated(InsertValidation.class) EquipmentTagAddVO vo) {
        equipmentTagService.add(BeanUtil.copyProperties(vo, EquipmentTagDTO.class));
        return ResponseInfo.success();
    }

    @PutMapping
    @ApiOperation("修改设备类型")
    public ResponseInfo<Void> updateTag(@RequestBody EquipmentTagUpdateVO vo) {
        equipmentTagService.modify(BeanUtil.copyProperties(vo, EquipmentTagDTO.class));
        return ResponseInfo.success();
    }

    @DeleteMapping
    @ApiOperation("删除设备类型")
    public ResponseInfo<Void> deleteTag(@RequestParam Long id) {
        equipmentTagService.delete(id);
        return ResponseInfo.success();
    }


    @GetMapping("/tree")
    @ApiOperation("获取设备类型树")
    public ResponseInfo<List<EquipmentTagTreeNodeVO>> getTree() {
        List<EquipmentTagDTO> list = equipmentTagService.listWithPropertyAndUseTemplate();
        if (CollectionUtil.isEmpty(list)) {
            return ResponseInfo.success();
        }
        return ResponseInfo.success(TreeUtil.buildTree(BeanUtil.copyToList(list, EquipmentTagTreeNodeVO.class), false));
    }
}
