package com.bmos.lims2.server.inspect.item.mapper;

import com.bmos.lims2.server.inspect.item.dto.InspectItemParamDTO;
import com.bmos.lims2.server.inspect.item.dto.InspectItemWithParameterDTO;
import com.bmos.lims2.server.inspect.item.entity.InspectItem;
import com.bmos.mybatis.mapper.BaseMapperX;
import com.bmos.mybatis.query.LambdaQueryWrapperX;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 检验项目(BmExperimentInspect)表数据库访问层
 *
 * @author makejava
 * @since 2024-03-02 13:22:17
 */
@Mapper
public interface InspectItemMapper extends BaseMapperX<InspectItem> {

    /**
     * 根据查询参数查询检验项目信息
     *
     * @param inspectParam
     * @return
     */
    List<InspectItemWithParameterDTO> selectByParam(@Param("param") InspectItemParamDTO inspectParam);

    /**
     * 仅分页查询检验项ID（用于两步分页的第一步）
     */
    List<Long> selectIdsByParam(@Param("param") InspectItemParamDTO inspectParam);

    /**
     * 根据ID集合查询检验项详情（包含分析项），不分页
     */
    List<InspectItemWithParameterDTO> selectDetailsByIds(@Param("ids") List<Long> ids);

    /**
     * 验证分析想编码是否存在
     *
     * @param id
     * @return
     */
    default boolean existById(Long id) {
        return exists(new LambdaQueryWrapperX<InspectItem>()
                .eq(InspectItem::getId, id));
    }

    /**
     * 验证分析想编码是否存在
     *
     * @param code
     * @return
     */
    default boolean existByCode(String code) {
        return exists(new LambdaQueryWrapperX<InspectItem>()
                .eq(InspectItem::getCode, code));
    }
}

