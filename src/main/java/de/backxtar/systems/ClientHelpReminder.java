package de.backxtar.systems;

import com.github.theholywaffle.teamspeak3.TS3Api;
import com.github.theholywaffle.teamspeak3.api.ChannelProperty;
import com.github.theholywaffle.teamspeak3.api.event.ClientMovedEvent;
import com.github.theholywaffle.teamspeak3.api.wrapper.Channel;
import com.github.theholywaffle.teamspeak3.api.wrapper.ChannelInfo;
import com.github.theholywaffle.teamspeak3.api.wrapper.Client;
import com.github.theholywaffle.teamspeak3.api.wrapper.ServerGroupClient;
import de.backxtar.Config;
import de.backxtar.DerGeraet;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ClientHelpReminder {
    private static final TS3Api api = DerGeraet.getInstance().api;

    public static void doSupport(ClientMovedEvent e, Client client) {
        if (!Config.getConfigData().supportChannels.contains(e.getTargetChannelId())) return;

        for (int serverGroup : client.getServerGroups()) {
            if (Config.getConfigData().supportGroups.contains(serverGroup))
                return;
        }
        ChannelInfo channelInfo = api.getChannelInfo(e.getTargetChannelId());
        List<Client> clients = new ArrayList<>();

        for (int i = 0; i < Config.getConfigData().supportGroups.size(); i++) {
            List<ServerGroupClient> sClients = api.getServerGroupClients(Config.getConfigData().supportGroups.get(i));
            for (ServerGroupClient sClient : sClients) {
                if(api.isClientOnline(sClient.getUniqueIdentifier())) {
                    Client supporter = api.getClientByUId(sClient.getUniqueIdentifier());
                    if (clients.parallelStream().noneMatch(sup -> sup.getUniqueIdentifier()
                            .equalsIgnoreCase(sClient.getUniqueIdentifier())))
                        clients.add(supporter);
                }
            }
        }
        String sendHelp = clients.size() > 0 ? "Momentan " + (clients.size() > 1 ? "sind" : "ist") +
                        " [color=" + Config.getColors().mainColor + "][b]" + clients.size() + " Supporter[/b][/color] online! " +
                        "Es wird sich sofort jemand um Dich kümmern!" :
                        "Momentan ist leider [color=" + Config.getColors().mainColor + "][b]kein Supporter[/b][/color] online. " +
                        "Bitte komme zu einem späteren Zeitpunkt wieder!";
        api.sendPrivateMessage(client.getId(), sendHelp);

        clients.parallelStream().forEach(supporter -> api.sendPrivateMessage(supporter.getId(),
                "[color=" + Config.getColors().mainColor + "][b]" + client.getNickname() + "[/b][/color] " +
                "wartet in [color=" + Config.getColors().mainColor + "][b]" + channelInfo.getName() + "[/b][/color] auf Hilfe!"));
    }

    public static void lockChannel(ClientMovedEvent e, Client client) {
        if (!Config.getConfigData().supportChannels.contains(e.getTargetChannelId())) return;
        boolean isSupporter = false;
        for (int serverGroup : client.getServerGroups()) {
            isSupporter = Config.getConfigData().supportGroups.parallelStream().anyMatch(group -> group == serverGroup);
            if (isSupporter) break;
        }

        if (!api.getChannelInfo(e.getTargetChannelId()).getName().contains("(taken)") && isSupporter) {
            ChannelInfo channelInfo = api.getChannelInfo(e.getTargetChannelId());

            final Map<ChannelProperty, String> properties = new HashMap<>();
            properties.put(ChannelProperty.CHANNEL_NAME, channelInfo.getName() + " (taken)");
            properties.put(ChannelProperty.CHANNEL_PASSWORD, "Vagina!");

            api.editChannel(e.getTargetChannelId(), properties);
        }
    }

    public static void unlockChannel() {
        Config.getConfigData().supportChannels.parallelStream().forEach(channelID -> {
            ChannelInfo channelInfo = api.getChannelInfo(channelID);
            Channel channel = api.getChannelByNameExact(channelInfo.getName(), true);

            if (channel.getTotalClients() <= 0 && channelInfo.getName().contains("(taken)")) {
                String channelName = channelInfo.getName().replace(" (taken)", "");

                final Map<ChannelProperty, String> properties = new HashMap<>();
                properties.put(ChannelProperty.CHANNEL_NAME, channelName);
                properties.put(ChannelProperty.CHANNEL_PASSWORD, "");

                api.editChannel(channelID, properties);
            }
        });
    }
}
