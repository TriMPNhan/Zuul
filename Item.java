
/**
 * Item class in Zuul
 * 
 * Items are objects within rooms that have weight,names, and etc..
 *
 * @author Tri Nhan
 * @version 16/10/17
 */
public class Item
{
    // instance variables that make up what a item is
    private String description;
    private double weight;
    private String name;

    /**
     * Constructor for objects of class Item with a set description with a label, weight, and name
     */
    public Item(String description, double weight, String name)
    {
        this.description = description;
        this.weight = weight;
        this.name = name;
    }

    /**
     * returns the complete descriptions of the item
     */
    public String getItem()
    {
        // put your code here
        return "This item weighs: " + weight + "\n" + description + "\n";
    }
    
    /**
     * Returns the shortcut name of the Item
     * 
     * @return label
     */
    public String getName()
    {
        return name;
    }
}
