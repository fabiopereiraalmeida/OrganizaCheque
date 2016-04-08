package br.com.grupocaravela.dialog;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import br.com.grupocaravela.aguarde.EsperaLista;
import br.com.grupocaravela.configuracao.EntityManagerProducer;
import br.com.grupocaravela.objeto.Agencia;
import br.com.grupocaravela.objeto.Destinatario;
import br.com.grupocaravela.objeto.Observacao;
import br.com.grupocaravela.objeto.Proprietario;
import br.com.grupocaravela.tablemodel.TableModelDestinatario;
import br.com.grupocaravela.tablemodel.TableModelProprietario;
import br.com.grupocaravela.view.JanelaProprietario;
import javax.swing.JTextPane;
import javax.swing.JComboBox;
import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;

public class ObservacaoCheque extends JDialog {

	private static final long serialVersionUID = 1L;

	// private EntityManagerFactory factory;
	private EntityManager manager;
	private EntityTransaction trx;

	private final JPanel contentPanel = new JPanel();

	private TableModelDestinatario tableModelDestinatario;
	private String observacao;
	private String info = "";

	private JTextPane textPane;

	private JComboBox cbObservacao;
	

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			ObservacaoCheque dialog = new ObservacaoCheque();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * Create the dialog.
	 */
	public ObservacaoCheque() {
		
		criarJanela();
		//carregarTableModel();
		//lblInfo.setText(info);
		iniciaConexao();
		carregaObservacoes();

		// Evento ao abrir a janela
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowActivated(WindowEvent e) {
				
				super.windowActivated(e);
			}
		});
		/*
		 * //Evento ao fechar a janela addWindowListener(new WindowAdapter() {
		 * 
		 * @Override public void windowClosing(WindowEvent e) { try {
		 * factory.close(); } catch (Exception e2) { // TODO: handle exception }
		 * 
		 * } });
		 */
	}

	private void iniciaConexao() {

		// factory = Persistence.createEntityManagerFactory("DesbravarPU");
		manager = EntityManagerProducer.createEntityManager();
		trx = manager.getTransaction();
	}
	
	private void criarJanela() {
		
		setIconImage(Toolkit.getDefaultToolkit()
				.getImage(ObservacaoCheque.class.getResource("/br/com/grupocaravela/icones/logoCaravela.png")));
		setTitle("Obseração de cheques");
		setAlwaysOnTop(true);
		
		setBounds(100, 100, 601, 241);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);

		JPanel panel = new JPanel();
		GroupLayout gl_contentPanel = new GroupLayout(contentPanel);
		gl_contentPanel.setHorizontalGroup(gl_contentPanel.createParallelGroup(Alignment.LEADING).addComponent(panel,
				GroupLayout.DEFAULT_SIZE, 688, Short.MAX_VALUE));
		gl_contentPanel.setVerticalGroup(gl_contentPanel.createParallelGroup(Alignment.LEADING).addComponent(panel,
				GroupLayout.DEFAULT_SIZE, 398, Short.MAX_VALUE));

		JPanel panel_1 = new JPanel();
		panel_1.setBorder(new LineBorder(Color.DARK_GRAY));

		JButton btnMovimentar = new JButton("Ok");
		btnMovimentar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				importar();
			}
		});
		btnMovimentar.setIcon(new ImageIcon(ObservacaoCheque.class.getResource("/br/com/grupocaravela/icones/aprovar_24.png")));
		GroupLayout gl_panel_1 = new GroupLayout(panel_1);
		gl_panel_1.setHorizontalGroup(
			gl_panel_1.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_panel_1.createSequentialGroup()
					.addContainerGap(470, Short.MAX_VALUE)
					.addComponent(btnMovimentar)
					.addContainerGap())
		);
		gl_panel_1.setVerticalGroup(
			gl_panel_1.createParallelGroup(Alignment.LEADING)
				.addGroup(Alignment.TRAILING, gl_panel_1.createSequentialGroup()
					.addContainerGap(21, Short.MAX_VALUE)
					.addComponent(btnMovimentar)
					.addContainerGap())
		);
		panel_1.setLayout(gl_panel_1);
		
		JPanel panel_2 = new JPanel();
		panel_2.setBorder(new LineBorder(new Color(0, 0, 0)));
		
		cbObservacao = new JComboBox();
		cbObservacao.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				
				carregarTextPane((Observacao) cbObservacao.getSelectedItem());
				
			}
		});
		
		JLabel lblObservao = new JLabel("Observação:");
		GroupLayout gl_panel = new GroupLayout(panel);
		gl_panel.setHorizontalGroup(
			gl_panel.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_panel.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
						.addComponent(panel_2, GroupLayout.DEFAULT_SIZE, 565, Short.MAX_VALUE)
						.addGroup(gl_panel.createSequentialGroup()
							.addComponent(lblObservao)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(cbObservacao, GroupLayout.PREFERRED_SIZE, 386, GroupLayout.PREFERRED_SIZE))
						.addComponent(panel_1, GroupLayout.DEFAULT_SIZE, 565, Short.MAX_VALUE))
					.addContainerGap())
		);
		gl_panel.setVerticalGroup(
			gl_panel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel.createSequentialGroup()
					.addGap(27)
					.addGroup(gl_panel.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblObservao)
						.addComponent(cbObservacao, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGap(18)
					.addComponent(panel_2, GroupLayout.DEFAULT_SIZE, 51, Short.MAX_VALUE)
					.addGap(9)
					.addComponent(panel_1, GroupLayout.PREFERRED_SIZE, 58, GroupLayout.PREFERRED_SIZE)
					.addContainerGap())
		);
		
		textPane = new JTextPane();
		GroupLayout gl_panel_2 = new GroupLayout(panel_2);
		gl_panel_2.setHorizontalGroup(
			gl_panel_2.createParallelGroup(Alignment.LEADING)
				.addComponent(textPane, GroupLayout.DEFAULT_SIZE, 565, Short.MAX_VALUE)
		);
		gl_panel_2.setVerticalGroup(
			gl_panel_2.createParallelGroup(Alignment.LEADING)
				.addComponent(textPane, GroupLayout.DEFAULT_SIZE, 134, Short.MAX_VALUE)
		);
		panel_2.setLayout(gl_panel_2);
		panel.setLayout(gl_panel);
		contentPanel.setLayout(gl_contentPanel);
	}
	
	private void carregaObservacoes() {

		cbObservacao.removeAllItems();

		try {

			//trx.begin();
			Query consulta = manager.createQuery("FROM Observacao");
			List<Observacao> listaObservacoes = consulta.getResultList();
			//trx.commit();

			for (int i = 0; i < listaObservacoes.size(); i++) {

				Observacao o = listaObservacoes.get(i);
				cbObservacao.addItem(o);
			}
			
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Erro no carregamento das observações! " + e);
		}
	}
	
	private void carregarTextPane(Observacao ob){
		
		textPane.setText("");
		textPane.setText(ob.getObservacao());
		
	}

	private void carregarTableModel() {
		this.tableModelDestinatario = new TableModelDestinatario();

	}

	private void importar() {
		observacao = textPane.getText();
		dispose();
	}

	public String getObservacao() {
		return observacao;
	}

	public void setInfo(String info) {
		this.info = info;
	}
}
