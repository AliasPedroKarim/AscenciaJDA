package fr.pedrokarim.ascencia.events;

import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public class EventReady extends ListenerAdapter {
    @Override
    public void onReady(@NotNull ReadyEvent event) {

        System.out.println("Ready !!! I'm exectuted ^^...");

    }
}
