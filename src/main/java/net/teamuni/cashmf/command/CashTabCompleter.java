package net.teamuni.cashmf.command;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class CashTabCompleter implements TabCompleter {

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> completions = new ArrayList<>();
        List<String> commands = new ArrayList<>();
        if (args.length == 1) {
            if (sender.hasPermission("cashmf.help"))
                commands.add("도움말");
            if (sender.hasPermission("cashmf.look"))
                commands.add("확인");
            if (sender.hasPermission("cashmf.pay"))
                commands.add("보내기");
            if (sender.hasPermission("cashmf.add"))
                commands.add("지급");
            if (sender.hasPermission("cashmf.sub"))
                commands.add("차감");
            StringUtil.copyPartialMatches(args[0], commands, completions);
        }
        Collections.sort(completions);

        return completions;
    }
}
