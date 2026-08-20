package com.bmos.mes.service.weigh.data.service.impl;

import com.bmos.common.holder.SysUserHolder;
import com.bmos.mes.service.components.BusinessComponentManager;
import com.bmos.mes.service.components.comps.WeighDataComponentsFromDataOPT;
import com.bmos.mes.service.components.dto.FormDataOPT;
import com.bmos.mes.service.components.model.BusinessComponentInstance;
import com.bmos.mes.service.weigh.data.convert.WeighDataConvert;
import com.bmos.mes.service.weigh.data.dto.WeighDataDTO;
import com.bmos.mes.service.weigh.data.entity.WeighDataDO;
import com.bmos.mes.service.weigh.data.mapper.IWeighDataMapper;
import com.bmos.mes.service.weigh.data.service.IWeighDataService;
import com.bmos.mes.service.weigh.data.vo.WeighDataVO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author liang
 * @version 1.0.0
 * @date 2024/11/12 18:00
 */
@Service
public class WeighDataServiceImpl implements IWeighDataService {

    @Resource
    private BusinessComponentManager businessComponentManager;

    @Resource
    private IWeighDataMapper weighDataMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveData(WeighDataDTO dto) {
        BusinessComponentInstance componentInstance = businessComponentManager.getComponentInstanceById(dto.getComponentInstanceId());
        List<FormDataOPT> optList = businessComponentManager.getFormDataOPTList(dto.getComponentInstanceId());
        WeighDataDO weighDataDO = new WeighDataDO();
        weighDataDO.setWeight(dto.getWeight());
        weighDataDO.setUnitId(dto.getUnitId());
        weighDataDO.setWeigherId(SysUserHolder.getUser().getUserId());
        weighDataDO.setWeighTime(LocalDateTime.now());
        weighDataDO.setComponentInstanceId(dto.getComponentInstanceId());
        weighDataMapper.insert(weighDataDO);
        List<WeighDataDO> list = weighDataMapper.selectListByComponentInstanceId(dto.getComponentInstanceId());
        // 根据weighTime升序排列
//        list.sort(Comparator.comparing(WeighDataDO::getWeighTime));
        List<WeighDataComponentsFromDataOPT> opts = WeighDataConvert.INSTANCE.convertToOPT(list);
        businessComponentManager.fillFormDataOPT(opts, optList);
        businessComponentManager.saveFormDataOPT(optList, componentInstance);
    }

    @Override
    public List<WeighDataVO> getWeighList(Long componentInstanceId) {
        List<WeighDataDO> weighDataDOS = weighDataMapper.selectListByComponentInstanceId(componentInstanceId);
        return WeighDataConvert.INSTANCE.convertToVO(weighDataDOS);
    }
}
