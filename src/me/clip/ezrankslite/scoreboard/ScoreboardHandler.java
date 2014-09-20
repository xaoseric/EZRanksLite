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

	public int getProgress(double balance, String cost) {

		int bal = (int) balance;

		int rcost = Integer.parseInt(cost);

		int progress = (int) Math.round(bal * 100.0 / rcost);

		if (progress >= 100) {
			double c = Double.parseDouble(cost);
			
			if (balance < c) {
				return 99;
			}
			return 100;
		}
		return progress;
	}

	public String getProgressBar(int progress) {
		String block = "\u258c";
		String s = "\u2503";
		if (progress >= 5 && progress <= 20) {
			return "&c"+s+"&f"+block+"    &c"+s;
		} else if (progress >= 21 && progress <= 40) {
			return "&c"+s+"&f"+block+block+"   &c"+s;
		} else if (progress >= 41 && progress <= 60) {
			return "&c"+s+"&f"+block+block+block+"  &c"+s;
		} else if (progress >= 61 && progress <= 80) {
			return "&c"+s+"&f"+block+block+block+block+" &c"+s;
		} else if (progress >= 81 && progress <= 99) {
			return "&c"+s+"&f"+block+block+block+block+block+"&c"+s;
		}
		else if (progress >= 100) {
			return "/rankup";
		}
		else {
			return "&c"+s+"&f     &c"+s;
		}
	}

	public EZScoreboard createScoreboard(Player p) {

		int online = Bukkit.getServer().getOnlinePlayers().length;

		OfflinePlayer pl = p;

		String name = p.getName();

		String rank = plugin.getVault().getMainGroup(p);

		String bal = EZRanksLite.fixMoney(plugin.getEco().getBalance(pl),
				String.valueOf(plugin.getEco().getBalance(pl)));

		String nextRank = "&cnone";

		String cost = "0";

		String votes = "";

		int pro = 0 ;
		
		String progress = "";

		String progressBar = "";

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
				pro = getProgress(plugin.getEco().getBalance(pl), cost);
				
				if (pro == 100) {
					progress = "/rankup";
					progressBar = "/rankup";
				}
				else {
					progress = pro+"%";
					progressBar = getProgressBar(pro);
				}
				
			}
		}

		ScoreboardOptions options = plugin.getSbOptions();

		if (options == null) {
			options = plugin.loadSBOptions();
		}
		
		String title = options.getTitle().replace("%player%", name)
				.replace("%rankfrom%", rank)
				.replace("%currentrank%", rank)
				.replace("%rankto%", nextRank)
				.replace("%rankup%", nextRank).replace("%cost%", cost)
				.replace("%rankupcost%", cost)
				.replace("%balance%", bal).replace("%bal%", bal)
				.replace("%online%", online + "")
				.replace("%progress%", progress)
				.replace("%progressbar%", progressBar)
				.replace("%votes%", votes);

		EZScoreboard sb = new EZScoreboard(
				ChatColor.translateAlternateColorCodes('&', title));

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
						.replace("%progress%", progress)
						.replace("%progressbar%", progressBar)
						.replace("%votes%", votes);

				sb.setLine(ChatColor.translateAlternateColorCodes('&', send));

			}

		}

		sb.build();
		sb.send(p);
		boards.put(p.getName(), sb);
		return sb;
	}

}
