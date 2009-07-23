package tudelft.nl.ir.gui;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import tudelft.nl.ir.docs.Document;
import tudelft.nl.ir.index.Index;
import tudelft.nl.ir.index.InvertedIndex;
import tudelft.nl.ir.query.SimpleAndQuery;
import javax.swing.table.TableModel;
import javax.swing.JLabel;
import javax.swing.border.BevelBorder;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class IRGui extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1129264315015857236L;
	private JPanel contentPane;
	private JTextField queryField;
	private JScrollPane scrollPane;
	private JTable table;
	private DefaultTableModel model;
	private JPanel panel_1;
	private JLabel status;
	private String lastQuery;
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					IRGui frame = new IRGui();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public IRGui() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 542, 408);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		{
			JPanel panel = new JPanel();
			panel.setLayout(new GridLayout(1, 0, 0, 0));
			contentPane.add(panel, BorderLayout.NORTH);
			{
				queryField = new JTextField();
				queryField.addKeyListener(new KeyAdapter() {
					@Override
					public void keyPressed(KeyEvent e) {
						int key = e.getKeyCode();
				        
				        if (key == KeyEvent.VK_ENTER) {
				            executeSearch();
				        }
					}
				});
				panel.add(queryField);
				queryField.setColumns(10);
			}
			{
				JButton btnSearch = new JButton("Search");
				btnSearch.addMouseListener(new MouseAdapter() {
					public void mouseClicked(MouseEvent arg0) {
						executeSearch();
						return;
//						int numrows = model.getRowCount(); 
//						for(int i = numrows - 1; i >=0; i--){
//							model.removeRow(i);
//						}
//						status.setText("searching");
//						Index index = new InvertedIndex();
//						String query = queryField.getText();
//						SimpleAndQuery t = new SimpleAndQuery(query);
//						ArrayList<Document> results = (ArrayList<Document>) t.evaluate(index);
//						
//						for(int i = 0; i < results.size(); i++ ){
//							model.addRow(new Object[]{results.get(i).getID()+"", results.get(i).getTitle(),""});
//						}
//						
//						status.setText(model.getRowCount()+" results found...");
					}
				});
				panel.add(btnSearch);
			}
		}
		{
			scrollPane = new JScrollPane();
			contentPane.add(scrollPane, BorderLayout.CENTER);
			{
				model = new DefaultTableModel(
					new Object[][] {
					},
					new String[] {
						"ID", "Document Name"
					}
				);
				
				table = new JTable();
				table.addMouseListener(new MouseAdapter() {
					@Override
					public void mouseClicked(MouseEvent e) {
						if(e.getClickCount() == 2){
							int row = table.rowAtPoint(e.getPoint());
							if(table.getRowCount() > row){
								IRDocumentFrame frame = new IRDocumentFrame((String)table.getValueAt(row, 0), lastQuery);
								frame.setVisible(true);
							}
						}
					}
				});
				table.setEnabled(false);
				table.setModel(model);
				table.getColumnModel().getColumn(0).setPreferredWidth(10);
				table.getColumnModel().getColumn(1).setPreferredWidth(400);
				//table.getColumnModel().getColumn(2).setPreferredWidth(330);
				scrollPane.setViewportView(table);
			}
		}
		{
			panel_1 = new JPanel();
			panel_1.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
			panel_1.setLayout(new BorderLayout(0, 0));
			contentPane.add(panel_1, BorderLayout.SOUTH);
			{
				status = new JLabel("irengine");
				panel_1.add(status);
			}
		}
	}
	protected void executeSearch(){
		int numrows = model.getRowCount(); 
		for(int i = numrows - 1; i >=0; i--){
			model.removeRow(i);
		}
		status.setText("searching");
		Index index = new InvertedIndex();
		String query = queryField.getText();
		this.lastQuery = query;
		SimpleAndQuery t = new SimpleAndQuery(query);
		ArrayList<Document> results = (ArrayList<Document>) t.evaluate(index);
		
		for(int i = 0; i < results.size(); i++ ){
			model.addRow(new Object[]{results.get(i).getID()+"", results.get(i).getTitle(),""});
		}
		
		status.setText(model.getRowCount()+" results found...");
	}
	protected TableModel getTableModel() {
		return table.getModel();
	}
	protected void setTableModel(TableModel model) {
		table.setModel(model);
	}
}
