package com.bmos.lims2.server.inspect.team.mapper;

import com.bmos.lims2.server.inspect.team.entity.InspectionTeamUser;
import com.bmos.mybatis.mapper.BaseMapperX;
import com.bmos.mybatis.query.LambdaQueryWrapperX;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface InspectionTeamUserMapper extends BaseMapperX<InspectionTeamUser> {

    default void deleteByInspectionTeamId(Long id) {
        delete(new LambdaQueryWrapperX<InspectionTeamUser>()
                .eq(InspectionTeamUser::getInspectionTeamId, id));
    }

    default List<InspectionTeamUser> selectByInspectionTeamId(Long id) {
        return selectList(new LambdaQueryWrapperX<InspectionTeamUser>()
                .eq(InspectionTeamUser::getInspectionTeamId, id));
    }
}
