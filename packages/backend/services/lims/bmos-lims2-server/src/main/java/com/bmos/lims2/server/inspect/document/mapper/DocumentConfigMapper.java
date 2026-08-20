package com.bmos.lims2.server.inspect.document.mapper;

import cn.hutool.core.util.StrUtil;
import com.bmos.lims2.server.inspect.document.dto.DocumentConfigPageReqDTO;
import com.bmos.lims2.server.inspect.document.entity.DocumentConfig;
import com.bmos.mybatis.mapper.BaseMapperX;
import com.bmos.mybatis.query.LambdaQueryWrapperX;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Objects;


@Mapper
public interface DocumentConfigMapper extends BaseMapperX<DocumentConfig> {

    default List<DocumentConfig> page(DocumentConfigPageReqDTO dto) {
        return selectList(new LambdaQueryWrapperX<DocumentConfig>()
                .like(StrUtil.isNotBlank(dto.getName()), DocumentConfig::getName, dto.getName()));
    }

    default boolean nameExists(String name, Long id) {
        return exists(new LambdaQueryWrapperX<DocumentConfig>()
                .eq(DocumentConfig::getName, name)
                .ne(Objects.nonNull(id), DocumentConfig::getId, id));
    }
}

