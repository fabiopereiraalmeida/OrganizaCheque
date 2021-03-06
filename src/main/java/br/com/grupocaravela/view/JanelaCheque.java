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
import java.util.GregorianCalendar;
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
import br.com.grupocaravela.objeto.Beneficiado;
import br.com.grupocaravela.objeto.Cheque;
import br.com.grupocaravela.objeto.Conta;
import br.com.grupocaravela.objeto.Destinatario;
import br.com.grupocaravela.objeto.Historico;
import br.com.grupocaravela.objeto.Proprietario;
import br.com.grupocaravela.objeto.Usuario;
import br.com.grupocaravela.render.MeuRenderer;
import br.com.grupocaravela.render.MoedaRender;
import br.com.grupocaravela.render.TableRenderer;
import br.com.grupocaravela.tablemodel.TableModelBeneficiado;
import br.com.grupocaravela.tablemodel.TableModelBeneficiado;
import br.com.grupocaravela.tablemodel.TableModelCheque;
import javax.swing.JCheckBox;
import java.awt.event.InputMethodListener;
import java.awt.event.InputMethodEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeEvent;

public class JanelaCheque extends JFrame {

	private JPanel contentPane;
	private JTable table;
	private JTextField tfCodBarrasCheque;

	private EntityManager manager;
	private EntityTransaction trx;
	private TableModelCheque tableModelCheque;
	private TableModelBeneficiado tableModelBeneficiado;

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
	private JComboBox cbDestinatarioAtual;
	private JLabel lblImagem;
		
	private File arquivoFoto;
	
	private Usuario usuario = UsuarioLogado.getUsuario();
	private JButton btnExcluir;
	private JCheckBox cbTerceiros;
	private JCheckBox chbDevolvido;
	private JTextField tfNumeroDias;
	private JTextField tfJurosBeneficiario;
	private JTextField tfLucroBeneficiaro;
	private JTable tableBeneficiado;
	private DecimalFormattedField tfJuros;
	private DecimalFormattedField tfValorPago;
	private DecimalFormattedField tfLucro;
	private JComboBox cbBeneficiario;
	private JButton button_1;
	private JButton button;
	private JButton button_2;
	private JButton button_4;
	private JButton btnCancelar;
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
		setBounds(100, 100, 814, 613);
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

		btnCancelar = new JButton("Cancelar");
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
		tfCodBarrasCheque.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				
				///######################################################
				
