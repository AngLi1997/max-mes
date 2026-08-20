package com.bmos.mes.service.weigh.data.convert;

import cn.hutool.extra.spring.SpringUtil;
import com.bmos.mes.service.components.comps.WeighDataComponentsFromDataOPT;
import com.bmos.mes.service.utils.UserUtils;
import com.bmos.mes.service.weigh.data.entity.WeighDataDO;
import com.bmos.mes.service.weigh.data.vo.WeighDataVO;
import com.bmos.mybatis.dataobject.BaseUserDO;
import com.bmos.unit.service.UnitCache;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author liang
 * @version 1.0.0
 * @date 2024/11/13 09:25
 */
@Mapper
public interface WeighDataConvert {
    WeighDataConvert INSTANCE = Mappers.getMapper(WeighDataConvert.class);

    default WeighDataComponentsFromDataOPT convertToOPT(WeighDataDO weighDataDO){
        if (weighDataDO == null){
            return null;
        }
        WeighDataComponentsFromDataOPT opt = new WeighDataComponentsFromDataOPT();
        opt.setWeight(weighDataDO.getWeight());
        opt.setUnit(SpringUtil.getBean(UnitCache.class).getGlobalUnitName(weighDataDO.getUnitId()));
        BaseUserDO weigher = UserUtils.getUser(weighDataDO.getWeigherId());
        if (weigher != null){
            opt.setWeigher(weigher.getUserName() + "-" + weigher.getLoginName());
        }
        opt.setWeighTime(Optional.ofNullable(weighDataDO.getWeighTime()).map(item -> item.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))).orElse(null));
        return opt;
    }

    default List<WeighDataComponentsFromDataOPT> convertToOPT(List<WeighDataDO> weighDataDOs){
        if (weighDataDOs == null){
            return null;
        }
        if (weighDataDOs.isEmpty()){
            return new ArrayList<>();
        }
        return weighDataDOs.stream()
                .map(this::convertToOPT)
                .collect(Collectors.toList());
    }

    default WeighDataVO convertToVO(WeighDataDO weighDataDO){
        if (weighDataDO == null){
            return null;
        }
        WeighDataVO weighDataVO = new WeighDataVO();
        weighDataVO.setId(weighDataDO.getId());
        weighDataVO.setWeight(weighDataDO.getWeight());
        weighDataVO.setUnit(SpringUtil.getBean(UnitCache.class).getGlobalUnitName(weighDataDO.getUnitId()));
        BaseUserDO weigher = UserUtils.getUser(weighDataDO.getWeigherId());
        if (weigher != null){
            weighDataVO.setWeigher(weigher.getUserName() + "-" + weigher.getLoginName());
        }
        weighDataVO.setWeighTime(weighDataDO.getWeighTime());
        return weighDataVO;
    }

    default List<WeighDataVO> convertToVO(List<WeighDataDO> weighDataDOS){
        if (weighDataDOS == null){
            return null;
        }
        if (weighDataDOS.isEmpty()){
            return new ArrayList<>();
        }
        return weighDataDOS.stream()
                .map(this::convertToVO)
                .collect(Collectors.toList());
    }
}
