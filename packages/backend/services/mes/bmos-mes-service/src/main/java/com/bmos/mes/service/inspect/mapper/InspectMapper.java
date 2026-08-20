package com.bmos.mes.service.inspect.mapper;

import com.bmos.mes.common.enums.inpspect.InspectStatusEnum;
import com.bmos.mes.service.inspect.model.Inspect;
import com.bmos.mes.service.inspect.service.dto.InspectPageDTO;
import com.bmos.mybatis.mapper.BaseMapperX;
import com.bmos.mybatis.query.LambdaQueryWrapperX;
import org.apache.ibatis.annotations.Mapper;

import java.util.Collection;
import java.util.List;
import java.util.Objects;

/**
 * 请验单(BmInspect)表数据库访问层
 *
 * @author makejava
 * @since 2025-02-17 18:26:28
 */
@Mapper
public interface InspectMapper extends BaseMapperX<Inspect> {


    default Inspect selectByInspectNo(String inspectNo){
        return selectOne(new LambdaQueryWrapperX<Inspect>()
                .eq(Inspect::getInspectNo,inspectNo));
    }

    default List<Inspect> queryPage(InspectPageDTO dto){
        LambdaQueryWrapperX<Inspect> wrapperX = new LambdaQueryWrapperX<Inspect>()
                .eq(Inspect::getPlanId, dto.getPlanId());
        if (Objects.nonNull(dto.getProcedureModelId())){
            wrapperX.eq(Inspect::getProcedureModelId, dto.getProcedureModelId());
        }
        if (Objects.nonNull(dto.getProcedureStepModelId())){
            wrapperX.eq(Inspect::getProcedureStepModelId, dto.getProcedureStepModelId());
        }
        if (Objects.nonNull(dto.getProcessChangeNum())){
            wrapperX.eq(Inspect::getProcessChangeNumber, dto.getProcessChangeNum());
        }
        if (Objects.nonNull(dto.getProcedureChangeNum())){
            wrapperX.eq(Inspect::getProcedureChangeNumber, dto.getProcedureChangeNum());
        }
        return selectList(wrapperX);
    }

    default List<Inspect> selectByInspectConfigId(Long id){
        return selectList(new LambdaQueryWrapperX<Inspect>()
                .eq(Inspect::getInspectConfigId,id));
    }

    default List<Inspect> queryNotRejectInspectResultPage(Long productPlanId){
        return selectList(new LambdaQueryWrapperX<Inspect>()
                .eq(Inspect::getPlanId,productPlanId)
                .ne(Inspect::getStatus, InspectStatusEnum.REJECTED));
    }

    default List<Inspect> selectByInspectNoList(List<String> inspectNoList){
        return selectList(new LambdaQueryWrapperX<Inspect>()
                .in(Inspect::getInspectNo, inspectNoList));
    }

    default List<Inspect> selectByMaterialBatchNoAndMaterialId(String materialBatchNo, Long materialId){
        return selectList(new LambdaQueryWrapperX<Inspect>()
                .eq(Inspect::getMaterialBatchNo, materialBatchNo)
                .eq(Inspect::getMaterialId, materialId));
    }

    default List<Inspect> selectByBatchNoAndMaterialIdList(Collection<String> batchNos, Collection<Long> materialIds){
        return selectList(new LambdaQueryWrapperX<Inspect>()
                .in(Inspect::getMaterialBatchNo, batchNos)
                .in(Inspect::getMaterialId, materialIds));
    }
}

