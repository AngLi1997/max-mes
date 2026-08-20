package com.bmos.mes.service.weigh.centre.config.controller;

import com.bmos.common.response.ResponseInfo;
import com.bmos.logging.annotation.OperationLog;
import com.bmos.mes.service.weigh.centre.config.dto.WeighCentreBindStationDTO;
import com.bmos.mes.service.weigh.centre.config.dto.WeighCentreCreateDTO;
import com.bmos.mes.service.weigh.centre.config.dto.WeighCentreEditDTO;
import com.bmos.mes.service.weigh.centre.config.dto.WeighCentrePageQuery;
import com.bmos.mes.service.weigh.centre.config.service.IWeighCentreService;
import com.bmos.mes.service.weigh.centre.config.vo.WeighCentreCategoryWithCentreVO;
import com.bmos.mes.service.weigh.centre.config.vo.WeighCentreDetailVO;
import com.bmos.mes.service.weigh.centre.config.vo.WeighCentrePageVO;
import com.bmos.mybatis.page.CommonPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 称量中心接口
 * @author liang
 * @version 1.0.0
 * @date 2024/6/7 10:20
 */
@RestController
@RequestMapping("/weigh/centre")
@Api(tags = "称量中心接口")
public class WeighCentreController {

    @Resource
    private IWeighCentreService weighCentreService;

    @ApiOperation("查询称量中心树")
    @GetMapping("/tree")
    public ResponseInfo<List<WeighCentreCategoryWithCentreVO>> weighCentreTree() {
        return ResponseInfo.success(weighCentreService.weighCentreTree());
    }

    @GetMapping("/queryPage")
    @ApiOperation("查询称量中心分页")
    public ResponseInfo<CommonPage<WeighCentrePageVO>> queryPage(@Validated WeighCentrePageQuery pageQuery){
        return ResponseInfo.success(weighCentreService.queryPage(pageQuery));
    }

    @GetMapping("/queryInfo")
    @ApiOperation("查询称量中心详情")
    @ApiImplicitParam(name = "id", value = "称量中心id", required = true)
    public ResponseInfo<WeighCentreDetailVO> queryCentreInfo(@RequestParam Long id){
        return ResponseInfo.success(weighCentreService.queryCentreInfo(id));
    }

    @PostMapping("/create")
    @ApiOperation("创建称量中心")
    @OperationLog
    public ResponseInfo<Void> createCentre(@RequestBody @Validated WeighCentreCreateDTO createDTO){
        weighCentreService.createCentre(createDTO);
        return ResponseInfo.success();
    }

    @PutMapping("/edit")
    @ApiOperation("编辑称量中心")
    @OperationLog
    public ResponseInfo<Void> editCentre(@RequestBody @Validated WeighCentreEditDTO editDTO){
        weighCentreService.editCentre(editDTO);
        return ResponseInfo.success();
    }


    @PutMapping("/enable")
    @ApiOperation("启用称量中心")
    @ApiImplicitParam(name = "id", value = "称量中心id", required = true)
    @OperationLog
    public ResponseInfo<Void> enableCentre(@RequestParam Long id){
        weighCentreService.enableCentre(id);
        return ResponseInfo.success();
    }

    @PutMapping("/disable")
    @ApiOperation("停用称量中心")
    @ApiImplicitParam(name = "id", value = "称量中心id", required = true)
    @OperationLog
    public ResponseInfo<Void> disableCentre(@RequestParam Long id){
        weighCentreService.disableCentre(id);
        return ResponseInfo.success();
    }

    @PutMapping("/bindStation")
    @ApiOperation("绑定称量中心工位")
    @OperationLog
    public ResponseInfo<Void> bindStation(@RequestBody @Validated WeighCentreBindStationDTO bindStationDTO){
        weighCentreService.bindStation(bindStationDTO);
        return ResponseInfo.success();
    }

    @DeleteMapping("/delete")
    @ApiOperation("删除称量中心")
    @ApiImplicitParam(name = "id", value = "称量中心id", required = true)
    @OperationLog
    public ResponseInfo<Void> deleteCentre(@RequestParam Long id){
        weighCentreService.deleteCentre(id);
        return ResponseInfo.success();
    }
}
