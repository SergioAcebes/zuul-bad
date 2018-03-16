/**
 *  This class is the main class of the "World of Zuul" application. 
 *  "World of Zuul" is a very simple, text based adventure game.  Users 
 *  can walk around some scenery. That's all. It should really be extended 
 *  to make it more interesting!
 * 
 *  To play this game, create an instance of this class and call the "play"
 *  method.
 * 
 *  This main class creates and initialises all the others: it creates all
 *  rooms, creates the parser and starts the game.  It also evaluates and
 *  executes the commands that the parser returns.
 * 
 * @author  Michael KÃ¶lling and David J. Barnes
 * @version 2011.07.31
 */

public class Game 
{
    private Parser parser;
    private Room currentRoom;

    /**
     * Create the game and initialise its internal map.
     */
    public Game() 
    {
        createRooms();
        parser = new Parser();
    }

    /**
     * Create all the rooms and link their exits together.
     */
    private void createRooms()
    {
        Room entrada, analiticas, salaDeEspera, informacion, urgencias, cafeteria, pediatria, maternidad,neurologia,cardiologia;

        // create the rooms
        entrada = new Room("en la entrada del hospital");
        informacion = new Room("en informacion");
        analiticas = new Room("en la sala de analiticas");
        salaDeEspera = new Room("en la sala de espera");
        urgencias = new Room("en la salda de urgecias");
        cafeteria = new Room("en la cafeteria");
        pediatria = new Room("en la sala de pediatria");
        maternidad = new Room("en la sala de maternidad");
        neurologia = new Room("en la sala de neurologia");
        cardiologia = new Room("en la sala de cardiologia");

        // initialise room exits
        entrada.setExits("north",informacion);
        entrada.setExits("east",urgencias);
        entrada.setExits("weast",analiticas);

        informacion.setExits("north",neurologia);
        informacion.setExits("south",entrada);
        informacion.setExits("weast",salaDeEspera);

        urgencias.setExits("north",cafeteria);
        urgencias.setExits("weast",entrada);

        cafeteria.setExits("south",urgencias);

        analiticas.setExits("north",salaDeEspera);
        analiticas.setExits("east",entrada);

        salaDeEspera.setExits("north",pediatria);
        salaDeEspera.setExits("east",informacion);
        salaDeEspera.setExits("south",analiticas);
        salaDeEspera.setExits("northEast",neurologia);

        pediatria.setExits("north",maternidad);
        pediatria.setExits("south",salaDeEspera);

        maternidad.setExits("east",cardiologia);
        maternidad.setExits("south",pediatria);

        cardiologia.setExits("weast",maternidad);

        neurologia.setExits("south",informacion);
        neurologia.setExits("southEast",urgencias);
        
        //Añadir objetos.
        entrada.addItem("maquina 24h",400);
        entrada.addItem("mesa",20);
         entrada.addItem("silla",7);

        currentRoom = entrada;  // start game outside
    }

    /**
     *  Main play routine.  Loops until end of play.
     */
    public void play() 
    {            
        printWelcome();

        // Enter the main command loop.  Here we repeatedly read commands and
        // execute them until the game is over.

        boolean finished = false;
        while (! finished) {
            Command command = parser.getCommand();
            finished = processCommand(command);
        }
        System.out.println("Gracias por jugar, HASTA LA VISTA!");
    }

    /**
     * Print out the opening message for the player.
     */
    private void printWelcome()
    {
        System.out.println();
        System.out.println("Bienvenido al hospital,veo que andas apurado con la hora de tu cita...");
        System.out.println("Las salas del hospital comunican a base de pasillos que en ocasiones son un lio.");
        System.out.println("ten cuidado no te pierdas!!");
        System.out.println("Escribe 'help' si necesitas ayuda.");
        System.out.println();
        printLocationInfo();
    }

    /**
     * Given a command, process (that is: execute) the command.
     * @param command The command to be processed.
     * @return true If the command ends the game, false otherwise.
     */
    private boolean processCommand(Command command) 
    {
        boolean wantToQuit = false;

        if(command.isUnknown()) {
            System.out.println("No se que quieres decir...");
            return false;
        }

        String commandWord = command.getCommandWord();
        if (commandWord.equals("help")) {
            printHelp();
        }
        else if (commandWord.equals("go")) {
            goRoom(command);
        }
        else if (commandWord.equals("look")) {
            look();
        }
        else if (commandWord.equals("eat")) {
            eat();
        }
        else if (commandWord.equals("quit")) {
            wantToQuit = quit(command);
        }

        return wantToQuit;
    }

    // implementations of user commands:

    /**
     * Print out some help information.
     * Here we print some stupid, cryptic message and a list of the 
     * command words.
     */
    private void printHelp() 
    {
        System.out.println("Llegarás tarde a tu cita de consulta. DATE PRISA!!");
        System.out.println("Encuentra la sala de cardiologia lo antes posible");
        System.out.println();
        System.out.println("Tus comandos disponibles son: ");
        System.out.println(parser.showCommands());
    }

    /** 
     * Try to go in one direction. If there is an exit, enter
     * the new room, otherwise print an error message.
     */
    private void goRoom(Command command) 
    {
        if(!command.hasSecondWord()) {
            // if there is no second word, we don't know where to go...
            System.out.println("Go where?");
            return;
        }

        String direction = command.getSecondWord();

        // Try to leave current room.
        Room nextRoom = currentRoom.getExit(direction);

        if (nextRoom == null) {
            System.out.println("Hacia ese lado no hay pasillo!");
        }
        else {
            currentRoom = nextRoom;
            printLocationInfo();
        }
    }

    /** 
     * "Quit" was entered. Check the rest of the command to see
     * whether we really quit the game.
     * @return true, if this command quits the game, false otherwise.
     */
    private boolean quit(Command command) 
    {
        if(command.hasSecondWord()) {
            System.out.println("Quit what?");
            return false;
        }
        else {
            return true;  // signal that we want to quit
        }
    }

    /**
     * Metodo que nos permitira saber la localizacion del personaje. 
     */
    private void printLocationInfo(){
        System.out.println(currentRoom.getLongDescription());
        System.out.println();
    }

    /**
     * Metodo que nos permitira ver la informacion de la habitacion.
     */
    private void look() 
    {
        System.out.println(currentRoom.getLongDescription());
    }
    
    /**
     * Metodo que nos permitira comer.
     */
    private void eat() 
    {
        System.out.println("Acabas de comer ya no tienes hambre");
    }
    
}

