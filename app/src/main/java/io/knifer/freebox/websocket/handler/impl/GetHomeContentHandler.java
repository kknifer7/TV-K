package io.knifer.freebox.websocket.handler.impl;

import com.fongmi.android.tv.bean.Site;
import com.google.gson.reflect.TypeToken;

import io.knifer.freebox.constant.MessageCodes;
import io.knifer.freebox.model.common.Message;
import io.knifer.freebox.util.GsonUtil;
import io.knifer.freebox.websocket.handler.ServiceMessageHandler;
import io.knifer.freebox.websocket.service.WSService;

public class GetHomeContentHandler extends ServiceMessageHandler<Site> {

    public GetHomeContentHandler(WSService service) {
        super(service);
    }

    @Override
    public boolean support(Message<?> message) {
        return message.getCode() == MessageCodes.GET_HOME_CONTENT;
    }

    @Override
    public Message<Site> resolve(String messageString) {
        return GsonUtil.fromJson(messageString, new TypeToken<>(){});
    }

    @Override
    public void handle(Message<Site> message) {
        service.sendHomeContent(message.getTopicId(), message.getData());
    }
}
