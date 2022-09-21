package ui;


import logic.*;

import javax.swing.*;
import java.sql.ResultSet;
import java.sql.SQLException;

public class JLists {
    static DBLogic dbLogic;

    static {
        try {
            dbLogic = new DBLogic();
        } catch (ErrorException e) {
            throw new RuntimeException(e);
        }
    }

    static Logic logic = dbLogic.passConnectionL();

    public JLists() {
    }

    public static JList<Integer> years() throws SQLException {
        Integer[] YearsTable = new Integer[logic.sizes(2)];
        for (int y = 0; y < logic.sizes(2); y++) {
            YearsTable[y]= 1950+y;
        }
        return new JList<>(YearsTable);
    }

    public static JList<String> drivers() throws SQLException {
        ResultSet DriversRes = logic.resForCombo(1);
        String[] DriverTable = new String[logic.sizes(3)];
        int d=0;
        while (DriversRes.next()){
            DriverTable[d]=DriversRes.getString("Driver");
            d++;
        }
        return new JList<>(DriverTable);
    }

    public static  JList<String> teams() throws SQLException {
        ResultSet TeamsRes = logic.resForCombo(2);
        String[] TeamsTable = new String[logic.sizes(4)];
        int t = 0;
        while (TeamsRes.next()){
            TeamsTable[t]=TeamsRes.getString("Constructor");
            t++;
        }
        return new JList<>(TeamsTable);
    }

    public static JList<String> engines() throws SQLException{
        ResultSet ESupplierRes = logic.resForCombo(3);
        String[] ESupplierTable = new String[logic.sizes(5)];
        int eS=0;
        while (ESupplierRes.next()){
            ESupplierTable[eS]=ESupplierRes.getString("EngineSupplier");
            eS++;
        }
        return new JList<>(ESupplierTable);
    }
}
