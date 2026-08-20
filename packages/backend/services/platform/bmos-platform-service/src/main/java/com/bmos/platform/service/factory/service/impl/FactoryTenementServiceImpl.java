package com.bmos.platform.service.factory.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.IdUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.bmos.common.exception.BmosException;
import com.bmos.platform.common.exception.PlatformResponseCode;
import com.bmos.platform.service.factory.mapper.FactoryTenementMapper;
import com.bmos.platform.service.factory.model.FactoryTenement;
import com.bmos.platform.service.factory.service.FactoryTenementService;
import com.bmos.platform.service.factory.service.dto.FactoryTenementDTO;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Optional;

/**
 * 楼宇(BpFactoryTenement)表服务实现类
 *
 * @author makejava
 * @since 2024-12-30 11:54:59
 */
@Service
public class FactoryTenementServiceImpl implements FactoryTenementService {
    @Resource
    private FactoryTenementMapper factoryTenementMapper;


    @Override
    public void add(FactoryTenementDTO factoryTenementDTO) {
        if (factoryTenementDTO.getParentId() != null && factoryTenementDTO.getParentId() != 0L) {
            throw new BmosException(PlatformResponseCode.TENEMENT_MUST_ONLY_ONE_LEVEL);
        }
        if (factoryTenementDTO.getParentId() == null) {
            factoryTenementDTO.setParentId(0L);
        }
        // 名称编码不能重复
        this.assertRepeat(factoryTenementDTO.getId(), factoryTenementDTO.getCode(), factoryTenementDTO.getName());
        // 保存数据库
        FactoryTenement factoryTenement = BeanUtil.copyProperties(factoryTenementDTO, FactoryTenement.class);
        factoryTenement.setId(IdUtil.getSnowflakeNextId());
        factoryTenementMapper.insert(factoryTenement);
    }

    @Override
    public void update(FactoryTenementDTO factoryTenementDTO) {
        FactoryTenement exits = factoryTenementMapper.selectById(factoryTenementDTO.getId());
        if (exits == null) {
            throw new BmosException(PlatformResponseCode.DETAIL_IS_NULL);
        }
        // 只允许存在一级
        if (factoryTenementDTO.getParentId() != null && factoryTenementDTO.getParentId() != 0L) {
            throw new BmosException(PlatformResponseCode.TENEMENT_MUST_ONLY_ONE_LEVEL);
        }
        if (factoryTenementDTO.getParentId() == null) {
            factoryTenementDTO.setParentId(0L);
        }
        // code和name不能重复
        this.assertRepeat(factoryTenementDTO.getId(), factoryTenementDTO.getCode(), factoryTenementDTO.getName());
        FactoryTenement factoryTenement = BeanUtil.copyProperties(factoryTenementDTO, FactoryTenement.class);
        factoryTenementMapper.updateById(factoryTenement);
    }


    /**
     * 删除楼栋
     * 1. 有子集不能删除
     * 2. 有楼层不能删除
     *
     * @param id
     */
    @Override
    public void delete(Long id) {
        // 查找数据库是否存在子级
        if (factoryTenementMapper.exists(new QueryWrapper<FactoryTenement>().lambda().eq(FactoryTenement::getParentId, id))) {
            throw new BmosException(PlatformResponseCode.TENEMENT_DELETE_HAS_CHILDREN);
        }
        factoryTenementMapper.deleteById(id);
    }

    @Override
    public List<FactoryTenementDTO> listAll() {
        return BeanUtil.copyToList(factoryTenementMapper.selectList(), FactoryTenementDTO.class);
    }

    private void assertRepeat(Long id, String code, String name) {
        // 查询数据库是否存在重复数据
        LambdaQueryWrapper<FactoryTenement> ql = new QueryWrapper<FactoryTenement>().lambda();
        ql.eq(FactoryTenement::getCode, code);
        ql.or(e -> e.eq(FactoryTenement::getName, name));
        ql.ne(id != null, FactoryTenement::getId, id);
        if (factoryTenementMapper.exists(ql)) {
            throw new BmosException(PlatformResponseCode.TENEMENT_CODE_OR_NAME_EXISTS);
        }
    }
}
