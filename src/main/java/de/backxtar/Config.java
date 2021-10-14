package de.backxtar;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class Config {
    private static final List<String> dayValues = Arrays.asList("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun");
    private static final Logger logger = LoggerFactory.getLogger(Config.class);
    private static ConfigData configData;
    private static Colors colors;
    private static File file;

    public static class Colors {
        public String mainColor = "#806BE3";
        public String secondColor = "#49b5cb";
    }

    public static class ConfigData {
        public String ts3Host;
        public String ts3Username;
        public String ts3Password;
        public String ts3Nickname = "Der Geraet (Bot)";
        public String dbHost;
        public String dbName;
        public String dbUser;
        public String dbPassword;
        public String prefix = "!";
        public String lang = "de";
        public int welcomeMessage = 0;
        public int tradingPostChannelID = 0;
        public int[] afkChannelID;
        public int infoChannelID = 0;
        public int guildChannelID = 0;
        public String missionDay = null;
        public String missionMessage = null;
        public String[] guildRanks;
        private int[] tempServerGroups;
        public HashMap<String, Integer> serverGroups;
        public List<Integer> ignoreGroups;
        public List<Integer> supportChannels;
        public List<Integer> supportGroups;
        public List<Integer> tempChannelList;
        public boolean unwantedGuests = false;
        public int arcDpsChannelID = 0;
        public int dailiesChannelID = 0;
        public String guildID;
        public String guildLeaderApiKey;
    }

    public static void loadConfig() throws IOException {
        Properties cfg = new Properties();
        boolean isLive = true;

        if (isLive) file = new File("config.cfg");
        else file = new File("config_test.cfg");
        if (!created(file)) return;

        InputStreamReader streamReader = new InputStreamReader(new FileInputStream(file.getName()), StandardCharsets.UTF_8);
        cfg.load(streamReader);
        Enumeration<Object> en = cfg.keys();
        configData = new ConfigData();
        colors = new Colors();

        while (en.hasMoreElements()) {
            String key = (String) en.nextElement();
            String value;
            String[] values;

            switch (key) {
                // Teamspeak3 data
                case "ts3Host": configData.ts3Host = (String) cfg.get(key);
                    break;
                case "ts3Username": configData.ts3Username = (String) cfg.get(key);
                    break;
                case "ts3Password": configData.ts3Password = (String) cfg.get(key);
                    break;
                case "ts3Nickname":
                    if (cfg.get(key) != null) configData.ts3Nickname = (String) cfg.get(key);
                    break;

                // Database data
                case "dbHost": configData.dbHost = (String) cfg.get(key);
                    break;
                case "dbName": configData.dbName = (String) cfg.get(key);
                    break;
                case "dbUser": configData.dbUser = (String) cfg.get(key);
                    break;
                case "dbPassword": configData.dbPassword = (String) cfg.get(key);
                    break;

                // Settings
                case "prefix": configData.prefix = (String) cfg.get(key);
                    break;
                case "lang": value = (String) cfg.get(key);
                    if (value.equalsIgnoreCase("en"))
                        configData.lang = "en";
                    if (value.equalsIgnoreCase("de"))
                        configData.lang = "de";
                    break;
                case "afkChannelID":
                    value = (String) cfg.get(key);
                    values = getArray(value);
                    if (values.length == 0 || values[0].equalsIgnoreCase("0")) {
                        configData.afkChannelID = new int[1];
                    } else {
                        configData.afkChannelID = new int[values.length];
                        for (int i = 0; i < configData.afkChannelID.length; i++) {
                            configData.afkChannelID[i] = Integer.parseInt(values[i]);
                        }
                    } break;
                case "infoChannelID": value = (String) cfg.get(key);
                    configData.infoChannelID = Integer.parseInt(value);
                    break;
                case "guildChannelID": value = (String) cfg.get(key);
                    configData.guildChannelID = Integer.parseInt(value);
                    break;
                case "missionDay": value = (String) cfg.get(key);
                    if (dayValues.contains(value)) configData.missionDay = value;
                    break;
                case "missionMessage": configData.missionMessage = (String) cfg.get(key);
                    break;
                case "guildRanks": value = (String) cfg.get(key);
                    values = getArray(value);
                    if (values.length == 0) {
                        configData.guildRanks = new String[1];
                        configData.guildRanks[0] = "0";
                    } else {
                        configData.guildRanks = new String[values.length];
                        System.arraycopy(values, 0, configData.guildRanks, 0, configData.guildRanks.length);
                    } break;
                case "serverGroups" : value = (String) cfg.get(key);
                    values = getArray(value);
                    configData.tempServerGroups = new int[values.length];
                    for (int i = 0; i < values.length; i++)
                        configData.tempServerGroups[i] = Integer.parseInt(values[i]);
                    break;
                case "ignoreGroups" : value = (String) cfg.get(key);
                    values = getArray(value);
                    if (!value.isEmpty()) {
                        configData.ignoreGroups = new ArrayList<>();
                        for (String str : values)
                            configData.ignoreGroups.add(Integer.parseInt(str));
                    } break;
                case "supportChannels" : value = (String) cfg.get(key);
                    values = getArray(value);
                    if (!value.isEmpty()) {
                        configData.supportChannels = new ArrayList<>();
                        for (String str : values)
                            configData.supportChannels.add(Integer.parseInt(str));
                    } break;
                case "supportGroups" : value = (String) cfg.get(key);
                    values = getArray(value);
                    if (!value.isEmpty()) {
                        configData.supportGroups = new ArrayList<>();
                        for (String str : values)
                            configData.supportGroups.add(Integer.parseInt(str));
                    } break;
                case "tempChannelList" : value = (String) cfg.get(key);
                    values = getArray(value);
                    if (values.length == 0 || values.length == 1 && values[0].equalsIgnoreCase("0"))
                        configData.tempChannelList = null;
                    else {
                        configData.tempChannelList = new ArrayList<>();
                        for (String str : values)
                            configData.tempChannelList.add(Integer.parseInt(str));
                    }
                    break;
                case "unwantedGuests" : value = (String) cfg.get(key);
                    if (value.equalsIgnoreCase("true")) configData.unwantedGuests = true;
                    else configData.unwantedGuests = false;
                    break;
                case "arcDpsChannelID": value = (String) cfg.get(key);
                    configData.arcDpsChannelID = Integer.parseInt(value);
                    break;
                case "tradingPostChannelID": value = (String) cfg.get(key);
                    configData.tradingPostChannelID = Integer.parseInt(value);
                    break;
                case "dailiesChannelID": value = (String) cfg.get(key);
                    configData.dailiesChannelID = Integer.parseInt(value);
                    break;
                case "welcomeMessage": value = (String) cfg.get(key);
                    configData.welcomeMessage = Integer.parseInt(value);
                    break;
                case "guildID": configData.guildID = (String) cfg.get(key);
                    break;
                case "guildLeaderApiKey": configData.guildLeaderApiKey = (String) cfg.get(key);
                    break;
            }
        }
        if (configData.tempServerGroups.length != configData.guildRanks.length)
            throw new IOException("Number of guildRanks are not equal to number of serverGroups!");
        if (configData.guildRanks.length == 1 && configData.guildRanks[0].equalsIgnoreCase("0"))
            configData.serverGroups = null;

        configData.serverGroups = new HashMap<>();
        for (int i = 0; i < configData.tempServerGroups.length; i++)
            configData.serverGroups.put(configData.guildRanks[i], configData.tempServerGroups[i]);
    }

    private static String[] getArray(String value) {
        return value.split(",");
    }

    private static boolean created(File file) throws IOException{
        if (file.length() == 0 && !file.createNewFile()) {
            logger.info(file.getName() + " is empty or could not be created!");
            return false;
        }
        if (file.createNewFile())
            logger.info("New " + file.getName() + " created.");
        return true;
    }

    public static ConfigData getConfigData() {
        return configData;
    }

    public static Colors getColors() {
        return colors;
    }

    public static File getFile() {
        return file;
    }
}
