// Created by Luzius on Mar 9, 2014

package com.agentland.market;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.PriorityQueue;

import com.agentland.firm.Farm;
import com.agentland.firm.Firm;
import com.agentland.firm.Stock;
import com.agentland.firm.WaterCompany;
import com.agentland.stats.BasicStats;

public class FoodMarket implements IFoodMarket {
	
	private BasicStats waterStats, breadStats;
	private PriorityQueue<Stock> water, bread;
	
	public FoodMarket(ArrayList<Farm> farms, ArrayList<WaterCompany> water){
		this.water = createQueue(water);
		this.bread = createQueue(farms);
		this.waterStats = new BasicStats("units of water sold");
		this.breadStats = new BasicStats("units of bread sold");
	}

	private PriorityQueue<Stock> createQueue(ArrayList<? extends Firm> firms) {
		PriorityQueue<Stock> pq = new PriorityQueue<Stock>(firms.size(), new Comparator<Stock>() {

			public int compare(Stock o1, Stock o2) {
				Integer i1 = o1.getPrice();
				return i1.compareTo(o2.getPrice());
			}
		});
		for (Firm f: firms){
			Stock s = f.getStock();
			if (s.getSize() > 0){
				pq.add(s);
			}
		}
		clean(pq);
		return pq;
	}

	private void clean(PriorityQueue<Stock> pq) {
		while (pq.size() > 0 && pq.peek().isEmpty()){
			pq.poll();
		}
	}
	
	public void printStats(){
		System.out.println(waterStats);
		System.out.println(breadStats);
//		printProductionStats("firms have water bottles", water);
//		printPriceStats("water bottle prices", water);
//		printProductionStats("firms have bread", bread);
//		printPriceStats("bread prices", bread);
	}
	
	private void printPriceStats(String string, Collection<Stock> firms) {
		BasicStats salaries = new BasicStats(string);
		for (Stock f: firms){
			salaries.include(f.getPrice());
		}
		System.out.println(salaries);
	}

	private void printProductionStats(String string, Collection<Stock> firms) {
		BasicStats salaries = new BasicStats(string);
		for (Stock f: firms){
			salaries.include(f.getSize());
		}
		System.out.println(salaries);
	}
	
	public BasicStats getWaterStats(){
		return waterStats;
	}
	
	public BasicStats getBreadStats(){
		return breadStats;
	}
	
	public boolean hasWater() {
		return water.size() > 0;
	}

	public boolean hasBread() {
		return bread.size() > 0;
	}
	
	public int getWaterPrice() {
		return water.peek().getPrice();
	}

	public int buyWater() {
		return buy(water, waterStats);
	}
	
	public int buyBread() {
		return buy(bread, breadStats);
	}

	private int buy(PriorityQueue<Stock> stock, BasicStats stats) {
		Stock s = stock.peek();
		stats.include(s.getPrice());
		s.buyOneUnit();
		if (s.getSize() == 0){
			stock.poll();
			clean(stock);
		}
		return s.getPrice();
	}

	public int getBreadPrice() {
		return bread.peek().getPrice();
	}

}
