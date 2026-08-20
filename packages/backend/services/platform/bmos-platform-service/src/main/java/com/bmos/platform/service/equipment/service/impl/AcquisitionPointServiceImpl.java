package com.bmos.platform.service.equipment.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bmos.common.exception.BmosException;
import com.bmos.common.util.id.IdUtils;
import com.bmos.mybatis.page.CommonPage;
import com.bmos.mybatis.query.LambdaQueryWrapperX;
import com.bmos.platform.common.enums.equipment.PropertyTypeEnum;
import com.bmos.platform.common.exception.PlatformResponseCode;
import com.bmos.platform.service.dict.service.DictService;
import com.bmos.platform.service.dict.vo.DictVO;
import com.bmos.platform.service.equipment.enums.AcquisitionPlatformEnum;
import com.bmos.platform.service.equipment.mapper.AcquisitionPointMapper;
import com.bmos.platform.service.equipment.mapper.EquipmentInfoMapper;
import com.bmos.platform.service.equipment.mapper.EquipmentPropertyInfoMapper;
import com.bmos.platform.service.equipment.model.AcquisitionPoint;
import com.bmos.platform.service.equipment.model.EquipmentInfo;
import com.bmos.platform.service.equipment.model.EquipmentPropertyInfo;
import com.bmos.platform.service.equipment.service.AcquisitionPointService;
import com.bmos.platform.service.equipment.service.dto.AcquisitionPointDTO;
import com.bmos.platform.service.equipment.service.dto.AcquisitionPointPageQueryDTO;
import com.bmos.platform.service.equipment.service.enums.AcquisitionPointStatusEnum;
import com.github.pagehelper.PageHelper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 采集点数据表(AcquisitionPoint)表服务实现类
 *
 * @author makejava
 * @since 2024-04-19 15:17:52
 */
@Service("acquisitionPointService")
public class AcquisitionPointServiceImpl extends ServiceImpl<AcquisitionPointMapper, AcquisitionPoint> implements AcquisitionPointService {
    @Resource
    private AcquisitionPointMapper acquisitionPointDao;

    @Resource
    private DictService dictService;

    @Resource
    private EquipmentPropertyInfoMapper equipmentPropertyInfoDao;

    @Resource
    private EquipmentInfoMapper equipmentInfoMapper;

    private static final String EQUIPMENT_DATA_DICT_CODE = "DeviceDataFields";

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    @Override
    public AcquisitionPoint queryById(Long id) {
        return this.acquisitionPointDao.selectById(id);
    }


    /**
     * 新增数据
     *
     * @param acquisitionPointDTO 实例对象
     * @return 实例对象
     */
    @Override
    public void add(AcquisitionPointDTO acquisitionPointDTO) {
        // 编码名称不能重复
        this.assertRepeat(acquisitionPointDTO);
        acquisitionPointDTO.setId(IdUtils.getSnowflake());
        acquisitionPointDTO.setStatus(AcquisitionPointStatusEnum.DISABLE);
        AcquisitionPoint acquisitionPoint = BeanUtil.toBean(acquisitionPointDTO, AcquisitionPoint.class);
        acquisitionPoint.setStatus(AcquisitionPointStatusEnum.DISABLE);
        this.acquisitionPointDao.insert(acquisitionPoint);
    }

    private void assertRepeat(AcquisitionPointDTO acquisitionPointDTO) {
        if (this.repeatCode(acquisitionPointDTO)) {
            throw new BmosException(PlatformResponseCode.EQUIPMENT_ACQUISITION_POINT_CODE_REPEAT,
                    acquisitionPointDTO.getCode());
        }
        if (this.repeatName(acquisitionPointDTO)) {
            throw new BmosException(PlatformResponseCode.EQUIPMENT_ACQUISITION_POINT_NAME_REPEAT,
                    acquisitionPointDTO.getName());
        }
    }

    private boolean repeatName(AcquisitionPointDTO acquisitionPointDTO) {
        LambdaQueryWrapper<AcquisitionPoint> lambda = new QueryWrapper<AcquisitionPoint>().lambda();
        lambda.eq(AcquisitionPoint::getName, acquisitionPointDTO.getName()).ne(acquisitionPointDTO.getId() != null,
                AcquisitionPoint::getId,
                acquisitionPointDTO.getId());
        return getBaseMapper().exists(lambda);
    }

