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
package me.clip.ezrankslite.listeners;

import me.clip.ezrankslite.EZRanksLite;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class JoinListener implements Listener {
	
	private EZRanksLite plugin;
	
	public JoinListener(EZRanksLite instance) {
		plugin = instance;
	}
	
	@EventHandler
	public void onJoin(PlayerJoinEvent e) {
		
		if (!plugin.useScoreboard()) {
			return;
		}
		
		Player p = e.getPlayer();
		
		plugin.getBoardhandler().createScoreboard(p);
		
		
	}
	
	@EventHandler
	public void onLeave(PlayerQuitEvent e) {
		
		if (!plugin.useScoreboard()) {
			return;
		}
		
		Player p = e.getPlayer();
		
		plugin.getBoardhandler().removeScoreboard(p);
	}
	
	@EventHandler
	public void onKick(PlayerKickEvent e) {
		
		if (!plugin.useScoreboard()) {
			return;
		}
		
		Player p = e.getPlayer();
		
		plugin.getBoardhandler().removeScoreboard(p);
	}

}
