package co.cm;

import java.util.ArrayList;
import java.util.HashSet;

public class Context
{
    private ArrayList<Package> packages;

    public Context()
    {
        packages = new ArrayList<>();
    }

    public ArrayList<Package> getPackages()
    {
        return packages;
    }

    public void setPackages(ArrayList<Package> packages)
    {
        this.packages = packages;
    }

    @Override
    public String toString()
    {
        String buffer = "CONTEXT:\n";
        for(Package pkg : packages)
        {
            buffer += "\t" + pkg.toString() + "\n";
        }
        return buffer;
    }
}
