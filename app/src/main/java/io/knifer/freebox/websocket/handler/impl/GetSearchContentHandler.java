package io.knifer.freebox.websocket.handler.impl;

import com.google.gson.reflect.TypeToken;

import io.knifer.freebox.constant.MessageCodes;
import io.knifer.freebox.model.common.Message;
import io.knifer.freebox.model.s2c.GetSearchContentDTO;
import io.knifer.freebox.util.GsonUtil;
import io.knifer.freebox.websocket.handler.ServiceMessageHandler;
import io.knifer.freebox.websocket.service.WSService;

public class GetSearchContentHandler extends ServiceMessageHandler<GetSearchContentDTO> {
    public GetSearchContentHandler(WSService service) {
        super(service);
    }

    @Override
    public boolean support(Message<?> message) {
        return message.getCode() == MessageCodes.GET_SEARCH_CONTENT;
    }

    @Override
    public Message<GetSearchContentDTO> resolve(String messageString) {
        return GsonUtil.fromJson(
                messageString, new TypeToken<>(){}
        );
    }

    @Override
    public void handle(Message<GetSearchContentDTO> message) {
        service.searchContent(message.getTopicId(), message.getData());
    }
}
