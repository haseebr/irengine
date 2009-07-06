package tudelft.nl.ir.gui;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import tudelft.nl.ir.docs.Document;
import tudelft.nl.ir.index.Index;
import tudelft.nl.ir.index.InvertedIndex;
import tudelft.nl.ir.query.SimpleAndQuery;

public class IRGui extends JFrame {

	private JPanel contentPane;
	private JTextField textField;
	private JTable table;
	private DefaultTableModel model;

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
		
		// Initialize the contentPane.
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setBounds(100, 100, 450, 300);
		this.contentPane = new JPanel();
		this.contentPane.setLayout(null);
		this.contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		this.setContentPane(this.contentPane);
		
//		final JTextArea textArea = new JTextArea();
//		textArea.setAutoscrolls(true);
		{
			JButton button = new JButton("Search");
			button.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent arg0) {
//					textArea.setText("searching...");
					
					Index index = new InvertedIndex();
					String query = textField.getText();
					SimpleAndQuery t = new SimpleAndQuery(query);
					ArrayList<Document> results = (ArrayList<Document>) t.evaluate(index);
					
//					textArea.setText("finished query '"+ query +"'");
					for(int i = 0; i < results.size(); i++ ){
						model.addRow(new Object[]{results.get(i).getID()+"", results.get(i).getTitle()});
//						textArea.append(results.get(i).getID() + ": "+results.get(i).getTitle()+"\n");
					}
				}
			});
			button.setBounds(317, 5, 128, 28);
			button.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
				}
			});
		
			{
				textField = new JTextField();
				textField.setBounds(5, 5, 306, 28);
				contentPane.add(textField);
				textField.setColumns(1);
			}
			
			contentPane.add(button);
			
			{
				model = new DefaultTableModel();
				table = new JTable(model);
				
				model.addColumn("ID");
				model.addColumn("Document ID");
				table.doLayout();
				
				model.addRow(new Object[]{"v1", "v2"});

			    JScrollPane scrollPane = new JScrollPane(table);

			    scrollPane.setBounds(10, 52, 430, 216);
				contentPane.add(scrollPane);
			}
		}
		
		
	}
}
