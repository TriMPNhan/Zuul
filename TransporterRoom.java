import java.util.Random;
import java.util.HashMap;
/**
 * Subclass of Room
 * 
 * A room with special properties being able to teleport to any location in the game.
 *
 * @author Tri Nhan
 * @version Novemeber 12th, 2017
 */
public class TransporterRoom extends Room
{
    // instance variables to generate random teleportation
    Random rand = new Random();
    private HashMap<String, Room> destinations;
    /**
     * Constructor for objects of class Transporter
     */
    public TransporterRoom()
    {
        super("The transportation room of transportation, you'd better transport yourself with caution");
        destinations = new HashMap<String, Room>();
    }

    /**
     * Retrieves the randomly generated exit
     * 
     * @return the exit
     */
    public Room getExit(String direction)
    {
        // put your code here
        return findRandomRoom();
    }
    
    /**
     * Associates the randomly genereated number with a room
     * 
     * @return the room
     */
    public void setDestination(String roomNum, Room name)
    {
        destinations.put(roomNum, name);
    }
    
    /**
     * Creates a random number and sets it to a string Note: This is only done because of the code given in the assignment otherwise would've kept it an integer to make it simpler
     * 
     * @return the random number
     */
    private Room findRandomRoom()
    {
        int r = rand.nextInt(6);
        String ran = Integer.toString(r);
        
        return destinations.get(ran);
    }
}
