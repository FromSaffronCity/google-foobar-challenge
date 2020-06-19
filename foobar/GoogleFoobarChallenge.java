package foobar;

import java.util.Scanner;
import java.math.BigInteger;
import java.util.Hashtable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.PriorityQueue;

class ElevatorVersion implements Comparable<ElevatorVersion> {
    private String elevatorVersionNumber;

    public ElevatorVersion(String elevatorVersionNumber) {
        this.elevatorVersionNumber = elevatorVersionNumber;
    }

    @Override
    public String toString() {
        return elevatorVersionNumber;
    }

    @Override
    public int compareTo(ElevatorVersion elevatorVersion) {
        String[] temp1 = this.elevatorVersionNumber.split("\\.");
        String[] temp2 = elevatorVersion.toString().split("\\.");
        int[] int_temp1 = new int[temp1.length];
        int[] int_temp2 = new int[temp2.length];

        for(int i=0; i<int_temp1.length; i++) {
            int_temp1[i] = Integer.parseInt(temp1[i]);
        }
        for(int i=0; i<int_temp2.length; i++) {
            int_temp2[i] = Integer.parseInt(temp2[i]);
        }

        if(int_temp1[0] != int_temp2[0]) {
            return int_temp1[0] - int_temp2[0];
        } else {
            if(int_temp1.length==1 && int_temp2.length==1) {
                return 0;
            } else if(int_temp1.length==1 && int_temp2.length>1) {
                return -1;
            } else if(int_temp1.length>1 && int_temp2.length==1) {
                return 1;
            } else if(int_temp1.length>1 && int_temp2.length>1) {
                if(int_temp1[1] != int_temp2[1]) {
                    return int_temp1[1] - int_temp2[1];
                } else {
                    if(int_temp1.length==2 && int_temp2.length==2) {
                        return 0;
                    } else if(int_temp1.length==2 && int_temp2.length>2) {
                        return -1;
                    } else if(int_temp1.length>2 && int_temp2.length==2) {
                        return 1;
                    } else if(int_temp1.length>2 && int_temp2.length>2) {
                        if(int_temp1[2] != int_temp2[2]) {
                            return int_temp1[2] - int_temp2[2];
                        } else {
                            return 0;
                        }
                    }
                }
            }
        }
        return 0;
    }
}

class Coordinate {
    private int x;
    private int y;

    public Coordinate(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public boolean isEqual(Coordinate coordinate) {
        return (this.x==coordinate.x && this.y==coordinate.y);
    }
}

class PathNode implements Comparable<PathNode> {
    private Coordinate current;
    private static Coordinate goal;
    private boolean isWallRemoved;
    private PathNode parent;
    private int distanceFromStart;

    public PathNode(Coordinate current, boolean isWallRemoved, PathNode parent, int distanceFromStart) {
        this.current = current;
        this.isWallRemoved = isWallRemoved;
        this.parent = parent;
        this.distanceFromStart = distanceFromStart;
    }

    public static void setGoal(Coordinate goalCoordinate) {
        goal = goalCoordinate;
        return ;
    }

    public void setIsWallRemoved(boolean isWallRemoved) {
        this.isWallRemoved = isWallRemoved;
        return ;
    }

    public boolean isWallRemoved() {
        return isWallRemoved;
    }

    public boolean isGoal() {
        return current.isEqual(goal);
    }

    public Coordinate getCurrent() {
        return current;
    }

    public int getFnDistance() {
        return this.distanceFromStart+(this.goal.getX()-this.current.getX())+(this.goal.getY()-this.current.getY());
    }

    public PathNode[] getChildren() {
        PathNode[] children = new PathNode[3];
        int index = 0;

        if(parent!=null && (!parent.current.isEqual(new Coordinate(current.getX()-1, current.getY())) && current.getX()>0)) {
            children[index++] = new PathNode(new Coordinate(current.getX()-1, current.getY()), isWallRemoved, this, distanceFromStart+1);
        }
        if(parent==null || (!parent.current.isEqual(new Coordinate(current.getX()+1, current.getY())) && current.getX()<goal.getX())) {
            children[index++] = new PathNode(new Coordinate(current.getX()+1, current.getY()), isWallRemoved, this, distanceFromStart+1);
        }
        if(parent!=null && (!parent.current.isEqual(new Coordinate(current.getX(), current.getY()-1)) && current.getY()>0)) {
            children[index++] = new PathNode(new Coordinate(current.getX(), current.getY()-1), isWallRemoved, this, distanceFromStart+1);
        }
        if(parent==null || (!parent.current.isEqual(new Coordinate(current.getX(), current.getY()+1)) && current.getY()<goal.getY())) {
            children[index++] = new PathNode(new Coordinate(current.getX(), current.getY()+1), isWallRemoved, this, distanceFromStart+1);
        }
        return children;
    }

    @Override
    public int compareTo(PathNode pathNode) {
        int this_fn = this.distanceFromStart+(this.goal.getX()-this.current.getX())+(this.goal.getY()-this.current.getY());
        int pathNode_fn = pathNode.distanceFromStart+(pathNode.goal.getX()-pathNode.current.getX())+(pathNode.goal.getY()-pathNode.current.getY());

        return this_fn-pathNode_fn;
    }

    @Override
    public boolean equals(Object object) {
        if(this == object) {
            return true;
        }
        if(object==null || this.getClass()!=object.getClass()) {
            return false;
        }

        PathNode pathNode = (PathNode) object;
        return (current.isEqual(pathNode.current) && this.isWallRemoved==pathNode.isWallRemoved);
    }

