package de.backxtar.systems;

import com.github.theholywaffle.teamspeak3.TS3Api;
import com.github.theholywaffle.teamspeak3.api.ChannelProperty;
import com.github.theholywaffle.teamspeak3.api.event.ClientMovedEvent;
import com.github.theholywaffle.teamspeak3.api.wrapper.ChannelInfo;
import com.github.theholywaffle.teamspeak3.api.wrapper.Client;
import com.github.theholywaffle.teamspeak3.api.wrapper.ServerGroupClient;
import de.backxtar.Config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ClientHelpReminder {
    private static final String lang = Config.getConfigData().lang;

    public static void doSupport(ClientMovedEvent e, Client client, TS3Api api) {
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
        String sendHelp = "";

        if (lang.equalsIgnoreCase("de")) {
            sendHelp = clients.size() > 0 ? "Momentan " + (clients.size() > 1 ? "sind" : "ist") +
                    " [color=" + Config.getColors().mainColor + "][b]" + clients.size() + " Supporter[/b][/color] online! " +
                    "Es wird sich sofort jemand um Dich kümmern!" :
                    "Momentan ist leider [color=" + Config.getColors().mainColor + "][b]kein Supporter[/b][/color] online. " +
                            "Bitte komme zu einem späteren Zeitpunkt wieder!";
        }
        if (lang.equalsIgnoreCase("en")) {
            sendHelp = clients.size() > 0 ? "At the moment there " + (clients.size() > 1 ? "are" : "is") +
                    " [color=" + Config.getColors().mainColor + "][b]" + clients.size() + " " + (clients.size() > 1 ? "Supporters" : "Supporter") +
                    "[/b][/color] online! Please wait!" :
                    "At the moment ist there are [color=" + Config.getColors().mainColor + "][b]no Supporters[/b][/color] online. " +
                            "Bitte komme zu einem späteren Zeitpunkt wieder!";
        }
        api.sendPrivateMessage(client.getId(), sendHelp);

        clients.parallelStream().forEach(supporter -> {
            String send = "";

            if (lang.equalsIgnoreCase("de")) send = "[color=" + Config.getColors().mainColor + "][b]" + client.getNickname() + "[/b][/color] " +
                        "wartet in [color=" + Config.getColors().mainColor + "][b]" + channelInfo.getName() + "[/b][/color] auf Hilfe!";
            if (lang.equalsIgnoreCase("en")) send = "[color=" + Config.getColors().mainColor + "][b]" + client.getNickname() + "[/b][/color] " +
                        "is waiting in [color=" + Config.getColors().mainColor + "][b]" + channelInfo.getName() + "[/b][/color] for help!";
            api.sendPrivateMessage(supporter.getId(), send);
        });
    }

    public static void lockChannel(ClientMovedEvent e, Client client, TS3Api api) {
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

    public static void unlockChannel(TS3Api api) {
        api.getChannels().parallelStream().forEach(channel -> {
            if (Config.getConfigData().supportChannels.stream().anyMatch(channelID -> channelID == channel.getId())) {
                ChannelInfo channelInfo = api.getChannelInfo(channel.getId());

                if (channelInfo.getName().contains("(taken)") && channel.getTotalClients() == 0) {
                    String channelName = channelInfo.getName().replace(" (taken)", "");
                    final Map<ChannelProperty, String> properties = new HashMap<>();
                    properties.put(ChannelProperty.CHANNEL_NAME, channelName);
                    properties.put(ChannelProperty.CHANNEL_PASSWORD, "");
                    api.editChannel(channel.getId(), properties);
                }
            }
        });
    }
}
