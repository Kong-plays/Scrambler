package com.kong.Scrambler.Listeners;

import com.kong.Scrambler.Commands.CMD_Scrambler;
import com.kong.Scrambler.Scrambler;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerListener implements Listener {
    private Scrambler instance;

    private Scrambler scrambler;

    public PlayerListener(Scrambler instance){
        this.instance = instance;
        Bukkit.getPluginManager().registerEvents(this, this.instance);
    }

    @EventHandler
    public void onChat(PlayerChatEvent event){
        if (instance.active){
            if(event.getMessage().equals(instance.Scrambled)){



                String rewardCmd = instance.getConfig().getString("rewards.Reward Command");
                String prefix = instance.getConfig().getString("Plugin Prefix").replace("&","§");
                rewardCmd = rewardCmd.replace("{player}", event.getPlayer().getDisplayName());
                if (rewardCmd.contains("%SERVER%")){
                    rewardCmd = rewardCmd.replace("%SERVER% ", "");
                    instance.getServer().broadcastMessage(String.format("%s §c%s has won, taking %s seconds!", prefix, event.getPlayer().getDisplayName(), instance.StartingTime));
                    instance.getServer().dispatchCommand(instance.getServer().getConsoleSender(), rewardCmd);
                    instance.active = false;
                }else if (rewardCmd.contains("%PLAYER%")){
                    rewardCmd = rewardCmd.replace("%PLAYER% ", "");
                    instance.getServer().dispatchCommand(event.getPlayer(), rewardCmd);
                    instance.getServer().broadcastMessage(String.format("%s §c%s has won, taking %s seconds!", prefix, event.getPlayer().getDisplayName(), instance.StartingTime));
                    instance.active = false;
                }else{
                    instance.log.warning("Please specify the reward command sender (%SERVER% or %Player%)");
                }

                Bukkit.getScheduler().cancelTask(CMD_Scrambler.timerTask);

            }


        }
    }
    @EventHandler
    void onLeave(PlayerQuitEvent event){
        instance.playersOnline = false;
    }

    @EventHandler
    void onJoin(PlayerJoinEvent event){

        instance.playersOnline = true;

    }

    public void rewardHandle(Player event){

    }
}
