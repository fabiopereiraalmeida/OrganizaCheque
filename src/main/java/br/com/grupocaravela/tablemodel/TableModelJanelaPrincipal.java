package br.com.grupocaravela.tablemodel;

import java.awt.Color;
import java.awt.Component;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;

import br.com.grupocaravela.objeto.Cheque;
import br.com.grupocaravela.render.TableRenderer;

public class TableModelJanelaPrincipal extends AbstractTableModel {

	private static final long serialVersionUID = 1L;

	private SimpleDateFormat formatData = new SimpleDateFormat("dd/MM/yyyy");
	private ArrayList<Cheque> listaCheque;
	private Cheque cheque;
	// Titulo das colunas
	private String[] colunas = { "Entrada", "Num. Cheque", "Conta", "Agência", "Banco", "Valor", "Vencimento",
			"Proprietario", "Destinatario", "Devolv.", "" };

	// Construtor
	public TableModelJanelaPrincipal() {
		this.listaCheque = new ArrayList<>();
	}

	// ############### Inicio dos Metodos do TableModel ###################
	
	//#######################Cor nas linhas ################################
	
	List<Color> rowColours = Arrays.asList(
	        Color.RED,
	        Color.GREEN,
	        Color.CYAN
	    );

	    public void setRowColour(int row, Color c) {
	        rowColours.set(row, c);
	        fireTableRowsUpdated(row, row);
	    }

	    public Color getRowColour(int row) {
	        return rowColours.get(row);
	    }
	
	//######################Fim cor nas linha ##############################
	
	
	public Class<?> getColumnClass(int coluna) {  
        if(coluna== 10){  
            return Boolean.class;  
        }  
        else  
            //return String.class;
        	return super.getColumnClass(coluna);
    }   
	
	@Override
    public boolean isCellEditable(int row, int col) {
        return col == 10;
    }

	@Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {

        //this.cheque.setTerceiros((Boolean) aValue);
		this.listaCheque.get(rowIndex).setSelecionado((Boolean) aValue);
        fireTableCellUpdated(rowIndex, columnIndex);// notify listeners
    }
	
	public void addCheque(Cheque c) {
		this.listaCheque.add(c);
		fireTableDataChanged();
	}

	public void removeCheque(int rowIndex) {
		this.listaCheque.remove(rowIndex);
		fireTableDataChanged();
	}

	public Cheque getCheque(int rowIndex) {
		return this.listaCheque.get(rowIndex);
	}

	// ############### Fim dos Metodos do TableModel ###################
	
	@Override
	public int getRowCount() {
		return this.listaCheque.size();
		// throw new UnsupportedOperationException("Not supported yet."); //To
		// change body of generated methods, choose Tools | Templates.
	}

	@Override
	public int getColumnCount() {
		return colunas.length;
		// throw new UnsupportedOperationException("Not supported yet."); //To
		// change body of generated methods, choose Tools | Templates.
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		switch (columnIndex) {

		case 0:
			return this.listaCheque.get(rowIndex).getDataEntrada();
			/*
			try {
				return formatData.format(this.listaCheque.get(rowIndex).getDataEntrada());
			} catch (Exception e) {
				return "";
			}
			*/
		case 1:
			return this.listaCheque.get(rowIndex).getNumCheque();

		case 2:
			return this.listaCheque.get(rowIndex).getConta().getNumero();

		case 3:
			return this.listaCheque.get(rowIndex).getAgencia().getCod();

		case 4:
			return this.listaCheque.get(rowIndex).getBanco().getNome();

		case 5:
			return this.listaCheque.get(rowIndex).getValor();

		case 6:
			return this.listaCheque.get(rowIndex).getDataVencimento();
			/*
			try {
				return formatData.format(this.listaCheque.get(rowIndex).getDataVencimento());
			} catch (Exception e) {
				return "";
			}
			*/
		case 7:
			return this.listaCheque.get(rowIndex).getProprietario().getNome();

		case 8:
			return this.listaCheque.get(rowIndex).getDestinatario().getNome();

		case 9:

			if (this.listaCheque.get(rowIndex).getVoltouUmaVez() != null) {

				if (this.listaCheque.get(rowIndex).getVoltouUmaVez()) {
					return "Sim";
				} else {
					return "Não";
				}
			}else{
				return "Não";
			}
			
		case 10:
			return this.listaCheque.get(rowIndex).getSelecionado();
			//return false;
						
		default:
			return this.listaCheque.get(rowIndex); // Desta forma é retornado o
													// objeto inteiro
		}
		// throw new UnsupportedOperationException("Not supported yet."); //To
		// change body of generated methods, choose Tools | Templates.
	}
/*
	@Override 
    public boolean isCellEditable(int row, int col){
        if(col == 10){
            return true;
        }
        return false;
    }
	
	public void setValueAt(Object value, int row, int col) {
        
		Cheque c = this.listaCheque.get(row);
		
		switch (col) {
		
			case 10:
				break;
			
		//data.get(row)[col] = value;
		//fireTableCellUpdated(row, col);
		}
		fireTableCellUpdated(row, col);
    }
*/
	@Override
	public String getColumnName(int columnIndex) {
		return this.colunas[columnIndex];
	}

	// ############################################################

}
