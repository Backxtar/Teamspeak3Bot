package de.backxtar.systems;

import com.github.theholywaffle.teamspeak3.TS3Api;
import com.github.theholywaffle.teamspeak3.api.ChannelProperty;
import de.backxtar.Config;
import de.backxtar.DerGeraet;
import de.backxtar.gw2.CallDaily;
import de.backxtar.gw2.CallPactSupply;
import de.backxtar.gw2.Gw2Utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class DailyCheck {
    private static final ExecutorService executor = Executors.newFixedThreadPool(2);
    private static final Config.Colors colors = Config.getColors();
    private static final Provisioner_Agents provisioner_agents = new Provisioner_Agents();
    private static final String lang = Config.getConfigData().lang;

    private static class Provisioner_Agents {
        List<String> namesDe = Arrays.asList("Skritt", "Quartier", "Aym", "Eve",
                "Legion", "Synergetik", "Natomi", "Rakatin", "Vorratshelfer");
        List<String> namesEn = Arrays.asList("Skritt", "Temple", "Aym", "Eve",
                "Legion", "Synergetics", "Natomi", "Rakatin", "Assistant");
        List<String> location = Arrays.asList("[&BAwEAAA=]", "[&BP4EAAA=]", "[&BIYDAAA=]", "[&BLsEAAA=]", "[&BKgDAAA=]",
                "[&BLYEAAA=]", "[&BN4HAAA=]", "[&BNYHAAA=]", "[&BMwHAAA=]");
    }

    public static void checkDailies(TS3Api api) {
        if (Config.getConfigData().dailiesChannelID == 0) return;
        CallDaily.GWCallDaily daily = CallDaily.getDailies(false);
        CallDaily.GWCallDaily dailyTomorrow = CallDaily.getDailies(true);

        if (daily == null || dailyTomorrow == null) return;
        Future<StringBuilder> dailyAsync = executor.submit(() -> getDailies(daily, false));
        Future<StringBuilder> dailyTomorrowAsync = executor.submit(() -> getDailies(dailyTomorrow, true));

        StringBuilder dailyStr = null, dailyTomorrowStr = null;
        try {
            dailyStr = dailyAsync.get();
            dailyTomorrowStr = dailyTomorrowAsync.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        if (dailyStr == null || dailyTomorrowStr == null) return;
        String des = "[center][size=11][URL=client://" + api.whoAmI().getId() + "/"
                + api.whoAmI().getUniqueIdentifier() + "]Message me![/URL][/size]" +
                "\n\n" + dailyStr + "\n\n\n" + dailyTomorrowStr;
        if (api.getChannelInfo(Config.getConfigData().dailiesChannelID).getDescription().equalsIgnoreCase(des))
            return;
        api.editChannel(Config.getConfigData().dailiesChannelID, ChannelProperty.CHANNEL_DESCRIPTION, des);
    }

    private static StringBuilder getDailies(CallDaily.GWCallDaily dailies, boolean tomorrow) {
        StringBuilder builder = new StringBuilder();
        ArrayList<Integer> ids = new ArrayList<>();
        ArrayList<CallDaily.GWCallDailyNames> daileNames;
        CallDaily.GWCallDailyStrikes strikes = !tomorrow ? CallDaily.getStrikes().get(0) : CallDaily.getStrikes().get(1);
        ArrayList<CallPactSupply.GWCallPactSupply> supply = !tomorrow ? CallPactSupply.getSupplies(0) : CallPactSupply.getSupplies(1);

        if (strikes == null || supply == null) return null;
        for (int i = 0; i < 4; i++) {
            switch (i) {
                case 0:
                    for (int j = 0; j < dailies.pve.size(); j++) {
                        if (dailies.pve.get(j).level.max == 80 && !ids.contains(dailies.pve.get(j).id))
                            ids.add(dailies.pve.get(j).id);
                    }
                    break;

                case 1:
                    for (int j = 0; j < dailies.pvp.size(); j++) {
                        if (!ids.contains(dailies.pvp.get(j).id))
                            ids.add(dailies.pvp.get(j).id);
                    }
                    break;

                case 2:
                    for (int j = 0; j < dailies.wvw.size(); j++) {
                        if (!ids.contains(dailies.wvw.get(j).id))
                            ids.add(dailies.wvw.get(j).id);
                    }
                    break;

                case 3:
                    for (int j = 0; j < dailies.fractals.size(); j++) {
                        if (!ids.contains(dailies.fractals.get(j).id))
                            ids.add(dailies.fractals.get(j).id);
                    }
                    break;
            }
        }
        daileNames = CallDaily.getDailiesName(ids);

        //1=pve, pvp, wvw, 2=dailyFractals, 3=recFractals
        List<String> dailyPve = splitDailies(daileNames, 1);
        List<String> dailyPvp = splitDailies(daileNames, 2);
        List<String> dailyWvW = splitDailies(daileNames, 3);
        List<String> dailyFractals = splitDailies(daileNames, 4);
        List<String> recFractals = splitDailies(daileNames, 5);

        if (lang.equalsIgnoreCase("de")) {
            builder.append("[size=11][color=" + colors.mainColor + "][b]")
                    .append(!tomorrow ? "Dailies von heute:" : "Dailies von morgen:")
                    .append("[/b][/color][/size]\n\n");
        }

        if (lang.equalsIgnoreCase("en")) {
            builder.append("[size=11][color=" + colors.mainColor + "][b]")
                    .append(!tomorrow ? "Dailies today:" : "Dailies of tomorrow:")
                    .append("[/b][/color][/size]\n\n");
        }

        builder.append("[size=10][color=" + colors.secondColor + "][b]PvE Dailies:\n")
                .append("[/b][/color][/size][size=9]");
        for (int i = 0; i < dailyPve.size(); i++) {
            builder.append("[size=9]").append(dailyPve.get(i)).append("[/size]");
            if (i < (dailyPve.size() - 1)) builder.append("\n");
        }
        builder.append("\n\n");

        builder.append("[size=10][color=" + colors.secondColor + "][b]PvP Dailies:\n")
                .append("[/b][/color][/size][size=9]");
        for (int i = 0; i < dailyPvp.size(); i++) {
            builder.append("[size=9]").append(dailyPvp.get(i)).append("[/size]");
            if (i < (dailyPvp.size() - 1)) builder.append("\n");
        }
        builder.append("\n\n");

        builder.append("[size=10][color=" + colors.secondColor + "][b]WvW Dailies:\n")
                .append("[/b][/color][/size][size=9]");
        for (int i = 0; i < dailyWvW.size(); i++) {
            builder.append("[size=9]").append(dailyWvW.get(i)).append("[/size]");
            if (i < (dailyWvW.size() - 1)) builder.append("\n");
        }
        builder.append("\n\n");

        if (lang.equalsIgnoreCase("de")) {
            builder.append("[size=10][color=" + colors.secondColor + "][b]Fraktal Dailies:\n")
                    .append("[/b][/color][/size][size=9]");
            for (int i = 0; i < dailyFractals.size(); i++) {
                builder.append("[size=9]").append(dailyFractals.get(i)).append("[/size]");
                if (i < (dailyFractals.size() - 1)) builder.append("\n");
            }
        }
        if (lang.equalsIgnoreCase("en")) {
            builder.append("[size=10][color=" + colors.secondColor + "][b]Fractal Dailies:\n")
                    .append("[/b][/color][/size][size=9]");
            for (int i = 0; i < dailyFractals.size(); i++) {
                builder.append("[size=9]").append(dailyFractals.get(i)).append("[/size]");
                if (i < (dailyFractals.size() - 1)) builder.append("\n");
            }
        }
        builder.append("\n\n");

        if (lang.equalsIgnoreCase("de")) {
            builder.append("[size=10][color=" + colors.secondColor + "][b]Empfohlene Fraktale:\n")
                    .append("[/b][/color][/size][size=9]");
            for (int i = 0; i < recFractals.size(); i++) {
                builder.append("[size=9]").append(recFractals.get(i)).append("[/size]");
                if (i < (recFractals.size() - 1)) builder.append("\n");
            }
        }
        if (lang.equalsIgnoreCase("en")) {
            builder.append("[size=10][color=" + colors.secondColor + "][b]Recommended Fractals:\n")
                    .append("[/b][/color][/size][size=9]");
            for (int i = 0; i < recFractals.size(); i++) {
                builder.append("[size=9]").append(recFractals.get(i)).append("[/size]");
                if (i < (recFractals.size() - 1)) builder.append("\n");
            }
        }
        builder.append("\n\n");

        builder.append("[size=10][color=" + colors.secondColor + "][b]Daily Strike Mission:\n")
                .append("[/b][/color][/size][img]https://i.epvpimg.com/cfb6fab.png[/img] [size=9]")
                .append(Gw2Utils.formatDailyStrike(strikes.strike));
        builder.append("[/size]\n\n");

        if (lang.equalsIgnoreCase("de")) {
            builder.append("[size=10][color=" + colors.secondColor + "][b]Daily Pakt-Vorratsnetz-Agenten:\n")
                    .append("[/b][/color][/size] [size=9]").append(getLocations(supply));
        }
        if (lang.equalsIgnoreCase("en")) {
            builder.append("[size=10][color=" + colors.secondColor + "][b]Daily Pact-Supply-Agents:\n")
                    .append("[/b][/color][/size] [size=9]").append(getLocations(supply));
        }
        builder.append("[/size]\n\n");

        builder.append("[size=10][color=" + colors.secondColor + "][b]Profit:\n")
                .append("[/b][/color][/size] [size=9]")
                .append("[URL=https://wiki.guildwars2.com/wiki/Map_bonus_reward/profit]Map Belohnungen[/URL][/size]");
        builder.append("[/size]\n\n");

        if (lang.equalsIgnoreCase("de")) {
            builder.append("[size=10][color=" + colors.secondColor + "][b]Fraktions Versorger:[/b][/color][/size]\n")
                    .append("[size=9]").append(getProvisioners()).append("[/size]");
        }
        if (lang.equalsIgnoreCase("en")) {
            builder.append("[size=10][color=" + colors.secondColor + "][b]Faction Provisioner:[/b][/color][/size]\n")
                    .append("[size=9]").append(getProvisioners()).append("[/size]");
        }

        return builder;
    }

    private static String getProvisioners() {
        StringBuilder provisioners = new StringBuilder();

        if (lang.equalsIgnoreCase("de")) {
            for (int i = 0; i < provisioner_agents.namesDe.size(); i++) {
                provisioners.append(provisioner_agents.namesDe.get(i)).append(" @").append(provisioner_agents.location.get(i));
                if (i < (provisioner_agents.namesDe.size() - 1)) provisioners.append(", ");
            }
        }
        if (lang.equalsIgnoreCase("en")) {
            for (int i = 0; i < provisioner_agents.namesEn.size(); i++) {
                provisioners.append(provisioner_agents.namesEn.get(i)).append(" @").append(provisioner_agents.location.get(i));
                if (i < (provisioner_agents.namesEn.size() - 1)) provisioners.append(", ");
            }
        }
        return provisioners.toString();
    }

    private static String getLocations(ArrayList<CallPactSupply.GWCallPactSupply> supplies) {
        StringBuilder supply = new StringBuilder();

        for (int i = 0; i < supplies.size(); i++) {
            supply.append(supplies.get(i).name).append(" @").append(supplies.get(i).location[1]);
            if (i < (supplies.size() - 1)) supply.append(", ");
        }
        return supply.toString();
    }

    private static List<String> splitDailies(ArrayList<CallDaily.GWCallDailyNames> dailyNames, int mode) {
        List<String> dailies = new ArrayList<>();

        switch (mode) {
            case 1:
                for (CallDaily.GWCallDailyNames dailyName : dailyNames) {
                    if (!dailyName.name.contains("Tier 1") && !dailyName.name.contains("Tier 2") &&
                            !dailyName.name.contains("Tier 3") && !dailyName.name.contains("Tier 4") &&
                            !dailyName.name.contains("Recommended") && !dailyName.name.contains("PvP") &&
                            !dailyName.name.contains("Top Stats") && !dailyName.name.contains("WvW") &&
                            !dailyName.name.contains("Mists Guard Killer")) {
                        String name = Gw2Utils.formatDaily(dailyName.name);
                        dailies.add("[img]https://i.epvpimg.com/f6E1cab.png[/img] " + name);
                    }
                }
                break;

            case 2:
                for (CallDaily.GWCallDailyNames dailyName : dailyNames) {
                    if (dailyName.name.contains("PvP") || dailyName.name.contains("Top Stats")) {
                        String name = Gw2Utils.formatDailiesPvpWvw(dailyName.name);
                        dailies.add("[img]https://i.epvpimg.com/MLQ3fab.png[/img] " + name);
                    }
                }
                break;

            case 3:
                for (CallDaily.GWCallDailyNames dailyName : dailyNames) {
                    if (dailyName.name.contains("WvW") || dailyName.name.contains("Mists Guard Killer")) {
                        String name = Gw2Utils.formatDailiesPvpWvw(dailyName.name);
                        dailies.add("[img]https://i.epvpimg.com/WHXtfab.png[/img] " + name);
                    }
                }
                break;

            case 4:
                List<String> unsorted = new ArrayList<>();
                for (CallDaily.GWCallDailyNames dailyName : dailyNames) {
                    if (!dailyName.name.contains("Tier 4")) continue;
                    String name;
                    name = Gw2Utils.formatDailyFractals(dailyName.name);
                    unsorted.add(name);
                }
                sort(unsorted);
                for (String s : unsorted) dailies.add("[img]https://i.epvpimg.com/UgDScab.png[/img] " + s);
                break;

            case 5:
                for (CallDaily.GWCallDailyNames dailyName : dailyNames) {
                    if (!dailyName.name.contains("Recommended")) continue;
                    String name = Gw2Utils.formatRecFractals(dailyName.name);
                    dailies.add("[img]https://i.epvpimg.com/UgDScab.png[/img] " + name);
                }
                break;
        }
        return dailies;
    }

    private static void sort(List<String> getRecFractals) {
        getRecFractals.sort((o1, o2) -> {
            int s1int = Integer.parseInt(o1.substring(0, o1.indexOf(" ")));
            int s2int = Integer.parseInt(o2.substring(0, o2.indexOf(" ")));
            return s1int - s2int;
        });
    }
}
