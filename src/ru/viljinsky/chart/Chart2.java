/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ru.viljinsky.chart;

/**
 *
 * @author vadik
 */
import java.util.List;
import java.util.ArrayList;
import java.awt.*;
import javax.swing.*;



public class Chart2 extends JPanel{
    ChartAxis xAxis;
    ChartAxis yAxis;
    public static ChartSeries series1;
    public static ChartSeries series2;
    public static ChartSeries series3;
    List<ChartSeries> seriesList = new ArrayList<>();
    
    public void addSeries(ChartSeries series){
        seriesList.add(series);
    }
    
    public int getSeriesCount(){
        return seriesList.size();
    }
    
    public ChartAxis getXAxis(){
        return xAxis;
    }
    
    public ChartAxis getYAxis(){
        return yAxis;
    }
    
    
    
    public Chart2(){
        setPreferredSize(new Dimension(800,600));
        xAxis = new ChartAxis(ChartAxis.X_AXIS);
        yAxis = new ChartAxis(ChartAxis.Y_AXIS);   
//        xAxis.setRange(-2,10);
//        yAxis.setRange(-2,10);
    }
    
    public void drawSeries(Graphics g,Rectangle rect,ChartSeries series,Integer xOffset){
        xAxis.begin();
        Integer xValue,yValue ;
        ChartBar bar;
        Rectangle r;
        
        float f = rect.height/(yAxis.maxValue-yAxis.minValue);
        
        while (xAxis.hasNext()){
            xValue = xAxis.next();
            bar = series.getBar(xValue);
            if (bar!=null){
                
                yValue = Math.round((bar.getValue()-yAxis.minValue)*f);
                
                int x=getBarCenter(rect, bar.key);
                int x1,y1,w,h;
                x1 = x-10+xOffset;
                w=20;
                
                y1=rect.y+rect.height-yValue;
                h=yValue;
                g.setColor(bar.series.color);
                g.fillRect(x1,y1,w,h);
                // 3d
                g.setColor(Color.gray);
                g.drawLine(x1, y1, x1+w, y1);
                g.drawLine(x1+w, y1, x1+w, y1+h);
            }
        }
    }
    
    public Integer getBarCenter(Rectangle rect,Integer xValue){
        float k = rect.width/(xAxis.maxValue-xAxis.minValue);
        return  rect.x + Math.round((xValue-xAxis.minValue) * k);
    }
    
    public void drawTics(Graphics g,Rectangle rect){
        
        Font f = new Font("courier",Font.BOLD,14);
        
        g.setFont(f);
        
        
        int fh = g.getFontMetrics(f).getHeight();
        
        
        xAxis.begin();
        Integer value;
        int x1,y1,x2,y2;
        g.setColor(Color.lightGray);
        
        float k = rect.width/(xAxis.maxValue-xAxis.minValue);
        while (xAxis.hasNext()){
            value = xAxis.next();
            x1=rect.x + Math.round((value-xAxis.minValue) * k);
            x2=x1;
            y1=rect.y;y2=rect.y+rect.height;
            g.drawLine(x1, y1, x2, y2);
            g.drawString(value.toString(), x1, y2+fh);
        }
        
        
        k = rect.height/(yAxis.maxValue-yAxis.minValue);
        yAxis.begin();
        while (yAxis.hasNext()){
            value = yAxis.next();
            x1=rect.x;
            x2=rect.x+rect.width;
            y1=rect.y+rect.height- Math.round((value-yAxis.minValue)*k);
            y2=y1;
            g.drawLine(x1, y1, x2, y2);
            g.drawString(value.toString(), x1-10, y1);
        }
    }
    
    @Override
    public void paint(Graphics g){
        super.paint(g);
        Rectangle r = new Rectangle(10,10,getWidth()-20,getHeight()-40);
        
        g.setColor(Color.white);
        g.fillRect(r.x, r.y, r.width,r.height);
        g.setColor(Color.gray);
        g.drawRect(r.x,r.y,r.width, r.height);
        drawTics(g, r);
        
        int barWidth = 20;
        int offset = -(getSeriesCount()*barWidth/2);
        for (ChartSeries series:seriesList){
            drawSeries(g, r, series, offset);
            offset+=barWidth;
        }
    }
    
    public static void createAndShow(){
        ChartSeries series1,series2,series3;
        series1 = new ChartSeries("Пример", Color.yellow);
        series1.addValue(1,1);
        series1.addValue(2,2);
        series1.addValue(3,3);
        series1.addValue(4,4);
        series1.addValue(5,-1);
        series1.addValue(6,6);
        series1.addValue(7,7);
        series1.addValue(8,1);
        series1.addValue(9,1);
        series1.rebuild();
        
        series2 = new ChartSeries("Пример2", new Color(125,255,255));
        series2.addValue(1,8);
        series2.addValue(2,7);
        series2.addValue(3,6);
        series2.addValue(7,5);
        series2.addValue(8,4);
        series2.addValue(9,-1);
        series2.rebuild();
        
        series3 = new ChartSeries("Пример3",new Color(225,125,225));
        series3.addValue(0,8);
        series3.addValue(1,7);
        series3.addValue(9,6);
        series3.addValue(10,5);
        series3.addValue(2,4);
        series3.addValue(3,-1);
        series3.rebuild();
        
        
        Chart2 chart = new Chart2();
        chart.getXAxis().setRange(-2, 12);
        chart.getYAxis().setRange(-2, 10);
        chart.addSeries(series1);
        chart.addSeries(series2);
        chart.addSeries(series3);
        JFrame frame = new JFrame("Test");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setContentPane(chart);
        frame.pack();
        frame.setVisible(true);
    }
    
    public static void main(String[] args){
        createAndShow();
    }
    
}
