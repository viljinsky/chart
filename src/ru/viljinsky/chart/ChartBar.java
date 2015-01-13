/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ru.viljinsky.chart;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

/**
 *
 * @author vadik
 */
public class ChartBar {
    ChartSeries series;
    Integer key;
    Object value;
    Rectangle bounds;

    public ChartBar(ChartSeries series, Integer key) {
        this.series = series;
        this.key = key;
    }

    public boolean hitTest(int x, int y) {
        if (bounds!=null)
            return bounds.contains(x, y);
        else 
            return false;
    }

    public void setBounds(Rectangle bounds) {
        this.bounds = bounds;
    }

    public void draw(Graphics g) {
        int x,y,w,h;
        x = bounds.x;
        y = bounds.y;
        h  = bounds.height;
        w  = bounds.width;
        
        g.setColor(series.color);
        g.fillRect(x,y,w,h);
        // 3d
        g.setColor(Color.gray);
        g.drawLine(x, y, x+w, y);
        g.drawLine(x+w, y, x+w, y+h);
        
    }

    public Integer getValue() {
        return series.getYValue(key);
    }

    public String toString() {
        return "bar xValue:" + key + " yValue:" + value + " series:" + series.name;
    }
    
}
