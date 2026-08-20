package com.bmos.cache.redis;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.bmos.common.redis.RedisKeyDefine;
import com.bmos.common.util.json.JsonUtils;
import org.springframework.data.redis.core.RedisTemplate;

import java.time.Duration;
import java.util.*;

public class RedisService {

    private final RedisTemplate<String, Object> redisTemplate;

    public RedisService(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }


    /**
     * 普通缓存获取
     *
     * @param key       键
     * @param keyDefine 键类型
     * @return 值
     */
    public Object get(String key, RedisKeyDefine keyDefine) {
        if (StrUtil.isEmpty(key)) {
            return null;
        }
        return JsonUtils.parseObject((String) redisTemplate.opsForValue().get(keyDefine.formatKey(key)), keyDefine.getValueType());
    }

    /**
     * 固定KEY缓存获取
     *
     * @param keyDefine 键类型
     * @return 值
     */
    public Object get(RedisKeyDefine keyDefine) {
        Object value = redisTemplate.opsForValue().get(keyDefine.getKeyTemplate());
        if (ObjectUtil.isNull(value)) {
            return value;
        }
        return JsonUtils.parseObject((String) value, keyDefine.getValueType());
    }

    /**
     * 普通缓存放入
     *
     * @param key       键
     * @param value     值
     * @param keyDefine 键类型
     * @return true成功 false失败
     */
    public boolean set(String key, Object value, RedisKeyDefine keyDefine) {
        if (StrUtil.isEmpty(key)) {
            return false;
        }
        Duration timeout = keyDefine.getTimeout();
        if (timeout.toMillis() == 0){
            redisTemplate.opsForValue().set(keyDefine.formatKey(key), JsonUtils.toJsonString(value));
            return true;
        }
        redisTemplate.opsForValue().set(keyDefine.formatKey(key), JsonUtils.toJsonString(value), keyDefine.getTimeout());
        return true;
    }

    public boolean batchSet(Map<String, String> map) {
        if (CollUtil.isEmpty(map)) {
            return false;
        }
        redisTemplate.opsForValue().multiSet(map);
        return true;
    }

    /**
     * 普通缓存放入
     *
     * @param keyDefine 键类型
     * @param value     值
     * @return true成功 false失败
     */
    public boolean set(RedisKeyDefine keyDefine, Object value) {
        Duration timeout = keyDefine.getTimeout();
        if (timeout.toMillis() == 0){
            redisTemplate.opsForValue().set(keyDefine.getKeyTemplate(), JsonUtils.toJsonString(value));
            return true;
        }
        redisTemplate.opsForValue().set(keyDefine.getKeyTemplate(), JsonUtils.toJsonString(value), timeout);
        return true;
    }

    /**
     * 缓存放入hash
     *
     * @param key       key
     * @param keyDefine 模板
     * @param values     值
     * @return true/false
     */
    public boolean batchSetHash(String key,RedisKeyDefine keyDefine, Map<String,Object> values) {
        if (StrUtil.isEmpty(key)) {
            return false;
        }
        redisTemplate.opsForHash().putAll(keyDefine.formatKey(key), values);
        Duration timeout = keyDefine.getTimeout();
        if (timeout.toMillis() > 0){
            redisTemplate.expire(keyDefine.formatKey(key),timeout);
        }
        return true;
    }
    
     /**
     * 缓存放入hash
     *
     * @param key       key
     * @param keyDefine 模板
     * @param hashKeys  hashKey
     * @return true/false
     */
    public List<Object> batchGetHash(String key, RedisKeyDefine keyDefine, String ...hashKeys) {
        if (StrUtil.isEmpty(key) || ArrayUtil.isEmpty(hashKeys)) {
            return Collections.emptyList();
        }
        return redisTemplate.opsForHash().multiGet(keyDefine.formatKey(key), Arrays.asList(hashKeys));

    }
    
    
    /**
     * 缓存放入hash
     *
     * @param key       key
     * @param hashKey   field
     * @param keyDefine 模板
     * @param value     值
     * @return true/false
     */
    public boolean setHash(String key, String hashKey, RedisKeyDefine keyDefine, Object value) {
        if (StrUtil.isEmpty(key) || StrUtil.isEmpty(hashKey)) {
            return false;
        }
        redisTemplate.opsForHash().put(keyDefine.formatKey(key), hashKey, value);
        Duration timeout = keyDefine.getTimeout();
        if (timeout.toMillis() > 0){
            redisTemplate.expire(keyDefine.formatKey(key),timeout);
        }
        return true;
    }

    /**
     * 缓存放入 hash
     *
     * @param hashKey   field
     * @param keyDefine 模板
     * @param value     值
     * @return true/false
     */
    public boolean setHash(String hashKey, RedisKeyDefine keyDefine, Object value) {
        if (StrUtil.isEmpty(hashKey)) {
            return false;
        }
        redisTemplate.opsForHash().put(keyDefine.getKeyTemplate(), hashKey, value);
        Duration timeout = keyDefine.getTimeout();
        if (timeout.toMillis() > 0){
            redisTemplate.expire(keyDefine.formatKey(keyDefine.getKeyTemplate()),timeout);
        }
        return true;
    }

    /**
     *
     * @param key     key
     * @param keyDefine 模板
     * @param hashKeys   field
     * @return
     */
    public boolean deleteHash(String key,RedisKeyDefine keyDefine,Object... hashKeys){
        redisTemplate.opsForHash().delete(keyDefine.formatKey(key),hashKeys);
        return true;
    }


    /**
     * 缓存取出 hash
     *
     * @param key       key
     * @param hashKey   field
     * @param keyDefine 模板
     * @return 值
     */
    public Object getHash(String key, String hashKey, RedisKeyDefine keyDefine) {
        if (StrUtil.isEmpty(key) || StrUtil.isEmpty(hashKey)) {
            return null;
        }
        return redisTemplate.opsForHash().get(keyDefine.formatKey(key), hashKey);
    }


    /**
     * 缓存取出 hash
     *
     * @param keyDefine 模板
     * @param hashKey   field
     * @return 值
     */
    public Object getHash(RedisKeyDefine keyDefine, String hashKey) {
        if (StrUtil.isEmpty(hashKey)) {
            return null;
        }
        return redisTemplate.opsForHash().get(keyDefine.getKeyTemplate(), hashKey);
    }


    public Boolean delete(String key, RedisKeyDefine keyDefine) {
        return redisTemplate.delete(keyDefine.formatKey(key));
    }

    public Boolean delete(RedisKeyDefine keyDefine) {
        return redisTemplate.delete(keyDefine.getKeyTemplate());
    }

    /**
     * 批量删除缓存
     *
     * @param keyDefine
     */
    public void clearGroup(RedisKeyDefine keyDefine) {
        String replace = keyDefine.getKeyTemplate().replace("%s", "*");
        Set<String> keys = redisTemplate.keys(replace);
        if (CollectionUtil.isNotEmpty(keys)) {
            redisTemplate.delete(keys);
        }
    }


    public Boolean hasKey(RedisKeyDefine keyDefine,String key){
        return redisTemplate.hasKey(keyDefine.formatKey(key));
    }
}
