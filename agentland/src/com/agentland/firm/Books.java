// Created by Luzius on Mar 8, 2014

package com.agentland.firm;

public class Books {
	
	private long cash;
	private long latestEarnings;
	
	public Books(long capital){
		this.cash = capital;
		this.latestEarnings = 0;
	}
	
	public boolean addEarnings(long earnings) {
		this.cash += earnings;
		boolean improvement = earnings > latestEarnings;
		this.latestEarnings = earnings;
		return improvement;
	}
	
	public void spend(long amount){
		assert amount >= 0;
		assert amount <= cash;
		this.cash -= amount;
	}
	
	public void earn(long amount){
		assert amount >= 0;
		assert cash <= Long.MAX_VALUE / 32;
		this.cash += amount;
	}

	public boolean canAfford(long budget) {
		return budget <= cash;
	}

	public int getAffordableSalary(long expenses, int employees) {
		long moneyToSpend = Math.min(cash, Math.max(latestEarnings, cash / 3));
		long freeMoney = moneyToSpend - expenses;
		if (freeMoney > 0) {
			assert freeMoney <= Integer.MAX_VALUE;
			return (int) (freeMoney / Math.max(employees, 3));
		} else {
			return 0;
		}
	}

	public long getMoney() {
		return cash;
	}
	
	@Override
	public String toString(){
		return cash + " cash";
	}

}
