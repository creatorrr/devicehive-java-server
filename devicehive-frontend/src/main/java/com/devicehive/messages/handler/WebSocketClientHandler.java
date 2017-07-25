package com.devicehive.messages.handler;

/*
 * #%L
 * DeviceHive Frontend Logic
 * %%
 * Copyright (C) 2016 DataArt
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import com.devicehive.websockets.converters.JsonMessageBuilder;
import com.devicehive.websockets.converters.WebSocketResponse;
import com.google.gson.JsonObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;

@Component
public class WebSocketClientHandler {
    private static final Logger logger = LoggerFactory.getLogger(WebSocketClientHandler.class);

    public void sendMessage(JsonObject json, WebSocketSession session) {
        if (!session.isOpen()) {
            return;
        }
        try {
            session.sendMessage(new TextMessage(json.toString()));
        } catch (IOException e) {
            logger.error("Exception while sending message", e);
        }
    }

    public void sendMessage(JsonObject request, WebSocketResponse response, WebSocketSession session) {
            JsonObject message = new JsonMessageBuilder()
                    .addAction(request.get(JsonMessageBuilder.ACTION))
                    .addRequestId(request.get(JsonMessageBuilder.REQUEST_ID))
                    .include(response.getResponseAsJson()).build();
        sendMessage(message, session);
    }

    public JsonObject buildErrorResponse(int errorCode, String message) {
        return JsonMessageBuilder
                .createErrorResponseBuilder(errorCode, message).build();
    }
}
