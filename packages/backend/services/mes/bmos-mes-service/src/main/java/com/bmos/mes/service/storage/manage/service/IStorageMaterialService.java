package com.bmos.mes.service.storage.manage.service;

import com.bmos.mes.common.enums.storage.StorageOperateTypeEnum;
import com.bmos.mes.service.output.weigh.model.OutputWeighRecord;
import com.bmos.mes.service.plan.info.model.Plan;
import com.bmos.mes.service.record.business.model.StorageMaterialDetailInfo;
import com.bmos.mes.service.requisition.dto.AvailableStorageMaterialQueryDTO;
import com.bmos.mes.service.requisition.vo.BatchAvailableMaterialVO;
import com.bmos.mes.service.storage.manage.dto.*;
import com.bmos.mes.service.storage.manage.model.StorageMaterial;
import com.bmos.mes.service.storage.manage.model.StorageMaterialReserve;
import com.bmos.mes.service.storage.manage.vo.*;
import com.bmos.mes.service.tag.dto.ScanDeviceCodeValidateStationDTO;
import com.bmos.mes.service.tag.dto.ScanMaterialDeviceCodeDTO;
import com.bmos.mes.service.tag.enums.CodeType;
import com.bmos.mes.service.tag.vo.ScanDeviceVO;
import com.bmos.mes.service.tag.vo.ScanMaterialVO;
import com.bmos.mybatis.page.CommonPage;

import javax.annotation.Nullable;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;

/**
 * @author liang
 * @version 1.0.0
 * @date 2024/2/19 15:44
 */
public interface IStorageMaterialService {


    /**
     * 物料入库
     *
     * @param dto
     */
    void inbound(StorageMaterialInboundDTO dto);

    /**
     * 物料入库
     *
     * @param dto
     */
    void sendBack(StorageMaterialSendBackDTO dto);

    CommonPage<StorageMaterialVO> queryPage(StorageMaterialPageQuery pageQuery);

    @Nullable
    StorageMaterialVO queryInfoById(Long id);


    void outbound(StorageMaterialOutboundDTO dto);

    void move(StorageMaterialMoveDTO dto);

    void check(StorageMaterialCheckDTO dto);

    List<MaterialPartListVO> queryListByBatchId(Long batchNoId, String storageMaterialNo);

    List<StorageMaterialVO> queryInfoList(StorageMaterialListQuery query);

    /**
     * 根据物料件编号查询物料件详情(移动端)
     *
     * @param materialNo 物料件/设备编号
     * @return 物料件详情
     */
    @Nullable
    StorageMaterialMobileVO queryInfoByMaterialNo(String materialNo, boolean validateAvailable, CodeType codeType);

    /**
     * 暂存物料件批量出库(移动端)
     *
     * @param dto
     */
    void outboundMobile(StorageMaterialOutboundMobileDTO dto);

    /**
     * 暂存物料件批量入库(移动端)
     *
     * @param dto
     */
    void sendBackMobile(StorageMaterialSendBackMobileDTO dto);

    /**
     * 暂存物料件批量移库(移动端)
     *
     * @param dto
     */
    void moveMobile(StorageMaterialMoveMobileDTO dto);

    /**
     * 物料预定
     *
     * @param dto
     */
    void reserve(StorageMaterialReserveDTO dto);

    /**
     * 物料预定
     *
     * @param dtos
     */
    void reserveBatch(List<StorageMaterialReserveDTO> dtos);

    /**
     * 物料取消预定
     *
     * @param dto
     */
    void cancelReserve(StorageMaterialCancelReserveDTO dto);

    /**
     * 拆包出库
     *
     * @param dto
     * @return 新的物料件号
     */
    String splitPackage(StorageMaterialSplitPackageDTO dto);

    List<BatchReservedMaterialVO> getBatchReservedMaterial(BatchReservedMaterialQueryDTO dto);

    List<BatchReservedAvailableMaterialVO> getReservedAvailableStorageMaterial(BatchReservedMaterialQueryDTO dto);

    /**
     * @param dto 批次量领料/物料量领料组件->预定暂存物料
     */
    void reserveBatch(StorageMaterialReserveBatchDTO dto);

    String getSerial();

    List<String> batchGetSerial(int size);

    void confirmSerial(String serial);

    void batchConfirmSerial(List<String> serial);

    @Nullable
    StorageMaterial queryByMaterialNo(String storageMaterialNo, Boolean available);

    void save(StorageMaterial storageMaterial);

    void saveBatch(List<StorageMaterial> storageMaterials);

