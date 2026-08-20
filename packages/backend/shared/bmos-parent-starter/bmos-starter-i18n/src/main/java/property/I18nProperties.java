package property;

import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.digest.DigestUtil;

import java.util.List;

public class I18nProperties {

    /**
     * 国际化文件在nacos中的配置
     */
    private List<NacosI18nLocations> locations;

    public List<NacosI18nLocations> getLocations() {
        return locations;
    }

    public void setLocations(List<NacosI18nLocations> locations) {
        this.locations = locations;
    }

    public static class NacosI18nLocations {

        private static String DEFAULT_GROUP = "DEFAULT_GROUP";

        /**
         * 配置分组
         */
        private String group;

        /**
         * 配置文件
         */
        private String dataId;

        public String getGroup() {
            return group;
        }

        public void setGroup(String group) {
            if (StrUtil.isEmpty(group)){
                this.group = DEFAULT_GROUP;
                return ;
            }
            this.group = group;
        }

        public String getDataId() {
            return dataId;
        }

        public void setDataId(String dataId) {
            this.dataId = dataId;
        }

        public NacosI18nLocations copy(){
            NacosI18nLocations nacosI18nLocations = new NacosI18nLocations();
            nacosI18nLocations.setDataId(this.getDataId());
            nacosI18nLocations.setGroup(this.getGroup());
            return nacosI18nLocations;
        }

        public Boolean equalData(NacosI18nLocations target){
            return target.getDataId().equals(this.dataId) && target.getGroup().equals(this.group);
        }
    }

    public String hash(){
        // 计算当前配置的locations的hash值
        StringBuffer sb = new StringBuffer();
        for (NacosI18nLocations location : locations) {
            sb.append(location.getDataId()).append(location.getGroup());
        }
        return DigestUtil.sha256Hex(sb.toString().getBytes());

    }

}
