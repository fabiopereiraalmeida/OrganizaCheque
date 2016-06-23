package br.com.grupocaravela.configuracao;

public class Empresa {

	private static String nomeEpresa = "Empresa";
	private static String ipLocalServidor = "localhost";
	private static String portaLocalHttpServidor = "80";
	private static String portaMysqlLocalServidor = "3306";
	public static String getNomeEpresa() {
		return nomeEpresa;
	}
	public static void setNomeEpresa(String nomeEpresa) {
		Empresa.nomeEpresa = nomeEpresa;
	}
	public static String getIpLocalServidor() {
		return ipLocalServidor;
	}
	public static void setIpLocalServidor(String ipLocalServidor) {
		Empresa.ipLocalServidor = ipLocalServidor;
	}
	public static String getPortaLocalHttpServidor() {
		return portaLocalHttpServidor;
	}
	public static void setPortaLocalHttpServidor(String portaLocalHttpServidor) {
		Empresa.portaLocalHttpServidor = portaLocalHttpServidor;
	}
	public static String getPortaMysqlLocalServidor() {
		return portaMysqlLocalServidor;
	}
	public static void setPortaMysqlLocalServidor(String portaMysqlLocalServidor) {
		Empresa.portaMysqlLocalServidor = portaMysqlLocalServidor;
	}
	
	
		
}
