package com.bmos.lims2.server.inspect.scheme.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bmos.lims2.server.inspect.scheme.entity.InspectionScheme;
import com.bmos.lims2.server.inspect.scheme.dto.response.InspectionSchemeDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 检验方案Mapper接口
 *
 * @author makejava
 * @since 2024-03-20 10:00:00
 */
@Mapper
public interface InspectionSchemeMapper extends BaseMapper<InspectionScheme> {



    /**
     * 检查名称是否重复
     *
     * @param name 名称
     * @param excludeId 排除的ID
     * @return 重复数量
     */
    Integer checkNameDuplicate(@Param("name") String name, @Param("excludeId") Long excludeId);

    /**
     * 分页查询仅包含有生效版本的检验方案
     *
     * @param page 分页参数
     * @param name 方案名称（模糊）
     * @param materialIds 物料ID集合
     * @return 分页结果记录
     */
    List<InspectionSchemeDTO> selectPageWithActiveVersion(
            @Param("name") String name,
            @Param("materialIds") List<Long> materialIds);

    /**
     * 根据物料ID集合查询检验方案列表
     * @param materialIds 物料ID集合
     * @return 方案列表
     */
    List<InspectionScheme> selectByMaterialIds(@Param("materialIds") List<Long> materialIds);

    /**
     * 根据实验包ID查询已绑定该包的方案名称列表
     * @param packageId 实验包ID
     * @return 方案名称列表
     */
    List<String> selectSchemeNamesByPackageId(@Param("packageId") Long packageId);
} 