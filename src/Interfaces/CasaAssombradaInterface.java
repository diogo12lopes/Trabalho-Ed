/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Interfaces;

import Exceptions.EmptyCollectionException;
import Jogo.Map;
import java.io.IOException;
import org.json.simple.parser.ParseException;

/**
 *
 * @author Diogo Lopes 8180121
 */
public interface CasaAssombradaInterface {

    /**
     * Méthod responsible for the gameplay.
     * @throws java.io.IOException Exception
     * @throws org.json.simple.parser.ParseException Exception
     * @throws Exceptions.EmptyCollectionException Exception
     */
    public void play()throws IOException, ParseException,EmptyCollectionException;

    /**
     *
     * Method for selecting the gamemode to play.
     *
     * @param Gamemode mode to be played on
     */
    public void Select_Game_Mode(String Gamemode);

    /**
     * @param MapChoice Integer that represents the map loaded into the sistem.
     *
     * Method for selecting the map wich to play.
     */
    public void Select_Map(int MapChoice);

    /**
     * @param DifficultyChoice integer that represents the difficulty
     * settings(1-3).
     *
     * Method for selecting the games difficulty.
     */
    public void Select_Difficulty(int DifficultyChoice);

    /**
     *
     * Method to check the leaderboard of the 15 players with the best scores.
     *
     * @param Map Map chosen 
     */
    public void CheckLeaderboard(String Map);

    /**
     *
     * Method to visualize the map.
     *
     */
    public void VisualizeMap();

    /**
     *
     * Loads a map to the system .
     *
     * @param Path Path of the file
     * @throws java.io.IOException Exception
     * @throws org.json.simple.parser.ParseException Exception
     */
    public void LoadAMap(String Path)throws IOException, ParseException;
     /**
      * Checks if the map is ready to be played
      * @param mapa the map to be tested
      * @return if is ready
      * @throws EmptyCollectionException  Exception
      */
    public boolean CheckMap(Map mapa) throws EmptyCollectionException;
    
    /**
     * Returns in a String array all maps names
     * @return in a String array all maps names
     */
     public String[] getAllMapsName() ;
     
     
     /**
      * Deletes the last map saved
      */
     public void deleteLastMap();
     
     
     /**
      * returns all maps available
      * @return returns all maps available
      */
     public Map[] getMaps();
     /**
      * set a preexisting 
      * @param Maps all maps wanted to set
      */
     public void setMaps(Map[] Maps);
     
     /**
      *  the number of maps existing
      * @return  the number of maps existing
      */
      public int getMapCounter();
     
/**
 * set the number of maps existing
 * @param MapCounter the number of maps existing
 */
    public void setMapCounter(int MapCounter);
   
   /**
    *returns  map selected by user
    * @return map selected by user
    */ 
    public Map getSelectedMap() ;
   
/**
 * map selected by user
 * @param SelectedMap map selected by user
 */
    public void setSelectedMap(Map SelectedMap);
/**
 * gamemode to be played
 * @return gamemode to be played
 */
    public String getGameMode();
  
/**
 * sets gamemode to be played
 * @param GameMode gamemode to be played
 */
    public void setGameMode(String GameMode);
    
/**
 * difficulty to be played
 * @return  difficulty to be played
 */
    public int getDifficulty();
     /**
      *  difficulty to be played
      * @param Difficulty  difficulty to be played
      */

    public void setDifficulty(int Difficulty);
/**
 * raises the capacity of the maps
 */
    public void ExtendMapCapacity();
}
