package me.devleo.eventos.Eventos;

import br.com.devpaulo.legendchat.api.events.ChatMessageEvent;
import me.devleo.eventos.Main;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class Matematica implements CommandExecutor, Listener {

    private static boolean acontecendo = false;
    private static String operacao = "";

    @EventHandler
    private static void tag(ChatMessageEvent e) {
        Player p = e.getSender();
        if (Main.plugin.getConfig().getString("Vencedores.Matematica").equals(p.getName())) {
            e.setTagValue("matematica", Main.plugin.getConfig().getString("Matematica.Premios.TAG").replace("&", "§"));
        }
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String s, String[] args) {
        if (!(sender instanceof Player)) {
            return true;
        }
        Player p = (Player) sender;
        if (cmd.getName().equalsIgnoreCase("matematica")) {
            if (!acontecendo) {
                p.sendMessage(Main.plugin.getConfig().getString("Mensagens.Nao_Acontecendo").replace("&", "§"));
                return true;
            }
            if (args.length != 1) {
                p.sendMessage(Main.plugin.getConfig().getString("Mensagens.Matematica_Erro_Args").replace("&", "§"));
                return true;
            }
            try {
                int resultado = Integer.parseInt(args[0]);
                if (!operacao.split("=")[1].equals("" + resultado)) {
                    p.sendMessage(Main.plugin.getConfig().getString("Mensagens.Matematica_Erro_Valor").replace("&", "§"));
                    return true;
                }
                for (String msg : Main.plugin.getConfig().getStringList("Anuncios.Matematica_Ganhador")) {
                    Bukkit.broadcastMessage(msg.replace("&", "§").replace("@vencedor", p.getName()).replace("@tag", Main.plugin.getConfig().getString("Matematica.Premios.TAG").replace("&", "§")).replace("@resultado", operacao.split("=")[1]));
                }
                Main.economy.depositPlayer(p.getName(), Main.plugin.getConfig().getInt("Matematica.Premios.Dinheiro"));
                Main.plugin.getConfig().set("Vencedores.Matematica", p.getName());
                Main.plugin.saveConfig();
                acontecendo = false;
            } catch (NumberFormatException e) {
                p.sendMessage(Main.plugin.getConfig().getString("Mensagens.Matematica_Erro_Args").replace("&", "§"));
                return true;
            }
        }
        return false;
    }

    public static void iniciar(String conta) {
        if (acontecendo) {
            return;
        }
        acontecendo = true;
        operacao = conta;
        for (String msg : Main.plugin.getConfig().getStringList("Anuncios.Matematica_Aberto")) {
            Bukkit.broadcastMessage(msg.replace("&", "§").replace("@operacao", operacao.split("=")[0]));
        }
    }
}