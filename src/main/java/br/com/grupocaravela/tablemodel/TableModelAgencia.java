package br.com.grupocaravela.tablemodel;

import java.util.ArrayList;

import javax.swing.table.AbstractTableModel;

import br.com.grupocaravela.objeto.Agencia;

public class TableModelAgencia extends AbstractTableModel{

	private static final long serialVersionUID = 1L;

	private ArrayList<Agencia> listaAgencia;
    //Titulo das colunas
    private String[] colunas = {"Id", "Cod.", "Banco", "Cidade"};
    
    //Construtor
    public TableModelAgencia(){
        this.listaAgencia = new ArrayList<>();
    }
    
    //############### Inicio dos Metodos do TableModel ###################
    public void addAgencia(Agencia c){
        this.listaAgencia.add(c);
        fireTableDataChanged();
    }
    
    public void removeAgencia(int rowIndex){
        this.listaAgencia.remove(rowIndex);
        fireTableDataChanged();
    }
    
    public Agencia getAgencia(int rowIndex){
        return this.listaAgencia.get(rowIndex); 
    }
    
    //############### Fim dos Metodos do TableModel ###################

    @Override
    public int getRowCount() {
        return this.listaAgencia.size();
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
                return this.listaAgencia.get(rowIndex).getId();
            
            case 1:
                return this.listaAgencia.get(rowIndex).getCod();
                
            case 2:
                return this.listaAgencia.get(rowIndex).getBanco().getNome();
                
            case 3:
                return this.listaAgencia.get(rowIndex).getCidade();
                
            default:
                return this.listaAgencia.get(rowIndex); //Desta forma é retornado o objeto inteiro
        }
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    @Override
    public String getColumnName(int columnIndex){
        return this.colunas[columnIndex];
    }
}
