package com.bmos.platform.service.dict.controller;

import com.bmos.common.response.ResponseInfo;
import com.bmos.logging.annotation.OperationLog;
import com.bmos.mybatis.page.CommonPage;
import com.bmos.platform.facade.dict.vo.DictDetailFeignVO;
import com.bmos.platform.service.dict.dto.*;
import com.bmos.platform.service.dict.service.DictDetailService;
import com.bmos.platform.service.dict.service.DictService;
import com.bmos.platform.service.dict.vo.DictDetailListVO;
import com.bmos.platform.service.dict.vo.DictListVO;
import com.bmos.platform.service.dict.vo.DictVO;
import com.bmos.platform.service.dict.vo.DictWatchVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author renjinguang
 */
@RestController
@RequestMapping("/dict")
@Validated
@Api(tags = "字典配置相关接口")
public class DictController {

    @Autowired
    private DictService dictService;

    @Autowired
    private DictDetailService detailService;

    @GetMapping("/list/dict")
    @ApiOperation(value = "查询字典列表")
    public ResponseInfo<CommonPage<DictListVO>> listDict(DictListQueryDTO dto) {
        return ResponseInfo.success(CommonPage.convertPage(dictService.listDict(dto)));
    }

    @PostMapping("/save/dict")
    @ApiOperation(value = "添加字典数据")
    @OperationLog
    public ResponseInfo<Boolean> saveDict(@Validated @RequestBody SaveDictDTO dto) {
        return ResponseInfo.success(dictService.saveDict(dto));
    }

    @GetMapping("/list/dict/detail")
    @ApiOperation(value = "查询字典详情数据")
    public ResponseInfo<CommonPage<DictDetailListVO>> listDictDetail(@Validated DictDetailListQueryDTO dto) {
        return ResponseInfo.success(CommonPage.convertPage(detailService.listDictDetail(dto)));
    }

    @GetMapping("/delete/dict")
    @ApiOperation(value = "根据id删除字典")
    @ApiParam(name = "id", value = "字典id", required = true)
    @OperationLog
    public ResponseInfo<Boolean> deleteDict(@NotNull Long id) {
        return ResponseInfo.success(dictService.deleteDict(id));
    }

    @PostMapping("/update/dict")
    @ApiOperation(value = "根据id编辑字典")
    @OperationLog
    public ResponseInfo<Boolean> updateDict(@Validated @RequestBody UpdateDictDTO dto) {
        return ResponseInfo.success(dictService.updateDict(dto));
    }

    @GetMapping("/watch/dict")
    @ApiOperation(value = "查看字典信息")
    @ApiParam(name = "id", value = "字典id", required = true)
    public ResponseInfo<DictWatchVO> watchDict(@NotNull Long id) {
        return ResponseInfo.success(dictService.watchDict(id));
    }

    @PostMapping("/update/dict/detail")
    @ApiOperation(value = "编辑字典数据")
    @OperationLog
    public ResponseInfo<Boolean> updateDictDetail(@Validated @RequestBody UpdateDetailDTO dto) {
        return ResponseInfo.success(detailService.updateDictDetail(dto));
    }

    @GetMapping("/delete/dict/detail")
    @ApiOperation(value = "删除字典详情数据")
    @ApiParam(name = "id", value = "字典id", required = true)
    @OperationLog
    public ResponseInfo<Boolean> deleteDictDetail(@NotNull Long id) {
        return ResponseInfo.success(detailService.deleteDictDetail(id));
    }

    @GetMapping("/watch/dict/detail")
    @ApiOperation(value = "查看字典详情")
    @ApiParam(name = "id", value = "字典数据表id", required = true)
    public ResponseInfo<DictDetailListVO> watchDictDetail(@NotNull Long id) {
        return ResponseInfo.success(detailService.watchDictDetail(id));
    }

    @PostMapping("/save/dict/detail")
    @ApiOperation(value = "添加字典数据")
    @OperationLog
    public ResponseInfo<Boolean> saveDictDetail(@Validated @RequestBody SaveDictDetailDTO dto) {
        return ResponseInfo.success(detailService.saveDictDetail(dto));
    }

    @GetMapping("/list/dict/down")
    @ApiOperation(value = "查询字典下拉框")
    @ApiParam(name = "dictId", value = "字典表id,查询字典数据时使用")
    public ResponseInfo<List<DictVO>> listDictDown(Long dictId) {
        return ResponseInfo.success(dictService.listDictDown(dictId));
    }

    @GetMapping("/list/dict/code")
    @ApiOperation(value = "根据code查询二级列表数据")
    @ApiParam(name = "code", value = "二级列表code")
    public ResponseInfo<List<DictVO>> queryDictDetailByCode(@Validated @NotBlank String code){
        return ResponseInfo.success(dictService.queryDictDetailByCode(code));
    }

    @GetMapping("/list/codeList")
    @ApiOperation(value = "根据code列表查询字典数据")
    public ResponseInfo<List<DictDetailFeignVO>> selectDictByCategory(@RequestParam("dictTypeList") List<String> dictTypeList) {
        return ResponseInfo.success(dictService.selectDictByCategory(dictTypeList));
    }
}
