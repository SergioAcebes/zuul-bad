
/**
 * Clase que nos dejara introducir objetos en nuestro juego
 *
 * @author (SergioAcebes)
 * @version (16/3/2018)
 */
public class Item
{
    
    private String itemName;
    private int itemWeight;
    private boolean hayEspacio;
    /**
     * Constructor for objects of class item
     */
    public Item(String nombre,int peso,boolean hayEspacio)
    {
        itemName = nombre;
        itemWeight = peso;
        this.hayEspacio = hayEspacio;
    }

    /**
     * Metodo que devuelve el nombre del item.
     * @return - nombre del item.
     */
    public String getItemName()
    {
        return itemName;
    }
    
    /**
     * Metodo que devuelve el peso del item.
     * @return - peso del item.
     */
    public int getItemWeight()
    {
        return itemWeight;
    }
    
    public boolean getHayEspacio(){
        return hayEspacio;
    }
    
    /**
     * Metodo que devuelve la informacion del item.
     * @return - informacion del item.
     */
    public String getItemInfo()
    {
        String hayItem = "Hay un/a ";
        if(itemName != null){
            hayItem += itemName + " de " + itemWeight + " kilos";
        }
        else {
            hayItem = "";
        }
        return hayItem;
    }
    
}
