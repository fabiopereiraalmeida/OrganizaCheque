package br.com.grupocaravela.view;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
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
import javax.swing.JFormattedTextField;
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
import javax.swing.text.MaskFormatter;

import br.com.grupocaravela.aguarde.EsperaLista;
import br.com.grupocaravela.configuracao.EntityManagerProducer;
import br.com.grupocaravela.ferramenta.UsuarioLogado;
import br.com.grupocaravela.objeto.Destinatario;
import br.com.grupocaravela.objeto.Usuario;
import br.com.grupocaravela.tablemodel.TableModelDestinatario;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;

public class JanelaDestinatario extends JFrame {

	private JPanel contentPane;
	private JTable table;
	private JTextField tfNome;
	
	private EntityManager manager;
	private EntityTransaction trx;
	private TableModelDestinatario tableModelDestinatario;
	
	private Destinatario Destinatario;
	private JTabbedPane tabbedPane;
	private JTextField tfLocalizar;
	private JTextField tfEndereco;
	private JTextField tfEnderecoNumero;
	private JTextField tfBairro;
	private JTextField tfCidade;
	private JTextField tfUf;
	private JFormattedTextField ftfCep;
	private JFormattedTextField ftfTelefone;
	private JTextField tfObservacao;
	private JCheckBox chckbxLocal;
	
	private Usuario usuario = UsuarioLogado.getUsuario();
	private JButton btnExcluir;
	private JCheckBox chckbxFilho;
	private JComboBox cbPai;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					JanelaDestinatario frame = new JanelaDestinatario();
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
	public JanelaDestinatario() {
		
		carregarJanela();
		carregarTableModel();
		iniciaConexao();
		
		carregajcbPaiTodos();
		
		table.setAutoCreateRowSorter(true);
		
		if (usuario.getAdiministrador()) {
			btnExcluir.setEnabled(true);
		}
	}
		
	private void carregarJanela(){
		setTitle("Destinatarios");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 700, 500);
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
		tabbedPane.addTab("Relação de Destinatarios", null, panel_1, null);
		
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
		btnFiltrar.setIcon(new ImageIcon(JanelaDestinatario.class.getResource("/br/com/grupocaravela/icones/lupa_24.png")));
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
				
