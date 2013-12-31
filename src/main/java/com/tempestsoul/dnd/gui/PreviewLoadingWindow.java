package com.tempestsoul.dnd.gui;

import java.awt.EventQueue;

import javax.swing.JFrame;
import java.awt.GridLayout;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JList;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.AbstractListModel;

public class PreviewLoadingWindow {

	private JFrame frame;
	private JTextField txtStrScore;
	private JTextField txtDexScore;
	private JTextField txtConScore;
	private JTextField txtIntScore;
	private JTextField txtWisScore;
	private JTextField txtChaScore;
	private JTextField txtNewStr;
	private JTextField txtNewDex;
	private JTextField txtNewCon;
	private JTextField txtNewHp;
	private JTextField txtNewAtks;
	private JTextField txtHitpoints;
	private JTextField txtAttacks;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					PreviewLoadingWindow window = new PreviewLoadingWindow();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public PreviewLoadingWindow() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(new GridLayout(2, 2, 3, 3));
		
		JPanel previewInputPanel = new JPanel();
		frame.getContentPane().add(previewInputPanel);
		previewInputPanel.setLayout(new GridLayout(8, 2, 0, 0));
		
		JLabel lblStr = new JLabel("Str");
		previewInputPanel.add(lblStr);
		
		txtStrScore = new JTextField();
		txtStrScore.setEditable(false);
		txtStrScore.setText("Str Score");
		previewInputPanel.add(txtStrScore);
		txtStrScore.setColumns(10);
		
		JLabel lblDex = new JLabel("Dex");
		previewInputPanel.add(lblDex);
		
		txtDexScore = new JTextField();
		txtDexScore.setEditable(false);
		txtDexScore.setText("Dex Score");
		previewInputPanel.add(txtDexScore);
		txtDexScore.setColumns(10);
		
		JLabel lblCon = new JLabel("Con");
		previewInputPanel.add(lblCon);
		
		txtConScore = new JTextField();
		txtConScore.setEditable(false);
		txtConScore.setText("Con Score");
		previewInputPanel.add(txtConScore);
		txtConScore.setColumns(10);
		
		JLabel lblInt = new JLabel("Int");
		previewInputPanel.add(lblInt);
		
		txtIntScore = new JTextField();
		txtIntScore.setEditable(false);
		txtIntScore.setText("Int Score");
		previewInputPanel.add(txtIntScore);
		txtIntScore.setColumns(10);
		
		JLabel lblWis = new JLabel("Wis");
		previewInputPanel.add(lblWis);
		
		txtWisScore = new JTextField();
		txtWisScore.setEditable(false);
		txtWisScore.setText("Wis Score");
		previewInputPanel.add(txtWisScore);
		txtWisScore.setColumns(10);
		
		JLabel lblCha = new JLabel("Cha");
		previewInputPanel.add(lblCha);
		
		txtChaScore = new JTextField();
		txtChaScore.setEditable(false);
		txtChaScore.setText("Cha Score");
		previewInputPanel.add(txtChaScore);
		txtChaScore.setColumns(10);
		
		JLabel lblHp_1 = new JLabel("HP");
		previewInputPanel.add(lblHp_1);
		
		txtHitpoints = new JTextField();
		txtHitpoints.setEditable(false);
		txtHitpoints.setText("HitPoints");
		previewInputPanel.add(txtHitpoints);
		txtHitpoints.setColumns(10);
		
		JLabel lblAtk = new JLabel("ATK");
		previewInputPanel.add(lblAtk);
		
		txtAttacks = new JTextField();
		txtAttacks.setEditable(false);
		txtAttacks.setText("Attacks");
		previewInputPanel.add(txtAttacks);
		txtAttacks.setColumns(10);
		
		JScrollPane scrollPane = new JScrollPane();
		frame.getContentPane().add(scrollPane);
		
		JList list = new JList();
		list.setModel(new AbstractListModel() {
			String[] values = new String[] {"we", "we", "we", "we", "w", "e", "wr", "e", "", "ewr", "w", "w", "e", "we", "w", "e", "we", "w", "e", "we", "w", "e", "we", "w", "e", "w", "e", "we", "w", "e", "we", "w", "e", "w", "we", "we", "we", "ew", "we", "we"};
			public int getSize() {
				return values.length;
			}
			public Object getElementAt(int index) {
				return values[index];
			}
		});
		scrollPane.setViewportView(list);
		
		JPanel buttonPanel = new JPanel();
		frame.getContentPane().add(buttonPanel);
		
		JButton btnSaveData = new JButton("Save Data");
		buttonPanel.add(btnSaveData);
		
		JButton btnLoadData = new JButton("Load Data");
		buttonPanel.add(btnLoadData);
		
		JPanel previewOutputPanel = new JPanel();
		frame.getContentPane().add(previewOutputPanel);
		previewOutputPanel.setLayout(new GridLayout(5, 2, 0, 0));
		
		JLabel lblStr_1 = new JLabel("Str");
		previewOutputPanel.add(lblStr_1);
		
		txtNewStr = new JTextField();
		txtNewStr.setEditable(false);
		txtNewStr.setText("new Str");
		previewOutputPanel.add(txtNewStr);
		txtNewStr.setColumns(10);
		
		JLabel lblDex_1 = new JLabel("Dex");
		previewOutputPanel.add(lblDex_1);
		
		txtNewDex = new JTextField();
		txtNewDex.setEditable(false);
		txtNewDex.setText("new Dex");
		previewOutputPanel.add(txtNewDex);
		txtNewDex.setColumns(10);
		
		JLabel lblCon_1 = new JLabel("Con");
		previewOutputPanel.add(lblCon_1);
		
		txtNewCon = new JTextField();
		txtNewCon.setEditable(false);
		txtNewCon.setText("new Con");
		previewOutputPanel.add(txtNewCon);
		txtNewCon.setColumns(10);
		
		JLabel lblHp = new JLabel("HP");
		previewOutputPanel.add(lblHp);
		
		txtNewHp = new JTextField();
		txtNewHp.setEditable(false);
		txtNewHp.setText("new HP");
		previewOutputPanel.add(txtNewHp);
		txtNewHp.setColumns(10);
		
		JLabel lblAtk_1 = new JLabel("ATK");
		previewOutputPanel.add(lblAtk_1);
		
		txtNewAtks = new JTextField();
		txtNewAtks.setEditable(false);
		txtNewAtks.setText("new atks");
		previewOutputPanel.add(txtNewAtks);
		txtNewAtks.setColumns(10);
	}

}
