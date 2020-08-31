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

        while (Ongoing) {

            System.out.println("Welcome to Casa Assombrada:\n1.Play \n2.LeaderBoard\n3.Exit");

            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

            String n = reader.readLine();

            switch (Integer.parseInt(n)) {
                case 1:

                    System.out.println("Choose a game mode:\n1.Manual \n2.Simulation");

                    n = reader.readLine();

                    switch (Integer.parseInt(n)) {

                        case 1:
                            Jogo.Select_Game_Mode("manual");

                            System.out.println("Choose a game difficulty:\n1.Basic \n2.Normal\n3.Hard");

                            n = reader.readLine();

                            switch (Integer.parseInt(n)) {

                                case 1:
                                    Jogo.Select_Difficulty(1);

                                    System.out.println("1.Play an existent map \n2.Load a map");

                                    n = reader.readLine();

                                    switch (Integer.parseInt(n)) {
                                        case 1:
                                            if (Jogo.getMaps().size() == 0) {
                                                throw new Exception("error");
                                            }
                                            ArrayUnorderedList<String> tmpMaps;

                                            tmpMaps = Jogo.getAllMapsName();

                                            System.out.println("Choose a map:");

                                            Iterator mapsIterator = tmpMaps.iterator();
                                            int i = 0;
                                            while(mapsIterator.hasNext())
                                                System.out.println((i + 1) + "." + mapsIterator.next());

                                            n = reader.readLine();

                                            if (Integer.parseInt(n) < 0 && Integer.parseInt(n) > tmpMaps.size() + 1) {
                                                System.out.println("Choose a existent map");
                                                break;
                                            } else {
                                                Jogo.Select_Map(Integer.parseInt(n) - 1);
                                            }

                                            System.out.println("1.Play the map\n2.Visualize map ");

                                            n = reader.readLine();

                                            switch (Integer.parseInt(n)) {
                                                case 1:
                                                    Jogo.play();
                                                    break;
                                                case 2:
                                                    Jogo.VisualizeMap();
                                                    break;
                                                default:
                                                    System.out.println("Select one of the available options (1-2)\n");
                                            }
                                            break;
                                        case 2:
                                            System.out.println("Input the path to the map you wish to load:\n");

                                            n = reader.readLine();

                                            Jogo.LoadAMap(n);
                                            if (Jogo.CheckMap(Jogo.getMaps().last()) == false) {
                                                System.out.println("Impossible map");
                                                Jogo.deleteLastMap();
                                            } else {

                                                Jogo.Select_Game_Mode("manual");
                                                Jogo.Select_Map(Jogo.getMaps().size() - 1);
                                                Jogo.play();
                                            }
                                            break;
                                        default:
                                            System.out.println("Select one of the available options(1-2)\n");
                                    }
                                    break;

                                case 2:
                                    Jogo.Select_Difficulty(2);

                                    System.out.println("1.Play an existent map \n2.Load a map");

                                    n = reader.readLine();

                                    switch (Integer.parseInt(n)) {
                                        case 1:
                                            if (Jogo.getMaps().size() == 0) {
                                                throw new Exception("error");
                                            }
                                            ArrayUnorderedList<String> tmpMaps;

                                            tmpMaps = Jogo.getAllMapsName();

                                            System.out.println("Choose a map:");

                                            Iterator mapsIterator = tmpMaps.iterator();
                                            int i = 0;
                                            while(mapsIterator.hasNext())
                                                System.out.println((i + 1) + "." + mapsIterator.next());

                                            n = reader.readLine();

                                            if (Integer.parseInt(n) < 0 && Integer.parseInt(n) > tmpMaps.size() + 1) {
                                                System.out.println("Choose a existent map");
                                                break;
                                            } else {
                                                Jogo.Select_Map(Integer.parseInt(n) - 1);
                                            }

                                            System.out.println("1.Play the map\n2.Visualize map ");

                                            n = reader.readLine();

                                            switch (Integer.parseInt(n)) {
                                                case 1:
                                                    Jogo.play();
                                                    break;
                                                case 2:
                                                    Jogo.VisualizeMap();
                                                    break;
                                                default:
                                                    System.out.println("Select one of the available options (1-2)\n");
                                            }
                                            break;
                                        case 2:
                                            System.out.println("Input the path to the map you wish to load:\n");

                                            n = reader.readLine();

                                            Jogo.LoadAMap(n);
                                            if (Jogo.CheckMap(Jogo.getMaps().last()) == false) {
                                                System.out.println("Impossible map");
                                                Jogo.deleteLastMap();
                                            } else {

                                                Jogo.Select_Game_Mode("manual");
                                                Jogo.Select_Map(Jogo.getMaps().size() - 1);
                                                Jogo.play();
                                            }
                                            break;
                                        default:
                                            System.out.println("Select one of the available options(1-2)\n");
                                    }
                                    break;

                                case 3:
                                    Jogo.Select_Difficulty(3);

                                    System.out.println("1.Play an existent map \n2.Load a map");

                                    n = reader.readLine();

                                    switch (Integer.parseInt(n)) {
                                        case 1:
                                            if (Jogo.getMaps().size() == 0) {
                                                throw new Exception("error");
                                            }
                                            ArrayUnorderedList<String> tmpMaps;

                                            tmpMaps = Jogo.getAllMapsName();

                                            System.out.println("Choose a map:");

                                            Iterator mapsIterator = tmpMaps.iterator();
                                            int i = 0;
                                            while(mapsIterator.hasNext())
                                                System.out.println((i + 1) + "." + mapsIterator.next());

                                            n = reader.readLine();

                                            if (Integer.parseInt(n) < 0 && Integer.parseInt(n) > tmpMaps.size() + 1) {
                                                System.out.println("Choose a existent map");
                                                break;
                                            } else {
                                                Jogo.Select_Map(Integer.parseInt(n) - 1);
                                            }

                                            System.out.println("1.Play the map\n2.Visualize map ");

                                            n = reader.readLine();

                                            switch (Integer.parseInt(n)) {
                                                case 1:
                                                    Jogo.play();
                                                    break;
                                                case 2:
                                                    Jogo.VisualizeMap();
                                                    break;
                                                default:
                                                    System.out.println("Select one of the available options (1-2)\n");
                                            }
                                            break;
                                        case 2:
                                            System.out.println("Input the path to the map you wish to load:\n");

                                            n = reader.readLine();

                                            Jogo.LoadAMap(n);
                                            if (Jogo.CheckMap(Jogo.getMaps().last()) == false) {
                                                System.out.println("Impossible map");
                                                Jogo.deleteLastMap();
                                            } else {

                                                Jogo.Select_Game_Mode("manual");
                                                Jogo.Select_Map(Jogo.getMaps().size() - 1);
                                                Jogo.play();
                                            }
                                            break;
                                        default:
                                            System.out.println("Select one of the available options(1-2)\n");
                                    }
                                    break;

                                default:
                                    System.out.println("Select one of the available options(1-3)\n");
                            }
                            break;

                        case 2:
                            Jogo.Select_Game_Mode("simulation");

                            System.out.println("Choose a game difficulty:\n1.Basic \n2.Normal\n3.Hard");

                            n = reader.readLine();

                            switch (Integer.parseInt(n)) {
                                case 1:
                                    Jogo.Select_Difficulty(1);

                                    System.out.println("1.Play an existent map \n2.Load a map");

                                    n = reader.readLine();

                                    switch (Integer.parseInt(n)) {
                                        case 1:
                                            if (Jogo.getMaps().size() == 0) {
                                                throw new Exception("error");
                                            }
                                            ArrayUnorderedList<String> tmpMaps;

                                            tmpMaps = Jogo.getAllMapsName();

                                            System.out.println("Choose a map:");

                                            Iterator mapsIterator = tmpMaps.iterator();
                                            int i = 0;
                                            while(mapsIterator.hasNext())
                                                System.out.println((i + 1) + "." + mapsIterator.next());


                                            n = reader.readLine();

                                            if (Integer.parseInt(n) < 0 && Integer.parseInt(n) < tmpMaps.size() + 1) {
                                                System.out.println("Choose a existent map");
                                                break;
                                            } else {
                                                Jogo.Select_Map(Integer.parseInt(n) - 1);
                                            }

                                            System.out.println("1.Play the map\n2.Visualize map ");

                                            n = reader.readLine();

                                            switch (Integer.parseInt(n)) {
                                                case 1:
                                                    Jogo.play();
                                                    break;
                                                case 2:
                                                    Jogo.VisualizeMap();
                                                    break;
                                                default:
                                                    System.out.println("Select one of the available options (1-2)\n");
                                            }
                                            break;
                                        case 2:
                                            System.out.println("Input the path to the map you wish to load:\n");

                                            n = reader.readLine();

                                            Jogo.LoadAMap(n);
                                            if (Jogo.CheckMap(Jogo.getMaps().last()) == false) {
                                                System.out.println("Impossible map");
                                                Jogo.deleteLastMap();
                                            } else {

                                                Jogo.Select_Game_Mode("simulation");
                                                Jogo.Select_Map(Jogo.getMaps().size() - 1);
                                                Jogo.play();
                                            }
                                            break;
                                        default:
                                            System.out.println("Select one of the available options(1-2)\n");
                                    }
                                    break;

                                case 2:
                                    Jogo.Select_Difficulty(2);

                                    System.out.println("1.Play an existent map \n2.Load a map");

                                    n = reader.readLine();

                                    switch (Integer.parseInt(n)) {
                                        case 1:
                                            if (Jogo.getMaps().size() == 0) {
                                                throw new Exception("error");
                                            }
                                            ArrayUnorderedList<String> tmpMaps;

                                            tmpMaps = Jogo.getAllMapsName();

                                            System.out.println("Choose a map:");

                                            Iterator mapsIterator = tmpMaps.iterator();
                                            int i = 0;
                                            while(mapsIterator.hasNext())
                                                System.out.println((i + 1) + "." + mapsIterator.next());

                                            n = reader.readLine();

                                            if (Integer.parseInt(n) < 0 && Integer.parseInt(n) > tmpMaps.size()) {
                                                System.out.println("Choose a existent map");
                                                break;
                                            } else {
                                                Jogo.Select_Map(Integer.parseInt(n) - 1);
                                            }

                                            System.out.println("1.Play the map\n2.Visualize map ");

                                            n = reader.readLine();

                                            switch (Integer.parseInt(n)) {
                                                case 1:
                                                    Jogo.play();
                                                    break;
                                                case 2:
                                                    Jogo.VisualizeMap();
                                                    break;
                                                default:
                                                    System.out.println("Select one of the available options (1-2)\n");
                                            }
                                            break;
                                        case 2:
                                            System.out.println("Input the path to the map you wish to load:\n");

                                            n = reader.readLine();

                                            Jogo.LoadAMap(n);
                                            if (Jogo.CheckMap(Jogo.getMaps().last()) == false) {
                                                System.out.println("Impossible map");
                                                Jogo.deleteLastMap();
                                            } else {

                                                Jogo.Select_Game_Mode("simulation");
                                                Jogo.Select_Map(Jogo.getMaps().size() - 1);
                                                Jogo.play();
                                            }
                                            break;
                                        default:
                                            System.out.println("Select one of the available options(1-2)\n");
                                    }
                                    break;

                                case 3:
                                    Jogo.Select_Difficulty(3);

                                    System.out.println("1.Play an existent map \n2.Load a map");

                                    n = reader.readLine();

                                    switch (Integer.parseInt(n)) {
                                        case 1:
                                            if (Jogo.getMaps().size() == 0) {
                                                throw new Exception("error");
                                            }
                                            ArrayUnorderedList<String> tmpMaps;

                                            tmpMaps = Jogo.getAllMapsName();

                                            System.out.println("Choose a map:");

                                            Iterator mapsIterator = tmpMaps.iterator();
                                            int i = 0;
                                            while(mapsIterator.hasNext())
                                                System.out.println((i + 1) + "." + mapsIterator.next());

                                            n = reader.readLine();

                                            if (Integer.parseInt(n) < 0 && Integer.parseInt(n) > tmpMaps.size()) {
                                                System.out.println("Choose a existent map");
                                                break;
                                            } else {
                                                Jogo.Select_Map(Integer.parseInt(n) - 1);
                                            }

                                            System.out.println("1.Play the map\n2.Visualize map ");

                                            n = reader.readLine();

                                            switch (Integer.parseInt(n)) {
                                                case 1:
                                                    Jogo.play();
                                                    break;
                                                case 2:
                                                    Jogo.VisualizeMap();
                                                    break;
                                                default:
                                                    System.out.println("Select one of the available options (1-2)\n");
                                            }
                                            break;
                                        case 2:
                                            System.out.println("Input the path to the map you wish to load:\n");

                                            n = reader.readLine();

                                            Jogo.LoadAMap(n);
                                            if (Jogo.CheckMap(Jogo.getMaps().last()) == false) {
                                                System.out.println("Impossible map");
                                                Jogo.deleteLastMap();
                                            } else {

                                                Jogo.Select_Game_Mode("simulation");
                                                Jogo.Select_Map(Jogo.getMaps().size() - 1);
                                                Jogo.play();
                                            }
                                            break;
                                        default:
                                            System.out.println("Select one of the available options(1-2)\n");
                                    }
                                    break;

                                default:
                                    System.out.println("Select one of the available options(1-3)\n");
                            }
                            break;

                        default:
                            System.out.println("Select one of the available options(1-2)\n");
                    }

                    break;

                case 2:
                    if (Jogo.getMaps().size() == 0) {
                        throw new Exception("error");
                    }
                    ArrayUnorderedList<String> tmpMaps;

                    tmpMaps = Jogo.getAllMapsName();

                    System.out.println("Choose a map from wich you want to check the leaderboard.");

                    Iterator mapsIterator = tmpMaps.iterator();
                    int i = 0;
                    while(mapsIterator.hasNext())
                        System.out.println((i + 1) + "." + mapsIterator.next());

                    n = reader.readLine();

                    mapsIterator = tmpMaps.iterator();
                    for (int j = 0; j < Integer.parseInt(n) - 1; j++)
                        mapsIterator.next();
                    Jogo.CheckLeaderboard((Map) mapsIterator.next());

                    break;
                case 3:
                    Ongoing = false;
                    break;

                default:
                    System.out.println("Select one of the available options(1-3)\n");
            }
        }
    }
}
