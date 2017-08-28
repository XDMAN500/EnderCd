package me.varmetek.endercd.commands;

import com.google.common.base.Preconditions;
import me.varmetek.endercd.EnderCdPlugin;
import me.varmetek.endercd.utility.ConfigManager;
import me.varmetek.endercd.utility.TextUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class CommandEnderCd implements CommandExecutor
{

  private final EnderCdPlugin plugin;
  private boolean isV1_9;


  public CommandEnderCd (EnderCdPlugin plugin){

    this.plugin = Preconditions.checkNotNull(plugin,"Plugin cannot be null");
  }

  @Override
  public boolean onCommand (CommandSender sender, Command command, String label, String[] args){
    boolean permission =sender.hasPermission("endercd.admin");
    if(args.length == 0){
      String version = plugin.getDescription().getVersion();
      TextUtil.sendMessage(sender,String.format(" &aEnderCd (%s) by Varmetek", version));
      if(permission){
        TextUtil.sendMessage(sender,"&7 Run /"+label+ " ? for more info");
      }
      return false;
    }

    if(!permission){
      TextUtil.sendMessage(sender,"&cError: No permission");
      return false;
    }

    switch (args[0].toLowerCase()){
      default:{
        TextUtil.sendMessage(sender,"&cError: Unknown sub-command");
        TextUtil.sendMessage(sender,"&7 Run /"+label+ " ? for help");
      }break;

      case "?":
      case "help":{
        TextUtil.sendMessage(sender,
          "&a[?] EnderCd Help Menu",
          "&e   /"+label+" reload",
          "&7   Reloads settings from the config",
          "",
          "&e   /"+label+" config",
          "&7   Displays current config settings"

        );

      }break;


      case "reload":{
        plugin.getConfigManager().reConfig();
        TextUtil.sendMessage(sender," &aConfig reloaded");
      }break;

      case "config":{
        ConfigManager config = plugin.getConfigManager();
        TextUtil.sendMessage(sender,
          "&a  Delay:&7 "+ config.getDelay(),
          "&a  UseXpBar:&7 "+ config.usingXpBar(),
          "&a  UseXpLevel:&7 "+ config.usingXpBar(),
          "&a  Cooldown Message:&7 "+ config.getCooldownMessage());

      }
    }

    return true;
  }
}
