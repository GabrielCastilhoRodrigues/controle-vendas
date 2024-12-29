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
 * @author gabri
 */
public class ClienteDAO {

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
    PreparedStatement operacao = null;

    /**
     * Realiza o cadastro do Cliente.
     *
     * @param cliente Cliente que deseja cadastrar
     *
     * @return <code>True</code> caso realize o cadastro. <code>False</code>
     * caso não consiga realizar o cadastro.
     */
    public boolean create(Cliente cliente) {
        String insertSql = ""
                + " INSERT INTO " + nomeTabela
                + "     (nome, valorLimiteCompra, dataFechamentoFatura) "
                + " VALUES "
                + "     (?, ?, ?) ";

        Connection conexao = dbConnection.conexao();

        try {
            operacao = conexao.prepareStatement(insertSql);

            operacao.setString(1, cliente.getNome());
            operacao.setDouble(2, cliente.getValorLimiteCompra());
            operacao.setTimestamp(3,
                    new Timestamp(cliente.getDataFechamentoFatura().getTime()));

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
     * Edita o Cliente.
     *
     * @param cliente Cliente que deseja editar.
     *
     * @return <code>True</code> caso realize a edição. <code>False</code> caso
     * não consiga realizar a edição.
     */
    public boolean edit(Cliente cliente) {
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

            operacao.setString(1, cliente.getNome());
            operacao.setDouble(2, cliente.getValorLimiteCompra());
            operacao.setTimestamp(3,
                    new Timestamp(cliente.getDataFechamentoFatura().getTime()));
            operacao.setLong(4, cliente.getCodigo());

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
     * Deleta o Cliente.
     *
     * @param codigo Código do Cliente que deseja deletar.
     *
     * @return <code>True</code> caso remova o registro. <code>False</code> caso
     * não consiga remover o registro.
     */
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
     * Lista todos os clientes cadastrados.
     *
     * @return Lista com todos os clientes cadastrados.
     */
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
     * Retorna um Cliente com base do código informado.
     *
     * @param codigo Código do Cliente.
     *
     * @return O Cliente localizado.
     */
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
