package com.viperhcf.minssentials.command.annotation;import lombok.SneakyThrows;import org.bukkit.ChatColor;import org.bukkit.command.CommandSender;import org.bukkit.entity.Player;import com.viperhcf.minssentials.command.CommandExecutable;import com.viperhcf.minssentials.command.condition.Condition;import java.lang.reflect.InvocationTargetException;import java.lang.reflect.Method;import java.util.ArrayList;import java.util.List;public class SimpleCommandWraper implements CommandExecutable {    private Object object;    private Method method;    private boolean op;    private List<Condition> conditions;    public SimpleCommandWraper(final Object object, final Method method, final boolean op) {        this.conditions = new ArrayList<>();        this.object = object;        (this.method = method).setAccessible(true);        this.op = op;    }    public void addCondition(final Condition condition) {        this.conditions.add(condition);    }    @Override	@SneakyThrows    public void execute(final CommandSender player, final String[] args) {        if (this.op && !player.isOp()) {            player.sendMessage(ChatColor.RED + "nope!");            return;        }        if (player instanceof Player) {            for (final Condition condition : this.conditions) {                if (!condition.canProcess((Player) player, args)) {                    return;                }            }        }        try{            this.method.invoke(this.object, player, args);        } catch (IllegalAccessException e) {            e.printStackTrace();        } catch (InvocationTargetException e) {            e.printStackTrace();        }    }}