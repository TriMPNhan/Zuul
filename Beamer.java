
/**
 * Subclass of Item
 * 
 * A specific Item used to teleport the User to the saved location where it was charges
 * 
 * @author Tri Nhan
 * @version November 12th, 2017
 */
public class Beamer extends Item
{
    // instance variables determing state of Beamer
    private Room saved;
    private boolean charge;

    /**
     * Constructor for objects of class Beamer
     * 
     * @param name, Beamer's specific name to avoid confusion between beamers
     */
    public Beamer(String name)
    {
        super(name + ": A device used to teleport to the location where it was charged", 5.0, name);
        saved = null;
        charge = false;
    }
    
    /**
     * Charge the beamer and save the current room
     * 
     * @param toSave, The room to be saved
     */
    public void charge(Room toSave)
    {
        if (saved != null)  //Checks if this beamer has already been charged before
        {
            System.out.println("You have already charged the beamer!");
        }
        //Confirmation of charge and setting the charge
        System.out.println("Beamer is charged!");
        saved = toSave;
        charge = true;
    }
    
    /**
     * Checks if the beamer is fireable
     * 
     * @return whether the beamer is charged or not
     */
    public boolean fire()
    {
        if (charge == false)
        {
            System.out.println("Your Beamer is not charged!");
            return false;
        }
        return true;
    }
    
    /**
     * Gives the charge of the beamer
     */
    public boolean getCharge()
    {
        return charge;
    }
    
    /**
     * Gives the room saved of the beamer
     */
    public Room getSaved()
    {
        return saved;
    }
}
