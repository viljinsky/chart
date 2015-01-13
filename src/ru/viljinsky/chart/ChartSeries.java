/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ru.viljinsky.chart;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.List;
import java.util.ArrayList;
import javax.swing.AbstractAction;

/**
 *
 * @author vadik
 */
enum SeriesType{
    BAR_CART,
    LINE_CHART,
    AREA_CHART
}

class ChartSeries{
    protected String name = "Noname";
    protected Color color = Color.pink;
    protected HashMap<Integer, Object> data = new HashMap<>();
    protected List<ChartElement> elements = new ArrayList<>();
    
    public String getCaption(){
        return name;
    }
    
    public Color getColor(){
        return color;
    }
    /**
     * Количество элементов в серии
     * @return Количество элементов в серии
     */
    public Integer getElementCount(){
        return data.size();
    }
    
    /**
     * Список всех элементов серии
     * @return Список всех элементов серии
     */
    public List<ChartElement> getElements(){
        return elements;
    }
    
    /**
     * Поиск элемента серии по значению X
     * @param xValue - значение элемента по оси X
     * @return  если элемент существует - найденный ChartElement,
     *  в противном случае null
     */
    public ChartElement getElementByValue(Integer xValue) {
        for (ChartElement element : elements) {
            if (element.key.equals(xValue)) {
                return element;
            }
        }
        return null;
    }

    /**
     * Получение элемента по порядковому номеру
     * @param index номер элемента по порядку в списке
     * @return ChartElement
     */
    public ChartElement getElement(Integer index){
        return elements.get(index);
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
    

    
    public void addValue(Integer xValue, Object yValue) {
        data.put(xValue, yValue);
    }

    public void rebuild() {
        elements = new ArrayList<>();
        Set<Integer> keySet = data.keySet();
        ChartBar bar;
        int id;
        for (Iterator it = keySet.iterator(); it.hasNext();) {
            id = (Integer) it.next();
            bar = new ChartBar(this, id);
            bar.value = data.get(id);
            elements.add(bar);
        }
    }

    
    
    
    public static ChartSeries createSeries(SeriesType seriesType,String caption,Color color){
        return new ChartBarSeries(caption, color);
    }
}


class ChartBarSeries extends ChartSeries{
    
    
    @Override
    public String toString(){
        return "ChartSeries "+name+" "+color;
    }

    public ChartBarSeries(String name, Color color) {
        this.color = color;
        this.name = name;
    }


    public void setData(HashMap<Integer, Object> data) {
        this.data = new HashMap<>();
        for (Integer key : data.keySet()) {
            this.data.put(key, data.get(key));
        }
        rebuild();
    }

    /**
     * Создание экземпляра случайных данных с указанным количеством элементов
     * @param count колличество элементов
     * @return сгенерированный набор данных
     */
    public static HashMap<Integer, Object> createData(Integer count) {
        HashMap<Integer, Object> result = new HashMap<>();
        for (int i = 0; i < count; i++) {
            result.put(i, Math.random() * 100);
        }
        return result;
    }
    
}
