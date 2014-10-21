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
import me.clip.ezrankslite.multipliers.CostHandler;
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
		if (plugin.getSbOptions().getDisabledWorlds() != null && 
				plugin.getSbOptions().getDisabledWorlds().contains(p.getLocation().getWorld().getName())) {
			return;
		}
		if (hasScoreboard(p)) {
			createScoreboard(p);
		}
	}

	public int getProgress(double balance, String cost) {

		float bal = (float) balance;

		float c = Float.parseFloat(cost);

	    float percent = (100 * bal) / c;
	    
	    int progress = (int) Math.floor(percent);

	    if (progress >= 100) {
	    	return 100;
	    }
	    else if (progress < 0) {
	    	return 0;
	    }
		return progress;
		
	}

	public String getProgressBar(int progress, String barColor, String endColor) {
		String s = "\u2503";
		if (progress >= 10 && progress <= 19) {
			return endColor + s + barColor + "&l:&8&l::::::::" + endColor + s;
		} else if (progress >= 20 && progress <= 29) {
			return endColor + s + barColor + "&l::&8&l:::::::" + endColor + s;
		} else if (progress >= 30 && progress <= 39) {
			return endColor + s + barColor + "&l:::&8&l::::::" + endColor + s;
		} else if (progress >= 40 && progress <= 49) {
			return endColor + s + barColor + "&l::::&8&l:::::" + endColor + s;
		} else if (progress >= 50 && progress <= 59) {
			return endColor + s + barColor + "&l:::::&8&l::::" + endColor + s;
		} else if (progress >= 60 && progress <= 69) {
			return endColor + s + barColor + "&l::::::&8&l:::" + endColor + s;
		} else if (progress >= 70 && progress <= 79) {
			return endColor + s + barColor + "&l:::::::&8&l::" + endColor + s;
		} else if (progress >= 80 && progress <= 89) {
			return endColor + s + barColor + "&l::::::::&8&l:" + endColor + s;
		} else if (progress >= 90 && progress <= 99) {
			return endColor + s + barColor + "&l:::::::::" + endColor + s;
		} else if (progress >= 100) {
			return "/rankup";
		} else {
			return endColor + s + "&8&l:::::::::" + endColor + s;
		}
	}

	public EZScoreboard createScoreboard(Player p) {
		
		ScoreboardOptions options = plugin.getSbOptions();

		if (options == null) {
			options = plugin.loadSBOptions();
		}

		int online = Bukkit.getServer().getOnlinePlayers().length;

		OfflinePlayer pl = p;

		String name = p.getName();

		String rank = plugin.getHooks().getGroup(p);

		String bal = EZRanksLite.fixMoney(plugin.getEco().getBalance(pl));

		String nextRank = options.getNoRankups();

		String cost = "0";

		String votes = "";
		String votesReceived = "";
		String totalVotesNeeded = "";

		int pro = 0;

		String progress = "";

		String progressBar = "";
		
		String rankupPrefix = "";
		
		String rankPrefix = "";
		
		String difference = "";

		if (EZRanksLite.useVoteParty) {
			votes = VotePartyAPI.getCurrentVoteCounter() + "";
			votesReceived = VotePartyAPI.getVotes()+"";
			totalVotesNeeded = VotePartyAPI.getTotalVotesNeeded()+"";
		}

		if (plugin.getRankHandler().hasRankData(rank)
				&& plugin.getRankHandler().getRankData(rank).hasRankups()) {
			rankPrefix = plugin.getRankHandler().getRankData(rank).getPrefix();
			
			for (EZRankup r : plugin.getRankHandler().getRankData(rank)
					.getRankups()) {
				
				nextRank = r.getRank();
				
				double needed = Double.parseDouble(r.getCost());
				
				needed = CostHandler.getMultiplier(p, needed);
				
				needed = CostHandler.getDiscount(p, needed);
				
				cost = EZRanksLite.fixMoney(needed);
				pro = getProgress(plugin.getEco().getBalance(pl), String.valueOf(needed));
				rankupPrefix = r.getPrefix();
				difference = EZRanksLite.getDifference(plugin.getEco().getBalance(pl), needed);
				if (pro == 100) {
					progress = pro + "%";
					progressBar = plugin.getSbOptions().getRankup();
				} else {
					progress = pro + "%";
					progressBar = getProgressBar(pro, options.getpBarColor(), options.getpBarEndColor());
				}

			}
		}

		String title = options.getTitle().replace("%player%", name)
				.replace("%rankfrom%", rank).replace("%currentrank%", rank)
				.replace("%rankto%", nextRank).replace("%rankup%", nextRank)
				.replace("%cost%", cost).replace("%rankupcost%", cost)
				.replace("%balance%", bal).replace("%bal%", bal)
				.replace("%online%", online + "")
				.replace("%progress%", progress)
				.replace("%progressbar%", progressBar)
				.replace("%votes%", votes)
				.replace("%votesreceived%", votesReceived)
				.replace("%votesneeded%", totalVotesNeeded)
				.replace("%rankprefix%", rankPrefix)
				.replace("%rankupprefix%", rankupPrefix)
				.replace("%difference%", difference)
				.replace("%needed%", difference);
		title = plugin.getPlaceholders().getGlobalPlaceholders(title);
		title = plugin.getPlaceholders().getPlayerPlaceholders(name, title);

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
						.replace("%votes%", votes)
						.replace("%votesreceived%", votesReceived)
						.replace("%votesneeded%", totalVotesNeeded)
						.replace("%rankprefix%", rankPrefix)
						.replace("%rankupprefix%", rankupPrefix)
						.replace("%difference%", difference)
						.replace("%needed%", difference);

				send = plugin.getPlaceholders().getGlobalPlaceholders(send);
				send = plugin.getPlaceholders().getPlayerPlaceholders(name, send);

				sb.setLine(ChatColor.translateAlternateColorCodes('&', send));

			}

		}

		sb.build();
		sb.send(p);
		boards.put(p.getName(), sb);
		return sb;
	}

}