    private boolean repeatCode(AcquisitionPointDTO acquisitionPointDTO) {
        LambdaQueryWrapper<AcquisitionPoint> lambda = new QueryWrapper<AcquisitionPoint>().lambda();
        lambda.eq(AcquisitionPoint::getCode, acquisitionPointDTO.getCode()).ne(acquisitionPointDTO.getId() != null,
                AcquisitionPoint::getId,
                acquisitionPointDTO.getId());
        return getBaseMapper().exists(lambda);
    }

    /**
     * 修改数据
     *
     * @param acquisitionPointDTO 实例对象
     * @return 实例对象
     */
    @Override
    public void update(AcquisitionPointDTO acquisitionPointDTO) {
        // 校验状态是否正确，停用状态才能编辑
        this.assertRepeat(acquisitionPointDTO);
        AcquisitionPoint acquisitionPoint = this.assertExits(acquisitionPointDTO);
        if (acquisitionPoint.getStatus() != AcquisitionPointStatusEnum.DISABLE) {
            throw new BmosException(PlatformResponseCode.EQUIPMENT_ACQUISITION_POINT_EDIT_STATUS_ERROR);
        }
        AcquisitionPoint update = BeanUtil.toBean(acquisitionPointDTO, AcquisitionPoint.class);
        this.acquisitionPointDao.updateById(update);
    }


    private AcquisitionPoint assertExits(AcquisitionPointDTO acquisitionPointDTO) {
        AcquisitionPoint byId = this.getById(acquisitionPointDTO.getId());
        if (byId == null) {
            throw new BmosException(PlatformResponseCode.EQUIPMENT_ACQUISITION_POINT_NOT_EXITS);
        }
        return byId;
    }

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    @Override
    public boolean deleteById(Long id) {
        return this.acquisitionPointDao.deleteById(id) > 0;
    }


    /**
     * 通过id批量删除
     *
     * @param ids id集合
     */
    @Override
    public void deleteByIds(List<Long> ids) {
        this.acquisitionPointDao.deleteBatchIds(ids);
    }

    /**
     * 分页查询
     *
     * @param acquisitionPointPageQueryDTO 查询条件
     * @return 查询结果
     */
    @Override
    public CommonPage<AcquisitionPointDTO> page(AcquisitionPointPageQueryDTO acquisitionPointPageQueryDTO) {
        LambdaQueryWrapper<AcquisitionPoint> lambda = new QueryWrapper<AcquisitionPoint>().lambda();
        lambda.eq(StringUtils.isNotBlank(acquisitionPointPageQueryDTO.getCode()), AcquisitionPoint::getCode,
                acquisitionPointPageQueryDTO.getCode());
        lambda.like(StringUtils.isNotBlank(acquisitionPointPageQueryDTO.getName()), AcquisitionPoint::getName,
                acquisitionPointPageQueryDTO.getName());
        lambda.eq(acquisitionPointPageQueryDTO.getStatus() != null, AcquisitionPoint::getStatus,
                acquisitionPointPageQueryDTO.getStatus());
        lambda.eq(acquisitionPointPageQueryDTO.getType() != null, AcquisitionPoint::getType,
                acquisitionPointPageQueryDTO.getType());
        PageHelper.startPage(acquisitionPointPageQueryDTO.getPageNum(), acquisitionPointPageQueryDTO.getPageSize(),
                acquisitionPointPageQueryDTO.getOrderSql());
        List<AcquisitionPoint> pageRes = getBaseMapper().selectList(lambda);
        return CommonPage.convertPage(pageRes, res -> BeanUtil.copyToList(res, AcquisitionPointDTO.class));
    }

    @Override
    public void enable(List<Long> ids) {
        List<AcquisitionPoint> acquisitionPoints = this.listByIds(ids);
        List<AcquisitionPoint> statusError =
                acquisitionPoints.stream().filter(item -> item.getStatus() != AcquisitionPointStatusEnum.DISABLE).collect(Collectors.toList());
        if (!CollectionUtils.isEmpty(statusError)) {
            throw new BmosException(PlatformResponseCode.EQUIPMENT_ACQUISITION_POINT_ENABLE_STATUS_ERROR,
                    statusError.stream().map(AcquisitionPoint::getCode).collect(Collectors.joining(",")));
        }
        LambdaUpdateWrapper<AcquisitionPoint> lambda = new UpdateWrapper<AcquisitionPoint>().lambda();
        lambda.in(AcquisitionPoint::getId, ids).set(AcquisitionPoint::getStatus, AcquisitionPointStatusEnum.ENABLE);
        this.update(lambda);
    }

