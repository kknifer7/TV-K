package io.knifer.freebox.websocket.handler.impl;

import com.google.gson.reflect.TypeToken;

import io.knifer.freebox.constant.MessageCodes;
import io.knifer.freebox.model.common.Message;
import io.knifer.freebox.model.s2c.GetOnePlayHistoryDTO;
import io.knifer.freebox.util.GsonUtil;
import io.knifer.freebox.websocket.handler.ServiceMessageHandler;
import io.knifer.freebox.websocket.service.WSService;

public class GetOnePlayHistoryHandler extends ServiceMessageHandler<GetOnePlayHistoryDTO> {
    public GetOnePlayHistoryHandler(WSService service) {
        super(service);
    }

    @Override
    public boolean support(Message<?> message) {
        return message.getCode() == MessageCodes.GET_ONE_PLAY_HISTORY;
    }

    @Override
    public Message<GetOnePlayHistoryDTO> resolve(String messageString) {
        return GsonUtil.fromJson(
                messageString, new TypeToken<>(){}
        );
    }

    @Override
    public void handle(Message<GetOnePlayHistoryDTO> message) {
        service.sendOnePlayHistory(message.getTopicId(), message.getData());
    }
}
