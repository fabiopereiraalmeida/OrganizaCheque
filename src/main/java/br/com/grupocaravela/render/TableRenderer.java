package br.com.grupocaravela.render;

import java.awt.Color;
import java.awt.Component;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

public class TableRenderer extends DefaultTableCellRenderer {

	private SimpleDateFormat formatDataBRA = new SimpleDateFormat("dd/MM/yyyy");
	private SimpleDateFormat formatDataUSA = new SimpleDateFormat("yyyy/MM/dd");
	
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        Component comp = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

        comp.setBackground(Color.GREEN);
        
        /*
        String estado = table.getModel().getValueAt(row, 2).toString();
        try {
        	//Date vencimento = formatDataBRA.parse(table.getModel().getValueAt(row, 6).toString());
            
            if(estado.equals("177075")){
            //if(dataAtual().after(vencimento)){
                comp.setBackground(Color.RED);
                //comp.setBackground(new Color(100, 200, 50));
            } else{
                comp.setBackground(Color.GREEN);
                //comp.setBackground(new Color(255, 91, 96));
            }
		} catch (Exception e) {
			// TODO: handle exception
		}
        */
        /*
        else if(estado.equals(Principal.PROCESSANDO)){
            //comp.setBackground(Color.BLUE);
            comp.setBackground(new Color(109, 149, 254));
        } else if(estado.equals(Principal.PARADO)){
            comp.setBackground(new Color(170, 170, 170));
            //comp.setBackground(Color.GRAY);
        }
        */
        return comp;
    }
    
    private java.util.Date dataAtual() {

		java.util.Date hoje = new java.util.Date();
		// java.util.Date hoje = Calendar.getInstance().getTime();
		return hoje;

	}
}
