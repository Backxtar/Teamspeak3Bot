package de.backxtar.systems;

import com.github.theholywaffle.teamspeak3.TS3Api;
import com.github.theholywaffle.teamspeak3.api.VirtualServerProperty;
import com.github.theholywaffle.teamspeak3.api.event.ServerEditedEvent;

public class WelcomeChanged {

    public static void notifyServer(ServerEditedEvent event, TS3Api api) {
        if (event.getBoolean(VirtualServerProperty.VIRTUALSERVER_HOSTMESSAGE)) {
            String message = api.getServerInfo().getHostMessage();
            api.sendServerMessage(message);
        }
    }
}
