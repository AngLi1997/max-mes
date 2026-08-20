package com.bmos.platform.service.tag.controller;

import com.bmos.common.response.ResponseInfo;
import com.bmos.platform.service.tag.service.ITagTypeService;
import com.bmos.platform.service.tag.vo.TagTypeVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * 标签类型controller
 *
 * @author liang
 * @version 1.0.0
 * @date 2024/3/7 10:12
 */

@RestController
@RequestMapping("/tag/type")
@Api(tags = "标签类型接口")
public class TagTypeController {

    @Resource
    private ITagTypeService tagTypeService;

    @GetMapping("/listAll")
    @ApiOperation(value = "查询标签分类列表")
    public ResponseInfo<List<TagTypeVO>> listAllTagType() {
        return ResponseInfo.success(tagTypeService.listAllTagType());
    }
}
