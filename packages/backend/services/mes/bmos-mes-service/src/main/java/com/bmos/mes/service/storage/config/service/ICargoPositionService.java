package com.bmos.mes.service.storage.config.service;

import com.bmos.mes.service.platform.user.vo.PlatformUserVO;
import com.bmos.mes.service.storage.config.dto.CargoPositionCreateDTO;
import com.bmos.mes.service.storage.config.dto.CargoPositionPageQuery;
import com.bmos.mes.service.storage.config.dto.MaterialPositionEditDTO;
import com.bmos.mes.service.storage.config.model.CargoPosition;
import com.bmos.mes.service.storage.config.vo.CargoPathVO;
import com.bmos.mes.service.storage.config.vo.CargoPositionVO;
import com.bmos.mybatis.page.CommonPage;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.List;
import java.util.Set;

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
     * 根据暂存间查询货位信息（包含所有下级）
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

    List<CargoPathVO> getCargoPositionPathMap(List<Long> longs, String slash);

    /**
     * 根据货位编码查询货位信息(带有权限)
     *
     * @param code 货位编码
     * @return
     */
    @Nullable
    CargoPosition getByCodeWithPermission(String code);

    List<CargoPosition> getByIdList(Collection<Long> longs);

    List<PlatformUserVO> queryPositionBoundUserListByPermissionCode(Long positionId, Long permissionCode);
}
