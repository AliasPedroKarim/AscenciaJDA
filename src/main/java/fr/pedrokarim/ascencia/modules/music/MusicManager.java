package fr.pedrokarim.ascencia.modules.music;

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.TextChannel;

import java.util.HashMap;
import java.util.Map;

public class MusicManager {
    private final AudioPlayerManager manager = new DefaultAudioPlayerManager();
    private final Map<String, MusicPlayer> players = new HashMap<>();

    public MusicManager(){
        AudioSourceManagers.registerRemoteSources(manager);
        AudioSourceManagers.registerLocalSource(manager);
    }

    public synchronized MusicPlayer getPlayer(Guild guild){
        if (!players.containsKey(guild.getId())) players.put(guild.getId(), new MusicPlayer(manager.createPlayer(), guild));
        return players.get(guild.getId());
    }

    public void loadTrack(final TextChannel channel, final String source){
        final MusicPlayer player = getPlayer(channel.getGuild());

        channel.getGuild().getAudioManager().setSendingHandler(player.getAudioHandler());

        manager.loadItemOrdered(player, source, new AudioLoadResultHandler() {
            @Override
            public void trackLoaded(AudioTrack audioTrack) {
                channel.sendMessage("Adding the track " + audioTrack.getInfo().title + ".").queue();
                player.playTrack(audioTrack);
            }

            @Override
            public void playlistLoaded(AudioPlaylist audioPlaylist) {
                StringBuilder builder = new StringBuilder();

                builder.append("Adding the playlist **").append(audioPlaylist.getName()).append("**\n");

                for (int i = 0; i < audioPlaylist.getTracks().size() && i < 5; i++){
                    AudioTrack audioTrack = audioPlaylist.getTracks().get(i);
                    builder.append("\n **>** ").append(audioTrack.getInfo().title);
                    player.playTrack(audioTrack);
                }

                channel.sendMessage(builder.toString()).queue();

            }

            @Override
            public void noMatches() {
                channel.sendMessage("The music **" + source + "** was not found.").queue();
            }

            @Override
            public void loadFailed(FriendlyException e) {
                channel.sendMessage("Impossible to play the music... " + e.getMessage()).queue();
            }
        });
    }
}
