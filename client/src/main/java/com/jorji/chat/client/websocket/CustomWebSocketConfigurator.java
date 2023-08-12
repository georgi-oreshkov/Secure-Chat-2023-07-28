package com.jorji.chat.client.websocket;

import jakarta.websocket.ClientEndpointConfig;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CustomWebSocketConfigurator extends ClientEndpointConfig.Configurator {
    private final Map<String, String> customHeaders;

    public CustomWebSocketConfigurator(Map<String, String> headers) {
        this.customHeaders = headers;
    }

    @Override
    public void beforeRequest(Map<String, List<String>> headers) {
        super.beforeRequest(headers);
        for (Map.Entry<String, String> entry : this.customHeaders.entrySet()) {
            headers.put(entry.getKey(), Collections.singletonList(entry.getValue()));
        }
    }

}
