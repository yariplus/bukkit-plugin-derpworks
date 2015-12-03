package com.yaricraft.derpworks;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;

/**
 * Created by Yari on 12/1/2015.
 */
public class DerpWorksListener implements Listener {

    ArrayList<Material> unplaceables = new ArrayList<Material>();
    ArrayList<Material> unmetable = new ArrayList<Material>();

    public DerpWorksListener() {
        unplaceables.add(Material.CHEST);
        unplaceables.add(Material.TRAPPED_CHEST);
        unplaceables.add(Material.ENDER_CHEST);
        unplaceables.add(Material.PORTAL);
        unplaceables.add(Material.ENDER_PORTAL);
        unplaceables.add(Material.ENDER_PORTAL_FRAME);
        unplaceables.add(Material.BEDROCK);
        unplaceables.add(Material.WATER);
        unplaceables.add(Material.STATIONARY_WATER);
        unplaceables.add(Material.LAVA);
        unplaceables.add(Material.STATIONARY_LAVA);
        unplaceables.add(Material.TNT);
        unplaceables.add(Material.FIRE);

        unmetable.add(Material.WOOL);
        unmetable.add(Material.HARD_CLAY);
        unmetable.add(Material.STAINED_CLAY);
        unmetable.add(Material.GLASS);
        unmetable.add(Material.STAINED_GLASS);
        unmetable.add(Material.STAINED_GLASS_PANE);
        unmetable.add(Material.DIRT);
        unmetable.add(Material.SPONGE);
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {

        if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {

            if (event.getClickedBlock().getType().equals(Material.SEA_LANTERN)) {

                Location below = event.getClickedBlock().getLocation().subtract(0,1,0);
                Location above = event.getClickedBlock().getLocation().add(0, 1, 0);

                Block blockDispenser = below.getBlock();
                Block blockMetable = above.getBlock();

                if (blockDispenser.getType().equals(Material.DISPENSER)) {

                    BlockFace blockFace = ((org.bukkit.material.Dispenser)blockDispenser.getState().getData()).getFacing();

                    int distanceTravelled = 0;
                    Location placeAt = below;

                    while (true) {
                        placeAt = placeAt.add(blockFace.getModX(), blockFace.getModY(), blockFace.getModZ());
                        if (placeAt == null || placeAt.getBlockY() > 255 || placeAt.getBlockY() < 2) return;
                        if (placeAt.getBlock().getType().equals(Material.AIR)) break;
                        if (++distanceTravelled == 512) return;
                    }

                    Inventory inv = ((org.bukkit.block.Dispenser)blockDispenser.getState()).getInventory();

                    ItemStack[] itemStacks = inv.getContents();

                    for (int i = 0; i < itemStacks.length; i++) {
                        if (itemStacks[i] != null) {

                            Material material = itemStacks[i].getData().getItemType();

                            if (material.isBlock() && !unplaceables.contains(material)) {

                                placeAt.getBlock().setType(itemStacks[i].getData().getItemType());
                                placeAt.getBlock().setData((byte)itemStacks[i].getDurability());

                                if (itemStacks[i].getAmount() == 1) {
                                    inv.remove(itemStacks[i]);
                                }else{
                                    itemStacks[i].setAmount(itemStacks[i].getAmount() - 1);
                                }

                                if (!unmetable.contains(material) && material.equals(blockMetable.getType())) {
                                    if (material.equals(Material.LOG) || material.equals(Material.LOG_2)) {
                                        placeAt.getBlock().setData((byte)((placeAt.getBlock().getData() & 3) | (blockMetable.getData() & 12)));
                                    }else{
                                        placeAt.getBlock().setData(blockMetable.getData());
                                    }
                                }

                                event.getPlayer().playSound(event.getClickedBlock().getLocation(), Sound.DIG_STONE, 50.0f, 10.0f);

                                return;
                            }
                        }
                    }
                }
            }
        }
    }
}
