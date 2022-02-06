package net.teamuni.cashmf.command;

import static net.teamuni.cashmf.CashMF.getMessageConf;
import net.teamuni.cashmf.Cash;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CashExecutor implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        // /캐시 명령어를 입력한 경우
        if (args.length==0) {
            // cashmf.cash 권한이 없을 경우
            if (!sender.hasPermission("cashmf.cash")) {
                 sender.sendMessage(getMessageConf().getMessage("no_perm"));
                return true;
            }
            look(sender);
            sender.sendMessage(getMessageConf().getMessage("details_ex"));

        // /캐시 도움말 명령어를 입력한 경우
        } else if (args[0].equals("도움말")) {
            // cashmf.help 권한이 없을 경우
            if (!sender.hasPermission("cashmf.help")) {
                sender.sendMessage(getMessageConf().getMessage("no_perm"));
                return true;
            }
            help(sender);

        // /캐시 확인 (<플레이어>) 명령어를 입력한 경우
        } else if (args[0].equals("확인")) {
            // cashmf.look 권한이 없을 경우
            if (!sender.hasPermission("cashmf.look")) {
                sender.sendMessage(getMessageConf().getMessage("no_perm"));
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

        // /캐시 보내기 <플레이어> <수량> 명령어를 입력한 경우
        } else if (args[0].equals("보내기")) {
            // cashmf.pay 권한이 없을 경우
            if (!sender.hasPermission("cashmf.pay")) {
                sender.sendMessage(getMessageConf().getMessage("no_perm"));
                return true;
            }

            check(sender, args, 0);

        // /캐시 지급 <플레이어> <수량> 명령어를 입력한 경우
        } else if (args[0].equals("지급")) {
            // cashmf.add 권한이 없을 경우
            if (!sender.hasPermission("cashmf.add")) {
                sender.sendMessage(getMessageConf().getMessage("no_perm"));
                return true;
            }

            check(sender, args, 1);

        // /캐시 차감 <플레이어> <수량> 명령어를 입력한 경우
        } else if (args[0].equals("차감")) {
            // cashmf.sub 권한이 없을 경우
            if (!sender.hasPermission("cashmf.sub")) {
                sender.sendMessage(getMessageConf().getMessage("no_perm"));
                return true;
            }

            check(sender, args, 2);
        } else {
            sender.sendMessage(getMessageConf().getMessage("exist_cmd"));
        }
        return true;
    }

    private void look(CommandSender sender) {
        // 플레이어가 자신의 캐시를 확인할 경우
        if (sender instanceof Player) {
            Player player = (Player)sender;
            Cash cash = Cash.getCash(player.getUniqueId());

            int point = cash.getCash();
            sender.sendMessage(getMessageConf().getMessage("look_cash")
                    .replace("%player%", player.getName())
                    .replace("%cash%", String.valueOf(point))
            );
        // 플레이어가 아닌 경우
        } else {
            sender.sendMessage(getMessageConf().getMessage("use_console"));
        }
    }

    private void look (CommandSender sender, String playerName) {
        Cash cash = Cash.getCash(playerName);
        // 플레이어를 확인했을 경우
        if (cash != null) {
            int point = cash.getCash();
            sender.sendMessage(getMessageConf().getMessage("look_cash")
                    .replace("%player%", Bukkit.getOfflinePlayer(cash.getUUID()).getName())
                    .replace("%cash%", String.valueOf(point))
            );
        // 플레이어를 확인할 수 없는 경우
        } else {
            sender.sendMessage(getMessageConf().getMessage("exist_player"));
        }
    }

    // 캐시 명령어 확인
    private void help(CommandSender sender) {
        sender.sendMessage(getMessageConf().getMessage("cash_look"));
        sender.sendMessage(getMessageConf().getMessage("cash_pay"));

        // 만약 관리자 명령어 권한이 있을 경우
        if (sender.hasPermission("cashmf.add") || sender.hasPermission("cashmf.sub"))
            sender.sendMessage("");
        if (sender.hasPermission("cashmf.add"))
            sender.sendMessage(getMessageConf().getMessage("cash_add"));
        if (sender.hasPermission("cashmf.sub"))
            sender.sendMessage(getMessageConf().getMessage("cash_sub"));
    }

    // 다른 플레이어한테 캐시 보내기
    private void pay(CommandSender sender, Cash player2, int amount) {
        // 명령어를 입력한 대상이 플레이어가 아닐 경우
        if (!(sender instanceof Player)) {
            sender.sendMessage(getMessageConf().getMessage("use_console"));
            return;
        }

        // 사용자의 데이터 불러오기
        Cash player1 = Cash.getCash(((Player)sender).getUniqueId());

        // 보내는 대상이 본인일 경우
        if (player1.getUUID().equals(player2.getUUID())) {
            sender.sendMessage(getMessageConf().getMessage("pay_self"));
            return;
        }

        // 플레이어의 소지 캐시가 보내려는 수량보다 적을 경우
        if (player1.getCash() < amount) {
            sender.sendMessage(getMessageConf().getMessage("lack_cash"));
            return;
        }

        OfflinePlayer p2 = Bukkit.getOfflinePlayer(player2.getUUID());
        // 플레이어의 캐시를 차감
        player1.addCash(-amount);
        sender.sendMessage(getMessageConf().getMessage("send_cash")
                .replace("%player%", p2.getName())
                .replace("%amount%", String.valueOf(amount))
        );

        // 대상에게 차감된 캐시만큼 전송
        player2.addCash(amount);
        // 플레이어가 온라인일 경우
        if (p2.isOnline()) {
            p2.getPlayer().sendMessage(getMessageConf().getMessage("receive_cash")
                    .replace("%player%", sender.getName())
                    .replace("%amount%", String.valueOf(amount))
            );
        }
    }

    // 플레이어에게 캐시 지급하기
    private void edit(CommandSender sender, Cash player, int amount, int type) {
        String name = Bukkit.getOfflinePlayer(player.getUUID()).getName();
        switch (type) {
            case 0:
                // 대상에게 캐시 지급
                player.addCash(amount);
                sender.sendMessage(getMessageConf().getMessage("add_cash")
                        .replace("%player%", name)
                        .replace("%amount%", String.valueOf(amount))
                );
                break;
            case 1:
                // 대상의 캐시 차감
                player.addCash(-amount);
                sender.sendMessage(getMessageConf().getMessage("sub_cash")
                        .replace("%player%", name)
                        .replace("%amount%", String.valueOf(amount))
                );
        }
    }

    // 일부 명령어 확인 기능
    private void check(CommandSender sender, String[] args, int type) {
        // <플레이어>, <수량>을 제대로 입력하지 않은 경우
        if (args.length < 3) {
            sender.sendMessage(getMessageConf().getMessage("exist_cmd"));

        // 제대로 입력했을 경우
        } else {
            try {
                int amount = Integer.parseInt(args[2]);

                // 수량이 자연수가 아닌경우
                if (amount <= 0) {
                    sender.sendMessage(getMessageConf().getMessage("natural_num"));
                    return;
                }

                Cash target = Cash.getCash(args[1]);
                if (target == null) {
                    throw new NullPointerException();
                }

                switch (type) {
                    case 0:
                        pay(sender, target, amount);
                        break;
                    case 1:
                        edit(sender, target, amount, 0);
                        break;
                    case 2:
                        edit(sender, target, amount, 1);
                }

            // 숫자가 잘못됐을 경우
            } catch (NumberFormatException e) {
                sender.sendMessage(getMessageConf().getMessage("natural_num"));

            // 플레이어의 정보를 불러올 수 없는 경우
            } catch (NullPointerException e) {
                sender.sendMessage(getMessageConf().getMessage("exist_player"));
            }
        }
    }
}
