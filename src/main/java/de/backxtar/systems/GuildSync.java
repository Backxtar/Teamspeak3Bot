package de.backxtar.systems;

import com.github.theholywaffle.teamspeak3.TS3Api;
import com.github.theholywaffle.teamspeak3.api.wrapper.Client;
import de.backxtar.Config;
import de.backxtar.gw2.CallGuild;
import de.backxtar.managers.SqlManager;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class GuildSync {

    public static void syncRights(TS3Api api) {
        if (Config.getConfigData().serverGroups == null
                || Config.getConfigData().guildRanks[0].equalsIgnoreCase("0"))
            return;

        List<CallGuild.GWCallGuildMembers> members = CallGuild.getMembers();
        if (members == null) return;

        try {
            ResultSet resultSet = SqlManager.selectAll("API_Keys");

            while (resultSet.next()) {
                String identity = resultSet.getString("clientIdentity");
                String accountName = resultSet.getString("accountName");

                if (!api.isClientOnline(identity)) continue;
                Client client = api.getClientByUId(identity);

                if (members.parallelStream().noneMatch(member -> member.name.equalsIgnoreCase(accountName)) ||
                    members.parallelStream().anyMatch(member -> member.name.equalsIgnoreCase(accountName) &&
                            member.rank.equalsIgnoreCase("invited"))) {
                    int[] groups = client.getServerGroups();

                    for (int group : groups) {
                        if (Config.getConfigData().serverGroups.containsValue(group))
                            api.removeClientFromServerGroup(group, client.getDatabaseId());
                    }
                    continue;
                }
                CallGuild.GWCallGuildMembers user = null;

                for (CallGuild.GWCallGuildMembers member : members) {
                    if (member.name.equalsIgnoreCase(accountName) && !member.rank.equalsIgnoreCase("invited")) {
                        user = member;
                        break;
                    }
                }
                if (user == null || !Config.getConfigData().serverGroups.containsKey(user.rank)) continue;
                int serverGroup = Config.getConfigData().serverGroups.get(user.rank);
                int[] groups = client.getServerGroups();
                boolean hasGroup = false;

                for (int group : groups) {
                    if (group != serverGroup && Config.getConfigData().serverGroups.containsValue(group))
                        api.removeClientFromServerGroup(group, client.getDatabaseId());
                    if (group == serverGroup) hasGroup = true;
                }
                if (!hasGroup) api.addClientToServerGroup(serverGroup, client.getDatabaseId());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
