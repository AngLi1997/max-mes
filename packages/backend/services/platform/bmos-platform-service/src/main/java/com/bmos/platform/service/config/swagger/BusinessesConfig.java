package com.bmos.platform.service.config.swagger;

import com.bmos.common.util.collection.CollectionUtils;
import com.bmos.platform.service.material.vo.IssueBusinessVO;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "business")
public class BusinessesConfig {
    List<IssueBusinessVO> platforms;

    public Map<String,IssueBusinessVO> getPlatformMap(){
        return CollectionUtils.convertMap(this.platforms,IssueBusinessVO::getPlatformName);
    }
}
