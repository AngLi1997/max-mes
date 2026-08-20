package com.bmos.mes.service.plan.document.mapper;

import com.bmos.mes.service.plan.document.model.BatchTemplateInfoProcess;
import com.bmos.mybatis.mapper.BaseMapperX;
import com.bmos.mybatis.query.LambdaQueryWrapperX;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 批记录模板信息版本与工艺的绑定关系(BmBatchTemplateInfoProcess)表数据库访问层
 *
 * @author makejava
 * @since 2024-08-19 11:06:42
 */
@Mapper
public interface BatchTemplateInfoProcessMapper extends BaseMapperX<BatchTemplateInfoProcess> {

    default void deleteByTemplateInfoId(Long templateInfoId){
        this.delete(new LambdaQueryWrapperX<BatchTemplateInfoProcess>().eq(BatchTemplateInfoProcess::getBatchTemplateInfoId, templateInfoId));
    }

    default List<BatchTemplateInfoProcess> selectByTemplateInfoIdList(List<Long> templateInfoIdList){
        return selectList(new LambdaQueryWrapperX<BatchTemplateInfoProcess>()
                .in(BatchTemplateInfoProcess::getBatchTemplateInfoId, templateInfoIdList));
    }

    default List<BatchTemplateInfoProcess> selectByTemplateInfoId(Long templateInfoId){
        return selectList(new LambdaQueryWrapperX<BatchTemplateInfoProcess>()
                .eq(BatchTemplateInfoProcess::getBatchTemplateInfoId, templateInfoId));
    }

    default List<BatchTemplateInfoProcess> selectTemplateProcessByProcessId(Long processId){
        return selectList(new LambdaQueryWrapperX<BatchTemplateInfoProcess>()
                .eq(BatchTemplateInfoProcess::getProcessId, processId));
    }

    /**
     * 查询具有模板的工艺
     * @return
     */
    List<Long> selectProcessIdListByTemplateId(@Param("templateId") Long templateId);

    /**
     * 根据工艺id查询与工艺绑定的模板id
     * @param processId
     * @return
     */
    default List<BatchTemplateInfoProcess> selectByProcessId(Long processId){
        return selectList(new LambdaQueryWrapperX<BatchTemplateInfoProcess>()
                .eq(BatchTemplateInfoProcess::getProcessId, processId));
    }

    List<Long> selectAllProcessIds();
}

