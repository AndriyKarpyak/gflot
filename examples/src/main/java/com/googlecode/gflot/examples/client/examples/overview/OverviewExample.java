package com.googlecode.gflot.examples.client.examples.overview;


import com.google.gwt.canvas.dom.client.CssColor;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Random;
import com.google.gwt.user.client.ui.Widget;
import com.googlecode.gflot.client.Axis;
import com.googlecode.gflot.client.Pan;
import com.googlecode.gflot.client.PlotModelStrategy;
import com.googlecode.gflot.client.PlotWithOverview;
import com.googlecode.gflot.client.PlotWithOverviewModel;
import com.googlecode.gflot.client.PlotWithOverviewSeriesHandler;
import com.googlecode.gflot.client.Series;
import com.googlecode.gflot.client.Zoom;
import com.googlecode.gflot.client.options.AxesOptions;
import com.googlecode.gflot.client.options.AxisOptions;
import com.googlecode.gflot.client.options.CrosshairOptions;
import com.googlecode.gflot.client.options.GlobalSeriesOptions;
import com.googlecode.gflot.client.options.GridOptions;
import com.googlecode.gflot.client.options.LegendOptions;
import com.googlecode.gflot.client.options.LineSeriesOptions;
import com.googlecode.gflot.client.options.Markings;
import com.googlecode.gflot.client.options.PlotOptions;
import com.googlecode.gflot.client.options.Range;
import com.googlecode.gflot.client.options.SelectionOptions;
import com.googlecode.gflot.client.options.SelectionOptions.SelectionMode;
import com.googlecode.gflot.client.options.ZoomOptions;
import com.googlecode.gflot.examples.client.examples.DefaultActivity;
import com.googlecode.gflot.examples.client.resources.Resources;
import com.googlecode.gflot.examples.client.source.SourceAnnotations.GFlotExamplesData;
import com.googlecode.gflot.examples.client.source.SourceAnnotations.GFlotExamplesRaw;
import com.googlecode.gflot.examples.client.source.SourceAnnotations.GFlotExamplesSource;

/**
 * @author Nicolas Morel
 */
