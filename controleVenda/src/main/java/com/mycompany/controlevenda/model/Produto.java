package com.mycompany.controlevenda.model;

/**
 * Entidade Produto.
 *
 * @author gabri
 */
public class Produto {

    /**
     * Código do Produto.
     */
    private long codigo;

    /**
     * Descrição do Produto.
     */
    private String descricao;

    /**
     * Preço do Produto.
     */
    private double preco;

    public Produto(String descricao, double preco) {
        this.descricao = descricao;
        this.preco = preco;
    }

    public long getCodigo() {
        return codigo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public double getPreco() {
        return preco;
    }

    public void setPreco(double preco) {
        this.preco = preco;
    }
}
