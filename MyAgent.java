import java.util.Random;

public class MyAgent extends Agent
{
    Random r;

    /**
     * Constructs a new agent, giving it the game and telling it whether it is Red or Yellow.
     * 
     * @param game The game the agent will be playing.
     * @param iAmRed True if the agent is Red, False if the agent is Yellow.
     */
    public MyAgent(Connect4Game game, boolean iAmRed)
    {
        super(game, iAmRed);
        r = new Random();
    }

    /**
     * The move method is run every time it is this agent's turn in the game. You may assume that
     * when move() is called, the game has at least one open slot for a token, and the game has not
     * already been won.
     * 
     * By the end of the move method, the agent should have placed one token into the game at some
     * point.
     * 
     * After the move() method is called, the game engine will check to make sure the move was
     * valid. A move might be invalid if:
     * - No token was place into the game.
     * - More than one token was placed into the game.
     * - A previous token was removed from the game.
     * - The color of a previous token was changed.
     * - There are empty spaces below where the token was placed.
     * 
     * If an invalid move is made, the game engine will announce it and the game will be ended.
     * 
     */
    public void move()
    {
        
        int gameEndingColumnIndex = iCanWin();
        
        if (gameEndingColumnIndex != -1
            && !myGame.getColumn(gameEndingColumnIndex).getIsFull()) // I can win
        {
            moveOnColumn(gameEndingColumnIndex); //play the winning move
        
        }
        else //i can't win on this move
        {
        
            gameEndingColumnIndex = theyCanWin();
        
            if (gameEndingColumnIndex != -1
                && ! myGame.getColumn(gameEndingColumnIndex).getIsFull()) //they can win
            {
                moveOnColumn(gameEndingColumnIndex); //block their win
         
            }
            else //neither they nor I can win
            {
                int i = randomMove(); //generate a random move
                int attemptCount = 1;
                while((thisMoveOffersImmediateHorizontalWin(i) || thisMoveOffersImmediateDiagonallWin(i)) //while this move provides an immediate winning situation for the opponent
                        && attemptCount<=100) //and we haven't tried a hundred random moves...
                {
                    i = randomMove(); //keep generating new random moves;
                    attemptCount++;
                }
                moveOnColumn(i); //play a random move
            }
        }
    }

    /**
     * Drops a token into a particular column so that it will fall to the bottom of the column.
     * If the column is already full, nothing will change.
     * 
     * @param columnNumber The column into which to drop the token.
     */
    public void moveOnColumn(int columnNumber)
    {
        int lowestEmptySlotIndex = getLowestEmptyIndex(myGame.getColumn(columnNumber));   // Find the top empty slot in the column
                                                                                                  // If the column is full, lowestEmptySlot will be -1
        if (lowestEmptySlotIndex > -1)  // if the column is not full
        {
            Connect4Slot lowestEmptySlot = myGame.getColumn(columnNumber).getSlot(lowestEmptySlotIndex);  // get the slot in this column at this index
            if (iAmRed) // If the current agent is the Red player...
            {
                lowestEmptySlot.addRed(); // Place a red token into the empty slot
            }
            else // If the current agent is the Yellow player (not the Red player)...
            {
                lowestEmptySlot.addYellow(); // Place a yellow token into the empty slot
            }
        }
    }

    /**
     * Returns the index of the top empty slot in a particular column.
     * 
     * @param column The column to check.
     * @return the index of the top empty slot in a particular column; -1 if the column is already full.
     */
    public int getLowestEmptyIndex(Connect4Column column) {
        int lowestEmptySlot = -1;
        for  (int i = 0; i < column.getRowCount(); i++)
        {
            if (!column.getSlot(i).getIsFilled())
            {
                lowestEmptySlot = i;
            }
        }
        return lowestEmptySlot;
    }

    /**
     * Returns a random valid move. If your agent doesn't know what to do, making a random move
     * can allow the game to go on anyway.
     * 
     * @return a random valid move.
     */
    public int randomMove()
    {
        int i = r.nextInt(myGame.getColumnCount());
        while (getLowestEmptyIndex(myGame.getColumn(i)) == -1)
        {
            i = r.nextInt(myGame.getColumnCount());
        }
        return i;

    }

