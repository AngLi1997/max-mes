package com.bmos.platform.service.equipment.controller;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.BooleanUtil;
import com.bmos.common.response.ResponseInfo;
import com.bmos.mybatis.page.CommonPage;
import com.bmos.platform.service.equipment.controller.vo.*;
import com.bmos.platform.service.equipment.service.AcquisitionPointImportService;
import com.bmos.platform.service.equipment.service.AcquisitionPointService;
import com.bmos.platform.service.equipment.service.dto.AcquisitionPointDTO;
import com.bmos.platform.service.equipment.service.dto.AcquisitionPointPageQueryDTO;
import com.bmos.platform.service.util.UploadFileUtils;
import com.bmos.platform.service.utils.UserUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotEmpty;
import java.util.List;
import java.util.Set;

/**
 * 采集点数据表(AcquisitionPoint)表控制层
 *
 * @author makejava
 * @since 2024-04-19 15:17:52
 */
@RestController
@RequestMapping("acquisitionPoint")
@Validated
@Api(tags = {"采集点接口"})
public class AcquisitionPointController {

    @Resource
    private AcquisitionPointService acquisitionPointService;

    @Resource
    private AcquisitionPointImportService acquisitionPointImportService;

    @PostMapping
    @ApiOperation("添加采集点")
    public ResponseInfo<Void> add(@RequestBody AcquisitionPointAddVO acquisitionPointAddVO) {
        acquisitionPointService.add(BeanUtil.toBean(acquisitionPointAddVO, AcquisitionPointDTO.class));
        return ResponseInfo.success();
    }


    @PutMapping
    @ApiOperation("修改采集点")
    public ResponseInfo<Void> update(@RequestBody AcquisitionPointUpdateVO updateVO) {
        acquisitionPointService.update(BeanUtil.toBean(updateVO, AcquisitionPointDTO.class));
        return ResponseInfo.success();
    }

    @ApiOperation("批量删除采集点")
    @DeleteMapping("/batch")
    public ResponseInfo<Void> delete(@RequestBody @NotEmpty(message = "{acquisitionPoint.id.null}") List<Long> ids) {
        acquisitionPointService.deleteByIds(ids);
        return ResponseInfo.success();
    }

    @GetMapping("/page")
    @ApiOperation("分页获取采集点")
    public ResponseInfo<CommonPage<AcquisitionPointVO>> getList(AcquisitionPointPageQueryVO queryVO) {
        CommonPage<AcquisitionPointDTO> page = acquisitionPointService.page(BeanUtil.toBean(queryVO,
                AcquisitionPointPageQueryDTO.class));
        List<AcquisitionPointVO> acquisitionPointVOS = this.pageMap(page.getList());
        CommonPage<AcquisitionPointVO> commonPage = BeanUtil.toBean(page, CommonPage.class);
        commonPage.setList(acquisitionPointVOS);
        if (!CollectionUtils.isEmpty(commonPage.getList())) {
            commonPage.getList().forEach(item -> {
                item.setCreateByName(UserUtils.getUsername(item.getCreateBy()));
                item.setUpdateByName(UserUtils.getUsername(item.getUpdateBy()));
            });
        }
        return ResponseInfo.success(commonPage);
    }

    private List<AcquisitionPointVO> pageMap(List<AcquisitionPointDTO> page) {
        return BeanUtil.copyToList(page, AcquisitionPointVO.class);
    }

    @PutMapping("/enable")
    @ApiOperation("启用采集点")
    public ResponseInfo<Void> enable(@RequestBody List<Long> ids) {
        acquisitionPointService.enable(ids);
        return ResponseInfo.success();
    }

    @PutMapping("/disable")
    @ApiOperation("停用采集点")
    public ResponseInfo<Void> disable(@RequestBody List<Long> ids) {
        acquisitionPointService.disable(ids);
        return ResponseInfo.success();
    }

    @PostMapping("/equipmentData")
    @ApiOperation("关联设备数据")
    public ResponseInfo<Void> bindEquipmentData(@RequestBody AcquisitionPointEquipmentDataVO acquisitionPointEquipmentDataVO) {
        acquisitionPointService.bindEquipmentData(acquisitionPointEquipmentDataVO.getAcquisitionPointList(),
                acquisitionPointEquipmentDataVO.getEquipmentTagDataCode());
        return ResponseInfo.success();
    }

    @GetMapping("/template")
    @ApiOperation("下载导出模板")
    public void downloadTemplate(HttpServletResponse response) {
        acquisitionPointImportService.getImportTemplate(response);
    }


    @ApiOperation("根据设备数据获取这些数据可用的采集点列表")
    @PostMapping("/enableByEquipmentDataProperty")
    public List<AcquisitionPointVO> getEnableByEquipmentDataProperty(@RequestBody EquipmentDataPropertyQueryVO equipmentDataPropertyQueryVO) {
        return BeanUtil.copyToList(acquisitionPointService.listEnableByEquipmentDataProperty(equipmentDataPropertyQueryVO.getCodeSet(), equipmentDataPropertyQueryVO.getAcquisitionPlatform()), AcquisitionPointVO.class);
    }

    /**
     * 导出采集点
     *
     * @param response http响应
     * @param queryVO  查询条件
     */
    @GetMapping("/export")
    @ApiOperation("导出采集点")
    public void export(HttpServletResponse response, AcquisitionPointExportVO queryVO) {
        AcquisitionPointPageQueryDTO acquisitionPointPageQueryDTO = BeanUtil.toBean(queryVO,
                AcquisitionPointPageQueryDTO.class);
        if (BooleanUtil.isTrue(queryVO.getAll())) {
            acquisitionPointPageQueryDTO.setPageNum(1);
            acquisitionPointPageQueryDTO.setPageSize(10000000);
        }
        CommonPage<AcquisitionPointDTO> pageResponseBo = acquisitionPointService.page(acquisitionPointPageQueryDTO);
        acquisitionPointImportService.export(response, pageResponseBo.getList());
    }


    /**
     * 导入采集点
     *
     * @param file 文件
     */
    @PostMapping("/import")
    @ApiOperation("导入采集点")
    public void importExtAttr(HttpServletResponse response, MultipartFile file) {
        UploadFileUtils.checkExcel(file);
        acquisitionPointImportService.importAcquisitionPoint(response, file);
    }
}

