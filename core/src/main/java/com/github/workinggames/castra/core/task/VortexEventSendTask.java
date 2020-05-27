package com.github.workinggames.castra.core.task;

import lombok.RequiredArgsConstructor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net;
import com.badlogic.gdx.net.HttpRequestBuilder;
import com.badlogic.gdx.utils.Timer;

@RequiredArgsConstructor
public class VortexEventSendTask extends Timer.Task
{
    private class LoggingResponseListener implements Net.HttpResponseListener
    {
        @Override
        public void handleHttpResponse(Net.HttpResponse httpResponse)
        {
            Gdx.app.debug("EventResponse",
                "Response was " + httpResponse.getStatus().getStatusCode() + ": " + httpResponse.getResultAsString());
        }

        @Override
        public void failed(Throwable t)
        {
            Gdx.app.error("EventResponse", "Failed sending event", t);
        }

        @Override
        public void cancelled()
        {
            Gdx.app.debug("EventResponse", "Task was cancelled");
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

        Gdx.app.debug("EventRequest", "Sending event " + eventJson);
        Gdx.net.sendHttpRequest(httpRequest, new LoggingResponseListener());
    }
}