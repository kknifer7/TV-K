package io.knifer.freebox.websocket.handler.impl;

import com.google.gson.reflect.TypeToken;

import io.knifer.freebox.constant.MessageCodes;
import io.knifer.freebox.model.common.Message;
import io.knifer.freebox.model.s2c.SavePlayHistoryDTO;
import io.knifer.freebox.util.GsonUtil;
import io.knifer.freebox.websocket.handler.ServiceMessageHandler;
import io.knifer.freebox.websocket.service.WSService;

public class SavePlayHistoryHandler extends ServiceMessageHandler<SavePlayHistoryDTO> {
    public SavePlayHistoryHandler(WSService service) {
        super(service);
    }

    @Override
    public boolean support(Message<?> message) {
        return message.getCode() == MessageCodes.SAVE_PLAY_HISTORY;
    }

    @Override
    public Message<SavePlayHistoryDTO> resolve(String messageString) {
        return GsonUtil.fromJson(
                messageString, new TypeToken<>(){}
        );
    }

    @Override
    public void handle(Message<SavePlayHistoryDTO> message) {
        service.savePlayHistory(message.getData());
    }
}
