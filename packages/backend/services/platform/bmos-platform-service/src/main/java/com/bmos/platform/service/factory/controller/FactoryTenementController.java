package com.bmos.platform.service.factory.controller;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import com.bmos.common.response.ResponseInfo;
import com.bmos.common.tree.TreeUtil;
import com.bmos.platform.service.equipment.controller.vo.EquipmentTagAddVO;
import com.bmos.platform.service.equipment.controller.vo.EquipmentTagTreeNodeVO;
import com.bmos.platform.service.equipment.service.dto.EquipmentTagDTO;
import com.bmos.platform.service.factory.controller.vo.TenementAddVO;
import com.bmos.platform.service.factory.controller.vo.TenementModifyVO;
import com.bmos.platform.service.factory.controller.vo.TenementTreeNodeVO;
import com.bmos.platform.service.factory.service.FactoryTenementService;
import com.bmos.platform.service.factory.service.dto.FactoryTenementDTO;
import com.bmos.web.validation.InsertValidation;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 楼宇(BpFactoryTenement)表控制层
 *
 * @author makejava
 * @since 2024-12-30 11:54:53
 */
@RestController
@RequestMapping("/tenement")
@Api(tags = {"楼宇接口"})
public class FactoryTenementController {
    /**
     * 服务对象
     */
    @Resource
    private FactoryTenementService factoryTenementService;

    @PostMapping
    @ApiOperation("新增楼栋")
    public ResponseInfo<Void> add(@RequestBody @Validated TenementAddVO vo) {
        factoryTenementService.add(BeanUtil.copyProperties(vo, FactoryTenementDTO.class));
        return ResponseInfo.success();
    }

    @PutMapping
    @ApiOperation("修改楼栋")
    public ResponseInfo<Void> updateTag(@RequestBody @Validated TenementModifyVO vo) {
        factoryTenementService.update(BeanUtil.copyProperties(vo, FactoryTenementDTO.class));
        return ResponseInfo.success();
    }

    @DeleteMapping
    @ApiOperation("删除楼栋")
    public ResponseInfo<Void> deleteTag(@RequestParam Long id) {
        factoryTenementService.delete(id);
        return ResponseInfo.success();
    }



    @GetMapping("/tree")
    @ApiOperation("获取楼栋树")
    public ResponseInfo<List<TenementTreeNodeVO>> getTree() {
        List<FactoryTenementDTO> list = factoryTenementService.listAll();
        if (CollectionUtil.isEmpty(list)) {
            return ResponseInfo.success();
        }
        List<TenementTreeNodeVO> tenementTreeNodeVOS = BeanUtil.copyToList(list, TenementTreeNodeVO.class);
        return ResponseInfo.success(TreeUtil.buildTree(tenementTreeNodeVOS, false));
    }
}

