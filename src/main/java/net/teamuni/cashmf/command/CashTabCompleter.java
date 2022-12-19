package net.teamuni.cashmf.command;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class CashTabCompleter implements TabCompleter {

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player player) {
            List<String> suggestions = new ArrayList<>();

            if (args.length == 1) {
                if (player.hasPermission("cashapi.help"))
                    suggestions.add("도움말");
                if (player.hasPermission("cashapi.look"))
                    suggestions.add("확인");
                if (player.hasPermission("cashapi.add"))
                    suggestions.add("지급");
                if (player.hasPermission("cashapi.sub"))
                    suggestions.add("차감");
            }
            switch (args[0]) {
                case "지급" -> {
                    if (player.hasPermission("cashapi.add")) {
                        if (args.length == 2) {
                            Bukkit.getOnlinePlayers().forEach(p -> suggestions.add(p.getName()));
                        }
                    }
                }
                case "차감" -> {
                    if (player.hasPermission("cashapi.sub")) {
                        if (args.length == 2) {
                            Bukkit.getOnlinePlayers().forEach(p -> suggestions.add(p.getName()));
                        }
                    }
                }
                case "확인" -> {
                    if (player.hasPermission("cashapi.seek")) {
                        if (args.length == 2) {
                            Bukkit.getOnlinePlayers().forEach(p -> suggestions.add(p.getName()));
                        }
                    }
                }
            }
            return suggestions;
        }
        return null;
    }
}
