package com.mycompany.controlevenda.control;

import com.mycompany.controlevenda.dao.ProdutoDAO;
import com.mycompany.controlevenda.model.Produto;
import java.util.List;

/**
 * Controller para a entidade {@link Produto}.
 */
public class ProdutoController implements EntityController<Produto> {

    /**
     * DAO do Produto.
     */
    private final ProdutoDAO produtoDao = new ProdutoDAO();

    /**
     * {@inheritDoc}.
     */
    @Override
    public List<Produto> listaTodos() {
        return produtoDao.findAll();
    }

    /**
     * {@inheritDoc}.
     */
    @Override
    public Produto retornaEntidadePeloCodigo(Long codigo) {
        return produtoDao.findByCodigo(codigo);
    }

    /**
     * {@inheritDoc}.
     */
    @Override
    public boolean cadastraEntidade(Produto entidade) {
        return produtoDao.create(entidade);
    }

    /**
     * {@inheritDoc}.
     */
    @Override
    public boolean editarEntidade(Produto entidade) {
        return produtoDao.edit(entidade);
    }

    /**
     * {@inheritDoc}.
     */
    @Override
    public boolean deletarEntidade(Long codigo) {
        return produtoDao.delete(codigo);
    }

    /**
     * Realiza a busca por Produtos que contenham uma descrição parecida com o
     * informado.
     *
     * @param descricao Descrição que está procurando.
     *
     * @return Lista com os Produtos que possuam a descrição parecida.
     */
    public List<Produto> retornaEntidadePorNomeParecido(String descricao) {
        return produtoDao.retornaEntidadePorNomeParecido(descricao);
    }
}
