package br.com.grupocaravela.view;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
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
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import br.com.grupocaravela.aguarde.EsperaLista;
import br.com.grupocaravela.configuracao.EntityManagerProducer;
import br.com.grupocaravela.ferramenta.UsuarioLogado;
import br.com.grupocaravela.objeto.Agencia;
import br.com.grupocaravela.objeto.Banco;
import br.com.grupocaravela.objeto.Conta;
import br.com.grupocaravela.objeto.Usuario;
import br.com.grupocaravela.tablemodel.TableModelConta;
import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;

public class JanelaConta extends JFrame {

	private JPanel contentPane;
	private JTable table;
	private JTextField tfNumero;
	
	private EntityManager manager;
	private EntityTransaction trx;
	private TableModelConta tableModelConta;
	
	private Conta conta;
	private JTabbedPane tabbedPane;
	private JTextField tfLocalizar;
	private JComboBox cbBanco;
	private JComboBox cbAgencia;
	private JButton btnExcluir;
	
	private Usuario usuario = UsuarioLogado.getUsuario();
	private JTextField tfCodAgencia;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					JanelaConta frame = new JanelaConta();
					frame.setVisible(true);
					frame.setLocationRelativeTo(null);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public JanelaConta() {
		
		carregarJanela();
		carregarTableModel();
		iniciaConexao();
		
		carregajcbAgencia();
		carregajcbBanco();
		
		table.setAutoCreateRowSorter(true);
		
		if (usuario.getAdiministrador()) {
			btnExcluir.setEnabled(true);
		}
	}
		
