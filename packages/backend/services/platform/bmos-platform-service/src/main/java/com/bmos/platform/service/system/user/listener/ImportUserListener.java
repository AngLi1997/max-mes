package com.bmos.platform.service.system.user.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.read.listener.ReadListener;
import com.alibaba.excel.util.ListUtils;
import com.bmos.common.util.json.JsonUtils;
import com.bmos.platform.service.system.user.mapper.UserMapper;
import com.bmos.platform.service.system.user.model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Slf4j
public class ImportUserListener implements ReadListener<User> {

    @Autowired
    private UserMapper userMapper;

    /**
     * 每隔5条存储数据库，实际使用中可以100条，然后清理list ，方便内存回收
     */
    private static final int BATCH_COUNT = 100;

    /**
     * 缓存的数据
     */
    private List<User> cachedDataList = ListUtils.newArrayListWithExpectedSize(BATCH_COUNT);

    /**
     * 每一条数据解析都会来调用
     *
     * @param user
     * @param context
     */
    @Override
        public void invoke(User user, AnalysisContext context) {
        log.info("解析到一条数据:{}", JsonUtils.toJsonString(user));
        cachedDataList.add(user);
        // 达到BATCH_COUNT了 需要去存储一次数据库 防止数据几万条数据在内存 容易造成性能影响
        if (cachedDataList.size() >= BATCH_COUNT) {
            saveData();
            //存储完成清理 list
            cachedDataList = ListUtils.newArrayListWithExpectedSize(BATCH_COUNT);
        }
    }

    /**
     * 存储数据库
     */
    private void saveData() {
        log.info("{}条数据，开始存储数据库！", cachedDataList.size());
        userMapper.insertBatch(cachedDataList);
        log.info("存储数据库成功！");
    }

    /**
     * 所有数据解析完成了再调用
     */
    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {
        //保存数据 确保最后遗留的数据也存储到数据库
        saveData();
        log.info("所有数据解析完成！");
    }
}
