package io.relay.limiter;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Component
public class RateLimiter {

    private final StringRedisTemplate redisTemplate;

    private static final int LIMIT = 5;
    private static final int WINDOW_SECONDS = 180;

    public RateLimiter(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public Result check(String key) {

        long nowSeconds = System.currentTimeMillis() / 1000;
        long windowId = nowSeconds / WINDOW_SECONDS;

        // one shared Redis key across ALL servers
        String redisKey = key + ":" + windowId;

        // ATOMIC increment (distributed-safe)
        Long count = redisTemplate.opsForValue().increment(redisKey);

        // first request → set TTL
        if (count != null && count == 1) {
            redisTemplate.expire(redisKey, Duration.ofSeconds(WINDOW_SECONDS));
        }

        boolean allowed = count != null && count <= LIMIT;
        int remaining = Math.max(0, LIMIT - (count != null ? count.intValue() : 0));

        return new Result(allowed, remaining);
    }

    public record Result(boolean allowed, int remaining) {}
}
