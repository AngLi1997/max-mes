package com.bmos.adaptor.platform.impl;

import com.bmos.adaptor.platform.PlatformApiAdaptor;
import com.bmos.adaptor.platform.dto.ValidatePwd;
import com.bmos.adaptor.platform.feign.PlatformOpenFeign;
import com.bmos.adaptor.platform.vo.UserInfoVO;
import com.bmos.common.exception.BaseResponseCode;
import com.bmos.common.exception.BmosException;
import com.bmos.common.response.ResponseInfo;
import com.bmos.common.tree.CommonTreeVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class PlatformApiAdaptorImpl implements PlatformApiAdaptor {

    private final Logger log = LoggerFactory.getLogger(PlatformApiAdaptor.class);

    private final PlatformOpenFeign platformOpenFeign;

    public PlatformApiAdaptorImpl(PlatformOpenFeign platformOpenFeign) {
        this.platformOpenFeign = platformOpenFeign;
    }

    @Override
    public List<Long> roleIds() {
        ResponseInfo<List<Long>> responseInfo;
        try {
            responseInfo = platformOpenFeign.getRolesIds();
        } catch (Exception e) {
            log.error("查询当前用户的角色信息失败：{}",e.getCause() + e.getMessage());
            throw new BmosException(BaseResponseCode.QUERY_ROLE_ERROR);
        }
        if (responseInfo.isError()){
            log.error("查询当前用户的角色信息失败：{}",responseInfo);
            throw new BmosException(BaseResponseCode.QUERY_ROLE_ERROR);
        }
        return responseInfo.getData();
    }

    @Override
    public List<Long> deptIds() {
        ResponseInfo<List<Long>> responseInfo;
        try {
            responseInfo = platformOpenFeign.getDeptIds();
        } catch (Exception e) {
            log.error("查询当前用户的部门信息失败：{}",e.getCause() + e.getMessage());
            throw new BmosException(BaseResponseCode.QUERY_DEPT_ERROR);
        }
        if (responseInfo.isError()){
            log.error("查询当前用户的部门信息失败：{}",responseInfo);
            throw new BmosException(BaseResponseCode.QUERY_DEPT_ERROR);
        }
        return responseInfo.getData();
    }

    @Override
    public List<Long> getMineDeptIds() {
        ResponseInfo<List<Long>> responseInfo;
        try {
            responseInfo = platformOpenFeign.getMineDeptIds();
        } catch (Exception e) {
            log.error("查询当前用户的部门信息失败：{}",e.getCause() + e.getMessage());
            throw new BmosException(BaseResponseCode.QUERY_DEPT_ERROR);
        }
        if (responseInfo.isError()){
            log.error("查询当前用户的部门信息失败：{}",responseInfo);
            throw new BmosException(BaseResponseCode.QUERY_DEPT_ERROR);
        }
        return responseInfo.getData();
    }

    @Override
    public List<CommonTreeVO> deptTree() {
        ResponseInfo<List<CommonTreeVO>> responseInfo;
        try {
            responseInfo = platformOpenFeign.getDeptTree();
        } catch (Exception e) {
            log.error("查询全量部门树信息失败：{}",e.getCause() + e.getMessage());
            throw new BmosException(BaseResponseCode.QUERY_DEPT_ERROR);
        }
        if (responseInfo.isError()){
            log.error("查询全量部门树信息失败：{}",responseInfo);
            throw new BmosException(BaseResponseCode.QUERY_DEPT_ERROR);
        }
        return responseInfo.getData();
    }

    @Override
    public List<CommonTreeVO> deptPartitionTree() {
        ResponseInfo<List<CommonTreeVO>> responseInfo;
        try {
            responseInfo = platformOpenFeign.getDeptPartitionTree();
        } catch (Exception e) {
            log.error("查询当前用户的部门树信息失败：{}",e.getCause() + e.getMessage());
            throw new BmosException(BaseResponseCode.QUERY_DEPT_ERROR);
        }
        if (responseInfo.isError()){
            log.error("查询当前用户的部门树信息失败：{}",responseInfo);
            throw new BmosException(BaseResponseCode.QUERY_DEPT_ERROR);
        }
        return responseInfo.getData();
    }

    @Override
    public UserInfoVO getUser(String userId) {
        ResponseInfo<UserInfoVO> responseInfo;
        try {
            responseInfo = platformOpenFeign.getUser(userId);
        } catch (Exception e) {
            log.error("查询用户息失败：{}",e.getCause() + e.getMessage());
            throw new BmosException(BaseResponseCode.QUERY_USER_ERROR);
        }
        if (responseInfo.isError()){
            log.error("查询用户息失败：{}",responseInfo);
            throw new BmosException(BaseResponseCode.QUERY_USER_ERROR);
        }
        return responseInfo.getData();
    }

    @Override
    public UserInfoVO validatePassword(ValidatePwd dto) {
        ResponseInfo<UserInfoVO> responseInfo;
        try {
            responseInfo = platformOpenFeign.validatePassword(dto);
        } catch (Exception e) {
            log.error("查询用户息失败：{}",e.getCause() + e.getMessage());
            throw new BmosException(BaseResponseCode.QUERY_USER_ERROR);
        }
        if (responseInfo.isError()){
            log.error("查询用户息失败：{}",responseInfo);
            throw new BmosException(BaseResponseCode.QUERY_USER_ERROR);
        }
        return responseInfo.getData();
    }
}
