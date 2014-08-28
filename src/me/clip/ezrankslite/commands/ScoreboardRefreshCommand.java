/* This file is a class of EZRanksLite
 * @author extended_clip
 * 
 * 
 * EZRanksLite is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * 
 * EZRanksLite is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * 
 */
package me.clip.ezrankslite.commands;

import me.clip.ezrankslite.EZRanksLite;
import me.clip.ezrankslite.Lang;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ScoreboardRefreshCommand implements CommandExecutor {

	private EZRanksLite plugin;

	public ScoreboardRefreshCommand(EZRanksLite instance) {
		plugin = instance;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label,
			String[] args) {
		
		if (!plugin.useScoreboard()) {
			plugin.sms(sender,
					Lang.SCOREBOARD_DISABLED.getConfigValue(null));
			return true;
		}

		if (!(sender instanceof Player)) {

			if (args.length > 0) {

				@SuppressWarnings("deprecation")
				Player target = Bukkit.getServer().getPlayer(args[0]);

				if (target == null) {
					plugin.sms(sender, args[0] + " &cis not online!");
					return true;
				}

				if (plugin.getBoardhandler().hasScoreboard(target)) {
					plugin.getBoardhandler().updateScoreboard(target);
					plugin.sms(sender, "&f" + target.getName()
							+ "s &bscoreboard has been updated&b.");
				} else {
					plugin.getBoardhandler().createScoreboard(target);
					plugin.sms(sender, "&f" + target.getName()
							+ "s &bscoreboard has been toggled &aon&b.");
				}

			}
			else {
				plugin.sms(sender, "&cIncorrect usage! use &f/sbrefresh <player>");
			}

			return true;
		}

		Player p = (Player) sender;
		if (args.length == 0) {
			if (plugin.getBoardhandler().hasScoreboard(p)) {
				plugin.getBoardhandler().updateScoreboard(p);
				plugin.sms(sender, "&bYour scoreboard has been updated.");
			} else {
				plugin.getBoardhandler().createScoreboard(p);
				plugin.sms(sender, "&bYour scoreboard has been toggled &aon&b.");
			}
			return true;
		} else if (args.length > 0) {

			if (!p.hasPermission("ezranks.admin.scoreboard")) {
				plugin.sms(sender, Lang.NO_PERMISSION.getConfigValue(null));
				return true;
			}

			@SuppressWarnings("deprecation")
			Player target = Bukkit.getServer().getPlayer(args[0]);

			if (target == null) {
				plugin.sms(sender, args[0] + " &cis not online!");
				return true;
			}

			if (plugin.getBoardhandler().hasScoreboard(target)) {
				plugin.getBoardhandler().updateScoreboard(target);
				plugin.sms(target, "&bYour scoreboard has been updated by &f"
						+ p.getName() + "&b.");
				plugin.sms(p, "&f" + target.getName()
						+ "s &bscoreboard has been updated&b.");
			} else {
				plugin.getBoardhandler().createScoreboard(target);
				plugin.sms(target, "&bYour scoreboard has toggled &aon by &f"
						+ p.getName() + "&b.");
				plugin.sms(p, "&f" + target.getName()
						+ "s &bscoreboard has been toggled &aon&b.");
			}

		}
		return true;
	}

}
