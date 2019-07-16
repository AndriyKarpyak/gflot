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

import java.util.List;

import com.google.gwt.core.client.JsArray;
import com.google.gwt.user.client.Command;
import com.googlecode.gflot.client.options.Range;

/**
 * @author Alexander De Leon
 */
public class PlotWithOverviewModel
    extends PlotModel
{
    private static class PopulateCommand
        implements Command
    {
        private Command toExecuteAfterAllDataPopulated;

        private int nbSeries;

        private int nbSeriesPopulated;

        PopulateCommand( Command toExecuteAfterAllDataPopulated, int nbSeries )
        {
            this.toExecuteAfterAllDataPopulated = toExecuteAfterAllDataPopulated;
            this.nbSeries = nbSeries;
            this.nbSeriesPopulated = 0;
        }

        @Override
        public void execute()
        {
            nbSeriesPopulated++;
            if ( nbSeriesPopulated == nbSeries )
            {
                toExecuteAfterAllDataPopulated.execute();
            }
        }
    }

    private final PlotModel windowModel;
    private final PlotModel overviewModel;
    private final double[] selection = new double[2];

    public PlotWithOverviewModel()
    {
        overviewModel = new PlotModel();
        windowModel = new PlotModel();
    }

    @Override
    protected PlotWithOverviewSeriesHandler createSeriesHandler( Series series, SeriesDataStrategy strategy )
    {
        return new PlotWithOverviewSeriesHandler( series, strategy,
        		windowModel.addSeries( ( Series ) series.copy(), PlotModelStrategy.defaultStrategy() ),
				overviewModel.addSeries( ( Series ) series.copy(), PlotModelStrategy.defaultStrategy() )
				);
    }

    public PlotModel getWindowPlotModel()
    {
        return windowModel;
    }

    public PlotModel getOverviewPlotModel()
    {
        return overviewModel;
    }

    public JsArray<Series> getWindowsSeries()
    {
        return windowModel.getSeries();
    }

    public JsArray<Series> getOverviewSeries()
    {
        return overviewModel.getSeries();
    }

    public void setSelection( double x1, double x2 )
    {
        setSelection( x1, x2, null );
    }

    public void setSelection( double x1, double x2, Command toExcuteAfterSelection )
    {
        selection[0] = x1;
        selection[1] = x2;

        Command command = null;
        if ( null != toExcuteAfterSelection )
        {
            command = new PopulateCommand( toExcuteAfterSelection, getHandlers().size() );
        }

        for ( PlotWithOverviewSeriesHandler handler : getHandlers() )
        {
        	handler.setSelection(selection);
            handler.populateWindowSeries( command );
        }
    }

    public double[] getSelection()
    {
        return selection;
    }

    /**
     * Get range representing min. and max. data points of X data set.
     * 
     * @return {@link Range} - range representing min. and max. data points of X data set.
     */
    public Range getXDataRange()
    {
    	return getXDataRange(0);
    }

    /**
     * Get range representing min. and max. data points of X data set for specified axis index.
     * 
     * @param axis - index of axis starting from 1. If value is less then 1 then min., max. for complete data set will be returned.
     * @return {@link Range} - range representing min. and max. data points of X data set.
     */
    public Range getXDataRange(int axis)
    {
    	Double dataMinX = null;
		Double dataMaxX = null;
		
		for ( PlotWithOverviewSeriesHandler handler : getHandlers() )
        {
			if (handler.getOverviewSeries().getXAxis() == axis || axis < 1) {
				double localMin = handler.getDataMinX();
				if (dataMinX == null || dataMinX >= localMin)
				{
					dataMinX = localMin;
				}
				
				double localMax = handler.getDataMaxX();
				if (dataMaxX == null || dataMaxX <= localMax)
				{
					dataMaxX = localMax;
				}
			}
        }
    	return Range.of(dataMinX, dataMaxX);
    }

    /**
     * Get range representing min. and max. data points of Y data set.
     * 
     * @return {@link Range} - range representing min. and max. data points of Y data set.
     */
    public Range getYDataRange()
    {
    	return getYDataRange(0);
    }

    /**
     * Get range representing min. and max. data points of Y data set for specified axis index.
     * 
     * @param axis - index of axis starting from 1. If value is less then 1 then min., max. for complete data set will be returned.
     * @return {@link Range} - range representing min. and max. data points of Y data set.
     */
    public Range getYDataRange(int axis)
    {
    	Double dataMinY = null;
    	Double dataMaxY = null;
    	
		for ( PlotWithOverviewSeriesHandler handler : getHandlers() )
        {
			if (handler.getOverviewSeries().getYAxis() == axis || axis < 1) {
				double localMin = handler.getDataMinY();
				if (dataMinY == null || dataMinY >= localMin)
				{
					dataMinY = localMin;
				}
				
				double localMax = handler.getDataMaxY();
				if (dataMaxY == null || dataMaxY <= localMax)
				{
					dataMaxY = localMax;
				}
			}
        }
    	return Range.of(dataMinY, dataMaxY);
    }

    @Override
    public void removeSeries( int index )
    {
        windowModel.removeSeries( index );
        overviewModel.removeSeries( index );
        super.removeSeries( index );
    }

    @Override
    public void removeSeries( SeriesHandler series )
    {
        windowModel.removeSeries( series );
        overviewModel.removeSeries( series );
        super.removeSeries( series );
    }

    @Override
    public void removeAllSeries()
    {
        windowModel.removeAllSeries();
        overviewModel.removeAllSeries();
        super.removeAllSeries();
    }

	public void push() {
        for ( PlotWithOverviewSeriesHandler handler : getHandlers() )
        {
            handler.populateOverviewSeries();
        }
	}

    @SuppressWarnings( "unchecked" )
    @Override
    public List<PlotWithOverviewSeriesHandler> getHandlers()
    {
        return (List<PlotWithOverviewSeriesHandler>) super.getHandlers();
    }
}
