package co.cm;

public class Named
{
    private String name;
    private Named superNamed;

    public Named(String name)
    {
        this.name = name;
    }

    public Named(String name, Named superNamed)
    {
        this(name);
        this.superNamed = superNamed;
    }

    public Named getSuperNamed()
    {
        return superNamed;
    }

    public void setSuperNamed(Named superNamed)
    {
        this.superNamed = superNamed;
    }

    public String getFullName()
    {
        if(superNamed != null)
        {
            return superNamed.getFullName() + "." + getName();
        }
        return getName();
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public boolean equals(String token)
    {
        return getName().equals(token) || getFullName().equals(token);
    }


    public boolean equals(Named o)
    {
        return equals(o.getFullName());
    }

    @Override
    public String toString()
    {
        return getFullName();
    }
}
