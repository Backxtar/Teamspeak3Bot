package de.backxtar.commands;

import com.github.theholywaffle.teamspeak3.TS3Api;
import com.github.theholywaffle.teamspeak3.api.event.TextMessageEvent;
import com.github.theholywaffle.teamspeak3.api.wrapper.Client;
import de.backxtar.managers.CommandInterface;
import de.backxtar.Config;

public class FriendCommand implements CommandInterface {

    @Override
    public void run(String cmdValue, TS3Api api, TextMessageEvent event, Client client) {
        int guest = api.getServerInfo().getDefaultServerGroup();
        boolean isGuest = false;

        for (int group : client.getServerGroups()) {
            if (group == guest) {
                isGuest = true;
                break;
            }
        }

        if (Config.getConfigData().tempServerGroupID == 0 || !isGuest) return;
        api.addClientToServerGroup(Config.getConfigData().tempServerGroupID, client.getDatabaseId());
        api.sendPrivateMessage(client.getId(),
                "Du wurdest [color=orange][b]temporär[/b][/color] freigeschaltet.");
    }
}
