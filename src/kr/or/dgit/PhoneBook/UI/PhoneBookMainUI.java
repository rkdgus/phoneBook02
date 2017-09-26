package kr.or.dgit.PhoneBook.UI;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Map;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import kr.or.dgit.PhoneBook.DTO.Phone;
import kr.or.dgit.PhoneBook.ctrl.PhoneControl;

@SuppressWarnings("serial")
public class PhoneBookMainUI extends JFrame {

	public static final String[] COL_NAMES = { "번호", "이름", "주소", "전화번호" };
	private JPanel contentPane;
	private JTable table;
	private PhoneControl phoneControl;
	private DefaultTableModel model;
	private int i;
	private JMenuItem deleteMenu;
	private JMenuItem changeMenu;
	private Object[] rowDates;
	private int index;
	private String name;
	private String addr;
	private String tel;
	private Phone modelPhone;
	String userDir = System.getProperty("user.dir");
	private JFileChooser chooser = new JFileChooser(userDir + "/save");

	public PhoneBookMainUI() {
		phoneControl = new PhoneControl();

		setTitle("연락처 관리 앱");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 571, 341);

		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);

		JMenu mn = new JMenu("menu");
		menuBar.add(mn);

		JMenuItem mntmOpen = new JMenuItem("open");
		mntmOpen.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int rec = chooser.showOpenDialog(null);

				if (rec != JFileChooser.APPROVE_OPTION) {
					JOptionPane.showMessageDialog(null, "파일을 선택하세요");
					return;
				}
				String path = chooser.getSelectedFile().getPath();

				try {
					ObjectInputStream input = new ObjectInputStream(new FileInputStream(path));
					phoneControl.setPhoneBook((Map) input.readObject());
				} catch (FileNotFoundException e1) {
					e1.printStackTrace();
				} catch (IOException e1) {
					e1.printStackTrace();
				} catch (ClassNotFoundException e1) {
					e1.printStackTrace();
				}
				model.setDataVector(getDatas(), PhoneBookMainUI.COL_NAMES);
				revalidate();
			}
		});
		mn.add(mntmOpen);

		JMenuItem mntmSave = new JMenuItem("save");
		mntmSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int rec = chooser.showSaveDialog(null);
				if (rec != JFileChooser.APPROVE_OPTION) {
					JOptionPane.showMessageDialog(null, "파일을 선택하세요");
					return;
				}
				String path = chooser.getSelectedFile().getPath();

				try {
					ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(path));
					out.writeObject(phoneControl.getPhoneBook());
				} catch (FileNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

			}
		});
		mn.add(mntmSave);

		JMenuItem mntmCancel = new JMenuItem("cancel");
		mntmCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setVisible(false);
			}
		});
		mn.add(mntmCancel);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);

		PhonePanel phonePanel = new PhonePanel();
		contentPane.add(phonePanel, BorderLayout.NORTH);

		JScrollPane scrollPane = new JScrollPane();
		contentPane.add(scrollPane, BorderLayout.CENTER);

		showTable(scrollPane);
		i = 1;

		phonePanel.getBtn().addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				if (e.getActionCommand() == "추가") {
					Phone newPhone = phonePanel.getPhone();
					rowDates = new Object[] { i, newPhone.getName(), newPhone.getAddr(), newPhone.getTel() };
					if (phoneControl.insertPhone(newPhone) == true) {
						i++;
						model.addRow(rowDates);
					}
					phonePanel.clearTf();
				} else if (e.getActionCommand() == "수정") {
					getModelValue();
					Phone p = phonePanel.getPhone();
					phoneControl.changePhone(modelPhone, p);
					model.setValueAt(p.getName(), index, 1);
					model.setValueAt(p.getAddr(), index, 2);
					model.setValueAt(p.getTel(), index, 3);
					phonePanel.clearTf();
					phonePanel.getBtn().setText("추가");

				}
			}
		});

		deleteMenu.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				getModelValue();
				phoneControl.deletePhone(modelPhone);
				model.removeRow(index);

			}

		});

		changeMenu.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				phonePanel.getBtn().setText(changeMenu.getText());
				getModelValue();
				phonePanel.setPhone(modelPhone);
			}
		});

	}

	private void showTable(JScrollPane scrollPane) {
		model = new DefaultTableModel(getDatas(), PhoneBookMainUI.COL_NAMES);
		table = new JTable(model);
		scrollPane.setViewportView(table);

		JPopupMenu popup = new JPopupMenu();
		deleteMenu = new JMenuItem("삭제");
		popup.add(deleteMenu);

		changeMenu = new JMenuItem("수정");
		popup.add(changeMenu);

		table.setComponentPopupMenu(popup);
	}

	private Object[][] getDatas() {
		return phoneControl.showPhones();
	}

	private void getModelValue() {
		index = table.getSelectedRow();

		name = String.valueOf(model.getValueAt(index, 1));
		addr = String.valueOf(model.getValueAt(index, 2));
		tel = String.valueOf(model.getValueAt(index, 3));
		modelPhone = new Phone(name, addr, tel);

	}
}