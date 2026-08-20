package com.bmos.platform.service.system.user.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.bmos.mybatis.mapper.BaseMapperX;
import com.bmos.platform.service.system.user.entity.UserSign;
import org.apache.ibatis.annotations.Mapper;

/**
 * 手写签名表(BpUserSign)表数据库访问层
 *
 * @author makejava
 * @since 2024-07-03 11:08:03
 */
@Mapper
public interface UserSignMapper extends BaseMapperX<UserSign> {


    /**
     * 根据用户id查询用户签名信息
     * @param userId
     * @return
     */
    default UserSign selectByUserId(String userId){
        return selectOne(new LambdaQueryWrapper<UserSign>()
                .eq(UserSign::getUserId,userId));
    }
}

