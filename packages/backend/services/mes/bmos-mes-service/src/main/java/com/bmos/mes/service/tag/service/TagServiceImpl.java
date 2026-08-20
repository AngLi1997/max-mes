package com.bmos.mes.service.tag.service;

import com.bmos.mes.common.utils.TimeUtil;
import com.bmos.mes.service.formula.service.ProductFormulaConfigureService;
import com.bmos.mes.service.formula.vo.ProductFormulaVersionDetailVO;
import com.bmos.mes.service.ingredient.weigh.mapper.IIngredientWeighRecordMapper;
import com.bmos.mes.service.ingredient.weigh.model.IngredientWeighRecord;
import com.bmos.mes.service.output.weigh.mapper.IOutputWeighRecordMapper;
import com.bmos.mes.service.output.weigh.model.OutputWeighRecord;
import com.bmos.mes.service.plan.info.mapper.PlanMapper;
import com.bmos.mes.service.plan.info.model.Plan;
import com.bmos.mes.service.preparation.measure.mapper.LiquidPreparationMeasureRecordMapper;
import com.bmos.mes.service.preparation.measure.model.LiquidPreparationMeasureRecord;
import com.bmos.mes.service.preparation.produce.mapper.PreparationProduceRecordMapper;
import com.bmos.mes.service.preparation.produce.model.PreparationProduceRecord;
import com.bmos.mes.service.product.mapper.ProductMaterialMapper;
import com.bmos.mes.service.product.model.ProductMaterial;
import com.bmos.mes.service.storage.config.mapper.ICargoPositionMapper;
import com.bmos.mes.service.storage.config.mapper.IStorageMapper;
import com.bmos.mes.service.storage.config.model.CargoPosition;
import com.bmos.mes.service.storage.config.model.Storage;
import com.bmos.mes.service.storage.manage.mapper.IStorageMaterialBatchMapper;
import com.bmos.mes.service.storage.manage.model.StorageMaterial;
import com.bmos.mes.service.storage.manage.model.StorageMaterialBatch;
import com.bmos.mes.service.storage.manage.service.IStorageMaterialService;
import com.bmos.mes.service.tag.convert.StorageMaterialTagConvert;
import com.bmos.mes.service.tag.dto.CargoPositionTagQuery;
import com.bmos.mes.service.tag.dto.ScanTareWeighDTO;
import com.bmos.mes.service.tag.dto.StorageMaterialTagQuery;
import com.bmos.mes.service.tag.vo.*;
import com.bmos.mes.service.tareweigh.config.convert.TareWeighConfigConvert;
import com.bmos.mes.service.tareweigh.config.mapper.ITareWeighConfigMapper;
import com.bmos.mes.service.tareweigh.config.model.TareWeighConfig;
import com.bmos.mes.service.utils.UserUtils;
import com.bmos.mes.service.weigh.centre.execute.mapper.IWeighExecuteWeighRecordMapper;
import com.bmos.mes.service.weigh.centre.execute.model.WeighExecuteWeighRecord;
import com.bmos.mes.service.weigh.centre2.execute.mapper.WeighRequirementRecordMapper;
import com.bmos.mes.service.weigh.centre2.execute.model.WeighRequirementRecordDO;
import com.bmos.mes.service.weigh.centre2.requirement.entity.TicketRequirementDO;
import com.bmos.mes.service.weigh.centre2.requirement.entity.TicketRequirementGroupDO;
import com.bmos.mes.service.weigh.centre2.requirement.mapper.ITicketRequirementGroupMapper;
import com.bmos.mes.service.weigh.centre2.requirement.mapper.ITicketRequirementMapper;
import com.bmos.mes.service.weigh.free.entity.FreeWeighHistoryDO;
import com.bmos.mes.service.weigh.free.mapper.IFreeWeighHistoryMapper;
import com.bmos.unit.service.UnitCache;
import org.springframework.stereotype.Service;

import javax.annotation.Nullable;
import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

/**
 * @author liang
 * @version 1.0.0
 * @date 2024/4/25 19:52
 */
@Service
public class TagServiceImpl implements ITagService {

    @Resource
    private UnitCache unitCache;

    @Resource
    private IStorageMaterialService storageMaterialService;

    @Resource
    private ICargoPositionMapper cargoPositionMapper;

