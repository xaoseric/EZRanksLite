package me.clip.ezrankslite.multipliers;

import java.util.TreeMap;

import org.bukkit.entity.Player;

import me.clip.ezrankslite.EZRanksLite;

public class CostHandler {

	EZRanksLite plugin;

	public CostHandler(EZRanksLite i) {
		plugin = i;
	}
    //                       priority, discount object that holds permission and discount
	protected static TreeMap<Integer, Discount> discounts = new TreeMap<Integer, Discount>();
	protected static TreeMap<Integer, CostMultiplier> multipliers = new TreeMap<Integer, CostMultiplier>();

	public static double getDiscount(Player p, double cost) {

		if (discounts == null || discounts.isEmpty()) {
			return cost;
		}

		for (int i : discounts.keySet()) {

			String perm = discounts.get(i).getPermission();

			if (p.hasPermission(perm)) {

				double d = (discounts.get(i).getMultiplier() / 100) * cost;

				if (cost - d >= 1) {
					return cost - d;
				}

				return 1;

			}
		}
		return cost;
	}

	public static double getMultiplier(Player p, double cost) {

		if (multipliers == null || multipliers.isEmpty()) {
			return cost;
		}

		for (int i : multipliers.keySet()) {

			String perm = multipliers.get(i).getPermission();

			if (p.hasPermission(perm)) {

				double d = (multipliers.get(i).getMultiplier() / 100) * cost;

				return cost + d;

			}
		}
		return cost;
	}

}
