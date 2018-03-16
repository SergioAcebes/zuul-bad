import java.util.HashMap;
import java.util.Set;
import java.util.ArrayList;
/**
 * Class Room - a room in an adventure game.
 *
 * This class is part of the "World of Zuul" application. 
 * "World of Zuul" is a very simple, text based adventure game.  
 *
 * A "Room" represents one location in the scenery of the game.  It is 
 * connected to other rooms via exits.  The exits are labelled north, 
 * east, south, west.  For each direction, the room stores a reference
 * to the neighboring room, or null if there is no exit in that direction.
 * 
 * @author  Michael KÃ¶lling and David J. Barnes
 * @version 2011.07.31
 */
public class Room 
{
    private String description;
    private HashMap<String, Room> exits;
    private ArrayList<Item> objects;
    /**
     * Create a room described "description". Initially, it has
     * no exits. "description" is something like "a kitchen" or
     * "an open court yard".
     * @param description The room's description.
     */
    public Room(String description) 
    {
        this.description = description;
        exits = new HashMap<>();
        objects = new ArrayList<>();
    }

    public void setExits(String direction, Room nextRoom){
        exits.put(direction, nextRoom);
    }

    public void addItem(String itemName, int itemWeight){
        Item itemNuevo= new Item(itemName, itemWeight);
        objects.add(itemNuevo);;
    }

    public String getAllItems(){
        String listaDeItems = "";
        if(objects.size() <= 0){
            listaDeItems="No hay ningun objeto en esta habitacion.";
        }
        else{
            for(Item objetoDeLaLista : objects){
                listaDeItems += objetoDeLaLista.getItemInfo() + ".\n";
            }
        }
        return listaDeItems;
    }
    

    /**
     * @return The description of the room.
     */
    public String getDescription(){
        return description;
    }

    public Room getExit(String direction){
        return exits.get(direction);
    }

    /**
     * Return a description of the room's exits.
     * For example: "Exits: north east west"
     *
     * @ return A description of the available exits.
     */
    public String getExitString(){
        Set<String> direcciones = exits.keySet();
        String descripcion = "Salidas: ";
        
        for(String direction : direcciones){
            descripcion += direction + " ";
        }
        return descripcion;
    }

    /**
     * Return a long description of this room, of the form:
     *     You are in the 'name of room'
     *     Exits: north west southwest
     * @return A description of the room, including exits.
     */
    public String getLongDescription(){
        return "Tu estas " + description + ".\n" + getExitString() + ".\n" + getAllItems() + "\n" + "¿Hacia donde quieres ir?" ;
    }

}
