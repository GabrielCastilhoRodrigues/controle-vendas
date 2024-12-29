package com.mycompany.controlevenda.model;

import java.util.Date;

/**
 * Entidade Cliente.
 *
 * @author gabri
 */
public class Cliente {

    /**
     * Código do Cliente.
     */
    private long codigo;

    /**
     * Nome do Cliente.
     */
    private String nome;

    /**
     * Valor limite de compra do cliente.
     */
    private double valorLimiteCompra;

    /**
     * Data de fechamento da fatura do cliente.
     */
    private Date dataFechamentoFatura;

    public Cliente() {
    }

    public Cliente(String nome, double valorLimiteCompra,
            Date dataFechamentoFatura) {
        this.nome = nome;
        this.valorLimiteCompra = valorLimiteCompra;
        this.dataFechamentoFatura = dataFechamentoFatura;
    }

    public long getCodigo() {
        return codigo;
    }

    public void setCodigo(long codigo) {
        this.codigo = codigo;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public double getValorLimiteCompra() {
        return valorLimiteCompra;
    }

    public void setValorLimiteCompra(double valorLimiteCompra) {
        this.valorLimiteCompra = valorLimiteCompra;
    }

    public Date getDataFechamentoFatura() {
        return dataFechamentoFatura;
    }

    public void setDataFechamentoFatura(Date dataFechamentoFatura) {
        this.dataFechamentoFatura = dataFechamentoFatura;
    }
}