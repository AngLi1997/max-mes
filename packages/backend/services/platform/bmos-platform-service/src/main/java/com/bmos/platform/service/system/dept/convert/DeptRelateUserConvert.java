package com.bmos.platform.service.system.dept.convert;

import com.bmos.platform.service.system.dept.dto.DeptRelateUserSaveDTO;
import com.bmos.platform.service.system.dept.dto.DeptSaveDTO;
import com.bmos.platform.service.system.dept.model.DeptRelateUser;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface DeptRelateUserConvert {
    DeptRelateUserConvert INSTANCE = Mappers.getMapper(DeptRelateUserConvert.class);

    DeptRelateUser convert(DeptRelateUserSaveDTO dto);

    List<DeptRelateUser> convert(List<DeptRelateUserSaveDTO> dto);

    DeptRelateUserSaveDTO convert(DeptSaveDTO dto);

}
