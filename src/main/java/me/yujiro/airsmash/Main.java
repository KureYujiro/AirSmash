package me.yujiro.airsmash;

import com.projectkorra.projectkorra.GeneralMethods;
import com.projectkorra.projectkorra.ProjectKorra;
import com.projectkorra.projectkorra.ability.AddonAbility;
import com.projectkorra.projectkorra.ability.AirAbility;
import com.projectkorra.projectkorra.ability.CoreAbility;
import com.projectkorra.projectkorra.util.DamageHandler;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class Main extends AirAbility implements AddonAbility {

    private int speed;
    private int range;
    private long cooldown;
    private double damage;
    private Location location;
    private Location origin;
    private Vector direction;

    public Main(Player player) {
        super(player);

        if (!bPlayer.canBend(this) || CoreAbility.hasAbility(player, this.getClass()) || bPlayer.isOnCooldown(this)){
            return;
        }

        setFields();
        start();
        bPlayer.addCooldown(this);

    }

    private void setFields() {

        this.origin = player.getLocation().clone().add(0, 1, 0);


        this.location = origin.clone();


        this.direction = player.getLocation().getDirection();

        this.speed = ProjectKorra.plugin.getConfig().getInt("Yujiro.Air.AirSmash.Speed");
        this.range = ProjectKorra.plugin.getConfig().getInt("Yujiro.Air.AirSmash.Range");
        this.cooldown = ProjectKorra.plugin.getConfig().getLong("Yujiro.Air.AirSmash.Cooldown");
        this.damage = ProjectKorra.plugin.getConfig().getDouble("Yujiro.Air.AirSmash.Damage");
    }

    @Override
    public void progress() {
        if (player.isDead() || !player.isOnline()) {
            remove();
            return;
        }


        if (origin.distance(location) > range) {
            remove();
            return;
        }


        location.add(direction.multiply(speed));

        AirAbility.playAirbendingParticles(location, 5, 0.2, 0.2, 0.2);


        if (GeneralMethods.isSolid(location.getBlock())) {
            remove();
            return;
        }


        for (Entity entity : GeneralMethods.getEntitiesAroundPoint(location, 1)) {

            if ((entity instanceof LivingEntity) && entity.getUniqueId() != player.getUniqueId()) {
                direction = GeneralMethods.getDirection(this.origin, this.location).normalize();

                DamageHandler.damageEntity(entity, damage, this);
                if (entity.isOnGround()) {
                    entity.setVelocity(direction);
                    AirAbility.playAirbendingSound(location);
                }
                else {
                    entity.setVelocity(direction);
                }
                remove();
                return;
            }
        }
    }

    @Override
    public boolean isSneakAbility() {
        return false;
    }

    @Override
    public boolean isHarmlessAbility() {
        return false;
    }

    @Override
    public long getCooldown() {
        return cooldown;
    }

    @Override
    public String getName() {
        return "AirSmash";
    }

    @Override
    public Location getLocation() {
        return location;
    }

    @Override
    public void load() {
        ProjectKorra.plugin.getServer().getPluginManager().registerEvents(new AbilityListener(), ProjectKorra.plugin);
        FileConfiguration config = ProjectKorra.plugin.getConfig();
        config.addDefault("Yujiro.Air.AirSmash.Range", 20);
        config.addDefault("Yujiro.Air.AirSmash.Cooldown", 1000);
        config.addDefault("Yujiro.Air.AirSmash.Damage", 2.0D);
        config.addDefault("Yujiro.Air.AirSmash.Speed", 1.0);
        config.options().copyDefaults(true);
        ProjectKorra.plugin.saveConfig();
        ProjectKorra.plugin.getLogger().info(String.format("%s %s, developed by %s, has been loaded.", getName(), getVersion(), getAuthor()));


        ProjectKorra.log.info("Successfully enabled " + getName() + " by " + getAuthor());
    }

    @Override
    public void stop() {
        ProjectKorra.log.info("Successfully disabled " + getName() + " by " + getAuthor());
        super.remove();
    }

    @Override
    public String getAuthor() {
        return "Yujiro";
    }

    @Override
    public String getVersion() {
        return "1.0";
    }
}
