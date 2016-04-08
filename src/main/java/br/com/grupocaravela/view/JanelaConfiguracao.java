package br.com.grupocaravela.view;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.border.TitledBorder;

import br.com.grupocaravela.configuracao.Empresa;
import br.com.grupocaravela.configuracao.EntityManagerProducer;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.JButton;
import javax.swing.ImageIcon;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class JanelaConfiguracao extends JFrame {

	private JPanel contentPane;
	private JTextField tfIp;
	private JPanel panel_1;
	private JButton btnConfirmar;
	private JButton btnCancelar;
	private JLabel lblNomeDaEmpresa;
	private JTextField tfNomeEmpresa;
	private JTextField tfPorta;
	private JLabel lblPorta;
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					JanelaConfiguracao frame = new JanelaConfiguracao();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public JanelaConfiguracao() {
		carregarJanela();
		try {
			tfNomeEmpresa.setText(lerArquivoNomeEmpresa());
			tfIp.setText(lerArquivoIp());
			tfPorta.setText(lerArquivoPorta());
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Erro ao ler o arquivo de configuração!");
		}
		
	}
	
	private void carregarJanela(){
		setTitle("Configuração do servidor");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 450, 274);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		
		JPanel panel = new JPanel();
		panel.setBorder(new TitledBorder(null, "Configura\u00E7\u00E3o do servidor", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		
		panel_1 = new JPanel();
		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addComponent(panel, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 438, Short.MAX_VALUE)
				.addComponent(panel_1, GroupLayout.DEFAULT_SIZE, 430, Short.MAX_VALUE)
		);
		gl_contentPane.setVerticalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addComponent(panel, GroupLayout.PREFERRED_SIZE, 162, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED, 34, Short.MAX_VALUE)
					.addComponent(panel_1, GroupLayout.PREFERRED_SIZE, 62, GroupLayout.PREFERRED_SIZE))
		);
		
		btnConfirmar = new JButton("Confirmar");
		btnConfirmar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				escreverArquivoNomeEmpresa(tfNomeEmpresa.getText());
				escreverArquivoIp(tfIp.getText());
				escreverArquivoPorta(tfPorta.getText());
				
				Empresa.setIpLocalServidor(lerArquivoIp());
				
				JOptionPane.showMessageDialog(null, "O sistema será fechado para que as configurações sejam aplicadas!");
				
				System.exit(0);
				
			}
		});
		btnConfirmar.setIcon(new ImageIcon(JanelaConfiguracao.class.getResource("/br/com/grupocaravela/icones/aprovar_24.png")));
		
		btnCancelar = new JButton("Cancelar");
		btnCancelar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		btnCancelar.setIcon(new ImageIcon(JanelaConfiguracao.class.getResource("/br/com/grupocaravela/icones/alerta_amarelo_24.png")));
		GroupLayout gl_panel_1 = new GroupLayout(panel_1);
		gl_panel_1.setHorizontalGroup(
			gl_panel_1.createParallelGroup(Alignment.LEADING)
				.addGroup(Alignment.TRAILING, gl_panel_1.createSequentialGroup()
					.addContainerGap(166, Short.MAX_VALUE)
					.addComponent(btnCancelar)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(btnConfirmar)
					.addContainerGap())
		);
		gl_panel_1.setVerticalGroup(
			gl_panel_1.createParallelGroup(Alignment.LEADING)
				.addGroup(Alignment.TRAILING, gl_panel_1.createSequentialGroup()
					.addContainerGap(16, Short.MAX_VALUE)
					.addGroup(gl_panel_1.createParallelGroup(Alignment.TRAILING)
						.addComponent(btnCancelar)
						.addComponent(btnConfirmar))
					.addContainerGap())
		);
		panel_1.setLayout(gl_panel_1);
		
		JLabel lblIp = new JLabel("IP:");
		
		tfIp = new JTextField();
		tfIp.setColumns(10);
		
		lblNomeDaEmpresa = new JLabel("Nome da empresa:");
		
		tfNomeEmpresa = new JTextField();
		tfNomeEmpresa.setColumns(10);
		
		tfPorta = new JTextField();
		tfPorta.setColumns(10);
		
		lblPorta = new JLabel("Porta:");
		GroupLayout gl_panel = new GroupLayout(panel);
		gl_panel.setHorizontalGroup(
			gl_panel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
						.addComponent(tfNomeEmpresa, GroupLayout.DEFAULT_SIZE, 404, Short.MAX_VALUE)
						.addComponent(lblNomeDaEmpresa)
						.addGroup(gl_panel.createSequentialGroup()
							.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
								.addComponent(tfIp, GroupLayout.PREFERRED_SIZE, 182, GroupLayout.PREFERRED_SIZE)
								.addComponent(lblIp))
							.addGap(18)
							.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
								.addComponent(lblPorta)
								.addComponent(tfPorta, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))))
					.addContainerGap())
		);
		gl_panel.setVerticalGroup(
			gl_panel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel.createSequentialGroup()
					.addContainerGap()
					.addComponent(lblNomeDaEmpresa)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(tfNomeEmpresa, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addGroup(gl_panel.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblIp)
						.addComponent(lblPorta))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_panel.createParallelGroup(Alignment.BASELINE)
						.addComponent(tfIp, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(tfPorta, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addContainerGap(36, Short.MAX_VALUE))
		);
		panel.setLayout(gl_panel);
		contentPane.setLayout(gl_contentPane);
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
	
	private void escreverArquivoNomeEmpresa(String nome){
		
		File file = null;
        
        //Permissao de acesso
        if ("Linux".equals(System.getProperty("os.name"))) {  //Verifica se o sistema é linux
                file = new File("/opt/GrupoCaravela/software/nome.txt");
            } else {
                file = new File("c:\\GrupoCaravela\\software\\nome.txt");
            }
        
        try {
        	BufferedWriter writer = new BufferedWriter(new FileWriter(file));
            writer.write(nome);
            writer.flush(); //Cria o conteúdo do arquivo.
            writer.close(); //Fechando conexão e escrita do arquivo.
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Erro ao criar arquivo de configuração nome!!!");
		}
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
			JOptionPane.showMessageDialog(null, "ERRO! Erro ao ler o arquivo de ip do sistema!");
		}

		return ip;
	}
	
	private void escreverArquivoIp(String ip){
		
		File file = null;
        
        //Permissao de acesso
        if ("Linux".equals(System.getProperty("os.name"))) {  //Verifica se o sistema é linux
                file = new File("/opt/GrupoCaravela/software/conf.txt");
            } else {
                file = new File("c:\\GrupoCaravela\\software\\conf.txt");
            }
        
        try {
        	BufferedWriter writer = new BufferedWriter(new FileWriter(file));
            writer.write(ip);
            writer.flush(); //Cria o conteúdo do arquivo.
            writer.close(); //Fechando conexão e escrita do arquivo.
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Erro ao criar arquivo de ip configuração!!!");
		}
	}	
	
	private String lerArquivoPorta() {

		FileReader fileReader;
		String sistema = System.getProperty("os.name");
		String porta = null;

		try {
			if ("Linux".equals(sistema)) {
				fileReader = new FileReader("/opt/GrupoCaravela/software/porta.txt");
			} else {
				fileReader = new FileReader("c:\\GrupoCaravela\\software\\porta.txt");
			}

			BufferedReader reader = new BufferedReader(fileReader);
			String data = null;
			while ((data = reader.readLine()) != null) {
				porta = String.valueOf(data);
			}
			fileReader.close();
			reader.close();
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "ERRO! Erro ao ler o arquivo de porta do sistema!");
		}

		return porta;
	}
	
	private void escreverArquivoPorta(String porta){
		
		File file = null;
        
        //Permissao de acesso
        if ("Linux".equals(System.getProperty("os.name"))) {  //Verifica se o sistema é linux
                file = new File("/opt/GrupoCaravela/software/porta.txt");
            } else {
                file = new File("c:\\GrupoCaravela\\software\\porta.txt");
            }
        
        try {
        	BufferedWriter writer = new BufferedWriter(new FileWriter(file));
            writer.write(porta);
            writer.flush(); //Cria o conteúdo do arquivo.
            writer.close(); //Fechando conexão e escrita do arquivo.
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Erro ao criar arquivo de porta configuração!!!");
		}
	}	
	
}
