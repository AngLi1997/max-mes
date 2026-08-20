package com.bmos.mes.service.facotry.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.BooleanUtil;
import cn.hutool.core.util.StrUtil;
import com.bmos.adaptor.platform.PlatformApiAdaptor;
import com.bmos.audit.engine.core.utils.ObjectUtil;
import com.bmos.common.exception.BmosException;
import com.bmos.common.holder.SysUserHolder;
import com.bmos.common.response.ResponseInfo;
import com.bmos.common.util.collection.CollectionUtils;
import com.bmos.common.util.json.JsonUtils;
import com.bmos.logging.annotation.OperationLog;
import com.bmos.mes.common.enums.record.BusinessComponentTypeEnum;
import com.bmos.mes.common.exception.MesResponseCode;
import com.bmos.mes.service.execute.model.ExecuteFormData;
import com.bmos.mes.service.execute.service.ExecuteFormDataService;
import com.bmos.mes.service.facotry.controller.vo.*;
import com.bmos.mes.service.facotry.converter.FactoryConverter;
import com.bmos.mes.service.facotry.service.FactoryService;
import com.bmos.mes.service.facotry.service.data.CleanRoomComponentConfig;
import com.bmos.mes.service.facotry.service.data.FactoryRoomInfo;
import com.bmos.mes.service.facotry.service.data.PlanComponentRoomDTO;
import com.bmos.mes.service.facotry.service.dto.*;
import com.bmos.mes.service.plan.info.model.Plan;
import com.bmos.mes.service.plan.info.service.PlanService;
import com.bmos.mes.service.platform.FeignUtils;
import com.bmos.mes.service.process.mapper.ProcedureModelRoomMapper;
import com.bmos.mes.service.process.mapper.ProcessProductionLineMapper;
import com.bmos.mes.service.process.mapper.ProcessVersionMapper;
import com.bmos.mes.service.process.model.*;
import com.bmos.mes.service.process.service.ProcedureModelService;
import com.bmos.mes.service.process.service.ProcedureStepConfigService;
import com.bmos.mes.service.process.service.ProcedureStepModelService;
import com.bmos.mes.service.process.vo.ProcessConfigVO;
import com.bmos.mes.service.product.model.ProductMaterial;
import com.bmos.mes.service.product.service.ProductMaterialService;
import com.bmos.mes.service.record.business.BusinessComponentStrategy;
import com.bmos.mes.service.record.business.model.ProductionDetailInfo;
import com.bmos.mes.service.record.model.BatchRecordComponent;
import com.bmos.mes.service.record.service.BatchRecordComponentService;
import com.bmos.mes.service.record.vo.ComponentListVO;
import com.bmos.mybatis.page.CommonPage;
import com.bmos.platform.facade.factory.dto.ChangeRoomStatusFeignDTO;
import com.bmos.platform.facade.factory.dto.MobileChangeRoomStatusFeignDTO;
import com.bmos.platform.facade.factory.feign.FactoryAppFeign;
import com.bmos.platform.facade.factory.feign.FactoryFeign;
import com.bmos.platform.facade.factory.vo.*;
import com.bmos.platform.facade.system.user.dto.UserResourceQueryDTO;
import com.bmos.platform.facade.system.user.feign.UserFeign;
import com.bmos.platform.facade.system.user.vo.FeignUserVO;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class FactoryServiceImpl implements FactoryService {

    @Autowired
    private FactoryFeign factoryFeign;

    @Autowired
    private Map<String, BusinessComponentStrategy> componentStrategyMap;

    @Autowired
    ExecuteFormDataService executeFormDataService;

    @Autowired
    private BatchRecordComponentService recordComponentService;

    @Autowired
    private ProcedureStepConfigService procedureStepConfigService;

    @Autowired
    private ProcedureStepModelService procedureStepModelService;

    @Autowired
    private ProcedureModelService procedureModelService;

    @Autowired
    PlanService planService;

    @Autowired
    FactoryAppFeign factoryAppFeign;

    @Autowired
    ProductMaterialService productMaterialService;

    @Autowired
    private ProcessProductionLineMapper processProductionLineMapper;

    @Autowired
    private ProcessVersionMapper processVersionMapper;
    @Autowired
    private UserFeign userFeign;
    @Autowired
    private PlatformApiAdaptor platformApiAdaptor;
    @Resource
    private ProcedureModelRoomMapper procedureModelRoomMapper;

    @Override
    public FactoryRoomInfoVO getRoomInfo(Long roomId) {
        ResponseInfo<List<RoomInfoFeignVO>> responseInfo = FeignUtils.handleRequest(data -> factoryFeign.selectByRoomIds(Collections.singletonList(data)), roomId);
        if (CollectionUtil.isEmpty(responseInfo.getData())){
            return null;
        }
        RoomInfoFeignVO roomInfoFeignVO = responseInfo.getData().get(0);
        return FactoryConverter.INSTANCE.convert2RoomInfoVO(roomInfoFeignVO);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @OperationLog
    public void cleanRoom(FactoryRoomCleanDTO dto) {
        ChangeRoomStatusFeignDTO changeRoomStatusFeignDTO = FactoryConverter.INSTANCE.convert2RoomStatusFeignDTO(dto);
        // 根据生产计划id查询生产计划
        Plan plan = planService.getById(dto.getProductPlanId());
        changeRoomStatusFeignDTO.setProductName(plan.getProductName());
        // 根据工序模型id查询工艺模型id
        ProcedureStepModel procedureStepModel = procedureStepModelService.getById(dto.getProcedureStepModelId());
        ProcedureModel procedureModel = procedureModelService.getById(procedureStepModel.getProcedureModelId());
        changeRoomStatusFeignDTO.setProcedureId(procedureModel.getId());
        changeRoomStatusFeignDTO.setProcedureName(procedureModel.getName());
        // 房间清场执行
        factoryFeign.changeRoomStatus(changeRoomStatusFeignDTO);
        // 若组件类型为清场检查 则需要修改房间清场检查表单数据
        ComponentListVO componentListVO = this.findComponentList(dto.getRecordVersionId(), dto.getRecordItemId(), dto.getComponentId());
        if (ObjectUtil.isEmpty(componentListVO)){
            throw new BmosException(MesResponseCode.ROOM_COMPONENT_NOT_EXIST);
        }
        BusinessComponentTypeEnum componentTypeEnum = BusinessComponentTypeEnum.getEnumByValue(componentListVO.getComponentType());
        if (BusinessComponentTypeEnum.CLEAN_CHECK_ROOM.equals(componentTypeEnum)){
            return ;
        }

        // 判断当前登陆人是否有清场检查权限
        validPermission(componentListVO, dto, procedureStepModel);
        FactoryRoomInfo factoryRoomInfo = this.buildFactoryInfo(dto, procedureModel);
        List<ExecuteFormData> executeFormDataList = this.generateFormDataList(componentListVO, factoryRoomInfo, componentTypeEnum);
        for (ExecuteFormData executeFormData : executeFormDataList) {
            executeFormData.setReuse(dto.getReuse());
            executeFormData.setProcedureStepId(dto.getProcedureStepId());
            executeFormData.setCopyVersion(dto.getCopyVersion());
            executeFormData.setRecordItemId(dto.getRecordItemId());
        }
        try{
            executeFormDataService.saveResultsAndHandleRelationComponentData(executeFormDataList, dto);
        } catch (Exception e){
            log.error("保存清场检查组件信息失败", e);
            throw new BmosException(MesResponseCode.CLEAN_CHECK_ROOM_SAVE_DOUBLE);
        }
    }

    private FactoryRoomInfo buildFactoryInfo(RoomCleanCheckSaveDTO dto, ProcedureModel procedureModel) {
        ResponseInfo<RoomCleanInfoFeignVO> responseInfo = FeignUtils.handleRequest(data -> factoryFeign.getRoomCleanInfoByRoomId(dto.getRoomId()), dto.getRoomId());
        if (ObjectUtil.isNull(responseInfo.getData())){
            throw new BmosException(MesResponseCode.CLEAN_ROOM_NOT_EXIST);
        }
        Plan plan = planService.getById(dto.getProductPlanId());

        return FactoryConverter.INSTANCE.convert2FactoryInfo(dto, responseInfo.getData(), plan, procedureModel);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveRoomCleanCheckComponent(RoomCleanCheckSaveDTO dto) {
        // 查询组件信息
        ComponentListVO componentListVO = this.findComponentList(dto.getRecordVersionId(), dto.getRecordItemId(), dto.getComponentId());

        ProcedureStepModel procedureStepModel = procedureStepModelService.getById(dto.getProcedureStepModelId());
        // 判断当前登陆人是否有清场检查权限
        validPermission(componentListVO, dto, procedureStepModel);

        BusinessComponentTypeEnum componentTypeEnum = BusinessComponentTypeEnum.getEnumByValue(componentListVO.getComponentType());
        if (!BusinessComponentTypeEnum.CLEAN_CHECK_ROOM.equals(componentTypeEnum)){
            throw new BmosException(MesResponseCode.CLEAN_CHECK_ROOM_TYPE_ERROR);
        }
        // 查询当前房间最近以此的清场信息
        ResponseInfo<RoomCleanInfoFeignVO> responseInfo = FeignUtils.handleRequest(data -> factoryFeign.getRoomCleanInfoByRoomId(dto.getRoomId()), dto.getRoomId());
        if (ObjectUtil.isNull(responseInfo.getData())){
            throw new BmosException(MesResponseCode.CLEAN_ROOM_NOT_EXIST);
        }
        ProcedureModel procedureModel = procedureModelService.getById(procedureStepModel.getProcedureModelId());
        // 构建表单所需要的房间清场信息
        FactoryRoomInfo factoryRoomInfo = this.buildFactoryInfo(dto, procedureModel);
        List<ExecuteFormData> executeFormDataList = this.generateFormDataList(componentListVO, factoryRoomInfo, componentTypeEnum);
        for (ExecuteFormData executeFormData : executeFormDataList) {
            executeFormData.setReuse(dto.getReuse());
            executeFormData.setProcedureStepId(dto.getProcedureStepId());
            executeFormData.setCopyVersion(dto.getCopyVersion());
            executeFormData.setRecordItemId(dto.getRecordItemId());
        }
        try{
            executeFormDataService.saveResultsAndHandleRelationComponentData(executeFormDataList, dto);
        } catch (Exception e){
            log.error("保存清场检查组件信息失败", e);
            throw new BmosException(MesResponseCode.CLEAN_CHECK_ROOM_SAVE_DOUBLE);
        }
        // 房间清场检查完成将状态变更为在用
        String userId = SysUserHolder.getUser().getUserId();
        ChangeRoomStatusFeignDTO changeRoomStatusFeignDTO = FactoryConverter.INSTANCE.convert2RoomStatusFeignDTO(dto, responseInfo.getData(), procedureStepModel, procedureModel,userId);
        Plan plan = planService.getById(dto.getProductPlanId());
        changeRoomStatusFeignDTO.setProductName(plan.getProductName());
        FeignUtils.handleRequest(data-> factoryFeign.changeRoomStatus(data), changeRoomStatusFeignDTO);
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveRoomCleanInfoComponent(RoomCleanInfoSaveDTO dto) {
        // 组件信息
        ComponentListVO componentListVO = this.findComponentList(dto.getRecordVersionId(), dto.getRecordItemId(), dto.getComponentId());

        ProcedureStepModel procedureStepModel = procedureStepModelService.getById(dto.getProcedureStepModelId());
        // 权限校验
        validPermission(componentListVO, dto, procedureStepModel);

        BusinessComponentTypeEnum componentTypeEnum = BusinessComponentTypeEnum.getEnumByValue(componentListVO.getComponentType());
        if (!BusinessComponentTypeEnum.CLEAN_INFO.equals(componentTypeEnum)){
            throw new BmosException(MesResponseCode.CLEAN_CHECK_ROOM_TYPE_ERROR);
        }
        // 查询当前房间最近以此的清场信息
        ResponseInfo<RoomCleanInfoFeignVO> responseInfo = FeignUtils.handleRequest(data -> factoryFeign.getRoomCleanInfoByRoomId(dto.getRoomId()), dto.getRoomId());
        if (ObjectUtil.isNull(responseInfo.getData())){
            throw new BmosException(MesResponseCode.CLEAN_ROOM_NOT_EXIST);
        }
        // 构建所需的房间清场相关信息
        ProcedureModel procedureModel = procedureModelService.getById(procedureStepModel.getProcedureModelId());
        FactoryRoomInfo factoryRoomInfo = this.buildFactoryInfo(dto, procedureModel);
        // 生成表单数据
        List<ExecuteFormData> executeFormDataList = this.generateFormDataList(componentListVO, factoryRoomInfo, componentTypeEnum);
        for (ExecuteFormData executeFormData : executeFormDataList) {
            executeFormData.setReuse(dto.getReuse());
            executeFormData.setProcedureStepId(dto.getProcedureStepId());
            executeFormData.setCopyVersion(dto.getCopyVersion());
            executeFormData.setRecordItemId(dto.getRecordItemId());
        }
        // 保存表单数据
        try{
            executeFormDataService.saveResultsAndHandleRelationComponentData(executeFormDataList, dto);
        } catch (Exception e){
            log.error("保存清场信息组件信息失败", e);
            throw new BmosException(MesResponseCode.CLEAN_CHECK_INFO_SAVE_DOUBLE);
        }


    }

    @Override
    public List<FactoryLineModuleTreeVO> getFactoryLine() {
        ResponseInfo<List<LineModuleTreeNodeFeignVO>> responseInfo = FeignUtils.handleRequest(data -> factoryFeign.getLineModuleTreeVO(), null);
        if (CollectionUtil.isEmpty(responseInfo.getData())){
            return new ArrayList<>();
        }
        return FactoryConverter.INSTANCE.convert2LineModuleInfoVO(responseInfo.getData());
    }

    @Override
    public List<FactoryRoomVO> getLineRoom(List<Long> lineIds) {
        ResponseInfo<List<FactoryLineDetailFeignVO>> responseInfo = FeignUtils.handleRequest(data -> factoryFeign.getLineDetailByLineIds(data, false), lineIds);
        if (CollectionUtil.isEmpty(responseInfo.getData())){
            return new ArrayList<>();
        }
        return FactoryConverter.INSTANCE.convert2FactoryRoomTree(responseInfo.getData());
    }

    @Override
    public CommonPage<RoomMobilePageVO> getRoomMobilePage(RoomMobilePageDTO dto) {
        ResponseInfo<CommonPage<RoomMobilePageFeignVO>> response = FeignUtils.handleRequest(data -> factoryAppFeign.getRoomMobilePage(data), FactoryConverter.INSTANCE.convert2FactoryFeignDTO(dto));
        List<RoomMobilePageVO> res = new ArrayList<>();
        CommonPage<RoomMobilePageFeignVO> pageFeignData = response.getData();
        if (ObjectUtil.isNotEmpty(pageFeignData)){
            res = FactoryConverter.INSTANCE.convert2MobilePageVO(pageFeignData.getList());
            return CommonPage.CommonPage(res, Long.valueOf(pageFeignData.getTotal()), dto);
        }
        return CommonPage.CommonPage(res, 0L, dto);
    }

    @Override
    public void operateRoomStatus(ChangeRoomStatusDTO dto) {
        MobileChangeRoomStatusFeignDTO feignDTO = FactoryConverter.INSTANCE.convertChangeRoomFeignDTO(dto);
        if (ObjectUtil.isNotEmpty(dto.getProductId())){
            ProductMaterial productMaterial = productMaterialService.selectById(dto.getProductId());
            if (ObjectUtil.isNotEmpty(productMaterial)){
                feignDTO.setProductName(productMaterial.getName());
            }
        }
        if (ObjectUtil.isNotEmpty(dto.getProcedureId())){
            ProcedureModel procedureModel = procedureModelService.getById(dto.getProcedureId());
            if (ObjectUtil.isNotEmpty(procedureModel)){
                feignDTO.setProcedureName(procedureModel.getName());
            }
        }
        FeignUtils.handleRequest(data -> factoryAppFeign.operateRoomStatus(data), feignDTO);
    }

    @Override
    public RoomInfoMobileVO getMobileRoomInfo(Long id) {
        ResponseInfo<RoomInfoMobileFeignVO> responseInfo = FeignUtils.handleRequest(data -> factoryAppFeign.getMobileRoomInfo(data), id);
        if (ObjectUtil.isEmpty(responseInfo.getData())){
            return null;
        }
        return FactoryConverter.INSTANCE.convert2RoomInfoMobileVO(responseInfo.getData());
    }

    @Override
    public List<FactoryLineInfoVO> getFactoryLineByProcessVersionId(Long processVersionId) {
        List<FactoryLineFeignVO> list = FeignUtils.handleRequest(data -> factoryFeign.getLineByCondition(data),
                StrUtil.EMPTY).getData();
        List<ProcessProductionLine> lineList = processProductionLineMapper.selectByProcessVersionId(processVersionId);
        if (CollUtil.isEmpty(list) || CollUtil.isEmpty(lineList)) {
            return new ArrayList<>();
        }
        List<FactoryLineInfoVO> result = FactoryConverter.INSTANCE.convert2LineInfoVO(list);
        Set<Long> boundSet = CollectionUtils.convertSet(lineList, ProcessProductionLine::getProductionLineId);
        return result.stream().filter(e -> boundSet.contains(e.getId())).collect(Collectors.toList());
    }

    @Override
    public List<FactoryLineInfoVO> getFactoryLineByProcessVersion(Long processId, String version) {
        ProcessVersion processVersion = processVersionMapper.selectByProcessIdAndVersion(processId, version);
        if (processVersion == null) {
            throw new BmosException(MesResponseCode.PROCESS_VERSION_NOT_EXIST);
        }
        List<FactoryLineFeignVO> list = FeignUtils.handleRequest(data -> factoryFeign.getLineByCondition(data),
                StrUtil.EMPTY).getData();
        List<ProcessProductionLine> lineList = processProductionLineMapper.selectByProcessVersionId(processVersion.getId());
        if (CollUtil.isEmpty(list) || CollUtil.isEmpty(lineList)) {
            return new ArrayList<>();
        }
        List<FactoryLineInfoVO> result = FactoryConverter.INSTANCE.convert2LineInfoVO(list);
        Set<Long> boundSet = CollectionUtils.convertSet(lineList, ProcessProductionLine::getProductionLineId);
        return result.stream().filter(e -> boundSet.contains(e.getId())).collect(Collectors.toList());
    }

    @Override
    public List<RoomInfoMobileVO> planStepComponentRoomList(PlanComponentRoomDTO dto) {
        // 根据生产计划id查询生产计划
        Plan plan = planService.getById(dto.getPlanId());
        if (ObjectUtil.isEmpty(plan)){
            throw new BmosException(MesResponseCode.PRODUCT_PLAN_NOT_EXISTS);
        }
        // 生产计划中绑定的产线id
        Long planLineId = plan.getProductionLineId();
        if (ObjectUtil.isNull(planLineId)){
            throw new BmosException(MesResponseCode.PLAN_NOT_BIND_LINE);
        }
        // 获取当前组件上绑定的房间信息
        ProcedureStepModel procedureStepModel = procedureStepModelService.getById(dto.getProcedureStepModelId());
        // 组件信息
        ComponentListVO componentListVO = this.findComponentList(procedureStepModel.getRecordVersionId(), procedureStepModel.getRecordItemId(), dto.getComponentId());
        String componentConfigJson = procedureStepConfigService.getComponentConfigJson(dto.getProcedureStepModelId(), componentListVO.getId(), procedureStepModel.getReusable(), procedureStepModel.getProcessId(), procedureStepModel.getProcessVersion());
        CleanRoomComponentConfig componentConfig = JsonUtils.parseObject(componentConfigJson, CleanRoomComponentConfig.class);
        List<Long> componentLineRoomList = new ArrayList<>();
        if (ObjectUtil.isNotEmpty(componentConfig) && CollectionUtil.isNotEmpty(componentConfig.getRoomIdListShow())){
            for (String roomIdShow : componentConfig.getRoomIdListShow()) {
                String[] split = roomIdShow.split(StrUtil.DASHED);
                if (!StrUtil.equals(String.valueOf(planLineId), split[0])){
                    continue;
                }
                componentLineRoomList.add(Long.valueOf(split[1]));
            }
        }

        ResponseInfo<List<RoomInfoFeignVO>> listResponseInfo = null;
        // 若componentLineRoomList为空 则查询生产计划产线下绑定的所有房间
        if (CollectionUtil.isEmpty(componentLineRoomList)){
            List<ProcedureModelRoom> procedureModelRooms = procedureModelRoomMapper.selectByProcedureModelId(procedureStepModel.getProcedureModelId());
            if (CollUtil.isEmpty(procedureModelRooms)) {
                // 查询产线下所有的房间信息
                listResponseInfo = FeignUtils.handleRequest(data -> factoryFeign.getRoomInfoByLineId(data, false), planLineId);
            } else {
                // 若工序绑定房间 则过滤出当前产线下的工序房间
                listResponseInfo = FeignUtils.handleRequest(data -> factoryFeign.selectByRoomIds(data), CollectionUtils.convertList(procedureModelRooms, ProcedureModelRoom::getRoomId));
            }
        }  else {
            listResponseInfo = FeignUtils.handleRequest(data -> factoryFeign.selectByRoomIds(data), componentLineRoomList);
        }
        List<RoomInfoFeignVO> roomInfoFeignVOS = listResponseInfo.getData();
        if (CollUtil.isEmpty(roomInfoFeignVOS)){
            return new ArrayList<>();
        }
        // 提出虚拟房间
        roomInfoFeignVOS = roomInfoFeignVOS.stream().filter(e -> !StrUtil.equals(e.getCode(), RoomInfoFeignVO.VIRTUAL_ROOM)).collect(Collectors.toList());
        // 获取当前登录用户所属部门
        List<Long> deptIds = platformApiAdaptor.deptIds();
        roomInfoFeignVOS = roomInfoFeignVOS.stream().filter(e -> judgePermission(e, deptIds)).collect(Collectors.toList());
        return FactoryConverter.INSTANCE.convert2RoomInfoMobileVO(roomInfoFeignVOS);
    }

    /**
     * 校验是否有房间权限
     * @param roomInfoFeignVO
     * @param deptIds
     * @return
     */
    private boolean judgePermission(RoomInfoFeignVO roomInfoFeignVO, List<Long> deptIds) {
        if (CollUtil.isEmpty(roomInfoFeignVO.getPermisionIdList())){
            return true;
        }
        if (CollUtil.isEmpty(deptIds)){
            return false;
        }
        Set<Long> deptIdSet = Sets.newHashSet(deptIds);
        for (Long deptId : roomInfoFeignVO.getPermisionIdList()){
            if (deptIdSet.contains(deptId)){
                return true;
            }
        }
        return false;
    }

    @Override
    public RoomInfoMobileVO getMobileComponentRoomInfo(CleanExecuteRoomInfoDTO dto) {
        // 查询组件信息
        ComponentListVO componentListVO = this.findComponentList(dto.getRecordVersionId(), dto.getRecordItemId(), dto.getComponentId());

        ProcedureStepModel procedureStepModel = procedureStepModelService.getById(dto.getProcedureStepModelId());
        // 判断当前登陆人是否有清场检查权限
        String componentConfigJson = procedureStepConfigService.getComponentConfigJson(dto.getProcedureStepModelId(), componentListVO.getId(), procedureStepModel.getReusable(), dto.getProcessId(), dto.getProcessVersion());
        CleanRoomComponentConfig componentConfig = JsonUtils.parseObject(componentConfigJson, CleanRoomComponentConfig.class);
        // 判断房间id是否在配置内
        if (!componentConfig.getRoomIdList().contains(dto.getRoomId())){
            throw new BmosException(MesResponseCode.CLEAN_ROOM_COMPONENT_NOT_CONFIG);
        }
        // 判断房间是否属于当前产线下的房间
        Plan plan = planService.getById(dto.getProductPlanId());
        if (ObjectUtil.isEmpty(plan)){
            throw new BmosException(MesResponseCode.PRODUCT_PLAN_NOT_EXISTS);
        }
        List<String> roomIdListShow = componentConfig.getRoomIdListShow();
        Set<Long> lineIdSet = new HashSet<>();
        for (String lineRoomId : roomIdListShow) {
            String[] split = lineRoomId.split(StrUtil.DASHED);
            if (!StrUtil.equals(String.valueOf(dto.getRoomId()), split[1])){
                continue;
            }
            lineIdSet.add(Long.valueOf(split[0]));
        }
        if (CollectionUtil.isEmpty(lineIdSet) || !lineIdSet.contains(plan.getProductionLineId())){
            throw new BmosException(MesResponseCode.LINE_NOT_BIND_ROOM);
        }
        return this.getMobileRoomInfo(dto.getRoomId());
    }

    @Override
    public List<FactoryRoomAuthUserVO> getRoomAuthUser(FactoryRoomAuthUserDTO dto) {
        UserResourceQueryDTO userResourceQueryDTO = new UserResourceQueryDTO();
        userResourceQueryDTO.setResourceId(dto.getRoomId());
        if (StrUtil.isNotEmpty(dto.getAuthCode())){
            userResourceQueryDTO.setMenuId(Long.valueOf(dto.getAuthCode()));
        }
        ResponseInfo<List<FeignUserVO>> responseInfo = FeignUtils.handleRequest(data -> userFeign.listByMenuIdAndResourceId(data), userResourceQueryDTO);
        return FactoryConverter.INSTANCE.convert2AuthUserVO(responseInfo.getData());
    }

    @Override
    public RoomInfoMobileVO getMobileRoomInfoByCode(String code) {
        ResponseInfo<RoomInfoMobileFeignVO> responseInfo = FeignUtils.handleRequest(data -> factoryAppFeign.getMobileRoomInfoByCode(data), code);
        if (ObjectUtil.isEmpty(responseInfo.getData())){
            return null;
        }
        return FactoryConverter.INSTANCE.convert2RoomInfoMobileVO(responseInfo.getData());
    }

    @Override
    public List<FactoryLineModuleTreeVO> getFactoryProcessLine(Long processVersionId) {
        //查询产线下拉框
        List<FactoryLineModuleTreeVO> factoryLine = this.getFactoryLine();
        //工艺配置产线信息
        List<ProcessProductionLine> lines = processProductionLineMapper.selectByProcessVersionId(processVersionId);
        if (CollUtil.isEmpty(lines)){
            return factoryLine;
        }
        List<Long> lineIds = new ArrayList<>();
        if (CollUtil.isNotEmpty(factoryLine)) {
            List<FactoryLineModuleTreeVO> treeVOS = CollectionUtils.convertList(factoryLine, FactoryLineModuleTreeVO::getChildren,
                    item -> CollUtil.isNotEmpty(item.getChildren()))
                    .stream()
                    .flatMap(Collection::stream)
                    .collect(Collectors.toList());
            //判断产线是否删除
            lines.forEach(item -> {
                if (BooleanUtil.isFalse(this.queryLineId(treeVOS, item.getProductionLineId()))) {
                    lineIds.add(item.getProductionLineId());
                }
                ;
            });
        }else {
            lineIds.addAll(CollectionUtils.convertList(lines,ProcessProductionLine::getProductionLineId));
        }
        //查询产线信息
        if (CollUtil.isNotEmpty(lineIds)){
            ResponseInfo<List<FactoryLineFeignVO>> listResponseInfo = factoryFeign.queryLineListByLineIds(lineIds);
            List<FactoryLineModuleTreeVO> treeVOList = listResponseInfo.getData().stream().map(item -> {
                FactoryLineModuleTreeVO vo = new FactoryLineModuleTreeVO();
                vo.setCode(item.getCode());
                vo.setId(item.getId());
                vo.setLineFlag(true);
                vo.setName(item.getName());
                vo.setParentId(item.getModuleId());
                vo.setDisabled(true);
                vo.setShowName(item.getName() + StrUtil.DASHED + item.getCode());
                return vo;
            }).collect(Collectors.toList());
            factoryLine.addAll(treeVOList);
        }
        return factoryLine;
    }

    @Override
    public List<FactoryRoomVO> getProcessLineRoom(List<Long> lineIds, Long procedureModelId) {
        List<FactoryRoomVO> lineRoom = this.getLineRoom(lineIds);
        List<ProcessConfigVO> roomIdList = procedureModelService.getRoomListByProcedureModelId(procedureModelId);
        if (CollUtil.isEmpty(roomIdList)){
            return lineRoom;
        }
        List<FactoryRoomVO> roomVoList = CollectionUtils.convertList(lineRoom, FactoryRoomVO::getChildren,
                item -> CollUtil.isNotEmpty(item.getChildren()))
                .stream()
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
        List<ProcessConfigVO> refreshRoomId = new ArrayList<>();
        if (CollUtil.isNotEmpty(lineRoom)) {
            roomIdList.forEach(item -> {
                if (BooleanUtil.isFalse(queryRoomId(item.getConfigId(), roomVoList,item.getRoomIdPath()))) {
                    refreshRoomId.add(item);
                }
            });
        }else {
            refreshRoomId.addAll(roomIdList);
        }
        if (CollUtil.isNotEmpty(refreshRoomId)){
            List<Long> roomId = CollectionUtils.convertList(refreshRoomId, ProcessConfigVO::getConfigId);
            ResponseInfo<List<FactoryRoomFeignVO>> list = factoryFeign.queryRoomListByRoomIds(roomId);
            Map<Long, List<ProcessConfigVO>> map = CollectionUtils.convertMultiMap(refreshRoomId, ProcessConfigVO::getConfigId);
            list.getData().forEach(item -> {
                List<ProcessConfigVO> processConfigVo = map.get(item.getId());
                processConfigVo.forEach(configVo ->{
                    FactoryRoomVO vo = new FactoryRoomVO();
                    vo.setCode(item.getCode());
                    vo.setId(item.getId());
                    vo.setName(item.getName());
                    vo.setRoomFlag(true);
                    vo.setDisabled(true);
                    vo.setRoomIdPath(configVo.getRoomIdPath());
                    vo.setShowName(item.getCode() + StrUtil.DASHED + item.getName());
                    lineRoom.add(vo);
                });
            });
        }
        return lineRoom;
    }

    /**
     * 循环查询当前产线下的房间时候被删除
     * @param roomId 工艺配置房间id
     * @param lineRoom 房间信息
     * @return
     */
    private Boolean queryRoomId(Long roomId,List<FactoryRoomVO> lineRoom,String roomPatch){
        for (FactoryRoomVO roomVO : lineRoom) {
            if (roomVO.getId().equals(roomId) && roomVO.getRoomIdPath().equals(roomPatch)){
                return true;
            }
            if (CollUtil.isNotEmpty(roomVO.getChildren())){
                this.queryRoomId(roomId,roomVO.getChildren(),roomPatch);
            }

        }
        return false;
    }

    /**
     * 循环查询当前产线是否被删除
     * @param factoryLine
     * @param lineId
     * @return
     */
    private Boolean queryLineId(List<FactoryLineModuleTreeVO> factoryLine,Long lineId){
        for (FactoryLineModuleTreeVO item : factoryLine) {
            if (BooleanUtil.isTrue(item.getLineFlag()) && item.getId().equals(lineId)){
                return true;
            }
            if (CollUtil.isNotEmpty(item.getChildren())){
                this.queryLineId(item.getChildren(),lineId);
            }
        }
        return false;

    }
    /**
     * 校验是否有清场权限
     * @param componentListVO
     * @param dto
     * @param procedureStepModel
     */
    private void validPermission(ComponentListVO componentListVO, RoomCleanCheckSaveDTO dto, ProcedureStepModel procedureStepModel) {
        String componentConfigJson = procedureStepConfigService.getComponentConfigJson(dto.getProcedureStepModelId(), componentListVO.getId(), procedureStepModel.getReusable(), dto.getProcessId(), dto.getProcessVersion());
        CleanRoomComponentConfig componentConfig = JsonUtils.parseObject(componentConfigJson, CleanRoomComponentConfig.class);
        if (Objects.isNull(componentConfig)) {
            return;
        }
        // 判断房间id是否在配置内
        if (CollectionUtil.isNotEmpty(componentConfig.getRoomIdList())&&!componentConfig.getRoomIdList().contains(dto.getRoomId())){
            throw new BmosException(MesResponseCode.CLEAN_ROOM_COMPONENT_NOT_CONFIG);
        }

        boolean permission = Boolean.TRUE;
        if (CollectionUtil.isNotEmpty(componentConfig.getStation())){
            permission = Boolean.FALSE;
            ResponseInfo<List<StationPermissionVO>> listResponseInfo = factoryFeign.checkStationPermission(componentConfig.getStation(), SysUserHolder.getUser().getUserId());
            if (CollectionUtil.isNotEmpty(listResponseInfo.getData())){
                for (StationPermissionVO stationPermissionVO : listResponseInfo.getData()) {
                    if (!stationPermissionVO.isPermission()){
                        continue;
                    }
                    permission = Boolean.TRUE;
                    break;
                }
            }
        }
        if (!permission){
            throw new BmosException(MesResponseCode.CLEAN_ROOM_COMPONENT_PERMISSION_ERROR);
        }
    }

    /**
     * 生成表单数据
     * @param componentListVO
     * @param factoryRoomInfo
     * @param componentTypeEnum
     * @return
     */
    private List<ExecuteFormData> generateFormDataList(ComponentListVO componentListVO, FactoryRoomInfo factoryRoomInfo, BusinessComponentTypeEnum componentTypeEnum) {
        List<ExecuteFormData> executeFormDataList = Lists.newArrayList();
        ProductionDetailInfo productionDetailInfo = new ProductionDetailInfo();
        productionDetailInfo.setFactoryRoomInfo(factoryRoomInfo);
        componentStrategyMap.get(componentTypeEnum.getValue())
                .handleBusinessComponent(executeFormDataList, componentListVO, productionDetailInfo, new HashMap<>(), null);
        return executeFormDataList;
    }

    /**
     * 根据组件相关信息查询其所属的组件树
     * @param recordVersionId
     * @param recordItemId
     * @param componentId
     * @return
     */
    private ComponentListVO findComponentList(Long recordVersionId, Long recordItemId, Long componentId) {
        BatchRecordComponent recordComponent = recordComponentService.getById(componentId);
        if (ObjectUtil.isNull(recordComponent)){
            throw new BmosException(MesResponseCode.COMPONENT_NOT_EXIST);
        }
        // 查询当前componentId
        ComponentListVO componentListVO =
                recordComponentService.selectUsedComponentDetail(recordVersionId,
                        recordItemId,
                        recordComponent.getParentId());
        return componentListVO;
    }


}