    /**
     * Returns the column that would allow the agent to win.
     * 
     * You might want your agent to check to see if it has a winning move available to it so that
     * it can go ahead and make that move. Implement this method to return what column would
     * allow the agent to win.
     *
     * @return the column that would allow the agent to win.
     */
    public int iCanWin()
    {
        int horizontalWinColumn = findHorizontalWinColumn("myColor");
        int diagonalWinColumn = findDiagonalWinColumn("myColor");
        int redVerticalWinColumn = findRedVerticalWinColumn();
        int yellowVerticalWinColumn = findYellowVerticalWinColumn();
        if (horizontalWinColumn != -1)
        {
            System.out.println("I'm going to win horizontally by placing at column index "+horizontalWinColumn);
            return horizontalWinColumn;
        }
        else if (diagonalWinColumn !=-1)
        {
            System.out.println("I'm going to win diagonally by placing at column index "+diagonalWinColumn);
            return diagonalWinColumn;
        }
        else if (iAmRed && redVerticalWinColumn != -1)
        {
            System.out.println("I'm going to win vertically by placing at column index " + redVerticalWinColumn);
            return redVerticalWinColumn;            
        }
        else if (!iAmRed && yellowVerticalWinColumn != -1)
        {
            System.out.println("I'm going to win vertically by placing at column index " + yellowVerticalWinColumn);
            return yellowVerticalWinColumn;            
        }
        else
        {
            return -1;
        }
    }

    /**
     * Returns the column that would allow the opponent to win.
     * 
     * You might want your agent to check to see if the opponent would have any winning moves
     * available so your agent can block them. Implement this method to return what column should
     * be blocked to prevent the opponent from winning.
     *
     * @return the column that would allow the opponent to win.
     */
    public int theyCanWin()
    {
        //check for horizontal winning opportunities i each column
        int horizontalWinColumn = findHorizontalWinColumn("notMyColor");
        int diagonalWinColumn = findDiagonalWinColumn("notMyColor");
        int redVerticalWinColumn = findRedVerticalWinColumn();
        int yellowVerticalWinColumn = findYellowVerticalWinColumn();
        if (horizontalWinColumn != -1)
        {
            System.out.println("I'm going to block the opponent from winning horizontally by placing at column index " + horizontalWinColumn);
            return horizontalWinColumn;
        }
        else if (diagonalWinColumn != -1)
        {
            System.out.println("I'm going to block the opponent from winning diagonally by placing at column index " + diagonalWinColumn);
            return diagonalWinColumn;
        }
        else if (!iAmRed && redVerticalWinColumn != -1)
        {
            System.out.println("I'm going to block the opponent from winning vertically by placing at column index " + redVerticalWinColumn);
            return redVerticalWinColumn;            
        }
        else if (iAmRed && yellowVerticalWinColumn != -1)
        {
            System.out.println("I'm going to block the opponent from winning vertically by placing at column index " + yellowVerticalWinColumn);
            return yellowVerticalWinColumn;            
        }
        else
        {
            return -1;
        }
    }

    /**
     * Returns the name of this agent.
     *
     * @return the agent's name
     */
    public String getName()
    {
        return "My Agent";
    }

    
    /**
    *Returns whether or not the last 3 slots in this column were played by Red
    *
    *@param column the column of interest
    *@return true if last 3 slots are Red, otherwise False
    */
    public boolean areLast3VerticalRed(Connect4Column column)
    {
        if (getLowestEmptyIndex(column) > 2)
        {
            return false;
        }
        else
        {   
            int redCount = 0;
            boolean lastWasRed = true; 
            int index = getLowestEmptyIndex(column) + 1;

            while (lastWasRed // last slot checked was red
                && redCount<3 // we haven't yet seen 3 consecutive red spaces
                && index<=column.getRowCount()) // we haven't exceeded the reange of slots that exist
            {
                lastWasRed = column.getSlot(index).getIsRed(); //check if this  slot is red
                if (lastWasRed)
                {
                    redCount++; //if it was, increment the counter
                }
                index++; //go to the previously played slot
            }
            if (redCount == 3) //if we found 3 consecutive red slots
            {
                return true;
            }
            else
            {
                return false;
            }
        }
    }

