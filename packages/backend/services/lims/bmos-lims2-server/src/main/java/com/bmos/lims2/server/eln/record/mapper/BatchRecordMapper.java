package com.bmos.lims2.server.eln.record.mapper;

import com.baomidou.mybatisplus.extension.toolkit.Db;
import com.bmos.common.exception.BmosException;
import com.bmos.lims2.common.i18n.LimsResponseCode;
import com.bmos.lims2.server.eln.record.dto.BatchRecordSaveDTO;
import com.bmos.lims2.server.eln.record.dto.RecordListQueryDTO;
import com.bmos.lims2.server.eln.record.entity.BatchRecord;
import com.bmos.lims2.server.eln.record.vo.RecordListVO;
import com.bmos.mybatis.mapper.BaseMapperX;
import com.bmos.mybatis.query.LambdaQueryWrapperX;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;


@Mapper
public interface BatchRecordMapper extends BaseMapperX<BatchRecord> {


    default List<BatchRecord> getRecordList(String id) {
        return selectList(new LambdaQueryWrapperX<BatchRecord>()
                .eq(BatchRecord::getCategoryId, id));
    }

    default boolean existsByCode(String code, Long excludeId) {
        return selectCount(new LambdaQueryWrapperX<BatchRecord>()
                .eq(BatchRecord::getCode, code)
                .eq(BatchRecord::getDeleted, Boolean.FALSE)
                .ne(excludeId != null, BatchRecord::getId, excludeId)) > 0;
    }

    default Boolean saveOrUpdateRecord(BatchRecordSaveDTO dto) {
        try {
            BatchRecord record = new BatchRecord();
            record.setCategoryId(dto.getCategoryId());
            record.setName(dto.getName());
            record.setId(dto.getRecordId());
            record.setCode(dto.getCode());
            return Db.saveOrUpdate(record);
        } catch (Exception e) {
            throw new BmosException(LimsResponseCode.RECORD_CODE_DUPLICATE);
        }
    }

    List<RecordListVO> getRecordPage(RecordListQueryDTO dto);

    List<RecordListVO> getFirstRecord(RecordListQueryDTO dto);

    String queryNameByVersionId(Long versionId);

    List<BatchRecord> selectByRecordIdList(@Param("recordIdList") List<Long> recordIdList,@Param("deptIds") List<Long> deptIds);

    List<BatchRecord> selectListWithPermission(@Param("deptIds") List<Long> longs);
}
