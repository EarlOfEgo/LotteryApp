package de.htwg.mocomp.lotteryapp.database;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class LotteryTicket {
	private UUID uuid;
	private String name;
	private List<Integer> lotteryNumbers;
	
	public LotteryTicket(){
		lotteryNumbers = new ArrayList<Integer>();
	}
	
	public String getUuid() {
		return uuid.toString();
	}
	public void setUuid(UUID uuid) {
		this.uuid = uuid;
	}
	public List<Integer> getLottaryNumbers() {
		return lotteryNumbers;
	}
	public void addLotteryNumber(int number){
		lotteryNumbers.add(number);
	}
	
	public void setLottaryNumbers(List<Integer> lottaryNumbers) {
		this.lotteryNumbers = lottaryNumbers;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
}
