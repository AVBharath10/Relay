package io.relay.limiter.policy;

public record RateLimitPolicy(
    int limit,
    int windowSeconds
) {}
