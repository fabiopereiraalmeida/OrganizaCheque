package br.com.grupocaravela.view;

import java.awt.EventQueue;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.JTextField;
import java.awt.Font;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.util.List;
import java.awt.event.ActionEvent;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;

import br.com.grupocaravela.aguarde.BootSplash;
import br.com.grupocaravela.configuracao.Empresa;
import br.com.grupocaravela.configuracao.EntityManagerProducer;
import br.com.grupocaravela.ferramenta.UsuarioLogado;
import br.com.grupocaravela.objeto.Destinatario;
import br.com.grupocaravela.objeto.Usuario;

import java.awt.Color;
import javax.swing.JPasswordField;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.FileReader;

public class JanelaLogin extends JFrame {

	private JPanel contentPane;
	private JTextField tfUsuario;
	private JPasswordField pfSenha;

	private EntityManagerProducer entityManagerProducer = new EntityManagerProducer();
	private EntityManager manager;
	private EntityTransaction trx;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					JanelaLogin frame = new JanelaLogin();
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
	public JanelaLogin() {

		carregarJanela();
		// iniciaConexao();
		
		Empresa.setIpLocalServidor(lerArquivoIp());

	}

	private void carregarJanela() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 400, 466);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);

		JPanel panel = new JPanel();
		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(gl_contentPane.createParallelGroup(Alignment.LEADING).addComponent(panel,
				GroupLayout.DEFAULT_SIZE, 388, Short.MAX_VALUE));
		gl_contentPane.setVerticalGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
						.addComponent(panel, GroupLayout.PREFERRED_SIZE, 430, GroupLayout.PREFERRED_SIZE)
						.addContainerGap(28, Short.MAX_VALUE)));

		JLabel lblNewLabel = new JLabel("");
		lblNewLabel.setIcon(new ImageIcon(JanelaLogin.class.getResource("/br/com/grupocaravela/imagens/cheque01.gif")));

		JPanel panel_1 = new JPanel();
		panel_1.setBorder(new LineBorder(Color.DARK_GRAY));
		GroupLayout gl_panel = new GroupLayout(panel);
		gl_panel.setHorizontalGroup(gl_panel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel.createSequentialGroup().addContainerGap()
						.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
								.addComponent(lblNewLabel, GroupLayout.DEFAULT_SIZE, 364, Short.MAX_VALUE)
								.addComponent(panel_1, GroupLayout.DEFAULT_SIZE, 364, Short.MAX_VALUE))
				.addContainerGap()));
		gl_panel.setVerticalGroup(gl_panel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel.createSequentialGroup().addComponent(lblNewLabel)
						.addPreferredGap(ComponentPlacement.RELATED, 24, Short.MAX_VALUE)
						.addComponent(panel_1, GroupLayout.PREFERRED_SIZE, 161, GroupLayout.PREFERRED_SIZE)
						.addContainerGap()));

		JLabel lblUsuario = new JLabel("Usuario");
		lblUsuario.setFont(new Font("Dialog", Font.BOLD, 14));
		lblUsuario.setHorizontalAlignment(SwingConstants.CENTER);

		tfUsuario = new JTextField();
		tfUsuario.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					pfSenha.requestFocus();
				}
			}
		});
		tfUsuario.addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent e) {
				tfUsuario.selectAll();
			}
		});
		tfUsuario.setHorizontalAlignment(SwingConstants.CENTER);
		tfUsuario.setFont(new Font("Dialog", Font.PLAIN, 14));
		tfUsuario.setColumns(10);

		JLabel lblSenha = new JLabel("Senha");
		lblSenha.setFont(new Font("Dialog", Font.BOLD, 14));
		lblSenha.setHorizontalAlignment(SwingConstants.CENTER);

		JButton btnEntrar = new JButton("Entrar");
		btnEntrar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				// #############################################
				final Thread tr = new Thread(new Runnable() {
					@Override
					public void run() {
						try {

							chamarMenuPrincipal();

						} catch (Exception ex) {

							JOptionPane.showMessageDialog(null,
									"Erro na tentativa de conexão com o servidor!!! Verifique a conexão!: " + ex);
						}

					}
				});
				new Thread(new Runnable() {
					@Override
					public void run() {
						tr.start();
						// .....
						BootSplash bootSplash = new BootSplash();
						bootSplash.setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
						// bootSplash.setUndecorated(true);
						// bootSplash.setBackground(new Color(0, 0, 0, 0));
						bootSplash.setVisible(true);
						bootSplash.setLocationRelativeTo(null);
						try {
							tr.join();
							bootSplash.dispose();

						} catch (InterruptedException ex) {
							// Logger.getLogger(MenuView.class.getName()).log(Level.SEVERE,
							// null, ex);
						}
					}
				}).start();

			}
		});
		btnEntrar.setIcon(
				new ImageIcon(JanelaLogin.class.getResource("/br/com/grupocaravela/icones/alerta_verde_24.png")));

		pfSenha = new JPasswordField();
		pfSenha.setHorizontalAlignment(SwingConstants.CENTER);
		pfSenha.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					chamarMenuPrincipal();
				}
			}
		});
		pfSenha.addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent e) {
				pfSenha.selectAll();
			}
		});
		pfSenha.setFont(new Font("Dialog", Font.PLAIN, 14));

		JButton btnConfigurar = new JButton("Configurar");
		btnConfigurar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				chamaConfigurar();
			}
		});
		btnConfigurar.setFont(new Font("Dialog", Font.BOLD, 10));
		GroupLayout gl_panel_1 = new GroupLayout(panel_1);
		gl_panel_1.setHorizontalGroup(gl_panel_1.createParallelGroup(Alignment.TRAILING).addGroup(gl_panel_1
				.createSequentialGroup().addContainerGap()
				.addGroup(gl_panel_1.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_panel_1.createSequentialGroup()
								.addGroup(gl_panel_1.createParallelGroup(Alignment.TRAILING)
										.addComponent(lblSenha, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 79,
												Short.MAX_VALUE)
										.addComponent(lblUsuario, Alignment.LEADING, GroupLayout.PREFERRED_SIZE, 79,
												GroupLayout.PREFERRED_SIZE))
								.addPreferredGap(ComponentPlacement.RELATED)
								.addGroup(gl_panel_1.createParallelGroup(Alignment.LEADING)
										.addComponent(pfSenha, GroupLayout.DEFAULT_SIZE, 247, Short.MAX_VALUE)
										.addComponent(tfUsuario, GroupLayout.DEFAULT_SIZE, 247, Short.MAX_VALUE)))
						.addGroup(Alignment.TRAILING,
								gl_panel_1.createSequentialGroup()
										.addComponent(btnEntrar, GroupLayout.DEFAULT_SIZE, 223, Short.MAX_VALUE)
										.addPreferredGap(ComponentPlacement.RELATED).addComponent(btnConfigurar)))
				.addContainerGap()));
		gl_panel_1.setVerticalGroup(gl_panel_1.createParallelGroup(Alignment.LEADING).addGroup(gl_panel_1
				.createSequentialGroup().addGap(27)
				.addGroup(gl_panel_1.createParallelGroup(Alignment.BASELINE).addComponent(lblUsuario).addComponent(
						tfUsuario, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
				.addGap(20)
				.addGroup(gl_panel_1.createParallelGroup(Alignment.BASELINE).addComponent(lblSenha).addComponent(
						pfSenha, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
				.addGap(18)
				.addGroup(gl_panel_1.createParallelGroup(Alignment.LEADING)
						.addComponent(btnConfigurar, GroupLayout.DEFAULT_SIZE, 47, Short.MAX_VALUE)
						.addComponent(btnEntrar, GroupLayout.DEFAULT_SIZE, 47, Short.MAX_VALUE))
				.addGap(51)));
		panel_1.setLayout(gl_panel_1);
		panel.setLayout(gl_panel);
		contentPane.setLayout(gl_contentPane);
	}

	private void chamarMenuPrincipal() {

		if (verificarUsuario(tfUsuario.getText(), pfSenha.getText())) {
			JanelaMenuPrincipal menuPrincipal = new JanelaMenuPrincipal();
			menuPrincipal.setVisible(true);
			menuPrincipal.setLocationRelativeTo(null);
			this.dispose();
		} else {
			JOptionPane.showMessageDialog(null, "Favor verificar os dados!");
			tfUsuario.requestFocus();
		}
	}

	private void iniciaConexao() {
		// factory = Persistence.createEntityManagerFactory("DesbravarPU");
		manager = entityManagerProducer.createEntityManager();
		trx = manager.getTransaction();
	}

	private Boolean verificarUsuario(String usuario, String senha) {

		iniciaConexao();

		Boolean retorno = false;

		try {
			Query consulta = manager
					.createQuery("FROM Usuario WHERE usuario LIKE '" + usuario + "' AND senha LIKE '" + senha + "'");
			List<Usuario> listaUsuarios = consulta.getResultList();

			Usuario u = listaUsuarios.get(0);

			UsuarioLogado.setUsuario(u);

			retorno = true;
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Ususario/Senha incorreto!");
			retorno = false;
		}

		return retorno;
	}

	private void chamaConfigurar() {
		JanelaConfiguracao configuracao = new JanelaConfiguracao();
		configuracao.setVisible(true);
		configuracao.setLocationRelativeTo(null);
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
}
