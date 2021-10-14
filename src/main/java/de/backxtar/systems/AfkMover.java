package de.backxtar.systems;

import com.github.theholywaffle.teamspeak3.TS3Api;
import de.backxtar.Config;
import java.util.HashMap;

public class AfkMover {
    private static final HashMap<String, MoveData> dataHashMap = new HashMap<>();
    private static final String lang = Config.getConfigData().lang;

    private static class MoveData {
        public long timestamp;
        public String channelName;
        public int channelID;
    }

    public static void checkAfk(TS3Api api) {
        int[] afkChannelIDs = Config.getConfigData().afkChannelID;
        if (afkChannelIDs.length == 0 || afkChannelIDs[0] == 0) return;
        api.getClients().parallelStream().forEach(client -> {
            if (client.isServerQueryClient()) return;
            if (client.isAway() || client.getIdleTime() >= 900000 && client.isInputMuted()
                    || client.getIdleTime() >= 900000 && client.isOutputMuted()) {
                boolean isAfkChannel = false;

                for (int id : Config.getConfigData().afkChannelID) {
                    if (client.getChannelId() == id)
                        isAfkChannel = true;
                }
                if (dataHashMap.containsKey(client.getUniqueIdentifier()) || isAfkChannel) return;
                MoveData moveData = new MoveData();
                moveData.channelID = client.getChannelId();
                moveData.channelName = api.getChannelInfo(client.getChannelId()).getName();
                moveData.timestamp = System.currentTimeMillis() - client.getIdleTime();
                dataHashMap.put(client.getUniqueIdentifier(), moveData);

                if (lang.equalsIgnoreCase("de")) {
                    api.sendPrivateMessage(client.getId(),
                            "Du wurdest in [color=" + Config.getColors().mainColor + "][b]" +
                                    api.getChannelInfo(afkChannelIDs[0]).getName() + "[/b][/color] verschoben!");
                }
                if (lang.equalsIgnoreCase("en")) {
                    api.sendPrivateMessage(client.getId(),
                            "You are moved to: [color=" + Config.getColors().mainColor + "][b]" +
                                    api.getChannelInfo(afkChannelIDs[0]).getName() + "[/b][/color]!");
                }
                api.moveClient(client.getId(), afkChannelIDs[0]);
            } else {
                if (!dataHashMap.containsKey(client.getUniqueIdentifier())) return;
                long[] duration = getTime(System.currentTimeMillis() - dataHashMap.get(client.getUniqueIdentifier()).timestamp);
                String time = (duration[2] > 0 ? duration[2] + "h " : "") +
                        (duration[1] > 0 ? duration[1] + "min " : "") + String.format("%02d", duration[0]) + " sec.";
                if (lang.equalsIgnoreCase("de")) {
                    api.sendPrivateMessage(client.getId(), "Deine AFK-Zeit betrug: " +
                            "[color=" + Config.getColors().mainColor + "][b]" + time + "[/b][/color]. " +
                            "Dein voheriger Channel: [color=" + Config.getColors().mainColor + "][b]" +
                            dataHashMap.get(client.getUniqueIdentifier()).channelName + "[/b][/color].");
                }
                if (lang.equalsIgnoreCase("en")) {
                    api.sendPrivateMessage(client.getId(), "Your AFK-Time: " +
                            "[color=" + Config.getColors().mainColor + "][b]" + time + "[/b][/color]. " +
                            "Your prev. Channel: [color=" + Config.getColors().mainColor + "][b]" +
                            dataHashMap.get(client.getUniqueIdentifier()).channelName + "[/b][/color].");
                }

                boolean exists = api.getChannels().parallelStream().anyMatch(channel ->
                        channel.getId() == dataHashMap.get(client.getUniqueIdentifier()).channelID);
                boolean isAfkChannel = false;

                for (int id : Config.getConfigData().afkChannelID) {
                    if (client.getChannelId() == id)
                        isAfkChannel = true;
                }
                if (exists && isAfkChannel) api.moveClient(client.getId(), dataHashMap.get(client.getUniqueIdentifier()).channelID);
                dataHashMap.remove(client.getUniqueIdentifier());
            }
        });
    }

    public static void checkOnline(TS3Api api) {
        dataHashMap.forEach((key, moveData) -> {
            if (!api.isClientOnline(key) || api.getClientByUId(key).getChannelId() != Config.getConfigData().afkChannelID[0])
                dataHashMap.remove(key);
        });
    }

    private static long[] getTime(long timeStamp) {
        long[] time = new long[3];
        time[0] = timeStamp / 1000; // Seconds
        time[1] = time[0] / 60; // Minutes
        time[2] = time[1] / 60; // Hours
        time[1] %= 60;
        time[0] %= 60;
        return time;
    }
}
