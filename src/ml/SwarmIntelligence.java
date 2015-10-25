package ml;

import java.io.IOException;
import java.util.Arrays;

public class SwarmIntelligence {

    public static void main(String args[]) throws IOException {
        final InputReader br = new InputReader(System.in);
        final int cities = br.readInt(), roads = br.readInt();
        final int[] from = new int[roads], to = new int[roads], distance = new int[roads];
        for (int i = 0; i < roads; i++) {
            from[i] = br.readInt();
            to[i] = br.readInt();
            distance[i] = br.readInt();
        }
        final int orders = br.readInt();
        final int[] source = new int[orders], destination = new int[orders], weight = new int[orders], income = new int[orders];
        for (int i = 0; i < orders; i++) {
            source[i] = br.readInt();
            destination[i] = br.readInt();
            weight[i] = br.readInt();
            income[i] = br.readInt();
        }
        final int STARTING_POINT = br.readInt(), FUEL = br.readInt(), CAPACITY = br.readInt();
        final AntColonyOptimization antColonyOptimization = new AntColonyOptimization(cities, roads, from, to, distance, orders, source, destination, weight, income);
        System.out.println(antColonyOptimization.solve(STARTING_POINT, FUEL, CAPACITY));
    }

}

class AntColonyOptimization {

    public static final int LIFE_SPAN = 3500;
    public static final int POPULATION = 400;
    private int maxPath[][] = new int[2][];
    private int maxPathLength;
    private final int maxIncome;
    private final int largestWeight;
    private final AntColony antColony;
    private final Order orders[];
    private final int deliveries[];
    private final int customers[];

    public AntColonyOptimization(final int cities,
                                 final int roads,
                                 final int[] from,
                                 final int[] to,
                                 final int[] distance,
                                 final int numberOfOrders,
                                 final int[] source,
                                 final int[] destination,
                                 final int[] weight,
                                 final int[] income) {
        antColony = new AntColony(cities, roads, from, to, distance);
        orders = new Order[numberOfOrders];
        int max = 0, mweight = 0;
        for (int i = 0; i < numberOfOrders; i++) {
            orders[i] = new Order(source[i], destination[i], weight[i], income[i], i + 1);
            if (income[i] > max) {
                max = income[i];
            }
            if (weight[i] > mweight) {
                mweight = weight[i];
            }
        }
        maxIncome = max;
        largestWeight = mweight;
        Arrays.parallelSort(orders);
        deliveries = new int[cities + 1];
        customers = new int[deliveries.length];
        for (int i = 1; i < orders.length; i++) {
            if (orders[i].source != orders[i - 1].source) {
                deliveries[orders[i].source] = i;
                customers[orders[i].source] = 1;
            } else {
                customers[orders[i].source]++;
            }
        }
    }

    public String solve(final int STARTING_POINT, final int FUEL, final int CAPACITY) {
        //send some ants
        long largestIncome = 0;
        for (int ant = 0; ant < POPULATION; ant++) {
            int currentCity = STARTING_POINT;
            int previousCity = currentCity;
            int currentFuel = FUEL;
            int currentCapacity = CAPACITY;
            long currentIncome = 0;
            int pathLength = 0;
            int tour = 0;
            int path[][] = new int[2][LIFE_SPAN * 3];
            final Order inventory[] = new Order[orders.length < path[0].length ? orders.length : path[0].length];
            int itemCount = 0;
            //each ant has a lifespan
            int pickUp = 0, drop = 0;
            boolean enjoyedLife = false;
            final Route trips[] = new Route[path[0].length];
            for (int life = LIFE_SPAN; life > 0 && pathLength < path[0].length; life++) {
                int possibleDestinations = antColony.optionCount[currentCity];
                double probability[] = new double[possibleDestinations];
                double probSum = 0;
                //add items to inventory
                for (int item = 0; item < customers[currentCity] && pathLength < path[0].length; item++) {
                    Order order = orders[deliveries[currentCity] + item];
                    if (order != null && order.weight <= currentCapacity && itemCount < 50) {
                        if (Math.random() <= pickUpProbability(currentFuel, order.weight, order.income, FUEL, life)) {
                            inventory[itemCount++] = order;
                            currentCapacity = currentCapacity - order.weight;
                            path[0][pathLength] = 1;
                            path[1][pathLength] = order.index;
                            pickUp++;
                            orders[deliveries[currentCity] + item] = null;
                            pathLength++;
                        }
                    }
                }
                //search for the next destination
                if (pathLength < path[0].length) {
                    for (int destination = 0; destination < possibleDestinations; destination++) {
                        Route route = antColony.routes[antColony.cityRoutes[currentCity] + destination];
                        long income = 0;
                        if (route.distance <= currentFuel) {
                            for (int item = 0; item < itemCount; item++) {
                                if (inventory[item] != null && inventory[item].destination == destination) {
                                    income += inventory[item].income;
                                }
                            }
                            probability[destination] = tripProbability(route.pheromone, route.distance, income, ant - route.updateTime, route.to == previousCity);
                            probSum = probSum + probability[destination];
                        }
                    }
                }
                //find next destination
                Route trip = null;
                double roulette = Math.random();
                for (int destination = 0; destination < probability.length && pathLength < path[0].length; destination++) {
                    if (probability[destination] > 0) {
                        probability[destination] = probability[destination] / probSum;
                        if (probability[destination] >= roulette) {
                            trip = antColony.routes[antColony.cityRoutes[currentCity] + destination];
                            path[0][pathLength] = 0;
                            path[1][pathLength] = trip.to;
                            pathLength++;
                            previousCity = currentCity;
                            currentCity = trip.to;
                            break;
                        } else {
                            roulette = roulette - probability[destination];
                        }
                    }
                }
                if (trip == null) {
                    break;
                }
                //drop all items with this destination
                for (int parcel = 0; parcel < itemCount && pathLength < path[0].length; parcel++) {
                    if (inventory[parcel] != null && inventory[parcel].destination == currentCity) {
                        currentIncome = currentIncome + inventory[parcel].income;
                        path[0][pathLength] = 2;
                        path[1][pathLength] = inventory[parcel].index;
                        pathLength++;
                        inventory[parcel] = null;
                        drop++;
                    }
                }
                currentFuel = currentFuel - trip.distance;
                trips[tour++] = trip;
                //set the maxPath if maximum
                if (pickUp == drop) {
                    if (currentIncome > 0) {
                        enjoyedLife = true;
                        if (largestIncome < currentIncome) {
                            largestIncome = currentIncome;
                            maxPath = path;
                            maxPathLength = pathLength;
                        }
                    }
                }
                //found the next city
            }
            if (enjoyedLife) {
                for (int i = 0; i < tour; i++) {
                    trips[i].pheromone = pheromoneUpdate(trips[i].pheromone,
                            pheromoneDeposit(FUEL - currentFuel, pickUp == drop ? 1.5d : 1d, currentIncome), ant - trips[i].updateTime);
                    trips[i].updateTime = ant;
                }
            }
        }
        return printPath(maxPath, maxPathLength);
    }

