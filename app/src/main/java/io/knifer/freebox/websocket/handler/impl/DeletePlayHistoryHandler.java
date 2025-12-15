package io.knifer.freebox.websocket.handler.impl;

import com.google.gson.reflect.TypeToken;

import io.knifer.freebox.constant.MessageCodes;
import io.knifer.freebox.model.common.Message;
import io.knifer.freebox.model.s2c.DeletePlayHistoryDTO;
import io.knifer.freebox.util.GsonUtil;
import io.knifer.freebox.websocket.handler.ServiceMessageHandler;
import io.knifer.freebox.websocket.service.WSService;

public class DeletePlayHistoryHandler extends ServiceMessageHandler<DeletePlayHistoryDTO> {
    public DeletePlayHistoryHandler(WSService service) {
        super(service);
    }

    @Override
    public boolean support(Message<?> message) {
        return message.getCode() == MessageCodes.DELETE_PLAY_HISTORY;
    }

    @Override
    public Message<DeletePlayHistoryDTO> resolve(String messageString) {
        return GsonUtil.fromJson(
                messageString, new TypeToken<>(){}
        );
    }

    @Override
    public void handle(Message<DeletePlayHistoryDTO> message) {
        service.deletePlayHistory(message.getTopicId(), message.getData());
    }
}
