package com.bmos.mes.service.product.mapper;

import com.bmos.mes.service.product.dto.MaterialLogPageQueryDTO;
import com.bmos.mes.service.product.model.MaterialLog;
import com.bmos.mes.service.product.vo.MaterialLogPageVO;
import com.bmos.mybatis.mapper.BaseMapperX;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface MaterialLogMapper extends BaseMapperX<MaterialLog> {

    List<MaterialLogPageVO> selectPageVOList(@Param("dto") MaterialLogPageQueryDTO dto, @Param("opList") List<Integer> opList);
}
