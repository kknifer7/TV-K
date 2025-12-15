package io.knifer.freebox.websocket.handler.impl;

import com.google.gson.reflect.TypeToken;

import io.knifer.freebox.constant.MessageCodes;
import io.knifer.freebox.model.common.Message;
import io.knifer.freebox.model.s2c.SaveMovieCollectionDTO;
import io.knifer.freebox.util.GsonUtil;
import io.knifer.freebox.websocket.handler.ServiceMessageHandler;
import io.knifer.freebox.websocket.service.WSService;

public class SaveMovieCollectionHandler extends ServiceMessageHandler<SaveMovieCollectionDTO> {
    public SaveMovieCollectionHandler(WSService service) {
        super(service);
    }

    @Override
    public boolean support(Message<?> message) {
        return message.getCode() == MessageCodes.SAVE_MOVIE_COLLECTION;
    }

    @Override
    public Message<SaveMovieCollectionDTO> resolve(String messageString) {
        return GsonUtil.fromJson(
                messageString, new TypeToken<>(){}
        );
    }

    @Override
    public void handle(Message<SaveMovieCollectionDTO> message) {
        service.saveMovieCollection(message.getTopicId(), message.getData());
    }
}
