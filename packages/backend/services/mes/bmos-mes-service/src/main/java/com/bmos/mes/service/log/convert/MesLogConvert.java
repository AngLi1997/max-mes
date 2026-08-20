package com.bmos.mes.service.log.convert;

import com.bmos.logging.enums.OperationTypeEnum;
import com.bmos.mes.service.log.model.MesLogModel;
import com.bmos.mes.service.log.vo.MesLogDetailVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.Arrays;

@Mapper
public interface MesLogConvert {

    MesLogConvert INSTANCE = Mappers.getMapper(MesLogConvert.class);

    static OperationTypeEnum getEnumByValue(Integer value) {
        return Arrays.stream(OperationTypeEnum.values())
                .filter(typeEnum -> typeEnum.getValue().equals(value))
                .findFirst()
                .orElse(OperationTypeEnum.INSERT);
    }

    MesLogDetailVO convert2DetailVO(MesLogModel mesLogModel);

}
