// Created by Luzius on Mar 7, 2014

package com.agentland.firm;


public abstract class Firm {

	private Books books;
	private Stock stock;
	private Workforce workers;
	
	public Firm(long capital) {
		this.workers = new Workforce();
		this.books = new Books(capital);
		this.stock = new Stock();
	}
	
	public void notifyDayStarted(){
		considerLayoffs();
	}

	private void considerLayoffs() {
		while (!books.canAfford(workers.getTotSalaries())) {
			workers.fireMostExpensiveWorker();
		}
	}

	public void addWork(int salary) {
		this.workers.notifyWorkDone();
		this.books.spend(salary);
	}

	public void produce() {
		int work = workers.harnessWork();
		int multiplier = 4;
		int units = 1;
		while (work > 10 && multiplier > 0){
			units += 10*multiplier;
			multiplier--;
		}
		if (work > 0){
			units+= work * multiplier;
		}
		stock.add(units);
	}

	public Job createJob() {
		Job job = new Job(this, books.getAffordableSalary(workers.getTotSalaries(), workers.getSize()));
		workers.addEmployee(job);
		return job;
	}

	public void removeJob(Job job, long inheritance) {
		boolean removed = this.workers.remove(job);
		assert removed;
		this.books.earn(inheritance);
	}

	public int getAdvertisedSalary() {
		return books.getAffordableSalary(workers.getTotSalaries(), workers.getSize());
	}

	public Stock getStock() {
		return stock;
	}

	public void notifyDayEnded() {
		boolean changeDirectionGood = books.addEarnings(stock.harnessEarnings());
		stock.adaptPrice(changeDirectionGood);
		stock.expire();
	}

	public long getCapital() {
		return books.getMoney() + stock.getEarnings();
	}
	
	@Override
	public String toString(){
		return getClass().getSimpleName() + " with " + workers + ", " + books + " and " + stock;
	}

}
