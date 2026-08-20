package com.bmos.mes.service.weigh.free.mapper;

import com.bmos.mes.service.weigh.free.dto.FreeWeighHistoryPageQuery;
import com.bmos.mes.service.weigh.free.entity.FreeWeighHistoryDO;
import com.bmos.mes.service.weigh.free.vo.FreeWeighHistoryPage;
import com.bmos.mybatis.mapper.BaseMapperX;
import com.bmos.mybatis.query.LambdaQueryWrapperX;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author liang
 * @version 1.0.0
 * @date 2025/2/27 10:59
 */
@Mapper
public interface IFreeWeighHistoryMapper extends BaseMapperX<FreeWeighHistoryDO> {

    List<FreeWeighHistoryPage> queryHistoryPage(@Param("pageQuery") FreeWeighHistoryPageQuery pageQuery);

    default FreeWeighHistoryDO selectByStorageMaterialId(Long storageMaterialId){
        if (storageMaterialId == null) {
            return null;
        }
        return selectOne(new LambdaQueryWrapperX<FreeWeighHistoryDO>()
                .eq(FreeWeighHistoryDO::getStorageMaterialId, storageMaterialId));
    }
}
