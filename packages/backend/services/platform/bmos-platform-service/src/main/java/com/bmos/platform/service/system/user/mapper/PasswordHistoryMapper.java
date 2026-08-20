package com.bmos.platform.service.system.user.mapper;

import com.bmos.mybatis.mapper.BaseMapperX;
import com.bmos.mybatis.query.LambdaQueryWrapperX;
import com.bmos.platform.service.system.user.entity.PasswordHistory;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 历史密码mapper
 */
@Mapper
public interface PasswordHistoryMapper extends BaseMapperX<PasswordHistory> {

    /**
     * 查询当前人前N个密码
     * @param userId
     * @param n
     * @return
     */
    default List<PasswordHistory> selectLastNPwd(String userId, int n){
        return selectList(new LambdaQueryWrapperX<PasswordHistory>()
                .eq(PasswordHistory::getUserId, userId)
                .orderByDesc(PasswordHistory::getCreateTime)
                .last("limit " + n));
    }

}
