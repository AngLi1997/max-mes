package com.bmos.platform.service.system.menu.service;

import com.bmos.platform.facade.system.menu.vo.MenuVO;
import com.bmos.platform.service.system.menu.dto.CurrentMenuTreeQueryDTO;
import com.bmos.platform.service.system.menu.dto.MenuSaveDTO;
import com.bmos.platform.service.system.menu.dto.MenuUpdateDTO;
import com.bmos.platform.service.system.menu.model.Menu;
import com.bmos.platform.service.system.menu.vo.FunctionVO;
import com.bmos.platform.service.system.menu.vo.MenuListVO;
import com.bmos.platform.service.system.menu.vo.MenuTreeVO;
import com.bmos.platform.service.system.role.dto.RoleRelateMenuSaveItemDTO;
import com.bmos.platform.service.system.role.vo.RoleTreeNodeVO;
import org.springframework.validation.annotation.Validated;

import java.util.Collection;
import java.util.List;

@Validated
public interface MenuService {


    List<MenuTreeVO> treeAll(String rootMenuCode);

    List<FunctionVO> getFunction(Long menuId, Long roleId);

    void save(MenuSaveDTO dto);

    void update(MenuUpdateDTO dto);

    void delete(Long id);

    List<Long> relateRoleData(Long menuId);

    List<MenuListVO> getAdminMenuRootList();

    List<MenuListVO> getMenuRootList();

    void saveMenuRole(@Validated RoleRelateMenuSaveItemDTO dto);

    List<MenuTreeVO> getCurrentMenuTree(CurrentMenuTreeQueryDTO dto);

    void saveAuthMenuRole(RoleRelateMenuSaveItemDTO dto);

    List<MenuTreeVO> getAuthMenuTree(String rootMenuCode,Boolean containsFunc);

    List<MenuTreeVO> getOperationMenuAll();

    List<RoleTreeNodeVO> getMenuRoleTree(Long menuId);

    List<Menu> getByCodes(Collection<String> menuCodeSet);

    /**
     * 根据menuid获取所有子集
     *
     * @param menuId
     * @return
     */
    List<MenuVO> getAllChildMenuIdList(Long menuId);

    /**
     * 【角色管理-权限授权】获取某个菜单下所有的功能并且标识当前角色id是否有功能的权限授权
     * @param menuId
     * @param roleId
     * @return
     */
    List<FunctionVO> getRoleAuthFunction(Long menuId, Long roleId);

    /**
     * 【角色管理】权限授权显示的菜单树
     * @return
     */
    List<MenuTreeVO> getMenuTree();

    /**
     * 获取当前人所有的菜单
     * @param dto
     * @return
     */
    List<MenuTreeVO> getCurrentAllMenu(CurrentMenuTreeQueryDTO dto);
}
