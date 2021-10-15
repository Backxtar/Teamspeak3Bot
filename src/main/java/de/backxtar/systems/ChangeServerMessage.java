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
        private List<String> de = Arrays.asList(
                "\uD83C\uDF83 Schauriges Halloween! \uD83C\uDF83",
                "\uD83C\uDF84 Fröhliche Weihnachten! \uD83C\uDF84",
                "\uD83C\uDF85 Ho-Ho-Ho! Der Nikolaus kommt! \uD83C\uDF85",
                "\uD83C\uDF7E Frohes neues Jahr! \uD83C\uDF7E",
                "\uD83C\uDF8A GUILD WARS hat Geburtstag! \uD83C\uDF8A");
        private List<String> en = Arrays.asList(
                "\uD83C\uDF83 Scary Halloween! \uD83C\uDF83",
                "\uD83C\uDF84 Mary Christmas! \uD83C\uDF84",
                "\uD83C\uDF85 Ho-Ho-Ho! Santa is comming! \uD83C\uDF85",
                "\uD83C\uDF7E Happy new year! \uD83C\uDF7E",
                "\uD83C\uDF8A GUILD WARS has birthday! \uD83C\uDF8A");
    }

    private static final SpecialEvents specialEvents = new SpecialEvents();
    private static final String lang = Config.getConfigData().lang;

    public static void changeMessage(TS3Api api) {
        try {
            if (Config.getConfigData().missionDay == null) return;
            String curMes = api.getServerInfo().getWelcomeMessage(), mes, date = "";

            LocalDate now = Utils.localDate();
            LocalDate nextEvent = Utils.getNextDate();
            long value = Duration.between(now.atStartOfDay(), nextEvent.atStartOfDay()).toDays();

            // Events
            LocalDate halloween = LocalDate.of(now.getYear(), 10, 31);
            LocalDate christmas = LocalDate.of(now.getYear(), 12, 24);
            LocalDate santa = LocalDate.of(now.getYear(), 12, 6);
            LocalDate newYear = LocalDate.of(now.getYear(), 1, 1);
            LocalDate birthday = LocalDate.of(now.getYear(), 8, 25);

            if (Config.getConfigData().lang.equalsIgnoreCase("de"))
                date = nextEvent.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
            if (Config.getConfigData().lang.equalsIgnoreCase("en"))
                date = nextEvent.format(DateTimeFormatter.ofPattern("MM-dd-yyyy"));
            if (date.isEmpty()) return;

            mes = Config.getConfigData().missionMessage.replaceAll("%date", date);

            // Set language
            List<String> eventLang = null;
            if (lang.equalsIgnoreCase("de")) eventLang = specialEvents.de;
            if (lang.equalsIgnoreCase("en")) eventLang = specialEvents.en;

            // Add Event-Message
            if (now.isEqual(halloween)) mes += "\n" + eventLang.get(0);
            if (now.isEqual(christmas)) mes += "\n" + eventLang.get(1);
            if (now.isEqual(santa)) mes += "\n" + eventLang.get(2);
            if (now.isEqual(newYear)) mes += "\n" + eventLang.get(3);
            if (now.isEqual(birthday)) mes += "\n" + eventLang.get(4);
            if (curMes.equalsIgnoreCase(mes) || eventLang == null) return;

            final Map<VirtualServerProperty, String> properties = new HashMap<>();
            properties.put(VirtualServerProperty.VIRTUALSERVER_HOSTMESSAGE, mes);
            if (value < 3) properties.put(VirtualServerProperty.VIRTUALSERVER_HOSTMESSAGE_MODE, "2");
            else properties.put(VirtualServerProperty.VIRTUALSERVER_HOSTMESSAGE_MODE, "0");

            api.editServer(properties);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
