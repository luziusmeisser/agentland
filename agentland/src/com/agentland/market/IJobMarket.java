// Created by Luzius on Mar 7, 2014

package com.agentland.market;

import com.agentland.firm.Job;

public interface IJobMarket {

	public Job findBetterJob(Job job);

	public Job getAny();

}
