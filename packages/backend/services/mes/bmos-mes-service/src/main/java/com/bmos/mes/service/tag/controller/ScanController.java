package com.bmos.mes.service.tag.controller;

import com.bmos.common.exception.BmosException;
import com.bmos.common.response.ResponseInfo;
import com.bmos.mes.common.exception.MesResponseCode;
import com.bmos.mes.service.execute.dto.ExecuteEquipmentCodeQueryDTO;
import com.bmos.mes.service.execute.service.ExecuteCommonService;
import com.bmos.mes.service.execute.vo.ExecuteEquipmentVO;
import com.bmos.mes.service.ingredient.input.service.IIngredientInputService;
import com.bmos.mes.service.ingredient.weigh.service.IIngredientWeighService;
import com.bmos.mes.service.ingredient.weigh.vo.IngredientWeighStorageMaterialVO;
import com.bmos.mes.service.preparation.input.service.PreparationInputService;
import com.bmos.mes.service.preparation.measure.dto.LiquidMeasureMaterialPieceQueryDTO;
import com.bmos.mes.service.preparation.measure.service.LiquidPreparationMeasureService;
import com.bmos.mes.service.preparation.measure.vo.LiquidMeasureMaterialPieceVO;
import com.bmos.mes.service.preparation.produce.service.PreparationProduceService;
import com.bmos.mes.service.storage.manage.dto.ScanLhStorageMaterialDTO;
import com.bmos.mes.service.storage.manage.dto.StorageMaterialQueryValidateDTO;
import com.bmos.mes.service.storage.manage.model.StorageMaterial;
import com.bmos.mes.service.storage.manage.model.StorageMaterialBatch;
import com.bmos.mes.service.storage.manage.service.ChargeRecycleService;
import com.bmos.mes.service.storage.manage.service.IStorageMaterialService;
import com.bmos.mes.service.storage.manage.vo.StorageMaterialDetailVO;
import com.bmos.mes.service.storage.manage.vo.StorageMaterialVO;
import com.bmos.mes.service.tag.dto.*;
import com.bmos.mes.service.tag.vo.*;
import com.bmos.mes.service.tareweigh.config.service.ITareWeighConfigService;
import com.bmos.mes.service.tareweigh.config.vo.TareWeighConfigVO;
import com.bmos.mes.service.weigh.centre.input.service.IWeighInputService;
import com.bmos.unit.PrecisionHelper;
import com.bmos.unit.service.UnitCache;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.constraints.NotBlank;
import java.math.BigDecimal;
import java.util.Objects;

/**
 * 标签扫描接口
 *
 * @author liang
 * @version 1.0.0
 * @date 2024/4/25 22:59
 */
@RestController
@RequestMapping("/tag/scan")
@Validated
@Api(tags = "标签扫描接口")
public class ScanController {

    @Resource
    private IIngredientWeighService ingredientWeighService;

    @Resource
    private ChargeRecycleService chargeRecycleService;

    @Resource
    private IIngredientInputService ingredientInputService;

    @Resource
    private LiquidPreparationMeasureService liquidPreparationMeasureService;

    @Resource
    private IStorageMaterialService storageMaterialService;

    @Resource
    private IWeighInputService weighInputService;

    @Resource
    private PreparationInputService preparationInputService;

    @Resource
    private PreparationProduceService preparationProduceService;

    @Resource
    private ITareWeighConfigService tareWeighConfigService;

    @Resource
    private ExecuteCommonService executeCommonService;

    @Resource
    private UnitCache unitCache;

    @PostMapping("/scanWeighMaterialCode")
    @ApiOperation("扫描物料件号/设备编号查询物料件信息")
    public ResponseInfo<IngredientWeighStorageMaterialVO> queryWeighStorageMaterial(@Validated @RequestBody ScanWeighMaterialCodeDTO scanQuery) {
        return ResponseInfo.success(ingredientWeighService.queryWeighStorageMaterial(scanQuery));
    }

    @PostMapping("/scanMaterialOrDeviceCodeWithProductPlanId")
    @ApiOperation("扫描物料件/设备号查询预定到生产计划的已出库物料件信息")
    public ResponseInfo<ScanMaterialVO> queryMaterialOrDeviceCodeWithProductPlanId(@Validated @RequestBody ScanMaterialDeviceCodeDTO scanQuery) {
        return ResponseInfo.success(storageMaterialService.queryWeighStorageMaterial(scanQuery));
    }

