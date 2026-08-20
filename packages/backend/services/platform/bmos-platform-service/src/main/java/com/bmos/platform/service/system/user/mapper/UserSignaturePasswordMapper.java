package com.bmos.platform.service.system.user.mapper;

import com.bmos.common.util.collection.CollectionUtils;
import com.bmos.mybatis.mapper.BaseMapperX;
import com.bmos.mybatis.query.LambdaQueryWrapperX;
import com.bmos.platform.service.system.user.model.UserSignaturePassword;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Mapper;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author liang
 * @version 1.0.0
 * @date 2024/11/26 13:47
 */
@Mapper
public interface UserSignaturePasswordMapper extends BaseMapperX<UserSignaturePassword> {

    default UserSignaturePassword selectByUserId(String userId){
        if (StringUtils.isEmpty(userId)){
            return null;
        }
        return selectOne(new LambdaQueryWrapperX<UserSignaturePassword>()
                .eq(UserSignaturePassword::getUserId, userId)
        );
    }

    default List<UserSignaturePassword> selectByUserIds(Collection<String> userIds){
        if (CollectionUtils.isAnyEmpty(userIds)){
            return new ArrayList<>();
        }
        return selectList(new LambdaQueryWrapperX<UserSignaturePassword>()
                .in(UserSignaturePassword::getUserId, userIds)
        );
    }
}
