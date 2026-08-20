package com.bmos.mes.service.plan.team.mapper;

import cn.hutool.core.collection.CollUtil;
import com.bmos.mes.common.enums.BooleanEnum;
import com.bmos.mes.service.plan.team.dto.ProductPlanTeamListDTO;
import com.bmos.mes.service.plan.team.dto.ProductPlanTeamPageDTO;
import com.bmos.mes.service.plan.team.model.InstructionTeam;
import com.bmos.mes.service.plan.team.model.ProductPlanTeam;
import com.bmos.mes.service.plan.team.vo.ProductPlanPageTeamVO;
import com.bmos.mes.service.plan.team.vo.ProductPlanTeamListVO;
import com.bmos.mybatis.mapper.BaseMapperX;
import com.bmos.mybatis.query.LambdaQueryWrapperX;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Mapper
public interface ProductPlanTeamMapper extends BaseMapperX<ProductPlanTeam> {
    List<ProductPlanPageTeamVO> list(ProductPlanTeamListDTO dto);

    List<ProductPlanPageTeamVO> page(ProductPlanTeamPageDTO dto);

    default void updateStatus(Long id, BooleanEnum status) {
        updateById(ProductPlanTeam.builder().id(id).status(status).build());
    }

    List<Long> listByUser(@Param("userId") String userId);

    default List<String> selectTeamPeople(List<Long> teamIds) {
        if (CollUtil.isEmpty(teamIds)) {
            return Collections.emptyList();
        }
        return selectList(new LambdaQueryWrapperX<ProductPlanTeam>()
                .in(ProductPlanTeam::getId, teamIds)
                .select(Collections.singletonList(ProductPlanTeam::getPeople)))
                .stream().map(ProductPlanTeam::getPeople)
                .flatMap(Collection::stream)
                .distinct()
                .collect(Collectors.toList());
    }

    List<ProductPlanTeamListVO> selectListByLineIds(@Param("lineIds") List<Long> lineIds);

    List<ProductPlanTeamListVO> selectListByIds(@Param("ids") List<Long> groupIds);
}
