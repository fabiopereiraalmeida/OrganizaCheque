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
import java.text.ParseException;
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
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.text.MaskFormatter;

import br.com.grupocaravela.aguarde.EsperaLista;
import br.com.grupocaravela.configuracao.EntityManagerProducer;
import br.com.grupocaravela.ferramenta.UsuarioLogado;
import br.com.grupocaravela.objeto.Usuario;
import br.com.grupocaravela.tablemodel.TableModelUsuario;

public class JanelaUsuario extends JFrame {

	private JPanel contentPane;
	private JTable table;
	private JTextField tfNome;
	
	private EntityManager manager;
	private EntityTransaction trx;
	private TableModelUsuario tableModelUsuario;
	
	private Usuario usuario = UsuarioLogado.getUsuario();
	private JTabbedPane tabbedPane;
	private JTextField tfLocalizar;
	private JCheckBox chbAdminitrador;
	private JPasswordField passwordField;
	private JTextField tfUsuario;
	
	private Usuario usuarioLogado = UsuarioLogado.getUsuario();
	private JButton btnExcluir;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					JanelaUsuario frame = new JanelaUsuario();
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
	public JanelaUsuario() {
		
		carregarJanela();
		carregarTableModel();
		iniciaConexao();
		
		table.setAutoCreateRowSorter(true);
		
		if (usuario.getAdiministrador()) {
			btnExcluir.setEnabled(true);
		}
	}
		
