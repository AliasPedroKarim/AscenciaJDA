package fr.pedrokarim.ascencia.command.lots;

import fr.pedrokarim.ascencia.command.AnnoCommand;
import fr.pedrokarim.ascencia.command.Command;
import fr.pedrokarim.ascencia.command.CommandManager;
import fr.pedrokarim.ascencia.config.Config;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;

public class CommandHelp {

    private final CommandManager commandManager;

    public CommandHelp(CommandManager commandManager){
        this.commandManager = commandManager;
    }

    @AnnoCommand(name = "help", type = AnnoCommand.ExecutorType.USER, description = "Permet d'avoir des informations sur les commandes disponibles pour le bot.", aliases = {"h"}, category = "utils", permissions = {})
    private void help(User user, MessageChannel channel){
        EmbedBuilder builder = new EmbedBuilder();
        builder
                .setTitle("List of commands")
                .setColor(Config.EMBED_COLOR);

        for (Command command : commandManager.getCommands()){
            if (command.getExecutorType() == AnnoCommand.ExecutorType.CONSOLE) continue;

            builder.addField(commandManager.getPrefix() + command.getName(), command.getDescription(), false);
        }

        // if (!user.hasPrivateChannel()) user.openPrivateChannel().complete();
        // ((UserImpl)user).getPrivateChannel().sendMessage(builder.build()).queue();

        channel.sendMessage(builder.build()).queue();
        // channel.sendMessage(user.getAsMention() + ", please check your private messages.").queue();
    }
}
