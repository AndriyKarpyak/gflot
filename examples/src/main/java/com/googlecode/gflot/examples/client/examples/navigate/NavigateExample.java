package com.googlecode.gflot.examples.client.examples.navigate;

import ca.nanometrics.gflot.client.DataPoint;
import ca.nanometrics.gflot.client.PlotModel;
import ca.nanometrics.gflot.client.SeriesHandler;
import ca.nanometrics.gflot.client.SimplePlot;
import ca.nanometrics.gflot.client.options.AxisOptions;
import ca.nanometrics.gflot.client.options.GlobalSeriesOptions;
import ca.nanometrics.gflot.client.options.LegendOptions;
import ca.nanometrics.gflot.client.options.LineSeriesOptions;
import ca.nanometrics.gflot.client.options.PanOptions;
import ca.nanometrics.gflot.client.options.PlotOptions;
import ca.nanometrics.gflot.client.options.ZoomOptions;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Widget;
import com.googlecode.gflot.examples.client.examples.DefaultActivity;
import com.googlecode.gflot.examples.client.resources.Resources;
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

    @UiField( provided = true )
    SimplePlot plot;

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
        PlotOptions plotOptions = new PlotOptions();
        plotOptions.setGlobalSeriesOptions( new GlobalSeriesOptions().setLineSeriesOptions( new LineSeriesOptions()
            .setShow( true ).setFill( true ) ) );
        plotOptions.setLegendOptions( new LegendOptions().setShow( false ) );
        plotOptions.addXAxisOptions( new AxisOptions().setZoomRange( 0.1d, 10d ).setPanRange(
            new Double[] { -10d, 10d } ) );
        plotOptions.addYAxisOptions( new AxisOptions().setZoomRange( false ).setPanRange( false ) );
        plotOptions.setZoomOptions( new ZoomOptions().setInteractive( true ) ).setPanOptions(
            new PanOptions().setInteractive( true ) );

        // create series
        SeriesHandler series1 = model.addSeries( "Series1" );

        // add data
        for ( double t = 0; t <= 2 * Math.PI; t += 0.01 )
        {
            series1.add( new DataPoint( sumfCos( t, 10 ), sumfSin( t, 10 ) ) );
        }

        // create the plot
        plot = new SimplePlot( model, plotOptions );

        return binder.createAndBindUi( this );
    }

    private double sumfCos( double t, double m )
    {
        double res = 0;
        for ( double i = 1; i < m; ++i )
        {
            res += Math.cos( i * i * t ) / ( i * i );
        }
        return res;
    }

    private double sumfSin( double t, double m )
    {
        double res = 0;
        for ( double i = 1; i < m; ++i )
        {
            res += Math.sin( i * i * t ) / ( i * i );
        }
        return res;
    }
}
