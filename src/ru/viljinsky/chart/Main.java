
package ru.viljinsky.chart;

import java.awt.*;
import java.awt.event.ActionEvent;
import javax.swing.*;
import java.util.Set;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;



//------------------------------------------------------------------------------
class Chart extends JPanel {
    List<ChartSeries> series;
    ChartAxis xAxis;
    ChartAxis yAxis;

    public Chart() {
        setPreferredSize(new Dimension(800, 600));
        series = new ArrayList<>();
        xAxis = new ChartAxis(ChartAxis.X_AXIS);
        yAxis = new ChartAxis(ChartAxis.Y_AXIS);
    }

    public ChartBar hitTest(int x, int y) {
        for (ChartSeries s : series) {
            for (ChartBar bar : s.bars) {
                if (bar.bounds.contains(x, y)) {
                    return bar;
                }
            }
        }
        return null;
    }

    public ChartAxis getXAxis() {
        return xAxis;
    }

    public ChartAxis getYAxis() {
        return yAxis;
    }

    public void addSeries(ChartSeries chartSeries) {
        series.add(chartSeries);
        updateAxis();
    }

    public void removeChartSeries(ChartSeries chartSeries) {
        series.remove(chartSeries);
        updateAxis();
    }

    public void updateAxis() {
        int minValue,maxValue;
        int value;
        
        if (xAxis.isAutoRange()) {
            minValue = Integer.MAX_VALUE;
            maxValue = Integer.MIN_VALUE;
            for (ChartSeries s : series) {
                value = s.getMinX();
                if (value < minValue) {
                    minValue = value;
                }
                value = s.getMaxX();
                if (value > maxValue) {
                    maxValue = value;
                }
            }
            xAxis.setRange(minValue, maxValue);
        }
        if (yAxis.isAutoRange()){
            minValue = Integer.MAX_VALUE;
            maxValue = Integer.MIN_VALUE;
            for (ChartSeries s : series) {
                value = s.getMinY();
                if (value < minValue) {
                    minValue = value;
                }
                value = s.getMaxY();
                if (value > maxValue) {
                    maxValue = value;
                }
            }
            yAxis.setRange(minValue, maxValue);
            
        }
    }

    protected void drawSeries(Graphics g, Rectangle drawArray) {
        if (series.isEmpty()) {
            return;
        }
        //        xAxis.value=-1;
        Integer xValue;
        Rectangle r;
        int x,y,h,boxWidth,barWidth,barOffset;
        x = drawArray.x;
        y = drawArray.y;
        h = drawArray.height;
        boxWidth = drawArray.width / (xAxis.maxValue - xAxis.minValue + 1);
        barWidth = boxWidth / series.size();
        float k = drawArray.height / (yAxis.maxValue - yAxis.minValue+1);
        ChartBar bar;
        xAxis.begin();
        while (xAxis.hasNext()) {
            barOffset = 0;
            xValue = xAxis.next();
            System.out.println(xValue);
            r = new Rectangle(x, y, boxWidth, h);
            for (ChartSeries chartSeries : series) {
                bar = chartSeries.getBar(xValue);
                if (bar != null) {
                    Integer value = Math.round(bar.getValue() * k);
                    bar.bounds = new Rectangle(r.x + barOffset, r.y + r.height - value, barWidth, value);
                    
                    bar.draw(g);
                    g.setColor(Color.gray);
                    g.drawRect(r.x + barOffset, r.y, barWidth, r.height);
                }
                barOffset += barWidth;
            }
            x += boxWidth;
        }
    }

    protected void drawAxis(Graphics g, Rectangle drawArray) {
        int x1,x2,y1,y2;
        // горизонтальные оси
        x1 = drawArray.x;
        y1 = drawArray.y+drawArray.height;
        x2 = drawArray.x + drawArray.width;
        y2 = drawArray.y+drawArray.height;
        g.setColor(Color.BLACK);
        g.drawLine(x1, y1, x2, y2);
        // вертикальные оси
        x1 = drawArray.x;
        y1 = drawArray.y + drawArray.height;
        x2 = drawArray.x;
        y2 = drawArray.y;
        g.setColor(Color.BLACK);
        g.drawLine(x1, y1, x2, y2);
    }

