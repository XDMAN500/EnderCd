package me.varmetek.endercd;

import me.clip.placeholderapi.PlaceholderAPI;
import me.clip.placeholderapi.external.EZPlaceholderHook;
import me.varmetek.endercd.commands.CommandEnderCd;
import me.varmetek.endercd.listener.EnderPearlListener;
import me.varmetek.endercd.utility.ConfigManager;
import me.varmetek.endercd.utility.CoolDownManager;
import me.varmetek.endercd.utility.MinecraftVersion;
import me.varmetek.endercd.utility.TextUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.text.DecimalFormat;

public class EnderCdPlugin extends JavaPlugin implements Listener
{

  private static final String placeHolder = "%endercd_cooldown%";
  private static final DecimalFormat time = new DecimalFormat("0.0");

  private MinecraftVersion version;
  private UpdateCDTask task;
  private ConfigManager configManager;
  private boolean hasPlaceHolderApi;


  @Override
  public void onEnable(){
    configManager = new ConfigManager(this);

    task = new UpdateCDTask();
    task.runTaskTimer(this,20,5);
    version =  MinecraftVersion.current();
    Bukkit.getPluginManager().registerEvents(new EnderPearlListener(this), this);
    hasPlaceHolderApi = Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI");
    if(hasPlaceHolderApi){
      getLogger().info("Hooking into PlaceholderAPI");
        new CoolDownHook().hook();
    }
    getCommand("endercd").setExecutor(new CommandEnderCd(this));

  }




  @Override
  public void onDisable(){
    configManager.getCoolDowns().clear();
    configManager = null;
    task.cancel();
    task = null;
  }


  public  MinecraftVersion getVersion (){
    return version;
  }


  public void sendCoolDown(Player player){

    String msg = configManager.getCooldownMessage();
    TextUtil.sendMessage(player, hasPlaceHolderApi ?
                                   PlaceholderAPI.setPlaceholders(player,msg) :
                                   msg.replace(placeHolder,
                                     time.format(configManager.getCoolDowns().ofPlayerSeconds(player))));
  }


  public void  sendEXP(Player pl){
    float cooldown = (float) configManager.getCoolDowns().ofPlayerSeconds(pl);
    if (configManager.usingXpLevel()){
      pl.setLevel((int) Math.ceil(cooldown));
    }


    float delay = configManager.getDelay();
    if (configManager.usingXpBar()){
      pl.setExp(cooldown / delay);
    }
  }

  public CoolDownManager getCoolDowns (){
    return configManager.getCoolDowns();
  }

  public ConfigManager getConfigManager (){
     return configManager;
  }

  private class UpdateCDTask extends BukkitRunnable{

    @Override
    public void run (){
      configManager.getCoolDowns().updateCoolDowns();
    }
  }

  private class CoolDownHook extends EZPlaceholderHook{


    public CoolDownHook(){
      super(EnderCdPlugin.this,"endercd");
    }

    @Override
    public String onPlaceholderRequest (Player player, String placeholder){
      if("cooldown".equals(placeholder) && player != null){
        return time.format(configManager.getCoolDowns().ofPlayerSeconds(player));
      }
      return null;
    }

  }



}
