package de.backxtar;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.time.DayOfWeek;
import java.util.*;

public class NewConfig {
    private static final Logger logger = LoggerFactory.getLogger(Config.class);
    private File file;

    private final String mainColor = "#806BE3";
    private final String secondColor = "#49b5cb";

    private String ts3Host;
    private String ts3Username;
    private String ts3Password;
    private String ts3Nickname = "Der Geraet (Bot)";

    private String dbHost;
    private String dbName;
    private String dbUser;
    private String dbPassword;

    private String prefix = "!";
    private Enums.Lang lang;
    private int defaultChannelID = 1;

    private String guildID;
    private String guildLeaderApiKey;

    private HashMap<Enums.Systems, Boolean> isActive;

    private boolean welcomeMessage;
    private boolean unwantedGuests;
    private DayOfWeek gmDay;
    private String gmMessage;
    private int infoChannelID;
    private int tpChannelID;
    private int guildChannelID;
    private int arcDpsChannelID;
    private int dailyChannelID;
    private List<String> guildRanks;
    private List<Integer> tempServerGroups;
    private HashMap<String, Integer> serverGroups;
    private List<Integer> afkChannelID;
    private List<Integer> supportChannelID;
    private List<Integer> supportGroups;
    private List<Integer> tempChannelID;

    public NewConfig() throws IOException {
        loadConfig();
    }

    private void loadConfig() throws IOException {
        Properties cfg = new Properties();
        file = new File("config.cfg");
        if (!created()) return;

        InputStreamReader streamReader = new InputStreamReader(new FileInputStream(file.getName()), StandardCharsets.UTF_8);
        cfg.load(streamReader);
        Enumeration<Object> en = cfg.keys();

        while (en.hasMoreElements()) {
            String key = (String) en.nextElement();

            switch (key) {
                case "ts3Host" -> ts3Host = (String) cfg.get(key);
                case "ts3Username" -> ts3Username = (String) cfg.get(key);
                case "ts3Password" -> ts3Password = (String) cfg.get(key);
                case "ts3Nickname" -> ts3Nickname = (String) cfg.get(key);
                case "dbHost" -> dbHost = (String) cfg.get(key);
                case "dbName" -> dbName = (String) cfg.get(key);
                case "dbUser" -> dbUser = (String) cfg.get(key);
                case "dbPassword" -> dbPassword = (String) cfg.get(key);
                case "prefix" -> prefix = (String) cfg.get(key);
                case "defaultChannelID" -> defaultChannelID = Integer.parseInt((String) cfg.get(key));
                case "lang" -> lang = setLang((String) cfg.get(key));
                case "guildID" -> guildID = (String) cfg.get(key);
                case "guildLeaderApiKey" -> guildLeaderApiKey = (String) cfg.get(key);
                case "gmDay" -> gmDay = calcDay((String) cfg.get(key));
                case "gmMessage" -> {
                    gmMessage = (String) cfg.get(key);
                    isActive.put(Enums.Systems.SERVER_MESSAGE, isActive(gmMessage));
                }
                case "afkChannelID" -> {
                    afkChannelID = (List<Integer>) createList((String) cfg.get(key), Enums.DataType.INT);
                    isActive.put(Enums.Systems.AFK_MOVER, isActive(afkChannelID));
                }
                case "infoChannelID" -> {
                    infoChannelID = Integer.parseInt((String) cfg.get(key));
                    isActive.put(Enums.Systems.INFO_CHANNEL, isActive(infoChannelID));
                }
                case "guildChannelID" -> {
                    guildChannelID = Integer.parseInt((String) cfg.get(key));
                    isActive.put(Enums.Systems.GW2_GUILD_INFO, isActive(guildChannelID));
                }
                case "tradingPostChannelID" -> {
                    tpChannelID = Integer.parseInt((String) cfg.get(key));
                    isActive.put(Enums.Systems.GW2_TP_CHECK, isActive(tpChannelID));
                }
                case "guildRanks" -> guildRanks = (List<String>) createList((String) cfg.get(key), Enums.DataType.STRING);
                case "serverGroups" -> tempServerGroups = (List<Integer>) createList((String) cfg.get(key), Enums.DataType.INT);
                case "supportGroups" -> supportGroups = (List<Integer>) createList((String) cfg.get(key), Enums.DataType.INT);
                case "supportChannels" -> {
                    supportChannelID = (List<Integer>) createList((String) cfg.get(key), Enums.DataType.INT);
                    isActive.put(Enums.Systems.CLIENT_SUPPORT, isActive(supportChannelID));
                }
            }
            mergeLists();
        }
    }

    private boolean created() throws IOException {
        if (file.exists() && file.length() == 0 || !file.exists()) {
            if (file.canWrite()) {
                BufferedWriter writer = new BufferedWriter(new FileWriter(file));
                String cfg = "Test";
                writer.write(cfg);
                writer.close();
            }
            logger.info(file.getName() + " was empty or does not exists!");
            return false;
        }
        return true;
    }

    private Enums.Lang setLang(String str) {
        return switch (str.toLowerCase()) {
            case "eng" -> Enums.Lang.ENG;
            default -> Enums.Lang.GER;
        };
    }

    private DayOfWeek calcDay(String str) {
        return switch (str.toLowerCase()) {
            case "mon" -> DayOfWeek.MONDAY;
            case "tue" -> DayOfWeek.TUESDAY;
            case "wed" -> DayOfWeek.WEDNESDAY;
            case "thu" -> DayOfWeek.THURSDAY;
            case "fri" -> DayOfWeek.FRIDAY;
            case "sat" -> DayOfWeek.SATURDAY;
            case "sun" -> DayOfWeek.SUNDAY;
            default -> null;
        };
    }

    private boolean isActive(Object obj) {
        return obj != null && !obj.toString().equalsIgnoreCase("0") && !obj.toString().isEmpty();
    }

    private Object createList(String value, Enums.DataType type) {
        String[] values;

        if (type == Enums.DataType.INT) {
            List<Integer> list = new ArrayList<>();
            values = value.split(",");

            for (String str : values)
                list.add(Integer.parseInt(str));
            return list;
        }
        if (type == Enums.DataType.STRING) {
            values = value.split(",");
            return Arrays.asList(values);
        }
        return null;
    }

    private void mergeLists() throws IOException{
        if (tempServerGroups.size() != guildRanks.size())
            throw new IOException("Number of guildRanks are not equal to number of serverGroups!");

        if (tempServerGroups.size() == 1 && guildRanks.size() == 1 &&
                tempServerGroups.get(0) == 0 && guildRanks.get(0).equalsIgnoreCase("0"))
            isActive.put(Enums.Systems.GW2_SYNC, true);
        else isActive.put(Enums.Systems.GW2_SYNC, false);

        serverGroups = new HashMap<>();
        for (int i = 0; i < tempServerGroups.size(); i++)
            serverGroups.put(guildRanks.get(i), tempServerGroups.get(i));
    }

    public Enums.Lang getLang() {
        return lang;
    }
}
