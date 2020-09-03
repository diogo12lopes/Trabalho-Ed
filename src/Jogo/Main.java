/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Jogo;

import Exceptions.EmptyCollectionException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Iterator;

import UnorederedList.ArrayUnorderedList;
import org.json.simple.parser.ParseException;

/**
 *
 * @author Diogo Lopes 8180121
 */
public class Main {

    /**
     * @param args the command line arguments
     * @throws java.io.IOException
     * @throws org.json.simple.parser.ParseException
     * @throws Exceptions.EmptyCollectionException
     */
    public static void main(String[] args) throws IOException, ParseException, EmptyCollectionException, Exception {

        CasaAssombrada Jogo = new CasaAssombrada();

        boolean Ongoing = true;

        while (Ongoing)
        {
            System.out.println("Welcome to Casa Assombrada:\n1.Play \n2.LeaderBoard\n3.Load Map\n4.Visualize Map\n5.Exit");

            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            String n = reader.readLine();
            try
            {
                switch (Integer.parseInt(n))
                {
                    case 1:
                        casaAssombradaMenu(Jogo);
                        break;
                    case 2:
                        leaderboardMenu(Jogo);
                        break;
                    case 3:
                        loadMapMenu(Jogo);
                        break;
                    case 4:
                        choseFromKnownMaps(Jogo);
                        Jogo.VisualizeMap();
                        break;
                    case 5:
                        Ongoing = false;
                        break;
                    default:
                        throw new Exception("Select one of the available options(1-5)\n");
                }
            }
            catch (NumberFormatException e)
            {
                System.out.println("Select one of the available options(1-5)\n");
            }
            catch (Exception e)
            {
                System.out.println(e.getMessage());
            }
        }
    }

    /**
     * Mostra o menu do leaderboard e baseado no input do utilizador mostra o conjunto de resultados obtidos para um determinado mapa
     * @param jogo a instância de jogo utilizada
     * @throws Exception
     */
    private static void leaderboardMenu(CasaAssombrada jogo) throws Exception
    {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String n;
        if (jogo.getMaps().size() == 0)
            throw new Exception("No maps loaded");

        ArrayUnorderedList<String>  tmpMaps = jogo.getAllMapsName();
        boolean validInput = false;
        while (validInput == false)
        {
            System.out.println("Choose a map from wich you want to check the leaderboard.");

            Iterator mapsNamesIterator = tmpMaps.iterator();
            int i = 0;
            while(mapsNamesIterator.hasNext())
            {
                System.out.println((i + 1) + "." + mapsNamesIterator.next());
                i++;
            }

            n = reader.readLine();
            int optionChoosen = -1;
            try
            {
                optionChoosen = Integer.parseInt(n);
            }
            catch(NumberFormatException e)
            {
                System.out.println("Select one of the available options(1-" + i + ")\n");
            }
            if(optionChoosen < 0 || optionChoosen > i)
                continue;

            mapsNamesIterator = tmpMaps.iterator();
            for (int j = 0; j < Integer.parseInt(n) - 1; j++)
                mapsNamesIterator.next();
            jogo.VisualizeLeaderboard((String) mapsNamesIterator.next());
            validInput = true;
        }
    }

    /**
     * Mostra o menu que permite dar inicio a um jogo e baseado no input do utilizador permite a escolha da dificuldade e modo de jogo
     * @param jogo a instância de jogo utilizada
     * @throws Exception
     */
    private static void casaAssombradaMenu(CasaAssombrada jogo) throws Exception
    {
        boolean validInput = false;
        while(validInput == false)
        {
            System.out.println("Choose a game mode:\n1.Manual \n2.Simulation");

            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            String n = reader.readLine();

            try
            {
                switch (Integer.parseInt(n))
                {
                    case 1:
                        validInput = true;
                        boolean validInput2 = false;
                        while(validInput2 == false)
                        {
                            jogo.Select_Game_Mode("manual");

                            System.out.println("Choose a game difficulty:\n1.Basic \n2.Normal\n3.Hard");

                            n = reader.readLine();

                            try
                            {
                                switch (Integer.parseInt(n)) {

                                    case 1:
                                        jogo.Select_Difficulty(1);
                                        validInput2 = true;
                                        break;

                                    case 2:
                                        jogo.Select_Difficulty(2);
                                        validInput2 = true;
                                        break;

                                    case 3:
                                        jogo.Select_Difficulty(3);
                                        validInput2 = true;
                                        break;

                                    default:
                                        throw new Exception("Select one of the available options(1-3)\n");
                                }
                            }
                            catch (NumberFormatException e)
                            {
                                System.out.println("Select one of the available options(1-3)\n");
                            }
                            catch (Exception e)
                            {
                                System.out.println(e.getMessage());
                            }
                        }

                        choseFromKnownMaps(jogo);
                        jogo.play();
                        break;

                    case 2:
                        validInput = true;
                        jogo.Select_Game_Mode("simulation");
                        choseFromKnownMaps(jogo);
                        jogo.play();

                    default:
                        throw new Exception("Select one of the available options(1-2)\n");
                }
            }
            catch (NumberFormatException e)
            {
                System.out.println("Select one of the available options(1-2)\n");
            }
            catch (Exception e)
            {
                System.out.println(e.getMessage());
            }
        }
    }

    /**
     * Método utilitário que permite a escolha de um mapa a partir da lista de mapas carregados em memória
     * @param jogo a instância de jogo utilizada
     * @throws Exception
     */
    private static void choseFromKnownMaps(CasaAssombrada jogo) throws Exception
    {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        if (jogo.getMaps().size() == 0)
            throw new Exception("No available maps");

        ArrayUnorderedList<String> tmpMaps = jogo.getAllMapsName();

        boolean validMapChosen = false;
        while (validMapChosen == false)
        {
                System.out.println("Choose a map:");

                Iterator mapsIterator = tmpMaps.iterator();
                int i = 0;
                while(mapsIterator.hasNext())
                {
                    System.out.println((i + 1) + "." + mapsIterator.next());
                    i++;
                }

                String n = reader.readLine();
                int optionChosen = -1;
                try
                {
                    optionChosen = Integer.parseInt(n);
                }
                catch (NumberFormatException e)
                {
                    System.out.println("Select one of the available options(1-" + i + ")\n");
                }

                if (optionChosen < 0 || optionChosen > tmpMaps.size()) {
                    System.out.println("Inexistent map");
                } else {
                jogo.Select_Map(Integer.parseInt(n) - 1);
                validMapChosen = true;
            }
        }
    }

    /**
     * Mostra o menu que permite carregar um novo mapa para memória, verificando a validade desse mapa
     * @param jogo a instância de jogo utilizada
     * @throws Exception
     */
    private static void loadMapMenu(CasaAssombrada jogo) throws IOException
    {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        boolean validInput = false;
        while(validInput == false)
        {
            System.out.println("Input the path to the map you wish to load:\n");
            String n = reader.readLine();

            try
            {
                jogo.LoadAMap(n);
                if (jogo.CheckMap(jogo.getMaps().last()) == false) {
                    System.out.println("Impossible map");
                    jogo.deleteLastMap();
                } else {
                    //save to persistent file
                    jogo.saveMaps();
                    jogo.Select_Map(jogo.getMaps().size() - 1);
                }
                validInput = true;
            }
            catch (Exception e)
            {
                System.out.println("Failed to load map");
            }
        }
    }
}
