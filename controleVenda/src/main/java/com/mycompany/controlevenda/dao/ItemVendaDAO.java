package com.mycompany.controlevenda.dao;

import com.mycompany.controlevenda.control.ProdutoController;
import com.mycompany.controlevenda.control.VendaController;
import com.mycompany.controlevenda.database.DatabaseConnection;
import com.mycompany.controlevenda.model.ItemVenda;
import com.mycompany.controlevenda.model.Produto;
import com.mycompany.controlevenda.model.Venda;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author gabri
 */
public class ItemVendaDAO implements EntityDAO<ItemVenda> {

    /**
     * Conexão com o banco de dados.
     */
    private final DatabaseConnection dbConnection = new DatabaseConnection();

    /**
     * Nome da tabela Cliente.
     */
    private final String nomeTabela = ItemVenda.class.getSimpleName();

    /**
     * Query padrão para consulta todas as colunas da tabela.
     */
    private final String SELECT_QUERY = "SELECT * FROM " + nomeTabela;

    /**
     * PreparedStatement para as operações no banco de dados.
     */
    private PreparedStatement operacao = null;

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean create(ItemVenda entidade) {
        String insertSql = ""
                + " INSERT INTO " + nomeTabela
                + "     (codigoVenda, codigoProduto, quantidade, totalItem) "
                + " VALUES "
                + "     (?, ?, ?, ?) ";

        Connection conexao = dbConnection.conexao();

        try {
            operacao = conexao.prepareStatement(insertSql);

            operacao.setLong(1, entidade.getVenda().getCodigo());
            operacao.setLong(2, entidade.getProduto().getCodigo());
            operacao.setBigDecimal(3, entidade.getQuantidade());
            operacao.setBigDecimal(4, entidade.getTotalItem());

            operacao.executeUpdate();
            return true;

        } catch (SQLException e) {
            Logger.getLogger(ItemVendaDAO.class.getName())
                    .log(Level.SEVERE, null, e);
        } finally {
            DatabaseConnection.fecharConexao(conexao, operacao);
        }

        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean edit(ItemVenda entidade) {
        String editSql = ""
                + " UPDATE " + nomeTabela
                + "     SET "
                + "         codigoProduto = ?, "
                + "         quantidade = ?, "
                + "         totalItem  = ? "
                + " WHERE "
                + "    Codigo = ?  ";

        Connection conexao = dbConnection.conexao();

        try {
            operacao = conexao.prepareStatement(editSql);

            operacao.setLong(1, entidade.getProduto().getCodigo());
            operacao.setBigDecimal(2, entidade.getQuantidade());
            operacao.setBigDecimal(3, entidade.getTotalItem());
            operacao.setLong(4, entidade.getCodigo());

            operacao.executeUpdate();
            return true;

        } catch (SQLException e) {
            Logger.getLogger(ItemVendaDAO.class.getName())
                    .log(Level.SEVERE, null, e);
        } finally {
            DatabaseConnection.fecharConexao(conexao, operacao);
        }

        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean delete(Long codigo) {
        String deleteSql = ""
                + " DELETE FROM " + nomeTabela
                + " WHERE codigo = ? ";

        Connection conexao = dbConnection.conexao();

        try {
            operacao = conexao.prepareStatement(deleteSql);
            operacao.setLong(1, codigo);

            operacao.executeUpdate();
            return true;
        } catch (SQLException e) {
            Logger.getLogger(ItemVendaDAO.class.getName())
                    .log(Level.SEVERE, null, e);
        } finally {
            DatabaseConnection.fecharConexao(conexao, operacao);
        }

        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<ItemVenda> findAll() {
        Connection conexao = dbConnection.conexao();
        ResultSet resultSet = null;
        List<ItemVenda> itensVenda = new ArrayList<>();

        try {
            operacao = conexao.prepareStatement(SELECT_QUERY);
            resultSet = operacao.executeQuery();

            while (resultSet.next()) {
                ItemVenda itemVenda = new ItemVenda();

                itemVenda.setCodigo(resultSet.getInt("codigo"));
                itemVenda.setVenda(consultaVenda(
                        resultSet.getLong("codigoVenda")));
                itemVenda.setProduto(
                        consultaProduto(resultSet.getLong("codigoProduto")));
                itemVenda.setQuantidade(resultSet.getBigDecimal("quantidade"));
                itemVenda.setTotalItem(resultSet.getBigDecimal("totalItem"));

                itensVenda.add(itemVenda);
            }
        } catch (SQLException e) {
            Logger.getLogger(ItemVendaDAO.class.getName())
                    .log(Level.SEVERE, null, e);
        } finally {
            DatabaseConnection.fecharConexao(conexao, operacao, resultSet);
        }

        return itensVenda;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ItemVenda findByCodigo(Long codigo) {
        Connection conexao = dbConnection.conexao();

        String consultaSQL = SELECT_QUERY + " where codigo = ?";
        ResultSet resultSet = null;
        ItemVenda itemVenda = null;

        try {
            operacao = conexao.prepareStatement(consultaSQL);

            operacao.setLong(1, codigo);
            resultSet = operacao.executeQuery();

            while (resultSet.next()) {
                itemVenda.setCodigo(resultSet.getInt("codigo"));
                itemVenda.setVenda(consultaVenda(
                        resultSet.getLong("codigoVenda")));
                itemVenda.setProduto(
                        consultaProduto(resultSet.getLong("codigoProduto")));
                itemVenda.setQuantidade(resultSet.getBigDecimal("quantidade"));
                itemVenda.setTotalItem(resultSet.getBigDecimal("totalItem"));
            }
        } catch (SQLException e) {
            Logger.getLogger(ItemVendaDAO.class.getName())
                    .log(Level.SEVERE, null, e);
        } finally {
            DatabaseConnection.fecharConexao(conexao, operacao, resultSet);
        }

        return itemVenda;
    }

    /**
     * Localiza todos os ItemVenda, vinculados ao codigoVenda.
     *
     * @param codigoVenda Código da Venda que deseja localizar os itens.
     *
     * @return Lista com todos os ItemVenda.
     */
    public List<ItemVenda> findAllByCodigoVenda(Long codigoVenda) {
        Connection conexao = dbConnection.conexao();

        String consultaSQL = SELECT_QUERY + " where codigoVenda = ?";
        ResultSet resultSet = null;
        ItemVenda itemVenda = null;
        List<ItemVenda> itensVenda = new ArrayList<>();

        try {
            operacao = conexao.prepareStatement(consultaSQL);

            operacao.setLong(1, codigoVenda);
            resultSet = operacao.executeQuery();

            while (resultSet.next()) {
                itemVenda = new ItemVenda();
                itemVenda.setCodigo(resultSet.getInt("codigo"));
                itemVenda.setVenda(consultaVenda(codigoVenda));
                itemVenda.setProduto(
                        consultaProduto(resultSet.getLong("codigoProduto")));
                itemVenda.setQuantidade(resultSet.getBigDecimal("quantidade"));
                itemVenda.setTotalItem(resultSet.getBigDecimal("totalItem"));

                itensVenda.add(itemVenda);
            }
        } catch (SQLException e) {
            Logger.getLogger(ItemVendaDAO.class.getName())
                    .log(Level.SEVERE, null, e);
        } finally {
            DatabaseConnection.fecharConexao(conexao, operacao, resultSet);
        }

        return itensVenda;
    }

    /**
     * Consulta o Produto com base do código informado.
     *
     * @param codigoProduto O código do Produto.
     *
     * @return O Produto localizado.
     */
    private Produto consultaProduto(Long codigoProduto) {
        ProdutoController produtoController = new ProdutoController();
        return produtoController
                .retornaEntidadePeloCodigo(codigoProduto);
    }

    /**
     * Consulta a Venda com base do código informado.
     *
     * @param codigoProduto A código da Venda.
     *
     * @return A Venda localizado.
     */
    private Venda consultaVenda(Long codigoVenda) {
        VendaController vendaController = new VendaController();
        return vendaController.retornaEntidadePeloCodigo(codigoVenda);
    }

    /**
     * Deleta todos os Itens de Venda, vinculados ao código de Venda.
     *
     * @param codigoVenda Código da Venda que vai deletar os itens.
     *
     * @return <code>True</code> caso remova o registro. <code>False</code> caso
     * não consiga remover o registro.
     */
    public boolean deleteAllByCodigoVenda(Long codigoVenda) {
        String deleteSql = ""
                + " DELETE FROM " + nomeTabela
                + " WHERE codigoVenda = ? ";

        Connection conexao = dbConnection.conexao();

        try {
            operacao = conexao.prepareStatement(deleteSql);
            operacao.setLong(1, codigoVenda);

            operacao.executeUpdate();
            return true;
        } catch (SQLException e) {
            Logger.getLogger(ItemVendaDAO.class.getName())
                    .log(Level.SEVERE, null, e);
        } finally {
            DatabaseConnection.fecharConexao(conexao, operacao);
        }

        return false;
    }
}