	private void carregarJanela(){
		setTitle("Contas");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 700, 400);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		
		JPanel panel = new JPanel();
		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addComponent(panel, GroupLayout.DEFAULT_SIZE, 688, Short.MAX_VALUE)
		);
		gl_contentPane.setVerticalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addComponent(panel, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 378, Short.MAX_VALUE)
		);
		
		tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		GroupLayout gl_panel = new GroupLayout(panel);
		gl_panel.setHorizontalGroup(
			gl_panel.createParallelGroup(Alignment.LEADING)
				.addComponent(tabbedPane, GroupLayout.DEFAULT_SIZE, 688, Short.MAX_VALUE)
		);
		gl_panel.setVerticalGroup(
			gl_panel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel.createSequentialGroup()
					.addComponent(tabbedPane, GroupLayout.PREFERRED_SIZE, 366, GroupLayout.PREFERRED_SIZE)
					.addContainerGap(24, Short.MAX_VALUE))
		);
		
		JPanel panel_1 = new JPanel();
		tabbedPane.addTab("Relação de contas", null, panel_1, null);
		
		JPanel panel_3 = new JPanel();
		
		JPanel panel_4 = new JPanel();
		panel_4.setBorder(new LineBorder(new Color(0, 0, 0)));
		
		JLabel lblLocalizar = new JLabel("Localizar:");
		
		tfLocalizar = new JTextField();
		tfLocalizar.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					buscarNome();
				}
			}
		});
		tfLocalizar.addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent e) {
				
				tfLocalizar.selectAll();
				
			}
		});
		tfLocalizar.setColumns(10);
		
		JButton btnFiltrar = new JButton("Filtrar");
		btnFiltrar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				buscarNome();
				
			}
		});
		btnFiltrar.setIcon(new ImageIcon(JanelaConta.class.getResource("/br/com/grupocaravela/icones/lupa_24.png")));
		GroupLayout gl_panel_1 = new GroupLayout(panel_1);
		gl_panel_1.setHorizontalGroup(
			gl_panel_1.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_panel_1.createSequentialGroup()
					.addContainerGap()
					.addComponent(lblLocalizar)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(tfLocalizar, GroupLayout.DEFAULT_SIZE, 461, Short.MAX_VALUE)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(btnFiltrar)
					.addContainerGap())
				.addComponent(panel_3, GroupLayout.DEFAULT_SIZE, 683, Short.MAX_VALUE)
				.addComponent(panel_4, GroupLayout.DEFAULT_SIZE, 683, Short.MAX_VALUE)
		);
		gl_panel_1.setVerticalGroup(
			gl_panel_1.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_1.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_panel_1.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblLocalizar)
						.addComponent(tfLocalizar, GroupLayout.PREFERRED_SIZE, 26, GroupLayout.PREFERRED_SIZE)
						.addComponent(btnFiltrar, GroupLayout.PREFERRED_SIZE, 34, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(panel_3, GroupLayout.DEFAULT_SIZE, 221, Short.MAX_VALUE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(panel_4, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
		);
		
		JButton btnNovo = new JButton("Novo");
		btnNovo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				botaoNovoConta();
				
			}
		});
		btnNovo.setIcon(new ImageIcon(JanelaConta.class.getResource("/br/com/grupocaravela/icones/novo_24.png")));
		
		JButton btnDetalhes = new JButton("Detalhes");
		btnDetalhes.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				botaoDetalhes();
				
			}
		});
		btnDetalhes.setIcon(new ImageIcon(JanelaConta.class.getResource("/br/com/grupocaravela/icones/alterar_24.png")));
		
		btnExcluir = new JButton("Excluir");
		btnExcluir.setEnabled(false);
		btnExcluir.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				Object[] options = { "Sim", "Não" };
				int i = JOptionPane.showOptionDialog(null,
						"ATENÇÃO!!! Esta operação irá excluir a conta selecionada! Gostaria de continuar?",
						"Excluir", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options,
						options[0]);

				if (i == JOptionPane.YES_OPTION) {
					botaoExcluir();
				}		
				
			}
		});
		btnExcluir.setIcon(new ImageIcon(JanelaConta.class.getResource("/br/com/grupocaravela/icones/alerta_vermelho_24.png")));
		GroupLayout gl_panel_4 = new GroupLayout(panel_4);
		gl_panel_4.setHorizontalGroup(
			gl_panel_4.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_panel_4.createSequentialGroup()
					.addContainerGap(327, Short.MAX_VALUE)
					.addComponent(btnExcluir)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(btnDetalhes)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(btnNovo)
					.addContainerGap())
		);
		gl_panel_4.setVerticalGroup(
			gl_panel_4.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_panel_4.createSequentialGroup()
					.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
					.addGroup(gl_panel_4.createParallelGroup(Alignment.BASELINE)
						.addComponent(btnNovo)
						.addComponent(btnDetalhes)
						.addComponent(btnExcluir))
					.addContainerGap())
		);
		panel_4.setLayout(gl_panel_4);
		
		JScrollPane scrollPane = new JScrollPane();
					
		table = new JTable();
		table.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2) {

					botaoDetalhes();

				}
			}
		});
		scrollPane.setViewportView(table);
		GroupLayout gl_panel_3 = new GroupLayout(panel_3);
		gl_panel_3.setHorizontalGroup(
			gl_panel_3.createParallelGroup(Alignment.LEADING)
				.addComponent(scrollPane, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 683, Short.MAX_VALUE)
		);
		gl_panel_3.setVerticalGroup(
			gl_panel_3.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_3.createSequentialGroup()
					.addGap(5)
					.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 241, Short.MAX_VALUE))
		);
		panel_3.setLayout(gl_panel_3);
		panel_1.setLayout(gl_panel_1);
		
		JPanel panel_2 = new JPanel();
		tabbedPane.addTab("Detalhes do conta", null, panel_2, null);
		
		JPanel panel_5 = new JPanel();
		GroupLayout gl_panel_2 = new GroupLayout(panel_2);
		gl_panel_2.setHorizontalGroup(
			gl_panel_2.createParallelGroup(Alignment.LEADING)
				.addComponent(panel_5, GroupLayout.DEFAULT_SIZE, 683, Short.MAX_VALUE)
		);
		gl_panel_2.setVerticalGroup(
			gl_panel_2.createParallelGroup(Alignment.LEADING)
				.addComponent(panel_5, GroupLayout.DEFAULT_SIZE, 344, Short.MAX_VALUE)
		);
		
		JPanel panel_6 = new JPanel();
		
		JPanel panel_7 = new JPanel();
		panel_7.setBorder(new LineBorder(new Color(0, 0, 0)));
		GroupLayout gl_panel_5 = new GroupLayout(panel_5);
		gl_panel_5.setHorizontalGroup(
			gl_panel_5.createParallelGroup(Alignment.LEADING)
				.addComponent(panel_7, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 683, Short.MAX_VALUE)
				.addComponent(panel_6, GroupLayout.DEFAULT_SIZE, 683, Short.MAX_VALUE)
		);
		gl_panel_5.setVerticalGroup(
			gl_panel_5.createParallelGroup(Alignment.LEADING)
				.addGroup(Alignment.TRAILING, gl_panel_5.createSequentialGroup()
					.addComponent(panel_6, GroupLayout.DEFAULT_SIZE, 269, Short.MAX_VALUE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(panel_7, GroupLayout.PREFERRED_SIZE, 57, GroupLayout.PREFERRED_SIZE)
					.addContainerGap())
		);
		
		JButton btnSalvar = new JButton("Salvar");
		btnSalvar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				salvarConta(conta);
				tabbedPane.setSelectedIndex(0);
				limparCampos();
				
			}
		});
		btnSalvar.setIcon(new ImageIcon(JanelaConta.class.getResource("/br/com/grupocaravela/icones/alerta_verde_24.png")));
		
		JButton btnCancelar = new JButton("Cancelar");
		btnCancelar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				tabbedPane.setSelectedIndex(0);
				conta = null;
				limparCampos();
				
			}
		});
		btnCancelar.setIcon(new ImageIcon(JanelaConta.class.getResource("/br/com/grupocaravela/icones/alerta_amarelo_24.png")));
		GroupLayout gl_panel_7 = new GroupLayout(panel_7);
		gl_panel_7.setHorizontalGroup(
			gl_panel_7.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_panel_7.createSequentialGroup()
					.addContainerGap(433, Short.MAX_VALUE)
					.addComponent(btnCancelar)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(btnSalvar)
					.addContainerGap())
		);
		gl_panel_7.setVerticalGroup(
			gl_panel_7.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_panel_7.createSequentialGroup()
					.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
					.addGroup(gl_panel_7.createParallelGroup(Alignment.BASELINE)
						.addComponent(btnSalvar)
						.addComponent(btnCancelar))
					.addContainerGap())
		);
		panel_7.setLayout(gl_panel_7);
		
		JLabel lblCod = new JLabel("Numero:");
		
		JLabel lblNome = new JLabel("Agência:");
		
		tfNumero = new JTextField();
		tfNumero.setColumns(10);
		
		cbAgencia = new JComboBox();
		cbAgencia.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				
				cbBanco.setSelectedItem(buscarBanco((Agencia) cbAgencia.getSelectedItem()));
			}
		});
		
		JLabel lblBanco = new JLabel("Banco:");
		
		cbBanco = new JComboBox();
		
		tfCodAgencia = new JTextField();
		tfCodAgencia.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				
				//carregajcbAgenciaCod(tfCodAgencia.getText());
				
			}
			@Override
			public void keyReleased(KeyEvent e) {
				
				carregajcbAgenciaCod(tfCodAgencia.getText());
				
			}
		});
		tfCodAgencia.setColumns(10);
		GroupLayout gl_panel_6 = new GroupLayout(panel_6);
		gl_panel_6.setHorizontalGroup(
			gl_panel_6.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_6.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_panel_6.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_panel_6.createSequentialGroup()
							.addGroup(gl_panel_6.createParallelGroup(Alignment.LEADING)
								.addComponent(tfNumero, GroupLayout.PREFERRED_SIZE, 148, GroupLayout.PREFERRED_SIZE)
								.addComponent(lblCod))
							.addGap(18)
							.addGroup(gl_panel_6.createParallelGroup(Alignment.LEADING)
								.addComponent(lblNome)
								.addGroup(gl_panel_6.createSequentialGroup()
									.addComponent(tfCodAgencia, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
									.addPreferredGap(ComponentPlacement.RELATED)
									.addComponent(cbAgencia, GroupLayout.PREFERRED_SIZE, 285, GroupLayout.PREFERRED_SIZE))))
						.addComponent(lblBanco)
						.addComponent(cbBanco, GroupLayout.PREFERRED_SIZE, 388, GroupLayout.PREFERRED_SIZE))
					.addContainerGap(100, Short.MAX_VALUE))
		);
		gl_panel_6.setVerticalGroup(
			gl_panel_6.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_6.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_panel_6.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblCod)
						.addComponent(lblNome))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_panel_6.createParallelGroup(Alignment.BASELINE)
						.addComponent(tfNumero, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(tfCodAgencia, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(cbAgencia, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGap(18)
					.addComponent(lblBanco)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(cbBanco, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addContainerGap(144, Short.MAX_VALUE))
		);
		panel_6.setLayout(gl_panel_6);
		panel_5.setLayout(gl_panel_5);
		panel_2.setLayout(gl_panel_2);
		panel.setLayout(gl_panel);
		contentPane.setLayout(gl_contentPane);
	}

	private void carregarTableModel() {
		this.tableModelConta = new TableModelConta();
		this.table.setModel(tableModelConta);
	}
	
	private void iniciaConexao() {

		// factory = Persistence.createEntityManagerFactory("DesbravarPU");
		manager = EntityManagerProducer.createEntityManager();
		trx = manager.getTransaction();
	}
	
	private void botaoNovoConta(){
		
		limparCampos();
		conta = new Conta();
		tabbedPane.setSelectedIndex(1);
		tfNumero.requestFocus();
		
	}
	
	private void botaoSalvar(){
		
		salvarConta(conta);
		tabbedPane.setSelectedIndex(0);
		limparCampos();
	}
	
	private void botaoExcluir(){
		
		int linha = table.getSelectedRow();
        int linhaReal = table.convertRowIndexToModel(linha);
		
		conta = tableModelConta.getConta(linhaReal);
		excluirConta(conta);
		limparTabela();
	}
	
	private void botaoDetalhes(){
		
		int linha = table.getSelectedRow();
        int linhaReal = table.convertRowIndexToModel(linha);
		
		conta = tableModelConta.getConta(linhaReal);
		carregarCampos(conta);
		tabbedPane.setSelectedIndex(1);
	}
	
	private void carregarCampos(Conta c){
		tfNumero.setText(c.getNumero());
		cbAgencia.setSelectedItem(c.getAgencia());
		cbBanco.setSelectedItem(c.getBanco());
	}
	
	private void limparCampos(){
		tfNumero.setText("");
		cbAgencia.setSelectedIndex(-1);
		cbBanco.setSelectedIndex(-1);
	}
	
	private void salvarConta(Conta c) {
		try {

			c.setNumero(tfNumero.getText());
			c.setBanco((Banco) cbBanco.getSelectedItem());
			c.setAgencia((Agencia) cbAgencia.getSelectedItem());

			trx.begin();
			manager.persist(c);
			trx.commit();

			JOptionPane.showMessageDialog(null, "O conta foi salva com sucesso!");
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "ERRO! " + e);
		}
	}
	
	private void excluirConta(Conta b) {
		try {
		
			trx.begin();
			manager.remove(b);
			trx.commit();

			JOptionPane.showMessageDialog(null, "O conta foi excluida com sucesso!");
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "ERRO! " + e);
		}
	}
	
	private void limparTabela() {
		while (table.getModel().getRowCount() > 0) {
			tableModelConta.removeConta(0);
		}
	}
	
	private void buscarNome() {

		// #############################################
		final Thread tr = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					TimeUnit.SECONDS.sleep(0);
				} catch (InterruptedException ex) {
					Logger.getLogger(JanelaConta.class.getName()).log(Level.SEVERE, null, ex);
				}
				// ######################METODO A SER
				// EXECUTADO##############################
				limparTabela();

				try {

					// trx.begin();
					Query consulta = manager
							.createQuery("FROM Conta WHERE numero LIKE '%" + tfLocalizar.getText() + "%'");
					List<Conta> listaContas = consulta.getResultList();
					// trx.commit();

					for (int i = 0; i < listaContas.size(); i++) {
						Conta b = listaContas.get(i);
						tableModelConta.addConta(b);
					}
				} catch (Exception e) {
					JOptionPane.showMessageDialog(null, "Erro ao carregar a tabela de contas: " + e);
				}
				// ######################FIM METODO A SER
				// EXECUTADO##############################
			}
		});
		new Thread(new Runnable() {
			@Override
			public void run() {
				tr.start();
				// .....
				EsperaLista espera = new EsperaLista();
				espera.setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
				espera.setUndecorated(true);
				espera.setVisible(true);
				espera.setLocationRelativeTo(null);
				try {
					tr.join();
					espera.dispose();

				} catch (InterruptedException ex) {
					// Logger.getLogger(MenuView.class.getName()).log(Level.SEVERE,
					// null, ex);
				}
			}
		}).start();

		// ###############################################
	}
	
	private void carregajcbAgencia() {

		cbAgencia.removeAllItems();

		try {

			//trx.begin();
			Query consulta = manager.createQuery("FROM Agencia");
			List<Agencia> listaAgencias = consulta.getResultList();
			//trx.commit();

			for (int i = 0; i < listaAgencias.size(); i++) {

				Agencia a = listaAgencias.get(i);
				cbAgencia.addItem(a);
			}

		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Erro no carregamento das agencias! " + e);
		}
	}
	
	private void carregajcbAgenciaCod(String cod) {

		cbAgencia.removeAllItems();

		try {

			//trx.begin();
			Query consulta = manager.createQuery("FROM Agencia WHERE cod LIKE '%" + cod + "%'");
			List<Agencia> listaAgencias = consulta.getResultList();
			//trx.commit();

			for (int i = 0; i < listaAgencias.size(); i++) {

				Agencia a = listaAgencias.get(i);
				cbAgencia.addItem(a);
			}

		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Erro no carregamento das agencias! " + e);
		}
	}
	
	private void carregajcbBanco() {

		cbBanco.removeAllItems();

		try {

			//trx.begin();
			Query consulta = manager.createQuery("FROM Banco ORDER BY nome ASC");
			List<Banco> listaBancos = consulta.getResultList();
			//trx.commit();

			for (int i = 0; i < listaBancos.size(); i++) {

				Banco b = listaBancos.get(i);
				cbBanco.addItem(b);
			}

		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Erro no carregamento dos bancos! " + e);
		}
	}
	
	private Banco buscarBanco(Agencia a){
		Banco retorno = null;
		
		try {
			Query consulta = manager.createQuery("FROM Banco WHERE id LIKE '" + a.getBanco().getId() + "'");
			List<Banco> listaBancos = consulta.getResultList();
			
			retorno = listaBancos.get(0);		
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		return retorno;
	}
	
	private void verificarUsuario(){
		
		if (usuario.getAdiministrador()) {
			btnExcluir.setEnabled(true);
		}
		
	}
}
