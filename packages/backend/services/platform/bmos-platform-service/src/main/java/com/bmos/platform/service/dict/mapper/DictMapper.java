package com.bmos.platform.service.dict.mapper;

import com.baomidou.mybatisplus.extension.toolkit.Db;
import com.bmos.mybatis.mapper.BaseMapperX;
import com.bmos.mybatis.query.LambdaQueryWrapperX;
import com.bmos.platform.service.dict.dto.DictListQueryDTO;
import com.bmos.platform.service.dict.model.Dict;
import com.bmos.platform.service.dict.vo.DictListVO;
import com.bmos.platform.service.dict.vo.DictVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author renjinguang
 */
@Mapper
public interface DictMapper extends BaseMapperX<Dict> {

    List<DictListVO> listDict(DictListQueryDTO dto);

    default Boolean saveDict(Dict dict){
        return Db.saveOrUpdate(dict);
    }

    void deleteDict(@Param("id") Long id,@Param("userId") String userId);

    default Boolean updateDict(Dict dict){
        return Db.saveOrUpdate(dict);
    }

    default Dict watchDict(Long id){
        return selectOne(new LambdaQueryWrapperX<Dict>().eq(Dict::getId,id));
    }

    List<DictVO> listDictDown();

    default Dict selectDict(Long id){
        return selectOne(new LambdaQueryWrapperX<Dict>().eq(Dict::getId,id));
    }

    default Dict queryDictBycode(String code){
        return selectOne(new LambdaQueryWrapperX<Dict>().eq(Dict::getDictCode,code));
    }

    default List<Dict> selectByCodeList(List<String> codeList){
        return selectList(new LambdaQueryWrapperX<Dict>()
                .in(Dict::getDictCode, codeList));
    }
}
