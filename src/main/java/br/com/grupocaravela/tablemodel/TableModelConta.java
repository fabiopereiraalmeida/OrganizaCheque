package br.com.grupocaravela.tablemodel;

import java.util.ArrayList;

import javax.swing.table.AbstractTableModel;

import br.com.grupocaravela.objeto.Conta;

public class TableModelConta extends AbstractTableModel{

	private static final long serialVersionUID = 1L;

	private ArrayList<Conta> listaConta;
    //Titulo das colunas
    private String[] colunas = {"Id", "Numero", "Agência", "Banco"};
    
    //Construtor
    public TableModelConta(){
        this.listaConta = new ArrayList<>();
    }
    
    //############### Inicio dos Metodos do TableModel ###################
    public void addConta(Conta c){
        this.listaConta.add(c);
        fireTableDataChanged();
    }
    
    public void removeConta(int rowIndex){
        this.listaConta.remove(rowIndex);
        fireTableDataChanged();
    }
    
    public Conta getConta(int rowIndex){
        return this.listaConta.get(rowIndex); 
    }
    
    //############### Fim dos Metodos do TableModel ###################

    @Override
    public int getRowCount() {
        return this.listaConta.size();
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
                return this.listaConta.get(rowIndex).getId();
            
            case 1:
                return this.listaConta.get(rowIndex).getNumero();
                
            case 2:
                return this.listaConta.get(rowIndex).getAgencia().getCod();

            case 3:
                return this.listaConta.get(rowIndex).getBanco().getNome();
                
            default:
                return this.listaConta.get(rowIndex); //Desta forma é retornado o objeto inteiro
        }
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    @Override
    public String getColumnName(int columnIndex){
        return this.colunas[columnIndex];
    }
}