    public boolean areLast3VerticalYellow(Connect4Column column)
    {
        if (getLowestEmptyIndex(column) > 2)
        {
            return false;
        }
        else
        {   
            int yellowCount = 0;
            boolean lastWasYellow = true; 
            int index = getLowestEmptyIndex(column) + 1;

            while (lastWasYellow // last slot checked was yellow
                && yellowCount<3 // we haven't yet seen 3 consecutive yello spaces
                && index<=column.getRowCount()) // we haven't exceeded the reange of slots that exist
            {
                lastWasYellow = (column.getSlot(index).getIsFilled() // check if the slot is filled
                                && !column.getSlot(index).getIsRed()); // check if this  slot is not red
                if (lastWasYellow)
                {
                    yellowCount++; //if it was, increment the counter
                }
                index++; //go to the previously played slot
            }
            if (yellowCount == 3) //if we found 3 consecutive red slots
            {
                return true;
            }
            else
            {
                return false;
            }
        }
    }

    public int findRedVerticalWinColumn()
    {
        int winningColumnIndex = -1;
        boolean columnFound = false;
        int colIndex = 0;
        while (colIndex < myGame.getColumnCount() && !columnFound)
        {
            Connect4Column thisColumn = myGame.getColumn(colIndex);
            if(areLast3VerticalRed(thisColumn) && !thisColumn.getIsFull())
            {
                winningColumnIndex = colIndex;
                columnFound = true;
            }
            colIndex++;
        }
        return winningColumnIndex;
        
    }

    public int findYellowVerticalWinColumn()
    {
        int winningColumnIndex = -1;
        boolean columnFound = false;
        int colIndex = 0;
        while (colIndex < myGame.getColumnCount() && !columnFound)
        {
            Connect4Column thisColumn = myGame.getColumn(colIndex);
            if(areLast3VerticalYellow(thisColumn) && !thisColumn.getIsFull())
            {
                winningColumnIndex = colIndex;
                columnFound = true;
            }
            colIndex++;
        }
        return winningColumnIndex;
        
    }

    public int findHorizontalWinColumn(String whosColor)
    {
        
        int thisColumnIndex = 0;
        int winningColumnIndex = -1;
        
        while (thisColumnIndex<myGame.getColumnCount() && winningColumnIndex == -1)
        {
            if(!myGame.getColumn(thisColumnIndex).getIsFull())
            {
                int playableSlotIndex = getLowestEmptyIndex(myGame.getColumn(thisColumnIndex));
                int matchesToLeft = matchingSpacesToLeft(thisColumnIndex, playableSlotIndex, whosColor);
                int matchesToRight = matchingSpacesToRight(thisColumnIndex, playableSlotIndex, whosColor);
                if (matchesToRight+matchesToLeft >=3)
                {
                    winningColumnIndex = thisColumnIndex;
                }    
            }

            thisColumnIndex++;
        }
        return winningColumnIndex;
    }

    public boolean isMyColor(Connect4Slot slot)
    {
        //System.out.println("Slot passed to isMyColor is: " + slot);
        if (slot.getIsFilled()
            &&((iAmRed && slot.getIsRed())
                || (! (iAmRed) && !slot.getIsRed())))
        {
            return true;
        }
        else
        {
            return false;
        }
            
    }

    public boolean isOpponentsColor(Connect4Slot slot)
    {
        //System.out.println("Slot passed to isMyColor is: " + slot);
        if (slot.getIsFilled()
            &&((!iAmRed && slot.getIsRed())
                || ( (iAmRed) && !slot.getIsRed())))
        {
            return true;
        }
        else
        {
            return false;
        }
            
    }

