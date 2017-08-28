package me.varmetek.endercd.utility;

import com.google.common.base.Preconditions;
import org.bukkit.Bukkit;
import org.bukkit.Server;

import java.util.regex.Matcher;
import java.util.regex.Pattern;



public class MinecraftVersion implements Comparable<MinecraftVersion>, Cloneable
{
  protected static final Pattern periodSplit =  Pattern.compile("\\.");
  protected static final Pattern verType = Pattern.compile("v(\\d+)_(\\d+)_R(\\d+)");

  protected final int major, minor, revision;
  protected final String version;





  protected MinecraftVersion(String version){
    Preconditions.checkNotNull(version,"Version string cannot be null");
    Matcher ver = verType.matcher(version);
    Preconditions.checkArgument(ver.matches(),"Invalid Server version: "+ version);
    this.major = Integer.parseInt(ver.group(1));
    this.minor = Integer.parseInt(ver.group(2));
    this.revision = Integer.parseInt(ver.group(2));
    this.version = version;
  }

  public MinecraftVersion(int major, int minor){
    this(major,minor,1);

  }
  public MinecraftVersion(int major, int minor, int revision){
    Preconditions.checkArgument(major>= 0,"Major version must not be negative");
    Preconditions.checkArgument(minor>= 0,"Minor version must not be negative");
    Preconditions.checkArgument(revision>= 1,"Revision version must not be less than one");
    this.major = major;
    this.minor = minor;
    this.revision = revision;
    version = major+"_"+minor+"_R"+revision;
  }

  public MinecraftVersion(MinecraftVersion ver){
    this(ver.major,ver.minor,ver.revision);
  }




  public int getMajor(){
    return major;
  }

  public int getMinor(){
    return minor;
  }


  public int getRevision(){
    return revision;
  }

  @Override
  public String toString(){
    return version;
  }

  @Override
  public int hashCode(){
    return (this.major << 12) + (this.minor << 6) + this.revision;
  }


  @Override
  public boolean equals(Object v){

   if(!(v instanceof MinecraftVersion)) return false;
    return  equals((MinecraftVersion)v);

  }

  public boolean equals(MinecraftVersion ver){
    if(ver == null) return false;
    return compareTo(ver) == 0;
  }

  public boolean isAfter(MinecraftVersion ver){
    return compareTo(ver) > 0;
  }

  public boolean isBefore(MinecraftVersion ver){
    return compareTo(ver) < 0;
  }

  public boolean isAfterOrEquals(MinecraftVersion ver){
    return compareTo(ver) >= 0;
  }

  public boolean isBeforeOrEquals(MinecraftVersion ver){
    return compareTo(ver) <= 0;
  }



  @Override
  public int compareTo (MinecraftVersion o){
    if(o == null) return 1;
    int comp = this.major - o.major;
    if(comp != 0) return comp;
     comp = this.minor - o.minor;
    if(comp != 0) return comp;
    comp = this.revision - o.revision;
    return comp;
  }

  public static MinecraftVersion current(){
    Server server = Bukkit.getServer();
    Preconditions.checkNotNull(server,"Server cannot be null");
    String path  = server.getClass().getName();
    return new MinecraftVersion( periodSplit.split(path)[3]);
  }

  public static MinecraftVersion parserVersion(String version){
    try {
      return new MinecraftVersion(version);
    }catch (Exception ex){
      throw new VersionParseException(ex);
    }
  }

  public static class  VersionParseException extends RuntimeException{


    public VersionParseException() {
      super();
    }


    public VersionParseException(String message) {
      super(message);
    }



    public VersionParseException(String message, Throwable cause) {
      super(message, cause);
    }


    public VersionParseException(Throwable cause) {
      super(cause);
    }

  }
}
