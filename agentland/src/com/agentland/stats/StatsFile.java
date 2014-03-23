// Created by Luzius on Mar 10, 2014

package com.agentland.stats;

import java.io.Closeable;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

public class StatsFile implements Closeable {

	public PrintWriter writer;
	
	public StatsFile(String string) throws FileNotFoundException {
		this.writer = new PrintWriter(string + ".csv");
	}
	
	public void close(){
		this.writer.close();
	}

	public void print(Object... string){
		boolean sep = false;
		for (Object s: string){
			if (sep){
				writer.print(",");
			} else {
				sep = true;
			}
			writer.print(s);
		}
		writer.println();
	}

}