    public int matchingSpacesToLeft(int columnIndex, int slotIndex, String whosColor)
    {
        int maxLeftDistance = -1;
        if (columnIndex >= 3)
        {
            maxLeftDistance = 3;
        }
        else
        {
            maxLeftDistance = columnIndex;
        }

        int matchingSpacesToLeft = 0;
        int distanceGoneToLeft = 1;
        boolean stillMatching = true;
        while (distanceGoneToLeft <= maxLeftDistance && matchingSpacesToLeft < 3 && stillMatching)
        {
            Connect4Slot thisTestSlot = myGame.getColumn(columnIndex - distanceGoneToLeft).getSlot(slotIndex);
            if (whosColor.equals("notMyColor"))
            {
                if (isOpponentsColor(thisTestSlot))
                {
                    matchingSpacesToLeft++;
                    distanceGoneToLeft++;
                }
                else
                {
                    stillMatching = false;
                }
            }
            else if (whosColor.equals("myColor"))
            {
                if (isMyColor(thisTestSlot))
                {
                    matchingSpacesToLeft++;
                    distanceGoneToLeft++;
                }
                else
                {
                    stillMatching = false;

                }
            }
        }
        return matchingSpacesToLeft;
    }

    public int matchingSpacesToRight(int columnIndex, int slotIndex, String whosColor)
    {
        int maxDistance = getMaxRightDistance(columnIndex);

        int matchingSpacesToRight = 0;
        int distanceGoneToRight = 1;
        boolean stillMatching = true;
        while (distanceGoneToRight <= maxDistance && matchingSpacesToRight < 3 && stillMatching)
        {
            Connect4Slot thisTestSlot = myGame.getColumn(columnIndex+distanceGoneToRight).getSlot(slotIndex);
            if (whosColor.equals("notMyColor"))
            {
                if (isOpponentsColor(thisTestSlot))
                {
                    matchingSpacesToRight++;
                    distanceGoneToRight++;
                }
                else
                {
                    stillMatching = false;
                }
            }
            else if (whosColor.equals("myColor"))
            {
                if (isMyColor(thisTestSlot))
                {
                    matchingSpacesToRight++;
                    distanceGoneToRight++;
                }
                else
                {
                    stillMatching = false;
                }
            }
        }
        return matchingSpacesToRight;
    }

    public int getColumnsToRight(int columnIndex)
    {
        return myGame.getColumnCount()-columnIndex-1;
    }

    //checks if moving on specified column index offers the opponent an immediate horizontal win
    public boolean thisMoveOffersImmediateHorizontalWin(int columnIndex) 
    {
        int nextPlayableSlotIndex = getLowestEmptyIndex(myGame.getColumn(columnIndex)) - 1;
        if (nextPlayableSlotIndex < 0)
        {            
            return false;
        }
        else 
        {
            int matchesToLeft = matchingSpacesToLeft(columnIndex, nextPlayableSlotIndex, "notMyColor");
            int matchesToRight = matchingSpacesToRight(columnIndex, nextPlayableSlotIndex, "notMyColor");
            if (matchesToRight+matchesToLeft >=3)
            {
                return true;
            }
            else
            {
                return false;
            }
        }
    }

    public boolean thisMoveOffersImmediateDiagonallWin(int columnIndex) 
    {
        int nextPlayableSlotIndex = getLowestEmptyIndex(myGame.getColumn(columnIndex)) - 1;
        if (nextPlayableSlotIndex < 0)
        {            
            return false;
        }
        else
        {
            int diagonalMatchesUpLeft = getDiagonalMatchesUpLeft(columnIndex, nextPlayableSlotIndex, "notMyColor");
            int diagonalMatchesDownLeft = getDiagonalMatchesDownLeft(columnIndex, nextPlayableSlotIndex, "notMyColor");
            int diagonalMatchesUpRight = getDiagonalMatchesUpRight(columnIndex, nextPlayableSlotIndex, "notMyColor");
            int diagonalMatchesDownRight = getDiagonalMatchesDownRight(columnIndex, nextPlayableSlotIndex, "notMyColor");

            if (diagonalMatchesUpLeft+diagonalMatchesDownRight >=3
                || diagonalMatchesDownLeft+diagonalMatchesUpRight >=3)
            {
                return true;
            }
            else
            {
                return false;
            }
        }   
    }

