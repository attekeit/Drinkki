/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package drinkit.drinkit;

/**
 *
 * @author attekeit
 */
public class JuomaRaakaAine implements Comparable<JuomaRaakaAine>{
    
    
    private int juomaId;
    private RaakaAine raakaAine;
    private int jarjestysluku;
    private String maara;
    private String ohje;

    public JuomaRaakaAine(int juomaId, RaakaAine aine, int jarjestysluku, String maara, String ohje) {
        this.juomaId = juomaId;
        this.raakaAine = aine;
        this.jarjestysluku = jarjestysluku;
        this.maara = maara;
        this.ohje = ohje;
    }

    public int getJuomaId() {
        return this.juomaId;
    }

    public RaakaAine getRaakaAine() {
        return this.raakaAine;
    }
    
    public int getJarjestysluku(){
        return this.jarjestysluku;
    }
    
    public String getMaara(){
        return this.maara;
    }
    
    public String getOhje(){
        return this.ohje;
    }

    @Override
    public int compareTo(JuomaRaakaAine toinen) {
        return this.getJarjestysluku() - toinen.getJarjestysluku();
    }

}
