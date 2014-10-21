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
package me.clip.ezrankslite;

import java.util.Collection;

import me.clip.ezrankslite.rankdata.EZRank;
import me.clip.ezrankslite.rankdata.EZRankup;

import org.bukkit.entity.Player;

public class EZAPI {

	private EZRanksLite plugin;
	
	public EZAPI(EZRanksLite instance) {
		plugin = instance;
	}
	
	public String getFormattedBalance(Player p) {
		double bal = getEconBalance(p);
		return EZRanksLite.fixMoney(bal);
	}
	
	public double getEconBalance(Player p) {
		return plugin.getEco().getBalance(p);
	}
	
	public String[] getPermissionsGroups(Player p) {
		return plugin.getHooks().getGroups(p);
	}
	
	public String[] getServerGroups() {
		return plugin.getHooks().getServerGroups();
	}
	
	/**
	 * set a custom placeholder for a specific player in the EZRanksLite scoreboard.
	 * if a custom placeholder is used for a specific player, it must be set for every player if the
	 * identifier is used in the ezrankslite scoreboard.
	 * Any player who does not have a value set will return a blank value
	 * @param playername player name to set the placeholder value for
	 * @param identifier for the ezrankslite scoreboard config to recognize where to put the value on the
	 * scoreboard. identifier must start and end with %
	 * @param value of identifier
	 */
	public void setPlayerPlaceholder(String playername, String identifier, String value) {
		plugin.getPlaceholders().setPlayerPlaceholder(playername, identifier, value);
	}
	
	@Deprecated
	public void setCustomPlaceholder(String playername, String identifier, String value) {
		plugin.getPlaceholders().setPlayerPlaceholder(playername, identifier, value);
	}
	
	
	/**
	 * set a custom placeholder for a specific player in the EZRanksLite scoreboard.
	 * if a custom placeholder is used for a specific player, it must be set for every player if the
	 * identifier is used in the ezrankslite scoreboard.
	 * Any player who does not have a value set will return a blank value
	 * @param p player to set the placeholder value for
	 * @param identifier for the ezrankslite scoreboard config to recognize where to put the value on the
	 * scoreboard. identifier must start and end with %
	 * @param value of identifier
	 */
	public void setPlayerPlaceholder(Player p, String identifier, String value) {
		plugin.getPlaceholders().setPlayerPlaceholder(p.getName(), identifier, value);
	}
	
	/**
	 * set a custom placeholder for all players in the EZRanksLite scoreboard.
	 * @param identifier for the ezrankslite scoreboard config to recognize where to put the value on the
	 * scoreboard. identifier must start and end with %
	 * @param value of identifier
	 */
	public void setGlobalPlaceholder(String identifier, String value) {
		plugin.getPlaceholders().setGlobalPlaceholder(identifier, value);
	}
	

	/**
	 * get the current permission rank of a player
	 * @param p player to get the rank for
	 * @return current permissions group of a player
	 */
	public String getCurrentRank(Player p) {
		return plugin.getHooks().getGroup(p);
	}

	/**
	 * Get a collection of all rankups for a players current rank
	 * @param p player to get the rankups for
	 * @return collection of EZRankup objects which the player currently has access to
	 * based on the permissions group they are currently in
	 * These objects hold information such as the commands that are executed for that specific
	 * rankup, the rank the rankup lets the player go to, the cost to rankup, and other information
	 * will return null if player does not have any rankups
	 */
	public Collection<EZRankup> getRankups(Player p) {
		String rank = getCurrentRank(p);
		if (plugin.getRankHandler().getRankData(rank) != null 
				&& plugin.getRankHandler().getRankData(rank).hasRankups()) {
			return plugin.getRankHandler().getRankData(rank).getRankups();
		}
		return null;
	}
	
	/**
	 * Get the EZRank data object that was created from the rankups.yml
	 * for a specific players rank
	 * @param p player to get the EZRank object for
	 * @return get the rankdata object that was created from the rankups.yml file 
	 * relative to the players current permission group
	 * This EZRank object holds all the options/rankups for that specific 
	 * permissions group
	 * will return null if the players current rank was not loaded/created from the rankups.yml
	 */
	public EZRank getRankData(Player p) {
		String rank = getCurrentRank(p);
		if (plugin.getRankHandler().getRankData(rank) != null) {
			
			return plugin.getRankHandler().getRankData(rank);	
		}
		return null;
	}
	
	/**
	 * Get the EZRank data object that was created from the rankups.yml
	 * for a specific permissions group
	 * @param permissionsGroup rank to get the EZRank object for
	 * @return get the rankdata object that was created from the rankups.yml file 
	 * relative to the permission plugin group specified
	 * This EZRank object holds all the options/rankups for that specific 
	 * permissions group
	 * will return null if the permissionsGroup was not loaded/created from the rankups.yml
	 */
	public EZRank getRankData(String permissionsGroup) {
		if (plugin.getRankHandler().getRankData(permissionsGroup) != null) {
			return plugin.getRankHandler().getRankData(permissionsGroup);	
		}
		return null;
	}
	
	
}
