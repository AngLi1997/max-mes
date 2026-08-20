package com.bmos.mes.service.equipment.controller;

import cn.hutool.core.bean.BeanUtil;
import com.bmos.common.response.ResponseInfo;
import com.bmos.logging.annotation.OperationLog;
import com.bmos.mes.service.equipment.service.EquipmentDataAcquisitionPictureService;
import com.bmos.mes.service.equipment.service.ProcedureEquipmentAcquisitionService;
import com.bmos.mes.service.equipment.service.ProcedureEquipmentInfoComponentService;
import com.bmos.mes.service.equipment.service.dto.AcquisitionPictureRangeDTO;
import com.bmos.mes.service.equipment.service.dto.AcquisitionPictureSaveDTO;
import com.bmos.mes.service.equipment.service.dto.EquipmentAcquisitionComponentDTO;
import com.bmos.mes.service.equipment.service.dto.EquipmentInfoComponentDTO;
import com.bmos.mes.service.equipment.vo.*;
import com.bmos.mes.service.execute.dto.ExecuteEquipmentQueryDTO;
import com.bmos.mes.service.execute.service.ExecuteCommonService;
import com.bmos.mes.service.execute.vo.ExecuteEquipmentVO;
import com.bmos.platform.facade.equipment.feign.EquipmentConfigFeign;
import com.bmos.platform.facade.equipment.vo.EquipmentInfoFeignVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 设备相关接口
 *
 * @author liang
 * @version 1.0.0
 * @date 2024/4/12 09:20
 */
@RestController
@RequestMapping("/equipment")
@Validated
@Api(tags = "设备相关接口")
public class EquipmentController {

    @Resource
    private EquipmentConfigFeign equipmentConfigFeign;

    @Resource
    private ProcedureEquipmentInfoComponentService procedureEquipmentInfoComponentService;

    @Resource
    private ProcedureEquipmentAcquisitionService procedureEquipmentAcquisitionService;

    @Resource
    private ExecuteCommonService executeCommonService;

    @Resource
    private EquipmentDataAcquisitionPictureService equipmentDataAcquisitionPictureService;

    @GetMapping("/getConfigByEquipmentId")
    @ApiOperation("根据设备id获取设备配置信息")
    public ResponseInfo<EquipmentInfoFeignVO> getConfigByEquipmentId(@RequestParam Long equipmentId) {
        return equipmentConfigFeign.getConfigByEquipmentId(equipmentId);
    }


    @PostMapping("/equipmentComponentInfo")
    @ApiOperation("保存设备信息组件信息")
    public ResponseInfo<Void> saveEquipmentInfoComponent(@RequestBody EquipmentInfoComponentAddVO equipmentInfoComponentAddVO) {
        procedureEquipmentInfoComponentService.saveEquipmentInfoComponent(BeanUtil.toBean(equipmentInfoComponentAddVO,
                EquipmentInfoComponentDTO.class));
        return ResponseInfo.success();
    }


    @PutMapping("/equipmentComponentInfo")
    @ApiOperation("修改设备信息组件")
    public ResponseInfo<Void> modifyEquipmentInfoComponent(@RequestBody EquipmentInfoComponentModifyVO modifyVO) {
        procedureEquipmentInfoComponentService.modifyEquipmentInfoComponent(BeanUtil.toBean(modifyVO,
                EquipmentInfoComponentDTO.class));
        return ResponseInfo.success();
    }

    @PostMapping("/acquisitionData")
    @ApiOperation("保存设备录入组件数据")
    @OperationLog
    public ResponseInfo<Void> saveEquipmentAcquisitionComponent(@RequestBody EquipmentAcquisitionComponentAddVO acquisitionComponentAddVO) {
        procedureEquipmentAcquisitionService.saveEquipmentAcquisitionComponent(BeanUtil.toBean(acquisitionComponentAddVO,
                EquipmentAcquisitionComponentDTO.class));
        return ResponseInfo.success();
    }

    @PutMapping("/acquisitionData")
    @ApiOperation("修改设备录入组件数据")
    @OperationLog
    public ResponseInfo<Void> modifyEquipmentAcquisitionComponent(@RequestBody EquipmentAcquisitionComponentModifyVO acquisitionComponentModifyVO) {
        procedureEquipmentAcquisitionService.modifyEquipmentAcquisitionComponent(BeanUtil.toBean(acquisitionComponentModifyVO,
                EquipmentAcquisitionComponentDTO.class));
        return ResponseInfo.success();
    }

    @GetMapping("/picture/equipmentList")
    @ApiOperation("设备数采绘图:设备列表")
    public ResponseInfo<List<ExecuteEquipmentVO>> getAcquisitionEquipmentList(ExecuteEquipmentQueryDTO dto) {
        return ResponseInfo.success(executeCommonService.getExecuteComponentEquipmentList(dto));
    }

    @GetMapping("/picture/range")
    @ApiOperation("设备数采绘图:区间计算")
    public ResponseInfo<AcquisitionPictureRangeVO> getAcquisitionPictureRange(AcquisitionPictureRangeDTO dto) {
        return ResponseInfo.success(equipmentDataAcquisitionPictureService.getAcquisitionPictureRange(dto));
    }

    @PostMapping("/picture/save")
    @ApiOperation("设备数采绘图:保存图片")
    public ResponseInfo<Void> saveAcquisitionPicture(@RequestBody AcquisitionPictureSaveDTO dto) {
        equipmentDataAcquisitionPictureService.saveAcquisitionPicture(dto);
        return ResponseInfo.success();
    }



}
