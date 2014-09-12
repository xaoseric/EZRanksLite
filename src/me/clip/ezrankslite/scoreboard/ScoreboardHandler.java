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
import me.clip.voteparty.VotePartyAPI;

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

		int online = Bukkit.getServer().getOnlinePlayers().length;

		OfflinePlayer pl = p;

		String name = p.getName();

		String rank = plugin.getVault().getMainGroup(p);

		String bal = EZRanksLite.fixMoney(plugin.getEco().getBalance(pl),
				String.valueOf(plugin.getEco().getBalance(pl)));

		String nextRank = "&cnone available";

		String cost = "0";

		String votes = "";

		if (EZRanksLite.useVoteParty) {
			votes = VotePartyAPI.getCurrentVoteCounter() + "";
		}

		if (plugin.getRankHandler().hasRankData(rank)
				&& plugin.getRankHandler().getRankData(rank).hasRankups()) {
			for (EZRankup r : plugin.getRankHandler().getRankData(rank)
					.getRankups()) {
				nextRank = r.getRank();
				cost = EZRanksLite.fixMoney(Double.parseDouble(r.getCost()),
						r.getCost());
			}
		}

		ScoreboardOptions options = plugin.getSbOptions();

		if (options == null) {
			options = plugin.loadSBOptions();
		}

		EZScoreboard sb = new EZScoreboard(
				ChatColor.translateAlternateColorCodes('&', options.getTitle()));

		for (String s : options.getText()) {

			if (s.equalsIgnoreCase("blank")) {
				sb.blank();
			} else {
				String send = s.replace("%player%", name)
						.replace("%rankfrom%", rank)
						.replace("%currentrank%", rank)
						.replace("%rankto%", nextRank)
						.replace("%rankup%", nextRank).replace("%cost%", cost)
						.replace("%rankupcost%", cost)
						.replace("%balance%", bal).replace("%bal%", bal)
						.replace("%online%", online + "")
						.replace("%votes%", votes);
				sb.setLine(ChatColor.translateAlternateColorCodes('&', send));
			}

		}

		sb.create();
		sb.send(p);
		boards.put(p.getName(), sb);
		return sb;
	}

}
