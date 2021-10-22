/**
   A class which demonstrates how a GraphADT graph is built
*/
public class GraphTest
{
   enum Name {
      Anna, Bill, Carl, Dave, Emma, Fred
   }

   public static void main(String[] args)
   {  
      Name[] name = Name.values();
      double[][] atable = {{0, 0.5, 0.4, 0, 0, 0}, //Anna
                          {0.5, 0, 0, 0.4, 0, 0}, //Bill
                          {0.4, 0, 0, 0.3, 0.5, 0}, //Carl
                          {0, 0.4, 0.3, 0, 0.8, 0}, //Dave
                          {0, 0, 0.5, 0.8, 0, 0.7}, //Emma
                          {0, 0, 0, 0, 0.7, 0}}; //Fred



      //*Constructer and calling methods for Project*
      
      
      
      //BAFBP test = new BAFBP(name, atable);// Class that find the Best Association Between People.
      //test.algorithm("Emma", "Anna");// Find the shortest path between Emma & Anna

      //CliqueFinder clFinder = new CliqueFinder<>(name, atable); // CliqueFinder which is implement Bron Kerbosch Algorithm
      //CliqueFinder2 clFinder2 = new CliqueFinder2<>(name, atable); // CliqueFinder which is implement Bron Kerbosch with pivot Algorithm 
      
      CommunityClusterFinder cfinder = new CommunityClusterFinder(name, atable);
    



   }
}
