package io.knifer.freebox.websocket.service;

import android.text.TextUtils;

import androidx.annotation.Nullable;
import androidx.collection.ArrayMap;

import com.fongmi.android.tv.App;
import com.fongmi.android.tv.Setting;
import com.fongmi.android.tv.api.config.LiveConfig;
import com.fongmi.android.tv.api.config.VodConfig;
import com.fongmi.android.tv.bean.Class;
import com.fongmi.android.tv.bean.History;
import com.fongmi.android.tv.bean.Keep;
import com.fongmi.android.tv.bean.Result;
import com.fongmi.android.tv.bean.Site;
import com.fongmi.android.tv.bean.Vod;
import com.fongmi.android.tv.db.AppDatabase;
import com.fongmi.android.tv.model.SearchTask;
import com.fongmi.android.tv.player.Source;
import com.fongmi.android.tv.utils.Sniffer;
import com.github.catvod.crawler.Spider;
import com.github.catvod.net.OkHttp;
import com.github.catvod.utils.Util;

import org.java_websocket.WebSocket;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import io.knifer.freebox.constant.MessageCodes;
import io.knifer.freebox.model.c2s.RegisterInfo;
import io.knifer.freebox.model.common.Message;
import io.knifer.freebox.model.s2c.DeleteMovieCollectionDTO;
import io.knifer.freebox.model.s2c.DeletePlayHistoryDTO;
import io.knifer.freebox.model.s2c.GetCategoryContentDTO;
import io.knifer.freebox.model.s2c.GetDetailContentDTO;
import io.knifer.freebox.model.s2c.GetMovieCollectedStatusDTO;
import io.knifer.freebox.model.s2c.GetOnePlayHistoryDTO;
import io.knifer.freebox.model.s2c.GetPlayHistoryDTO;
import io.knifer.freebox.model.s2c.GetPlayerContentDTO;
import io.knifer.freebox.model.s2c.GetSearchContentDTO;
import io.knifer.freebox.model.s2c.SaveMovieCollectionDTO;
import io.knifer.freebox.model.s2c.SavePlayHistoryDTO;
import io.knifer.freebox.util.GsonUtil;
import okhttp3.Call;
import okhttp3.Response;

/**
 * WebSocket服务
 * @author knifer
 */
public class WSService {

    private final WebSocket connection;

    private final String clientId;

    private final static int PROTOCOL_VERSION_CODE = 1;

    public WSService(WebSocket connection, String clientId) {
        this.connection = connection;
        this.clientId = clientId;
    }

    public void register() {
        send(Message.oneWay(
                MessageCodes.REGISTER,
                RegisterInfo.of(clientId, "tv-k-default", 0, PROTOCOL_VERSION_CODE)
        ));
    }

    public void sendSourceBeanList(String topicId) {
        send(Message.oneWay(
                MessageCodes.GET_SOURCE_BEAN_LIST_RESULT,
                VodConfig.get().getSites(),
                topicId
        ));
    }

    public void sendHomeContent(String topicId, Site source) {
        send(Message.oneWay(
                MessageCodes.GET_HOME_CONTENT_RESULT,
                getHomeContent(source.getKey()),
                topicId
        ));
    }

    /**
     * 改写自SourceViewModel中的homeContent方法
     * @see com.fongmi.android.tv.model.SiteViewModel
     * @param sourceKey sourceKey
     * @return homeContent信息
     */
    private Result getHomeContent(String sourceKey) {
        if (sourceKey == null) {
            return null;
        }

        Site site = VodConfig.get().getSite(sourceKey);
        Integer type = site.getType();
        Spider spider;
        Result result = null;
        List<Vod> list;
        ArrayMap<String, String> params;

        try {
            switch (type == null ? -1 : type) {
                case 3:
                    spider = site.spider();
                    result = Result.fromJson(spider.homeContent(true));
                    list = result.getList();
                    if (list.isEmpty()) {
                        list = Result.fromJson(spider.homeVideoContent()).getList();
                        if (!list.isEmpty()) {
                            result.setList(list);
                        }
                    }
                    setTypes(site, result);
                    break;
                case 4:
                    params = new ArrayMap<>();
                    params.put("filter", "true");
                    result = Result.fromJson(call(site, params));
                    break;
                default:
                    try (Response response = OkHttp.newCall(site.getApi(), site.getHeader()).execute()) {
                        result = Result.fromType(site.getType(), response.body().string());
                        fetchPic(site, result);
                    }
                    break;
            }
            setTypes(site, result);
        } catch (Exception ignored) {
            if (result == null) {
                result = Result.empty();
            }
        }

        return result;
    }

