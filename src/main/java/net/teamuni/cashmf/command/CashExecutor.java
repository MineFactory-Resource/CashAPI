package net.teamuni.cashmf.command;

import net.teamuni.cashmf.CashMF;
import net.teamuni.cashmf.config.MessageConf;
import net.teamuni.cashmf.data.CashManager;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Map;

public class CashExecutor implements CommandExecutor {
    private final CashMF main;

    public CashExecutor(CashMF instance) {
        this.main = instance;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (sender instanceof Player player) {
            CashManager cashManager = main.getCashManager();
            MessageConf messageConf = main.getMessageConf();
            Map<String, List<String>> messageMap = messageConf.getMessages();
            DecimalFormat df = new DecimalFormat("###,###");

            if (command.getName().equals("캐시")) {
                if (args.length == 0) {
                    if (!player.hasPermission("cashapi.cash")) {
                        messageConf.sendTranslatedMessage(player, messageMap.get("not_available_command"));
                        return false;
                    }
                    messageConf.sendTranslatedMessage(player, messageMap.get("cash_command_details"));
                } else if (args[0].equals("도움말")) {
                    if (!player.hasPermission("cashapi.help") | args.length != 1) {
                        messageConf.sendTranslatedMessage(player, messageMap.get("not_available_command"));
                        return false;
                    }
                    messageConf.sendTranslatedMessage(player, messageMap.get("cash_help"));
                } else if (args[0].equals("확인")) {
                    switch (args.length) {
                        case 1 -> {
                            if (!player.hasPermission("cashapi.look")) {
                                messageConf.sendTranslatedMessage(player, messageMap.get("not_available_command"));
                                return false;
                            }
                            cashManager.look(player, player);
                        }
                        case 2 -> {
                            OfflinePlayer target = Bukkit.getOfflinePlayerIfCached(args[1]);
                            if (!player.hasPermission("cashapi.seek")) {
                                messageConf.sendTranslatedMessage(player, messageMap.get("not_available_command"));
                                return false;
                            }
                            if (isInvalidPlayer(target)) {
                                messageConf.sendTranslatedMessage(player, messageMap.get("incorrect_player_name"));
                                return false;
                            }
                            cashManager.look(player, target);
                        }
                        default -> messageConf.sendTranslatedMessage(player, messageMap.get("not_available_command"));
                    }
                } else if (args[0].equals("지급") || args[0].equals("차감")) {
                    OfflinePlayer target = Bukkit.getOfflinePlayerIfCached(args[1]);
                    if (isInvalidPlayer(target)) {
                        messageConf.sendTranslatedMessage(player, messageMap.get("incorrect_player_name"));
                        return false;
                    }
                    if (!args[2].matches("[0-9]+")) {
                        messageConf.sendTranslatedMessage(player, messageMap.get("invalid_syntax"));
                        return false;
                    }
                    switch (args[0]) {
                        case "지급" -> {
                            if (!player.hasPermission("cashapi.add") | args.length != 3) {
                                messageConf.sendTranslatedMessage(player, messageMap.get("not_available_command"));
                                return false;
                            }
                            cashManager.edit(target, CashManager.EditType.ADD, Long.parseLong(args[2]));
                            messageConf.translate(player, messageMap.get("add_cash")
                                            , "%player%", target.getName()
                                            , "%amount%", df.format(Long.parseLong(args[2])))
                                    .forEach(player::sendMessage);
                        }
                        case "차감" -> {
                            if (!player.hasPermission("cashapi.sub") | args.length != 3) {
                                messageConf.sendTranslatedMessage(player, messageMap.get("not_available_command"));
                                return false;
                            }
                            cashManager.edit(target, CashManager.EditType.SUB, Long.parseLong(args[2]));
                            messageConf.translate(player, messageMap.get("sub_cash")
                                            , "%player%", target.getName()
                                            , "%amount%", df.format(Long.parseLong(args[2])))
                                    .forEach(player::sendMessage);
                        }
                    }
                } else {
                    messageConf.sendTranslatedMessage(player, messageMap.get("not_available_command"));
                }
            } else if (command.getName().equals("누적후원처리")) {
                if (args.length == 0 | args.length != 2) {
                    if (!player.hasPermission("cashapi.add")) {
                        messageConf.sendTranslatedMessage(player, messageMap.get("not_available_command"));
                        return false;
                    }
                    messageConf.sendTranslatedMessage(player, messageMap.get("cumul_cash_command_details"));
                }
                OfflinePlayer target = Bukkit.getOfflinePlayerIfCached(args[0]);
                if (isInvalidPlayer(target)) {
                    messageConf.sendTranslatedMessage(player, messageMap.get("incorrect_player_name"));
                    return false;
                }
                if (!args[1].matches("[0-9]+")) {
                    messageConf.sendTranslatedMessage(player, messageMap.get("invalid_syntax"));
                    return false;
                }
                cashManager.updateCumul(target, Long.parseLong(args[1]));
                messageConf.translate(player, messageMap.get("add_cumul_cash")
                                , "%player%", target.getName()
                                , "%amount%", df.format(Long.parseLong(args[1])))
                        .forEach(player::sendMessage);
            }
        }
        return true;
    }

    private boolean isInvalidPlayer(OfflinePlayer player) {
        if (player == null) return true;
        return !main.getDatabase().hasAccount(player.getUniqueId());
    }
}
