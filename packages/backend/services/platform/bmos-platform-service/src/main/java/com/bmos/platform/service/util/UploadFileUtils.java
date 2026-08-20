package com.bmos.platform.service.util;

import com.bmos.common.exception.BmosException;
import com.bmos.platform.common.exception.PlatformResponseCode;
import lombok.NonNull;
import org.apache.commons.io.FilenameUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * @author : ldl
 * @version : 1.0
 */

public class UploadFileUtils {
    private static Set<String> excelTails = new HashSet<>(Arrays.asList("xls,xlsx".split(",")));

    private static String joinSet(String delimiter, Set<String> set) {
        return String.join(delimiter, set.toArray(new String[0]));
    }

    public static void checkFileType(MultipartFile file, @NonNull Set<String> fileExtensionSet) {
        String fileExtension = FilenameUtils.getExtension(file.getOriginalFilename());
        //DiAssert.isTrue(fileExtensionSet.contains(fileExtension), "请上传正确的文件,包含" + joinSet(",", fileExtensionSet));
        if (!fileExtensionSet.contains(fileExtension)) {
            throw new BmosException(PlatformResponseCode.EXPORT_TEMPLATE_TYPE_ERROR, joinSet(",", fileExtensionSet));
        }
    }

    public static void checkFileSize(MultipartFile file, int maxSizeMb) {
        // byte 转化为MB
        if (file.getSize() >> 20 <= maxSizeMb) {
            throw new BmosException(PlatformResponseCode.EXPORT_TEMPLATE_FILE_SIZE_ERROR, maxSizeMb + "");
        }
    }

    public static void checkFileTypeAndSize(MultipartFile file, @NonNull Set<String> fileExtensionSet, int maxSizeMb) {
        checkFileType(file, fileExtensionSet);
        checkFileSize(file, maxSizeMb);
    }

    public static void checkExcel(MultipartFile file) {
        checkFileType(file, excelTails);
    }

    public static void checkExcel(MultipartFile file, int maxSizeMb) {
        checkFileTypeAndSize(file, excelTails, maxSizeMb);
    }

}