    private void setTypes(Site site, Result result) {
        result.getTypes().stream().filter(type -> result.getFilters().containsKey(type.getTypeId())).forEach(type -> type.setFilters(result.getFilters().get(type.getTypeId())));
        List<Class> types = site.getCategories().stream().flatMap(cate -> result.getTypes().stream().filter(type -> cate.equals(type.getTypeName()))).toList();
        if (!types.isEmpty()) result.setTypes(types);
    }

    public String call(Site site, androidx.collection.ArrayMap<String, String> params) throws IOException {
        if (!site.getExt().isEmpty()) params.put("extend", site.getExt());
        Call get = OkHttp.newCall(site.getApi(), site.getHeader(), params);
        Call post = OkHttp.newCall(site.getApi(), site.getHeader(), OkHttp.toBody(params));
        try (Response response = (site.getExt().length() <= 1000 ? get : post).execute()) {
            return response.body().string();
        }
    }

    private void fetchPic(Site site, Result result) throws Exception {
        if (site.getType() > 2 || result.getList().isEmpty() || !result.getVod().getPic().isEmpty()) return;
        ArrayList<String> ids = new ArrayList<>();
        boolean empty = site.getCategories().isEmpty();
        for (Vod item : result.getList()) if (empty || site.getCategories().contains(item.getTypeName())) ids.add(item.getId());
        if (ids.isEmpty()) {
            result.clear();

            return;
        }
        ArrayMap<String, String> params = new ArrayMap<>();
        params.put("ac", site.getType() == 0 ? "videolist" : "detail");
        params.put("ids", TextUtils.join(",", ids));
        try (Response response = OkHttp.newCall(site.getApi(), site.getHeader(), params).execute()) {
            result.setList(Result.fromType(site.getType(), response.body().string()).getList());
        }
    }

    public void sendCategoryContent(String topicId, GetCategoryContentDTO dto) {
        send(Message.oneWay(
                MessageCodes.GET_CATEGORY_CONTENT_RESULT,
                getCategoryContent(dto),
                topicId
        ));
    }

    private Result getCategoryContent(GetCategoryContentDTO dto) {
        Result result;
        Site site;
        Integer type;
        Spider spider;
        String page;
        ArrayMap<String, String> params;
        Map<String, String> extend;

        try {
            site = VodConfig.get().getSite(dto.getSourceKey());
            page = dto.getPage();
            type = site.getType();
            if (type == 3) {
                spider = site.spider();
                result = Result.fromJson(spider.categoryContent(
                        dto.getTid(),
                        page,
                        dto.isFilter(),
                        dto.getExtend()
                ));
            } else {
                params = new ArrayMap<>();
                extend = dto.getExtend();
                if (extend == null) {
                    extend = Map.of();
                }
                if (site.getType() == 1 && !extend.isEmpty()) params.put("f", App.gson().toJson(extend));
                if (site.getType() == 4) params.put("ext", Util.base64(App.gson().toJson(extend), Util.URL_SAFE));
                params.put("ac", site.getType() == 0 ? "videolist" : "detail");
                params.put("t", dto.getTid());
                params.put("pg", page);
                result = Result.fromType(type, call(site, params));
            }
        } catch (Exception ignored) {
            result = Result.empty();
        }

        return result;
    }

    public void sendDetailContent(String topicId, GetDetailContentDTO dto) {
        send(Message.oneWay(
                MessageCodes.GET_DETAIL_CONTENT_RESULT,
                getDetailContent(dto),
                topicId
        ));
    }

    private Result getDetailContent(GetDetailContentDTO dto) {
        Result result;
        Site site;
        Spider spider;
        ArrayMap<String, String> params;

        try {
            site = VodConfig.get().getSite(dto.getSourceKey());
            if (site.getType() == 3) {
                spider = site.spider();
                result = Result.fromJson(spider.detailContent(List.of(dto.getVodId())));
            } else {
                params = new ArrayMap<>();
                params.put("ac", site.getType() == 0 ? "videolist" : "detail");
                params.put("ids", dto.getVodId());
                result = Result.fromType(site.getType(), call(site, params));
            }
        } catch (Exception ignored) {
            result = Result.empty();
        }

        return result;
    }

