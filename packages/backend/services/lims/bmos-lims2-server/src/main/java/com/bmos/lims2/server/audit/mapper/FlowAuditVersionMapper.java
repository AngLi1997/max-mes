package com.bmos.lims2.server.audit.mapper;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.toolkit.Db;
import com.bmos.lims2.common.enums.FlowAuditStateEnum;
import com.bmos.lims2.server.audit.dto.AuditPageDTO;
import com.bmos.lims2.server.audit.dto.SaveAuditDTO;
import com.bmos.lims2.server.audit.entity.FlowAuditVersion;
import com.bmos.lims2.server.audit.vo.FlowAuditVO;
import com.bmos.mybatis.mapper.BaseMapperX;
import com.bmos.mybatis.query.LambdaQueryWrapperX;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Objects;


/**
 * @author renjinguang
 */
@Mapper
public interface FlowAuditVersionMapper extends BaseMapperX<FlowAuditVersion> {


    List<FlowAuditVO> selectVersionList(AuditPageDTO dto);

    default Boolean saveFlowAuditVersion(FlowAuditVersion version) {
        return Db.saveOrUpdate(version);
    }

    default FlowAuditVersion selectVersionState(Long flowAuditId) {
        return selectOne(new LambdaQueryWrapperX<FlowAuditVersion>()
                .eq(FlowAuditVersion::getFlowAuditId, flowAuditId)
                .eq(FlowAuditVersion::getState, FlowAuditStateEnum.STATE.getValue()));
    }

    default FlowAuditVersion queryById(Long versionId) {
        return selectOne(new LambdaQueryWrapperX<FlowAuditVersion>()
                .eq(FlowAuditVersion::getId, versionId));
    }

    default void deleteFlowAudit(Long versionId) {
        Db.removeById(versionId, FlowAuditVersion.class);
    }

    Integer selectMaxVersionByAuditId(Long flowAuditId);

    default List<FlowAuditVersion> queryByAuditIdListAndState(List<Long> auditIdList, Integer code){
        return selectList(new LambdaQueryWrapperX<FlowAuditVersion>()
                .in(FlowAuditVersion::getFlowAuditId,auditIdList)
                .eq(FlowAuditVersion::getState,code));
    }

    default List<FlowAuditVersion> selectListByAuditId(Long auditId){
        return selectList(new LambdaQueryWrapperX<FlowAuditVersion>()
                .eq(FlowAuditVersion::getFlowAuditId,auditId));
    }

    default void changeStateById(Long id, Boolean enable){
        update(null, new LambdaUpdateWrapper<FlowAuditVersion>()
                .eq(FlowAuditVersion::getId, id)
                .set(FlowAuditVersion::getState, enable ? FlowAuditStateEnum.STATE.getValue() : FlowAuditStateEnum.HISTORY.getValue()));
    }

    default boolean versionExist(SaveAuditDTO dto){
        return exists(new LambdaQueryWrapperX<FlowAuditVersion>()
                .eq(FlowAuditVersion::getFlowAuditId, dto.getFlowAuditId())
                .eq(FlowAuditVersion::getVersion, dto.getVersion())
                .ne(Objects.nonNull(dto.getVersionId()), FlowAuditVersion::getId, dto.getVersionId()));
    }
}
