package com.mycompany.controlevenda.model;

import java.math.BigDecimal;

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
    private BigDecimal preco;

    public Produto() {
    }

    public Produto(String descricao, BigDecimal preco) {
        this.descricao = descricao;
        this.preco = preco;
    }

    public long getCodigo() {
        return codigo;
    }

    public void setCodigo(long codigo) {
        this.codigo = codigo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public BigDecimal getPreco() {
        return preco;
    }

    public void setPreco(BigDecimal preco) {
        this.preco = preco;
    }
}
