package de.backxtar.systems;

import com.github.theholywaffle.teamspeak3.TS3Api;
import com.github.theholywaffle.teamspeak3.api.ChannelProperty;
import com.github.theholywaffle.teamspeak3.api.event.ClientMovedEvent;
import com.github.theholywaffle.teamspeak3.api.wrapper.ChannelInfo;
import com.github.theholywaffle.teamspeak3.api.wrapper.Client;
import de.backxtar.Config;

import java.util.HashMap;
import java.util.Map;

public class TempChannel {

    public static void createTempChannel(ClientMovedEvent e, Client client, TS3Api api) {
        if (Config.getConfigData().tempChannelList == null ||
            !Config.getConfigData().tempChannelList.contains(e.getTargetChannelId()))
            return;

        ChannelInfo channelInfo = api.getChannelInfo(e.getTargetChannelId());
        String channelName = channelInfo.getName() + " #" + client.getNickname();
        int count = 0;

        while (api.getChannelByNameExact(channelName, true) != null) {
            count++;
            channelName = channelInfo.getName() + " #" + client.getNickname() + "(" + count + ")";
        }
        final Map<ChannelProperty, String> properties = new HashMap<>();
        properties.put(ChannelProperty.CHANNEL_FLAG_TEMPORARY, "1");
        properties.put(ChannelProperty.CPID, String.valueOf(e.getTargetChannelId()));
        properties.put(ChannelProperty.CHANNEL_CODEC_QUALITY, "10");
        api.createChannel(channelName, properties);
        api.moveClient(client.getId(), api.getChannelByNameExact(channelName, true).getId());
        api.moveClient(api.whoAmI().getId(), Config.getConfigData().defaultChannelID);
    }
}
