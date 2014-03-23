// Created by Luzius on Mar 9, 2014

package com.agentland.stats;

public class BasicStats {
	
	private String name;
	private long sum;
	private int count;
	private long max;
	private long min;
	
	public BasicStats(String name){
		this.name = name;
		this.sum = 0;
		this.count = 0;
		this.max = Long.MIN_VALUE;
		this.min = Long.MAX_VALUE;
	}
	
	public void include(long value){
		this.count ++;
		this.sum += value;
		this.max = Math.max(value, max);
		this.min = Math.min(value, min);
	}
	
	public double getAverage(){
		return (((double)sum) / count);
	}
	
	@Override
	public String toString(){
		return "\t" + count + " " + name + " between " + min + " and " + max + ", average " + getAverage();
	}

}
