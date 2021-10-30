package de.backxtar.managers;

import com.github.theholywaffle.teamspeak3.TS3Api;
import com.github.theholywaffle.teamspeak3.api.event.*;
import com.github.theholywaffle.teamspeak3.api.wrapper.Client;
import de.backxtar.Config;
import de.backxtar.DerGeraet;
import de.backxtar.events.OnClientJoin;
import de.backxtar.systems.ClientHelpReminder;
import de.backxtar.systems.TempChannel;
import de.backxtar.systems.Utils;
import de.backxtar.systems.WelcomeChanged;

public class EventManager {
    public static void loadEvents() {
        TS3Api api = DerGeraet.getInstance().api;

        api.registerAllEvents();
        api.addTS3Listeners(new TS3Listener() {
            @Override
            public void onTextMessage(TextMessageEvent event) {
                if (event.getInvokerUniqueId().equalsIgnoreCase(api.whoAmI().getUniqueIdentifier())) return;
                String message = event.getMessage();
                Client client = api.getClientByUId(event.getInvokerUniqueId());

                if (message.startsWith(Config.getConfigData().prefix)) {
                    String[] command = message.substring(Config.getConfigData().prefix.length()).split(" ");

                    if (!DerGeraet.getInstance().getCmdManager().runCmd(command, api, event, client))
                        api.sendPrivateMessage(client.getId(), "[color=red]✘[/color] Dieser Befehl ist mir nicht bekannt, [b]" + client.getNickname() + "[/b]!\n" +
                                "Für eine Liste aller Commands gebe [color=" + Config.getColors().mainColor + "][b]!help[/b][/color] ein!");
                }
            }

            @Override
            public void onClientJoin(ClientJoinEvent clientJoinEvent) {
                try {
                    if (api.getClientInfo(clientJoinEvent.getClientId()).getIp().equalsIgnoreCase("94.23.235.222")) return;
                    String UID = clientJoinEvent.getUniqueClientIdentifier();

                    if (!api.isClientOnline(UID) || api.getClientByUId(UID).isServerQueryClient()) return;
                    Client client = api.getClientByUId(UID);
                    Utils.changeInfo();
                    OnClientJoin.sendWelcome(client);
                    OnClientJoin.gw2ApiReminder(client);
                } catch (Exception ignored) {}
            }

            @Override
            public void onClientLeave(ClientLeaveEvent clientLeaveEvent) {
                Utils.changeInfo();
            }

            @Override
            public void onServerEdit(ServerEditedEvent serverEditedEvent) {
                WelcomeChanged.notifyServer(serverEditedEvent, api);
            }

            @Override
            public void onChannelEdit(ChannelEditedEvent channelEditedEvent) {

            }

            @Override
            public void onChannelDescriptionChanged(ChannelDescriptionEditedEvent channelDescriptionEditedEvent) {

            }

            @Override
            public void onClientMoved(ClientMovedEvent clientMovedEvent) {
                int clientID = clientMovedEvent.getClientId();

                try {
                    if (!api.isClientOnline(clientID) || api.getClientInfo(clientID).isServerQueryClient()) return;
                    Client client = api.getClientInfo(clientID);
                    ClientHelpReminder.doSupport(clientMovedEvent, client, api);
                    ClientHelpReminder.lockChannel(clientMovedEvent, client, api);
                    TempChannel.createTempChannel(clientMovedEvent, client, api);
                } catch (Exception ignored) {}
            }

            @Override
            public void onChannelCreate(ChannelCreateEvent channelCreateEvent) {

            }

            @Override
            public void onChannelDeleted(ChannelDeletedEvent channelDeletedEvent) {

            }

            @Override
            public void onChannelMoved(ChannelMovedEvent channelMovedEvent) {

            }

            @Override
            public void onChannelPasswordChanged(ChannelPasswordChangedEvent channelPasswordChangedEvent) {

            }

            @Override
            public void onPrivilegeKeyUsed(PrivilegeKeyUsedEvent privilegeKeyUsedEvent) {

            }
        });
    }
}
