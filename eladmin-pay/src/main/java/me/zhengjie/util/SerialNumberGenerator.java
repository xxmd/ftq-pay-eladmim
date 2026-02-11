package me.zhengjie.util;

import me.zhengjie.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

@Component
public class SerialNumberGenerator {
    
    @Autowired
    private StringRedisTemplate redisTemplate;
    
    private static final String DATE_FORMAT = "yyyyMMdd";
    private static final String REDIS_KEY_PREFIX = "order:serial:";
    
    /**
     * 生成订单流水号（格式：年月日 + 6位序列号）
     * 示例：20231224000001
     */
    public String generateTimeSerial() {
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
        String dateStr = sdf.format(new Date());
        
        // 使用 Redis 原子递增保证唯一性
        String key = REDIS_KEY_PREFIX + dateStr;
        Long sequence = redisTemplate.opsForValue().increment(key, 1);
        
        // 设置过期时间（24小时）
        if (sequence == 1) {
            redisTemplate.expire(key, 1, TimeUnit.DAYS);
        }
        
        // 格式化为6位序列号
        String seqStr = String.format("%06d", sequence);
        
        return dateStr + seqStr;
    }

}