package co.cm;

import java.util.HashSet;

public class Context
{
    private HashSet<Package> packages;

    public Context()
    {
        packages = new HashSet<>();
    }

    public HashSet<Package> getPackages()
    {
        return packages;
    }

    public void setPackages(HashSet<Package> packages)
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
