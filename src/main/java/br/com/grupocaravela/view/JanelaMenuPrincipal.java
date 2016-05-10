package br.com.grupocaravela.view;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.FileReader;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;
import javax.swing.ButtonGroup;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;

import org.jfree.ui.DateCellRenderer;

import com.toedter.calendar.JDateChooser;

import br.com.grupocaravela.aguarde.EsperaJanela;
import br.com.grupocaravela.aguarde.EsperaLista;
import br.com.grupocaravela.configuracao.BackUp;
import br.com.grupocaravela.configuracao.EntityManagerProducer;
import br.com.grupocaravela.dialog.MovimentarCheque;
import br.com.grupocaravela.dialog.ObservacaoCheque;
import br.com.grupocaravela.ferramenta.UsuarioLogado;
import br.com.grupocaravela.mask.DecimalFormattedField;
import br.com.grupocaravela.objeto.Cheque;
import br.com.grupocaravela.objeto.Destinatario;
import br.com.grupocaravela.objeto.Historico;
import br.com.grupocaravela.objeto.Usuario;
import br.com.grupocaravela.relatorios.ChamaRelatorio;
import br.com.grupocaravela.relatorios.ChamaRelatorioChequesSelecionados;
import br.com.grupocaravela.relatorios.ChamaRelatorioMovimentacao;
import br.com.grupocaravela.render.CoresJTable;
import br.com.grupocaravela.render.DateRenderer;
import br.com.grupocaravela.render.MoedaRender;
import br.com.grupocaravela.tablemodel.TableModelHistorico;
import br.com.grupocaravela.tablemodel.TableModelJanelaPrincipal;

public class JanelaMenuPrincipal extends JFrame {

	private EntityManager manager;
	private EntityTransaction trx;
	private TableModelJanelaPrincipal tableModelJanelaPrincipal;
	private TableModelHistorico tableModelHistorico;

	private JPanel contentPane;
	private JTable table;
	private JTextField tfLocalizar;
	private JTable tableHistorico;

	private Destinatario destinatario;
	private Cheque cheque;
	private Historico historico;
	private JTabbedPane tabbedPane;
	private JCheckBox chbSomenteChequeEm;

	private Usuario usuario = UsuarioLogado.getUsuario();
	private JLabel lblNomeUsuario;
	private JLabel lblSimNao;
	private JDateChooser dcFinal;
	private JDateChooser dcInicial;
	private JCheckBox chbFiltrarPorDataVencimento;

	private SimpleDateFormat formatSql = new SimpleDateFormat("yyyy-MM-dd");
	private SimpleDateFormat formatSqlHora = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	private SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
	private SimpleDateFormat formatData = new SimpleDateFormat("dd/MM/yyyy");
	private SimpleDateFormat formatDataHora = new SimpleDateFormat("dd-MM-yyyy_HH-mm-ss");

	private JTextField tfTotal;
	private JButton btnBuscar;
	private JTextPane tpObservacao;
	private final ButtonGroup buttonGroup = new ButtonGroup();
	private JRadioButton rdbtnCheque;
	private JRadioButton rdbtnProprietario;
	private JRadioButton rdbtnDestinatrio;

	private String observacaoMovimentacao;
	private JTextField tfHistoricoNumeroCheque;
	private JTextField tfHistoricoDataEntrada;
	private JTextField tfHistoricoNomeProprietario;
	private JTextField tfHistoricoBomPara;
	private JTextField tfHistoricoValor;

	private static final DateFormat DATE_FORMAT = new SimpleDateFormat("dd/MM/yyyy");
	private JMenuItem mntmUsurio;
	private JTextField tfTotalCheques;
	private JMenuItem mntmDownloadBackup;
	private JMenuItem mntmUploadBackup;
	private JCheckBox chckbxSelecionarTudo;

	private TableCellRenderer renderer = new CoresJTable();
	private TableCellRenderer rendererDate = new DateCellRenderer();
	private JCheckBox chbDataEntr;
	private final ButtonGroup buttonGroup_1 = new ButtonGroup();
	private JButton btnMovimentar;
	private JTextField tfQtdChequesDebitadosOrigem;
	private JTextField tfChequesDebitadosValorTotal;
	private JTextField tfQtdChequesCreditados;
	private JTextField tfValorTotalChequesCreditados;
	private JTextField tfQtdChequesCriados;
	private JTextField tfValorTotalChequesCriados;
	private JDateChooser dcExtratoDataInicial;
	private JDateChooser dcExtratoDataFinal;
	private JRadioButton rdbtnInformarData;
	private JRadioButton rdbtnHoje;
	private JRadioButton rdbt7Dias;
	private JRadioButton rdbt15Dias;
	private JRadioButton rdbt30Dias;
	
	private ArrayList<Cheque> listChequeDebito = new ArrayList<>();
	private ArrayList<Cheque> listChequeCredito = new ArrayList<>();
	private ArrayList<Cheque> listChequeCriados = new ArrayList<>();
	private ArrayList<Cheque> listChequeVencidos = new ArrayList<>();
	//private ArrayList<Cheque> listChequeHistorico = new ArrayList<>();
	private final ButtonGroup buttonGroup_2 = new ButtonGroup();
	private JButton btnGerarRelatrioDebitados;
	private JButton btnGerarRelatorioCriados;
	private JButton btnGerarRelatorioCreditados;
	private JTextField tfQtdExtratoVencidosOrigem;
	private JButton btnGerarRelatorioVencidos;
	private DecimalFormattedField tfValorTotalExtratoVencidosOrigem;
	private JButton btnSelecionarVencidos;
	private JButton btnSelecionarDebitados;
	private JButton btnSelecionarCreditados;
	private JButton btnSelecionarOrigem;
	private JLabel lblEmpresa;

