package io.knifer.freebox.websocket.handler.impl;

import com.google.gson.reflect.TypeToken;

import io.knifer.freebox.constant.MessageCodes;
import io.knifer.freebox.model.common.Message;
import io.knifer.freebox.model.s2c.GetDetailContentDTO;
import io.knifer.freebox.util.GsonUtil;
import io.knifer.freebox.websocket.handler.ServiceMessageHandler;
import io.knifer.freebox.websocket.service.WSService;

public class GetDetailContentHandler extends ServiceMessageHandler<GetDetailContentDTO> {
    public GetDetailContentHandler(WSService service) {
        super(service);
    }

    @Override
    public boolean support(Message<?> message) {
        return message.getCode() == MessageCodes.GET_DETAIL_CONTENT;
    }

    @Override
    public Message<GetDetailContentDTO> resolve(String messageString) {
        return GsonUtil.fromJson(messageString, new TypeToken<>(){});
    }

    @Override
    public void handle(Message<GetDetailContentDTO> message) {
        service.sendDetailContent(message.getTopicId(), message.getData());
    }
}
