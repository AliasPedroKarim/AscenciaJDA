package fr.pedrokarim.ascencia.events;

import fr.pedrokarim.ascencia.command.CommandManager;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class EventMessage extends ListenerAdapter {

    private CommandManager commandManager;

    public EventMessage(CommandManager commandManager) {
        this.commandManager = commandManager;
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        if (event.getAuthor().equals(event.getJDA().getSelfUser())) return;
        if (event.getAuthor().isBot()) return;
        /*if (event.getGuild().getSelfMember().hasPermission(Permission.MESSAGE_WRITE))
            // On peut Utiliser complete ou queue (pour soit envoyer les messages d'un coup "Risqué en cas de beaucoup de message" ou envoyer les messages 1 par 1)
            event.getTextChannel().sendMessage("Hello " + event.getAuthor().getAsMention()).complete();*/


        String message = event.getMessage().getContentDisplay();
        if (message.startsWith(this.commandManager.getPrefix())) {
            message = message.replaceFirst(this.commandManager.getPrefix(), "");
            if (this.commandManager.commandUser(event.getAuthor(), message, event.getMessage())) {
                if (event.getTextChannel() != null && event.getGuild().getSelfMember().hasPermission(Permission.MESSAGE_MANAGE)) {
                    event.getMessage().delete().queue();
                }
            }
        }
    }
}