    public void sendPlayerContent(String topicId, GetPlayerContentDTO dto) {
        send(Message.oneWay(
                MessageCodes.GET_PLAYER_CONTENT_RESULT,
                getPlayerContent(dto),
                topicId
        ));
    }

    private Result getPlayerContent(GetPlayerContentDTO dto) {
        Result result;
        Site site;
        Spider spider;
        Integer type;
        String key = dto.getSourceKey();
        String flag = dto.getPlayFlag();
        String id = dto.getVodId();
        ArrayMap<String, String> params;

        try {
            site = VodConfig.get().getSite(key);
            type = site.getType();
            switch (type == null ? -1 : type) {
                case 3:
                    spider = site.spider();
                    result = Result.fromJson(spider.playerContent(
                            flag, id, VodConfig.get().getFlags()
                    ));
                    if (result.getFlag().isEmpty()) result.setFlag(flag);
                    result.setUrl(Source.get().fetch(result));
                    result.setHeader(site.getHeader());
                    result.setKey(key);
                    break;
                case 4:
                    params = new ArrayMap<>();
                    params.put("play", id);
                    params.put("flag", flag);
                    result = Result.fromJson(call(site, params));
                    if (result.getFlag().isEmpty()) result.setFlag(flag);
                    result.setUrl(Source.get().fetch(result));
                    result.setHeader(site.getHeader());
                    break;
                default:
                    result = new Result();
                    result.setUrl(id);
                    result.setFlag(flag);
                    result.setHeader(site.getHeader());
                    result.setPlayUrl(site.getPlayUrl());
                    result.setParse(Sniffer.isVideoFormat(id) && result.getPlayUrl().isEmpty() ? 0 : 1);
                    result.setUrl(Source.get().fetch(result));
                    break;
            }
        } catch (Exception ignored) {
            result = Result.empty();
        }

        return result;
    }

    public void sendPlayHistory(String topicId, GetPlayHistoryDTO dto) {
        send(Message.oneWay(
                MessageCodes.GET_PLAY_HISTORY_RESULT,
                getPlayHistory(),
                topicId
        ));
    }

    public void sendOnePlayHistory(String topicId, GetOnePlayHistoryDTO dto) {
        send(Message.oneWay(
                MessageCodes.GET_ONE_PLAY_HISTORY_RESULT,
                getOnePlayHistory(dto),
                topicId
        ));
    }

    private History getOnePlayHistory(GetOnePlayHistoryDTO dto) {
        String sourceKey = dto.getSourceKey();
        String vodId = dto.getVodId();
        History history;

        if (TextUtils.isEmpty(sourceKey) || TextUtils.isEmpty(vodId)) {
            return null;
        }
        history = History.find(createVodPrimaryKey(sourceKey, vodId));

        return history;
    }

    private List<History> getPlayHistory() {
        return AppDatabase.get().getHistoryDao().findAll();
    }

    public void searchContent(String topicId, GetSearchContentDTO dto) {
        Result result;

        try {
            result = getSearchContent(dto);
        } catch (Exception ignored) {
            result = Result.empty();
        }
        send(Message.oneWay(
                MessageCodes.GET_SEARCH_CONTENT_RESULT,
                result,
                topicId
        ));
    }

    private Result getSearchContent(GetSearchContentDTO dto) {
        String sourceKey = dto.getSourceKey();
        String keyword = dto.getKeyword();
        Result result;

        try {
            result = SearchTask.create(
                    null, VodConfig.get().getSite(sourceKey), keyword, false
            ).call();
        } catch (Exception ignored) {
            result = Result.empty();
        }

        return result;
    }

    public void deletePlayHistory(String topicId, DeletePlayHistoryDTO dto) {
        String sourceKey = dto.getSourceKey();
        String vodId = dto.getVodId();

        AppDatabase.get()
                .getHistoryDao()
                .delete(VodConfig.getCid(), createVodPrimaryKey(sourceKey, vodId));
        send(Message.oneWay(
                MessageCodes.DELETE_PLAY_HISTORY_RESULT,
                null,
                topicId
        ));
    }

