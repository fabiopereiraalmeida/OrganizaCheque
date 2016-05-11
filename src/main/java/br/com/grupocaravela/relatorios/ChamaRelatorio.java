package br.com.grupocaravela.relatorios;

import java.net.URL;
import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.swing.ImageIcon;

import br.com.grupocaravela.objeto.Cheque;
import br.com.grupocaravela.util.ConectaBanco;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.view.JasperViewer;

public class ChamaRelatorio {

	String sistema = System.getProperty("os.name");
	
	ImageIcon gto = new ImageIcon(getClass().getResource("/br/com/grupocaravela/relatorios/logo_caravela.png"));
	//map.put("LOGO", gto.getImage());

	//método
	    public void report(String endereco, Cheque cheque, Date dataInicial, Date dataFinal) throws JRException {

	        Connection conn = ConectaBanco.getConnection();

	        JasperReport jasper;

	        Map map = new HashMap<>();

	        URL arquivo = getClass().getResource(endereco);
	        jasper = (JasperReport) JRLoader.loadObject(arquivo);
	        
	        map.put("LOGO", gto.getImage());
	        
	        if (cheque != null) {
	        	//map.put("ID_CHEQUE", "6");
	        	map.put("ID_CHEQUE", cheque.getId());
			}
	        
	        
	        if (dataInicial != null && dataFinal != null) {
	        	SimpleDateFormat formatUsa = new SimpleDateFormat("yyyy/MM/dd");
		        SimpleDateFormat formatBra = new SimpleDateFormat("dd/MM/yyyy");
		       	        
		        map.put("DATA_INICIAL_SQL", formatUsa.format(dataInicial));
		        map.put("DATA_FINAL_SQL", formatUsa.format(dataFinal));
		        
		        map.put("DATA_INICIAL", formatBra.format(dataInicial));
		        map.put("DATA_FINAL", formatBra.format(dataFinal));
			}
	        
	        	        
	        JasperPrint jasperPrint = JasperFillManager.fillReport(jasper, map, conn);

	        JasperViewer jv = new JasperViewer(jasperPrint, false);
	        jv.setVisible(true);

	    }
	
}
