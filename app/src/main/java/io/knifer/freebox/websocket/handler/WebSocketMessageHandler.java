package io.knifer.freebox.websocket.handler;

import io.knifer.freebox.model.common.Message;

/**
 * WS消息处理器
 *
 * @author Knifer
 */
public interface WebSocketMessageHandler<T> {

    /**
     * 是否支持指定的消息
     * @see io.knifer.freebox.constant.MessageCodes
     * @param message 消息
     * @return bool
     */
    boolean support(Message<?> message);

    /**
     * 解析Message对象
     * @param messageString 消息字符串
     * @return Message对象
     */
    Message<T> resolve(String messageString);

    /**
     * 处理消息
     * @param message 消息
     */
    void handle(Message<T> message);
}