    @Override
    public int hashCode() {
        return (current.getX()*10+current.getY())<<1+(isWallRemoved? 1: 0);
    }
}

public class GoogleFoobarChallenge {
    private static Scanner scanner = new Scanner(System.in);

    public static int[] solution1(int[] data, int n) {
        /* minion labor shifts */
        Hashtable<Integer, Integer> hashTable = new Hashtable<>();
        for(int i=0; i<data.length; i++){
            if(!hashTable.containsKey(data[i])) {
                hashTable.put(data[i], 1);
            } else {
                hashTable.replace(data[i], hashTable.get(data[i])+1);
            }
        }

        ArrayList<Integer> arrayList = new ArrayList<>();
        for(int i=0; i<data.length; i++){
            if(hashTable.get(data[i]) <= n) {
                arrayList.add(data[i]);
            }
        }

        int[] solution = new int[arrayList.size()];
        for(int i=0; i<solution.length; i++){
            solution[i] = arrayList.get(i);
        }

        return solution;
    }

    public static int solution2A(String s) {
        /* en route salute */
        int total_salute, current_left;
        total_salute = current_left = 0;

        for(int i=s.length()-1; i>=0; i--) {
            if(s.charAt(i) == '<') {
                current_left++;
            }
            if(s.charAt(i) == '>') {
                total_salute += current_left*2;
            }
        }

        return total_salute;
    }

    public static String[] solution2B(String[] l) {
        /* elevator maintenance */
        ArrayList<ElevatorVersion> arrayList = new ArrayList<>();
        for(int i=0; i<l.length; i++) {
            arrayList.add(new ElevatorVersion(l[i]));
        }
        Collections.sort(arrayList);

        for(int i=0; i<l.length; i++) {
            l[i] = arrayList.get(i).toString();
        }
        return l;
    }

    public static String solution3A(String x, String y) {
        /* bomb, baby! */
        BigInteger machBomb = new BigInteger(x);
        BigInteger faculaBomb = new BigInteger(y);
        BigInteger minimumGenerations = BigInteger.ZERO;
        boolean isPossible = true;

        while(!machBomb.equals(BigInteger.ONE) && !faculaBomb.equals(BigInteger.ONE)) {
            if(machBomb.equals(BigInteger.ZERO) || faculaBomb.equals(BigInteger.ZERO)) {
                isPossible = false;
                break;
            }

            if(machBomb.compareTo(faculaBomb) < 0) {
                minimumGenerations = minimumGenerations.add(faculaBomb.divide(machBomb));
                faculaBomb = faculaBomb.mod(machBomb);
            } else {
                minimumGenerations = minimumGenerations.add(machBomb.divide(faculaBomb));
                machBomb = machBomb.mod(faculaBomb);
            }
        }

        if(isPossible) {
            if(machBomb.equals(BigInteger.ONE)) {
                return minimumGenerations.add(faculaBomb.divide(machBomb).subtract(BigInteger.ONE)).toString();
            } else {
                return minimumGenerations.add(machBomb.divide(faculaBomb).subtract(BigInteger.ONE)).toString();
            }
        } else {
            return "impossible";
        }
    }

    public static int countStaircase(int current_height, int bricks_left, int n) {
        if(current_height == bricks_left) {
            return 1;
        } else {
            if(bricks_left==n && bricks_left-(current_height+1)<(current_height+2)) {
                return countStaircase(current_height+1, bricks_left-current_height, n);
            } else if(bricks_left-current_height < current_height+1) {
                return 1;
            } else {
                return countStaircase(current_height+1, bricks_left-current_height, n)+countStaircase(current_height+1, bricks_left, n);
            }
        }
    }

    public static int solution3B(int n) {
        /* the grandest staircase of them all */
        return countStaircase(1, n, n);
    }

    public static int solution3C(int[][] map) {
        /* prepare the bunnies' escape */
        PriorityQueue<PathNode> openListPQ = new PriorityQueue<>();
        Hashtable<PathNode, Integer> openList = new Hashtable<>();
        Hashtable<PathNode, Integer> closedList = new Hashtable<>();

        PathNode.setGoal(new Coordinate(map.length-1, map[0].length-1));
        PathNode tempNode = new PathNode(new Coordinate(0, 0), false, null, 1);
        openListPQ.add(tempNode);
        openList.put(tempNode, tempNode.getFnDistance());

        boolean isGoalFound = false;
        while(!isGoalFound && !openListPQ.isEmpty()) {
            tempNode = openListPQ.poll();
            closedList.put(tempNode, tempNode.getFnDistance());

            PathNode[] children = tempNode.getChildren();
            for(int i=0; i<children.length; i++) {
                if(children[i]==null || (children[i].isWallRemoved() && map[children[i].getCurrent().getX()][children[i].getCurrent().getY()]==1)) {
                    continue;
                }
                if(children[i].isGoal()) {
                    isGoalFound = true;
                    break;
                }
                if(!children[i].isWallRemoved() && map[children[i].getCurrent().getX()][children[i].getCurrent().getY()]==1) {
                    children[i].setIsWallRemoved(true);
                }
                if(closedList.containsKey(children[i])) {
                    continue;
                }
                if(openList.containsKey(children[i]) && openList.get(children[i])<=children[i].getFnDistance()) {
                    continue;
                }
                openListPQ.add(children[i]);
                openList.put(children[i], children[i].getFnDistance());
            }
        }

        return tempNode.getFnDistance();
    }

    public static void main(String[] args) {

        return ;
    }
}
