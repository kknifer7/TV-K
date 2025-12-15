package io.knifer.freebox.websocket.handler.impl;

import com.google.gson.reflect.TypeToken;

import io.knifer.freebox.constant.MessageCodes;
import io.knifer.freebox.model.common.Message;
import io.knifer.freebox.util.GsonUtil;
import io.knifer.freebox.websocket.handler.ServiceMessageHandler;
import io.knifer.freebox.websocket.service.WSService;

/**
 * 获取源列表消息处理器
 */
public class GetSourceBeanListHandler extends ServiceMessageHandler<Void> {

    public GetSourceBeanListHandler(WSService service) {
        super(service);
    }

    @Override
    public boolean support(Message<?> message) {
        return MessageCodes.GET_SOURCE_BEAN_LIST == message.getCode();
    }

    @Override
    public Message<Void> resolve(String messageString) {
        return GsonUtil.fromJson(messageString, new TypeToken<>(){});
    }

    @Override
    public void handle(Message<Void> message) {
        service.sendSourceBeanList(message.getTopicId());
    }
}
