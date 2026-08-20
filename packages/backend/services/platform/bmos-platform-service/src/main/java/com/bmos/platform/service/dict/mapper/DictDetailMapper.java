package com.bmos.platform.service.dict.mapper;

import com.baomidou.mybatisplus.extension.toolkit.Db;
import com.bmos.mybatis.mapper.BaseMapperX;
import com.bmos.mybatis.query.LambdaQueryWrapperX;
import com.bmos.platform.service.dict.dto.DictDetailListQueryDTO;
import com.bmos.platform.service.dict.model.DictDetail;
import com.bmos.platform.service.dict.vo.DictDetailListVO;
import com.bmos.platform.service.dict.vo.DictVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author renjinguang
 */
@Mapper
public interface DictDetailMapper extends BaseMapperX<DictDetail> {

    default boolean saveDictDetail(List<DictDetail> dictDetails) {
        return Db.saveOrUpdateBatch(dictDetails);
    }

    List<DictDetailListVO> listDictDetail(DictDetailListQueryDTO dto);

    default Boolean updateDictDetail(DictDetail detail) {
        return Db.saveOrUpdate(detail);
    }


    default DictDetail watchDictDetail(Long id) {
        return selectOne(new LambdaQueryWrapperX<DictDetail>().eq(DictDetail::getId, id));
    }

    default Boolean saveDictDetailOne(DictDetail detail) {
        return Db.saveOrUpdate(detail);
    }

    List<DictVO> listDictDetailDown(Long dictId);

    default List<DictDetail> queryListById(Long id) {
        return selectList(new LambdaQueryWrapperX<DictDetail>().eq(DictDetail::getDictId, id));
    }

    default Boolean updateList(List<DictDetail> list) {
        return Db.saveOrUpdateBatch(list);
    }

    default Boolean deleteByIdList(List<Long> dictIdList){
        return Db.removeByIds(dictIdList,DictDetail.class);
    }

    void deleteDictDetail(@Param("id") Long id,@Param("userId") String userId);

    default List<DictDetail> selectDetailList(Long dictId){
        return selectList(new LambdaQueryWrapperX<DictDetail>()
                .eq(DictDetail::getDictId,dictId));
    }

    default List<DictDetail> selectDetailByDictIdList(List<Long> dictIdList){
        return selectList(new LambdaQueryWrapperX<DictDetail>()
                .in(DictDetail::getDictId,dictIdList));
    }
}
