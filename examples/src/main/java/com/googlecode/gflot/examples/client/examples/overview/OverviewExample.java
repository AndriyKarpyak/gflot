package com.googlecode.gflot.examples.client.examples.overview;


import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Random;
import com.google.gwt.user.client.ui.Widget;
import com.googlecode.gflot.client.DataPoint;
import com.googlecode.gflot.client.PlotWithOverview;
import com.googlecode.gflot.client.PlotWithOverviewModel;
import com.googlecode.gflot.client.PlotWithOverviewModel.PlotWithOverviewSeriesHandler;
import com.googlecode.gflot.client.Series;
import com.googlecode.gflot.client.SeriesHandler;
import com.googlecode.gflot.client.options.GlobalSeriesOptions;
import com.googlecode.gflot.client.options.LineSeriesOptions;
import com.googlecode.gflot.client.options.PlotOptions;
import com.googlecode.gflot.client.options.Range;
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
        PlotOptions plotOptions = PlotOptions.create();
        plotOptions.setGlobalSeriesOptions( GlobalSeriesOptions.create().setLineSeriesOptions(
            LineSeriesOptions.create().setLineWidth( 0 ).setShow( true ).setFill( true ) ) );

        // create the plot
        plot = new PlotWithOverview( model, plotOptions );

        generateRandomData();

        Range xDataRange = plot.getModel().getXDataRange();
		plot.setLinearSelection( xDataRange.getFrom(), xDataRange.getTo() );
		
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
        Range xDataRange = plot.getModel().getXDataRange();
        plot.redraw();
		plot.setLinearSelection( xDataRange.getFrom(), xDataRange.getTo() );
    }

    /**
     * On click on insert button
     * 
     * @param e event
     */
    @GFlotExamplesSource
    @UiHandler( "insert" )
    void onClickInsert( ClickEvent e )
    {
    	int size = Random.nextInt(500);
        for ( int i = 1; i < (size > 10 ? size : 200); i++ )
        {
            for ( PlotWithOverviewSeriesHandler series : plot.getModel().getHandlers() )
            {
                series.getWindowSeriesHandler().add( DataPoint.of( i, 1.5 + Random.nextDouble(), 1.5 - Random.nextDouble() ) );
            }
        }
        plot.getWindowPlot().redraw();
    }

    /**
     * Generate random data
     */
    @GFlotExamplesSource
    private void generateRandomData()
    {
        int nbSeries = Random.nextInt( 3 ) + 1;
        for ( int i = 0; i < nbSeries; i++ )
        {
            plot.getModel().addSeries( Series.of( "Random Series " + i ) );
        }
        int size = Random.nextInt(500);
        for ( int i = 1; i < (size > 10 ? size : 200); i++ )
        {
            for ( SeriesHandler series : plot.getModel().getHandlers() )
            {
                series.add( DataPoint.of( i, 1.5 + Random.nextDouble(), 1.5 - Random.nextDouble() ) );
            }
        }
    }

}
