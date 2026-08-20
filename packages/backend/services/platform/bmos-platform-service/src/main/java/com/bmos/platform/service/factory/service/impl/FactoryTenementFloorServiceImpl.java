package com.bmos.platform.service.factory.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.bmos.common.exception.BmosException;
import com.bmos.common.util.id.IdUtils;
import com.bmos.mybatis.page.BasePage;
import com.bmos.platform.common.exception.PlatformResponseCode;
import com.bmos.platform.service.factory.enums.TenementFloorStatusEnums;
import com.bmos.platform.service.factory.mapper.FactoryTenementFloorMapper;
import com.bmos.platform.service.factory.mapper.FactoryTenementMapper;
import com.bmos.platform.service.factory.model.FactoryTenementFloor;
import com.bmos.platform.service.factory.service.TenementFloorService;
import com.bmos.platform.service.factory.service.dto.FactoryTenementFloorDTO;
import com.bmos.platform.service.factory.service.dto.FactoryTenementListQueryDTO;
import com.bmos.platform.service.factory.service.dto.TenementFloorEquipmentStatisticsDTO;
import com.bmos.platform.service.util.PageUtils;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 楼宇楼层表(BpTenementFloor)表服务实现类
 *
 * @author makejava
 * @since 2024-12-30 14:09:26
 */
@Service
public class FactoryTenementFloorServiceImpl implements TenementFloorService {

    @Autowired
    private FactoryTenementFloorMapper factoryTenementFloorMapper;

    @Autowired
    private FactoryTenementMapper factoryTenementMapper;

    @Override
    public void add(FactoryTenementFloorDTO factoryTenementFloorDTO) {
        // 确认楼宇是否存在
        if (factoryTenementMapper.selectById(factoryTenementFloorDTO.getTenementId()) == null) {
            throw new BmosException(PlatformResponseCode.TENEMENT_NOT_EXISTS);
        }
        // 楼层名称和楼层编码均为一
        this.assertNameCodeExits(factoryTenementFloorDTO.getTenementId(), factoryTenementFloorDTO.getName(), factoryTenementFloorDTO.getCode());
        FactoryTenementFloor factoryTenementFloor = BeanUtil.copyProperties(factoryTenementFloorDTO, FactoryTenementFloor.class);
        factoryTenementFloor.setId(IdUtils.getSnowflake());
        factoryTenementFloor.setStatus(TenementFloorStatusEnums.DISABLE);
        factoryTenementFloorMapper.insert(factoryTenementFloor);
    }

    private void assertNameCodeExits(Long id, String name, String code) {
        // 查询数据库是否存在重复数据
        LambdaQueryWrapper<FactoryTenementFloor> ql = new QueryWrapper<FactoryTenementFloor>().lambda();
        ql.eq(FactoryTenementFloor::getCode, code);
        ql.or(e -> e.eq(FactoryTenementFloor::getName, name));
        ql.ne(id != null, FactoryTenementFloor::getId, id);
        if (factoryTenementFloorMapper.exists(ql)) {
            throw new BmosException(PlatformResponseCode.TENEMENT_FLOOR_CODE_OR_NAME_EXISTS);
        }
    }

    @Override
    public List<FactoryTenementFloorDTO> queryByPage(FactoryTenementFloorDTO tenementFloorDTO, BasePage page) {
        PageHelper.startPage(page.getPageNum(), page.getPageSize(), PageUtils.getOrderByOrDefaultByUpdateTimeDesc(page));
        LambdaQueryWrapper<FactoryTenementFloor> ql = new QueryWrapper<FactoryTenementFloor>().lambda();
        ql.eq(FactoryTenementFloor::getDeleted, false);
        ql.eq(tenementFloorDTO.getTenementId() != null, FactoryTenementFloor::getTenementId, tenementFloorDTO.getTenementId());
        ql.like(StrUtil.isNotBlank(tenementFloorDTO.getName()), FactoryTenementFloor::getName, tenementFloorDTO.getName());
        ql.eq(tenementFloorDTO.getStatus() != null, FactoryTenementFloor::getStatus, tenementFloorDTO.getStatus());
        ql.eq(StrUtil.isNotBlank(tenementFloorDTO.getCode()) , FactoryTenementFloor::getCode, tenementFloorDTO.getCode());
        List<FactoryTenementFloor> factoryTenementFloors = factoryTenementFloorMapper.selectList(ql);
        return BeanUtil.copyToList(factoryTenementFloors, FactoryTenementFloorDTO.class);
    }

    @Override
    public void update(FactoryTenementFloorDTO tenementFloorDTO) {
        // 确认楼宇是否存在
        if (factoryTenementMapper.selectById(tenementFloorDTO.getTenementId()) == null) {
            throw new BmosException(PlatformResponseCode.TENEMENT_NOT_EXISTS);
        }
        // 确认要修改的数据是否存在
        FactoryTenementFloor exits = factoryTenementFloorMapper.selectById(tenementFloorDTO.getId());
        if (exits == null){
            throw new BmosException(PlatformResponseCode.TENEMENT_FLOOR_NOT_EXISTS);
        }
        assertNameCodeExits(tenementFloorDTO.getId(), tenementFloorDTO.getName(), tenementFloorDTO.getCode());
        FactoryTenementFloor factoryTenementFloor = BeanUtil.copyProperties(tenementFloorDTO, FactoryTenementFloor.class);
        factoryTenementFloorMapper.updateById(factoryTenementFloor);
    }

    @Override
    public void deleteById(Long id) {
        factoryTenementFloorMapper.deleteById(id);
    }

    @Override
    public void enable(Long id) {
        FactoryTenementFloor factoryTenementFloor = factoryTenementFloorMapper.selectById(id);
        if (factoryTenementFloor == null) {
            throw new BmosException(PlatformResponseCode.TENEMENT_FLOOR_NOT_EXISTS);
        }
        if (factoryTenementFloor.getStatus().equals(TenementFloorStatusEnums.ENABLE)) {
            factoryTenementFloor.setStatus(TenementFloorStatusEnums.DISABLE);
        } else {
            factoryTenementFloor.setStatus(TenementFloorStatusEnums.ENABLE);
        }
        factoryTenementFloorMapper.updateById(factoryTenementFloor);
    }

    /**
     * 查询楼栋楼层列表
     *
     * @param listQueryDTO 查询条件
     * @return
     */
    @Override
    public List<FactoryTenementFloorDTO> list(FactoryTenementListQueryDTO listQueryDTO) {
        LambdaQueryWrapper<FactoryTenementFloor> ql = new QueryWrapper<FactoryTenementFloor>().lambda();
        ql.eq(FactoryTenementFloor::getDeleted, false);
        ql.in(CollectionUtil.isNotEmpty(listQueryDTO.getTenementIds()), FactoryTenementFloor::getTenementId, listQueryDTO.getTenementIds());
        ql.like(StrUtil.isNotBlank(listQueryDTO.getName()), FactoryTenementFloor::getName, listQueryDTO.getName());
        ql.eq(listQueryDTO.getStatus() != null, FactoryTenementFloor::getStatus, listQueryDTO.getStatus());
        ql.eq(listQueryDTO.getCode() != null, FactoryTenementFloor::getCode, listQueryDTO.getCode());
        List<FactoryTenementFloor> factoryTenementFloors = factoryTenementFloorMapper.selectList(ql);
        return BeanUtil.copyToList(factoryTenementFloors, FactoryTenementFloorDTO.class);
    }
}
