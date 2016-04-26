package br.com.grupocaravela.view;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Component;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.NumberFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.ImageIO;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableModel;

import com.toedter.calendar.JDateChooser;

import br.com.grupocaravela.aguarde.EsperaJanela;
import br.com.grupocaravela.aguarde.EsperaLista;
import br.com.grupocaravela.configuracao.Empresa;
import br.com.grupocaravela.configuracao.EntityManagerProducer;
import br.com.grupocaravela.dialog.BuscarProprietario;
import br.com.grupocaravela.dialog.ImportarAgencia;
import br.com.grupocaravela.dialog.ImportarBanco;
import br.com.grupocaravela.dialog.ImportarConta;
import br.com.grupocaravela.dialog.ImportarProprietario;
import br.com.grupocaravela.ferramenta.UsuarioLogado;
import br.com.grupocaravela.mask.DecimalFormattedField;
import br.com.grupocaravela.objeto.Agencia;
import br.com.grupocaravela.objeto.Banco;
import br.com.grupocaravela.objeto.Cheque;
import br.com.grupocaravela.objeto.Conta;
import br.com.grupocaravela.objeto.Destinatario;
import br.com.grupocaravela.objeto.Historico;
import br.com.grupocaravela.objeto.Proprietario;
import br.com.grupocaravela.objeto.Usuario;
import br.com.grupocaravela.render.MeuRenderer;
import br.com.grupocaravela.render.MoedaRender;
import br.com.grupocaravela.render.TableRenderer;
import br.com.grupocaravela.tablemodel.TableModelCheque;
import javax.swing.JCheckBox;

public class JanelaCheque extends JFrame {

	private JPanel contentPane;
	private JTable table;
	private JTextField tfCodBarrasCheque;

	private EntityManager manager;
	private EntityTransaction trx;
	private TableModelCheque tableModelCheque;

	private Cheque cheque;
	private JTabbedPane tabbedPane;
	private JTextField tfLocalizar;
	private JTextField tfCodAgencia;
	private JTextField tfCodBanco;
	private JTextField tfNomeBanco;
	private JTextField tfNumConta;
	private JTextField tfNomeProprietario;
	private JTextField tfValor;
	private JTextField tfNumCheque;
	private JDateChooser dcBomData;
	private JDateChooser dcEntradaData;

	private Banco banco;
	private Agencia agencia;
	private Conta conta;
	private Proprietario proprietario;
	private Destinatario destinatario;
	private JTextField tfCodVenda;
	private JTextField tfObs;

	private Historico historico;
	private JComboBox cbDestinatario;
	private JLabel lblImagem;
		
	private File arquivoFoto;
	
	private Usuario usuario = UsuarioLogado.getUsuario();
	private JButton btnExcluir;
	private JCheckBox cbTerceiros;
	private JCheckBox chbDevolvido;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					JanelaCheque frame = new JanelaCheque();
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
	public JanelaCheque() {

		carregarJanela();
		carregarTableModel();
		tamanhoColunas();
		iniciaConexao();
				
		table.setAutoCreateRowSorter(true);
		
		if (usuario.getAdiministrador()) {
			btnExcluir.setEnabled(true);
			chbDevolvido.setEnabled(true);
		}
		
	}

