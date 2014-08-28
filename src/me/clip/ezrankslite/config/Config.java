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
package me.clip.ezrankslite.config;

import java.util.Arrays;
import java.util.List;

import me.clip.ezrankslite.EZRanksLite;

import org.bukkit.configuration.file.FileConfiguration;

public class Config {
	
	private EZRanksLite plugin;
	
	public Config(EZRanksLite instance) {
		plugin = instance;
	}

	public void loadDefaultConfiguration() {
		FileConfiguration config = plugin.getConfig();
		config.options().header("EZRanksLite version: " + plugin.getDescription().getVersion() + " Main Configuration");
		config.addDefault("debug", Boolean.valueOf(false));
		config.addDefault("auto_update", Boolean.valueOf(true));
		config.addDefault("server_name", "EZRankCraft");
		config.addDefault("ranks.enabled", true);
		config.addDefault("ranks.format_has_access", "&a%rankfrom% &bto &a%rankto%  &bcost: &f%cost%");
		config.addDefault("ranks.format_no_access", "&7%rankfrom% &cto &7%rankto%  &ccost: &7%cost%");
		config.addDefault("ranks.header", Arrays.asList(new String[] {"none"}));
		config.addDefault("ranks.footer", Arrays.asList(new String[] {"none"}));
		config.addDefault("scoreboard.enabled", true);
		config.addDefault("scoreboard.refresh_time", 30);
		config.addDefault("scoreboard.text.custom_header", Arrays.asList(new String[] {"none"}));
		config.addDefault("scoreboard.text.custom_footer", Arrays.asList(new String[] {"blank"}));
		config.addDefault("scoreboard.text.current_rank_section", "&bCurrent Rank:");
		config.addDefault("scoreboard.text.current_rank", "&f%rankfrom%");
		config.addDefault("scoreboard.text.rankup_section_singular", "&bRankup:");
		config.addDefault("scoreboard.text.rankup_section_plural", "&bRankups:");
		config.addDefault("scoreboard.text.rankup", "&e%rankto%");
		config.addDefault("scoreboard.text.rankup_cost_section", "&bCost:");
		config.addDefault("scoreboard.text.rankup_cost", "&a$&f%cost%");
		config.addDefault("scoreboard.text.balance_section", "&bBalance:");
		config.addDefault("scoreboard.text.balance", "&a$&f%balance%");
		config.addDefault("scoreboard.refresh_time", 1);
		config.addDefault("rankup_cooldown.enabled", true);
		config.addDefault("rankup_cooldown.time", 5);
		config.addDefault("money.fix_thousands", true);
		config.addDefault("money.fix_millions", true);
		config.addDefault("money.thousands_format", "k");
		config.addDefault("money.millions_format", "M");
		config.addDefault("money.billions_format", "B");
		config.addDefault("money.trillions_format", "T");
		config.options().copyDefaults(true);
		plugin.saveConfig();
	}
	
	public boolean isDebug() {
		return plugin.getConfig().getBoolean("debug");
	}
	
	public boolean checkUpdates() {
		return plugin.getConfig().getBoolean("auto_update");
	}
	
	public boolean useRankupCooldown() {
		return plugin.getConfig().getBoolean("rankup_cooldown.enabled");
	}
	
	public int rankupCooldownTime() {
		return plugin.getConfig().getInt("rankup_cooldown.time");
	}
	
	public boolean useRanks() {
		return plugin.getConfig().getBoolean("ranks.enabled");
	}
	
	public String ranksAccess() {
		return plugin.getConfig().getString("ranks.format_has_access");
	}
	public String ranksNoAccess() {
		return plugin.getConfig().getString("ranks.format_no_access");
	}
	public List<String> ranksHeader() {
		return plugin.getConfig().getStringList("ranks.header");
	}
	public List<String> ranksFooter() {
		return plugin.getConfig().getStringList("ranks.footer");
	}
	//scoreboard
	public boolean useScoreboard() {
		return plugin.getConfig().getBoolean("scoreboard.enabled");
	}
	
	public List<String> sbCustomHeader() {
		return plugin.getConfig().getStringList("scoreboard.text.custom_header");
	}
	
	public List<String> sbCustomFooter() {
		return plugin.getConfig().getStringList("scoreboard.text.custom_footer");
	}
	
	public int getScoreboardRefreshTime() {
		return plugin.getConfig().getInt("scoreboard.refresh_time");
	}
	
	private String gst(String s) {
		return plugin.getConfig().getString("scoreboard.text." + s);
	}
	
	public String sbCurrentRankSection() {
		return gst("current_rank_section");	
	}
	public String sbCurrentRank() {
		return gst("current_rank");	
	}
	public String sbRankupSectionSingular() {
		return gst("rankup_section_singular");	
	}
	public String sbRankupSectionPlural() {
		return gst("rankup_section_plural");	
	}
	public String sbRankup() {
		return gst("rankup");	
	}
	public String sbRankupCostSection() {
		return gst("rankup_cost_section");	
	}
	public String sbRankupCost() {
		return gst("rankup_cost");	
	}
	public String sbBalanceSection() {
		return gst("balance_section");	
	}
	public String sbBalance() {
		return gst("balance");	
	}
	
	//settings
	public String getServerName() {
		return plugin.getConfig().getString("server_name");
	}
	
	public boolean fixThousands() {
		return plugin.getConfig().getBoolean("money.fix_thousands");
	}
	
	public boolean fixMillions() {
		return plugin.getConfig().getBoolean("money.fix_millions");
	}
	
	public String getTFormat() {
		return plugin.getConfig().getString("money.trillions_format");
	}
	
	public String getBFormat() {
		return plugin.getConfig().getString("money.billions_format");
	}
	
	public String getMFormat() {
		return plugin.getConfig().getString("money.millions_format");
	}
	
	public String getKFormat() {
		return plugin.getConfig().getString("money.thousands_format");
	}
	
}
