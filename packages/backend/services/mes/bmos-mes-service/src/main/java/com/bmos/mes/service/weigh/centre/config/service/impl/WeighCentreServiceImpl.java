package com.bmos.mes.service.weigh.centre.config.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import com.bmos.adaptor.platform.PlatformApiAdaptor;
import com.bmos.common.exception.BmosException;
import com.bmos.mes.common.enums.weigh.centre.RequirementStatusEnum;
import com.bmos.mes.common.exception.MesResponseCode;
import com.bmos.mes.service.permission.dto.ResourcePermissionSaveDTO;
import com.bmos.mes.service.permission.service.ResourcePermissionService;
import com.bmos.mes.service.platform.FeignUtils;
import com.bmos.mes.service.weigh.centre.config.convert.WeighCentreCategoryConvert;
import com.bmos.mes.service.weigh.centre.config.convert.WeighCentreConvert;
import com.bmos.mes.service.weigh.centre.config.dto.WeighCentreBindStationDTO;
import com.bmos.mes.service.weigh.centre.config.dto.WeighCentreCreateDTO;
import com.bmos.mes.service.weigh.centre.config.dto.WeighCentreEditDTO;
import com.bmos.mes.service.weigh.centre.config.dto.WeighCentrePageQuery;
import com.bmos.mes.service.weigh.centre.config.mapper.IWeighCentreCategoryMapper;
import com.bmos.mes.service.weigh.centre.config.mapper.IWeighCentreMapper;
import com.bmos.mes.service.weigh.centre.config.mapper.IWeighCentreStationMapper;
import com.bmos.mes.service.weigh.centre.config.model.WeighCentre;
import com.bmos.mes.service.weigh.centre.config.model.WeighCentreCategory;
import com.bmos.mes.service.weigh.centre.config.model.WeighCentreStation;
import com.bmos.mes.service.weigh.centre.config.service.IWeighCentreService;
import com.bmos.mes.service.weigh.centre.config.util.BmosTreeUtil;
import com.bmos.mes.service.weigh.centre.config.vo.WeighCentreCategoryPath;
import com.bmos.mes.service.weigh.centre.config.vo.WeighCentreCategoryWithCentreVO;
import com.bmos.mes.service.weigh.centre.config.vo.WeighCentreDetailVO;
import com.bmos.mes.service.weigh.centre.config.vo.WeighCentrePageVO;
import com.bmos.mes.service.weigh.centre.requirement.mapper.IWeighRequirementMapper;
import com.bmos.mes.service.weigh.centre.requirement.model.WeighRequirement;
import com.bmos.mybatis.page.CommonPage;
import com.bmos.platform.facade.equipment.feign.EquipmentConfigFeign;
import com.bmos.platform.facade.factory.dto.LineUseDTO;
import com.bmos.platform.facade.factory.feign.FactoryFeign;
import com.github.pagehelper.PageHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 称量中心 service impl
 *
 * @author liang
 * @version 1.0.0
 * @date 2024/7/3 17:40
 */
@Service
@Slf4j
public class WeighCentreServiceImpl implements IWeighCentreService {

    private static final String LOG_PREFIX = "[称量中心]";

    @Resource
    private IWeighCentreCategoryMapper weighCentreCategoryMapper;

    @Resource
    private IWeighCentreMapper weighCentreMapper;

    @Resource
    private IWeighCentreStationMapper weighCentreStationMapper;

    @Resource
    private ResourcePermissionService resourcePermissionService;

    @Resource
    private PlatformApiAdaptor platformApiAdaptor;

    @Resource
    private FactoryFeign factoryFeign;

    @Resource
    private EquipmentConfigFeign equipmentConfigFeign;

    @Resource
    private IWeighRequirementMapper weighRequirementMapper;


