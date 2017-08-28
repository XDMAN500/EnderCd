package me.varmetek.endercd.utility;

import com.google.common.base.Preconditions;
import me.varmetek.endercd.EnderCdPlugin;
import org.bukkit.configuration.file.FileConfiguration;

public class ConfigManager
{
  private CoolDownManager coolDowns;
  private EnderCdPlugin plugin;
  private String cooldownMessage;
  private boolean useXpBar;
  private boolean useXpLevel;
  private int delay;

  public ConfigManager(EnderCdPlugin plugin){
    this.plugin = Preconditions.checkNotNull(plugin,"Plugin cannot be null");
    reConfig();
  }



  public CoolDownManager getCoolDowns (){
    return coolDowns;
  }

  public void reConfig(){
    plugin.saveDefaultConfig();

    plugin.reloadConfig();
    FileConfiguration config = plugin.getConfig();
    Number delay = (Number)config.get("delay");
    this.delay = delay.intValue();
    coolDowns = new CoolDownManager(this.plugin,delay.longValue()* 1000L);

    this.useXpBar  = config.getBoolean("use_xpbar");
    this.useXpLevel = config.getBoolean("use_xplevel");
    this.cooldownMessage = config.getString("message_time");


  }


  public int getDelay(){
    return delay;
  }

  public boolean usingXpLevel(){
    return useXpLevel;
  }

  public boolean usingXpBar(){
    return useXpBar;
  }

  public String getCooldownMessage(){
    return cooldownMessage;
  }
}
