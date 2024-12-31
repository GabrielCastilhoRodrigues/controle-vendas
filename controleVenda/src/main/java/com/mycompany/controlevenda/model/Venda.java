package com.mycompany.controlevenda.model;

import com.mycompany.controlevenda.constants.ValidacoesConstants;
import com.mycompany.controlevenda.constants.model.ItemVendaConstants;
import com.mycompanyy.controlevenda.utilities.NumberUtilities;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Entidade Venda.
 *
 * @author gabri
 */
public class Venda {

    /**
     * Código da Venda.
     */
    private long codigo;

    /**
     * Código do Cliente vinculado a Venda.
     */
    private Cliente cliente;

    /**
     * Data que foi realizada a venda.
     */
    private LocalDate dataVenda;

    /**
     * Valor total da Venda.
     */
    private BigDecimal valorTotal;

    /**
     * Itens presentes na venda.
     */
    private List<ItemVenda> itensVenda = new ArrayList<>();

    public Venda() {
    }

    public Venda(Cliente cliente) {
        this.cliente = cliente;
        this.dataVenda = LocalDate.now();
    }

    public long getCodigo() {
        return codigo;
    }

    public void setCodigo(long codigo) {
        this.codigo = codigo;
    }

    public LocalDate getDataVenda() {
        return dataVenda;
    }

    public void setDataVenda(LocalDate dataVenda) {
        this.dataVenda = dataVenda;
    }

    public BigDecimal getValorTotal() {
        return valorTotal;
    }

    public void setValorTotal(BigDecimal valorTotal) {
        this.valorTotal = valorTotal;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public List<ItemVenda> getItensVenda() {
        return itensVenda;
    }

    public void setItensVenda(List<ItemVenda> itensVenda) {
        this.itensVenda = itensVenda;
    }
}
