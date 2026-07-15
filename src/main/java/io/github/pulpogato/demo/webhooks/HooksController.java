package io.github.pulpogato.demo.webhooks;

import io.github.pulpogato.rest.schemas.WebhookPing;
import io.github.pulpogato.rest.schemas.WebhookPush;
import io.github.pulpogato.rest.webhooks.PingWebhooks;
import io.github.pulpogato.rest.webhooks.PushWebhooks;
import io.github.pulpogato.rest.webhooks.WebhookHeaders;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/github-webhook")
public class HooksController implements PushWebhooks<UUID>, PingWebhooks<UUID> {

    private final Logger log = LoggerFactory.getLogger(HooksController.class);

    @Override
    public ResponseEntity<UUID> processPush(WebhookHeaders headers, WebhookPush requestBody) {
        log.info("Received push event: {}", requestBody);
        return ResponseEntity.ok(UUID.randomUUID());
    }

    @Override
    public ResponseEntity<UUID> processPing(WebhookHeaders headers, WebhookPing requestBody) {
        log.info("Received ping: {}", requestBody);
        return ResponseEntity.ok(UUID.randomUUID());
    }
}
