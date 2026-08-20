package com.bmos.platform.service.equipment.service;

import com.bmos.platform.service.equipment.service.dto.AcquisitionPointDTO;
import com.bmos.platform.service.equipment.service.dto.AcquisitionPointPageQueryDTO;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 采集点导入服务
 *
 * @author yigaohui
 * @date 2024/4/22
 **/
public interface AcquisitionPointImportService {

    /**
     * 获取导入模板
     *
     * @param response
     */
    void getImportTemplate(HttpServletResponse response);

    /**
     * 导出
     *
     * @param response
     * @param list
     */
    void export(HttpServletResponse response, List<AcquisitionPointDTO> list);

    /**
     * 导入
     *
     * @param response
     * @param file
     */
    void importAcquisitionPoint(HttpServletResponse response, MultipartFile file);
}
