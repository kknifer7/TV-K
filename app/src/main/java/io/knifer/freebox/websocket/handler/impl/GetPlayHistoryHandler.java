package io.knifer.freebox.websocket.handler.impl;

import com.google.gson.reflect.TypeToken;

import io.knifer.freebox.constant.MessageCodes;
import io.knifer.freebox.model.common.Message;
import io.knifer.freebox.model.s2c.GetPlayHistoryDTO;
import io.knifer.freebox.util.GsonUtil;
import io.knifer.freebox.websocket.handler.ServiceMessageHandler;
import io.knifer.freebox.websocket.service.WSService;

public class GetPlayHistoryHandler extends ServiceMessageHandler<GetPlayHistoryDTO> {
    public GetPlayHistoryHandler(WSService service) {
        super(service);
    }

    @Override
    public boolean support(Message<?> message) {
        return message.getCode() == MessageCodes.GET_PLAY_HISTORY;
    }

    @Override
    public Message<GetPlayHistoryDTO> resolve(String messageString) {
        return GsonUtil.fromJson(
                messageString, new TypeToken<>(){}
        );
    }

    @Override
    public void handle(Message<GetPlayHistoryDTO> message) {
        service.sendPlayHistory(message.getTopicId(), message.getData());
    }
}
