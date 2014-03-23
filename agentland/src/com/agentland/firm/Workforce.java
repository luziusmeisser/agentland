// Created by Luzius on Mar 8, 2014

package com.agentland.firm;

import java.util.ArrayList;

public class Workforce {

	private int workdone;
	private int totsalaries;
	private ArrayList<Job> employees;

	public Workforce() {
		this.employees = new ArrayList<Job>();
		this.totsalaries = 0;
	}

	public void fireMostExpensiveWorker() {
		if (employees.size() > 0) {
			Job max = employees.get(0);
			for (int i = 1; i < employees.size(); i++) {
				if (employees.get(i).getSalary() > max.getSalary()) {
					max = employees.get(i);
				}
			}
			this.totsalaries -= max.getSalary();
			max.terminate();
		}
	}
	
	public int getSize() {
		return employees.size();
	}

	public long getTotSalaries() {
		return totsalaries;
	}

	public void notifyWorkDone() {
		this.workdone++;
	}

	public int harnessWork() {
		try {
			assert workdone == employees.size();
			return workdone;
		} finally {
			this.workdone = 0;
		}
	}

	public boolean remove(Job job) {
		this.totsalaries -= job.getSalary();
		return employees.remove(job);
	}

	public void addEmployee(Job job) {
		this.totsalaries += job.getSalary();
		this.employees.add(job);
	}

	@Override
	public String toString() {
		return employees.size() + " employees";
	}

}
