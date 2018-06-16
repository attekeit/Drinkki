/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package drinkit.drinkit.dao;

import drinkit.drinkit.JuomaRaakaAine;
import drinkit.drinkit.RaakaAine;
import drinkit.drinkit.database.Database;
import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author attekeit
 */
public class JuomaRaakaAineDao implements Dao<JuomaRaakaAine, Integer> {

    private Database database;

    public JuomaRaakaAineDao(Database database) {
        this.database = database;
    }

    @Override
    public JuomaRaakaAine findOne(Integer key) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<JuomaRaakaAine> findAll() throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public JuomaRaakaAine saveOrUpdate(JuomaRaakaAine object) throws SQLException {
        // JuomaRaakaAinetta voidaan vain tallentaa, ei päivittää.
        try (Connection connection = this.database.getConnection()) {
            PreparedStatement statement = connection.prepareStatement("INSERT INTO JuomaRaakaaine (juoma_id, raakaaine_id, jarjestys, maara, ohje) VALUES (?, ?, ?, ?, ?)");
            statement.setInt(1, object.getJuomaId());
            statement.setInt(2, object.getRaakaAine().getId());
            statement.setInt(3, object.getJarjestysluku());
            statement.setString(4, object.getMaara());
            statement.setString(5, object.getOhje());
            statement.executeUpdate();
        };

        return object;
    }

    @Override
    public void delete(Integer key) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public List<JuomaRaakaAine> findAllWithJuomaId(int juomaId) throws SQLException {
        List<JuomaRaakaAine> ohjeet = new ArrayList<>();
        try (Connection connection = this.database.getConnection()) {
            PreparedStatement statement = connection.prepareStatement("SELECT "
                    + "raakaaine_id, jarjestys, "
                    + "maara, ohje, nimi "
                    + "FROM JuomaRaakaAine, RaakaAine "
                    + "WHERE JuomaRaakaAine.raakaaine_id = RaakaAine.id "
                    + "AND juoma_id = (?)");
            statement.setInt(1, juomaId);
            ResultSet result = statement.executeQuery();

            while (result.next()) {
                ohjeet.add(new JuomaRaakaAine(juomaId,
                        new RaakaAine(result.getInt("raakaaine_id"), result.getString("nimi")),
                        result.getInt("jarjestys"),
                        result.getString("maara"), result.getString("ohje")));
            }

            Collections.sort(ohjeet); //laitetaan lista järjestykseen järjestysluvun mukaan.

        };

        return ohjeet;
    }
    
    public void deleteByJuomaId(int juomaId)throws SQLException {
        try (Connection connection = this.database.getConnection()) {
            PreparedStatement statement = connection.prepareStatement("DELETE FROM "
                    + "JuomaRaakaAine WHERE juoma_id = ?");
            statement.setInt(1, juomaId);
            statement.executeUpdate();
            
        }
    }
    
    public void deleteByRaakaAineId(int ainesId)throws SQLException {
        try (Connection connection = this.database.getConnection()) {
            PreparedStatement statement = connection.prepareStatement("DELETE FROM "
                    + "JuomaRaakaAine WHERE raakaaine_id = ?");
            statement.setInt(1, ainesId);
            statement.executeUpdate();
            
        }
    }
    
    
    public int getEsiintymiskerrat(int raakaAineId) throws SQLException{
        try (Connection connection = this.database.getConnection()) {
            PreparedStatement statement = connection.prepareStatement("SELECT COUNT(*) AS lkm "
                    + "FROM JuomaRaakaAine "
                    + "WHERE raakaaine_id = ?");
            statement.setInt(1, raakaAineId);
            ResultSet result = statement.executeQuery();
            int kerrat = 0;
            while (result.next()) { //vain yksi tulos
                kerrat = result.getInt("lkm");
            }
            
            return kerrat;
        }
    }

}
