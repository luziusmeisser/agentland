// Created by Luzius on Mar 8, 2014

package com.agentland.firm;

public class Stock {

	private final static boolean REVENUE_ORIENTED_PRICES = false;

	private static final int MIN_PRICE = 10;

	private int units;
	private int price;
	private long earned;
	private boolean adaptUpwards;

	public Stock() {
		this.units = 0;
		this.price = 100;
		this.earned = 0;
		this.adaptUpwards = true;
	}

	public void adaptPrice(boolean sameAsYesterday) {
		if (REVENUE_ORIENTED_PRICES) {
			if (!sameAsYesterday) {
				adaptUpwards = !adaptUpwards;
			}
		} else {
			adaptUpwards = units == 0;
		}
		if (adaptUpwards) {
			increasePrice();
		} else {
			decreasePrice();
		}
	}

	private void decreasePrice() {
		int decreased = price * 9 / 10;
		this.price = Math.max(MIN_PRICE, decreased);
	}

	private void increasePrice() {
		int increased = price * 10 / 9;
		this.price = increased;
	}

	public void expire() {
		this.units = 0;
	}

	public void add(int unitsProduced) {
		this.units += unitsProduced;
	}

	public int getSize() {
		return units;
	}

	public int getPrice() {
		return price;
	}

	public boolean isEmpty() {
		return units == 0;
	}

	public void buyOneUnit() {
		this.units--;
		this.earned += price;
	}

	public long getEarnings() {
		return earned;
	}

	public long harnessEarnings() {
		try {
			return earned;
		} finally {
			earned = 0;
		}
	}

	@Override
	public String toString() {
		return units + " in stock at price " + price;
	}

}
