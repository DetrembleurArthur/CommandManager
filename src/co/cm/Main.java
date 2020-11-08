package co.cm;

import javax.swing.*;

public class Main {

    public static void main(String[] args)
    {
        Interpreter interpreter = new Interpreter();

        Context context = new Context();

        Package pkg = new Package("basic");
        Package pkg1 = new Package("second");

        Command add = new Command("add").setAction(2, args1 -> String.valueOf(Integer.parseInt(args1.get(0)) + Integer.parseInt(args1.get(1))));

        pkg.add(add);

        Command add1 = new Command("add").setAction(2, args1 -> args1.get(0) + "+" + args1.get(1));
        pkg1.add(add1);

        context.getPackages().add(pkg);
        context.getPackages().add(pkg1);

        interpreter.setContext(context);


        try
        {
            System.err.println(interpreter.executeFile("src/co/cm/code.txt"));
        } catch (Exception e)
        {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, e.getMessage());
        }

        System.err.println(context);
    }
}
