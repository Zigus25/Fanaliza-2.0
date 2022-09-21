package ui;

import logic.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.SQLException;
import java.util.concurrent.atomic.AtomicInteger;

public class Window {

    DBLogic dbLogic = new DBLogic();
    Logic logic = dbLogic.passConnectionL();
    StatisticAna statistic = dbLogic.passConnectionS();

    JFrame frame;

    public Window() throws ErrorException, SQLException{


        this.frame = new JFrame("Fanaliza");
        this.frame.setSize(800,600);
        this.frame.setLayout(new BorderLayout());



        //Tables for JList
        JList<Integer> Years = JLists.years();
        JList<String> Drivers = JLists.drivers();
        JList<String> Teams = JLists.teams();
        JList<String> ESuppliers = JLists.engines();


        JPanel MainPanel = new JPanel(new BorderLayout());
        JPanel statsPanel = new JPanel();
        JLabel statsResults = new JLabel();

        JScrollPane stats = new JScrollPane(statsPanel, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        JPanel optionPanel = new JPanel(new GridLayout(1,5));
        JLabel blank = new JLabel();
        JButton show = new JButton("Show");
        AtomicInteger shoWhi = new AtomicInteger();
        show.addActionListener(e->{
            System.out.println(shoWhi);
            switch(shoWhi.get()){
                case 1, 2 -> {
                    var year = Years.getSelectedValue();
                    try {
                        statsResults.setText(statistic.convertForLabel(shoWhi.get(),"",year,0));
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }
                }
            }

            statsPanel.add(statsResults);
            MainPanel.add(stats,BorderLayout.CENTER);
            MainPanel.repaint();
            MainPanel.revalidate();
        });



//        optionPanel.add(new JScrollPane(Years));
//        optionPanel.add(new JScrollPane(Drivers));
//        optionPanel.add(new JScrollPane(Teams));
//        optionPanel.add(new JScrollPane(ESuppliers));
//        optionPanel.add(show);



        JPanel sideButtonPanel = new JPanel(new GridLayout(50,1));

        //DriversChampionships
        JButton yearD = new JButton("Drivers Championship");
        yearD.addActionListener(e->{
            shoWhi.set(1);
            MainPanel.removeAll();
            optionPanel.removeAll();
            Years.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            optionPanel.add(new JScrollPane(Years));
            for (int i = 0; i < 3; i++)
                optionPanel.add(blank);
            optionPanel.add(show);
            MainPanel.add(optionPanel,BorderLayout.NORTH);
            MainPanel.repaint();
            MainPanel.revalidate();
            optionPanel.revalidate();
            optionPanel.repaint();
        });
        sideButtonPanel.add(yearD);

        //TeamsChampionships
        JButton yearT = new JButton("Team Championship");
        yearT.addActionListener(e->{
            shoWhi.set(2);
            MainPanel.removeAll();
            optionPanel.removeAll();
            Years.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            optionPanel.add(new JScrollPane(Years));
            for (int i = 0; i < 3; i++)
                optionPanel.add(blank);
            optionPanel.add(show);
            MainPanel.add(optionPanel,BorderLayout.NORTH);
            MainPanel.repaint();
            MainPanel.revalidate();
            optionPanel.revalidate();
            optionPanel.repaint();
        });
        sideButtonPanel.add(yearT);


        JScrollPane navButton = new JScrollPane(sideButtonPanel, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);


        frame.add(MainPanel,BorderLayout.CENTER);
        frame.add(navButton,BorderLayout.WEST);

        frame.setVisible(true);

        //Events on closing window
        this.frame.addWindowListener(new WindowAdapter(){
            @Override
            public void windowClosing(WindowEvent e) {
                try {
                    dbLogic.close();
                } catch (SQLException throwable) {
                    throwable.printStackTrace();
                }
                System.exit(0);
            }
        });
    }

}
