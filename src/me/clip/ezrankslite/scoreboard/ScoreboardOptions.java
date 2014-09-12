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

import java.util.List;

public class ScoreboardOptions {
	
	public ScoreboardOptions() {
	}
	
	private static String title;

	private static List<String> text;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		ScoreboardOptions.title = title;
	}
	
	public List<String> getText() {
		return text;
	}

	public void setText(List<String> text) {
		ScoreboardOptions.text = text;
	}

}
