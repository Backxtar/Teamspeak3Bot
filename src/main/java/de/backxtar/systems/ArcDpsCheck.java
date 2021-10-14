package de.backxtar.systems;

import com.github.theholywaffle.teamspeak3.TS3Api;
import com.github.theholywaffle.teamspeak3.api.ChannelProperty;
import de.backxtar.Config;
import de.backxtar.DerGeraet;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

public class ArcDpsCheck {

    public static void checkArcDpsVersion(TS3Api api) {
        if (Config.getConfigData().arcDpsChannelID == 0) return;

        String webpage = "https://www.deltaconnected.com/arcdps/x64/";
        String[] data;
        String d3d9 = "", md5sum = "", d3d9Date = "", md5sumDate = "";

        try {
            String html = Jsoup.connect(webpage).get().html();
            Document document = Jsoup.parse(html);
            Elements elements = document.select("tbody");

            data = elements.get(0).text().split(" ");
            d3d9 = data[8];
            d3d9Date = data[9] + " " + data[10] + " " + data[11];
            md5sum = data[12];
            md5sumDate = data[13] + " " + data[14] + " " + data[15];
        } catch (Exception e) {
            e.printStackTrace();
        }
        String desc = "[center][size=11][URL=client://" + api.whoAmI().getId() + "/" +
                        api.whoAmI().getUniqueIdentifier()+ "]Message me![/URL]" +
                        "\n[img]http://i.epvpimg.com/IhT7eab.png[/img]" +
                        "\n[color=" + Config.getColors().mainColor + "][b]Last modified[/b][/color][/size]" +
                        "\n[size=10][color=" + Config.getColors().secondColor + "][b]" + d3d9 + ":[/b][/color] " + d3d9Date +
                        "\n[color=" + Config.getColors().secondColor + "][b]" + md5sum + ":[/b][/color] " + md5sumDate +
                        "\n[color=" + Config.getColors().secondColor + "][b]Download:[/b][/color] [URL=https://www.deltaconnected.com/arcdps/x64/]Index of X64[/URL][/size]";
        if (api.getChannelInfo(Config.getConfigData().arcDpsChannelID).getDescription().equalsIgnoreCase(desc)) return;
        api.editChannel(Config.getConfigData().arcDpsChannelID, ChannelProperty.CHANNEL_DESCRIPTION, desc);
    }
}
