package io.knifer.freebox.websocket.handler;

import io.knifer.freebox.websocket.service.WSService;

/**
 * 业务消息处理器
 * @author knifer
 */
public abstract class ServiceMessageHandler<T> implements WebSocketMessageHandler<T> {

    protected final WSService service;

    public ServiceMessageHandler(WSService service) {
        this.service = service;
    }
}
