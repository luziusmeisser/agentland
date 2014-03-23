// Created by Luzius on Mar 7, 2014

package com.agentland.citizen;

import com.agentland.firm.Job;
import com.agentland.market.IFoodMarket;
import com.agentland.market.IJobMarket;

public class Citizen {
	
	private static final int PREGNANCY_IN_DAYS = 10;
	
	private long cash;
	private Job job;
	private int thirst;
	private int hunger;
	private int consecutiveWellFedDays;
	
	public Citizen(Job job, long money){
		this.cash = money;
		this.thirst = 0;
		this.hunger = 0;
		this.job = job;
		this.consecutiveWellFedDays = 0;
	}
	
	public void considerJobSwitch(IJobMarket market){
		Job better = market.findBetterJob(job);
		if (better != null){
			this.job.dispose(0);
			this.job = better;
		}
	}

	public void work(){
		this.cash += job.work();
	}
	
	public void consume(IFoodMarket market){
		drink(market);
		eat(market);
	}
	
	private void drink(IFoodMarket market){
		if (market.hasWater() && market.getWaterPrice() <= cash){
			cash -= market.buyWater();
			thirst = Math.max(0, thirst - 1);
		} else {
			thirst++;
		}
	}
	
	private void eat(IFoodMarket market){
		if (market.hasBread() && market.getBreadPrice() <= cash){
			cash -= market.buyBread();
			hunger = Math.max(0, hunger - 1);
		} else {
			hunger++;
		}
	}
	
	public boolean isDying(){
		return thirst >= 3 || hunger >= 7;
	}
	
	public Citizen live(IJobMarket jobs){
		if (isWellFed()){
			consecutiveWellFedDays++;
			if (consecutiveWellFedDays >= PREGNANCY_IN_DAYS){
				consecutiveWellFedDays = 0;
				long half = cash / 2;
				this.cash -= half;
				return new Citizen(jobs.getAny(), half);
			} else {
				return null;
			}
		} else {
			return null;
		}
	}
	
	public void dispose(){
		job.dispose(cash);
		cash = 0;
	}

	public boolean isWellFed() {
		return thirst == 0 && hunger == 0;
	}

	public int getSalary() {
		return job.getSalary();
	}

	public long getWealth() {
		return cash;
	}
	
	@Override
	public String toString(){
		return "Citizen, thirst: " + thirst + " / " + hunger + " with wealth " + cash + " and " + job;
	}
	
}
