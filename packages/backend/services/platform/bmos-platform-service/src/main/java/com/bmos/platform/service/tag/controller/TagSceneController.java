package com.bmos.platform.service.tag.controller;

import com.bmos.common.response.ResponseInfo;
import com.bmos.platform.service.tag.service.ITagSceneService;
import com.bmos.platform.service.tag.vo.TagSceneDetailVO;
import com.bmos.platform.service.tag.vo.TagSceneVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * 标签场景controller
 *
 * @author liang
 * @version 1.0.0
 * @date 2024/3/7 10:12
 */

@RestController
@RequestMapping("/tag/scene")
@Api(tags = "标签场景接口")
public class TagSceneController {

    @Resource
    private ITagSceneService tagSceneService;

    @GetMapping("/listByTypeId")
    @ApiOperation(value = "根据标签类型id查询标签场景列表")
    @ApiImplicitParam(name = "typeId", value = "标签类型id", required = true, example = "1")
    public ResponseInfo<List<TagSceneVO>> listAllTagSceneByTypeId(@RequestParam @Validated Long typeId) {
        return ResponseInfo.success(tagSceneService.listTagSceneByTypeId(typeId));
    }

    @GetMapping("/info")
    @ApiOperation(value = "根据id查询标签定义详情")
    @ApiImplicitParam(name = "tagSceneId", value = "标签场景id", required = true, example = "1")
    public ResponseInfo<TagSceneDetailVO> queryInfoById(@RequestParam @Validated Long tagSceneId) {
        return ResponseInfo.success(tagSceneService.queryInfoById(tagSceneId));
    }
}
