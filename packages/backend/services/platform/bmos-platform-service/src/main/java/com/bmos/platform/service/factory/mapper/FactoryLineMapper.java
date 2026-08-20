package com.bmos.platform.service.factory.mapper;

import com.bmos.mybatis.mapper.BaseMapperX;
import com.bmos.mybatis.query.LambdaQueryWrapperX;
import com.bmos.platform.facade.factory.vo.FactoryLineFeignVO;
import com.bmos.platform.service.factory.mapper.param.LineParam;
import com.bmos.platform.service.factory.model.FactoryLine;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 产线(BpFactoryLine)表数据库访问层
 *
 * @author makejava
 * @since 2024-05-21 17:04:56
 */
@Mapper
public interface FactoryLineMapper extends BaseMapperX<FactoryLine> {

    /**
     * 校验编码是否重复
     * @param code
     * @return
     */
    default boolean existsByCode(String code){
        return exists(new LambdaQueryWrapperX<FactoryLine>()
                .eq(FactoryLine::getCode,code));
    }

    List<FactoryLine> selectByParam(@Param("param") LineParam lineParam);


    /**
     * 当前模型id下是否存在产线
     * @param moduleId
     * @return
     */
    default boolean existByModuleId(Long moduleId){
        return exists(new LambdaQueryWrapperX<FactoryLine>()
                .eq(FactoryLine::getModuleId, moduleId));
    }

    List<FactoryLineFeignVO> queryLineListByLineIds(@Param("lineIdList")List<Long> lineIdList);
}

