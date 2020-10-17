package fr.pedrokarim.ascencia;

import fr.pedrokarim.ascencia.command.CommandManager;
import fr.pedrokarim.ascencia.config.Config;
import fr.pedrokarim.ascencia.events.EventMessage;
import fr.pedrokarim.ascencia.events.EventReady;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;

import javax.security.auth.login.LoginException;

public class Ascencia implements Runnable {

    private JDA api;
    private final CommandManager commandManager = new CommandManager(this);

    public JDA getAPI() {
        return this.api;
    }

    public CommandManager getCommandManager() {
        return this.commandManager;
    }

    @Override
    public void run() {
        try {
            this.api = JDABuilder.createDefault(Config.TOKEN).build();
        } catch (LoginException e) {
            e.printStackTrace();
        }

        // Register Events
        this.api.addEventListener(new EventReady());
        this.api.addEventListener(new EventMessage(this.commandManager));
    }

    public static void main(String[] args) {
        try {
            Ascencia ascencia = new Ascencia();
            new Thread(ascencia, "ascencia").start();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
    }

}
