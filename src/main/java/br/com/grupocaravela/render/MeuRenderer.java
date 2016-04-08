package br.com.grupocaravela.render;

import java.awt.Color;
import java.awt.Component;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;

import br.com.grupocaravela.objeto.Cheque;

/**
 *
 * @author Julio Cezar
 */
public class MeuRenderer implements TableCellRenderer {
	public static final DefaultTableCellRenderer DEFAULT_RENDERER = new DefaultTableCellRenderer();
	boolean tabela;

	private SimpleDateFormat formatData = new SimpleDateFormat("dd/MM/yyyy");
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");     

	public MeuRenderer() {
		this.tabela = tabela;
		
	}

	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
			int row, int column) {
		Component renderer = DEFAULT_RENDERER.getTableCellRendererComponent(table, value, isSelected, hasFocus, row,
				column);
		
		//Color background;
		//Color foreground;
		
		//foreground = Color.BLACK;
		//background = Color.WHITE;
		
		//renderer.setBackground(background);
		//renderer.setForeground(foreground);
		
		Boolean objeto = (Boolean) table.getModel().getValueAt(row, 10);
						
		if (objeto == null) {
			objeto = false;
		}
		
		if (objeto == true) {
			renderer.setBackground(Color.GREEN);			
		}
		
		return renderer;
		/*
		
		Color foreground;
		Color background;

		foreground = Color.BLACK;
		background = Color.GREEN;

		Object valueData = table.getModel().getValueAt(row, 6);
		Object valueDevolvido = table.getModel().getValueAt(row, 9);

		Date vencimento;
		String devolvido = valueDevolvido.toString();
		
		try {
			vencimento = formatData.parse(valueData.toString());

		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			vencimento = null;
		}

		if (vencimento != null) {

			if (vencimento.equals(dataAtual())) {
				foreground = Color.GREEN;
				background = Color.WHITE;
			} else if (vencimento.before(dataAtual())) {
				foreground = Color.MAGENTA;
				background = Color.WHITE;
			} else {
				foreground = Color.BLACK;
				background = Color.WHITE;
			}
		}
		
		if (devolvido.equals("Sim")) {
			foreground = Color.WHITE;
			background = Color.BLACK;
		}
		
		if (isSelected) {
			foreground = Color.WHITE;
			background = new Color(113, 113, 255);
		}

		renderer.setForeground(foreground);
		renderer.setBackground(background);
		return renderer;
		*/
	}

	private java.util.Date dataAtual() {

		 
		Date hoje = null;
		try {
			hoje = sdf.parse(sdf.format(new Date()));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// java.util.Date hoje = Calendar.getInstance().getTime();
		return hoje;
	}
	
	private boolean verificaChequeVoltou(Cheque cheque){
		
		boolean retorno = false;
		
		if (cheque.getVoltouUmaVez()) {
			retorno = true;
		}
		
		return retorno;
	}
}