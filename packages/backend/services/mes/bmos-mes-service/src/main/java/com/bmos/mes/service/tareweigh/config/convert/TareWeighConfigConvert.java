package com.bmos.mes.service.tareweigh.config.convert;

import cn.hutool.extra.spring.SpringUtil;
import com.bmos.mes.service.tag.vo.TareWeighTag;
import com.bmos.mes.service.tareweigh.config.model.TareWeighConfig;
import com.bmos.mes.service.tareweigh.config.vo.TareWeighConfigVO;
import com.bmos.mes.service.utils.UserUtils;
import com.bmos.mybatis.dataobject.BaseUserDO;
import com.bmos.mybatis.page.CommonPage;
import com.bmos.unit.service.UnitCache;
import com.bmos.unit.vo.CacheUnit;
import org.apache.commons.lang3.StringUtils;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * @author liang
 * @version 1.0.0
 * @date 2024/9/23 10:58
 */
@Mapper
public interface TareWeighConfigConvert {

    TareWeighConfigConvert INSTANCE = Mappers.getMapper(TareWeighConfigConvert.class);

    CommonPage<TareWeighConfigVO> convertToVO(CommonPage<TareWeighConfig> page);

    default TareWeighConfigVO convertToVO(TareWeighConfig tareWeighConfig){
        TareWeighConfigVO result = new TareWeighConfigVO();
        result.setId(tareWeighConfig.getId());
        result.setTareWeigh(tareWeighConfig.getTareWeigh());
        result.setUnit(tareWeighConfig.getUnit());
        result.setUnitId(tareWeighConfig.getUnitId());
        UnitCache unitCache = SpringUtil.getBean(UnitCache.class);
        Long baseUnitId = unitCache.getBaseUnitId(tareWeighConfig.getUnitId());
        CacheUnit basicUnit = unitCache.getGlobalUnit(baseUnitId);
        if (basicUnit != null){
            result.setBasicUnitId(basicUnit.getUnitId());
            result.setBasicUnit(basicUnit.getUnitName());
        }
        result.setDescribeInfo(tareWeighConfig.getDescribeInfo());
        result.setEditorId(tareWeighConfig.getEditorId());
        result.setEditTime(tareWeighConfig.getEditTime());
        if (StringUtils.isNotBlank(tareWeighConfig.getEditorId())){
            BaseUserDO user = UserUtils.getUser(tareWeighConfig.getEditorId());
            if (user != null){
                result.setEditorName(user.getUserName());
                result.setEditorLoginName(user.getLoginName());
            }
        }
        return result;
    }

    TareWeighTag convertToTag(TareWeighConfig tareWeighConfig);
}
