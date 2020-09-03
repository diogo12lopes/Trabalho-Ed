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
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Iterator;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import static Utils.Utils.deserializeObject;
import static Utils.Utils.serializeObject;

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

    /**
     * Cria uma representação base do jogo. Todos os mapas conhecidos são carregados para memória do programa.
     */
    public CasaAssombrada()
    {
        loadKnownMaps();
        player = new Player("", 0);
        undos = new LinkedStack<UndoInfo>();
        numberOfUndosUsed = 0;
    }

    /**
     * Verifica a existência da habilidade de teletransporte e usa-a para alterar a posição atual do jogador.
     * @return true se o teletransporte foi possível. Caso contrário retorna false.
     */
    public boolean checkAndUseTeletransporte()
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

    /**
     *  Escolhe de forma aleatória uma divisão de uma lista de divisões
     *
     * @param rooms lista de divisões disponíveis
     * @return a divisão escolhida
     */
    private Room getRandomRoomFromArray(ArrayUnorderedList<Room> rooms)
    {
        int chosenRoomIndex = (int) (Math.random() * rooms.size());
        Iterator roomsIterator = rooms.iterator();
        for(int k = 0; k < chosenRoomIndex ; k++)
            roomsIterator.next();
        return (Room) roomsIterator.next();
    }

    /**
     * Escolhe todas as divisões onde não existem fantasmas.
     * @return lista com todas as divisões onde não existem fantasmas
     */
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

    /**
     * Metodo responsável por ambos os modos de jogo, manual e simulação. Este método desencadeia toda a lógica de jogo no mapa selecionado (SelectedMap)
     * @throws IOException
     * @throws EmptyCollectionException
     * @throws ElementNotFoundException
     */
    @Override
    public void play() throws IOException, EmptyCollectionException, ElementNotFoundException
    {
        player.setLifePoints(SelectedMap.getInitialLifePoints());
        if (GameMode.equals("manual"))
            OnGameModeManual();
        else if (GameMode.equals("simulation"))
            OnGameModeSimulation();
        else
            System.out.println("There is no such game mode");
    }

    /**
     * Método responsável por toda a lógica de jogo para o modo manual.
     * @throws IOException
     * @throws ElementNotFoundException
     * @throws EmptyCollectionException
     */
    private void OnGameModeManual() throws IOException, ElementNotFoundException, EmptyCollectionException
    {
        currentRoom = new Room(SelectedMap.getMapStartingLocation().getName(), SelectedMap.getMapStartingLocation().getGhosts());

        tmp = SelectedMap.getGraph();

        //alteração da dificuldade através do aumento do número de fantasmas e respetivos peso no grafo
        changedGhostsNumberBasedOnDifficulty();

        // colocação do powerup escolhido de forma aleatória numa posição aleatória do mapa
        putRandomPowerUpOnRoom();

        //ciclo de jogo
        while (!(currentRoom.getName().equals("exterior")) && player.getLifePoints() > 0)
        {
            // verificação se atingiu powerup
            if(currentRoom.getPowerUp() != PowerUp.None)
            {
                System.out.println("Congratz you got a power up :" + currentRoom.getPowerUp());
                setPlayerPowerUp(currentRoom.getPowerUp());
            }

            // mostrar estado atual do jogo
            System.out.println("Current Room:" + currentRoom.getName() + "\n");
            VisualizeMap();
            System.out.println("Current life points: "+ player.getLifePoints());

            // escolha do a próxima jogada do jogador
            int[] AdjacentRooms = tmp.GetIndexOfAdjVertices(currentRoom);
            Object[] Vertices = tmp.getVertices();
            int i = 0;
            int optionChosen = -1;
            boolean validInput = false;
            while(validInput == false)
             {
                 i=0;
                System.out.println("Wich room to go:\n");

                for (; i < AdjacentRooms.length; i++)
                    System.out.println((i + 1) + "." + Vertices[AdjacentRooms[i]]);

                System.out.println((i + 1) + ".undo");

                BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
                String n = reader.readLine();
                try{
                    optionChosen = Integer.parseInt(n);
                    if (optionChosen > 0 && optionChosen <= i+1)
                        validInput = true;

                }
                catch (NumberFormatException e){
                    System.out.println("Input a valid number (1-"+ (i+1) + ")");
                }

            }

            // jogada escolhida igual a undo da última jogada
            if(optionChosen == i+1)
            {
                undoLastMove();
                continue;
            }

            //save game state for undo
            byte[] undoInfoBytes = serializeObject(new UndoInfo(SelectedMap, player, currentRoom, tmp));
            UndoInfo undoInfo  = (UndoInfo) deserializeObject(undoInfoBytes);
            undos.push(undoInfo);

            // set da posição na nova divisão
            Room tempRoom = currentRoom;
            currentRoom = ((Room) Vertices[AdjacentRooms[optionChosen - 1]]);

            //lógica de verficar se perde pontos
            long totalInflictedDamage = (long) tmp.getWeightMatrix()[tmp.getIndex(tempRoom)][tmp.getIndex(currentRoom)];
            if(totalInflictedDamage > 0)
            {
                if(checkAndUseTeletransporte())
                {
                    System.out.println("You got saved by teletranporte");
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

            //lógica de mover fantasmas
            for (i = 0; i < tmp.size(); i++)
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

                    // peso para a nova edge entre os antecessores de room e a room é igual aos fantasmas em room
                    int[] previousVerticesIndexesForRoom =  tmp.GetIndexOfPreviousVertices(room);
                    for (int j = 0; j < previousVerticesIndexesForRoom.length; j++)
                    {
                        tmp.removeEdge((Room) tmp.getVertices()[previousVerticesIndexesForRoom[j]], room);
                        tmp.addEdge((Room) tmp.getVertices()[previousVerticesIndexesForRoom[j]], room, room.getTotalDamage());
                    }

                    // peso para a nova edge entre os antecessores de chosenRoom e a chosenRoom é igual aos fantasmas em chosenRoom
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

            setAllRoomsHasGhostBeenPlacedHereInThisPlayToBaseValue();
        }

        // verificação dos pontos de vida do jogador para decisão de ganhar ou perder
        if (player.getLifePoints() > 0)
            OnPlayerWin();
        else
            OnPlayerLose();
    }

    /**
     * Método responsável por lidar com a derrota de um jogador. Isto implica a indicação da derrota
     * e a colocação do mapa no seu estado original.
     * @throws IOException
     */
    private void OnPlayerLose()
    {
        System.out.println("You just lost the game!");
        loadKnownMaps(); // reset map to original state
    }

    /**
     * Método responsável por lidar com a vitória de um jogador. Isto implica a indicação da vitória, entrada do jogador para uma dada posição no leaderboard
     * e a colocação do mapa no seu estado original.
     * @throws IOException
     */
    private void OnPlayerWin() throws IOException
    {
        System.out.println("You just won the game!\nPlease gives you nickname:");

        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String n = reader.readLine();

        Player InfoToTheLeaderBoard = new Player(n, player.getLifePoints());

        loadKnownMaps();
        Iterator mapsIterator = Maps.iterator();
        while (mapsIterator.hasNext())
        {
            Map currMap = (Map)mapsIterator.next();
            if(currMap.getName().equals(SelectedMap.getName()))
                currMap.AddToTheLeaderboard(InfoToTheLeaderBoard);
        }
        saveMaps();

        System.out.println("You can know check your leaderboard and see the top players!");
    }

    /**
     * Método responsável por toda a lógica de jogo para o modo simulação.
     * @throws EmptyCollectionException
     */
    private void OnGameModeSimulation() throws EmptyCollectionException
    {
        Iterator it;

        Room Exit = new Room("exterior", new ArrayUnorderedList<>());

        it = SelectedMap.getGraph().iteratorShortestPathWeight(SelectedMap.getMapStartingLocation(), Exit);

        while (it.hasNext()) {

            System.out.print(it.next().toString() + "-> ");

        }
        System.out.print("\n");
    }

    /**
     * Método responsável por alterar a dificuldade através do aumento do número de fantasmas numa dada divisão e respetivos peso no grafo
     */
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

    /**
     * Reverte o estado do jogo para aquele anterior á última jogada do jogador, verificando se o número máximo de undos para aquele jogador ainda não foi atingido.
     */
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

    /**
     * Set flag que indica se uma dada divisão já teve um fanatasma lá colocado na jogada atual para o seu valor padrão, false.
     */
    private void setAllRoomsHasGhostBeenPlacedHereInThisPlayToBaseValue()
    {
        for (int i = 0; i < tmp.size(); i++)
        {
            Room room = (Room) tmp.getVertices()[i];
            room.setHasGhostBeenPlacedHereInThisPlay(false);
        }
    }

    /**
     * Coloca um powerup escolhido aleatoriamente (de um grupo de três, pontos de vida, escudo e teletransporte) numa divisão escolhida aleatoriamente do mapa
     */
    private void putRandomPowerUpOnRoom()
    {
        ArrayUnorderedList<Room> roomsWithoutFantasmas = getRoomsWithoutGhosts();
        Room randomRoomChosen = getRandomRoomFromArray(roomsWithoutFantasmas);
        int randomNumberForPowerUpType = (int) ((Math.random() * 3) + 1);
        switch (randomNumberForPowerUpType)
        {
            case 1:
                randomRoomChosen.setPowerUp(PowerUp.LifePoints);
                break;
            case 2:
                randomRoomChosen.setPowerUp(PowerUp.Escudo);
                break;
            case 3:
                randomRoomChosen.setPowerUp(PowerUp.Teletransporte);
                break;
        }
    }

    /**
     * Adiciona o powerup recebido ao jogador
     * @param powerUp o powerup recebido
     */
    private void setPlayerPowerUp(PowerUp powerUp)
    {
        switch (powerUp)
        {
            case LifePoints:
                player.setLifePoints(player.getLifePoints() + 25);
                break;
            case Escudo:
                int randomNumberForShield = (int) ((Math.random() * getSelectedMap().getMaxDivisionDamage()) + 1);
                player.setEscudo(randomNumberForShield);
                break;
            case Teletransporte:
                player.setTeletransporte(true);
                break;
        }
    }

    /**
     * Verifica a validade de um mapa. Verifica se existem fantasmas na entrada (não podem existir), se existe pelo menos uma saída
     * e se é possível atingir uma das saídas com pontos de vida positivos.
     * @param mapa o mapa a ser testado
     * @return true se o mapa for válido. Caso contrário retorna false
     * @throws EmptyCollectionException
     */
    @Override
    public boolean CheckMap(Map mapa) throws EmptyCollectionException {
        long LifePoints = mapa.getInitialLifePoints();
        Room Exit;

        if(mapa.getMapStartingLocation() == null || mapa.getMapStartingLocation().getGhosts().size() != 0)
        {
            System.out.println("There can not be ghost in the entrance");
            return false;
        }

        for(Object vertice : tmp.getVertices())
        {
            Room currVerticeRoom = (Room) vertice;

            if(currVerticeRoom.getName().equals("exterior") == false)
                continue;

            Exit = currVerticeRoom;
            Iterator it = mapa.getGraph().iteratorShortestPathWeight(mapa.getMapStartingLocation(), Exit);
            
            if(it.next() == null)
            {
                System.out.println("No path beetween entrance and exit ");
                return false;
            }
            while (it.hasNext())
                LifePoints -= ((Room) it.next()).getTotalDamage();

            if (LifePoints < 0)
            {
                System.out.println("It is impossible to complete this map alive");
                return false;
            }

            return true;
        }

        return false;
    }

    /**
     * Seleciona o modo de jogo atual como o modo de jogo recebido como parâmetro
     * @param Gamemode modo de jogo
     */
    @Override
    public void Select_Game_Mode(String Gamemode) {
        this.GameMode = Gamemode;
    }

    /**
     * Seleciona o mapa de jogo atual como o mapa da lista Maps na posição MapChoice
     * @param MapChoice posição do mapa na lista Maps
     */
    @Override
    public void Select_Map(int MapChoice) {
        Iterator iterator = Maps.iterator();
        for (int i = 0; i < MapChoice; i++)
            iterator.next();
        this.SelectedMap = (Map) iterator.next();
    }

    /**
     * Seleciona a dificuldade de jogo atual como a dificuldade recebida como parâmetro
     * @param DifficultyChoice valor da dificuldade
     */
    @Override
    public void Select_Difficulty(int DifficultyChoice) {
        this.Difficulty = DifficultyChoice;
    }

    /**
     * Mostra a lista ordenada de melhores jogadores para um dado mapa cujo nome é dado por mapName
     * @param mapName nome do mapa escolhido
     */
    @Override
    public void VisualizeLeaderboard(String mapName) {

        Iterator mapLeaderboardIterator = null;
        Iterator mapsIterator = Maps.iterator();
        while(mapsIterator.hasNext())
        {
            Map currMap = (Map)mapsIterator.next();
            if(currMap.getName().equals(mapName))
            {
                mapLeaderboardIterator = currMap.getMapLeaderboard().iterator();
                break;
            }
        }

        int i = 0;
        while (mapLeaderboardIterator.hasNext())
        {
            System.out.println(i + 1 + "º Lugar:\n" + mapLeaderboardIterator.next().toString());
            i++;
        }
    }

    /**
     * Mostra o mapa atualmente selecionado
     */
    @Override
    public void VisualizeMap()
    {
        System.out.println(SelectedMap.getGraph().toString());
    }

    /**
     * Carrega um mapa para memória a partir da sua representação json em disco
     * @param path caminho para o ficheiro json que contém o mapa
     * @throws IOException
     * @throws ParseException
     */
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
            System.out.println("Map already exists");
        }

    }

    /**
     * Cria um objeto que representa um fantasma a partir da sua representação json
     * @param index index do fantasma no array de json
     * @param jsonArray array de json
     * @return lista com apenas um fantasma
     */
    private ArrayUnorderedList<Fantasma> createGhostObjectFromGhostDamageJsonObject(int index, JSONArray jsonArray)
    {
        long ghostDamage = (long) ((JSONObject) jsonArray.get(index)).get("fantasma");
        ArrayUnorderedList<Fantasma> ghosts = new ArrayUnorderedList<>();
        if(ghostDamage != 0)
            ghosts.addToRear(new Fantasma(ghostDamage));

        return ghosts;
    }

    /**
     * Encontra e retorna todos os nomes dos mapas carregados em memória
     * @return lista de nomes do mapas carregados em memória
     */
    @Override
    public ArrayUnorderedList<String> getAllMapsName() {

        ArrayUnorderedList<String> tmpMaps = new ArrayUnorderedList<String>();

        Iterator mapsIterator = Maps.iterator();
        while(mapsIterator.hasNext())
            tmpMaps.addToRear(((Map)mapsIterator.next()).getName());

        return tmpMaps;
    }

    /**
     * Apaga o último mapa adicionado à lista de mapas
     * @throws EmptyCollectionException
     */
    @Override
    public void deleteLastMap() throws EmptyCollectionException
    {
        Maps.removeLast();
    }

    /**
     * Devolve a lista de mapas carregados em memória
     * @return lista de mapas carregados em memória
     */
    @Override
    public ArrayUnorderedList<Map> getMaps() {
        return Maps;
    }

    /**
     * Devolve o mapa atualmente selecionado
     * @return o mapa atualmente selecionado
     */
    @Override
    public Map getSelectedMap() {
        return SelectedMap;
    }

    /**
     * Guarda os mapas carregados em memória para o ficheiro de memória persistente em disco chamado "maps"
     */
    @Override
    public void saveMaps()
    {
        try
        {
            byte[] mapsBytes = serializeObject(Maps);
            Files.write(Paths.get("maps"), mapsBytes);
        } catch (IOException e)
        {
            System.out.println("Error saving map! The game will continue without saving");
        }
    }

    /**
     * Carrega os mapas guardados num ficheiro em disco chamado "maps" para memória
     */
    @Override
    public void loadKnownMaps()
    {
        try
        {
            byte[] mapsBytes = Files.readAllBytes(Paths.get("maps"));
            if(mapsBytes == null)
                Maps = new ArrayUnorderedList<>();
            Maps = (ArrayUnorderedList<Map>) deserializeObject(mapsBytes);
        } catch (IOException e)
        {
            System.out.println("No known maps");
            Maps = new ArrayUnorderedList<>();
        }
    }

}
