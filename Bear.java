import java.util.List;
import java.util.Iterator;
import java.util.Random;

/**
 * A simple model of a bear.
 * Bears age, move, eat rabbits, eat foxes, and die. The bears should start out
 * slow, but the population grows and stays on par with the fox population for
 * the majority of the first 4000 steps, maybe growing slightly larger over a 
 * long period of time.
 * 
 * @author Christopher Urban
 * @version 04-09-2021
 */
public class Bear extends Animal
{
    // Characteristics shared by all Bears (class variables).
    
    // The age at which a bear can start to breed.
    private static final int BREEDING_AGE = 33;
    // The age to which a bear can live.
    private static final int MAX_AGE = 105;
    // The likelihood of a bear breeding.
    private static final double BREEDING_PROBABILITY = 0.03525;
    // The maximum number of births.
    private static final int MAX_LITTER_SIZE = 1;
    // The food value of a single rabbit. In effect, this is the
    // number of steps a bear can go before it has to eat again.
    private static final int RABBIT_FOOD_VALUE = 5;
    // The food value of a single fox. In effect, this is the
    // number of steps a bear can go before it has to eat again.
    private static final int FOX_FOOD_VALUE = 7;
    // A shared random number generator to control breeding.
    private static final Random rand = Randomizer.getRandom();
    
    // The bear's food level, which is increased by eating rabbits or foxes.
    private int foodLevel;

    /**
     * Create a bear. A bear can be created as a new born (age zero
     * and not hungry) or with a random age and food level.
     * 
     * @param randomAge If true, the bear will have random age and hunger level.
     * @param field The field currently occupied.
     * @param location The location within the field.
     */
    public Bear(boolean randomAge, Field field, Location location)
    {
        super(field, location);
        if(randomAge) {
            setAge(rand.nextInt(MAX_AGE));
            foodLevel = rand.nextInt(FOX_FOOD_VALUE);
        }
        else {
            setAge(0);
            foodLevel = RABBIT_FOOD_VALUE;
        }
    }
    
    /**
     * This is what the bear does most of the time: it hunts for
     * rabbits or foxes. In the process, it might breed, die of hunger,
     * or die of old age.
     * @param field The field currently occupied.
     * @param newBears A list to return newly born bears.
     */
    public void act(List<Animal> newBears)
    {
        incrementAge();
        incrementHunger();
        if(isAlive()) {
            giveBirth(newBears);            
            // Move towards a source of food if found.
            Location newLocation = findFood();
            if(newLocation == null) { 
                // No food found - try to move to a free location.
                newLocation = getField().freeAdjacentLocation(getLocation());
            }
            // See if it was possible to move.
            if(newLocation != null) {
                setLocation(newLocation);
            }
            else {
                // Overcrowding.
                setDead();
            }
        }
    }

    /**
     * @return the max age of a bear.
     */
    public int getMaxAge()
    {
        return MAX_AGE;
    }
    
    /**
     * Make this bear more hungry. This could result in the bear's death.
     */
    private void incrementHunger()
    {
        foodLevel--;
        if(foodLevel <= 0) {
            setDead();
        }
    }
    
    /**
     * Look for rabbits or foxes adjacent to the current location.
     * All adjacent rabbits or foxes are eaten.
     * Only gains foodLevel up to a cap.
     * @return Where food was found, or null if it wasn't.
     */
    private Location findFood()
    {
        Field field = getField();
        List<Location> adjacent = field.adjacentLocations(getLocation());
        Iterator<Location> it = adjacent.iterator();
        int hungerCap = 13;
        while(it.hasNext()) {
            Location where = it.next();
            Object animal = field.getObjectAt(where);
            if(animal instanceof Rabbit) {
                Rabbit rabbit = (Rabbit) animal;
                if(rabbit.isAlive()) { 
                    rabbit.setDead();
                    if(foodLevel+RABBIT_FOOD_VALUE <= hungerCap){
                        foodLevel += RABBIT_FOOD_VALUE;
                    }
                    return where;
                }
            }
            if(animal instanceof Fox) {
                Fox fox = (Fox) animal;
                if(fox.isAlive()) { 
                    fox.setDead();
                    if(foodLevel+FOX_FOOD_VALUE <= hungerCap){
                        foodLevel += FOX_FOOD_VALUE;
                    }
                    return where;
                }
            }
        }
        return null;
    }
    
    /**
     * Check whether or not this bear is to give birth at this step.
     * New births will be made into free adjacent locations.
     * @param newBears A list to return newly born bears.
     */
    private void giveBirth(List<Animal> newBears)
    {
        // New foxes are born into adjacent locations.
        // Get a list of adjacent free locations.
        Field field = getField();
        List<Location> free = field.getFreeAdjacentLocations(getLocation());
        int births = breed();
        for(int b = 0; b < births && free.size() > 0; b++) {
            Location loc = free.remove(0);
            Bear young = new Bear(false, field, loc);
            newBears.add(young);
        }
    }
        
    /**
     * Returns the maximum litter size
     * @return The max litter size.
     */
    public int getMaxLitterSize()
    {
        return MAX_LITTER_SIZE;
    }
    
    /**
     * Returns the breeding probability of bears
     * @return The breeding probability.
     */
    public double getBreedingProbability()
    {
        return BREEDING_PROBABILITY;
    }

    /**
     * return the breeding age of bears.
     */
    public int getBreedingAge()
    {
        return BREEDING_AGE;
    }
}
