package com.bmos.platform.service.tag.controller;

import com.bmos.common.response.ResponseInfo;
import com.bmos.platform.service.tag.service.ITagDefineService;
import com.bmos.platform.service.tag.vo.TagDefineVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * 标签定义controller
 *
 * @author liang
 * @version 1.0.0
 * @date 2024/3/7 10:12
 */

@RestController
@RequestMapping("/tag/define")
@Api(tags = "标签定义接口")
public class TagDefineController {

    @Resource
    private ITagDefineService tagDefineService;

    @GetMapping("/listAll")
    @ApiOperation(value = "查询标签定义列表")
    public ResponseInfo<List<TagDefineVO>> listAllTagDefine() {
        return ResponseInfo.success(tagDefineService.listAllTagDefine());
    }
}
