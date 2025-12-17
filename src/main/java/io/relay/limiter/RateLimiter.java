package io.relay.limiter;

import io.relay.limiter.policy.RateLimitPolicy;
import io.relay.limiter.policy.RateLimitPolicyResolver;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Component
public class RateLimiter {

    private final StringRedisTemplate redisTemplate;
    private final RateLimitPolicyResolver policyResolver;

    public RateLimiter(
            StringRedisTemplate redisTemplate,
            RateLimitPolicyResolver policyResolver
    ) {
        this.redisTemplate = redisTemplate;
        this.policyResolver = policyResolver;
    }

    public Result check(String key) {

        //  Resolve policy dynamically
        RateLimitPolicy policy = policyResolver.resolve(key);
        int limit = policy.limit();
        int windowSeconds = policy.windowSeconds();

        //  Calculate window
        long nowSeconds = System.currentTimeMillis() / 1000;
        long windowId = nowSeconds / windowSeconds;

        //  Redis key (shared across servers)
        String redisKey = "rl:" + key + ":" + windowId;

        //  Atomic increment
        Long count = redisTemplate.opsForValue().increment(redisKey);

        //  Set TTL on first hit
        if (count != null && count == 1) {
            redisTemplate.expire(redisKey, Duration.ofSeconds(windowSeconds));
        }

        // Decision
        boolean allowed = count != null && count <= limit;
        int remaining = Math.max(0, limit - (count != null ? count.intValue() : 0));

        return new Result(allowed, remaining);
    }

    public record Result(boolean allowed, int remaining) {}
}
