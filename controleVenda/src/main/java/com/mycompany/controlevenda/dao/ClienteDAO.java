package com.mycompany.controlevenda.dao;

import com.mycompany.controlevenda.database.DatabaseConnection;
import com.mycompany.controlevenda.model.Cliente;
import java.util.List;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * DAO para as operações do Cliente.
 *
 * @author gabri
 */
public class ClienteDAO implements EntityDAO<Cliente> {

    /**
     * Conexão com o banco de dados.
     */
    private final DatabaseConnection dbConnection = new DatabaseConnection();

    /**
     * Nome da tabela Cliente.
     */
    private final String nomeTabela = Cliente.class.getSimpleName();

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
    public boolean create(Cliente entidade) {
        String insertSql = ""
                + " INSERT INTO " + nomeTabela
                + "     (nome, valorLimiteCompra, dataFechamentoFatura) "
                + " VALUES "
                + "     (?, ?, ?) ";

        Connection conexao = dbConnection.conexao();

        try {
            operacao = conexao.prepareStatement(insertSql);

            operacao.setString(1, entidade.getNome());
            operacao.setDouble(2, entidade.getValorLimiteCompra());
            operacao.setTimestamp(3, new Timestamp(
                    entidade.getDataFechamentoFatura().getTime()));

            operacao.executeUpdate();
            return true;

        } catch (SQLException e) {
            Logger.getLogger(ClienteDAO.class.getName())
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
    public boolean edit(Cliente entidade) {
        String editSql = ""
                + " UPDATE " + nomeTabela
                + "     SET "
                + "         nome = ?, "
                + "         valorLimiteCompra = ?, "
                + "         dataFechamentoFatura = ? "
                + " WHERE "
                + "    Codigo = ?  ";

        Connection conexao = dbConnection.conexao();

        try {
            operacao = conexao.prepareStatement(editSql);

            operacao.setString(1, entidade.getNome());
            operacao.setDouble(2, entidade.getValorLimiteCompra());
            operacao.setTimestamp(3, new Timestamp(
                    entidade.getDataFechamentoFatura().getTime()));
            operacao.setLong(4, entidade.getCodigo());

            operacao.executeUpdate();
            return true;

        } catch (SQLException e) {
            Logger.getLogger(ClienteDAO.class.getName())
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
            Logger.getLogger(ClienteDAO.class.getName())
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
    public List<Cliente> findAll() {
        Connection conexao = dbConnection.conexao();
        ResultSet resultSet = null;
        List<Cliente> clientes = new ArrayList<>();

        try {
            operacao = conexao.prepareStatement(SELECT_QUERY);
            resultSet = operacao.executeQuery();

            while (resultSet.next()) {
                Cliente cliente = new Cliente();
                cliente.setCodigo(resultSet.getInt("codigo"));
                cliente.setNome(resultSet.getString("nome"));
                cliente.setValorLimiteCompra(
                        resultSet.getDouble("valorLimiteCompra"));
                cliente.setDataFechamentoFatura(
                        resultSet.getDate("dataFechamentoFatura"));

                clientes.add(cliente);
            }
        } catch (SQLException e) {
            Logger.getLogger(ClienteDAO.class.getName())
                    .log(Level.SEVERE, null, e);
        } finally {
            DatabaseConnection.fecharConexao(conexao, operacao, resultSet);
        }

        return clientes;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Cliente findByCodigo(Long codigo) {
        Connection conexao = dbConnection.conexao();

        String consultaSQL = SELECT_QUERY + " where codigo = ?";
        ResultSet resultSet = null;
        Cliente cliente = null;

        try {
            operacao = conexao.prepareStatement(consultaSQL);

            operacao.setLong(1, codigo);
            resultSet = operacao.executeQuery();

            if (resultSet.next()) {
                cliente = new Cliente();

                cliente.setCodigo(resultSet.getInt("codigo"));
                cliente.setNome(resultSet.getString("nome"));
                cliente.setValorLimiteCompra(
                        resultSet.getDouble("valorLimiteCompra"));
                cliente.setDataFechamentoFatura(
                        resultSet.getDate("dataFechamentoFatura"));
            }
        } catch (SQLException e) {
            Logger.getLogger(ClienteDAO.class.getName())
                    .log(Level.SEVERE, null, e);
        } finally {
            DatabaseConnection.fecharConexao(conexao, operacao, resultSet);
        }

        return cliente;
    }
}
