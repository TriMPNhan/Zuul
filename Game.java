import java.util.*;
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
 * @author  Michael Kolling and David J. Barnes
 * @version 2006.03.30
 * 
 * @author Lynn Marshall
 * @version October 21, 2012
 */

public class Game 
{
    private Parser parser;
    private Room currentRoom;
    private Room lastRoom;
    private Stack<Room> bStack;
    private Item itemHeld;
    private int energy;
        
    /**
     * Create the game and initialise its internal map.
     */
    public Game() 
    {
        energy = 0;
        bStack = new Stack<Room>();
        createRooms();
        parser = new Parser();
    }

    /**
     * Create all the rooms and link their exits together.
     */
    private void createRooms()
    {
        Room outside, theatre, pub, lab, office;
        TransporterRoom transport;
        
        
        Beamer beamer1 = new Beamer("Beamer1");
        Beamer beamer2 = new Beamer("Beamer2");
        // create the rooms
        transport = new TransporterRoom();
        outside = new Room("outside the main entrance of the university");
        theatre = new Room("in a lecture theatre");
        pub = new Room("in the campus pub");
        lab = new Room("in a computing lab");
        office = new Room("in the computing admin office");
        
        // initialise room exits
        outside.setExit("east", theatre);
        outside.setExit("south", lab);
        outside.setExit("west", pub);
        outside.addItem("Nickel: A coin given a value of 5 cents", 0.5, "Nickel");
        outside.addItem("Bag: A bag used to hold things", 0.1, "Bag");
        outside.addItem("Cookie: A delicious snack!", 1.5, "Cookie");
        

        theatre.setExit("west", outside);
        theatre.addItem(beamer2);
        theatre.addItem("Earphones: Black wired device primarily used to listen to music", 2.0, "Earphones");

        pub.setExit("east", outside);
        pub.addItem("Cookie: A delicious snack!", 1.5, "Cookie");
        pub.addItem("Cookie: A delicious snack!", 1.5, "Cookie");
        pub.addItem("Pint: A large tall glass cup", 5.0, "Pint");
        pub.addItem("Shot Glass: A small cup usually used to contain 1 ounce of alcohol", 2.5, "Shot Glass");

        lab.setExit("north", outside);
        lab.setExit("east", office);
        lab.setExit("south", transport);
        lab.addItem(beamer1);
        lab.addItem("HCL: A white powdered acidic substance.", 1.5, "HCL");
        lab.addItem("Erlenmeyer Flask: A large glass tube with a tapered bottom", 3.5, "Erlenmeyer Flask");

        office.setExit("west", lab);
        office.addItem("Cookie: A delicious snack!", 1.5, "Cookie");
        office.addItem("Pencil: A writing utensil that can be easily corrected", 1.5, "Pencil");
        
        //Set Transporter destination using a hashmap
        transport.setDestination("0", outside);
        transport.setDestination("1", theatre);
        transport.setDestination("2", pub);
        transport.setDestination("3", lab);
        transport.setDestination("4", office);
        transport.setDestination("5", transport);

        currentRoom = outside;  // start game outside
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
        System.out.println("Thank you for playing.  Good bye.");
    }

    /**
     * Print out the opening message for the player.
     */
    private void printWelcome()
    {
        System.out.println();
        System.out.println("Welcome to the World of Zuul!");
        System.out.println("World of Zuul is a new, incredibly boring adventure game.");
        System.out.println("Type 'help' if you need help.");
        System.out.println();
        System.out.println(currentRoom.getLongDescription());
    }

    /**
     * Given a command, process (that is: execute) the command.
     * 
     * @param command The command to be processed
     * @return true If the command ends the game, false otherwise
     */
    private boolean processCommand(Command command) 
    {
        boolean wantToQuit = false;

        if(command.isUnknown()) {
            System.out.println("I don't know what you mean...");
            return false;
        }

        String commandWord = command.getCommandWord();
        if (commandWord.equals("help")) {
            printHelp();
        }
        else if (commandWord.equals("go")) {
            goRoom(command);
        }
        else if (commandWord.equals("quit")) {
            wantToQuit = quit(command);
        }
        else if (commandWord.equals("look"))
        {
            look(command);
        }
        else if (commandWord.equals("eat"))
        {
            eat(command);
        }
        else if (commandWord.equals("back"))
        {
            back(command);
        }
        else if (commandWord.equals("stackBack"))
        {
            stackBack(command);
        }
        else if (commandWord.equals("take"))
        {
            take(command);
        }
        else if (commandWord.equals("drop"))
        {
            drop(command);
        }
        else if (commandWord.equals("charge"))
        {
            charge(command);
        }
        else if (commandWord.equals("fire"))
        {
            fire(command);
        }
        
        // else command not recognised.
        return wantToQuit;
    }

    // implementations of user commands:

    /**
     * Print out some help information.
     * Here we print a cryptic message and a list of the 
     * command words.
     */
    private void printHelp() 
    {
        System.out.println("You are lost. You are alone. You wander");
        System.out.println("around at the university.");
        System.out.println();
        System.out.println("Your command words are:");
        System.out.println(parser.getCommands());
    }

