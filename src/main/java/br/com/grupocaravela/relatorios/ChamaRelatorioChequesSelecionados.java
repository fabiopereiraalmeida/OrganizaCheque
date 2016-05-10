package br.com.grupocaravela.relatorios;

import java.net.URL;
import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

	//método
	    public void report(String endereco, ArrayList<Cheque> listaCheque, String valorTotal) throws JRException {
    	
	        JasperReport jasper;

	        Map map = new HashMap<>();
	        
	        URL arquivo = getClass().getResource(endereco);
	        jasper = (JasperReport) JRLoader.loadObject(arquivo);
	        
	        map.put("VALOR_TOTAL", valorTotal);
	        
	        /*
	        // criando os dados que serão passados ao datasource
	        List dados = new ArrayList();	 
	        
	        for (int j = 0; j < listaCheque.size(); j++) {
	        	
	        	Cheque c = listaCheque.get(j);
	        	
	        	dados.add(j);
	        }	        
	        */
	        JRBeanCollectionDataSource ds = new JRBeanCollectionDataSource(listaCheque);
	       	        
	        JasperPrint jasperPrint = JasperFillManager.fillReport(jasper, map, ds);
	        	            
	        //JasperPrint jasperPrint = JasperFillManager.fillReport(jasper, map, ds);

	        JasperViewer jv = new JasperViewer(jasperPrint, false);
	        jv.setVisible(true);

	    }
	
	    public void reportHistoricoMovimentacao(String endereco, ArrayList<Cheque> listaCheque, String valorTotal, String data, String destinatario) throws JRException {
	    	
	        JasperReport jasper;

	        Map map = new HashMap<>();
	        
	        URL arquivo = getClass().getResource(endereco);
	        jasper = (JasperReport) JRLoader.loadObject(arquivo);
	        
	        map.put("VALOR_TOTAL", valorTotal);
	        map.put("DATA", data);
	        map.put("DESTINATARIO", destinatario);
	        
	        /*
	        // criando os dados que serão passados ao datasource
	        List dados = new ArrayList();	 
	        
	        for (int j = 0; j < listaCheque.size(); j++) {
	        	
	        	Cheque c = listaCheque.get(j);
	        	
	        	dados.add(j);
	        }	        
	        */
	        JRBeanCollectionDataSource ds = new JRBeanCollectionDataSource(listaCheque);
	       	        
	        JasperPrint jasperPrint = JasperFillManager.fillReport(jasper, map, ds);
	        	            
	        //JasperPrint jasperPrint = JasperFillManager.fillReport(jasper, map, ds);

	        JasperViewer jv = new JasperViewer(jasperPrint, false);
	        jv.setVisible(true);

	    }
}
