package me.devleo.eventos.Eventos;

import me.devleo.eventos.Main;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.Random;

public class Loteria implements CommandExecutor {

    public boolean onCommand(CommandSender sender, Command cmd, String s, String[] args) {
        if (!(sender instanceof Player)) {
            return true;
        }
        Player p = (Player) sender;
        if (cmd.getName().equalsIgnoreCase("loteria")) {
            if (!acontecendo) {
                p.sendMessage(Main.plugin.getConfig().getString("Mensagens.Nao_Acontecendo").replace("&", "§"));
                return true;
            }
            if (args.length != 1) {
                p.sendMessage(Main.plugin.getConfig().getString("Mensagens.Loteria_Erro_Args").replace("&", "§"));
                return true;
            }
            try {
                int numero = Integer.valueOf(args[0]);
                if (numero != correto) {
                    p.sendMessage(Main.plugin.getConfig().getString("Mensagens.Loteria_Erro_Invalido").replace("&", "§"));
                    return true;
                }
                Main.economy.depositPlayer(p.getName(), Main.plugin.getConfig().getInt("Loteria.Premio"));
                acontecendo = false;
                for (String msg : Main.plugin.getConfig().getStringList("Anuncios.Loteria_Ganhador")) {
                    Bukkit.broadcastMessage(msg.replace("&", "§").replace("@vencedor", p.getName()).replace("@ncorreto", "" + correto));
                }
            } catch (NumberFormatException e) {
                p.sendMessage(Main.plugin.getConfig().getString("Mensagens.Loteria_Erro_Args").replace("&", "§"));
                return true;
            }
        }
        return false;
    }

    private static boolean acontecendo = false;
    public static int correto = 0;

    public static void iniciar() {
        if (acontecendo) {
            return;
        }
        acontecendo = true;
        int nmaximo = Main.plugin.getConfig().getInt("Loteria.Numero_Maximo");
        correto = new Random().nextInt(nmaximo);
        BukkitTask task = new BukkitRunnable() {
            int anuncios = Main.plugin.getConfig().getInt("Loteria.Anuncios");

            @Override
            public void run() {
                if (!acontecendo) {
                    cancel();
                    return;
                }
                if (anuncios > 0) {
                    for (String msg : Main.plugin.getConfig().getStringList("Anuncios.Loteria_Aberta")) {
                        Bukkit.broadcastMessage(msg.replace("&", "§").replace("@nmaximo", "" + nmaximo));
                    }
                    anuncios--;
                }
                if (anuncios == 0) {
                    acontecendo = false;
                    for (String msg : Main.plugin.getConfig().getStringList("Anuncios.Loteria_Encerrado")) {
                        Bukkit.broadcastMessage(msg.replace("&", "§").replace("@ncorreto", "" + correto));
                    }
                    cancel();
                }
            }
        }.runTaskTimer(Main.plugin, 0L, Main.plugin.getConfig().getInt("Loteria.Intervalo") * 20);
    }
}
