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

import java.io.IOException;
import java.util.List;

import me.clip.ezrankslite.commands.EZAdminCommand;
import me.clip.ezrankslite.commands.RanksCommand;
import me.clip.ezrankslite.commands.RankupCommand;
import me.clip.ezrankslite.commands.ScoreboardRefreshCommand;
import me.clip.ezrankslite.commands.ScoreboardToggleCommand;
import me.clip.ezrankslite.config.Config;
import me.clip.ezrankslite.config.ConfigWrapper;
import me.clip.ezrankslite.effects.EffectsHandler;
import me.clip.ezrankslite.hooks.VaultEco;
import me.clip.ezrankslite.hooks.VaultPerms;
import me.clip.ezrankslite.listeners.JoinListener;
import me.clip.ezrankslite.metricslite.MetricsLite;
import me.clip.ezrankslite.rankdata.PlayerRankupHandler;
import me.clip.ezrankslite.rankdata.RankHandler;
import me.clip.ezrankslite.rankdata.RankupFile;
import me.clip.ezrankslite.scoreboard.RefreshScoreboardTask;
import me.clip.ezrankslite.scoreboard.ScoreboardHandler;
import me.clip.ezrankslite.scoreboard.ScoreboardOptions;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

public class EZRanksLite extends JavaPlugin {

	private EffectsHandler effectsHandler = new EffectsHandler(this);
	private PlayerRankupHandler playerhandler = new PlayerRankupHandler(this);
	private RankHandler rankhandler = new RankHandler(this);
	private ScoreboardHandler boardhandler = new ScoreboardHandler(this);

	private VaultPerms vaultperms = new VaultPerms(this);
	private VaultEco vaulteco = new VaultEco(this);

	private EZAdminCommand admincommand = new EZAdminCommand(this);
	private RankupCommand rankupcommand = new RankupCommand(this);
	private RanksCommand rankscommand = new RanksCommand(this);
	private ScoreboardToggleCommand togglecommand = new ScoreboardToggleCommand(this);
	private ScoreboardRefreshCommand refreshcommand = new ScoreboardRefreshCommand(this);
	
	private Config config = new Config(this);
	private RankupFile rankupfile = new RankupFile(this);
	private ConfigWrapper messagesFile = new ConfigWrapper(this, "", "messages.yml");
	
	private static boolean debug;
	private static String servername;
	private static boolean fixThousands;
	private static boolean fixMillions;
	private static String millions;
	private static String billions;
	private static String thousands;
	private static String trillions;
	private static String ranksNo;
	private static String ranksYes;
	private static List<String> ranksHeader;
	private static List<String> ranksFooter;
	private static boolean useRanks;
	private static boolean useRankupCooldown;
	private static int rankupCooldownTime;
	
	//scoreboard options
	private static boolean useScoreboard;
	private static int sbRefresh;
	private static ScoreboardOptions sbOptions = null;
	
	private static BukkitTask sbTask = null;
	
	public static boolean useVoteParty = false;

	@Override
	public void onEnable() {
		if (Bukkit.getServer().getPluginManager().getPlugin("VoteParty") != null 
				&& Bukkit.getServer().getPluginManager().getPlugin("VoteParty").isEnabled()) {
			useVoteParty = true;
		}
			
		if (!vaultperms.setupVault()) {
			debug(true,
					"Could not detect Vault for permissions Hooking! Disabling EZRanksLite!");
			Bukkit.getServer().getPluginManager().disablePlugin(this);
		} else if (!vaulteco.setupEconomy()) {
			debug(true,
					"Could not hook into an Economy plugin through Vault! Disabling EZRanksLite!");
			Bukkit.getServer().getPluginManager().disablePlugin(this);
		}
		
		init();
		rankupfile.reload();
		rankupfile.save();
		messagesFile.createNewFile("Loading EZRanksLite messages.yml", "EZRanksLite messages file");
		loadMessages();
		
		getLogger().info(rankupfile.loadRankupsFromFile());
		registerCmds();
		registerListeners();
		if (!startMetricsLite()) {
			debug(false, "Could not start MetricsLite!");
		}
		if (useScoreboard) {
			debug(false,
					"Scoreboard features are enabled!");
		startScoreboardTask();
		}
	}

	@Override
	public void onDisable() {
		stopScoreboardTask();
	}
	
	private void registerCmds() {
		getCommand("ezadmin").setExecutor(admincommand);
		getCommand("rankup").setExecutor(rankupcommand);
		getCommand("ranks").setExecutor(rankscommand);
		getCommand("sbtoggle").setExecutor(togglecommand);
		getCommand("sbrefresh").setExecutor(refreshcommand);
		debug(false,
				"Commands registered");
	}
	
	private void loadMessages() {
		Lang.setFile(messagesFile.getConfig());

		for (final Lang value : Lang.values()) {
			messagesFile.getConfig().addDefault(value.getPath(), value.getDefault());
		}

		messagesFile.getConfig().options().copyDefaults(true);
		messagesFile.saveConfig();
	}

	private void init() {
		config.loadDefaultConfiguration();
		debug = config.isDebug();
		servername = config.getServerName();
		useScoreboard = config.useScoreboard();
		fixThousands = config.fixThousands();
		fixMillions = config.fixMillions();
		thousands = config.getKFormat();
		millions = config.getMFormat();
		billions = config.getBFormat();
		trillions = config.getTFormat();
		useRanks = config.useRanks();
		ranksYes = config.ranksAccess();
		ranksNo = config.ranksNoAccess();
		ranksHeader = config.ranksHeader();
		ranksFooter = config.ranksFooter();
		useRanks = config.useRanks();
		useRankupCooldown = config.useRankupCooldown();
		rankupCooldownTime = config.rankupCooldownTime();
		loadSBOptions();
		sbRefresh = config.getScoreboardRefreshTime();
	}
	
