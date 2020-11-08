package co.cm;

import javax.swing.*;

public class Main {

    public static void main(String[] args)
    {
        Interpreter interpreter = new Interpreter();

        Context context = new Context();

        Package pkg = new Package("std");

        Command nop = new Command("deco").setAction(1, args1 -> "[{(" + args1.get(0) + ")}]");
        Command ask = new Command("ask").setAction(1, args1 -> JOptionPane.showInputDialog(args1.get(0)));
        pkg.add(nop);
        pkg.add(ask);

        context.getPackages().add(pkg);


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
