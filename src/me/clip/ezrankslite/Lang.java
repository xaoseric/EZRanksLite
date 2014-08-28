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

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;

public enum Lang {
	/**
	 * No permission
	 */
	NO_PERMISSION("no_permission", "&4You don't have permission to do that!"),
	/**
	 * No Rankups
	 */
	RANKS_NO_RANKUPS_LOADED("ranks_no_rankups_loaded", "&cThere are no rankups setup!"),
	/**
	 * Ranks disabled
	 */
	RANKS_DISABLED("ranks_disabled", "&c/ranks is disabled in the config.yml!"),
	/**
	 * Rankup on cooldown
	 */
	RANKUP_ON_COOLDOWN("rankup_on_cooldown", "&cYou need to wait &f{0} &cseconds to rankup again!"),
	/**
	 * Rankup no rankups available
	 */
	RANKUP_NO_RANKUPS_AVAILABLE("rankup_no_rankups_available", "&cRank &f{0} &cdoes not have any rankups available!"),
	/**
	 * Rankup disabled
	 */
	RANKUP_DISABLED("rankup_disabled", "&cThe rankup to &f{0} &cis currently disabled!"),
	/**
	 * Rankup confirmation
	 */
	RANKUP_CONFIRMATION("rankup_confirmation", "&bAre you sure you want to rankup to &f{0} &bfor &a${1}&b? Type &e/rankup &bagain to confirm!"),
	/**
	 * Rankup confirmation different rankup command used
	 */
	RANKUP_CONFIRMATION_MULTIPLE_RANKUPS("rankup_confirmation_multiple_rankups", "&bAre you sure you want to rankup to &f{0} &bfor &a${1}&b? Type &e/rankup {0} &bagain to confirm!"),
	/**
	 * Rankup confirmation different rankup command used
	 */
	RANKUP_CONFIRMATION_INCORRECT_RANKUP("rankup_confirmation_incorrect_rankup", "&cYou need to wait for your rankup confirmation for &f{0} &cto expire before you can rankup to &f{1}&c!"),
	/**
	 * Rankup multiple rankups available
	 */
	RANKUP_MULTIPLE_RANKUPS("rankup_multiple_rankups", "&aYou have &e{0} &arankups available at rank &f{1}"),
	/**
	 * Rankup multiple rankups commands message
	 */
	RANKUP_MULTIPLE_RANKUPS_LIST("rankup_multiple_rankups_list", "&b/rankup {0}  &aCost: &f{1}"),
	/**
	 * Rankup incorrect rank argument
	 */
	RANKUP_INCORRECT_RANK_ARGUMENT("rankup_incorrect_rankname_argument", "&f{0} &cis not a valid rankup name! type /rankup!"),
	/**
	 * Help header
	 */
	HELP_HEADER("help_header", "{0} &fRankup Help"),
	/**
	 * Help rankup command
	 */
	HELP_RANKUP("help_rankup", "&b/rankup &7- &frankup to your next rank"),
	/**
	 * Help ranks command
	 */
	HELP_RANKS("help_ranks", "&b/ranks &7- &fview ranks list"),
	/**
	 * Help rank reset
	 */
	HELP_RANK_RESET("help_rank_reset", "&b/rankup reset &7- &freset your rank"),
	/**
	 * Help rank reset
	 */
	HELP_SCOREBOARD_TOGGLE("help_scoreboard_toggle", "&b/sbtoggle &7- &ftoggle scoreboard on/off"),
	/**
	 * Help rank reset
	 */
	HELP_SCOREBOARD_REFRESH("help_scoreboard_refresh", "&b/sbrefresh &7- &frefresh scoreboard stats"),
	/**
	 * Help rank reset
	 */
	RESET_NOT_ALLOWED("reset_not_allowed", "&cYou are not allowed to use &7/rankup reset &cat your current rank!"),
	/**
	 * Help rank reset
	 */
	RESET_NOT_ENOUGH_MONEY("reset_not_enough_money", "&cYou need &a$&f{0} &cto reset your rank! You only have &a$&f{1}&c!"),
	/**
	 * Help rank reset
	 */
	RESET_CONFIRMATION_FREE("reset_confirmation_free", "&bAre you sure you want to reset your rank? Use &7/rankup reset &bagain to confirm!"),
	/**
	 * Help rank reset
	 */
	RESET_CONFIRMATION_COST("reset_confirmation_has_cost", "&bAre you sure you want to reset your rank for &a${0}&b? Use &7/rankup reset &aagain to confirm!"),
	/**
	 * Help rank reset
	 */
	SCOREBOARD_DISABLED("ezscoreboard_disabled", "&cThe scoreboard is currently disabled!"),
	;

	private String path, def;
	private static FileConfiguration LANG;

	Lang(final String path, final String start) {
		this.path = path;
		this.def = start;
	}

	public static void setFile(final FileConfiguration config) {
		LANG = config;
	}

	public String getDefault() {
		return this.def;
	}

	public String getPath() {
		return this.path;
	}
	
	public String getConfigValue(final String[] args) {
		String value = ChatColor.translateAlternateColorCodes('&',
				LANG.getString(this.path, this.def));

		if (args == null)
			return value;
		else {
			if (args.length == 0)
				return value;

			for (int i = 0; i < args.length; i++) {
				value = value.replace("{" + i + "}", args[i]);
			}
		}

		return value;
	}
}
