package tudelft.nl.ir.gui;
import java.awt.BorderLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import tudelft.nl.ir.docs.DocumentImpl;
import tudelft.nl.ir.index.InvertedIndex;
import javax.swing.JTextPane;
import java.awt.GridLayout;
import javax.swing.JScrollPane;

public class IRDocumentFrame extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private String m_DocumentId;
	private String m_Term;
	private JTextPane bodyTextPane;
	private JTextPane snippetTextPane;

	/**
	 * Launch the application.
	 */
//	public static void main(String[] args) {
//		EventQueue.invokeLater(new Runnable() {
//			public void run() {
//				try {
//					IRDocumentFrame frame = new IRDocumentFrame();
//					frame.setVisible(true);
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//			}
//		});
//	}

	/**
	 * Create the frame.
	 */
	public IRDocumentFrame(String document_id, String term) {
		//setDefaultCloseOperation(JFrame.);
		setBounds(100, 100, 600, 600);
		contentPane = new JPanel();
		contentPane.setLayout(new GridLayout(2, 0, 0, 0));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		{
			JPanel snippetPanel = new JPanel();
			snippetPanel.setLayout(new BorderLayout(0, 0));
			contentPane.add(snippetPanel);
			{
				JLabel lblSnippet = new JLabel("Snippet:");
				snippetPanel.add(lblSnippet, BorderLayout.NORTH);
			}
			{
				JScrollPane scrollPane = new JScrollPane();
				snippetPanel.add(scrollPane, BorderLayout.CENTER);
				{
					snippetTextPane = new JTextPane();
					scrollPane.setViewportView(snippetTextPane);
				}
			}
		}
		{
			JPanel bodyPanel = new JPanel();
			bodyPanel.setLayout(new BorderLayout(0, 0));
			contentPane.add(bodyPanel);
			{
				JLabel lblBody = new JLabel("Body:");
				bodyPanel.add(lblBody, BorderLayout.NORTH);
			}
			{
				JScrollPane scrollPane = new JScrollPane();
				bodyPanel.add(scrollPane, BorderLayout.CENTER);
				{
					bodyTextPane = new JTextPane();
					scrollPane.setViewportView(bodyTextPane);
				}
			}
		}
		
		this.m_DocumentId = document_id;
		this.m_Term = term;
		this.initFrame();
	}
	
	private void initFrame(){
		this.setTitle(this.m_DocumentId);
		InvertedIndex index = new InvertedIndex();
		DocumentImpl doc = index.getDocument(this.m_DocumentId);
		this.setTitle(doc.getTitle());
		this.getSnippetTextPane().setText(doc.getSnippet(this.m_Term, index));
		this.getBodyTextPane().setText(doc.getContent());
		this.repaint();
	}
	protected JTextPane getBodyTextPane() {
		return bodyTextPane;
	}
	protected JTextPane getSnippetTextPane() {
		return snippetTextPane;
	}
}
