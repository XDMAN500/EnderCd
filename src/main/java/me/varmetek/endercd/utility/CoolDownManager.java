package me.varmetek.endercd.utility;

import com.google.common.base.Preconditions;
import me.varmetek.endercd.EnderCdPlugin;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.*;

public class CoolDownManager
{
  private Map<UUID,Long> coolDowns = new HashMap();
  private final long delay;
  private EnderCdPlugin plugin;
  public CoolDownManager (EnderCdPlugin plugin ,long delay){
    this.delay = delay;
    this.plugin = plugin;
  }

  public void addPlayer(OfflinePlayer player){
    addPlayer(player.getUniqueId());
  }
  public void addPlayer(UUID player){
    Preconditions.checkNotNull(player,"Player cannot be null");
    coolDowns.put(player, System.currentTimeMillis());
  }

  public void resetPlayer(OfflinePlayer player){
    resetPlayer(player.getUniqueId());
  }

  public void resetPlayer(UUID player){
    coolDowns.remove(player);
  }

  public boolean hasCoolDown(OfflinePlayer player){
    return hasCoolDown(player.getUniqueId());
  }
  public boolean hasCoolDown(UUID player){
    return ofPlayer(player) != 0L;
  }

  public long ofPlayer(OfflinePlayer player){
   return ofPlayer(player.getUniqueId());
  }

  public long ofPlayer(UUID player){
    if(coolDowns.containsKey(player)){
      long del = this.delay - (System.currentTimeMillis() - coolDowns.get(player));
      return Math.max(del,0L);
    }else return 0L;
  }

  public double ofPlayerSeconds(UUID player){
    return ((double)ofPlayer(player))/1000.0;
  }
  public double ofPlayerSeconds(OfflinePlayer player){
    return ofPlayerSeconds(player.getUniqueId());
  }




  public void updateCoolDowns(){

    long now = System.currentTimeMillis();
    Set<UUID> players = new HashSet<>(coolDowns.keySet());
    for(UUID player: players){
      if(player == null) continue;
      Player pl = Bukkit.getPlayer(player);

      if(pl != null){
     plugin.sendEXP(pl);
      }
      if(now -coolDowns.get(player) > this.delay){
        resetPlayer(player);
      }
    }

  }

  public void clear(){
    coolDowns.clear();
  }

  public Set<UUID> getPlayers(){
    return Collections.unmodifiableSet(coolDowns.keySet());
  }
}
