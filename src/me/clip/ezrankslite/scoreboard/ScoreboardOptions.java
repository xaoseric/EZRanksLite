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

	private static String currentRankSection;
	
	private static String currentRank;
	
	private static String rankupSectionSingular;
	
	private static String rankupSectionPlural;
	
	private static String rankup;
	
	private static String costSection;
	
	private static String cost;
	
	private static String balanceSection;
	
	private static String balance;
	
	private static List<String> customHeader;
	private static List<String> customFooter;
	
	public String getCurrentRankSection() {
		return currentRankSection;
	}

	public void setCurrentRankSection(String currentRankSection) {
		ScoreboardOptions.currentRankSection = currentRankSection;
	}

	public String getCurrentRank() {
		return currentRank;
	}

	public void setCurrentRank(String currentRank) {
		ScoreboardOptions.currentRank = currentRank;
	}

	public String getRankupSectionSingular() {
		return rankupSectionSingular;
	}

	public void setRankupSectionSingular(String rankupSectionSingular) {
		ScoreboardOptions.rankupSectionSingular = rankupSectionSingular;
	}

	public String getRankupSectionPlural() {
		return rankupSectionPlural;
	}

	public void setRankupSectionPlural(String rankupSectionPlural) {
		ScoreboardOptions.rankupSectionPlural = rankupSectionPlural;
	}

	public String getRankup() {
		return rankup;
	}

	public void setRankup(String rankup) {
		ScoreboardOptions.rankup = rankup;
	}

	public String getCostSection() {
		return costSection;
	}

	public void setCostSection(String costSection) {
		ScoreboardOptions.costSection = costSection;
	}

	public String getCost() {
		return cost;
	}

	public void setCost(String cost) {
		ScoreboardOptions.cost = cost;
	}

	public String getBalanceSection() {
		return balanceSection;
	}

	public void setBalanceSection(String balanceSection) {
		ScoreboardOptions.balanceSection = balanceSection;
	}

	public String getBalance() {
		return balance;
	}

	public void setBalance(String balance) {
		ScoreboardOptions.balance = balance;
	}

	public List<String> getCustomHeader() {
		return customHeader;
	}

	public void setCustomHeader(List<String> customHeader) {
		ScoreboardOptions.customHeader = customHeader;
	}

	public List<String> getCustomFooter() {
		return customFooter;
	}

	public void setCustomFooter(List<String> customFooter) {
		ScoreboardOptions.customFooter = customFooter;
	}


}
