/*
 * Copyright (c) 2012 Nicolas Morel
 * 
 * Permission is hereby granted, free of charge, to any person
 * obtaining a copy of this software and associated documentation
 * files (the "Software"), to deal in the Software without
 * restriction, including without limitation the rights to use,
 * copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the
 * Software is furnished to do so, subject to the following
 * conditions:
 * 
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES
 * OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
 * HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
 * WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
 * FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
 * OTHER DEALINGS IN THE SOFTWARE.
 */
package com.googlecode.gflot.client;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * @author Alexander De Leon
 */
public class LTTBDownsamplingSeriesDataStrategy
    extends DefaultSeriesDataStrategy
{

    private final int threshold;

    public LTTBDownsamplingSeriesDataStrategy( int threshold )
    {
        this( threshold, SeriesData.create() );
    }

    public LTTBDownsamplingSeriesDataStrategy( int threshold, SeriesData data )
    {
        super( data );
        this.threshold = threshold;
    }
    
    public int getThreshold() {
        return threshold;
    }
    
    @Override
    public void setData(SeriesData newData) {
        super.setData(downsample(newData));
    }

    private SeriesData downsample(SeriesData data)
    {
        if (threshold < 3 || data.length() <= threshold) {
            return data;
        }
        
        SeriesData downsampled = SeriesData.create();
        
        double averageTimeOfNextBucket = 0;
        double averageValueOfNextBucket = 0;
        
        //value, stamp, area
        List<PointWithWeight<DataPoint, Double>> tempList = new ArrayList<PointWithWeight<DataPoint, Double>>();
        
        //get the timestamp and value of the first entry in the time period
        DataPoint point = data.get(0);
        double timeOfPreviousBucket = point.getX();
        double valueOfPreviousBucket = point.getY();
        
        //insert the first entry into the result table
        downsampled.push(DataPoint.of(timeOfPreviousBucket, valueOfPreviousBucket));
        
        //get the timestamp of the second entry in the time period
        double startTicks = data.get(1).getX();
        
        //get the timestamp of the second to last entry in the time period
        double endTicks = data.get(data.length() - 2).getX();
        
        //calculate the step size based on the provided threshold
        double stepSize = (endTicks - startTicks)/(threshold - 2);
        
        //calculate the start and end ticks of the third bucket
        double startTicksNextBucket = startTicks + stepSize;
        double endTicksNextBucket = startTicksNextBucket + stepSize;
        
        int loopCounter = 0;
        List<DataPoint> averageList = new ArrayList<DataPoint>();
        boolean last = false;
        
        //loop through every bucket except the first and the last
        while (loopCounter < threshold - 2)
        {
            //get the average timestamp and value of the next bucket
            //if its the second to last bucket then choose the 
            //values for the last entry
            if (loopCounter == threshold - 3)
            {
                last = true;
                averageTimeOfNextBucket = data.getX(data.length() - 3);
                averageValueOfNextBucket = data.getY(data.length() - 3);
                
            } else //otherwise get the average of the next bucket
            {
                averageList.clear();
                for (int i = 1; i < data.length() - 2; i++) 
                {
                    DataPoint p = data.get(i);
                    if (p.getX() > startTicksNextBucket && p.getX() <= endTicksNextBucket) 
                    {
                        averageList.add(p);
                    }
                }
                if (averageList.size() != 0)
                {
                    // get average time
                    averageTimeOfNextBucket = 0;
                    for (DataPoint p : averageList) {
                        averageTimeOfNextBucket += p.getX() / averageList.size();
                    }
                    //get average value
                    averageValueOfNextBucket = 0;
                    for (DataPoint p : averageList) {
                        averageValueOfNextBucket += p.getY();
                    }
                    averageValueOfNextBucket /= averageList.size();
                }
            }
            
            //insert into temp list with a column for triangle area - THIS IS THE PRIMARY CALCULATION
            tempList = new ArrayList<PointWithWeight<DataPoint, Double>>();
            
            if (averageList.size() > 0 || last)
            {
                for (int i = 0; i < data.length(); i++)
                {
                    DataPoint p = data.get(i);
                    if (p.getX() > startTicks && p.getX() < startTicks + stepSize) {
                        tempList.add(new PointWithWeight<DataPoint, Double>(DataPoint.of(p.getX(), p.getY()),
                                Math.abs(((valueOfPreviousBucket - averageValueOfNextBucket)*(p.getX() - timeOfPreviousBucket) - (valueOfPreviousBucket - p.getY())*(averageTimeOfNextBucket - timeOfPreviousBucket))*0.5)));
                    }
                }
            }
            
            if (tempList.size() > 0)
            {
                //get the value with the max area and insert it into result table and update previous
                Collections.sort(tempList, new Comparator<PointWithWeight<DataPoint, Double>>() {

                    @Override
                    public int compare(PointWithWeight<DataPoint, Double> o1, PointWithWeight<DataPoint, Double> o2) {
                        return (int) (o1.area - o2.area) * -1;
                    }
                });
                DataPoint p = tempList.get(0).point;
                timeOfPreviousBucket = p.getX();
                valueOfPreviousBucket = p.getY();
            }
            
            downsampled.push(DataPoint.of(timeOfPreviousBucket, valueOfPreviousBucket));
            
            //increment the ticks counter for the next bucket
            startTicksNextBucket = startTicksNextBucket + stepSize;
            endTicksNextBucket = endTicksNextBucket + stepSize;

            //increment the start of the next bucket
            startTicks = startTicks + stepSize;

            //increment counter
            loopCounter = loopCounter + 1;
        }
        
        //insert final value
        downsampled.push(DataPoint.of(averageTimeOfNextBucket, averageValueOfNextBucket));
        
        return downsampled;
    }
    
    class PointWithWeight<T, E> {
        final T point;
        final E area;
        
        public PointWithWeight(T point, E area) {
            super();
            this.point = point;
            this.area = area;
        }
    }
}