	private void carregarJanela(){
		setTitle("Usuarios");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 700, 450);
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
				.addComponent(tabbedPane, GroupLayout.PREFERRED_SIZE, 688, Short.MAX_VALUE)
		);
		gl_panel.setVerticalGroup(
			gl_panel.createParallelGroup(Alignment.LEADING)
				.addComponent(tabbedPane, GroupLayout.DEFAULT_SIZE, 386, Short.MAX_VALUE)
		);
		
		JPanel panel_1 = new JPanel();
		tabbedPane.addTab("Relação de Usuarios", null, panel_1, null);
		
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
		btnFiltrar.setIcon(new ImageIcon(JanelaUsuario.class.getResource("/br/com/grupocaravela/icones/lupa_24.png")));
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
				
				botaoNovoUsuario();
				
			}
		});
		btnNovo.setIcon(new ImageIcon(JanelaUsuario.class.getResource("/br/com/grupocaravela/icones/novo_24.png")));
		
		JButton btnDetalhes = new JButton("Detalhes");
		btnDetalhes.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				botaoDetalhes();
				
			}
		});
		btnDetalhes.setIcon(new ImageIcon(JanelaUsuario.class.getResource("/br/com/grupocaravela/icones/alterar_24.png")));
		
		btnExcluir = new JButton("Excluir");
		btnExcluir.setEnabled(false);
		btnExcluir.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				Object[] options = { "Sim", "Não" };
				int i = JOptionPane.showOptionDialog(null,
						"ATENÇÃO!!! Esta operação irá excluir o usuario selecionado! Gostaria de continuar?",
						"Excluir", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options,
						options[0]);

				if (i == JOptionPane.YES_OPTION) {
					botaoExcluir();
				}		
				
			}
		});
		btnExcluir.setIcon(new ImageIcon(JanelaUsuario.class.getResource("/br/com/grupocaravela/icones/alerta_vermelho_24.png")));
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
		tabbedPane.addTab("Detalhes do Usuario", null, panel_2, null);
		
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
				.addGroup(Alignment.TRAILING, gl_panel_5.createSequentialGroup()
					.addGroup(gl_panel_5.createParallelGroup(Alignment.TRAILING)
						.addComponent(panel_7, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 687, Short.MAX_VALUE)
						.addComponent(panel_6, GroupLayout.PREFERRED_SIZE, 687, Short.MAX_VALUE))
					.addContainerGap())
		);
		gl_panel_5.setVerticalGroup(
			gl_panel_5.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_panel_5.createSequentialGroup()
					.addComponent(panel_6, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(panel_7, GroupLayout.PREFERRED_SIZE, 57, GroupLayout.PREFERRED_SIZE)
					.addContainerGap())
		);
		
		JButton btnSalvar = new JButton("Salvar");
		btnSalvar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				Object[] options = { "Sim", "Não" };
				int i = JOptionPane.showOptionDialog(null,
						"Gostaria de salvar o usuario de nome " + tfNome.getText() + "?", "Salvar",
						JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);

				if (i == JOptionPane.YES_OPTION) {

					salvarUsuario(usuario);
				}
								
			}
		});
		btnSalvar.setIcon(new ImageIcon(JanelaUsuario.class.getResource("/br/com/grupocaravela/icones/alerta_verde_24.png")));
		
		JButton btnCancelar = new JButton("Cancelar");
		btnCancelar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				tabbedPane.setSelectedIndex(0);
				usuario = null;
				limparCampos();
				
			}
		});
		btnCancelar.setIcon(new ImageIcon(JanelaUsuario.class.getResource("/br/com/grupocaravela/icones/alerta_amarelo_24.png")));
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
		
		JLabel lblCod = new JLabel("Nome:");
		
		tfNome = new JTextField();
		tfNome.setColumns(10);
		
		JLabel lblTelefone = new JLabel("Usuario:");
		
		try {
			MaskFormatter masktel8 = new MaskFormatter("(##) ####-####");
		} catch (ParseException e2) {
			e2.printStackTrace();
		}
		
		try {
			MaskFormatter maskCep = new MaskFormatter("##.###-###");
		} catch (ParseException e1) {			
			e1.printStackTrace();
		}
		
		JLabel lblObservao = new JLabel("Senha:");
		
		chbAdminitrador = new JCheckBox("Adminitrador");
		
		passwordField = new JPasswordField();
		
		tfUsuario = new JTextField();
		tfUsuario.setColumns(10);
		
		GroupLayout gl_panel_6 = new GroupLayout(panel_6);
		gl_panel_6.setHorizontalGroup(
			gl_panel_6.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_6.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_panel_6.createParallelGroup(Alignment.LEADING)
						.addComponent(lblCod)
						.addGroup(gl_panel_6.createSequentialGroup()
							.addGroup(gl_panel_6.createParallelGroup(Alignment.LEADING)
								.addComponent(lblTelefone)
								.addComponent(tfUsuario, GroupLayout.PREFERRED_SIZE, 185, GroupLayout.PREFERRED_SIZE))
							.addGap(18)
							.addGroup(gl_panel_6.createParallelGroup(Alignment.LEADING)
								.addComponent(lblObservao)
								.addGroup(gl_panel_6.createSequentialGroup()
									.addComponent(passwordField, GroupLayout.PREFERRED_SIZE, 198, GroupLayout.PREFERRED_SIZE)
									.addGap(18)
									.addComponent(chbAdminitrador))))
						.addComponent(tfNome, GroupLayout.PREFERRED_SIZE, 610, GroupLayout.PREFERRED_SIZE))
					.addContainerGap(166, Short.MAX_VALUE))
		);
		gl_panel_6.setVerticalGroup(
			gl_panel_6.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_6.createSequentialGroup()
					.addContainerGap()
					.addComponent(lblCod)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(tfNome, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addGap(18)
					.addGroup(gl_panel_6.createParallelGroup(Alignment.TRAILING)
						.addComponent(chbAdminitrador)
						.addGroup(gl_panel_6.createSequentialGroup()
							.addGroup(gl_panel_6.createParallelGroup(Alignment.TRAILING)
								.addComponent(passwordField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
								.addGroup(gl_panel_6.createSequentialGroup()
									.addComponent(lblObservao)
									.addGap(21))
								.addGroup(gl_panel_6.createSequentialGroup()
									.addComponent(lblTelefone)
									.addPreferredGap(ComponentPlacement.RELATED)
									.addComponent(tfUsuario, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)))
							.addPreferredGap(ComponentPlacement.RELATED)))
					.addGap(196))
		);
		panel_6.setLayout(gl_panel_6);
		panel_5.setLayout(gl_panel_5);
		panel_2.setLayout(gl_panel_2);
		panel.setLayout(gl_panel);
		contentPane.setLayout(gl_contentPane);
	}

	private void carregarTableModel() {
		this.tableModelUsuario = new TableModelUsuario();
		this.table.setModel(tableModelUsuario);
	}
	
	private void iniciaConexao() {

		// factory = Persistence.createEntityManagerFactory("DesbravarPU");
		manager = EntityManagerProducer.createEntityManager();
		trx = manager.getTransaction();
	}
	
	private void botaoNovoUsuario(){
		
		limparCampos();
		usuario = new Usuario();
		tabbedPane.setSelectedIndex(1);
		tfNome.requestFocus();
		
	}
	
	private void botaoSalvar(){
		
		salvarUsuario(usuario);
		tabbedPane.setSelectedIndex(0);
		limparCampos();
	}
	
	private void botaoExcluir(){
		
		int linha = table.getSelectedRow();
        int linhaReal = table.convertRowIndexToModel(linha);
		
		usuario = tableModelUsuario.getUsuario(linhaReal);
		excluirUsuario(usuario);
		limparTabela();
	}
	
	private void botaoDetalhes(){
		
		int linha = table.getSelectedRow();
        int linhaReal = table.convertRowIndexToModel(linha);
		
		usuario = tableModelUsuario.getUsuario(linhaReal);
		carregarCampos(usuario);
		tabbedPane.setSelectedIndex(1);
	}
	
	private void carregarCampos(Usuario u){
		tfNome.setText(u.getNome());
		tfUsuario.setText(u.getUsuario());
		passwordField.setText(u.getSenha());
		
		if (u.getAdiministrador()) {
			chbAdminitrador.setSelected(true);
		}else{
			chbAdminitrador.setSelected(false);
		}
	}
	
	private void limparCampos(){
		tfNome.setText("");
		tfUsuario.setText("");
		passwordField.setText("");
		chbAdminitrador.setSelected(false);
	}
	
	private void salvarUsuario(Usuario u) {
		try {
			
			u.setNome(tfNome.getText());
			u.setUsuario(tfUsuario.getText());
			u.setSenha(passwordField.getText());
			
			if (chbAdminitrador.isSelected()) {
				u.setAdiministrador(true);
			}else{
				u.setAdiministrador(false);
			}
			
			trx.begin();
			manager.persist(u);
			trx.commit();

			JOptionPane.showMessageDialog(null, "O Usuario foi salvo com sucesso!");
			
			tabbedPane.setSelectedIndex(0);
			limparCampos();
			
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "ERRO! " + e);
		}
	}
	
	private void excluirUsuario(Usuario b) {
		try {
		
			trx.begin();
			manager.remove(b);
			trx.commit();

			JOptionPane.showMessageDialog(null, "O Usuario foi excluido com sucesso!");
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "ERRO! " + e);
		}
	}
	
	private void limparTabela() {
		while (table.getModel().getRowCount() > 0) {
			tableModelUsuario.removeUsuario(0);
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
					Logger.getLogger(JanelaUsuario.class.getName()).log(Level.SEVERE, null, ex);
				}
				// ######################METODO A SER
				// EXECUTADO##############################
				limparTabela();

				try {

					// trx.begin();
					Query consulta = manager
							.createQuery("FROM Usuario WHERE nome LIKE '%" + tfLocalizar.getText() + "%'");
					List<Usuario> listaUsuarios = consulta.getResultList();
					// trx.commit();

					for (int i = 0; i < listaUsuarios.size(); i++) {
						Usuario b = listaUsuarios.get(i);
						tableModelUsuario.addUsuario(b);
					}
				} catch (Exception e) {
					JOptionPane.showMessageDialog(null, "Erro ao carregar a tabela de Usuarios: " + e);
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

		// ###############################################]

	}
	
	private void verificarUsuario(){
		
		if (usuarioLogado.getAdiministrador()) {
			btnExcluir.setEnabled(true);
		}
		
	}
}