    /**
     * 根据物料件id批量签字
     *
     * @param storageMaterialIds
     */
    void signBatchByIdList(List<Long> storageMaterialIds);

    List<StorageMaterial> queryListByIds(Collection<Long> consumeStorageMaterialIdList);

    /**
     * 称量/量取消耗
     *
     * @param list   待称量物料件列表
     * @param remark 备注
     * @param plan 生产计划
     * @param operateType  操作类型(称量、量取)
     * @return 消耗总量
     */
    BigDecimal weighConsume(List<StorageMaterial> list, String remark, Plan plan, StorageOperateTypeEnum operateType);

    List<StorageMaterial> inventoryMaterialInbound(InventoryMaterialInboundDTO dto, Plan productPlanId);

    void chargeConsume(List<StorageMaterial> storageMaterialIdList, String operatorId, Long productPlanId);

    StorageMaterial recycleStorageMaterial(RecycleStorageMaterialDTO dto);

    void scrapBatch(List<OutputWeighRecord> list, String weigherId, String reCheckerId, String remark, Long productPlanId);

    @Nullable
    StorageMaterial queryByMaterialNoIgnoreAvailable(String storageMaterialNo);

    @Nullable
    StorageMaterial selectStorageMaterialByContainerId(Long id);

    /**
     * 根据容器编号查询物料件
     * @param no 容器编号
     * @return
     */
    @Nullable
    StorageMaterial queryByContainerNo(String no);

    StorageMaterial getByContainerId(Long id);

    List<StorageMaterialVO> queryInfoByIds(List<Long> ids);

    /**
     * 批生产结束物料件根据生产计划自动取消预定
     * @param productPlanId 生产计划id
     */
    void cancelReserveByProductPlanId(Long productPlanId);

    /**
     * 根据物料件id解绑容器
     * @param ids 物料件id
     */
    void unbindContainersByIds(List<Long> ids);

    List<BatchAvailableMaterialVO> getAvailableStorageMaterial(AvailableStorageMaterialQueryDTO dto);

    void updateBatch(List<StorageMaterial> storageMaterials);

    /**
     * 物料预定组件 预定物料件
     * @param reserveBatchDTO
     */
    List<StorageMaterial> reserveComponentReserve(ReserveComponentReserveMaterialDTO reserveBatchDTO);


    /**
     * 扫码查询物料件
     * @param scanQuery 查询参数
     * @return
     */
    ScanMaterialVO queryWeighStorageMaterial(ScanMaterialDeviceCodeDTO scanQuery);

    /**
     * 整件消耗 并且记录物料日志
     * @param inputMaterialIdList
     * @param inputUserId
     * @param plan
     * @param operateTypeEnum
     */
    void consumeWholeMaterial(List<Long> inputMaterialIdList, String inputUserId, Plan plan,
                              StorageOperateTypeEnum operateTypeEnum);

    List<StorageMaterial> queryListByNos(List<String> storateMaterialNoList);

    /**
     * 移动端退库并消耗
     * @param dto
     */
    void sendBackAndConsumeMobile(StorageMaterialConsumeDTO dto);

    /**
     * 移动端销毁并消耗
     * @param dto
     */
    void destroyAndConsumeMobile(StorageMaterialConsumeDTO dto);

    /**
     * 移动端使用并消耗
     * @param dto
     */
    void useAndConsumeMobile(StorageMaterialConsumeDTO dto);

    /**
     * 物料接收
     * @param dto
     * @return 物料件号列表
     */
    List<String> receiveMobile(StorageMaterialReceiveMobileDTO dto);

    /**
     * 扫描设备校验工位
     * @param scanQuery
     * @return
     */
    ScanDeviceVO scanDeviceCodeAndValidateStationIds(ScanDeviceCodeValidateStationDTO scanQuery);

    /**
     * 校验物料件是否被未被预定
     * 或被当前生产批次或当前生产批次关联批次预定
     *
     * @param storageMaterialId
     * @param productPlanId
     * @return true则被其他批次预定
     */
    boolean validateReserveStatus(Long storageMaterialId, Long productPlanId);

    /**
     * 根据code查询物料件并进行基础校验
     * @param dto
     * @return
     */
    StorageMaterialDetailVO queryByCodeAndValidate(StorageMaterialQueryValidateDTO dto);

    /**
     * 查询物料件
     * @param id
     * @return
     */
    StorageMaterial selectById(Long id);

    /**
     * 扫描物料件/容器(联华称量中心)
     * @param dto
     * @return
     */
    StorageMaterialDetailVO scanLHStorageMaterial(ScanLhStorageMaterialDTO dto);

}