    public void sendMovieCollection(String topicId) {
        send(Message.oneWay(
                MessageCodes.GET_MOVIE_COLLECTION_RESULT,
                AppDatabase.get().getKeepDao().getVod(),
                topicId
        ));
    }

    public void saveMovieCollection(String topicId, SaveMovieCollectionDTO dto) {
        Keep keep = new Keep();
        String sourceKey = dto.getSourceKey();
        String vodId = dto.getVodId();
        Site site = VodConfig.get().getSite(sourceKey);

        if (site == null || TextUtils.isEmpty(vodId)) {
            send(Message.oneWay(
                    MessageCodes.SAVE_MOVIE_COLLECTION_RESULT,
                    null,
                    topicId
            ));

            return;
        }
        keep.setKey(createVodPrimaryKey(sourceKey, vodId));
        keep.setCid(VodConfig.getCid());
        keep.setVodPic(dto.getVodPic());
        keep.setVodName(dto.getVodName());
        keep.setSiteName(site.getName());
        keep.setCreateTime(System.currentTimeMillis());
        keep.save();
        send(Message.oneWay(
                MessageCodes.SAVE_MOVIE_COLLECTION_RESULT,
                null,
                topicId
        ));
    }

    private String createVodPrimaryKey(String siteKey, String vodId) {
        return siteKey.concat(AppDatabase.SYMBOL)
                .concat(vodId)
                .concat(AppDatabase.SYMBOL)
                .concat(String.valueOf(VodConfig.getCid()));
    }

    public void deleteMovieCollection(String topicId, DeleteMovieCollectionDTO dto) {
        String siteKey = dto.getSourceKey();
        String vodId = dto.getVodId();
        Keep keep;

        if (!TextUtils.isEmpty(siteKey) && !TextUtils.isEmpty(vodId)) {
            keep = Keep.find(createVodPrimaryKey(siteKey, vodId));
            if (keep != null) {
                keep.delete();
            }
        }
        send(Message.oneWay(
                MessageCodes.DELETE_MOVIE_COLLECTION_RESULT,
                null,
                topicId
        ));
    }

    public void getMovieCollectedStatus(String topicId, GetMovieCollectedStatusDTO dto) {
        String siteKey = dto.getSourceKey();
        String vodId = dto.getVodId();
        boolean result = false;

        if (!TextUtils.isEmpty(siteKey) && !TextUtils.isEmpty(vodId)) {
            result = Keep.find(createVodPrimaryKey(siteKey, vodId)) != null;
        }
        send(Message.oneWay(
                MessageCodes.GET_MOVIE_COLLECTED_STATUS_RESULT,
                result,
                topicId
        ));
    }

    public void sendLives(String topicId) {
        send(Message.oneWay(
                MessageCodes.GET_LIVES_RESULT,
                LiveConfig.get().getLives(),
                topicId
        ));
    }

    private void send(Object obj) {
        connection.send(GsonUtil.toJson(obj));
    }

    public void savePlayHistory(SavePlayHistoryDTO dto) {
        History history;

        if (Setting.isIncognito()) {

            return;
        }
        history = createHistory(dto);
        if (history != null) {
            history.save();
        }
    }

    @Nullable
    private History createHistory(SavePlayHistoryDTO dto) {
        History history = new History();
        String siteKey = dto.getSourceKey();
        String vodId = dto.getVodId();
        String episodeFlag = dto.getEpisodeFlag();
        String playFlag = dto.getPlayFlag();

        if (
                TextUtils.isEmpty(siteKey) ||
                TextUtils.isEmpty(vodId) ||
                TextUtils.isEmpty(episodeFlag) ||
                TextUtils.isEmpty(playFlag)
        ) {

            return null;
        }
        history.setKey(createVodPrimaryKey(siteKey, vodId));
        history.setCid(VodConfig.getCid());
        history.setVodName(dto.getVodName());
        history.setVodPic(dto.getVodPic());
        history.setVodFlag(playFlag);
        history.setVodRemarks(episodeFlag);
        history.setEpisodeUrl(dto.getEpisodeUrl());
        history.setPosition(dto.getPosition());
        history.setDuration(dto.getDuration());
        history.setRevSort(dto.isRevSort());
        history.setCreateTime(System.currentTimeMillis());

        return history;
    }
}
