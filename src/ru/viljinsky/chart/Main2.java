package ru.viljinsky.chart;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Main2 extends JFrame{
    Chart chart;
    ChartSeries series1;
    JToolBar toolBar;
    
    public Main2(){
        super("Main2");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        initComponents(getContentPane());
        pack();
    }
    
    public void initComponents(Container content){
        String[] commands={
            "chart1","chart2","chart3"
        };
        
        series1 = new ChartSeries("Series1", Color.yellow);
        series1.addValue(1,new Integer(2));
        series1.addValue(2,new Integer(4));
        series1.addValue(3,new Integer(5));
        series1.addValue(4,new Integer(3));
        series1.addValue(5,new Integer(10));
        series1.rebuild();
        
    
        chart = new Chart();
        chart.getYAxis().setMinValue(0);
        chart.getYAxis().setMaxValue(10);
        chart.getXAxis().setRange(0,100);
//        chart.getXAxis().setAutoRange(false);
        
        content.add(chart);
        toolBar = new JToolBar();
        for (String command:commands){
            toolBar.add(new AbstractAction(command) {
                
                @Override
                public void actionPerformed(ActionEvent e) {
                    doCommand(e.getActionCommand());
                }
            });
        }
        content.add(toolBar,BorderLayout.PAGE_START);
        
        chart.addMouseListener(new MouseAdapter() {

            @Override
            public void mousePressed(MouseEvent e) {
                int x= e.getX();
                int y = e.getY();
                ChartBar bar = chart.hitTest(x,y);
                if (bar!=null){
                    System.out.println(bar);
                }
            }
            
        });
    }
    
    public void doCommand(String command){
        
        switch (command){
            case "chart1":
                chart.addSeries(series1);
                chart.updateUI();
                break;                
            case "chart2":
                chart.removeChartSeries(series1);
                chart.updateUI();
                break;
            case "chart3":
                break;
        }
    }
    
    public static void main(String[] args){
        Main2 frame = new Main2();
        frame.setVisible(true);
    }
}
