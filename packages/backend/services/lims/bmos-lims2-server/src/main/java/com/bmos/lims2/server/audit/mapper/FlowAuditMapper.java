package com.bmos.lims2.server.audit.mapper;

import com.baomidou.mybatisplus.extension.toolkit.Db;
import com.bmos.lims2.server.audit.dto.AuditPageDTO;
import com.bmos.lims2.server.audit.entity.FlowAudit;
import com.bmos.lims2.server.audit.vo.FlowAuditVO;
import com.bmos.mybatis.mapper.BaseMapperX;
import com.bmos.mybatis.query.LambdaQueryWrapperX;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;


/**
 * @author renjinguang
 */
@Mapper
public interface FlowAuditMapper extends BaseMapperX<FlowAudit> {


    List<FlowAuditVO> selectAuditList(AuditPageDTO dto);

    default Boolean saveFlowAudit(FlowAudit audit) {
        return Db.saveOrUpdate(audit);
    }

    default FlowAudit findAuditById(Long flowAuditId) {
        return selectOne(new LambdaQueryWrapperX<FlowAudit>().eq(FlowAudit::getId, flowAuditId));
    }

    String findDeploymentIdByCode(@Param("code") String code, @Param("categoryCode") String categoryCode);

    default FlowAudit selectByCode(String code){
        return selectOne(new LambdaQueryWrapperX<FlowAudit>().eq(FlowAudit::getCode, code));
    }
}
