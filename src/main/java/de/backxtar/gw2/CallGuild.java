package de.backxtar.gw2;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import de.backxtar.Config;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class CallGuild {
    private static final Config.ConfigData configData = Config.getConfigData();

    public static class GWCallGuild {
        public long level;
        public String motd;
        public long influence;
        public long aetherium;
        public long resonance;
        public long favor;
        public int member_count;
        public int member_capacity;
        public String id;
        public String name;
        public String tag;
    }

    public static GWCallGuild getGuild(String guildID) {
        String json = "";
        int fails = 0, maxFails = 3;

        while (fails != maxFails) {
            try {
                json = Gw2Utils.getJson("https://api.guildwars2.com/v2/guild/" + guildID);
                fails = 3;
            } catch (IOException e) {
                if (++fails == 3) e.printStackTrace();
            }
        }
        Gson gson = new Gson();

        try {
            return gson.fromJson(json, GWCallGuild.class);
        } catch (Exception e) {
            return null;
        }
    }

    public static GWCallGuild getOwnGuild() {
        String json = "";
        int fails = 0, maxFails = 3;

        while (fails != maxFails) {
            try {
                json = Gw2Utils.getJson("https://api.guildwars2.com/v2/guild/" + configData.guildID +
                        "?access_token=" + configData.guildLeaderApiKey);
                fails = 3;
            } catch (IOException e) {
                if (++fails == 3) e.printStackTrace();
            }
        }
        Gson gson = new Gson();

        try {
            return gson.fromJson(json, GWCallGuild.class);
        } catch (Exception e) {
            return null;
        }
    }

    public static class GWCallGuildMembers {
        public String name;
        public String rank;
        public String joined;
    }

    public static List<GWCallGuildMembers> getMembers() {
        String json = "";
        int fails = 0, maxFails = 3;

        while (fails != maxFails) {
            try {
                json = Gw2Utils.getJson("https://api.guildwars2.com/v2/guild/" + configData.guildID +
                        "/members?access_token=" + configData.guildLeaderApiKey);
                fails = 3;
            } catch (IOException e) {
                if (++fails == 3) e.printStackTrace();
            }
        }
        Gson gson = new Gson();

        try {
            Type callGuildMembers = new TypeToken<ArrayList<GWCallGuildMembers>>() {}.getType();
            return gson.<ArrayList<GWCallGuildMembers>>fromJson(json, callGuildMembers);
        } catch (Exception e) {
            return null;
        }
    }
}
