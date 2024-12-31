package com.mycompany.controlevenda.dao;

import com.mycompany.controlevenda.database.DatabaseConnection;
import com.mycompany.controlevenda.model.Produto;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * DAO para as operações do Produto.
 *
 * @author gabri
 */
public class ProdutoDAO implements EntityDAO<Produto> {

    /**
     * Conexão com o banco de dados.
     */
    private final DatabaseConnection dbConnection = new DatabaseConnection();

    /**
     * Nome da tabela Produto.
     */
    private final String nomeTabela = Produto.class.getSimpleName();

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
    public boolean create(Produto entidade) {
        String insertSql = ""
                + " INSERT INTO " + nomeTabela
                + "     (descricao, preco) "
                + " VALUES "
                + "     (?, ?) ";

        Connection conexao = dbConnection.conexao();

        try {
            operacao = conexao.prepareStatement(insertSql);

            operacao.setString(1, entidade.getDescricao());
            operacao.setBigDecimal(2, entidade.getPreco());

            operacao.executeUpdate();
            return true;

        } catch (SQLException e) {
            Logger.getLogger(ProdutoDAO.class.getName())
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
    public boolean edit(Produto entidade) {
        String editSql = ""
                + " UPDATE " + nomeTabela
                + "     SET "
                + "         descricao = ?, "
                + "         preco = ? "
                + " WHERE "
                + "    Codigo = ?  ";

        Connection conexao = dbConnection.conexao();

        try {
            operacao = conexao.prepareStatement(editSql);

            operacao.setString(1, entidade.getDescricao());
            operacao.setBigDecimal(2, entidade.getPreco());
            operacao.setLong(3, entidade.getCodigo());

            operacao.executeUpdate();
            return true;

        } catch (SQLException e) {
            Logger.getLogger(ProdutoDAO.class.getName())
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
            Logger.getLogger(ProdutoDAO.class.getName())
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
    public List<Produto> findAll() {
        Connection conexao = dbConnection.conexao();
        ResultSet resultSet = null;
        List<Produto> produtos = new ArrayList<>();

        try {
            operacao = conexao.prepareStatement(SELECT_QUERY);
            resultSet = operacao.executeQuery();

            while (resultSet.next()) {
                Produto produto = new Produto();
                produto.setCodigo(resultSet.getInt("codigo"));
                produto.setDescricao(resultSet.getString("descricao"));
                produto.setPreco(
                        resultSet.getBigDecimal("preco"));

                produtos.add(produto);
            }
        } catch (SQLException e) {
            Logger.getLogger(ProdutoDAO.class.getName())
                    .log(Level.SEVERE, null, e);
        } finally {
            DatabaseConnection.fecharConexao(conexao, operacao, resultSet);
        }

        return produtos;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Produto findByCodigo(Long codigo) {
        Connection conexao = dbConnection.conexao();

        String consultaSQL = SELECT_QUERY + " where codigo = ?";
        ResultSet resultSet = null;
        Produto produto = null;

        try {
            operacao = conexao.prepareStatement(consultaSQL);

            operacao.setLong(1, codigo);
            resultSet = operacao.executeQuery();

            if (resultSet.next()) {
                produto = new Produto();
                produto.setCodigo(resultSet.getInt("codigo"));
                produto.setDescricao(resultSet.getString("descricao"));
                produto.setPreco(
                        resultSet.getBigDecimal("preco"));
            }
        } catch (SQLException e) {
            Logger.getLogger(ProdutoDAO.class.getName())
                    .log(Level.SEVERE, null, e);
        } finally {
            DatabaseConnection.fecharConexao(conexao, operacao, resultSet);
        }

        return produto;
    }

    /**
     * Busca por Clientes que contenham o nome parecido com o informado.
     *
     * @param descricao Nome que está procurando
     *
     * @return Lista com os Clientes que possuam o nome parecido
     */
    public List<Produto> retornaEntidadePorNomeParecido(String descricao) {
        Connection conexao = dbConnection.conexao();

        String consultaSQL = SELECT_QUERY
                + " where UPPER(descricao) like UPPER('%" + descricao + "%')";
        ResultSet resultSet = null;
        List<Produto> produtos = new ArrayList<>();

        try {
            operacao = conexao.prepareStatement(consultaSQL);
            resultSet = operacao.executeQuery();

            while (resultSet.next()) {
                Produto produto = new Produto();
                produto.setCodigo(resultSet.getInt("codigo"));
                produto.setDescricao(resultSet.getString("descricao"));
                produto.setPreco(
                        resultSet.getBigDecimal("preco"));

                produtos.add(produto);
            }
        } catch (SQLException e) {
            Logger.getLogger(ClienteDAO.class.getName())
                    .log(Level.SEVERE, null, e);
        } finally {
            DatabaseConnection.fecharConexao(conexao, operacao, resultSet);
        }

        return produtos;
    }
}
