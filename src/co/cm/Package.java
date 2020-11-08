package co.cm;

import java.util.HashSet;

public class Package extends Named
{
    private HashSet<Named> namedSet;

    public Package(String name)
    {
        super(name);
        namedSet = new HashSet<>();
    }

    public Package add(Named named)
    {
        named.setSuperNamed(this);
        namedSet.add(named);
        return this;
    }

    public HashSet<Named> getNamedSet()
    {
        return namedSet;
    }

    public void setNamedSet(HashSet<Named> namedSet)
    {
        this.namedSet = namedSet;
    }

    public HashSet<Command> bulkCommands()
    {
        HashSet<Command> commands = new HashSet<>();
        for(var named : namedSet)
        {
            if(named instanceof Command)
                commands.add((Command) named);
            else
                commands.addAll(((Package)named).bulkCommands());
        }
        return commands;
    }

    @Override
    public String toString()
    {
        String buffer = "PACKAGE: " + getFullName() + "\n";
        for(var named : namedSet)
        {
            buffer += "\t\t" + named.toString() + "\n";
        }
        return buffer;
    }
}
