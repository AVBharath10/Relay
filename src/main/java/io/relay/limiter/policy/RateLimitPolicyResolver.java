package io.relay.limiter.policy;

public interface RateLimitPolicyResolver{
    RateLimitPolicy resolve(String key);
}