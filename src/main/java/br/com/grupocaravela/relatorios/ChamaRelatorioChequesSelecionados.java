package br.com.grupocaravela.relatorios;

import java.net.URL;
import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.ImageIcon;

import br.com.grupocaravela.configuracao.Empresa;
import br.com.grupocaravela.objeto.Cheque;
import br.com.grupocaravela.util.ConectaBanco;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.view.JasperViewer;

public class ChamaRelatorioChequesSelecionados {

	String sistema = System.getProperty("os.name");
	
	ImageIcon gto = new ImageIcon(getClass().getResource("/br/com/grupocaravela/relatorios/logo_caravela.png"));
	
	    public void report(String endereco, ArrayList<Cheque> listaCheque, String valorTotal) throws JRException {
    	
	        JasperReport jasper;

	        Map map = new HashMap<>();
	        
	        URL arquivo = getClass().getResource(endereco);
	        jasper = (JasperReport) JRLoader.loadObject(arquivo);
	        
	        map.put("LOGO", gto.getImage());
	        
	        map.put("VALOR_TOTAL", valorTotal);
	        
	        JRBeanCollectionDataSource ds = new JRBeanCollectionDataSource(listaCheque);
	       	        
	        JasperPrint jasperPrint = JasperFillManager.fillReport(jasper, map, ds);

	        JasperViewer jv = new JasperViewer(jasperPrint, false);
	        jv.setVisible(true);

	    }
	
	    public void reportHistoricoMovimentacao( Double total, String endereco, ArrayList<Cheque> listaCheque, String obs, String data, String destinatario) throws JRException {
	    	
	        JasperReport jasper;

	        Map map = new HashMap<>();
	        
	        URL arquivo = getClass().getResource(endereco);
	        jasper = (JasperReport) JRLoader.loadObject(arquivo);
	        
	        map.put("OBS", obs);
	        map.put("DATA", data);
	        map.put("DESTINATARIO", destinatario);
	        map.put("TOTAL", total);
	        map.put("LOGO", gto.getImage());
	        	        
	        JRBeanCollectionDataSource ds = new JRBeanCollectionDataSource(listaCheque);
	       	        
	        JasperPrint jasperPrint = JasperFillManager.fillReport(jasper, map, ds);
	        	 
	        JasperViewer jv = new JasperViewer(jasperPrint, false);
	        jv.setVisible(true);

	    }
	    
	    public void reportLucro(String endereco, ArrayList<Cheque> listaCheque, String valorTotal, String pagoTotal, String ganhoTotal, String mediaTotal,
	    		String destinatario, String dtInicial, String dtFinal) throws JRException {
	    	
	        JasperReport jasper;

	        Map map = new HashMap<>();
	        
	        URL arquivo = getClass().getResource(endereco);
	        jasper = (JasperReport) JRLoader.loadObject(arquivo);
	        
	        //map.put("LOGO", gto.getImage());
	        
	        map.put("VALOR_TOTAL", valorTotal);
	        map.put("TOTAL_LUCRO", ganhoTotal);
	        map.put("TOTAL_LUCRO_INDIVIDUAL", ganhoTotal);
	        map.put("TOTAL_PAGO", pagoTotal);
	        map.put("MEDIA_JUROS", mediaTotal);
	        map.put("DESTINATARIO", destinatario);
	        map.put("DATA_INICIAL", dtInicial);
	        map.put("DATA_FINAL", dtFinal);
	     
	        JRBeanCollectionDataSource ds = new JRBeanCollectionDataSource(listaCheque);
	       	        
	        JasperPrint jasperPrint = JasperFillManager.fillReport(jasper, map, ds);
	        	            
	        JasperViewer jv = new JasperViewer(jasperPrint, false);
	        jv.setVisible(true);

	    }
	    
	    public void reportLucroIndividual(String endereco, String valorTotal, String pagoTotal, String ganhoTotal, String ganhoTotalIndividual, String mediaTotal,
	    		String destinatario, String idDestinatario, String dtInicial, String dtFinal, String dtInicialSql, String dtFinalSql) throws JRException {
	    	
	    	Connection conn = ConectaBanco.getConnection();

	        JasperReport jasper; 

	        Map map = new HashMap<>();	                

	        URL arquivo = getClass().getResource(endereco);
	        jasper = (JasperReport) JRLoader.loadObject(arquivo);

	        //map.put("LOGO", gto.getImage());
	        map.put("ID_BENEFICIARIO", idDestinatario);
	        
	        map.put("VALOR_TOTAL", valorTotal);
	        map.put("TOTAL_LUCRO", ganhoTotal);
	        map.put("TOTAL_LUCRO_INDIVIDUAL", ganhoTotalIndividual);
	        map.put("TOTAL_PAGO", pagoTotal);
	        map.put("MEDIA_JUROS", mediaTotal);
	        map.put("DESTINATARIO", destinatario);
	        map.put("DATA_INICIAL", dtInicial);
	        map.put("DATA_FINAL", dtFinal);
	        map.put("DATA_INICIAL_SQL", dtInicialSql);
	        map.put("DATA_FINAL_SQL", dtFinalSql);
	        
	        JasperPrint jasperPrint = JasperFillManager.fillReport(jasper, map, conn);

	        JasperViewer jv = new JasperViewer(jasperPrint, false);
	        jv.setVisible(true);
	    	
	        
	     
	        //JRBeanCollectionDataSource ds = new JRBeanCollectionDataSource(listaCheque);
	       	        
	        //JasperPrint jasperPrint = JasperFillManager.fillReport(jasper, map, ds);
	        	            
	        //JasperViewer jv = new JasperViewer(jasperPrint, false);
	        //jv.setVisible(true);

	    }
}
