package co.cm;

import java.util.*;

public class Command extends Named
{
    private Named superCommand;
    private ArrayList<Command> subcommands;
    private HashMap<Integer, Action> prototypes;

    public Command(String name)
    {
        super(name);
        subcommands = new ArrayList<>();
        prototypes = new HashMap<>();
    }

    private Command(String name, Command superCommand)
    {
        this(name);
        this.superCommand = superCommand;
    }

    public Command setAction(int nparam, Action action)
    {
        prototypes.put(nparam, action);
        return this;
    }

    public int getMaxArgs()
    {
        return prototypes.containsKey(-1) ? -1 : Collections.max(prototypes.keySet());
    }

    public String execute(ArrayList<String> args, boolean undefined) throws Exception
    {
        System.err.println("EXECUTE " + getFullName() + " with " + args.size() + " arguments");
        if(!undefined)
        {
            var action = prototypes.get(args.size());
            if(action == null)
            {
                action = prototypes.get(-1);
                if(action == null)
                {
                    throw new Exception("No prototype with " + args.size() + " arguments for " + getFullName());
                }
            }
            return action.run(args);
        }
        else
        {
            var action = prototypes.get(-1);
            if(action == null)
            {
                throw new Exception("No prototype with ? arguments for " + getFullName());
            }
            return action.run(args);
        }
    }

    public Command createSub(String name)
    {
        var cmd = new Command(name, this);
        subcommands.add(cmd);
        return cmd;
    }


    public ArrayList<Command> getSubcommands()
    {
        return subcommands;
    }

    public void setSubcommands(ArrayList<Command> subcommands)
    {
        this.subcommands = subcommands;
    }

    public Command getSub(String name)
    {
        for(var sub : subcommands)
        {
            if(sub.getName().equals(name))
            {
                return sub;
            }
        }
        return null;
    }



    public boolean hasPrototype(int nargs)
    {
        return prototypes.containsKey(nargs);
    }
}
