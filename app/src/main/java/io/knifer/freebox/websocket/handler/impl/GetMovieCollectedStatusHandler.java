package io.knifer.freebox.websocket.handler.impl;

import com.google.gson.reflect.TypeToken;

import io.knifer.freebox.constant.MessageCodes;
import io.knifer.freebox.model.common.Message;
import io.knifer.freebox.model.s2c.GetMovieCollectedStatusDTO;
import io.knifer.freebox.util.GsonUtil;
import io.knifer.freebox.websocket.handler.ServiceMessageHandler;
import io.knifer.freebox.websocket.service.WSService;

public class GetMovieCollectedStatusHandler extends ServiceMessageHandler<GetMovieCollectedStatusDTO> {
    public GetMovieCollectedStatusHandler(WSService service) {
        super(service);
    }

    @Override
    public boolean support(Message<?> message) {
        return message.getCode() == MessageCodes.GET_MOVIE_COLLECTED_STATUS;
    }

    @Override
    public Message<GetMovieCollectedStatusDTO> resolve(String messageString) {
        return GsonUtil.fromJson(
                messageString, new TypeToken<>(){}
        );
    }

    @Override
    public void handle(Message<GetMovieCollectedStatusDTO> message) {
        service.getMovieCollectedStatus(message.getTopicId(), message.getData());
    }
}
