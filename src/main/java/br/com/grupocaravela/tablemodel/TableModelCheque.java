package br.com.grupocaravela.tablemodel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import javax.swing.table.AbstractTableModel;

import br.com.grupocaravela.objeto.Cheque;

public class TableModelCheque extends AbstractTableModel{

	private static final long serialVersionUID = 1L;

	private SimpleDateFormat formatData = new SimpleDateFormat("dd/MM/yyyy");
	private ArrayList<Cheque> listaCheque;
    //Titulo das colunas
    private String[] colunas = {"Id", "Num. Cheque", "Conta", "Agência", "Banco", "Valor", "Vencimento", "Proprietario"};
    
    //Construtor
    public TableModelCheque(){
        this.listaCheque = new ArrayList<>();
    }
    
    //############### Inicio dos Metodos do TableModel ###################
    public void addCheque(Cheque c){
        this.listaCheque.add(c);
        fireTableDataChanged();
    }
    
    public void removeCheque(int rowIndex){
        this.listaCheque.remove(rowIndex);
        fireTableDataChanged();
    }
    
    public Cheque getCheque(int rowIndex){
        return this.listaCheque.get(rowIndex); 
    }
    
    //############### Fim dos Metodos do TableModel ###################

    @Override
    public int getRowCount() {
        return this.listaCheque.size();
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int getColumnCount() {
        return colunas.length;
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        switch(columnIndex){
            case 0:
                return this.listaCheque.get(rowIndex).getId();
            
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
            	try {
					return formatData.format(this.listaCheque.get(rowIndex).getDataVencimento());
				} catch (Exception e) {
					return "";
				}

            case 7:
                return this.listaCheque.get(rowIndex).getProprietario().getNome();
                
            default:
                return this.listaCheque.get(rowIndex); //Desta forma é retornado o objeto inteiro
        }
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    @Override
    public String getColumnName(int columnIndex){
        return this.colunas[columnIndex];
    }
}
