package com.googlecode.gflot.examples.client.examples.navigate;


import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.googlecode.gflot.client.Axes;
import com.googlecode.gflot.client.DataPoint;
import com.googlecode.gflot.client.Pan;
import com.googlecode.gflot.client.PlotModel;
import com.googlecode.gflot.client.Series;
import com.googlecode.gflot.client.SeriesHandler;
import com.googlecode.gflot.client.SimplePlot;
import com.googlecode.gflot.client.event.PlotPanListener;
import com.googlecode.gflot.client.event.PlotZoomListener;
import com.googlecode.gflot.client.jsni.Plot;
import com.googlecode.gflot.client.options.AxisOptions;
import com.googlecode.gflot.client.options.GlobalSeriesOptions;
import com.googlecode.gflot.client.options.LegendOptions;
import com.googlecode.gflot.client.options.LineSeriesOptions;
import com.googlecode.gflot.client.options.PanOptions;
import com.googlecode.gflot.client.options.PlotOptions;
import com.googlecode.gflot.client.options.ZoomOptions;
import com.googlecode.gflot.examples.client.examples.DefaultActivity;
import com.googlecode.gflot.examples.client.resources.Resources;
import com.googlecode.gflot.examples.client.source.SourceAnnotations.GFlotExamplesData;
import com.googlecode.gflot.examples.client.source.SourceAnnotations.GFlotExamplesRaw;
import com.googlecode.gflot.examples.client.source.SourceAnnotations.GFlotExamplesSource;

/**
 * @author Nicolas Morel
 */
@GFlotExamplesRaw( NavigatePlace.UI_RAW_SOURCE_FILENAME )
public class NavigateExample
    extends DefaultActivity
{

    private static Binder binder = GWT.create( Binder.class );

    interface Binder
        extends UiBinder<Widget, NavigateExample>
    {
    }

    private static final String NO_ZOOM_MESSAGE = "No current zoom or pan";

    /**
     * Plot
     */
    @GFlotExamplesData
    @UiField( provided = true )
    SimplePlot plot;

    /**
     * Message showing panning and zooming information
     */
    @GFlotExamplesData
    @UiField
    Label message;

    public NavigateExample( Resources resources )
    {
        super( resources );
    }

    /**
     * Create plot
     */
    @GFlotExamplesSource
    protected Widget createPlot()
    {
        PlotModel model = new PlotModel();
        PlotOptions plotOptions = PlotOptions.create();
        plotOptions.setGlobalSeriesOptions( GlobalSeriesOptions.create()
            .setLineSeriesOptions( LineSeriesOptions.create().setShow( true ).setFill( true ) ).setShadowSize( 0 ) );
        plotOptions.setLegendOptions( LegendOptions.create().setShow( false ) );

        plotOptions.addXAxisOptions( AxisOptions.create().setZoomRange( 0.1d, 10d ).setPanRange( -10d, 10d ) );
        plotOptions.addYAxisOptions( AxisOptions.create().setZoomRange( 0.1d, 10d ).setPanRange( -10d, 10d )
            .setLabelWidth( 20 ) );
        plotOptions.setZoomOptions( ZoomOptions.create().setInteractive( true ) ).setPanOptions(
            PanOptions.create().setInteractive( true ) );

        // create series
        SeriesHandler series1 = model.addSeries( Series.of( "Series1" ) );

        // add data
        for ( double t = 0; t <= 2 * Math.PI; t += 0.01 )
        {
            series1.add( DataPoint.of( sumfCos( t, 10 ), sumfSin( t, 10 ) ) );
        }

        // create the plot
        plot = new SimplePlot( model, plotOptions );

        final NumberFormat format = NumberFormat.getDecimalFormat();

        plot.addZoomListener( new PlotZoomListener() {
            @Override
            public void onPlotZoom( Plot plot )
            {
                Axes axes = plot.getAxes();
                message.setText( "Zooming to x=[min:\"" + format.format( axes.getX().getMinimumValue() ) + "\", max:\""
                    + format.format( axes.getX().getMaximumValue() ) + "\"], y=[min:\""
                    + format.format( axes.getY().getMinimumValue() ) + "\", max:\""
                    + format.format( axes.getY().getMaximumValue() ) + "\"]" );
            }
        } );

        plot.addPanListener( new PlotPanListener() {

            @Override
            public void onPlotPan( Plot plot )
            {
                Axes axes = plot.getAxes();
                message.setText( "Panning to x=[min:\"" + format.format( axes.getX().getMinimumValue() ) + "\", max:\""
                    + format.format( axes.getX().getMaximumValue() ) + "\"], y=[min:\""
                    + format.format( axes.getY().getMinimumValue() ) + "\", max:\""
                    + format.format( axes.getY().getMaximumValue() ) + "\"]" );
            }
        } );

        Widget widget = binder.createAndBindUi( this );
        message.setText( NO_ZOOM_MESSAGE );
        return widget;
    }

    /**
     *
     */
    @GFlotExamplesSource
    private double sumfCos( double t, double m )
    {
        double res = 0;
        for ( double i = 1; i < m; ++i )
        {
            res += Math.cos( i * i * t ) / ( i * i );
        }
        return res;
    }

    /**
     *
     */
    @GFlotExamplesSource
    private double sumfSin( double t, double m )
    {
        double res = 0;
        for ( double i = 1; i < m; ++i )
        {
            res += Math.sin( i * i * t ) / ( i * i );
        }
        return res;
    }

    /**
     * On click left arrow
     */
    @GFlotExamplesSource
    @UiHandler( "left" )
    void onClickLeft( ClickEvent event )
    {
        plot.pan( Pan.create().setLeft( -100 ) );
    }

    /**
     * On click right arrow
     */
    @GFlotExamplesSource
    @UiHandler( "right" )
    void onClickRight( ClickEvent event )
    {
        plot.pan( Pan.create().setLeft( 100 ) );
    }

    /**
     * On click up arrow
     */
    @GFlotExamplesSource
    @UiHandler( "up" )
    void onClickUp( ClickEvent event )
    {
        plot.pan( Pan.create().setTop( -100 ) );
    }

    /**
     * On click down arrow
     */
    @GFlotExamplesSource
    @UiHandler( "down" )
    void onClickDown( ClickEvent event )
    {
        plot.pan( Pan.create().setTop( 100 ) );
    }

    /**
     * On click zoom out
     */
    @GFlotExamplesSource
    @UiHandler( "zoomOut" )
    void onClickZoomOut( ClickEvent event )
    {
        plot.zoomOut();
    }

    /**
     * On click reset
     */
    @GFlotExamplesSource
    @UiHandler( "reset" )
    void onClickReset( ClickEvent event )
    {
        message.setText( NO_ZOOM_MESSAGE );
        plot.getOptions().getXAxisOptions().clearMinimum().clearMaximum();
        plot.getOptions().getYAxisOptions().clearMinimum().clearMaximum();
        plot.redraw();
    }
}