    @Resource
    private IStorageMaterialBatchMapper storageMaterialBatchMapper;

    @Resource
    private ProductMaterialMapper productMaterialMapper;

    @Resource
    private IIngredientWeighRecordMapper ingredientWeighRecordMapper;

    @Resource
    private IOutputWeighRecordMapper outputWeighRecordMapper;

    @Resource
    private IWeighExecuteWeighRecordMapper weighExecuteWeighRecordMapper;

    @Resource
    private IFreeWeighHistoryMapper freeWeighHistoryMapper;

    @Resource
    private WeighRequirementRecordMapper weighRequirementRecordMapper;

    @Resource
    private ITicketRequirementGroupMapper requirementGroupMapper;

    @Resource
    private ITicketRequirementMapper requirementMapper;

    @Resource
    private PlanMapper planMapper;

    @Resource
    private IStorageMapper storageMapper;

    @Resource
    private PreparationProduceRecordMapper preparationProduceRecordMapper;

    @Resource
    private ITareWeighConfigMapper tareWeighConfigMapper;

    @Resource
    private LiquidPreparationMeasureRecordMapper measureRecordMapper;

    @Resource
    private ProductFormulaConfigureService productFormulaConfigureService;


    @Override
    @Nullable
    public StorageMaterialTag queryStorageMaterialByStorageMaterialNo(StorageMaterialTagQuery query) {
        StorageMaterial storageMaterial = storageMaterialService.queryByMaterialNo(query.getNo(), false);
        if (storageMaterial == null) {
            return null;
        }
        BaseStorageMaterialTag storageMaterialTag = createBaseStorageMaterialTag(storageMaterial);
        StorageMaterialTag result = StorageMaterialTagConvert.INSTANCE.convert(storageMaterialTag);
        result.setExtBatchNo(storageMaterial.getBatchNo());
        if (storageMaterial.getProductId() != null){
            ProductMaterial product = productMaterialMapper.selectById(storageMaterial.getProductId());
            if (product != null){
                result.setExtProductName(product.getName());
                result.setExtProductMergeCode(product.getMergeCode());
                result.setExtProductFullName(result.getExtProductMergeCode() + "-" + result.getExtProductName());
            }
        }
        IngredientWeighRecord weighRecord = ingredientWeighRecordMapper.queryByStorageMaterialId(storageMaterial.getId());
        OutputWeighRecord outWeighRecord;
        WeighExecuteWeighRecord weighExecuteWeighRecord;
        LiquidPreparationMeasureRecord measureRecord;
        FreeWeighHistoryDO freeWeighRecord;
        WeighRequirementRecordDO weighRequirementRecord;
        if (weighRecord != null) {
            // 配料称量结果
            result.setTareWeightWithUnit(getQuantityWithUnit(weighRecord.getTareWeight(), weighRecord.getUnitId()));
            result.setGrossWeightWithUnit(getQuantityWithUnit(weighRecord.getGrossWeight(), weighRecord.getUnitId()));
            result.setNetWeightWithUnit(getQuantityWithUnit(weighRecord.getNetWeight(), weighRecord.getUnitId()));
            result.setWeigherName(UserUtils.getUsername(weighRecord.getWeigherId()));
            result.setReCheckerName(UserUtils.getUsername(weighRecord.getReCheckerId()));
            result.setWeighTime(weighRecord.getWeighTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        } else if ((outWeighRecord = outputWeighRecordMapper.queryByStorageMaterialId(storageMaterial.getId())) != null){
            // 产出称量结果
            result.setTareWeightWithUnit(getQuantityWithUnit(outWeighRecord.getTareWeight(), outWeighRecord.getUnitId()));
            result.setGrossWeightWithUnit(getQuantityWithUnit(outWeighRecord.getGrossWeight(), outWeighRecord.getUnitId()));
            result.setNetWeightWithUnit(getQuantityWithUnit(outWeighRecord.getNetWeight(), outWeighRecord.getUnitId()));
            result.setWeigherName(UserUtils.getUsername(outWeighRecord.getWeigherId()));
            result.setReCheckerName(UserUtils.getUsername(outWeighRecord.getReCheckerId()));
            result.setWeighTime(outWeighRecord.getWeighTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        } else if ((weighExecuteWeighRecord = weighExecuteWeighRecordMapper.queryByStorageMaterialId(storageMaterial.getId())) != null){
            // 称量中心称量结果
            result.setTareWeightWithUnit(weighExecuteWeighRecord.getTareWeight() + unitCache.getGlobalUnitName(weighExecuteWeighRecord.getUnitId()));
            result.setGrossWeightWithUnit(weighExecuteWeighRecord.getGrossWeight() + unitCache.getGlobalUnitName(weighExecuteWeighRecord.getUnitId()));
            result.setNetWeightWithUnit(weighExecuteWeighRecord.getNetWeight() + unitCache.getGlobalUnitName(weighExecuteWeighRecord.getUnitId()));
            result.setWeigherName(UserUtils.getUsername(weighExecuteWeighRecord.getWeigherId()));
            result.setReCheckerName(UserUtils.getUsername(weighExecuteWeighRecord.getReCheckerId()));
            result.setWeighTime(weighExecuteWeighRecord.getWeighTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        } else if ((measureRecord = measureRecordMapper.selectByStorageMaterialId(storageMaterial.getId())) != null) {
            result.setMeasurerName(UserUtils.getUsername(measureRecord.getMeasurerId()));
            result.setMeasureTime(measureRecord.getMeasureTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            result.setQuantityWithUnit(measureRecord.getQuantity() + unitCache.getGlobalUnitName(measureRecord.getUnitId()));
        } else if ((freeWeighRecord = freeWeighHistoryMapper.selectByStorageMaterialId(storageMaterial.getId())) != null) {
            // 物料称量结果
            result.setTareWeightWithUnit(freeWeighRecord.getTareWeight() + unitCache.getGlobalUnitName(freeWeighRecord.getUnitId()));
            result.setGrossWeightWithUnit(freeWeighRecord.getGrossWeight() + unitCache.getGlobalUnitName(freeWeighRecord.getUnitId()));
            result.setNetWeightWithUnit(freeWeighRecord.getNetWeight() + unitCache.getGlobalUnitName(freeWeighRecord.getUnitId()));
            result.setWeigherName(UserUtils.getUsername(freeWeighRecord.getWeigherId()));
            result.setReCheckerName(UserUtils.getUsername(freeWeighRecord.getReCheckerId()));
            result.setWeighTime(freeWeighRecord.getWeighTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        } else if ((weighRequirementRecord = weighRequirementRecordMapper.queryByStorageMaterialId(storageMaterial.getId())) != null) {
            if (weighRequirementRecord.getWeighTicketRequirementId() != null){
                Long requirementId = weighRequirementRecord.getWeighTicketRequirementId();
                TicketRequirementDO requirementDO = requirementMapper.selectById(requirementId);
                TicketRequirementGroupDO group = requirementGroupMapper.selectById(requirementDO.getRequirementGroupId());
                // 工单称量结果
                result.setTareWeightWithUnit(weighRequirementRecord.getTareWeight() + unitCache.getGlobalUnitName(weighRequirementRecord.getUnitId()));
                result.setGrossWeightWithUnit(weighRequirementRecord.getGrossWeight() + unitCache.getGlobalUnitName(weighRequirementRecord.getUnitId()));
                result.setNetWeightWithUnit(weighRequirementRecord.getNetWeight() + unitCache.getGlobalUnitName(weighRequirementRecord.getUnitId()));
                result.setWeigherName(UserUtils.getUsername(weighRequirementRecord.getWeighUserId()));
                result.setReCheckerName(UserUtils.getUsername(weighRequirementRecord.getSignUser()));
                result.setWeighTime(weighRequirementRecord.getWeighTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
                result.setRequirementBatchNo(group.getBatchNo());

                ProductMaterial material = productMaterialMapper.selectById(group.getMaterialId());
                if (material != null){
                    result.setRequirementProductName(material.getName());
                    result.setRequirementProductMergeCode(material.getCode());
                    result.setRequirementProductFullName(material.getCode() + "-" + material.getName());
                }

                ProductFormulaVersionDetailVO bom = productFormulaConfigureService.getProductFormulaVersionDetail(group.getBomVersionId());
                if (bom != null){
                    result.setRequirementBom(bom.getName());
                }
                result.setRequirementUsage(requirementDO.getRequirementUsage());
            }
        }
        return result;
    }

    @Override
    @Nullable
    public CargoPositionTag queryCargoPositionByPositionNo(CargoPositionTagQuery query) {
        CargoPosition cargoPosition = cargoPositionMapper.selectByCode(query.getNo());
        if (cargoPosition == null){
            return null;
        }
        CargoPositionTag cargoPositionTag = new CargoPositionTag();
        cargoPositionTag.setName(cargoPosition.getPosition());
        cargoPositionTag.setCode(cargoPosition.getCode());
        Storage storage = storageMapper.selectById(cargoPosition.getStorageId());
        if (storage != null){
            cargoPositionTag.setStorageName(storage.getName());
        }
        cargoPositionTag.setPrintDate(LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        return cargoPositionTag;
    }

    @Override
    public PreparationProduceStorageMaterialTag queryPreparationProduceStorageMaterial(StorageMaterialTagQuery query) {
        StorageMaterial storageMaterial = storageMaterialService.queryByMaterialNo(query.getNo(), false);
        if (storageMaterial == null) {
            return null;
        }
        BaseStorageMaterialTag storageMaterialTag = createBaseStorageMaterialTag(storageMaterial);
        PreparationProduceStorageMaterialTag result = StorageMaterialTagConvert.INSTANCE.convert2Produce(storageMaterialTag);
        PreparationProduceRecord produceRecord = preparationProduceRecordMapper.selectByStorageMaterialId(storageMaterial.getId());
        if (Objects.nonNull(produceRecord)){
            result.setProducerName(UserUtils.getUsername(produceRecord.getProducerId()));
            result.setReCheckerName(UserUtils.getUsername(produceRecord.getReCheckerId()));
            result.setProduceTime(produceRecord.getProduceTime().format(DateTimeFormatter.ofPattern(TimeUtil.F_DATETIME)));
        }
        return result;
    }

    @Nullable
    @Override
    public TareWeighTag queryTareWeighByTareWeighId(ScanTareWeighDTO query) {
        TareWeighConfig tareWeighConfig = tareWeighConfigMapper.selectById(query.getId());
        if (tareWeighConfig == null){
            return null;
        }
        return TareWeighConfigConvert.INSTANCE.convertToTag(tareWeighConfig);
    }

    private String getQuantityWithUnit(BigDecimal value, Long unitId) {
        if (value == null || unitId == null) {
            return null;
        }
        return unitCache.toExt(value, unitId).stripTrailingZeros().toPlainString() + unitCache.getGlobalUnitName(unitId);
    }

    private BaseStorageMaterialTag createBaseStorageMaterialTag(StorageMaterial storageMaterial) {
        BaseStorageMaterialTag result = new BaseStorageMaterialTag();
        result.setMaterialNo(storageMaterial.getNo());
        result.setQuantityWithUnit(getQuantityWithUnit(storageMaterial.getQuantity(), storageMaterial.getFinalUnitId()));
        Long productPlanId = storageMaterial.getProductPlanId();
        if (productPlanId != null){
            Plan plan = planMapper.selectById(productPlanId);
            if (plan != null) {
                result.setProductName(plan.getProductName());
                result.setProductMergeCode(plan.getProductMergeCode());
                result.setProductSpecification(plan.getProductSpecification());
                result.setProcessName(plan.getProcessName());
                result.setBatchNo(plan.getBatchNo());
            }
        }
        StorageMaterialBatch batch = storageMaterialBatchMapper.selectById(storageMaterial.getStorageMaterialBatchId());
        if (batch != null) {
            result.setMaterialBatchNo(batch.getMaterialBatchNo());
            result.setExpiredDate(batch.getExpiredDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        }
        ProductMaterial material = productMaterialMapper.selectById(storageMaterial.getMaterialId());
        if (material != null) {
            result.setMaterialName(material.getName());
            result.setMaterialMergeCode(material.getMergeCode());
            result.setFullName(result.getMaterialMergeCode() + "-" + result.getMaterialName());
            result.setMaterialSpecification(material.getSpecification());
        }
        CargoPosition cargoPosition = cargoPositionMapper.selectById(storageMaterial.getMaterialPositionId());
        if (cargoPosition != null) {
            String cargoPositionPath = cargoPositionMapper.getCargoPositionPath(cargoPosition.getId(), "-");
            result.setPositionFullName(cargoPositionPath + "-" + cargoPosition.getPosition());
        }
        return result;
    }
}
