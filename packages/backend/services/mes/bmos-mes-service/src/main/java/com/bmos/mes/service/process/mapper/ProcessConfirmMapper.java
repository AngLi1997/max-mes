package com.bmos.mes.service.process.mapper;

import com.baomidou.mybatisplus.extension.toolkit.Db;
import com.bmos.audit.engine.core.db.repository.base.LambdaQueryWrapperX;
import com.bmos.mes.service.process.dto.query.AuditOpinionQueryDTO;
import com.bmos.mes.service.process.dto.query.ProcessConfirmQueryDTO;
import com.bmos.mes.service.process.model.ProcessConfirm;
import com.bmos.mes.service.process.vo.AuditOpinionVO;
import com.bmos.mes.service.process.vo.ProcessConfirmVO;
import com.bmos.mybatis.mapper.BaseMapperX;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author renjinguang
 */
@Mapper
public interface ProcessConfirmMapper extends BaseMapperX<ProcessConfirm> {


    default Boolean saveProcessConfirm(ProcessConfirm processConfirm) {
        return Db.saveOrUpdate(processConfirm);
    }

    default ProcessConfirm queryProcessConfirmByInstanceId(String instanceId) {
        return selectOne(new LambdaQueryWrapperX<ProcessConfirm>()
                .eq(ProcessConfirm::getInstanceId, instanceId));
    }

    List<ProcessConfirmVO> getProcessConfirmPageList(ProcessConfirmQueryDTO dto);

    default List<ProcessConfirm> getProcessNameList(){
        return selectList(new LambdaQueryWrapperX<ProcessConfirm>());
    }

    default ProcessConfirm queryProcessById(Long id){
        return selectOne(new LambdaQueryWrapperX<ProcessConfirm>()
                .eq(ProcessConfirm::getId, id));
    }

    List<AuditOpinionVO> listProcedureOpinionPage(AuditOpinionQueryDTO dto);

    List<AuditOpinionVO> listProcessOpinionPage(AuditOpinionQueryDTO dto);
}