				botaoNovoDestinatario();
				
			}
		});
		btnNovo.setIcon(new ImageIcon(JanelaDestinatario.class.getResource("/br/com/grupocaravela/icones/novo_24.png")));
		
		JButton btnDetalhes = new JButton("Detalhes");
		btnDetalhes.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				botaoDetalhes();
				
			}
		});
		btnDetalhes.setIcon(new ImageIcon(JanelaDestinatario.class.getResource("/br/com/grupocaravela/icones/alterar_24.png")));
		
		btnExcluir = new JButton("Excluir");
		btnExcluir.setEnabled(false);
		btnExcluir.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				Object[] options = { "Sim", "Não" };
				int i = JOptionPane.showOptionDialog(null,
						"ATENÇÃO!!! Esta operação irá excluir o destinatário selecionado! Gostaria de continuar?",
						"Excluir", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options,
						options[0]);

				if (i == JOptionPane.YES_OPTION) {
					botaoExcluir();
				}		
				
			}
		});
		btnExcluir.setIcon(new ImageIcon(JanelaDestinatario.class.getResource("/br/com/grupocaravela/icones/alerta_vermelho_24.png")));
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
		tabbedPane.addTab("Detalhes do Destinatario", null, panel_2, null);
		
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
								
				Object[] options = { "Sim", "Não" };
				int i = JOptionPane.showOptionDialog(null,
						"Gostaria de salvar o destinatario de nome" + tfNome.getText() + "?", "Salvar",
						JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);

				if (i == JOptionPane.YES_OPTION) {

					salvarDestinatario(Destinatario);
				}
				
			}
		});
		btnSalvar.setIcon(new ImageIcon(JanelaDestinatario.class.getResource("/br/com/grupocaravela/icones/alerta_verde_24.png")));
		
		JButton btnCancelar = new JButton("Cancelar");
		btnCancelar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				tabbedPane.setSelectedIndex(0);
				Destinatario = null;
				limparCampos();
				
			}
		});
		btnCancelar.setIcon(new ImageIcon(JanelaDestinatario.class.getResource("/br/com/grupocaravela/icones/alerta_amarelo_24.png")));
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
		tfNome.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				tfNome.setText(tfNome.getText().toUpperCase());
			}
		});
		tfNome.setColumns(10);
		
		JLabel lblTelefone = new JLabel("Telefone:");
		
		ftfTelefone = new JFormattedTextField();
		ftfTelefone.addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent e) {
				ftfTelefone.selectAll();
			}
			@Override
			public void focusLost(FocusEvent e) {
				
				String numero = ftfTelefone.getText().replace("-", "").replace(".", "").replace("/", "")
						.replace("(", "").replace(")", "");

				if (numero.length() == 8) {
					try {
						MaskFormatter mask = new MaskFormatter("####-####");
						mask.install(ftfTelefone);						
					} catch (ParseException e1) {
						e1.printStackTrace();
					}
				}

				if (numero.length() == 9) {
					try {
						MaskFormatter mask = new MaskFormatter("#.####-####");
						mask.install(ftfTelefone);
					} catch (ParseException e1) {
						e1.printStackTrace();
					}
				}

				if (numero.length() == 10) {
					try {
						MaskFormatter mask = new MaskFormatter("(##) ####-####");
						mask.install(ftfTelefone);
					} catch (ParseException e1) {
						e1.printStackTrace();
					}
				}

				if (numero.length() == 11) {
					try {
						MaskFormatter mask = new MaskFormatter("(##) #.####-####");
						mask.install(ftfTelefone);
					} catch (ParseException e1) {
						e1.printStackTrace();
					}
				}

				ftfTelefone.setText(numero);
			}
		});
		

		
		JLabel lblEndereo = new JLabel("Endereço:");
		
		tfEndereco = new JTextField();
		tfEndereco.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				tfEndereco.setText(tfEndereco.getText().toUpperCase());
			}
		});
		tfEndereco.setColumns(10);
		
		tfEnderecoNumero = new JTextField();
		tfEnderecoNumero.setColumns(10);
		
		JLabel lblNumero = new JLabel("Numero:");
		
		JLabel lblNewLabel = new JLabel("Bairro:");
		
		tfBairro = new JTextField();
		tfBairro.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				tfBairro.setText(tfBairro.getText().toUpperCase());
			}
		});
		tfBairro.setColumns(10);
		
		tfCidade = new JTextField();
		tfCidade.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				tfCidade.setText(tfCidade.getText().toUpperCase());
			}
		});
		tfCidade.setColumns(10);
		
		JLabel lblCidade = new JLabel("Cidade:");
		
		tfUf = new JTextField();
		tfUf.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				tfUf.setText(tfUf.getText().toUpperCase());
			}
		});
		tfUf.setColumns(10);
				
		
		JLabel lblUf = new JLabel("Uf:");
		
		JLabel lblCep = new JLabel("CEP:");
		
		ftfCep = new JFormattedTextField();
		ftfCep.addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent e) {
				ftfCep.selectAll();
			}
		});
		
		try {
			MaskFormatter maskCep = new MaskFormatter("##.###-###");
			maskCep.install(ftfCep);
		} catch (ParseException e1) {			
			e1.printStackTrace();
		}
		
		JLabel lblObservao = new JLabel("Observação:");
		
		tfObservacao = new JTextField();
		tfObservacao.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				tfObservacao.setText(tfObservacao.getText().toUpperCase());
			}
		});
		tfObservacao.setColumns(10);
		
		chckbxLocal = new JCheckBox("Local");
		
		chckbxFilho = new JCheckBox("Filho");
		chckbxFilho.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if (chckbxFilho.isSelected()) {
					cbPai.setEnabled(true);
				}else{
					cbPai.setEnabled(false);
				}
			}
		});
		
		cbPai = new JComboBox();
		
		JLabel lblPai = new JLabel("Pai:");
		
		GroupLayout gl_panel_6 = new GroupLayout(panel_6);
		gl_panel_6.setHorizontalGroup(
			gl_panel_6.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_6.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_panel_6.createParallelGroup(Alignment.LEADING)
						.addComponent(lblCod)
						.addGroup(gl_panel_6.createSequentialGroup()
							.addComponent(tfNome, GroupLayout.PREFERRED_SIZE, 559, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.UNRELATED)
							.addComponent(chckbxLocal))
						.addGroup(gl_panel_6.createSequentialGroup()
							.addComponent(chckbxFilho)
							.addGap(18)
							.addComponent(lblPai)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(cbPai, GroupLayout.PREFERRED_SIZE, 323, GroupLayout.PREFERRED_SIZE))
						.addComponent(tfObservacao, GroupLayout.DEFAULT_SIZE, 659, Short.MAX_VALUE)
						.addGroup(gl_panel_6.createSequentialGroup()
							.addGroup(gl_panel_6.createParallelGroup(Alignment.LEADING)
								.addComponent(tfCidade, GroupLayout.PREFERRED_SIZE, 240, GroupLayout.PREFERRED_SIZE)
								.addComponent(lblCidade))
							.addPreferredGap(ComponentPlacement.UNRELATED)
							.addGroup(gl_panel_6.createParallelGroup(Alignment.LEADING)
								.addComponent(lblUf)
								.addComponent(tfUf, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
							.addPreferredGap(ComponentPlacement.UNRELATED)
							.addGroup(gl_panel_6.createParallelGroup(Alignment.LEADING)
								.addComponent(lblCep)
								.addComponent(ftfCep, GroupLayout.PREFERRED_SIZE, 119, GroupLayout.PREFERRED_SIZE)))
						.addGroup(gl_panel_6.createSequentialGroup()
							.addGroup(gl_panel_6.createParallelGroup(Alignment.TRAILING, false)
								.addGroup(gl_panel_6.createSequentialGroup()
									.addComponent(lblTelefone)
									.addGap(104))
								.addGroup(gl_panel_6.createSequentialGroup()
									.addComponent(ftfTelefone)
									.addPreferredGap(ComponentPlacement.UNRELATED)))
							.addGroup(gl_panel_6.createParallelGroup(Alignment.LEADING)
								.addComponent(lblEndereo)
								.addComponent(tfEndereco, GroupLayout.PREFERRED_SIZE, 465, GroupLayout.PREFERRED_SIZE)))
						.addGroup(gl_panel_6.createSequentialGroup()
							.addGroup(gl_panel_6.createParallelGroup(Alignment.LEADING)
								.addComponent(lblNumero)
								.addComponent(tfEnderecoNumero, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
							.addPreferredGap(ComponentPlacement.UNRELATED)
							.addGroup(gl_panel_6.createParallelGroup(Alignment.LEADING)
								.addComponent(tfBairro, GroupLayout.PREFERRED_SIZE, 271, GroupLayout.PREFERRED_SIZE)
								.addComponent(lblNewLabel)))
						.addComponent(lblObservao))
					.addContainerGap())
		);
		gl_panel_6.setVerticalGroup(
			gl_panel_6.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_6.createSequentialGroup()
					.addContainerGap()
					.addComponent(lblCod)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_panel_6.createParallelGroup(Alignment.BASELINE)
						.addComponent(tfNome, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(chckbxLocal))
					.addGap(18)
					.addGroup(gl_panel_6.createParallelGroup(Alignment.BASELINE)
						.addComponent(chckbxFilho)
						.addComponent(lblPai)
						.addComponent(cbPai, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGap(18)
					.addGroup(gl_panel_6.createParallelGroup(Alignment.TRAILING)
						.addGroup(gl_panel_6.createSequentialGroup()
							.addComponent(lblTelefone)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(ftfTelefone, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
						.addGroup(gl_panel_6.createSequentialGroup()
							.addComponent(lblEndereo)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(tfEndereco, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)))
					.addGap(18)
					.addGroup(gl_panel_6.createParallelGroup(Alignment.TRAILING)
						.addGroup(gl_panel_6.createSequentialGroup()
							.addComponent(lblNumero)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(tfEnderecoNumero, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
						.addGroup(gl_panel_6.createSequentialGroup()
							.addComponent(lblNewLabel)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(tfBairro, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)))
					.addGap(18)
					.addGroup(gl_panel_6.createParallelGroup(Alignment.TRAILING)
						.addGroup(gl_panel_6.createSequentialGroup()
							.addComponent(lblCidade)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(tfCidade, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
						.addGroup(gl_panel_6.createSequentialGroup()
							.addComponent(lblUf)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(tfUf, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
						.addGroup(gl_panel_6.createSequentialGroup()
							.addComponent(lblCep)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(ftfCep, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)))
					.addGap(18)
					.addComponent(lblObservao)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(tfObservacao, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addContainerGap(28, Short.MAX_VALUE))
		);
		panel_6.setLayout(gl_panel_6);
		panel_5.setLayout(gl_panel_5);
		panel_2.setLayout(gl_panel_2);
		panel.setLayout(gl_panel);
		contentPane.setLayout(gl_contentPane);
	}

	private void carregarTableModel() {
		this.tableModelDestinatario = new TableModelDestinatario();
		this.table.setModel(tableModelDestinatario);
	}
	
	private void iniciaConexao() {

		// factory = Persistence.createEntityManagerFactory("DesbravarPU");
		manager = EntityManagerProducer.createEntityManager();
		trx = manager.getTransaction();
	}
	
	private void botaoNovoDestinatario(){
		
		limparCampos();
		Destinatario = new Destinatario();
		tabbedPane.setSelectedIndex(1);
		tfNome.requestFocus();
		
	}
	
	private void botaoSalvar(){
		
		salvarDestinatario(Destinatario);
		tabbedPane.setSelectedIndex(0);
		limparCampos();
	}
	
	private void botaoExcluir(){
		
		int linha = table.getSelectedRow();
        int linhaReal = table.convertRowIndexToModel(linha);
		
		Destinatario = tableModelDestinatario.getDestinatario(linhaReal);
		excluirDestinatario(Destinatario);
		limparTabela();
	}
	
	private void botaoDetalhes(){
		
		int linha = table.getSelectedRow();
        int linhaReal = table.convertRowIndexToModel(linha);
		
		Destinatario = tableModelDestinatario.getDestinatario(linhaReal);
		carregarCampos(Destinatario);
		tabbedPane.setSelectedIndex(1);
	}
	
	private void carregarCampos(Destinatario d){
		tfNome.setText(d.getNome());
		tfBairro.setText(d.getBairro());
		tfCidade.setText(d.getCidade());
		tfEndereco.setText(d.getEndereco());
		tfEnderecoNumero.setText(d.getEnderecoNumero());
		tfUf.setText(d.getUf());
		ftfCep.setText(d.getCep());
		ftfTelefone.setText(d.getTelefone());
		tfObservacao.setText(d.getObservacao());
		
		try {
			if (d.getLocal()) {
				chckbxLocal.setSelected(true);
			}else{
				
			}
		} catch (Exception e) {
			chckbxLocal.setSelected(false);
		}		
		
		try {
			if (d.getFilho()) {
				cbPai.setEnabled(true);
				chckbxFilho.setSelected(true);
				cbPai.setSelectedItem(d.getDestinatario());
			}else{
				chckbxFilho.setSelected(false);
				cbPai.setEnabled(false);
				//carregajcbPaiTodos();
			}
		} catch (Exception e) {
			chckbxFilho.setSelected(false);
			cbPai.setEnabled(false);
			//carregajcbPaiTodos();
		}
		
	}
	
	private void limparCampos(){
		tfNome.setText("");
		tfBairro.setText("");
		tfCidade.setText("");
		tfEndereco.setText("");
		tfEnderecoNumero.setText("");
		tfUf.setText("");
		ftfCep.setText("");
		ftfTelefone.setText("");
		tfObservacao.setText("");
		chckbxLocal.setSelected(false);
	}
	
	private void salvarDestinatario(Destinatario d) {
		try {
			
			d.setNome(tfNome.getText());
			d.setBairro(tfBairro.getText());
			d.setCep(ftfCep.getText());
			d.setCidade(tfCidade.getText());
			d.setEndereco(tfEndereco.getText());
			d.setEnderecoNumero(tfEnderecoNumero.getText());
			d.setTelefone(ftfTelefone.getText());
			d.setUf(tfUf.getText());
			d.setObservacao(tfObservacao.getText());
			
			if (chckbxLocal.isSelected()) {
				d.setLocal(true);
			}else{
				d.setLocal(false);
			}
			
			if (chckbxFilho.isSelected()) {
				d.setFilho(true);
				d.setDestinatario((br.com.grupocaravela.objeto.Destinatario) cbPai.getSelectedItem());
			}else{
				d.setFilho(false);
				d.setDestinatario(null);
			}
			
			trx.begin();
			manager.persist(d);
			trx.commit();

			JOptionPane.showMessageDialog(null, "O destinatario foi salvo com sucesso!");
			
			tabbedPane.setSelectedIndex(0);
			limparCampos();
			
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "ERRO! " + e);
		}
	}
	
	private void excluirDestinatario(Destinatario b) {
		try {
		
			trx.begin();
			manager.remove(b);
			trx.commit();

			JOptionPane.showMessageDialog(null, "O Destinatario foi excluido com sucesso!");
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "ERRO! " + e);
		}
	}
	
	private void limparTabela() {
		while (table.getModel().getRowCount() > 0) {
			tableModelDestinatario.removeDestinatario(0);
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
					Logger.getLogger(JanelaDestinatario.class.getName()).log(Level.SEVERE, null, ex);
				}
				// ######################METODO A SER
				// EXECUTADO##############################
				limparTabela();

				try {

					// trx.begin();
					Query consulta = manager
							.createQuery("FROM Destinatario WHERE nome LIKE '%" + tfLocalizar.getText() + "%' ORDER BY nome ASC");
					List<Destinatario> listaDestinatarios = consulta.getResultList();
					// trx.commit();

					for (int i = 0; i < listaDestinatarios.size(); i++) {
						Destinatario b = listaDestinatarios.get(i);
						tableModelDestinatario.addDestinatario(b);
					}
				} catch (Exception e) {
					JOptionPane.showMessageDialog(null, "Erro ao carregar a tabela de Destinatarios: " + e);
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
		
		if (usuario.getAdiministrador()) {
			btnExcluir.setEnabled(true);
		}
		
	}
	
	private void carregajcbPaiTodos() {

		cbPai.removeAllItems();

		try {

			//trx.begin();
			Query consulta = manager.createQuery("FROM Destinatario ORDER BY nome ASC");
			List<Destinatario> listaDestinatarios = consulta.getResultList();
			//trx.commit();

			for (int i = 0; i < listaDestinatarios.size(); i++) {

				Destinatario d = listaDestinatarios.get(i);
				cbPai.addItem(d);
			}

		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Erro no carregamento do Combo box Pai!: " + e);
		}
	}
	/*
	private void carregajcbPaiSelecionado(Destinatario dest) {

		cbPai.removeAllItems();

		try {

			//trx.begin();
			Query consulta = manager.createQuery("FROM Destinatario ORDER BY nome ASC");
			List<Destinatario> listaDestinatarios = consulta.getResultList();
			//trx.commit();

			for (int i = 0; i < listaDestinatarios.size(); i++) {

				Destinatario d = listaDestinatarios.get(i);
				cbPai.addItem(d);
				
				if (dest.equals(d.getDestinatario())) {
					cbPai.setSelectedIndex(i);
				}
			}

		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Erro no carregamento do Combo box Pai!: " + e);
		}
	}
	*/
}
