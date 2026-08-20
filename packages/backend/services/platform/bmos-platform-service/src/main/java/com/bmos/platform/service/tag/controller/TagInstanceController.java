package com.bmos.platform.service.tag.controller;

import com.bmos.common.response.ResponseInfo;
import com.bmos.logging.annotation.OperationLog;
import com.bmos.mybatis.page.CommonPage;
import com.bmos.platform.service.tag.dto.*;
import com.bmos.platform.service.tag.service.ITagInstanceService;
import com.bmos.platform.service.tag.vo.TagInstanceDetailVO;
import com.bmos.platform.service.tag.vo.TagInstancePageVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 标签实例controller
 *
 * @author liang
 * @version 1.0.0
 * @date 2024/3/7 10:12
 */

@RestController
@RequestMapping("/tag/instance")
@Api(tags = "标签实例接口")
public class TagInstanceController {

    @Resource
    private ITagInstanceService tagInstanceService;

    @GetMapping("/queryPage")
    @ApiOperation(value = "查询标签实例分页")
    public ResponseInfo<CommonPage<TagInstancePageVO>> queryPage(@Validated TagInstancePageQuery pageQuery) {
        return ResponseInfo.success(tagInstanceService.queryPage(pageQuery));
    }

    @GetMapping("/info")
    @ApiOperation("根据id查询标签实例详情")
    @ApiImplicitParam(name = "id", value = "标签实例id", required = true, example = "1")
    public ResponseInfo<TagInstanceDetailVO> queryInfoById(@RequestParam Long id) {
        return ResponseInfo.success(tagInstanceService.queryInfoById(id));
    }

    @PostMapping("/preview")
    @ApiModelProperty("预览标签")
    public void previewTag(@RequestBody PrintPreviewDTO previewDTO, HttpServletResponse response) throws Exception {
        tagInstanceService.previewTag(previewDTO, response);
    }

    @PostMapping("/create")
    @ApiOperation(value = "创建标签实例")
    @OperationLog
    public ResponseInfo<Void> createTagInstance(@RequestBody @Validated(TagInstanceDTO.Create.class) TagInstanceDTO dto) {
        tagInstanceService.createTagInstance(dto);
        return ResponseInfo.success();
    }

    @PutMapping("/edit")
    @ApiOperation("编辑标签实例")
    @OperationLog
    public ResponseInfo<Void> editTagInstance(@RequestBody @Validated(TagInstanceDTO.Update.class) TagInstanceDTO dto) {
        tagInstanceService.editTagInstance(dto);
        return ResponseInfo.success();
    }

    @PutMapping("/enable")
    @ApiOperation("启用标签实例")
    @ApiImplicitParam(name = "id", value = "标签实例id", required = true, example = "1")
    @OperationLog
    public ResponseInfo<Void> enableTagInstance(@RequestParam Long id) {
        tagInstanceService.enableTagInstance(id);
        return ResponseInfo.success();
    }

    @PutMapping("/disable")
    @ApiOperation("停用标签实例")
    @ApiImplicitParam(name = "id", value = "标签实例id", required = true, example = "1")
    @OperationLog
    public ResponseInfo<Void> disableTagInstance(@RequestParam Long id) {
        tagInstanceService.disableTagInstance(id);
        return ResponseInfo.success();
    }

    @DeleteMapping("/delete")
    @ApiOperation("删除标签实例")
    @ApiImplicitParam(name = "id", value = "标签实例id", required = true, example = "1")
    @OperationLog
    public ResponseInfo<Void> deleteTagInstance(@RequestParam Long id) {
        tagInstanceService.deleteTagInstance(id);
        return ResponseInfo.success();
    }

    @PostMapping("/print")
    @ApiModelProperty("打印标签")
    @OperationLog
    public ResponseInfo<Void> printTag(@RequestBody PrintCommonDTO printerDTO) {
        tagInstanceService.printTag(printerDTO);
        return ResponseInfo.success();
    }

    @PostMapping("/printBatch")
    @ApiModelProperty("打印批量标签")
    @OperationLog
    public ResponseInfo<Void> printBatchTag(@RequestBody List<PrintCommonDTO> printerDTOList) {
        tagInstanceService.printBatchTag(printerDTOList);
        return ResponseInfo.success();
    }

    @PostMapping("/printBatchSameDevice")
    @ApiModelProperty("打印批量标签 - 同一打印机")
    public ResponseInfo<Void> printBatchTags(@RequestBody PrintBatchDTO printBatchDTO) {
        tagInstanceService.printBatchTags(printBatchDTO);
        return ResponseInfo.success();
    }
}
