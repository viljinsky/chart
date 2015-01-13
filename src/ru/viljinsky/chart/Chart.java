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
import com.sun.jmx.snmp.BerDecoder;
import java.util.List;
import java.util.ArrayList;
import java.awt.*;
import java.awt.event.*;
import java.util.Iterator;
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
     * Добавление серии в диаграмму
     * @param series
     */
    public void addSeries(ChartSeries series){
        seriesList.add(series);
    }
    
    /**
     * Колличество серий в диаграмме
     * @return Колличество серий в диаграмме
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
     * Проверка клика мыши по бар-диаграмма
     * @param x позиция мыши
     * @param y позиция мыши
     * @return бар если таковоё есть, в противном случаее null
     */
    protected ChartBar hitTest(int x,int y){
        ChartSeries series;
        for (int i=seriesList.size()-1;i>=0;i--){
            series = seriesList.get(i);
            for (ChartBar bar:series.bars)
                if (bar.hitTest(x, y))
                    return bar;
        }
        return null;
    }
    
    /**
     * событие клика по диаграмме если клик пришёлся на бар
     * @param bar по которому сделан клик.
     */
    protected void onBarClick(ChartBar bar){
        System.out.println(bar.toString());
    }
    
    
    /**
     * Прорисовка бар-серии 
     * @param g контекст графики
     * @param rect рабочая область диаграммы
     * @param series  прорисовываемая бар-серия
     * @param xOffset смещение серии от центра значения по х
     */
    public void drawSeries(Graphics g,Rectangle rect,ChartSeries series,Integer xOffset){
        Integer xValue,yValue,yValue0 ;
        ChartBar bar;
        
        float f = rect.height/(yAxis.maxValue-yAxis.minValue);
        
        yValue0 = Math.round(-yAxis.minValue*f);
        xAxis.begin();
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
                
                if (yValue>=0)
                    h=(yValue0>0?yValue-yValue0:yValue);
                else
                    h=(yValue0>0?yValue0:yValue);
                
                bar.setBounds(new Rectangle(x1,y1,w,h));
                bar.draw(g);
                
                if (yValue0>0){
                    y1 = rect.y+rect.height-yValue0;
                    g.drawLine(x1, y1, x1+w, y1);
                }
            }
        }
    }
    
    /**
     * Находит позицию значения по оси Х
     * @param rect рабочая область диаграммы
     * @param xValue
     * @return занчение Х на диаграмме для значения Х оси 
     */
    public Integer getBarCenter(Rectangle rect,Integer xValue){
        float k = rect.width/(xAxis.maxValue-xAxis.minValue);
        return  rect.x + Math.round((xValue-xAxis.minValue) * k);
    }
    
    /**
     * Прорисовка осей координат и сетки
     * @param g контекст графики
     * @param rect рабочая область
     */
    public void drawAxis(Graphics g,Rectangle rect){
        
        Font f = new Font("courier",Font.BOLD,14);
        
        g.setFont(f);
        
        int fh = g.getFontMetrics(f).getHeight();
        
        xAxis.begin();
        Integer value;
        int x1,y1,x2,y2;
        g.setColor(Color.lightGray);
        
        float kX = rect.width/(xAxis.maxValue-xAxis.minValue);
        while (xAxis.hasNext()){
            value = xAxis.next();
            x1=rect.x + Math.round((value-xAxis.minValue) * kX);
            x2=x1;
            y1=rect.y;y2=rect.y+rect.height;
            g.drawLine(x1, y1, x2, y2);
            g.drawString(value.toString(), x1, y2+fh);
        }
        
        
        float kY = rect.height/(yAxis.maxValue-yAxis.minValue);
        yAxis.begin();
        while (yAxis.hasNext()){
            value = yAxis.next();
            x1=rect.x;
            x2=rect.x+rect.width;
            y1=rect.y+rect.height- Math.round((value-yAxis.minValue)*kY);
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
        drawAxis(g, r);
        
        int barWidth = 20;
        int offset = -(getSeriesCount()*barWidth/2);
        for (ChartSeries series:seriesList){
            drawSeries(g, r, series, offset);
            offset+=barWidth;
        }
    }
    
    public void autoRange(){
        Integer minValue = Integer.MAX_VALUE;
        Integer maxValue = Integer.MIN_VALUE;
        for (ChartSeries series:seriesList){
            if (series.getMinX()<minValue) minValue=series.getMinX();
            if (series.getMaxX()>maxValue) maxValue=series.getMaxX();
        }
        xAxis.setRange(minValue, maxValue);
        
        minValue = Integer.MAX_VALUE;
        maxValue = Integer.MIN_VALUE;
        for (ChartSeries series:seriesList){
            if (series.getMinY()<minValue) minValue=series.getMinY();
            if (series.getMaxY()>maxValue) maxValue=series.getMaxY();
        }
        yAxis.setRange(minValue, maxValue);
    }
    
}
