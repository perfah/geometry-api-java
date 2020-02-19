package com.esri.core.geometry;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class BranchCoverage {
    public String funcName;
    private static HashMap<String, BranchCoverage> instances = new HashMap<>();
    private HashMap<Integer, Boolean> coverage;
    private int pos;

    public static BranchCoverage ofFunction(String funcName){
        BranchCoverage bc = BranchCoverage.instances.getOrDefault(funcName, new BranchCoverage(funcName));
        BranchCoverage.instances.putIfAbsent(bc.funcName, bc);
        bc.reset();
        return bc;
    }

    private BranchCoverage(String funcName){
        this.funcName = funcName;
        coverage = new HashMap<>();
        pos = 0;
    }

    public void reset(){
        pos = 0;
    }

    // Gives a set of ID's that each point towards a branch corresponding to a condition (ifCondition or
    // elseIfConditions or the else condition).
    // parentBranchID: the
    public void addBranchingPoint(boolean ifCondition, boolean... elseIfConditions){
        boolean pathAlreadyChoosen = ifCondition;

        // IF
        coverage.put(pos,  ifCondition || coverage.getOrDefault(pos, false));
        pos++;

        // ELSE-IF
        for(boolean elseIfCond : elseIfConditions){
            coverage.put(pos,  (!pathAlreadyChoosen && elseIfCond) || coverage.getOrDefault(pos, false));
            pos++;

            if(!pathAlreadyChoosen)
                pathAlreadyChoosen = elseIfCond;
        }

        // ELSE / NOTHING
        coverage.put(pos, !pathAlreadyChoosen || coverage.getOrDefault(pos, false));
        pos++;
    }

    public int countVisitedBranches(){
        int numVisistedBranches = 0;
        for(boolean boolVal : coverage.values()){
            if(boolVal)
                numVisistedBranches++;
        }
        return numVisistedBranches;
    }

    public int countTotalBranches(){
        return coverage.size();
    }

    public double getBranchCoverageRatio(){
        return (double) countVisitedBranches() / (double) countTotalBranches();
    }

    @Override
    public String toString() {
        return
                "\n ### BRANCH COVERAGE REPORT FOR " + funcName + " ###\n" +
                        "   * Visisted branches: " + countVisitedBranches() + "\n" +
                        "   * Total branches: " + countTotalBranches() + "\n" +
                        "   * Coverage: " + (int)(100 * getBranchCoverageRatio()) + "%\n";
    }
}
