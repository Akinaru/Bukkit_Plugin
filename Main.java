package fr.akinaru.main;

import net.minecraft.server.v1_12_R1.ChunkRegionLoader;
import net.minecraft.server.v1_12_R1.EnumParticle;
import net.minecraft.server.v1_12_R1.PacketPlayOutWorldParticles;
import net.minecraft.server.v1_12_R1.World;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import java.lang.reflect.Method;
import java.util.Random;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Firework;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Random;

public class Main extends JavaPlugin implements Listener {

    double nb = 0;
    double state = 0; // 0 monté, 1 descente

    float Red = 255;
    float Green = 0;
    float Blue = 0;

    public void PColor(){

        int soustracteur = 15;

        if(Red == 255 && Green <= 254 && Blue == 0){
            Green = Green + soustracteur;
            Bukkit.broadcastMessage(" 1 Red: "+Red +" §aGreen§r: "+Green + " Blue: "+Blue);

        }else if (Red <= 255 && Red > 0 && Green == 255 && Blue == 0){
            Red = Red - soustracteur;
            Bukkit.broadcastMessage(" 2 §cRed§r: "+Red +" Green: "+Green + " Blue: "+Blue);

        }else if (Red == 0 && Green == 255 && Blue <= 254){
            Blue = Blue + soustracteur;
            Bukkit.broadcastMessage(" 3 Red: "+Red +" Green: "+Green + " §bBlue§r: "+Blue);

        }else if (Red == 0 && Green <= 255 && Green > 0 && Blue == 255){
            Green = Green - soustracteur;
            Bukkit.broadcastMessage(" 4 Red: "+Red +" §aGreen§r: "+Green + " Blue: "+Blue);
        }else if (Red <= 254 && Green == 0 && Blue == 255){
            Red = Red + soustracteur;
            Bukkit.broadcastMessage(" 5 §cRed§r: "+Red +" Green: "+Green + " Blue: "+Blue);

        }else if (Red == 255 && Green == 0 && Blue <= 255 && Blue > 0) {
            Blue = Blue - soustracteur;
            Bukkit.broadcastMessage(" 6 Red: " + Red + " Green: " + Green + " §bBlue§r: " + Blue);
        }
    }

    @Override
    public void onEnable() {
        getLogger().info("logregyfuiezgufrezhfuiezhuifeziuf");
        Bukkit.getServer().getPluginManager().registerEvents(this, this);

        for (Entity entity : Bukkit.getWorld("world").getEntities()) {
            if(entity instanceof Player){

                new BukkitRunnable() {

                    public void run() {
                        if(nb > 3){
                            state = 1;
                        }else if (nb < 0){
                            state = 0;
                        }

                        if(state == 0) {

                            nb = nb + 0.1;
                            //getLogger().info(""+nb + " state "+state);
                        }else if(state == 1){

                            nb = nb - 0.1;
                            //getLogger().info(""+nb + " state "+state);
                        }

                        Player p = (Player) entity;
                        //drawCircle(p.getLocation(), 1f, EnumParticle.SPELL_MOB, (float) nb);
                        //drawCircle(p.getLocation(), 1f, EnumParticle.PORTAL, (float) nb);
                        //drawCircle(p.getLocation(), 1.5f, EnumParticle.FLAME, (float) nb);
                        //drawCircle(p.getLocation(), 1f, EnumParticle.SPELL_MOB_AMBIENT, (float) nb);
                        //drawCircle(p.getLocation(),2f, EnumParticle.FALLING_DUST, (float)0);
                        //drawCircle(p.getLocation(),0.5f, EnumParticle.SPELL_MOB, (float) 1.9);
                        drawCircle(p.getLocation(),0.1f, EnumParticle.SPELL_MOB, (float) 0);
                        PColor();
                    }
                }.runTaskTimerAsynchronously(this, 0, 0);
            }
        }

    }

    @EventHandler
    public void Interact(PlayerInteractEvent e){
        Player p = e.getPlayer();
        Location loc = p.getEyeLocation();
        Block block = p.getTargetBlock(null, 100);
        Location bl = block.getLocation();
        //bl.getBlock().setType(Material.AIR);
        //bl.getWorld().spawnEntity(bl, EntityType.PRIMED_TNT);


        double distance = p.getLocation().distance(bl);

        Bukkit.broadcastMessage(""+bl.getX() + " player "+p.getLocation().getX());
        Bukkit.broadcastMessage(""+bl.getY() + " player "+p.getLocation().getY());
        Bukkit.broadcastMessage(""+bl.getZ() + " player "+p.getLocation().getZ());
        Bukkit.broadcastMessage(""+block.getType());
        Bukkit.broadcastMessage("§c"+bl);


        float x = (float) loc.getX();
        float y = (float) loc.getY() - 1;
        float z = (float) loc.getZ();



        for(double t = 0; t<distance; t+=0.1) {

            PacketPlayOutWorldParticles packet = new PacketPlayOutWorldParticles(EnumParticle.FLAME, true, (float) x, y, z, 255, 255, 255, 0, 0);
            ((CraftPlayer) p).getHandle().playerConnection.sendPacket(packet);
            loc.add(loc.getDirection().multiply(t));
            x = (float) loc.getX();
            y = (float) loc.getY() - 1;
            z = (float) loc.getZ();

        }
    }


    public void drawCircle(Location loc, float radius, EnumParticle particle, Float nb){
        for(double t = 0; t<50; t+=0.5){
            float x = radius*(float)Math.sin(t);
            float z = radius*(float)Math.cos(t);



            PacketPlayOutWorldParticles packet = new PacketPlayOutWorldParticles(particle, true, (float) loc.getX() + x, (float) loc.getY() + nb, (float) loc.getZ() + z, Red, Green, Blue, (float)255, 0);
            for(Player online : Bukkit.getOnlinePlayers()){
                ((CraftPlayer)online).getHandle().playerConnection.sendPacket(packet);
            }
        }

    }

}
