package com.bmos.mybatis.handler;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.bmos.common.holder.SysUserHolder;
import com.bmos.mybatis.dataobject.BaseDO;
import org.apache.ibatis.reflection.MetaObject;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * 通用参数填充实现类
 * 如果没有显式的对通用参数进行赋值，这里会对通用参数进行填充、赋值
 *
 * @author liangzhihao
 */
public class DefaultDBFieldHandler implements MetaObjectHandler {


    @Override
    public void insertFill(MetaObject metaObject) {
        if (Objects.isNull(metaObject)) {
            return;
        }

        insertFillTime(metaObject);

        insertFillUser(metaObject);

        if (metaObject.getOriginalObject() instanceof BaseDO) {
            this.strictInsertFill(metaObject, "draft", Boolean.class, Boolean.FALSE);
            this.strictInsertFill(metaObject, "status", Boolean.class, Boolean.FALSE);
        }
    }


    @Override
    public void updateFill(MetaObject metaObject) {
        // 更新时间为空，则以当前时间为更新时间
        LocalDateTime current = LocalDateTime.now();

        setFieldValByName("updateBy", SysUserHolder.getUser().getUserId(), metaObject);
        setFieldValByName("updateTime", current, metaObject);
    }

    private void insertFillTime(MetaObject metaObject) {
        LocalDateTime current = LocalDateTime.now();
        this.strictInsertFill(metaObject, "createTime", LocalDateTime.class, current);
        this.strictInsertFill(metaObject, "updateTime", LocalDateTime.class, current);
    }

    private void insertFillUser(MetaObject metaObject) {
        String userId = SysUserHolder.getUser().getUserId();
        Object createBy = getFieldValByName("createBy", metaObject);
        Object updateBy = getFieldValByName("updateBy", metaObject);
        if (Objects.isNull(createBy)) {
            this.strictInsertFill(metaObject, "createBy", String.class, userId);
        }
        if (Objects.isNull(updateBy)) {
            this.strictInsertFill(metaObject, "updateBy", String.class, userId);
        }
    }
}
