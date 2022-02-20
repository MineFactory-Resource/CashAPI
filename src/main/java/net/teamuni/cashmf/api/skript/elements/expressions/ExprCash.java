package net.teamuni.cashmf.api.skript.elements.expressions;

import ch.njol.skript.classes.Changer;
import ch.njol.skript.expressions.base.SimplePropertyExpression;
import ch.njol.util.coll.CollectionUtils;
import net.teamuni.cashmf.Cash;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;

import javax.annotation.Nullable;

public class ExprCash extends SimplePropertyExpression<Player, Number> {
    static {
        register(ExprCash.class, Number.class, "cashapi", "player");
    }

    @Override
    protected String getPropertyName() {
        return "cashapi";
    }

    @Override
    @Nullable
    public Number convert(Player player) {
        Cash cash = Cash.getCash(player.getUniqueId());
        if (cash != null)
            return cash.getCash();
        return null;
    }

    @Override
    public Class<? extends Number> getReturnType() {
        return Number.class;
    }

    @Override
    public void change(Event event, Object[] delta, Changer.ChangeMode mode) {
        Player player = getExpr().getSingle(event);
        if (player == null) return;
        Cash cash = Cash.getCash(player.getUniqueId());
        if (cash == null) return;
        int mod = 1;
        int amount;
        switch (mode) {
            case REMOVE:
                mod = -1;
            case ADD:
                amount = ((Number)delta[0]).intValue();
                cash.addCash(mod * amount);
                break;
            case SET:
                amount = ((Number)delta[0]).intValue();
                cash.addCash(amount - cash.getCash());
                break;
            case RESET:
                cash.addCash(-cash.getCash());
                break;
            default:
                assert false;
        }
    }

    @Override
    public Class<?>[] acceptChange(final Changer.ChangeMode mode) {
        switch (mode) {
            case ADD:
            case REMOVE:
            case SET:
                return CollectionUtils.array(Number.class);
            default:
                return null;
        }
    }
}
