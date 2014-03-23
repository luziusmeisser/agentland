// Created by Luzius on Mar 7, 2014

package com.agentland.market;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.PriorityQueue;

import com.agentland.firm.Firm;
import com.agentland.firm.Job;

public class JobMarket implements IJobMarket {

	private PriorityQueue<Firm> ranking;
	
	public JobMarket(ArrayList<Firm> allFirms){
		this.ranking = new PriorityQueue<Firm>(allFirms.size(), new Comparator<Firm>() {

			public int compare(Firm o1, Firm o2) {
				Integer i1 = o1.getAdvertisedSalary();
				return - i1.compareTo(o2.getAdvertisedSalary());
			}
		});
		this.ranking.addAll(allFirms);
	}
	
	public Job findBetterJob(Job job) {
		if (job.getSalary() < ranking.peek().getAdvertisedSalary()){
			Firm f = ranking.poll();
			Job newJob = f.createJob();
			ranking.add(f);
			return newJob;
		} else {
			return null;
		}
	}

	public Job getAny() {
		Firm f = ranking.poll();
		Job newJob = f.createJob();
		ranking.add(f);
		return newJob;
	}
	
}
