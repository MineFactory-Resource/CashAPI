package net.teamuni.cashmf.command;

import java.text.DecimalFormat;
import net.teamuni.cashmf.CashMF;
import net.teamuni.cashmf.data.CashManager;
import net.teamuni.cashmf.data.CashManager.EditType;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class CashExecutor implements CommandExecutor {
    private final CashMF main;
    private final DecimalFormat df = new DecimalFormat("###,###");
    private static final String PERM_MANAGE = "cashapi.manage";

    public CashExecutor(CashMF instance) {
        this.main = instance;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command,
        @NotNull String label, String[] args) {
        if (!(sender instanceof Player player)) {
            return false;
        }
        switch (command.getName()) {
            case "캐시" -> handleCashCommand(player, args);
            case "누적후원처리" -> handleCumulativeDonationCommand(player, args);
        }

        return true;
    }

    private void handleCashCommand(Player player, String[] args) {
        if (args.length > 0) {
            if (!player.hasPermission(PERM_MANAGE)) {
                sendMessage(player, "not_available_command");
                return;
            }
        } else {
            main.getCashManager().look(player, player, false);
            return;
        }

        switch (args[0]) {
            case "확인" -> handleCheckCommand(player, args, false);
            case "지급" -> handlePaymentCommand(player, args,
                EditType.ADD, "add_cash", false);
            case "차감" -> handlePaymentCommand(player, args,
                EditType.SUB, "sub_cash", false);
            case "리로드" -> handleReloadCommand(player);
            default -> handleHelpCommand(player, false);
        }
    }

    private void handleCumulativeDonationCommand(Player player, String[] args) {
        if (!player.hasPermission(PERM_MANAGE)) {
            sendMessage(player, "not_available_command");
            return;
        }

        switch (args[0]) {
            case "확인" -> handleCheckCommand(player, args, true);
            case "증가" -> handlePaymentCommand(player, args,
                EditType.ADD, "add_cumul_cash", true);
            case "감소" -> handlePaymentCommand(player, args,
                EditType.SUB, "sub_cumul_cash", true);
            default -> handleHelpCommand(player, true);
        }
    }

    private void handleCheckCommand(Player player, String[] args, boolean isCumul) {
        if (args.length != 2) {
            sendMessage(player, "not_available_command");
            return;
        }

        OfflinePlayer target = Bukkit.getOfflinePlayerIfCached(args[1]);

        if (isInvalidPlayer(target)) {
            sendMessage(player, "incorrect_player_name");
            return;
        }

        main.getCashManager().look(player, target, isCumul);
    }

    private void handlePaymentCommand(Player player, String[] args,
        EditType editType, String messageKey, boolean isCumul) {
        if (args.length != 3) {
            sendMessage(player, "not_available_command");
            return;
        }

        OfflinePlayer target = Bukkit.getOfflinePlayerIfCached(args[1]);
        CashManager cashManager = main.getCashManager();

        if (isInvalidPlayer(target)) {
            sendMessage(player, "incorrect_player_name");
            return;
        }

        if (!args[2].matches("[0-9]+")) {
            sendMessage(player, "invalid_syntax");
            return;
        }

        long amount = Long.parseLong(args[2]);

        if (isCumul) {
            String msgKey = editType == EditType.ADD ? "add_cumul_cash" : "sub_cumul_cash";
            cashManager.updateCumul(target, amount);
            sendManageMessage(player, msgKey, amount);
            return;
        }

        cashManager.edit(target, editType, amount);
        sendManageMessage(player, messageKey, amount);
        sendMessage(player, "remind_update_cumul_cash");
    }

    private void handleReloadCommand(Player player) {
        main.reloadConfig();
        main.getMessageConf().reload();
        sendMessage(player, "reload_message");
    }

    private void handleHelpCommand(Player player, boolean isCumul) {
        String messageKey = isCumul ? "cash_cumul_help" : "cash_help";
        sendMessage(player, messageKey);
    }

    private boolean isInvalidPlayer(OfflinePlayer player) {
        return player == null || !main.getDatabase().hasAccount(player.getUniqueId());
    }

    private void sendMessage(Player player, String messageKey) {
        main.getMessageConf().sendTranslatedMessage(player, getMessages(messageKey));
    }

    private void sendManageMessage(Player player, String messageKey, long amount) {
        List<String> infoMessageKeys = List.of("add_cash", "sub_cash", "add_cumul_cash", "sub_cumul_cash");
        List<String> messages;

        if (!infoMessageKeys.contains(messageKey)) {
            return;
        }

        messages = main.getMessageConf().translate(player, getMessages(messageKey),
                "%player%", player.getName(), "%amount%", df.format(amount));

        main.getMessageConf().sendTranslatedMessage(player, messages);
    }

    private List<String> getMessages(String messageKey) {
        return main.getMessageConf().getMessages().get(messageKey);
    }
}