    @PostMapping("/scanMaterialOrDeviceCode")
    @ApiOperation("扫描物料件/设备号查询物料件信息")
    public ResponseInfo<ScanMaterialVO> scanMaterialOrDeviceCode(@Validated @RequestBody ScanMaterialDeviceCodeDTO scanQuery) {
        return ResponseInfo.success(storageMaterialService.queryWeighStorageMaterial(scanQuery));
    }

    @PostMapping("/scanDeviceCode")
    @ApiOperation("扫描设备编号查询设备信息")
    public ResponseInfo<ScanDeviceVO> scanDeviceCode(@Validated @RequestBody ScanDeviceCodeDTO scanQuery) {
        return ResponseInfo.success(ingredientWeighService.scanDeviceCode(scanQuery));
    }

    @PostMapping("/scanDeviceCodeAndValidateStationIds")
    @ApiOperation("扫描设备编号查询设备信息")
    public ResponseInfo<ScanDeviceVO> scanDeviceCodeAndValidateStationIds(@Validated @RequestBody ScanDeviceCodeValidateStationDTO scanQuery) {
        return ResponseInfo.success(storageMaterialService.scanDeviceCodeAndValidateStationIds(scanQuery));
    }

    @PostMapping("/scanMaterialOrDevice")
    @ApiOperation("投料回收:扫描物料件或容器获取物料件")
    public ResponseInfo<ScanMaterialOrDeviceVO> scanMaterialOrDevice(@Validated @RequestBody ScanMaterialOrDeviceDTO dto) {
        return ResponseInfo.success(chargeRecycleService.scanMaterialOrDevice(dto));
    }

    @PostMapping("/scanWeighMaterialCodeWithIngredientPlanId")
    @ApiOperation("扫描物料件号查询物料件信息（校验配料单信息）")
    public ResponseInfo<StorageMaterialVO> scanWeighMaterialCodeWithIngredientPlanId(@Validated @RequestBody ScanWeighMaterialCodeWithIngredientPlanId scanQuery) {
        return ResponseInfo.success(ingredientInputService.scanWeighMaterialCodeWithIngredientPlanId(scanQuery));
    }

    @PostMapping("/scanWeighMaterialCodeWithMaterialWeighComponentId")
    @ApiOperation("扫描物料件号查询物料件信息（校验配料单信息）")
    public ResponseInfo<StorageMaterialVO> scanWeighMaterialCodeWithMaterialWeighComponentId(@Validated @RequestBody ScanWeighMaterialCodeWithMaterialWeighComponentId scanQuery) {
        return ResponseInfo.success(weighInputService.scanWeighMaterialCodeWithMaterialWeighComponentId(scanQuery));
    }

    @PostMapping("/scanChargeRecycleDeviceCode")
    @ApiOperation("扫描投料回收组件设备信息")
    public ResponseInfo<ScanDeviceVO> scanChargeRecycleDeviceCode(@Validated @RequestBody ScanChargeRecycleDeviceCodeDTO dto) {
        return ResponseInfo.success(chargeRecycleService.scanChargeRecycleDeviceCode(dto));
    }

    @PostMapping("/scanChargeRecycleContainer")
    @ApiOperation("扫描投料回收容器")
    public ResponseInfo<ScanDeviceVO> scanChargeRecycleContainer(@Validated @RequestBody ScanChargeRecycleDeviceCodeDTO dto){
        return ResponseInfo.success(chargeRecycleService.scanChargeRecycleContainer(dto));
    }


    @GetMapping("/scanWeighContainerCode")
    @ApiOperation("扫描称量组件容器信息(带校验)")
    @ApiImplicitParam(name = "code", value = "容器编号", required = true)
    public ResponseInfo<ScanDeviceVO> scanWeighContainerCode(@Validated @RequestParam @NotBlank String code) {
        return ResponseInfo.success(ingredientWeighService.scanWeighContainerCode(code));
    }

    @GetMapping("/scanWeighPositionCode")
    @ApiOperation("扫描称量组件货位信息(带校验)")
    @ApiImplicitParam(name = "code", value = "货位编号", required = true)
    public ResponseInfo<ScanCargoPositionVO> scanWeighPositionCode(@Validated @RequestParam @NotBlank String code) {
        return ResponseInfo.success(ingredientWeighService.scanWeighPositionCode(code));
    }

