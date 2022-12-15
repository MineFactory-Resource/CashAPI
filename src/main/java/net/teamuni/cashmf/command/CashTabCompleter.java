package net.teamuni.cashmf.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.util.StringUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CashTabCompleter implements TabCompleter {

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> completions = new ArrayList<>();
        List<String> commands = new ArrayList<>();
        if (args.length == 1) {
            if (sender.hasPermission("cashapi.help"))
                commands.add("도움말");
            if (sender.hasPermission("cashapi.look"))
                commands.add("확인");
            if (sender.hasPermission("cashapi.add"))
                commands.add("지급");
            if (sender.hasPermission("cashapi.sub"))
                commands.add("차감");
            StringUtil.copyPartialMatches(args[0], commands, completions);
        }
        Collections.sort(completions);

        return completions;
    }
}
