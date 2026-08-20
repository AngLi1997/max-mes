package com.bmos.mes.service.weigh.centre2.execute.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.bmos.common.base.enums.CommonEnum;
import com.bmos.common.base.user.SysUser;
import com.bmos.common.exception.BmosException;
import com.bmos.common.holder.SysUserHolder;
import com.bmos.common.response.ResponseInfo;
import com.bmos.common.util.collection.CollectionUtils;
import com.bmos.mes.common.enums.CategoryInfoTypeEnum;
import com.bmos.mes.common.enums.formula.ToleranceTypeEnum;
import com.bmos.mes.common.enums.ingredient.WeighSignStatus;
import com.bmos.mes.common.enums.ingredient.WeighType;
import com.bmos.mes.common.enums.storage.StorageOperateTypeEnum;
import com.bmos.mes.common.enums.weigh.centre.RequirementStatusEnum;
import com.bmos.mes.common.enums.weigh.centre.RequirementWeighStatusEnum;
import com.bmos.mes.common.enums.weigh.centre.TicketRequirementReleaseStatus;
import com.bmos.mes.common.enums.weigh.centre2.SignStatusEnum;
import com.bmos.mes.common.enums.weigh.centre2.TicketStatusEnum;
import com.bmos.mes.common.enums.weigh.centre2.TicketWeighStatusEnum;
import com.bmos.mes.common.enums.weigh.centre2.WeighTypeEnum;
import com.bmos.mes.common.exception.MesResponseCode;
import com.bmos.mes.service.formula.model.ProductFormulaMaterial;
import com.bmos.mes.service.formula.service.ProductFormulaConfigureService;
import com.bmos.mes.service.ingredient.weigh.dto.WeighLogSaveDTO;
import com.bmos.mes.service.ingredient.weigh.service.WeighLogService;
import com.bmos.mes.service.ingredient.weigh.vo.WeighBalanceEquipment;
import com.bmos.mes.service.platform.FeignUtils;
import com.bmos.mes.service.product.model.ProductMaterial;
import com.bmos.mes.service.product.service.ProductMaterialService;
import com.bmos.mes.service.storage.config.model.CargoPosition;
import com.bmos.mes.service.storage.config.service.impl.CargoPositionServiceImpl;
import com.bmos.mes.service.storage.log.dto.StorageMaterialPositionLogDTO;
import com.bmos.mes.service.storage.log.service.IStorageMaterialPositionLogService;
import com.bmos.mes.service.storage.manage.model.StorageMaterial;
import com.bmos.mes.service.storage.manage.model.StorageMaterialBatch;
import com.bmos.mes.service.storage.manage.service.IStorageMaterialBatchService;
import com.bmos.mes.service.storage.manage.service.IStorageMaterialService;
import com.bmos.mes.service.tag.convert.ScanDeviceConvert;
import com.bmos.mes.service.utils.MaterialQuantityCalculateUtil;
import com.bmos.mes.service.weigh.centre.config.service.IWeighCentreService;
import com.bmos.mes.service.weigh.centre.config.vo.WeighCentreDetailVO;
import com.bmos.mes.service.weigh.centre2.execute.controller.vo.*;
import com.bmos.mes.service.weigh.centre2.execute.converter.WeighTicketConverter;
import com.bmos.mes.service.weigh.centre2.execute.mapper.*;
import com.bmos.mes.service.weigh.centre2.execute.model.*;
import com.bmos.mes.service.weigh.centre2.execute.service.TicketWeighExecuteService;
import com.bmos.mes.service.weigh.centre2.execute.service.dto.*;
import com.bmos.mes.service.weigh.centre2.requirement.entity.TicketRequirementDO;
import com.bmos.mes.service.weigh.centre2.requirement.entity.TicketRequirementGroupDO;
import com.bmos.mes.service.weigh.centre2.requirement.mapper.ITicketRequirementGroupMapper;
import com.bmos.mes.service.weigh.centre2.requirement.mapper.ITicketRequirementMapper;
import com.bmos.mes.service.weigh.centre2.ticket.entity.TicketDO;
import com.bmos.mes.service.weigh.centre2.ticket.mapper.ITicketMapper;
import com.bmos.mes.service.weigh.centre2.ticket.service.ITicketService;
import com.bmos.mybatis.page.CommonPage;
import com.bmos.platform.facade.equipment.feign.EquipmentConfigFeign;
import com.bmos.platform.facade.equipment.vo.EquipmentInfoFeignVO;
import com.bmos.platform.facade.factory.feign.FactoryFeign;
import com.bmos.platform.facade.system.user.feign.UserFeign;
import com.bmos.platform.facade.system.user.vo.FeignUserVO;
import com.bmos.unit.service.UnitCache;
import com.bmos.unit.vo.CacheUnit;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class TicketWeighExecuteServiceImpl implements TicketWeighExecuteService {

    private static final Logger log = LoggerFactory.getLogger(TicketWeighExecuteServiceImpl.class);

    @Autowired
    private WeighTicketUserMapper weighTicketUserMapper;
    @Autowired
    private WeighStorageMaterialRequirementMapper weighStorageMaterialRequirementMapper;
    @Autowired
    private WeighRequirementRecordMapper weighRequirementRecordMapper;
    @Autowired
    private UnitCache unitCache;
    @Autowired
    private WeighTicketConverter weighTicketConverter;
    @Autowired
    private ITicketService ticketService;
    @Autowired
    private ITicketRequirementMapper ticketRequirementMapper;
    @Autowired
    private ProductMaterialService productMaterialService;
    @Autowired
    IWeighCentreService weighCentreService;
    @Autowired
    private IStorageMaterialService storageMaterialService;
    @Autowired
    private ITicketMapper ticketMapper;
    @Autowired
    private WeighTicketQualityMapper weighTicketQualityMapper;
    @Autowired
    private WeighRequirementQualityMapper weighRequirementQualityMapper;
    @Autowired
    private ProductFormulaConfigureService productFormulaConfigureService;
    @Autowired
    private ITicketRequirementGroupMapper ticketRequirementGroupMapper;
    @Autowired
    private IStorageMaterialBatchService storageMaterialBatchService;
    @Autowired
    private UserFeign userFeign;
    @Autowired
    private CargoPositionServiceImpl cargoPositionServiceImpl;
    @Autowired
    private IStorageMaterialPositionLogService storageMaterialPositionLogService;
    @Autowired
    private EquipmentConfigFeign equipmentConfigFeign;
    @Autowired
    private WeighLogService weighLogService;
    @Autowired
    private FactoryFeign factoryFeign;

    @Override
    public CommonPage<WeighTicketPageVO> pageWeighTicket(WeighTicketPageDTO dto, Boolean history) {
        // 查询当前人所属工位
        ResponseInfo<List<String>> listResponseInfo = FeignUtils.handleRequest(data -> factoryFeign.getStationIdsByUserId(data), SysUserHolder.getUser().getUserId());
        if (CollUtil.isEmpty(listResponseInfo.getData())) {
            return CommonPage.CommonPage(new ArrayList<>(), 0L, dto);
        }
        List<Long> stationIdList = listResponseInfo.getData().stream().map(Long::parseLong).collect(Collectors.toList());
        // 根据工位id查询称量中心
        List<Long> weighCentreIds = weighCentreService.selectByStationIds(stationIdList);
        if (CollUtil.isEmpty(weighCentreIds)) {
            return CommonPage.CommonPage(new ArrayList<>(), 0L, dto);
        }
        String orderSql = dto.getOrderSql();
        if (StrUtil.isEmpty(orderSql)) {
            orderSql = "send_time" + " desc";
        }
        PageHelper.startPage(dto.getPageNum(), dto.getPageSize(), orderSql);
        List<WeighTicketPageVO> voList = ticketMapper.selectPageByCondition(dto, history, weighCentreIds);
        PageInfo<WeighTicketPageVO> pageInfo = new PageInfo<>(voList);
        // 批量查询所有工单的操作人
        List<Long> ticketIds = voList.stream().map(WeighTicketPageVO::getId).collect(Collectors.toList());
        List<WeighTicketUserDO> userDOList = ticketIds.isEmpty() ? new ArrayList<>() : weighTicketUserMapper.getByTicketIds(ticketIds);
        Map<Long, WeighTicketUserDO> userDOMap = userDOList.stream().collect(Collectors.toMap(WeighTicketUserDO::getWeighTicketId, u -> u));
        for (WeighTicketPageVO vo : voList) {
            WeighTicketUserDO userDO = userDOMap.get(vo.getId());
            vo.setOperator(userDO != null ? userDO.getOperator() : null);
            vo.setUnitName(vo.getUnitId() == null ? null : unitCache.getGlobalUnitName(vo.getUnitId()));
        }
        return CommonPage.CommonPage(voList, pageInfo.getTotal(), dto);
    }

    @Override
    public WeighTicketDetailVO getWeighTicketDetail(Long ticketId) {
        TicketDO ticket = ticketService.getTicketInfo(ticketId);
        if (ticket == null) {
            throw new BmosException(MesResponseCode.WEIGH_TICKET_NOT_EXIST);
        }
        WeighTicketDetailVO vo = weighTicketConverter.toDetailVO(ticket);
        // 查询工单物料件的总量
        vo.setUnitId(ticket.getUnitId());
        vo.setUnitName(ticket.getUnitId() == null ? null : unitCache.getGlobalUnitName(ticket.getUnitId()));
        // 查询中心名称
        WeighCentreDetailVO centreDetailVO = getCentreName(ticket.getWeighCentreId());
        vo.setCentreCode(centreDetailVO != null ? centreDetailVO.getCode() : null);
        vo.setCentreName(centreDetailVO != null ? centreDetailVO.getName() : null);
        vo.setStationIdList(centreDetailVO != null ? centreDetailVO.getStationIds() : new ArrayList<>());
        // 查询物料批次
        StorageMaterialBatch storageMaterialBatch = storageMaterialBatchService.getById(ticket.getStorageMaterialBatchId());
        vo.setMaterialBatchNo(storageMaterialBatch != null ? storageMaterialBatch.getMaterialBatchNo() : null);

        List<TicketRequirementDO> requirements = ticketRequirementMapper.getRequirementsByTicketId(ticketId);
        WeighTicketQualityDO ticketQuality = weighTicketQualityMapper.selectByTicketId(ticketId);
        vo.setWeighedQuantity(ticketQuality != null && ticketQuality.getWeighQuality() != null ? ticketQuality.getWeighQuality() : BigDecimal.ZERO);
        // 查询工单下所有非余料称量的记录
        List<WeighRequirementRecordDO> requirementRecordDOList = weighRequirementRecordMapper.listByTicketIdAndWeighType(ticketId, WeighTypeEnum.NORMAL);
        if (CollUtil.isEmpty(requirementRecordDOList)){
            vo.setWeighRequirementWeighedQuantity(BigDecimal.ZERO);
        } else {
            vo.setWeighRequirementWeighedQuantity(requirementRecordDOList.stream().map(WeighRequirementRecordDO::getNetWeight).reduce(BigDecimal.ZERO, BigDecimal::add));
        }
        vo.setQuality(ticketQuality != null ? ticketQuality.getQuality() : BigDecimal.ZERO);
        BigDecimal notWeighQuantity = vo.getRequirementQuantity().subtract(vo.getWeighRequirementWeighedQuantity());
        vo.setNotWeighedQuantity(notWeighQuantity.compareTo(BigDecimal.ZERO) >= 0 ? notWeighQuantity : BigDecimal.ZERO);
        vo.setOddmentEnough(judgeOddment(requirements));
        // 查询绑定的操作人
        WeighTicketUserDO ticketUser = weighTicketUserMapper.getByTicketId(ticketId);
        if (Objects.nonNull(ticketUser)) {
            vo.setWeighUserId(ticketUser.getOperator());
            vo.setSignUserId(ticketUser.getSignUser());
        }
        List<WeighRequirementVO> reqVOList = convertToWeighRequirementVOList(ticketQuality, requirements, storageMaterialBatch != null ? storageMaterialBatch.getMaterialBatchNo() : null);
        vo.setRequirements(reqVOList);
        // 查询工单已称量量
        return vo;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void executeWeighRequirement(TicketRequirementBindStorageMaterialDTO dto) {
        TicketDO ticketDO = ticketService.getTicketInfo(dto.getTicketId());
        if (ticketDO == null) {
            throw new BmosException(MesResponseCode.WEIGH_TICKET_NOT_EXIST);
        }
        // 新增：校验工单是否绑定操作人
        WeighTicketUserDO ticketUser = weighTicketUserMapper.getByTicketId(dto.getTicketId());
        // 新增：校验当前登录人
        SysUser loginUser;
        try {
            loginUser = SysUserHolder.getUser();
        } catch (Exception e) {
            log.error("executeWeighRequirement failed, cannot get current user");
            throw new BmosException(MesResponseCode.USER_NOT_LOGIN);
        }
        if (loginUser == null || loginUser.getUserId() == null) {
            log.error("executeWeighRequirement failed, user not login");
            throw new BmosException(MesResponseCode.USER_NOT_LOGIN);
        }
        if (!loginUser.getUserId().equals(ticketUser.getOperator())) {
            log.error("executeWeighRequirement failed, loginUser {} not match operator {}", loginUser.getUserId(), ticketUser.getOperator());
            throw new BmosException(MesResponseCode.WEIGH_TICKET_OPERATOR_NOT_MATCH);
        }
        boolean oddment = judgeOddment(ticketDO.getId());
        if (oddment) {
            // 代表为余料称量
            return;
        } else if (Objects.isNull(dto.getRequirementId())) {
            throw new BmosException(MesResponseCode.WEIGH_REQUIREMENT_NOT_EXIST);
        }
        Long requirementId = dto.getRequirementId();
        TicketRequirementDO req = ticketRequirementMapper.selectById(requirementId);
        if (req == null) {
            log.error("executeWeighRequirement failed, requirementId: {} not found", requirementId);
            throw new BmosException(MesResponseCode.WEIGH_REQUIREMENT_NOT_EXIST);
        }

        if (ticketUser.getOperator() == null) {
            log.error("executeWeighRequirement failed, ticketId: {} not bind operator", req.getTicketId());
            throw new BmosException(MesResponseCode.WEIGH_TICKET_NOT_BIND_OPERATOR);
        }
        // 检查工单下是否有其他称量中的需求
        List<TicketRequirementDO> weighingReqs = ticketRequirementMapper.selectWeighingByTicketId(req.getTicketId());
        if (!weighingReqs.isEmpty() && !weighingReqs.get(0).getId().equals(requirementId)) {
            log.error("executeWeighRequirement failed: another requirement {} is weighing for ticket {}",
                    weighingReqs.get(0).getId(), req.getTicketId());
            throw new BmosException(MesResponseCode.WEIGH_REQUIREMENT_ANOTHER_WEIGHING);
        }
        if (req.getRequirementStatus() == RequirementStatusEnum.UN_WEIGHED) {
            req.setRequirementStatus(RequirementStatusEnum.WEIGHING);
        } else if (req.getRequirementStatus() == RequirementStatusEnum.WEIGHED) {
            throw new BmosException(MesResponseCode.WEIGH_REQUIREMENT_ALREADY_WEIGHED);
        }
        ticketRequirementMapper.updateById(req);
        WeighRequirementQualityDO qualityDO = weighRequirementQualityMapper.selectByRequirementId(requirementId);
        if (qualityDO == null) {
            WeighRequirementQualityDO newQuality = new WeighRequirementQualityDO();
            newQuality.setWeighTicketRequirementId(requirementId);
            newQuality.setQuality(BigDecimal.ZERO);
            newQuality.setWeighQuality(BigDecimal.ZERO);
            newQuality.setStorageMaterialCount(0L);
            weighRequirementQualityMapper.insert(newQuality);
        }
        WeighTicketQualityDO quality = weighTicketQualityMapper.selectByTicketId(req.getTicketId());
        if (quality == null) {
            WeighTicketQualityDO newQuality = new WeighTicketQualityDO();
            newQuality.setWeighTicketId(req.getTicketId());
            newQuality.setQuality(BigDecimal.ZERO);
            newQuality.setWeighQuality(BigDecimal.ZERO);
            weighTicketQualityMapper.insert(newQuality);
        }
        // 绑定物料件
        log.info("executeWeighRequirement: set requirementId {} to WEIGHING", requirementId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void bindOperator(TicketBindOperatorDTO dto) {
        TicketDO ticketDO = ticketMapper.selectById(dto.getTicketId());
        if (ticketDO == null) {
            log.error("bindOperator failed, ticketId {} not found", dto.getTicketId());
            throw new BmosException(MesResponseCode.WEIGH_TICKET_NOT_EXIST);
        }
        // 校验当前工单是否已完成
        if (ticketDO.getTicketWeighStatus() == null || TicketWeighStatusEnum.WEIGHED.equals(ticketDO.getTicketWeighStatus())) {
            log.error("bindOperator failed, ticketId {} is executed or not send", dto.getTicketId());
            throw new BmosException(MesResponseCode.WEIGH_TICKET_EXECUTED_OR_NOT_SEND);
        }
        // 操作人和签名人不能相同
        if (dto.getUserId().equals(dto.getSignUser())) {
            log.error("bindOperator failed, userId {} cannot be operator and signUser at the same time", dto.getUserId());
            throw new BmosException(MesResponseCode.WEIGH_TICKET_OPERATOR_SIGN_USER_NOT_EQUAL);
        }
        WeighTicketUserDO exist = weighTicketUserMapper.getByTicketId(dto.getTicketId());
        if (exist == null) {
            ticketDO.setEnoughCompleteCondition(false);
            ticketDO.setTicketWeighStatus(TicketWeighStatusEnum.WEIGHING);
            ticketMapper.updateById(ticketDO);
            weighTicketUserMapper.bindOperator(dto.getTicketId(), dto.getUserId(), dto.getSignUser(), dto.getRemark());
            log.info("bindOperator: bind operator userId {} to ticketId {}", dto.getUserId(), dto.getTicketId());
            return;
        }
        // 校验当前工单是否有未签名的称量记录
        if (weighRequirementRecordMapper.existsNotSign(dto.getTicketId())) {
            log.error("bindOperator failed, ticketId {} has not sign record", dto.getTicketId());
            throw new BmosException(MesResponseCode.WEIGH_TICKET_HAS_NOT_SIGN_RECORD);
        }
        // 校验通过 允许切换操作人和签名人
        weighTicketUserMapper.unbindOperator(dto.getTicketId());
        weighTicketUserMapper.bindOperator(dto.getTicketId(), dto.getUserId(), dto.getSignUser(), dto.getRemark());
        log.info("bindOperator: rebind operator userId {} to ticketId {} by signUser {}", dto.getUserId(), dto.getTicketId(), dto.getSignUser());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public TicketRequirementEnoughVO saveWeighRequirementRecord(WeighRequirementRecordDTO dto) {
        TicketRequirementDO req = ticketRequirementMapper.selectById(dto.getWeighTicketRequirementId());
        if (req == null) {
            log.error("saveWeighRequirementRecord failed, requirementId: {} not found", dto.getWeighTicketRequirementId());
            throw new BmosException(MesResponseCode.WEIGH_REQUIREMENT_NOT_EXIST);
        }
        // 判断device_id是否绑定在其他物料件中
        if (Objects.nonNull(storageMaterialService.queryByContainerNo(dto.getDeviceCode()))) {
            throw new BmosException(MesResponseCode.WEIGH_DEVICE_BIND_OTHER_STORAGE_MATERIAL, dto.getDeviceName());
        }
        TicketDO ticketDO = ticketMapper.selectById(req.getTicketId());
        if (ticketDO == null) {
            log.error("saveWeighRequirementRecord failed, requirementId: {} not found", dto.getWeighTicketRequirementId());
            throw new BmosException(MesResponseCode.WEIGH_TICKET_NOT_EXIST);
        }
        if (Objects.nonNull(ticketDO.getEnoughCompleteCondition()) && ticketDO.getEnoughCompleteCondition()) {
            log.error("saveWeighRequirementRecord failed, requirementId: {} is enough complete condition", dto.getWeighTicketRequirementId());
            throw new BmosException(MesResponseCode.WEIGH_ODDMENT_ALREADY_FINISH);
        }
        if (RequirementStatusEnum.WEIGHED.equals(req.getRequirementStatus())) {
            log.error("saveWeighRequirementRecord failed, requirementId: {} is already WEIGHED", dto.getWeighTicketRequirementId());
            throw new BmosException(MesResponseCode.WEIGH_REQUIREMENT_ALREADY_WEIGHED);
        }
        TicketRequirementEnoughVO enoughVO = new TicketRequirementEnoughVO();
        enoughVO.setTicketCompleteCondition(false);
        doWeighRecord(req, dto, enoughVO);
        // 判断是否进入余料称量
        boolean oddmentEnough = judgeOddment(req.getTicketId());
        enoughVO.setOddmentEnough(oddmentEnough);
        enoughVO.setWeighRequirementRecordVOList(this.getRequirementRecords(dto.getWeighTicketRequirementId()));
        return enoughVO;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public TicketRequirementEnoughVO saveOddmentWeighRecord(WeighRequirementRecordDTO dto) {
        TicketDO ticketDO = ticketMapper.selectById(dto.getTicketId());
        if (ticketDO == null) {
            log.error("saveOddmentWeighRecord failed, requirementId: {} not found", dto.getWeighTicketRequirementId());
            throw new BmosException(MesResponseCode.WEIGH_REQUIREMENT_NOT_EXIST);
        }
        // 判断余料称量是否完成
        if (Objects.nonNull(ticketDO.getEnoughCompleteCondition()) && ticketDO.getEnoughCompleteCondition()) {
            throw new BmosException(MesResponseCode.WEIGH_ODDMENT_ALREADY_FINISH);
        }
        if (!judgeOddment(dto.getTicketId())) {
            throw new BmosException(MesResponseCode.WEIGH_ODDMENT_NOT_ENOUGH);
        }
        WeighRequirementRecordDO record = weighTicketConverter.toRecordDO(dto);
        WeighTicketUserDO ticketUser = weighTicketUserMapper.getByTicketId(dto.getTicketId());
        if (ticketUser == null) {
            throw new BmosException(MesResponseCode.WEIGH_REQUIREMENT_NOT_BIND_OPERATOR);
        }
        record.setWeighUserId(ticketUser.getOperator());
        record.setSignUser(ticketUser.getSignUser());
        record.setTicketId(ticketDO.getId());
        record.setWeighType(WeighTypeEnum.ODDMENT);
        record.setSignStatus(SignStatusEnum.UNSIGNED);
        log.info("saveOddmentWeighRecord: saved oddment record for requirementId {} by dto {}", dto.getWeighTicketRequirementId(), dto);
        // 查询最后一次完成的物料称量需求
        Long ticketId = ticketDO.getId();
        TicketRequirementDO ticketRequirementDO = ticketRequirementMapper.selectLastCompleteRequirementByTicketId(ticketId);
        // 查询需求组
        ProductFormulaMaterial formulaMaterial = productFormulaConfigureService.selectById(ticketRequirementDO.getFormulaMaterialId());
        // 生成新物料件并绑定到批次
        StorageMaterial newMaterialAndBindToBatch = createNewMaterialAndBindToBatch(record, formulaMaterial, ticketDO.getMaterialId(), ticketDO.getStorageMaterialBatchId());
        // 自动更新工单的已称量量
        BigDecimal netWeight = dto.getNetWeight() == null ? BigDecimal.ZERO : dto.getNetWeight();
        // 2. 更新工单已称量量
        WeighTicketQualityDO ticketQuality = weighTicketQualityMapper.selectByTicketId(ticketId);
        if (ticketQuality == null || ticketQuality.getWeighQuality() == null) {
            throw new BmosException(MesResponseCode.WEIGH_TICKET_NOT_EXIST);
        }
        BigDecimal newQ = ticketQuality.getWeighQuality().add(netWeight);
        ticketQuality.setWeighQuality(newQ);
        weighTicketQualityMapper.updateById(ticketQuality);
        // 创建称量记录
        record.setStorageMaterialId(newMaterialAndBindToBatch.getId());
        record.setStorageMaterialNo(newMaterialAndBindToBatch.getNo());
        // 查询物料批次
        StorageMaterialBatch storageMaterialBatch = storageMaterialBatchService.getById(ticketDO.getStorageMaterialBatchId());
        record.setStorageMaterialBatchId(ticketDO.getStorageMaterialBatchId());
        record.setStorageMaterialBatchNo(storageMaterialBatch != null ? storageMaterialBatch.getMaterialBatchNo() : null);
        // 查询当前物料需求组
        TicketRequirementGroupDO ticketRequirementGroupDO = ticketRequirementGroupMapper.selectById(ticketRequirementDO.getRequirementGroupId());
        record.setProductMaterialId(ticketRequirementGroupDO.getMaterialId());
        weighRequirementRecordMapper.insertRecord(record);
        TicketRequirementEnoughVO enoughVO = new TicketRequirementEnoughVO();
        enoughVO.setStorageMaterialNo(newMaterialAndBindToBatch.getNo());
        // 是否满足余料目标量
        this.judgeEnoughOddmentQuality(ticketDO, ticketQuality, enoughVO);
        enoughVO.setOddmentEnough(true);
        enoughVO.setWeighRequirementRecordVOList(this.getOddmentRecords(dto.getTicketId()));
        if (enoughVO.getRequirementEnough()) {
            // 代表余料在目标范围内 则直接完成称量
            ticketDO.setEnoughCompleteCondition(true);
            enoughVO.setTicketCompleteCondition(true);
            ticketMapper.updateById(ticketDO);
        } else if (enoughVO.getOutOddmentTargetEnough()) {
            if (StrUtil.isEmpty(dto.getFinishSignUser())) {
                throw new BmosException(MesResponseCode.WEIGH_ODDMENT_NOT_FINISH_SIGN);
            }
            // 代表余料不在目标范围内
            FinishWeighDTO finishWeighDTO = new FinishWeighDTO(null, dto.getTicketId(), WeighTypeEnum.ODDMENT.getValue(), dto.getFinishSignUser());
            this.finishTicketOddmentSign(finishWeighDTO);
            enoughVO.setTicketCompleteCondition(true);
        } else {
            enoughVO.setTicketCompleteCondition(false);
        }
        // 查询所称量的物料类型
        ProductMaterial productMaterial = productMaterialService.selectById(newMaterialAndBindToBatch.getMaterialId());
        if (Objects.nonNull(productMaterial)) {
            enoughVO.setCategoryInfoType(CategoryInfoTypeEnum.getEnumByValue(productMaterial.getCategoryType()));
        }
        // 记录日志
        this.saveWeighRecordLog(ticketRequirementDO, record, newMaterialAndBindToBatch, storageMaterialBatch, StorageOperateTypeEnum.WEIGH_TICKET_POSITION_ODD_EXECUTE, dto.getEquipmentId());
        return enoughVO;
    }

    private List<WeighRequirementRecordVO> getRequirementRecords(Long requirementId) {
        List<WeighRequirementRecordDO> records = weighRequirementRecordMapper.listByRequirementId(requirementId);
        List<WeighRequirementRecordVO> voList = weighTicketConverter.toRecordVOList(records);
        Set<Long> storageIdList = records.stream().map(WeighRequirementRecordDO::getStorageId).filter(Objects::nonNull).collect(Collectors.toSet());
        List<CargoPosition> cargoPosition = cargoPositionServiceImpl.getByIdList(storageIdList);
        Map<Long, CargoPosition> cargoPositionMap = cargoPosition.stream().collect(Collectors.toMap(CargoPosition::getId, Function.identity()));
        for (int i = 0; i < voList.size(); i++) {
            WeighRequirementRecordVO vo = voList.get(i);
            WeighRequirementRecordDO record = records.get(i);
            vo.setUnitName(record.getUnitId() == null ? null : unitCache.getGlobalUnitName(record.getUnitId()));
            if (record.getStorageId() != null && cargoPositionMap.containsKey(record.getStorageId())) {
                vo.setStorageName(cargoPositionMap.get(record.getStorageId()).getPosition());
                vo.setStorageCode(cargoPositionMap.get(record.getStorageId()).getCode());
            }
        }
        return voList;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void signWeigh(SignWeighDTO dto) {
        TicketDO ticketDO = ticketMapper.selectById(dto.getTicketId());
        if (ticketDO == null) {
            log.error("signWeigh failed, ticketId: {} not found", dto.getTicketId());
            throw new BmosException(MesResponseCode.WEIGH_REQUIREMENT_NOT_EXIST);
        }
        WeighTicketUserDO weighTicketUserDO = weighTicketUserMapper.getByTicketId(dto.getTicketId());
        if (weighTicketUserDO == null) {
            log.error("signWeigh faile ticket_id:{} not found", dto.getTicketId());
            throw new BmosException(MesResponseCode.WEIGH_REQUIREMENT_NOT_BIND_OPERATOR);
        }
        // 判断签名人与绑定的签名不一致
        if (!weighTicketUserDO.getSignUser().equals(dto.getSignUser())) {
            log.error("signWeigh failed, ticketId: {} sign_user:{} not equal", dto.getTicketId(), dto.getSignUser());
            throw new BmosException(MesResponseCode.WEIGH_REQUIREMENT_SIGN_BIND_NOT_EQUAL);
        }
        // 批量签名
        List<WeighRequirementRecordDO> requirementRecordDOList = weighRequirementRecordMapper.selectByTicketIdNotSign(dto.getTicketId());
        List<Long> storageMaterialIdList = requirementRecordDOList.stream().map(WeighRequirementRecordDO::getStorageMaterialId).collect(Collectors.toList());
        storageMaterialService.signBatchByIdList(storageMaterialIdList);
        if (CollUtil.isEmpty(requirementRecordDOList)) {
            throw new BmosException(MesResponseCode.WEIGH_NO_RECORD_NEED_SIGN);
        }
        for (WeighRequirementRecordDO weighRequirementRecordDO : requirementRecordDOList) {
            weighRequirementRecordDO.setSignUser(weighTicketUserDO.getSignUser());
            weighRequirementRecordDO.setSignTime(LocalDateTime.now());
            weighRequirementRecordDO.setSignStatus(SignStatusEnum.SIGNED);
            weighRequirementRecordDO.setSignRemark(dto.getRemark());
        }
        weighRequirementRecordMapper.updateBatch(requirementRecordDOList);
        // 校验是否完成工单
        if (Objects.nonNull(ticketDO.getEnoughCompleteCondition()) && ticketDO.getEnoughCompleteCondition()) {
            ticketDO.setStatus(TicketStatusEnum.EXECUTED);
            ticketDO.setCompleteTime(LocalDateTime.now());
            ticketDO.setTicketWeighStatus(TicketWeighStatusEnum.WEIGHED);
            ticketMapper.updateById(ticketDO);
        }

        // 刷新需求组状态
        refreshGroupStatus(ticketDO.getId());
        log.info("signWeigh: signed all records for ticketId {} by signUser {}", dto.getTicketId(), dto.getSignUser());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void finishWeighRequirement(FinishWeighDTO dto) {
        if (!WeighTypeEnum.ODDMENT.getValue().equals(dto.getWeighType())) {
            Long requirementId = dto.getRequirementId();
            TicketRequirementDO req = ticketRequirementMapper.selectById(requirementId);
            if (req == null) {
                log.error("finishWeighRequirement failed, requirementId: {} not found", requirementId);
                throw new BmosException(MesResponseCode.WEIGH_REQUIREMENT_NOT_EXIST);
            }
            req.setRequirementStatus(RequirementStatusEnum.WEIGHED);
            req.setCompleteTime(LocalDateTime.now());
            ticketRequirementMapper.updateById(req);
            log.info("finishWeighRequirement: set requirementId {} to WEIGHED", requirementId);

            // 更新需求组状态
            this.refreshGroupStatus(dto.getTicketId());
        } else {
            // 完成余料称量
            this.finishTicketOddmentSign(dto);
        }
    }

    @Override
    public List<WeighRequirementVO> listUnWeighedOrWeighingRequirements(Long ticketId) {
        TicketDO ticketDO = ticketMapper.selectById(ticketId);
        if (Objects.isNull(ticketDO)) {
            return new ArrayList<>();
        }
        List<TicketRequirementDO> requirements = ticketRequirementMapper.listUnweighedOrWeighingByTicketId(ticketId);
        StorageMaterialBatch storageMaterialBatch = storageMaterialBatchService.getById(ticketDO.getStorageMaterialBatchId());
        WeighTicketQualityDO ticketQualityDO = weighTicketQualityMapper.selectByTicketId(ticketId);
        return convertToWeighRequirementVOList(ticketQualityDO, requirements, storageMaterialBatch != null ? storageMaterialBatch.getMaterialBatchNo() : null);
    }

    @Override
    public WeighRequirementVO getWeighRequirementDetail(Long requirementId) {
        TicketRequirementDO req = ticketRequirementMapper.selectById(requirementId);
        if (req == null) {
            throw new BmosException(MesResponseCode.WEIGH_REQUIREMENT_NOT_EXIST);
        }
        StorageMaterialBatch storageMaterialBatch = storageMaterialBatchService.getById(req.getStorageMaterialBatchId());
        WeighTicketQualityDO ticketQualityDO = weighTicketQualityMapper.selectByTicketId(req.getTicketId());
        return convertToWeighRequirementVOList(ticketQualityDO, Lists.newArrayList(req),
                storageMaterialBatch != null ? storageMaterialBatch.getMaterialBatchNo() : null).get(0);
    }

    @Override
    public TicketOddmentInfoVO getOddmentInfoByTicketId(Long ticketId) {
        // 1. 查询工单信息
        TicketDO ticketDO = ticketMapper.selectById(ticketId);
        if (ticketDO == null) {
            throw new BmosException(MesResponseCode.WEIGH_TICKET_NOT_EXIST);
        }
        // 2. 查询工单内的称量量
        WeighTicketQualityDO qualityDO = weighTicketQualityMapper.selectByTicketId(ticketId);
        // 3. 查询工单内的所有余料称量记录
        List<WeighRequirementRecordDO> oddmentRecords = weighRequirementRecordMapper.listByTicketIdAndWeighType(ticketId, WeighTypeEnum.ODDMENT);
        BigDecimal oddmentQuality = oddmentRecords.stream().map(WeighRequirementRecordDO::getNetWeight).reduce(BigDecimal.ZERO, BigDecimal::add);
        // 4. 查询物料批号
        StorageMaterialBatch storageMaterialBatch = storageMaterialBatchService.getById(ticketDO.getStorageMaterialBatchId());
        // 5. 查询工单内所有的称量需求内的物料量
        List<WeighRequirementRecordDO> requirementRecordDOList = weighRequirementRecordMapper.listByTicketIdAndWeighType(ticketId, WeighTypeEnum.NORMAL);
        BigDecimal requirementRecordQuality = requirementRecordDOList.stream().map(WeighRequirementRecordDO::getNetWeight).reduce(BigDecimal.ZERO, BigDecimal::add);
        // 查询最后一个称量记录对应的需求
        TicketRequirementDO requirementDO = ticketRequirementMapper.selectLastCompleteRequirementByTicketId(ticketId);
        ProductFormulaMaterial productFormulaMaterial = productFormulaConfigureService.selectById(requirementDO.getFormulaMaterialId());
        // 查询工单绑定的称两人
        WeighTicketUserDO weighTicketUserDO = weighTicketUserMapper.getByTicketId(ticketId);
        if (weighTicketUserDO == null) {
            throw new BmosException(MesResponseCode.WEIGH_REQUIREMENT_NOT_BIND_OPERATOR);
        }
        // 组装数据
        TicketOddmentInfoVO oddmentInfoVO = new TicketOddmentInfoVO();
        oddmentInfoVO.setTicketId(ticketId);
        oddmentInfoVO.setMaterialMergeCode(ticketDO.getMaterialMergeCode());
        oddmentInfoVO.setMaterialName(ticketDO.getMaterialName());
        oddmentInfoVO.setStorageMaterialBatchNo(storageMaterialBatch != null ? storageMaterialBatch.getMaterialBatchNo() : null);
        if (qualityDO != null) {
            oddmentInfoVO.setTicketQuality(qualityDO.getQuality());
            BigDecimal requirementQuality = qualityDO.getQuality().subtract(requirementRecordQuality);
            oddmentInfoVO.setRequirementQuantity(requirementQuality.compareTo(BigDecimal.ZERO) >= 0 ? requirementQuality : BigDecimal.ZERO);
            BigDecimal remainingQuality = qualityDO.getQuality().subtract(qualityDO.getWeighQuality());
            oddmentInfoVO.setRemainingQuality(remainingQuality.compareTo(BigDecimal.ZERO) >= 0 ? remainingQuality : BigDecimal.ZERO);
            oddmentInfoVO.setNotWeighQuality(remainingQuality.compareTo(BigDecimal.ZERO) >= 0 ? remainingQuality : BigDecimal.ZERO);
        } else {
            oddmentInfoVO.setTicketQuality(BigDecimal.ZERO);
            oddmentInfoVO.setRequirementQuantity(BigDecimal.ZERO);
            oddmentInfoVO.setRemainingQuality(BigDecimal.ZERO);
            oddmentInfoVO.setNotWeighQuality(BigDecimal.ZERO);
        }

        oddmentInfoVO.setWeighedQuantity(oddmentQuality);
        oddmentInfoVO.setUnitId(ticketDO.getUnitId());
        oddmentInfoVO.setUnitName(unitCache.getGlobalUnitName(ticketDO.getUnitId()));
        oddmentInfoVO.setChargeMixtureToleranceType(productFormulaMaterial != null ? productFormulaMaterial.getOddmentToleranceType() : null);
        oddmentInfoVO.setChargeMixtureToleranceLower(productFormulaMaterial != null ? productFormulaMaterial.getOddmentToleranceLower() : null);
        oddmentInfoVO.setChargeMixtureToleranceUpper(productFormulaMaterial != null ? productFormulaMaterial.getOddmentToleranceUpper() : null);
        oddmentInfoVO.setWeighUserId(weighTicketUserDO.getOperator());
        if (productFormulaMaterial != null) {
            BigDecimal[] maxAndMin = calMaxAndMin(oddmentInfoVO.getRequirementQuantity(), productFormulaMaterial, true);
            oddmentInfoVO.setChargeUpperQuality(maxAndMin[1]);
            oddmentInfoVO.setChargeLowerQuality(maxAndMin[0]);
            oddmentInfoVO.setNotWeighToleranceLower(maxAndMin[0].subtract(oddmentInfoVO.getWeighedQuantity()));
            oddmentInfoVO.setNotWeighToleranceUpper(oddmentInfoVO.getChargeUpperQuality().subtract(oddmentInfoVO.getWeighedQuantity()));
        }
        return oddmentInfoVO;
    }

    @Override
    public TicketWeighRequirementRecordVO getWeighRecordsByTicketId(Long ticketId) {
        TicketDO ticketDO = ticketMapper.selectById(ticketId);
        if (ticketDO == null) {
            throw new BmosException(MesResponseCode.WEIGH_TICKET_NOT_EXIST);
        }
        //  查询工单操作人、签名人
        WeighTicketUserDO userDO = weighTicketUserMapper.getByTicketId(ticketId);
        if (userDO == null) {
            throw new BmosException(MesResponseCode.WEIGH_REQUIREMENT_NOT_BIND_OPERATOR);
        }
        // 查询称量中心
        WeighCentreDetailVO centreDetailVO = weighCentreService.queryCentreInfo(ticketDO.getWeighCentreId());
        ResponseInfo<Map<String, FeignUserVO>> userMap = userFeign.getByUserIds(Lists.newArrayList(userDO.getOperator(), userDO.getSignUser()));
        TicketWeighRequirementRecordVO ticketWeighRequirementRecordVO = new TicketWeighRequirementRecordVO();
        FeignUserVO operatorUserVO = userMap.getData().get(userDO.getOperator());
        FeignUserVO signUserVO = userMap.getData().get(userDO.getSignUser());
        ticketWeighRequirementRecordVO.setTicketId(ticketId);
        ticketWeighRequirementRecordVO.setTicketNo(ticketDO.getTicketNo());
        ticketWeighRequirementRecordVO.setCompleteTime(ticketDO.getCompleteTime());
        ticketWeighRequirementRecordVO.setSendTime(ticketDO.getSendTime());
        ticketWeighRequirementRecordVO.setCentreCode(centreDetailVO != null ? centreDetailVO.getCode() : null);
        ticketWeighRequirementRecordVO.setCentreName(centreDetailVO != null ? centreDetailVO.getName() : null);
        ticketWeighRequirementRecordVO.setStationIdList(centreDetailVO != null ? centreDetailVO.getStationIds() : new ArrayList<>());
        ticketWeighRequirementRecordVO.setDeptIdList(centreDetailVO != null ? centreDetailVO.getDeptIds() : new ArrayList<>());
        ticketWeighRequirementRecordVO.setWeighUserName(operatorUserVO != null ? operatorUserVO.getUserName() : null);
        ticketWeighRequirementRecordVO.setWeighUserLoginName(operatorUserVO != null ? operatorUserVO.getLoginName() : null);
        ticketWeighRequirementRecordVO.setWeighUserId(operatorUserVO != null ? operatorUserVO.getUserId() : null);
        ticketWeighRequirementRecordVO.setSignUserId(signUserVO != null ? signUserVO.getUserId() : null);
        ticketWeighRequirementRecordVO.setSignUserName(signUserVO != null ? signUserVO.getUserName() : null);
        ticketWeighRequirementRecordVO.setSignUserLoginName(signUserVO != null ? signUserVO.getLoginName() : null);
        List<TicketWeighRecordVO> recordVOList = new ArrayList<>();
        ticketWeighRequirementRecordVO.setRecordVOList(recordVOList);
        List<TicketWeighRecordVO> oddmentRecords = new ArrayList<>();
        ticketWeighRequirementRecordVO.setOddmentRecordVOList(oddmentRecords);
        //  查询称量记录（按ticketId和weighFunc过滤）
        List<WeighRequirementRecordDO> records = weighRequirementRecordMapper.listByTicketId(ticketId);
        if (CollUtil.isEmpty(records)) {
            return ticketWeighRequirementRecordVO;
        }
        Set<String> userIdList = new HashSet<>();
        Set<Long> storageIdList = new HashSet<>();
        Set<Long> productMaterialIdList = Sets.newHashSet(ticketDO.getMaterialId());
        Map<Long, Long> recordIdTORequirementIdMap = new HashMap<>();
        for (WeighRequirementRecordDO record : records) {
            userIdList.add(record.getWeighUserId());
            userIdList.add(record.getSignUser());
            if (record.getStorageId() != null) {
                storageIdList.add(record.getStorageId());
            }
            if (record.getProductMaterialId() != null) {
                productMaterialIdList.add(record.getProductMaterialId());
            }
            if (record.getWeighTicketRequirementId() != null) {
                recordIdTORequirementIdMap.put(record.getId(), record.getWeighTicketRequirementId());
            }
        }
        Map<String, FeignUserVO> userVOMap = FeignUtils.handleRequest(userFeign::getByUserIds, userIdList).getData();
        // 查询暂存货位
        List<CargoPosition> cargoPosition = cargoPositionServiceImpl.getByIdList(storageIdList);
        Map<Long, CargoPosition> cargoPositionMap = cargoPosition.stream().collect(Collectors.toMap(CargoPosition::getId, Function.identity()));
        Map<Long, ProductMaterial> productMaterialMap = new HashMap<>();
        if (CollUtil.isNotEmpty(productMaterialIdList)) {
            List<ProductMaterial> productMaterialList = productMaterialService.getByIds(productMaterialIdList);
            productMaterialMap = productMaterialList.stream().collect(Collectors.toMap(ProductMaterial::getId, Function.identity()));
        }
        // 查询所有对应的需求组
        List<TicketRequirementDO> requirements = new ArrayList<>();
        if (CollUtil.isNotEmpty(recordIdTORequirementIdMap.values())){
            requirements = ticketRequirementMapper.selectBatchIds(new HashSet<>(recordIdTORequirementIdMap.values()));
        }
        Map<Long, Long> requirementIdTORequirementGroupIdMap = requirements.stream()
                .collect(Collectors.toMap(TicketRequirementDO::getId, TicketRequirementDO::getRequirementGroupId));
        List<TicketRequirementGroupDO> requirementGroups = new ArrayList<>();
        if (CollUtil.isNotEmpty(requirementIdTORequirementGroupIdMap.values())){
            requirementGroups = ticketRequirementGroupMapper.selectBatchIds(new HashSet<>(requirementIdTORequirementGroupIdMap.values()));
        }
        Map<Long, String> groupIdTOBatchNoMap = requirementGroups.stream()
                .collect(Collectors.toMap(TicketRequirementGroupDO::getId, TicketRequirementGroupDO::getBatchNo));
        Map<Long, String> recordIdTOBatchNoMap = new HashMap<>();
        recordIdTORequirementIdMap.keySet().forEach(recordId -> {
            Long requirementId = recordIdTORequirementIdMap.get(recordId);
            Long requirementGroupId = requirementIdTORequirementGroupIdMap.get(requirementId);
            recordIdTOBatchNoMap.put(recordId, groupIdTOBatchNoMap.get(requirementGroupId));
        });
        // 查询最后一次完成的需求
        TicketRequirementDO lastCompleteRequirement = ticketRequirementMapper.selectLastCompleteRequirementByTicketId(ticketId);
        ProductMaterial ticketProductMaterial = productMaterialMap.get(ticketDO.getMaterialId());
        for (WeighRequirementRecordDO record : records) {
            TicketWeighRecordVO recordVO = new TicketWeighRecordVO();
            recordVO.setRecordId(record.getId());
            recordVO.setSignStatus(record.getSignStatus());
            recordVO.setMaterialMergeCode(ticketDO.getMaterialMergeCode());
            recordVO.setMaterialName(ticketDO.getMaterialName());
            recordVO.setStorageMaterialBatchNo(record.getStorageMaterialBatchNo());
            recordVO.setStorageMaterialNo(record.getStorageMaterialNo());
            recordVO.setNetWeight(record.getNetWeight());
            recordVO.setTareWeight(record.getTareWeight());
            recordVO.setGrossWeight(record.getGrossWeight());
            recordVO.setUnitName(unitCache.getGlobalUnitName(ticketDO.getUnitId()));
            recordVO.setWeighTime(record.getWeighTime());
            FeignUserVO weighUserVO = userVOMap.get(record.getWeighUserId());
            recordVO.setWeighUserId(weighUserVO != null ? weighUserVO.getUserId() : null);
            recordVO.setWeighUserLoginName(weighUserVO != null ? weighUserVO.getLoginName() : null);
            recordVO.setWeighUserName(weighUserVO != null ? weighUserVO.getUserName() : null);
            FeignUserVO curSignUserVO = userVOMap.get(record.getSignUser());
            recordVO.setSignUserId(curSignUserVO != null ? curSignUserVO.getUserId() : null);
            recordVO.setSignUserLoginName(curSignUserVO != null ? curSignUserVO.getLoginName() : null);
            recordVO.setSignUserName(curSignUserVO != null ? curSignUserVO.getUserName() : null);
            recordVO.setDeviceName(record.getDeviceName());
            recordVO.setDeviceCode(record.getDeviceCode());
            CargoPosition position = cargoPositionMap.get(record.getStorageId());
            recordVO.setStorageId(record.getStorageId());
            recordVO.setStorageCode(position != null ? position.getCode() : null);
            recordVO.setStorageName(position != null ? position.getPosition() : null);
            ProductMaterial productMaterial = productMaterialMap.get(record.getProductMaterialId());
            recordVO.setProductMaterialMergeCode(productMaterial != null ? productMaterial.getMergeCode() : null);
            recordVO.setProductMaterialName(productMaterial != null ? productMaterial.getName() : null);
            recordVO.setCategoryInfoType(ticketProductMaterial != null ? CommonEnum.getEnumByValue(CategoryInfoTypeEnum.class, ticketProductMaterial.getCategoryType()) : null);
            if (WeighTypeEnum.NORMAL.equals(record.getWeighType())) {
                recordVOList.add(recordVO);
                recordVO.setBatchNo(recordIdTOBatchNoMap.get(record.getId()));
            } else {
                oddmentRecords.add(recordVO);
                Long requirementGroupId = requirementIdTORequirementGroupIdMap.get(lastCompleteRequirement.getId());
                recordVO.setBatchNo(groupIdTOBatchNoMap.get(requirementGroupId));
            }
        }
        return ticketWeighRequirementRecordVO;
    }

    private void finishTicketOddmentSign(FinishWeighDTO dto) {
        TicketDO ticketDO = ticketMapper.selectById(dto.getTicketId());
        if (ticketDO == null) {
            throw new BmosException(MesResponseCode.WEIGH_TICKET_NOT_EXIST);
        }
        ticketDO.setEnoughCompleteCondition(true);
        // 判断当前所有称量记录是否签名
        if (!weighRequirementRecordMapper.existsNotSign(dto.getTicketId())) {
            ticketDO.setStatus(TicketStatusEnum.EXECUTED);
            ticketDO.setCompleteTime(LocalDateTime.now());
            ticketDO.setTicketWeighStatus(TicketWeighStatusEnum.WEIGHED);
        }
        ticketMapper.updateById(ticketDO);

        // 刷新需求组状态
        refreshGroupStatus(ticketDO.getId());
    }

    private WeighCentreDetailVO getCentreName(Object centreIdObj) {
        if (centreIdObj == null) return null;
        try {
            Long centreId = Long.valueOf(centreIdObj.toString());
            return weighCentreService.queryCentreInfo(centreId);
        } catch (Exception e) {
            return null;
        }
    }

    private StorageMaterial createNewMaterialAndBindToBatch(WeighRequirementRecordDO record, ProductFormulaMaterial formulaMaterial, Long materialId, Long storageMaterialBatchId) {
        log.info("创建新物料件, materialId:{}, storageMaterialBatchId:{}", materialId, storageMaterialBatchId);
        // 生成新物料件
        StorageMaterial storageMaterial = new StorageMaterial();
        storageMaterial.setContainerId(record.getDeviceId());
        storageMaterial.setContainer(record.getDeviceId() != null ? StrUtil.format("{}-{}", record.getDeviceCode(), record.getDeviceName()) : null);
        storageMaterial.setMaterialPositionId(record.getStorageId());
        storageMaterial.setMaterialId(materialId);
        storageMaterial.setStorageMaterialBatchId(storageMaterialBatchId);
        storageMaterial.setNo(storageMaterialService.getSerial());
        BigDecimal basic = unitCache.toBasic(record.getNetWeight(), record.getUnitId());
        storageMaterial.setInitQuantity(basic);
        storageMaterial.setAvailableQuantity(storageMaterial.getInitQuantity());
        storageMaterial.setConsumeQuantity(BigDecimal.ZERO);
        storageMaterial.setReserveQuantity(BigDecimal.ZERO);
        storageMaterial.setSignStatus(WeighSignStatus.UN_SIGNED);
        Long unitId = record.getUnitId();
        CacheUnit globalUnit = unitCache.getGlobalUnit(record.getUnitId());
        if (globalUnit != null) {
            if (globalUnit.getExtend()) {
                storageMaterial.setUnitId(globalUnit.getParentUnitId());
                storageMaterial.setUnitExtendId(globalUnit.getUnitId());
            } else {
                storageMaterial.setUnitId(unitId);
            }
        }
        storageMaterialService.save(storageMaterial);
        storageMaterialService.confirmSerial(storageMaterial.getNo());
        log.info("创建新物料件完成, materialId:{}, storageMaterialBatchId:{}", materialId, storageMaterialBatchId);
        return storageMaterial;
    }

    /**
     * 批量转换TicketRequirementDO为WeighRequirementVO，并批量填充物料、batchNo、质量、允差等信息
     *
     * @param requirements 需求DO列表
     * @return VO列表
     */
    private List<WeighRequirementVO> convertToWeighRequirementVOList(WeighTicketQualityDO ticketQualityDO, List<TicketRequirementDO> requirements, String storageMaterialBatchNo) {
        if (requirements == null || requirements.isEmpty()) return Collections.emptyList();
        // 批量查物料
        List<Long> materialIds = requirements.stream().map(TicketRequirementDO::getMaterialId).filter(Objects::nonNull).distinct().collect(Collectors.toList());
        List<ProductMaterial> materials = materialIds.isEmpty() ? Collections.emptyList() : productMaterialService.getByIds(materialIds);
        Map<Long, ProductMaterial> materialMap = materials.stream().collect(Collectors.toMap(ProductMaterial::getId, m -> m));
        // 批量查需求组
        List<Long> groupIds = requirements.stream().map(TicketRequirementDO::getRequirementGroupId).filter(Objects::nonNull).distinct().collect(Collectors.toList());
        Map<Long, TicketRequirementGroupDO> groupIdToGroup = new HashMap<>();
        Map<Long, ProductMaterial> groupIdToProductMaterialMap = new HashMap<>();
        if (!groupIds.isEmpty()) {
            List<TicketRequirementGroupDO> groupList = ticketRequirementGroupMapper.selectBatchIds(groupIds);
            groupIdToGroup = groupList.stream().collect(Collectors.toMap(TicketRequirementGroupDO::getId, g -> g));
            // 查询产品物料
            Map<Long, Long> groupIdToProductMaterialIdToMap = groupList.stream().collect(Collectors.toMap(TicketRequirementGroupDO::getId, TicketRequirementGroupDO::getMaterialId));
            List<ProductMaterial> productMaterials = productMaterialService.getByIds(new HashSet<>(groupIdToProductMaterialIdToMap.values()));
            Map<Long, ProductMaterial> productMaterialMap = productMaterials.stream().collect(Collectors.toMap(ProductMaterial::getId, p -> p));
            groupIdToProductMaterialMap = groupList.stream().collect(Collectors.toMap(TicketRequirementGroupDO::getId, g -> productMaterialMap.get(groupIdToProductMaterialIdToMap.get(g.getId()))));
        }
        // 批量查配方物料
        List<Long> formulaMaterialIds = requirements.stream().map(TicketRequirementDO::getFormulaMaterialId).filter(Objects::nonNull).distinct().collect(Collectors.toList());
        Map<Long, ProductFormulaMaterial> formulaMaterialMap = new HashMap<>();
        if (!formulaMaterialIds.isEmpty()) {
            List<ProductFormulaMaterial> formulaMaterials = productFormulaConfigureService.selectByIds(formulaMaterialIds);
            formulaMaterialMap = formulaMaterials.stream().collect(Collectors.toMap(ProductFormulaMaterial::getId, f -> f));
        }
        // 批量查需求质量
        List<Long> requirementIds = requirements.stream().map(TicketRequirementDO::getId).collect(Collectors.toList());
        Map<Long, WeighRequirementQualityDO> qualityMap = new HashMap<>();
        if (!requirementIds.isEmpty()) {
            List<WeighRequirementQualityDO> qualityList = weighRequirementQualityMapper.selectByRequirementIdList(requirementIds);
            qualityMap = qualityList.stream().collect(Collectors.toMap(WeighRequirementQualityDO::getWeighTicketRequirementId, q -> q));
        }
        // 转换
        List<WeighRequirementVO> voList = new ArrayList<>(requirements.size());
        BigDecimal ticketQuality = ticketQualityDO != null ? ticketQualityDO.getQuality() : BigDecimal.ZERO;
        BigDecimal ticketWeighQuality = ticketQualityDO != null ? ticketQualityDO.getWeighQuality() : BigDecimal.ZERO;
        BigDecimal ticketR = ticketQuality.subtract(ticketWeighQuality);
        WeighTicketUserDO ticketUser = weighTicketUserMapper.getByTicketId(requirements.get(0).getTicketId());
        WeighRequirementVO lastFinishRequirementVO = null;
        for (TicketRequirementDO req : requirements) {
            WeighRequirementVO vo = weighTicketConverter.toRequirementVO(req);
            // 物料
            ProductMaterial material = materialMap.get(req.getMaterialId());
            vo.setWeighUserId(ticketUser != null ? ticketUser.getOperator() : null);
            vo.setUnitId(req.getUnitId());
            vo.setUnitName(unitCache.getGlobalUnitName(req.getUnitId()));
            vo.setMaterialMergeCode(material == null ? null : material.getMergeCode());
            vo.setMaterialName(material == null ? null : material.getName());
            vo.setStorageMaterialBatchId(req.getStorageMaterialBatchId());
            vo.setStorageMaterialBatchNo(storageMaterialBatchNo);
            vo.setCompleteTime(req.getCompleteTime());
            vo.setLastFlg(false);
            // batchNo
            if (req.getRequirementGroupId() != null) {
                TicketRequirementGroupDO ticketRequirementGroupDO = groupIdToGroup.get(req.getRequirementGroupId());
                if (Objects.isNull(ticketRequirementGroupDO)) {
                    continue;
                }
                vo.setBatchNo(ticketRequirementGroupDO.getBatchNo());
                vo.setPlanDate(ticketRequirementGroupDO.getPlanDate());
                vo.setRemark(ticketRequirementGroupDO.getRemark());
                ProductMaterial productMaterial = groupIdToProductMaterialMap.get(req.getRequirementGroupId());
                vo.setProductMaterialMergeCode(productMaterial == null ? null : productMaterial.getMergeCode());
                vo.setProductMaterialName(productMaterial == null ? null : productMaterial.getName());
            }
            // 需求质量
            WeighRequirementQualityDO reqQuality = qualityMap.get(req.getId());
            vo.setWeighedQuantity(reqQuality != null && reqQuality.getWeighQuality() != null ? reqQuality.getWeighQuality() : BigDecimal.ZERO);
            vo.setQuality(isABoolean(reqQuality) && reqQuality.getQuality() != null ? reqQuality.getQuality() : BigDecimal.ZERO);
            vo.setStorageMaterialCount(reqQuality != null && reqQuality.getStorageMaterialCount() != null ? reqQuality.getStorageMaterialCount() : 0L);
            BigDecimal notWeightingQuality = req.getFormulaQuantity().subtract(vo.getWeighedQuantity());
            vo.setNotWeighQuality(notWeightingQuality.compareTo(BigDecimal.ZERO) >= 0 ? notWeightingQuality : BigDecimal.ZERO);
            vo.setTicketQuality(ticketQuality);
            vo.setRemainingQuality(ticketR.compareTo(BigDecimal.ZERO) >= 0 ? ticketR : BigDecimal.ZERO);
            // 允差
            ProductFormulaMaterial formulaMaterial = formulaMaterialMap.get(req.getFormulaMaterialId());
            if (formulaMaterial != null) {
                vo.setChargeMixtureToleranceType(formulaMaterial.getChargeMixtureToleranceType());
                vo.setChargeMixtureToleranceUpper(formulaMaterial.getChargeMixtureToleranceUpper());
                vo.setChargeMixtureToleranceLower(formulaMaterial.getChargeMixtureToleranceLower());
                BigDecimal[] minAndMax = calMaxAndMin(vo.getRequirementQuantity(), formulaMaterial, false);
                vo.setChargeUpperQuality(minAndMax[1]);
                vo.setChargeLowerQuality(minAndMax[0]);
                vo.setNotWeighToleranceUpper(minAndMax[1].subtract(vo.getWeighedQuantity()));
                vo.setNotWeighToleranceLower(minAndMax[0].subtract(vo.getWeighedQuantity()));
            }
            voList.add(vo);
            // 根据称量完成时间进行对比
            if (req.getRequirementStatus() == RequirementStatusEnum.WEIGHED) {
                if (lastFinishRequirementVO == null) {
                    lastFinishRequirementVO = vo;
                    continue;
                }
                if (lastFinishRequirementVO.getCompleteTime().isBefore(req.getCompleteTime())) {
                    lastFinishRequirementVO = vo;
                }
            }
        }
        if (lastFinishRequirementVO != null) {
            lastFinishRequirementVO.setLastFlg(true);
        }
        return voList;
    }

    private static boolean isABoolean(WeighRequirementQualityDO reqQuality) {
        return reqQuality != null;
    }

    private void doWeighRecord(TicketRequirementDO req, WeighRequirementRecordDTO dto, TicketRequirementEnoughVO enoughVO) {
        WeighRequirementRecordDO record = weighTicketConverter.toRecordDO(dto);
        WeighTicketUserDO ticketUser = weighTicketUserMapper.getByTicketId(req.getTicketId());
        if (ticketUser == null) {
            throw new BmosException(MesResponseCode.WEIGH_REQUIREMENT_NOT_BIND_OPERATOR);
        }
        record.setWeighUserId(ticketUser.getOperator());
        record.setSignUser(ticketUser.getSignUser());
        record.setWeighTime(LocalDateTime.now());
        record.setSignStatus(SignStatusEnum.UNSIGNED);
        record.setTicketId(req.getTicketId());
        BigDecimal netWeight = dto.getNetWeight() == null ? BigDecimal.ZERO : dto.getNetWeight();
        ProductFormulaMaterial formulaMaterial = productFormulaConfigureService.selectById(req.getFormulaMaterialId());
        // 生成新物料件并绑定到批次
        StorageMaterial newMaterialAndBindToBatch = createNewMaterialAndBindToBatch(record, formulaMaterial, req.getMaterialId(), req.getStorageMaterialBatchId());
        record.setStorageMaterialId(newMaterialAndBindToBatch.getId());
        record.setStorageMaterialNo(newMaterialAndBindToBatch.getNo());
        // 查询物料批次
        StorageMaterialBatch storageMaterialBatch = storageMaterialBatchService.getById(newMaterialAndBindToBatch.getStorageMaterialBatchId());
        record.setStorageMaterialBatchId(newMaterialAndBindToBatch.getStorageMaterialBatchId());
        record.setStorageMaterialBatchNo(storageMaterialBatch != null ? storageMaterialBatch.getMaterialBatchNo() : null);
        // 产品物料id
        TicketRequirementGroupDO ticketRequirementGroupDO = ticketRequirementGroupMapper.selectById(req.getRequirementGroupId());
        record.setProductMaterialId(ticketRequirementGroupDO.getMaterialId());
        weighRequirementRecordMapper.insertRecord(record);
        log.info("saveWeighRequirementRecord: saved record for requirementId {} by dto {}", dto.getWeighTicketRequirementId(), JSON.toJSONString(dto));
        // 自动更新需求和工单的已称量量
        WeighRequirementQualityDO reqQuality = weighRequirementQualityMapper.selectByRequirementId(dto.getWeighTicketRequirementId());
        Long ticketId = req.getTicketId();
        WeighTicketQualityDO ticketQuality = weighTicketQualityMapper.selectByTicketId(ticketId);
        // 1. 更新需求已称量量
        BigDecimal newQ = reqQuality.getWeighQuality() == null ? netWeight : reqQuality.getWeighQuality().add(netWeight);
        reqQuality.setWeighQuality(newQ);
        weighRequirementQualityMapper.updateById(reqQuality);

        // 2. 更新工单已称量量
        BigDecimal newTicketQ = ticketQuality.getWeighQuality() == null ? netWeight : ticketQuality.getWeighQuality().add(netWeight);
        // 校验物料剩余量是否在允差上限范围内
        BigDecimal[] minAndMax = calMaxAndMin(req.getFormulaQuantity(), formulaMaterial, false);
        BigDecimal materialQualityMax = ticketQuality.getQuality().add(minAndMax[1].subtract(req.getFormulaQuantity()));
        if (materialQualityMax.compareTo(newTicketQ) < 0) {
            throw new BmosException(MesResponseCode.WEIGH_STORAGE_MATERIAL_NOT_ENOUGH);
        }
        weighTicketQualityMapper.updateWeighQuality(ticketId, newTicketQ);

        // 3. 判断已称量量是否达到需求量，若达到则变更状态为已称量
        // 根据配方id查询允差范围
        WeighRequirementQualityDO afterReqQuality = weighRequirementQualityMapper.selectByRequirementId(dto.getWeighTicketRequirementId());
        BigDecimal weighed = afterReqQuality != null && afterReqQuality.getWeighQuality() != null ? afterReqQuality.getWeighQuality() : BigDecimal.ZERO;
        BigDecimal requirement = req.getFormulaQuantity() == null ? BigDecimal.ZERO : req.getFormulaQuantity();
        if (judgeEnoughQuality(req.getFormulaMaterialId(), weighed, requirement)) {
            req.setRequirementStatus(RequirementStatusEnum.WEIGHED);
            req.setCompleteTime(LocalDateTime.now());
            req.setComplete_user(SysUserHolder.getUser().getUserId());
            ticketRequirementMapper.updateById(req);
            enoughVO.setRequirementEnough(true);
        } else {
            enoughVO.setRequirementEnough(false);
        }
        enoughVO.setStorageMaterialNo(newMaterialAndBindToBatch.getNo());
        // 查询所称量的物料类型
        ProductMaterial productMaterial = productMaterialService.selectById(newMaterialAndBindToBatch.getMaterialId());
        if (Objects.nonNull(productMaterial)) {
            enoughVO.setCategoryInfoType(CategoryInfoTypeEnum.getEnumByValue(productMaterial.getCategoryType()));
        }
        // 记录日志
        this.saveWeighRecordLog(req, record, newMaterialAndBindToBatch, storageMaterialBatch, StorageOperateTypeEnum.WEIGH_TICKET_POSITION_EXECUTE, dto.getEquipmentId());
    }

    private void judgeEnoughOddmentQuality(TicketDO ticketDO, WeighTicketQualityDO ticketQuality, TicketRequirementEnoughVO enoughVO) {
        TicketRequirementDO requirementDO = ticketRequirementMapper.selectLastCompleteRequirementByTicketId(ticketDO.getId());
        if (requirementDO == null) {
            enoughVO.setRequirementEnough(false);
            enoughVO.setOddmentEnough(true);
            enoughVO.setOutOddmentTargetEnough(true);
            return;
        }
        Long formulaMaterialId = requirementDO.getFormulaMaterialId();
        BigDecimal weighed = ticketQuality.getWeighQuality();
        BigDecimal requirement = ticketQuality.getQuality();
        ProductFormulaMaterial productFormulaMaterial = productFormulaConfigureService.selectById(formulaMaterialId);
        // 获取当前工单下所有需求称量的量
        // 5. 查询工单内所有的称量需求内已称量的物料量
        List<WeighRequirementRecordDO> requirementRecordDOList = weighRequirementRecordMapper.listByTicketIdAndWeighType(ticketDO.getId(), WeighTypeEnum.NORMAL);
        BigDecimal requirementRecordQuality = requirementRecordDOList.stream().map(WeighRequirementRecordDO::getNetWeight).reduce(BigDecimal.ZERO, BigDecimal::add);
        judgeEnoughQuality(productFormulaMaterial, weighed.subtract(requirementRecordQuality), requirement.subtract(requirementRecordQuality), enoughVO);
    }

    private void judgeEnoughQuality(ProductFormulaMaterial productFormulaMaterial, BigDecimal weighed, BigDecimal requirement, TicketRequirementEnoughVO enoughVO) {
        if (productFormulaMaterial == null) {
            enoughVO.setOddmentEnough(false);
            enoughVO.setOutOddmentTargetEnough(false);
            return;
        }
        BigDecimal[] minAndMax = calMaxAndMin(requirement, productFormulaMaterial, true);
        BigDecimal min = minAndMax[0];
        BigDecimal max = minAndMax[1];
        boolean requirementEnough = weighed.compareTo(min) >= 0 && weighed.compareTo(max) <= 0;
        enoughVO.setRequirementEnough(requirementEnough);
        enoughVO.setOddmentEnough(requirementEnough);
        enoughVO.setOutOddmentTargetEnough(weighed.compareTo(max) > 0);
    }

    private BigDecimal[] calMaxAndMin(BigDecimal requirement, ProductFormulaMaterial productFormulaMaterial, Boolean oddment) {
        BigDecimal chargeMixtureToleranceLower;
        BigDecimal chargeMixtureToleranceUpper;
        ToleranceTypeEnum toleranceType;
        if (oddment) {
            chargeMixtureToleranceLower = productFormulaMaterial.getOddmentToleranceLower() != null ? productFormulaMaterial.getOddmentToleranceLower() : BigDecimal.ZERO;
            chargeMixtureToleranceUpper = productFormulaMaterial.getOddmentToleranceUpper() != null ? productFormulaMaterial.getOddmentToleranceUpper() : BigDecimal.ZERO;
            toleranceType = productFormulaMaterial.getOddmentToleranceType();
        } else {
            chargeMixtureToleranceLower = productFormulaMaterial.getChargeMixtureToleranceLower() != null ? productFormulaMaterial.getChargeMixtureToleranceLower() : BigDecimal.ZERO;
            chargeMixtureToleranceUpper = productFormulaMaterial.getChargeMixtureToleranceUpper() != null ? productFormulaMaterial.getChargeMixtureToleranceUpper() : BigDecimal.ZERO;
            toleranceType = productFormulaMaterial.getChargeMixtureToleranceType();
        }
        BigDecimal min = requirement;
        BigDecimal max = requirement;
        if (Objects.nonNull(productFormulaMaterial.getChargeMixtureToleranceType()) && toleranceType == ToleranceTypeEnum.PERCENTAGE) {
            if (chargeMixtureToleranceLower.compareTo(BigDecimal.ZERO) > 0) {
                BigDecimal minDiff = MaterialQuantityCalculateUtil.roundingOff(requirement.multiply(chargeMixtureToleranceLower).divide(BigDecimal.valueOf(100)), productFormulaMaterial.getScale(), productFormulaMaterial.getScaleLength(), RoundingMode.DOWN);
                min = requirement.subtract(minDiff);
            }
            if (chargeMixtureToleranceUpper.compareTo(BigDecimal.ZERO) > 0) {
                BigDecimal maxDiff = MaterialQuantityCalculateUtil.roundingOff(requirement.multiply(chargeMixtureToleranceUpper).divide(BigDecimal.valueOf(100)), productFormulaMaterial.getScale(), productFormulaMaterial.getScaleLength(), RoundingMode.DOWN);
                max = requirement.add(maxDiff);
            }
        } else {
            if (chargeMixtureToleranceLower.compareTo(BigDecimal.ZERO) > 0) {
                BigDecimal minDiff = MaterialQuantityCalculateUtil.roundingOff(chargeMixtureToleranceLower, productFormulaMaterial.getScale(), productFormulaMaterial.getScaleLength(), RoundingMode.DOWN);
                min = requirement.subtract(minDiff);
            }
            if (chargeMixtureToleranceUpper.compareTo(BigDecimal.ZERO) > 0) {
                BigDecimal maxDiff = MaterialQuantityCalculateUtil.roundingOff(chargeMixtureToleranceUpper, productFormulaMaterial.getScale(), productFormulaMaterial.getScaleLength(), RoundingMode.DOWN);
                max = requirement.add(maxDiff);
            }

        }
        return new BigDecimal[]{min.compareTo(BigDecimal.ZERO) > 0 ? min : BigDecimal.ZERO, max.compareTo(BigDecimal.ZERO) > 0 ? max : BigDecimal.ZERO};
    }

    private boolean judgeEnoughQuality(Long formulaMaterialId, BigDecimal weighed, BigDecimal requirement) {
        ProductFormulaMaterial productFormulaMaterial = productFormulaConfigureService.selectById(formulaMaterialId);
        if (productFormulaMaterial == null) {
            return false;
        }
        // 判断允差方式
        BigDecimal[] minAndMax = calMaxAndMin(requirement, productFormulaMaterial, false);
        BigDecimal min = minAndMax[0];
        BigDecimal max = minAndMax[1];
        return weighed.compareTo(min) >= 0 && weighed.compareTo(max) <= 0;
    }

    private boolean judgeOddment(Long ticketId) {
        List<TicketRequirementDO> requirements = ticketRequirementMapper.getRequirementsByTicketId(ticketId);
        for (TicketRequirementDO requirement : requirements) {
            if (!(requirement.getRequirementStatus() == RequirementStatusEnum.WEIGHED || requirement.getRequirementStatus() == RequirementStatusEnum.EXPIRED)) {
                return false;
            }
        }
        return true;
    }

    private boolean judgeOddment(List<TicketRequirementDO> requirements) {
        if (CollUtil.isEmpty(requirements)) {
            return true;
        }
        for (TicketRequirementDO requirement : requirements) {
            if (!(requirement.getRequirementStatus() == RequirementStatusEnum.WEIGHED || requirement.getRequirementStatus() == RequirementStatusEnum.EXPIRED)) {
                return false;
            }
        }
        return true;
    }

    private List<WeighRequirementRecordVO> getOddmentRecords(Long ticketId) {
        List<WeighRequirementRecordDO> records = weighRequirementRecordMapper.listOddmentByTicketId(ticketId);
        List<WeighRequirementRecordVO> voList = weighTicketConverter.toRecordVOList(records);
        Set<Long> storageIdList = records.stream().map(WeighRequirementRecordDO::getStorageId).filter(Objects::nonNull).collect(Collectors.toSet());
        List<CargoPosition> cargoPosition = cargoPositionServiceImpl.getByIdList(storageIdList);
        Map<Long, CargoPosition> cargoPositionMap = cargoPosition.stream().collect(Collectors.toMap(CargoPosition::getId, Function.identity()));
        for (int i = 0; i < voList.size(); i++) {
            WeighRequirementRecordVO vo = voList.get(i);
            WeighRequirementRecordDO record = records.get(i);
            vo.setUnitName(record.getUnitId() == null ? null : unitCache.getGlobalUnitName(record.getUnitId()));
            if (record.getStorageId() != null && cargoPositionMap.containsKey(record.getStorageId())) {
                vo.setStorageName(cargoPositionMap.get(record.getStorageId()).getPosition());
                vo.setStorageCode(cargoPositionMap.get(record.getStorageId()).getCode());
            }
        }
        return voList;
    }

    @Override
    public void bindMaterialToRequirement(Long requirementId, List<Long> storageMaterialIds) {
        TicketRequirementDO req = ticketRequirementMapper.selectById(requirementId);
        if (req == null) {
            log.error("bindMaterialToRequirement failed, requirementId: {} not found", requirementId);
            throw new BmosException(MesResponseCode.WEIGH_REQUIREMENT_NOT_EXIST);
        }
        if (storageMaterialIds == null || storageMaterialIds.isEmpty()) {
            // 判断当前工单是否添加过物料件
            WeighTicketQualityDO qualityDO = weighTicketQualityMapper.selectByTicketId(req.getTicketId());
            if (qualityDO.getQuality().compareTo(BigDecimal.ZERO) <= 0) {
                log.error("bindMaterialToRequirement failed, ticketId: {} not bind material", req.getTicketId());
                throw new BmosException(MesResponseCode.WEIGH_STORAGE_MATERIAL_NOT_APPEND);
            }
            return;
        }
        // 校验物料批次
        StorageMaterialBatch storageMaterialBatch = storageMaterialBatchService.getById(req.getStorageMaterialBatchId());
        if (storageMaterialBatch == null) {
            log.error("bindMaterialToRequirement failed, storageMaterialBatchId: {} not found", req.getStorageMaterialBatchId());
            throw new BmosException(MesResponseCode.STORAGE_MATERIAL_BATCH_NOT_EXIST);
        }
        List<WeighStorageMaterialRequirementDO> binds = new ArrayList<>();
        BigDecimal totalConsume = BigDecimal.ZERO;
        List<StorageMaterial> storageMaterials = storageMaterialService.queryListByIds(storageMaterialIds);
        this.saveStorageMaterialLogs(storageMaterials, req);
        for (StorageMaterial storageMaterial : storageMaterials) {
            // 校验当前物料件是否属于当前物料批次
            if (!storageMaterial.getStorageMaterialBatchId().equals(req.getStorageMaterialBatchId())) {
                throw new BmosException(MesResponseCode.WEIGH_STORAGE_MATERIAL_NOT_BELONG_BATCH);
            }
            storageMaterial.availableValidate();
            storageMaterialBatch.availableValidate();
            // 是否预定
            if (storageMaterial.isReserved()) {
                throw new BmosException(MesResponseCode.WEIGH_STORAGE_MATERIAL_RESERVED);
            }
            storageMaterial.outboundValidate();
            BigDecimal basic = unitCache.toBasic(storageMaterial.getAvailableQuantity(), storageMaterial.getUnitId());
            BigDecimal consume = unitCache.toExt(basic, req.getUnitId());
            // 记录消耗量
            totalConsume = totalConsume.add(consume);
            WeighStorageMaterialRequirementDO bind = new WeighStorageMaterialRequirementDO();
            bind.setStorageMaterialId(storageMaterial.getId());
            bind.setConsumeQuantity(consume);
            binds.add(bind);
            // 消耗物料件
            storageMaterial.consumeAllQuantity();
        }
        // 查询配方物料
        ProductFormulaMaterial productFormulaMaterial = productFormulaConfigureService.selectById(req.getFormulaMaterialId());
        // 对消耗量做精度换算
        totalConsume = MaterialQuantityCalculateUtil.roundingOff(totalConsume, productFormulaMaterial);
        storageMaterialService.updateBatch(storageMaterials);
        // 解绑物料件与容器的绑定关系
        storageMaterialService.unbindContainersByIds(storageMaterialIds);
        weighStorageMaterialRequirementMapper.insertBatchBind(requirementId, binds);

        // 更新WeighRequirementQualityDO
        WeighRequirementQualityDO qualityDO = weighRequirementQualityMapper.selectByRequirementId(requirementId);
        long addCount = storageMaterialIds.size();
        BigDecimal oldQuality = qualityDO.getQuality() == null ? BigDecimal.ZERO : qualityDO.getQuality();
        long oldCount = qualityDO.getStorageMaterialCount() == null ? 0L : qualityDO.getStorageMaterialCount();
        qualityDO.setQuality(oldQuality.add(totalConsume));
        qualityDO.setStorageMaterialCount(oldCount + addCount);
        weighRequirementQualityMapper.updateById(qualityDO);
        // 更新WeighTicketQualityDO
        WeighTicketQualityDO quality = weighTicketQualityMapper.selectByTicketId(req.getTicketId());

        BigDecimal oldTicketQuality = quality.getQuality() == null ? BigDecimal.ZERO : quality.getQuality();
        quality.setQuality(oldTicketQuality.add(totalConsume));
        weighTicketQualityMapper.updateById(quality);

        log.info("bindMaterialToRequirement: batch bind storageMaterialIds {} to requirementId {}, totalConsume {}", storageMaterialIds, requirementId, totalConsume);
    }

    private void saveStorageMaterialLogs(List<StorageMaterial> storageMaterials, TicketRequirementDO req) {
        TicketRequirementGroupDO ticketRequirementGroupDO = ticketRequirementGroupMapper.selectById(req.getRequirementGroupId());
        ProductMaterial productMaterial = null;
        if (Objects.nonNull(ticketRequirementGroupDO)) {
            productMaterial = productMaterialService.selectById(ticketRequirementGroupDO.getMaterialId());
        }
        List<StorageMaterialPositionLogDTO> logDTOS = new ArrayList<>();
        for (StorageMaterial storageMaterial : storageMaterials) {
            StorageMaterialPositionLogDTO logDTO = new StorageMaterialPositionLogDTO();
            logDTO.setStorageMaterialId(storageMaterial.getId());
            logDTO.setOperateType(StorageOperateTypeEnum.MATERIAL_WEIGH_CONSUME);
            logDTO.setQuantity(storageMaterial.getAvailableQuantity());
            logDTO.setUnitId(storageMaterial.getFinalUnitId());
            logDTO.setSenderId(SysUserHolder.getUser().getUserId());
            logDTO.setReceiverId(SysUserHolder.getUser().getUserId());
            logDTO.setProductId(productMaterial != null ? productMaterial.getId() : null);
            logDTO.setProductName(productMaterial != null ? productMaterial.getName() : null);
            logDTO.setProductCode(productMaterial != null ? productMaterial.getMergeCode() : null);
            logDTO.setProductBatchNo(ticketRequirementGroupDO != null ? ticketRequirementGroupDO.getBatchNo() : null);
            logDTO.setMaterialPositionId(storageMaterial.getMaterialPositionId());
            logDTOS.add(logDTO);
        }
        storageMaterialPositionLogService.saveLogs(logDTOS);
    }


    private void saveWeighRecordLog(TicketRequirementDO req, WeighRequirementRecordDO record,
                                    StorageMaterial storageMaterial, StorageMaterialBatch storageMaterialBatch,
                                    StorageOperateTypeEnum storageOperateTypeEnum, Long equipmentId) {
        // 保存物料日志和货位日志
        TicketRequirementGroupDO ticketRequirementGroupDO = ticketRequirementGroupMapper.selectById(req.getRequirementGroupId());
        ProductMaterial productMaterial = null;
        if (Objects.nonNull(ticketRequirementGroupDO)) {
            productMaterial = productMaterialService.selectById(ticketRequirementGroupDO.getMaterialId());
        }
        StorageMaterialPositionLogDTO logDTO = new StorageMaterialPositionLogDTO();
        logDTO.setStorageMaterialId(storageMaterial.getId());
        logDTO.setOperateType(storageOperateTypeEnum);
        logDTO.setQuantity(storageMaterial.getAvailableQuantity());
        logDTO.setUnitId(storageMaterial.getFinalUnitId());
        logDTO.setSenderId(record.getWeighUserId());
        logDTO.setReceiverId(record.getSignUser());
        logDTO.setMaterialPositionId(storageMaterial.getMaterialPositionId());
        logDTO.setProductId(productMaterial != null ? productMaterial.getId() : null);
        logDTO.setProductName(productMaterial != null ? productMaterial.getName() : null);
        logDTO.setProductCode(productMaterial != null ? productMaterial.getMergeCode() : null);
        logDTO.setProductBatchNo(ticketRequirementGroupDO != null ? ticketRequirementGroupDO.getBatchNo() : null);
        logDTO.setMaterialPositionId(storageMaterial.getMaterialPositionId());
        logDTO.setTareWeight(record.getTareWeight());
        logDTO.setGrossWeight(record.getGrossWeight());
        storageMaterialPositionLogService.saveLog(logDTO);
        // 保存称量日志
        TicketDO ticketDO = ticketMapper.selectById(req.getTicketId());
        ProductMaterial weighMaterial = productMaterialService.selectById(storageMaterial.getMaterialId());
        WeighLogSaveDTO saveLogDTO = WeighLogSaveDTO
                .builder()
                .unitId(record.getUnitId())
                .weigherId(record.getWeighUserId())
                .reCheckerId(record.getSignUser())
                .weighType(WeighTypeEnum.NORMAL.equals(record.getWeighType()) ? WeighType.WEIGH_TICKET_EXECUTE : WeighType.WEIGH_TICKET_ODD_EXECUTE)
                .netWeight(unitCache.toBasic(record.getNetWeight(), record.getUnitId()))
                .grossWeight(unitCache.toBasic(record.getGrossWeight(), record.getUnitId()))
                .tareWeight(unitCache.toBasic(record.getTareWeight(), record.getUnitId()))
                .weighTime(record.getWeighTime())
                .materialName(ticketDO != null ? ticketDO.getMaterialName() : null)
                .materialMergeCode(ticketDO != null ? ticketDO.getMaterialMergeCode() : null)
                .materialNo(record.getStorageMaterialNo())
                .materialId(storageMaterial.getMaterialId())
                .materialBatchId(record.getStorageMaterialBatchId())
                .materialId(storageMaterial.getMaterialId())
                .materialType(weighMaterial.getCategoryType())
                .materialBatchNo(storageMaterialBatch.getMaterialBatchNo())
                .productName(productMaterial != null ? productMaterial.getName() : null)
                .productMergeCode(productMaterial != null ? productMaterial.getMergeCode() : null)
                .productBatchNo(ticketDO != null ? ticketRequirementGroupDO.getBatchNo() : null)
                .equipmentId(equipmentId)
                .build();
        // 保存称量设备
        if (Objects.nonNull(equipmentId)) {
            EquipmentInfoFeignVO device = FeignUtils.handleRequest(data -> equipmentConfigFeign.getConfigByEquipmentId(data), equipmentId).getData();
            if (device != null) {
                WeighBalanceEquipment weighBalanceEquipment = ScanDeviceConvert.INSTANCE.convertToEquipment(device);
                saveLogDTO.setEquipmentCode(device.getCode());
                saveLogDTO.setEquipmentName(device.getName());
                saveLogDTO.setEquipmentExpireDate(weighBalanceEquipment.getCalibrateExpiredDate());
                saveLogDTO.setEquipmentStatus(weighBalanceEquipment.getIsCalibrated());
            }
        }
        weighLogService.saveLog(saveLogDTO);
    }


    /**
     * 刷新需求组状态
     *
     * @param ticketId 工单id (直接完成的)
     */
    private void refreshGroupStatus(Long ticketId) {
            // 根据直接完成的工单查询工单下的所有需求
        List<TicketRequirementDO> allGroupRequirements = ticketRequirementMapper.selectWeighingByTicketId(ticketId);

        // 按需求组ID分组
        Map<Long, List<TicketRequirementDO>> requirementGroupMap = allGroupRequirements.stream()
                .collect(Collectors.groupingBy(TicketRequirementDO::getRequirementGroupId));

        // 找出所有需求都已完成的需求组ID
        List<Long> completedGroupIds = requirementGroupMap.entrySet().stream()
                .filter(entry -> entry.getValue().stream().allMatch(req -> (
                        // 已完成的
                        RequirementStatusEnum.WEIGHED.equals(req.getRequirementStatus())
                                // 失效的
                                || RequirementStatusEnum.EXPIRED.equals(req.getRequirementStatus())) && RequirementWeighStatusEnum.FINISHED_SIGN.equals(req.getWeighStatus())
                ))
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());

        if (!CollectionUtils.isAnyEmpty(completedGroupIds)) {
            // 批量查询需要更新的需求组
            List<TicketRequirementGroupDO> groups = ticketRequirementGroupMapper.selectBatchIds(completedGroupIds);

            // 批量更新需求组状态
            for (TicketRequirementGroupDO group : groups) {
                group.setReleaseStatus(TicketRequirementReleaseStatus.FINISHED);
            }
            ticketRequirementGroupMapper.updateBatch(groups);

            log.info("需求组 {} 下所有需求已完成，更新状态为已完成", completedGroupIds);
        }
    }
}