    protected void drawArea(Graphics g,Rectangle drawArrea) {
        int x,y,w,h;
        x = drawArrea.x;
        y = drawArrea.y;
        w = drawArrea.width;
        h = drawArrea.height;
        g.setColor(Color.white);
        g.fillRect(x, y, w, h);
        g.setColor(Color.gray);
        g.drawRect(x, y, w, h);
    }

    public void drawTicks(Graphics g, Rectangle drawArray) {
        // drawTics
//        g.setColor(Color.pink);
//        g.fillRect(drawArray.x,drawArray.y, drawArray.width,drawArray.height);
        int n,n1;
        g.setColor(Color.red);
        float k;
        
        // вертикальные тики
        k = (drawArray.width) / (xAxis.maxValue - xAxis.minValue+1);
        xAxis.begin();
        while (xAxis.hasNext()) {
            n = xAxis.next()-xAxis.minValue;
            n1 = Math.round(n * k)+drawArray.x;
            g.drawLine(n1, drawArray.y, n1, drawArray.y + drawArray.height);
        }
        
        // горизонтальные тики
        k = drawArray.height / (yAxis.maxValue - yAxis.minValue+1);
        yAxis.begin();
        while (yAxis.hasNext()) {
            n = yAxis.next()-yAxis.minValue;
            n1 = Math.round(k * n);
            g.drawLine(drawArray.x, drawArray.y+drawArray.height- n1, drawArray.x + drawArray.width, drawArray.y+drawArray.height- n1);
        }
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        Rectangle r,r2;
        r2 = new Rectangle(10,10,getWidth()-20,80);
        g.setColor(Color.red);
        g.drawRect(r2.x,r2.y, r2.width,r2.height);
        g.drawString("Chart1",r2.x, r2.y+r2.height);
        
        r= new Rectangle(10,100,getWidth()-20,getHeight()-110);
        drawArea(g,r);
        r.x+=10;
        r.y+=10;
        r.width-=20;
        r.height-=20;
        drawAxis(g, r);
        drawSeries(g, r);
        drawTicks(g, r);
    }
    
}


public class Main  extends JFrame{
    Chart chart;
    JToolBar toolBar;
    
    public Main(){
        super("Chart");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        initComponents(getContentPane());
        
    }
    
    public void initComponents(Container content){
        String[] commands = {"add","clear","series1","series2","series3"};
        
        chart = new Chart();
//        chart.getXAxis().setRange(0,20);
//        chart.getXAxis().setAutoRange(false);
        
        content.add(chart);
        toolBar = new JToolBar();
        for (String command:commands){
            toolBar.add(new AbstractAction(command) {

                @Override
                public void actionPerformed(ActionEvent e) {
                    String s = e.getActionCommand();
                    doCommand(s);
                }
            });
        }
        content.add(toolBar,BorderLayout.PAGE_START);
    }
    
    public void doCommand(String command){
        System.out.println(command);
        ChartSeries s;
        int r=125,g=125,b=255;
        switch (command){
            case "add":
                    s = new ChartSeries(new Color(r,g,b));
                    chart.addSeries(s);
                    chart.updateUI();
                break;
            case "clear":
                chart.series.clear();
                chart.updateUI();
                break;
            case "series1":
                s =  new ChartSeries(command,Color.red);
                s.setData(ChartSeries.createData(12));
                chart.addSeries(s);
                chart.updateUI();
                break;
        }
                
    }
            
    public static void createAndShow(){
        Main frame = new Main();
        frame.pack();
        frame.setVisible(true);
    
    }
    
    public static void main(String[] args){
        createAndShow();
    }
}
