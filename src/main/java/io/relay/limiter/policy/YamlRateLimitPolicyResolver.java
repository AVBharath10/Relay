package io.relay.limiter.policy;

import io.relay.limiter.config.RateLimiterProperties;
import org.springframework.stereotype.Component;

@Component
public class YamlRateLimitPolicyResolver
        implements RateLimitPolicyResolver {

    private final RateLimiterProperties properties;

    public YamlRateLimitPolicyResolver(RateLimiterProperties properties) {
        this.properties = properties;
    }

    @Override
    public RateLimitPolicy resolve(String key) {

        for (RateLimiterProperties.Policy policy : properties.getPolicies()) {
            if (key.contains(policy.getPattern())) {
                return new RateLimitPolicy(
                        policy.getLimit(),
                        policy.getWindowSeconds()
                );
            }
        }

        RateLimiterProperties.Policy def = properties.getDefault();

        return new RateLimitPolicy(
                def.getLimit(),
                def.getWindowSeconds()
        );
    }
}
