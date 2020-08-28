/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Jogo;

import Exceptions.EmptyCollectionException;
import Interfaces.CasaAssombradaInterface;
import directednetworkwithmatrix.DirectedNetworkWithMatrix;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Iterator;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 *
 * @author Diogo Lopes 8180121
 */
public class CasaAssombrada implements CasaAssombradaInterface {

    private Map[] Maps;
    private int MapCounter;
    private final int DEFAULT_CAPACITY = 10;
    private Map SelectedMap;
    private String GameMode;
    private int Difficulty;

    public CasaAssombrada() {
        Maps = new Map[DEFAULT_CAPACITY];
        this.MapCounter = 0;
    }

    @Override
    public void play() throws IOException, ParseException, EmptyCollectionException {
        long LifePoints = SelectedMap.getInitialLifePoints();
        if ("manual" == GameMode) {

            Room CurrentRoom = new Room(SelectedMap.getMapStartingLocation().getName(), SelectedMap.getMapStartingLocation().getGhost());

            DirectedNetworkWithMatrix<Room> tmp = SelectedMap.getMap();

            while ("exterior" != CurrentRoom.getName() && LifePoints > 0) {

                System.out.println("Current Room:" + CurrentRoom.getName() + "\n");
                if (CurrentRoom.getGhost() != 0) {
                    System.out.println("UUUU fantasma perdes-te : " + CurrentRoom.getGhost() + "Pontos\n");
                }
                  System.out.println("Current life points"+ LifePoints);
                System.out.println("Wich room to go:\n");

                int[] AdjacentRooms = tmp.GetIndexOfAdjVertices(CurrentRoom);

                Object[] Vertices = new Object[tmp.getVertices().length];

                Vertices = tmp.getVertices();

                int i = 0;

                while (AdjacentRooms[i] != -1) {
                    System.out.println((i + 1) + "." + (Room) Vertices[AdjacentRooms[i]]);
                    i++;
                }

                BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

                String n = reader.readLine();

                CurrentRoom = ((Room) Vertices[AdjacentRooms[Integer.parseInt(n) - 1]]);
                LifePoints -= Difficulty * ((Room) tmp.getVertices()[Integer.parseInt(n)]).getGhost();
              

            }

            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

            if (LifePoints > 0) {
                System.out.println("You just won the game!\nPlease gives you nickname:");

                String n = reader.readLine();

                PlayerInformation InfoToTheLeaderBoard = new PlayerInformation(n, LifePoints);

                SelectedMap.AddToTheLeaderboard(InfoToTheLeaderBoard);

                System.out.println("You can know check your leaderboard and see the top players!");

            } else {
                System.out.println("You just lost the game!");
            }
        } else if (GameMode == "simulation") {
            Iterator it;

            Room Exit = new Room("exterior", 0);

            it = SelectedMap.getMap().iteratorShortestPathWeight(SelectedMap.getMapStartingLocation(), Exit);

            while (it.hasNext()) {

                System.out.print(it.next().toString() + "-> ");

            }
            System.out.print("\n");
        } else {
            System.out.println("Erro nao existe esse modo de jogo");
        }
    }

    @Override
    public boolean CheckMap(Map mapa) throws EmptyCollectionException {
        boolean a = true;
        long LifePoints = mapa.getInitialLifePoints();
        Room Exit = new Room("exterior", 0);
        Iterator it;
        it = mapa.getMap().iteratorShortestPathWeight(mapa.getMapStartingLocation(), Exit);
        while (it.hasNext()) {
            LifePoints -= getDifficulty() * ((Room) it.next()).getGhost();
        }
        if (LifePoints < 0) {
            a = false;
        }
        return a;
    }

    @Override
    public void Select_Game_Mode(String Gamemode) {
        this.GameMode = Gamemode;
    }

    @Override
    public void Select_Map(int MapChoice) {
        this.SelectedMap = Maps[MapChoice];
    }

    @Override
    public void Select_Difficulty(int DifficultyChoice) {
        this.Difficulty = DifficultyChoice;
    }

    @Override
    public void CheckLeaderboard(String Map) {
        int i = 0;

        while (!Maps[i].getName().equals(Map)) {
            i++;
        }

        Iterator it = Maps[i].getMapLeaderboard().iterator();

        while (it.hasNext()) {
            System.out.println(i + 1 + "º Lugar:\n" + it.next().toString());
            i++;
        }
    }

    @Override
    public void VisualizeMap() {

        if ("simulation" == this.GameMode) {
            System.out.println(SelectedMap.getMap().toString());
        } else {
            String tmp = SelectedMap.getMap().toString();
            String[] Arraytmp;
            Arraytmp = tmp.split("Weights of Edges");
            System.out.println(Arraytmp[0]);
        }
    }

