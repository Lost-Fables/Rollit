package net.lostfables.lughgk.rollit.inventoryitems;

import net.md_5.bungee.api.ChatColor;

public enum InventoryItemType {
    MONEYBAG("MONEYBAG", ChatColor.WHITE + "Moneybag", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNGNiM2FjZGMxMWNhNzQ3YmY3MTBlNTlmNGM4ZTliM2Q5NDlmZGQzNjRjNjg2OTgzMWNhODc4ZjA3NjNkMTc4NyJ9fX0=", 9);

    String tag;
    String title;
    String skullTexture;
    int size;

    InventoryItemType(String tag, String title, String skullTexture, int size) {
        this.title = title;
        this.skullTexture = skullTexture;
        this.tag = tag;
        this.size = size;
    }

}
