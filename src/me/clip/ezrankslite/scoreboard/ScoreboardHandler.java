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
package me.clip.ezrankslite.scoreboard;

import java.util.HashMap;

import me.clip.ezrankslite.EZRanksLite;
import me.clip.ezrankslite.rankdata.EZRankup;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

public class ScoreboardHandler {

	private EZRanksLite plugin;

	public ScoreboardHandler(EZRanksLite instance) {
		plugin = instance;
	}

	protected HashMap<String, EZScoreboard> boards = new HashMap<String, EZScoreboard>();

	public boolean hasScoreboard(Player p) {
		return boards.containsKey(p.getName())
				&& boards.get(p.getName()) != null;
	}

	public boolean hasScoreboard(String p) {
		return boards.containsKey(p) && boards.get(p) != null;
	}

	public EZScoreboard getEZBoard(String player) {
		if (hasScoreboard(player)) {
			return boards.get(player);
		}
		return null;
	}

	public void removeScoreboard(Player p) {
		if (hasScoreboard(p)) {
			EZScoreboard board = boards.get(p.getName());
			board.reset();
			boards.remove(p.getName());
			p.setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());
		}
	}

	public void updateScoreboard(Player p) {
		if (hasScoreboard(p)) {
			createScoreboard(p);
		}
	}

	public EZScoreboard createScoreboard(Player p) {
		
		OfflinePlayer pl = p;
		
		String rank = plugin.getVault().getMainGroup(p);
		ScoreboardOptions options = plugin.getSbOptions();
		if (options == null) {
			options = plugin.loadSBOptions();
		}
		EZScoreboard scoreboard = new EZScoreboard(
				ChatColor.translateAlternateColorCodes('&',
						plugin.getServername()));
		if (options.getCustomHeader() == null
				|| options.getCustomHeader().isEmpty()) {
		} else {
			for (String s : options.getCustomHeader()) {
				if (s.equalsIgnoreCase("blank")) {
					scoreboard.blank();
				} else if (s.equalsIgnoreCase("none")
						|| s.equalsIgnoreCase("off")) {
					continue;
				} else {
					scoreboard.setLine(ChatColor
							.translateAlternateColorCodes(
									'&',
									s.replace( "%online%", Bukkit.getServer().getOnlinePlayers().length + "")
										.replace("%player%", p.getName())
										.replace("%balance%", plugin.getEco().getBalance(pl) + "")));
				}

			}
		}
		if (options.getCurrentRankSection().isEmpty()
				|| options.getCurrentRankSection().equalsIgnoreCase("none")
				|| options.getCurrentRankSection().equalsIgnoreCase("off")) {
		} else {
			if (options.getCurrentRankSection().equalsIgnoreCase("line")
					|| options.getCurrentRankSection().equalsIgnoreCase("blank")) {
				scoreboard.blank();
			}
			else {
			scoreboard.setLine(ChatColor.translateAlternateColorCodes('&',
					options.getCurrentRankSection()));
			}
		}
		if (options.getCurrentRank().isEmpty()
				|| options.getCurrentRank().equalsIgnoreCase("none")
				|| options.getCurrentRank().equalsIgnoreCase("off")) {
		} else {
			String current = options.getCurrentRank().replace("%rankfrom%",
					rank);
			scoreboard.setLine(ChatColor.translateAlternateColorCodes('&',
					current));
		}
		scoreboard.blank();

		if (plugin.getRankHandler().hasRankData(rank)) {
			if (plugin.getRankHandler().getRankData(rank).getRankups().size() == 1) {
				if (options.getRankupSectionSingular().isEmpty()
						|| options.getRankupSectionSingular().equalsIgnoreCase(
								"none")
						|| options.getRankupSectionSingular().equalsIgnoreCase(
								"off")) {
				} else {
					scoreboard.setLine(ChatColor.translateAlternateColorCodes(
							'&', options.getRankupSectionSingular()));
				}

			} else {
				if (options.getRankupSectionPlural().isEmpty()
						|| options.getRankupSectionPlural().equalsIgnoreCase(
								"none")
						|| options.getRankupSectionPlural().equalsIgnoreCase(
								"off")) {
				} else {
					scoreboard.setLine(ChatColor.translateAlternateColorCodes(
							'&', options.getRankupSectionPlural()));
				}
			}
			for (EZRankup rankup : plugin.getRankHandler().getRankData(rank)
					.getRankups()) {
				if (options.getRankup().isEmpty()
						|| options.getRankup().equalsIgnoreCase("none")
						|| options.getRankup().equalsIgnoreCase("off")) {
				} else {
					String rankto = options.getRankup().replace("%rankto%",
							rankup.getRank());
					scoreboard.setLine(ChatColor.translateAlternateColorCodes(
							'&', rankto));
					
				}
				scoreboard.blank();
				if (options.getCostSection().isEmpty()
						|| options.getCostSection().equalsIgnoreCase("none")
						|| options.getCostSection().equalsIgnoreCase("off")) {
				} else {
					scoreboard.setLine(ChatColor.translateAlternateColorCodes(
							'&', options.getCostSection()));
				}
				if (options.getCost().isEmpty()
						|| options.getCost().equalsIgnoreCase("none")
						|| options.getCost().equalsIgnoreCase("off")) {
				} else {
					String cost = options.getCost().replace(
							"%cost%",
							EZRanksLite.fixMoney(
									Double.parseDouble(rankup.getCost()),
									rankup.getCost()));
					scoreboard.setLine(ChatColor.translateAlternateColorCodes(
							'&', cost));
				}
			}
		} else {
			scoreboard.setLine(ChatColor.translateAlternateColorCodes('&',
					"&cYou have no"));
			scoreboard.setLine(ChatColor.translateAlternateColorCodes('&',
					"&crankups"));
		}
		scoreboard.blank();
		if (options.getBalanceSection().isEmpty()
				|| options.getBalanceSection().equalsIgnoreCase("none")
				|| options.getBalanceSection().equalsIgnoreCase("off")) {
		} else {
			scoreboard.setLine(ChatColor.translateAlternateColorCodes('&',
					options.getBalanceSection()));
		}
		if (options.getBalance().isEmpty()
				|| options.getBalance().equalsIgnoreCase("none")
				|| options.getBalance().equalsIgnoreCase("off")) {
		} else {
			String bal = options.getBalance().replace(
					"%balance%",
					EZRanksLite.fixMoney(plugin.getEco().getBalance(pl),
							String.valueOf(plugin.getEco().getBalance(pl))));
			scoreboard.setLine(ChatColor.translateAlternateColorCodes('&', bal));
		}

		if (options.getCustomFooter() == null
				|| options.getCustomFooter().isEmpty()) {
		} else {
			for (String s : options.getCustomFooter()) {
				if (s.equalsIgnoreCase("blank")) {
					scoreboard.blank();
				} else if (s.equalsIgnoreCase("none")
						|| s.equalsIgnoreCase("off")) {
					continue;
				} else {
					scoreboard.setLine(ChatColor.translateAlternateColorCodes(
							'&', s.replace( "%online%", Bukkit.getServer().getOnlinePlayers().length + "")
							.replace("%player%", p.getName())
							.replace("%balance%", plugin.getEco().getBalance(pl) + "")));
				}

			}
		}
		scoreboard.create();
		scoreboard.send(p);
		boards.put(p.getName(), scoreboard);
		return scoreboard;
	}

}
