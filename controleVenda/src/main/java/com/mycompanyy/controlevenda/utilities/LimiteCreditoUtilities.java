package com.mycompanyy.controlevenda.utilities;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 *
 * @author gabri
 */
public class LimiteCreditoUtilities {

    private BigDecimal valorDisponivel;

    private LocalDate dataProximoFechamento;

    public LimiteCreditoUtilities() {
    }

    public LimiteCreditoUtilities(BigDecimal valorDisponivel,
            LocalDate dataProximoFechamento) {
        this.valorDisponivel = valorDisponivel;
        this.dataProximoFechamento = dataProximoFechamento;
    }

    public BigDecimal getValorDisponivel() {
        return valorDisponivel;
    }

    public void setValorDisponivel(BigDecimal valorDisponivel) {
        this.valorDisponivel = valorDisponivel;
    }

    public LocalDate getDataProximoFechamento() {
        return dataProximoFechamento;
    }

    public void setDataProximoFechamento(LocalDate dataProximoFechamento) {
        this.dataProximoFechamento = dataProximoFechamento;
    }

}
