package com.bmos.lims2.web.inspect.entry;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateUtil;
import com.alibaba.excel.EasyExcel;
import com.bmos.common.response.ResponseInfo;
import com.bmos.lims2.server.inspect.entry.dto.DataPointQueryPageReqDTO;
import com.bmos.lims2.server.inspect.entry.dto.DataPointQueryRespDTO;
import com.bmos.lims2.server.inspect.entry.service.InspectionEntryService;
import com.bmos.lims2.web.inspect.entry.vo.req.DataPointQueryPageReqVO;
import com.bmos.lims2.web.inspect.entry.vo.resp.DataPointHeaderRespVO;
import com.bmos.lims2.web.inspect.entry.vo.resp.DataPointRowRespVO;
import com.bmos.lims2.web.inspect.entry.vo.resp.DataPointQueryPageRespVO;
import com.bmos.mybatis.page.CommonPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * @Description: 检项查询（表头+数据、导出）
 * @Author: yigaohui
 * @Date: 2025/09/05 10:55
 */
@Api(tags = "检项查询-接口")
@RestController
@RequestMapping("/inspect/entry/query")
@Validated
public class InspectionEntryQueryController {

	@Autowired
	private InspectionEntryService inspectionEntryService;


	@ApiOperation("检项查询-表头+分页数据（按请验时间倒序）")
	@PostMapping("/page")
	public ResponseInfo<DataPointQueryPageRespVO> page(@RequestBody @Valid DataPointQueryPageReqVO reqVO) {
		DataPointQueryPageReqDTO reqDTO = BeanUtil.copyProperties(reqVO, DataPointQueryPageReqDTO.class);

		// 加载表头
		DataPointQueryRespDTO headerDTO = inspectionEntryService.loadHeaders(reqDTO);
		DataPointHeaderRespVO headerVO = new DataPointHeaderRespVO();
		// 分组表头
		if (headerDTO.getHeaderGroups() != null) {
			java.util.List<DataPointHeaderRespVO.HeaderGroup> groups = new java.util.ArrayList<>();
			for (DataPointQueryRespDTO.HeaderGroup g : headerDTO.getHeaderGroups()) {
				DataPointHeaderRespVO.HeaderGroup vg = new DataPointHeaderRespVO.HeaderGroup();
				vg.setParameterId(g.getParameterId());
				vg.setParameterCode(g.getParameterCode());
				vg.setParameterName(g.getParameterName());
				java.util.List<DataPointHeaderRespVO.HeaderLevel2> l2s = new java.util.ArrayList<>();
				if (g.getDataPoints() != null) {
					for (DataPointQueryRespDTO.HeaderLevel2 l2 : g.getDataPoints()) {
						DataPointHeaderRespVO.HeaderLevel2 vv = new DataPointHeaderRespVO.HeaderLevel2();
						vv.setPointName(l2.getPointName());
						vv.setPointType(l2.getPointType());
						l2s.add(vv);
					}
				}
				vg.setDataPoints(l2s);
				groups.add(vg);
			}
			headerVO.setHeaderGroups(groups);
		}

		// 分页查询数据行
		CommonPage<DataPointQueryRespDTO.DataRow> page = inspectionEntryService.queryDataPointRows(reqDTO);
		CommonPage<DataPointRowRespVO> rowPage = new CommonPage<>();
		rowPage.setPageNum(page.getPageNum());
		rowPage.setPageSize(page.getPageSize());
		rowPage.setTotal(page.getTotal());
		rowPage.setList(page.getList().stream().map(r -> {
			DataPointRowRespVO v = new DataPointRowRespVO();
			v.setInspectionOrderId(r.getInspectionOrderId());
			v.setInspectionOrderNo(r.getInspectionOrderNo());
			v.setRequestTime(r.getRequestTime());
			// 平铺结构（键=名称|类型）：DTO值对象 -> VO值对象
			if (r.getPointNameToValue() != null) {
				java.util.Map<String, DataPointRowRespVO.DataPointValue> voMap = new java.util.HashMap<>();
				for (java.util.Map.Entry<String, com.bmos.lims2.server.inspect.entry.dto.DataPointQueryRespDTO.DataPointValueDTO> e : r.getPointNameToValue().entrySet()) {
					DataPointRowRespVO.DataPointValue vv = new DataPointRowRespVO.DataPointValue();
					vv.setValue(e.getValue().getValue());
					vv.setParameterId(e.getValue().getParameterId());
					vv.setParameterCode(e.getValue().getParameterCode());
					voMap.put(e.getKey(), vv);
				}
				v.setPointNameToValue(voMap);
			}
			v.setParameterToPointValues(r.getParameterToPointValues());
			return v;
		}).collect(java.util.stream.Collectors.toList()));

		// 组装返回
		DataPointQueryPageRespVO resp = new DataPointQueryPageRespVO();
		resp.setHeaderGroups(headerVO.getHeaderGroups());
		resp.setPage(rowPage);
		return ResponseInfo.success(resp);
	}

