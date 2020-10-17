package fr.pedrokarim.ascencia.command.lots;

import fr.pedrokarim.ascencia.Ascencia;
import fr.pedrokarim.ascencia.command.AnnoCommand;
import fr.pedrokarim.ascencia.config.Config;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;

public class CommandDefault {

    private final Ascencia ascencia;

    public CommandDefault(Ascencia ascencia) {
        this.ascencia = ascencia;
    }

    // @Command(name = "stop", type = AnnoCommand.ExecutorType.CONSOLE)
    // private void stop(){
    //     ascencia.setRunning(false);
    // }

    @AnnoCommand(name = "info", type = AnnoCommand.ExecutorType.USER, aliases = {}, category = "default", permissions = {})
    private void info(User user, MessageChannel channel) {
        channel.sendMessage(user.getAsMention() + " est dans le channel " + channel.getName()).complete();
    }

    @AnnoCommand(name = "avatar", type = AnnoCommand.ExecutorType.USER, aliases = {}, category = "default", permissions = {})
    private void sendEmbed(User user, MessageChannel channel) {
        if (channel instanceof TextChannel) {
            TextChannel textChannel = (TextChannel) channel;
            if (!textChannel.getGuild().getSelfMember().hasPermission(Permission.MESSAGE_EMBED_LINKS)) return;
        }

        channel.sendMessage(
                new EmbedBuilder()
                .setAuthor(user.getName(), null, user.getAvatarUrl() + "?size=256")
                .setImage(user.getAvatarUrl())
                .setTitle(user.getName() + "'s avatar")
                .setColor(Config.EMBED_COLOR
                ).build()).queue();
    }

    @AnnoCommand(name = "game", type = AnnoCommand.ExecutorType.ALL, aliases = {}, category = "", permissions = {})
    private void game(JDA api, String[] args) {
        StringBuilder builder = new StringBuilder();
        for (String str : args) {
            if (builder.length() > 0) builder.append(" ");
            builder.append(str);
        }

        api.getPresence().setActivity(Activity.of(Activity.ActivityType.DEFAULT, builder.toString(), null));
    }

}
