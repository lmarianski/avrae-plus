package io.github.lmarianski.avraeplus;

import org.javacord.api.entity.server.Server;

import java.util.TimerTask;

public class ForecastTask extends TimerTask {

    ServerData data;

    public ForecastTask(ServerData data) {
        this.data = data;
    }

    @Override
    public void run() {
        data.server.getChannelById(data.forecastChannel).ifPresent(ch -> ch.asServerTextChannel().ifPresent(channel -> {
            channel.sendMessage("Test");
        }));
    }
}
