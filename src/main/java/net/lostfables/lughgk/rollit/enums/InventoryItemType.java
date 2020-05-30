package net.lostfables.lughgk.rollit.enums;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.Material;

import java.util.Arrays;
import java.util.List;

public enum InventoryItemType {
    MONEYBAG("MONEYBAG", ChatColor.WHITE + "Moneybag", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNGNiM2FjZGMxMWNhNzQ3YmY3MTBlNTlmNGM4ZTliM2Q5NDlmZGQzNjRjNjg2OTgzMWNhODc4ZjA3NjNkMTc4NyJ9fX0=", 9, new Material[]{Material.AIR, Material.GHAST_TEAR, Material.ACACIA_BUTTON, Material.IRON_NUGGET, Material.GOLD_NUGGET}),
    QUIVER("QUIVER", ChatColor.WHITE + "Quiver", null, 3, new Material[]{Material.AIR});

    String tag;
    String title;
    String skullTexture;
    int size;
    List<Material> mats;

    InventoryItemType(String tag, String title, String skullTexture, int size, Material[] mats) {
        this.title = title;
        this.skullTexture = skullTexture;
        this.tag = tag;
        this.size = size;
        this.mats = Arrays.asList(mats);
    }

    public String getTag() {
        return tag;
    }

    public String getTitle() {
        return title;
    }

    public String getSkullTexture() {
        return skullTexture;
    }

    public int getSize() {
        return size;
    }

    public List<Material> getMats() {
        return mats;
    }
}
