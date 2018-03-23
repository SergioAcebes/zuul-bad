import java.util.Stack;
import java.util.ArrayList;
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
    private Stack<Room> retroceder;
    private static final int CARGAMAXIMA = 300;
    private int pesoActual;
    private ArrayList<Item> items;
    /**
     * Create the game and initialise its internal map.
     */
    public Game() 
    {
        createRooms();
        parser = new Parser();
        retroceder = new Stack<>();
        items = new ArrayList<>();
        pesoActual = 0; 
    }

    /**
     * Create all the rooms and link their exits together.
     */
    private void createRooms()
    {
        Room entrada, analiticas, salaDeEspera, informacion, urgencias, cafeteria, pediatria, maternidad,neurologia,cardiologia;
        Item maquina,mesa,silla,boligrafo,corazon,bisturi,bebe,coche,cocacola;
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
        maquina = new Item("maquina 24h",400,true);
        mesa = new Item("mesa",20,true);
        silla = new Item("silla",7,true);
        boligrafo = new Item("boligrafo",1,true);
        corazon = new Item("corazon",2,true);
        bisturi = new Item("bisturi",10,true);
        cocacola = new Item("cocacola",5,true);
        bebe = new Item("bebe",15,true);
        coche = new Item("coche de jugete",50,true);
        
        
        entrada.addItem(maquina);
        entrada.addItem(mesa);
        entrada.addItem(silla);
        
        informacion.addItem(boligrafo);
        
        cardiologia.addItem(corazon);
        cardiologia.addItem(bisturi);
        
        cafeteria.addItem(cocacola);
        
        maternidad.addItem(bebe);
        
        pediatria.addItem(coche);

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
        else if (commandWord.equals("back")) {
            back();
        }
        else if (commandWord.equals("take")) {
            take(command.getSecondWord());
        }
        else if (commandWord.equals("drop")) {
            drop(command.getSecondWord());
        }
        else if (commandWord.equals("items")) {
            items();
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
            retroceder.push(currentRoom);
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
        printLocationInfo();
    }

    /**
     * Metodo que nos permitira comer.
     */
    private void eat() 
    {
        System.out.println("Acabas de comer ya no tienes hambre.");
    }

    /**
     * Metodo que nos permitira volver a la sala anterior.
     */
    private void back() 
    {
        if (!retroceder.empty()){
            currentRoom = retroceder.pop();
            printLocationInfo();
        }
    }
    
    /**
     * Metodo que nos permitira coger un objeto de una sala y almacenarlo.
     */
      private void addItemMochila(Item item)
    {
        items.add(item);
    } 
    
    private void take(String nombre) 
    {   
        ArrayList<Item> itemsRoom = currentRoom.getArrayListItems();
        Item item = null;
       for(int i=0;i < itemsRoom.size(); i++)
        {
            if(itemsRoom.get(i).getItemName().equals(nombre))
            {
                item= itemsRoom.get(i);
            }
        }
        if(item.getHayEspacio() && item != null){
            int pesoTotal = pesoActual + item.getItemWeight();
            boolean buscando = true;
            int contador = 0;            
            if(pesoTotal <= CARGAMAXIMA){
                items.add(item);
                pesoActual += item.getItemWeight();
                itemsRoom.remove(item);
                System.out.println("Añadido a la mochila un/una" + nombre);
            }
            else{
                System.out.println("No puedes carga tanto la mochila");
            }            
       }
        else{
            System.out.println("No puedes coger este objeto,pesa demasiado");
       }
    }
    
     private void drop(String nombre)
    {                 
        if(items.size() > 0){
            boolean sePuedeCoger = true;
            int contador = 0;
            Item item = null;
            while(sePuedeCoger)
            {
                if(items.get(contador).getItemName().equals(nombre)){
                    item = items.get(contador);
                    currentRoom.addItem(item);
                    sePuedeCoger = false;
                    pesoActual -= item.getItemWeight();
                    items.remove(contador);
                }
                contador++;
            }
            System.out.println("Sacado de la mochila  " + nombre);
        }
        else
       {
            System.out.println("No tienes ningun objeto");
       }
    }   
    
    private void items()
    {   
        String texto = "Tienes guardado ";
        if(items.size() > 0){
            for(Item objeto : items)
            {
                texto += objeto.getItemName() + " ";         
            }
            texto += "\n"+ "El peso de la mochila es :" + pesoActual + "kilos";
            System.out.println(texto);
        }
        else{
            System.out.println("No tienes ningun objeto");
        }
    }
}

