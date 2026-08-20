package com.bmos.mes.service.process.mapper;


import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.bmos.mes.service.plan.production.vo.BuildPlanBatchNextNoVO;
import com.bmos.mes.service.process.dto.query.ProcessListQueryDTO;
import com.bmos.mes.service.process.dto.query.ProcessPageQueryDTO;
import com.bmos.mes.service.process.dto.query.ProcessRelationQueryDTO;
import com.bmos.mes.service.process.model.Process;
import com.bmos.mes.service.process.vo.ProcessPageVO;
import com.bmos.mybatis.mapper.BaseMapperX;
import com.bmos.mybatis.query.LambdaQueryWrapperX;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Mapper
public interface ProcessMapper extends BaseMapperX<Process> {

    List<ProcessPageVO> selectPageList(ProcessPageQueryDTO dto);

    default Boolean existsName(String name) {
        return exists(new LambdaQueryWrapperX<Process>()
                .eq(Process::getName, name)
                .last("limit 1"));
    }

    default void updateVersion(Long processId, String version) {
            LambdaUpdateWrapper<Process> updateWrapper = new LambdaUpdateWrapper<>();
            updateWrapper.eq(Process::getId, processId)
                    .set(Process::getActiveVersion, version);
            Process process = new Process();
            process.setId(processId);
            process.setActiveVersion(Optional.ofNullable(version).orElse(null));
            updateById(process);
            update(process, updateWrapper);
    }

    default List<Process> selectCustomList(ProcessListQueryDTO dto,List<Long> processIds) {
        return selectList(new LambdaQueryWrapperX<Process>()
                .eqIfPresent(Process::getProductId, dto.getProductId())
                .inIfPresent(Process::getId,processIds)
                .isNotNull(ObjectUtil.isNotNull(dto.getActive()) && dto.getActive(),
                        Process::getActiveVersion));
    }

    List<Process> selectRelationList(ProcessRelationQueryDTO dto);

    List<String> getAuditBusinessKey(@Param("deptIdList") List<Long> deptIdList);

    List<Long> getIdListByDeptIds(@Param("deptIdList") List<Long> deptIds);

    default List<Process> selectListByIdS(List<Long> ids){
        return selectList(new LambdaQueryWrapperX<Process>().in(Process::getId,ids));
    }

    List<Process> selectByProductIdsAndDeptIds(@Param("productIds") List<Long> productIds, @Param("deptIds") List<Long> deptIds);

    List<BuildPlanBatchNextNoVO> selectProductListByProcessIdS(@Param("list") Set<Long> processIdList);
}
