package com.bmos.mes.service.weigh.centre2.execute.converter;

import com.bmos.common.base.enums.CommonEnum;
import com.bmos.mes.common.enums.weigh.centre2.WeighFuncEnum;
import com.bmos.mes.common.enums.weigh.centre2.WeighTypeEnum;
import com.bmos.mes.service.weigh.centre2.execute.controller.vo.WeighRequirementRecordVO;
import com.bmos.mes.service.weigh.centre2.execute.controller.vo.WeighRequirementVO;
import com.bmos.mes.service.weigh.centre2.execute.controller.vo.WeighTicketDetailVO;
import com.bmos.mes.service.weigh.centre2.execute.model.WeighRequirementRecordDO;
import com.bmos.mes.service.weigh.centre2.execute.service.dto.WeighRequirementRecordDTO;
import com.bmos.mes.service.weigh.centre2.requirement.entity.TicketRequirementDO;
import com.bmos.mes.service.weigh.centre2.ticket.entity.TicketDO;
import com.bmos.mes.service.weigh.centre2.execute.controller.vo.TicketOddmentInfoVO;
import com.bmos.mes.service.weigh.centre2.execute.controller.vo.TicketWeighRecordVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.time.LocalDateTime;
import java.util.List;

@Mapper(componentModel = "spring")
public interface WeighTicketConverter {
    WeighTicketConverter INSTANCE = Mappers.getMapper(WeighTicketConverter.class);

    // 工单详情
    default WeighTicketDetailVO toDetailVO(TicketDO entity){
        if (entity == null) return null;
        WeighTicketDetailVO vo = new WeighTicketDetailVO();
        vo.setId(entity.getId());
        vo.setTicketNo(entity.getTicketNo());
        vo.setMaterialMergeCode(entity.getMaterialMergeCode());
        vo.setMaterialName(entity.getMaterialName());
        vo.setStorageMaterialBatchId(entity.getStorageMaterialBatchId());
        vo.setRequirementQuantity(entity.getRequirementQuantity());
        vo.setTicketWeighStatus(entity.getTicketWeighStatus() != null ? entity.getTicketWeighStatus() : null);
        vo.setEnoughCompleteCondition(entity.getEnoughCompleteCondition());
        return vo;
    }

    // 需求
    default WeighRequirementVO toRequirementVO(TicketRequirementDO entity) {
        if (entity == null) return null;
        WeighRequirementVO vo = new WeighRequirementVO();
        vo.setId(entity.getId());
        vo.setRequirementQuantity(entity.getFormulaQuantity());
        vo.setRequirementUsage(entity.getRequirementUsage());
        vo.setRequirementStatus(entity.getRequirementStatus() != null ? entity.getRequirementStatus() : null);
        return vo;
    }

    // 称量记录
    WeighRequirementRecordVO toRecordVO(WeighRequirementRecordDO recordDO);
    List<WeighRequirementRecordVO> toRecordVOList(List<WeighRequirementRecordDO> recordDOList);

    default WeighRequirementRecordDO toRecordDO(WeighRequirementRecordDTO dto){
        if ( dto == null ) {
            return null;
        }
        WeighRequirementRecordDO weighRequirementRecordDO = new WeighRequirementRecordDO();
        weighRequirementRecordDO.setWeighTime(LocalDateTime.now());
        weighRequirementRecordDO.setWeighTicketRequirementId( dto.getWeighTicketRequirementId() );
        weighRequirementRecordDO.setTicketId( dto.getTicketId() );
        weighRequirementRecordDO.setNetWeight( dto.getNetWeight() );
        weighRequirementRecordDO.setTareWeight( dto.getTareWeight() );
        weighRequirementRecordDO.setGrossWeight( dto.getGrossWeight() );
        weighRequirementRecordDO.setWeighFunc(CommonEnum.getEnumByValue(WeighFuncEnum.class, dto.getWeighFunc()));
        weighRequirementRecordDO.setWeighType( CommonEnum.getEnumByValue(WeighTypeEnum.class, dto.getWeighType()) );
        weighRequirementRecordDO.setUnitId( dto.getUnitId() );
        weighRequirementRecordDO.setDeviceId( dto.getDeviceId() );
        weighRequirementRecordDO.setDeviceName( dto.getDeviceName() );
        weighRequirementRecordDO.setDeviceCode( dto.getDeviceCode() );
        weighRequirementRecordDO.setStorageId( dto.getStorageId() );

        return weighRequirementRecordDO;
    }

    public static TicketOddmentInfoVO toTicketOddmentInfoVO(/*参数自定义*/) {
        TicketOddmentInfoVO vo = new TicketOddmentInfoVO();
        // TODO: 填充字段
        return vo;
    }

    public static TicketWeighRecordVO toTicketWeighRecordVO(/*参数自定义*/) {
        TicketWeighRecordVO vo = new TicketWeighRecordVO();
        // TODO: 填充字段
        return vo;
    }
} 