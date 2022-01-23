package net.teamuni.cashmf.api.skript.elements.expressions;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import net.teamuni.cashmf.Cash;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;

import javax.annotation.Nullable;

public class ExprCash extends SimpleExpression {
    static {
        Skript.registerExpression(ExprCash.class, Integer.class, ExpressionType.SIMPLE, "%player%'s cashmf");
    }
    private Expression<Player> player;

    @Override
    protected Integer[] get(Event e) {
        Player p = player.getSingle(e);
        if (p != null) {
            Cash cash = Cash.getCash(p.getUniqueId());
            if (cash != null)
                return new Integer[] {cash.getCash()};
        }
        return null;
    }

    @Override
    public boolean isSingle() {
        return true;
    }

    @Override
    public Class<? extends Integer> getReturnType() {
        return Integer.class;
    }

    @Override
    public String toString(@Nullable Event e, boolean debug) {
        return "플레이어 표현식 예제: " + player.toString(e, debug);
    }

    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        player = (Expression<Player>) exprs[0];
        return true;
    }
}
