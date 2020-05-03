package me.devleo.eventos;

import me.devleo.eventos.Eventos.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class Comandos implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String s, String[] args) {
        if (cmd.getName().equalsIgnoreCase("myevent")) {
            if (!sender.hasPermission("levent.admin")) {
                sender.sendMessage("§cVocê não possui permissão para isto.");
                return true;
            }
            if (args.length == 0) {
                sender.sendMessage(" ");
                sender.sendMessage("§f§lCOMANDOS EVENTOS:");
                sender.sendMessage("  §7/myevent bolao §8- §eInicia o evento bolão");
                sender.sendMessage("  §7/myevent loteria §8- §eInicia o evento loteria");
                sender.sendMessage("  §7/myevent randomvip §8- §eInicia o evento RandomVIP");
                sender.sendMessage("  §7/myevent speedchat §8- §eInicia o evento SpeedChat");
                sender.sendMessage("  §7/myevent matematica §8- §eInicia o evento matemática");
                sender.sendMessage(" ");
                return true;
            }
            if (args[0].equalsIgnoreCase("bolao")) {
                Bolao.iniciar();
            }
            if (args[0].equalsIgnoreCase("loteria")) {
                Loteria.iniciar();
                sender.sendMessage("§aEvento iniciado com sucesso, número correto: §f" + Loteria.correto);
            }
            if (args[0].equalsIgnoreCase("randomvip")) {
                RandomVIP.iniciar();
            }
            if (args[0].equalsIgnoreCase("speedchat")) {
                SpeedChat.iniciar();
            }
            if (args[0].equalsIgnoreCase("matematica")) {
                if (args.length != 2 || !args[1].contains("=")) {
                    sender.sendMessage("§cUtilize /myevent matematica [operação].");
                    sender.sendMessage("§cA operação deve ser [numero1 numero2=resultado]");
                    sender.sendMessage("§cExemplo: /myevent matematica 1+1=2");
                    return true;
                }
                Matematica.iniciar(args[1]);
            }
        }
        return false;
    }
}
