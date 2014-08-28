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

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;

import me.clip.ezrankslite.EZRanksLite;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class RankupFile {


	private EZRanksLite plugin;
	private FileConfiguration dataConfig = null;
	private File dataFile = null;

	public RankupFile(EZRanksLite instance) {
		this.plugin = instance;
	}

	public boolean reload() {
		boolean firstLoad;
		if (this.dataFile == null) {
			this.dataFile = new File(this.plugin.getDataFolder(), "rankups.yml");
		}
		if (this.dataFile.exists()) {
			firstLoad = false;
		} else {
			firstLoad = true;
		}

		this.dataConfig = YamlConfiguration.loadConfiguration(this.dataFile);
		return firstLoad;
	}

	public FileConfiguration load() {
		if (this.dataConfig == null) {
			reload();
		}
		return this.dataConfig;
	}

	public void save() {
		if ((this.dataConfig == null) || (this.dataFile == null))
			return;
		try {
			load().save(this.dataFile);
		} catch (IOException ex) {
			this.plugin.getLogger().log(Level.SEVERE,
					"Could not save to " + this.dataFile, ex);
		}
	}

	public void createRankSection(String rank, String rankTo, String cost) {
		String prefix = rank + "." + rankTo + ".";
		if (!this.dataConfig.contains(rank + ".options.allow_reset")) {
			this.dataConfig.set(rank + ".options.allow_reset", Boolean.valueOf(false));
		}
		if (!this.dataConfig.contains(rank + ".options.reset_cost")) {
			this.dataConfig.set(rank + ".options.reset_cost", "0");
		}
		if (!this.dataConfig.contains(rank + ".options.ranks_display_order")) {
			if (plugin.getRankHandler().getLoadedRanks() != null) {
				this.dataConfig.set(rank + ".options.ranks_display_order", (plugin.getRankHandler().getLoadedRanks().size()+1));
			}
			this.dataConfig.set(rank + ".options.ranks_display_order", 1);
		}
		if (!this.dataConfig.contains(rank + ".options.reset_commands")) {
			this.dataConfig.set(rank + ".options.reset_commands", Arrays.asList(new String[] { "ezbroadcast %player% reset their rank to Guest!", "ezmsg You will need to start over!!", "pex user %player% group set Guest", "clearinventory %player%" }));
		}
		this.dataConfig.set(prefix + "active", Boolean.valueOf(false));
		this.dataConfig.set(prefix + "confirm_to_rankup", Boolean.valueOf(true));
		this.dataConfig.set(prefix + "cost", cost);

		this.dataConfig.set(prefix + "requirement_message",
				Arrays.asList(new String[] { "Your current rank is %rankfrom%", "You need $%cost% to rank up to %rankto%",
						"Your balance is $%balance%"}));
		this.dataConfig.set(prefix + "rankup_commands",
				Arrays.asList(new String[] { "ezbroadcast %player% ranked up to %rankto%!", "ezmsg &eCongrats on your rankup!", "ezmsg enjoy the fireworks!", "ezeffect fireworks", "spawn %player%", "pex user %player% group add %rankto%" , "pex user %player% group remove %rankfrom%"}));
		save();
	}
	
	public boolean containsEntry(String path) {
		return this.dataConfig.contains(path);
	}

	public Set<String> getRanks() {
		return this.dataConfig.getKeys(false);
	}
	
	public boolean allowReset(String rank) {
		return this.dataConfig.getBoolean(rank + ".options.allow_reset");
	}
	
	public List<String> resetCommands(String rank) {
		return this.dataConfig.getStringList(rank + ".options.reset_commands");
	}
	
	public int displayOrder(String rank) {
		return this.dataConfig.getInt(rank + ".options.ranks_display_order");
	}
	
	public String resetCost(String rank) {
		return this.dataConfig.getString(rank + ".options.reset_cost");
	}

	public Set<String> getRankupSections(String rank) {
		return this.dataConfig.getConfigurationSection(rank).getKeys(false);
	}

	public boolean isActive(String rank, String toRank) {
		return this.dataConfig.getBoolean(rank + "." + toRank + ".active");
	}
	
	public boolean confirmToRankup(String rank, String toRank) {
		return this.dataConfig.getBoolean(rank + "." + toRank + ".confirm_to_rankup");
	}

	public String getCost(String rank, String toRank) {
		return this.dataConfig.getString(rank + "." + toRank + ".cost");
	}

	public List<String> getRequirementMessage(String rank, String toRank) {
		return this.dataConfig.getStringList(rank + "." + toRank
				+ ".requirement_message");
	}

	public List<String> getRankupCommands(String rank, String toRank) {
		return this.dataConfig.getStringList(rank + "." + toRank
				+ ".rankup_commands");
	}
	
	public boolean checkValidRankOptions(String rank) {
		boolean missingEntry = false;
		if (containsEntry(rank + ".options.allow_reset") != true) {
			missingEntry = true;
			
			dataConfig.set(rank + ".options.allow_reset", false);
		}
		if (containsEntry(rank + ".options.reset_commands") != true) {
			missingEntry = true;
			dataConfig.set(rank + ".options.reset_commands", "GroupNameHere");
		}
		if (containsEntry(rank + ".options.reset_cost") != true) {
			missingEntry = true;
			dataConfig.set(rank + ".options.reset_cost", "0");
		}
		if (containsEntry(rank + ".options.ranks_display_order") != true) {
			missingEntry = true;
			dataConfig.set(rank + ".options.ranks_display_order", 1);
		}
		return missingEntry;
	}

	public boolean checkValidRankup(String rank, String rankTo) {
		
		boolean missingEntry = false;
		
		if (containsEntry(rank + "." + rankTo + ".active") != true) {
			missingEntry = true;
			dataConfig.set(rank + "." + rankTo + ".active", false);
		}
		if (containsEntry(rank + "." + rankTo + ".confirm_to_rankup") != true) {
			missingEntry = true;
			dataConfig.set(rank + "." + rankTo + ".confirm_to_rankup", true);
		}
		if (containsEntry(rank + "." + rankTo + ".cost") != true) {
			missingEntry = true;
			dataConfig.set(rank + "." + rankTo + ".cost", "5000");
		}
		if (containsEntry(rank + "." + rankTo + ".requirement_message") != true) {
			missingEntry = true;
			this.dataConfig.set(rank + "." + rankTo + ".requirement_message",
					Arrays.asList(new String[] { "Your current rank is %rankfrom%", "You need $%cost% to rank up to %rankto%",
							"Your balance is $%balance%"}));
		}
		if (containsEntry(rank + "." + rankTo + ".rankup_commands") != true) {
			missingEntry = true;
			dataConfig.set(rank + "." + rankTo + ".rankup_commands",
					Arrays.asList(new String[] { "ezbroadcast [rankup] %player% ranked up to %rankto%!", "ezmsg Congrats on your rankup!", "ezmsg Here are some tools!", "kit tools %player%", "pex user %player% group add %rankto%", "pex user %player% group remove %rankfrom%" }));
		}		
		return missingEntry;
	}

	public String loadRankupsFromFile() {

		int ranks = 0;
		int rankups = 0;

		plugin.getRankHandler().loadMap();
		
		if (getRanks() == null || getRanks().isEmpty()) {
			plugin.getLogger().warning("You have not created any rankups yet!");
			plugin.getLogger()
					.info("Create your first rankup with /ezadmin createrankup <rankfrom> <rankto> <cost>");
			plugin.getLogger()
			.info("Modify the configuration section in the rankups.yml for the rankup you just created, then use /ezadmin reload to update your changes");
			plugin.getLogger()
			.info("You need to include the command(s) to rank the player up in rankup_commands - pex user %player% group set %rankto%, manuadd %player% %rankto% %world%");
			plugin.getLogger()
			.info("You can also use ezcommands in the rankup_commands section \n(ezmsg <message>) will send a raw message to the player ranking up \n(ezbroadcast <message>) will broadcast a raw message to the server");
			plugin.getLogger()
			.info("Valid placeholders for commands are %player% %world% %rankfrom% %rankto% %balance% %cost%");
			plugin.getLogger()
			.info("EZRanksLite will automatically take money from the player on a successful rankup");
			return "No rankups loaded!";
		}
			
		
		for (String rank : getRanks()) {
			
			if (getRankupSections(rank).isEmpty()) {
				plugin.getLogger().warning(rank
						+ " does not contain any rankups to rank up to in the rankups.yml!");
				continue;
			}

			if (!plugin.getVault().isValidServerGroup(rank)) {
				plugin.getLogger().warning(rank
						+ " does not exist in the server permissions plugin!");
				plugin.getLogger()
						.warning("Skipping rankup validation for rank: "
								+ rank);
				continue;
			}
			
			
			
			EZRank baserank = new EZRank(rank);
			
			if (checkValidRankOptions(rank)) {
				save();
			}
			baserank.setRankOrder(displayOrder(rank));
			if (baserank.setAllowReset(allowReset(rank))) {
				baserank.setResetCost(resetCost(rank));
				baserank.setResetCommands(resetCommands(rank));				
			}
			
			for (String rankTo : getRankupSections(rank)) {
				
				if (rankTo.equals("options")) {
					continue;
				}

				if (plugin.getVault().isValidServerGroup(rankTo) == false) {
					plugin.getLogger()
							.warning(rank
									+ " has a rankup to rank "
									+ rankTo
									+ "  that does not exist in the server permissions plugin!");
					plugin.getLogger()
							.warning("Skipping validation for rankup: "
									+ rank + " to " + rankTo);
					continue;
				}

				if (checkValidRankup(rank, rankTo)) {
					save();
				}
				
				EZRankup r = new EZRankup(rankTo);
				
				if (!plugin.isDouble(getCost(rank, rankTo))) {
					plugin.getLogger()
					.warning(rank
							+ " has a rankup to rank "
							+ rankTo
							+ " that has an invalid cost!");
					continue;
				}
				r.setActive(isActive(rank, rankTo));
				r.setConfirmToRank(confirmToRankup(rank, rankTo));
				r.setCost(getCost(rank, rankTo));
				r.setRequirementMsg(getRequirementMessage(rank, rankTo));
				r.setCommands(getRankupCommands(rank, rankTo));
				baserank.addRankup(rankTo, r);
				rankups++;
			}
			plugin.getRankHandler().putRankData(rank, baserank);
			ranks++;
		}

		String msg = ranks + " Ranks loaded with " + rankups
				+ " unique RankUps!";
		return msg;

	}
}

