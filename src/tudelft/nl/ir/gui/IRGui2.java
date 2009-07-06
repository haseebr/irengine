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
import javax.swing.JTextPane;
import javax.swing.JLabel;
import java.awt.FlowLayout;
import javax.swing.border.BevelBorder;

public class IRGui2 extends JFrame {

	private JPanel contentPane;
	private JTextField queryField;
	private JScrollPane scrollPane;
	private JTable table;
	private DefaultTableModel model;
	private JPanel panel_1;
	private JLabel status;
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					IRGui2 frame = new IRGui2();
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
	public IRGui2() {
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
				panel.add(queryField);
				queryField.setColumns(10);
			}
			{
				JButton btnSearch = new JButton("Search");
				btnSearch.addMouseListener(new MouseAdapter() {
					public void mouseClicked(MouseEvent arg0) {
						int numrows = model.getRowCount(); 
						for(int i = numrows - 1; i >=0; i--){
							model.removeRow(i);
						}
						status.setText("searching");
						Index index = new InvertedIndex();
						String query = queryField.getText();
						SimpleAndQuery t = new SimpleAndQuery(query);
						ArrayList<Document> results = (ArrayList<Document>) t.evaluate(index);
						
						for(int i = 0; i < results.size(); i++ ){
							model.addRow(new Object[]{results.get(i).getID()+"", results.get(i).getTitle(),""});
						}
						
						status.setText(model.getRowCount()+" results found...");
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
						"ID", "Document Name", "Snippet"
					}
				);
				
				table = new JTable();
				table.setModel(model);
				table.getColumnModel().getColumn(0).setPreferredWidth(50);
				table.getColumnModel().getColumn(1).setPreferredWidth(209);
				table.getColumnModel().getColumn(2).setPreferredWidth(330);
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

	protected TableModel getTableModel() {
		return table.getModel();
	}
	protected void setTableModel(TableModel model) {
		table.setModel(model);
	}
}
