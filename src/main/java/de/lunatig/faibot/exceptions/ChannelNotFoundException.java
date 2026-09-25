package de.lunatig.faibot.exceptions;

import de.lunatig.faibot.config.BotChannel;

public class ChannelNotFoundException extends RuntimeException {

    public ChannelNotFoundException(BotChannel channel, String cause) {
        super(String.format("Failed to resolve channel '%s' (%s): %s", channel.name(), channel.getConfigKey(), cause));
    }
}
