package com.bmos.mes.service.weigh.data.service;

import com.bmos.mes.service.weigh.data.dto.WeighDataDTO;
import com.bmos.mes.service.weigh.data.vo.WeighDataVO;

import java.util.List;

/**
 * @author liang
 * @version 1.0.0
 * @date 2024/11/12 18:00
 */
public interface IWeighDataService {

    void saveData(WeighDataDTO dto);

    List<WeighDataVO> getWeighList(Long componentInstanceId);
}
