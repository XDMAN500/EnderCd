package me.varmetek.endercd.listener;

import com.google.common.base.Preconditions;
import me.varmetek.endercd.EnderCdPlugin;
import me.varmetek.endercd.utility.MinecraftVersion;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;

public class EnderPearlListener implements Listener
{
  public static final MinecraftVersion v1_9 = new MinecraftVersion(1,9);
  private final EnderCdPlugin plugin;
  private boolean isV1_9;


  public EnderPearlListener (EnderCdPlugin plugin){

    this.plugin = Preconditions.checkNotNull(plugin,"Plugin cannot be null");
    isV1_9 = this.plugin.getVersion().isAfterOrEquals(v1_9);
  }

  @EventHandler
  public void joinEvent(PlayerJoinEvent ev){
    plugin.sendEXP(ev.getPlayer());
  }

  @EventHandler
  public void pearlClickEvent(PlayerInteractEvent ev){

    if(!ev.hasItem()) return;
    if(ev.getItem().getType() != Material.ENDER_PEARL) return;
    Player player = ev.getPlayer();
    if(player.hasPermission("endercd.bypass")) return;

    if(plugin.getCoolDowns().hasCoolDown(player)){
      if(player.isOnline()){
       plugin.sendCoolDown(player);

      }


      ev.setCancelled(true);
      if(isV1_9){
        ev.setUseItemInHand(Event.Result.DENY);
      }
    }else{
      plugin.getCoolDowns().addPlayer(ev.getPlayer());
    }


  }
}
