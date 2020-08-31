/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Jogo;

import Exceptions.ElementNotFoundException;
import Exceptions.EmptyCollectionException;
import Interfaces.CasaAssombradaInterface;
import LinkedStack.LinkedStack;
import UnorederedList.ArrayUnorderedList;
import directednetworkwithmatrix.DirectedNetworkWithMatrix;

import java.io.*;
import java.util.Iterator;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javax.swing.plaf.synth.SynthOptionPaneUI;

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
    private LinkedStack<UndoInfo> undos;
    private int numberOfUndosUsed;

    public CasaAssombrada()
    {
        Maps = new ArrayUnorderedList<>();
        player = new Player("", 0);
        undos = new LinkedStack<UndoInfo>();
        numberOfUndosUsed = 0;
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


            //TODO handle difficulty
            changedGhostsNumberBasedOnDifficulty();


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

                for (int i = 0; i < AdjacentRooms.length; i++)
                    System.out.println((i + 1) + "." + Vertices[AdjacentRooms[i]]);

                System.out.println("To Undo enter \"Undo\"\n");

                BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

                String n = reader.readLine();

                if(n.equalsIgnoreCase("undo"))
                {
                    undoLastMove();
                    continue;
                }

                //save game state for undo
                byte[] undoInfoBytes = serializeObject(new UndoInfo(SelectedMap, player, currentRoom, tmp));
                UndoInfo undoInfo  = (UndoInfo) deserializeObject(undoInfoBytes);
                undos.push(undoInfo);

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
                for (int i = 0; i < tmp.size(); i++)
                {
                    Room room = (Room) tmp.getVertices()[i];
                    ArrayUnorderedList<Fantasma> roomFantasmas = room.getGhosts();
                    if(roomFantasmas.size() == 0 || room.hasGhostBeenPlacedHereInThisPlay())
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

                        //cannot send ghosts to exterior
                        if(adjacentRoom.getName().equals("exterior"))
                            continue;

                        verticesThatComplyWithConditions.addToRear(adjacentRoom);
                    }

                    Iterator roomFantasmasIterator = roomFantasmas.iterator();
                    while(roomFantasmasIterator.hasNext() && verticesThatComplyWithConditions.size() != 0)
                    {
                        Fantasma fantasma = (Fantasma) roomFantasmasIterator.next();
                        int chosenRoomIndex = (int) (Math.random() * verticesThatComplyWithConditions.size());
                        Iterator verticesThatComplyWithConditionsIterator = verticesThatComplyWithConditions.iterator();
                        for(int k = 0; k < chosenRoomIndex ; k++)
                            verticesThatComplyWithConditionsIterator.next();
                        Room chosenRoom = (Room) verticesThatComplyWithConditionsIterator.next();

                        //troca de uma room para outra
                        chosenRoom.addGhost(fantasma);
                        room.removeGhost(fantasma);

                        int[] previousVerticesIndexesForRoom =  tmp.GetIndexOfPreviousVertices(room);
                        for (int j = 0; j < previousVerticesIndexesForRoom.length; j++)
                        {
                            tmp.removeEdge((Room) tmp.getVertices()[previousVerticesIndexesForRoom[j]], room);
                            tmp.addEdge((Room) tmp.getVertices()[previousVerticesIndexesForRoom[j]], room, room.getTotalDamage());
                        }

                        // peso para a nova edge entre room e chosenRoom cujo peso é igual aos fanatsama em chosen room
                        int[] previousVerticesIndexesChosenRoom =  tmp.GetIndexOfPreviousVertices(chosenRoom);
                        for (int j = 0; j < previousVerticesIndexesChosenRoom.length; j++)
                        {
                            tmp.removeEdge((Room) tmp.getVertices()[previousVerticesIndexesChosenRoom[j]], chosenRoom);
                            tmp.addEdge((Room) tmp.getVertices()[previousVerticesIndexesChosenRoom[j]], chosenRoom, chosenRoom.getTotalDamage());
                        }

                        chosenRoom.setHasGhostBeenPlacedHereInThisPlay(true);

                        //retirar o que deixou de cumprir a condição de não ter lá um ghost
                        verticesThatComplyWithConditions.remove(chosenRoom);
                    }
                }

                //back all rooms hasGhostBeenPlacedHereInThisPlay field to base value false
                setAllRoomsHasGhostBeenPlacedHereInThisPlayToBaseValue();
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

    private Object deserializeObject(byte[] yourBytes) throws IOException
    {
        Object result = null;
        ByteArrayInputStream bis = new ByteArrayInputStream(yourBytes);
        ObjectInput in = null;
        try {
            in = new ObjectInputStream(bis);
            result = in.readObject();
        } catch (ClassNotFoundException e)
        {
            e.printStackTrace();
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (IOException ex) {
                // ignore close exception
            }
        }
        return result;
    }

    private byte[] serializeObject(Object obj) throws IOException
    {
        byte[] yourBytes;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream out = null;
        try {
            out = new ObjectOutputStream(bos);
            out.writeObject(obj);
            out.flush();
           yourBytes = bos.toByteArray();
        } finally {
            try {
                bos.close();
            } catch (IOException ex) {
                // ignore close exception
            }
        }
        return yourBytes;
    }

    private void changedGhostsNumberBasedOnDifficulty()
    {
        for (int i = 0; i < tmp.size(); i++)
        {
            Room room = (Room) tmp.getVertices()[i];
            ArrayUnorderedList<Fantasma> roomFantasmas = room.getGhosts();
            if(roomFantasmas.size() == 0)
                continue;
            long ghostDamage = roomFantasmas.first().getDamage();
            for (int j = 0; j < Difficulty - 1; j++)
                roomFantasmas.addToRear(new Fantasma(ghostDamage));

            int[] previousVerticesIndexesForRoom =  tmp.GetIndexOfPreviousVertices(room);
            for (int j = 0; j < previousVerticesIndexesForRoom.length; j++)
            {
                tmp.removeEdge((Room) tmp.getVertices()[previousVerticesIndexesForRoom[j]], room);
                tmp.addEdge((Room) tmp.getVertices()[previousVerticesIndexesForRoom[j]], room, room.getTotalDamage());
            }
        }
    }

    public void undoLastMove()
    {
        numberOfUndosUsed++;

        //check it can continue based on set difficulty
        boolean undosExpired = false;
        switch (Difficulty)
        {
            case 1:
                if(numberOfUndosUsed > 5)
                    undosExpired = true;
                break;
            case 2:
                if(numberOfUndosUsed > 3)
                    undosExpired = true;
                break;
            case 3:
                if(numberOfUndosUsed > 1)
                    undosExpired = true;
                break;
        }
        if(undosExpired)
        {
            System.out.println("Todos os undos já foram usados \n");
            return;
        }

        try
        {
            UndoInfo undoInfo = undos.pop();
            this.SelectedMap = undoInfo.getSelectedMap();
            this.currentRoom = undoInfo.getCurrentRoom();
            this.player = undoInfo.getPlayer();
            this.tmp = undoInfo.getTmp();
        } catch (EmptyCollectionException e)
        {
            numberOfUndosUsed--;
            return;
        }
    }

    private void setAllRoomsHasGhostBeenPlacedHereInThisPlayToBaseValue()
    {
        for (int i = 0; i < tmp.size(); i++)
        {
            Room room = (Room) tmp.getVertices()[i];
            room.setHasGhostBeenPlacedHereInThisPlay(false);
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

        if(mapa.getMapStartingLocation() == null || mapa.getMapStartingLocation().getGhosts().size() != 0)
        {
            System.out.println("Não podem existir fantasmas na entrada");
            return false;
        }

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

            for (int i = 0; i < jsonArray.size(); i++)
            {
                ArrayUnorderedList<Fantasma> ghosts = createGhostObjectFromGhostDamageJsonObject(i, jsonArray);
                Room Place = new Room((String) ((JSONObject) jsonArray.get(i)).get("aposento"), ghosts);

                ArrayOfRooms[i] = Place;

                tmp.addVertex(Place);
            }

            Room Exit = new Room("exterior", new ArrayUnorderedList<>());
            tmp.addVertex(Exit);

            long biggestGhost = 0;

            for (int i = 0; i < jsonArray.size(); i++)
            {
                JSONArray jsonArray2 = (JSONArray) ((JSONObject) jsonArray.get(i)).get("ligacoes");

                for (int j = 0; j < jsonArray2.size(); j++)
                {
                    if ("entrada".equals((String) jsonArray2.get(j))) {
                        ArrayUnorderedList<Fantasma> ghosts = createGhostObjectFromGhostDamageJsonObject(i, jsonArray);
                        if(ghosts.size() != 0) // can not exist ghosts on entrance
                            continue;
                        Room StartLocation = new Room((String) ((JSONObject) jsonArray.get(i)).get("aposento"), ghosts);
                        newMap.setMapStartingLocation(StartLocation);
                        if ((long) ((JSONObject) jsonArray.get(i)).get("fantasma") > biggestGhost) {
                            biggestGhost = (long) ((JSONObject) jsonArray.get(i)).get("fantasma");
                        }
                    } else if ("exterior".equals((String) jsonArray2.get(j))) {
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
                            if (ArrayOfRooms[k].getName().equals((String) jsonArray2.get(j))) {
                                tmp.addEdge(tmpRoom, ArrayOfRooms[k], ArrayOfRooms[k].getTotalDamage());
                                break;
                            }
                        }
                    }
                }
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
