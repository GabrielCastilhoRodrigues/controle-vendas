package com.mycompany.controlevenda.control;

import com.mycompany.controlevenda.dao.VendaDAO;
import com.mycompany.controlevenda.model.ItemVenda;
import com.mycompany.controlevenda.model.Venda;
import java.util.List;

/**
 * Controller para a entidade {@link Venda}.
 *
 * @author gabri
 */
public class VendaController implements EntityController<Venda> {

    /**
     * DAO da Venda.
     */
    private final VendaDAO vendaDao = new VendaDAO();

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Venda> listaTodos() {
        return vendaDao.findAll();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Venda retornaEntidadePeloCodigo(Long codigo) {
        return vendaDao.findByCodigo(codigo);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean cadastraEntidade(Venda entidade) {
        if (vendaDao.create(entidade)) {
            ItemVendaController itemVendaController = new ItemVendaController();
            for (ItemVenda itemVenda : entidade.getItensVenda()) {
                itemVenda.setVenda(entidade);
                itemVendaController.cadastraEntidade(itemVenda);
            }

            return true;
        }

        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean editarEntidade(Venda entidade) {
        if (vendaDao.edit(entidade)) {
            ItemVendaController itemController = new ItemVendaController();

            if (itemController.deletaTodosItensPorCodigoVenda(
                    entidade.getCodigo())) {
                for (ItemVenda itemVenda : entidade.getItensVenda()) {
                    itemVenda.setVenda(entidade);
                    itemController.cadastraEntidade(itemVenda);
                }

                return true;
            }
        }

        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean deletarEntidade(Long codigo) {
        ItemVendaController itemController = new ItemVendaController();
        if (itemController.retornaEntidadePeloCodigoVenda(codigo).isEmpty()) {
            return vendaDao.delete(codigo);
        } else {
            if (itemController.deletaTodosItensPorCodigoVenda(codigo)) {
                return vendaDao.delete(codigo);
            }
        }

        return false;
    }

    /**
     * Retorna Lista contendo as Vendas que possuem o mesmo código do Cliente.
     *
     * @param codigoCliente Código do Cliente que será consultado.
     *
     * @return Lista contendo as Vendas que possuem o mesmo código do Cliente.
     */
    public List<Venda> retornaVendasPeloCodigoCliente(Long codigoCliente) {
        return vendaDao.findAllByCodigoCliente(codigoCliente);
    }

}
