package com.bmos.mes.service.utils;

import com.aspose.words.HeaderFooterType;
import com.bmos.mes.service.execute.vo.IntactFormDataVO;
import com.bmos.mes.service.plan.info.model.Plan;
import org.jetbrains.annotations.NotNull;

import java.io.File;

/**
 * @author yigaohui
 * @date 2024/6/7
 **/
public final class PlanArchivePathUtil {

    private static final String TEMP_FOLDER = File.separator + "data" + File.separator + "temp" + File.separator;

    @NotNull
    public static String getFileName(Long itemId, Long procedureStepModelId, Integer processChangeNumber, Integer procedureChangeNumber, Long copyVersion) {
        return itemId + "-" +procedureStepModelId+"-"+processChangeNumber+"-"+ procedureChangeNumber+"-"+copyVersion + ".docx";
    }

    @NotNull
    public static String getTempFilePath(Long planId) {
        return TEMP_FOLDER + getRelativePath(planId);
    }

    public static String getRelativePath(Long planId) {
        return planId + "";
    }

    public static String getPlanArchiveFileName(Long planId) {
        return planId + ".pdf";
    }

    public static String getPlanArchiveDoxFileName(Long planId) {
        return planId + ".docx";
    }

    public static String getPlanMinioCompleteFilePath(String archiveBucket, Long planId) {
        return archiveBucket + "/" + getRelativePath(planId
        ) + "/" + getPlanArchiveFileName(planId);
    }
}
