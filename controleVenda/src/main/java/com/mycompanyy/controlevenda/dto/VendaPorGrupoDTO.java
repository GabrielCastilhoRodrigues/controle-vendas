package com.mycompanyy.controlevenda.dto;

import java.math.BigDecimal;

/**
 * DTO para a consulta de Venda por Grupos.
 *
 * @author gabri
 */
public class VendaPorGrupoDTO {

    /**
     * Nome.
     */
    private String nome;

    /**
     * Valor total de Vendas para o Cliente.
     */
    private BigDecimal valorTotalVenda;

    /**
     * Tipo de Consulta realizada
     */
    private char tipo;

    public VendaPorGrupoDTO(String nome, BigDecimal valorTotalVenda,
            char tipo) {
        this.nome = nome;
        this.valorTotalVenda = valorTotalVenda;
        this.tipo = tipo;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public BigDecimal getValorTotalVenda() {
        return valorTotalVenda;
    }

    public void setValorTotalVenda(BigDecimal valorTotalVenda) {
        this.valorTotalVenda = valorTotalVenda;
    }

    public char getTipo() {
        return tipo;
    }

    public void setTipo(char tipo) {
        this.tipo = tipo;
    }

    /**
     * Constante para consulta por Cliente.
     */
    public static final char CLIENTE = 'A';

    /**
     * Constante para consulta por Produto.
     */
    public static final char PRODUTO = 'B';
}
