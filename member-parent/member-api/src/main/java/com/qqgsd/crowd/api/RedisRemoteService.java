package com.qqgsd.crowd.api;

import com.qqgsd.crowd.util.ResultEntity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.concurrent.TimeUnit;

@FeignClient("crowd-redis")
public interface RedisRemoteService {

    /**
     * 设置key,value值
     * @param key key值
     * @param value value值
     */
    @RequestMapping(value = "/set/redis/key/value/remote")
    ResultEntity<String> setRedisKeyValueRemote(
            @RequestParam("key") String key,
            @RequestParam("value") String value);

    /**
     * 设置key,value值(有时间限制)
     *@param key key值
     *@param value value值
     * @param time 超时时间
     * @param timeUnit 时间单位
     */
    @RequestMapping(value = "/set/redis/key/value/remote/with/timeout")
    ResultEntity<String> setRedisKeyValueRemoteWithTimeout(
            @RequestParam("key") String key,
            @RequestParam("value") String value,
            @RequestParam("time") long time,
            @RequestParam("timeUnix") TimeUnit timeUnit);

    /**
     * 根据key获取value
     * @param key key值
     */
    @RequestMapping(value = "/get/redis/string/value/by/key/remote")
    ResultEntity<String> getRedisStringValueByKeyRemote(@RequestParam("key") String key);

    /**
     * 删除key
     * @param key key值
     */
    @RequestMapping(value = "/remove/redis/key/remote")
    ResultEntity<String> removeRedisKeyRemote(@RequestParam("key") String key);
}
