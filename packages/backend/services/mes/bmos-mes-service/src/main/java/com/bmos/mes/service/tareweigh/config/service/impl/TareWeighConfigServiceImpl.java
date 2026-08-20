package com.bmos.mes.service.tareweigh.config.service.impl;

import com.bmos.common.exception.BmosException;
import com.bmos.common.holder.SysUserHolder;
import com.bmos.mes.common.exception.MesResponseCode;
import com.bmos.mes.service.tag.dto.ScanTareWeighTagDTO;
import com.bmos.mes.service.tareweigh.config.convert.TareWeighConfigConvert;
import com.bmos.mes.service.tareweigh.config.dto.TareWeighConfigCreateDTO;
import com.bmos.mes.service.tareweigh.config.dto.TareWeighConfigEditDTO;
import com.bmos.mes.service.tareweigh.config.dto.TareWeighConfigQuery;
import com.bmos.mes.service.tareweigh.config.mapper.ITareWeighConfigMapper;
import com.bmos.mes.service.tareweigh.config.model.TareWeighConfig;
import com.bmos.mes.service.tareweigh.config.service.ITareWeighConfigService;
import com.bmos.mes.service.tareweigh.config.vo.TareWeighConfigVO;
import com.bmos.mybatis.page.CommonPage;
import com.bmos.unit.service.UnitCache;
import com.github.pagehelper.PageHelper;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

/**
 * 皮重配置 service impl
 *
 * @author liang
 * @version 1.0.0
 * @date 2024/9/23 10:34
 */
@Service
@Slf4j
public class TareWeighConfigServiceImpl implements ITareWeighConfigService {

    private static final String LOG_PREFIX = "[皮重配置]";

    @Resource
    private ITareWeighConfigMapper tareWeighConfigMapper;

    @Resource
    private UnitCache unitCache;

    @Override
    public CommonPage<TareWeighConfigVO> queryPage(TareWeighConfigQuery query) {
        PageHelper.startPage(query.getPageNum(), query.getPageSize());
        List<TareWeighConfig> list = tareWeighConfigMapper.queryPage(query);
        CommonPage<TareWeighConfig> page = CommonPage.convertPage(list);
        return TareWeighConfigConvert.INSTANCE.convertToVO(page);
    }

    @Nullable
    @Override
    public TareWeighConfigVO queryTareWeighConfigById(Long id) {
        if (id == null){
            return null;
        }
        TareWeighConfig tareWeighConfig = tareWeighConfigMapper.selectById(id);
        if (tareWeighConfig == null){
            return null;
        }
        return TareWeighConfigConvert.INSTANCE.convertToVO(tareWeighConfig);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createTareWeighConfig(TareWeighConfigCreateDTO dto) {
        log.info("{}新增皮重配置:{}", LOG_PREFIX, dto);
        TareWeighConfig tareWeighConfig = new TareWeighConfig();
        tareWeighConfig.setTareWeigh(dto.getTareWeigh().toPlainString());
        tareWeighConfig.setUnitId(dto.getUnitId());
        tareWeighConfig.setUnit(unitCache.getGlobalUnitName(dto.getUnitId()));
        tareWeighConfig.setDescribeInfo(dto.getDescribeInfo());
        tareWeighConfig.setEditorId(SysUserHolder.getUser().getUserId());
        tareWeighConfig.setEditTime(LocalDateTime.now());
        tareWeighConfigMapper.insert(tareWeighConfig);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void editTareWeighConfig(TareWeighConfigEditDTO dto) {
        log.info("{}编辑皮重配置:{}", LOG_PREFIX, dto);
        TareWeighConfig tareWeighConfig = tareWeighConfigMapper.selectById(dto.getId());
        if (tareWeighConfig == null){
            throw new BmosException(MesResponseCode.TARE_WEIGH_CONFIG_NOT_EXIST);
        }
        tareWeighConfig.setTareWeigh(dto.getTareWeigh().toPlainString());
        tareWeighConfig.setDescribeInfo(dto.getDescribeInfo());
        tareWeighConfig.setUnitId(dto.getUnitId());
        tareWeighConfig.setUnit(unitCache.getGlobalUnitName(dto.getUnitId()));
        tareWeighConfig.setEditorId(SysUserHolder.getUser().getUserId());
        tareWeighConfig.setEditTime(LocalDateTime.now());
        tareWeighConfigMapper.updateById(tareWeighConfig);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteTareWeighConfig(Long id) {
        log.info("{}删除皮重配置:{}", LOG_PREFIX, id);
        TareWeighConfig tareWeighConfig = tareWeighConfigMapper.selectById(id);
        if (tareWeighConfig == null){
            throw new BmosException(MesResponseCode.TARE_WEIGH_CONFIG_NOT_EXIST);
        }
        tareWeighConfigMapper.deleteById(id);
    }

    @Override
    public TareWeighConfigVO scanTareWeighTag(ScanTareWeighTagDTO dto) {
        TareWeighConfigVO tareWeighConfigVO = queryTareWeighConfigById(dto.getTareWeighId());
        if (tareWeighConfigVO == null){
            throw new BmosException(MesResponseCode.TARE_WEIGH_CONFIG_NOT_EXIST);
        }
        Long tagBaseUnitId = unitCache.getBaseUnitId(dto.getUnitId());
        Long tareWeighBaseUnitId = unitCache.getBaseUnitId(tareWeighConfigVO.getUnitId());
        if (!Objects.equals(tagBaseUnitId, tareWeighBaseUnitId)){
            throw new BmosException(MesResponseCode.TARE_WEIGH_UNIT_CONVERT_ERROR);
        }
        return tareWeighConfigVO;
    }
}
