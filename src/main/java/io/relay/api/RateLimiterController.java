package io.relay.api;

import io.relay.limiter.RateLimiter;
import org.springframework.web.bind.annotation.*;

@RestController
public class RateLimiterController {

    private final RateLimiter rateLimiter;

    public RateLimiterController(RateLimiter rateLimiter) {
        this.rateLimiter = rateLimiter;
    }

    @PostMapping("/check")
    public CheckResponse check(@RequestBody CheckRequest req) {

        RateLimiter.Result result = rateLimiter.check(req.key());

        return new CheckResponse(result.allowed(), result.remaining());
    }

    public record CheckRequest(String key) {}
    public record CheckResponse(boolean allowed, int remaining) {}
}
