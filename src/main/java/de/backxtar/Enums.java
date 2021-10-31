package de.backxtar;

public class Enums {

    public enum Systems {
        AFK_MOVER("AFK Mover"),
        ARC_DPS("ArcDps Version"),
        INFO_CHANNEL("Info Channel"),
        SERVER_MESSAGE("Host Message"),
        CLIENT_DESC("Client Description"),
        CLIENT_SUPPORT("Client Support"),
        GW2_DAILIES("Daily Achievements"),
        GW2_TP_CHECK("TP Exchange"),
        GW2_GUILD_INFO("Guild Information"),
        GW2_SYNC("GW2-Teamspeak Synchronisation"),
        TEMP_CHANNEL("Temp Channel"),
        UNWANTED_GUEST("Auto Kicker");
        final String system;

        Systems(String system) {
            this.system = system;
        }
    }

    public enum Lang {
        GER("German"),
        ENG("English");
        final String language;

        Lang(String language) {
            this.language = language;
        }
    }

    public enum DataType {
        INT("Integer"),
        STRING("String"),
        HASHMAP("Hashmap");
        final String type;

        DataType(String type) {
            this.type = type;
        }
    }
}
