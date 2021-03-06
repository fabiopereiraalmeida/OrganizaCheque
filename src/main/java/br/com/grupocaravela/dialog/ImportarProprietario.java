package br.com.grupocaravela.dialog;

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
import javax.swing.JDialog;
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
import br.com.grupocaravela.objeto.Proprietario;
import br.com.grupocaravela.tablemodel.TableModelProprietario;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class ImportarProprietario extends JDialog {

	private JPanel contentPane;
	private JTable table;
	private JTextField tfNome;
	
	private EntityManager manager;
	private EntityTransaction trx;
	private TableModelProprietario tableModelProprietario;
	
	private Proprietario proprietario;
	private JTabbedPane tabbedPane;
	private JTextField tfLocalizar;
	private JTextField tfEndereco;
	private JTextField tfEnderecoNumero;
	private JTextField tfBairro;
	private JTextField tfCidade;
	private JTextField tfUf;
	private JFormattedTextField ftfCep;
	private JFormattedTextField ftfTelefone;
	private JFormattedTextField tfCpfCnpj;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ImportarProprietario frame = new ImportarProprietario();
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
	public ImportarProprietario() {
		
		carregarJanela();
		carregarTableModel();
		iniciaConexao();
		
		table.setAutoCreateRowSorter(true);
	}
		
	private void carregarJanela(){
		setTitle("Proprietarios");
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
		tabbedPane.addTab("Relação de Proprietarios", null, panel_1, null);
		
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
		btnFiltrar.setIcon(new ImageIcon(ImportarProprietario.class.getResource("/br/com/grupocaravela/icones/lupa_24.png")));
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
				
				botaoNovoProprietario();
				
			}
		});
		btnNovo.setIcon(new ImageIcon(ImportarProprietario.class.getResource("/br/com/grupocaravela/icones/novo_24.png")));
		
		JButton btnDetalhes = new JButton("Detalhes");
		btnDetalhes.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				botaoDetalhes();
				
			}
		});
		btnDetalhes.setIcon(new ImageIcon(ImportarProprietario.class.getResource("/br/com/grupocaravela/icones/alterar_24.png")));
		
		JButton btnImpotar = new JButton("Impotar");
		btnImpotar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				proprietario = tableModelProprietario.getProprietario(table.getSelectedRow());
				dispose();
				
			}
		});
		btnImpotar.setIcon(new ImageIcon(ImportarProprietario.class.getResource("/br/com/grupocaravela/icones/aprovar_24.png")));
		GroupLayout gl_panel_4 = new GroupLayout(panel_4);
		gl_panel_4.setHorizontalGroup(
			gl_panel_4.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_panel_4.createSequentialGroup()
					.addContainerGap(345, Short.MAX_VALUE)
					.addComponent(btnImpotar)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(btnDetalhes)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(btnNovo)
					.addContainerGap())
		);
		gl_panel_4.setVerticalGroup(
			gl_panel_4.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_panel_4.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_panel_4.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_panel_4.createParallelGroup(Alignment.BASELINE)
							.addComponent(btnNovo)
							.addComponent(btnDetalhes))
						.addComponent(btnImpotar, GroupLayout.DEFAULT_SIZE, 34, Short.MAX_VALUE))
					.addContainerGap())
		);
		panel_4.setLayout(gl_panel_4);
		
		JScrollPane scrollPane = new JScrollPane();
				
		table = new JTable();
		table.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2) {

					//botaoDetalhes();
					proprietario = tableModelProprietario.getProprietario(table.getSelectedRow());
					dispose();

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
		tabbedPane.addTab("Detalhes do Proprietario", null, panel_2, null);
		
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
		
		JButton btnSalvar = new JButton("Salvar e importar");
		btnSalvar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				Object[] options = { "Sim", "Não" };
				int i = JOptionPane.showOptionDialog(null,
						"Gostaria de salvar o proprietário de nome " + tfNome.getText() + "?", "Salvar",
						JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);

				if (i == JOptionPane.YES_OPTION) {

					salvarProprietario(proprietario);
				}
			}
		});
		btnSalvar.setIcon(new ImageIcon(ImportarProprietario.class.getResource("/br/com/grupocaravela/icones/alerta_verde_24.png")));
		
		JButton btnCancelar = new JButton("Cancelar");
		btnCancelar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				tabbedPane.setSelectedIndex(0);
				proprietario = null;
				limparCampos();
				
			}
		});
		btnCancelar.setIcon(new ImageIcon(ImportarProprietario.class.getResource("/br/com/grupocaravela/icones/alerta_amarelo_24.png")));
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
				
				String numero = ftfTelefone.getText().replace("-", "").replace(".", "").replace("/", "").replace("(", "").replace(")", "");
				
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
		
		JLabel lblObservao = new JLabel("CPF/CNPJ:");
		
		tfCpfCnpj = new JFormattedTextField();
		tfCpfCnpj.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				
				String numero = tfCpfCnpj.getText().replace("-", "").replace(".", "").replace("/", "");
				
				if (numero.length() == 11) {
					try {
						MaskFormatter mask = new MaskFormatter("###.###.###-##");
						mask.install(tfCpfCnpj);
					} catch (ParseException e1) {			
						e1.printStackTrace();
					}
				}
				
				if (numero.length() == 14) {
					try {
						MaskFormatter mask = new MaskFormatter("##.###.###/####-##");
						mask.install(tfCpfCnpj);
					} catch (ParseException e1) {			
						e1.printStackTrace();
					}
				}
				tfCpfCnpj.setText(numero);
				
			}
			@Override
			public void focusGained(FocusEvent e) {
				tfCpfCnpj.selectAll();
			}
		});
		tfCpfCnpj.setColumns(10);
		
		GroupLayout gl_panel_6 = new GroupLayout(panel_6);
		gl_panel_6.setHorizontalGroup(
			gl_panel_6.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_6.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_panel_6.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_panel_6.createParallelGroup(Alignment.TRAILING)
							.addComponent(lblCod, Alignment.LEADING)
							.addComponent(tfNome, Alignment.LEADING, GroupLayout.PREFERRED_SIZE, 610, GroupLayout.PREFERRED_SIZE)
							.addGroup(gl_panel_6.createSequentialGroup()
								.addGroup(gl_panel_6.createParallelGroup(Alignment.LEADING)
									.addComponent(lblTelefone)
									.addComponent(ftfTelefone, GroupLayout.PREFERRED_SIZE, 135, GroupLayout.PREFERRED_SIZE))
								.addPreferredGap(ComponentPlacement.UNRELATED)
								.addGroup(gl_panel_6.createParallelGroup(Alignment.LEADING)
									.addComponent(lblObservao)
									.addComponent(tfCpfCnpj, GroupLayout.PREFERRED_SIZE, 222, GroupLayout.PREFERRED_SIZE))
								.addGap(261)))
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
						.addComponent(tfEndereco, GroupLayout.PREFERRED_SIZE, 465, GroupLayout.PREFERRED_SIZE)
						.addComponent(lblEndereo)
						.addGroup(gl_panel_6.createSequentialGroup()
							.addGroup(gl_panel_6.createParallelGroup(Alignment.LEADING)
								.addComponent(lblNumero)
								.addComponent(tfEnderecoNumero, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
							.addPreferredGap(ComponentPlacement.UNRELATED)
							.addGroup(gl_panel_6.createParallelGroup(Alignment.LEADING)
								.addComponent(tfBairro, GroupLayout.PREFERRED_SIZE, 271, GroupLayout.PREFERRED_SIZE)
								.addComponent(lblNewLabel))))
					.addContainerGap(151, Short.MAX_VALUE))
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
						.addGroup(gl_panel_6.createSequentialGroup()
							.addComponent(lblTelefone)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(ftfTelefone, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
						.addGroup(gl_panel_6.createSequentialGroup()
							.addComponent(lblObservao)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(tfCpfCnpj, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)))
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(lblEndereo)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(tfEndereco, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
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
					.addContainerGap())
		);
		panel_6.setLayout(gl_panel_6);
		panel_5.setLayout(gl_panel_5);
		panel_2.setLayout(gl_panel_2);
		panel.setLayout(gl_panel);
		contentPane.setLayout(gl_contentPane);
	}

	private void carregarTableModel() {
		this.tableModelProprietario = new TableModelProprietario();
		this.table.setModel(tableModelProprietario);
	}
	
	private void iniciaConexao() {

		// factory = Persistence.createEntityManagerFactory("DesbravarPU");
		manager = EntityManagerProducer.createEntityManager();
		trx = manager.getTransaction();
	}
	
	private void botaoNovoProprietario(){
		
		limparCampos();
		proprietario = new Proprietario();
		tabbedPane.setSelectedIndex(1);
		tfNome.requestFocus();
		
	}
	
	private void botaoSalvar(){
		
		salvarProprietario(proprietario);
		tabbedPane.setSelectedIndex(0);
		limparCampos();
	}
	
	private void botaoDetalhes(){
		proprietario = tableModelProprietario.getProprietario(table.getSelectedRow());
		carregarCampos(proprietario);
		tabbedPane.setSelectedIndex(1);
	}
	
	private void carregarCampos(Proprietario d){
		tfNome.setText(d.getNome());
		tfBairro.setText(d.getBairro());
		tfCidade.setText(d.getCidade());
		tfEndereco.setText(d.getEndereco());
		tfEnderecoNumero.setText(d.getEnderecoNumero());
		tfUf.setText(d.getUf());
		ftfCep.setText(d.getCep());
		ftfTelefone.setText(d.getTelefone());
		tfCpfCnpj.setText(d.getCpfCnpj());
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
		tfCpfCnpj.setText("");
	}
	
	private void salvarProprietario(Proprietario d) {
		try {
			
			d.setNome(tfNome.getText());
			d.setBairro(tfBairro.getText());
			d.setCep(ftfCep.getText());
			d.setCidade(tfCidade.getText());
			d.setEndereco(tfEndereco.getText());
			d.setEnderecoNumero(tfEnderecoNumero.getText());
			d.setTelefone(ftfTelefone.getText());
			d.setUf(tfUf.getText());
			d.setCpfCnpj(tfCpfCnpj.getText());
			
			trx.begin();
			manager.persist(d);
			trx.commit();

			proprietario = d;
			
			JOptionPane.showMessageDialog(null, "O Proprietario foi salvo com sucesso!");
			
			tabbedPane.setSelectedIndex(0);
			limparCampos();
			
			dispose();
			
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "ERRO! " + e);
		}
	}
	
	private void excluirProprietario(Proprietario b) {
		try {
		
			trx.begin();
			manager.remove(b);
			trx.commit();

			JOptionPane.showMessageDialog(null, "O Proprietario foi excluido com sucesso!");
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "ERRO! " + e);
		}
	}
	
	private void limparTabela() {
		while (table.getModel().getRowCount() > 0) {
			tableModelProprietario.removeProprietario(0);
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
					Logger.getLogger(ImportarProprietario.class.getName()).log(Level.SEVERE, null, ex);
				}
				// ######################METODO A SER
				// EXECUTADO##############################
				limparTabela();

				try {

					// trx.begin();
					Query consulta = manager
							.createQuery("FROM Proprietario WHERE nome LIKE '%" + tfLocalizar.getText() + "%'");
					List<Proprietario> listaProprietarios = consulta.getResultList();
					// trx.commit();

					for (int i = 0; i < listaProprietarios.size(); i++) {
						Proprietario b = listaProprietarios.get(i);
						tableModelProprietario.addProprietario(b);
					}
				} catch (Exception e) {
					JOptionPane.showMessageDialog(null, "Erro ao carregar a tabela de Proprietarios: " + e);
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

	public Proprietario getProprietario() {
		return proprietario;
	}
	
	
}
