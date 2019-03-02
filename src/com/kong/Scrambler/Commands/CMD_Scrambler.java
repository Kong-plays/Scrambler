package com.kong.Scrambler.Commands;

import com.kong.Scrambler.Scrambler;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.util.Random;

public class CMD_Scrambler implements CommandExecutor {

    public static int timerTask;
    private Scrambler instance;

    public CMD_Scrambler(Scrambler instance){
        this.instance = instance;

        this.instance.getCommand("scrambler").setExecutor(this);
    }

    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        String prefix = instance.getConfig().getString("Plugin Prefix").replace("&","§");
        if(args.length <= 0){
            commandSender.sendMessage(String.format("%s §4§l§nHelp", prefix));
            commandSender.sendMessage(String.format("%s §c/scrambler start - Start automatic sending of challenges", prefix));
            commandSender.sendMessage(String.format("%s §c/scrambler pause - Pause automatic sending of challenges", prefix));
            commandSender.sendMessage(String.format("%s §c/scrambler forcestart - Force a challenge to start", prefix));
            commandSender.sendMessage(String.format("%s §c/scrambler forcestop - Force a challenge to stop", prefix));

            return true;
        }
        else if(args[0].equalsIgnoreCase("pause")){
            if(commandSender.hasPermission("scrambler.pause")) {
                if (instance.started) {
                    instance.getServer().getScheduler().cancelTask(instance.initTimer);
                    commandSender.sendMessage(String.format("%s §cAutomatic Challenges Turned Off", prefix));
                } else {
                    commandSender.sendMessage(String.format("%s §cAutomatic Challenges Already Turned Off", prefix));
                }
                instance.started = false;
                return true;
            }else{
                commandSender.sendMessage(String.format("%s §cYou dont have permission to execute this command", prefix));
                return true;
            }
        }else if(args[0].equalsIgnoreCase("start")){
            if(commandSender.hasPermission("scrambler.start")) {
                if (!instance.started) {
                    instance.startTimer();
                    commandSender.sendMessage(String.format("%s §cAutomatic Challenges Turned On", prefix));
                } else {
                    commandSender.sendMessage(String.format("%s §cAutomatic Challenges Already Turned On", prefix));
                }
                return true;
            }else{
                commandSender.sendMessage(String.format("%s §cYou dont have permission to execute this command", prefix));
                return true;
            }
        }else if(args[0].equalsIgnoreCase("forcestop")){
            if(commandSender.hasPermission("scrambler.forcestop")) {
                if (!instance.active) {
                    commandSender.sendMessage(String.format("%s §cNo game currently running!", prefix));
                } else {
                    commandSender.sendMessage(String.format("%s §cStopped the current game", prefix));
                    forceEnd();
                }
                return true;
            }else{
                commandSender.sendMessage(String.format("%s §cYou dont have permission to execute this command", prefix));
                return true;
            }
        }else if (args[0].equalsIgnoreCase("forcestart")){
            if(commandSender.hasPermission("scrambler.forcestop")) {
                if (!instance.active) {
                    scrable();
                    instance.getServer().getScheduler().cancelTask(instance.initTimer);
                    instance.startTimer();
                } else {
                    forceEnd();
                    scrable();
                }
                return true;
            }else{
                commandSender.sendMessage(String.format("%s §cYou dont have permission to execute this command", prefix));
                return true;
            }
        }else{
            return false;
        }

    }

    public void scrable() {
        instance.StartingTime = 0;
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
        StringBuilder generated = new StringBuilder();

        Integer min = instance.getConfig().getInt("words.Minimum Length");
        Integer max = instance.getConfig().getInt("words.Maximum Length");

        String prefix = instance.getConfig().getString("Plugin Prefix").replace("&","§");

        int length = (int)(Math.random() * max + min);


        Random rnd = new Random();
        while (generated.length() < length) { // length of the random string.
            int index = (int) (rnd.nextFloat() * chars.length());
            generated.append(chars.charAt(index));
        }
        instance.Scrambled = generated.toString();
        instance.getServer().broadcastMessage(String.format("%s §cType out '%s' first to receive a prize", prefix, instance.Scrambled));


        timerTask = Bukkit.getScheduler().scheduleSyncRepeatingTask(instance, new Runnable() {
            @Override
            public void run() {
                if (instance.active) {
                    instance.StartingTime++;
                }
            }
        }, 0L, 20L); //0 Tick initial delay, 20 Tick (1 Second) between repeats




        instance.active = true;

    }

    public void forceEnd(){
        String prefix = instance.getConfig().getString("Plugin Prefix").replace("&","§");
        if (instance.active){
            instance.getServer().broadcastMessage(String.format("%s §cNo one typed out the scrambled message!", prefix));
            Bukkit.getScheduler().cancelTask(timerTask);
            instance.active = false;
        }
    }
}
