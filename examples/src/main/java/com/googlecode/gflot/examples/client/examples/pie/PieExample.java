package com.googlecode.gflot.examples.client.examples.pie;


import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.ParagraphElement;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Widget;
import com.googlecode.gflot.client.PieDataPoint;
import com.googlecode.gflot.client.PlotModel;
import com.googlecode.gflot.client.Series;
import com.googlecode.gflot.client.SeriesHandler;
import com.googlecode.gflot.client.SimplePlot;
import com.googlecode.gflot.client.event.PlotHoverListener;
import com.googlecode.gflot.client.event.PlotItem;
import com.googlecode.gflot.client.event.PlotPosition;
import com.googlecode.gflot.client.jsni.Plot;
import com.googlecode.gflot.client.options.GlobalSeriesOptions;
import com.googlecode.gflot.client.options.GridOptions;
import com.googlecode.gflot.client.options.LegendOptions;
import com.googlecode.gflot.client.options.PieSeriesOptions;
import com.googlecode.gflot.client.options.PlotOptions;
import com.googlecode.gflot.client.options.PieSeriesOptions.Label;
import com.googlecode.gflot.client.options.PieSeriesOptions.Label.Background;
import com.googlecode.gflot.client.options.PieSeriesOptions.Label.Formatter;
import com.googlecode.gflot.examples.client.examples.DefaultActivity;
import com.googlecode.gflot.examples.client.resources.Resources;
import com.googlecode.gflot.examples.client.source.SourceAnnotations.GFlotExamplesData;
import com.googlecode.gflot.examples.client.source.SourceAnnotations.GFlotExamplesRaw;
import com.googlecode.gflot.examples.client.source.SourceAnnotations.GFlotExamplesSource;

/**
 * @author Nicolas Morel
 */
@GFlotExamplesRaw( PiePlace.UI_RAW_SOURCE_FILENAME )
public class PieExample
    extends DefaultActivity
{
    private static NumberFormat formatter = NumberFormat.getFormat( "0.#" );

    private static Binder binder = GWT.create( Binder.class );

    interface Binder
        extends UiBinder<Widget, PieExample>
    {
    }

    /**
     * Plot
     */
    @GFlotExamplesData
    @UiField( provided = true )
    SimplePlot plot;

    /**
     * Paragraph to add the hovering series
     */
    @GFlotExamplesData
    @UiField
    ParagraphElement hovering;

    public PieExample( Resources resources )
    {
        super( resources );
    }

    /**
     * Create plot
     */
    @Override
    @GFlotExamplesSource
    public Widget createPlot()
    {
        final PlotModel model = new PlotModel();
        final PlotOptions plotOptions = PlotOptions.create();

        // activate the pie
        plotOptions.setGlobalSeriesOptions( GlobalSeriesOptions.create().setPieSeriesOptions(
            PieSeriesOptions
                .create()
                .setShow( true )
                .setRadius( 1 )
                .setInnerRadius( 0.2 )
                .setLabel(
                    Label.create().setShow( true ).setRadius( 3d / 4d )
                        .setBackground( Background.create().setOpacity( 0.8 ) ).setThreshold( 0.05 )
                        .setFormatter( new Formatter() {
                            @Override
                            public String format( String label, Series series )
                            {
                                return "<div style=\"font-size:8pt;text-align:center;padding:2px;color:white;\">"
                                    + label + "<br/>" + formatter.format( series.getData().getY( 0 ) ) + " / "
                                    + formatter.format( series.getPercent() ) + "%</div>";
                            }
                        } ) ) ) );
        plotOptions.setLegendOptions( LegendOptions.create().setShow( false ) );
        plotOptions.setGridOptions( GridOptions.create().setHoverable( true ) );

        // create series
        SeriesHandler series1 = model.addSeries( Series.of( "Series 1" ) );
        series1.add( PieDataPoint.of( 148 ) );

        SeriesHandler series2 = model.addSeries( Series.of( "Series 2" ) );
        series2.add( PieDataPoint.of( 221 ) );

        SeriesHandler series3 = model.addSeries( Series.of( "Series 3" ) );
        series3.add( PieDataPoint.of( 25 ) );

        SeriesHandler series4 = model.addSeries( Series.of( "Series 4" ) );
        series4.add( PieDataPoint.of( 35 ) );

        SeriesHandler series5 = model.addSeries( Series.of( "Series 5" ) );
        series5.add( PieDataPoint.of( 102 ) );

        // create the plot
        plot = new SimplePlot( model, plotOptions );

        plot.addHoverListener( new PlotHoverListener() {
            @Override
            public void onPlotHover( Plot plot, PlotPosition position, PlotItem item )
            {
                hovering.setInnerText( "Hovering series n\u00b0" + ( item.getSeriesIndex() + 1 ) + " : "
                    + formatter.format( item.getSeries().getData().getY( 0 ) ) + " / "
                    + formatter.format( item.getSeries().getPercent() ) + "%" );
            }
        }, true );

        plot.addDomHandler( new MouseOutHandler() {
            @Override
            public void onMouseOut( MouseOutEvent event )
            {
                hovering.setInnerText( "" );
            }
        }, MouseOutEvent.getType() );

        return binder.createAndBindUi( this );
    }
}
