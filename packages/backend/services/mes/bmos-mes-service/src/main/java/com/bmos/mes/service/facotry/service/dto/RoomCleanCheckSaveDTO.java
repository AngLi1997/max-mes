package com.bmos.mes.service.facotry.service.dto;

import com.bmos.mes.service.execute.dto.BusinessDataHandleBaseDTO;
import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;

/**
 * 房间清场检查DTO
 */
@Getter
@Setter
@ApiModel("房间清场检查组件保存")
public class RoomCleanCheckSaveDTO extends BusinessDataHandleBaseDTO {

    private Long roomId;

}
