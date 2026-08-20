package com.bmos.lims2.web.inspect.team;

import cn.hutool.core.bean.BeanUtil;
import com.bmos.adaptor.platform.vo.UserInfoVO;
import com.bmos.cache.redis.lock.DistributedLock;
import com.bmos.common.response.ResponseInfo;
import com.bmos.lims2.server.inspect.team.dto.InspectionTeamAssignUserDTO;
import com.bmos.lims2.server.inspect.team.dto.InspectionTeamDTO;
import com.bmos.lims2.server.inspect.team.dto.InspectionTeamPageReqDTO;
import com.bmos.lims2.server.inspect.team.dto.InspectionTeamSaveDTO;
import com.bmos.lims2.server.inspect.team.dto.InspectionTeamUpdateDTO;
import com.bmos.lims2.server.inspect.team.service.InspectionTeamService;
import com.bmos.lims2.server.platform.util.UserUtils;
import com.bmos.lims2.web.inspect.team.vo.req.InspectionTeamAssignUserVO;
import com.bmos.lims2.web.inspect.team.vo.req.InspectionTeamPageReqVO;
import com.bmos.lims2.web.inspect.team.vo.req.InspectionTeamSaveVO;
import com.bmos.lims2.web.inspect.team.vo.req.InspectionTeamUpdateVO;
import com.bmos.lims2.web.inspect.team.vo.req.TeamUserBySchemeItemReqVO;
import com.bmos.lims2.web.inspect.team.vo.resp.TeamVO;
import com.bmos.lims2.web.inspect.team.vo.resp.TeamUserRespVO;
import com.bmos.mybatis.dataobject.BaseUserDO;
import com.bmos.mybatis.page.CommonPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Api(tags = "班组管理")
@Validated
@RestController
@RequestMapping("/team")
public class InspectionTeamController {

    @Resource
    private InspectionTeamService inspectionTeamService;

    @GetMapping("/users/by-scheme-and-item")
    @ApiOperation("按方案版本ID与检验项目ID查询班组人员列表")
    public ResponseInfo<List<TeamUserRespVO>> listUsersBySchemeAndItem(@Validated TeamUserBySchemeItemReqVO reqVO) {
        List<com.bmos.lims2.server.inspect.team.dto.InspectionTeamUserDTO> dtos =
                inspectionTeamService.listUsersBySchemeVersionAndInspectItem(reqVO.getSchemeVersionId(), reqVO.getInspectItemId());
        List<TeamUserRespVO> result = dtos.stream().map(d -> {
            TeamUserRespVO vo = new TeamUserRespVO();
            vo.setUserId(d.getUserId());
            vo.setUserName(d.getUserName());
            vo.setTeamId(d.getTeamId());
            vo.setTeamName(d.getTeamName());
            vo.setLoginName(d.getLoginName());
            return vo;
        }).collect(Collectors.toList());
        return ResponseInfo.success(result);
    }

    @PostMapping("/save")
    @ApiOperation("班组管理-新建")
    @DistributedLock(expression = "#saveVO.code")
    public ResponseInfo<Void> saveInspectionTeam(@RequestBody @Validated InspectionTeamSaveVO saveVO) {
        inspectionTeamService.saveInspectionTeam(BeanUtil.copyProperties(saveVO, InspectionTeamSaveDTO.class));
        return ResponseInfo.success();
    }

    @PutMapping("/update")
    @ApiOperation("班组管理-编辑")
    @DistributedLock(expression = "#updateVO.code")
    public ResponseInfo<Void> updateInspectionTeam(@RequestBody @Validated InspectionTeamUpdateVO updateVO) {
        inspectionTeamService.updateInspectionTeam(BeanUtil.copyProperties(updateVO, InspectionTeamUpdateDTO.class));
        return ResponseInfo.success();
    }

    @GetMapping("/page")
    @ApiOperation("班组管理-分页")
    public ResponseInfo<CommonPage<InspectionTeamDTO>> getInspectionTeamPage(@Validated InspectionTeamPageReqVO pageReqVO) {
        return ResponseInfo.success(inspectionTeamService.getInspectionTeamPage(BeanUtil.copyProperties(pageReqVO, InspectionTeamPageReqDTO.class)));
    }

    @PutMapping("/enable/{id}")
    @ApiOperation("班组管理-启用")
    @ApiParam(name = "id", value = "班组id", required = true)
    public ResponseInfo<Void> enableInspectionTeam(@PathVariable Long id) {
        inspectionTeamService.enableInspectionTeam(id);
        return ResponseInfo.success();
    }

    @PutMapping("/disable/{id}")
    @ApiOperation("班组管理-停用")
    @ApiParam(name = "id", value = "班组id", required = true)
    public ResponseInfo<Void> disableInspectionTeam(@PathVariable Long id) {
        inspectionTeamService.disableInspectionTeam(id);
        return ResponseInfo.success();
    }

    @ApiOperation("班组管理-人员分配")
    @PostMapping("/assign")
    public ResponseInfo<Void> inspectionTeamAssignUser(@RequestBody @Validated InspectionTeamAssignUserVO teamAssignUserVO) {
        inspectionTeamService.inspectionTeamAssignUser(BeanUtil.copyProperties(teamAssignUserVO, InspectionTeamAssignUserDTO.class));
        return ResponseInfo.success();
    }

    @GetMapping("/users/{id}")
    @ApiOperation("班组管理-人员列表")
    public ResponseInfo<List<UserInfoVO>> getInspectionTeamUserList(@PathVariable Long id) {
        List<UserInfoVO> result = inspectionTeamService.getInspectionTeamUserIdList(id)
                .stream()
                .map(userId -> {
                    BaseUserDO user = UserUtils.getUser(userId);
                    UserInfoVO userInfoVO = new UserInfoVO();
                    userInfoVO.setUserId(userId);
                    Optional.ofNullable(user).ifPresent(item -> {
                        userInfoVO.setUserName(item.getUserName());
                        userInfoVO.setLoginName(item.getLoginName());
                    });
                    return userInfoVO;
                })
                .collect(Collectors.toList());
        return ResponseInfo.success(result);
    }


    @GetMapping("/team/list/permission")
    @ApiOperation("获取用户有权限的班组列表")
    public ResponseInfo<List<InspectionTeamDTO>> getUserTeamList() {
    	List<InspectionTeamDTO> result = inspectionTeamService.getTeamListByPermission();
    	return ResponseInfo.success(result);
    }
}
