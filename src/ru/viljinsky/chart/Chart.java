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
import java.awt.event.*;
import javax.swing.*;

/**
 *
 * @author vadik
 */
public class Chart extends JPanel{
    ChartAxis xAxis;
    ChartAxis yAxis;
    List<ChartSeries> seriesList = new ArrayList<>();

    /**
     *
     */
    public void clear(){
        seriesList.clear();
    }
    
    /**
     *
     * @param series
     */
    public void addSeries(ChartSeries series){
        seriesList.add(series);
    }
    
    /**
     *
     * @return
     */
    public int getSeriesCount(){
        return seriesList.size();
    }
    
    /**
     *
     * @return
     */
    public ChartAxis getXAxis(){
        return xAxis;
    }
    
    /**
     *
     * @return
     */
    public ChartAxis getYAxis(){
        return yAxis;
    }

    /**
     *
     */
    public Chart(){
        setPreferredSize(new Dimension(800,600));
        xAxis = new ChartAxis(ChartAxis.X_AXIS);
        yAxis = new ChartAxis(ChartAxis.Y_AXIS);   
//        initComponents();
        addMouseListener(new MouseAdapter() {

            @Override
            public void mousePressed(MouseEvent e) {
                int x,y;
                x=e.getX();y=e.getY();
                ChartBar bar = hitTest(x,y);
                if (bar!=null){
                    onBarClick(bar);
                }
            }
        });
    }
    
    /**
     *
     * @param x
     * @param y
     * @return
     */
    protected ChartBar hitTest(int x,int y){
        for (ChartSeries series:seriesList){
            for (ChartBar bar:series.bars){
                if (bar.bounds.contains(x, y)){
                    return bar;
                }
            }
        }
        return null;
    }
    
    /**
     *
     * @param bar
     */
    protected void onBarClick(ChartBar bar){
        System.out.println(bar.toString());
    }
    
    
    /**
     *
     * @param g
     * @param rect
     * @param series
     * @param xOffset
     */
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
                x1 = x+xOffset;
                w=20;
                
                y1=rect.y+rect.height-yValue;
                h=yValue;
                bar.setBounds(new Rectangle(x1,y1,w,h));
                g.setColor(bar.series.color);
                g.fillRect(x1,y1,w,h);
                // 3d
                g.setColor(Color.gray);
                g.drawLine(x1, y1, x1+w, y1);
                g.drawLine(x1+w, y1, x1+w, y1+h);
            }
        }
    }
    
    /**
     *
     * @param rect
     * @param xValue
     * @return
     */
    public Integer getBarCenter(Rectangle rect,Integer xValue){
        float k = rect.width/(xAxis.maxValue-xAxis.minValue);
        return  rect.x + Math.round((xValue-xAxis.minValue) * k);
    }
    
    /**
     *
     * @param g
     * @param rect
     */
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
    
    /**
     *
     */
//    public static void createAndShow(){
//        
//        Chart chart = new Chart();
//        chart.getXAxis().setRange(-2, 12);
//        chart.getYAxis().setRange(-2, 10);
//        chart.addSeries(series1);
//        chart.addSeries(series2);
//        chart.addSeries(series3);
//        JFrame frame = new JFrame("Test");
//        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        frame.setContentPane(chart);
//        frame.pack();
//        frame.setVisible(true);
//    }
//    
//    /**
//     *
//     * @param args
//     */
//    public static void main(String[] args){
//        createAndShow();
//    }
    
}
