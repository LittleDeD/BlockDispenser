package com.Aeodyn.BlockDispenser;

import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.Dispenser;
import org.bukkit.event.block.BlockDispenseEvent;
import org.bukkit.event.block.BlockListener;
import org.bukkit.event.block.BlockRedstoneEvent;
import org.bukkit.inventory.ItemStack;

// TODO Add updating after placing.
public class BlockDispenserBlockListener extends BlockListener {

	public static BlockDispenser plugin;

	public BlockDispenserBlockListener(BlockDispenser instance) {
		plugin = instance;
	}

	java.util.logging.Logger log = BlockDispenser.log;
	boolean debug = BlockDispenser.debug;

	@Override
	public void onBlockDispense(BlockDispenseEvent event) {
		event.setCancelled(true);
		if (debug && event.isCancelled()) {
			log.info("BlockDispense event caught!");
		}
	}

	/* (non-Javadoc)
	 * @see org.bukkit.event.block.BlockListener#onBlockRedstoneChange(org.bukkit.event.block.BlockRedstoneEvent)
	 */
	@Override
	public void onBlockRedstoneChange(BlockRedstoneEvent event) {
		if (debug) {
			log.info("RedstoneChange event received!");
		}
		if (event.getOldCurrent() == 0 && event.getNewCurrent() > 0) {
			Block tTarget = event.getBlock();
			org.bukkit.Location loc = tTarget.getLocation();
			for (int _x = -1; _x <= 1; _x++) { //This stuff finds the Dispenser.
				for (int _y = -1; _y < 1; _y++) {
					for (int _z = -1; _z <= 1; _z++) {
						if (_z + _x == 1 || _z + _x == -1) {
							if (debug) {
								log.info(_x + ", " + _y + ", " + _z);
								log.info(tTarget.getWorld().getBlockAt(loc.getBlockX() + _x, loc.getBlockY() + _y, loc.getBlockZ() + _z).getState().getTypeId() + "");
							}
							if (tTarget.getWorld().getBlockAt(loc.getBlockX() + _x, loc.getBlockY() + _y, loc.getBlockZ() + _z).getState().getType() == org.bukkit.Material.DISPENSER) {

								Dispenser dispenser = (Dispenser) tTarget.getWorld().getBlockAt(loc.getBlockX() + _x, loc.getBlockY() + _y, loc.getBlockZ() + _z).getState();
								World world = event.getBlock().getWorld(); //The local world
								Block targetBlock; //The block at the target
								org.bukkit.material.Dispenser dispenserMaterial = (org.bukkit.material.Dispenser) tTarget.getWorld().getBlockAt(loc.getBlockX() + _x, loc.getBlockY() + _y, loc.getBlockZ() + _z).getState().getData(); //Used for the "getFacing()" method

								int itemID = 0; // The item ID that will be taken from the dispenser, defaults to nothing
								ItemStack item = new ItemStack(0); // The actual ItemStack that will be taken, also defaults to nothing.

								for (ItemStack i : dispenser.getInventory().getContents()) { // Finds the next itemtype to dispense
									if (i != null) {
										if (i.getTypeId() != 0) {
											itemID = i.getTypeId();
											item = i;
											break;
										}
									}
								}

								targetBlock = world.getBlockAt(dispenser.getX() + dispenserMaterial.getFacing().getModX(), dispenser.getY() + dispenserMaterial.getFacing().getModY(), dispenser.getZ() + dispenserMaterial.getFacing().getModZ()); //Determines the targeted block
								if ((itemID < 256 || itemID == 326 || itemID == 327 || itemID == 328 || itemID == 342 || itemID == 343) && itemID > 0) { //If it is a block, a bucket, or a minecart
									if (debug) {
										log.info("Placeable");
									}
									if (targetBlock.getTypeId() == 0 || targetBlock.getTypeId() == 8 || targetBlock.getTypeId() == 9 || targetBlock.getTypeId() == 10 || targetBlock.getTypeId() == 11 || targetBlock.getTypeId() == 27 || targetBlock.getTypeId() == 28 || targetBlock.getTypeId() == 66) {
										if (debug) {
											log.info("Target empty");
										}
										switch (itemID) {
										case 326:
											if (debug) {
												log.info("Is W bucket");
											}
											targetBlock.setTypeId(9, true); //Dispenses the water
											dispenser.getInventory().removeItem(item);
											dispenser.getInventory().addItem(new ItemStack(325, 1)); //Puts an empty bucket in the dispenser
											if (debug) {
												log.info("Placed");
											}
											break;
										case 327:
											if (debug) {
												log.info("Is L bucket");
											}
											targetBlock.setTypeId(11, true); //Dispenses the lava
											dispenser.getInventory().removeItem(item);
											dispenser.getInventory().addItem(new ItemStack(325, 1)); //Puts an empty bucket in the dispenser
											if (debug) {
												log.info("Placed");
											}
											break;
										case 328:
											if (debug) {
												log.info("Is minecart");
											}
											world.spawn(targetBlock.getLocation(), org.bukkit.entity.Minecart.class); // Dispenses the minecart
											dispenser.getInventory().removeItem(item);
											if (debug) {
												log.info("Placed");
											}
											break;
										case 342:
											if (debug) {
												log.info("Is storage minecart");
											}
											world.spawn(targetBlock.getLocation(), org.bukkit.entity.StorageMinecart.class); // Dispenses the minecart
											dispenser.getInventory().removeItem(item);
											if (debug) {
												log.info("Placed");
											}
											break;
										case 343:
											if (debug) {
												log.info("Is powered minecart");
											}
											world.spawn(targetBlock.getLocation(), org.bukkit.entity.PoweredMinecart.class); // Dispenses the minecart
											dispenser.getInventory().removeItem(item);
											if (debug) {
												log.info("Placed");
											}
											break;
										default:
											if (debug) {
												log.info("Is block");
											}
											targetBlock.setTypeIdAndData(itemID, (byte) item.getDurability(), true); //Dispenses the block
											dispenser.getInventory().removeItem(item);
											if (debug) {
												log.info("Placed");
											}
										}
									}
								}
							}
						}
					}
				}
			}
		}
		if ((event.getOldCurrent() > 0 && event.getNewCurrent() == 0) && BlockDispenser.removes) {
			Block target = event.getBlock();
			org.bukkit.Location loc = target.getLocation();
			for (int _x = -1; _x <= 1; _x++) {
				for (int _y = -1; _y < 1; _y++) {
					for (int _z = -1; _z <= 1; _z++) {
						if (_z + _x == 1 || _z + _x == -1) {
							if (target.getWorld().getBlockAt(loc.getBlockX() + _x, loc.getBlockY() + _y, loc.getBlockZ() + _z).getState().getType() == org.bukkit.Material.DISPENSER) {
								Dispenser dispenser = (Dispenser) target.getWorld().getBlockAt(loc.getBlockX() + _x, loc.getBlockY() + _y, loc.getBlockZ() + _z).getState();
								World world = event.getBlock().getWorld(); //The local world
								org.bukkit.material.Dispenser dispenserMaterial = (org.bukkit.material.Dispenser) target.getWorld().getBlockAt(loc.getBlockX() + _x, loc.getBlockY() + _y, loc.getBlockZ() + _z).getState().getData(); //Used for the "getFacing()" method
								Block targetBlock; //The block at the target

								ItemStack item = new ItemStack(0); // The actual ItemStack that will be given, also defaults to nothing.

								targetBlock = world.getBlockAt(dispenser.getX() + dispenserMaterial.getFacing().getModX(), dispenser.getY() + dispenserMaterial.getFacing().getModY(), dispenser.getZ() + dispenserMaterial.getFacing().getModZ()); //Determines the targeted block
								if (targetBlock.getTypeId() != 0) {
									item = new ItemStack(targetBlock.getTypeId(), 1, targetBlock.getData());
									try {
										if (dispenser.getInventory().addItem(item).isEmpty()) {
											targetBlock.setTypeId(0, true);

										} else if (debug) {
											log.severe("Dispenser at " + dispenser.getX() + "," + dispenser.getY() + "," + dispenser.getZ() + " is full!");
										}
									} catch (Exception e) {
										log.severe(e.getMessage());
									}
								}
							}
						}
					}
				}
			}
		}
	}
}