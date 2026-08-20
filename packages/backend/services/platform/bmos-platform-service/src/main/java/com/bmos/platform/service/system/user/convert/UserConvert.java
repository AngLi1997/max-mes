package com.bmos.platform.service.system.user.convert;

import cn.hutool.core.collection.CollUtil;
import com.bmos.mybatis.dataobject.BaseUserDO;
import com.bmos.platform.facade.system.user.vo.FeignUserVO;
import com.bmos.platform.service.system.user.dto.UserSaveDTO;
import com.bmos.platform.service.system.user.dto.UserStartDTO;
import com.bmos.platform.service.system.user.dto.UserUpdateDTO;
import com.bmos.platform.service.system.user.enums.ActiveEnum;
import com.bmos.platform.service.system.user.enums.GenderEnum;
import com.bmos.platform.service.system.user.model.User;
import com.bmos.platform.service.system.user.vo.*;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Mapper
public interface UserConvert {
    UserConvert INSTANCE = Mappers.getMapper(UserConvert.class);

    User convertUser(UserSaveDTO dto);

    User convertUser(UserUpdateDTO dto);

    User convertUser(UserStartDTO dto);

    UserLoginVO convertVO(User user);

    User convertSysUser(BaseUserDO user);

    UserInfoVO convertUserVO(User user);

    BaseUserDO convertUserVO2(User user);

    List<BaseUserDO> convertUserVOList(List<User> users);

    List<UserListItemVO> convertVoList(List<User> users);

    FeignUserVO convertToPlatformUserVO(User user);

    List<FeignUserVO> convertToPlatformUserVOList(List<UserListItemVO> UserListItemVO);

    List<FeignUserVO> convert2FeignUserVOList(List<User> users);

    UserDetailInfoVO convert2DetailVO(User user);

    List<User> convertUserList(List<UserTemplateVO> userList);

    default List<UserExportVO> convertUserExportVO(List<UserPageVO> pageVo) {
        if (CollUtil.isEmpty(pageVo)) {
            return new ArrayList<>();
        }
        return pageVo.stream().map(item -> {
            UserExportVO userExportVO = convertVo(item);
            userExportVO.setGenderEnum(GenderEnum.convertByCode(item.getGender()));
            userExportVO.setStatusEnum(ActiveEnum.convertByCode(item.getStatus()));
            return userExportVO;
        }).collect(Collectors.toList());
    }

    UserExportVO convertVo(UserPageVO pageVO);
}
