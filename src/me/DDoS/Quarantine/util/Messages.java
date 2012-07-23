package me.DDoS.Quarantine.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Logger;
import me.DDoS.Quarantine.Quarantine;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

/**
 *
 * @author DDoS
 */
public class Messages {

	private static final Map<String, String> messages = new HashMap<String, String>();

	public static void load(Quarantine plugin) {

		final FileConfiguration config = new YamlConfiguration();
		final InputStream input;
		final File messageConfig = new File("plugins/Quarantine/messages.yml");

		if (messageConfig.exists()) {

			InputStream is;

			try {

				is = new FileInputStream("plugins/Quarantine/messages.yml");

			} catch (Exception ex) {

				Logger.getLogger("[Quarantine] Couldn't load the message config. Using defaults from jar.");
				is = plugin.getResource("messages.yml");

			}

			input = is;

		} else {

			Quarantine.log.info("[Quarantine] Couldn't find the message config. Using defaults from jar.");
			input = plugin.getResource("messages.yml");

		}

		try {

			config.load(input);

		} catch (Exception ex) {

			Quarantine.log.info("[Quarantine] Couldn't load the message config: " + ex.getMessage());

		}

		for (String key : config.getKeys(false)) {

			messages.put(key, config.getString(key, ""));

		}

		for (Entry<String, String> entry : messages.entrySet()) {

			final String[] splits = entry.getValue().split("\\Q%\\E");

			for (int i = 0; i < splits.length; i++) {

				for (ChatColor color : ChatColor.values()) {

					if (splits[i].equals(color.name())) {

						splits[i] = color.toString();

					}
				}
			}

			entry.setValue(QUtil.join(splits));

		}
	}

	public static String get(String key) {

		final String message = messages.get(key);
		return message == null ? "-- Message Not Found! --" : message;

	}

	public static String get(String key, String... vars) {

		String message = get(key);

		for (int i = 0; i < vars.length; i++) {

			message = message.replaceAll("\\Qvar-" + i + "\\E", vars[i]);

		}

		return message;

	}

	public static String get(String key, int... vars) {

		String[] varStrings = new String[vars.length];

		for (int i = 0; i < vars.length; i++) {
			
			varStrings[i] = Integer.toString(vars[i]);
			
		}

		return get(key, varStrings);
		
	}
}