				//iniciaConexao();
				
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
						if (cheque.getAgencia().equals(null)) {
							JOptionPane.showMessageDialog(null, "O cheque informado ja se encontra cadastrado no sistema");
						}
					}
					
				}else{
					JOptionPane.showMessageDialog(null, "ERRO!!! favor verificar o código do cheque");
					tfCodBarrasCheque.requestFocus();
					tfCodBarrasCheque.selectAll();
				}
				
				//######################################################
			}
		});
		tfCodBarrasCheque.setDisabledTextColor(Color.BLACK);
		tfCodBarrasCheque.setHorizontalAlignment(SwingConstants.CENTER);
		tfCodBarrasCheque.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					
					//iniciaConexao();
					
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
		
		cbDestinatarioAtual = new JComboBox();
		
		JLabel lblDestinatrio = new JLabel("Destinatário atual:");
		
		chbDevolvido = new JCheckBox("Devolvido");
		chbDevolvido.setEnabled(false);
		GroupLayout gl_panel_6 = new GroupLayout(panel_6);
		gl_panel_6.setHorizontalGroup(
			gl_panel_6.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_panel_6.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_panel_6.createParallelGroup(Alignment.LEADING)
						.addComponent(panel_12, Alignment.TRAILING, GroupLayout.PREFERRED_SIZE, 774, Short.MAX_VALUE)
						.addComponent(tfCodBarrasCheque, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 774, Short.MAX_VALUE)
						.addComponent(lblCod, Alignment.TRAILING)
						.addGroup(Alignment.TRAILING, gl_panel_6.createSequentialGroup()
							.addComponent(panel_10, GroupLayout.PREFERRED_SIZE, 269, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(panel_11, GroupLayout.DEFAULT_SIZE, 499, Short.MAX_VALUE))
						.addGroup(Alignment.TRAILING, gl_panel_6.createSequentialGroup()
							.addComponent(panel_8, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(panel_9, GroupLayout.PREFERRED_SIZE, 225, GroupLayout.PREFERRED_SIZE))
						.addGroup(Alignment.TRAILING, gl_panel_6.createSequentialGroup()
							.addComponent(cbDestinatarioAtual, GroupLayout.PREFERRED_SIZE, 260, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED, 421, Short.MAX_VALUE)
							.addComponent(chbDevolvido))
						.addComponent(lblDestinatrio))
					.addContainerGap())
		);
		gl_panel_6.setVerticalGroup(
			gl_panel_6.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_6.createSequentialGroup()
					.addContainerGap()
					.addComponent(lblCod)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(tfCodBarrasCheque, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addGroup(gl_panel_6.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_panel_6.createSequentialGroup()
							.addGap(21)
							.addComponent(chbDevolvido))
						.addGroup(gl_panel_6.createSequentialGroup()
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(lblDestinatrio)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(cbDestinatarioAtual, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_panel_6.createParallelGroup(Alignment.LEADING, false)
						.addComponent(panel_9, 0, 0, Short.MAX_VALUE)
						.addComponent(panel_8, GroupLayout.PREFERRED_SIZE, 50, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addGroup(gl_panel_6.createParallelGroup(Alignment.LEADING, false)
						.addComponent(panel_11, 0, 0, Short.MAX_VALUE)
						.addComponent(panel_10, GroupLayout.PREFERRED_SIZE, 50, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(panel_12, GroupLayout.PREFERRED_SIZE, 105, GroupLayout.PREFERRED_SIZE)
					.addContainerGap(117, Short.MAX_VALUE))
		);

		JLabel lblValor = new JLabel("Valor:");

		tfValor = new DecimalFormattedField(DecimalFormattedField.REAL);
		tfValor.setColumns(10);
		tfValor.setDisabledTextColor(Color.BLACK);

		JLabel lblNumCheque = new JLabel("Num. Cheque:");

		tfNumCheque = new JTextField();
		tfNumCheque.setDisabledTextColor(Color.BLACK);
		tfNumCheque.setColumns(10);

		JLabel lblDataEntrada = new JLabel("Data Entrada:");

		dcEntradaData = new JDateChooser();
		
		dcEntradaData.addPropertyChangeListener(new PropertyChangeListener() {
			public void propertyChange(PropertyChangeEvent evt) {
				//calcularJuros();
				
				
				try {
					tfNumeroDias.setText(String.valueOf(calcularDias()));
				} catch (Exception e2) {
					tfNumeroDias.setText("0");
				}
				
			}
		});
		dcEntradaData.getCalendarButton().addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				
			}
		});
		dcEntradaData.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				
			}
		});

		JLabel lblBomParaDia = new JLabel("Bom para data:");

		dcBomData = new JDateChooser();
		dcBomData.addPropertyChangeListener(new PropertyChangeListener() {
			public void propertyChange(PropertyChangeEvent evt) {
				
				//calcularJuros();
				
				
				try {
					tfNumeroDias.setText(String.valueOf(calcularDias()));
				} catch (Exception e2) {
					tfNumeroDias.setText("0");
				}
				
			}
		});
		dcBomData.addInputMethodListener(new InputMethodListener() {
			public void caretPositionChanged(InputMethodEvent event) {
			}
			public void inputMethodTextChanged(InputMethodEvent event) {
				
			}
		});
		dcBomData.getCalendarButton().addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {				
			}
		});
		dcBomData.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {								
			}
		});
		
		JLabel lblCodVenda = new JLabel("Cod. Venda:");
		
		tfCodVenda = new JTextField();
		tfCodVenda.setDisabledTextColor(Color.BLACK);
		tfCodVenda.setColumns(10);
		
		JLabel lblObs = new JLabel("Obs:");
		
		tfObs = new JTextField();
		tfObs.setDisabledTextColor(Color.BLACK);
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
							.addComponent(lblNumCheque)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(tfNumCheque, GroupLayout.PREFERRED_SIZE, 129, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(lblValor)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(tfValor, GroupLayout.PREFERRED_SIZE, 159, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.UNRELATED)
							.addComponent(lblCodVenda)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(tfCodVenda, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
							.addGap(69))
						.addGroup(gl_panel_12.createSequentialGroup()
							.addComponent(lblDataEntrada)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(dcEntradaData, GroupLayout.PREFERRED_SIZE, 158, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(lblBomParaDia)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(dcBomData, GroupLayout.PREFERRED_SIZE, 151, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(cbTerceiros)
							.addContainerGap(85, Short.MAX_VALUE))
						.addGroup(gl_panel_12.createSequentialGroup()
							.addComponent(lblObs)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(tfObs, GroupLayout.DEFAULT_SIZE, 717, Short.MAX_VALUE)
							.addContainerGap())))
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
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addGroup(gl_panel_12.createParallelGroup(Alignment.LEADING)
						.addComponent(lblBomParaDia)
						.addComponent(dcBomData, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(cbTerceiros)
						.addComponent(lblDataEntrada)
						.addComponent(dcEntradaData, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addGroup(gl_panel_12.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblObs)
						.addComponent(tfObs, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addContainerGap(88, Short.MAX_VALUE))
		);
		panel_12.setLayout(gl_panel_12);

		JLabel lblNome = new JLabel("Nome");

		tfNomeProprietario = new JTextField();
		tfNomeProprietario.setEditable(false);
		tfNomeProprietario.setColumns(10);
		
		button_4 = new JButton("");
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

		button_2 = new JButton("");
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
		tfCodAgencia.setDisabledTextColor(Color.BLACK);
		tfCodAgencia.setColumns(10);

		button = new JButton("");
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
		tfCodBanco.setDisabledTextColor(Color.BLACK);
		tfCodBanco.setColumns(10);

		JLabel lblNome_1 = new JLabel("nome");

		tfNomeBanco = new JTextField();
		tfNomeBanco.setEditable(false);
		tfNomeBanco.setColumns(10);

		button_1 = new JButton("");
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
		
		JPanel panel_15 = new JPanel();
		tabbedPane.addTab("Juros e ganhos", null, panel_15, null);
		
		JPanel panel_16 = new JPanel();
		panel_16.setBorder(new TitledBorder(null, "Informa\u00E7\u00F5es de troca", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		
		JLabel label_1 = new JLabel("Nº Dias:");
		
		tfNumeroDias = new JTextField();
		tfNumeroDias.setEditable(false);
		tfNumeroDias.setColumns(10);
		
		JLabel label_2 = new JLabel("Juros:");
		
		tfJuros = new DecimalFormattedField("#,##0.00'%';-#,##0.00'%'");
		tfJuros.setEditable(false);
		tfJuros.setEnabled(false);
		tfJuros.setColumns(10);
		tfJuros.setDisabledTextColor(Color.BLACK);
		
		JLabel label_3 = new JLabel("Lucro:");
		
		tfLucro = new DecimalFormattedField("R$ #,##0.00;R$ -#,##0.00");
		tfLucro.setForeground(Color.BLACK);
		tfLucro.setEnabled(false);
		tfLucro.setEditable(false);
		tfLucro.setDisabledTextColor(Color.BLACK);
		tfLucro.setColumns(10);
		
		JLabel label_4 = new JLabel("Valor a ser pago:");
		
		tfValorPago = new DecimalFormattedField("R$ #,##0.00;R$ -#,##0.00");
		tfValorPago.setEnabled(false);
		tfValorPago.setEditable(false);
		tfValorPago.setDisabledTextColor(Color.BLACK);
		tfValorPago.setColumns(10);
		GroupLayout gl_panel_16 = new GroupLayout(panel_16);
		gl_panel_16.setHorizontalGroup(
			gl_panel_16.createParallelGroup(Alignment.LEADING)
				.addGap(0, 774, Short.MAX_VALUE)
				.addGroup(gl_panel_16.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_panel_16.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_panel_16.createSequentialGroup()
							.addComponent(label_1, GroupLayout.PREFERRED_SIZE, 57, GroupLayout.PREFERRED_SIZE)
							.addGap(12)
							.addComponent(tfNumeroDias, GroupLayout.PREFERRED_SIZE, 105, GroupLayout.PREFERRED_SIZE)
							.addGap(18)
							.addComponent(label_2, GroupLayout.PREFERRED_SIZE, 41, GroupLayout.PREFERRED_SIZE)
							.addGap(12)
							.addComponent(tfJuros, GroupLayout.PREFERRED_SIZE, 114, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.UNRELATED)
							.addComponent(label_3)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(tfLucro, GroupLayout.PREFERRED_SIZE, 136, GroupLayout.PREFERRED_SIZE))
						.addGroup(gl_panel_16.createSequentialGroup()
							.addComponent(label_4)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(tfValorPago, GroupLayout.PREFERRED_SIZE, 150, GroupLayout.PREFERRED_SIZE)))
					.addContainerGap(183, Short.MAX_VALUE))
		);
		gl_panel_16.setVerticalGroup(
			gl_panel_16.createParallelGroup(Alignment.LEADING)
				.addGap(0, 90, Short.MAX_VALUE)
				.addGroup(gl_panel_16.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_panel_16.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_panel_16.createSequentialGroup()
							.addGap(2)
							.addComponent(label_1))
						.addComponent(tfNumeroDias, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addGroup(gl_panel_16.createSequentialGroup()
							.addGap(2)
							.addComponent(label_2))
						.addGroup(gl_panel_16.createParallelGroup(Alignment.BASELINE)
							.addComponent(tfJuros, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
							.addComponent(label_3)
							.addComponent(tfLucro, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)))
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addGroup(gl_panel_16.createParallelGroup(Alignment.BASELINE)
						.addComponent(label_4)
						.addComponent(tfValorPago, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
		);
		panel_16.setLayout(gl_panel_16);
		
		JPanel panel_17 = new JPanel();
		panel_17.setBorder(new TitledBorder(null, "Beneficiarios", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		GroupLayout gl_panel_15 = new GroupLayout(panel_15);
		gl_panel_15.setHorizontalGroup(
			gl_panel_15.createParallelGroup(Alignment.LEADING)
				.addGroup(Alignment.TRAILING, gl_panel_15.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_panel_15.createParallelGroup(Alignment.TRAILING)
						.addComponent(panel_17, Alignment.LEADING, GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
						.addComponent(panel_16, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 774, Short.MAX_VALUE))
					.addGap(12))
		);
		gl_panel_15.setVerticalGroup(
			gl_panel_15.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_15.createSequentialGroup()
					.addContainerGap()
					.addComponent(panel_16, GroupLayout.PREFERRED_SIZE, 90, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(panel_17, GroupLayout.DEFAULT_SIZE, 418, Short.MAX_VALUE)
					.addContainerGap())
		);
		
		JLabel lblBeneficiario = new JLabel("Destinatário beneficiario:");
		
		cbBeneficiario = new JComboBox();
		
		tfJurosBeneficiario = new DecimalFormattedField(DecimalFormattedField.PORCENTAGEM);
		tfJurosBeneficiario.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				
				calcularJurosBeneficiario();
				
			}
		});
		tfJurosBeneficiario.setColumns(10);
		
		JLabel lblJuros = new JLabel("Juros:");
		
		tfLucroBeneficiaro = new DecimalFormattedField(DecimalFormattedField.REAL);
		tfLucroBeneficiaro.setEnabled(false);
		tfLucroBeneficiaro.setEditable(false);
		tfLucroBeneficiaro.setColumns(10);
		tfLucroBeneficiaro.setDisabledTextColor(Color.BLACK);
		
		JLabel lblLucro = new JLabel("Lucro:");
		
		JButton btnIncluir = new JButton("Incluir beneficiario");
		btnIncluir.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				if (cbBeneficiario.getSelectedIndex() == -1) {
					JOptionPane.showMessageDialog(null, "É necessario informar um beneficiario!");
					cbBeneficiario.requestFocus();
				}else{
					limparTabelaBeneficiao();
					adicionarBeneficio();
					carregarTabelaBeneficio(cheque);
					limparCamposBeneficiario();
				}				
			}
		});
		btnIncluir.setIcon(new ImageIcon(JanelaCheque.class.getResource("/br/com/grupocaravela/icones/mais_64.png")));
		
		JPanel panel_18 = new JPanel();
		
		JButton btnExcluirBeneficiario = new JButton("Excluir beneficiario");
		btnExcluirBeneficiario.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Object[] options = { "Sim", "Não" };
				int i = JOptionPane.showOptionDialog(null,
						"ATENÇÃO!!! Esta operação irá excluir o beneficiado selecionado! Gostaria de continuar?",
						"Excluir", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options,
						options[0]);

				if (i == JOptionPane.YES_OPTION) {
					botaoExcluirBeneficiado();
				}		
			}
		});
		btnExcluirBeneficiario.setIcon(new ImageIcon(JanelaCheque.class.getResource("/br/com/grupocaravela/icones/alerta_vermelho_64.png")));
		GroupLayout gl_panel_17 = new GroupLayout(panel_17);
		gl_panel_17.setHorizontalGroup(
			gl_panel_17.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_17.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_panel_17.createParallelGroup(Alignment.LEADING)
						.addComponent(panel_18, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
						.addGroup(gl_panel_17.createParallelGroup(Alignment.LEADING)
							.addGroup(gl_panel_17.createSequentialGroup()
								.addGroup(gl_panel_17.createParallelGroup(Alignment.LEADING)
									.addComponent(cbBeneficiario, GroupLayout.PREFERRED_SIZE, 260, GroupLayout.PREFERRED_SIZE)
									.addComponent(lblBeneficiario))
								.addGap(18)
								.addGroup(gl_panel_17.createParallelGroup(Alignment.LEADING)
									.addComponent(tfJurosBeneficiario, GroupLayout.PREFERRED_SIZE, 99, GroupLayout.PREFERRED_SIZE)
									.addComponent(lblJuros))
								.addGap(18)
								.addGroup(gl_panel_17.createParallelGroup(Alignment.LEADING)
									.addComponent(lblLucro)
									.addComponent(tfLucroBeneficiaro, GroupLayout.PREFERRED_SIZE, 91, GroupLayout.PREFERRED_SIZE))
								.addPreferredGap(ComponentPlacement.UNRELATED)
								.addComponent(btnIncluir)
								.addContainerGap())
							.addGroup(Alignment.TRAILING, gl_panel_17.createSequentialGroup()
								.addComponent(btnExcluirBeneficiario)
								.addGap(12)))))
		);
		gl_panel_17.setVerticalGroup(
			gl_panel_17.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_17.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_panel_17.createParallelGroup(Alignment.TRAILING)
						.addGroup(gl_panel_17.createSequentialGroup()
							.addGroup(gl_panel_17.createParallelGroup(Alignment.BASELINE)
								.addComponent(lblBeneficiario)
								.addComponent(lblJuros)
								.addComponent(lblLucro))
							.addPreferredGap(ComponentPlacement.RELATED)
							.addGroup(gl_panel_17.createParallelGroup(Alignment.BASELINE)
								.addComponent(cbBeneficiario, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
								.addComponent(tfJurosBeneficiario, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
								.addComponent(tfLucroBeneficiaro, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
							.addGap(13))
						.addGroup(gl_panel_17.createSequentialGroup()
							.addComponent(btnIncluir, GroupLayout.PREFERRED_SIZE, 46, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.UNRELATED)))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(panel_18, GroupLayout.DEFAULT_SIZE, 215, Short.MAX_VALUE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(btnExcluirBeneficiario, GroupLayout.PREFERRED_SIZE, 46, GroupLayout.PREFERRED_SIZE)
					.addContainerGap())
		);
		
		JScrollPane scrollPane_1 = new JScrollPane();
		
		tableBeneficiado = new JTable();
		scrollPane_1.setViewportView(tableBeneficiado);
		GroupLayout gl_panel_18 = new GroupLayout(panel_18);
		gl_panel_18.setHorizontalGroup(
			gl_panel_18.createParallelGroup(Alignment.LEADING)
				.addComponent(scrollPane_1, GroupLayout.DEFAULT_SIZE, 752, Short.MAX_VALUE)
		);
		gl_panel_18.setVerticalGroup(
			gl_panel_18.createParallelGroup(Alignment.LEADING)
				.addComponent(scrollPane_1, GroupLayout.DEFAULT_SIZE, 204, Short.MAX_VALUE)
		);
		panel_18.setLayout(gl_panel_18);
		panel_17.setLayout(gl_panel_17);
		panel_15.setLayout(gl_panel_15);

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
		
		this.tableModelBeneficiado = new TableModelBeneficiado();
		this.tableBeneficiado.setModel(tableModelBeneficiado);

		
		// Render de Moeda
		NumberFormat numeroMoeda = NumberFormat.getNumberInstance();
		numeroMoeda.setMinimumFractionDigits(2);
		DefaultTableCellRenderer cellRendererCustomMoeda = new MoedaRender(numeroMoeda);
		table.getColumnModel().getColumn(5).setCellRenderer(cellRendererCustomMoeda);
		
		tableBeneficiado.getColumnModel().getColumn(2).setCellRenderer(cellRendererCustomMoeda);
		
		
	}

	private void tamanhoColunas() {
		// tableProdutos.setAutoResizeMode(tableProdutos.AUTO_RESIZE_OFF);
		table.getColumnModel().getColumn(0).setWidth(30);
		table.getColumnModel().getColumn(0).setMaxWidth(50);		
		table.getColumnModel().getColumn(7).setWidth(150);
		table.getColumnModel().getColumn(7).setMinWidth(150);
		
		tableBeneficiado.getColumnModel().getColumn(0).setWidth(450);
		tableBeneficiado.getColumnModel().getColumn(0).setMinWidth(400);
		

	}
	
	private void iniciaConexao() {

		// factory = Persistence.createEntityManagerFactory("DesbravarPU");
		manager = EntityManagerProducer.createEntityManager();
		trx = manager.getTransaction();
	}

	private void botaoNovoCheque() {

		limparCampos();
		carregajcbDestinatarioNovo();
		carregajcbBeneficiario();
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
	
private void botaoExcluirBeneficiado() {
		
		int linha = tableBeneficiado.getSelectedRow();
        int linhaReal = tableBeneficiado.convertRowIndexToModel(linha);
		
        Beneficiado ben = tableModelBeneficiado.getBeneficiado(linhaReal);		
		excluirBeneficio(ben);
		limparTabelaBeneficiao();
		carregarTabelaBeneficio(cheque);
	}

	private void botaoDetalhes() {
		
		limparTabelaBeneficiao();
		limparCamposBeneficiario();
		
		limparCampos();
		carregajcbDestinatarioTodos();
		carregajcbBeneficiario();
		
		int linha = table.getSelectedRow();
        int linhaReal = table.convertRowIndexToModel(linha);
		
		cheque = tableModelCheque.getCheque(linhaReal);
		
		banco = cheque.getBanco();
		agencia = cheque.getAgencia();
		conta = cheque.getConta();
		proprietario = cheque.getProprietario();
		destinatario = cheque.getDestinatario();		
		
		carregarCampos(cheque);
		tfNumeroDias.setText(String.valueOf(calcularDias()));
		buscarImagemServidor();
		tabbedPane.setSelectedIndex(1);
				
		carregarTabelaBeneficio(cheque);
		verificaAdministrador();
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
		
		cbDestinatarioAtual.setSelectedItem(c.getDestinatario());

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
		
		//Juros
		
		//tfNumeroDias.setText(c.getDias().toString());
		try {
			tfJuros.setText(c.getJuros().toString());
		} catch (Exception e) {
			tfJuros.setText("0");
		}
		
		try {
			tfLucro.setText(c.getLucro().toString());
		} catch (Exception e) {
			tfLucro.setText("0");
		}
		
		try {
			tfValorPago.setText(c.getValorPago().toString());
		} catch (Exception e) {
			tfValorPago.setText("0");
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
		tfNumeroDias.setText("0");
		
		try {
			cbDestinatarioAtual.setSelectedIndex(-1);
			//cbDestinatarioOrigem.setSelectedIndex(-1);
		} catch (Exception e) {
			// TODO: handle exception
		}
		
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
			
			destinatario = (Destinatario) cbDestinatarioAtual.getSelectedItem();
			
			
			if (novo) {
				c.setVoltouUmaVez(false);
				c.setVoltouDuasVezes(false);
				c.setDestinatarioOrigem(destinatario);
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
			//c.setDestinatarioOrigem((Destinatario) cbDestinatarioOrigem.getSelectedItem());
			
			c.setTerceiros(cbTerceiros.isSelected());
			
			if (chbDevolvido.isSelected()) {
				c.setVoltouUmaVez(true);
			}else{
				c.setVoltouUmaVez(false);
				c.setVoltouDuasVezes(false);
			}
			
			//Lucro
			//c.setDias(Double.parseDouble(tfNumeroDias.getText()));
			c.setLucro(Double.parseDouble(tfLucro.getText().replace("R$ ", "").replace(".", "").replace(",", ".")));
			c.setJuros(Double.parseDouble(tfJuros.getText().replace(".", "").replace(",", ".").replace("%", "")));
			c.setValorPago(Double.parseDouble(tfValorPago.getText().replace("R$ ", "").replace(".", "").replace(",", ".")));
			
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
	
	private void excluirBeneficio(Beneficiado b) {
		try {

			trx.begin();
			manager.remove(b);
			trx.commit();

			JOptionPane.showMessageDialog(null, "O beneficiado foi excluido com sucesso!");
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "ERRO! " + e);
		}
	}

	private void limparTabela() {
		while (table.getModel().getRowCount() > 0) {
			tableModelCheque.removeCheque(0);
		}
	}
	
	private void limparTabelaBeneficiao() {
		while (tableBeneficiado.getModel().getRowCount() > 0) {
			tableModelBeneficiado.removeBeneficiado(0);
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
	
	private void carregajcbBeneficiario() {

		cbBeneficiario.removeAllItems();

		try {

			//trx.begin();
			Query consulta = manager.createQuery("FROM Destinatario WHERE local = '1'");
			List<Destinatario> listaDestinatarios = consulta.getResultList();
			//trx.commit();

			for (int i = 0; i < listaDestinatarios.size(); i++) {

				Destinatario d = listaDestinatarios.get(i);
				cbBeneficiario.addItem(d);
			}

		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Erro no carregamento do cbDestinatario! " + e);
		}
		
		//cbDestinatario.setSelectedIndex(0);
	}
	
	private void carregajcbDestinatarioNovo() {

		cbDestinatarioAtual.removeAllItems();

		try {

			//trx.begin();
			Query consulta = manager.createQuery("FROM Destinatario WHERE local = '1'");
			List<Destinatario> listaDestinatarios = consulta.getResultList();
			//trx.commit();

			for (int i = 0; i < listaDestinatarios.size(); i++) {

				Destinatario d = listaDestinatarios.get(i);
				cbDestinatarioAtual.addItem(d);
			}

		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Erro no carregamento do cbDestinatario! " + e);
		}
		
		//cbDestinatario.setSelectedIndex(0);
	}
	
	private void carregajcbDestinatarioTodos() {

		cbDestinatarioAtual.removeAllItems();

		try {

			//trx.begin();
			Query consulta = manager.createQuery("FROM Destinatario");
			List<Destinatario> listaDestinatarios = consulta.getResultList();
			//trx.commit();

			for (int i = 0; i < listaDestinatarios.size(); i++) {

				Destinatario d = listaDestinatarios.get(i);
				cbDestinatarioAtual.addItem(d);
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
	
	private void calcularJuros(){
		
		Double juros = 0.0;
		try {
			juros = Double.parseDouble(tfJuros.getText().replace(".", "").replace(",", ".").replace("%", ""));
		} catch (Exception e) {
			juros = 0.0;
		}
				
		int dias = calcularDias();
		tfNumeroDias.setText(String.valueOf(dias));
				
		Double valorCheque = 0.0;
		
		try {
			valorCheque = Double.parseDouble(tfValor.getText().replace("R$ ", "").replace(".", "").replace(",", "."));
		} catch (Exception e) {
			valorCheque = 0.0;
		}
		
		Double jurosDesconto = (juros/30)*dias;
		
		Double lucro = (valorCheque/100) * jurosDesconto;
		
		Double valorPago = valorCheque - lucro;
		
		tfLucro.setText(lucro.toString());
		tfValorPago.setText(valorPago.toString());
		
	}
	
private void calcularJurosBeneficiario(){
		
		Double juros = 0.0;
		try {
			juros = Double.parseDouble(tfJurosBeneficiario.getText().replace(".", "").replace(",", ".").replace("%", ""));
		} catch (Exception e) {
			juros = 0.0;
		}
				
		int dias = calcularDias();
		tfNumeroDias.setText(String.valueOf(dias));
				
		Double valorCheque = 0.0;
		
		try {
			valorCheque = Double.parseDouble(tfValor.getText().replace("R$ ", "").replace(".", "").replace(",", "."));
		} catch (Exception e) {
			valorCheque = 0.0;
		}
		
		Double jurosDesconto = (juros/30)*dias;
		
		Double lucro = (valorCheque/100) * jurosDesconto;
		
		Double valorPago = valorCheque - lucro;
		
		tfLucroBeneficiaro.setText(lucro.toString());
		//tfValorPago.setText(valorPago.toString());
		
	}
	
	private int calcularDias(){
		try {
			GregorianCalendar ini = new GregorianCalendar();
			 GregorianCalendar fim = new GregorianCalendar();  
			 
			 ini.setTime(dcEntradaData.getDate());
			 fim.setTime(dcBomData.getDate());
			 
			 long dt1 = ini.getTimeInMillis();
			 long dt2 = fim.getTimeInMillis(); 
			 
			 return (int) (((dt2 - dt1) / 86400000)+1);
		} catch (Exception e) {
			return 0;
		}		 
	}
	
	private void adicionarBeneficio(){
		
		Beneficiado beneficiado = new Beneficiado();
		
		try {
			beneficiado.setCheque(cheque);
			beneficiado.setDestinatario((Destinatario) cbBeneficiario.getSelectedItem());
			beneficiado.setJuros(Double.parseDouble(tfJurosBeneficiario.getText().replace(".", "").replace(",", ".").replace("%", "")));
			beneficiado.setLucro(Double.parseDouble(tfLucroBeneficiaro.getText().replace("R$ ", "").replace(".", "").replace(",", ".")));
			
			trx.begin();
			manager.persist(beneficiado);
			manager.persist(cheque);
			trx.commit();
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Erro!");
		}
	}
	
	private void carregarTabelaBeneficio(Cheque cq){
		
		Double jurosTotal = 0.0;
		Double lucroTotal = 0.0;
		
		try {

			Query consulta = manager.createQuery("FROM Beneficiado WHERE cheque_id LIKE '" + cq.getId() + "'");
			List<Beneficiado> listaBeneficiados = consulta.getResultList();
			
			for (int i = 0; i < listaBeneficiados.size(); i++) {
				Beneficiado b = listaBeneficiados.get(i);
				tableModelBeneficiado.addBeneficiado(b);
				
				jurosTotal = jurosTotal + b.getJuros();
				lucroTotal = lucroTotal + b.getLucro();
				
			}
			
			tfLucro.setText(lucroTotal.toString());
			tfJuros.setText(jurosTotal.toString());
			
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Erro ao carregar a tabela de beneficiado: " + e);
		}		
	}
	
	private void limparCamposBeneficiario(){		
		cbBeneficiario.setSelectedIndex(-1);
		tfJurosBeneficiario.setText("0");
		tfLucroBeneficiaro.setText("0");		
	}
	
	private void verificaAdministrador(){
				
		if (usuario.getAdiministrador()) {
			
			tfCodBarrasCheque.setEnabled(true);
			cbDestinatarioAtual.setEnabled(true);
			tfCodBanco.setEnabled(true);
			button.setEnabled(true);
			button_1.setEnabled(true);
			button_2.setEnabled(true);
			button_4.setEnabled(true);
			tfCodAgencia.setEnabled(true);
			tfNumCheque.setEnabled(true);
			tfCodVenda.setEnabled(true);
			dcEntradaData.setEnabled(true);
			dcBomData.setEnabled(true);
			cbTerceiros.setEnabled(true);
			tfObs.setEnabled(true);
		}else{
			tfCodBarrasCheque.setEnabled(false);
			cbDestinatarioAtual.setEnabled(false);
			tfCodBanco.setEnabled(false);
			button.setEnabled(false);
			button_1.setEnabled(false);
			button_2.setEnabled(false);
			button_4.setEnabled(false);
			tfCodAgencia.setEnabled(false);
			tfNumCheque.setEnabled(false);
			tfCodVenda.setEnabled(false);
			dcEntradaData.setEnabled(false);
			dcBomData.setEnabled(false);
			cbTerceiros.setEnabled(false);
			tfObs.setEnabled(false);
		}
		
	}
}
