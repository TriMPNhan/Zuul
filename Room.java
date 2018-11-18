import java.util.*;
/**
 * Class Room - a room in an adventure game.
 *
 * This class is part of the "World of Zuul" application. 
 * "World of Zuul" is a very simple, text based adventure game.  
 *
 * A "Room" represents one location in the scenery of the game.  It is 
 * connected to other rooms via exits.  For each existing exit, the room 
 * stores a reference to the neighboring room.
 * 
 * @author Tri Nhan
 * @version November 12th, 2017
 */

public class Room 
{
    private String description;
    private HashMap<String, Room> exits;        // stores exits of this room.
    
    private ArrayList<Item> items;
    
    /**
     * Create a room described "description". Initially, it has
     * no exits. "description" is something like "a kitchen" or
     * "an open court yard".
     * 
     * @param description The room's description.
     */
    public Room(String description) 
    {
        this.description = description;
        exits = new HashMap<String, Room>();
        items = new ArrayList<Item>();
    }

    /**
     * Define an exit from this room.
     * 
     * @param direction The direction of the exit
     * @param neighbour The room to which the exit leads
     */
    public void setExit(String direction, Room neighbour) 
    {
        exits.put(direction, neighbour);
    }

    /**
     * Returns a short description of the room, i.e. the one that
     * was defined in the constructor
     * 
     * @return The short description of the room
     */
    public String getShortDescription()
    {
        return description;
    }

    /**
     * Return a long description of the room in the form:
     *     You are in the kitchen.
     *     Exits: north west
     *     
     * @return A long description of this room
     */
    public String getLongDescription()
    {
        String temp = new String();
        for (Item I : items)
        {
            temp += I.getItem();
        }
        return "You are " + description + ".\n" + getExitString() 
        + ".\n" + temp;
    }

    /**
     * Return a string describing the room's exits, for example
     * "Exits: north west".
     * 
     * @return Details of the room's exits
     */
    private String getExitString()
    {
        String returnString = "Exits:";
        Set<String> keys = exits.keySet();
        for(String exit : keys) {
            returnString += " " + exit;
        }
        return returnString;
    }

    /**
     * Return the room that is reached if we go from this room in direction
     * "direction". If there is no room in that direction, return null.
     * 
     * @param direction The exit's direction
     * @return The room in the given direction
     */
    public Room getExit(String direction) 
    {
        return exits.get(direction);
    }
    
    /**
     * Add new item to the room
     * 
     * @param description, Item label and description
     * @param weight, How heavy the item is
     * @param name, Label shortcut
     */
    public void addItem(String description, double weight, String name)
    {
        items.add(new Item(description, weight, name));
    }
    
    /**
     * Adds a item to the room copied from another created item
     * 
     * @param item, Item that is already made
     */
    public void addItem(Item item)
    {
        if (item != null)       // checks if the item you're trying to add exists or not
        {
            items.add(item);
        }
    }
    
    /**
     * Removes a item from the room
     * 
     * @param if, the label shortcut of the item to be removed
     * @return null if the item does not exist
     */
    public Item removeItem(String id)
    {
        for (int i = 0; i < items.size(); i++)      // Goes through entire set of items in room
        {
            if (items.get(i).getName().equals(id))  // Checks if the item exists in the room
            {
                return items.remove(i);             // Removes item from room and returns it as well
            }
        }
        return null;
    }
}