	private Double th;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					JanelaMenuPrincipal frame = new JanelaMenuPrincipal();
					frame.setVisible(true);
					frame.setLocationRelativeTo(null);
										
				} catch (Exception e) {
					e.printStackTrace();
				}
								
			}
		});
		/*
		 * //Evento ao fechar a janela addWindowListener(new WindowAdapter() {
		 * 
		 * @Override public void windowClosing(WindowEvent e) { //sair(); } });
		 */

	}

	/**
	 * Create the frame.
	 */
	public JanelaMenuPrincipal() {
		CarregarJanela();
		carregarTableModel();
		tamanhoColunas();
		iniciaConexao();
		
		try {
			lblEmpresa.setText(lerArquivoNomeEmpresa());
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		table.setAutoCreateRowSorter(true);
		
		JPanel panel = new JPanel();
		tabbedPane.addTab("Extrato", null, panel, null);
		
		JLabel lblDataInicial_2 = new JLabel("Data Inicial:");
		
		JLabel lblDataFinal = new JLabel("Data Final:");
		
		dcExtratoDataInicial = new JDateChooser();
		
		dcExtratoDataFinal = new JDateChooser();
		
		rdbtnInformarData = new JRadioButton("Informar data");
		rdbtnInformarData.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if (rdbtnInformarData.isSelected()) {
					dcExtratoDataInicial.setDate(null);
					dcExtratoDataFinal.setDate(null);
					
					dcExtratoDataInicial.requestFocus();
				}
			}
		});
		buttonGroup_2.add(rdbtnInformarData);
		
		rdbtnHoje = new JRadioButton("Hoje");
		rdbtnHoje.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if (rdbtnHoje.isSelected()) {
					
					Date hj = dataAtual();
					
					dcExtratoDataInicial.setDate(hj);
					dcExtratoDataFinal.setDate(hj);
					
				}
			}
		});
		buttonGroup_2.add(rdbtnHoje);
		
		rdbt7Dias = new JRadioButton("7 Dias");
		rdbt7Dias.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if (rdbt7Dias.isSelected()) {
					
					Date hj = dataAtual();
					java.util.Calendar cal = Calendar.getInstance();
					cal.add(Calendar.DATE, -7);
					
					dcExtratoDataInicial.setDate(cal.getTime());
					dcExtratoDataFinal.setDate(hj);
				}				
			}
		});
		buttonGroup_2.add(rdbt7Dias);
		
		rdbt15Dias = new JRadioButton("15 Dias");
		rdbt15Dias.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if (rdbt15Dias.isSelected()) {
					Date hj = dataAtual();
					java.util.Calendar cal = Calendar.getInstance();
					cal.add(Calendar.DATE, -15);
					
					dcExtratoDataInicial.setDate(cal.getTime());
					dcExtratoDataFinal.setDate(hj);
				}
			}
		});
		buttonGroup_2.add(rdbt15Dias);
		
		rdbt30Dias = new JRadioButton("30 Dias");
		rdbt30Dias.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				Date hj = dataAtual();
				java.util.Calendar cal = Calendar.getInstance();
				cal.add(Calendar.DATE, -30);
				
				dcExtratoDataInicial.setDate(cal.getTime());
				dcExtratoDataFinal.setDate(hj);
			}
		});
		buttonGroup_2.add(rdbt30Dias);
		
		JPanel panel_1 = new JPanel();
		panel_1.setBorder(new TitledBorder(new LineBorder(new Color(0, 0, 0)), "Cheques debitados na origem", TitledBorder.LEADING, TitledBorder.TOP, null, Color.BLACK));
		
		JPanel panel_2 = new JPanel();
		panel_2.setBorder(new TitledBorder(new LineBorder(new Color(0, 0, 0)), "Cheques creditados na origem", TitledBorder.LEADING, TitledBorder.TOP, null, Color.BLACK));
		
		JPanel panel_3 = new JPanel();
		panel_3.setBorder(new TitledBorder(new LineBorder(new Color(0, 0, 0)), "Cheques criados na origem", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		
		JLabel label_2 = new JLabel("Qtd.:");
		
		tfQtdChequesCriados = new JTextField();
		tfQtdChequesCriados.setColumns(10);
		
		JLabel label_3 = new JLabel("Valor Total.:");
		
		tfValorTotalChequesCriados = new DecimalFormattedField(DecimalFormattedField.REAL);
		tfValorTotalChequesCriados.setColumns(10);
		
		btnGerarRelatorioCriados = new JButton("Gerar Relatório");
		btnGerarRelatorioCriados.setEnabled(false);
		btnGerarRelatorioCriados.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
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
						ChamaRelatorioChequesSelecionados chamaRelatorioChequesSelecionados = new ChamaRelatorioChequesSelecionados();
						
						try {

							chamaRelatorioChequesSelecionados.report("ChequesSelecionados.jasper", listChequeCriados,
									tfValorTotalChequesCriados.getText());

						} catch (Exception e2) {
							JOptionPane.showMessageDialog(null, "ERRO! Não foi possível gerar o relatório solicitado: " + e2);
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
		});
		btnGerarRelatorioCriados.setIcon(new ImageIcon(JanelaMenuPrincipal.class.getResource("/br/com/grupocaravela/icones/relatorios_24.png")));
		
		btnSelecionarOrigem = new JButton("Selecionar");
		btnSelecionarOrigem.setIcon(new ImageIcon(JanelaMenuPrincipal.class.getResource("/br/com/grupocaravela/icones/maismais_24.png")));
		btnSelecionarOrigem.setEnabled(false);
		btnSelecionarOrigem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
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
						try {
							limparTabela();
							
							for (int i = 0; i < listChequeCriados.size(); i++) {
								Cheque b = listChequeCriados.get(i);
								tableModelJanelaPrincipal.addCheque(b);
							}
							
							tfTotal.setText(calcularTotalChequesFiltrados().toString());
							
							tabbedPane.setSelectedIndex(0);
							
						} catch (Exception e2) {
							JOptionPane.showMessageDialog(null, "Erro ao carregar a tabela!!!");
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
		});
		GroupLayout gl_panel_3 = new GroupLayout(panel_3);
		gl_panel_3.setHorizontalGroup(
			gl_panel_3.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_3.createSequentialGroup()
					.addContainerGap()
					.addComponent(label_2, GroupLayout.PREFERRED_SIZE, 35, GroupLayout.PREFERRED_SIZE)
					.addGap(12)
					.addComponent(tfQtdChequesCriados, GroupLayout.PREFERRED_SIZE, 84, GroupLayout.PREFERRED_SIZE)
					.addGap(18)
					.addComponent(label_3, GroupLayout.PREFERRED_SIZE, 87, GroupLayout.PREFERRED_SIZE)
					.addGap(12)
					.addComponent(tfValorTotalChequesCriados, GroupLayout.PREFERRED_SIZE, 171, GroupLayout.PREFERRED_SIZE)
					.addGap(18)
					.addComponent(btnGerarRelatorioCriados, GroupLayout.PREFERRED_SIZE, 171, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(btnSelecionarOrigem)
					.addContainerGap(72, Short.MAX_VALUE))
		);
		gl_panel_3.setVerticalGroup(
			gl_panel_3.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_3.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_panel_3.createParallelGroup(Alignment.LEADING, false)
						.addGroup(gl_panel_3.createSequentialGroup()
							.addGap(10)
							.addComponent(label_2))
						.addGroup(gl_panel_3.createSequentialGroup()
							.addGap(8)
							.addComponent(tfQtdChequesCriados, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
						.addGroup(gl_panel_3.createSequentialGroup()
							.addGap(10)
							.addComponent(label_3))
						.addGroup(gl_panel_3.createSequentialGroup()
							.addGap(8)
							.addComponent(tfValorTotalChequesCriados, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
						.addComponent(btnGerarRelatorioCriados, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 34, Short.MAX_VALUE)
						.addComponent(btnSelecionarOrigem, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
					.addContainerGap(19, Short.MAX_VALUE))
		);
		panel_3.setLayout(gl_panel_3);
		
		JButton btnGerarExtrato = new JButton("Gerar Extrato");
		btnGerarExtrato.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
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
						gerarExtratoData();
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
		});
		btnGerarExtrato.setIcon(new ImageIcon(JanelaMenuPrincipal.class.getResource("/br/com/grupocaravela/icones/contrato_24.png")));
		
		JPanel panel_4 = new JPanel();
		panel_4.setBorder(new TitledBorder(new LineBorder(new Color(0, 0, 0)), "Cheques vencidos na origem", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		
		JLabel label_4 = new JLabel("Qtd.:");
		
		tfQtdExtratoVencidosOrigem = new JTextField();
		tfQtdExtratoVencidosOrigem.setColumns(10);
		
		JLabel label_5 = new JLabel("Valor Total.:");
		
		tfValorTotalExtratoVencidosOrigem = new DecimalFormattedField("R$ #,##0.00;R$ -#,##0.00");
		tfValorTotalExtratoVencidosOrigem.setColumns(10);
		
		btnGerarRelatorioVencidos = new JButton("Gerar Relatório");
		btnGerarRelatorioVencidos.setIcon(new ImageIcon(JanelaMenuPrincipal.class.getResource("/br/com/grupocaravela/icones/relatorios_24.png")));
		btnGerarRelatorioVencidos.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
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
						ChamaRelatorioChequesSelecionados chamaRelatorioChequesSelecionados = new ChamaRelatorioChequesSelecionados();
						
						try {

							chamaRelatorioChequesSelecionados.report("ChequesSelecionados.jasper", listChequeVencidos,
									tfValorTotalExtratoVencidosOrigem.getText());

						} catch (Exception e2) {
							JOptionPane.showMessageDialog(null, "ERRO! Não foi possível gerar o relatório solicitado: " + e2);
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
		});
		btnGerarRelatorioVencidos.setEnabled(false);
		
		btnSelecionarVencidos = new JButton("Selecionar");
		btnSelecionarVencidos.setIcon(new ImageIcon(JanelaMenuPrincipal.class.getResource("/br/com/grupocaravela/icones/maismais_24.png")));
		btnSelecionarVencidos.setEnabled(false);
		btnSelecionarVencidos.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
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
						try {
							limparTabela();
							
							for (int i = 0; i < listChequeVencidos.size(); i++) {
								Cheque b = listChequeVencidos.get(i);
								tableModelJanelaPrincipal.addCheque(b);
							}
							
							tfTotal.setText(calcularTotalChequesFiltrados().toString());
							
							tabbedPane.setSelectedIndex(0);
							
						} catch (Exception e2) {
							JOptionPane.showMessageDialog(null, "Erro ao carregar a tabela!!!");
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
		});
		GroupLayout gl_panel_4 = new GroupLayout(panel_4);
		gl_panel_4.setHorizontalGroup(
			gl_panel_4.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_4.createSequentialGroup()
					.addContainerGap()
					.addComponent(label_4, GroupLayout.PREFERRED_SIZE, 35, GroupLayout.PREFERRED_SIZE)
					.addGap(12)
					.addComponent(tfQtdExtratoVencidosOrigem, GroupLayout.PREFERRED_SIZE, 84, GroupLayout.PREFERRED_SIZE)
					.addGap(18)
					.addComponent(label_5, GroupLayout.PREFERRED_SIZE, 87, GroupLayout.PREFERRED_SIZE)
					.addGap(12)
					.addComponent(tfValorTotalExtratoVencidosOrigem, GroupLayout.PREFERRED_SIZE, 171, GroupLayout.PREFERRED_SIZE)
					.addGap(18)
					.addComponent(btnGerarRelatorioVencidos, GroupLayout.PREFERRED_SIZE, 171, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(btnSelecionarVencidos)
					.addContainerGap(72, Short.MAX_VALUE))
		);
		gl_panel_4.setVerticalGroup(
			gl_panel_4.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_4.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_panel_4.createParallelGroup(Alignment.LEADING, false)
						.addGroup(gl_panel_4.createSequentialGroup()
							.addGap(10)
							.addComponent(label_4))
						.addGroup(gl_panel_4.createSequentialGroup()
							.addGap(8)
							.addComponent(tfQtdExtratoVencidosOrigem, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
						.addGroup(gl_panel_4.createSequentialGroup()
							.addGap(10)
							.addComponent(label_5))
						.addGroup(gl_panel_4.createSequentialGroup()
							.addGap(8)
							.addComponent(tfValorTotalExtratoVencidosOrigem, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
						.addComponent(btnGerarRelatorioVencidos, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 34, Short.MAX_VALUE)
						.addComponent(btnSelecionarVencidos, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
					.addContainerGap(19, Short.MAX_VALUE))
		);
		panel_4.setLayout(gl_panel_4);
		GroupLayout gl_panel = new GroupLayout(panel);
		gl_panel.setHorizontalGroup(
			gl_panel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
						.addComponent(panel_4, GroupLayout.DEFAULT_SIZE, 823, Short.MAX_VALUE)
						.addGroup(gl_panel.createSequentialGroup()
							.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
								.addGroup(gl_panel.createSequentialGroup()
									.addComponent(rdbtnInformarData)
									.addGap(18)
									.addComponent(rdbtnHoje)
									.addGap(18)
									.addComponent(rdbt7Dias)
									.addGap(18)
									.addComponent(rdbt15Dias)
									.addGap(18)
									.addComponent(rdbt30Dias))
								.addGroup(gl_panel.createSequentialGroup()
									.addComponent(lblDataInicial_2)
									.addPreferredGap(ComponentPlacement.RELATED)
									.addComponent(dcExtratoDataInicial, GroupLayout.PREFERRED_SIZE, 163, GroupLayout.PREFERRED_SIZE)
									.addPreferredGap(ComponentPlacement.UNRELATED)
									.addComponent(lblDataFinal)
									.addPreferredGap(ComponentPlacement.RELATED)
									.addComponent(dcExtratoDataFinal, GroupLayout.PREFERRED_SIZE, 163, GroupLayout.PREFERRED_SIZE)))
							.addGap(18)
							.addComponent(btnGerarExtrato))
						.addComponent(panel_3, GroupLayout.DEFAULT_SIZE, 823, Short.MAX_VALUE)
						.addComponent(panel_2, GroupLayout.DEFAULT_SIZE, 823, Short.MAX_VALUE)
						.addComponent(panel_1, GroupLayout.DEFAULT_SIZE, 823, Short.MAX_VALUE))
					.addContainerGap())
		);
		gl_panel.setVerticalGroup(
			gl_panel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_panel.createParallelGroup(Alignment.TRAILING, false)
						.addGroup(gl_panel.createSequentialGroup()
							.addGroup(gl_panel.createParallelGroup(Alignment.BASELINE)
								.addComponent(rdbtnInformarData)
								.addComponent(rdbtnHoje)
								.addComponent(rdbt7Dias)
								.addComponent(rdbt15Dias)
								.addComponent(rdbt30Dias))
							.addGap(16)
							.addGroup(gl_panel.createParallelGroup(Alignment.TRAILING)
								.addComponent(dcExtratoDataFinal, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
								.addComponent(lblDataFinal)
								.addComponent(dcExtratoDataInicial, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
								.addComponent(lblDataInicial_2)))
						.addComponent(btnGerarExtrato, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
					.addGap(35)
					.addComponent(panel_1, GroupLayout.PREFERRED_SIZE, 82, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(panel_2, GroupLayout.PREFERRED_SIZE, 87, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(panel_3, GroupLayout.PREFERRED_SIZE, 87, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(panel_4, GroupLayout.PREFERRED_SIZE, 87, GroupLayout.PREFERRED_SIZE)
					.addContainerGap(25, Short.MAX_VALUE))
		);
		
		JLabel label = new JLabel("Qtd.:");
		
		tfQtdChequesCreditados = new JTextField();
		tfQtdChequesCreditados.setColumns(10);
		
		JLabel label_1 = new JLabel("Valor Total.:");
		
		tfValorTotalChequesCreditados = new DecimalFormattedField(DecimalFormattedField.REAL);
		tfValorTotalChequesCreditados.setColumns(10);
		
		btnGerarRelatorioCreditados = new JButton("Gerar Relatório");
		btnGerarRelatorioCreditados.setEnabled(false);
		btnGerarRelatorioCreditados.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
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
						ChamaRelatorioChequesSelecionados chamaRelatorioChequesSelecionados = new ChamaRelatorioChequesSelecionados();
						
						try {

							chamaRelatorioChequesSelecionados.report("ChequesSelecionados.jasper", listChequeCredito,
									tfValorTotalChequesCreditados.getText());

						} catch (Exception e2) {
							JOptionPane.showMessageDialog(null, "ERRO! Não foi possível gerar o relatório solicitado: " + e2);
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
		});
		btnGerarRelatorioCreditados.setIcon(new ImageIcon(JanelaMenuPrincipal.class.getResource("/br/com/grupocaravela/icones/relatorios_24.png")));
		
		btnSelecionarCreditados = new JButton("Selecionar");
		btnSelecionarCreditados.setIcon(new ImageIcon(JanelaMenuPrincipal.class.getResource("/br/com/grupocaravela/icones/maismais_24.png")));
		btnSelecionarCreditados.setEnabled(false);
		btnSelecionarCreditados.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
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
						try {
							limparTabela();
							
							for (int i = 0; i < listChequeCredito.size(); i++) {
								Cheque b = listChequeCredito.get(i);
								tableModelJanelaPrincipal.addCheque(b);
							}
							
							tfTotal.setText(calcularTotalChequesFiltrados().toString());
							
							tabbedPane.setSelectedIndex(0);
							
						} catch (Exception e2) {
							JOptionPane.showMessageDialog(null, "Erro ao carregar a tabela!!!");
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
		});
		GroupLayout gl_panel_2 = new GroupLayout(panel_2);
		gl_panel_2.setHorizontalGroup(
			gl_panel_2.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_2.createSequentialGroup()
					.addContainerGap()
					.addComponent(label, GroupLayout.PREFERRED_SIZE, 35, GroupLayout.PREFERRED_SIZE)
					.addGap(12)
					.addComponent(tfQtdChequesCreditados, GroupLayout.PREFERRED_SIZE, 84, GroupLayout.PREFERRED_SIZE)
					.addGap(18)
					.addComponent(label_1, GroupLayout.PREFERRED_SIZE, 87, GroupLayout.PREFERRED_SIZE)
					.addGap(12)
					.addComponent(tfValorTotalChequesCreditados, GroupLayout.PREFERRED_SIZE, 171, GroupLayout.PREFERRED_SIZE)
					.addGap(18)
					.addComponent(btnGerarRelatorioCreditados, GroupLayout.PREFERRED_SIZE, 171, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(btnSelecionarCreditados)
					.addContainerGap(72, Short.MAX_VALUE))
		);
		gl_panel_2.setVerticalGroup(
			gl_panel_2.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_2.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_panel_2.createParallelGroup(Alignment.LEADING, false)
						.addGroup(gl_panel_2.createSequentialGroup()
							.addGap(10)
							.addComponent(label))
						.addGroup(gl_panel_2.createSequentialGroup()
							.addGap(8)
							.addComponent(tfQtdChequesCreditados, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
						.addGroup(gl_panel_2.createSequentialGroup()
							.addGap(10)
							.addComponent(label_1))
						.addGroup(gl_panel_2.createSequentialGroup()
							.addGap(8)
							.addComponent(tfValorTotalChequesCreditados, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
						.addComponent(btnGerarRelatorioCreditados, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 34, Short.MAX_VALUE)
						.addComponent(btnSelecionarCreditados, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
					.addContainerGap(19, Short.MAX_VALUE))
		);
		panel_2.setLayout(gl_panel_2);
		
		JLabel lblQtd = new JLabel("Qtd.:");
		
		tfQtdChequesDebitadosOrigem = new JTextField();
		tfQtdChequesDebitadosOrigem.setColumns(10);
		
		JLabel lblValorTotal = new JLabel("Valor Total.:");
		
		tfChequesDebitadosValorTotal = new DecimalFormattedField(DecimalFormattedField.REAL);
		tfChequesDebitadosValorTotal.setColumns(10);
		
		btnGerarRelatrioDebitados = new JButton("Gerar Relatório");
		btnGerarRelatrioDebitados.setEnabled(false);
		btnGerarRelatrioDebitados.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
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
						ChamaRelatorioChequesSelecionados chamaRelatorioChequesSelecionados = new ChamaRelatorioChequesSelecionados();
						
						try {

							chamaRelatorioChequesSelecionados.report("ChequesSelecionados.jasper", listChequeDebito,
									tfChequesDebitadosValorTotal.getText());

						} catch (Exception e2) {
							JOptionPane.showMessageDialog(null, "ERRO! Não foi possível gerar o relatório solicitado: " + e2);
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
		});
		btnGerarRelatrioDebitados.setIcon(new ImageIcon(JanelaMenuPrincipal.class.getResource("/br/com/grupocaravela/icones/relatorios_24.png")));
		
		btnSelecionarDebitados = new JButton("Selecionar");
		btnSelecionarDebitados.setIcon(new ImageIcon(JanelaMenuPrincipal.class.getResource("/br/com/grupocaravela/icones/maismais_24.png")));
		btnSelecionarDebitados.setEnabled(false);
		btnSelecionarDebitados.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
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
						try {
							limparTabela();
							
							for (int i = 0; i < listChequeDebito.size(); i++) {
								Cheque b = listChequeDebito.get(i);
								tableModelJanelaPrincipal.addCheque(b);
							}
							
							tfTotal.setText(calcularTotalChequesFiltrados().toString());
							
							tabbedPane.setSelectedIndex(0);
							
						} catch (Exception e2) {
							JOptionPane.showMessageDialog(null, "Erro ao carregar a tabela!!!");
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
		});
		GroupLayout gl_panel_1 = new GroupLayout(panel_1);
		gl_panel_1.setHorizontalGroup(
			gl_panel_1.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_1.createSequentialGroup()
					.addContainerGap()
					.addComponent(lblQtd)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(tfQtdChequesDebitadosOrigem, GroupLayout.PREFERRED_SIZE, 84, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(lblValorTotal)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(tfChequesDebitadosValorTotal, GroupLayout.PREFERRED_SIZE, 171, GroupLayout.PREFERRED_SIZE)
					.addGap(18)
					.addComponent(btnGerarRelatrioDebitados)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(btnSelecionarDebitados)
					.addContainerGap(72, Short.MAX_VALUE))
		);
		gl_panel_1.setVerticalGroup(
			gl_panel_1.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_1.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_panel_1.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_panel_1.createParallelGroup(Alignment.BASELINE)
							.addComponent(lblQtd)
							.addComponent(tfQtdChequesDebitadosOrigem, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
							.addComponent(lblValorTotal)
							.addComponent(tfChequesDebitadosValorTotal, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
							.addComponent(btnGerarRelatrioDebitados))
						.addComponent(btnSelecionarDebitados, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 36, Short.MAX_VALUE))
					.addContainerGap())
		);
		panel_1.setLayout(gl_panel_1);
		panel.setLayout(gl_panel);
		/*
		 * table.getModel().addTableModelListener(new TableModelListener() {
		 * 
		 * @Override public void tableChanged(TableModelEvent e) {
		 * 
		 * try { ArrayList<Cheque> listaCheque = new ArrayList();
		 * 
		 * //for(int j=0;j<table.getModel().getRowCount();j++){ for(int
		 * j=0;j<table.getRowCount();j++){ if ((Boolean)
		 * table.getModel().getValueAt(j,10)) {
		 * 
		 * Cheque c = tableModelJanelaPrincipal.getCheque(j);
		 * listaCheque.add(c);
		 * 
		 * tfTotal.setText(CalcularTotalChequesSelecionados(listaCheque).
		 * toString());
		 * 
		 * //System.out.println(">\t"+table.getSelectedRow()); //break; } } }
		 * catch (Exception e2) { // TODO: handle exception }
		 * 
		 * 
		 * } });
		 */
		// table.setDefaultRenderer(Object.class, new MeuRenderer());

		// TableRowSorter<TableModel> sorter = new
		// TableRowSorter<TableModel>(table.getModel());
		// table.setRowSorter(sorter);

		lblNomeUsuario.setText(usuario.getNome());

		if (usuario.getAdiministrador()) {
			lblSimNao.setText("Sim");
			mntmUsurio.setEnabled(true);
			mntmUploadBackup.setEnabled(true);
			mntmDownloadBackup.setEnabled(true);
		} else {
			lblSimNao.setText("Não");
		}

		// Render das datas
		table.getColumnModel().getColumn(6).setCellRenderer(new DateRenderer());
		table.getColumnModel().getColumn(0).setCellRenderer(new DateRenderer());

		corCelula();

	}

	private void CarregarJanela() {

		// Evento ao fechar a janela
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {

				String dt = formatDataHora.format(dataAtual());

				try {
					BackUp backUp = new BackUp();

					if ("Linux".equals(System.getProperty("os.name"))) {
						// JOptionPane.showMessageDialog(null, dt);
						backUp.salvar("/opt/" + dt);
					} else {
						backUp.salvar("C:\\GrupoCaravela\\backup\\" + dt);
					}

					JOptionPane.showMessageDialog(null, "Obrigado!!!");

				} catch (Exception e2) {
					JOptionPane.showMessageDialog(null, "ERRO! Não foi possivel salvar o backup!!!");
				}

				System.exit(0);
			}
		});

		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 956, 680);

		JMenuBar menuBar = new JMenuBar();
		menuBar.setFont(new Font("Dialog", Font.BOLD, 14));
		setJMenuBar(menuBar);

		JMenu mnArquivo = new JMenu("Arquivo");
		menuBar.add(mnArquivo);

		JMenuItem mntmAgencia = new JMenuItem("Agência");
		mntmAgencia.setIcon(
				new ImageIcon(JanelaMenuPrincipal.class.getResource("/br/com/grupocaravela/icones/agencia_24.png")));
		mntmAgencia.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				JanelaAgencia janelaAgencia = new JanelaAgencia();
				janelaAgencia.setVisible(true);
				janelaAgencia.setLocationRelativeTo(null);

			}
		});
		mnArquivo.add(mntmAgencia);

		JMenuItem mntmBanco = new JMenuItem("Banco");
		mntmBanco.setIcon(
				new ImageIcon(JanelaMenuPrincipal.class.getResource("/br/com/grupocaravela/icones/banco_24.png")));
		mntmBanco.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

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
						JanelaBanco janelaBanco = new JanelaBanco();
						janelaBanco.setVisible(true);
						janelaBanco.setLocationRelativeTo(null);
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
		});
		mnArquivo.add(mntmBanco);

		JMenuItem mntmCheque = new JMenuItem("Cheque");
		mntmCheque.setIcon(
				new ImageIcon(JanelaMenuPrincipal.class.getResource("/br/com/grupocaravela/icones/cheque_24.png")));
		mntmCheque.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				JanelaCheque janelaCheque = new JanelaCheque();
				janelaCheque.setVisible(true);
				janelaCheque.setLocationRelativeTo(null);

			}
		});
		mnArquivo.add(mntmCheque);

		JMenuItem mntmConta = new JMenuItem("Conta");
		mntmConta.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

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
						JanelaConta janelaConta = new JanelaConta();
						janelaConta.setVisible(true);
						janelaConta.setLocationRelativeTo(null);
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
		});
		mntmConta.setIcon(
				new ImageIcon(JanelaMenuPrincipal.class.getResource("/br/com/grupocaravela/icones/conta_24.png")));
		mnArquivo.add(mntmConta);

		JMenuItem mntmDestinatrio = new JMenuItem("Destinatário");
		mntmDestinatrio.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

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
						JanelaDestinatario janelaDestinatario = new JanelaDestinatario();
						janelaDestinatario.setVisible(true);
						janelaDestinatario.setLocationRelativeTo(null);
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
		});
		mntmDestinatrio.setIcon(new ImageIcon(
				JanelaMenuPrincipal.class.getResource("/br/com/grupocaravela/icones/destinatario_24.png")));
		mnArquivo.add(mntmDestinatrio);

		JMenuItem mntmProprietrio = new JMenuItem("Proprietário");
		mntmProprietrio.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

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
						JanelaProprietario janelaProprietario = new JanelaProprietario();
						janelaProprietario.setVisible(true);
						janelaProprietario.setLocationRelativeTo(null);
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
		});
		mntmProprietrio.setIcon(new ImageIcon(
				JanelaMenuPrincipal.class.getResource("/br/com/grupocaravela/icones/proprietario_24.png")));
		mnArquivo.add(mntmProprietrio);

		mntmUsurio = new JMenuItem("Usuários");
		mntmUsurio.setEnabled(false);
		mntmUsurio.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

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
						JanelaUsuario janelaUsuario = new JanelaUsuario();
						janelaUsuario.setVisible(true);
						janelaUsuario.setLocationRelativeTo(null);
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
		});
		mntmUsurio.setIcon(
				new ImageIcon(JanelaMenuPrincipal.class.getResource("/br/com/grupocaravela/icones/usuario_24.png")));
		mnArquivo.add(mntmUsurio);

		JMenuItem mntmSair = new JMenuItem("Sair");
		mntmSair.setIcon(
				new ImageIcon(JanelaMenuPrincipal.class.getResource("/br/com/grupocaravela/icones/sair_24.png")));
		mnArquivo.add(mntmSair);

		JMenu mnFerramentas = new JMenu("Ferramentas");
		menuBar.add(mnFerramentas);

		mntmDownloadBackup = new JMenuItem("Donwload BackUp");
		mntmDownloadBackup.setEnabled(false);
		mntmDownloadBackup.setIcon(
				new ImageIcon(JanelaMenuPrincipal.class.getResource("/br/com/grupocaravela/icones/backup_24.png")));
		mntmDownloadBackup.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				BackUp backUp = new BackUp();

				backUp.salvarBackup();

			}
		});
		mnFerramentas.add(mntmDownloadBackup);

		mntmUploadBackup = new JMenuItem("Upload Backup");
		mntmUploadBackup.setEnabled(false);
		mntmUploadBackup.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				BackUp backUp = new BackUp();

				backUp.uploadBackup();
			}
		});
		mntmUploadBackup.setIcon(new ImageIcon(
				JanelaMenuPrincipal.class.getResource("/br/com/grupocaravela/icones/backup_upload_24.png")));
		mnFerramentas.add(mntmUploadBackup);

		JMenuItem mntmHistrico = new JMenuItem("Histórico");
		mntmHistrico.setIcon(
				new ImageIcon(JanelaMenuPrincipal.class.getResource("/br/com/grupocaravela/icones/historico_24.png")));
		mnFerramentas.add(mntmHistrico);

		JMenuItem mntmObservaes = new JMenuItem("Observações");
		mntmObservaes.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				JanelaObservacao janelaObservacao = new JanelaObservacao();
				janelaObservacao.setVisible(true);
				janelaObservacao.setLocationRelativeTo(null);

			}
		});
		mntmObservaes.setIcon(new ImageIcon(
				JanelaMenuPrincipal.class.getResource("/br/com/grupocaravela/icones/atend_producao_24.png")));
		mnFerramentas.add(mntmObservaes);

		JMenu mnAjuda = new JMenu("Ajuda");
		menuBar.add(mnAjuda);

		JMenuItem mntmSobre = new JMenuItem("Sobre");
		mntmSobre.setIcon(
				new ImageIcon(JanelaMenuPrincipal.class.getResource("/br/com/grupocaravela/icones/sobre_24.png")));
		mnAjuda.add(mntmSobre);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);

		tabbedPane = new JTabbedPane(JTabbedPane.BOTTOM);

		JPanel panel_1 = new JPanel();
		panel_1.setBorder(new TitledBorder(null, "Atalhos", TitledBorder.LEADING, TitledBorder.TOP, null, null));

		JPanel panel_6 = new JPanel();
		panel_6.setBorder(
				new TitledBorder(new LineBorder(new Color(184, 207, 229)), "Informa\u00E7\u00F5es do usu\u00E1rio",
						TitledBorder.CENTER, TitledBorder.TOP, null, new Color(51, 51, 51)));
		
		JPanel panel_8 = new JPanel();
		panel_8.setBorder(new TitledBorder(new LineBorder(new Color(184, 207, 229)), "Sistema info", TitledBorder.CENTER, TitledBorder.TOP, null, new Color(51, 51, 51)));
		
		JLabel label_1 = new JLabel((String) null);
		
		JLabel lblNewLabel = new JLabel("");
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel.setIcon(new ImageIcon(JanelaMenuPrincipal.class.getResource("/br/com/grupocaravela/imagens/footer_logo.png")));
		
		JLabel lblLicenciadoPara = new JLabel("Licenciado para:");
		
		lblEmpresa = new JLabel("Empresa");
		GroupLayout gl_panel_8 = new GroupLayout(panel_8);
		gl_panel_8.setHorizontalGroup(
			gl_panel_8.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_panel_8.createSequentialGroup()
					.addGroup(gl_panel_8.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_panel_8.createSequentialGroup()
							.addGap(78)
							.addComponent(label_1, GroupLayout.PREFERRED_SIZE, 341, GroupLayout.PREFERRED_SIZE))
						.addGroup(Alignment.TRAILING, gl_panel_8.createSequentialGroup()
							.addContainerGap()
							.addGroup(gl_panel_8.createParallelGroup(Alignment.LEADING)
								.addComponent(lblLicenciadoPara)
								.addComponent(lblEmpresa))
							.addPreferredGap(ComponentPlacement.RELATED, 186, Short.MAX_VALUE)
							.addComponent(lblNewLabel)))
					.addContainerGap())
		);
		gl_panel_8.setVerticalGroup(
			gl_panel_8.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_8.createSequentialGroup()
					.addComponent(label_1)
					.addGroup(gl_panel_8.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_panel_8.createSequentialGroup()
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(lblNewLabel, GroupLayout.DEFAULT_SIZE, 45, Short.MAX_VALUE)
							.addGap(48))
						.addGroup(gl_panel_8.createSequentialGroup()
							.addComponent(lblLicenciadoPara)
							.addPreferredGap(ComponentPlacement.UNRELATED)
							.addComponent(lblEmpresa)
							.addGap(57))))
		);
		panel_8.setLayout(gl_panel_8);
		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_contentPane.createSequentialGroup()
							.addComponent(panel_6, GroupLayout.PREFERRED_SIZE, 30, Short.MAX_VALUE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(panel_8, GroupLayout.PREFERRED_SIZE, 448, GroupLayout.PREFERRED_SIZE))
						.addGroup(Alignment.TRAILING, gl_contentPane.createSequentialGroup()
							.addComponent(tabbedPane)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(panel_1, GroupLayout.PREFERRED_SIZE, 74, GroupLayout.PREFERRED_SIZE)))
					.addGap(0))
		);
		gl_contentPane.setVerticalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
						.addComponent(panel_1, GroupLayout.DEFAULT_SIZE, 538, Short.MAX_VALUE)
						.addGroup(gl_contentPane.createSequentialGroup()
							.addContainerGap()
							.addComponent(tabbedPane)))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING, false)
						.addComponent(panel_8, GroupLayout.PREFERRED_SIZE, 73, GroupLayout.PREFERRED_SIZE)
						.addComponent(panel_6, GroupLayout.PREFERRED_SIZE, 73, GroupLayout.PREFERRED_SIZE))
					.addGap(0))
		);

		JLabel lblUsuario = new JLabel("Usuário:");

		lblNomeUsuario = new JLabel("Nome do usuário interio");

		JLabel lblAdministrador = new JLabel("Administrador:");

		lblSimNao = new JLabel("Sim/Não");
		GroupLayout gl_panel_6 = new GroupLayout(panel_6);
		gl_panel_6.setHorizontalGroup(
			gl_panel_6.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_6.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_panel_6.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_panel_6.createSequentialGroup()
							.addComponent(lblUsuario)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(lblNomeUsuario, GroupLayout.PREFERRED_SIZE, 341, GroupLayout.PREFERRED_SIZE))
						.addGroup(gl_panel_6.createSequentialGroup()
							.addComponent(lblAdministrador)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(lblSimNao)))
					.addContainerGap(49, Short.MAX_VALUE))
		);
		gl_panel_6.setVerticalGroup(
			gl_panel_6.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_6.createSequentialGroup()
					.addGroup(gl_panel_6.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblUsuario)
						.addComponent(lblNomeUsuario))
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addGroup(gl_panel_6.createParallelGroup(Alignment.LEADING)
						.addComponent(lblSimNao)
						.addComponent(lblAdministrador))
					.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
		);
		panel_6.setLayout(gl_panel_6);

		JButton btnNovo = new JButton("");
		btnNovo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

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
						JanelaCheque janelaCheque2 = new JanelaCheque();
						janelaCheque2.setVisible(true);
						janelaCheque2.setLocationRelativeTo(null);
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
		});
		btnNovo.setToolTipText("Cheque");
		btnNovo.setIcon(
				new ImageIcon(JanelaMenuPrincipal.class.getResource("/br/com/grupocaravela/icones/cheque_64.png")));

		JButton btnDestinatario = new JButton("");
		btnDestinatario.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

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
						JanelaDestinatario janelaDestinatario = new JanelaDestinatario();
						janelaDestinatario.setVisible(true);
						janelaDestinatario.setLocationRelativeTo(null);
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
		});
		btnDestinatario.setIcon(new ImageIcon(
				JanelaMenuPrincipal.class.getResource("/br/com/grupocaravela/icones/destinatario_64.png")));
		btnDestinatario.setToolTipText("Destinatário");

		JButton btnProprietario = new JButton("");
		btnProprietario.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

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
						JanelaProprietario janelaProprietario = new JanelaProprietario();
						janelaProprietario.setVisible(true);
						janelaProprietario.setLocationRelativeTo(null);
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
		});
		btnProprietario.setIcon(new ImageIcon(
				JanelaMenuPrincipal.class.getResource("/br/com/grupocaravela/icones/proprietario_64.png")));
		btnProprietario.setToolTipText("Proprietário");

		JButton btnBanco = new JButton("");
		btnBanco.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

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
						JanelaBanco janelaBanco = new JanelaBanco();
						janelaBanco.setVisible(true);
						janelaBanco.setLocationRelativeTo(null);
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
		});
		btnBanco.setIcon(
				new ImageIcon(JanelaMenuPrincipal.class.getResource("/br/com/grupocaravela/icones/banco_64.png")));
		btnBanco.setToolTipText("Banco");

		JButton btnAgencia = new JButton("");
		btnAgencia.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

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
						JanelaAgencia janelaAgencia = new JanelaAgencia();
						janelaAgencia.setVisible(true);
						janelaAgencia.setLocationRelativeTo(null);
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
		});
		btnAgencia.setIcon(
				new ImageIcon(JanelaMenuPrincipal.class.getResource("/br/com/grupocaravela/icones/agencia_64.png")));
		btnAgencia.setToolTipText("Agência");

		JButton btnConta = new JButton("");
		btnConta.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

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
						JanelaConta janelaConta = new JanelaConta();
						janelaConta.setVisible(true);
						janelaConta.setLocationRelativeTo(null);
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
		});
		btnConta.setIcon(
				new ImageIcon(JanelaMenuPrincipal.class.getResource("/br/com/grupocaravela/icones/conta_64.png")));
		btnConta.setToolTipText("Conta");
		GroupLayout gl_panel_1 = new GroupLayout(panel_1);
		gl_panel_1.setHorizontalGroup(gl_panel_1.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_panel_1.createSequentialGroup()
						.addGroup(gl_panel_1.createParallelGroup(Alignment.LEADING)
								.addComponent(btnNovo, GroupLayout.PREFERRED_SIZE, 64, GroupLayout.PREFERRED_SIZE)
								.addComponent(btnDestinatario, GroupLayout.PREFERRED_SIZE, 64,
										GroupLayout.PREFERRED_SIZE)
						.addComponent(btnProprietario, GroupLayout.PREFERRED_SIZE, 64, GroupLayout.PREFERRED_SIZE)
						.addComponent(btnBanco, GroupLayout.PREFERRED_SIZE, 64, GroupLayout.PREFERRED_SIZE)
						.addComponent(btnAgencia, GroupLayout.PREFERRED_SIZE, 64, GroupLayout.PREFERRED_SIZE)
						.addComponent(btnConta, GroupLayout.PREFERRED_SIZE, 64, GroupLayout.PREFERRED_SIZE))
				.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
		gl_panel_1
				.setVerticalGroup(
						gl_panel_1.createParallelGroup(Alignment.LEADING)
								.addGroup(
										gl_panel_1.createSequentialGroup()
												.addComponent(btnNovo, GroupLayout.PREFERRED_SIZE, 64,
														GroupLayout.PREFERRED_SIZE)
												.addPreferredGap(ComponentPlacement.RELATED)
												.addComponent(btnDestinatario, GroupLayout.PREFERRED_SIZE, 64,
														GroupLayout.PREFERRED_SIZE)
								.addPreferredGap(ComponentPlacement.RELATED)
								.addComponent(btnProprietario, GroupLayout.PREFERRED_SIZE, 64,
										GroupLayout.PREFERRED_SIZE).addGap(6)
				.addComponent(btnBanco, GroupLayout.PREFERRED_SIZE, 64, GroupLayout.PREFERRED_SIZE).addGap(6)
				.addComponent(btnAgencia, GroupLayout.PREFERRED_SIZE, 64, GroupLayout.PREFERRED_SIZE).addGap(6)
				.addComponent(btnConta, GroupLayout.PREFERRED_SIZE, 64, GroupLayout.PREFERRED_SIZE)
				.addContainerGap(84, Short.MAX_VALUE)));
		panel_1.setLayout(gl_panel_1);

		JPanel panel = new JPanel();
		tabbedPane.addTab("Todos", null, panel, null);

		JPanel panel_2 = new JPanel();

		btnMovimentar = new JButton("Movimentar");
		btnMovimentar.setEnabled(false);
		btnMovimentar.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {

				MovimentarCheque movimentarCheque = new MovimentarCheque();
				movimentarCheque.setModal(true);
				movimentarCheque.setLocationRelativeTo(null);
				movimentarCheque.setVisible(true);
				destinatario = movimentarCheque.getDestinatario();

				ArrayList<Cheque> listaCheque = new ArrayList();
				/*
				 * int idx[] = table.getSelectedRows();
				 * 
				 * for (int h = 0; h < idx.length; h++) { int linhaReal =
				 * table.convertRowIndexToModel(idx[h]);
				 * listaCheque.add(tableModelJanelaPrincipal.getCheque(linhaReal
				 * )); }
				 */

				for (int i = 0; i < table.getRowCount(); i++) {
					boolean isChecked = false;
					try {
						isChecked = (boolean) table.getValueAt(i, 10);
					} catch (Exception e2) {
						isChecked = false;
					}

					if (isChecked) {
						int linhaReal = table.convertRowIndexToModel(i);
						Cheque c = tableModelJanelaPrincipal.getCheque(linhaReal);
						c.setSelecionado(false);
						listaCheque.add(c);
					}
				}

				// ############################ Voltou duas vezes
				// ################################

				for (int i = 0; i < listaCheque.size(); i++) {

					boolean voltou1vez = false;
					boolean voltou2vezes = false;

					try {
						voltou1vez = listaCheque.get(i).getVoltouUmaVez();
					} catch (Exception e2) {
						voltou1vez = false;
					}
					
					try {
						voltou2vezes = listaCheque.get(i).getVoltouDuasVezes();
					} catch (Exception e2) {
						voltou2vezes = false;
					}

					if (voltou1vez) {
						//listaCheque.get(i).setVoltouDuasVezes(true);
					}
					
					if (voltou2vezes) {

					}

				}

				// ############################ Voltou duas vezes
				// ################################

				if (verificaLocalCheque(listaCheque) == false) {

					Object[] options = { "Sim", "Não" };
					int i = JOptionPane.showOptionDialog(null,
							"ATENÇÃO!!! Esta operação irá movimentar o valor de " + tfTotal.getText()
									+ " reais em cheques para o destinatário " + destinatario.getNome()
									+ ". Gostaria de continuar?",
							"Movimentar", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options,
							options[0]);

					if (i == JOptionPane.YES_OPTION) {

						// observacaoMovimentacao =
						// JOptionPane.showInputDialog("Observação do
						// cheque").toUpperCase();

						ObservacaoCheque obc = new ObservacaoCheque();
						obc.setModal(true);
						obc.setLocationRelativeTo(null);
						obc.setVisible(true);

						observacaoMovimentacao = obc.getObservacao();

						/*
						 * ArrayList<Cheque> listaCheque = new ArrayList(); int
						 * idx[] = table.getSelectedRows(); for (int h = 0; h <
						 * idx.length; h++) {
						 * listaCheque.add(tableModelJanelaPrincipal.getCheque(
						 * idx[h])); }
						 */
						for (int j = 0; j < listaCheque.size(); j++) {
							// botaoMovimentarCheque(tableModelJanelaPrincipal.getCheque(table.getSelectedRow()));
							// ##########################################################

							if (destinatario.getLocal()) {

								Query consulta = manager.createQuery("FROM Historico WHERE cheque_id LIKE '"
										+ listaCheque.get(j).getId()
										+ "' AND destinatario_id IN (SELECT id FROM Destinatario WHERE local = '1')");
								List<Historico> listaHistorico = consulta.getResultList();

								// "FROM Cheque WHERE proprietario_id IN (SELECT
								// id FROM Proprietario WHERE nome LIKE '%" nome
								// + "%') AND vencimento BETWEEN '" +
								// dataInicial + "' AND '" + dataFinal + "'"
								// trx.commit();
								// BETWEEN $P{DATA_INICIAL_SQL} AND
								// $P{DATA_FINAL_SQL}

								Historico h = listaHistorico.get(0);

								Destinatario dest = h.getDestinatario();

								if (dest.equals(destinatario) || dest.equals(destinatario.getDestinatario())) {

									try {
										if (listaCheque.get(j).getVoltouUmaVez()) {
											listaCheque.get(j).setVoltouDuasVezes(true);
										} else {
											listaCheque.get(j).setVoltouUmaVez(true);
											listaCheque.get(j).setVoltouDuasVezes(false);
										}
									} catch (Exception e2) {
										listaCheque.get(j).setVoltouUmaVez(true);
										listaCheque.get(j).setVoltouDuasVezes(false);
									}
									

									botaoMovimentarCheque(listaCheque.get(j));
								} else {
									JOptionPane.showMessageDialog(null,
											"Não é possivel devolver o cheque no valor de R$ "
													+ listaCheque.get(j).getValor() + " para a "
													+ "origem selecionada. Este cheque só pode ser devolvido para sua origem geradora!");
								}

							} else {
								/*
								try {
									if (listaCheque.get(j).getVoltouUmaVez()) {
										listaCheque.get(j).setVoltouDuasVezes(true);
										

										Query consulta = manager.createQuery("FROM Historico WHERE cheque_id LIKE '"
												+ listaCheque.get(j).getId()
												+ "' AND destinatario_id IN (SELECT id FROM Destinatario WHERE local = '1')");
										List<Historico> listaHistorico = consulta.getResultList();

										// "FROM Cheque WHERE proprietario_id IN (SELECT
										// id FROM Proprietario WHERE nome LIKE '%" nome
										// + "%') AND vencimento BETWEEN '" +
										// dataInicial + "' AND '" + dataFinal + "'"
										// trx.commit();
										// BETWEEN $P{DATA_INICIAL_SQL} AND
										// $P{DATA_FINAL_SQL}

										Historico h = listaHistorico.get(0);

										Destinatario dest = h.getDestinatario();										
										
										if (dest.equals(destinatario) || dest.equals(destinatario.getDestinatario())) {
											botaoMovimentarCheque(listaCheque.get(j));
										}
										
									}else{
										listaCheque.get(j).setVoltouDuasVezes(false);
										
										botaoMovimentarCheque(listaCheque.get(j));
									}
								} catch (Exception e2) {
									
									listaCheque.get(j).setVoltouUmaVez(false);
									listaCheque.get(j).setVoltouDuasVezes(false);
									
									botaoMovimentarCheque(listaCheque.get(j));
								}
								*/
								botaoMovimentarCheque(listaCheque.get(j));
								
							}

							// ##########################################################

							// botaoMovimentarCheque(listaCheque.get(j));

						}

						// ########################################################################################################

						ChamaRelatorioMovimentacao chamaRelatorioMovimentacao = new ChamaRelatorioMovimentacao();
						/*
						 * ArrayList<Cheque> listaChequeMovimentacao = new
						 * ArrayList();
						 * 
						 * int idxy[] = table.getSelectedRows(); for (int h = 0;
						 * h < idxy.length; h++) {
						 * listaChequeMovimentacao.add(tableModelJanelaPrincipal
						 * .getCheque(idx[h])); }
						 * 
						 * //
						 * listaCheque.add(tableModelJanelaPrincipal.getCheque(
						 * table.getSelectedRow()));
						 */
						try {
/*
							chamaRelatorioMovimentacao.report("ChequesMovimentacao.jasper", listaCheque,
									observacaoMovimentacao, destinatario.getNome(), tfTotal.getText());
*/
							chamaRelatorioMovimentacao.report("ChequesMovimentacao.jasper", listaCheque,
									observacaoMovimentacao, destinatario.getNome(), CalcularTotalChequesSelecionados(listaCheque).toString());
							
						} catch (Exception e2) {
							JOptionPane.showMessageDialog(null,
									"ERRO! Não foi possível gerar o relatório solicitado: " + e2);
						}

						// #######################################################################################################
						try {
							buscar(tfLocalizar.getText(), format.format(dcInicial.getDate()),
									format.format(dcFinal.getDate()));
						} catch (Exception e2) {
							buscar(tfLocalizar.getText(), null, null);
						}
						// #######################################################################################################

						JOptionPane.showMessageDialog(null,
								"Foi movimentado um total de " + tfTotalCheques.getText() + " cheques no valor de "
										+ tfTotal.getText() + " para o destinatario " + destinatario.getNome() + "!!!");

						table.repaint();
						tfTotal.setText(CalcularTotalChequesSelecionados(listaCheque).toString());

					} else {
						JOptionPane.showMessageDialog(null, "Os cheques não foram movimentados!!!");
					}
				}
				
				btnMovimentar.setEnabled(false);
				
				tfTotal.setText("0");
				tfTotalCheques.setText("0");
				
			}
		});
		btnMovimentar.setIcon(
				new ImageIcon(JanelaMenuPrincipal.class.getResource("/br/com/grupocaravela/icones/move_24.png")));

		JButton btnHistorico = new JButton("Histórico");
		btnHistorico.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				tpObservacao.setText("");
				botaoHistorico();

			}
		});
		btnHistorico.setIcon(
				new ImageIcon(JanelaMenuPrincipal.class.getResource("/br/com/grupocaravela/icones/historico_24.png")));

		JLabel lblTotalDeCheques = new JLabel("Total de ");
		lblTotalDeCheques.setFont(new Font("Dialog", Font.BOLD, 14));

		tfTotal = new DecimalFormattedField(DecimalFormattedField.REAL);
		tfTotal.setFont(new Font("Dialog", Font.BOLD, 14));
		tfTotal.setColumns(10);

		JButton btnImprimir_1 = new JButton("imprimir");
		btnImprimir_1.setIcon(
				new ImageIcon(JanelaMenuPrincipal.class.getResource("/br/com/grupocaravela/icones/impressora_24.png")));
		btnImprimir_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				ChamaRelatorioChequesSelecionados chamaRelatorioChequesSelecionados = new ChamaRelatorioChequesSelecionados();

				ArrayList<Cheque> listaCheque = new ArrayList();
				/*
				 * int idx[] = table.getSelectedRows(); for (int h = 0; h <
				 * idx.length; h++) {
				 * listaCheque.add(tableModelJanelaPrincipal.getCheque(idx[h]));
				 * }
				 */
				for (int i = 0; i < table.getRowCount(); i++) {

					boolean isChecked = false;

					try {
						isChecked = (boolean) table.getValueAt(i, 10);
					} catch (Exception e2) {
						isChecked = false;
					}

					if (isChecked) {
						int linhaReal = table.convertRowIndexToModel(i);
						Cheque c = tableModelJanelaPrincipal.getCheque(linhaReal);
						// c.setSelecionado(false);
						listaCheque.add(c);
					}
				}

				// listaCheque.add(tableModelJanelaPrincipal.getCheque(table.getSelectedRow()));

				try {

					chamaRelatorioChequesSelecionados.report("ChequesSelecionados.jasper", listaCheque,
							tfTotal.getText());

				} catch (Exception e2) {
					JOptionPane.showMessageDialog(null, "ERRO! Não foi possível gerar o relatório solicitado: " + e2);
				}

			}
		});

		tfTotalCheques = new JTextField();
		tfTotalCheques.setHorizontalAlignment(SwingConstants.CENTER);
		tfTotalCheques.setColumns(10);

		JLabel lblCheques = new JLabel("cheques:");
		lblCheques.setFont(new Font("Dialog", Font.BOLD, 14));
		GroupLayout gl_panel = new GroupLayout(panel);
		gl_panel.setHorizontalGroup(
				gl_panel.createParallelGroup(Alignment.TRAILING)
						.addGroup(gl_panel.createSequentialGroup().addContainerGap().addComponent(lblTotalDeCheques)
								.addPreferredGap(ComponentPlacement.RELATED)
								.addComponent(tfTotalCheques, GroupLayout.PREFERRED_SIZE, 80,
										GroupLayout.PREFERRED_SIZE)
								.addPreferredGap(ComponentPlacement.RELATED)
								.addComponent(lblCheques, GroupLayout.PREFERRED_SIZE, 83, GroupLayout.PREFERRED_SIZE)
								.addPreferredGap(ComponentPlacement.RELATED)
								.addComponent(tfTotal, GroupLayout.PREFERRED_SIZE, 128, GroupLayout.PREFERRED_SIZE)
								.addPreferredGap(ComponentPlacement.RELATED, 26, Short.MAX_VALUE)
								.addComponent(btnImprimir_1).addPreferredGap(ComponentPlacement.RELATED)
								.addComponent(btnHistorico).addPreferredGap(ComponentPlacement.RELATED)
								.addComponent(btnMovimentar).addContainerGap())
				.addComponent(panel_2, GroupLayout.DEFAULT_SIZE, 847, Short.MAX_VALUE));
		gl_panel.setVerticalGroup(gl_panel.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_panel.createSequentialGroup()
						.addComponent(panel_2, GroupLayout.DEFAULT_SIZE, 439, Short.MAX_VALUE)
						.addPreferredGap(ComponentPlacement.RELATED)
						.addGroup(gl_panel.createParallelGroup(Alignment.BASELINE).addComponent(btnMovimentar)
								.addComponent(btnHistorico).addComponent(lblTotalDeCheques)
								.addComponent(tfTotalCheques, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
										GroupLayout.PREFERRED_SIZE)
						.addComponent(btnImprimir_1, GroupLayout.PREFERRED_SIZE, 33, GroupLayout.PREFERRED_SIZE)
						.addComponent(lblCheques, GroupLayout.PREFERRED_SIZE, 17, GroupLayout.PREFERRED_SIZE)
						.addComponent(tfTotal, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
								GroupLayout.PREFERRED_SIZE))
						.addContainerGap()));

		JScrollPane scrollPane = new JScrollPane();

		table = new JTable();

		table.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {

				// ################################
				/*
				 * ArrayList<Cheque> listaCheque = new ArrayList();
				 * 
				 * //int linha = tableHistorico.getSelectedRow(); //int
				 * linhaReal = tableHistorico.convertRowIndexToModel(linha);
				 * 
				 * int idx[] = table.getSelectedRows();
				 * 
				 * for (int h = 0; h < idx.length; h++) { int linhaReal =
				 * table.convertRowIndexToModel(idx[h]);
				 * listaCheque.add(tableModelJanelaPrincipal.getCheque(linhaReal
				 * )); }
				 */
				// ################################

				/*
				 * int idx[] = table.getSelectedRows();
				 * 
				 * for (int i = 0; i < idx.length; i++) { int linhaReal =
				 * table.convertRowIndexToModel(idx[i]);
				 * listaCheque.add(tableModelJanelaPrincipal.getCheque(linhaReal
				 * )); }
				 */
				tabbedPane.setEnabledAt(1, false);		
				//tabbedPane.setSelectedIndex(1);
				
				ArrayList<Cheque> listaCheque = new ArrayList();

				for (int i = 0; i < table.getRowCount(); i++) {
					boolean isChecked = false;
					try {
						isChecked = (boolean) table.getValueAt(i, 10);
					} catch (Exception e2) {
						isChecked = false;
					}

					if (isChecked) {
						int linhaReal = table.convertRowIndexToModel(i);
						Cheque c = tableModelJanelaPrincipal.getCheque(linhaReal);
						listaCheque.add(c);
					}
				}
				// } catch (Exception e2) {
				// // TODO: handle exception
				// }

				tfTotal.setText(CalcularTotalChequesSelecionados(listaCheque).toString());

				table.repaint();

				if (listaCheque.size() > 0) {
					btnMovimentar.setEnabled(true);

				} else {
					btnMovimentar.setEnabled(false);

				}
			}
		});

		scrollPane.setViewportView(table);

		JPanel panel_4 = new JPanel();
		panel_4.setBorder(new TitledBorder(null, "Localizar", TitledBorder.LEADING, TitledBorder.TOP, null, null));

		chckbxSelecionarTudo = new JCheckBox("Selecionar tudo");
		chckbxSelecionarTudo.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				selecao();
			}
		});
		GroupLayout gl_panel_2 = new GroupLayout(panel_2);
		gl_panel_2.setHorizontalGroup(gl_panel_2.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_2.createSequentialGroup().addContainerGap()
						.addGroup(gl_panel_2.createParallelGroup(Alignment.LEADING)
								.addComponent(scrollPane, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 823,
										Short.MAX_VALUE)
								.addComponent(chckbxSelecionarTudo, Alignment.TRAILING).addComponent(panel_4,
										Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 823, Short.MAX_VALUE))
				.addContainerGap()));
		gl_panel_2
				.setVerticalGroup(
						gl_panel_2.createParallelGroup(Alignment.LEADING)
								.addGroup(gl_panel_2.createSequentialGroup().addContainerGap()
										.addComponent(panel_4, GroupLayout.PREFERRED_SIZE, 136,
												GroupLayout.PREFERRED_SIZE)
										.addPreferredGap(ComponentPlacement.RELATED).addComponent(chckbxSelecionarTudo)
										.addPreferredGap(ComponentPlacement.RELATED)
										.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 252, Short.MAX_VALUE)
										.addContainerGap()));

		tfLocalizar = new JTextField();
		tfLocalizar.setHorizontalAlignment(SwingConstants.CENTER);
		tfLocalizar.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {

				if (e.getKeyCode() == KeyEvent.VK_ENTER) {

					botaoBuscar();

				}
			}
		});
		tfLocalizar.setColumns(10);

		btnBuscar = new JButton("Buscar");
		btnBuscar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				botaoBuscar();

				chckbxSelecionarTudo.setSelected(false);

			}
		});
		btnBuscar.setIcon(
				new ImageIcon(JanelaMenuPrincipal.class.getResource("/br/com/grupocaravela/icones/lupa_24.png")));

		chbSomenteChequeEm = new JCheckBox("Somente cheque em mãos");
		chbSomenteChequeEm.setSelected(true);

		dcFinal = new JDateChooser();

		JLabel lblDataInicial = new JLabel("Data final:");

		dcInicial = new JDateChooser();

		JLabel lblDataInicial_1 = new JLabel("Data inicial:");

		chbFiltrarPorDataVencimento = new JCheckBox("Data Venc.");
		chbFiltrarPorDataVencimento.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				chbDataEntr.setSelected(false);
			}
		});

		rdbtnCheque = new JRadioButton("Cheque");
		rdbtnCheque.setSelected(true);
		buttonGroup.add(rdbtnCheque);

		rdbtnProprietario = new JRadioButton("Proprietário");
		buttonGroup.add(rdbtnProprietario);

		rdbtnDestinatrio = new JRadioButton("Destinatário");
		buttonGroup.add(rdbtnDestinatrio);

		chbDataEntr = new JCheckBox("Data Entr.");
		chbDataEntr.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				chbFiltrarPorDataVencimento.setSelected(false);
			}
		});
		GroupLayout gl_panel_4 = new GroupLayout(panel_4);
		gl_panel_4
				.setHorizontalGroup(
						gl_panel_4
								.createParallelGroup(
										Alignment.LEADING)
								.addGroup(gl_panel_4.createSequentialGroup().addContainerGap()
										.addGroup(gl_panel_4.createParallelGroup(Alignment.TRAILING)
												.addGroup(gl_panel_4.createSequentialGroup().addComponent(rdbtnCheque)
														.addGap(18).addComponent(rdbtnProprietario).addGap(18)
														.addComponent(rdbtnDestinatrio)
														.addPreferredGap(ComponentPlacement.RELATED, 230,
																Short.MAX_VALUE)
														.addComponent(lblDataInicial)
														.addPreferredGap(ComponentPlacement.RELATED)
														.addComponent(dcFinal, GroupLayout.PREFERRED_SIZE, 133,
																GroupLayout.PREFERRED_SIZE))
												.addGroup(gl_panel_4.createSequentialGroup()
														.addComponent(chbSomenteChequeEm)
														.addPreferredGap(ComponentPlacement.RELATED, 96,
																Short.MAX_VALUE)
														.addComponent(chbDataEntr)
														.addPreferredGap(ComponentPlacement.UNRELATED)
														.addComponent(chbFiltrarPorDataVencimento).addGap(18)
														.addComponent(lblDataInicial_1)
														.addPreferredGap(ComponentPlacement.RELATED)
														.addComponent(dcInicial, GroupLayout.PREFERRED_SIZE, 133,
																GroupLayout.PREFERRED_SIZE))
						.addGroup(gl_panel_4.createSequentialGroup()
								.addComponent(tfLocalizar, GroupLayout.DEFAULT_SIZE, 667, Short.MAX_VALUE)
								.addPreferredGap(ComponentPlacement.UNRELATED).addComponent(btnBuscar)))
				.addContainerGap()));
		gl_panel_4.setVerticalGroup(gl_panel_4.createParallelGroup(Alignment.TRAILING).addGroup(gl_panel_4
				.createSequentialGroup().addGap(12)
				.addGroup(gl_panel_4.createParallelGroup(Alignment.TRAILING)
						.addGroup(gl_panel_4.createSequentialGroup().addComponent(chbSomenteChequeEm).addGap(27))
						.addGroup(gl_panel_4.createSequentialGroup()
								.addGroup(gl_panel_4.createParallelGroup(Alignment.LEADING)
										.addGroup(gl_panel_4.createParallelGroup(Alignment.BASELINE)
												.addComponent(lblDataInicial_1)
												.addComponent(chbFiltrarPorDataVencimento).addComponent(chbDataEntr))
								.addComponent(dcInicial, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
										GroupLayout.PREFERRED_SIZE))
								.addPreferredGap(ComponentPlacement.UNRELATED)
								.addGroup(gl_panel_4.createParallelGroup(Alignment.LEADING)
										.addGroup(gl_panel_4.createParallelGroup(Alignment.BASELINE)
												.addComponent(lblDataInicial).addComponent(rdbtnCheque)
												.addComponent(rdbtnProprietario).addComponent(rdbtnDestinatrio))
										.addComponent(dcFinal, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
												GroupLayout.PREFERRED_SIZE))))
				.addPreferredGap(ComponentPlacement.UNRELATED)
				.addGroup(gl_panel_4.createParallelGroup(Alignment.BASELINE)
						.addComponent(tfLocalizar, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE)
						.addComponent(btnBuscar))
				.addGap(35)));
		panel_4.setLayout(gl_panel_4);
		panel_2.setLayout(gl_panel_2);
		panel.setLayout(gl_panel);

		JPanel panel_3 = new JPanel();
		tabbedPane.addTab("Histórico", null, panel_3, null);

		JPanel panel_5 = new JPanel();

		JLabel lblNCheque = new JLabel("Nº Cheque:");

		JLabel lblDataEntrada = new JLabel("Data Entrada:");

		JLabel lblBomPara = new JLabel("Bom para:");

		JLabel lblProprietrio = new JLabel("Proprietário:");

		JLabel lblValor = new JLabel("Valor:");

		tfHistoricoNumeroCheque = new JTextField();
		tfHistoricoNumeroCheque.setEditable(false);
		tfHistoricoNumeroCheque.setColumns(10);

		tfHistoricoDataEntrada = new JTextField();
		tfHistoricoDataEntrada.setEditable(false);
		tfHistoricoDataEntrada.setColumns(10);

		tfHistoricoNomeProprietario = new JTextField();
		tfHistoricoNomeProprietario.setEditable(false);
		tfHistoricoNomeProprietario.setColumns(10);

		tfHistoricoBomPara = new JTextField();
		tfHistoricoBomPara.setEditable(false);
		tfHistoricoBomPara.setColumns(10);

		tfHistoricoValor = new DecimalFormattedField(DecimalFormattedField.REAL);
		tfHistoricoValor.setEditable(false);
		tfHistoricoValor.setColumns(10);
		GroupLayout gl_panel_3 = new GroupLayout(panel_3);
		gl_panel_3
				.setHorizontalGroup(
						gl_panel_3.createParallelGroup(Alignment.LEADING)
								.addGroup(gl_panel_3.createSequentialGroup().addContainerGap()
										.addGroup(gl_panel_3.createParallelGroup(Alignment.LEADING)
												.addGroup(gl_panel_3.createSequentialGroup().addComponent(panel_5,
														GroupLayout.DEFAULT_SIZE, 823, Short.MAX_VALUE)
												.addContainerGap())
						.addGroup(Alignment.TRAILING, gl_panel_3.createSequentialGroup()
								.addGroup(gl_panel_3.createParallelGroup(Alignment.TRAILING)
										.addGroup(gl_panel_3.createSequentialGroup().addComponent(
												lblNCheque)
										.addPreferredGap(ComponentPlacement.RELATED)
										.addComponent(tfHistoricoNumeroCheque, GroupLayout.PREFERRED_SIZE,
												GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE).addGap(7)
										.addComponent(lblProprietrio).addPreferredGap(ComponentPlacement.RELATED)
										.addComponent(tfHistoricoNomeProprietario, GroupLayout.DEFAULT_SIZE, 466,
												Short.MAX_VALUE))
								.addGroup(gl_panel_3.createSequentialGroup().addComponent(lblDataEntrada)
										.addPreferredGap(ComponentPlacement.RELATED)
										.addComponent(tfHistoricoDataEntrada, GroupLayout.PREFERRED_SIZE, 164,
												GroupLayout.PREFERRED_SIZE)
										.addPreferredGap(ComponentPlacement.UNRELATED).addComponent(lblBomPara)
										.addPreferredGap(ComponentPlacement.RELATED)
										.addComponent(tfHistoricoBomPara, GroupLayout.PREFERRED_SIZE, 174,
												GroupLayout.PREFERRED_SIZE)
										.addPreferredGap(ComponentPlacement.RELATED, 63, Short.MAX_VALUE)
										.addComponent(lblValor).addPreferredGap(ComponentPlacement.RELATED)
										.addComponent(tfHistoricoValor, GroupLayout.PREFERRED_SIZE,
												GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)))
								.addGap(53)))));
		gl_panel_3.setVerticalGroup(gl_panel_3.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_3.createSequentialGroup().addContainerGap()
						.addGroup(gl_panel_3.createParallelGroup(Alignment.BASELINE).addComponent(lblNCheque)
								.addComponent(lblProprietrio)
								.addComponent(tfHistoricoNumeroCheque, GroupLayout.PREFERRED_SIZE,
										GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(tfHistoricoNomeProprietario, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
								GroupLayout.PREFERRED_SIZE))
				.addGap(18)
				.addGroup(gl_panel_3.createParallelGroup(Alignment.BASELINE).addComponent(lblDataEntrada)
						.addComponent(lblBomPara).addComponent(lblValor)
						.addComponent(tfHistoricoDataEntrada, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
								GroupLayout.PREFERRED_SIZE)
						.addComponent(tfHistoricoBomPara, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
								GroupLayout.PREFERRED_SIZE)
						.addComponent(tfHistoricoValor, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
								GroupLayout.PREFERRED_SIZE))
						.addGap(18).addComponent(panel_5, GroupLayout.DEFAULT_SIZE, 401, Short.MAX_VALUE)
						.addContainerGap()));

		JScrollPane scrollPane_1 = new JScrollPane();

		tableHistorico = new JTable();
		tableHistorico.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {

				tpObservacao.setText("");

				int linha = tableHistorico.getSelectedRow();
				int linhaReal = tableHistorico.convertRowIndexToModel(linha);

				Historico h = tableModelHistorico.getHistorico(linhaReal);
				tpObservacao.setText(h.getObservacao());

			}
		});
		scrollPane_1.setViewportView(tableHistorico);

		JPanel panel_7 = new JPanel();
		panel_7.setBorder(
				new TitledBorder(null, "Observa\u00E7\u00E3o", TitledBorder.LEADING, TitledBorder.TOP, null, null));

		JButton btnImprimir = new JButton("Impressão do histórico");
		btnImprimir.setIcon(
				new ImageIcon(JanelaMenuPrincipal.class.getResource("/br/com/grupocaravela/icones/relatorios_24.png")));
		btnImprimir.addActionListener(new ActionListener() {

			private ChamaRelatorio chamaRelatorio;

			public void actionPerformed(ActionEvent e) {

				chamaRelatorio = new ChamaRelatorio();

				try {

					// int linha = table.getSelectedRow();
					// int linhaReal = table.convertRowIndexToModel(linha);

					chamaRelatorio.report("HistoricoCheque.jasper", cheque, null, null);

				} catch (Exception e2) {
					JOptionPane.showMessageDialog(null, "ERRO! Não foi possível gerar o relatório solicitado: " + e2);
				}

			}
		});
		
		JButton btnDemaisCheques = new JButton("Demais cheques");
		btnDemaisCheques.setIcon(new ImageIcon(JanelaMenuPrincipal.class.getResource("/br/com/grupocaravela/icones/atend_producao_24.png")));
		btnDemaisCheques.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
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
						ChamaRelatorioChequesSelecionados chamaRelatorioChequesSelecionados = new ChamaRelatorioChequesSelecionados();
						
						try {

							//demaisCheques(tableModelHistorico.getHistorico(tableHistorico.getSelectedRow()));
							
							chamaRelatorioChequesSelecionados.reportHistoricoMovimentacao("ChequesHostoricoMovimentado.jasper", demaisCheques(tableModelHistorico.getHistorico(tableHistorico.getSelectedRow())),
									"R$ " + th.toString(), formatData.format(tableModelHistorico.getHistorico(tableHistorico.getSelectedRow()).getData()), tableModelHistorico.getHistorico(tableHistorico.getSelectedRow()).getDestinatario().getNome());

						} catch (Exception e2) {
							JOptionPane.showMessageDialog(null, "ERRO! Não foi possível gerar o relatório solicitado: " + e2);
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
		});

		GroupLayout gl_panel_5 = new GroupLayout(panel_5);
		gl_panel_5.setHorizontalGroup(
			gl_panel_5.createParallelGroup(Alignment.LEADING)
				.addComponent(scrollPane_1, GroupLayout.DEFAULT_SIZE, 823, Short.MAX_VALUE)
				.addGroup(Alignment.TRAILING, gl_panel_5.createSequentialGroup()
					.addContainerGap()
					.addComponent(panel_7, GroupLayout.DEFAULT_SIZE, 557, Short.MAX_VALUE)
					.addGap(18)
					.addGroup(gl_panel_5.createParallelGroup(Alignment.TRAILING)
						.addComponent(btnDemaisCheques, GroupLayout.PREFERRED_SIZE, 224, GroupLayout.PREFERRED_SIZE)
						.addComponent(btnImprimir))
					.addContainerGap())
		);
		gl_panel_5.setVerticalGroup(
			gl_panel_5.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_panel_5.createSequentialGroup()
					.addGap(5)
					.addComponent(scrollPane_1, GroupLayout.DEFAULT_SIZE, 294, Short.MAX_VALUE)
					.addGap(6)
					.addGroup(gl_panel_5.createParallelGroup(Alignment.TRAILING)
						.addComponent(panel_7, GroupLayout.PREFERRED_SIZE, 72, GroupLayout.PREFERRED_SIZE)
						.addGroup(gl_panel_5.createSequentialGroup()
							.addComponent(btnImprimir)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(btnDemaisCheques, GroupLayout.PREFERRED_SIZE, 34, GroupLayout.PREFERRED_SIZE)))
					.addContainerGap())
		);

		tpObservacao = new JTextPane();
		tpObservacao.setEditable(false);
		GroupLayout gl_panel_7 = new GroupLayout(panel_7);
		gl_panel_7.setHorizontalGroup(gl_panel_7.createParallelGroup(Alignment.LEADING).addComponent(tpObservacao,
				GroupLayout.DEFAULT_SIZE, 777, Short.MAX_VALUE));
		gl_panel_7.setVerticalGroup(gl_panel_7.createParallelGroup(Alignment.LEADING).addComponent(tpObservacao,
				GroupLayout.DEFAULT_SIZE, 50, Short.MAX_VALUE));
		panel_7.setLayout(gl_panel_7);
		panel_5.setLayout(gl_panel_5);
		panel_3.setLayout(gl_panel_3);
		contentPane.setLayout(gl_contentPane);
	}

	private void carregarTableModel() {
		this.tableModelJanelaPrincipal = new TableModelJanelaPrincipal();
		this.table.setModel(tableModelJanelaPrincipal);

		this.tableModelHistorico = new TableModelHistorico();
		this.tableHistorico.setModel(tableModelHistorico);

		// Render de Moeda

		NumberFormat numeroMoeda = NumberFormat.getNumberInstance();
		numeroMoeda.setMinimumFractionDigits(2);

		DefaultTableCellRenderer cellRendererCustomMoeda = new MoedaRender(numeroMoeda);
		table.getColumnModel().getColumn(5).setCellRenderer(cellRendererCustomMoeda);

	}

	private void tamanhoColunas() {
		// tableProdutos.setAutoResizeMode(tableProdutos.AUTO_RESIZE_OFF);
		table.getColumnModel().getColumn(7).setWidth(150);
		table.getColumnModel().getColumn(7).setMinWidth(150);

		table.getColumnModel().getColumn(10).setMinWidth(50);
		table.getColumnModel().getColumn(10).setMaxWidth(50);

		tableHistorico.getColumnModel().getColumn(4).setWidth(190);
		tableHistorico.getColumnModel().getColumn(4).setMinWidth(190);
		tableHistorico.getColumnModel().getColumn(0).setWidth(50);
		tableHistorico.getColumnModel().getColumn(0).setMaxWidth(50);

	}

	private void iniciaConexao() {

		// factory = Persistence.createEntityManagerFactory("DesbravarPU");
		manager = EntityManagerProducer.createEntityManager();
		trx = manager.getTransaction();
		
	}
	
	private void fecharConexao() {

		// factory = Persistence.createEntityManagerFactory("DesbravarPU");
		//manager = EntityManagerProducer.createEntityManager();
		//trx = manager.getTransaction();
		
		manager.close();
		
	}

	private void limparTabela() {
		while (table.getModel().getRowCount() > 0) {
			tableModelJanelaPrincipal.removeCheque(0);
		}
	}

	private void limparTabelaHistorico() {
		while (tableHistorico.getModel().getRowCount() > 0) {
			tableModelHistorico.removeHistorico(0);
		}
	}

	private void buscaSomenteChequeEmMãos(String nome) {
		try {

			// trx.begin();
			Query consulta = manager.createQuery(
					"FROM Cheque WHERE destinatario_id IN (SELECT id FROM Destinatario WHERE local = '1') AND numCheque LIKE '%"
							+ nome
							+ "%' OR destinatario_id IN (SELECT id FROM Destinatario WHERE local = '1') AND codCheque LIKE '%"
							+ nome + "%' ORDER BY dataVencimento DESC");
			List<Cheque> listaCheques = consulta.getResultList();
			// trx.commit();
			// BETWEEN $P{DATA_INICIAL_SQL} AND $P{DATA_FINAL_SQL}

			for (int i = 0; i < listaCheques.size(); i++) {
				Cheque b = listaCheques.get(i);
				tableModelJanelaPrincipal.addCheque(b);
			}
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Erro ao carregar a tabela de cheques: " + e);
		}
	}

	private void buscaSomenteChequeEmMãosComDataVencimento(String nome, String dataInicial, String dataFinal) {
		try {

			// trx.begin();
			Query consulta = manager
					.createQuery("FROM Cheque WHERE destinatario_id IN (SELECT id FROM Destinatario WHERE local = '1') "
							+ "AND numCheque LIKE '%" + nome + "%' AND vencimento BETWEEN '" + dataInicial + "' AND '"
							+ dataFinal
							+ "' OR destinatario_id IN (SELECT id FROM Destinatario WHERE local = '1') AND codCheque LIKE '%"
							+ nome + "%' AND vencimento BETWEEN '" + dataInicial + "' AND '" + dataFinal
							+ "' ORDER BY vencimento DESC");

			List<Cheque> listaCheques = consulta.getResultList();
			// "FROM Cheque WHERE proprietario_id IN (SELECT id FROM
			// Proprietario WHERE nome LIKE '%" + nome + "%')"
			// trx.commit();
			// BETWEEN $P{DATA_INICIAL_SQL} AND $P{DATA_FINAL_SQL}

			for (int i = 0; i < listaCheques.size(); i++) {
				Cheque b = listaCheques.get(i);
				tableModelJanelaPrincipal.addCheque(b);
			}
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Erro ao carregar a tabela de cheques: " + e);
		}
	}

	private void buscaSomenteChequeEmMãosComDataEntrada(String nome, String dataInicial, String dataFinal) {
		try {

			// trx.begin();
			Query consulta = manager
					.createQuery("FROM Cheque WHERE destinatario_id IN (SELECT id FROM Destinatario WHERE local = '1') "
							+ "AND numCheque LIKE '%" + nome + "%' AND data_entrada BETWEEN '" + dataInicial + " 00:00:00' AND '"
							+ dataFinal
							+ " 23:59:59' OR destinatario_id IN (SELECT id FROM Destinatario WHERE local = '1') AND codCheque LIKE '%"
							+ nome + "%' AND data_entrada BETWEEN '" + dataInicial + " 00:00:00' AND '" + dataFinal
							+ " 23:59:59' ORDER BY data_entrada DESC");

			List<Cheque> listaCheques = consulta.getResultList();
			// "FROM Cheque WHERE proprietario_id IN (SELECT id FROM
			// Proprietario WHERE nome LIKE '%" + nome + "%')"
			// trx.commit();
			// BETWEEN $P{DATA_INICIAL_SQL} AND $P{DATA_FINAL_SQL}

			for (int i = 0; i < listaCheques.size(); i++) {
				Cheque b = listaCheques.get(i);
				tableModelJanelaPrincipal.addCheque(b);
			}
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Erro ao carregar a tabela de cheques: " + e);
		}
	}

	private void buscaChequeComDataProprietarioVencimento(String nome, String dataInicial, String dataFinal) {

		// "from ContaReceber where cliente_id in (select id from Cliente where
		// razao_social like '%" tfLocalizar.getText() + "%') and quitada =
		// '0'");

		try {

			Query consulta = manager
					.createQuery("FROM Cheque WHERE proprietario_id IN (SELECT id FROM Proprietario WHERE nome LIKE '%"
							+ nome + "%') AND vencimento BETWEEN '" + dataInicial + "' AND '" + dataFinal
							+ "' ORDER BY dataVencimento DESC");
			List<Cheque> listaCheques = consulta.getResultList();

			for (int i = 0; i < listaCheques.size(); i++) {
				Cheque b = listaCheques.get(i);
				tableModelJanelaPrincipal.addCheque(b);
			}
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Erro ao carregar a tabela de cheques: " + e);
		}
	}

	private void buscaChequeComDataProprietarioDataEntrada(String nome, String dataInicial, String dataFinal) {

		// "from ContaReceber where cliente_id in (select id from Cliente where
		// razao_social like '%" tfLocalizar.getText() + "%') and quitada =
		// '0'");

		try {

			Query consulta = manager
					.createQuery("FROM Cheque WHERE proprietario_id IN (SELECT id FROM Proprietario WHERE nome LIKE '%"
							+ nome + "%') AND data_entrada BETWEEN '" + dataInicial + " 00:00:00' AND '" + dataFinal
							+ " 23:59:59' ORDER BY data_entrada DESC");
			List<Cheque> listaCheques = consulta.getResultList();

			for (int i = 0; i < listaCheques.size(); i++) {
				Cheque b = listaCheques.get(i);
				tableModelJanelaPrincipal.addCheque(b);
			}
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Erro ao carregar a tabela de cheques: " + e);
		}
	}

	private void buscaChequeProprietario(String nome) {

		// "from ContaReceber where cliente_id in (select id from Cliente where
		// razao_social like '%" tfLocalizar.getText() + "%') and quitada =
		// '0'");

		try {

			Query consulta = manager
					.createQuery("FROM Cheque WHERE proprietario_id IN (SELECT id FROM Proprietario WHERE nome LIKE '%"
							+ nome + "%') ORDER BY dataVencimento DESC");
			List<Cheque> listaCheques = consulta.getResultList();

			for (int i = 0; i < listaCheques.size(); i++) {
				Cheque b = listaCheques.get(i);
				tableModelJanelaPrincipal.addCheque(b);
			}
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Erro ao carregar a tabela de cheques: " + e);
		}
	}

	private void buscaChequeComDataProprietarioEmMaosVencimento(String nome, String dataInicial, String dataFinal) {

		// "from ContaReceber where cliente_id in (select id from Cliente where
		// razao_social like '%" tfLocalizar.getText() + "%') and quitada =
		// '0'");
		// SELECT id FROM Destinatario WHERE local = '1'

		try {

			Query consulta = manager.createQuery(
					"FROM Cheque WHERE proprietario_id IN (SELECT id FROM Proprietario WHERE nome LIKE '%" + nome
							+ "%') AND destinatario_id IN (SELECT id FROM Destinatario WHERE local = '1') AND vencimento BETWEEN '"
							+ dataInicial + "' AND '" + dataFinal + "' ORDER BY dataVencimento DESC");
			List<Cheque> listaCheques = consulta.getResultList();

			for (int i = 0; i < listaCheques.size(); i++) {
				Cheque b = listaCheques.get(i);
				tableModelJanelaPrincipal.addCheque(b);
			}
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Erro ao carregar a tabela de cheques: " + e);
		}
	}

	private void buscaChequeComDataProprietarioEmMaosDataEntrada(String nome, String dataInicial, String dataFinal) {

		// "from ContaReceber where cliente_id in (select id from Cliente where
		// razao_social like '%" tfLocalizar.getText() + "%') and quitada =
		// '0'");
		// SELECT id FROM Destinatario WHERE local = '1'

		try {

			Query consulta = manager.createQuery(
					"FROM Cheque WHERE proprietario_id IN (SELECT id FROM Proprietario WHERE nome LIKE '%" + nome
							+ "%') AND destinatario_id IN (SELECT id FROM Destinatario WHERE local = '1') AND data_entrada BETWEEN '"
							+ dataInicial + " 00:00:00' AND '" + dataFinal + " 23:59:59' ORDER BY data_entrada DESC");
			List<Cheque> listaCheques = consulta.getResultList();

			for (int i = 0; i < listaCheques.size(); i++) {
				Cheque b = listaCheques.get(i);
				tableModelJanelaPrincipal.addCheque(b);
			}
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Erro ao carregar a tabela de cheques: " + e);
		}
	}

	private void buscaChequeProprietarioEmMaos(String nome) {

		// "from ContaReceber where cliente_id in (select id from Cliente where
		// razao_social like '%" tfLocalizar.getText() + "%') and quitada =
		// '0'");

		try {

			Query consulta = manager.createQuery(
					"FROM Cheque WHERE proprietario_id IN (SELECT id FROM Proprietario WHERE nome LIKE '%" + nome
							+ "%') AND destinatario_id IN (SELECT id FROM Destinatario WHERE local = '1') ORDER BY dataVencimento DESC");
			List<Cheque> listaCheques = consulta.getResultList();

			for (int i = 0; i < listaCheques.size(); i++) {
				Cheque b = listaCheques.get(i);
				tableModelJanelaPrincipal.addCheque(b);
			}
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Erro ao carregar a tabela de cheques: " + e);
		}
	}

	private void buscaChequeComDataDestinatarioEmMaosVencimento(String nome, String dataInicial, String dataFinal) {

		// "from ContaReceber where cliente_id in (select id from Cliente where
		// razao_social like '%" tfLocalizar.getText() + "%') and quitada =
		// '0'");
		// AND destinatario_id IN (SELECT id FROM Destinatario WHERE local =
		// '1')

		try {

			Query consulta = manager
					.createQuery("FROM Cheque WHERE destinatario_id IN (SELECT id FROM Destinatario WHERE nome LIKE '%"
							+ nome + "%' AND local = '1') AND vencimento BETWEEN '" + dataInicial + "' AND '"
							+ dataFinal + "' ORDER BY dataVencimento DESC");
			List<Cheque> listaCheques = consulta.getResultList();

			for (int i = 0; i < listaCheques.size(); i++) {
				Cheque b = listaCheques.get(i);
				tableModelJanelaPrincipal.addCheque(b);
			}
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Erro ao carregar a tabela de cheques: " + e);
		}
	}

	private void buscaChequeComDataDestinatarioEmMaosDataEntrada(String nome, String dataInicial, String dataFinal) {

		// "from ContaReceber where cliente_id in (select id from Cliente where
		// razao_social like '%" tfLocalizar.getText() + "%') and quitada =
		// '0'");
		// AND destinatario_id IN (SELECT id FROM Destinatario WHERE local =
		// '1')

		try {

			Query consulta = manager
					.createQuery("FROM Cheque WHERE destinatario_id IN (SELECT id FROM Destinatario WHERE nome LIKE '%"
							+ nome + "%' AND local = '1') AND data_entrada BETWEEN '" + dataInicial + " 00:00:00' AND '"
							+ dataFinal + " 23:59:59' ORDER BY data_entrada DESC");
			List<Cheque> listaCheques = consulta.getResultList();

			for (int i = 0; i < listaCheques.size(); i++) {
				Cheque b = listaCheques.get(i);
				tableModelJanelaPrincipal.addCheque(b);
			}
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Erro ao carregar a tabela de cheques: " + e);
		}
	}

	private void buscaChequeComDataDestinatarioVencimento(String nome, String dataInicial, String dataFinal) {

		// "from ContaReceber where cliente_id in (select id from Cliente where
		// razao_social like '%" tfLocalizar.getText() + "%') and quitada =
		// '0'");

		try {

			Query consulta = manager
					.createQuery("FROM Cheque WHERE destinatario_id IN (SELECT id FROM Destinatario WHERE nome LIKE '%"
							+ nome + "%') AND vencimento BETWEEN '" + dataInicial + "' AND '" + dataFinal
							+ "' ORDER BY dataVencimento DESC");
			List<Cheque> listaCheques = consulta.getResultList();

			for (int i = 0; i < listaCheques.size(); i++) {
				Cheque b = listaCheques.get(i);
				tableModelJanelaPrincipal.addCheque(b);
			}
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Erro ao carregar a tabela de cheques: " + e);
		}
	}

	private void buscaChequeComDataDestinatarioDataEntrada(String nome, String dataInicial, String dataFinal) {

		// "from ContaReceber where cliente_id in (select id from Cliente where
		// razao_social like '%" tfLocalizar.getText() + "%') and quitada =
		// '0'");

		try {

			Query consulta = manager
					.createQuery("FROM Cheque WHERE destinatario_id IN (SELECT id FROM Destinatario WHERE nome LIKE '%"
							+ nome + "%') AND data_entrada BETWEEN '" + dataInicial + " 00:00:00' AND '" + dataFinal
							+ " 23:59:59' ORDER BY data_entrada DESC");
			List<Cheque> listaCheques = consulta.getResultList();

			for (int i = 0; i < listaCheques.size(); i++) {
				Cheque b = listaCheques.get(i);
				tableModelJanelaPrincipal.addCheque(b);
			}
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Erro ao carregar a tabela de cheques: " + e);
		}
	}

	private void buscaChequeDestinatarioEmMaos(String nome) {

		// "from ContaReceber where cliente_id in (select id from Cliente where
		// razao_social like '%" tfLocalizar.getText() + "%') and quitada =
		// '0'");

		try {

			Query consulta = manager
					.createQuery("FROM Cheque WHERE destinatario_id IN (SELECT id FROM Destinatario WHERE nome LIKE '%"
							+ nome + "%' AND local = '1') ORDER BY dataVencimento DESC");
			List<Cheque> listaCheques = consulta.getResultList();

			for (int i = 0; i < listaCheques.size(); i++) {
				Cheque b = listaCheques.get(i);
				tableModelJanelaPrincipal.addCheque(b);
			}
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Erro ao carregar a tabela de cheques: " + e);
		}
	}

	private void buscaChequeDestinatario(String nome) {

		// "from ContaReceber where cliente_id in (select id from Cliente where
		// razao_social like '%" tfLocalizar.getText() + "%') and quitada =
		// '0'");

		try {

			Query consulta = manager
					.createQuery("FROM Cheque WHERE destinatario_id IN (SELECT id FROM Destinatario WHERE nome LIKE '%"
							+ nome + "%') ORDER BY dataVencimento DESC");
			List<Cheque> listaCheques = consulta.getResultList();

			for (int i = 0; i < listaCheques.size(); i++) {
				Cheque b = listaCheques.get(i);
				tableModelJanelaPrincipal.addCheque(b);
			}
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Erro ao carregar a tabela de cheques: " + e);
		}
	}

	private void buscaTodosCheques(String nome) {
		try {

			// trx.begin();
			Query consulta = manager.createQuery("FROM Cheque WHERE numCheque LIKE '%" + nome
					+ "%' OR codCheque LIKE '%" + nome + "%' ORDER BY dataVencimento DESC");
			List<Cheque> listaCheques = consulta.getResultList();
			// trx.commit();

			for (int i = 0; i < listaCheques.size(); i++) {
				Cheque b = listaCheques.get(i);
				tableModelJanelaPrincipal.addCheque(b);
			}
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Erro ao carregar a tabela de cheques: " + e);
		}
	}

	private void buscaTodosChequesComDataVencimento(String nome, String dataInicial, String dataFinal) {
		try {
			// trx.begin();
			Query consulta = manager.createQuery("FROM Cheque WHERE vencimento BETWEEN '" + dataInicial + "' AND '"
					+ dataFinal + "' AND numCheque LIKE '%" + nome + "%' OR vencimento BETWEEN '" + dataInicial
					+ "' AND '" + dataFinal + "' AND codCheque LIKE '%" + nome + "%' ORDER BY dataVencimento DESC");
			List<Cheque> listaCheques = consulta.getResultList();
			// trx.commit();

			for (int i = 0; i < listaCheques.size(); i++) {
				Cheque b = listaCheques.get(i);
				tableModelJanelaPrincipal.addCheque(b);
			}
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Erro ao carregar a tabela de cheques: " + e);
		}
	}

	private void buscaTodosChequesComDataEntrada(String nome, String dataInicial, String dataFinal) {
		try {
			// trx.begin();
			Query consulta = manager.createQuery("FROM Cheque WHERE data_entrada BETWEEN '" + dataInicial + " 00:00:00' AND '"
					+ dataFinal + " 23:59:59' AND numCheque LIKE '%" + nome + "%' OR data_entrada BETWEEN '" + dataInicial
					+ " 00:00:00' AND '" + dataFinal + " 23:59:59' AND codCheque LIKE '%" + nome + "%' ORDER BY data_entrada DESC");
			List<Cheque> listaCheques = consulta.getResultList();
			// trx.commit();

			for (int i = 0; i < listaCheques.size(); i++) {
				Cheque b = listaCheques.get(i);
				tableModelJanelaPrincipal.addCheque(b);
			}
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Erro ao carregar a tabela de cheques: " + e);
		}
	}

	private void buscar(String nome, String dataInicial, String dataFinal) {

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

				if (chbSomenteChequeEm.isSelected()) {

					if (chbFiltrarPorDataVencimento.isSelected()) {
						buscaSomenteChequeEmMãosComDataVencimento(nome, dataInicial, dataFinal);
					} else if (chbDataEntr.isSelected()) {
						buscaSomenteChequeEmMãosComDataEntrada(nome, dataInicial, dataFinal);
					} else {
						buscaSomenteChequeEmMãos(nome);
					}

				} else {
					if (chbFiltrarPorDataVencimento.isSelected()) {
						buscaTodosChequesComDataVencimento(nome, dataInicial, dataFinal);
					} else if (chbDataEntr.isSelected()) {
						buscaTodosChequesComDataEntrada(nome, dataInicial, dataFinal);
					} else {
						buscaTodosCheques(nome);
					}
				}

				tfTotal.setText(calcularTotalChequesFiltrados().toString());

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

	private void buscarProprietario(String nome, String dataInicial, String dataFinal) {

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

				if (chbSomenteChequeEm.isSelected()) {

					if (chbFiltrarPorDataVencimento.isSelected()) {
						buscaChequeComDataProprietarioEmMaosVencimento(nome, dataInicial, dataFinal);
					} else if (chbDataEntr.isSelected()) {
						buscaChequeComDataProprietarioEmMaosDataEntrada(nome, dataInicial, dataFinal);
					} else {
						buscaChequeProprietarioEmMaos(nome);

					}

				} else {

					if (chbFiltrarPorDataVencimento.isSelected()) {
						buscaChequeComDataProprietarioVencimento(nome, dataInicial, dataFinal);
					} else if (chbDataEntr.isSelected()) {
						buscaChequeComDataProprietarioDataEntrada(nome, dataInicial, dataFinal);
					} else {
						buscaChequeProprietario(nome);
					}
				}

				tfTotal.setText(calcularTotalChequesFiltrados().toString());

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

	private void buscarDestinatario(String nome, String dataInicial, String dataFinal) {

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

				if (chbSomenteChequeEm.isSelected()) {
					if (chbFiltrarPorDataVencimento.isSelected()) {
						buscaChequeComDataDestinatarioEmMaosVencimento(nome, dataInicial, dataFinal);
					} else if (chbDataEntr.isSelected()) {
						buscaChequeComDataDestinatarioEmMaosDataEntrada(nome, dataInicial, dataFinal);
					} else {
						buscaChequeDestinatarioEmMaos(nome);
					}
				} else {
					if (chbFiltrarPorDataVencimento.isSelected()) {
						buscaChequeComDataDestinatarioVencimento(nome, dataInicial, dataFinal);
					} else if (chbDataEntr.isSelected()) {
						buscaChequeComDataDestinatarioDataEntrada(nome, dataInicial, dataFinal);
					} else {
						buscaChequeDestinatario(nome);
					}
				}

				tfTotal.setText(calcularTotalChequesFiltrados().toString());

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

	private void botaoMovimentarCheque(Cheque c) {

		if (destinatario != null) {

			try {
				if (c.getVoltouUmaVez()) {
					
					if (!destinatario.getLocal()) {
						c.setVoltouDuasVezes(true);
					}					
				}
			} catch (Exception e) {
				c.setVoltouUmaVez(false);
				c.setVoltouDuasVezes(false);
			}		
			
			salvarCheque(c, destinatario);
		} else {

		}
	}

	private void salvarCheque(Cheque c, Destinatario d) {

		Destinatario destinatarioOld;
		destinatarioOld = c.getDestinatario();

		try {

			c.setDestinatario(d);

			trx.begin();
			manager.persist(c);
			trx.commit();

			criarHistorico("De " + destinatarioOld.getNome() + " para Destinatario " + d.getNome() + "",
					UsuarioLogado.getUsuario(), c, d);
			/*
			 * JOptionPane.showMessageDialog(null, "O cheque nº " +
			 * c.getNumCheque() + " no valor de " + c.getValor() +
			 * " foi movimentado com sucesso!");
			 */

		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "ERRO! " + e);
		}
	}

	public void criarHistorico(String opearacao, Usuario u, Cheque c, Destinatario d) {

		historico = new Historico();

		historico.setCheque(c);
		historico.setUsuario(u);
		historico.setDestinatario(d);
		historico.setData(dataAtual());
		historico.setOperacao(opearacao);
		// historico.setObservacao(JOptionPane.showInputDialog("Observação do
		// cheque").toUpperCase());
		historico.setObservacao(observacaoMovimentacao);

		try {
			trx.begin();
			manager.persist(historico);
			trx.commit();

		} catch (Exception e) {

		}
	}

	private java.util.Date dataAtual() {

		java.util.Date hoje = new java.util.Date();
		// java.util.Date hoje = Calendar.getInstance().getTime();
		return hoje;

	}

	private void botaoHistorico() {
		limparLb();

		int linha = table.getSelectedRow();
		int linhaReal = table.convertRowIndexToModel(linha);

		cheque = tableModelJanelaPrincipal.getCheque(linhaReal);

		tfHistoricoBomPara.setText(formatData.format(cheque.getDataVencimento()));
		tfHistoricoDataEntrada.setText(formatData.format(cheque.getDataEntrada()));
		tfHistoricoNomeProprietario.setText(cheque.getProprietario().getNome());
		tfHistoricoNumeroCheque.setText(cheque.getNumCheque());
		tfHistoricoValor.setText(cheque.getValor().toString());

		
		tabbedPane.setEnabledAt(1, true);		
		tabbedPane.setSelectedIndex(1);
		// carregarHistorico(tableModelJanelaPrincipal.getCheque(table.getSelectedRow()));
		carregarHistorico(cheque);
	}

	private void carregarHistorico(Cheque c) {

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
				limparTabelaHistorico();

				try {

					// trx.begin();
					Query consulta = manager.createQuery("FROM Historico WHERE cheque_id LIKE '" + c.getId() + "'");
					List<Historico> listaHistorico = consulta.getResultList();
					// trx.commit();

					for (int i = 0; i < listaHistorico.size(); i++) {
						Historico h = listaHistorico.get(i);
						tableModelHistorico.addHistorico(h);
					}
				} catch (Exception e) {
					JOptionPane.showMessageDialog(null, "Erro ao carregar a tabela de históricos: " + e);
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

	private Double calcularTotalChequesFiltrados() {

		Double total = 0.0;

		for (int i = 0; i < table.getRowCount(); i++) {
			Cheque c = tableModelJanelaPrincipal.getCheque(i);

			total = total + c.getValor();
		}
		
		tfTotalCheques.setText(String.valueOf(table.getRowCount()));

		return total;
	}

	private Double CalcularTotalChequesSelecionados(ArrayList<Cheque> listaSelecao) {

		Double total = 0.0;

		for (int i = 0; i < listaSelecao.size(); i++) {

			Cheque c = listaSelecao.get(i);
			total = total + c.getValor();

		}

		tfTotalCheques.setText(String.valueOf(listaSelecao.size()));

		return total;
	}

	private void botaoBuscar() {
		//iniciaConexao();

		if (rdbtnCheque.isSelected()) {
			try {
				buscar(tfLocalizar.getText(), format.format(dcInicial.getDate()), format.format(dcFinal.getDate()));
			} catch (Exception e2) {
				buscar(tfLocalizar.getText(), null, null);
			}
		}

		if (rdbtnProprietario.isSelected()) {
			try {
				buscarProprietario(tfLocalizar.getText(), format.format(dcInicial.getDate()),
						format.format(dcFinal.getDate()));
			} catch (Exception e2) {
				buscarProprietario(tfLocalizar.getText(), null, null);
			}
		}

		if (rdbtnDestinatrio.isSelected()) {
			try {
				buscarDestinatario(tfLocalizar.getText(), format.format(dcInicial.getDate()),
						format.format(dcFinal.getDate()));
			} catch (Exception e2) {
				buscarDestinatario(tfLocalizar.getText(), null, null);
			}

		}
		//fecharConexao();
	}

	private void limparLb() {

		tfHistoricoBomPara.setText("");
		tfHistoricoDataEntrada.setText("");
		tfHistoricoNomeProprietario.setText("");
		tfHistoricoNumeroCheque.setText("");
		tfHistoricoValor.setText("");

	}

	private boolean verificaLocalCheque(ArrayList<Cheque> listaCheque) {

		boolean retorno = false;

		for (int j = 0; j < listaCheque.size(); j++) {

			if (destinatario.getLocal() == true) {
				
				//if (listaCheque.get(j).getVoltouUmaVez()) {
				//	listaCheque.get(j).setVoltouDuasVezes(true);
				//}

				Query consulta = manager
						.createQuery("FROM Historico WHERE cheque_id LIKE '" + listaCheque.get(j).getId()
								+ "' AND destinatario_id IN (SELECT id FROM Destinatario WHERE local = '1')");
				List<Historico> listaHistorico = consulta.getResultList();

				if (listaHistorico.size() != 0) {
					Historico h = listaHistorico.get(0);

					Destinatario dest = h.getDestinatario();

					if (dest.equals(destinatario)) {

					} else {
						JOptionPane.showMessageDialog(null,
								"Não é possivel devolver o cheque de numero " + listaCheque.get(j).getNumCheque()
										+ " no valor de R$ " + listaCheque.get(j).getValor() + " para a "
										+ "origem selecionada. Este cheque só pode ser devolvido para sua origem geradora que é "
										+ dest.getNome() + "!");

						retorno = true;
					}

				}

			}else{
			
			// ############################ Voltou duas vezes
			// ################################

			Destinatario destFilho = null;
			
			Query consultaFilho = manager
					.createQuery("FROM Historico WHERE cheque_id LIKE '" + listaCheque.get(j).getId()
							+ "' AND destinatario_id IN (SELECT id FROM Destinatario WHERE local = '1')");
			List<Historico> listaHistoricoFilho = consultaFilho.getResultList();
			
			if (listaHistoricoFilho.size() != 0) {
				Historico h = listaHistoricoFilho.get(0);

				destFilho = h.getDestinatario();
			}
			
			boolean voltou2vezes = false;

			try {
				voltou2vezes = listaCheque.get(j).getVoltouDuasVezes();
			} catch (Exception e2) {
				voltou2vezes = false;
			}

			if (voltou2vezes) {
				
				Destinatario destFilho2 = null;
				
				try {
					destFilho2 = destinatario.getDestinatario();
					
				} catch (Exception e) {
					destFilho2 = null;
				}
				
				try {
					if (destFilho2.equals(destFilho)) {
						
					}else{
						
						JOptionPane.showMessageDialog(null,
								"Não é possivel mover o cheque de numero " + listaCheque.get(j).getNumCheque()
										+ " no valor de R$ " + listaCheque.get(j).getValor() + " para o "
										+ "destino selecionado. Este cheque só pode ser movido para um destino filho de sua origem geradora!");
						
						retorno = true;						

					}
				} catch (Exception e) {
					JOptionPane.showMessageDialog(null,
							"Não é possivel mover o cheque de numero " + listaCheque.get(j).getNumCheque()
									+ " no valor de R$ " + listaCheque.get(j).getValor() + " para o "
									+ "destino selecionado. Este cheque só pode ser movido para um destino filho de sua origem geradora!");
					
					retorno = true;
					
				}
								
			}

			// ############################ Voltou duas vezes
			// ################################
		}
		}
		
		return retorno;

	}
	
	private String lerArquivoIp() {

		FileReader fileReader;
		String sistema = System.getProperty("os.name");
		String ip = null;

		try {
			if ("Linux".equals(sistema)) {
				fileReader = new FileReader("/opt/GrupoCaravela/software/conf.txt");
			} else {
				fileReader = new FileReader("c:\\GrupoCaravela\\software\\conf.txt");
			}

			BufferedReader reader = new BufferedReader(fileReader);
			String data = null;
			while ((data = reader.readLine()) != null) {
				ip = String.valueOf(data);
			}
			fileReader.close();
			reader.close();
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "ERRO! Erro ao ler o arquivo de versão do sistema!" + e);
		}

		return ip;
	}

	private void selecao() {

		if (chckbxSelecionarTudo.isSelected()) {
			for (int i = 0; i < table.getRowCount(); i++) {
				table.getModel().setValueAt(true, i, 10);

			}
		} else {
			for (int i = 0; i < table.getRowCount(); i++) {
				table.getModel().setValueAt(false, i, 10);
			}
		}
/*
		ArrayList<Cheque> listaCheque = new ArrayList();

		for (int i = 0; i < table.getRowCount(); i++) {
			boolean isChecked = false;
			try {
				isChecked = (boolean) table.getValueAt(i, 10);
			} catch (Exception e2) {
				isChecked = false;
			}

			if (isChecked) {
				int linhaReal = table.convertRowIndexToModel(i);
				Cheque c = tableModelJanelaPrincipal.getCheque(linhaReal);
				listaCheque.add(c);
			}
		}

		tfTotal.setText(CalcularTotalChequesSelecionados(listaCheque).toString());

		table.repaint();
		*/
		
		ArrayList<Cheque> listaCheque = new ArrayList();

		for (int i = 0; i < table.getRowCount(); i++) {
			boolean isChecked = false;
			try {
				isChecked = (boolean) table.getValueAt(i, 10);
			} catch (Exception e2) {
				isChecked = false;
			}

			if (isChecked) {
				int linhaReal = table.convertRowIndexToModel(i);
				Cheque c = tableModelJanelaPrincipal.getCheque(linhaReal);
				listaCheque.add(c);
			}
		}
		// } catch (Exception e2) {
		// // TODO: handle exception
		// }

		tfTotal.setText(CalcularTotalChequesSelecionados(listaCheque).toString());

		table.repaint();

		if (listaCheque.size() > 0) {
			btnMovimentar.setEnabled(true);

		} else {
			btnMovimentar.setEnabled(false);

		}
	}

	private void corCelula() {

		for (int c = 0; c < 10; c++) {
			// table.setDefaultRenderer(table.getColumnClass(c), renderer);
			table.setDefaultRenderer(table.getColumnClass(c), renderer);

		}
	}
	
	private void gerarExtratoData(){
					
		String dti = formatSql.format(dcExtratoDataInicial.getDate());
		String dtf = formatSql.format(dcExtratoDataFinal.getDate());
		
		listChequeCredito.clear();
		listChequeCriados.clear();
		listChequeDebito.clear();
		listChequeVencidos.clear();
		
		int qtd = 0;
		Double vTotal = 0.0;
		
		try {

			Query consulta = manager
					.createQuery("FROM Historico WHERE data BETWEEN '" + dti + " 00:00:00' AND '" + dtf + " 23:59:59'");

			List<Historico> listaHistorico = consulta.getResultList();
			
			for (int i = 0; i < listaHistorico.size(); i++) {
				Historico h = listaHistorico.get(i);
				
				if (h.getDestinatario().getLocal()) {
					
					Query consultaQtd = manager
							.createQuery("FROM Historico WHERE cheque_id like '" + h.getCheque().getId() + "' AND data BETWEEN '" + dti + " 00:00:00' AND '" + dtf + " 23:59:59'");

					List<Historico> listaHistoricoQtd = consultaQtd.getResultList();
					
					if (listaHistoricoQtd.size() > 1) {
						listChequeCredito.add(h.getCheque());
					}
					if (listaHistoricoQtd.size() == 1) {
						listChequeCriados.add(h.getCheque());
					}					
										
				}else {					
					listChequeDebito.add(h.getCheque());					
				}
					
			}
			
			//Cheques Vencidos
			
			Query consultaVencidos = manager
					.createQuery("FROM Cheque WHERE vencimento BETWEEN '" + dti + "' AND '" + dtf + "' AND destinatario_id IN (SELECT id FROM Destinatario WHERE local = '1')");

			List<Cheque> listaChequesVencidos = consultaVencidos.getResultList();
			
			for (int j = 0; j < listaChequesVencidos.size(); j++) {
				if (listaChequesVencidos.get(j).getDestinatario().getLocal()) {
					listChequeVencidos.add(listaChequesVencidos.get(j));
				}
			}
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Erro ao carregar o historico por data definida: " + e);
		}
		
		tfQtdChequesCreditados.setText(String.valueOf(listChequeCredito.size()));
		tfQtdChequesDebitadosOrigem.setText(String.valueOf(listChequeDebito.size()));
		tfQtdChequesCriados.setText(String.valueOf(listChequeCriados.size()));
		tfQtdExtratoVencidosOrigem.setText(String.valueOf(listChequeVencidos.size()));
		
		if (listChequeCredito.size() > 0) {
			btnGerarRelatorioCreditados.setEnabled(true);
			btnSelecionarCreditados.setEnabled(true);
		}else{
			btnGerarRelatorioCreditados.setEnabled(false);
			btnSelecionarCreditados.setEnabled(false);
		}
		
		if (listChequeDebito.size() > 0) {
			btnGerarRelatrioDebitados.setEnabled(true);
			btnSelecionarDebitados.setEnabled(true);
		}else{
			btnGerarRelatrioDebitados.setEnabled(false);
			btnSelecionarDebitados.setEnabled(false);
		}
		
		if (listChequeCriados.size() > 0) {
			btnGerarRelatorioCriados.setEnabled(true);
			btnSelecionarOrigem.setEnabled(true);
		}else{
			btnGerarRelatorioCriados.setEnabled(false);
			btnSelecionarOrigem.setEnabled(false);
		}
		
		if (listChequeVencidos.size() > 0) {
			btnGerarRelatorioVencidos.setEnabled(true);
			btnSelecionarVencidos.setEnabled(true);
		}else{
			btnGerarRelatorioVencidos.setEnabled(false);
			btnSelecionarVencidos.setEnabled(false);
		}
		
		vTotal = 0.0;
		for (int i = 0; i < listChequeCredito.size(); i++) {
			vTotal = vTotal + listChequeCredito.get(i).getValor();
		}
		
		tfValorTotalChequesCreditados.setText(vTotal.toString());
		
		vTotal = 0.0;
		for (int i = 0; i < listChequeDebito.size(); i++) {
			vTotal = vTotal + listChequeDebito.get(i).getValor();
		}
		
		tfChequesDebitadosValorTotal.setText(vTotal.toString());
		
		vTotal = 0.0;
		
		for (int i = 0; i < listChequeCriados.size(); i++) {
			vTotal = vTotal + listChequeCriados.get(i).getValor();
		}
		
		tfValorTotalChequesCriados.setText(vTotal.toString());
		
		vTotal = 0.0;
		
		for (int i = 0; i < listChequeVencidos.size(); i++) {
			vTotal = vTotal + listChequeVencidos.get(i).getValor();
		}
		
		tfValorTotalExtratoVencidosOrigem.setText(vTotal.toString());
	}
	
	private String lerArquivoNomeEmpresa() {

		FileReader fileReader;
		String sistema = System.getProperty("os.name");
		String nome = null;

		try {
			if ("Linux".equals(sistema)) {
				fileReader = new FileReader("/opt/GrupoCaravela/software/nome.txt");
			} else {
				fileReader = new FileReader("c:\\GrupoCaravela\\software\\nome.txt");
			}

			BufferedReader reader = new BufferedReader(fileReader);
			String data = null;
			while ((data = reader.readLine()) != null) {
				nome = String.valueOf(data);
			}
			fileReader.close();
			reader.close();
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "ERRO! Erro ao ler o arquivo do nome da empresa!");
		}

		return nome;
	}
	
	private ArrayList<Cheque> demaisCheques(Historico h){
		
		th = 0.0;
		ArrayList<Cheque> listChequeHistorico = new ArrayList<>();
		
		//listChequeHistorico.clear();						
		
		Query consulta = manager
				.createQuery("FROM Historico WHERE observacao like '" + h.getObservacao() + "' AND destinatario_id like '" 
		+ h.getDestinatario().getId() + "' AND data BETWEEN '" + formatSql.format(h.getData()) + " 00:00:00' AND '" + formatSql.format(h.getData()) + " 23:59:59'");

		List<Historico> listaHistorico = consulta.getResultList();
		
		for (int i = 0; i < listaHistorico.size(); i++) {
			Cheque ch = listaHistorico.get(i).getCheque();
			
			th = th + ch.getValor();
			
			listChequeHistorico.add(ch);
		}
		
		return listChequeHistorico;
	}
}