    @Override
    public CommonPage<WeighCentrePageVO> queryPage(WeighCentrePageQuery pageQuery) {
        List<Long> weighCentreCategoryIds = weighCentreCategoryMapper.listAllChildren(pageQuery.getCategoryId())
                .stream()
                .map(WeighCentreCategory::getId)
                .collect(Collectors.toList());
        weighCentreCategoryIds.add(pageQuery.getCategoryId());
        PageHelper.startPage(pageQuery.getPageNum(), pageQuery.getPageSize(), pageQuery.getOrderSql());
        List<Long> deptIds = platformApiAdaptor.deptIds();
        List<WeighCentre> list = weighCentreMapper.queryPage(pageQuery, weighCentreCategoryIds, deptIds);
        if (CollectionUtil.isEmpty(list)) {
            return CommonPage.convertPage(new ArrayList<>());
        }
        List<Long> categoryIds = list.stream().map(WeighCentre::getCategoryId).collect(Collectors.toList());
        Map<Long, String> pathMap = weighCentreCategoryMapper.getNamePath(categoryIds)
                .stream()
                .collect(Collectors.toMap(WeighCentreCategoryPath::getId, WeighCentreCategoryPath::getNamePath, (v1, v2) -> v1));
        CommonPage<WeighCentre> page = CommonPage.convertPage(list);
        CommonPage<WeighCentrePageVO> result = WeighCentreConvert.INSTANCE.convertToVO(page);
        result.getList().forEach(item -> {
            item.setCategoryNamePath(pathMap.get(item.getCategoryId()));
        });
        return result;
    }