    @Override
    public void disable(List<Long> ids) {
        List<AcquisitionPoint> acquisitionPoints = this.listByIds(ids);
        if (CollectionUtils.isEmpty(acquisitionPoints)) {
            return;
        }
        List<AcquisitionPoint> statusError =
                acquisitionPoints.stream().filter(item -> item.getStatus() != AcquisitionPointStatusEnum.ENABLE).collect(Collectors.toList());
        if (!CollectionUtils.isEmpty(statusError)) {
            throw new BmosException(PlatformResponseCode.EQUIPMENT_ACQUISITION_POINT_DISABLE_STATUS_ERROR,
                    statusError.stream().map(AcquisitionPoint::getCode).collect(Collectors.joining(",")));
        }
        LambdaUpdateWrapper<AcquisitionPoint> lambda = new UpdateWrapper<AcquisitionPoint>().lambda();
        lambda.in(AcquisitionPoint::getId, ids).set(AcquisitionPoint::getStatus, AcquisitionPointStatusEnum.DISABLE);
        this.update(lambda);
    }

    @Override
    public List<AcquisitionPointDTO> getList(List<Long> acquisitionIds) {
        return BeanUtil.copyToList(this.listByIds(acquisitionIds), AcquisitionPointDTO.class);
    }

    @Override
    public List<AcquisitionPointDTO> listAcquisition() {
        List<AcquisitionPoint> acquisitionPointList = acquisitionPointDao.selectList(
                new LambdaQueryWrapperX<AcquisitionPoint>()
                        .eq(AcquisitionPoint::getStatus, AcquisitionPointStatusEnum.ENABLE.getValue())
                        .orderByAsc(AcquisitionPoint::getName)
        );
        return BeanUtil.copyToList(acquisitionPointList, AcquisitionPointDTO.class);
    }


    /**
     * 采集点关联设备数据
     *
     * @param acquisitionPointList 采集点id列表
     * @param equipmentTagDataCode 设备数据code
     */
    @Override
    public void bindEquipmentData(List<Long> acquisitionPointList, String equipmentTagDataCode) {
        // 校验采集点是不是已经和设备匹配
        if (CollectionUtils.isEmpty(acquisitionPointList)) {
            return;
        }
        LambdaQueryWrapper<EquipmentPropertyInfo> lambda = new QueryWrapper<EquipmentPropertyInfo>().lambda();
        lambda.in(EquipmentPropertyInfo::getValue, acquisitionPointList.stream().map(String::valueOf).collect(Collectors.toList()));
        lambda.eq(EquipmentPropertyInfo::getPropertyType, PropertyTypeEnum.TAG_DATA_PROPERTY.getCode());
        lambda.ne(EquipmentPropertyInfo::getPropertyCode, equipmentTagDataCode);
        lambda.eq(EquipmentPropertyInfo::getDeleted, false);
        List<EquipmentPropertyInfo> equipmentPropertyInfos = equipmentPropertyInfoDao.selectList(lambda);
        if (!CollectionUtils.isEmpty(equipmentPropertyInfos)) {
            List<EquipmentInfo> equipmentInfos = equipmentInfoMapper.selectBatchIds(equipmentPropertyInfos.stream().map(EquipmentPropertyInfo::getEquipmentId).collect(Collectors.toList()));
            List<Long> acquisitionId = equipmentPropertyInfos.stream().filter(item -> item.getValue() != null).map(item -> Long.valueOf(item.getValue())).collect(Collectors.toList());
            List<AcquisitionPoint> acquisitionPoints = this.listByIds(acquisitionId);
            Map<Long, String> acquisitionMap = acquisitionPoints.stream().collect(Collectors.toMap(AcquisitionPoint::getId, AcquisitionPoint::getCode));
            Map<String, List<EquipmentPropertyInfo>> stringListMap = equipmentPropertyInfos.stream().collect(Collectors.groupingBy(EquipmentPropertyInfo::getValue));
            StringBuilder stringBuilder = new StringBuilder();
            for (Map.Entry<String, List<EquipmentPropertyInfo>> entry : stringListMap.entrySet()) {
                String key = entry.getKey();
                String acquisitionCode = acquisitionMap.get(Long.valueOf(key));
                if (acquisitionCode == null) {
                    continue;
                }
                List<EquipmentPropertyInfo> value = entry.getValue();
                Set<Long> equipmnents = value.stream().map(EquipmentPropertyInfo::getEquipmentId).collect(Collectors.toSet());
                String equipmentCode = equipmentInfos.stream().filter(item -> equipmnents.contains(item.getId())).map(EquipmentInfo::getCode).collect(Collectors.joining("，"));
                stringBuilder.append(acquisitionCode).append("：").append(equipmentCode).append("；");
            }
            // 去除最后一个逗号
            if (stringBuilder.length() > 0) {
                stringBuilder.deleteCharAt(stringBuilder.length() - 1);
            }
            throw new BmosException(PlatformResponseCode.EQUIPMENT_ACQUISITION_POINT_HAS_BIND, stringBuilder.toString());
        }
        LambdaUpdateWrapper<AcquisitionPoint> ul = new UpdateWrapper<AcquisitionPoint>().lambda();
        ul.in(AcquisitionPoint::getId, acquisitionPointList)
                .set(AcquisitionPoint::getEquipmentTagDataCode, equipmentTagDataCode);
        this.update(ul);
    }


