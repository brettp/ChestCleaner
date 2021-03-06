package chestcleaner.utils.messages;

import org.bukkit.entity.Player;

import chestcleaner.main.ChestCleaner;
import chestcleaner.utils.Tuple;
import chestcleaner.utils.messages.enums.MessageID;
import chestcleaner.utils.messages.enums.MessageType;

public class MessageSystem {

	public static void sendMessageToPlayer(MessageType type, String arg, Player p) {
		p.sendMessage(getMessageString(type, arg));
	}

	public static void sendMessageToPlayer(MessageType type, MessageID messageID, Player p) {
		p.sendMessage(getMessageString(type, ChestCleaner.main.getRB().getString(messageID.getID())));
	}

	/**
	 * Sends a message with the MessageID {@code messageID} and the MessageType
	 * {@code messageType} to the player {@code p} replacing the DEFAULT_PLACEHOLDER
	 * ("%variable%") with the String {@code Replacement}.
	 * 
	 * @param type the MessageType of the message.
	 * @param messageID the MessageID of the Message.
	 * @param p the player who should receive the message.
	 * @param replacement the replacement of the DEFAULT_PLACEHOLDEN.
	 */
	public static void sendMessageToPlayerWithReplacements(MessageType type, MessageID messageID, Player p,
			String replacement) {
		String message = ChestCleaner.main.getRB().getString(messageID.getID());
		p.sendMessage(getMessageString(type, replaceVariable(message, replacement)));
	}

	public static void sendConsoleMessage(MessageType type, String arg) {
		ChestCleaner.main.getServer().getConsoleSender().sendMessage(getMessageString(type, arg));
	}

	public static void sendConsoleMessage(MessageType type, MessageID messageID) {
		ChestCleaner.main.getServer().getConsoleSender()
				.sendMessage(getMessageString(type, ChestCleaner.main.getRB().getString(messageID.getID())));
	}

	/**
	 * Just replaces all {@code DEFAULT_PLACEHOLDER} ("%variable%") Substrings in
	 * {@code str} with the {@code replacement}.
	 * 
	 * @param str         The string in which substrings should be replaced. It uses
	 *                    String.replaceAll().
	 * @param replacement the replacement.
	 * @return A String with all replacements in {@code str}.
	 */
	public static String replaceVariable(String str, String replacement) {
		return str.replaceAll(DEFAULT_PLACEHOLDER, replacement);
	}

	/**
	 * Replaces all Strings from the tuples {@code repplacements} first elements in
	 * the String {@code str} with the seconds elements of the tuples
	 * {@code replacements}. The first element of the tuple is the element to get
	 * replaced, the second is the replacement. The method does the replacement with
	 * every entry of the array {@code replaceVariables}. For a single replacement
	 * you can use the method
	 * {@code chestcleaner.utils.messages.MessageSystem.replaceVariable()}. </br>
	 * <b>Example:</b> </br>
	 * [For the sake of clarity, we represent a tuple as (x, y)] </br>
	 * str = "This is an <b>variable0</b> for more <b>varibale1.</b>" </br>
	 * replacements = {("variable0", "example"), ("variable1", "clarity")} </br>
	 * </br>
	 * result = "This is an <b>example</b> for more <b>clarity</b>.
	 * 
	 * @param str          the string in which strings get replaced.
	 * @param replacements an array of tuples with replacements. The first element
	 *                     is the string which you want to replace, the second
	 *                     element is the string which is the replacement.
	 * @return returns a string with all replacements done in {@code str}.
	 */
	public static String replaceVariables(String str, Tuple<String, String>[] replacements) {
		String newStr = str;

		for (Tuple<String, String> tupel : replacements) {
			newStr = newStr.replaceAll(tupel.getFirstElement(), tupel.getSecondElement());
		}
		return newStr;
	}

	private static String getMessageString(MessageType type, String arg) {

		String out = ChestCleaner.main.getRB().getString(MessageID.PREFIX.getID()) + " ";

		switch (type) {
		case SYNTAX_ERROR:
			out += "�c" + ChestCleaner.main.getRB().getString(MessageID.SYNTAX_ERROR.getID()) + ": " + arg;
			break;
		case ERROR:
			out += "�c" + ChestCleaner.main.getRB().getString(MessageID.ERROR.getID()) + ": " + arg;
			break;
		case SUCCESS:
			out += "�a" + arg;
			break;
		case MISSING_PERMISSION:
			out += "�c" + ChestCleaner.main.getRB().getString(MessageID.NO_PERMISSION_FOR_COMMAND.getID()) + " ( " + arg
					+ " )";
			break;
		case UNHEADED_INFORMATION:
			out = "�7" + arg;
			break;
		default:
			throw new IllegalArgumentException();
		}

		return out;

	}

	private static final String DEFAULT_PLACEHOLDER = "%variable%";

}
