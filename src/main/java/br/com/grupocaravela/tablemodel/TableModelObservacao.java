package br.com.grupocaravela.tablemodel;

import java.util.ArrayList;

import javax.swing.table.AbstractTableModel;

import br.com.grupocaravela.objeto.Observacao;

public class TableModelObservacao extends AbstractTableModel{

	private static final long serialVersionUID = 1L;

	private ArrayList<Observacao> listaObservacao;
    //Titulo das colunas
    private String[] colunas = {"Id", "Nome", "Observação"};
    
    //Construtor
    public TableModelObservacao(){
        this.listaObservacao = new ArrayList<>();
    }
    
    //############### Inicio dos Metodos do TableModel ###################
    public void addObservacao(Observacao c){
        this.listaObservacao.add(c);
        fireTableDataChanged();
    }
    
    public void removeObservacao(int rowIndex){
        this.listaObservacao.remove(rowIndex);
        fireTableDataChanged();
    }
    
    public Observacao getObservacao(int rowIndex){
        return this.listaObservacao.get(rowIndex); 
    }
    
    //############### Fim dos Metodos do TableModel ###################

    @Override
    public int getRowCount() {
        return this.listaObservacao.size();
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
                return this.listaObservacao.get(rowIndex).getId();
            
            case 1:
                return this.listaObservacao.get(rowIndex).getNome();
                
            case 2:
                return this.listaObservacao.get(rowIndex).getObservacao();
                
            default:
                return this.listaObservacao.get(rowIndex); //Desta forma é retornado o objeto inteiro
        }
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    @Override
    public String getColumnName(int columnIndex){
        return this.colunas[columnIndex];
    }
}
