package br.com.grupocaravela.configuracao;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.swing.JOptionPane;

public class EntityManagerProducer {

	private static EntityManagerFactory factory;

	public EntityManagerProducer() {

		try {
			
			Map<String, String> properties = new HashMap<String, String>();
			properties.put("javax.persistence.jdbc.url", "jdbc:mysql://" + lerArquivoIp() +"/cheque");
			properties.put("javax.persistence.jdbc.user", "root");
			properties.put("javax.persistence.jdbc.password", "peperoni");
			
			factory = Persistence.createEntityManagerFactory("chequePU", properties);
		} catch (Exception e) {

			JOptionPane.showMessageDialog(null, "ATENÇÃO! Servidor não encontrado!!!" + e);

		}
	}

	public static EntityManager createEntityManager() {
		return factory.createEntityManager();
	}

	public void closeEntityManager(EntityManager manager) {
		manager.close();
	}
	
	private String lerArquivoIp() {

		FileReader fileReader;
		String sistema = System.getProperty("os.name");
		String ip = null;

		try {
			if ("Linux".equals(sistema)) {
				fileReader = new FileReader("/opt/GrupoCaravela/software/conf.txt");
			} else {
				fileReader = new FileReader("C:\\GrupoCaravela\\software\\conf.txt");
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
