package kr.or.dgit.PhoneBook.ctrl;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.JOptionPane;

import kr.or.dgit.PhoneBook.DTO.Phone;

public class PhoneControl {
	private Map<String, Phone> phoneBook;

	

	public PhoneControl(Map<String, Phone> phoneBook) {
		this.phoneBook = phoneBook;
	}



	public PhoneControl() {
		phoneBook = new HashMap<>();
	}



	@Override
	public String toString() {
		return String.format("%s", phoneBook);
	}
	
	

	public void setPhoneBook(Map<String, Phone> phoneBook) {
		this.phoneBook = phoneBook;
	}

	public Map<String, Phone> getPhoneBook() {
		return phoneBook;
	}

	public boolean insertPhone(Phone newPhone) {
		if (isExist(newPhone)) {
			JOptionPane.showMessageDialog(null, "이미 등록된 이름입니다.");
			return false;
		}
		phoneBook.put(newPhone.getName(), newPhone);
		return true;
	}

	public boolean isExist(Phone newPhone) {
		return phoneBook.containsKey(newPhone.getName());
	}

	public boolean deletePhone(Phone delPhone) {
		if (!isExist(delPhone)) {
			return false;
		}
		phoneBook.remove(delPhone.getName());
		return true;
	}

	public Phone searchPhone(Phone searchPhone) {
		if (!isExist(searchPhone)) {
			return null;
		}
		return phoneBook.get(searchPhone.getName());

	}
	
	public void changePhone(Phone p1,Phone p2){
	
			
			phoneBook.replace(p1.getName(), p2);
			
		
		
	}

	public Object[][] showPhones() {
		Object[][] datas = new Object[phoneBook.size()][];

		int i = 0;
		for (Entry<String, Phone> p : phoneBook.entrySet()) {
			Object[] arrs = new Object[4];
			arrs[0] = i + 1; // 번호,"","",""
			Object[] phoneArr = p.getValue().toArray(); // "홍길동",대구,010-...

			System.arraycopy(phoneArr, 0, arrs, 1, phoneArr.length);// 배열카피
																	// "번호,홍길동,대구,011-...
			datas[i] = arrs;// toArray 가 일차원 배열

			i++;
		}

		return datas;
	}
}