    @GetMapping("/scanStorageMaterial")
    @ApiOperation("扫描配液量取物料件信息")
    public ResponseInfo<LiquidMeasureMaterialPieceVO> scanLiquidMeasureMaterialPiece(@Validated LiquidMeasureMaterialPieceQueryDTO dto) {
        return ResponseInfo.success(liquidPreparationMeasureService.scanLiquidMeasureMaterialPiece(dto));
    }

    /**
     * 扫描配液投入确认的物料件信息（附带校验）
     * 校验物料件是否为配液投入列表中“待投入”状态的物料件，若不是，提示请扫描待投入的物料件；
     * 校验物料件已预定到当前生产批次，若未预定，提示请扫描预定的物料件标签；
     * 校验物料件有效，否则提示物料件未生效；
     * 校验物料件所在批次是否超出有效期至，通过状态校验，若超过，提示物料件已超过有效期；
     * 校验物料件已出暂存货位，若物料件仍在暂存货位中，提示物料件未出库；
     * @param dto
     * @return
     */
    @GetMapping("/scanPreparationInputMaterial")
    @ApiOperation("扫描配液投入确认的物料件信息(附带校验)")
    public ResponseInfo<ScanInputMaterialVO> scanPreparationInputMaterial(ScanPreparationInputMaterialDTO dto){
        return ResponseInfo.success(preparationInputService.scanPreparationInputMaterial(dto));
    }

    /**
     * 扫描配液投入确认的设备信息（附带校验）
     * 配液投入组件配置工位，须扫描该工位（通过生产批次的产线过滤）下绑定的设备标签，否则提示不可投入该设备
     * 配液投入组件未配置工位，可扫描工艺绑定的产线的所有工位（通过生产批次的产线过滤）下的设备，否则提示不可投入该设备
     * 设备可用性校验
     * @param dto
     * @return
     */
    @GetMapping("/preparationInputContainerCode")
    @ApiOperation("扫描配液投入确认的设备信息(附带校验)")
    public ResponseInfo<ScanDeviceVO> scanPreparationInputContainer(ScanPreparationInputContainerDTO dto) {
        return ResponseInfo.success(preparationInputService.scanPreparationInputContainer(dto));
    }

    /**
     * 【配液产出】通过设备编码查询设备信息（附带校验）
     * 校验容器是否已有物料件
     * 校验容器是否可用
     * @param code
     * @return
     */
    @GetMapping("/scanPreparationProduceContainer")
    @ApiOperation("【配液产出】通过设备编码查询设备信息（附带校验）")
    public ResponseInfo<ScanDeviceVO> scanPreparationProduceContainer(@RequestParam("code") String code) {
        return ResponseInfo.success(preparationProduceService.scanPreparationProduceContainer(code));
    }

    /**
     * 【配液产出】通过货位编码查询货位信息（附带校验）
     * 校验当前登录用户是否有此货位的数据权限
     * @param code
     * @return
     */
    @GetMapping("/preparationCargoCode")
    @ApiOperation("【配液产出】通过货位编码查询货位信息（附带校验）")
    @ApiImplicitParam(name = "code", value = "货位编号", required = true)
    public ResponseInfo<ScanCargoPositionVO> preparationCargoCode(@Validated @RequestParam @NotBlank String code) {
        return ResponseInfo.success(preparationProduceService.scanPreparationCargoCode(code));
    }

    @PostMapping("/scanTareWeighTag")
    @ApiOperation("【皮重管理】扫描皮重标签获取皮重信息（附带校验）")
    public ResponseInfo<TareWeighConfigVO> scanTareWeighTag(@Validated @RequestBody ScanTareWeighTagDTO dto) {
        return ResponseInfo.success(tareWeighConfigService.scanTareWeighTag(dto));
    }

    @GetMapping("/scanEquipmentCode")
    @ApiOperation("扫描设备code获取设备")
    public ResponseInfo<ExecuteEquipmentVO> scanEquipmentCode(ExecuteEquipmentCodeQueryDTO dto) {
        return ResponseInfo.success(executeCommonService.getEquipmentByCode(dto));
    }

