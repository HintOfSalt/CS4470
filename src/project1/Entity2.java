package project1;

public class Entity2 extends Entity
{    
    // Perform any necessary initialization in the constructor
    public Entity2()
    {
      for(int x = 0; x < 4; x++){
        for(int y = 0; y < 4; y++){
          distanceTable[x][y] = 999;
        }
      }
      distanceTable[0][2] = 3;
      distanceTable[1][2] = 1;
      distanceTable[2][2] = 0;
      distanceTable[3][2] = 2;
     
      int[] minDistance = new int[4];
      for(int h = 0; h < 4; h++){
        int a = Math.min(distanceTable[h][0], distanceTable[h][1]);
        int b = Math.min(distanceTable[h][2], distanceTable[h][3]);
        minDistance[h] = Math.min(a, b);
      }
      printDT();
      for(int i = 0; i < 4; i++){
        if(i != 2){
          Packet dtPacket = new Packet(2, i, minDistance);
          NetworkSimulator.toLayer2(dtPacket);
        }
      }
    }
    
    // Handle updates when a packet is received.  Students will need to call
    // NetworkSimulator.toLayer2() with new packets based upon what they
    // send to update.  Be careful to construct the source and destination of
    // the packet correctly.  Read the warning in NetworkSimulator.java for more
    // details.
    public void update(Packet p)
    {
      boolean send = false;
      int[] minDistance = new int[4];
      for(int i = 0; i < 4; i++){
        distanceTable[i][p.getSource()] = p.getMincost(i) + distanceTable[i][p.getDest()];
      }
      for(int h = 0; h < 4; h++){
        int a = Math.min(distanceTable[h][0], distanceTable[h][1]);
        int b = Math.min(distanceTable[h][2], distanceTable[h][3]);
        minDistance[h] = Math.min(a, b);
      }
      for(int k = 0; k<4; k++){
        if(p.getMincost(k)+minDistance[p.getSource()] < distanceTable[p.getSource()][k]){
          distanceTable[k][p.getSource()] = p.getMincost(k)+minDistance[p.getSource()];
          if(distanceTable[k][p.getSource()]<minDistance[k]){
            minDistance[k] = distanceTable[k][p.getSource()];
            send = true;
          }
        }
      }
      if(send){
        printDT();
        for(int i = 0; i < 4; i++){
          if(i != 2){
            Packet dtPacket = new Packet(2, i, minDistance);
            NetworkSimulator.toLayer2(dtPacket);
          }
        }
      }
    }
    
    public void linkCostChangeHandler(int whichLink, int newCost)
    {
      int[] minDistance = new int[4];
      distanceTable[whichLink][whichLink] = newCost;
      for(int h = 0; h < 4; h++){
        int a = Math.min(distanceTable[h][0], distanceTable[h][1]);
        int b = Math.min(distanceTable[h][2], distanceTable[h][3]);
        minDistance[h] = Math.min(a, b);
      }
      printDT();
      for(int i = 0; i < 4; i++){
        if(i != 2){
          Packet dtPacket = new Packet(2, i, minDistance);
          NetworkSimulator.toLayer2(dtPacket);
        }
      }
    }
    
    public void printDT()
    {
        System.out.println();
        System.out.println("           via");
        System.out.println(" D2 |   0   1   3");
        System.out.println("----+------------");
        for (int i = 0; i < NetworkSimulator.NUMENTITIES; i++)
        {
            if (i == 2)
            {
                continue;
            }
            
            System.out.print("   " + i + "|");
            for (int j = 0; j < NetworkSimulator.NUMENTITIES; j++)
            {
                if (j == 2)
                {
                    continue;
                }
                
                if (distanceTable[i][j] < 10)
                {    
                    System.out.print("   ");
                }
                else if (distanceTable[i][j] < 100)
                {
                    System.out.print("  ");
                }
                else 
                {
                    System.out.print(" ");
                }
                
                System.out.print(distanceTable[i][j]);
            }
            System.out.println();
        }
    }
}
