package de.backxtar.gw2;

import de.backxtar.Config;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class Gw2Utils {
    private static final Config.Colors colors = Config.getColors();
    private static final String lang = Config.getConfigData().lang;

    public static String getJson(String urlString) throws IOException {
        BufferedReader reader = null;
        try {
            URL url = new URL(urlString);
            reader = new BufferedReader(new InputStreamReader(read(url)));
            StringBuilder stringBuilder = new StringBuilder();
            int read;
            char[] chars = new char[1024];
            while ((read = reader.read(chars)) != -1) {
                stringBuilder.append(chars, 0, read);
            }
            return stringBuilder.toString();
        } finally {
            if (reader != null) {
                reader.close();
            }
        }
    }

    private static InputStream read(URL url) throws IOException {
        HttpURLConnection httpCon = (HttpURLConnection) url.openConnection();
        httpCon.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/41.0.2228.0 Safari/537.36");
        httpCon.setConnectTimeout(10000);
        httpCon.setReadTimeout(10000);
        return httpCon.getInputStream();
    }

    public static String currency(int id, long amount) {
        //https://api.guildwars2.com/v2/currencies?ids=all
        String currency = "";

        switch (id) {
            case 1 -> {
                long[] coins = getCoins(amount);
                if (lang.equalsIgnoreCase("de")) currency = "╰ [color=" + colors.mainColor + "][b]Coins:[/b][/color] " +
                        (coins[2] > 0 ? "[b]" + coins[2] + "[/b] Gold, " : "") +
                        (coins[1] > 0 ? "[b]" + coins[1] + "[/b] Silber, " : "") +
                        (coins[0] > 0 ? "[b]" + coins[0] + "[/b] Kupfer" : "");
                if (lang.equalsIgnoreCase("en")) currency = "╰ [color=" + colors.mainColor + "][b]Coins:[/b][/color] " +
                        (coins[2] > 0 ? "[b]" + coins[2] + "[/b] Gold, " : "") +
                        (coins[1] > 0 ? "[b]" + coins[1] + "[/b] Silver, " : "") +
                        (coins[0] > 0 ? "[b]" + coins[0] + "[/b] Copper" : "");
            }
            case 2 -> currency = "╰ [color=" + colors.mainColor + "][b]Karma:[/b][/color] " + amount;
            case 3 -> {
                if (lang.equalsIgnoreCase("de"))
                    currency = "╰ [color=" + colors.mainColor + "][b]Lorbeeren:[/b][/color] " + amount;
                if (lang.equalsIgnoreCase("en"))
                    currency = "╰ [color=" + colors.mainColor + "][b]Laurels:[/b][/color] " + amount;
            }
            case 4 -> {
                if (lang.equalsIgnoreCase("de"))
                    currency = "╰ [color=" + colors.mainColor + "][b]Edelsteine:[/b][/color] " + amount;
                if (lang.equalsIgnoreCase("en"))
                    currency = "╰ [color=" + colors.mainColor + "][b]Gems:[/b][/color] " + amount;
            }
            case 5 -> {
                if (lang.equalsIgnoreCase("de"))
                    currency = "╰ [color=" + colors.mainColor + "][b]Ascalonische Tränen:[/b][/color] " + amount;
                if (lang.equalsIgnoreCase("en"))
                    currency = "╰ [color=" + colors.mainColor + "][b]Ascalonian Tears:[/b][/color] " + amount;
            }
            case 6 -> {
                if (lang.equalsIgnoreCase("de"))
                    currency = "╰ [color=" + colors.mainColor + "][b]Scherben des Zhaitan:[/b][/color] " + amount;
                if (lang.equalsIgnoreCase("en"))
                    currency = "╰ [color=" + colors.mainColor + "][b]Shards of Zhaitan:[/b][/color] " + amount;
            }
            case 7 -> {
                if (lang.equalsIgnoreCase("de"))
                    currency = "╰ [color=" + colors.mainColor + "][b]Fraktal-Relikte:[/b][/color] " + amount;
                if (lang.equalsIgnoreCase("en"))
                    currency = "╰ [color=" + colors.mainColor + "][b]Fractals Relics:[/b][/color] " + amount;
            }
            case 9 -> {
                if (lang.equalsIgnoreCase("de"))
                    currency = "╰ [color=" + colors.mainColor + "][b]Beetletuns Siegel:[/b][/color] " + amount;
                if (lang.equalsIgnoreCase("en"))
                    currency = "╰ [color=" + colors.mainColor + "][b]Seals of Beetletuns:[/b][/color] " + amount;
            }
            case 10 -> {
                if (lang.equalsIgnoreCase("de"))
                    currency = "╰ [color=" + colors.mainColor + "][b]Manifeste des Maulwetariats:[/b][/color] " + amount;
                if (lang.equalsIgnoreCase("en"))
                    currency = "╰ [color=" + colors.mainColor + "][b]Manifestos of the Moletariate:[/b][/color] " + amount;
            }
            case 11 -> {
                if (lang.equalsIgnoreCase("de"))
                    currency = "╰ [color=" + colors.mainColor + "][b]Tödliche Blüten:[/b][/color] " + amount;
                if (lang.equalsIgnoreCase("en"))
                    currency = "╰ [color=" + colors.mainColor + "][b]Deadly Blooms:[/b][/color] " + amount;
            }
            case 12 -> {
                if (lang.equalsIgnoreCase("de"))
                    currency = "╰ [color=" + colors.mainColor + "][b]Symbole Kodas:[/b][/color] " + amount;
                if (lang.equalsIgnoreCase("en"))
                    currency = "╰ [color=" + colors.mainColor + "][b]Symbols of Koda:[/b][/color] " + amount;
            }
            case 13 -> {
                if (lang.equalsIgnoreCase("de"))
                    currency = "╰ [color=" + colors.mainColor + "][b]Flammen-Legion-Charr-Schnitzereien:[/b][/color] " + amount;
                if (lang.equalsIgnoreCase("en"))
                    currency = "╰ [color=" + colors.mainColor + "][b]Flame Legion Charr Carvings:[/b][/color] " + amount;
            }
            case 14 -> {
                if (lang.equalsIgnoreCase("de"))
                    currency = "╰ [color=" + colors.mainColor + "][b]Wissenskristalle:[/b][/color] " + amount;
                if (lang.equalsIgnoreCase("en"))
                    currency = "╰ [color=" + colors.mainColor + "][b]Knowledge Crystals:[/b][/color] " + amount;
            }
            case 15 -> {
                if (lang.equalsIgnoreCase("de"))
                    currency = "╰ [color=" + colors.mainColor + "][b]Ehrenabzeichen:[/b][/color] " + amount;
                if (lang.equalsIgnoreCase("en"))
                    currency = "╰ [color=" + colors.mainColor + "][b]Badges of Honor:[/b][/color] " + amount;
            }
            case 16 -> {
                if (lang.equalsIgnoreCase("de"))
                    currency = "╰ [color=" + colors.mainColor + "][b]Gilden-Belobigungen:[/b][/color] " + amount;
                if (lang.equalsIgnoreCase("en"))
                    currency = "╰ [color=" + colors.mainColor + "][b]Guild Commendations:[/b][/color] " + amount;
            }
            case 18 -> {
                if (lang.equalsIgnoreCase("de"))
                    currency = "╰ [color=" + colors.mainColor + "][b]Transmutation-Ladungen:[/b][/color] " + amount;
                if (lang.equalsIgnoreCase("en"))
                    currency = "╰ [color=" + colors.mainColor + "][b]Transmutation Charges:[/b][/color] " + amount;
            }
            case 19 -> {
                if (lang.equalsIgnoreCase("de"))
                    currency = "╰ [color=" + colors.mainColor + "][b]Luftschiff-Teile:[/b][/color] " + amount;
                if (lang.equalsIgnoreCase("en"))
                    currency = "╰ [color=" + colors.mainColor + "][b]Airship Parts:[/b][/color] " + amount;
            }
            case 20 -> {
                if (lang.equalsIgnoreCase("de"))
                    currency = "╰ [color=" + colors.mainColor + "][b]Ley-Linien-Kristalle:[/b][/color] " + amount;
                if (lang.equalsIgnoreCase("en"))
                    currency = "╰ [color=" + colors.mainColor + "][b]Ley Line Crystals:[/b][/color] " + amount;
            }
            case 22 -> {
                if (lang.equalsIgnoreCase("de"))
                    currency = "╰ [color=" + colors.mainColor + "][b]Aurilliumklumpen:[/b][/color] " + amount;
                if (lang.equalsIgnoreCase("en"))
                    currency = "╰ [color=" + colors.mainColor + "][b]Lumps of Aurillium:[/b][/color] " + amount;
            }
            case 23 -> {
                if (lang.equalsIgnoreCase("de"))
                    currency = "╰ [color=" + colors.mainColor + "][b]Geister-Scherben:[/b][/color] " + amount;
                if (lang.equalsIgnoreCase("en"))
                    currency = "╰ [color=" + colors.mainColor + "][b]Spirit Shards:[/b][/color] " + amount;
            }
            case 24 -> {
                if (lang.equalsIgnoreCase("de"))
                    currency = "╰ [color=" + colors.mainColor + "][b]Makelloses Fraktal-Relikte:[/b][/color] " + amount;
                if (lang.equalsIgnoreCase("en"))
                    currency = "╰ [color=" + colors.mainColor + "][b]Pristine Fractal Relics:[/b][/color] " + amount;
            }
            case 25 -> {
                if (lang.equalsIgnoreCase("de"))
                    currency = "╰ [color=" + colors.mainColor + "][b]Geoden:[/b][/color] " + amount;
                if (lang.equalsIgnoreCase("en"))
                    currency = "╰ [color=" + colors.mainColor + "][b]Geodes:[/b][/color] " + amount;
            }
            case 26 -> {
                if (lang.equalsIgnoreCase("de"))
                    currency = "╰ [color=" + colors.mainColor + "][b]WvW-Gefecht-Tickets:[/b][/color] " + amount;
                if (lang.equalsIgnoreCase("en"))
                    currency = "╰ [color=" + colors.mainColor + "][b]WvW Skirmish Claim Ticket:[/b][/color] " + amount;
            }
            case 27 -> {
                if (lang.equalsIgnoreCase("de"))
                    currency = "╰ [color=" + colors.mainColor + "][b]Banditen-Wappen:[/b][/color] " + amount;
                if (lang.equalsIgnoreCase("en"))
                    currency = "╰ [color=" + colors.mainColor + "][b]Bandit Crests:[/b][/color] " + amount;
            }
            case 28 -> {
                if (lang.equalsIgnoreCase("de"))
                    currency = "╰ [color=" + colors.mainColor + "][b]Magnetitscherben:[/b][/color] " + amount;
                if (lang.equalsIgnoreCase("en"))
                    currency = "╰ [color=" + colors.mainColor + "][b]Magnetite Shards:[/b][/color] " + amount;
            }
            case 29 -> {
                if (lang.equalsIgnoreCase("de"))
                    currency = "╰ [color=" + colors.mainColor + "][b]Versorger-Marken:[/b][/color] " + amount;
                if (lang.equalsIgnoreCase("en"))
                    currency = "╰ [color=" + colors.mainColor + "][b]Provisioner Tokens:[/b][/color] " + amount;
            }
            case 30 -> {
                if (lang.equalsIgnoreCase("de"))
                    currency = "╰ [color=" + colors.mainColor + "][b]PvP-Liga-Tickets:[/b][/color] " + amount;
                if (lang.equalsIgnoreCase("en"))
                    currency = "╰ [color=" + colors.mainColor + "][b]PvP League Tickets:[/b][/color] " + amount;
            }
            case 31 -> {
                if (lang.equalsIgnoreCase("de"))
                    currency = "╰ [color=" + colors.mainColor + "][b]Beweise der Heldentaten:[/b][/color] " + amount;
                if (lang.equalsIgnoreCase("en"))
                    currency = "╰ [color=" + colors.mainColor + "][b]Proof of Heroics:[/b][/color] " + amount;
            }
            case 32 -> {
                if (lang.equalsIgnoreCase("de"))
                    currency = "╰ [color=" + colors.mainColor + "][b]Aufgestiegene Scherben des Ruhms:[/b][/color] " + amount;
                if (lang.equalsIgnoreCase("en"))
                    currency = "╰ [color=" + colors.mainColor + "][b]Ascended Shard of Glory:[/b][/color] " + amount;
            }
            case 33 -> {
                if (lang.equalsIgnoreCase("de"))
                    currency = "╰ [color=" + colors.mainColor + "][b]Entfesselte Magie:[/b][/color] " + amount;
                if (lang.equalsIgnoreCase("en"))
                    currency = "╰ [color=" + colors.mainColor + "][b]Unbound Magic:[/b][/color] " + amount;
            }
            case 34 -> {
                if (lang.equalsIgnoreCase("de"))
                    currency = "╰ [color=" + colors.mainColor + "][b]Handelsverträge:[/b][/color] " + amount;
                if (lang.equalsIgnoreCase("en"))
                    currency = "╰ [color=" + colors.mainColor + "][b]Trade Contracts:[/b][/color] " + amount;
            }
            case 35 -> {
                if (lang.equalsIgnoreCase("de"))
                    currency = "╰ [color=" + colors.mainColor + "][b]Elegie-Mosaike:[/b][/color] " + amount;
                if (lang.equalsIgnoreCase("en"))
                    currency = "╰ [color=" + colors.mainColor + "][b]Elegy Mosaics:[/b][/color] " + amount;
            }
            case 36 -> {
                if (lang.equalsIgnoreCase("de"))
                    currency = "╰ [color=" + colors.mainColor + "][b]Zeugnisse von Heldentaten:[/b][/color] " + amount;
                if (lang.equalsIgnoreCase("en"))
                    currency = "╰ [color=" + colors.mainColor + "][b]Testimony of Heroics:[/b][/color] " + amount;
            }
            case 37 -> {
                if (lang.equalsIgnoreCase("de"))
                    currency = "╰ [color=" + colors.mainColor + "][b]Erhabene-Schlüssel:[/b][/color] " + amount;
                if (lang.equalsIgnoreCase("en"))
                    currency = "╰ [color=" + colors.mainColor + "][b]Exalted Keys:[/b][/color] " + amount;
            }
            case 38 -> {
                if (lang.equalsIgnoreCase("de"))
                    currency = "╰ [color=" + colors.mainColor + "][b]Macheten:[/b][/color] " + amount;
                if (lang.equalsIgnoreCase("en"))
                    currency = "╰ [color=" + colors.mainColor + "][b]Machetes:[/b][/color] " + amount;
            }
            case 39 -> {
                if (lang.equalsIgnoreCase("de"))
                    currency = "╰ [color=" + colors.mainColor + "][b]Gaets-Kristalle:[/b][/color] " + amount;
                if (lang.equalsIgnoreCase("en"))
                    currency = "╰ [color=" + colors.mainColor + "][b]Gaeting Crystals:[/b][/color] " + amount;
            }
            case 40 -> {
                if (lang.equalsIgnoreCase("de"))
                    currency = "╰ [color=" + colors.mainColor + "][b]Banditen-Dietriche:[/b][/color] " + amount;
                if (lang.equalsIgnoreCase("en"))
                    currency = "╰ [color=" + colors.mainColor + "][b]Bandit Skeleton Keys:[/b][/color] " + amount;
            }
            case 41 -> {
                if (lang.equalsIgnoreCase("de"))
                    currency = "╰ [color=" + colors.mainColor + "][b]Pakt-Brechstangen:[/b][/color] " + amount;
                if (lang.equalsIgnoreCase("en"))
                    currency = "╰ [color=" + colors.mainColor + "][b]Pact Crowbars:[/b][/color] " + amount;
            }
            case 42 -> {
                if (lang.equalsIgnoreCase("de"))
                    currency = "╰ [color=" + colors.mainColor + "][b]Phiolen Chak-Säure:[/b][/color] " + amount;
                if (lang.equalsIgnoreCase("en"))
                    currency = "╰ [color=" + colors.mainColor + "][b]Vials of Chak Acid:[/b][/color] " + amount;
            }
            case 43 -> {
                if (lang.equalsIgnoreCase("de"))
                    currency = "╰ [color=" + colors.mainColor + "][b]Zephyriten-Dietriche:[/b][/color] " + amount;
                if (lang.equalsIgnoreCase("en"))
                    currency = "╰ [color=" + colors.mainColor + "][b]Zephyrite Lockpicks:[/b][/color] " + amount;
            }
            case 44 -> {
                if (lang.equalsIgnoreCase("de"))
                    currency = "╰ [color=" + colors.mainColor + "][b]Schlüssel des Händlers:[/b][/color] " + amount;
                if (lang.equalsIgnoreCase("en"))
                    currency = "╰ [color=" + colors.mainColor + "][b]Trader's Keys:[/b][/color] " + amount;
            }
            case 45 -> {
                if (lang.equalsIgnoreCase("de"))
                    currency = "╰ [color=" + colors.mainColor + "][b]Flüchtige Magie:[/b][/color] " + amount;
                if (lang.equalsIgnoreCase("en"))
                    currency = "╰ [color=" + colors.mainColor + "][b]Volatile Magic:[/b][/color] " + amount;
            }
            case 46 -> {
                if (lang.equalsIgnoreCase("de"))
                    currency = "╰ [color=" + colors.mainColor + "][b]PvP-Turnier-Gutscheine:[/b][/color] " + amount;
                if (lang.equalsIgnoreCase("en"))
                    currency = "╰ [color=" + colors.mainColor + "][b]PvP Turnament Tickets:[/b][/color] " + amount;
            }
            case 47 -> {
                if (lang.equalsIgnoreCase("de"))
                    currency = "╰ [color=" + colors.mainColor + "][b]Renn-Medaillen:[/b][/color] " + amount;
                if (lang.equalsIgnoreCase("en"))
                    currency = "╰ [color=" + colors.mainColor + "][b]Racing Medallions:[/b][/color] " + amount;
            }
            case 49 -> {
                if (lang.equalsIgnoreCase("de"))
                    currency = "╰ [color=" + colors.mainColor + "][b]Nebelgeborenere Schlüssel:[/b][/color] " + amount;
                if (lang.equalsIgnoreCase("en"))
                    currency = "╰ [color=" + colors.mainColor + "][b]Mistborn Keys:[/b][/color] " + amount;
            }
            case 50 -> {
                if (lang.equalsIgnoreCase("de"))
                    currency = "╰ [color=" + colors.mainColor + "][b]Festmarken:[/b][/color] " + amount;
                if (lang.equalsIgnoreCase("en"))
                    currency = "╰ [color=" + colors.mainColor + "][b]Festival Tokens:[/b][/color] " + amount;
            }
            case 51 -> {
                if (lang.equalsIgnoreCase("de"))
                    currency = "╰ [color=" + colors.mainColor + "][b]Lager-Schlüssel:[/b][/color] " + amount;
                if (lang.equalsIgnoreCase("en"))
                    currency = "╰ [color=" + colors.mainColor + "][b]Cache Keys:[/b][/color] " + amount;
            }
            case 52 -> {
                if (lang.equalsIgnoreCase("de"))
                    currency = "╰ [color=" + colors.mainColor + "][b]Rote Propheten-Scherben:[/b][/color] " + amount;
                if (lang.equalsIgnoreCase("en"))
                    currency = "╰ [color=" + colors.mainColor + "][b]Red Prophet Shards:[/b][/color] " + amount;
            }
            case 53 -> {
                if (lang.equalsIgnoreCase("de"))
                    currency = "╰ [color=" + colors.mainColor + "][b]Grüne Propheten-Scherben:[/b][/color] " + amount;
                if (lang.equalsIgnoreCase("en"))
                    currency = "╰ [color=" + colors.mainColor + "][b]Green Prophet Shards:[/b][/color] " + amount;
            }
            case 54 -> {
                if (lang.equalsIgnoreCase("de"))
                    currency = "╰ [color=" + colors.mainColor + "][b]Blauer Propheten-Kristalle:[/b][/color] " + amount;
                if (lang.equalsIgnoreCase("en"))
                    currency = "╰ [color=" + colors.mainColor + "][b]Blue Prophet Crystals:[/b][/color] " + amount;
            }
            case 55 -> {
                if (lang.equalsIgnoreCase("de"))
                    currency = "╰ [color=" + colors.mainColor + "][b]Grüner Propheten-Kristalle:[/b][/color] " + amount;
                if (lang.equalsIgnoreCase("en"))
                    currency = "╰ [color=" + colors.mainColor + "][b]Green Prophet Crystals:[/b][/color] " + amount;
            }
            case 56 -> {
                if (lang.equalsIgnoreCase("de"))
                    currency = "╰ [color=" + colors.mainColor + "][b]Roter Propheten-Kristalle:[/b][/color] " + amount;
                if (lang.equalsIgnoreCase("en"))
                    currency = "╰ [color=" + colors.mainColor + "][b]Red Prophet Crystals:[/b][/color] " + amount;
            }
            case 57 -> {
                if (lang.equalsIgnoreCase("de"))
                    currency = "╰ [color=" + colors.mainColor + "][b]Blaue Propheten-Scherben:[/b][/color] " + amount;
                if (lang.equalsIgnoreCase("en"))
                    currency = "╰ [color=" + colors.mainColor + "][b]Blue Prophet Shards:[/b][/color] " + amount;
            }
            case 58 -> {
                if (lang.equalsIgnoreCase("de"))
                    currency = "╰ [color=" + colors.mainColor + "][b]Kriegs-Vorräte:[/b][/color] " + amount;
                if (lang.equalsIgnoreCase("en"))
                    currency = "╰ [color=" + colors.mainColor + "][b]War Supplies:[/b][/color] " + amount;
            }
            case 59 -> {
                if (lang.equalsIgnoreCase("de"))
                    currency = "╰ [color=" + colors.mainColor + "][b]Instabile Fraktal-Essenzen:[/b][/color] " + amount;
                if (lang.equalsIgnoreCase("en"))
                    currency = "╰ [color=" + colors.mainColor + "][b]Unstable Fractal Essence:[/b][/color] " + amount;
            }
            case 60 -> {
                if (lang.equalsIgnoreCase("de"))
                    currency = "╰ [color=" + colors.mainColor + "][b]Tyrianische Verteidigungssiegel:[/b][/color] " + amount;
                if (lang.equalsIgnoreCase("en"))
                    currency = "╰ [color=" + colors.mainColor + "][b]Tyrian Defense Seals:[/b][/color] " + amount;
            }
        }
        return currency;
    }

    public static long[] getCoins(long amount) {
        long[] coins = new long[3];

        long copper = amount;
        long silver = copper / 100;
        long gold   = silver / 100;
        copper %= 100;
        silver %= 100;

        coins[0] = copper;
        coins[1] = silver;
        coins[2] = gold;
        return coins;
    }

    public static String formatRecFractals(String input) {
        String subString = input.substring(32);
        String formatted = null;

        switch (subString) {
            case "1", "19", "28", "52" -> {
                if (lang.equalsIgnoreCase("de")) formatted = subString + " - Vulkanisch";
                if (lang.equalsIgnoreCase("en")) formatted = subString + " - Volcanic";
            }
            case "2", "36", "44", "62" -> {
                if (lang.equalsIgnoreCase("de")) formatted = subString + " - Nicht kategorisiert";
                if (lang.equalsIgnoreCase("en")) formatted = subString + " - Uncategorized";
            }
            case "3", "27", "51", "68" -> {
                if (lang.equalsIgnoreCase("de")) formatted = subString + " - Schneeblind";
                if (lang.equalsIgnoreCase("en")) formatted = subString + " - Snowblind";
            }
            case "4", "31", "57", "66" -> {
                if (lang.equalsIgnoreCase("de")) formatted = subString + " - Urbanes Schlachtfeld";
                if (lang.equalsIgnoreCase("en")) formatted = subString + " - Urban Battleground";
            }
            case "5", "17", "32", "56" -> {
                if (lang.equalsIgnoreCase("de")) formatted = subString + " - Sumpfland";
                if (lang.equalsIgnoreCase("en")) formatted = subString + " - Swampland";
            }
            case "6", "21", "47", "69" -> {
                if (lang.equalsIgnoreCase("de")) formatted = subString + " - Felswand";
                if (lang.equalsIgnoreCase("en")) formatted = subString + " - Cliffside";
            }
            case "7", "26", "61" -> {
                if (lang.equalsIgnoreCase("de")) formatted = subString + " - Unterwasserruinen";
                if (lang.equalsIgnoreCase("en")) formatted = subString + " - Aquatic Ruins";
            }
            case "8", "29", "53" -> {
                if (lang.equalsIgnoreCase("de")) formatted = subString + " - Untergrundeinrichtung";
                if (lang.equalsIgnoreCase("en")) formatted = subString + " - Underground Facility";
            }
            case "9", "22", "39", "58" -> {
                if (lang.equalsIgnoreCase("de")) formatted = subString + " - Feuriger Hochofen";
                if (lang.equalsIgnoreCase("en")) formatted = subString + " - Molten Furnace";
            }
            case "10", "40", "70" -> {
                if (lang.equalsIgnoreCase("de")) formatted = subString + " - Feuriger Boss";
                if (lang.equalsIgnoreCase("en")) formatted = subString + " - Molten Boss";
            }
            case "11", "33", "67" -> {
                if (lang.equalsIgnoreCase("de")) formatted = subString + " - Tiefenstein";
                if (lang.equalsIgnoreCase("en")) formatted = subString + " - Deepstone";
            }
            case "12", "37", "54" -> {
                if (lang.equalsIgnoreCase("de")) formatted = subString + " - Riff der Sirene";
                if (lang.equalsIgnoreCase("en")) formatted = subString + " - Siren's Reef";
            }
            case "13", "30", "38", "63" -> formatted = subString + " - Chaos";
            case "14", "46", "65", "71" -> {
                if (lang.equalsIgnoreCase("de")) formatted = subString + " - Ätherklinge";
                if (lang.equalsIgnoreCase("en")) formatted = subString + " - Aetherblade";
            }
            case "15", "34", "43", "55", "64" -> {
                if (lang.equalsIgnoreCase("de")) formatted = subString + " - Thaumanova-Reaktor";
                if (lang.equalsIgnoreCase("en")) formatted = subString + " - Thaumanova Reactor";
            }
            case "16", "41", "59" -> {
                if (lang.equalsIgnoreCase("de")) formatted = subString + " - Zwielichtoase";
                if (lang.equalsIgnoreCase("en")) formatted = subString + " - Twilight Oasis";
            }
            case "18", "42", "72" -> {
                if (lang.equalsIgnoreCase("de")) formatted = subString + " - Kapitän Mai Trin Boss";
                if (lang.equalsIgnoreCase("en")) formatted = subString + " - Captain Mai Trin Boss";
            }
            case "20", "35", "45", "60" -> {
                if (lang.equalsIgnoreCase("de")) formatted = subString + " - Solider Ozean";
                if (lang.equalsIgnoreCase("en")) formatted = subString + " - Solid Ocean";
            }
            case "23", "48", "73" -> {
                if (lang.equalsIgnoreCase("de")) formatted = subString + " - Albtraum";
                if (lang.equalsIgnoreCase("en")) formatted = subString + " - Nightmare";
            }
            case "24", "49", "74" -> {
                if (lang.equalsIgnoreCase("de")) formatted = subString + " - Zerschmettertes Observatorium";
                if (lang.equalsIgnoreCase("en")) formatted = subString + " - Shattered Observatory";
            }
            case "25", "50", "75" -> {
                if (lang.equalsIgnoreCase("de")) formatted = subString + " - Sunqua-Gipfel";
                if (lang.equalsIgnoreCase("en")) formatted = subString + " - Sunqua Peak";
            }
            default -> formatted = "ERROR";
        }
        return formatted;
    }

    public static String formatDailyFractals(String input) {
        String subString = input.substring(13);
        String formatted = null;

        switch (subString) {
            case "Aquatic Ruins" -> {
                if (lang.equalsIgnoreCase("de")) formatted = "76 - Unterwasserruinen";
                if (lang.equalsIgnoreCase("en")) formatted = "76 - " + subString;
            }
            case "Swampland" -> {
                if (lang.equalsIgnoreCase("de")) formatted = "89 - Sumpfland";
                if (lang.equalsIgnoreCase("en")) formatted = "89 - " + subString;
            }
            case "Siren's Reef" -> {
                if (lang.equalsIgnoreCase("de")) formatted = "78 - Riff der Sirene";
                if (lang.equalsIgnoreCase("en")) formatted = "78 - " + subString;
            }
            case "Uncategorized" -> {
                if (lang.equalsIgnoreCase("de")) formatted = "91 - Nicht kategorisiert";
                if (lang.equalsIgnoreCase("en")) formatted = "91 - " + subString;
            }
            case "Solid Ocean" -> {
                if (lang.equalsIgnoreCase("de")) formatted = "80 - Solider Ozean";
                if (lang.equalsIgnoreCase("en")) formatted = "80 - " + subString;
            }
            case "Underground Facility" -> {
                if (lang.equalsIgnoreCase("de")) formatted = "81 - Untergrundeinrichtung";
                if (lang.equalsIgnoreCase("en")) formatted = "81 - " + subString;
            }
            case "Thaumanova Reactor" -> {
                if (lang.equalsIgnoreCase("de")) formatted = "82 - Thaumanova-Reaktor";
                if (lang.equalsIgnoreCase("en")) formatted = "82 - " + subString;
            }
            case "Molten Furnace" -> {
                if (lang.equalsIgnoreCase("de")) formatted = "83 - Feuriger Hochofen";
                if (lang.equalsIgnoreCase("en")) formatted = "83 - " + subString;
            }
            case "Deepstone" -> {
                if (lang.equalsIgnoreCase("de")) formatted = "84 - Tiefenstein";
                if (lang.equalsIgnoreCase("en")) formatted = "84 - " + subString;
            }
            case "Urban Battleground" -> {
                if (lang.equalsIgnoreCase("de")) formatted = "85 - Urbanes Schlachtfeld";
                if (lang.equalsIgnoreCase("en")) formatted = "85 - " + subString;
            }
            case "Snowblind" -> {
                if (lang.equalsIgnoreCase("de")) formatted = "93 - Schneeblind";
                if (lang.equalsIgnoreCase("en")) formatted = "93 - " + subString;
            }
            case "Twilight Oasis" -> {
                if (lang.equalsIgnoreCase("de")) formatted = "87 - Zwielichtoase";
                if (lang.equalsIgnoreCase("en")) formatted = "87 - " + subString;
            }
            case "Chaos" -> formatted = "97 - Chaos";
            case "Molten Boss" -> {
                if (lang.equalsIgnoreCase("de")) formatted = "90 - Feuriger Boss";
                if (lang.equalsIgnoreCase("en")) formatted = "90 - " + subString;
            }
            case "Volcanic" -> {
                if (lang.equalsIgnoreCase("de")) formatted = "92 - Vulkanisch";
                if (lang.equalsIgnoreCase("en")) formatted = "92 - " + subString;
            }
            case "Cliffside" -> {
                if (lang.equalsIgnoreCase("de")) formatted = "94 - Felswand";
                if (lang.equalsIgnoreCase("en")) formatted = "94 - " + subString;
            }
            case "Captain Mai Trin Boss" -> {
                if (lang.equalsIgnoreCase("de")) formatted = "95 - Kapitän Mai Trin Boss";
                if (lang.equalsIgnoreCase("en")) formatted = "95 - " + subString;
            }
            case "Aetherblade" -> {
                if (lang.equalsIgnoreCase("de")) formatted = "96 - Ätherklinge";
                if (lang.equalsIgnoreCase("en")) formatted = "96 - " + subString;
            }
            case "Nightmare" -> {
                if (lang.equalsIgnoreCase("de")) formatted = "98 - Albtraum";
                if (lang.equalsIgnoreCase("en")) formatted = "98 - " + subString;
            }
            case "Shattered Observatory" -> {
                if (lang.equalsIgnoreCase("de")) formatted = "99 - Zerschmettertes Observatorium";
                if (lang.equalsIgnoreCase("en")) formatted = "99 - " + subString;
            }
            case "Sunqua Peak" -> {
                if (lang.equalsIgnoreCase("de")) formatted = "100 - Sunqua-Gipfel";
                if (lang.equalsIgnoreCase("en")) formatted = "100 - " + subString;
            }
            default -> formatted = "ERROR";
        }
        return formatted;
    }

    public static String formatDailyStrike(String input) {
        String formatted = null;

        switch (input) {
            case "boneskinner" -> {
                if (lang.equalsIgnoreCase("de")) formatted = "Knochenhäuter";
                if (lang.equalsIgnoreCase("en")) formatted = "Boneskinner";
            }
            case "fraenir_of_jormag" -> {
                if (lang.equalsIgnoreCase("de")) formatted = "Fraenir Jormags";
                if (lang.equalsIgnoreCase("en")) formatted = "Fraenir of Jormag";
            }
            case "icebrood_construct" -> {
                if (lang.equalsIgnoreCase("de")) formatted = "Zittergipfel-Pass";
                if (lang.equalsIgnoreCase("en")) formatted = "Shiverpeaks Pass";
            }
            case "voice_of_the_fallen" -> {
                if (lang.equalsIgnoreCase("de")) formatted = "Stimme der Gefallenen und Klaue der Gefallenen";
                if (lang.equalsIgnoreCase("en")) formatted = "Voice of the Fallen and Claw of the Fallen";
            }
            case "whisper_of_jormag" -> {
                if (lang.equalsIgnoreCase("de")) formatted = "Geflüster des Jormag";
                if (lang.equalsIgnoreCase("en")) formatted = "Whisper of Jormag";
            }
            case "cold_war" -> {
                if (lang.equalsIgnoreCase("de")) formatted = "Kalter Krieg";
                if (lang.equalsIgnoreCase("en")) formatted = "Cold War";
            }
            default -> formatted = "ERROR";
        }
        return formatted;
    }

    public static String formatDaily(String input) {
        String formatted = null;

        if (lang.equalsIgnoreCase("de")) {
            formatted = switch (input) {
                case "Daily Ascalonian Catacombs" -> "Tägliche Erforschung: Katakomben von Ascalon";
                case "Daily Caudecus's Manor" -> "Tägliche Erforschung: Caudecus' Anwesen";
                case "Daily Twilight Arbor" -> "Tägliche Erforschung: Zwielichtgarten";
                case "Daily Sorrow's Embrace" -> "Tägliche Erforschung: Umarmung der Betrübnis";
                case "Daily Citadel of Flame" -> "Tägliche Erforschung: Flammenzitadelle";
                case "Daily Honor of the Waves" -> "Tägliche Erforschung: Zierde der Wogen";
                case "Daily Crucible of Eternity" -> "Tägliche Erforschung: Schmelztiegel der Ewigkeit";
                case "Daily Ruined City of Arah" -> "Tägliche Erforschung: Ruinenstadt Arah";
                case "Daily Ascalon Forager" -> "Täglicher Erntearbeiter von Ascalon";
                case "Daily Ascalon Lumberer" -> "Täglicher Holzfäller von Ascalon";
                case "Daily Ascalon Miner" -> "Täglicher Bergarbeiter von Ascalon";
                case "Daily Kryta Forager" -> "Täglicher Erntearbeiter von Kryta";
                case "Daily Kryta Lumberer" -> "Täglicher Holzfäller von Kryta";
                case "Daily Kryta Miner" -> "Täglicher Bergarbeiter von Kryta";
                case "Daily Maguuma Jungle Forager" -> "Täglicher Erntearbeiter des Maguuma-Dschungels";
                case "Daily Maguuma Jungle Lumberer" -> "Täglicher Holzfäller des Maguuma-Dschungels";
                case "Daily Maguuma Jungle Miner" -> "Täglicher Bergarbeiter des Maguuma-Dschungels";
                case "Daily Maguuma Wastes Forager" -> "Täglicher Erntearbeiter der Maguuma-Einöde";
                case "Daily Maguuma Wastes Lumberer" -> "Täglicher Holzfäller der Maguuma-Einöde";
                case "Daily Maguuma Wastes Miner" -> "Täglicher Bergarbeiter der Maguuma-Einöde";
                case "Daily Orr Forager" -> "Täglicher Erntearbeiter von Orr";
                case "Daily Orr Lumberer" -> "Täglicher Holzfäller von Orr";
                case "Daily Orr Miner" -> "Täglicher Bergarbeiter von Orr";
                case "Daily Shiverpeaks Forager" -> "Täglicher Erntearbeiter der Zittergipfel";
                case "Daily Shiverpeaks Lumberer" -> "Täglicher Holzfäller der Zittergipfel";
                case "Daily Shiverpeaks Miner" -> "Täglicher Bergarbeiter der Zittergipfel";
                case "Daily Heart of Maguuma Forager" -> "Täglicher Sammler: Herz von Maguuma";
                case "Daily Heart of Maguuma Lumberer" -> "Täglicher Holzfäller: Herz von Maguuma";
                case "Daily Heart of Maguuma Miner" -> "Täglicher Bergarbeiter: Herz von Maguuma";
                case "Daily Desert Forager" -> "Täglicher Erntearbeiter der Wüste";
                case "Daily Desert Lumberer" -> "Täglicher Holzfäller der Wüste";
                case "Daily Desert Miner" -> "Täglicher Bergarbeiter der Wüste";
                // Renown Hearts
                case "Daily Elon Riverlands Taskmaster" -> "Täglicher Meister der Aufgaben der Elon-Flusslande";
                case "Daily Desolation Taskmaster" -> "Täglicher Meister der Aufgaben des Ödlandes";
                // World Bosses
                case "Daily Claw of Jormag" -> "Tägliche Klaue von Jormag";
                case "Daily Demolisher" -> "Täglicher Zerstörer";
                case "Daily Fire Elemental" -> "Täglicher Feuer-Elementar";
                case "Daily Frozen Maw" -> "Täglicher Gefrorener Schlund";
                case "Daily Great Jungle Wurm" -> "Täglicher Großer Dschungelwurm";
                case "Daily Hound Master" -> "Täglicher Meister der Hunde";
                case "Daily Inquest Golem Mark II" -> "Täglicher Inquestur-Golem Typ II";
                case "Daily Megadestroyer" -> "Täglicher Mega-Zerstörer";
                case "Daily Shadow Behemoth" -> "Täglicher Schatten-Behemoth";
                case "Daily Shatterer" -> "Täglicher Zerschmetterer";
                // Other
                case "Daily Activity Participation" -> "Täglicher Aktivitäts-Teilnehmer";
                case "Daily Mystic Forger" -> "Täglicher mystischer Schmied";
                // Adventures
                case "Daily Adventure: A Fungus Among Us" -> "Tägliches Abenteuer: Pöse Pilze!";
                case "Daily Adventure: Beetle Feast" -> "Tägliches Abenteuer: Käferschmaus";
                case "Daily Adventure: Bugs in the Branches" -> "Tägliches Abenteuer: Käfer im Geäst";
                case "Daily Adventure: Drone Race" -> "Tägliches Abenteuer: Drohnen-Rennen";
                case "Daily Adventure: Fallen Masks" -> "Tägliches Abenteuer: Gefallenen-Masken";
                case "Daily Adventure: Flying Circus" -> "Tägliches Abenteuer: Schauflug";
                case "Daily Adventure: Haywire Punch-o-Matic Battle" -> "Tägliches Abenteuer: Kampf als durchdrehender Haudrauf-o-Mat";
                case "Daily Adventure: On Wings of Gold" -> "Tägliches Abenteuer: Auf goldenen Schwingen";
                case "Daily Adventure: Salvage Pit" -> "Tägliches Abenteuer: Bergungsmaterial-Grube";
                case "Daily Adventure: Sanctum Scramble" -> "Tägliches Abenteuer: Rauferei am Refugium";
                case "Daily Adventure: Scrap Rifle Field Test" -> "Tägliches Abenteuer: Schrottgewehr-Feldversuch";
                case "Daily Adventure: Shooting Gallery" -> "Tägliches Abenteuer: Schießbude";
                case "Daily Adventure: Tendril Torchers" -> "Tägliches Abenteuer: Rankenbrenner";
                case "Daily Adventure: The Floor Is Lava?" -> "Tägliches Abenteuer: Der Boden besteht aus Lava?";
                case "Daily Adventure: The Ley-Line Run" -> "Tägliches Abenteuer: Das Ley-Linien-Rennen";
                case "Daily Desert Adventurer" -> "Täglicher Abenteurer der Wüste";
                //Vistas
                case "Daily Ascalon Vista Viewer" -> "Täglicher Panorama-Genießer von Ascalon";
                case "Daily Kryta Vista Viewer" -> "Täglicher Panorama-Genießer von Kryta";
                case "Daily Maguuma Vista Viewer" -> "Täglicher Panorama-Genießer von Maguuma";
                case "Daily Maguuma Wastes Vista Viewer" -> "Täglicher Panorama-Genießer der Maguuma-Einöde";
                case "Daily Orr Vista Viewer" -> "Täglicher Panorama-Genießer von Orr";
                case "Daily Shiverpeaks Vista Viewer" -> "Täglicher Panorama-Genießer der Zittergipfel";
                case "Daily Heart of Maguuma Vista Viewer" -> "Täglicher Panorama-Genießer: Herz von Maguuma";
                case "Daily Desert Vista Viewer" -> "Täglicher Panorama-Genießer der Wüste";
                // Jumping Puzzle
                case "Daily Antre of Adjournment Jumping Puzzle" -> "Tägliches Sprungrätsel: Der Abgrund des Plünderers";
                case "Daily Behem Gauntlet Jumping Puzzle" -> "Tägliches Sprungrätsel: Behem-Spießrutenlauf";
                case "Daily Branded Mine Jumping Puzzle" -> "Tägliches Sprungrätsel: Die Gebrandmarkte Mine";
                case "Daily Buried Archives Jumping Puzzle" -> "Tägliches Sprungrätsel: Verschüttete Archive";
                case "Daily Chaos Crystal Cavern Jumping Puzzle" -> "Tägliches Sprungrätsel: Die Chaoskristallhöhle";
                case "Daily Coddler's Cove Jumping Puzzle" -> "Tägliches Sprungrätsel: Knuddlerbucht";
                case "Daily Collapsed Observatory Jumping Puzzle" -> "Tägliches Sprungrätsel: Das eingestürzte Observatorium";
                case "Daily Conundrum Cubed Jumping Puzzle" -> "Tägliches Sprungrätsel: Zwickmühle hoch drei";
                case "Daily Crash Site Jumping Puzzle" -> "Tägliches Sprungrätsel: Absturzstelle";
                case "Daily Craze's Folly Jumping Puzzle" -> "Tägliches Sprungrätsel: Crazes Torheit";
                case "Daily Crimson Plateau Jumping Puzzle" -> "Tägliches Sprungrätsel: Purpur-Plateau";
                case "Daily Dark Reverie Jumping Puzzle" -> "Tägliches Sprungrätsel: Finsterer Tagtraum";
                case "Daily Demongrub Pits Jumping Puzzle" -> "Tägliches Sprungrätsel: Dämonenraupen-Gruben";
                case "Daily Fawcett's Bounty Jumping Puzzle" -> "Tägliches Sprungrätsel: Fawcetts Beute";
                case "Daily Goemm's Lab Jumping Puzzle" -> "Tägliches Sprungrätsel: Goemms Labor";
                case "Daily Grendich Gamble Jumping Puzzle" -> "Tägliches Sprungrätsel: Grendich-Spiel";
                case "Daily Griffonrook Run Jumping Puzzle" -> "Tägliches Sprungrätsel: Greifenturmstrecke";
                case "Daily Hexfoundry Jumping Puzzle" -> "Tägliches Sprungrätsel: Fluchgießerei Abgedreht";
                case "Daily Hidden Garden Jumping Puzzle" -> "Tägliches Sprungrätsel: Verborgener Garten";
                case "Daily King Jalis's Refuge Jumping Puzzle" -> "Tägliches Sprungrätsel: König Jalis' Zuflucht";
                case "Daily Loreclaw Expanse Jumping Puzzle" -> "Tägliches Sprungrätsel: Sagenklauen-Weite";
                case "Daily Morgan's Leap Jumping Puzzle" -> "Tägliches Sprungrätsel: Morgans Sprung";
                case "Daily Only Zuhl Jumping Puzzle" -> "Tägliches Sprungrätsel: Nur Zuhl";
                case "Daily Pig Iron Quarry Jumping Puzzle" -> "Tägliches Sprungrätsel: Die Roheisen-Grube";
                case "Daily Portmatt's Lab Jumping Puzzle" -> "Tägliches Sprungrätsel: Professor Portmatts Labor";
                case "Daily Scavenger's Chasm Jumping Puzzle" -> "Tägliches Sprungrätsel: Der Abgrund des Plünderers";
                case "Daily Shaman's Rookery Jumping Puzzle" -> "Tägliches Sprungrätsel: Schamanen-Kolonie";
                case "Daily Shattered Ice Ruins Jumping Puzzle" -> "Tägliches Sprungrätsel: Die Zerschmetterten Eisruinen";
                case "Daily Skipping Stones Jumping Puzzle" -> "Tägliches Sprungrätsel: Steine hüpfen lassen";
                case "Daily Spekks's Lab Jumping Puzzle" -> "Tägliches Sprungrätsel: Spekks Labor";
                case "Daily Spelunker's Delve Jumping Puzzle" -> "Tägliches Sprungrätsel: Höhlenforscher-Senke";
                case "Daily Swashbuckler's Cove Jumping Puzzle" -> "Tägliches Sprungrätsel: Die Säbelrassler-Bucht";
                case "Daily Tribulation Caverns Jumping Puzzle" -> "Tägliches Sprungrätsel: Trübsalriss-Höhlen";
                case "Daily Tribulation Rift Jumping Puzzle" -> "Tägliches Sprungrätsel: Trübsalriss";
                case "Daily Under New Management Jumping Puzzle" -> "Tägliches Sprungrätsel: Unter neuer Leitung";
                case "Daily Urmaug's Secret Jumping Puzzle" -> "Tägliches Sprungrätsel: Urmaugs Geheimnis";
                case "Daily Vizier's Tower Jumping Puzzle" -> "Tägliches Sprungrätsel: Der Turm des Wesirs";
                case "Daily Wall Breach Blitz Jumping Puzzle" -> "Tägliches Sprungrätsel: Wallbrecherblitz";
                case "Daily Weyandt's Revenge Jumping Puzzle" -> "Tägliches Sprungrätsel: Weyandts Rache";
                // Events
                case "Daily Blazeridge Steppes Event Completer" -> "Tägliche Event-Koryphäe der Flammenkamm-Steppe";
                case "Daily Bloodtide Coast Event Completer" -> "Tägliche Event-Koryphäe der Blutstrom-Küste";
                case "Daily Brisban Wildlands Event Completer" -> "Tägliche Event-Koryphäe der Brisban-Wildnis";
                case "Daily Caledon Forest Event Completer" -> "Tägliche Event-Koryphäe des Caledon-Walds";
                case "Daily Cursed Shore Event Completer" -> "Tägliche Event-Koryphäe der Fluchküste";
                case "Daily Diessa Plateau Event Completer" -> "Tägliche Event-Koryphäe des Diessa-Plateaus";
                case "Daily Dredgehaunt Cliffs Event Completer" -> "Tägliche Event-Koryphäe der Schauflerschreck-Klippen";
                case "Daily Dry Top Event Completer" -> "Tägliche Event-Koryphäe der Trockenkuppe";
                case "Daily Fields of Ruin Event Completer" -> "Tägliche Event-Koryphäe der Felder der Verwüstung";
                case "Daily Fireheart Rise Event Completer" -> "Tägliche Event-Koryphäe der Feuerherzhügel";
                case "Daily Frostgorge Sound Event Completer" -> "Tägliche Event-Koryphäe des Eisklamm-Sunds";
                case "Daily Gendarran Fields Event Completer" -> "Tägliche Event-Koryphäe der Gendarran-Felder";
                case "Daily Harathi Hinterlands Event Completer" -> "Tägliche Event-Koryphäe des Harathi-Hinterlands";
                case "Daily Iron Marches Event Completer" -> "Tägliche Event-Koryphäe der Eisenmark";
                case "Daily Kessex Hills Event Completer" -> "Tägliche Event-Koryphäe der Kessex-Hügel";
                case "Daily Lornar's Pass Event Completer" -> "Tägliche Event-Koryphäe von Lornars Pass";
                case "Daily Malchor's Leap Event Completer" -> "Tägliche Event-Koryphäe von Malchors Sprung";
                case "Daily Metrica Province Event Completer" -> "Tägliche Event-Koryphäe der Provinz Metrica";
                case "Daily Mount Maelstrom Event Completer" -> "Tägliche Event-Koryphäe des Mahlstromgipfels";
                case "Daily Plains of Ashford Event Completer" -> "Tägliche Event-Koryphäe der Ebenen von Aschfurt";
                case "Daily Queensdale Event Completer" -> "Tägliche Event-Koryphäe des Königintals";
                case "Daily Silverwastes Event Completer" -> "Tägliche Event-Koryphäe der Silberwüste";
                case "Daily Snowden Drifts Event Completer" -> "Tägliche Event-Koryphäe der Schneekuhlenhöhen";
                case "Daily Southsun Cove Event Completer" -> "Tägliche Event-Koryphäe der Südlicht-Bucht";
                case "Daily Sparkfly Fen Event Completer" -> "Tägliche Event-Koryphäe des Funkenschwärmersumpfs";
                case "Daily Straits of Devastation Event Completer" -> "Tägliche Event-Koryphäe der Meerenge der Verwüstung";
                case "Daily Timberline Falls Event Completer" -> "Tägliche Event-Koryphäe der Baumgrenzen-Fälle";
                case "Daily Wayfarer Foothills Event Completer" -> "Tägliche Event-Koryphäe der Wanderer-Hügel";
                case "Daily Verdant Brink Event Completer" -> "Tägliche Event-Koryphäe: Grasgrüne Schwelle";
                case "Daily Auric Basin Event Completer" -> "Tägliche Event-Koryphäe: Güldener Talkessel";
                case "Daily Tangled Depths Event Completer" -> "Tägliche Event-Koryphäe: Verschlungene Tiefen";
                case "Daily Dragon's Stand Event Completer" -> "Tägliche Event-Koryphäe: Drachensturz";
                case "Daily Crystal Oasis Event Completer" -> "Tägliche Event-Koryphäe: Kristall Oase";
                case "Daily Desert Highlands Event Completer" -> "Tägliche Event-Koryphäe: Wüsten-Hochland";
                case "Daily Elon Riverlands Event Completer" -> "Tägliche Event-Koryphäe: Elon-Flusslande";
                case "Daily Desolation Event Completer" -> "Tägliche Event-Koryphäe: Ödland";
                case "Daily Vabbi Event Completer" -> "Tägliche Event-Koryphäe: Vabbi";
                // Bounties
                case "Daily Crystal Oasis Bounty Hunter" -> "Tägliche Kopfgeldjäger der Kristalloase";
                case "Daily Desert Highlands Bounty Hunter" -> "Täglicher Kopfgeldjäger des Wüsten-Hochlands";
                case "Daily Elon Riverlands Bounty Hunter" -> "Täglicher Kopfgeldjäger der Elon-Flusslande";
                case "Daily Desolation Bounty Hunter" -> "Täglicher Kopfgeldjäger des Ödlands";
                case "Daily Vabbian Bounty Hunter" -> "Täglicher Kopfgeldjäger von Vaabi";
                // Minidungeons
                case "Daily Bad Neighborhood Minidungeon" -> "Tägliches Mini-Verlies: Miese Nachbarschaft";
                case "Daily Don't Touch the Shiny Minidungeon" -> "Tägliches Mini-Verlies: Finger weg vom Glänzigen";
                case "Daily Forgotten Stream Minidungeon" -> "Tägliches Mini-Verlies: Der vergessene Fluss";
                case "Daily Forsaken Fortune Minidungeon" -> "Tägliches Mini-Verlies: Verdammte Schätze";
                case "Daily Goff's Loot Minidungeon" -> "Tägliches Mini-Verlies: Goffs Beute";
                case "Daily Grounded Minidungeon" -> "Tägliches Mini-Verlies: Geerdet";
                case "Daily Magellan's Memento Minidungeon" -> "Tägliches Mini-Verlies: Magellans Andenken";
                case "Daily Rebel's Seclusion Minidungeon" -> "Tägliches Mini-Verlies: Unterschlupf der Rebellen";
                case "Daily Ship of Sorrows Minidungeon" -> "Tägliches Mini-Verlies: Schiff des Leids";
                case "Daily Tears of Itlaocol Minidungeon" -> "Tägliches Mini-Verlies: Die Tränen Itlaocols";
                case "Daily The Long Way Around Minidungeon" -> "Tägliches Mini-Verlies: Ein großer Umweg";
                case "Daily Vexa's Lab Minidungeon" -> "Tägliches Mini-Verlies: Vexas Labor";
                case "Daily Windy Cave Treasure Minidungeon" -> "Tägliches Mini-Verlies: Schatz der Zugigen Höhle";
                default -> "ERROR";
            };
        }
        if (lang.equalsIgnoreCase("en")) formatted = input;
        return formatted;
    }

    public static String formatDailiesPvpWvw(String input) {
        String formatted = null;

        if (lang.equalsIgnoreCase("de")) {
            formatted = switch (input) {
                case "Daily PvP Matches Played in Unranked or Ranked Arena" -> "Tägliche PvP-Matches in Arena mit oder ohne Rangwertung";
                case "Daily PvP Player Kills" -> "Tägliche PvP-Spielersiege";
                case "Daily PvP Rank Points" -> "Tägliche PvP-Rangpunkte";
                case "Daily PvP Rated Game Winner" -> "Täglicher Gewinner eines bewerteten PvP-Spiels";
                case "Daily PvP Reward Earner" -> "Täglicher PvP-Belohnungsmeister";
                case "Daily PvP Tournament Participator" -> "Täglicher Teilnehmer an einem PvP-Turnier";
                case "Daily Top Stats" -> "Tägliche Top-Stats";
                // WvW
                case "Daily Mists Guard Killer" -> "Täglicher Nebelwachen-Schlächter";
                case "Daily WvW Big Spender" -> "Täglicher WvW-Spendierer";
                case "Daily WvW Camp Capturer" -> "Täglicher WvW-Lager-Eroberer";
                case "Daily WvW Caravan Disruptor" -> "Täglicher WvW-Karawanen-Zerstörer";
                case "Daily WvW Invasion Defender" -> "Täglicher WvW-Invasions-Abwehrer";
                case "Daily WvW Keep Capturer" -> "Täglicher WvW-Festeneroberer";
                case "Daily WvW Land Claimer" -> "Täglicher WvW-Land-Beansprucher";
                case "Daily WvW Objective Defender" -> "Täglicher WvW-Zielobjekt-Verteidiger";
                case "Daily WvW Tower Capturer" -> "Täglicher WvW-Turm-Eroberer";
                case "Daily WvW World Ranker" -> "Täglicher WvW-Weltrang-Stürmer";
                case "Daily WvW Master of Monuments" -> "Täglicher Meister der Monumente";
                case "Daily WvW Veteran Creature Slayer" -> "Täglicher Bezwinger von WvW-Veteran-Kreaturen";
                default -> "ERROR";
            };
        }
        if (lang.equalsIgnoreCase("en")) formatted = input;
        return formatted;
    }
}
