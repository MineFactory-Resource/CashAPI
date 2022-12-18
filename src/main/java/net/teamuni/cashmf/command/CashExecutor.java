package net.teamuni.cashmf.command;

import net.teamuni.cashmf.CashMF;
import net.teamuni.cashmf.config.MessageConf;
import net.teamuni.cashmf.data.Cash;
import net.teamuni.cashmf.data.CashInfo;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class CashExecutor implements CommandExecutor {
    private final CashMF main;
    private final MessageConf messageConf;

    public CashExecutor(CashMF instance) {
        this.main = instance;
        this.messageConf = instance.getMessageConf();
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        // /캐시 명령어를 입력한 경우
        if (args.length == 0) {
            // cashapi.cash 권한이 없을 경우
            if (!sender.hasPermission("cashapi.cash")) {
                sender.sendMessage(messageConf.getMessage("no_perm"));
                return true;
            }
            look(sender);
            sender.sendMessage(messageConf.getMessage("details_ex"));

            // /캐시 도움말 명령어를 입력한 경우
        } else if (args[0].equals("도움말")) {
            // cashapi.help 권한이 없을 경우
            if (!sender.hasPermission("cashapi.help")) {
                sender.sendMessage(messageConf.getMessage("no_perm"));
                return true;
            }
            help(sender);

            // /캐시 확인 (<플레이어>) 명령어를 입력한 경우
        } else if (args[0].equals("확인")) {
            // cashapi.look 권한이 없을 경우
            if (!sender.hasPermission("cashapi.look")) {
                sender.sendMessage(messageConf.getMessage("no_perm"));
                return true;
            }

            // /캐시 확인
            if (args.length == 1) {
                look(sender);

                // /캐시 확인 <플레이어>
            } else {
                if (sender instanceof Player && args[1].equalsIgnoreCase(sender.getName())) {
                    look(sender);
                } else {
                    look(sender, args[1]);
                }
            }
        } else if (args[0].equals("지급")) {
            if (!sender.hasPermission("cashapi.add")) {
                sender.sendMessage(messageConf.getMessage("no_perm"));
                return true;
            }
            check(sender, args, 1);
        } else if (args[0].equals("차감")) {
            if (!sender.hasPermission("cashapi.sub")) {
                sender.sendMessage(messageConf.getMessage("no_perm"));
                return true;
            }
            check(sender, args, 2);
        } else {
            sender.sendMessage(messageConf.getMessage("exist_cmd"));
        }
        return true;
    }

    private void look(CommandSender sender) {
        // 플레이어가 자신의 캐시를 확인할 경우
        if (sender instanceof Player player) {
            Cash cash = main.getCashManager().getCache(player.getUniqueId());

            long point = cash.getInfo().cash();
            sender.sendMessage(messageConf.getMessage("look_cash")
                    .replace("%player%", player.getName())
                    .replace("%cash%", String.valueOf(point))
            );
            // 플레이어가 아닌 경우
        } else {
            sender.sendMessage(messageConf.getMessage("use_console"));
        }
    }

    private void look(CommandSender sender, String playerName) {
        Player player = Bukkit.getPlayer(playerName);
        if (player == null) return;
        Cash cash = main.getCashManager().getCache(player.getUniqueId());
        long point = cash.getInfo().cash();
        sender.sendMessage(messageConf.getMessage("look_cash")
                .replace("%player%", playerName)
                .replace("%cash%", String.valueOf(point))
        );
    }

    // 캐시 명령어 확인
    private void help(CommandSender sender) {
        sender.sendMessage(messageConf.getMessage("cash_look"));
        // 만약 관리자 명령어 권한이 있을 경우
        if (sender.hasPermission("cashapi.add") || sender.hasPermission("cashapi.sub"))
            sender.sendMessage("");
        if (sender.hasPermission("cashapi.add"))
            sender.sendMessage(messageConf.getMessage("cash_add"));
        if (sender.hasPermission("cashapi.sub"))
            sender.sendMessage(messageConf.getMessage("cash_sub"));
    }

    // 플레이어에게 캐시 지급하기
    private void edit(CommandSender sender, Cash data, long amount) {
        String name = Bukkit.getOfflinePlayer(data.getUuid()).getName();
        long oldCash = data.getInfo().cash();
        long newCash = oldCash + amount;
        long cumulativeCash;
        if (newCash > oldCash) {
            cumulativeCash = data.getInfo().cumulativeCash() + amount;
        } else {
            cumulativeCash = data.getInfo().cumulativeCash();
        }
        data.update(new CashInfo(newCash, cumulativeCash));

        sender.sendMessage(messageConf.getMessage("add_cash")
                .replace("%player%", name)
                .replace("%amount%", String.valueOf(amount)));
    }

    // 일부 명령어 확인 기능
    private void check(CommandSender sender, String[] args, int type) {
        if (args.length < 3) {
            sender.sendMessage(messageConf.getMessage("exist_cmd"));
        } else {
            try {
                long amount = Long.parseLong(args[2]);
                // 수량이 자연수가 아닌경우
                if (amount <= 0) {
                    sender.sendMessage(messageConf.getMessage("natural_num"));
                    return;
                }
                Player player = Bukkit.getPlayer(args[1]);
                if (player == null || !main.getDatabase().hasAccount(player.getUniqueId())) return;
                Cash target = main.getCashManager().getCache(player.getUniqueId());

                switch (type) {
                    case 1 -> edit(sender, target, amount);
                    case 2 -> edit(sender, target, amount * -1);
                }
                // 숫자가 잘못됐을 경우
            } catch (NumberFormatException e) {
                sender.sendMessage(messageConf.getMessage("natural_num"));
                // 플레이어의 정보를 불러올 수 없는 경우
            } catch (NullPointerException e) {
                sender.sendMessage(messageConf.getMessage("exist_player"));
            }
        }
    }
}