@GFlotExamplesRaw( OverviewPlace.UI_RAW_SOURCE_FILENAME )
public class OverviewExample
    extends DefaultActivity
{

    private static Binder binder = GWT.create( Binder.class );

    interface Binder
        extends UiBinder<Widget, OverviewExample>
    {
    }

    /**
     * Plot
     */
    @GFlotExamplesData
    @UiField( provided = true )
    PlotWithOverview plot;

    public OverviewExample( Resources resources )
    {
        super( resources );
    }

    /**
     * Create plot
     */
    @GFlotExamplesSource
    public Widget createPlot()
    {
        PlotWithOverviewModel model = new PlotWithOverviewModel();
        
        final PlotOptions windowOptions = PlotOptions.create();
		windowOptions.setCanvasEnabled(true);
		windowOptions.setGlobalSeriesOptions(GlobalSeriesOptions.create());
		windowOptions.setLegendOptions(LegendOptions.create().setShow(false));
		windowOptions.setCrosshairOptions(CrosshairOptions.create().setMode(CrosshairOptions.Mode.X).setLineWidth(1).setColor("gray"));
		windowOptions.setGridOptions(GridOptions.create().setHoverable(true).setClickable(true).setMouseActiveRadius(5).setBorderColor("#bababa").setBorderWidth(1)
				.setColor("#bfbfbf").setBackgroundColor("white").setMarkings(Markings.create()));
		windowOptions.setSelectionOptions(SelectionOptions.create().setMode(SelectionMode.X).setShape(SelectionOptions.SelectionShape.BEVEL).setMinSize(10));
		windowOptions.setZoomOptions(ZoomOptions.create().setInteractive(true));
//		windowOptions.addXAxisOptions( TimeSeriesAxisOptions.create() );

		final PlotOptions overviewOptions = PlotOptions.create();
		overviewOptions.setCanvasEnabled(false);
		overviewOptions.setGlobalSeriesOptions(GlobalSeriesOptions.create().setShadowSize(0).setLineSeriesOptions(LineSeriesOptions.create().setLineWidth(1).setFill(false)));
		overviewOptions.setLegendOptions(LegendOptions.create().setShow(false));
		overviewOptions.setCrosshairOptions(CrosshairOptions.create().setMode(CrosshairOptions.Mode.X).setLineWidth(1).setColor("gray"));
		overviewOptions.setGridOptions(GridOptions.create().setHoverable(false).setClickable(false).setBorderColor("#bababa").setBorderWidth(1).setColor("#bfbfbf")
				.setBackgroundColor("white").setMarkings(Markings.create()));
		overviewOptions.setSelectionOptions(SelectionOptions.create().setMode(SelectionMode.X).setShape(SelectionOptions.SelectionShape.BEVEL).setMinSize(10));
		overviewOptions.setZoomOptions(ZoomOptions.create().setInteractive(false));
		overviewOptions.addXAxisOptions(AxisOptions.create().setZoomRange(false).setPanRange(false));
//		overviewOptions.addXAxisOptions( TimeSeriesAxisOptions.create() );

        // create the plot
        plot = new PlotWithOverview( model, windowOptions, overviewOptions );

//        Range xDataRange = plot.getModel().getXDataRange();
//		plot.setLinearSelection( xDataRange.getFrom(), xDataRange.getTo() );
		
        return binder.createAndBindUi( this );
    }

    /**
     * On click on generate button
     * 
     * @param e event
     */
    @GFlotExamplesSource
    @UiHandler( "generate" )
    void onClickGenerate( ClickEvent e )
    {
        plot.getModel().removeAllSeries();
        generateRandomData();
        plot.redraw();
        
        Range xDataRange = plot.getModel().getXDataRange();
		plot.setLinearSelection( xDataRange.getFrom(), xDataRange.getTo() );

		for (int i = 1; i <= 2; i++) {
			Axis yAxis = plot.getWindowPlot().getAxes().getY(i);
			plot.getWindowOptions().getYAxisOptions(i).setZoomRange(true).setPanRange(yAxis.getMinimumValue(), yAxis.getMaximumValue());
		}
    }

    /**
     * On click on insert button
     * 
     * @param e event
     */
    @GFlotExamplesSource
    @UiHandler( "color1" )
    void onClickColor1( ClickEvent e )
    {
    	String color = CssColor.make(Random.nextInt(255), Random.nextInt(255), Random.nextInt(255)).value();
		plot.getModel().getHandlers().get(0).getWindowSeries().setColor(color);
    	plot.getModel().getHandlers().get(0).getOverviewSeries().setColor(color);
    	
    	plot.refresh();
    }

    /**
     * On click on insert button
     * 
     * @param e event
     */
    @GFlotExamplesSource
    @UiHandler( "color2" )
    void onClickColor2( ClickEvent e )
    {
    	String color = CssColor.make(Random.nextInt(255), Random.nextInt(255), Random.nextInt(255)).value();
		plot.getModel().getHandlers().get(1).getWindowSeries().setColor(color);
    	plot.getModel().getHandlers().get(1).getOverviewSeries().setColor(color);
    	
    	plot.refresh();
    }
    
    /**
     * On click on insert button
     * 
     * @param e event
     */
    @GFlotExamplesSource
    @UiHandler( "lock1" )
    void onClickLock1( ClickEvent e )
    {
    	for (int i = 1; i <= 2; i++) {
    		if (i == 1) {
    			plot.getWindowOptions().getYAxisOptions(i).setZoomRange(true).setPanRange(true);
			} else {
				Axis yAxis = plot.getWindowPlot().getAxes().getY(i);
				plot.getWindowOptions().getYAxisOptions(i).setZoomRange(false).setPanRange(false).setMinimum(yAxis.getMinimumValue()).setMaximum(yAxis.getMaximumValue());
			}
		}
    	plot.getWindowOptions().getXAxisOptions().setZoomRange(false).setPanRange(false);
    	
    	plot.getWindowPlot().redraw();
    }

    /**
     * On click on insert button
     * 
     * @param e event
     */
    @GFlotExamplesSource
    @UiHandler( "lock2" )
    void onClickLock2( ClickEvent e )
    {
    	for (int i = 1; i <= 2; i++) {
    		if (i == 2) {
    			plot.getWindowOptions().getYAxisOptions(i).setZoomRange(true).setPanRange(true);
			} else {
				Axis yAxis = plot.getWindowPlot().getAxes().getY(i);
				plot.getWindowOptions().getYAxisOptions(i).setZoomRange(false).setPanRange(false).setMinimum(yAxis.getMinimumValue()).setMaximum(yAxis.getMaximumValue());
			}
		}
    	plot.getWindowOptions().getXAxisOptions().setZoomRange(false).setPanRange(false);
    	
    	plot.getWindowPlot().redraw();
    }
    
    /**
     * On click on insert button
     * 
     * @param e event
     */
    @GFlotExamplesSource
    @UiHandler( "unlock" )
    void onClickUnLock( ClickEvent e )
    {
		for (int i = 1; i <= 2; i++) {
			Axis yAxis = plot.getWindowPlot().getAxes().getY(i);
			plot.getWindowOptions().getYAxisOptions(i).setZoomRange(true).setPanRange(yAxis.getMinimumValue(), yAxis.getMaximumValue()).setMinimum(yAxis.getMinimumValue()).setMaximum(yAxis.getMaximumValue());
		}
		plot.getWindowOptions().getXAxisOptions().setZoomRange(true).setPanRange(plot.getModel().getXDataRange().getFrom(), plot.getModel().getXDataRange().getTo());
		
		plot.getWindowPlot().redraw();
    }
    
    /**
     * On click on insert button
     * 
     * @param e event
     */
    @GFlotExamplesSource
    @UiHandler( "zoomIn" )
    void onClickZoomIn( ClickEvent e )
    {
    	plot.getWindowPlot().zoom(Zoom.create().setPreventEvent(true));
    }
    
    /**
     * On click on insert button
     * 
     * @param e event
     */
    @GFlotExamplesSource
    @UiHandler( "zoomOut" )
    void onClickZoomOut( ClickEvent e )
    {
    	plot.getWindowPlot().zoomOut(Zoom.create().setPreventEvent(true));
    }
    
    /**
     * On click on insert button
     * 
     * @param e event
     */
    @GFlotExamplesSource
    @UiHandler( "moveUp" )
    void onClickMoveUp( ClickEvent e )
    {
    	plot.getWindowPlot().pan(Pan.create().setTop(10).setPreventEvent(true));
    }
    
    /**
     * On click on insert button
     * 
     * @param e event
     */
    @GFlotExamplesSource
    @UiHandler( "moveDown" )
    void onClickMoveDown( ClickEvent e )
    {
    	plot.getWindowPlot().pan(Pan.create().setTop(-10).setPreventEvent(true));
    }
    /**
     * Generate random data
     */
    @GFlotExamplesSource
    private void generateRandomData()
    {
    	
    	plot.getWindowOptions().setYAxesOptions(AxesOptions.create());
    	plot.getOverviewOptions().setYAxesOptions(AxesOptions.create());
    	
		SeriesDataHelper seriesOne = new SeriesDataHelper(new SeriesDataOne());
		PlotWithOverviewSeriesHandler seriesOneHandler = (PlotWithOverviewSeriesHandler) plot.getModel().addSeries(Series.of("Series One"), PlotModelStrategy.downSamplingStrategy(100));
		
		for (int i = 0; i < seriesOne.size(); i++) {
			seriesOneHandler.add(seriesOne.getPoint(i));
		}
		seriesOneHandler.getWindowSeries().setYAxis(1);
		seriesOneHandler.getOverviewSeries().setYAxis(1);
		
		plot.getWindowOptions().addYAxisOptions(AxisOptions.create());
		plot.getOverviewOptions().addYAxisOptions(AxisOptions.create().setZoomRange(false).setPanRange(false));
		
		SeriesDataHelper seriesTwo = new SeriesDataHelper(new SeriesDataTwo());
		PlotWithOverviewSeriesHandler seriesTwoHandler = (PlotWithOverviewSeriesHandler) plot.getModel().addSeries(Series.of("Series Two"), PlotModelStrategy.downSamplingStrategy(100));

		for (int i = 0; i < seriesTwo.size(); i++) {
			seriesTwoHandler.add(seriesTwo.getPoint(i));
		}
		seriesTwoHandler.getWindowSeries().setYAxis(2);
		seriesTwoHandler.getOverviewSeries().setYAxis(2);
		
		plot.getWindowOptions().addYAxisOptions(AxisOptions.create());
		plot.getOverviewOptions().addYAxisOptions(AxisOptions.create().setZoomRange(false).setPanRange(false));
		
		plot.getWindowOptions().setXAxesOptions(AxesOptions.create());
		plot.getWindowOptions().addXAxisOptions(AxisOptions.create().setZoomRange(true).setPanRange(plot.getModel().getXDataRange().getFrom(), plot.getModel().getXDataRange().getTo()));
    }

}
