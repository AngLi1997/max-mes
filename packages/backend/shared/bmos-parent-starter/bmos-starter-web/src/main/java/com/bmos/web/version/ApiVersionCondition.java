package com.bmos.web.version;

import com.bmos.web.version.constant.ApiVersionConstant;
import org.springframework.web.servlet.mvc.condition.RequestCondition;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ApiVersionCondition implements RequestCondition<ApiVersionCondition> {

    /**
     * 支持的3种接口版本号
     */
    private static final List<Pattern> VERSION_PATTERN = Arrays.asList(
            //v1.1.1
            Pattern.compile("/v\\d\\.\\d\\.\\d/"),
            //v1.1
            Pattern.compile("/v\\d\\.\\d/"),
            //v1
            Pattern.compile("/v\\d/"));

    private static final String SUFFIX_0 = ".0";
    private static final String SUFFIX_0_0 = ".0.0";


    private final String apiVersion;

    public ApiVersionCondition(String apiVersion) {
        this.apiVersion = apiVersion;
    }

    @Override
    public ApiVersionCondition combine(ApiVersionCondition other) {
        return new ApiVersionCondition(other.apiVersion);
    }

    @Override
    public ApiVersionCondition getMatchingCondition(HttpServletRequest request) {
        for (int index = 0; index < VERSION_PATTERN.size(); index++) {
            Matcher matcher = VERSION_PATTERN.get(index).matcher(request.getRequestURI());
            if (matcher.find()) {
                if (compareVersion(appendSuffix(index, replaceVersion(matcher.group())), this.apiVersion) >= 0) {
                    return this;
                }
            }
        }
        return null;
    }

    private static String appendSuffix(int index, String version) {
        if (index == 1) {
            return version + SUFFIX_0;
        }
        if (index == 2) {
            return version + SUFFIX_0_0;
        }
        return version;
    }

    @Override
    public int compareTo(ApiVersionCondition other, HttpServletRequest request) {
        return compareVersion(other.getApiVersion(), this.apiVersion);
    }

    protected String replaceVersion(String original) {
        return original.replace("/" + ApiVersionConstant.API_PATH_PLACEHOLDER, "").replace("/", "");
    }

    private int compareVersion(String version1, String version2) {
        if (version1 == null || version2 == null) {
            throw new RuntimeException("compareVersion error:illegal params.");
        }
        String[] versionArray1 = version1.split("\\.");
        String[] versionArray2 = version2.split("\\.");
        int idx = 0;
        int minLength = Math.min(versionArray1.length, versionArray2.length);
        int diff = 0;
        while (idx < minLength
                && (diff = versionArray1[idx].length() - versionArray2[idx].length()) == 0
                && (diff = versionArray1[idx].compareTo(versionArray2[idx])) == 0) {
            ++idx;
        }
        diff = (diff != 0) ? diff : versionArray1.length - versionArray2.length;
        return diff;
    }

    public String getApiVersion() {
        return apiVersion;
    }
}
