package me.devleo.eventos.Eventos;

import me.devleo.eventos.Main;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Random;

public class RandomVIP {

    private static boolean acontecendo = false;

    public static void iniciar() {
        if (acontecendo) {
            return;
        }
        acontecendo = true;
        if (Bukkit.getOnlinePlayers().size() > Main.plugin.getConfig().getInt("RandomVIP.Minimo_Online")) {
            ArrayList<Player> online = new ArrayList<>();
            for (Player p : Bukkit.getOnlinePlayers()) {
                online.add(p);
                p.sendMessage(" ");
                p.sendMessage("§b[EVENTO] §6Sorteando VIP...");
                p.sendMessage(" ");
            }
            Bukkit.getScheduler().scheduleSyncDelayedTask(Main.plugin, new Runnable() {
                @Override
                public void run() {
                    Player ganhador = Bukkit.getPlayer(online.get(new Random().nextInt(online.size())).getName());
                    Bukkit.broadcastMessage(" ");
                    Bukkit.broadcastMessage("§b[EVENTO] §6Sorteio finalizado");
                    Bukkit.broadcastMessage("§b[EVENTO] §6Ganhador: §e" + ganhador.getName());
                    Bukkit.broadcastMessage(" ");
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), Main.plugin.getConfig().getString("RandomVIP.Comando").replace("@jogador", ganhador.getName()));
                    online.clear();
                    acontecendo = false;
                }
            }, 10 * 20L);
        } else {
            Main.plugin.getLogger().warning("Numero de jogadores insuficiente para iniciar o evento 'RandomVIP'.");
        }
    }
}
