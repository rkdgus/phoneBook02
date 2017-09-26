package kr.or.dgit.PhoneBoo02;

import java.awt.EventQueue;

import kr.or.dgit.PhoneBook.UI.PhoneBookMainUI;

public class phoneBook {

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					PhoneBookMainUI frame = new PhoneBookMainUI();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
}
