package com.bmos.lims2.server.inspect.pack.mapper;

import com.bmos.lims2.server.inspect.pack.dto.InspectPackageWithItemsDTO;
import com.bmos.lims2.server.inspect.pack.dto.InspectPackageFullConfigDTO;
import com.bmos.lims2.server.inspect.pack.dto.PackageParamDTO;
import com.bmos.lims2.server.inspect.pack.entity.InspectPackage;
import com.bmos.mybatis.mapper.BaseMapperX;
import com.bmos.mybatis.query.LambdaQueryWrapperX;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 实验包(BmExperimentPackage)表数据库访问层
 *
 * @author makejava
 * @since 2024-03-02 13:22:35
 */
@Mapper
public interface InspectPackageMapper extends BaseMapperX<InspectPackage> {


    /**
     * 分页查询
     *
     * @param param
     * @return
     */
    List<InspectPackage> selectByParam(@Param("param") PackageParamDTO param);

    /**
     * 分页查询实验包及其关联的检验项目信息
     *
     * @param param
     * @return
     */
    List<InspectPackageWithItemsDTO> selectWithItemsByParam(@Param("param") PackageParamDTO param);

	/**
	 * 仅分页查询实验包ID（用于两步分页的第一步）
	 *
	 * @param param
	 * @return
	 */
	List<Long> selectIdsByParam(@Param("param") PackageParamDTO param);

	/**
	 * 根据ID集合查询实验包详情（包含关联检验项目），不分页
	 *
	 * @param ids
	 * @return
	 */
	List<InspectPackageWithItemsDTO> selectWithItemsByIds(@Param("ids") List<Long> ids);

    /**
     * 验证分析想编码是否存在
     *
     * @param id
     * @return
     */
    default boolean existById(Long id) {
        return exists(new LambdaQueryWrapperX<InspectPackage>()
                .eq(InspectPackage::getId, id));
    }

    /**
     * 验证分析想编码是否存在
     *
     * @param code
     * @return
     */
    default boolean existByCode(String code) {
        return exists(new LambdaQueryWrapperX<InspectPackage>()
                .eq(InspectPackage::getCode, code));
    }

    /**
     * 根据实验包ID查询完整配置信息（包含检项、分析项、数据点）
     *
     * @param packageId 实验包ID
     * @return 完整配置信息
     */
    InspectPackageFullConfigDTO selectFullConfigByPackageId(@Param("packageId") Long packageId);

}

