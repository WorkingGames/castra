package com.github.workinggames.castra.core.task;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net;
import com.badlogic.gdx.net.HttpRequestBuilder;
import com.badlogic.gdx.utils.Timer;

@Slf4j
@RequiredArgsConstructor
public class VortexEventSendTask extends Timer.Task
{
    private class DontCareResponseListener implements Net.HttpResponseListener
    {
        @Override
        public void handleHttpResponse(Net.HttpResponse httpResponse)
        {
            log.debug("Response had statusCode: {}", httpResponse.getStatus().getStatusCode());
        }

        @Override
        public void failed(Throwable t)
        {
            log.error("failed sending event", t);
        }

        @Override
        public void cancelled()
        {
            log.error("cancelled sending event");
        }
    }

    private final String url;
    private final String eventJson;

    @Override
    public void run()
    {
        HttpRequestBuilder requestBuilder = new HttpRequestBuilder();
        Net.HttpRequest httpRequest = requestBuilder.newRequest()
            .method(Net.HttpMethods.POST)
            .url(url)
            .header("Content-Type", "application/json")
            .content(eventJson)
            .build();

        log.debug("Sending event {}", eventJson);
        Gdx.net.sendHttpRequest(httpRequest, new DontCareResponseListener());
    }
}