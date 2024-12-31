package com.mycompany.controlevenda.control;

import com.mycompany.controlevenda.dao.ClienteDAO;
import com.mycompany.controlevenda.model.Cliente;
import com.mycompany.controlevenda.model.Venda;
import com.mycompanyy.controlevenda.utilities.LimiteCreditoUtilities;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * Controller para a entidade {@link Cliente}.
 */
public class ClienteController implements EntityController<Cliente> {

    /**
     * DAO do Cliente.
     */
    private final ClienteDAO clienteDao = new ClienteDAO();

    /**
     * {@inheritDoc}.
     */
    @Override
    public List<Cliente> listaTodos() {
        return clienteDao.findAll();
    }

    /**
     * {@inheritDoc}.
     */
    @Override
    public Cliente retornaEntidadePeloCodigo(Long codigo) {
        return clienteDao.findByCodigo(codigo);
    }

    /**
     * {@inheritDoc}.
     */
    @Override
    public boolean cadastraEntidade(Cliente entidade) {
        return clienteDao.create(entidade);
    }

    /**
     * {@inheritDoc}.
     */
    @Override
    public boolean editarEntidade(Cliente entidade) {
        return clienteDao.edit(entidade);
    }

    /**
     * {@inheritDoc}.
     */
    @Override
    public boolean deletarEntidade(Long codigo) {
        return clienteDao.delete(codigo);
    }

    /**
     * Realiza a busca por Clientes que contenham o nome parecido com o
     * informado.
     *
     * @param nome Nome que está procurando
     *
     * @return Lista com os Clientes que possuam o nome parecido
     */
    public List<Cliente> retornaEntidadePorNomeParecido(String nome) {
        return clienteDao.retornaEntidadePorNomeParecido(nome);
    }

    /**
     * Processamento para conferir se o Cliente passou o valor do limite de
     * crédito definido em seu cadastro.
     *
     * @param cliente Cliente que será consultado.
     *
     * @return Limite de Crédito e próxima data de fechamento.
     */
    public LimiteCreditoUtilities validarLimiteCredito(Cliente cliente) {
        LimiteCreditoUtilities limiteCreditoUtilities = null;

        LocalDate dataFechamentoAtual = cliente.getDataFechamentoFatura();
        LocalDate hoje = LocalDate.now();

        LocalDate proximoFechamento
                = calcularProximoFechamento(dataFechamentoAtual, hoje);

        VendaController vendaController = new VendaController();

        List<Venda> vendas = vendaController
                .retornaVendasPeloCodigoCliente(cliente.getCodigo());

        BigDecimal totalCompras = calcularTotalComprasAposFechamento(
                vendas, dataFechamentoAtual);

        BigDecimal limiteCredito
                = BigDecimal.valueOf(cliente.getValorLimiteCompra());
        if (totalCompras.compareTo(limiteCredito) > 0) {
            BigDecimal valorDisponivel = limiteCredito.subtract(totalCompras);
            limiteCreditoUtilities = new LimiteCreditoUtilities(
                    valorDisponivel.abs(), proximoFechamento);
        }

        return limiteCreditoUtilities;
    }

    /**
     * Confere a data que será o próximo Fechamento da fatura.
     *
     * @param dataFechamento Data atual do fechamento.
     *
     * @param referencia Data para comparar a data de fechamento.
     *
     * @return A data do próximo fechamento.
     */
    private static LocalDate calcularProximoFechamento(LocalDate dataFechamento,
            LocalDate referencia) {
        LocalDate proximoFechamento = dataFechamento;

        while (!proximoFechamento.isAfter(referencia)) {
            proximoFechamento = proximoFechamento.plusMonths(1);
        }
        return proximoFechamento;
    }

    /**
     * Confere o valor total de compras, realizadas após a data do fechamento da
     * fatura.
     *
     * @param vendas As vendas realizadas após o fechamento da fatura.
     *
     * @param dataFechamentoAtual Data de fechamento configurada para o cliente.
     *
     * @return O valor total das compras realizadas pelo Cliente.
     */
    private static BigDecimal calcularTotalComprasAposFechamento(
            List<Venda> vendas, LocalDate dataFechamentoAtual) {
        return vendas.stream().filter(
                venda -> !venda.getDataVenda().isBefore(dataFechamentoAtual))
                .map(Venda::getValorTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
