package com.bmos.lims2.server.inspect.pack.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.bmos.common.exception.BmosException;
import com.bmos.lims2.common.constants.BasicConstants;
import com.bmos.lims2.common.i18n.LimsResponseCode;
import com.bmos.lims2.server.inspect.convert.InspectPackageConvert;
import com.bmos.lims2.server.inspect.item.dto.InspectItemDTO;
import com.bmos.lims2.server.inspect.item.entity.InspectPackageItem;
import com.bmos.lims2.server.inspect.item.mapper.InspectPackageItemMapper;
import com.bmos.lims2.server.inspect.item.service.InspectItemService;
import com.bmos.lims2.server.inspect.pack.dto.*;
import com.bmos.lims2.server.inspect.pack.entity.InspectPackage;
import com.bmos.lims2.server.inspect.pack.mapper.InspectPackageMapper;
import com.bmos.lims2.server.inspect.pack.service.InspectPackageService;
import com.bmos.lims2.server.inspect.parameter.entity.InspectParameterOption;
import com.bmos.lims2.server.inspect.parameter.entity.InspectParameterTrend;
import com.bmos.lims2.server.inspect.parameter.mapper.InspectParameterOptionMapper;
import com.bmos.lims2.server.inspect.parameter.mapper.InspectParameterTrendMapper;
import com.bmos.lims2.server.inspect.scheme.mapper.InspectionSchemeMapper;
import com.bmos.mybatis.page.CommonPage;
import com.github.pagehelper.PageHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class InspectPackageServiceImpl implements InspectPackageService {

    @Autowired
    InspectPackageMapper packageMapper;

    @Autowired
    InspectPackageItemMapper inspectPackageItemMapper;

    @Autowired
    InspectItemService inspectItemService;

    @Autowired
    private InspectParameterOptionMapper optionMapper;
    @Autowired
    private InspectParameterTrendMapper trendMapper;

    @Autowired
    private InspectionSchemeMapper inspectionSchemeMapper;


    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<Long> savePackage(PackageCreateReqDTO reqVO) {
        List<Long> dubInspectIdList = validSaveReq(reqVO);
        if (CollectionUtil.isNotEmpty(dubInspectIdList)) {
            return dubInspectIdList;
        }
        doSavePackage(reqVO);
        return dubInspectIdList;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deletePackage(Long id) {
        validDeleteReq(id);
        doDelete(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<Long> updatePackage(PackageUpdateReqDTO packageUpdateReqDTO) {
        // 校验重复
        List<Long> dubInspectIdList = validDubInspect(packageUpdateReqDTO.getPackageItemList());
        if (CollectionUtil.isNotEmpty(dubInspectIdList)) {
            return dubInspectIdList;
        }
        // 校验实验包是否存在
        InspectPackage packageDO = packageMapper.selectById(packageUpdateReqDTO.getId());
        if (Objects.isNull(packageDO)) {
            throw new BmosException(LimsResponseCode.CHOOSE_NOT_EXIST);
        }
        // 更新到数据苦衷
        doUpdate(packageUpdateReqDTO, packageDO);
        return dubInspectIdList;
    }

    @Override
    public CommonPage<InspectPackageDTO> packagePage(PackagePageReqDTO packagePageReqDTO) {
        PageHelper.startPage(packagePageReqDTO.getPageNum(), packagePageReqDTO.getPageSize(), BasicConstants.DEFAULT_SORTING_FIELD);
        PackageParamDTO param = InspectPackageConvert.INSTANCE.convert2Param(packagePageReqDTO);
        List<InspectPackage> packageDOList = packageMapper.selectByParam(param);
        CommonPage<InspectPackage> packageCommonPage = CommonPage.convertPage(packageDOList);
        return InspectPackageConvert.INSTANCE.convert2PackagePageRespVO(packageCommonPage);
    }

    @Override
    public InspectPackageWithItemDTO packageInfo(Long id) {
        // 校验实验包是否存在
        InspectPackage packageDO = packageMapper.selectById(id);
        if (Objects.isNull(packageDO)) {
            throw new BmosException(LimsResponseCode.CHOOSE_NOT_EXIST);
        }
        InspectPackageWithItemDTO respVO = InspectPackageConvert.INSTANCE.convert2PackageInfoVO(packageDO);
        List<InspectPackageItemDTO> packageInspectVOList = packageInspectInfo(id);
        respVO.setPackageItemList(packageInspectVOList);
        return respVO;
    }

    @Override
    public List<InspectPackageItemDTO> packageInspectInfo(Long id) {
        List<InspectPackageItemDTO> packageWithItemDTOS = new ArrayList<>();
        List<InspectPackageItem> inspectPackageItems = inspectPackageItemMapper.selectByPackageId(id);
        if (CollectionUtil.isEmpty(inspectPackageItems)) {
            return packageWithItemDTOS;
        }
        List<Long> inspectIdList = inspectPackageItems.stream().map(InspectPackageItem::getInspectItemId).collect(Collectors.toList());
        List<InspectItemDTO> inspectItemDTOS = inspectItemService.selectByIdList(inspectIdList);
        for (InspectItemDTO inspectItemDTO : inspectItemDTOS) {
            InspectPackageItemDTO packageItemDTO = InspectPackageConvert.INSTANCE.convert2PackageInspectVO(inspectItemDTO);
            packageItemDTO.setInspectItemId(inspectItemDTO.getId());
            packageWithItemDTOS.add(packageItemDTO);
        }
        return packageWithItemDTOS;
    }

    @Override
    public List<InspectPackageDTO> selectByIdList(List<Long> packageIdList) {
        return BeanUtil.copyToList(packageMapper.selectBatchIds(packageIdList), InspectPackageDTO.class);
    }

    @Override
    public List<InspectPackageListDTO> getList() {
        LambdaQueryWrapper<InspectPackage> wrapper = new LambdaQueryWrapper<>();
        wrapper.select(InspectPackage::getId, InspectPackage::getCode, InspectPackage::getName)
                .eq(InspectPackage::getDeleted, false)
                .orderByDesc(InspectPackage::getCreateTime);

        List<InspectPackage> entityList = packageMapper.selectList(wrapper);
        return BeanUtil.copyToList(entityList, InspectPackageListDTO.class);
    }

    @Override
    public CommonPage<InspectPackageWithItemsDTO> packagePageWithItems(PackagePageReqDTO packagePageReqDTO) {
        // 第一步：仅分页查询主表ID
        PageHelper.startPage(packagePageReqDTO.getPageNum(), packagePageReqDTO.getPageSize());
        PackageParamDTO param = InspectPackageConvert.INSTANCE.convert2Param(packagePageReqDTO);
        List<Long> ids = packageMapper.selectIdsByParam(param);
        CommonPage<Long> idPage = CommonPage.convertPage(ids);
        if (CollectionUtil.isEmpty(idPage.getList())) {
            return CommonPage.CommonPage(Collections.emptyList(), 0L, packagePageReqDTO);
        }

        // 第二步：按ID集合查询详情（包含关联项及聚合）
        List<InspectPackageWithItemsDTO> details = packageMapper.selectWithItemsByIds(idPage.getList());
        return CommonPage.CommonPage(details, Long.valueOf(idPage.getTotal()), packagePageReqDTO);
    }

    /**
     * 删除实验包
     *
     * @param id
     */
    private void doDelete(Long id) {
        packageMapper.deleteById(id);
        inspectPackageItemMapper.deleteByPackageId(id);
    }

    /**
     * 校验删除实验包请求参数
     *
     * @param id
     */
    private void validDeleteReq(Long id) {
        if (!packageMapper.existById(id)) {
            throw new BmosException(LimsResponseCode.CHOOSE_NOT_EXIST);
        }
        // 绑定方案校验：若已被方案绑定，禁止删除，并提示方案名称
        List<String> boundSchemeNames = inspectionSchemeMapper.selectSchemeNamesByPackageId(id);
        if (CollectionUtil.isNotEmpty(boundSchemeNames)) {
            String joinedNames = String.join("，", boundSchemeNames);
            // 使用占位符消息常量，并传递参数
            throw new BmosException(LimsResponseCode.PACKAGE_BIND_SCHEMES, joinedNames);
        }
    }

    /**
     * 保存实验包
     *
     * @param packageCreateReqDTO
     */
    private void doSavePackage(PackageCreateReqDTO packageCreateReqDTO) {
        InspectPackage packageDO = InspectPackageConvert.INSTANCE.convert2DO(packageCreateReqDTO);
        packageMapper.insert(packageDO);
        if (CollectionUtil.isEmpty(packageCreateReqDTO.getPackageItemList())) {
            return;
        }
        List<InspectPackageItem> packageInspectDOList = new ArrayList<>();
        for (InspectPackageItemDTO inspectPackageItemDTO : packageCreateReqDTO.getPackageItemList()) {
            InspectPackageItem packageInspectDO = InspectPackageConvert.INSTANCE.convert2PackageInspectDO(inspectPackageItemDTO);
            packageInspectDO.setInspectPackageId(packageDO.getId());
            packageInspectDOList.add(packageInspectDO);
        }
        inspectPackageItemMapper.insertBatch(packageInspectDOList);
    }

    /**
     * 编辑实验包信息 包含实验包与检验项目的关联关系
     *
     * @param reqVO
     * @param packageDO
     */
    private void doUpdate(PackageUpdateReqDTO reqVO, InspectPackage packageDO) {
        packageDO.setName(reqVO.getName());
        packageDO.setRemark(reqVO.getRemark());
        packageMapper.updateById(packageDO);

        // 构建新的关联关系
        List<InspectPackageItem> odlPackageItems = inspectPackageItemMapper.selectByPackageId(packageDO.getId());
        Set<Long> curInspectIdSet = reqVO.getPackageItemList().stream().map(InspectPackageItemDTO::getInspectItemId).collect(Collectors.toSet());
        // 需要解除绑定的
        List<InspectPackageItem> deletePackageItemList = new ArrayList<>();
        Map<Long, InspectPackageItem> oldPackageInspectMap = new HashMap<>();
        if (CollectionUtil.isNotEmpty(odlPackageItems)) {
            odlPackageItems.forEach(packageInspectDO -> {
                oldPackageInspectMap.put(packageInspectDO.getInspectPackageId(), packageInspectDO);
                if (!curInspectIdSet.contains(packageInspectDO.getInspectItemId())) {
                    deletePackageItemList.add(packageInspectDO);
                }
            });
        }
        // 需要新增的关联关系
        List<InspectPackageItem> inspectPackageItems = new ArrayList<>();
        for (InspectPackageItemDTO inspectPackageItemDTO : reqVO.getPackageItemList()) {
            if (!oldPackageInspectMap.containsKey(inspectPackageItemDTO.getInspectItemId())) {
                InspectPackageItem packageInspectDO = InspectPackageConvert.INSTANCE.convert2PackageInspectDO(inspectPackageItemDTO);
                packageInspectDO.setInspectPackageId(packageDO.getId());
                inspectPackageItems.add(packageInspectDO);
            }
        }
        if (CollectionUtil.isNotEmpty(deletePackageItemList)) {
            inspectPackageItemMapper.deleteBatchIds(deletePackageItemList);
        }
        if (CollectionUtil.isNotEmpty(inspectPackageItems)) {
            inspectPackageItemMapper.insertBatch(inspectPackageItems);
        }
    }

    /**
     * 校验实验包保存参数
     * 1. 编码唯一
     * 2. 实验包下的检验项目不能重复
     *
     * @param reqVO
     * @return
     */
    private List<Long> validSaveReq(PackageCreateReqDTO reqVO) {
        if (packageMapper.existByCode(reqVO.getCode())) {
            throw new BmosException(LimsResponseCode.CODE_EXISTED);
        }
        return validDubInspect(reqVO.getPackageItemList());
    }

    /**
     * 校验实验包下的检品项目是否重复
     *
     * @param inspectPackageItemDTOS
     * @return
     */
    private List<Long> validDubInspect(List<InspectPackageItemDTO> inspectPackageItemDTOS) {
        List<Long> dubInspectIdList = new ArrayList<>();
        if (CollectionUtil.isEmpty(inspectPackageItemDTOS)) {
            return dubInspectIdList;
        }
        Set<Long> inspectIdSet = new HashSet<>();
        for (InspectPackageItemDTO inspectPackageItemDTO : inspectPackageItemDTOS) {
            if (inspectIdSet.contains(inspectPackageItemDTO.getInspectItemId())) {
                dubInspectIdList.add(inspectPackageItemDTO.getInspectItemId());
            } else {
                inspectIdSet.add(inspectPackageItemDTO.getInspectItemId());
            }
        }
        return dubInspectIdList;
    }

    @Override
    public InspectPackageFullConfigDTO getFullConfigByPackageId(Long packageId) {
        log.info("根据实验包ID查询完整配置信息, packageId: {}", packageId);

        // 验证实验包是否存在
        if (!packageMapper.existById(packageId)) {
            throw new BmosException(LimsResponseCode.DATA_NOT_EXISTS);
        }

        // 查询完整配置信息
        InspectPackageFullConfigDTO fullConfig = packageMapper.selectFullConfigByPackageId(packageId);

        // 查询数据点关联的选项和趋势线配置信息
        if (fullConfig != null && CollectionUtil.isNotEmpty(fullConfig.getInspectionItems())) {
            for (InspectPackageFullConfigDTO.InspectionItemDTO inspectionItemDTO : fullConfig.getInspectionItems()) {
                if (CollectionUtil.isNotEmpty(inspectionItemDTO.getInspectionParameters())) {
                    for (InspectPackageFullConfigDTO.AnalysisItemDTO analysisItemDTO : inspectionItemDTO.getInspectionParameters()) {
                        if (CollectionUtil.isNotEmpty(analysisItemDTO.getDataPoints())) {
                            for (InspectPackageFullConfigDTO.DataPointDTO dataPointDTO : analysisItemDTO.getDataPoints()) {
                                List<InspectParameterOption> inspectParameterOptions = optionMapper.selectByDataPointId(dataPointDTO.getDataPointId());
                                if (CollectionUtil.isNotEmpty(inspectParameterOptions)) {
                                    List<InspectPackageFullConfigDTO.OptionDTO> optionDTOS = inspectParameterOptions.stream().map(inspectParameterOption -> {
                                        InspectPackageFullConfigDTO.OptionDTO optionDTO = new InspectPackageFullConfigDTO.OptionDTO();
                                        optionDTO.setDataPointId(inspectParameterOption.getDataPointId());
                                        optionDTO.setParameterId(analysisItemDTO.getParameterId());
                                        optionDTO.setOptionValue(inspectParameterOption.getOptionValue());
                                        return optionDTO;
                                    }).collect(Collectors.toList());
                                    dataPointDTO.setOptions(optionDTOS);
                                }
                                List<InspectParameterTrend> inspectParameterTrends = trendMapper.selectByDataPointId(dataPointDTO.getDataPointId());
                                if (CollectionUtil.isNotEmpty(inspectParameterTrends)) {
                                    List<InspectPackageFullConfigDTO.TrendDTO> trendDTOS = inspectParameterTrends.stream().map(inspectParameterTrend -> {
                                        InspectPackageFullConfigDTO.TrendDTO trendDTO = new InspectPackageFullConfigDTO.TrendDTO();
                                        trendDTO.setDataPointId(inspectParameterTrend.getDataPointId());
                                        trendDTO.setParameterId(analysisItemDTO.getParameterId());
                                        trendDTO.setRangeName(inspectParameterTrend.getRangeName());
                                        trendDTO.setMinValue(inspectParameterTrend.getMinValue());
                                        trendDTO.setMaxValue(inspectParameterTrend.getMaxValue());
                                        trendDTO.setMinOperator(inspectParameterTrend.getMinOperator());
                                        trendDTO.setMaxOperator(inspectParameterTrend.getMaxOperator());
                                        return trendDTO;
                                    }).collect(Collectors.toList());
                                    dataPointDTO.setTrends(trendDTOS);
                                }
                            }
                        }
                    }
                }
            }
        }

        if (fullConfig == null) {
            // 如果没有配置信息，返回基础实验包信息
            InspectPackage inspectPackage = packageMapper.selectById(packageId);
            if (inspectPackage != null) {
                fullConfig = new InspectPackageFullConfigDTO();
                fullConfig.setPackageId(inspectPackage.getId());
                fullConfig.setPackageCode(inspectPackage.getCode());
                fullConfig.setPackageName(inspectPackage.getName());
                fullConfig.setRemark(inspectPackage.getRemark());
                fullConfig.setInspectionItems(new ArrayList<>());
            }
        }

        log.info("查询完成，实验包: {}, 检项数量: {}",
                fullConfig != null ? fullConfig.getPackageName() : "未找到",
                fullConfig != null && fullConfig.getInspectionItems() != null ? fullConfig.getInspectionItems().size() : 0);

        return fullConfig;
    }
}
