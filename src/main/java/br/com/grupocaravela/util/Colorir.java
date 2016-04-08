package br.com.grupocaravela.util;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

public class Colorir extends JLabel implements TableCellRenderer{

public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column){
 		//if(estoque == 0){
	       setForeground(Color.RED);	
	//}else{
	      // setForeground(Color.BLACK);		
	//}
 		setText(value.toString());
	return this;   	
}

}