	@ApiOperation("检项查询-导出（按当前筛选条件）")
	@PostMapping("/export")
	public ResponseEntity<byte[]> export(@RequestBody @Valid DataPointQueryPageReqVO reqVO) throws UnsupportedEncodingException {
		DataPointQueryPageReqDTO reqDTO = BeanUtil.copyProperties(reqVO, DataPointQueryPageReqDTO.class);

		// 表头（使用分组表头）
		DataPointQueryRespDTO headerDTO = inspectionEntryService.loadHeaders(reqDTO);
		List<DataPointQueryRespDTO.HeaderGroup> headerGroups = headerDTO.getHeaderGroups();

		// 全量分页
		int pageNum = 1;
		int pageSize = Math.max(reqVO.getPageSize() == null ? 1000 : reqVO.getPageSize(), 1000);
		List<DataPointQueryRespDTO.DataRow> allRows = new ArrayList<>();
		while (true) {
			reqDTO.setPageNum(pageNum);
			reqDTO.setPageSize(pageSize);
			CommonPage<DataPointQueryRespDTO.DataRow> page = inspectionEntryService.queryDataPointRows(reqDTO);
			if (page.getList() == null || page.getList().isEmpty()) {
				break;
			}
			allRows.addAll(page.getList());
			if (allRows.size() >= page.getTotal()) {
				break;
			}
			pageNum++;
		}

		// 头与数据（两级表头：分析项/数据点）
		List<List<String>> head = new ArrayList<>();
		head.add(java.util.Arrays.asList("基础信息", "检验单号"));
		head.add(java.util.Arrays.asList("基础信息", "请验时间"));
		if (headerGroups != null) {
			for (DataPointQueryRespDTO.HeaderGroup g : headerGroups) {
				if (g.getDataPoints() == null) { continue; }
				java.util.List<DataPointQueryRespDTO.HeaderLevel2> l2List = g.getDataPoints();
				boolean singleAndSame = l2List.size() == 1
						&& l2List.get(0).getPointName() != null
						&& l2List.get(0).getPointName().equals(g.getParameterName());
				for (DataPointQueryRespDTO.HeaderLevel2 l2 : l2List) {
					String level2Name = l2.getPointName();
					// 避免 EasyExcel 将两级相同名称（且仅一个数据点）的表头自动纵向合并
					if (singleAndSame) {
						level2Name = level2Name + "\u200B"; // 追加零宽空格，视觉不变但值不同
					}
					head.add(java.util.Arrays.asList(g.getParameterName(), level2Name));
				}
			}
		}

		List<List<String>> data = new ArrayList<>();
		for (DataPointQueryRespDTO.DataRow r : allRows) {
			List<String> row = new ArrayList<>();
			row.add(r.getInspectionOrderNo());
			row.add(r.getRequestTime() == null ? "" : DateUtil.format(r.getRequestTime(), "yyyy-MM-dd HH:mm:ss"));
			if (headerGroups != null) {
				for (DataPointQueryRespDTO.HeaderGroup g : headerGroups) {
					if (g.getDataPoints() == null) { continue; }
					for (DataPointQueryRespDTO.HeaderLevel2 l2 : g.getDataPoints()) {
						String v = null;
						if (r.getParameterToPointValues() != null) {
							java.util.Map<String, String> name2v = r.getParameterToPointValues().get(g.getParameterId());
							if (name2v != null) {
								v = name2v.get(l2.getPointName());
							}
						}
						if (v == null && r.getPointNameToValue() != null) {
							String key = l2.getPointName() + "|" + l2.getPointType();
							com.bmos.lims2.server.inspect.entry.dto.DataPointQueryRespDTO.DataPointValueDTO dv = r.getPointNameToValue().get(key);
							if (dv != null) {
								v = dv.getValue();
							}
						}
						row.add(v == null ? "" : v);
					}
				}
			}
			data.add(row);
		}

		ByteArrayOutputStream out = new ByteArrayOutputStream();
		EasyExcel.write(out)
				.head(head)
				.sheet("检项查询")
				.doWrite(data);

		byte[] bytes = out.toByteArray();
		String filename = java.net.URLEncoder.encode("检项查询导出.xlsx", StandardCharsets.UTF_8.name());
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"));
		headers.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename + "; filename*=UTF-8''" + filename);
		headers.setContentLength(bytes.length);
		return ResponseEntity.ok().headers(headers).body(bytes);
	}
}


