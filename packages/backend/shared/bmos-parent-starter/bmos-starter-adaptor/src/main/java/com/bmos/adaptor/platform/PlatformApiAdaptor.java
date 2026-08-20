package com.bmos.adaptor.platform;

import com.bmos.adaptor.platform.vo.UserInfoVO;
import com.bmos.adaptor.platform.dto.ValidatePwd;
import com.bmos.common.tree.CommonTreeVO;

import java.util.List;

public interface PlatformApiAdaptor {

    /**
     * 获取当前用户的角色
     * @return 角色id集合
     */
    List<Long> roleIds();


    /**
     * 获取当前用户的部门及部门下的子部门
     * @return 部门id集合
     */
    List<Long> deptIds();

    /**
     * 获取当前用户的部门
     * @return 部门id集合
     */
    List<Long> getMineDeptIds();


    /**
     * 查询所有的部门
     * @return 部门id集合
     */
    List<CommonTreeVO> deptTree();


    /**
     * 查询当前人的部门及父部门
     * @return 部门id集合
     */
    List<CommonTreeVO> deptPartitionTree();


    UserInfoVO getUser(String userId);


    /**
     * 校验密码
     * @param dto dto
     */
    UserInfoVO validatePassword(ValidatePwd dto);

}
