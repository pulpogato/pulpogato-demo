package io.github.pulpogato.demo;

import io.github.pulpogato.rest.webhooks.WebhookHeadersArgumentResolver;
import java.util.List;
import org.springframework.stereotype.Component;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Component
public class WebhookMvcConfigurer implements WebMvcConfigurer {
    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new WebhookHeadersArgumentResolver());
    }
}
