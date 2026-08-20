package com.bmos.mes.service.ingredient.weigh.mapper;

import com.bmos.mes.service.ingredient.weigh.dto.WeighLogQueryDTO;
import com.bmos.mes.service.ingredient.weigh.dto.WeighLogSaveDTO;
import com.bmos.mes.service.ingredient.weigh.model.WeighLog;
import com.bmos.mes.service.ingredient.weigh.vo.WeighLogPageVO;
import com.bmos.mybatis.mapper.BaseMapperX;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;


@Mapper
public interface WeighLogMapper extends BaseMapperX<WeighLog> {

    List<WeighLogPageVO> selectPageList(WeighLogQueryDTO dto);

}
