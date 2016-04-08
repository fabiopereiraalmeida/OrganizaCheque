package br.com.grupocaravela.tablemodel;

import java.util.ArrayList;

import javax.swing.table.AbstractTableModel;

import br.com.grupocaravela.objeto.Banco;

public class TableModelBanco extends AbstractTableModel{

	private static final long serialVersionUID = 1L;

	private ArrayList<Banco> listaBanco;
    //Titulo das colunas
    private String[] colunas = {"Id", "Cod.", "Nome"};
    
    //Construtor
    public TableModelBanco(){
        this.listaBanco = new ArrayList<>();
    }
    
    //############### Inicio dos Metodos do TableModel ###################
    public void addBanco(Banco c){
        this.listaBanco.add(c);
        fireTableDataChanged();
    }
    
    public void removeBanco(int rowIndex){
        this.listaBanco.remove(rowIndex);
        fireTableDataChanged();
    }
    
    public Banco getBanco(int rowIndex){
        return this.listaBanco.get(rowIndex); 
    }
    
    //############### Fim dos Metodos do TableModel ###################

    @Override
    public int getRowCount() {
        return this.listaBanco.size();
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
                return this.listaBanco.get(rowIndex).getId();
            
            case 1:
                return this.listaBanco.get(rowIndex).getCod();
                
            case 2:
                return this.listaBanco.get(rowIndex).getNome();
                
            default:
                return this.listaBanco.get(rowIndex); //Desta forma é retornado o objeto inteiro
        }
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    @Override
    public String getColumnName(int columnIndex){
        return this.colunas[columnIndex];
    }
}
