package com.mycompany.controlevenda.control;

import com.mycompany.controlevenda.dao.ClienteDAO;
import com.mycompany.controlevenda.model.Cliente;
import java.util.List;

/**
 * Controller para a entidade {@link Cliente}.
 */
public class ClienteController implements EntityController<Cliente>{

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
}
