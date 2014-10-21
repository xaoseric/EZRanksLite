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
package me.clip.ezrankslite.rankdata;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import me.clip.ezrankslite.EZRanksLite;
import me.clip.ezrankslite.events.EZRankUpEvent;
import me.clip.ezrankslite.events.EZResetEvent;

public class PlayerRankupHandler {
	
	EZRanksLite plugin;
	
	public PlayerRankupHandler(EZRanksLite instance) {
		plugin = instance;
	}
	
	public boolean resetPlayer(final Player p, final EZRank base) {
		
		if (!base.allowReset()) {
			return false;
		}
		
		OfflinePlayer pl = p;
		
		double balance = plugin.getEco().getBalance(pl);
		double needed = Double.parseDouble(base.getResetCost());
		
		if (balance < needed) {
			//hmmm why am I checking this twice
			return false;
		}
		
		List<String> commands = base.getResetCommands();
		
		if (commands == null || commands.isEmpty()) {
			plugin.debug(true, "There were no reset_commands for rank " 
			+ base.getRank() + "! Players will not be reset!");
			plugin.sms(p, "&cThis reset is not setup correctly! Please contact an admin!");
			return false;
		}
		
		for (String cmd : commands) {
			if (cmd.startsWith("ezmsg") || cmd.startsWith("ezmessage")) {
				plugin.sms(p, cmd.replace("%rankfrom%", base.getRank())
						.replace("%player%", p.getName())
						.replace("%world%", p.getWorld().getName())
						.replace("%rankprefix%", base.getPrefix())
						.replace("%rankfrom%", base.getRank())
						.replace("%balance%", EZRanksLite.fixMoney(balance))
						.replace("%cost%", EZRanksLite.fixMoney(needed))
						.replace("ezmsg ", "")
						.replace("ezmessage ", ""));
			}
			else if (cmd.startsWith("ezbroadcast") || cmd.startsWith("ezbcast")) {
				plugin.bcast(cmd.replace("%rankfrom%", base.getRank())
						.replace("%player%", p.getName())
						.replace("%world%", p.getWorld().getName())
						.replace("%rankprefix%", base.getPrefix())
						.replace("%rankfrom%", base.getRank())
						.replace("%balance%", EZRanksLite.fixMoney(balance))
						.replace("%cost%", EZRanksLite.fixMoney(needed))
						.replace("ezbroadcast ", "")
						.replace("ezbcast ", ""));
			}
			else {
			Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), cmd.replace("%rankfrom%", base.getRank())
					.replace("%player%", p.getName())
					.replace("%world%", p.getWorld().getName())
					.replace("%rankprefix%", base.getPrefix())
					.replace("%rankfrom%", base.getRank())
					.replace("%balance%", balance + "")
					.replace("%cost%", needed + ""));	
			}
		}	
		EZResetEvent resetEvent = new EZResetEvent(p, base.getRank(), base.getRank(), base.getResetCost());
		Bukkit.getServer().getPluginManager().callEvent(resetEvent);
		plugin.getEco().withdrawMoney(needed, pl);
		if (plugin.useScoreboard()) {
			if (plugin.getBoardhandler().hasScoreboard(p)) {
				plugin.getBoardhandler().updateScoreboard(p);
			}
			
		}
		return true;
	}
	
	public void rankupPlayer(final Player p, final EZRank base, final EZRankup r, final double cost) {
		
		OfflinePlayer pl = p;
		
		double balance = plugin.getEco().getBalance(pl);

		
		List<String> commands = r.getCommands();
		
		if (commands == null || commands.isEmpty()) {
			plugin.debug(true, "There were no rankup_commands for rankup " 
			+ base.getRank() + " to " 
					+ r.getRank() + "! The player will not be ranked up!");
			plugin.sms(p, "&cThis rankup is not setup correctly! Please contact an admin!");
			return;
		}
		
		for (String cmd : commands) {
			if (cmd.startsWith("ezmsg") || cmd.startsWith("ezmessage")) {
				plugin.sms(p, cmd.replace("%rankfrom%", base.getRank())
						.replace("%rankto%", r.getRank())
						.replace("%player%", p.getName())
						.replace("%rankprefix%", base.getPrefix())
						.replace("%rankupprefix%", r.getPrefix())
						.replace("%world%", p.getWorld().getName())
						.replace("%balance%", EZRanksLite.fixMoney(balance))
						.replace("%cost%", EZRanksLite.fixMoney(cost))
						.replace("ezmsg ", "")
						.replace("ezmessage ", ""));
			}
			else if (cmd.startsWith("ezbroadcast") || cmd.startsWith("ezbcast")) {
				plugin.bcast(cmd.replace("%rankfrom%", base.getRank())
						.replace("%rankto%", r.getRank())
						.replace("%player%", p.getName())
						.replace("%rankprefix%", base.getPrefix())
						.replace("%rankupprefix%", r.getPrefix())
						.replace("%world%", p.getWorld().getName())
						.replace("%balance%", EZRanksLite.fixMoney(balance))
						.replace("%cost%", EZRanksLite.fixMoney(cost))
						.replace("ezbroadcast ", "")
						.replace("ezbcast ", ""));
			}
			else if (cmd.startsWith("ezeffect") || cmd.startsWith("ezeffects")) {
				String[] args = cmd.split(" ");
				
				if (args.length < 2) {
					continue;
				}
				
				String type = args[1];
				if (type.equalsIgnoreCase("firework") || type.equalsIgnoreCase("fireworks")) {
					plugin.getEffectsHandler().fireworks(p.getLocation());
				}
				else if (type.equalsIgnoreCase("spawner") || type.equalsIgnoreCase("flames")) {
					plugin.getEffectsHandler().flames(p.getLocation());
				}
				else if (type.equalsIgnoreCase("smoke") || type.equalsIgnoreCase("smokeeffect")) {
					plugin.getEffectsHandler().smoke(p.getLocation());
				}
				else if (type.equalsIgnoreCase("ender") || type.equalsIgnoreCase("endereffect")) {
					plugin.getEffectsHandler().ender(p.getLocation());
				}	
				else if (type.equalsIgnoreCase("potion") || type.equalsIgnoreCase("potionbreak")) {
					plugin.getEffectsHandler().potionBreak(p.getLocation());
				}
				else if (type.equalsIgnoreCase("explosion") || type.equalsIgnoreCase("explosioneffect") || type.equalsIgnoreCase("tnt")) {
					plugin.getEffectsHandler().explosion(p.getLocation());
				}
				
			}
			else {
			Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), cmd.replace("%rankfrom%", base.getRank())
					.replace("%rankto%", r.getRank())
					.replace("%player%", p.getName())
					.replace("%world%", p.getWorld().getName())
					.replace("%balance%", balance + "")
					.replace("%cost%", EZRanksLite.fixMoney(cost)));
			}
		}	
		
		EZRankUpEvent rankupEvent = new EZRankUpEvent(p, base.getRank(), r.getRank(), r.getCost());
		Bukkit.getServer().getPluginManager().callEvent(rankupEvent);
		
		plugin.getEco().withdrawMoney(cost, pl);
		
		if (plugin.useScoreboard()) {
			if (plugin.getBoardhandler().hasScoreboard(p)) {
				plugin.getBoardhandler().updateScoreboard(p);
			}
		}
		
	}

}
