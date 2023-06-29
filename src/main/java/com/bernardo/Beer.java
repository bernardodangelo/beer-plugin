package com.bernardo;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapelessRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public final class Beer extends JavaPlugin implements Listener {

    @Override
    public void onEnable() {
        getLogger().info("BeerPlugin has been enabled.");
        getServer().getPluginManager().registerEvents(this, this);

        ItemStack beer = createBeerItem();
        NamespacedKey beerKey = new NamespacedKey(this, "beer_recipe");
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
        if (item != null && item.getType() == Material.POTION && item.getItemMeta().hasDisplayName() &&
                item.getItemMeta().getDisplayName().equals("Cerveja")) {
            Player player = event.getPlayer();
            player.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 7 * 20, 0));
            player.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 7 * 20, 0));
            player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 3 * 20, 0));
        }
    }

    private ItemStack createBeerItem() {
        ItemStack beer = new ItemStack(Material.POTION);
        ItemMeta meta = beer.getItemMeta();
        meta.setDisplayName("Cerveja");
        meta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
        beer.setItemMeta(meta);
        return beer;
    }
}
