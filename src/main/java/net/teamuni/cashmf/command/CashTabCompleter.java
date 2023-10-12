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
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender,
        @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player player)) {
            return null;
        }

        if (!player.hasPermission("cashapi.manage")) {
            return null;
        }

        List<String> suggestions = new ArrayList<>();

        if (command.getName().equals("캐시")) {
            switch (args.length) {
                case 1 -> {
                    suggestions.add("확인");
                    suggestions.add("지급");
                    suggestions.add("차감");
                    suggestions.add("리로드");
                }
                case 2 -> {
                    switch (args[0]) {
                        case "지급", "차감", "확인" -> {
                            Bukkit.getOnlinePlayers()
                                .forEach(p -> suggestions.add(p.getName()));
                        }
                    }
                }
                case 3 -> {
                    switch (args[0]) {
                        case "지급", "차감" -> suggestions.add("금액");
                    }
                }
            }
            return suggestions;

        } else if (command.getName().equals("누적후원처리")) {
            if (player.hasPermission("cashapi.manage")) {
                switch (args.length) {
                    case 1 -> {
                        suggestions.add("증가");
                        suggestions.add("감소");
                    }
                    case 2 -> {
                        switch (args[0]) {
                            case "증가", "감소" -> {
                                Bukkit.getOnlinePlayers()
                                    .forEach(p -> suggestions.add(p.getName()));
                            }
                        }
                    }
                    case 3 -> {
                        switch (args[0]) {
                            case "증가", "감소" -> suggestions.add("금액");
                        }
                    }
                }
            }
            return suggestions;
        }
        return null;
    }
}
