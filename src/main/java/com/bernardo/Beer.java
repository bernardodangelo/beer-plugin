package com.bernardo;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
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
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionType;
import org.bukkit.inventory.RecipeChoice;
import org.bukkit.configuration.file.FileConfiguration;
import org.jetbrains.annotations.NotNull;

public final class Beer extends JavaPlugin implements @NotNull Listener, TabExecutor {

    private String beerName;
    private int confusionDuration;
    private int weaknessDuration;
    private int slowDuration;
    private int confusionAmplifier;
    private int weaknessAmplifier;
    private int slowAmplifier;

    @Override
    public void onEnable() {
        getLogger().info("Beer enabled.");
        getServer().getPluginManager().registerEvents(this, this);
        getCommand("beer").setExecutor(this);

        loadConfig();

        NamespacedKey beerKey = new NamespacedKey(this, "beer_recipe");
        ItemStack beer = createBeerItem();
        ShapelessRecipe beerRecipe = new ShapelessRecipe(beerKey, beer);

        ItemStack waterBottle = new ItemStack(Material.POTION);
        PotionMeta waterMeta = (PotionMeta) waterBottle.getItemMeta();
        if (waterMeta != null) {
            waterMeta.setBasePotionData(new PotionData(PotionType.WATER));
            waterBottle.setItemMeta(waterMeta);
        }

        beerRecipe.addIngredient(new RecipeChoice.ExactChoice(waterBottle));
        beerRecipe.addIngredient(Material.WHEAT);
        beerRecipe.addIngredient(Material.FERMENTED_SPIDER_EYE);

        getServer().addRecipe(beerRecipe);
    }

    @Override
    public void onDisable() {
        getLogger().info("Beer disabled.");
    }

    @EventHandler
    public void onPlayerDrink(PlayerItemConsumeEvent event) {
        ItemStack item = event.getItem();
        if (item != null && item.getType() == Material.POTION) {
            PotionMeta potionMeta = (PotionMeta) item.getItemMeta();
            if (potionMeta != null && potionMeta.hasCustomEffect(PotionEffectType.CONFUSION)) {
                Player player = event.getPlayer();
                player.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, confusionDuration * 20, confusionAmplifier - 1), true);
                player.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, weaknessDuration * 20, weaknessAmplifier -1), true);
                player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, slowDuration * 20, slowAmplifier - 1), true);
            }
        }
    }


    private ItemStack createBeerItem() {
        ItemStack beer = new ItemStack(Material.POTION);
        PotionMeta potionMeta = (PotionMeta) beer.getItemMeta();
        if (potionMeta != null) {
            potionMeta.setColor(Color.YELLOW);
            potionMeta.setDisplayName("§r" + beerName);
            potionMeta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
            potionMeta.addCustomEffect(new PotionEffect(PotionEffectType.CONFUSION, confusionDuration * 20, confusionAmplifier - 1), true);
            potionMeta.addCustomEffect(new PotionEffect(PotionEffectType.WEAKNESS, weaknessDuration * 20, weaknessAmplifier - 1), true);
            potionMeta.addCustomEffect(new PotionEffect(PotionEffectType.SLOW, slowDuration * 20, slowAmplifier - 1), true);
            beer.setItemMeta(potionMeta);
        }
        return beer;
    }

    private void loadConfig() {
        saveDefaultConfig();

        FileConfiguration config = getConfig();

        beerName = config.getString("beer_name", "Beer");
        confusionDuration = config.getInt("nausea_duration", 14);
        weaknessDuration = config.getInt("weakness_duration", 14);
        slowDuration = config.getInt("slowness_duration", 10);
        confusionAmplifier = config.getInt("nausea_amplifier", 2);
        weaknessAmplifier = config.getInt("weakness_amplifier", 0);
        slowAmplifier = config.getInt("slowness_amplifier", 0);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (command.getName().equalsIgnoreCase("beer")) {
            if (args.length > 0 && args[0].equalsIgnoreCase("reload")) {
                reloadConfig();
                loadConfig();
                sender.sendMessage(ChatColor.YELLOW + "[Beer]" + ChatColor.WHITE + " Config reloaded.");
                return true;
            }
        }
        return false;
    }
}
