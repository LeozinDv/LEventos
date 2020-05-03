package me.devleo.eventos.Eventos;

import br.com.devpaulo.legendchat.api.events.ChatMessageEvent;
import me.devleo.eventos.Main;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.Random;

public class SpeedChat implements Listener {

    private static boolean acontecendo = false;
    private static String chars = "";

    @EventHandler
    private static void falar(AsyncPlayerChatEvent e) {
        Player p = e.getPlayer();
        if (acontecendo) {
            if (e.getMessage().equals(chars)) {
                for (String msg : Main.plugin.getConfig().getStringList("Anuncios.SpeedChat_Ganhador")) {
                    Bukkit.broadcastMessage(msg.replace("&", "§").replace("@vencedor", p.getName()).replace("@tag", Main.plugin.getConfig().getString("SpeedChat.Premios.TAG").replace("&", "§")));
                }
                Main.economy.depositPlayer(p.getName(), Main.plugin.getConfig().getInt("SpeedChat.Premios.Dinheiro"));
                Main.plugin.getConfig().set("Vencedores.SpeedChat", p.getName());
                Main.plugin.saveConfig();
                acontecendo = false;
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    private static void tag(ChatMessageEvent e) {
        Player p = e.getSender();
        if (Main.plugin.getConfig().getString("Vencedores.SpeedChat").equals(p.getName())) {
            e.setTagValue("speedchat", Main.plugin.getConfig().getString("SpeedChat.Premios.TAG").replace("&", "§"));
        }
    }

    public static void iniciar() {
        if (acontecendo) {
            return;
        }
        StringBuilder sb = new StringBuilder();
        char[] letras = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789".toCharArray();
        for (int i = 0; i != Main.plugin.getConfig().getInt("SpeedChat.Quantia_Caracteres"); i++) {
            int ch = new Random().nextInt(letras.length);
            sb.append(letras[ch]);
        }
        acontecendo = true;
        chars = sb.toString();
        for (String msg : Main.plugin.getConfig().getStringList("Anuncios.SpeedChat_Aberto")) {
            Bukkit.broadcastMessage(msg.replace("&", "§").replace("@key", chars));
        }
    }
}