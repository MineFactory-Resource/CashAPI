package net.teamuni.cashmf.api.skript.elements.expressions;

import ch.njol.skript.expressions.base.SimplePropertyExpression;
import net.teamuni.cashmf.Cash;
import org.bukkit.entity.Player;

import javax.annotation.Nullable;

public class ExprCash extends SimplePropertyExpression<Player, Integer> {
    static {
        register(ExprCash.class, Integer.class, "cashmf", "player");
    }

    @Override
    protected String getPropertyName() {
        return "cashmf";
    }

    @Override
    @Nullable
    public Integer convert(Player player) {
        Cash cash = Cash.getCash(player.getUniqueId());
        if (cash != null)
            return cash.getCash();
        return null;
    }

    @Override
    public Class<? extends Integer> getReturnType() {
        return Integer.class;
    }
}
