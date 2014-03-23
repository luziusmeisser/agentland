// Created by Luzius on Mar 7, 2014

package com.agentland.firm;


public class Job {
	
	private Firm firm;
	private int salary;

	public Job(Firm firm, int salary) {
		this.firm = firm;
		this.salary = salary;
		assert salary >= 0;
	}

	public int work() {
		firm.addWork(salary);
		return salary;
	}
	
	public void terminate(){
		this.salary = 0; // setting salary to zero. If worker prefers to stay, he can, but won't earn anything any more.
	}

	public void dispose(long inheritance) {
		this.firm.removeJob(this, inheritance);
	}

	public int getSalary() {
		return salary;
	}
	
	@Override
	public String toString(){
		return "Job with salary " + salary;
	}

}
