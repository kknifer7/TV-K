package io.knifer.freebox.websocket.handler.impl;

import com.google.gson.reflect.TypeToken;

import io.knifer.freebox.constant.MessageCodes;
import io.knifer.freebox.model.common.Message;
import io.knifer.freebox.util.GsonUtil;
import io.knifer.freebox.websocket.handler.ServiceMessageHandler;
import io.knifer.freebox.websocket.service.WSService;

public class CancelAllSearchingHandler extends ServiceMessageHandler<Void> {

    public CancelAllSearchingHandler(WSService service) {
        super(service);
    }

    @Override
    public boolean support(Message<?> message) {
        return message.getCode() == MessageCodes.CANCEL_ALL_SEARCHING;
    }

    @Override
    public Message<Void> resolve(String messageString) {
        return GsonUtil.fromJson(
                messageString, new TypeToken<>(){}
        );
    }

    @Override
    public void handle(Message<Void> message) {
        service.cancelAllSearching(message.getTopicId());
    }
}
