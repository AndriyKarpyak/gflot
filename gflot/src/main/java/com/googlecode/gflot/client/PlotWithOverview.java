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

import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Widget;
import com.googlecode.gflot.client.event.PlotClickListener;
import com.googlecode.gflot.client.event.PlotHoverListener;
import com.googlecode.gflot.client.event.PlotLoadEvent.Handler;
import com.googlecode.gflot.client.event.PlotSelectedListener;
import com.googlecode.gflot.client.event.PlotSelectingListener;
import com.googlecode.gflot.client.event.PlotUnselectedListener;
import com.googlecode.gflot.client.event.PlotZoomListener;
import com.googlecode.gflot.client.jsni.Plot;
import com.googlecode.gflot.client.options.GlobalSeriesOptions;
import com.googlecode.gflot.client.options.LegendOptions;
import com.googlecode.gflot.client.options.LineSeriesOptions;
import com.googlecode.gflot.client.options.PlotOptions;
import com.googlecode.gflot.client.options.Range;
import com.googlecode.gflot.client.options.SelectionOptions;
import com.googlecode.gflot.client.options.SelectionOptions.SelectionMode;

/**
 * @author Alexander De Leon
 */
public class PlotWithOverview
    extends Composite
    implements PlotWidget
{
    public static final int DEFAULT_OVERVIEW_HEIGHT = 100; // px

    public static final PlotOptions DEFAULT_OVERVIEW_OPTIONS = PlotOptions
        .create()
        .setLegendOptions( LegendOptions.create().setShow( false ) )
        .setGlobalSeriesOptions( GlobalSeriesOptions.create().setLineSeriesOptions( LineSeriesOptions.create().setLineWidth( 1 )
            .setFill( true ) ).setShadowSize( 0d ) ).setSelectionOptions( SelectionOptions.create().setMode( SelectionMode.X ) );

    private final SimplePlot windowPlot;

    private final SimplePlot overviewPlot;

    private final PlotWithOverviewModel model;

    public PlotWithOverview( PlotWithOverviewModel model )
    {
        this( model, PlotOptions.create() );
    }

    public PlotWithOverview( PlotWithOverviewModel model, PlotOptions plotOptions )
    {
        this( model, plotOptions, DEFAULT_OVERVIEW_OPTIONS );
    }

    public PlotWithOverview( PlotWithOverviewModel model, PlotOptions windowPlotOptions, PlotOptions overviewPlotOptions )
    {
        this.model = model;
        this.windowPlot = new SimplePlot( model.getWindowPlotModel(), windowPlotOptions );
        this.overviewPlot = new SimplePlot( model.getOverviewPlotModel(), overviewPlotOptions );
        setupPlots();
        initWidget( createUi() );
    }

    public PlotWithOverview( PlotWithOverviewModel model, PlotOptions windowPlotOptions, Element windowPlotContainer,
                             Element overviewPlotContainer )
    {
        this.model = model;
        this.windowPlot = new SimplePlot( windowPlotContainer, model.getWindowPlotModel(), windowPlotOptions );
        this.overviewPlot = new SimplePlot( overviewPlotContainer, model.getOverviewPlotModel(), null );
        setupPlots();
        initWidget( createUi() );
    }

    public PlotWithOverview( PlotWithOverviewModel model, PlotOptions windowPlotOptions,
                             PlotOptions overviewPlotOptions, Element windowPlotContainer, Element overviewPlotContainer )
    {
        this.model = model;
        this.windowPlot = new SimplePlot( windowPlotContainer, model.getWindowPlotModel(), windowPlotOptions );
        this.overviewPlot = new SimplePlot( overviewPlotContainer, model.getOverviewPlotModel(), overviewPlotOptions );
        setupPlots();
        initWidget( createUi() );
    }

    /* ---------------------- PlotWidget API -- */
    public void addClickListener( PlotClickListener listener, boolean onlyOnDatapoint )
    {
        windowPlot.addClickListener( listener, onlyOnDatapoint );
    }

    public void addHoverListener( PlotHoverListener listener, boolean onlyOnDatapoint )
    {
        windowPlot.addHoverListener( listener, onlyOnDatapoint );
    }

    public void addSelectedListener( PlotSelectedListener listener )
    {
        overviewPlot.addSelectedListener( listener );
    }

    @Override
    public void addSelectingListener( final PlotSelectingListener listener )
    {
        overviewPlot.addSelectingListener( listener );
    }

    @Override
    public void addUnselectedListener( final PlotUnselectedListener listener )
    {
        overviewPlot.addUnselectedListener( listener );
    }

    @Override
    public PlotSelectionArea getSelection()
    {
        return overviewPlot.getSelection();
    }

    @Override
    public void setSelection( PlotSelectionArea area )
    {
        overviewPlot.setSelection( area );
    }

    @Override
    public void setSelection( PlotSelectionArea area, boolean preventEvent )
    {
        overviewPlot.setSelection( area, preventEvent );
    }

    @Override
    public void clearSelection()
    {
        overviewPlot.clearSelection();
    }

    @Override
    public void clearSelection( boolean preventEvent )
    {
        overviewPlot.clearSelection( preventEvent );
    }

    public int getHeight()
    {
        return windowPlot.getHeight() + overviewPlot.getHeight();
    }

    public int getWindowHeight()
    {
        return windowPlot.getHeight();
    }

    public int getOverviewHeight()
    {
        return overviewPlot.getHeight();
    }

    public PlotWithOverviewModel getModel()
    {
        return model;
    }

    public PlotOptions getWindowOptions()
    {
        return windowPlot.getOptions();
    }

    /**
     * @deprecated since 3.2.0 this method always returns the options created by user and not the internal plot options used by flot. Use
     * {@link PlotWithOverview#getWindowOptions()} to get user's option and {@link PlotWithOverview#getPlot()}.getOptions() to get
     * internal flot options.
     *
     * @return the {@link PlotOptions}
     */
    @Deprecated
    public PlotOptions getWindowPlotOptions()
    {
        return windowPlot.getOptions();
    }

    public PlotOptions getOverviewOptions()
    {
        return overviewPlot.getOptions();
    }

    /**
     * @deprecated since 3.2.0 this method always returns the options created by user and not the internal plot options used by flot. Use
     * {@link PlotWithOverview#getOverviewOptions()} to get user's option and {@link PlotWithOverview#getOverviewPlot()}.getPlot()
     * .getOptions() to get internal flot options.
     *
     * @return the {@link PlotOptions}
     */
    @Deprecated
    public PlotOptions getOverviewPlotOptions()
    {
        return overviewPlot.getOptions();
    }

    public int getWidth()
    {
        return windowPlot.getWidth();
    }

	public void push() {
        model.push();
		windowPlot.redraw();
        overviewPlot.redraw();
	}
	
	public void refresh()
	{
		overviewPlot.redraw();
		
    	Double selectionFrom = windowPlot.getAxes().getX().getMinimumValue();
		Double selectionTo = windowPlot.getAxes().getX().getMaximumValue();
		
		Double dataFrom = model.getXDataRange().getFrom();
		Double dataTo = model.getXDataRange().getTo();
		
		Range xRange = Range.of(selectionFrom >= dataFrom ? selectionFrom : dataFrom , selectionTo <= dataTo ? selectionTo : dataTo);
        overviewPlot.setSelection( PlotSelectionArea.create().setX( xRange ) );
	}

    public void redraw()
    {
        double[] selection = model.getSelection();
        // we have to redraw first and then select
        windowPlot.redraw();
        overviewPlot.redraw();

        if ( selection[0] < selection[1] )
        {
            overviewPlot
                .setSelection( PlotSelectionArea.create().setX( Range.of( selection[0], selection[1] ) ), false );
        }
    }

    public void setHeight( int height )
    {
        windowPlot.setHeight( height - getOverviewHeight() );
    }

    public void setWindowHeight( int height )
    {
        windowPlot.setHeight( height );
    }

    public void setOverviewHeight( int height )
    {
        overviewPlot.setHeight( height );
    }

    public void setLinearSelection( double x1, double x2 )
    {
        overviewPlot.setSelection( PlotSelectionArea.create().setX( Range.of( x1, x2 ) ), false );
    }

    public void setRectangularSelection( double x1, double y1, double x2, double y2 )
    {
        overviewPlot.setSelection( PlotSelectionArea.create().setX( Range.of( x1, x2 ) ).setY( Range.of( y1, y2 ) ),
            false );
    }

    public void setWidth( int width )
    {
        overviewPlot.setWidth( width );
        windowPlot.setWidth( width );
    }

    public SimplePlot getWindowPlot()
    {
        return windowPlot;
    }

    public SimplePlot getOverviewPlot()
    {
        return overviewPlot;
    }

    /* ------------------------- SelectionListener API -- */
	public void onWindowPlotSelected(PlotSelectionArea area) {
		
		double min = -1;
		double max = -1;

		for (PlotWithOverviewSeriesHandler handler : model.getHandlers()) {

			SeriesData data = handler.getData(area.getX().getFrom(), area.getX().getTo(), true);
			
			if (data.length() > 1) {
				
				if (min == -1 || data.get(0).getX() <= min) {
					min = data.get(0).getX();
				}
				
				if (max == -1 || max <= data.get(data.length() - 1).getX()) {
					max = data.get(data.length() - 1).getX();
				}
			} else {
				windowPlot.clearSelection();
				return;
			}
		}
		
		final PlotSelectionArea frozzenSelection = overviewPlot.getSelection();
		final Range xRange = Range.of(min, max);

		model.setSelection(xRange.getFrom(), xRange.getTo(), new Command() {
			public void execute() {
				windowPlot.redraw();
				overviewPlot.setSelection(PlotSelectionArea.create().setX(xRange), true);
				
				if (overviewPlot.getSelection() == null) {
					windowPlot.clearSelection();
					overviewPlot.setSelection(frozzenSelection, true);
				}
			}
		});
	}
    
    public void onOverviewPlotSelected( PlotSelectionArea area )
    {
        Range xRange = area.getX();
        model.setSelection( xRange.getFrom(), xRange.getTo(), new Command()
        {
            public void execute()
            {
                windowPlot.redraw();
            }
        } );
    }

    /* ------------------------- ZoomListener API -- */
    public void onWindowPlotZoom(Plot plot) 
    {
//    	Double selectionFrom = new Double(windowPlot.getAxes().getX().getMinimumValue().intValue());
//		Double selectionTo = windowPlot.getAxes().getX().getMaximumValue();
//		
//		Double dataFrom = model.getXDataRange().getFrom();
//		Double dataTo = model.getXDataRange().getTo();
//		
//		onWindowPlotSelected(PlotSelectionArea.create().setX(Range.of(selectionFrom >= dataFrom ? selectionFrom : dataFrom , selectionTo <= dataTo ? selectionTo : dataTo)));
		
//		final Range xRange = Range.of(selectionFrom >= dataFrom ? selectionFrom : dataFrom , selectionTo <= dataTo ? selectionTo : dataTo);
//                
//        PlotSelectionArea frozzenSelection = overviewPlot.getSelection();
//        
//        overviewPlot.setSelection(PlotSelectionArea.create().setX(xRange));
//        if (overviewPlot.getSelection() == null) {
//        	overviewPlot.setSelection(frozzenSelection);
//        }
	}
    
    public void onOverviewPlotZoom(Plot plot) 
    {
		// not supporting zooming on overview model
	}
    
    /* -------------------------- Helper Methods -- */
    private Widget createUi()
    {
        FlowPanel panel = new FlowPanel();
        panel.add( windowPlot );
        panel.add( overviewPlot );

        panel.setWidth( SimplePlot.DEFAULT_WIDTH + "px" );
        windowPlot.setWidth( "auto" );
        overviewPlot.setWidth( "auto" );
        overviewPlot.setHeight( DEFAULT_OVERVIEW_HEIGHT );
        return panel;
    }

    protected void setupPlots()
    {
    	windowPlot.addSelectedListener( new PlotSelectedListener() {
			
			@Override
			public void onPlotSelected(PlotSelectionArea area) {
				onWindowPlotSelected( area );
			}
		} );
        overviewPlot.addSelectedListener( new PlotSelectedListener() {
			
			@Override
			public void onPlotSelected(PlotSelectionArea area) {
				onOverviewPlotSelected( area );
			}
		} );
        windowPlot.addZoomListener(new PlotZoomListener() {
			
			@Override
			public void onPlotZoom(Plot plot) {
				onWindowPlotZoom(plot);
			}
		});
        overviewPlot.addZoomListener(new PlotZoomListener() {
			
			@Override
			public void onPlotZoom(Plot plot) {
				onOverviewPlotZoom(plot);
			}
		});
    }

    @Override
    public HandlerRegistration addLoadHandler( Handler handler )
    {
        return overviewPlot.addLoadHandler( handler );
    }

    @Override
    public Plot getPlot()
    {
        return getWindowPlot().getPlot();
    }
}
