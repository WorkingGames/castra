package com.github.workinggames.castra.core.task;

import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonWriter;
import com.badlogic.gdx.utils.Timer;
import com.github.workinggames.castra.core.statistics.event.VortexEvent;

public class VortexEventSender
{
    private static final String VORTEX_ENDPOINT = "https://vortex.incub8.de/api/event?apikey=" + ApiKey.API_KEY;
    private final Json json;

    public VortexEventSender()
    {
        json = new Json();
        json.setOutputType(JsonWriter.OutputType.json);
        json.setTypeName(null);
    }

    public void send(VortexEvent event)
    {
        String eventJson = json.toJson(event);
        Timer.post(new VortexEventSendTask(VORTEX_ENDPOINT, eventJson));
    }
}