    private String printPath(int path[][], int length) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(length).append('\n');
        for (int i = 0; i < length; i++) {
            stringBuilder.append(path[0][i]).append(' ').append(path[1][i]).append('\n');
        }
        return stringBuilder.toString();
    }

    private double pickUpProbability(int fuel, int weight, double income, double startFuel, double life) {
        return Math.pow((life / LIFE_SPAN) * (fuel * income * largestWeight / (weight * startFuel * maxIncome)), 0.33);
    }

    private double tripProbability(double deposition, int distance, double income, int time, boolean sameCity) {
        return (sameCity ? 0.95 : 1) *
                ((((income + 10) / maxIncome) + (deposition * Math.pow(0.8, time)) + (antColony.maxDistance / distance)));
    }

    private double pheromoneUpdate(double deposition, double pheromone, int time) {
        return deposition * Math.pow(0.9, time) + pheromone;
    }

    private double pheromoneDeposit(double length, double reinforcement, double income) {
        return reinforcement * (antColony.maxDistance / length) + (income / maxIncome);
    }

}

class AntColony {

    final Route routes[];
    final int cityRoutes[];
    final int optionCount[];
    final int maxDistance;

    public AntColony(final int cities,
                     final int roads,
                     final int[] from,
                     final int[] to,
                     final int[] distance) {
        this.routes = new Route[roads << 1];
        this.cityRoutes = new int[cities + 1];
        this.optionCount = new int[cities + 1];
        int maxDist = 0;
        for (int i = 0; i < routes.length; i = i + 2) {
            routes[i] = new Route(to[i >> 1], from[i >> 1], distance[i >> 1]);
            routes[i + 1] = new Route(from[i >> 1], to[i >> 1], distance[i >> 1]);
            if (routes[i].distance > maxDist) {
                maxDist = routes[i].distance;
            }
        }
        maxDistance = maxDist;
        Arrays.parallelSort(routes);
        optionCount[routes[0].from] = 1;
        for (int i = 1; i < routes.length; i++) {
            if (routes[i].from != routes[i - 1].from) {
                cityRoutes[routes[i].from] = i;
                optionCount[routes[i].from] = 1;
            } else {
                optionCount[routes[i].from]++;
            }
        }
    }

}

class Order implements Comparable<Order> {
    final int source;
    final int destination;
    final int weight;
    final int income;
    final int index;

    Order(int source, int destination, int weight, int income, int index) {
        this.source = source;
        this.destination = destination;
        this.weight = weight;
        this.income = income;
        this.index = index;
    }

    @Override
    public String toString() {
        return "Order{" +
                "source=" + source +
                ", destination=" + destination +
                ", weight=" + weight +
                ", income=" + income +
                ", index=" + index +
                '}';
    }

    @Override
    public int compareTo(Order other) {
        return this.source - other.source;
    }
}

class Route implements Comparable<Route> {
    final int to, from, distance;
    double pheromone;
    int updateTime;

    Route(int to, int from, int distance) {
        this.to = to;
        this.from = from;
        this.distance = distance;
    }

    @Override
    public String toString() {
        return "Route{" +
                "to=" + to +
                ", from=" + from +
                ", distance=" + distance +
                ", pheromone=" + pheromone +
                '}';
    }

    @Override
    public int compareTo(Route other) {
        return this.from - other.from;
    }
}

/*
    private static int[][] bruteForce(int a[]) {
        //generate all binary numbers between 1 and 2^n --> zeros and ones
        //if the ith bit in the binary number is one-> the ith element belongs to group 2
        //if the ith bit in the binary number is zero-> the ith element belongs to group 1
        //find the difference in the group sums
        //take the result such that difference is minimum
        return new int[a.length][2];
    }

    //Partition problem

    //difference the two groups
    //individual --> random binary
    private static int[][] geneticAlgorithm(int a[]){
        //Higher fitness --> Higher probabilty to mate--> Higher chance of offspring
        //Some common attributes from both parents
        //replace the parents with the children
        return null;
    }
 */