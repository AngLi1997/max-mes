package com.bmos.mes.service.process.mapper;

import com.baomidou.mybatisplus.extension.toolkit.Db;
import com.bmos.mes.service.process.model.ProcedureConfirm;
import com.bmos.mybatis.mapper.BaseMapperX;
import com.bmos.mybatis.query.LambdaQueryWrapperX;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author renjinguang
 */
@Mapper
public interface ProcedureConfirmMapper extends BaseMapperX<ProcedureConfirm> {


    default Boolean saveProcedureConfirm(ProcedureConfirm procedureConfirm) {
        return Db.saveOrUpdate(procedureConfirm);
    }

    default List<ProcedureConfirm> queryProcedurePageByProcessId(Long processId) {
        return selectList(new LambdaQueryWrapperX<ProcedureConfirm>()
                .eq(ProcedureConfirm::getProcessConfirmId, processId));
    }

    default ProcedureConfirm queryOneById(Long id){
        return selectOne(new LambdaQueryWrapperX<ProcedureConfirm>()
                .eq(ProcedureConfirm::getId, id));
    }

    default List<ProcedureConfirm> queryProcedureNameByProcessId(Long processId){
        return selectList(new LambdaQueryWrapperX<ProcedureConfirm>()
                .eq(ProcedureConfirm::getProcessId, processId));
    }
}
