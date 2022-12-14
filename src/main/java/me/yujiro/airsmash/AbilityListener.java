package me.yujiro.airsmash;

import com.projectkorra.projectkorra.BendingPlayer;
import com.projectkorra.projectkorra.ability.CoreAbility;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerAnimationEvent;

public class AbilityListener implements Listener {

    @EventHandler
    public void onSwing(PlayerAnimationEvent event) {
        Player player = event.getPlayer();
        BendingPlayer bPlayer = BendingPlayer.getBendingPlayer(player);

        if (event.isCancelled() || bPlayer == null) {
            return;

        } else if (bPlayer.getBoundAbilityName().equalsIgnoreCase(null)) {
            return;

        } else if (bPlayer.getBoundAbilityName().equalsIgnoreCase("AirSmash")) {
            if (!CoreAbility.hasAbility(player, Main.class)) {
                new Main(player);
            }
        }
    }
}
