package com.bmos.lims2.web.inspect.pack;

import cn.hutool.core.bean.BeanUtil;
import com.bmos.common.response.ResponseInfo;
import com.bmos.lims2.server.inspect.pack.dto.*;
import com.bmos.lims2.server.inspect.pack.service.InspectPackageService;
import com.bmos.lims2.web.inspect.pack.vo.req.InspectPackageItemVO;
import com.bmos.lims2.web.inspect.pack.vo.req.PackageCreateReqVO;
import com.bmos.lims2.web.inspect.pack.vo.req.PackagePageReqVO;
import com.bmos.lims2.web.inspect.pack.vo.req.PackageUpdateReqVO;
import com.bmos.lims2.web.inspect.pack.vo.resp.InspectPackageListRespVO;
import com.bmos.lims2.web.inspect.pack.vo.resp.InspectPackageFullConfigRespVO;
import com.bmos.logging.annotation.OperationLog;
import com.bmos.mybatis.page.CommonPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 实验包
 */
@RestController
@RequestMapping("/inspect/package")
@Api(tags = "实验包-接口")
@Validated
public class InspectPackageController {

    @Autowired
    InspectPackageService packageService;

    @PostMapping("/save")
    @ApiOperation("新增实验包")
    @OperationLog
    public ResponseInfo<List<Long>> savePackage(@RequestBody @Validated PackageCreateReqVO reqVO) {
        return ResponseInfo.success(packageService.savePackage(BeanUtil.copyProperties(reqVO, PackageCreateReqDTO.class)));
    }

    @DeleteMapping("/delete/{id}")
    @ApiOperation("删除实验包")
    @OperationLog
    public ResponseInfo<Void> deletePackage(@PathVariable Long id) {
        packageService.deletePackage(id);
        return ResponseInfo.success();
    }

    @PutMapping("/update")
    @ApiOperation("编辑实验包")
    @OperationLog
    public ResponseInfo<List<Long>> updatePackage(@RequestBody @Validated PackageUpdateReqVO reqVO) {
        return ResponseInfo.success(packageService.updatePackage(BeanUtil.copyProperties(reqVO, PackageUpdateReqDTO.class)));
    }

    @GetMapping("/page")
    @ApiOperation("实验包分页查询")
    public ResponseInfo<CommonPage<InspectPackageDTO>> packagePage(@Validated PackagePageReqVO reqVO) {
        return ResponseInfo.success(packageService.packagePage(BeanUtil.copyProperties(reqVO, PackagePageReqDTO.class)));
    }

    @GetMapping("/page/with-items")
    @ApiOperation("实验包分页查询 - 包含检验项目信息")
    public ResponseInfo<CommonPage<InspectPackageWithItemsDTO>> packagePageWithItems(@Validated PackagePageReqVO reqVO) {
        return ResponseInfo.success(packageService.packagePageWithItems(BeanUtil.copyProperties(reqVO, PackagePageReqDTO.class)));
    }

    @GetMapping("/info/{id}")
    @ApiOperation("实验包详情查询")
    public ResponseInfo<InspectPackageWithItemDTO> packageInfo(@PathVariable Long id) {
        return ResponseInfo.success(packageService.packageInfo(id));
    }

    @GetMapping("/inspect/info/{id}")
    @ApiOperation("根据实验包id查询实验包下的检验项目信息")
    public ResponseInfo<List<InspectPackageItemVO>> packageInspectInfo(@PathVariable Long id) {
        return ResponseInfo.success(BeanUtil.copyToList(packageService.packageInspectInfo(id), InspectPackageItemVO.class));
    }

    @GetMapping("/list")
    @ApiOperation("查询实验包列表 - 用于下拉选择")
    public ResponseInfo<List<InspectPackageListRespVO>> getPackageList() {
        List<InspectPackageListDTO> dtoList = packageService.getList();
        List<InspectPackageListRespVO> voList = BeanUtil.copyToList(dtoList, InspectPackageListRespVO.class);
        return ResponseInfo.success(voList);
    }

    @GetMapping("/full-config/{packageId}")
    @ApiOperation("根据实验包ID查询完整配置信息（包含检项、分析项、数据点）")
    public ResponseInfo<InspectPackageFullConfigRespVO> getPackageFullConfig(@PathVariable Long packageId) {
        InspectPackageFullConfigDTO fullConfigDTO = packageService.getFullConfigByPackageId(packageId);
        InspectPackageFullConfigRespVO respVO = BeanUtil.copyProperties(fullConfigDTO, InspectPackageFullConfigRespVO.class);
        return ResponseInfo.success(respVO);
    }

}
