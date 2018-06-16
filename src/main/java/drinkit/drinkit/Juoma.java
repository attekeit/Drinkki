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
public class Juoma {
    
    private int id;
    private String nimi;
    
    public Juoma(int id, String nimi) {
        this.id = id;
        this.nimi = nimi;
    }
    
    public int getId(){
        return id;
    }
    
    public String getNimi(){
        return nimi;
    }
    
}
