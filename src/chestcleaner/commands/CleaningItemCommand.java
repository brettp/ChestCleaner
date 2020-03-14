package chestcleaner.commands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.StringUtil;

import chestcleaner.config.PluginConfig;
import chestcleaner.config.PluginConfig.ConfigPath;
import chestcleaner.main.ChestCleaner;
import chestcleaner.utils.PluginPermissions;
import chestcleaner.utils.messages.MessageSystem;
import chestcleaner.utils.messages.StringTable;
import chestcleaner.utils.messages.enums.MessageID;
import chestcleaner.utils.messages.enums.MessageType;

/**
 * A command class representing the CleaningItem command. CleaningItem Command
 * explained: https://github.com/tom2208/ChestCleaner/wiki/Command-cleaningitem
 * 
 * @author Tom2208
 *
 */
public class CleaningItemCommand implements CommandExecutor, TabCompleter {

	/* sub-commands */
	private final String renameSubCommand = "rename";
	private final String setLoreSubCommand = "setLore";
	private final String setItemSubCommand = "setItem";
	private final String getSubCommand = "get";
	private final String setActiveSubCommand = "setActive";
	private final String setDurabilityLossSubCommand = "setDurabilityLoss";
	private final String giveSubCommand = "give";
	private final String setEventDetectionModeSubCommand = "setEventDetectionMode";
	
	private final String trueStr = "true";
	private final String falseStr = "false";
	
	/* placeholder */
	private final String newItemPlaceholder = "%newitem";
	private final String itemNamePlaceholder = "%itemname";
	private final String playerNamePlaceholder = "%playername";
	private final String modeBooleanPlaceholder = "%modeBoolean";

	/* Syntax Error Messages */
	private final String renameSytaxError = "/cleaningitem rename <name>";
	private final String setActiveSyntaxError = "/cleaningitem setactive <true/false>";
	private final String setDurabilityLossSyntaxError = "/cleaningitem setDurabilityLoss <true/false>";
	private final String subCommandsSyntaxError = "/cleaningitem <setitem/setactive/setdurabilityLoss/get/give/rename/setlore/seteventdetectionmode>";
	private final String consoleSubCommandsSyntaxError = "/cleaningitem <setactive/setdurabilityloss/give/rename/setlore>";

