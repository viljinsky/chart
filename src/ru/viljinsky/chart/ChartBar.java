/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ru.viljinsky.chart;

import java.awt.Graphics;
import java.awt.Rectangle;

/**
 *
 * @author vadik
 */
class ChartBar {
    ChartSeries series;
    Integer key;
    Object value;
    Rectangle bounds;

    public ChartBar(ChartSeries series, Integer key) {
        this.series = series;
        this.key = key;
    }

    public boolean hitTest(int x, int y) {
        return false;
    }

    public void setBounds(Rectangle bounds) {
        this.bounds = bounds;
    }

    public void draw(Graphics g) {
        g.setColor(series.color);
        g.fillRect(bounds.x, bounds.y, bounds.width, bounds.height);
    }

    public Integer getValue() {
        return series.getYValue(key);
    }

    public String toString() {
        return "bar xValue:" + key + " yValue:" + value + " series:" + series.name;
    }
    
}
