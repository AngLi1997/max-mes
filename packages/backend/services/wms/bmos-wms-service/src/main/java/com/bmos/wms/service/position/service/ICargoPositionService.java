package com.bmos.wms.service.position.service;

import com.bmos.mybatis.page.CommonPage;
import com.bmos.wms.service.platform.user.vo.PlatformUserVO;
import com.bmos.wms.service.position.dto.CargoPositionCreateDTO;
import com.bmos.wms.service.position.dto.CargoPositionPageQuery;
import com.bmos.wms.service.position.model.CargoPosition;
import com.bmos.wms.service.position.vo.CargoPositionVO;
import com.bmos.wms.service.storage.dto.MaterialPositionEditDTO;
import org.apache.ibatis.annotations.Param;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.List;

/**
 * 暂存货位 service
 *
 * @author liang
 * @version 1.0.0
 * @date 2024/2/18 17:52
 */
public interface ICargoPositionService {

    /**
     * 新建暂存货位
     *
     * @param dto
     */
    void createCargoPosition(CargoPositionCreateDTO dto);

    /**
     * 编辑暂存货位
     *
     * @param dto
     */
    void editCargoPosition(MaterialPositionEditDTO dto);

    /**
     * 根据id查询暂存货位详情
     *
     * @param id 暂存货位id
     * @return
     */
    @Nullable
    CargoPositionVO queryInfoById(Long id);

    /**
     * 分页查询暂存货位
     *
     * @param pageQuery 分页参数
     * @return 暂存货位分页数据
     */
    CommonPage<CargoPositionVO> queryPage(CargoPositionPageQuery pageQuery);

    /**
     * 启用货位
     *
     * @param id
     */
    void enableCargoPosition(Long id);

    /**
     * 停用货位
     *
     * @param id
     */
    void disableCargoPosition(Long id);

    /**
     * 删除货位
     *
     * @param id
     */
    void deleteCargoPosition(Long id);

    List<PlatformUserVO> queryPositionBoundUserList(Long positionId);

    /**
     * 根据存储区域查询货位信息（包含所有下级）
     *
     * @param storageId
     * @return
     */
    List<CargoPosition> queryAllEnabledChildrenByStorageId(Long storageId);

    /**
     * 根据货位编码查询货位信息
     *
     * @param code 货位编码
     * @return
     */
    @Nullable
    CargoPositionVO queryInfoByCode(String code);

    /**
     * 根据货位id查询货位信息(带有权限)
     *
     * @param positionId 货位id
     * @return
     */
    @Nullable
    CargoPosition getByIdWithPermission(Long positionId);

    /**
     * 根据id查询货位路径
     *
     * @param id
     * @return
     */
    String getCargoPositionPath(@Param("id") Long id);

    Collection<CargoPosition> selectBatchIds(List<Long> positionIds);
}
