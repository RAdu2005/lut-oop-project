package com.example.oopandroidapi;


public class CrimeData {
    private int violentCrimes, nonViolentCrimes;

    public CrimeData(int violentCrimes, int nonViolentCrimes){
        this.violentCrimes = violentCrimes;
        this.nonViolentCrimes = nonViolentCrimes;
    }
    
    public int getViolentCrimes(){
        return violentCrimes;
    }

    public int getNonViolentCrimes(){
        return nonViolentCrimes;
    }
}
