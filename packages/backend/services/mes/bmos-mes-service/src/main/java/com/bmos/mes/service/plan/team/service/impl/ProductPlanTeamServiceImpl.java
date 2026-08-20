package com.bmos.mes.service.plan.team.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.bmos.adaptor.platform.PlatformApiAdaptor;
import com.bmos.common.exception.BmosException;
import com.bmos.common.response.ResponseInfo;
import com.bmos.common.util.collection.CollectionUtils;
import com.bmos.mes.common.enums.BooleanEnum;
import com.bmos.mes.common.exception.MesResponseCode;
import com.bmos.mes.service.facotry.controller.vo.FactoryLineInfoVO;
import com.bmos.mes.service.facotry.converter.FactoryConverter;
import com.bmos.mes.service.permission.dto.ResourcePermissionSaveDTO;
import com.bmos.mes.service.permission.service.ResourcePermissionService;
import com.bmos.mes.service.plan.info.mapper.PlanMapper;
import com.bmos.mes.service.plan.info.model.Plan;
import com.bmos.mes.service.plan.team.constant.TeamConstant;
import com.bmos.mes.service.plan.team.convert.ProductPlanTeamConverter;
import com.bmos.mes.service.plan.team.dto.*;
import com.bmos.mes.service.plan.team.mapper.ProductPlanTeamMapper;
import com.bmos.mes.service.plan.team.mapper.TeamProductionLineMapper;
import com.bmos.mes.service.plan.team.model.ProductPlanTeam;
import com.bmos.mes.service.plan.team.model.TeamProductionLine;
import com.bmos.mes.service.plan.team.service.ProductPlanTeamService;
import com.bmos.mes.service.plan.team.vo.ProductPlanPageTeamVO;
import com.bmos.mes.service.plan.team.vo.ProductPlanTeamDetailVO;
import com.bmos.mes.service.plan.team.vo.ProductPlanTeamListVO;
import com.bmos.mes.service.platform.FeignUtils;
import com.bmos.mes.service.process.mapper.ProcessProductionLineMapper;
import com.bmos.mes.service.process.model.ProcedureModelGroup;
import com.bmos.mes.service.process.model.ProcedureStepModel;
import com.bmos.mes.service.process.model.ProcedureStepRole;
import com.bmos.mes.service.process.model.ProcessProductionLine;
import com.bmos.mes.service.process.service.ProcedureModelGroupService;
import com.bmos.mes.service.process.service.ProcedureModelService;
import com.bmos.mes.service.process.service.ProcedureStepModelService;
import com.bmos.mes.service.process.service.ProcedureStepRoleRelationService;
import com.bmos.mes.service.process.vo.ProcessConfigVO;
import com.bmos.platform.facade.factory.feign.FactoryFeign;
import com.bmos.platform.facade.factory.vo.FactoryLineFeignVO;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ProductPlanTeamServiceImpl implements ProductPlanTeamService {
    @Autowired
    private ProductPlanTeamMapper productPlanTeamMapper;

    @Autowired
    private ResourcePermissionService resourcePermissionService;

    @Autowired
    private PlatformApiAdaptor platformApiAdaptor;

    @Autowired
    private ProcessProductionLineMapper processProductionLineMapper;

    @Autowired
    private TeamProductionLineMapper teamProductionLineMapper;

    @Autowired
    private PlanMapper planMapper;

    @Autowired
    private FactoryFeign factoryFeign;

    @Resource
    private ProcedureModelService modelService;

    @Resource
    private ProcedureStepModelService stepModelService;

    @Resource
    private ProcedureStepRoleRelationService relationService;

    @Override
    public List<ProductPlanPageTeamVO> page(ProductPlanTeamPageDTO dto) {
        // 数据权限
        List<Long> deptIds = platformApiAdaptor.deptIds();
        if (CollUtil.isEmpty(deptIds)) {
            return Collections.emptyList();
        }
        dto.setDeptIds(deptIds);
        PageHelper.startPage(dto.getPageNum(), dto.getPageSize(), dto.getOrderSql());
        return productPlanTeamMapper.page(dto);
    }

    @Override
    public ProductPlanTeamDetailVO detail(Long id) {
        ProductPlanTeamDetailVO result = ProductPlanTeamConverter.INSTANCE.convertVO(productPlanTeamMapper.selectById(id));
        List<TeamProductionLine> relation = teamProductionLineMapper.selectByTeamId(result.getId());
        List<Long> productionLineIds = CollectionUtils.convertList(relation, TeamProductionLine::getProductionLineId);
        List<FactoryLineFeignVO> lines = FeignUtils.handleRequest(data -> factoryFeign.getLineByCondition(data), StrUtil.EMPTY).getData();
        result.setProductionLineIds(productionLineIds);
        if (CollUtil.isNotEmpty(lines)) {
            result.setProductionLines(lines.stream().filter(e-> productionLineIds.contains(e.getId())).collect(Collectors.toList()));
        }
        return result;
    }

    @Override
    public List<ProductPlanPageTeamVO> list(ProductPlanTeamListDTO dto) {
        return productPlanTeamMapper.list(dto);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void save(ProductPlanTeamSaveDTO dto) {
        ProductPlanTeam productPlanTeam = ProductPlanTeamConverter.INSTANCE.convertDO(dto);
        try {
            productPlanTeamMapper.insert(productPlanTeam);
        } catch (DuplicateKeyException exception) {
            throw new BmosException(TeamConstant.findException(exception));
        }
        // 保存数据权限
        resourcePermissionService
                .save(ResourcePermissionSaveDTO.builder().resourceId(productPlanTeam.getId()).deptIds(dto.getDeptIds()).build());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(ProductPlanTeamUpdateDTO dto) {
        ProductPlanTeam productPlanTeam = productPlanTeamMapper.selectById(dto.getId());
        if (Objects.isNull(productPlanTeam)) {
            throw new BmosException(MesResponseCode.TEAM_NOT_EXISTS);
        }
        if (BooleanEnum.TRUE == productPlanTeam.getStatus()) {
            throw new BmosException(MesResponseCode.TEAM_STATUS_FALSE_CAN_EDIT);
        }
        try {
            productPlanTeamMapper.updateById(ProductPlanTeamConverter.INSTANCE.convertDO(dto));
        } catch (DuplicateKeyException exception) {
            throw new BmosException(TeamConstant.findException(exception));
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void enable(Long id) {
        ProductPlanTeam productPlanTeam = productPlanTeamMapper.selectById(id);
        if (Objects.isNull(productPlanTeam)) {
            throw new BmosException(MesResponseCode.TEAM_NOT_EXISTS);
        }
        productPlanTeamMapper.updateStatus(id, BooleanEnum.TRUE);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void disable(Long id) {
        ProductPlanTeam productPlanTeam = productPlanTeamMapper.selectById(id);
        if (Objects.isNull(productPlanTeam)) {
            throw new BmosException(MesResponseCode.TEAM_NOT_EXISTS);
        }
        productPlanTeamMapper.updateStatus(id, BooleanEnum.FALSE);
    }

    @Override
    public List<Long> getListByUserId(String userId) {
        return productPlanTeamMapper.listByUser(userId);
    }

    @Override
    public List<ProductPlanTeamListVO> getTeamListByProductionLineIds(List<Long> lineIds) {
        if (CollUtil.isEmpty(lineIds)) {
            return new ArrayList<>();
        }
        return productPlanTeamMapper.selectListByLineIds(lineIds);
    }

    @Override
    public List<ProductPlanTeamListVO> getTeamListByProductPlanId(Long productPlanId) {
        Plan plan = planMapper.selectById(productPlanId);
        if (plan == null) {
            throw new BmosException(MesResponseCode.PRODUCT_PLAN_NOT_EXISTS);
        }
        return productPlanTeamMapper.selectListByLineIds(Collections.singletonList(plan.getProductionLineId()));
    }

    @Override
    public List<ProductPlanTeamListVO> getTeamListByProcessVersionId(Long processVersionId) {
        List<ProcessProductionLine> lineList = processProductionLineMapper.selectByProcessVersionId(processVersionId);
        if(CollUtil.isEmpty(lineList)){
            return new ArrayList<>();
        }
        return productPlanTeamMapper.selectListByLineIds(CollectionUtils.convertList(lineList, ProcessProductionLine::getProductionLineId));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void boundProductionLines(TeamBoundProductionLineDTO dto) {
        Long id = dto.getId();
        teamProductionLineMapper.deleteByTeamId(id);
        if (CollUtil.isNotEmpty(dto.getProductionLineIds())) {
            List<TeamProductionLine> collect = dto.getProductionLineIds().stream().map(e -> {
                TeamProductionLine relation = new TeamProductionLine();
                relation.setTeamId(dto.getId());
                relation.setProductionLineId(e);
                return relation;
            }).collect(Collectors.toList());
            teamProductionLineMapper.insertBatch(collect);
        }
    }

    @Override
    public List<FactoryLineInfoVO> listLinesByTeamId(Long teamId) {
        List<TeamProductionLine> teamProductionLines = teamProductionLineMapper.selectByTeamId(teamId);
        if (CollUtil.isEmpty(teamProductionLines)) {
            return new ArrayList<>();
        }
        Set<Long> ids = CollectionUtils.convertSet(teamProductionLines, TeamProductionLine::getProductionLineId);
        ResponseInfo<List<FactoryLineFeignVO>> responseInfo = FeignUtils.handleRequest(data -> factoryFeign.getLineByCondition(data), StrUtil.EMPTY);
        if (CollectionUtil.isEmpty(responseInfo.getData())){
            return new ArrayList<>();
        }
        List<FactoryLineInfoVO> result = FactoryConverter.INSTANCE.convert2LineInfoVO(responseInfo.getData());
        if (CollUtil.isEmpty(result)) {
            return new ArrayList<>();
        }
        return result.stream().filter(e-> ids.contains(e.getId())).collect(Collectors.toList());
    }

    @Override
    public List<ProductPlanTeamListVO> getProcessTeamListByProductionLineIds(List<Long> lineIds, Long processVersionId) {
        List<ProductPlanTeamListVO> teamList = this.getTeamListByProductionLineIds(lineIds);
        if (ObjectUtil.isNull(processVersionId)){
            return teamList;
        }
        List<ProcessConfigVO> teamConfig = modelService.getTeamByProcessVersionId(processVersionId);
        if (CollUtil.isNotEmpty(teamConfig)){
            List<Long> team = CollectionUtils.convertList(teamConfig, ProcessConfigVO::getConfigId);
            Set<Long> teamIds = CollectionUtils.convertSet(teamList, ProductPlanTeamListVO::getId);
            List<Long> groupIds = team.stream().filter(item -> !teamIds.contains(item)).collect(Collectors.toList());
            if (CollUtil.isNotEmpty(groupIds)) {
                List<ProductPlanTeamListVO> productPlanTeamList = productPlanTeamMapper.selectListByIds(groupIds);
                productPlanTeamList.forEach(item -> item.setDisabled(true));
                teamList.addAll(productPlanTeamList);
            }
        }
        return teamList;
    }

    @Override
    public List<ProductPlanTeamListVO> getStepTeamListByProcessVersionId(Long processVersionId, Long procedureModelId) {
        List<ProductPlanTeamListVO> teamList = this.getTeamListByProcessVersionId(processVersionId);
        if (ObjectUtil.isNull(procedureModelId)){
            return teamList;
        }
        List<ProcedureStepModel> stepModelList = stepModelService.getByProcedureModelIds(Collections.singletonList(procedureModelId));
        List<ProcedureStepRole> roles = relationService.getListByProcedureStepIds(CollectionUtils.convertSet(stepModelList,ProcedureStepModel::getId));
        List<Long> teamIds = CollectionUtils.convertList(roles, ProcedureStepRole::getRoleId);
        if (CollUtil.isEmpty(teamIds) || CollUtil.isEmpty(teamIds)){
            return teamList;
        }
        List<Long> deleteTeamIds = CollectionUtils.filterList(teamIds, item -> !CollectionUtils.convertList(teamList, ProductPlanTeamListVO::getId).contains(item));
        if (CollUtil.isNotEmpty(deleteTeamIds)) {
            List<ProductPlanTeamListVO> productPlanTeamList = productPlanTeamMapper.selectListByIds(deleteTeamIds);
            productPlanTeamList.forEach(item -> item.setDisabled(true));
            teamList.addAll(productPlanTeamList);
        }
        return teamList;
    }
}
