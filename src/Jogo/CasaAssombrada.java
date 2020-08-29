/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Jogo;

import Exceptions.ElementNotFoundException;
import Exceptions.EmptyCollectionException;
import Interfaces.CasaAssombradaInterface;
import UnorederedList.ArrayUnorderedList;
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

    private ArrayUnorderedList<Map> Maps;
    private Map SelectedMap;
    private String GameMode;
    private int Difficulty;
    private Player player;
    private Room currentRoom;
    private DirectedNetworkWithMatrix<Room> tmp;

    public CasaAssombrada() {
        Maps = new ArrayUnorderedList<>();
    }

    public boolean checkTeletransporte()
    {
        if(player.hasTeletransporte())
        {
            ArrayUnorderedList<Room> roomsWithoutFantasmas = getRoomsWithoutGhosts();
            currentRoom = getRandomRoomFromArray(roomsWithoutFantasmas);
        }
    }

    private Room getRandomRoomFromArray(ArrayUnorderedList<Room> rooms)
    {
        int chosenRoomIndex = (int) (Math.random() * rooms.size());
        Iterator roomsIterator = rooms.iterator();
        for(int k = 0; k < chosenRoomIndex ; k++)
            roomsIterator.next();
        return (Room) roomsIterator.next();
    }

    private ArrayUnorderedList<Room> getRoomsWithoutGhosts()
    {
        ArrayUnorderedList<Room> roomsWithoutFantasmas = new ArrayUnorderedList<>();
        for(Object vertice : tmp.getVertices())
        {
            Room currVerticeRoom = (Room) vertice;
            if(currVerticeRoom.getTotalDamage() == 0)
                roomsWithoutFantasmas.addToRear(currVerticeRoom);
        }
        return roomsWithoutFantasmas;
    }

    @Override
    public void play() throws IOException, ParseException, EmptyCollectionException, ElementNotFoundException
    {
        player.setLifePoints(SelectedMap.getInitialLifePoints());
        if (GameMode.equals("manual")) {

            //TODO undo


            currentRoom = new Room(SelectedMap.getMapStartingLocation().getName(), SelectedMap.getMapStartingLocation().getGhosts());

            tmp = SelectedMap.getGraph();

            //TODO: por power up no mapa
            putRandomPowerUpOnRoom();

            while (!(currentRoom.getName().equals("exterior")) && player.getLifePoints() > 0)
            {
                //TODO ver se atingiu powerup
                if(currentRoom.getRewardItem() != RewardItem.None)
                    setPlayerPowerUp(currentRoom.getRewardItem());

                System.out.println("Current Room:" + currentRoom.getName() + "\n");

                VisualizeMap(); //TODO: mostrar fantasmas

                System.out.println("Current life points: "+ player.getLifePoints());
                System.out.println("Wich room to go:\n");

                int[] AdjacentRooms = tmp.GetIndexOfAdjVertices(currentRoom);

                Object[] Vertices = tmp.getVertices();

                int i = 0;

                while (AdjacentRooms[i] != -1) {
                    System.out.println((i + 1) + "." + (Room) Vertices[AdjacentRooms[i]]);
                    i++;
                }

                BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

                String n = reader.readLine();

                //TODO: try catch para o parse int
                currentRoom = ((Room) Vertices[AdjacentRooms[Integer.parseInt(n) - 1]]);

                //TODO: lógica de verficar se perde pontos
                long totalInflictedDamage = ((Room) tmp.getVertices()[Integer.parseInt(n)]).getTotalDamage();
                if(totalInflictedDamage > 0)
                {
                    if(checkTeletransporte())
                    {
                        player.setTeletransporte(false);
                        totalInflictedDamage = 0;
                    }
                    if(player.getEscudo() > 0)
                    {
                        long baseTotalInflictedDamage = totalInflictedDamage;
                        totalInflictedDamage -= player.getEscudo();
                        if(totalInflictedDamage < 0)
                            totalInflictedDamage = 0;
                        player.setEscudo(player.getEscudo() - baseTotalInflictedDamage);
                        if(player.getEscudo() < 0)
                            player.setEscudo(0);
                    }

                    player.setLifePoints(player.getLifePoints() - totalInflictedDamage);
                }

                //TODO: lógica de mover fantasmas
                for(Object roomObject : Vertices)
                {
                    Room room = (Room) roomObject;
                    ArrayUnorderedList<Fantasma> roomFantasmas = room.getGhosts();
                    if(roomFantasmas.size() == 0)
                        continue;

                    ArrayUnorderedList<Room> verticesThatComplyWithConditions = new ArrayUnorderedList<>();
                    int playerCurrentVerticeIndex = tmp.getIndex(currentRoom);
                    int[] adjacentVertices = tmp.GetIndexOfAdjVertices(room);
                    for(int j = 0; j < adjacentVertices.length; j++)
                    {
                        //if player is in this place
                        if(adjacentVertices[j] == playerCurrentVerticeIndex)
                            continue;

                        Room adjacentRoom = (Room)(tmp.getVertices())[adjacentVertices[j]];
                        //if a ghost is already in this place
                        if(adjacentRoom.getGhosts().size() > 0)
                            continue;

                        verticesThatComplyWithConditions.addToRear(adjacentRoom);
                    }

                    while(verticesThatComplyWithConditions.size() != 0)
                    {
                        Room chosenRoom;
                        Iterator roomFantasmasIterator = roomFantasmas.iterator();
                        while(roomFantasmasIterator.hasNext())
                        {
                            Fantasma fantasma = (Fantasma) roomFantasmasIterator.next();
                            int chosenRoomIndex = (int) (Math.random() * verticesThatComplyWithConditions.size());
                            Iterator verticesThatComplyWithConditionsIterator = verticesThatComplyWithConditions.iterator();
                            for(int k = 0; k < chosenRoomIndex ; k++)
                                verticesThatComplyWithConditionsIterator.next();
                            chosenRoom = (Room) verticesThatComplyWithConditionsIterator.next();

                            //troca de uma room para outra
                            chosenRoom.addGhost(fantasma);
                            room.removeGhost(fantasma);

                            //retirar o que deixou de cumprir a condição de não ter lá uma ghost
                            verticesThatComplyWithConditionsIterator.remove();
                        }
                    }
                }
            }

            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

            if (player.getLifePoints() > 0)
            {
                System.out.println("You just won the game!\nPlease gives you nickname:");

                String n = reader.readLine();

                Player InfoToTheLeaderBoard = new Player(n, player.getLifePoints());

                SelectedMap.AddToTheLeaderboard(InfoToTheLeaderBoard);

                System.out.println("You can know check your leaderboard and see the top players!");

            } else {
                System.out.println("You just lost the game!");
            }
        } else if (GameMode.equals("simulation")) {
            Iterator it;

            Room Exit = new Room("exterior", 0);

            it = SelectedMap.getGraph().iteratorShortestPathWeight(SelectedMap.getMapStartingLocation(), Exit);

            while (it.hasNext()) {

                System.out.print(it.next().toString() + "-> ");

            }
            System.out.print("\n");
        } else {
            System.out.println("Erro nao existe esse modo de jogo");
        }
    }

    private void putRandomPowerUpOnRoom()
    {
        ArrayUnorderedList<Room> roomsWithoutFantasmas = getRoomsWithoutGhosts();
        Room randomRoomChosen = getRandomRoomFromArray(roomsWithoutFantasmas);
        int randomNumberForPowerUpType = (int) ((Math.random() * 3) + 1);
        switch (randomNumberForPowerUpType)
        {
            case 1:
                randomRoomChosen.setRewardItem(RewardItem.LifePoints);
                break;
            case 2:
                randomRoomChosen.setRewardItem(RewardItem.Escudo);
                break;
            case 3:
                randomRoomChosen.setRewardItem(RewardItem.Teletransporte);
                break;
        }
    }

    private void setPlayerPowerUp(RewardItem rewardItem)
    {
        switch (rewardItem)
        {
            case LifePoints:
                player.setLifePoints(player.getLifePoints() + 25);
                break;
            case Escudo:
                int randomNumberForShield = (int) ((Math.random() * getSelectedMap().getBiggestGhost()) + 1);
                player.setEscudo(randomNumberForShield);
                break;
            case Teletransporte:
                player.setTeletransporte(true);
                break;
        }
    }


    @Override
    public boolean CheckMap(Map mapa) throws EmptyCollectionException {
        boolean a = true;
        long LifePoints = mapa.getInitialLifePoints();
        Room Exit = new Room("exterior", 0);
        Iterator it;
        it = mapa.getGraph().iteratorShortestPathWeight(mapa.getMapStartingLocation(), Exit);
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
            System.out.println(SelectedMap.getGraph().toString());
        } else {
            String tmp = SelectedMap.getGraph().toString();
            String[] Arraytmp;
            Arraytmp = tmp.split("Weights of Edges");
            System.out.println(Arraytmp[0]);
        }
    }

    @Override
    public void LoadAMap(String path) throws IOException, ParseException {

        int i = 0;

        JSONObject obj = (JSONObject) new JSONParser().parse(new FileReader(path));
        boolean nameDoesntExists = true;

        Iterator iterator = Maps.iterator();
        while(iterator.hasNext())
        {
            Map map = (Map) iterator.next();
            if (map.getName().equals(obj.get("nome"))) {
                nameDoesntExists = false;
            }
        }
        if (nameDoesntExists == true) {
            Map newMap = new Map((String) obj.get("nome"), (long) obj.get("pontos"));
            Maps.addToRear(newMap);

            JSONArray jsonArray = (JSONArray) obj.get("mapa");

            DirectedNetworkWithMatrix tmp = newMap.getGraph();

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

            while (i < jsonArray.size()) {

                JSONArray jsonArray2 = (JSONArray) ((JSONObject) jsonArray.get(i)).get("ligacoes");

                int o = 0;

                while (o < jsonArray2.size()) {
                    if ("entrada".equals((String) jsonArray2.get(o))) {
                        Room StartLocation = new Room((String) ((JSONObject) jsonArray.get(i)).get("aposento"), (long) ((JSONObject) jsonArray.get(i)).get("fantasma"));
                        newMap.setMapStartingLocation(StartLocation);
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
}
