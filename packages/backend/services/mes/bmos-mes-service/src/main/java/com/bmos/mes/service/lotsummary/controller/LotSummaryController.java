package com.bmos.mes.service.lotsummary.controller;

import com.bmos.common.response.ResponseInfo;
import com.bmos.logging.annotation.OperationLog;
import com.bmos.mes.service.lotsummary.dto.LotSummaryCreateDTO;
import com.bmos.mes.service.lotsummary.dto.LotSummaryEditDTO;
import com.bmos.mes.service.lotsummary.dto.LotSummaryPageQuery;
import com.bmos.mes.service.lotsummary.dto.LotSummaryProductDataPageQuery;
import com.bmos.mes.service.lotsummary.service.ILotSummaryService;
import com.bmos.mes.service.lotsummary.vo.LotSummaryDetailVO;
import com.bmos.mes.service.lotsummary.vo.LotSummaryPageVO;
import com.bmos.mes.service.lotsummary.vo.LotSummaryProductData;
import com.bmos.mes.service.lotsummary.vo.LotSummaryProductListData;
import com.bmos.mybatis.page.CommonPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.simpleframework.xml.core.Validate;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 批次摘要相关接口
 * @author liang
 * @version 1.0.0
 * @date 2024/9/5 10:27
 */
@RestController
@RequestMapping("/lotSummary")
@Api(tags = "批次摘要相关接口")
public class LotSummaryController {

    @Resource
    private ILotSummaryService lotSummaryService;

    @PostMapping("/create")
    @OperationLog
    @ApiOperation("新增批次摘要")
    public ResponseInfo<Void> createLotSummary(@Validate @RequestBody LotSummaryCreateDTO dto){
        lotSummaryService.createLotSummary(dto);
        return ResponseInfo.success();
    }

    @GetMapping("/queryPage")
    @ApiOperation("分页查询批次摘要")
    public ResponseInfo<CommonPage<LotSummaryPageVO>> queryPage(@Validate LotSummaryPageQuery pageQuery){
        return ResponseInfo.success(lotSummaryService.queryPage(pageQuery));
    }

    @GetMapping("/queryDetail")
    @ApiOperation("查询批次摘要详情")
    @ApiImplicitParam(name = "id", value = "批次摘要id", required = true)
    public ResponseInfo<LotSummaryDetailVO> queryDetail(@RequestParam Long id){
        return ResponseInfo.success(lotSummaryService.queryDetail(id));
    }

    @PutMapping("/edit")
    @OperationLog
    @ApiOperation("编辑批次摘要")
    public ResponseInfo<Void> editLotSummary(@Validate @RequestBody LotSummaryEditDTO dto){
        lotSummaryService.editLotSummary(dto);
        return ResponseInfo.success();
    }

    @DeleteMapping("/delete")
    @OperationLog
    @ApiOperation("删除批次摘要")
    @ApiImplicitParam(name = "id", value = "批次摘要id", required = true)
    public ResponseInfo<Void> deleteLotSummary(@Validate @RequestParam Long id){
        lotSummaryService.deleteLotSummary(id);
        return ResponseInfo.success();
    }

    @GetMapping("/queryProductDataPage")
    @ApiOperation("查询批次摘要生产数据分页")
    public ResponseInfo<LotSummaryProductData> queryProductDataPage(@Validate LotSummaryProductDataPageQuery pageQuery){
        return ResponseInfo.success(lotSummaryService.queryProductDataPage(pageQuery));
    }

    @PostMapping("/queryProductDataList")
    @ApiOperation("查询批次摘要生产数据列表")
    public ResponseInfo<LotSummaryProductListData> queryProductDataList(@RequestBody @Validate LotSummaryProductDataPageQuery pageQuery){
        return ResponseInfo.success(lotSummaryService.queryProductDataList(pageQuery));
    }

    @PostMapping("/exportProductDataPage")
    @ApiOperation("导出批次摘要生产数据分页")
    public ResponseInfo<Void> exportProductDataPage(HttpServletResponse response, @Validate @RequestBody LotSummaryProductDataPageQuery pageQuery) throws IOException {
        lotSummaryService.exportProductDataPage(response, pageQuery);
        return ResponseInfo.success();
    }
}
