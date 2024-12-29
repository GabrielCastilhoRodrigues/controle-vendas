package com.mycompany.controlevenda.control;

import java.util.List;

/**
 * Interface com as operações padrões do Controller.
 *
 * @param <T> Representa um Controller.
 *
 * @author gabri
 */
public interface EntityController<T> {

    /**
     * Lista todos os registros da Entidade.
     *
     * @return Lista com todos os registros da Entidade.
     */
    public List<T> listaTodos();

    /**
     * Retorna um registro da Entidade, com base do código informado.
     *
     * @param codigo Código da Entidade.
     *
     * @return O registro localizado.
     */
    public T retornaEntidadePeloCodigo(Long codigo);

    /**
     * Realiza o cadastro da Entidade.
     *
     * @param entidade Entidade que deseja cadastrar.
     *
     * @return <code>True</code> caso realize o cadastro. <code>False</code>
     * caso não consiga realizar o cadastro.
     */
    public boolean cadastraEntidade(T entidade);

    /**
     * Edita a Entidade.
     *
     * @param entidade Entidade que deseja editar.
     *
     * @return <code>True</code> caso realize a edição. <code>False</code> caso
     * não consiga realizar a edição.
     */
    public boolean editarEntidade(T entidade);

    /**
     * Deleta a Entidade.
     *
     * @param codigo Código da Entidade que deseja deletar.
     *
     * @return <code>True</code> caso remova o registro. <code>False</code> caso
     * não consiga remover o registro.
     */
    public boolean deletarEntidade(Long codigo);
}
