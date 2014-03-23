// Created by Luzius on Mar 7, 2014

package com.agentland;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.Random;

import com.agentland.citizen.Citizen;
import com.agentland.firm.Farm;
import com.agentland.firm.Firm;
import com.agentland.firm.WaterCompany;
import com.agentland.market.FoodMarket;
import com.agentland.market.IJobMarket;
import com.agentland.market.JobMarket;
import com.agentland.stats.BasicStats;
import com.agentland.stats.StatsFile;

public class Country {

	private final static long MONEY_SUPPLY = 1l * 1000 * 1000;

	private Random rand = new Random(1314);

	private int day;

	private ArrayList<Farm> farms;
	private ArrayList<WaterCompany> water;
	private ArrayList<Firm> allFirms;

	private FoodMarket latestMarket;
	private ArrayList<Citizen> citizens;

	public Country(int farms, int water, int initialCitizens) {
		long capitalPerFirm = MONEY_SUPPLY / (farms + water + initialCitizens);
		long left = MONEY_SUPPLY - capitalPerFirm * (farms + water + initialCitizens);
		this.farms = new ArrayList<Farm>();
		for (int i = 0; i < farms; i++) {
			this.farms.add(new Farm(capitalPerFirm + left));
			left = 0;
		}
		this.water = new ArrayList<WaterCompany>();
		for (int i = 0; i < water; i++) {
			this.water.add(new WaterCompany(capitalPerFirm));
		}
		this.allFirms = new ArrayList<Firm>();
		this.allFirms.addAll(this.farms);
		this.allFirms.addAll(this.water);
		this.citizens = new ArrayList<Citizen>();
		JobMarket jobs = new JobMarket(allFirms);
		for (int i = 0; i < initialCitizens; i++) {
			Citizen c = new Citizen(jobs.getAny(), capitalPerFirm);
			this.citizens.add(c);
		}
		this.day = 0;
	}
	
	public void run(String filename, int totSteps) throws FileNotFoundException {
		StatsFile file = new StatsFile(filename);
		try {
			file.print("Day", "Citizens", "Avg. salary", "Water price", "Bread price");
			for (int i = 0; i <= totSteps; i++) {
				step();
				if (i % (totSteps / 1000) == 0) {
					file.print(day, citizens.size(), getSalaryStats().getAverage(), latestMarket.getWaterStats().getAverage(), latestMarket.getBreadStats().getAverage());
				}
			}
		} finally {
			file.close();
		}
	}

	public void step() {
		Collections.shuffle(citizens, rand); // randomize order of citizens
		for (Firm f : allFirms) {
			f.notifyDayStarted();
		}
		System.out.println("Starting day " + day++ + " with " + toString());
		JobMarket jobs = new JobMarket(allFirms);
		workAndProduce(jobs);
		consumeAndLive(jobs);
		for (Firm f : allFirms) {
			f.notifyDayEnded();
		}
		assert calculateMoneySupply() == MONEY_SUPPLY;
	}

	private long calculateMoneySupply() {
		long total = 0;
		for (Firm f : allFirms) {
			total += f.getCapital();
		}
		for (Citizen c : citizens) {
			total += c.getWealth();
		}
		return total;
	}

	private void workAndProduce(IJobMarket jobs) {
		for (Citizen c : citizens) {
			c.considerJobSwitch(jobs);
		}
		for (Citizen c : citizens) {
			c.work();
		}
		System.out.println(getSalaryStats());
		for (Firm f : allFirms) {
			f.produce();
		}
	}

	private BasicStats getSalaryStats() {
		BasicStats salaries = new BasicStats("salaries");
		for (Citizen c : citizens) {
			salaries.include(c.getSalary());
		}
		return salaries;
	}

	private void consumeAndLive(IJobMarket jobs) {
		latestMarket = new FoodMarket(farms, water);
		for (Citizen c : citizens) {
			c.consume(latestMarket);
		}
		latestMarket.printStats();
		ArrayList<Citizen> newborn = new ArrayList<Citizen>();
		Iterator<Citizen> iter = citizens.iterator();
		while (iter.hasNext()) {
			Citizen current = iter.next();
			Citizen offspring = current.live(jobs);
			if (offspring != null) {
				newborn.add(offspring);
			}
			if (current.isDying()) {
				current.dispose();
				iter.remove();
			}
		}
		citizens.addAll(newborn);
	}

	private int countWellFedCitizens() {
		int wellfed = 0;
		for (Citizen c : citizens) {
			if (c.isWellFed()) {
				wellfed++;
			}
		}
		return wellfed;
	}

	@Override
	public String toString() {
		return citizens.size() + " citizens out of which " + countWellFedCitizens() + " are well-fed";
	}

	public static void main(String[] args) throws FileNotFoundException {
		Country c1 = new Country(5, 100, 10);
		int totSteps = 10000;
		StatsFile file = new StatsFile("StockOrientedPrices-1314");
		try {
			file.print("Day", "Citizens", "Avg. salary", "Water price", "Bread price");
			for (int i = 0; i < totSteps; i++) {
				c1.step();
				if (i % (totSteps / 100) == 0) {
					file.print(c1.day, c1.citizens.size(), c1.getSalaryStats().getAverage(), c1.latestMarket.getWaterStats().getAverage(), c1.latestMarket.getBreadStats().getAverage());
				}
			}
		} finally {
			file.close();
		}
	}

}
