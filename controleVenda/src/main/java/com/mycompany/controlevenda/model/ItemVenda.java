package com.mycompany.controlevenda.model;

import java.math.BigDecimal;

/**
 * Entidade ItemVenda.
 *
 * @author gabri
 */
public class ItemVenda {

    /**
     * Código do ItemVenda.
     */
    private long codigo;

    /**
     * Venda vinculado ao ItemVenda.
     */
    private Venda venda;

    /**
     * Produto vinculado ao ItemVenda.
     */
    private Produto produto;

    /**
     * Quantidade do Produto no ItemVenda.
     */
    private BigDecimal quantidade;

    /**
     * Valor Total do ItemVenda, considerando Quantidade * Preço do Produto.
     */
    private BigDecimal totalItem;

    public ItemVenda() {
    }

    public ItemVenda(Venda venda, Produto produto, BigDecimal quantidade) {
        this.produto = produto;
        this.quantidade = quantidade;
        this.totalItem = produto.getPreco().multiply(quantidade);
    }

    public ItemVenda(Produto produto, BigDecimal quantidade) {
        this.produto = produto;
        this.quantidade = quantidade;
    }

    public long getCodigo() {
        return codigo;
    }

    public void setCodigo(long codigo) {
        this.codigo = codigo;
    }

    public Venda getVenda() {
        return venda;
    }

    public void setVenda(Venda venda) {
        this.venda = venda;
    }

    public Produto getProduto() {
        return produto;
    }

    public void setProduto(Produto produto) {
        this.produto = produto;
    }

    public BigDecimal getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(BigDecimal quantidade) {
        this.quantidade = quantidade;
    }

    public BigDecimal getTotalItem() {
        return totalItem;
    }

    public void setTotalItem(BigDecimal totalItem) {
        this.totalItem = totalItem;
    }
}
