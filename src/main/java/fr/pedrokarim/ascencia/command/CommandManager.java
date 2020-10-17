package fr.pedrokarim.ascencia.command;


import fr.pedrokarim.ascencia.Ascencia;
import fr.pedrokarim.ascencia.command.lots.CommandBot;
import fr.pedrokarim.ascencia.command.lots.CommandDefault;
import fr.pedrokarim.ascencia.command.lots.HelpCommand;
import fr.pedrokarim.ascencia.config.Config;
import fr.pedrokarim.ascencia.modules.music.MusicCommand;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.*;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CommandManager {
    private final Ascencia ascencia;

    private final Map<String, Command> commands = new HashMap<>();
    private final Map<String, String> aliases = new HashMap<>();

    public CommandManager(Ascencia ascencia) {
        this.ascencia = ascencia;

        registerCommands(new CommandDefault(ascencia), new CommandBot(ascencia), new MusicCommand(), new HelpCommand(this));
    }

    public String getPrefix() {
        return Config.PREFIX;
    }

    public Collection<Command> getCommands() {
        return commands.values();
    }

    public void registerCommands(Object... objects) {
        for (Object object : objects) registerCommand(object);
    }

    public void registerCommand(Object object) {
        for (Method method : object.getClass().getDeclaredMethods()) {
            if (method.isAnnotationPresent(AnnoCommand.class)) {
                AnnoCommand command = method.getAnnotation(AnnoCommand.class);
                method.setAccessible(true);

                // Pyload config command
                Map<String, Object> config = new HashMap<>();
                config.put("name", command.name());
                config.put("description", command.description());
                config.put("type", command.type());
                config.put("category", command.category());
                config.put("aliases", command.aliases());
                config.put("permissions", command.permissions());
                config.put("test", command.test());
                config.put("disabled", command.disabled());
                config.put("nfsw", command.nfsw());

                Command commandGeneric = new Command(config, object, method);

                // Aliases
                for (String el : (String[]) command.aliases()) {
                    if (this.aliases.get(el) == null) {
                        this.aliases.put(el, command.name());
                    } else {
                        System.out.printf(
                            "A collision has just been detected between: new -> (alias: %s, command: %s) and old -> (alias: %s, command: %s)%n",
                            el, command.name(), el, this.aliases.get(el)
                        );
                    }
                }

                // Command
                commands.put(command.name(), commandGeneric);
            }
        }
    }

    private Object[] getCommand(String content) {
        String[] commandSplit = content.split(" ");
        String[] args = new String[commandSplit.length - 1];

        if (commandSplit.length > 1) {
            args = Arrays.copyOfRange(commandSplit, 1, commandSplit.length);
        }

        // If commandSplit[0] is alias
        String nameCommand = this.aliases.get(commandSplit[0]);

        Command commandGeneric = commands.get(nameCommand != null ? nameCommand : commandSplit[0]);
        return new Object[]{commandGeneric, args};
    }

    private void execute(Command commandGeneric, String command, String[] args, Message message) throws Exception {
        Parameter[] parameters = commandGeneric.getMethod().getParameters();
        Object[] objects = new Object[parameters.length];
        for (int i = 0; i < parameters.length; i++) {
            if (parameters[i].getType() == String[].class)
                objects[i] = args;
            else if (parameters[i].getType() == User.class)
                objects[i] = message == null ? null : message.getAuthor();
            else if (parameters[i].getType() == TextChannel.class)
                objects[i] = message == null ? null : message.getTextChannel();
            else if (parameters[i].getType() == PrivateChannel.class)
                objects[i] = message == null ? null : message.getPrivateChannel();
            else if (parameters[i].getType() == Guild.class)
                objects[i] = message == null ? null : message.getGuild();
            else if (parameters[i].getType() == String.class)
                objects[i] = command;
            else if (parameters[i].getType() == Message.class)
                objects[i] = message;
            else if (parameters[i].getType() == JDA.class)
                objects[i] = ascencia.getAPI();
            else if (parameters[i].getType() == MessageChannel.class)
                objects[i] = message == null ? null : message.getChannel();
        }
        commandGeneric.getMethod().invoke(commandGeneric.getObject(), objects);
    }

    public void commandConsole(String command) {
        Object[] object = getCommand(command);
        if (object[0] == null || ((Command) object[0]).getExecutorType() == AnnoCommand.ExecutorType.USER) {
            System.out.println("Commande inconnue !");
            return;
        }

        try {
            execute(((Command) object[0]), command, (String[]) object[1], null);
        } catch (Exception e) {
            System.out.println("La methode (" + ((Command) object[0]).getMethod().getName() + ") n'est pas correctement innitialisé. " + e.getMessage());
            Logger.getLogger(HelpCommand.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    public boolean commandUser(User user, String command, Message message) {
        Object[] object = getCommand(command);
        if (object[0] == null || ((Command) object[0]).getExecutorType() == AnnoCommand.ExecutorType.CONSOLE)
            return false;

        try {
            execute(((Command) object[0]), command, (String[]) object[1], message);
        } catch (Exception e) {
            System.out.println("La methode (" + ((Command) object[0]).getMethod().getName() + ") n'est pas correctement initialisé. ");
            Logger.getLogger(HelpCommand.class.getName()).log(Level.SEVERE, null, e);
        }
        return true;
    }

}
