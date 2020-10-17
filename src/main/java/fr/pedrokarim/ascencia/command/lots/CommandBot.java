package fr.pedrokarim.ascencia.command.lots;

import fr.pedrokarim.ascencia.Ascencia;
import fr.pedrokarim.ascencia.command.AnnoCommand;
import fr.pedrokarim.ascencia.config.Config;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.requests.restaction.MessageAction;

import java.util.Date;

public class CommandBot {

    private final Ascencia ascencia;

    public CommandBot(Ascencia ascencia) {
        this.ascencia = ascencia;
    }

    private String getColorByPing(long ping) {
        if (ping < 100)
            return "<:online_a:766898247129628683>";
        if (ping < 400)
            return "<:idle_a:766898247011663913>";
        if (ping < 700)
            return "<:orange_a:766901000077312030>";
        return "<:dnd_a:766898246853197845>";
    }

    @AnnoCommand(name = "ping", type = AnnoCommand.ExecutorType.USER, aliases = {}, category = "default", permissions = {})
    private void ping(JDA api, User user, MessageChannel channel) {
        long t0 = new Date().getTime();

        MessageAction ma = channel.sendMessage("Ping!");

        Message pingMessage = ma.complete();

        long t1 = new Date().getTime() - t0;

        long gatewayPing = api.getGatewayPing();
        long restPing = api.getRestPing().complete();

        pingMessage.editMessage(new EmbedBuilder()
                .setAuthor("Ping process")
                .setColor(Config.EMBED_COLOR)
                .addField(this.getColorByPing(t1) + " Message", "**" + String.valueOf(t1) + "**ms", false)
                .addField(this.getColorByPing(gatewayPing) + " Gateway", "**" + String.valueOf(gatewayPing) + "**ms", false)
                .addField(this.getColorByPing(restPing) + " Rest", "**" + String.valueOf(restPing) + "**ms", false)
                .build()
        )
                .content(null)
                .override(true)
                .queue();
    }

}