	private void carregarJanela() {
		setTitle("Cheques");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 814, 514);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);

		JPanel panel = new JPanel();
		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(gl_contentPane.createParallelGroup(Alignment.LEADING).addComponent(panel,
				GroupLayout.DEFAULT_SIZE, 688, Short.MAX_VALUE));
		gl_contentPane.setVerticalGroup(gl_contentPane.createParallelGroup(Alignment.LEADING).addComponent(panel,
				Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 378, Short.MAX_VALUE));

		tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		GroupLayout gl_panel = new GroupLayout(panel);
		gl_panel.setHorizontalGroup(
			gl_panel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel.createSequentialGroup()
					.addComponent(tabbedPane, GroupLayout.PREFERRED_SIZE, 803, Short.MAX_VALUE)
					.addContainerGap())
		);
		gl_panel.setVerticalGroup(
			gl_panel.createParallelGroup(Alignment.LEADING)
				.addComponent(tabbedPane, GroupLayout.DEFAULT_SIZE, 408, Short.MAX_VALUE)
		);

		JPanel panel_1 = new JPanel();
		tabbedPane.addTab("Relação de cheques", null, panel_1, null);

		JPanel panel_3 = new JPanel();

		JPanel panel_4 = new JPanel();
		panel_4.setBorder(new LineBorder(new Color(0, 0, 0)));

		JLabel lblLocalizar = new JLabel("Localizar:");

		tfLocalizar = new JTextField();
		tfLocalizar.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					
					tfLocalizar.setText(tfLocalizar.getText().replaceAll("<", "").replaceAll(">", ""));
					buscarNome(tfLocalizar.getText());
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

				buscarNome(tfLocalizar.getText());

			}
		});
		btnFiltrar.setIcon(new ImageIcon(JanelaCheque.class.getResource("/br/com/grupocaravela/icones/lupa_24.png")));
		GroupLayout gl_panel_1 = new GroupLayout(panel_1);
		gl_panel_1.setHorizontalGroup(
			gl_panel_1.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_panel_1.createSequentialGroup()
					.addContainerGap()
					.addComponent(lblLocalizar)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(tfLocalizar, GroupLayout.DEFAULT_SIZE, 588, Short.MAX_VALUE)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(btnFiltrar)
					.addContainerGap())
				.addComponent(panel_3, GroupLayout.DEFAULT_SIZE, 810, Short.MAX_VALUE)
				.addComponent(panel_4, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 798, Short.MAX_VALUE)
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
					.addComponent(panel_3, GroupLayout.DEFAULT_SIZE, 263, Short.MAX_VALUE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(panel_4, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
		);

		JButton btnNovo = new JButton("Novo");
		btnNovo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				botaoNovoCheque();

			}
		});
		btnNovo.setIcon(new ImageIcon(JanelaCheque.class.getResource("/br/com/grupocaravela/icones/novo_24.png")));

		JButton btnDetalhes = new JButton("Detalhes");
		btnDetalhes.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				botaoDetalhes();

			}
		});
		btnDetalhes
				.setIcon(new ImageIcon(JanelaCheque.class.getResource("/br/com/grupocaravela/icones/alterar_24.png")));

		btnExcluir = new JButton("Excluir");
		btnExcluir.setEnabled(false);
		btnExcluir.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				Object[] options = { "Sim", "Não" };
				int i = JOptionPane.showOptionDialog(null,
						"ATENÇÃO!!! Esta operação irá excluir o cheque selecionado! Gostaria de continuar?",
						"Excluir", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options,
						options[0]);

				if (i == JOptionPane.YES_OPTION) {
					botaoExcluir();
				}				
				
			}
		});
		btnExcluir.setIcon(
				new ImageIcon(JanelaCheque.class.getResource("/br/com/grupocaravela/icones/alerta_vermelho_24.png")));
		GroupLayout gl_panel_4 = new GroupLayout(panel_4);
		gl_panel_4.setHorizontalGroup(gl_panel_4.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_panel_4.createSequentialGroup().addContainerGap(327, Short.MAX_VALUE)
						.addComponent(btnExcluir).addPreferredGap(ComponentPlacement.RELATED).addComponent(btnDetalhes)
						.addPreferredGap(ComponentPlacement.RELATED).addComponent(btnNovo).addContainerGap()));
		gl_panel_4.setVerticalGroup(gl_panel_4.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_panel_4.createSequentialGroup().addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
						.addGroup(gl_panel_4.createParallelGroup(Alignment.BASELINE).addComponent(btnNovo)
								.addComponent(btnDetalhes).addComponent(btnExcluir))
						.addContainerGap()));
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
		gl_panel_3.setHorizontalGroup(gl_panel_3.createParallelGroup(Alignment.LEADING).addComponent(scrollPane,
				Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 683, Short.MAX_VALUE));
		gl_panel_3.setVerticalGroup(
				gl_panel_3.createParallelGroup(Alignment.LEADING).addGroup(gl_panel_3.createSequentialGroup().addGap(5)
						.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 241, Short.MAX_VALUE)));
		panel_3.setLayout(gl_panel_3);
		panel_1.setLayout(gl_panel_1);

		JPanel panel_2 = new JPanel();
		tabbedPane.addTab("Detalhes do cheque", null, panel_2, null);

		JPanel panel_5 = new JPanel();
		GroupLayout gl_panel_2 = new GroupLayout(panel_2);
		gl_panel_2.setHorizontalGroup(gl_panel_2.createParallelGroup(Alignment.LEADING).addComponent(panel_5,
				GroupLayout.DEFAULT_SIZE, 683, Short.MAX_VALUE));
		gl_panel_2.setVerticalGroup(gl_panel_2.createParallelGroup(Alignment.LEADING).addComponent(panel_5,
				GroupLayout.DEFAULT_SIZE, 344, Short.MAX_VALUE));

		JPanel panel_6 = new JPanel();

		JPanel panel_7 = new JPanel();
		panel_7.setBorder(new LineBorder(new Color(0, 0, 0)));
		GroupLayout gl_panel_5 = new GroupLayout(panel_5);
		gl_panel_5.setHorizontalGroup(
			gl_panel_5.createParallelGroup(Alignment.LEADING)
				.addGroup(Alignment.TRAILING, gl_panel_5.createSequentialGroup()
					.addGroup(gl_panel_5.createParallelGroup(Alignment.TRAILING)
						.addComponent(panel_7, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 798, Short.MAX_VALUE)
						.addComponent(panel_6, GroupLayout.PREFERRED_SIZE, 798, Short.MAX_VALUE))
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
						"Gostaria de salvar o cheque do Banco " + tfNomeBanco.getText() + " no valor de " + tfValor.getText() + "?", "Salvar",
						JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);

				if (i == JOptionPane.YES_OPTION) {

					salvarCheque(cheque);
				}
				
			}
		});
		btnSalvar.setIcon(
				new ImageIcon(JanelaCheque.class.getResource("/br/com/grupocaravela/icones/alerta_verde_24.png")));

		JButton btnCancelar = new JButton("Cancelar");
		btnCancelar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				tabbedPane.setSelectedIndex(0);
				cheque = null;
				limparCampos();

			}
		});
		btnCancelar.setIcon(
				new ImageIcon(JanelaCheque.class.getResource("/br/com/grupocaravela/icones/alerta_amarelo_24.png")));
		GroupLayout gl_panel_7 = new GroupLayout(panel_7);
		gl_panel_7.setHorizontalGroup(gl_panel_7.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_panel_7.createSequentialGroup().addContainerGap(433, Short.MAX_VALUE)
						.addComponent(btnCancelar).addPreferredGap(ComponentPlacement.RELATED).addComponent(btnSalvar)
						.addContainerGap()));
		gl_panel_7.setVerticalGroup(gl_panel_7.createParallelGroup(Alignment.TRAILING).addGroup(gl_panel_7
				.createSequentialGroup()
				.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addGroup(gl_panel_7
						.createParallelGroup(Alignment.BASELINE).addComponent(btnSalvar).addComponent(btnCancelar))
				.addContainerGap()));
		panel_7.setLayout(gl_panel_7);

		JLabel lblCod = new JLabel("Código do cheque:");

		tfCodBarrasCheque = new JTextField();
		tfCodBarrasCheque.setHorizontalAlignment(SwingConstants.CENTER);
		tfCodBarrasCheque.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					
					iniciaConexao();
					
					tfCodBarrasCheque.setText(tfCodBarrasCheque.getText().replaceAll("<", "").replaceAll(">", "")
							.replaceAll("Ç", "").replaceAll("ç", "").replaceAll(":", "").replaceAll(";", ""));
					
					String tamanhoCodigo = tfCodBarrasCheque.getText();
					
					if (tamanhoCodigo.length() == 30) {
						
						if (verificaExistenciaCheque() == false) {
							buscarCheque(tfCodBarrasCheque.getText());

							buscarBanco(tfCodBanco.getText());
							if (banco != null) {
								buscarAgencia(tfCodAgencia.getText());
								buscarConta(tfNumConta.getText());
								buscarProprietario(banco, agencia, conta);
							}
						}else{
							JOptionPane.showMessageDialog(null, "O cheque informado ja se encontra cadastrado no sistema");
							
						}
						
					}else{
						JOptionPane.showMessageDialog(null, "ERRO!!! favor verificar o código do cheque");
						tfCodBarrasCheque.requestFocus();
						tfCodBarrasCheque.selectAll();
					}
					
				}
			}
		});
		tfCodBarrasCheque.setFont(new Font("Dialog", Font.BOLD, 24));
		tfCodBarrasCheque.setColumns(10);

		JPanel panel_8 = new JPanel();
		panel_8.setBorder(new TitledBorder(null, "Banco", TitledBorder.LEADING, TitledBorder.TOP, null, null));

		JPanel panel_9 = new JPanel();
		panel_9.setBorder(new TitledBorder(null, "Ag\u00EAncia", TitledBorder.LEADING, TitledBorder.TOP, null, null));

		JPanel panel_10 = new JPanel();
		panel_10.setBorder(new TitledBorder(null, "Conta", TitledBorder.LEADING, TitledBorder.TOP, null, null));

		JPanel panel_11 = new JPanel();
		panel_11.setBorder(
				new TitledBorder(null, "Propriet\u00E1rio", TitledBorder.LEADING, TitledBorder.TOP, null, null));

		JPanel panel_12 = new JPanel();
		panel_12.setBorder(new TitledBorder(null, "Demais informa\u00E7\u00F5es", TitledBorder.LEADING,
				TitledBorder.TOP, null, null));
		
		cbDestinatario = new JComboBox();
		
		JLabel lblDestinatrio = new JLabel("Destinatário:");
		
		chbDevolvido = new JCheckBox("Devolvido");
		chbDevolvido.setEnabled(false);
		GroupLayout gl_panel_6 = new GroupLayout(panel_6);
		gl_panel_6.setHorizontalGroup(
			gl_panel_6.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_6.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_panel_6.createParallelGroup(Alignment.LEADING)
						.addComponent(tfCodBarrasCheque, GroupLayout.DEFAULT_SIZE, 774, Short.MAX_VALUE)
						.addComponent(panel_12, GroupLayout.PREFERRED_SIZE, 774, Short.MAX_VALUE)
						.addComponent(lblCod)
						.addGroup(gl_panel_6.createSequentialGroup()
							.addComponent(panel_10, GroupLayout.PREFERRED_SIZE, 269, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(panel_11, GroupLayout.DEFAULT_SIZE, 499, Short.MAX_VALUE))
						.addGroup(gl_panel_6.createSequentialGroup()
							.addGroup(gl_panel_6.createParallelGroup(Alignment.LEADING)
								.addComponent(lblDestinatrio)
								.addComponent(cbDestinatario, 0, 258, Short.MAX_VALUE))
							.addGap(423)
							.addComponent(chbDevolvido))
						.addGroup(gl_panel_6.createSequentialGroup()
							.addComponent(panel_8, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(panel_9, GroupLayout.PREFERRED_SIZE, 225, GroupLayout.PREFERRED_SIZE)))
					.addContainerGap())
		);
		gl_panel_6.setVerticalGroup(
			gl_panel_6.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_6.createSequentialGroup()
					.addContainerGap()
					.addComponent(lblCod)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(tfCodBarrasCheque, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_panel_6.createParallelGroup(Alignment.TRAILING)
						.addGroup(gl_panel_6.createSequentialGroup()
							.addComponent(lblDestinatrio)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(cbDestinatario, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
						.addComponent(chbDevolvido))
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addGroup(gl_panel_6.createParallelGroup(Alignment.LEADING, false)
						.addComponent(panel_9, 0, 0, Short.MAX_VALUE)
						.addComponent(panel_8, GroupLayout.PREFERRED_SIZE, 50, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addGroup(gl_panel_6.createParallelGroup(Alignment.LEADING, false)
						.addComponent(panel_11, 0, 0, Short.MAX_VALUE)
						.addComponent(panel_10, GroupLayout.PREFERRED_SIZE, 50, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(panel_12, GroupLayout.DEFAULT_SIZE, 105, Short.MAX_VALUE)
					.addContainerGap())
		);

		JLabel lblValor = new JLabel("Valor:");

		tfValor = new DecimalFormattedField(DecimalFormattedField.REAL);
		tfValor.setColumns(10);

		JLabel lblNumCheque = new JLabel("Num. Cheque:");

		tfNumCheque = new JTextField();
		tfNumCheque.setColumns(10);

		JLabel lblDataEntrada = new JLabel("Data Entrada:");

		dcEntradaData = new JDateChooser();

		JLabel lblBomParaDia = new JLabel("Bom para data:");

		dcBomData = new JDateChooser();
		
		JLabel lblCodVenda = new JLabel("Cod. Venda:");
		
		tfCodVenda = new JTextField();
		tfCodVenda.setColumns(10);
		
		JLabel lblObs = new JLabel("Obs:");
		
		tfObs = new JTextField();
		tfObs.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				tfObs.setText(tfObs.getText().toUpperCase());
			}
		});
		tfObs.setColumns(10);
		
		cbTerceiros = new JCheckBox("De terceiros");
		GroupLayout gl_panel_12 = new GroupLayout(panel_12);
		gl_panel_12.setHorizontalGroup(
			gl_panel_12.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_12.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_panel_12.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_panel_12.createSequentialGroup()
							.addComponent(lblObs)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(tfObs, GroupLayout.DEFAULT_SIZE, 707, Short.MAX_VALUE))
						.addGroup(gl_panel_12.createSequentialGroup()
							.addGroup(gl_panel_12.createParallelGroup(Alignment.LEADING)
								.addGroup(gl_panel_12.createSequentialGroup()
									.addComponent(lblNumCheque)
									.addPreferredGap(ComponentPlacement.RELATED)
									.addComponent(tfNumCheque, GroupLayout.PREFERRED_SIZE, 129, GroupLayout.PREFERRED_SIZE)
									.addPreferredGap(ComponentPlacement.RELATED)
									.addComponent(lblValor)
									.addPreferredGap(ComponentPlacement.RELATED)
									.addComponent(tfValor, GroupLayout.PREFERRED_SIZE, 159, GroupLayout.PREFERRED_SIZE)
									.addPreferredGap(ComponentPlacement.UNRELATED)
									.addComponent(lblCodVenda))
								.addGroup(gl_panel_12.createSequentialGroup()
									.addComponent(lblDataEntrada)
									.addPreferredGap(ComponentPlacement.RELATED)
									.addComponent(dcEntradaData, GroupLayout.PREFERRED_SIZE, 158, GroupLayout.PREFERRED_SIZE)
									.addPreferredGap(ComponentPlacement.RELATED)
									.addComponent(lblBomParaDia)
									.addPreferredGap(ComponentPlacement.RELATED)
									.addComponent(dcBomData, GroupLayout.PREFERRED_SIZE, 151, GroupLayout.PREFERRED_SIZE)))
							.addPreferredGap(ComponentPlacement.RELATED)
							.addGroup(gl_panel_12.createParallelGroup(Alignment.LEADING)
								.addComponent(cbTerceiros)
								.addComponent(tfCodVenda, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
							.addGap(57)))
					.addGap(12))
		);
		gl_panel_12.setVerticalGroup(
			gl_panel_12.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_12.createSequentialGroup()
					.addGroup(gl_panel_12.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblValor)
						.addComponent(tfValor, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(lblNumCheque)
						.addComponent(tfNumCheque, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(lblCodVenda)
						.addComponent(tfCodVenda, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_panel_12.createParallelGroup(Alignment.LEADING)
						.addComponent(lblDataEntrada)
						.addComponent(dcEntradaData, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(lblBomParaDia)
						.addComponent(dcBomData, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(cbTerceiros))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_panel_12.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblObs)
						.addComponent(tfObs, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addContainerGap(18, Short.MAX_VALUE))
		);
		panel_12.setLayout(gl_panel_12);

		JLabel lblNome = new JLabel("Nome");

		tfNomeProprietario = new JTextField();
		tfNomeProprietario.setEditable(false);
		tfNomeProprietario.setColumns(10);
		
		JButton button_4 = new JButton("");
		button_4.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				botaoAddProprietario();
				
			}
		});
		button_4.setIcon(new ImageIcon(JanelaCheque.class.getResource("/br/com/grupocaravela/icones/maismais_24.png")));
		GroupLayout gl_panel_11 = new GroupLayout(panel_11);
		gl_panel_11.setHorizontalGroup(
			gl_panel_11.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_11.createSequentialGroup()
					.addContainerGap()
					.addComponent(lblNome, GroupLayout.PREFERRED_SIZE, 41, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(tfNomeProprietario, GroupLayout.DEFAULT_SIZE, 380, Short.MAX_VALUE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(button_4, GroupLayout.PREFERRED_SIZE, 38, GroupLayout.PREFERRED_SIZE))
		);
		gl_panel_11.setVerticalGroup(
			gl_panel_11.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_11.createSequentialGroup()
					.addGroup(gl_panel_11.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_panel_11.createParallelGroup(Alignment.BASELINE)
							.addComponent(lblNome)
							.addComponent(tfNomeProprietario, GroupLayout.PREFERRED_SIZE, 23, GroupLayout.PREFERRED_SIZE))
						.addComponent(button_4, GroupLayout.PREFERRED_SIZE, 23, GroupLayout.PREFERRED_SIZE))
					.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
		);
		panel_11.setLayout(gl_panel_11);

		JLabel lblNum = new JLabel("Num.");

		tfNumConta = new JTextField();
		tfNumConta.setColumns(10);

		JButton button_2 = new JButton("");
		button_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				botaoAddConta();
			}
		});
		button_2.setIcon(new ImageIcon(JanelaCheque.class.getResource("/br/com/grupocaravela/icones/maismais_24.png")));
		GroupLayout gl_panel_10 = new GroupLayout(panel_10);
		gl_panel_10.setHorizontalGroup(
			gl_panel_10.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_10.createSequentialGroup()
					.addContainerGap()
					.addComponent(lblNum, GroupLayout.PREFERRED_SIZE, 41, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(tfNumConta, GroupLayout.PREFERRED_SIZE, 146, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(button_2, GroupLayout.PREFERRED_SIZE, 38, GroupLayout.PREFERRED_SIZE)
					.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
		);
		gl_panel_10.setVerticalGroup(
			gl_panel_10.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_10.createSequentialGroup()
					.addGroup(gl_panel_10.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_panel_10.createSequentialGroup()
							.addGap(6)
							.addComponent(lblNum))
						.addComponent(button_2, GroupLayout.PREFERRED_SIZE, 23, GroupLayout.PREFERRED_SIZE)
						.addComponent(tfNumConta, GroupLayout.PREFERRED_SIZE, 23, GroupLayout.PREFERRED_SIZE))
					.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
		);
		panel_10.setLayout(gl_panel_10);

		JLabel label = new JLabel("Cod.");

		tfCodAgencia = new JTextField();
		tfCodAgencia.setColumns(10);

		JButton button = new JButton("");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				botaoAddAgencia();
			}
		});
		button.setIcon(new ImageIcon(JanelaCheque.class.getResource("/br/com/grupocaravela/icones/maismais_24.png")));
		GroupLayout gl_panel_9 = new GroupLayout(panel_9);
		gl_panel_9.setHorizontalGroup(
			gl_panel_9.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_9.createSequentialGroup()
					.addContainerGap()
					.addComponent(label)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(tfCodAgencia, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(button, GroupLayout.PREFERRED_SIZE, 38, GroupLayout.PREFERRED_SIZE)
					.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
		);
		gl_panel_9.setVerticalGroup(
			gl_panel_9.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_9.createSequentialGroup()
					.addGroup(gl_panel_9.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_panel_9.createSequentialGroup()
							.addGap(6)
							.addComponent(label))
						.addComponent(button, GroupLayout.PREFERRED_SIZE, 23, GroupLayout.PREFERRED_SIZE)
						.addComponent(tfCodAgencia, GroupLayout.PREFERRED_SIZE, 23, GroupLayout.PREFERRED_SIZE))
					.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
		);
		panel_9.setLayout(gl_panel_9);

		JLabel lblCod_1 = new JLabel("Cod.");

		tfCodBanco = new JTextField();
		tfCodBanco.setColumns(10);

		JLabel lblNome_1 = new JLabel("nome");

		tfNomeBanco = new JTextField();
		tfNomeBanco.setEditable(false);
		tfNomeBanco.setColumns(10);

		JButton button_1 = new JButton("");
		button_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				botaoAddBanco();
			}
		});
		button_1.setIcon(new ImageIcon(JanelaCheque.class.getResource("/br/com/grupocaravela/icones/maismais_24.png")));
		GroupLayout gl_panel_8 = new GroupLayout(panel_8);
		gl_panel_8.setHorizontalGroup(
			gl_panel_8.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_8.createSequentialGroup()
					.addContainerGap()
					.addComponent(lblCod_1)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(tfCodBanco, GroupLayout.PREFERRED_SIZE, 78, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(lblNome_1)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(tfNomeBanco, GroupLayout.PREFERRED_SIZE, 280, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(button_1, GroupLayout.PREFERRED_SIZE, 38, GroupLayout.PREFERRED_SIZE)
					.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
		);
		gl_panel_8.setVerticalGroup(
			gl_panel_8.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_8.createSequentialGroup()
					.addGroup(gl_panel_8.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_panel_8.createSequentialGroup()
							.addGap(6)
							.addComponent(lblCod_1))
						.addGroup(gl_panel_8.createSequentialGroup()
							.addGap(6)
							.addComponent(lblNome_1))
						.addComponent(button_1, GroupLayout.PREFERRED_SIZE, 23, GroupLayout.PREFERRED_SIZE)
						.addComponent(tfNomeBanco, GroupLayout.PREFERRED_SIZE, 23, GroupLayout.PREFERRED_SIZE)
						.addGroup(gl_panel_8.createSequentialGroup()
							.addGap(4)
							.addComponent(tfCodBanco, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)))
					.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
		);
		panel_8.setLayout(gl_panel_8);
		panel_6.setLayout(gl_panel_6);
		panel_5.setLayout(gl_panel_5);
		panel_2.setLayout(gl_panel_2);

		JPanel panel_13 = new JPanel();
		tabbedPane.addTab("Imagem", null, panel_13, null);
		
		JPanel panel_14 = new JPanel();
		panel_14.setBorder(new TitledBorder(null, "Imagem do cheque - para alterar clique na imagem", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel_14.setBackground(Color.WHITE);
		GroupLayout gl_panel_13 = new GroupLayout(panel_13);
		gl_panel_13.setHorizontalGroup(
			gl_panel_13.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_13.createSequentialGroup()
					.addContainerGap()
					.addComponent(panel_14, GroupLayout.DEFAULT_SIZE, 774, Short.MAX_VALUE)
					.addContainerGap())
		);
		gl_panel_13.setVerticalGroup(
			gl_panel_13.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_13.createSequentialGroup()
					.addContainerGap()
					.addComponent(panel_14, GroupLayout.DEFAULT_SIZE, 357, Short.MAX_VALUE)
					.addContainerGap())
		);
		
		lblImagem = new JLabel("");
		lblImagem.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				try {
					buscarImagemLocal();
				} catch (Exception e2) {
					// TODO: handle exception
				}
				
			}
		});
		lblImagem.setIcon(new ImageIcon(JanelaCheque.class.getResource("/br/com/grupocaravela/imagens/semFoto.png")));
		lblImagem.setHorizontalAlignment(SwingConstants.CENTER);
		GroupLayout gl_panel_14 = new GroupLayout(panel_14);
		gl_panel_14.setHorizontalGroup(
			gl_panel_14.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_14.createSequentialGroup()
					.addContainerGap()
					.addComponent(lblImagem, GroupLayout.DEFAULT_SIZE, 750, Short.MAX_VALUE)
					.addContainerGap())
		);
		gl_panel_14.setVerticalGroup(
			gl_panel_14.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_14.createSequentialGroup()
					.addContainerGap()
					.addComponent(lblImagem, GroupLayout.DEFAULT_SIZE, 333, Short.MAX_VALUE)
					.addContainerGap())
		);
		panel_14.setLayout(gl_panel_14);
		panel_13.setLayout(gl_panel_13);
		panel.setLayout(gl_panel);
		contentPane.setLayout(gl_contentPane);
	}

	private void carregarTableModel() {
		this.tableModelCheque = new TableModelCheque();
		this.table.setModel(tableModelCheque);
/*
		TableCellRenderer renderer = new MeuRenderer();
		table.setDefaultRenderer(String.class, renderer);
		table.setDefaultRenderer(Integer.class, renderer);
		table.setDefaultRenderer(Date.class, renderer);
*/	
		/*
		// Render de Moeda
		NumberFormat numeroMoeda = NumberFormat.getNumberInstance();
		numeroMoeda.setMinimumFractionDigits(2);
		DefaultTableCellRenderer cellRendererCustomMoeda = new MoedaRender(numeroMoeda);
		table.getColumnModel().getColumn(5).setCellRenderer(cellRendererCustomMoeda);
		*/
	}

	private void tamanhoColunas() {
		// tableProdutos.setAutoResizeMode(tableProdutos.AUTO_RESIZE_OFF);
		table.getColumnModel().getColumn(0).setWidth(30);
		table.getColumnModel().getColumn(0).setMaxWidth(50);
		
		table.getColumnModel().getColumn(7).setWidth(150);
		table.getColumnModel().getColumn(7).setMinWidth(150);

	}
	
	private void iniciaConexao() {

		// factory = Persistence.createEntityManagerFactory("DesbravarPU");
		manager = EntityManagerProducer.createEntityManager();
		trx = manager.getTransaction();
	}

	private void botaoNovoCheque() {

		limparCampos();
		carregajcbDestinatarioNovo();
		cheque = new Cheque();
		tabbedPane.setSelectedIndex(1);
		tfCodBarrasCheque.requestFocus();

		dcEntradaData.setDate(dataAtual());

	}

	private void botaoSalvar() {

		salvarCheque(cheque);
		tabbedPane.setSelectedIndex(0);
		limparCampos();
	}

	private void botaoExcluir() {
		
		int linha = table.getSelectedRow();
        int linhaReal = table.convertRowIndexToModel(linha);
		
		cheque = tableModelCheque.getCheque(linhaReal);
		excluirCheque(cheque);
		limparTabela();
	}

	private void botaoDetalhes() {
		limparCampos();
		carregajcbDestinatarioTodos();
		
		int linha = table.getSelectedRow();
        int linhaReal = table.convertRowIndexToModel(linha);
		
		cheque = tableModelCheque.getCheque(linhaReal);
		
		banco = cheque.getBanco();
		agencia = cheque.getAgencia();
		conta = cheque.getConta();
		proprietario = cheque.getProprietario();
		destinatario = cheque.getDestinatario();		
		
		carregarCampos(cheque);
		buscarImagemServidor();
		tabbedPane.setSelectedIndex(1);
	}

	private void carregarCampos(Cheque c) {
		tfCodBarrasCheque.setText(c.getCodCheque());
		tfCodBanco.setText(c.getBanco().getCod());
		tfNomeBanco.setText(c.getBanco().getNome());
		tfCodAgencia.setText(c.getAgencia().getCod());
		tfNumConta.setText(c.getConta().getNumero());
		tfNomeProprietario.setText(c.getProprietario().getNome());
		tfNumCheque.setText(c.getNumCheque());
		tfValor.setText(c.getValor().toString());
		tfCodVenda.setText(c.getCodVenda());
		tfObs.setText(c.getObservacao());
		
		cbDestinatario.setSelectedItem(c.getDestinatario());

		dcEntradaData.setDate(c.getDataEntrada());
		dcBomData.setDate(c.getDataVencimento());
		
		if (c.getTerceiros() != null) {
			if (c.getTerceiros() == true) {
				cbTerceiros.setSelected(true);
			}else{
				cbTerceiros.setSelected(false);
			}
		}
		
		try {
			if (c.getVoltouUmaVez() == true) {
				chbDevolvido.setSelected(true);	
			}else{
				chbDevolvido.setSelected(false);
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		
	}

	private void limparCampos() {
		tfCodBarrasCheque.setText("");
		tfCodBanco.setText("");
		tfNomeBanco.setText("");
		tfCodAgencia.setText("");
		tfNumConta.setText("");
		tfNomeProprietario.setText("");
		tfNumCheque.setText("");
		tfValor.setText("0.0");
		tfCodVenda.setText("");
		tfObs.setText("");
		
		//cbDestinatario.setSelectedIndex(0);

		dcEntradaData.setDate(null);
		dcBomData.setDate(null);

		tfNumConta.setBackground(Color.WHITE);
		tfCodBanco.setBackground(Color.WHITE);
		tfCodAgencia.setBackground(Color.WHITE);
		tfNomeProprietario.setBackground(Color.WHITE);
		
		cbTerceiros.setSelected(false);
		
		chbDevolvido.setSelected(false);

		// dcEntradaData.re
		// dcBomData.setDate(c.getDataVencimento());
	}

	private void salvarCheque(Cheque c) {
		try {
			
			boolean novo = true;
			try {
				if (c.getBanco().equals(null)) {
					novo = true;
				}else{
					novo = false;
				}
			} catch (Exception e) {				
				novo = false;				
			}
			
			
			//Query consulta = manager.createQuery("FROM Destinatario WHERE id LIKE '1'");
			//List<Destinatario> listaDestinatario = consulta.getResultList();

			//destinatario = listaDestinatario.get(0);
			destinatario = (Destinatario) cbDestinatario.getSelectedItem();
			
			//c.setAtivo(true);
			
			if (novo) {
				c.setVoltouUmaVez(false);
				c.setVoltouDuasVezes(false);
			}
			
			c.setAgencia(agencia);
			c.setBanco(banco);
			c.setConta(conta);
			c.setProprietario(proprietario);

			c.setCodCheque(tfCodBarrasCheque.getText());
			c.setCodVenda(tfCodVenda.getText());
			c.setDataEntrada(dcEntradaData.getDate());
			c.setDataVencimento(dcBomData.getDate());
			c.setNumCheque(tfNumCheque.getText());
			c.setObservacao(tfObs.getText());
			c.setValor(Double.parseDouble(tfValor.getText().replace("R$ ", "").replace(".", "").replace(",", ".")));
			c.setDestinatario(destinatario);
			
			c.setTerceiros(cbTerceiros.isSelected());
			
			if (chbDevolvido.isSelected()) {
				c.setVoltouUmaVez(true);
			}else{
				c.setVoltouUmaVez(false);
				c.setVoltouDuasVezes(false);
			}
			
			trx.begin();
			manager.persist(c);	
			trx.commit();

			JOptionPane.showMessageDialog(null, "O cheque foi salvo com sucesso!");
			
			
			if (novo) {
				criarHistorico("Criação do cheque", UsuarioLogado.getUsuario(), c, destinatario);
			}else{
				criarHistorico("Alteração do cheque", UsuarioLogado.getUsuario(), c, destinatario);
			}
			
			
			tabbedPane.setSelectedIndex(0);
			limparCampos();
			
		} catch (Exception e) {
			
			JOptionPane.showMessageDialog(null, "ERRO! " + e);
			
		}
	}

public void criarHistorico(String opearacao, Usuario u, Cheque c, Destinatario d){
					
		historico = new Historico();
		
		historico.setCheque(c);
		historico.setUsuario(u);
		historico.setDestinatario(d);
		historico.setData(dataAtual());
		historico.setOperacao(opearacao);
		
		try {
			trx.begin();
			manager.persist(historico);
			trx.commit();
			
			JOptionPane.showMessageDialog(null, "Histórico criado com sucesso!");
			
		} catch (Exception e) {
			
		}
		
	}
	
	private void excluirCheque(Cheque b) {
		try {

			trx.begin();
			manager.remove(b);
			trx.commit();

			JOptionPane.showMessageDialog(null, "O cheque foi excluida com sucesso!");
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "ERRO! " + e);
		}
	}

	private void limparTabela() {
		while (table.getModel().getRowCount() > 0) {
			tableModelCheque.removeCheque(0);
		}
	}

	private void buscarNome(String nome) {

		// #############################################
		final Thread tr = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					TimeUnit.SECONDS.sleep(0);
				} catch (InterruptedException ex) {
					Logger.getLogger(JanelaCheque.class.getName()).log(Level.SEVERE, null, ex);
				}
				// ######################METODO A SER
				// EXECUTADO##############################
				limparTabela();

				try {

					// trx.begin();
					Query consulta = manager.createQuery("FROM Cheque WHERE numCheque LIKE '%" + nome + "%' OR codCheque LIKE '%" + nome + "%'");
					List<Cheque> listaCheques = consulta.getResultList();
					// trx.commit();

					for (int i = 0; i < listaCheques.size(); i++) {
						Cheque b = listaCheques.get(i);
						tableModelCheque.addCheque(b);
					}
				} catch (Exception e) {
					JOptionPane.showMessageDialog(null, "Erro ao carregar a tabela de cheques: " + e);
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

	private void buscarCheque(String cod) {
		String codCheque = cod;

		String banco = codCheque.substring(0, 3);		
		
		String agencia = codCheque.substring(3, 7);
		String numeroCheque = codCheque.substring(11, 17);
		String conta = "000";
		
		if (banco.equals("001")) { //BANCO DO BRASIL
			conta = codCheque.substring(23, 29);
		}
		if (banco.equals("237")) { //BRADESCO
			conta = codCheque.substring(22, 29);
		}
		if (banco.equals("104")) { //CAIXA
			conta = codCheque.substring(20, 29);
		}
		if (banco.equals("341")) { //ITAU
			conta = codCheque.substring(23, 29);
		}
		if (banco.equals("353")) { //SANTANDER
			conta = codCheque.substring(21, 29);
		}
		if (banco.equals("004")) { //BANCO DO NORDESTE
			conta = codCheque.substring(23, 29);
		}
		if (banco.equals("756")) { // SICOOB
			conta = codCheque.substring(22, 29);
		}

		tfCodAgencia.setText(agencia);
		tfCodBanco.setText(banco);
		tfNumCheque.setText(numeroCheque);
		tfNumConta.setText(conta);

	}

	private void buscarBanco(String cod) {

		tfCodBanco.setBackground(Color.WHITE);
		try {

			Query consulta = manager.createQuery("FROM Banco WHERE cod LIKE '" + cod + "'");
			List<Banco> listaBancos = consulta.getResultList();

			banco = listaBancos.get(0);
			tfNomeBanco.setText(banco.getNome());
			tfCodBanco.setBackground(Color.GREEN);

		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Banco não encontrado!");
			tfCodBanco.setBackground(Color.ORANGE);
		}
	}

	private void buscarAgencia(String cod) {

		tfCodAgencia.setBackground(Color.WHITE);

		try {

			Query consulta = manager.createQuery("FROM Agencia WHERE cod LIKE '" + cod + "'");
			List<Agencia> listaAgencias = consulta.getResultList();

			agencia = listaAgencias.get(0);
			tfCodAgencia.setBackground(Color.GREEN);

		} catch (Exception e) {
			
			Object[] options = { "Sim", "Não" };
			int resposta = JOptionPane.showOptionDialog(null,
							"Agência não encontrada, gostaria que seja criado altomaticamente? ",
							"Agência não encontrada", JOptionPane.YES_NO_OPTION,
							JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
			
			if (resposta == JOptionPane.YES_OPTION) {
				   criarAgencia();
				} else if (resposta == JOptionPane.NO_OPTION) {
					JOptionPane.showMessageDialog(null, "Agência não encontrada!");
					tfCodAgencia.setBackground(Color.ORANGE);
				}
						
		}
	}

	private void buscarConta(String cod) {

		tfNumConta.setBackground(Color.WHITE);

		try {

			Query consulta = manager.createQuery("FROM Conta WHERE numero LIKE '" + cod + "'");
			List<Conta> listarContas = consulta.getResultList();

			conta = listarContas.get(0);
			tfNumConta.setBackground(Color.GREEN);

		} catch (Exception e) {
			
			Object[] options = { "Sim", "Não" };
			int resposta = JOptionPane.showOptionDialog(null,
							"Conta não encontrada, gostaria que seja criado altomaticamente? ",
							"Conta não encontrada", JOptionPane.YES_NO_OPTION,
							JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
			
			if (resposta == JOptionPane.YES_OPTION) {
				   criarConta();
				} else if (resposta == JOptionPane.NO_OPTION) {
					JOptionPane.showMessageDialog(null, "Conta não encontrada!");
					tfCodAgencia.setBackground(Color.ORANGE);
				}
			//JOptionPane.showMessageDialog(null, "Conta não encontrada!");
			//tfNumConta.setBackground(Color.ORANGE);
		}
	}

	private void buscarProprietario(Banco b, Agencia a, Conta c) {

		tfNomeProprietario.setBackground(Color.WHITE);

		try {

			Query consulta = manager.createQuery("FROM Cheque WHERE banco_id LIKE '" + b.getId()
					+ "' AND agencia_id LIKE '" + a.getId() + "' AND conta_id LIKE '" + c.getId() + "'");
			List<Cheque> listarCheques = consulta.getResultList();

			Cheque chq = listarCheques.get(0);

			proprietario = chq.getProprietario();

			tfNomeProprietario.setText(proprietario.getNome());
			tfNomeProprietario.setBackground(Color.GREEN);

		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Proprietario não encontrado!");
			tfNomeProprietario.setBackground(Color.ORANGE);
		}
	}

	private java.util.Date dataAtual() {

		java.util.Date hoje = new java.util.Date();
		// java.util.Date hoje = Calendar.getInstance().getTime();
		return hoje;

	}

	private void botaoAddBanco() {
		// #############################################
		final Thread tr = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					TimeUnit.SECONDS.sleep(0);
				} catch (InterruptedException ex) {
					Logger.getLogger(JanelaMenuPrincipal.class.getName()).log(Level.SEVERE, null, ex);
				}
				// ######################METODO A SER
				// EXECUTADO##############################
				ImportarBanco importarBanco = new ImportarBanco();
				importarBanco.setModal(true);
				importarBanco.setVisible(true);
				importarBanco.setLocationRelativeTo(null);
				
				banco = importarBanco.getBanco();
				
				if (banco != null) {
					tfNomeBanco.setText(banco.getNome());
					tfNomeBanco.setBackground(Color.GREEN);
					
					tfCodBanco.setText(banco.getCod());
					tfCodBanco.setBackground(Color.GREEN);
				} else {
					tfNomeBanco.setBackground(Color.ORANGE);
					tfCodBanco.setBackground(Color.ORANGE);
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
				EsperaJanela espera = new EsperaJanela();
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

	private void botaoAddAgencia() {
		// #############################################
		final Thread tr = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					TimeUnit.SECONDS.sleep(0);
				} catch (InterruptedException ex) {
					Logger.getLogger(JanelaMenuPrincipal.class.getName()).log(Level.SEVERE, null, ex);
				}
				// ######################METODO A SER
				// EXECUTADO##############################
				ImportarAgencia importarAgencia = new ImportarAgencia();
				importarAgencia.setModal(true);
				importarAgencia.setVisible(true);
				importarAgencia.setLocationRelativeTo(null);
				
				agencia = importarAgencia.getAgencia();
				
				if (agencia != null) {
					tfCodAgencia.setText(agencia.getCod());
					tfCodAgencia.setBackground(Color.GREEN);
					
				} else {
					tfCodAgencia.setBackground(Color.ORANGE);
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
				EsperaJanela espera = new EsperaJanela();
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

	private void botaoAddConta() {
		// #############################################
		final Thread tr = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					TimeUnit.SECONDS.sleep(0);
				} catch (InterruptedException ex) {
					Logger.getLogger(JanelaMenuPrincipal.class.getName()).log(Level.SEVERE, null, ex);
				}
				// ######################METODO A SER
				// EXECUTADO##############################
				ImportarConta importarConta = new ImportarConta();
				importarConta.setModal(true);
				importarConta.setVisible(true);
				importarConta.setLocationRelativeTo(null);
				
				conta = importarConta.getConta();
				
				if (conta != null) {
					tfNumConta.setText(conta.getNumero());
					tfNumConta.setBackground(Color.GREEN);
					
				} else {
					tfNumConta.setBackground(Color.ORANGE);
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
				EsperaJanela espera = new EsperaJanela();
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
	
	private void botaoAddProprietario() {
		// #############################################
		final Thread tr = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					TimeUnit.SECONDS.sleep(0);
				} catch (InterruptedException ex) {
					Logger.getLogger(JanelaMenuPrincipal.class.getName()).log(Level.SEVERE, null, ex);
				}
				// ######################METODO A SER
				// EXECUTADO##############################
				ImportarProprietario importarProprietario = new ImportarProprietario();		
				importarProprietario.setModal(true);
				importarProprietario.setVisible(true);
				importarProprietario.setLocationRelativeTo(null);
				
				proprietario = importarProprietario.getProprietario();
				
				if (proprietario != null) {
					tfNomeProprietario.setText(proprietario.getNome());
					tfNomeProprietario.setBackground(Color.GREEN);
				} else {
					tfNomeProprietario.setBackground(Color.ORANGE);
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
				EsperaJanela espera = new EsperaJanela();
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
	
	private void carregajcbDestinatarioNovo() {

		cbDestinatario.removeAllItems();

		try {

			//trx.begin();
			Query consulta = manager.createQuery("FROM Destinatario WHERE local = '1'");
			List<Destinatario> listaDestinatarios = consulta.getResultList();
			//trx.commit();

			for (int i = 0; i < listaDestinatarios.size(); i++) {

				Destinatario d = listaDestinatarios.get(i);
				cbDestinatario.addItem(d);
			}

		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Erro no carregamento do cbDestinatario! " + e);
		}
		
		//cbDestinatario.setSelectedIndex(0);
	}
	
	private void carregajcbDestinatarioTodos() {

		cbDestinatario.removeAllItems();

		try {

			//trx.begin();
			Query consulta = manager.createQuery("FROM Destinatario");
			List<Destinatario> listaDestinatarios = consulta.getResultList();
			//trx.commit();

			for (int i = 0; i < listaDestinatarios.size(); i++) {

				Destinatario d = listaDestinatarios.get(i);
				cbDestinatario.addItem(d);
			}

		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Erro no carregamento do cbDestinatario! " + e);
		}
	}
	
	private void buscarImagemLocal() throws IOException, InterruptedException {

		BufferedImage tmp = null;
		BufferedImage newImage = null;

		String sistema = System.getProperty("os.name");

		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setDialogTitle("Salvar imagem...");

		int opcao = fileChooser.showSaveDialog(null);

		if (opcao == JFileChooser.APPROVE_OPTION) {

			File url = fileChooser.getSelectedFile();

			try {

				tmp = ImageIO.read(url);

				int w = 540;
				int h = 250;

				newImage = new BufferedImage(w, h, tmp.getType());
				Graphics2D g2d = newImage.createGraphics();
				g2d.setComposite(AlphaComposite.Src);
				g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
				g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
				g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
				g2d.drawImage(tmp, 0, 0, w, h, null);
				g2d.dispose();

				lblImagem.setIcon(new ImageIcon(newImage));

			} catch (IOException e) {

				e.printStackTrace();
			}
		}
		
		if ("Linux".equals(sistema)) {
			arquivoFoto = new File("/opt/GrupoCaravela/imagens/img.png");
		} else {
			arquivoFoto = new File("c:\\GrupoCaravela\\imagens\\img.png");
		}
		
		ImageIO.write(newImage, "png", new File(arquivoFoto.getAbsolutePath()));
	
	}

	private void salvarImagem() throws Exception {
		
		try {
			int totalBytes;
			int byteTrasferred;

			HttpURLConnection httpUrlConnection = (HttpURLConnection) new URL(
					"http://" + Empresa.getIpLocalServidor() + ":" + Empresa.getPortaLocalHttpServidor()
							+ "/organiza/upload.php?filename=" + cheque.getId() + ".png")
									.openConnection();
			httpUrlConnection.setDoOutput(true);
			httpUrlConnection.setRequestMethod("POST");
			OutputStream os = httpUrlConnection.getOutputStream();
			Thread.sleep(1000);
			
			BufferedInputStream fis = new BufferedInputStream(new FileInputStream(arquivoFoto));
			totalBytes = fis.available();

			for (int i = 0; i < totalBytes; i++) {
				os.write(fis.read());
				byteTrasferred = i + 1;
			}

			os.close();
			BufferedReader in = new BufferedReader(new InputStreamReader(httpUrlConnection.getInputStream()));

			String s = null;
			while ((s = in.readLine()) != null) {
				System.out.println(s);
			}
			in.close();
			fis.close();
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	private void buscarImagemServidor() {

		//String sistema = System.getProperty("os.name");
		ImageIcon imgThisImg = null;

		try {
			URL url = null;
			url = new URL("http://" + Empresa.getIpLocalServidor() + ":" + Empresa.getPortaLocalHttpServidor()
					+ "/organiza/" + cheque.getId() + ".png");
			url.openStream();
			imgThisImg = new ImageIcon(url);
			lblImagem.setIcon(imgThisImg);

		} catch (Exception e1) {
			lblImagem.setIcon(new ImageIcon(JanelaCheque.class.getResource("/br/com/grupocaravela/imagens/semFoto.png")));
		}
	}	
	
	private void verificarUsuario(){
		
		if (usuario.getAdiministrador()) {
			btnExcluir.setEnabled(true);
		}
		
	}
	
	private void criarAgencia(){
		agencia = new Agencia();
		
		agencia.setCod(tfCodAgencia.getText());
		agencia.setBanco(banco);
		
		try {
			trx.begin();
			manager.persist(agencia);
			trx.commit();
			
			JOptionPane.showMessageDialog(null, "Agência de código " + tfCodAgencia.getText() + " salva com sucesso");
			tfCodAgencia.setBackground(Color.GREEN);
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Erro ao salvar a agência!!! ERRO: " + e);
		}
				
		
	}
	
	private void criarConta(){
		
		conta = new Conta();
		
		conta.setAgencia(agencia);
		conta.setBanco(banco);
		conta.setNumero(tfNumConta.getText());
		
		try {
			
			trx.begin();
			manager.persist(conta);
			trx.commit();
			
			JOptionPane.showMessageDialog(null, "Conta de numero " + tfNumConta.getText() + " salva com sucesso");
			tfNumConta.setBackground(Color.GREEN);
			
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Erro ao salvar a agência!!! ERRO: " + e );
		}
		
	}
	
	private boolean verificaExistenciaCheque(){
		boolean retorno = false;
		
		try {
			Query consulta = manager.createQuery("FROM Cheque WHERE codCheque LIKE '" + tfCodBarrasCheque.getText() + "'");
			List<Cheque> listaCheques = consulta.getResultList();
			//trx.commit();

			if (listaCheques.size() > 0) {
				retorno = true;
			}else{
				retorno = false;
			}
			
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Erro ao consultar existência de cheque!!! ERRO: " + e );
		}
		
		return retorno;
		
	}
}