    public int getDiagonalMatchesUpLeft(int columnIndex, int slotIndex, String whosColor)
    {

        int maxColumnTraversal = getMaxLeftDistance(columnIndex);
        int maxRowTraversal = slotIndex;

        int matchingSlots = 0;
        int columnsTraversed = 1;
        int rowsTraversed = 1;
        boolean stillMatching = true;
        while (columnsTraversed <= maxColumnTraversal
                && rowsTraversed <= maxRowTraversal
                && matchingSlots < 3
                && stillMatching)
        {
            Connect4Slot thisTestSlot = myGame.getColumn(columnIndex - columnsTraversed).getSlot(slotIndex - rowsTraversed);
            if (whosColor.equals("notMyColor"))
            {
                if (isOpponentsColor(thisTestSlot))
                {
                    matchingSlots++;
                    columnsTraversed++;
                    rowsTraversed++;
                }
                else
                {
                    stillMatching = false;
                }
            }
            else if (whosColor.equals("myColor"))
            {
                if (isMyColor(thisTestSlot))
                {
                    matchingSlots++;
                    columnsTraversed++;
                    rowsTraversed++;
                }
                else
                {
                    stillMatching = false;

                }
            }
        }
        return matchingSlots;

    }

    public int getDiagonalMatchesDownLeft(int columnIndex, int slotIndex, String whosColor)
    {
        int maxColumnTraversal = getMaxLeftDistance(columnIndex);
        int maxRowTraversal = getSlotsBelow(slotIndex);

        int matchingSlots = 0;
        int columnsTraversed = 1;
        int rowsTraversed = 1;
        boolean stillMatching = true;
        while (columnsTraversed <= maxColumnTraversal
                && rowsTraversed <= maxRowTraversal
                && matchingSlots < 3
                && stillMatching)
        {
            Connect4Slot thisTestSlot = myGame.getColumn(columnIndex - columnsTraversed).getSlot(slotIndex+rowsTraversed);
            if (whosColor.equals("notMyColor"))
            {
                if (isOpponentsColor(thisTestSlot))
                {
                    matchingSlots++;
                    columnsTraversed++;
                    rowsTraversed++;
                }
                else
                {
                    stillMatching = false;
                }
            }
            else if (whosColor.equals("myColor"))
            {
                if (isMyColor(thisTestSlot))
                {
                    matchingSlots++;
                    columnsTraversed++;
                    rowsTraversed++;
                }
                else
                {
                    stillMatching = false;
                }
            }
        }
        return matchingSlots;

    }

    public int getDiagonalMatchesUpRight(int columnIndex, int slotIndex, String whosColor)
    {
        int maxColumnTraversal = getMaxRightDistance(columnIndex);
        int maxRowTraversal = slotIndex;

        int matchingSlots = 0;
        int columnsTraversed = 1;
        int rowsTraversed = 1;
        boolean stillMatching = true;
        while (columnsTraversed <= maxColumnTraversal
                && rowsTraversed <= maxRowTraversal
                && matchingSlots < 3
                && stillMatching)
        {
            Connect4Slot thisTestSlot = myGame.getColumn(columnIndex + columnsTraversed).getSlot(slotIndex-rowsTraversed);
            if (whosColor.equals("notMyColor"))
            {
                if (isOpponentsColor(thisTestSlot))
                {
                    matchingSlots++;
                    columnsTraversed++;
                    rowsTraversed++;
                }
                else
                {
                    stillMatching = false;
                }
            }
            else if (whosColor.equals("myColor"))
            {
                if (isMyColor(thisTestSlot))
                {
                    matchingSlots++;
                    columnsTraversed++;
                    rowsTraversed++;
                }
                else
                {
                    stillMatching = false;
                }
            }
        }
        return matchingSlots;

    }

