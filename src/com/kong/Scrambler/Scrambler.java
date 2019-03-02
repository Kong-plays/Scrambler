package com.kong.Scrambler;

import com.kong.Scrambler.Commands.CMD_Scrambler;
import com.kong.Scrambler.Listeners.PlayerListener;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.logging.Logger;

public class Scrambler extends JavaPlugin {

    public boolean active = false;
    public String Scrambled = "";
    public long StartingTime = 0;
    public boolean playersOnline = true;
    public static boolean started = false;
    public static Integer initTimer;


    public final Logger log = Logger.getLogger("Minecraft");

    private CMD_Scrambler cmd_scrambler;

    @Override
    public void onEnable(){
        loadConfiguration();
        startTimer();

        new CMD_Scrambler(this);
        new PlayerListener(this);

        log.info(String.format("[%s] Successfully loaded!", getDescription().getName()));


    }


    public void loadConfiguration() {
        final FileConfiguration c = this.getConfig();

        if (!new File("plugins" + File.separator + "Scrambler" + File.separator + "config.yml").exists()) {
            log.warning(String.format("[%s] Config File Doesn't exist, Generating now!", this.getDescription().getName()));

            c.addDefault("Plugin Prefix", "&8[&5Scrambler&8]");
            c.addDefault("Interval", 600);
            c.addDefault("words.Minimum Length", 8);
            c.addDefault("words.Maximum Length", 12);
            c.addDefault("rewards.Reward Command", "%SERVER% give {player} diamond 12");


            c.options().copyDefaults(true);
            this.saveConfig();
        }
    }

    public void startTimer(){
        started = true;
        cmd_scrambler = new CMD_Scrambler(this);

        Integer intervalPeriod = getConfig().getInt("Interval");

        initTimer = Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {
            @Override
            public void run() {
                if(!active) {
                    cmd_scrambler.scrable();
                }else{
                    cmd_scrambler.forceEnd();
                    cmd_scrambler.scrable();
                }
            }
        }, 0L, intervalPeriod*20); //0 Tick initial delay, 20 Tick (1 Second) between repeats

    }

}