    /** 
     * Try to go to one direction. If there is an exit, enter the new
     * room, otherwise print an error message.
     * 
     * @param command The command to be processed
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
            System.out.println("There is no door!");
        }
        else {
            //Do prepatory work for back and stackBack
            lastRoom = currentRoom;
            currentRoom = nextRoom;
            bStack.push(lastRoom);
            
            fullDescription();
        }
    }

    /** 
     * "Quit" was entered. Check the rest of the command to see
     * whether we really quit the game.
     * 
     * @param command The command to be processed
     * @return true, if this command quits the game, false otherwise
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
     * Get a full description of room and Player's status
     * 
     * @param command, User's input
     */
    private void look(Command command)
    {   
        if (command.hasSecondWord())    //Look is a one word command
        {
            System.out.println("Look what?");
            return;
        }
        
        fullDescription();
    }
    
    /**
     * Eat Cookies to gain energy if a Cookie is being carried
     * 
     * @param command, User input
     */
    private void eat(Command command)
    {
        if (command.hasSecondWord())    //Eat is a one word command
        {
            System.out.println("Eat what?");
            return;
        }
        if (itemHeld.getName().equals( "Cookie"))     // Checks if User is holding a cookie to eat
        {
            System.out.println("You have eaten the cookie");
            itemHeld = null;        //Sets it so that User is not holding anything
            energy += 5;
            return;
        }
        System.out.println("You have no food!");
    }
    
    /**
     * Goes back to the previous room you were in using the saved room from go
     * 
     * @param command, User input
     */
     private void back(Command command)
    {
        if (command.hasSecondWord())
        {
            System.out.println("Back what?");   //Back is a one word command
            return;
        }
        
        else if (lastRoom == null)      // Cam't go back hen the User just started
        {
            System.out.println("Can't go back you just started!");
            return;
        }
        
        // setup for moving rooms
        Room holder = currentRoom;
        
        //Move rooms
        currentRoom = lastRoom;
        lastRoom = holder;
        fullDescription();
    }
    
    /**
     * Goes to the previous SET of rooms allowing to go back more than 1 room to not loop
     * 
     * @param command, User input
     */
    private void stackBack(Command command)
    {
        if (command.hasSecondWord())
        {
            System.out.println("stackBack what?");   //stackBack is a one word command
            return;
        }
        
        if (lastRoom == null)       //Catch the user if they try to go when when they just started
        {
            System.out.println("Can't go back you just started!");
            return;
        }
        
        // Move rooms
        currentRoom = bStack.pop();
        fullDescription();
    }
    
    /**
     * Takes an item from the room
     */
    private void take(Command command)
    {
        if (!command.hasSecondWord()) 
        {
            // if there is no second word, we don't know what to take...
            System.out.println("Take what?");
            return;
        }
        
        String item = command.getSecondWord();
        
        if (energy == 0 && !item.equals("Cookie"))  //Checks if the user has sufficient energy to pick up the item
        {
            System.out.println("You don't have enough energy to pick that up.");
            return;
        }
        
        if (itemHeld != null)   // User cannot pick more than one item up at a time
        {
            System.out.println("You are already carrying something");
            return;
        }
        
        itemHeld = currentRoom.removeItem(item);    // Removes the item from the room and saves it in itemHeld for further checks
        
        if (itemHeld == null)       // Null means that the item was not in the room
        {
            System.out.println("That item is not in the room");
            return;
        }
        
        //Increment energy and updates the user with information
        energy -= 1;
        System.out.println("You have taken the " + item + ".");
    }
    
    /**
     * Drop the item held into the room
     * 
     * @param command, User input
     */
    private void drop(Command command)
    {
        if (itemHeld == null)   //Checks if the user has anything to even drop
        {
            System.out.println("You have nothing to drop");
            return;
        }
        
        //Adds item to the room and updates the User's status and information
        currentRoom.addItem(itemHeld);
        System.out.println("You have dropped the " + itemHeld.getName() + ".");
        itemHeld = null;
    }
    
    /**
     * Charges the beamer and saves the current room
     * 
     * @param command, User input
     */
    private void charge(Command command)
    {
        if (!(itemHeld instanceof Beamer))      //Checks if user is not carrying a Beamer
        {
            System.out.println("You don't have a Beamer to charge!");
            return;
        }
        
        // Cast the beamer and charge it
        Beamer beam = (Beamer) itemHeld;
        beam.charge(currentRoom);
    }
    
    /**
     * Fires the Beamer teleporting the User
     * 
     * @param command. User input
     */
    private void fire(Command command)
    {
        if (!(itemHeld instanceof Beamer))  // Checks the if the user is not carrying a beamer
        {
            System.out.println("You don't have a Beamer to fire!");
            return;
        }
        
        Beamer beam = (Beamer) itemHeld;    // Cast the beamer and fire it
        if (beam.fire())
        {
            lastRoom = currentRoom;     // back commands prep and goes to the saved room
            bStack.push(currentRoom);
            currentRoom = beam.getSaved();
        
            fullDescription();
        }
    }
    
    /**
     * Prints out the entirety of the room as well as the details of the User's Status to the console
     */
    private void fullDescription()
    {
        System.out.println(currentRoom.getLongDescription());
        if (itemHeld == null)
        {
            System.out.println("You are not carrying anything");
        }
        else
        {
            System.out.println("You are carrying a " + itemHeld.getName() + ".");
        }
    }
    
    
}

