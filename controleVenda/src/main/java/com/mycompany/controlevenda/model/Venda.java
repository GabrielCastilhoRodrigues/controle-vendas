package com.mycompany.controlevenda.model;

/**
 * Entidade Venda.
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
    private long codigoCliente;

    /**
     * Código do Produto vinculado a Venda.
     */
    private long codigoProduto;

    /**
     * Valor total da Venda.
     */
    private double valorTotal;

    public Venda(long codigoCliente, long codigoProduto, double valorTotal) {
        this.codigoCliente = codigoCliente;
        this.codigoProduto = codigoProduto;
        this.valorTotal = valorTotal;
    }

    public long getCodigo() {
        return codigo;
    }

    public long getCodigoCliente() {
        return codigoCliente;
    }

    public void setCodigoCliente(long codigoCliente) {
        this.codigoCliente = codigoCliente;
    }

    public long getCodigoProduto() {
        return codigoProduto;
    }

    public void setCodigoProduto(long codigoProduto) {
        this.codigoProduto = codigoProduto;
    }

    public double getValorTotal() {
        return valorTotal;
    }

    public void setValorTotal(double valorTotal) {
        this.valorTotal = valorTotal;
    }
}
