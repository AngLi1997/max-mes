package com.bmos.mes.service.dataset.controller;

import com.bmos.common.base.enums.CommonEnum;
import com.bmos.common.response.ResponseInfo;
import com.bmos.logging.annotation.OperationLog;
import com.bmos.mes.service.dataset.dto.*;
import com.bmos.mes.service.dataset.enums.DatasetType;
import com.bmos.mes.service.dataset.service.IDatasetService;
import com.bmos.mes.service.dataset.vo.*;
import com.bmos.mybatis.page.CommonPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 数据集接口
 * @author liang
 * @version 1.0.0
 * @date 2024/8/19 10:41
 */
@RestController
@RequestMapping("/dataset")
@Api(tags = "数据集接口")
public class DatasetController {

    @Resource
    private IDatasetService datasetService;

    @GetMapping("/queryDatasetPage")
    @ApiOperation("分页查询数据集")
    public ResponseInfo<CommonPage<DatasetSimpleVO>> queryDatasetPage(@Validated DatasetPageQuery pageQuery) {
        return ResponseInfo.success(datasetService.queryDatasetPage(pageQuery));
    }

    @GetMapping("/queryDatasetPointPage")
    @ApiOperation("分页查询数据点")
    public ResponseInfo<CommonPage<DatasetPointPageVO>> queryDatasetPointPage(@Validated DatasetPointPageQuery pageQuery) {
        return ResponseInfo.success(datasetService.queryDatasetPointPage(pageQuery));
    }

    @GetMapping("/queryDatasetDetail")
    @ApiOperation("查询数据集详情")
    @ApiImplicitParam(name = "id", value = "数据集id", required = true)
    public ResponseInfo<DatasetVO> queryDatasetDetail(@RequestParam Long id) {
        return ResponseInfo.success(datasetService.queryDatasetDetail(id));
    }

    @GetMapping("/queryDatasetListByProcessId")
    @ApiOperation("根据工艺id查询数据集列表")
    @ApiImplicitParams ({
            @ApiImplicitParam(name = "processId", value = "工艺id", required = true),
            @ApiImplicitParam(name = "datasetType", value = "数据集类型", required = true)
    })
    public ResponseInfo<List<DatasetSimpleVO>> queryDatasetListByProcessId(@RequestParam Long processId, @RequestParam String datasetType) {
        return ResponseInfo.success(datasetService.queryByProcessIdAndType(processId, CommonEnum.getEnumByValue(DatasetType.class, datasetType)));
    }

    @PostMapping("/previewDatasetPointData")
    @ApiOperation("预览数据点(批记录数据)")
    public ResponseInfo<DatasetPointDataPreviewVO> previewDatasetPointData(@Validated @RequestBody DatasetPointDataPreviewPageQuery dto) {
        return ResponseInfo.success(datasetService.previewDatasetPointData(dto));
    }

    @PostMapping("/previewDatasetPointDataList")
    @ApiOperation("预览数据点(批记录数据)列表")
    public ResponseInfo<DatasetPointDataPreviewListVO> previewDatasetPointDataList(@Validated @RequestBody DatasetPointDataPreviewPageQuery dto) {
        return ResponseInfo.success(datasetService.previewDatasetPointListData(dto));
    }

    @PostMapping("/createDataset")
    @ApiOperation("创建数据集")
    @OperationLog
    public ResponseInfo<Long> createDataset(@Validated @RequestBody DatasetCreateDTO dto) {
        return ResponseInfo.success(datasetService.createDataset(dto));
    }

    @PostMapping("/editDataset")
    @ApiOperation("修改数据集")
    @OperationLog
    public ResponseInfo<Long> editDataset(@Validated @RequestBody DatasetEditDTO dto) {
        return ResponseInfo.success(datasetService.editDataset(dto));
    }

    @DeleteMapping("/delete")
    @ApiOperation("删除数据集")
    @OperationLog
    @ApiImplicitParam(name = "id", value = "数据集id", required = true)
    public ResponseInfo<Void> delete(@RequestParam Long id) {
        datasetService.deleteDataset(id);
        return ResponseInfo.success();
    }
}
