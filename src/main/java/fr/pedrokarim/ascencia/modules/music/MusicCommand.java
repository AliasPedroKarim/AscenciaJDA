package fr.pedrokarim.ascencia.modules.music;

import fr.pedrokarim.ascencia.command.AnnoCommand;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.VoiceChannel;

public class MusicCommand {

    private final MusicManager manager = new MusicManager();

    /**
     * YouTube Search: ytsearch:
     * Sound Cloud Search: scsearch:
     * @param guild
     * @param channel
     * @param user
     * @param args
     */
    @AnnoCommand(name = "play", type = AnnoCommand.ExecutorType.USER, aliases = {"p"}, category = "music", permissions = {})
    private void play(Guild guild, TextChannel channel, User user, String[] args){

        if (guild == null) return;

        if (!guild.getAudioManager().isConnected() && !guild.getAudioManager().isAttemptingToConnect()){
            VoiceChannel voiceChannel = guild.getMember(user).getVoiceState().getChannel();

            if (voiceChannel == null){
                channel.sendMessage("You must be logged into a voice chat room to be able to play music.").queue();
                return;
            }
            guild.getAudioManager().openAudioConnection(voiceChannel);
        }

        String resolvable = String.join(" ", args);

        if (!resolvable.startsWith("http")) {
            resolvable = "ytsearch:" + resolvable;
        }

        manager.loadTrack(channel, resolvable);
    }

    @AnnoCommand(name = "skip", type = AnnoCommand.ExecutorType.USER, aliases = {"s"}, category = "music", permissions = {})
    private void skip(Guild guild, TextChannel channel){
        if (!guild.getAudioManager().isConnected() && !guild.getAudioManager().isAttemptingToConnect()){
            channel.sendMessage("I'm not playing music right now.").queue();
            return;
        }

        manager.getPlayer(guild).skipTrack();
        channel.sendMessage("Next music...").queue();
    }

    @AnnoCommand(name = "clear", type = AnnoCommand.ExecutorType.USER, aliases = {"cl"}, category = "music", permissions = {})
    private void clear(TextChannel channel){
        MusicPlayer player = manager.getPlayer(channel.getGuild());

        if (player.getListener().getTracks().isEmpty()){
            channel.sendMessage("There is no music in the queue.").queue();
            return;
        }

        player.getListener().getTracks().clear();
        channel.sendMessage("The queue has been cleared.").queue();
    }
}
