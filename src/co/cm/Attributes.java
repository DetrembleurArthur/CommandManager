package co.cm;

public class Attributes
{
    private boolean undefined = false;
    private boolean has = false;
    private boolean constexpr = false;
    private int nargs;
    private boolean root;

    public Attributes()
    {
        nargs = -2;
        root = false;
    }

    public Attributes(String expr)
    {
        this();
        if(expr.indexOf(':') == -1)
        {
            return;
        }
        has = true;
        var splitted = expr.split(":");
        for(int i = 1; i < splitted.length; i++)
        {
            if(splitted[i].equals("#"))
            {
                root = true;
            }
            else if(splitted[i].equals("*"))
            {
                undefined = true;
            }
            else if(splitted[i].equals("!"))
            {
                constexpr = true;
            }
            else
            {
                try
                {
                    nargs = Integer.parseInt(splitted[i]);
                    if(nargs < 0)
                        throw new NumberFormatException();
                }
                catch (NumberFormatException e)
                {

                }
            }
        }
    }

    public int getNargs()
    {
        return nargs;
    }

    public boolean isRoot()
    {
        return root;
    }

    public boolean has()
    {
        return has;
    }

    public boolean isUndefined()
    {
        return undefined;
    }

    public boolean isConstexpr()
    {
        return constexpr;
    }
}
