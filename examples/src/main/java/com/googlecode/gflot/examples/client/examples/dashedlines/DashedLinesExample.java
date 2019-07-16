package com.googlecode.gflot.examples.client.examples.dashedlines;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Widget;
import com.googlecode.gflot.client.DataPoint;
import com.googlecode.gflot.client.PlotModel;
import com.googlecode.gflot.client.SeriesHandler;
import com.googlecode.gflot.client.SimplePlot;
import com.googlecode.gflot.client.options.AxisOptions;
import com.googlecode.gflot.client.options.DashedLineSeriesOptions;
import com.googlecode.gflot.client.options.PlotOptions;
import com.googlecode.gflot.examples.client.examples.DefaultActivity;
import com.googlecode.gflot.examples.client.resources.Resources;
import com.googlecode.gflot.examples.client.source.SourceAnnotations.GFlotExamplesData;
import com.googlecode.gflot.examples.client.source.SourceAnnotations.GFlotExamplesRaw;
import com.googlecode.gflot.examples.client.source.SourceAnnotations.GFlotExamplesSource;

@GFlotExamplesRaw(DashedLinesPlace.UI_RAW_SOURCE_FILENAME)
public class DashedLinesExample extends DefaultActivity {

    private static Binder binder = GWT.create(Binder.class);

    interface Binder extends UiBinder<Widget, DashedLinesExample> {
    }

    /**
     * Plot
     */
    @GFlotExamplesData
    @UiField(provided = true)
    SimplePlot plot;

    public DashedLinesExample(Resources resources) {
        super(resources);
    }

    /**
     * Create plot
     */
    @Override
    @GFlotExamplesSource
    protected Widget createPlot() {
        final PlotModel model = new PlotModel();
        final PlotOptions plotOptions = PlotOptions.create();

        // add tick formatter to the options
        plotOptions.addXAxisOptions(AxisOptions.create().setMinimum(-9).setMaximum(5));
        plotOptions.addYAxisOptions(AxisOptions.create().setMinimum(-9).setMaximum(1));

        // create a series
        SeriesHandler handlerLine = model.addSeries();

        handlerLine = model.addSeries();
        handlerLine.add(DataPoint.of(-8, -8));
        handlerLine.add(DataPoint.of(-6, -4));
        handlerLine.add(DataPoint.of(-2, -8));
        handlerLine.add(DataPoint.of(4, 0));

        handlerLine.getSeries().setDashedLineSeriesOptions(DashedLineSeriesOptions.create().setShow(true).setLineWidth(1).setDashLength(6, 3));
        handlerLine.getSeries().getDashedLineSeriesOptions().setSteps(true);

        // create a series
        handlerLine = model.addSeries();
        handlerLine.add(DataPoint.of(-8, -8));
        handlerLine.add(DataPoint.of(-6, -4));
        handlerLine.add(DataPoint.of(-2, -8));
        handlerLine.add(DataPoint.of(4, 0));

        handlerLine.getSeries().setDashedLineSeriesOptions(DashedLineSeriesOptions.create().setShow(true).setLineWidth(1).setDashLength(6, 3));
        handlerLine.getSeries().getDashedLineSeriesOptions().setSteps(false);

        // create the plot
        plot = new SimplePlot(model, plotOptions);

        return binder.createAndBindUi(this);
    }

}
