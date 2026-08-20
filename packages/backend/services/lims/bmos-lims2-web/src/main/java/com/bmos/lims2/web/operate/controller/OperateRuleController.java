package com.bmos.lims2.web.operate.controller;

import com.bmos.common.exception.BmosException;
import com.bmos.common.response.ResponseInfo;
import com.bmos.lims2.common.constants.RecordConstant;
import com.bmos.lims2.common.i18n.LimsResponseCode;
import com.bmos.lims2.server.operate.dto.OperateRulePageDTO;
import com.bmos.lims2.server.operate.dto.SaveOperateRuleDTO;
import com.bmos.lims2.server.operate.service.OperateRuleService;
import com.bmos.lims2.server.operate.vo.OperateRulePageVO;
import com.bmos.mybatis.page.CommonPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * @author renjinguang
 */
@RestController
@RequestMapping("/operate/rule")
@Api(tags = "操作规程文件管理相关接口")
@Validated
public class OperateRuleController {

    @Autowired
    private OperateRuleService ruleService;

    @Autowired
    private com.bmos.lims2.server.inspect.parameter.service.InspectMethodOperateBindService inspectMethodOperateBindService;

    @GetMapping("/page/list")
    @ApiOperation(value = "查询操作规程管理列表")
    public ResponseInfo<CommonPage<OperateRulePageVO>> getRecordPage(@Validated OperateRulePageDTO dto) {
        return ResponseInfo.success(ruleService.getRecordPage(dto));
    }

    @PostMapping("/save")
    @ApiOperation(value = "新增文件")
    public ResponseInfo<Void> save(@RequestBody SaveOperateRuleDTO dto) {
        ruleService.save(dto);
        return ResponseInfo.success();
    }

    @PostMapping("/upload")
    @ApiOperation(value = "文件上传")
    public ResponseInfo<String> upload(MultipartFile file){
        return ResponseInfo.success(ruleService.upload(file));
    }

    @PostMapping("/bind/method/batch-save-by-operate")
    @ApiOperation(value = "按操作规程批量绑定方法（覆盖式）")
    public ResponseInfo<Void> batchSaveMethodByOperate(@RequestBody @Validated com.bmos.lims2.web.operate.vo.req.OperateRuleMethodBindBatchReqVO reqVO) {
        com.bmos.lims2.server.inspect.parameter.dto.OperateRuleMethodBindBatchSaveDTO dto =
                cn.hutool.core.bean.BeanUtil.copyProperties(reqVO, com.bmos.lims2.server.inspect.parameter.dto.OperateRuleMethodBindBatchSaveDTO.class);
        inspectMethodOperateBindService.saveMethodBindingsForOperate(dto);
        return ResponseInfo.success();
    }

    @PostMapping("/preview")
    @ApiOperation(value = "文件浏览")
    public void upload(@RequestParam("file") MultipartFile file, HttpServletResponse response) {
        try (OutputStream outputStream = response.getOutputStream()) {
            String originalFilename = file.getOriginalFilename();
            if (!originalFilename.contains(RecordConstant.UPLOAD)) {
                throw new BmosException(LimsResponseCode.OPERATE_UPLOAD_ERROR);
            }
            InputStream inputStream = file.getInputStream();
            byte[] buffer = new byte[1024];
            int bytesRead = -1;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
            // 刷新输出流
            outputStream.flush();
            // 关闭流
            inputStream.close();
        } catch (Exception e) {
            throw new BmosException(LimsResponseCode.OPERATE_UPLOAD_ERROR);
        }
    }
}
