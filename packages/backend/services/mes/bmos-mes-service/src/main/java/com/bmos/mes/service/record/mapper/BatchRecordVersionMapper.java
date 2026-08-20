package com.bmos.mes.service.record.mapper;

import com.baomidou.mybatisplus.extension.toolkit.Db;
import com.bmos.mes.service.record.enums.RecordStateEnum;
import com.bmos.mes.service.record.model.BatchRecordVersion;
import com.bmos.mes.service.record.vo.PageRecordAuditVO;
import com.bmos.mes.service.record.vo.RecordVersionVO;
import com.bmos.mybatis.mapper.BaseMapperX;
import com.bmos.mybatis.query.LambdaQueryWrapperX;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;


@Mapper
public interface BatchRecordVersionMapper extends BaseMapperX<BatchRecordVersion> {


    default Boolean saveOrUpdateVersion(BatchRecordVersion version) {
        return Db.saveOrUpdate(version);
    }

    default List<BatchRecordVersion> getRecordVersionList(Long recordId) {
        return selectList(new LambdaQueryWrapperX<BatchRecordVersion>()
                .eq(BatchRecordVersion::getRecordId, recordId));
    }

    default Boolean updateVersion(BatchRecordVersion version) {
        return Db.saveOrUpdate(version);
    }

    List<RecordVersionVO> listVersion(Long recordId);

    List<RecordVersionVO> queryByProductId(Long productId);

    default BatchRecordVersion queryById(Long recordVersionId) {
        return selectOne(new LambdaQueryWrapperX<BatchRecordVersion>().eq(BatchRecordVersion::getId, recordVersionId));
    }

    List<PageRecordAuditVO> queryByVersionIdList( @Param("recordName") String recordName);

    default BatchRecordVersion queryByInstanceId(String processInstanceId) {
        return selectOne(new LambdaQueryWrapperX<BatchRecordVersion>().eq(BatchRecordVersion::getInstanceId, processInstanceId));
    }

    String getRecordName(Long recordId);

    List<String> getAuditBusinessKey(@Param("deptIdList") List<Long> deptIdList);

    default List<BatchRecordVersion> queryVersionByRecordIdList(List<Long> recordIdList) {
        return selectList(new LambdaQueryWrapperX<BatchRecordVersion>().in(BatchRecordVersion::getRecordId, recordIdList));
    }

    default List<BatchRecordVersion> selectListByIds(Set<Long> versionId){
        return selectList(new LambdaQueryWrapperX<BatchRecordVersion>().in(BatchRecordVersion::getId,versionId));
    }

    List<BatchRecordVersion> selectDeleteListByIds(@Param("ids") Set<Long> convertSet);

    BatchRecordVersion selectOneById(@Param("id") Long businessId);
}