    public int getDiagonalMatchesDownRight(int columnIndex, int slotIndex, String whosColor)
    {
        int maxColumnTraversal = getMaxRightDistance(columnIndex);
        int maxRowTraversal = getSlotsBelow(slotIndex);

        int matchingSlots = 0;
        int columnsTraversed = 1;
        int rowsTraversed = 1;
        boolean stillMatching = true;
        while (columnsTraversed <= maxColumnTraversal
                && rowsTraversed <= maxRowTraversal
                && matchingSlots < 3
                && stillMatching)
        {
            Connect4Slot thisTestSlot = myGame.getColumn(columnIndex + columnsTraversed).getSlot(slotIndex+rowsTraversed);
            if (whosColor.equals("notMyColor"))
            {
                if (isOpponentsColor(thisTestSlot))
                {
                    matchingSlots++;
                    columnsTraversed++;
                    rowsTraversed++;
                }
                else
                {
                    stillMatching = false;
                }
            }
            else if (whosColor.equals("myColor"))
            {
                if (isMyColor(thisTestSlot))
                {
                    matchingSlots++;
                    columnsTraversed++;
                    rowsTraversed++;
                }
                else
                {
                    stillMatching = false;
                }
            }
        }
        return matchingSlots;

    }

    public int findDiagonalWinColumn(String whosColor)
    {
        
        int thisColumnIndex = 0;
        int winningColumnIndex = -1;
        
        while (thisColumnIndex<myGame.getColumnCount() && winningColumnIndex == -1)
        {
            if(!myGame.getColumn(thisColumnIndex).getIsFull())
            {
                int playableSlotIndex = getLowestEmptyIndex(myGame.getColumn(thisColumnIndex));
                int diagonalMatchesUpLeft = getDiagonalMatchesUpLeft(thisColumnIndex, playableSlotIndex, whosColor);
                int diagonalMatchesDownLeft = getDiagonalMatchesDownLeft(thisColumnIndex, playableSlotIndex, whosColor);
                int diagonalMatchesUpRight = getDiagonalMatchesUpRight(thisColumnIndex, playableSlotIndex, whosColor);
                int diagonalMatchesDownRight = getDiagonalMatchesDownRight(thisColumnIndex, playableSlotIndex, whosColor);

                if (diagonalMatchesUpLeft+diagonalMatchesDownRight >=3
                    || diagonalMatchesDownLeft+diagonalMatchesUpRight >=3)
                {
                    winningColumnIndex = thisColumnIndex;
                }    
            }

            thisColumnIndex++;
        }
        return winningColumnIndex;
    }

    public int getSlotsBelow(int slotIndex)
    {
        return myGame.getColumn(0).getRowCount() - 1 - slotIndex;
    }

    public int getMaxLeftDistance(int columnIndex)
    {
        int maxLeftDistance = -1;
        if (columnIndex >= 3)
        {
            maxLeftDistance = 3;
        }
        else
        {
            maxLeftDistance = columnIndex;
        }
        return maxLeftDistance;
    }

    public int getMaxRightDistance(int columnIndex)
    {
        int columnsToRight = getColumnsToRight(columnIndex);

        int maxDistance = -1;
        if (columnsToRight >= 3)
        {
            maxDistance = 3;
        }
        else
        {
            maxDistance = columnsToRight;
        }

        return maxDistance;

    }

    /**
    *Traverses horizontally in the specified direction and finds the number of consecutive matches for a specified color
    *@param direction specifies the direction in which to tranverse: -1 = left, +1 = right
    *@param whosColor specifies which color is being checked for: "myColor" = this player's color, "notMyColor" = the opponents color
    *@return the number of uninterrrupted matches found in the specified direction for the specified color
    */
    // return marked as void for now so as to avoid compile-time errors
    public void horizontalTraverseForMatches(int direction, String whosColor)
    {
        

    }


}
