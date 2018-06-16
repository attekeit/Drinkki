/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package drinkit.drinkit.dao;

import drinkit.drinkit.RaakaAine;
import drinkit.drinkit.database.Database;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author attekeit
 */
public class RaakaAineDao implements Dao<RaakaAine, Integer> {

    private Database database;

    public RaakaAineDao(Database database) {
        this.database = database;
    }

    @Override
    public RaakaAine findOne(Integer key) throws SQLException {
        try (Connection connection = this.database.getConnection()) {
            PreparedStatement statement = connection.prepareStatement("SELECT (nimi) FROM RaakaAine "
                    + "WHERE id = ?");
            statement.setInt(1, key);
            ResultSet result = statement.executeQuery();
            if (result.next()) {
                return new RaakaAine(key, result.getString("nimi"));
            }
            
        };
        return null;

    }

    @Override
    public List<RaakaAine> findAll() throws SQLException {
        //Etsitään kaikki tietokannan raaka-aineet.
        List<RaakaAine> ainekset = new ArrayList<>();
        try (Connection connection = this.database.getConnection()) {
            PreparedStatement statement = connection.prepareStatement("SELECT id, nimi "
                    + "FROM RaakaAine");
            ResultSet set = statement.executeQuery();
            while (set.next()) {
                ainekset.add(new RaakaAine(set.getInt("id"), set.getString("nimi")));
            }
           
        };

        return ainekset;
    }

    @Override
    public RaakaAine saveOrUpdate(RaakaAine object) throws SQLException {
        //Sovelluksessa ei raaka-aineita voida päivittää. Vain lisäys onnistuu.
        try (Connection connection = this.database.getConnection()) {
            PreparedStatement statement = connection.prepareStatement("INSERT INTO RaakaAine (nimi) "
                    + "VALUES (?)");
            statement.setString(1, object.getNimi());
            statement.executeUpdate();
            
        };
        return null;

    }

    public RaakaAine findByName(String nimi) throws SQLException {
        Connection connection = database.getConnection();
        PreparedStatement statement = connection.prepareStatement("SELECT id, nimi FROM RaakaAine WHERE nimi = ?");
        statement.setString(1, nimi);

        ResultSet result = statement.executeQuery();
        if (!result.next()) {
            return null;
        }

        return new RaakaAine(result.getInt("id"), result.getString("nimi"));

    }

    @Override
    public void delete(Integer key) throws SQLException {
        try (Connection connection = this.database.getConnection()) {
            PreparedStatement statement = connection.prepareStatement("DELETE FROM RaakaAine "
                    + "WHERE id = ?");
            statement.setInt(1, key);
            statement.executeUpdate();

        };

    }
    

}
