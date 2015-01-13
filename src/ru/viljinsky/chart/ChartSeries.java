/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ru.viljinsky.chart;

import java.awt.Color;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.List;
import java.util.ArrayList;

/**
 *
 * @author vadik
 */
public class ChartSeries {
    String name = "Noname";
    Color color = Color.pink;
    HashMap<Integer, Object> data = new HashMap<>();
//    ChartBar[] bars;
    List<ChartBar> bars = new ArrayList<>();

    @Override
    public String toString(){
        return "ChartSeries "+name+" "+color;
    }
    public ChartSeries(Color color) {
        this.color = color;
        setData(createData(10));
    }

    public ChartSeries(String name, Color color) {
        this.color = color;
        this.name = name;
    }

    public Integer getMinX() {
        Integer result = Integer.MAX_VALUE;
        for (Integer n : data.keySet()) {
            if (n < result) {
                result = n;
            }
        }
        return result;
    }

    public Integer getYValue(Integer xValue) {
        Object v = data.get(xValue);
        if (v != null) {
            if (v instanceof Integer) {
                return (Integer) v;
            } else if (v instanceof Long) {
                return ((Long) v).intValue();
            } else if (v instanceof Float) {
                return Math.round((Float) v);
            } else if (v instanceof Double) {
                return Math.round(new Float((Double) v));
            }
        }
        return null;
    }
    /**
     * Получение значение с коэффициентом
     * @param xValue
     * @param k
     * @return 
     */
    public Integer getYValueK(Integer xValue,Float k) {
        Object v = data.get(xValue);
        if (v != null) {
            if (v instanceof Integer) {
                return Math.round((Integer) v * k);
            } else if (v instanceof Long) {
//                Integer L = ((Long) v ).intValue();
                return  Math.round(((Long) v ).intValue()*k);
            } else if (v instanceof Float) {
                return Math.round((Float) v * k);
            } else if (v instanceof Double) {
                return Math.round(new Float((Double) v) * k);
            }
        }
        return null;
    }
    

    public Integer getMaxX() {
        Integer result = Integer.MIN_VALUE;
        for (Integer n : data.keySet()) {
            if (n > result) {
                result = n;
            }
        }
        return result;
    }

    public Integer getMaxY() {
        Integer result = Integer.MIN_VALUE;
        Integer value;
        for (Integer n : data.keySet()) {
            value = getYValue(n);
            if (value > result) {
                result = value;
            }
        }
        return result;
    }

    public Integer getMinY() {
        Integer result = Integer.MAX_VALUE;
        Integer value;
        for (Integer n : data.keySet()) {
            value = getYValue(n);
            if (value < result) {
                result = value;
            }
        }
        return result;
    }

    public void setData(HashMap<Integer, Object> data) {
        this.data = new HashMap<>();
        for (Integer key : data.keySet()) {
            this.data.put(key, data.get(key));
        }
        rebuild();
    }

    public void rebuild() {
        bars = new ArrayList<>();
        Set<Integer> keySet = data.keySet();
        ChartBar bar;
        int id;
        for (Iterator it = keySet.iterator(); it.hasNext();) {
            id = (Integer) it.next();
            bar = new ChartBar(this, id);
            bar.value = data.get(id);
            bars.add(bar);
        }
    }

    public void addValue(Integer xValue, Object yValue) {
        data.put(xValue, yValue);
    }

    public static HashMap<Integer, Object> createData(Integer count) {
        HashMap<Integer, Object> result = new HashMap<>();
        //        Long value;
        for (int i = 0; i < count; i++) {
            //            value = Math.round(Math.random()*100);
            result.put(i, Math.random() * 100);
        }
        return result;
    }

    public ChartBar getBar(Integer xValue) {
        for (ChartBar bar : bars) {
            if (bar.key.equals(xValue)) {
                return bar;
            }
        }
        return null;
    }
    
}
