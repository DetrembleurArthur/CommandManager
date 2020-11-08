package co.cm;

import java.io.*;
import java.util.ArrayList;
import java.util.HashSet;

public class Interpreter
{
    private Context context;
    private ArrayList<Command> commandBulk;

    public Interpreter()
    {
        context = new Context();
        commandBulk = new ArrayList<>();
    }

    public Context getContext()
    {
        return context;
    }

    public void setContext(Context context)
    {
        this.context = context;
        commandBulk.clear();
        for(var pkg : context.getPackages())
        {
            commandBulk.addAll(pkg.bulkCommands());
        }
        System.err.println(commandBulk.get(0));
    }

    public static ArrayList<String> parse(String expr)
    {
        String[] tokens = expr.replace("\n", " ").split(" ");
        boolean solidExpression = false;
        String buffer = "";
        ArrayList<String> v_tokens = new ArrayList<>();
        for(String token : tokens)
        {
            if(token.length() > 0)
            {
                if(solidExpression)
                {
                    buffer += " " + token;
                    if(token.charAt(token.length() - 1) == '\"')
                    {
                        v_tokens.add(buffer);
                        solidExpression = false;
                    }
                }
                else
                {
                    if(token.charAt(0) == '\"')
                    {
                        solidExpression = true;
                        buffer = token;
                    }
                    else
                    {
                        v_tokens.add(token);
                    }
                }
            }
        }
        return v_tokens;
    }

    public String interpret(String expr) throws Exception
    {
        var tokens = parse(expr);
        String res = "";
        while(tokens.size() > 0)
        {
            res += execute(tokens, null, 0, null) + ", ";
            //System.err.println(tokens.size());
        }

        return res;
    }

    public Command bulkCommand(String name)
    {
        for(var cmd : commandBulk)
        {
            if(cmd.equals(name))
            {
                return cmd;
            }
        }
        return null;
    }

    public String[] attributes(String expr)
    {
        return expr.split(":");
    }

    public String execute(ArrayList<String> tokens, Command command, int i, Attributes attributes) throws Exception
    {
        if(command != null)
        {
            //System.err.println("COMMAND: " + command.getFullName());
            int nargs = 0;
            ArrayList<String> args = new ArrayList<>();
            nargs = tokens.size() - i;
            if(attributes.getNargs() > -1)
            {
                if(!command.hasPrototype(attributes.getNargs()))
                    throw new Exception(command.getFullName() +  " has not a prototype with " + attributes.getNargs() + " arguments");
                if(nargs < attributes.getNargs())
                {
                    throw new Exception(nargs + " arguments given but expected " + attributes.getNargs());
                }
                nargs = attributes.getNargs();
            }
            else
            {
                //System.err.println("MAX: " + command.getMaxArgs());
                if(nargs > command.getMaxArgs())
                    nargs = command.getMaxArgs();
            }
            //System.err.println("ARGS: " + nargs);
            if(tokens.size() > 0)
            {
                if(!attributes.isRoot() && ! attributes.isConstexpr())
                {
                    Command sub = command.getSub(tokens.get(0));
                    if (sub != null)
                    {
                        //System.err.println("SUBCOMMAND: " + sub.getFullName());
                        tokens.remove(0);
                        return execute(tokens, sub, i, attributes);
                    }
                }

                for (int j = i; j < tokens.size() && (args.size() < nargs || attributes.isUndefined() || nargs == -1); j++)
                {
                    var attr = new Attributes(tokens.get(j));
                    if(attr.isHas())
                        tokens.set(j, tokens.get(j).substring(0, tokens.get(j).indexOf(':')));
                    Command cmd = null;
                    if(!attr.isConstexpr())
                        cmd = bulkCommand(tokens.get(j));
                    if (cmd != null)
                    {
                        var res = execute(tokens, cmd, j + 1, attr);
                        //System.err.println("CMD RESULT: " + res);
                        args.add(res);
                    } else
                    {
                       // System.err.println("CONST RESULT: " + tokens.get(j));
                        args.add(tokens.get(j));
                    }
                }
                for (int j = 0; j < args.size(); j++)
                {
                    tokens.remove(i);
                }
            }
            if(!attributes.isUndefined() && attributes.getNargs() > -2 && attributes.getNargs() != args.size())
                throw new Exception(nargs + " arguments given but expected " + attributes.getNargs());
            return command.execute(args, attributes.isUndefined());
        }
        else
        {
            var attr = new Attributes(tokens.get(0));
            if(attr.isHas())
                tokens.set(0, tokens.get(0).substring(0, tokens.get(0).indexOf(':')));
            Command cmd = bulkCommand(tokens.get(0));
            if(cmd == null)
                throw new Exception(tokens.get(0) + " is not a command");
            tokens.remove(0);
            return execute(tokens, cmd, i, attr);
        }
    }
    
    public String executeFile(String filename)
    {
        try
        {
            BufferedReader reader = new BufferedReader(new FileReader(filename));
            String buffer = "";
            String buf = null;
            while((buf = reader.readLine()) != null)
                buffer += buf + " ";
            reader.close();
            return interpret(buffer);
        } catch (FileNotFoundException e)
        {
            e.printStackTrace();
        } catch (IOException e)
        {
            e.printStackTrace();
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }
}
