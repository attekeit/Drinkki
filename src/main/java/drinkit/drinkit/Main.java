/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package drinkit.drinkit;

import drinkit.drinkit.dao.JuomaDao;
import drinkit.drinkit.dao.JuomaRaakaAineDao;
import drinkit.drinkit.dao.RaakaAineDao;
import drinkit.drinkit.database.Database;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import spark.Spark;
import spark.ModelAndView;
import spark.template.thymeleaf.ThymeleafTemplateEngine;

/**
 *
 * @author attekeit
 */
public class Main {

    public static void main(String[] args) throws Exception {

        Database database = new Database("jdbc:sqlite:juomat.db");
        Connection connection = database.getConnection();

        JuomaDao juomadao = new JuomaDao(database);
        RaakaAineDao raakaainedao = new RaakaAineDao(database);
        JuomaRaakaAineDao juomaraakaainedao = new JuomaRaakaAineDao(database);

        Spark.get("/", (req, res) -> {
            HashMap map = new HashMap<>();
            map.put("drinkit", juomadao.findAll());

            return new ModelAndView(map, "index");
        }, new ThymeleafTemplateEngine());

        Spark.get("/ainekset/", (req, res) -> {
            HashMap map = new HashMap<>();
            map.put("ainekset", raakaainedao.findAll());

            return new ModelAndView(map, "ainekset");
        }, new ThymeleafTemplateEngine());

        Spark.post("/ainekset/", (req, res) -> {
            RaakaAine aine = new RaakaAine(-1, req.queryParams("ainesosa"));
            raakaainedao.saveOrUpdate(aine);

            res.redirect("/ainekset/");
            return "";
        });
        
        Spark.get("/ainekset/:id", (req, res) -> {
            HashMap map = new HashMap<>();
            int id = Integer.parseInt(req.params(":id"));
            map.put("aines", raakaainedao.findOne(id));
            map.put("kerrat", juomaraakaainedao.getEsiintymiskerrat(id));
            return new ModelAndView(map, "aines");
        }, new ThymeleafTemplateEngine());

        Spark.post("/ainekset/poista/:id", (req, res) -> {
            int id = Integer.parseInt(req.params(":id"));
            //poistetaan kaikki ohjeet, missä on raaka-ainetta sekä raaka-aine
            juomaraakaainedao.deleteByRaakaAineId(id);
            raakaainedao.delete(id);
            res.redirect("/ainekset/");
            return "";
        });

        Spark.get("/drinkit/", (req, res) -> {
            HashMap map = new HashMap<>();
            map.put("drinkit", juomadao.findAll());

            return new ModelAndView(map, "drinkit");
        }, new ThymeleafTemplateEngine());

        Spark.post("/drinkit/", (req, res) -> {
            Juoma drinkki = new Juoma(-1, req.queryParams("drinkki"));

            juomadao.saveOrUpdate(drinkki);
            res.redirect("/drinkit/");
            return "";
        });

        Spark.post("/drinkit/poista/:id", (req, res) -> {
            int id = Integer.parseInt(req.params(":id"));
            //poistetaan kaikki juomaan liittyvä tieto
            juomaraakaainedao.deleteByJuomaId(id);
            juomadao.delete(id);
            res.redirect("/drinkit/");
            return "";
        });

        Spark.get("/drinkit/:id", (req, res) -> {
            HashMap map = new HashMap<>();

            int id = Integer.parseInt(req.params(":id"));
            map.put("drinkki", juomadao.findOne(id));
            map.put("ainekset", raakaainedao.findAll());
            map.put("reseptit", juomaraakaainedao.findAllWithJuomaId(id));

            return new ModelAndView(map, "drinkki");
        }, new ThymeleafTemplateEngine());

        Spark.post("/drinkit/:id", (req, res) -> {
            // Varmistetaan, että syötetty järjestysluku oj kokonaisluku. Jos
            // ei ole, ohjataan käyttäjä eri osoitteeseen.
            HashMap map = new HashMap<>();
            if (isInteger(req.params(":id")) && isInteger(req.queryParams("luku"))) {
                int id = Integer.parseInt(req.params(":id"));
                String ohje = req.queryParams("ohje");
                int luku = Integer.parseInt(req.queryParams("luku"));
                int raakaaineid = Integer.parseInt(req.queryParams("nimi"));
                String maara = req.queryParams("maara");
                JuomaRaakaAine reseptiosa = new JuomaRaakaAine(id, raakaainedao.findOne(raakaaineid),
                        luku, maara, ohje);
                //Yhteen juomaan voi lisätä vain yhden kerran kutakin raaka-ainetta.
                //Jos koitetaan lisätä toisen kerran, ohjataan käyttäjä eri osoitteeseen.
                //Myöskään samalla järjestysluvulla ei voida lisätä uutta JuomaRaakaAinetta
                try  {
                    juomaraakaainedao.saveOrUpdate(reseptiosa);
                    res.redirect("/drinkit/" + req.params(":id"));
                } catch (Exception e) {
                    res.redirect("/virhe");
                }

                
            } else {
                res.redirect("/virhe");
            }
            return "";
        });
        
        Spark.get("virhe", (req, res) -> {
            HashMap map = new HashMap<>();
            return new ModelAndView(map, "virhe");
        }, new ThymeleafTemplateEngine());

    }

    public static boolean isInteger(String jono) {
        //Tämän avulla kokeillaan, ovatko syötetyt merkkijonot kokonaislukuja
        try {
            Integer.parseInt(jono);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

}
