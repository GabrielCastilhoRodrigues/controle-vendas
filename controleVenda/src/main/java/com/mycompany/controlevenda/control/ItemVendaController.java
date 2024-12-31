package com.mycompany.controlevenda.control;

import com.mycompany.controlevenda.dao.ItemVendaDAO;
import com.mycompany.controlevenda.model.ItemVenda;
import java.util.List;

/**
 * Controller para a entidade {@link ItemVenda}.
 *
 * @author gabri
 */
public class ItemVendaController implements EntityController<ItemVenda> {

    /**
     * DAO do ItemVenda.
     */
    private final ItemVendaDAO itemVendaDao = new ItemVendaDAO();

    /**
     * {@inheritDoc}.
     */
    @Override
    public List<ItemVenda> listaTodos() {
        return itemVendaDao.findAll();
    }

    /**
     * {@inheritDoc}.
     */
    @Override
    public ItemVenda retornaEntidadePeloCodigo(Long codigo) {
        return itemVendaDao.findByCodigo(codigo);
    }

    /**
     * {@inheritDoc}.
     */
    @Override
    public boolean cadastraEntidade(ItemVenda entidade) {
        return itemVendaDao.create(entidade);
    }

    /**
     * {@inheritDoc}.
     */
    @Override
    public boolean editarEntidade(ItemVenda entidade) {
        return itemVendaDao.edit(entidade);
    }

    /**
     * {@inheritDoc}.
     */
    @Override
    public boolean deletarEntidade(Long codigo) {
        return itemVendaDao.delete(codigo);
    }

    /**
     * Invoca a operação que localiza todos os registros de ItemVenda,
     * vinculados ao código da Venda informado.
     *
     * @param codigoVenda Código da Venda que deseja localizar os itens.
     *
     * @return Lista com todos os ItemVenda.
     */
    public List<ItemVenda> retornaEntidadePeloCodigoVenda(Long codigoVenda) {
        return itemVendaDao.findAllByCodigoVenda(codigoVenda);
    }

    /**
     * Deleta todos os itens que possuem o mesmo código de venda.
     *
     * @param codigoVenda O código de venda que deve ser consultado.
     *
     * @return <code>True</code> caso remova o registro. <code>False</code> caso
     * não consiga remover o registro.
     */
    public boolean deletaTodosItensPorCodigoVenda(Long codigoVenda) {
        return itemVendaDao.deleteAllByCodigoVenda(codigoVenda);
    }
}
