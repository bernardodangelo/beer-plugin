package com.bernardo;

import net.kyori.adventure.text.Component;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapelessRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import javax.xml.stream.events.Namespace;

public final class Beer extends JavaPlugin implements Listener {
    private NamespacedKey CUSTOM_ITEMS_KEY;
    private final String beerItemId = "beer_potion";

    @Override
    public void onEnable() {
        getLogger().info("BeerPlugin has been enabled.");
        getServer().getPluginManager().registerEvents(this, this);
        this.CUSTOM_ITEMS_KEY = new NamespacedKey(this, "com.bernado.items");
        NamespacedKey beerKey = new NamespacedKey(this, "com.bernado.recipes.beer");
        ShapelessRecipe beerRecipe = new ShapelessRecipe(beerKey, createBeerItem());
        beerRecipe.addIngredient(Material.POTION);
        beerRecipe.addIngredient(Material.WHEAT);
        beerRecipe.addIngredient(Material.FERMENTED_SPIDER_EYE);
    }

    @Override
    public void onDisable() {
        getLogger().info("BeerPlugin has been disabled.");
        HandlerList.unregisterAll();
    }

    @EventHandler
    public void onPlayerDrink(PlayerItemConsumeEvent event) {
        Player player = event.getPlayer();
        ItemStack item = event.getItem();
        PersistentDataContainer pdc = item.getItemMeta().getPersistentDataContainer();
        if (pdc.has(CUSTOM_ITEMS_KEY, PersistentDataType.STRING) && pdc.get(CUSTOM_ITEMS_KEY, PersistentDataType.STRING).equals(beerItemId)) {
            player.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 14 * 20, 2));
            player.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 14 * 20, 0));
            player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 10 * 20, 0));
        }
    }

    private ItemStack createBeerItem() {
        ItemStack beer = new ItemStack(Material.POTION);
        PotionMeta potionMeta = (PotionMeta) beer.getItemMeta();
        potionMeta.setColor(Color.YELLOW);
        potionMeta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
        beer.setItemMeta(potionMeta);
        ItemMeta meta = beer.getItemMeta();
        meta.getPersistentDataContainer().set(CUSTOM_ITEMS_KEY, PersistentDataType.STRING, beerItemId);
        meta.displayName(Component.text("Beer"));
        beer.setItemMeta(meta);
        return beer;
    }

    private PotionEffect createPotionEffect(PotionEffectType type, int duration) {
        return new PotionEffect(type, duration, 0, false, false);
    }
}