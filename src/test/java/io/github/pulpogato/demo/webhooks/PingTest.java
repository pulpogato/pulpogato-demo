package io.github.pulpogato.demo.webhooks;

import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

import io.github.pulpogato.rest.webhooks.WebhookHeadersArgumentResolver;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpMethod;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

class PingTest {

    MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = standaloneSetup(new HooksController())
                .setCustomArgumentResolvers(new WebhookHeadersArgumentResolver())
                .build();
    }

    @Test
    void pingProcesses() throws Exception {
        var builder = fromFile("/ping.http");
        mockMvc.perform(builder)
                .andDo(result -> System.out.println(result.getResponse().getContentAsString()));
    }

    MockHttpServletRequestBuilder fromFile(String filename) {
        var resourceAsStream = this.getClass().getResourceAsStream(filename);
        if (resourceAsStream == null) {
            throw new IllegalArgumentException("Resource not found: " + filename);
        }
        var content = new Scanner(resourceAsStream, StandardCharsets.UTF_8)
                .useDelimiter("\\A")
                .next();
        System.out.println(content);
        List<String> lines = new ArrayList<>(Arrays.asList(content.split("\n")));

        String line0 = lines.removeFirst();
        String[] s = line0.split(" ");
        if (s.length < 2) {
            return null;
        }
        var requestBuilder = MockMvcRequestBuilders.request(HttpMethod.valueOf(s[0]), s[1]);
        while (!lines.isEmpty()) {
            String line = lines.removeFirst();
            if (line.isBlank()) {
                break;
            }
            String[] split = line.split(": ");
            if (split.length != 2) {
                throw new IllegalArgumentException("Invalid header: " + line);
            }
            requestBuilder.header(split[0], split[1]);
        }

        return requestBuilder.content(String.join("\n", lines));
    }
}
