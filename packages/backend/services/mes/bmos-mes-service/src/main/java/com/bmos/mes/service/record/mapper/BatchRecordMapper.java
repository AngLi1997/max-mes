package com.bmos.mes.service.record.mapper;

import com.baomidou.mybatisplus.extension.toolkit.Db;
import com.bmos.common.exception.BmosException;
import com.bmos.mes.common.exception.MesResponseCode;
import com.bmos.mes.service.record.dto.BatchRecordSaveDTO;
import com.bmos.mes.service.record.dto.RecordListQueryDTO;
import com.bmos.mes.service.record.model.BatchRecord;
import com.bmos.mes.service.record.vo.RecordListVO;
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

    default Boolean saveOrUpdateRecord(BatchRecordSaveDTO dto) {
        try {
            BatchRecord record = new BatchRecord();
            record.setCategoryId(dto.getCategoryId());
            record.setName(dto.getName());
            record.setId(dto.getRecordId());
            return Db.saveOrUpdate(record);
        } catch (Exception e) {
            throw new BmosException(MesResponseCode.RECORD_SAVE_ERROR);
        }
    }

    List<RecordListVO> getRecordPage(RecordListQueryDTO dto);

    List<RecordListVO> getFirstRecord(RecordListQueryDTO dto);

    String queryNameByVersionId(Long versionId);

    List<BatchRecord> selectByRecordIdList(@Param("recordIdList") List<Long> recordIdList,@Param("deptIds") List<Long> deptIds);

    List<BatchRecord> selectListWithPermission(@Param("deptIds") List<Long> longs);
}