    @Override
    public void LoadAMap(String path) throws IOException, ParseException {

        int i = 0;

        if (Maps.length == MapCounter) {
            ExtendMapCapacity();
        }

        JSONObject obj = (JSONObject) new JSONParser().parse(new FileReader(path));
        boolean l = true;
        for (int p = 0; p < MapCounter; p++) {
            if (Maps[p].getName().equals((String) obj.get("nome"))) {
                l = false;
            }
        }
        if (l == true) {
            Maps[MapCounter] = new Map((String) obj.get("nome"), (long) obj.get("pontos"));

            JSONArray jsonArray = (JSONArray) obj.get("mapa");

            DirectedNetworkWithMatrix tmp = Maps[MapCounter].getMap();

            Room[] ArrayOfRooms = new Room[jsonArray.size()];

            while (i < jsonArray.size()) {

                Room Place = new Room((String) ((JSONObject) jsonArray.get(i)).get("aposento"), (long) ((JSONObject) jsonArray.get(i)).get("fantasma"));

                ArrayOfRooms[i] = Place;

                tmp.addVertex(Place);

                i++;
            }

            Room Exit = new Room("exterior", 0);

            tmp.addVertex(Exit);

            i = 0;
            long biggestGhost = 0;
            int leftLimit = 0;
            int rightLimit = tmp.size();
            int generatedint = leftLimit + (int) (Math.random() * (rightLimit - leftLimit));

            while (i < jsonArray.size()) {

                JSONArray jsonArray2 = (JSONArray) ((JSONObject) jsonArray.get(i)).get("ligacoes");

                int o = 0;

                while (o < jsonArray2.size()) {
                    if ("entrada".equals((String) jsonArray2.get(o))) {
                        Room StartLocation = new Room((String) ((JSONObject) jsonArray.get(i)).get("aposento"), (long) ((JSONObject) jsonArray.get(i)).get("fantasma"));
                        Maps[MapCounter].setMapStartingLocation(StartLocation);
                        if ((long) ((JSONObject) jsonArray.get(i)).get("fantasma") > biggestGhost) {
                            biggestGhost = (long) ((JSONObject) jsonArray.get(i)).get("fantasma");
                        }
                    } else if ("exterior".equals((String) jsonArray2.get(o))) {
                        Room tmpRoom = new Room((String) ((JSONObject) jsonArray.get(i)).get("aposento"), (long) ((JSONObject) jsonArray.get(i)).get("fantasma"));
                        tmp.addEdge(tmpRoom, Exit, 0);
                        if ((long) ((JSONObject) jsonArray.get(i)).get("fantasma") > biggestGhost) {
                            biggestGhost = (long) ((JSONObject) jsonArray.get(i)).get("fantasma");
                        }
                    } else {
                        Room tmpRoom = new Room((String) ((JSONObject) jsonArray.get(i)).get("aposento"), (long) ((JSONObject) jsonArray.get(i)).get("fantasma"));
                        if ((long) ((JSONObject) jsonArray.get(i)).get("fantasma") > biggestGhost) {
                            biggestGhost = (long) ((JSONObject) jsonArray.get(i)).get("fantasma");
                        }
                        for (int k = 0; k < ArrayOfRooms.length; k++) {
                            if (ArrayOfRooms[k].getName().equals((String) jsonArray2.get(o))) {
                                tmp.addEdge(tmpRoom, ArrayOfRooms[k], ArrayOfRooms[k].getGhost());
                                break;
                            }
                        }
                    }
                    o++;
                }
                i++;
            }
            int leftLimit2 = 1;
            int rightLimit3 = (int) biggestGhost;
            int generatedint3 = leftLimit + (int) (Math.random() * (rightLimit - leftLimit));
            do {
                generatedint = leftLimit + (int) (Math.random() * (rightLimit - leftLimit));
            } while ((((Room) tmp.getVertices()[generatedint]).getGhost()) != 0);
            ((Room) tmp.getVertices()[generatedint]).setGhost(generatedint3);
            MapCounter++;

        } else {
            System.out.println("Erro mapa ja existe");
        }

    }

    @Override
    public String[] getAllMapsName() {

        String[] tmpMaps = new String[MapCounter];

        for (int i = 0; i < MapCounter; i++) {
            tmpMaps[i] = getMaps()[i].getName();
        }

        return tmpMaps;
    }

    @Override
    public void deleteLastMap() {
        Maps[MapCounter] = null;
        MapCounter--;
    }

    @Override
    public Map[] getMaps() {
        return Maps;
    }

    @Override
    public void setMaps(Map[] Maps) {
        this.Maps = Maps;
    }

    @Override
    public int getMapCounter() {
        return MapCounter;
    }

    @Override
    public void setMapCounter(int MapCounter) {
        this.MapCounter = MapCounter;
    }

    @Override
    public Map getSelectedMap() {
        return SelectedMap;
    }

    @Override
    public void setSelectedMap(Map SelectedMap) {
        this.SelectedMap = SelectedMap;
    }

    @Override
    public String getGameMode() {
        return GameMode;
    }

    @Override
    public void setGameMode(String GameMode) {
        this.GameMode = GameMode;
    }

    @Override
    public int getDifficulty() {
        return Difficulty;
    }

    @Override
    public void setDifficulty(int Difficulty) {
        this.Difficulty = Difficulty;
    }

    @Override
    public void ExtendMapCapacity() {
        Map[] tmp = new Map[Maps.length + DEFAULT_CAPACITY];

        System.arraycopy(Maps, 0, tmp, 0, MapCounter);

        Maps = tmp;
    }
}
