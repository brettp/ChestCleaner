package chestcleaner.main;

import org.bukkit.Bukkit;
import org.bukkit.Material;

import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import chestcleaner.commands.BlacklistCommand;
import chestcleaner.commands.CleanInvenotryCommand;
import chestcleaner.commands.CleaningItemCommand;
import chestcleaner.commands.SortingConfigCommand;
import chestcleaner.commands.TimerCommand;
import chestcleaner.config.PluginConfig;
import chestcleaner.listeners.SortingListener;
import chestcleaner.listeners.DataLoadingListener;
import chestcleaner.listeners.RefillListener;

public class ChestCleaner extends JavaPlugin {

	public static boolean cleanInvPermission = true;
	public static ItemStack item = new ItemStack(Material.IRON_HOE);
	public static boolean durability = true;
	public static boolean itemBoolean = true;
	public static boolean eventmode = false;
	public static boolean blockRefill = true;
	public static boolean consumablesRefill = true;
	private boolean updateCheckerActive = true;
	
	public static ChestCleaner main;
	
	@Override
	public void onEnable() {
		main = this;
		PluginConfig.getInstance().loadConfig();
		getCommand("cleaninventory").setExecutor(new CleanInvenotryCommand());
		getCommand("timer").setExecutor(new TimerCommand());
		getCommand("cleaningitem").setExecutor(new CleaningItemCommand());
		getCommand("blacklist").setExecutor(new BlacklistCommand());
		getCommand("sortingconfig").setExecutor(new SortingConfigCommand());
		Bukkit.getPluginManager().registerEvents(new SortingListener(), this);
		Bukkit.getPluginManager().registerEvents(new RefillListener(), this);
		Bukkit.getPluginManager().registerEvents(new DataLoadingListener(), this);
		
		if(updateCheckerActive) {
			new UpdateChecker(this).checkForUpdate();
		}
	}
	
	public void setUpdateCheckerActive(boolean b) {
		this.updateCheckerActive = b;
	}
	
}
