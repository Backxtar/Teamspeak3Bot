package de.backxtar.systems;

import com.github.theholywaffle.teamspeak3.TS3Api;
import com.github.theholywaffle.teamspeak3.api.VirtualServerProperty;
import de.backxtar.Config;
import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChangeServerMessage {

    private static class SpecialEvents {
        private final List<String> de = Arrays.asList(
                "\uD83C\uDF83 Schauriges Halloween! \uD83C\uDF83",
                "\uD83C\uDF84 Fröhliche Weihnachten! \uD83C\uDF84",
                "\uD83C\uDF85 Ho-Ho-Ho! Der Nikolaus kommt! \uD83C\uDF85",
                "\uD83C\uDF7E Frohes neues Jahr! \uD83C\uDF7E",
                "\uD83C\uDF8A GUILD WARS hat Geburtstag! \uD83C\uDF8A",
                "\uD83D\uDC30 Fröhliche Ostern! \uD83D\uDC30");
        private final List<String> en = Arrays.asList(
                "\uD83C\uDF83 Scary Halloween! \uD83C\uDF83",
                "\uD83C\uDF84 Mary Christmas! \uD83C\uDF84",
                "\uD83C\uDF85 Ho-Ho-Ho! Santa is comming! \uD83C\uDF85",
                "\uD83C\uDF7E Happy new year! \uD83C\uDF7E",
                "\uD83C\uDF8A GUILD WARS has birthday! \uD83C\uDF8A",
                "\uD83D\uDC30 Happy Easter! \uD83D\uDC30");
    }

    private static final SpecialEvents specialEvents = new SpecialEvents();
    private static final String lang = Config.getConfigData().lang;

    public static void changeMessage(TS3Api api) {
        try {
            if (Config.getConfigData().missionDay == null) return;
            String curMes = api.getServerInfo().getWelcomeMessage(), mes, event = "", date = "";

            LocalDate now = Utils.localDate();
            LocalDate nextEvent = Utils.getNextDate();
            long value = Duration.between(now.atStartOfDay(), nextEvent.atStartOfDay()).toDays();

            // Events
            LocalDate halloween = LocalDate.of(now.getYear(), 10, 31);
            LocalDate christmas = LocalDate.of(now.getYear(), 12, 24);
            LocalDate santa = LocalDate.of(now.getYear(), 12, 6);
            LocalDate newYear = LocalDate.of(now.getYear(), 1, 1);
            LocalDate birthday = LocalDate.of(now.getYear(), 8, 25);
            LocalDate easter = Utils.getEasterSunday();

            if (Config.getConfigData().lang.equalsIgnoreCase("de"))
                date = nextEvent.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
            if (Config.getConfigData().lang.equalsIgnoreCase("en"))
                date = nextEvent.format(DateTimeFormatter.ofPattern("MM-dd-yyyy"));
            if (date.isEmpty()) return;

            if (now.isEqual(nextEvent) && lang.equalsIgnoreCase("de"))
                mes = Config.getConfigData().missionMessage.replaceAll("%date", "HEUTE");
            else if (now.isEqual(nextEvent) && lang.equalsIgnoreCase("en"))
                mes = Config.getConfigData().missionMessage.replaceAll("%date", "TODAY");
            else mes = Config.getConfigData().missionMessage.replaceAll("%date", date);

            // Set language
            List<String> eventLang = null;
            if (lang.equalsIgnoreCase("de")) eventLang = specialEvents.de;
            if (lang.equalsIgnoreCase("en")) eventLang = specialEvents.en;
            if (eventLang == null) return;

            // Add Event-Message
            if (now.isEqual(halloween)) event = eventLang.get(0);
            if (now.isEqual(christmas)) event = eventLang.get(1);
            if (now.isEqual(santa)) event = eventLang.get(2);
            if (now.isEqual(newYear)) event = eventLang.get(3);
            if (now.isEqual(birthday)) event = eventLang.get(4);
            if (now.isEqual(easter)) event = eventLang.get(5);
            if (curMes.equalsIgnoreCase(mes)) return;
            boolean isEvent = now.isEqual(halloween) | now.isEqual(christmas) |
                    now.isEqual(santa) | now.isEqual(newYear) | now.isEqual(birthday) |
                    now.isEqual(easter);

            final Map<VirtualServerProperty, String> properties = new HashMap<>();
            if (isEvent && value > 2) properties.put(VirtualServerProperty.VIRTUALSERVER_HOSTMESSAGE, event);
            else if (isEvent && value <= 2) properties.put(VirtualServerProperty.VIRTUALSERVER_HOSTMESSAGE, mes + "\n" + event);
            else properties.put(VirtualServerProperty.VIRTUALSERVER_HOSTMESSAGE, mes);

            if (value <= 2 || isEvent) properties.put(VirtualServerProperty.VIRTUALSERVER_HOSTMESSAGE_MODE, "2");
            else properties.put(VirtualServerProperty.VIRTUALSERVER_HOSTMESSAGE_MODE, "0");

            api.editServer(properties);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