	public void loadOptions() {
		debug = config.isDebug();
		servername = config.getServerName();
		useScoreboard = config.useScoreboard();
		fixThousands = config.fixThousands();
		fixMillions = config.fixMillions();
		thousands = config.getKFormat();
		millions = config.getMFormat();
		billions = config.getBFormat();
		trillions = config.getTFormat();
		useRanks = config.useRanks();
		ranksYes = config.ranksAccess();
		ranksNo = config.ranksNoAccess();
		ranksHeader = config.ranksHeader();
		ranksFooter = config.ranksFooter();
		useRankupCooldown = config.useRankupCooldown();
		rankupCooldownTime = config.rankupCooldownTime();
		loadSBOptions();
		sbRefresh = config.getScoreboardRefreshTime();
	}
	
	public ScoreboardOptions loadSBOptions() {
		ScoreboardOptions options = new ScoreboardOptions();
		options.setTitle(config.sbTitle());
		options.setText(config.sbDisplay());
		sbOptions = options;
		debug(false,
				"Scoreboard options loaded!");
		return options;
	}


	private void registerListeners() {
		Bukkit.getServer().getPluginManager()
				.registerEvents(new JoinListener(this), this);
	}

	public VaultPerms getVault() {
		return vaultperms;
	}

	public VaultEco getEco() {
		return vaulteco;
	}

	public RankHandler getRankHandler() {
		return rankhandler;
	}

	public PlayerRankupHandler getPlayerhandler() {
		return playerhandler;
	}

	public EffectsHandler getEffectsHandler() {
		return effectsHandler;
	}

	public ScoreboardHandler getBoardhandler() {
		return boardhandler;
	}

	public Config getConfigFile() {
		return config;
	}

	public RankupFile getRankFile() {
		return rankupfile;
	}

	public void debug(boolean severe, String msg) {
		if (severe) {
			getLogger().severe(msg);
		} else {
			if (debug) {
				getLogger().info(msg);
			}
		}
	}

	private boolean startMetricsLite() {
		try {
			MetricsLite ml = new MetricsLite(this);
			ml.start();
			return true;
		} catch (IOException e) {
			return false;
		}
	}
	
	public void startScoreboardTask() {
		if (useScoreboard && sbTask == null) {
		sbTask = getServer().getScheduler().runTaskTimer(this,
				new RefreshScoreboardTask(this), 0L, (20L * sbRefresh));
		debug(false,
				"Scoreboard refresh task has started and will refresh every " + sbRefresh + " seconds.");
		}
	}
	
	public void stopScoreboardTask() {
		if (!useScoreboard && sbTask != null) {
		sbTask.cancel();
		sbTask = null;
		debug(false,
				"Scoreboard refresh task has been cancelled!");
		}
	}

	public void sms(Player p, String msg) {
		p.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
	}

	public void sms(CommandSender s, String msg) {
		s.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
	}

	public void bcast(String msg) {
		Bukkit.broadcastMessage(ChatColor
				.translateAlternateColorCodes('&', msg));
	}
	
	public static String fixMoney(double amount, String amt) {
		
		if (amount >= 1000000000000.0D)
			if (fixMillions) {
			return String.format("%.2f"+trillions,
					new Object[] { Double.valueOf(amount / 1000000000000.0D) });
			}
			else {
				long send = (long) amount;
				return send + "";
			}
		else if (amount >= 1000000000.0D)
			if (fixMillions) {
			return String.format("%.2f"+billions,
					new Object[] { Double.valueOf(amount / 1000000000.0D) });
			}
			else {
				long send = (long) amount;
				return send + "";
			}
		else if (amount >= 1000000.0D) {
			if (fixMillions) {
			return String.format("%.2f"+millions,
					new Object[] { Double.valueOf(amount / 1000000.0D) });
			}
			else {
				long send = (long) amount;
				return send + "";
			}
		}
		else if (amount >= 1000.0D) {
			if (fixThousands) {
			return String.format("%.2f"+thousands,
					new Object[] { Double.valueOf(amount / 1000.0D) });
			}
		}
		if (amt.contains(".")) {
			String[] parts = amt.split(".");
			if (parts == null || parts.length < 1 || parts[0].isEmpty()) {
				return amt;
			}
			return parts[0];
		}
		return amt;
	}

	public boolean isDouble(String s) {
		try {
			Double.parseDouble(s);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public ScoreboardOptions getSbOptions() {
		return sbOptions;
	}

	public String getRanksNo() {
		return ranksNo;
	}

	public String getRanksYes() {
		return ranksYes;
	}

	public List<String> getRanksHeader() {
		return ranksHeader;
	}

	public List<String> getRanksFooter() {
		return ranksFooter;
	}
	
	public String getServername() {
		return servername;
	}

	public void setServername(String servername) {
		EZRanksLite.servername = servername;
	}
	
	public boolean useScoreboard() {
		return useScoreboard;
	}

	public boolean useRanksCommand() {
		return useRanks;
	}

	public boolean useRankupCooldown() {
		return useRankupCooldown;
	}

	public int getRankupCooldownTime() {
		return rankupCooldownTime;
	}
	
	public EZAPI getAPI() {
		return new EZAPI(this);
	}


}
