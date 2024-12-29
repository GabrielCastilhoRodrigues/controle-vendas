package com.mycompany.controlevenda.control;

import com.mycompany.controlevenda.dao.ClienteDAO;
import com.mycompany.controlevenda.model.Cliente;
import java.util.List;

/**
 * Controller para a entidade {@link Cliente}.
 */
public class ClienteController {

    /**
     * DAO do Cliente.
     */
    private final ClienteDAO clienteDao = new ClienteDAO();

    /**
     * Lista todos os clientes cadastrados.
     *
     * @return Lista com todos os clientes cadastrados.
     */
    public List<Cliente> listaClientes() {
        return clienteDao.findAll();
    }

    /**
     * Retorna um Cliente com base do código informado.
     *
     * @param codigo Código do Cliente.
     *
     * @return O Cliente localizado.
     */
    public Cliente retornaClientePeloCodigo(Long codigo) {
        return clienteDao.findByCodigo(codigo);
    }

    /**
     * Realiza o cadastro do Cliente.
     *
     * @param cliente Cliente que deseja cadastrar
     *
     * @return <code>True</code> caso realize o cadastro. <code>False</code>
     * caso não consiga realizar o cadastro.
     */
    public boolean cadastraCliente(Cliente cliente) {
        return clienteDao.create(cliente);
    }

    /**
     * Edita o Cliente.
     *
     * @param cliente Cliente que deseja editar.
     *
     * @return <code>True</code> caso realize a edição. <code>False</code> caso
     * não consiga realizar a edição.
     */
    public boolean editarCliente(Cliente cliente) {
        return clienteDao.edit(cliente);
    }

    /**
     * Deleta o Cliente.
     *
     * @param codigo Código do Cliente que deseja deletar.
     *
     * @return <code>True</code> caso remova o registro. <code>False</code> caso
     * não consiga remover o registro.
     */
    public boolean deleteCliente(Long codigo) {
        return clienteDao.delete(codigo);
    }
}