    /**
     * 通过特定设备数据code集合查询可用的采集点
     *
     * @param codeSet             设备数据code集合
     * @param acquisitionPlatform
     * @return 查询结果
     */
    @Override
    public List<AcquisitionPointDTO> listEnableByEquipmentDataProperty(Set<String> codeSet, AcquisitionPlatformEnum acquisitionPlatform) {
        LambdaQueryWrapper<AcquisitionPoint> ql = new QueryWrapper<AcquisitionPoint>().lambda();
        ql.eq(AcquisitionPoint::getStatus, AcquisitionPointStatusEnum.ENABLE.getValue());
        ql.eq(acquisitionPlatform != null, AcquisitionPoint::getAcquisitionPlatform, acquisitionPlatform);
        ql.and(l -> l.in(!CollectionUtils.isEmpty(codeSet), AcquisitionPoint::getEquipmentTagDataCode, codeSet)
                .or()
                .isNull(AcquisitionPoint::getEquipmentTagDataCode));
        List<AcquisitionPoint> list = list(ql);
        return BeanUtil.copyToList(list, AcquisitionPointDTO.class);
    }


    /**
     * 绑定设备数据
     *
     * @param dataPointEquipmentDataMap 采集点id、设备数据code
     */
    @Override
    public void bindEquipmentData(Map<Long, String> dataPointEquipmentDataMap) {
        Set<Long> ids = dataPointEquipmentDataMap.keySet();
        List<AcquisitionPoint> acquisitionPoints = this.listByIds(ids);
        List<DictVO> dictVOS = dictService.queryDictDetailByCode(EQUIPMENT_DATA_DICT_CODE);
        Map<String, String> dictMap = dictVOS.stream().collect(Collectors.toMap(DictVO::getValue, DictVO::getLabel));
        for (AcquisitionPoint acquisitionPoint : acquisitionPoints) {
            if (StringUtils.isNotEmpty(acquisitionPoint.getEquipmentTagDataCode()) && !acquisitionPoint.getEquipmentTagDataCode().equals(dataPointEquipmentDataMap.get(acquisitionPoint.getId()))) {
                String label = dictMap.get(acquisitionPoint.getEquipmentTagDataCode());
                throw new BmosException(PlatformResponseCode.EQUIPMENT_ACQUISITION_POINT_BIND_ERROR,
                        acquisitionPoint.getCode(), label + "-" + acquisitionPoint.getEquipmentTagDataCode());
            }
            acquisitionPoint.setEquipmentTagDataCode(dataPointEquipmentDataMap.get(acquisitionPoint.getId()));
        }
        this.updateBatchById(acquisitionPoints);
    }
}
