import javax.swing.JPanel;
import javax.swing.JLabel;
import java.awt.BorderLayout;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.json.JsonValue;
import javax.swing.BoxLayout;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JTable;
import javax.swing.JScrollPane;
import javax.swing.JFileChooser;
import javax.swing.table.DefaultTableModel;

import burp.api.montoya.MontoyaApi;

import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneLayout;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import javax.swing.ScrollPaneConstants;

public class GUI extends JPanel {
	private JTable table;
	public DefaultTableModel tmodel;
	private MontoyaApi api;
	/**
	 * Create the panel.
	 */
	public GUI(MontoyaApi api) {
		this.api = api;
		setLayout(new BorderLayout(0, 0));
		
		JPanel panel = new JPanel();
		add(panel, BorderLayout.NORTH);
		panel.setLayout(new BorderLayout(0, 0));
		
		JMenuBar menuBar = new JMenuBar();
		panel.add(menuBar);
		
		JMenuItem mntmNewMenuItem = new JMenuItem("+ Import HAR");
		mntmNewMenuItem.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				JFileChooser fileChooser = new JFileChooser();
		        int returnValue = fileChooser.showOpenDialog(null);
		        if (returnValue == JFileChooser.APPROVE_OPTION) {
		            File selectedFile = fileChooser.getSelectedFile();
		            //api.logging().logToOutput(selectedFile.getAbsolutePath());
		            parseHar(selectedFile.getAbsolutePath());
		        }
			}
		});
		menuBar.add(mntmNewMenuItem);		
		table = new JTable();
		tmodel = new DefaultTableModel(null,new String[] {"Method", "URL"});
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table.setModel(tmodel);
		table.getColumnModel().getColumn(0).setMinWidth(575);
		table.getColumnModel().getColumn(1).setMinWidth(575);
		JScrollPane pnlTable = new JScrollPane(table);
		add(pnlTable, BorderLayout.CENTER);
		pnlTable.setLayout(new ScrollPaneLayout());		
	}
	public void parseHar(String file) {
		api.logging().logToOutput(file);
		FileReader reader = null;
		try {
			reader = new FileReader(file);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			api.logging().logToError(e.getMessage());
		}
        JsonReader jsonReader = Json.createReader(reader);
        api.logging().logToOutput("made it!!!!!!!!!!");
        JsonObject harObject = jsonReader.readObject();

        JsonObject log = harObject.getJsonObject("log");
        JsonArray entries = log.getJsonArray("entries");

        for (JsonValue entryValue : entries) {
            JsonObject entry = (JsonObject) entryValue;
            this.api.logging().logToOutput("Request: " + entry.getJsonObject("request"));
            try {
            	this.tmodel.addRow(new Object[] {entry.getJsonObject("request"),""});
            } catch (Exception e) {
            	this.api.logging().logToError(e.getMessage());
            }
            //System.out.println("Response: " + entry.getJsonObject("response"));
        }
	}
}