    @Override
    public WeighCentreDetailVO queryCentreInfo(Long id) {
        WeighCentre weighCentre = weighCentreMapper.selectById(id);
        if (weighCentre == null) {
            throw new BmosException(MesResponseCode.WEIGH_CENTRE_NOT_EXIST);
        }
        WeighCentreDetailVO result = WeighCentreConvert.INSTANCE.convertToVO(weighCentre);
        List<Long> deptIds = resourcePermissionService.getDeptListByResourceId(id);
        result.setDeptIds(deptIds);
        List<Long> stationIds = weighCentreStationMapper.queryStationIdsByCentreId(id);
        result.setStationIds(stationIds);
        result.setCategoryName(Optional.ofNullable(weighCentreCategoryMapper.selectById(weighCentre.getCategoryId()))
                .map(WeighCentreCategory::getName)
                .orElse(null));
        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createCentre(WeighCentreCreateDTO createDTO) {
        log.info("{} 创建称量中心:{}", LOG_PREFIX, createDTO);
        WeighCentreCategory weighCentreCategory = weighCentreCategoryMapper.selectById(createDTO.getCategoryId());
        if (weighCentreCategory == null) {
            throw new BmosException(MesResponseCode.WEIGH_CENTRE_CATEGORY_NOT_EXIST);
        }
        List<WeighCentre> list = weighCentreMapper.listByCode(createDTO.getCode());
        if (CollectionUtil.isNotEmpty(list)) {
            throw new BmosException(MesResponseCode.WEIGH_CENTRE_CODE_EXIST);
        }
        WeighCentre weighCentre = new WeighCentre();
        weighCentre.setCategoryId(createDTO.getCategoryId());
        weighCentre.setName(createDTO.getName());
        weighCentre.setCode(createDTO.getCode());
        weighCentre.setRemark(createDTO.getRemark());
        // 默认停用
        weighCentre.setEnabled(false);
        weighCentreMapper.insert(weighCentre);
        // 保存数据权限
        resourcePermissionService.save(ResourcePermissionSaveDTO.builder()
                .resourceId(weighCentre.getId())
                .deptIds(createDTO.getDeptIds())
                .build());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void editCentre(WeighCentreEditDTO editDTO) {
        log.info("{} 编辑称量中心:{}", LOG_PREFIX, editDTO);
        WeighCentre weighCentre = weighCentreMapper.selectById(editDTO.getId());
        if (weighCentre == null) {
            throw new BmosException(MesResponseCode.WEIGH_CENTRE_NOT_EXIST);
        }
        // 停用才可以编辑
        if (weighCentre.getEnabled()) {
            throw new BmosException(MesResponseCode.WEIGH_CENTRE_ENABLED);
        }
        // 编码不可以重复
        if (!Objects.equals(editDTO.getCode(), weighCentre.getCode()) && CollectionUtil.isNotEmpty(weighCentreMapper.listByCode(editDTO.getCode()))) {
            throw new BmosException(MesResponseCode.WEIGH_CENTRE_CODE_EXIST);
        }
        weighCentre.setName(editDTO.getName());
        weighCentre.setCode(editDTO.getCode());
        weighCentre.setRemark(editDTO.getRemark());
        weighCentreMapper.updateById(weighCentre);
        // 保存数据权限
        resourcePermissionService.save(ResourcePermissionSaveDTO.builder()
                .resourceId(weighCentre.getId())
                .deptIds(editDTO.getDeptIds())
                .build());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteCentre(Long id) {
        log.info("{} 删除称量中心:{}", LOG_PREFIX, id);
        WeighCentre weighCentre = weighCentreMapper.selectById(id);
        if (weighCentre == null) {
            throw new BmosException(MesResponseCode.WEIGH_CENTRE_NOT_EXIST);
        }
        // 先停用 才能删除
        if (weighCentre.getEnabled()) {
            throw new BmosException(MesResponseCode.WEIGH_CENTRE_ENABLED);
        }
        List<WeighRequirement> requirements = weighRequirementMapper.listByWeighCentreId(id);
        if (CollectionUtil.isNotEmpty(requirements)) {
            // 若该称量中心存在过物料称量需求，无法删除该称量中心
            throw new BmosException(MesResponseCode.WEIGH_CENTRE_NOT_ALLOWED_DELETE_WITH_REQUIREMENT);
        }
        weighCentreMapper.deleteById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void enableCentre(Long id) {
        log.info("{} 启用称量中心:{}", LOG_PREFIX, id);
        WeighCentre weighCentre = weighCentreMapper.selectById(id);
        if (weighCentre == null) {
            throw new BmosException(MesResponseCode.WEIGH_CENTRE_NOT_EXIST);
        }
        if (weighCentre.getEnabled()) {
            throw new BmosException(MesResponseCode.WEIGH_CENTRE_ENABLED);
        }
        weighCentre.setEnabled(true);
        weighCentreMapper.updateById(weighCentre);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void disableCentre(Long id) {
        log.info("{} 停用称量中心:{}", LOG_PREFIX, id);
        WeighCentre weighCentre = weighCentreMapper.selectById(id);
        if (weighCentre == null) {
            throw new BmosException(MesResponseCode.WEIGH_CENTRE_NOT_EXIST);
        }
        if (!weighCentre.getEnabled()) {
            throw new BmosException(MesResponseCode.WEIGH_CENTRE_DISABLED);
        }
        List<WeighRequirement> requirements = weighRequirementMapper.listByWeighCentreId(id);
        // 查询未完成的需求
        List<WeighRequirement> pending = requirements.stream()
                // 和产品确认 未完成指的是 除了已完成和已失效都算未完成
                .filter(item -> !Objects.equals(item.getRequirementStatus(), RequirementStatusEnum.WEIGHED)
                        && !Objects.equals(item.getRequirementStatus(), RequirementStatusEnum.EXPIRED))
                .collect(Collectors.toList());
        if (CollectionUtil.isNotEmpty(pending)) {
            // 若称量中心存在未完成的物料称量需求，无法停用该称量中心
            throw new BmosException(MesResponseCode.WEIGH_CENTRE_NOT_ALLOWED_DISABLED_WITH_REQUIREMENT);
        }
        weighCentre.setEnabled(false);
        weighCentreMapper.updateById(weighCentre);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void bindStation(WeighCentreBindStationDTO bindStationDTO) {
        log.info("{} 绑定称量中心工位:{}", LOG_PREFIX, bindStationDTO);
        WeighCentre weighCentre = weighCentreMapper.selectById(bindStationDTO.getId());
        if (weighCentre == null) {
            throw new BmosException(MesResponseCode.WEIGH_CENTRE_NOT_EXIST);
        }

        // 之前的绑定关系
        List<Long> stationIds = weighCentreStationMapper.queryStationIdsByCentreId(bindStationDTO.getId());
        Map<Long, WeighCentreStation> stationMap = weighCentreStationMapper.queryCentreIdsByStationIds(bindStationDTO.getStationIdList())
                .stream()
                .collect(Collectors.toMap(WeighCentreStation::getStationId, Function.identity(), (v1, v2) -> v1));
        for (Long stationId : bindStationDTO.getStationIdList()) {
            if (stationMap.containsKey(stationId) && !Objects.equals(stationMap.get(stationId).getWeighCentreId(), bindStationDTO.getId())) {
                String s = Optional.of(stationId)
                        .map(id -> FeignUtils.handleRequest(d -> factoryFeign.queryStationById(d), stationId).getData())
                        .map(item -> item.getCode() + "-" + item.getName())
                        .orElse(null);
                throw new BmosException(MesResponseCode.WEIGH_CENTRE_STATION_BOUNDED, s);
            }
        }
        // 通知平台之前的工位被解绑
        platformUnBindStationIds(stationIds);
        // 通知平台新的工位被绑定
        platformBindStationIds(bindStationDTO.getStationIdList());
        // 保存绑定关系
        weighCentreStationMapper.bind(bindStationDTO.getId(), bindStationDTO.getStationIdList());
    }

    @Override
    public List<WeighCentreCategoryWithCentreVO> weighCentreTree() {
        List<WeighCentreCategory> weighCentreCategories = weighCentreCategoryMapper.selectList();
        List<WeighCentreCategoryWithCentreVO> list = WeighCentreCategoryConvert.INSTANCE.convertToVOWithCentre(weighCentreCategories);

        List<Long> deptIds = platformApiAdaptor.deptIds();
        Map<Long, List<WeighCentre>> map = weighCentreMapper.listAllByDeptIds(deptIds)
                .stream()
                .collect(Collectors.groupingBy(WeighCentre::getCategoryId));
        for (WeighCentreCategoryWithCentreVO category : list) {
            if (map.containsKey(category.getId())) {
                List<WeighCentre> weighCentres = map.get(category.getId());
                category.setChildren(WeighCentreConvert.INSTANCE.convertCategoryNodeVO(weighCentres));
            }
        }
        BmosTreeUtil.buildTree(list, null);
        return list;
    }

    @Override
    public List<Long> selectByStationIds(List<Long> stationIdList) {
        if (CollUtil.isEmpty(stationIdList)){
            return new ArrayList<>();
        }
        List<WeighCentreStation> weighCentreStations = weighCentreStationMapper.queryCentreIdsByStationIds(stationIdList);
        if (CollUtil.isEmpty(weighCentreStations)){
            return new ArrayList<>();
        }
        return weighCentreStations.stream()
                .map(WeighCentreStation::getWeighCentreId)
                .collect(Collectors.toList());
    }

    private void platformUnBindStationIds(List<Long> stationIds) {
        if (CollectionUtil.isEmpty(stationIds)) {
            return;
        }
        Map<Long, Boolean> unbindStationUseMap = new HashMap<>();
        for (Long stationId : stationIds) {
            unbindStationUseMap.put(stationId, false);
        }
        LineUseDTO lineUseDTO = new LineUseDTO();
        lineUseDTO.setStationUseMap(unbindStationUseMap);
        factoryFeign.bindUseCount(lineUseDTO);
    }

    private void platformBindStationIds(List<Long> stationIds) {
        if (CollectionUtil.isEmpty(stationIds)) {
            return;
        }
        Map<Long, Boolean> bindStationUseMap = new HashMap<>();
        for (Long stationId : stationIds) {
            bindStationUseMap.put(stationId, true);
        }
        LineUseDTO lineUseDTO = new LineUseDTO();
        lineUseDTO.setStationUseMap(bindStationUseMap);
        factoryFeign.bindUseCount(lineUseDTO);
    }
}
