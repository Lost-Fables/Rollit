package net.lostfables.lughgk.rollit.utilitycommands;

import co.lotc.core.command.CommandTemplate;
import co.lotc.core.command.annotate.Cmd;
import net.lostfables.lughgk.rollit.Rollit;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.scoreboard.*;

import java.util.HashMap;


public class TurnOrderCommand extends CommandTemplate implements Listener {

    public final static String BASE_TURNORDER_PERMISSION = "turnorder";
    private Rollit plugin;


    public TurnOrderCommand(Rollit plugin) {
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    public void createBoard(Player player) {
        ScoreboardManager manager = Bukkit.getScoreboardManager();
        Scoreboard board = manager.getNewScoreboard();
        Objective obj = board.registerNewObjective("turnorder" + plugin.getTurnOrders().size(),"dummy","Turn Order");
        obj.setDisplaySlot(DisplaySlot.SIDEBAR);
        Score score = obj.getScore("=-=-=-=-=-=-=-=-=");
        score.setScore(3);
        player.setScoreboard(board);
    }

    @Cmd(value = "Starts a turn order.", permission = Rollit.BASE_PERMISSION + "." + BASE_TURNORDER_PERMISSION + ".start")
    public void start(CommandSender sender) {
        if(sender instanceof Player) {
            Player player = (Player) sender;
            if(!plugin.getTurnOrders().containsKey(player.getUniqueId())) {
                plugin.getTurnOrders().put(player.getUniqueId(), new HashMap<>());
                createBoard(player);

            }
        }
    }

}
