package com.bmos.platform.service.factory.controller;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import com.bmos.common.holder.SysUserHolder;
import com.bmos.common.response.ResponseInfo;
import com.bmos.mybatis.page.BasePage;
import com.bmos.platform.service.factory.controller.vo.*;
import com.bmos.platform.service.factory.service.TenementFloorService;
import com.bmos.platform.service.factory.service.dto.FactoryTenementFloorDTO;
import com.bmos.platform.service.factory.service.dto.FactoryTenementListQueryDTO;
import com.bmos.platform.service.utils.UserUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.ibatis.annotations.Param;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.xml.ws.Response;
import java.util.List;

/**
 * 楼宇楼层表(BpTenementFloor)表控制层
 *
 * @author makejava
 * @since 2024-12-30 14:09:26
 */
@RestController
@RequestMapping("/tenement/floor")
@Api(tags = "楼宇楼层接口")
public class FactoryTenementFloorController {
    /**
     * 服务对象
     */
    @Resource
    private TenementFloorService bpTenementFloorService;


    @ApiOperation(value = "添加楼宇楼层")
    @PostMapping
    public ResponseInfo<Void> add(@RequestBody TenementFloorAddVO tenementFloorAddVO) {
        bpTenementFloorService.add(BeanUtil.copyProperties(tenementFloorAddVO, FactoryTenementFloorDTO.class));
        return ResponseInfo.success();
    }

    // 分页查询
    @GetMapping("/page")
    @ApiOperation(value = "楼宇楼层分页查询")
    public ResponseInfo<List<TenementFloorVO>> queryByPage(TenementFloorPageQueryVO queryVO) {
        List<TenementFloorVO> tenementFloorVOS = BeanUtil.copyToList(bpTenementFloorService.queryByPage(BeanUtil.copyProperties(queryVO, FactoryTenementFloorDTO.class),
                BeanUtil.copyProperties(queryVO, BasePage.class)), TenementFloorVO.class);
        if (CollectionUtil.isNotEmpty(tenementFloorVOS)){
            tenementFloorVOS.forEach(item->item.setCreateByName(UserUtils.getUsername(item.getCreateBy())));
        }
        return ResponseInfo.success(tenementFloorVOS);
    }

    @PutMapping
    @ApiOperation(value = "修改楼宇楼层")
    public ResponseInfo<Void> edit(@RequestBody @Validated TenementFloorModifyVO tenementFloorModifyVO) {
        bpTenementFloorService.update(BeanUtil.copyProperties(tenementFloorModifyVO, FactoryTenementFloorDTO.class));
        return ResponseInfo.success();
    }


    @ApiOperation(value = "删除楼宇楼层")
    @PostMapping("/delete")
    public ResponseInfo<Void> delete(@RequestParam("id") Long id) {
        bpTenementFloorService.deleteById(id);
        return ResponseInfo.success();
    }

    @ApiOperation(value = "启用/禁用楼宇楼层")
    @PutMapping("enable")
    public ResponseInfo<Void> enable(@RequestParam("id") Long id) {
        bpTenementFloorService.enable(id);
        return ResponseInfo.success();
    }


    @ApiOperation("获取楼层列表")
    @PostMapping("/list")
    public ResponseInfo<List<TenementFloorVO>> list(@RequestBody TenementFloorListQueryVO tenementFloorListQueryVO) {
        return ResponseInfo.success(BeanUtil.copyToList(bpTenementFloorService.list(BeanUtil.copyProperties(tenementFloorListQueryVO, FactoryTenementListQueryDTO.class)), TenementFloorVO.class));
    }
}

