package com.acmemail.judah.sanbox;

public class LabData
{
    private double[]    results;
    public LabData()
    {
        // TODO Auto-generated constructor stub
    }
 
    public double[] getResults()
    {
        return results;
    }
 
    public void setResults(double[] results)
    {
        this.results = results;
    }

    public double getResult( int index )
    {
        return results[index];
    }
    
    public void setResult( double value, int index )
    {
        results[index] = value;
    }
}
