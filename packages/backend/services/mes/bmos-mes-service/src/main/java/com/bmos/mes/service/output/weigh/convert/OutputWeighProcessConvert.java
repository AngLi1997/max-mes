package com.bmos.mes.service.output.weigh.convert;

import com.bmos.mes.service.output.weigh.model.OutputWeighProcess;
import com.bmos.mes.service.output.weigh.vo.OutputWeighProcessVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * @author liang
 * @version 1.0.0
 * @date 2024/4/22 18:08
 */
@Mapper
public interface OutputWeighProcessConvert {

    OutputWeighProcessConvert INSTANCE = Mappers.getMapper(OutputWeighProcessConvert.class);

    OutputWeighProcessVO convertToVO(OutputWeighProcess process);
}
