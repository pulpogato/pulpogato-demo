package io.github.pulpogato.demo.webhooks;

import io.github.pulpogato.rest.schemas.WebhookPing;
import io.github.pulpogato.rest.schemas.WebhookPush;
import io.github.pulpogato.rest.webhooks.PingWebhooks;
import io.github.pulpogato.rest.webhooks.PushWebhooks;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/github-webhook")
public class HooksController implements PushWebhooks<UUID>, PingWebhooks<UUID> {

    private final Logger log = LoggerFactory.getLogger(HooksController.class);

    @Override
    public ResponseEntity<UUID> processPush(
            String userAgent,
            String xGithubHookId,
            String xGithubEvent,
            String xGithubHookInstallationTargetId,
            String xGithubHookInstallationTargetType,
            String xGitHubDelivery,
            String xHubSignature256,
            WebhookPush requestBody) {
        log.info("Received push event: {}", requestBody);
        return ResponseEntity.ok(UUID.randomUUID());
    }

    @Override
    public ResponseEntity<UUID> processPing(
            String userAgent,
            String xGithubHookId,
            String xGithubEvent,
            String xGithubHookInstallationTargetId,
            String xGithubHookInstallationTargetType,
            String xGitHubDelivery,
            String xHubSignature256,
            WebhookPing requestBody) {
        log.info("Received ping: {}", requestBody);
        return ResponseEntity.ok(UUID.randomUUID());
    }

}
