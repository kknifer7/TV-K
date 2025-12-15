package io.knifer.freebox.websocket.handler.impl;

import com.google.gson.reflect.TypeToken;

import io.knifer.freebox.constant.MessageCodes;
import io.knifer.freebox.model.common.Message;
import io.knifer.freebox.model.s2c.GetCategoryContentDTO;
import io.knifer.freebox.util.GsonUtil;
import io.knifer.freebox.websocket.handler.ServiceMessageHandler;
import io.knifer.freebox.websocket.service.WSService;

public class GetCategoryContentHandler extends ServiceMessageHandler<GetCategoryContentDTO> {
    public GetCategoryContentHandler(WSService service) {
        super(service);
    }

    @Override
    public boolean support(Message<?> message) {
        return message.getCode() == MessageCodes.GET_CATEGORY_CONTENT;
    }

    @Override
    public Message<GetCategoryContentDTO> resolve(String messageString) {
        return GsonUtil.fromJson(
                messageString, new TypeToken<>(){}
        );
    }

    @Override
    public void handle(Message<GetCategoryContentDTO> message) {
        service.sendCategoryContent(message.getTopicId(), message.getData());
    }
}