	@Override
	public boolean onCommand(CommandSender sender, Command command, String alias, String[] args) {

		boolean isPlayer = sender instanceof Player;
		Player player = (Player) sender;

		if (args.length > 1) {

			/* RENAME SUBCOMMAND */
			if (args[0].equalsIgnoreCase(renameSubCommand)) {

				if (player.hasPermission(PluginPermissions.CMD_CLEANING_ITEM_RENAME.getString()) || !isPlayer) {

					String newname = new String();
					for (int i = 1; i < args.length; i++) {

						if (i == 1)
							newname = args[1];
						else
							newname = newname + " " + args[i];

					}

					newname = newname.replace("&", "�");
					if (isPlayer)
						MessageSystem.sendMessageToPlayer(MessageType.SUCCESS,
								StringTable.getMessage(MessageID.NEW_ITEM_NAME, itemNamePlaceholder, newname), player);
					else
						MessageSystem.sendConsoleMessage(MessageType.SUCCESS,
								StringTable.getMessage(MessageID.NEW_ITEM_NAME, itemNamePlaceholder, newname));

					ItemStack is = ChestCleaner.item;
					ItemMeta im = is.getItemMeta();
					im.setDisplayName(newname);
					ChestCleaner.item.setItemMeta(im);
					PluginConfig.getInstance().setIntoConfig(ConfigPath.CLEANING_ITEM, ChestCleaner.item);
					if (args.length == 1)
						return true;

				} else {
					MessageSystem.sendMessageToPlayer(MessageType.MISSING_PERMISSION,
							PluginPermissions.CMD_CLEANING_ITEM_RENAME.getString(), player);
					return true;
				}
				return true;

				/* SETLORE SUBCOMMAND */
			} else if (args[0].equalsIgnoreCase(setLoreSubCommand)) {

				if (player.hasPermission(PluginPermissions.CMD_CLEANING_ITEM_SET_LORE.getString()) || !isPlayer) {

					String lore = args[1];
					for (int i = 2; i < args.length; i++) {
						lore = lore + " " + args[i];
					}

					String[] lorearray = lore.split("/n");

					ArrayList<String> lorelist = new ArrayList<>();

					for (String obj : lorearray) {
						obj = obj.replace("&", "�");
						lorelist.add(obj);

					}

					ItemMeta im = ChestCleaner.item.getItemMeta();
					im.setLore(lorelist);
					ChestCleaner.item.setItemMeta(im);
					PluginConfig.getInstance().setIntoConfig(ConfigPath.CLEANING_ITEM, ChestCleaner.item);

					if (isPlayer)
						MessageSystem.sendMessageToPlayer(MessageType.SUCCESS, MessageID.NEW_ITEM_LORE, player);
					else
						MessageSystem.sendConsoleMessage(MessageType.SUCCESS, MessageID.NEW_ITEM_LORE);
					return true;

				} else {
					MessageSystem.sendMessageToPlayer(MessageType.MISSING_PERMISSION,
							PluginPermissions.CMD_CLEANING_ITEM_SET_LORE.getString(), player);
					return true;
				}

			}

		}

		if (args.length == 1) {

			/* RENAME SUBCOMMAND ERRORS */
			if (args[0].equalsIgnoreCase(renameSubCommand)) {
				if (isPlayer)
					MessageSystem.sendMessageToPlayer(MessageType.SYNTAX_ERROR, renameSytaxError, player);
				else
					MessageSystem.sendConsoleMessage(MessageType.SYNTAX_ERROR, renameSytaxError);
				return true;
			}

			/* SETITEM SUBCOMMAND */
			else if (args[0].equalsIgnoreCase(setItemSubCommand) && isPlayer) {

				if (player.hasPermission(PluginPermissions.CMD_CLEANING_ITEM_SET_ITEM.getString())) {

					ItemStack item = player.getInventory().getItemInMainHand().clone();
					if (item != null) {
						item.setDurability((short) 0);
						item.setAmount(1);
						PluginConfig.getInstance().setIntoConfig(ConfigPath.CLEANING_ITEM, item);

						ChestCleaner.item = item;
						MessageSystem.sendMessageToPlayer(MessageType.SUCCESS,
								StringTable.getMessage(MessageID.NEW_ITEM, newItemPlaceholder, item.toString()),
								player);
						return true;

					} else {
						MessageSystem.sendMessageToPlayer(MessageType.ERROR, MessageID.HOLD_AN_ITEM, player);
						return true;
					}
				} else {
					MessageSystem.sendMessageToPlayer(MessageType.MISSING_PERMISSION,
							PluginPermissions.CMD_CLEANING_ITEM_SET_ITEM.getString(), player);
					return true;
				}

				/* GET SUBCOMMAND */
			} else if (args[0].equalsIgnoreCase(getSubCommand) && isPlayer) {

				if (player.hasPermission(PluginPermissions.CMD_CLEANING_ITEM_GET.getString())) {

					player.getInventory().addItem(ChestCleaner.item);
					MessageSystem.sendMessageToPlayer(MessageType.SUCCESS, MessageID.GOT_ITEM, player);
					return true;

				} else {
					MessageSystem.sendMessageToPlayer(MessageType.MISSING_PERMISSION,
							PluginPermissions.CMD_CLEANING_ITEM_GET.getString(), player);
					return true;
				}

			}

		} else if (args.length == 2) {

			/* SETACTIVE SUBCOMMAND */
			if (args[0].equalsIgnoreCase(setActiveSubCommand)) {

				if (player.hasPermission(PluginPermissions.CMD_CLEANING_ITEM_SET_ACTIVE.getString()) || !isPlayer) {

					if (args[1].equalsIgnoreCase(trueStr) || args[1].equalsIgnoreCase(falseStr)) {

						boolean b = false;
						if (args[1].equalsIgnoreCase(trueStr))
							b = true;

						PluginConfig.getInstance().setIntoConfig(ConfigPath.CLEANING_ITEM_ACTIVE, b);
						ChestCleaner.itemBoolean = b;

						if (b) {
							MessageSystem.sendMessageToPlayer(MessageType.SUCCESS, MessageID.ITEM_ACTIVATED, player);
						} else {
							MessageSystem.sendMessageToPlayer(MessageType.SUCCESS, MessageID.ITEM_DEACTIVATED, player);
						}
						return true;

					} else {
						if (isPlayer)
							MessageSystem.sendMessageToPlayer(MessageType.SYNTAX_ERROR, setActiveSyntaxError, player);
						else
							MessageSystem.sendConsoleMessage(MessageType.SYNTAX_ERROR, setActiveSyntaxError);
						return true;
					}

				} else {
					MessageSystem.sendMessageToPlayer(MessageType.MISSING_PERMISSION,
							PluginPermissions.CMD_CLEANING_ITEM_SET_ACTIVE.getString(), player);
					return true;
				}

				/* SETDURIBILITYLOSS SUBCOMMAND */
			} else if (args[0].equalsIgnoreCase(setDurabilityLossSubCommand)) {

				if (player.hasPermission(PluginPermissions.CMD_CLEANING_ITEM_SET_DURABILITYLOSS.getString())
						|| !isPlayer) {

					if (args[1].equalsIgnoreCase(trueStr) || args[1].equalsIgnoreCase(falseStr)) {

						boolean b = false;
						if (args[1].equalsIgnoreCase(trueStr))
							b = true;

						PluginConfig.getInstance().setIntoConfig(ConfigPath.CLEANING_ITEM_DURABILITY, b);
						ChestCleaner.durability = b;
						if (ChestCleaner.durability) {
							MessageSystem.sendMessageToPlayer(MessageType.SUCCESS, MessageID.DURABILITYLOSS_ACTIVATED,
									player);
						} else {
							MessageSystem.sendMessageToPlayer(MessageType.SUCCESS, MessageID.DURABILITYLOSS_DEACTIVATED,
									player);
						}
						return true;

					} else {
						MessageSystem.sendMessageToPlayer(MessageType.SYNTAX_ERROR, setDurabilityLossSyntaxError,
								player);
						return true;
					}

				} else {
					MessageSystem.sendMessageToPlayer(MessageType.MISSING_PERMISSION,
							PluginPermissions.CMD_CLEANING_ITEM_SET_DURABILITYLOSS.getString(), player);
					return true;
				}

				/* GIVE SUBCOMMAND */
			} else if (args[0].equalsIgnoreCase(giveSubCommand)) {

				if (player.hasPermission(PluginPermissions.CMD_CLEANING_ITEM_GIVE.getString()) || !isPlayer) {

					Player p2 = Bukkit.getPlayer(args[1]);

					if (p2 != null) {

						p2.getInventory().addItem(ChestCleaner.item);
						if (isPlayer)
							MessageSystem.sendMessageToPlayer(MessageType.SUCCESS, StringTable.getMessage(
									MessageID.PLAYER_GOT_ITEM, playerNamePlaceholder, p2.getName()), player);
						else
							MessageSystem.sendConsoleMessage(MessageType.SUCCESS, StringTable
									.getMessage(MessageID.PLAYER_GOT_ITEM, playerNamePlaceholder, player.getName()));
						return true;

					} else {

						if (args[1].equalsIgnoreCase("@a")) {

							Object[] players = Bukkit.getOnlinePlayers().toArray();

							for (Object p : players) {
								Player pl = (Player) p;
								pl.getInventory().addItem(ChestCleaner.item);
								if (isPlayer)
									MessageSystem.sendMessageToPlayer(MessageType.SUCCESS, StringTable.getMessage(
											MessageID.PLAYER_GOT_ITEM, playerNamePlaceholder, pl.getName()), player);
							}
							return true;
						}

						if (isPlayer)
							MessageSystem.sendMessageToPlayer(MessageType.ERROR, StringTable
									.getMessage(MessageID.PLAYER_IS_NOT_ONLINE, playerNamePlaceholder, args[1]), p2);
						else
							MessageSystem.sendConsoleMessage(MessageType.ERROR, StringTable
									.getMessage(MessageID.PLAYER_IS_NOT_ONLINE, playerNamePlaceholder, args[1]));
						return true;
					}

				} else {
					MessageSystem.sendMessageToPlayer(MessageType.MISSING_PERMISSION,
							PluginPermissions.CMD_CLEANING_ITEM_GIVE.getString(), player);
					return true;
				}

				/* SETEVENTDETECTIONMODE SUBCOMMAND */
			} else if (args[0].equalsIgnoreCase(setEventDetectionModeSubCommand)) {

				if (player.hasPermission(PluginPermissions.CMD_CLEANING_ITEM_SET_EVENT_MODE.getString())) {

					boolean b = Boolean.parseBoolean(args[1]);
					ChestCleaner.eventmode = b;
					MessageSystem.sendMessageToPlayer(MessageType.SUCCESS, StringTable.getMessage(
							MessageID.SET_INVENTORY_DETECTION_MODE, modeBooleanPlaceholder, String.valueOf(b)), player);
					PluginConfig.getInstance().setIntoConfig(ConfigPath.OPEN_INVENTORY_MODE, b);

					return true;

				} else {
					if (isPlayer)
						MessageSystem.sendMessageToPlayer(MessageType.MISSING_PERMISSION,
								PluginPermissions.CMD_CLEANING_ITEM_SET_EVENT_MODE.getString(), player);
					return true;
				}

			} else {

				if (isPlayer)
					MessageSystem.sendMessageToPlayer(MessageType.SYNTAX_ERROR, subCommandsSyntaxError, player);
				else
					MessageSystem.sendConsoleMessage(MessageType.SYNTAX_ERROR, consoleSubCommandsSyntaxError);
				return true;
			}

		} else {
			if (isPlayer)
				MessageSystem.sendMessageToPlayer(MessageType.SYNTAX_ERROR, subCommandsSyntaxError, player);
			else
				MessageSystem.sendConsoleMessage(MessageType.SYNTAX_ERROR, consoleSubCommandsSyntaxError);
			return true;
		}

		return true;

	}

	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {

		final List<String> completions = new ArrayList<>();
		final String[] strCleaningitemCommands = { renameSubCommand, setLoreSubCommand, setItemSubCommand,
				getSubCommand, setActiveSubCommand, setDurabilityLossSubCommand, giveSubCommand,
				setEventDetectionModeSubCommand };
		final List<String> cleaningItemCommands = Arrays.asList(strCleaningitemCommands);

		switch (args.length) {
		case 0:
			StringUtil.copyPartialMatches(args[0], cleaningItemCommands, completions);
			break;
		case 1:
			StringUtil.copyPartialMatches(args[0], cleaningItemCommands, completions);
			break;
		case 2:
			if (args[0].equalsIgnoreCase(setActiveSubCommand) || args[0].equalsIgnoreCase(setDurabilityLossSubCommand)
					|| args[0].equalsIgnoreCase(setEventDetectionModeSubCommand)) {

				ArrayList<String> commands = new ArrayList<>();
				commands.add(trueStr);
				commands.add(falseStr);

				StringUtil.copyPartialMatches(args[1], commands, completions);
			}

		}

		Collections.sort(completions);
		return completions;
	}

}
