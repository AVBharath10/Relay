package io.relay.limiter.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@ConfigurationProperties(prefix = "ratelimiter")
public class RateLimiterProperties {

    // maps: ratelimiter.policies
    private List<Policy> policies;

    // maps: ratelimiter.default
    private Policy defaultPolicy;

    public List<Policy> getPolicies() {
        return policies;
    }

    public void setPolicies(List<Policy> policies) {
        this.policies = policies;
    }

    public Policy getDefault() {
        return defaultPolicy;
    }

    public void setDefault(Policy defaultPolicy) {
        this.defaultPolicy = defaultPolicy;
    }

    // Represents ONE rate-limit rule
    public static class Policy {

        private String pattern;
        private int limit;
        private int windowSeconds;

        public String getPattern() {
            return pattern;
        }

        public void setPattern(String pattern) {
            this.pattern = pattern;
        }

        public int getLimit() {
            return limit;
        }

        public void setLimit(int limit) {
            this.limit = limit;
        }

        public int getWindowSeconds() {
            return windowSeconds;
        }

        public void setWindowSeconds(int windowSeconds) {
            this.windowSeconds = windowSeconds;
        }
    }
}
