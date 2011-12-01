package de.htwg.mocomp.lotteryapp.database;

import java.io.Serializable;
import java.util.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class LotteryTicket implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private UUID uuid;
	private int id;
	private boolean delete;
	private List<Integer> lotteryNumbers;
	private Date ticketCreationDate;

	public LotteryTicket() {
		lotteryNumbers = new ArrayList<Integer>();
		id = -1;
		setDelete(false);
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

	public void addLotteryNumber(int number) {
		lotteryNumbers.add(number);
	}

	public void setLottaryNumbers(List<Integer> lottaryNumbers) {
		this.lotteryNumbers = lottaryNumbers;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public boolean isDelete() {
		return delete;
	}

	public void setDelete(boolean delete) {
		this.delete = delete;
	}

	public Date getTicketCreationDate() {
		return ticketCreationDate;
	}

	public void setTicketCreationDate(Date date) {
		this.ticketCreationDate = date;
	}
}
