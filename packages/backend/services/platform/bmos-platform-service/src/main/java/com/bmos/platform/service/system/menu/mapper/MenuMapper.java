package com.bmos.platform.service.system.menu.mapper;

import com.bmos.mybatis.mapper.BaseMapperX;
import com.bmos.mybatis.query.LambdaQueryWrapperX;
import com.bmos.platform.service.system.menu.dto.CurrentMenuTreeQueryDTO;
import com.bmos.platform.service.system.menu.model.Menu;
import com.bmos.platform.service.system.menu.vo.FunctionVO;
import com.bmos.platform.service.system.menu.vo.MenuTreeVO;
import com.bmos.platform.service.system.menu.vo.TerminalTypeVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import static com.bmos.platform.service.system.menu.constant.MenuConstant.*;

@Mapper
public interface MenuMapper extends BaseMapperX<Menu> {

    List<MenuTreeVO> selectAllMenu(@Param("rootMenuCode") String rootMenuCode);

    List<FunctionVO> selectFunction(@Param("menuId") Long menuId,
                                    @Param("roleId") Long roleId,
                                    @Param("notMenu") Integer notMenu);

    /**
     * 查询 菜单列表
     *
     * @param userId  用户id
     * @param idRange 菜单id范围，用小于等于判断，控制id位数
     * @return 菜单列表
     */
    List<Menu> selectListByUserId(@Param("userId") String userId,
                                  @Param("idRange") Long idRange);

    List<Long> selectType();

    List<TerminalTypeVO> selectHighLevel(@Param("parentId") Long parentId,
                                         @Param("isMenu") Integer isMenu);

    List<Long> getMenuIdList(@Param("tabIdList") List<Long> tabIdList);

    default List<Menu> selectRootMenuList() {
        //根节点 固定只有 3 位，写死 小于等于 999
        return selectList(new LambdaQueryWrapperX<Menu>().le(Menu::getId, MAX_ROOT_ID));
    }

    List<Menu> selectMenuList(@Param("userId") String userId,
                              @Param("dto") CurrentMenuTreeQueryDTO dto,
                              @Param("idRange")Long idRange);

    List<Menu> selectMenuAdminList(@Param("dto") CurrentMenuTreeQueryDTO dto,
                                   @Param("idRange")Long idRange);

    List<MenuTreeVO> selectAllMenuFilterPermission();

    /**
     * 根据菜单id查询当前菜单下的所有功能
     * @param menuId
     * @return
     */
    default List<Menu> selectMenuFunction(Long menuId, Boolean function){
        LambdaQueryWrapperX<Menu> wrapperX = new LambdaQueryWrapperX<>();
        wrapperX.eq(Menu::getParentId, menuId);
        if (function){
            wrapperX.eq(Menu::getIsMenu, false);
        } else {
            wrapperX.eq(Menu::getIsMenu, true);
        }
        return selectList(wrapperX);
    }

    /**
     * 查询当前菜单下的所有子菜单
     * @param parentMenu
     * @return
     */
    default List<Menu> selectByParentId(Long parentMenu){
        return selectList(new LambdaQueryWrapperX<Menu>().eq(Menu::getParentId, parentMenu));
    }

    /**
     * 查询Admin用户的菜单
     * @return
     */
    default List<Menu> selectAdminMenuIdSet(Boolean containsFunc){
        LambdaQueryWrapperX<Menu> wrapperX = new LambdaQueryWrapperX<>();
        if (!containsFunc){
            wrapperX.eq(Menu::getIsMenu, IS_MENU);
        }
        return selectList(wrapperX);
    }

    default List<Menu> selectMenuIdList(Set<Long> curLoginUserAuthMenuIdSet, Boolean containsFunc){
        LambdaQueryWrapperX<Menu> wrapperX = new LambdaQueryWrapperX<Menu>().in(Menu::getId, curLoginUserAuthMenuIdSet);
        if (!containsFunc){
            wrapperX.eq(Menu::getIsMenu, IS_MENU);
        }
        return selectList(wrapperX);
    }

    default List<Menu> selectByParentIdList(Set<Long> deleteRootMenuIdSet){
        return selectList(new LambdaQueryWrapperX<Menu>().in(Menu::getParentId, deleteRootMenuIdSet));
    }

    /**
     * 根据菜单编码查询菜单信息
     * @param code
     * @return
     */
    default Menu selectByCode(String code){
        return selectOne(new LambdaQueryWrapperX<Menu>().eq(Menu::getCode, code));
    }

    default List<Menu> getByCodes(Collection<String> menuCodeSet){
        return selectList(new LambdaQueryWrapperX<Menu>().in(Menu::getCode, menuCodeSet));
    }

    List<FunctionVO> selectAuthFunction(@Param("menuId") Long menuId, @Param("roleId") Long roleId, @Param("notMenu") Integer notMenu);

    default List<Menu> selectAllMenuList(Boolean funcFlg){
        if (!funcFlg){
            return selectList(new LambdaQueryWrapperX<Menu>().eq(Menu::getIsMenu, IS_MENU));
        } else {
            return selectList(new LambdaQueryWrapperX<Menu>().eq(Menu::getIsMenu, NOT_MENU));
        }
    }
}
