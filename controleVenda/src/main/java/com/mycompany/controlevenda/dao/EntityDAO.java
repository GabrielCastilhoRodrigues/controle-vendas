package com.mycompany.controlevenda.dao;

import java.util.List;

/**
 * Interface para as operações com banco de dados das entidades.
 *
 * @author gabri
 */
public interface EntityDAO<T> {

    /**
     * Realiza o cadastro da Entidade.
     *
     * @param entidade Entidade que deseja cadastrar
     *
     * @return <code>True</code> caso realize o cadastro. <code>False</code>
     * caso não consiga realizar o cadastro.
     */
    public boolean create(T entidade);

    /**
     * Edita o Entidade.
     *
     * @param entidade Entidade que deseja editar.
     *
     * @return <code>True</code> caso realize a edição. <code>False</code> caso
     * não consiga realizar a edição.
     */
    public boolean edit(T entidade);

    /**
     * Deleta a Entidade.
     *
     * @param codigo Código da Entidade que deseja deletar.
     *
     * @return <code>True</code> caso remova o registro. <code>False</code> caso
     * não consiga remover o registro.
     */
    public boolean delete(Long codigo);

    /**
     * Lista todos os registros cadastrados da Entidade.
     *
     * @return Lista com todos os registros cadastrados da Entidade.
     */
    public List<T> findAll();

    /**
     * Retorna um registro da Entidade, com base do código informado.
     *
     * @param codigo Código da Entidade.
     *
     * @return O registro da Entidade localizado.
     */
    public T findByCodigo(Long codigo);
}
