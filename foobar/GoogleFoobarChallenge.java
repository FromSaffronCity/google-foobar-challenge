package foobar;

import java.math.BigInteger;
import java.util.Hashtable;
import java.util.ArrayList;
import java.util.Collections;

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

public class GoogleFoobarChallenge {
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

    public static void main(String[] args) {
        System.out.println(solution3A("2", "2"));
        return ;
    }
}
