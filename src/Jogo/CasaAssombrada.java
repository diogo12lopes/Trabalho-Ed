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

    public CasaAssombrada()
    {
        Maps = new ArrayUnorderedList<>();
        player = new Player("", 0);
    }

    public boolean checkTeletransporte()
    {
        if(player.hasTeletransporte())
        {
            ArrayUnorderedList<Room> roomsWithoutFantasmas = getRoomsWithoutGhosts();
            if(roomsWithoutFantasmas.size() == 0)
                return false;
            currentRoom = getRandomRoomFromArray(roomsWithoutFantasmas);
            return true;
        }

        return false;
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
        for (int i = 0; i < tmp.size(); i++)
        {
            Room currVerticeRoom = (Room) tmp.getVertices()[i];
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

            Room Exit = new Room("exterior", new ArrayUnorderedList<>());

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
        long LifePoints = mapa.getInitialLifePoints();
        Room Exit;

        if(mapa.getMapStartingLocation() == null)
            return false;

        for(Object vertice : tmp.getVertices())
        {
            Room currVerticeRoom = (Room) vertice;

            if(currVerticeRoom.getName().equals("exterior") == false)
                continue;

            Exit = currVerticeRoom;
            Iterator it = mapa.getGraph().iteratorShortestPathWeight(mapa.getMapStartingLocation(), Exit);
                while (it.hasNext()) {
                LifePoints -= ((Room) it.next()).getTotalDamage();
            }
            if (LifePoints < 0) {
                return false;
            }
            return true;
        }

        return false;
    }

    @Override
    public void Select_Game_Mode(String Gamemode) {
        this.GameMode = Gamemode;
    }

    @Override
    public void Select_Map(int MapChoice) {
        Iterator iterator = Maps.iterator();
        for (int i = 0; i < MapChoice; i++)
            iterator.next();
        this.SelectedMap = (Map) iterator.next();
    }

    @Override
    public void Select_Difficulty(int DifficultyChoice) {
        this.Difficulty = DifficultyChoice;
    }

    @Override
    public void CheckLeaderboard(Map map) {

        Iterator it = map.getMapLeaderboard().iterator();
        int i = 0;
        while (it.hasNext())
        {
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
            System.out.println(tmp);
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

            tmp = newMap.getGraph();

            Room[] ArrayOfRooms = new Room[jsonArray.size()];

            while (i < jsonArray.size()) {

                ArrayUnorderedList<Fantasma> ghosts = createGhostObjectFromGhostDamageJsonObject(i, jsonArray);
                Room Place = new Room((String) ((JSONObject) jsonArray.get(i)).get("aposento"), ghosts);

                ArrayOfRooms[i] = Place;

                tmp.addVertex(Place);

                i++;
            }

            Room Exit = new Room("exterior", new ArrayUnorderedList<>());
            tmp.addVertex(Exit);

            i = 0;
            long biggestGhost = 0;

            while (i < jsonArray.size()) {

                JSONArray jsonArray2 = (JSONArray) ((JSONObject) jsonArray.get(i)).get("ligacoes");

                int o = 0;

                while (o < jsonArray2.size()) {
                    if ("entrada".equals((String) jsonArray2.get(o))) {
                        ArrayUnorderedList<Fantasma> ghosts = createGhostObjectFromGhostDamageJsonObject(i, jsonArray);
                        if(ghosts.size() != 0) // can not exist ghosts on entrance
                            continue;
                        Room StartLocation = new Room((String) ((JSONObject) jsonArray.get(i)).get("aposento"), ghosts);
                        newMap.setMapStartingLocation(StartLocation);
                        if ((long) ((JSONObject) jsonArray.get(i)).get("fantasma") > biggestGhost) {
                            biggestGhost = (long) ((JSONObject) jsonArray.get(i)).get("fantasma");
                        }
                    } else if ("exterior".equals((String) jsonArray2.get(o))) {
                        ArrayUnorderedList<Fantasma> ghosts = createGhostObjectFromGhostDamageJsonObject(i, jsonArray);
                        Room tmpRoom = new Room((String) ((JSONObject) jsonArray.get(i)).get("aposento"), ghosts);
                        tmp.addEdge(tmpRoom, Exit, 0);
                        if ((long) ((JSONObject) jsonArray.get(i)).get("fantasma") > biggestGhost) {
                            biggestGhost = (long) ((JSONObject) jsonArray.get(i)).get("fantasma");
                        }
                    } else {
                        ArrayUnorderedList<Fantasma> ghosts = createGhostObjectFromGhostDamageJsonObject(i, jsonArray);
                        Room tmpRoom = new Room((String) ((JSONObject) jsonArray.get(i)).get("aposento"), ghosts);
                        if ((long) ((JSONObject) jsonArray.get(i)).get("fantasma") > biggestGhost) {
                            biggestGhost = (long) ((JSONObject) jsonArray.get(i)).get("fantasma");
                        }
                        for (int k = 0; k < ArrayOfRooms.length; k++) {
                            if (ArrayOfRooms[k].getName().equals((String) jsonArray2.get(o))) {
                                tmp.addEdge(tmpRoom, ArrayOfRooms[k], ArrayOfRooms[k].getTotalDamage());
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

    private ArrayUnorderedList<Fantasma> createGhostObjectFromGhostDamageJsonObject(int i, JSONArray jsonArray)
    {
        long ghostDamage = (long) ((JSONObject) jsonArray.get(i)).get("fantasma");
        ArrayUnorderedList<Fantasma> ghosts = new ArrayUnorderedList<>();
        if(ghostDamage != 0)
            ghosts.addToRear(new Fantasma(ghostDamage));

        return ghosts;
    }

    @Override
    public ArrayUnorderedList<String> getAllMapsName() {

        ArrayUnorderedList<String> tmpMaps = new ArrayUnorderedList<String>();

        Iterator mapsIterator = Maps.iterator();
        while(mapsIterator.hasNext())
            tmpMaps.addToRear(((Map)mapsIterator.next()).getName());

        return tmpMaps;
    }

    @Override
    public void deleteLastMap() throws EmptyCollectionException
    {
        Maps.removeLast();
    }

    @Override
    public ArrayUnorderedList<Map> getMaps() {
        return Maps;
    }

    @Override
    public void setMaps(ArrayUnorderedList Maps) {
        this.Maps = Maps;
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
