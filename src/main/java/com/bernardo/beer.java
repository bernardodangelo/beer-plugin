package com.bernardo;

import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapelessRecipe;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public final class beer extends JavaPlugin implements Listener {

    @Override
    public void onEnable() {
        getLogger().info("BeerPlugin has been enabled.");
        getServer().getPluginManager().registerEvents(this, this);

        NamespacedKey beerKey = new NamespacedKey(this, "beer_recipe");
        ItemStack beer = createBeerItem(beerKey);
        ShapelessRecipe beerRecipe = new ShapelessRecipe(beerKey, beer);
        beerRecipe.addIngredient(Material.POTION);
        beerRecipe.addIngredient(Material.WHEAT);
        beerRecipe.addIngredient(Material.FERMENTED_SPIDER_EYE);
        getServer().addRecipe(beerRecipe);
    }

    @Override
    public void onDisable() {
        getLogger().info("BeerPlugin has been disabled.");
    }

    @EventHandler
    public void onPlayerDrink(PlayerItemConsumeEvent event) {
        ItemStack item = event.getItem();
        if (item != null && item.getType() == Material.POTION) {
            PotionMeta potionMeta = (PotionMeta) item.getItemMeta();
            if (potionMeta.hasCustomEffect(PotionEffectType.CONFUSION)) {
                Player player = event.getPlayer();
                player.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 14 * 20, 2));
                player.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 14 * 20, 0));
                player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 10 * 20, 0));
            }
        }
    }

    private NamespacedKey beerPotionKey;

    private ItemStack createBeerItem(NamespacedKey beerKey) {
        beerPotionKey = new NamespacedKey(this, "beer_potion");
        ItemStack beer = new ItemStack(Material.POTION);
        PotionMeta potionMeta = (PotionMeta) beer.getItemMeta();
        potionMeta.setColor(Color.YELLOW);
        potionMeta.setDisplayName("Cerveja");
        potionMeta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
        potionMeta.addCustomEffect(new PotionEffect(PotionEffectType.CONFUSION, 14 * 20, 2), true);
        potionMeta.addCustomEffect(new PotionEffect(PotionEffectType.WEAKNESS, 14 * 20, 0), true);
        potionMeta.addCustomEffect(new PotionEffect(PotionEffectType.SLOW, 10 * 20, 0), true);
        beer.setItemMeta(potionMeta);
        return beer;
    }
}