    /**
     * 称量中心添加物料件校验
     * 先后校验批次相符、物料件有效、批次效期、批次质量状态、预定状态（必须未被预定）、出库状态
     * @param dto
     * @return
     */
    @GetMapping("/weighCenterAddMaterial")
    @ApiOperation("[称量中心]扫描物料件/容器")
    public ResponseInfo<ScanMaterialCommonVO> scanStorageMaterialWithCommonValidateAndBatchMatch(StorageMaterialQueryBatchMatchDTO dto) {
        StorageMaterialDetailVO detailVO = storageMaterialService.queryByCodeAndValidate(dto);
        // 批次是否符合校验
        StorageMaterialBatch batch = detailVO.getStorageMaterialBatch();
        if (Objects.nonNull(dto.getStorageMaterialBatchId()) && !Objects.equals(batch.getId(), dto.getStorageMaterialBatchId())) {
            throw new BmosException(MesResponseCode.STORAGE_MATERIAL_NOT_MATCH);
        }
        // 物料件有效性校验
        detailVO.getStorageMaterial().availableValidate();
        // 物料批次校验
        detailVO.getStorageMaterialBatch().availableValidate();
        // 必须为未预定
        if (Objects.nonNull(detailVO.getStorageMaterialReserve())) {
            throw new BmosException(MesResponseCode.WEIGH_CENTRE_SELECT_UNRESERVED_MATERIAL);
        }
        // 出库状态校验
        detailVO.getStorageMaterial().outboundValidate();
        return ResponseInfo.success(this.wrapperStorageMaterialDetailVO(detailVO));
    }

    @GetMapping("/scanStorageMaterialWithCommonValidate")
    @ApiOperation("扫描物料件/容器(公共校验)")
    public ResponseInfo<ScanMaterialCommonVO> scanStorageMaterialWithCommonValidate(StorageMaterialQueryValidateDTO dto) {
        StorageMaterialDetailVO storageMaterialDetailVO = storageMaterialService.queryByCodeAndValidate(dto);
        // 公共校验
        storageMaterialDetailVO.validateAll();
        return ResponseInfo.success(this.wrapperStorageMaterialDetailVO(storageMaterialDetailVO));
    }

    @GetMapping("/lh-storage-material")
    @ApiOperation("扫描物料件/容器(联环称量中心)")
    public ResponseInfo<ScanMaterialCommonVO> scanLHStorageMaterial(ScanLhStorageMaterialDTO dto) {
        StorageMaterialDetailVO storageMaterialDetailVO = storageMaterialService.scanLHStorageMaterial(dto);
        return ResponseInfo.success(this.wrapperStorageMaterialDetailVO(storageMaterialDetailVO));
    }

    private ScanMaterialCommonVO wrapperStorageMaterialDetailVO(StorageMaterialDetailVO detail) {
        ScanMaterialCommonVO result = new ScanMaterialCommonVO();
        StorageMaterial storageMaterial = detail.getStorageMaterial();
        StorageMaterialBatch batch = detail.getStorageMaterialBatch();
        result.setId(storageMaterial.getId());
        result.setMaterialId(batch.getMaterialId());
        result.setMaterialNo(storageMaterial.getNo());
        result.setMaterialBatchNo(batch.getMaterialBatchNo());
        result.setMaterialBatchId(batch.getId());
        result.setUnitId(storageMaterial.getUnitId());
        result.setUnitExtendId(storageMaterial.getUnitExtendId());
        result.setUnit(unitCache.getGlobalUnitName(result.getFinalUnitId()));
        result.setQuantity(PrecisionHelper.formatBigDecimal(unitCache.toExt(storageMaterial.getQuantity(), storageMaterial.getFinalUnitId())));
        result.setAvailableQuantity(PrecisionHelper.formatBigDecimal(unitCache.toExt(storageMaterial.getAvailableQuantity(), storageMaterial.getFinalUnitId())));
        result.setInitQuantity(PrecisionHelper.formatBigDecimal(unitCache.toExt(storageMaterial.getInitQuantity(), storageMaterial.getFinalUnitId())));
        result.setConsumeQuantity(PrecisionHelper.formatBigDecimal(unitCache.toExt(storageMaterial.getConsumeQuantity(), storageMaterial.getFinalUnitId())));
        result.setReserveQuantity(PrecisionHelper.formatBigDecimal(unitCache.toExt(storageMaterial.getReserveQuantity(), storageMaterial.getFinalUnitId())));
        result.setExpiredDate(batch.getExpiredDate());
        result.setOriginalCode(batch.getOriginalBatchNo());
        result.setFactoryBatchNo(batch.getFactoryBatchNo());
        result.setSupplier(batch.getSupplier());
        result.setProducer(batch.getProducer());
        return result;
    }

}
