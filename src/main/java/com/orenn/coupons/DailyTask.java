package com.orenn.coupons;

import java.util.Timer;
import java.util.TimerTask;

import com.orenn.coupons.exceptions.ApplicationException;
import com.orenn.coupons.logic.CouponsController;

public class DailyTask extends TimerTask {
	
	public static void main(String[] args) {
		
		TimerTask timerTask = new DailyTask();
		Timer timer = new Timer();
		
		timer.schedule(timerTask, 0, 1000*60*60*24);
		
	}
	CouponsController couponsController = new CouponsController();

	@Override
	public void run() {
		try {
			couponsController.removeExpiredCoupons();
		} catch (ApplicationException e) {
			e.printStackTrace();
		}
	}

}
