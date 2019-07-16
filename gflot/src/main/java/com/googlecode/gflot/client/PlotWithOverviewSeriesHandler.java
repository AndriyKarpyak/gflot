package com.googlecode.gflot.client;

import java.util.List;

import com.google.gwt.user.client.Command;
import com.googlecode.gflot.client.util.Algorithm;

public class PlotWithOverviewSeriesHandler implements SeriesHandler
{
    public interface DataProvider
    {
        void add(DataPoint datapoint);

        SeriesData getData( double x1, double x2 );
        SeriesData getData( double x1, double x2, boolean allowEmpty );

        void setData(SeriesData newData);

        SeriesData getData();
        
        void clear();

        void insertData(List<DataPoint> newData);
    }

    private class LocalDataProvider
        implements DataProvider
    {

        private final SeriesDataStrategy strategy;

        public LocalDataProvider( SeriesDataStrategy strategy )
        {
            this.strategy = strategy;
        }
        
        @Override
        public void add(DataPoint datapoint) {
            strategy.add(datapoint);
        }
        
        @Override
        public SeriesData getData( double x1, double x2 )
        {
            return getData( x1, x2, false );
        }
        
        @Override
        public SeriesData getData( double x1, double x2, boolean allowEmpty )
        {
            SeriesData data = strategy.getData();

            if ( x2 < data.getX( 0 ) || x1 > data.getX( data.length() - 1 ) )
            {
                return SeriesData.create();
            }

            int start = Algorithm.xBinarySearch( data, x1 );
            int end = Algorithm.xBinarySearch( data, x2 );
            
            if ( allowEmpty && start == -1 && end == -1 && start == end )
            {
                return SeriesData.create();
            }
            if ( start == -1 )
            {
                start = 0;
            }
            if ( end == -1 )
            {
                return data.slice( start );
            }
            // slice method doesn't include the end index
            return data.slice( start, end + 1 );
        }

        @Override
        public void setData(SeriesData newData) {
            strategy.setData(newData);
        }

        @Override
        public SeriesData getData() {
            return strategy.getData();
        }

        @Override
        public void insertData(List<DataPoint> newData) {
            SeriesData result = SeriesData.create();
            double insertionStart = newData.get(0).getX();
            double insertionEnd = newData.get(newData.size() - 1).getX();
            
            SeriesData data = strategy.getData();
            for (int i = 0; i < data.length(); i++) {
                if (data.get(i).getX() < insertionStart) {
                    result.push(data.get(i));
                } else {
                    for (int j = 0; j < newData.size(); j++) {
                        result.push(newData.get(j));
                    }
                    for (int k = i; k < data.length(); k++) {
                        if (data.get(k).getX() > insertionEnd) {
                            result.push(data.get(k));
                        }
                    }
                    strategy.setData(result);
                    return;
                }
            }
        }

        @Override
        public void clear() {
            strategy.clear();
        }
    }

    private SeriesHandler overviewHandler;
    private SeriesHandler windowHandler;
    private DataProvider provider;
    private SeriesDataStrategy strategy;

    private DataPoint lastDataPoint;
    private DataPoint firstDataPoint;
    private Double dataMinX;
    private Double dataMaxX;
    private Double dataMinY;
    private Double dataMaxY;
    private double[] selection = new double[2];

    public PlotWithOverviewSeriesHandler( Series series, SeriesDataStrategy strategy, SeriesHandler windowHandler, SeriesHandler overviewHandler )
    {
        this.strategy = strategy;
        this.provider = new LocalDataProvider( PlotModelStrategy.defaultStrategy());
        this.windowHandler = windowHandler;
        this.overviewHandler = overviewHandler;
    }

    @Override
    public void add( DataPoint datapoint )
    {
        provider.add( datapoint );
        
        if ( firstDataPoint == null )
        {
            firstDataPoint = datapoint;
        }
        lastDataPoint = datapoint;

        if (this.dataMinX == null || this.dataMinX > datapoint.getX())
            this.dataMinX = datapoint.getX();
        if (this.dataMaxX == null || this.dataMaxX < datapoint.getX())
            this.dataMaxX = datapoint.getX();

        if (this.dataMinY == null || this.dataMinY > datapoint.getY()) 
            this.dataMinY = datapoint.getY();
        if (this.dataMaxY == null || this.dataMaxY < datapoint.getY())
            this.dataMaxY = datapoint.getY();
    }
    
    @Override
    public void clear()
    {
        strategy.clear();
        provider.clear();
        overviewHandler.clear();
        windowHandler.clear();

        lastDataPoint = null;
        firstDataPoint = null;
        dataMinX = null;
        dataMaxX = null;
        dataMinY = null;
        dataMaxY = null;
        selection = new double[2];
    }

    @Override
    public SeriesData getData()
    {
        return provider.getData();
    }

    @Override
    public void setData( SeriesData newData )
    {
        provider.setData( newData );
        populateOverviewSeries();
        populateWindowSeries( null );
    }
    
    public void insertData( List<DataPoint> newData )
    {
        provider.insertData( newData );
        populateOverviewSeries();
        populateWindowSeries( null );
    }

    public void setSelection(double[] selection) {
        this.selection[0] = selection[0];
        this.selection[1] = selection[1];
    }

    @Override
    public void setVisible( boolean visisble )
    {
        overviewHandler.setVisible( visisble );
        windowHandler.setVisible( visisble );
    }

    @Override
    public boolean isVisible( )
    {
        return overviewHandler.isVisible() && windowHandler.isVisible();
    }

    void populateWindowSeries( final Command toExcuteAfterSelection )
    {
        final double x1 = getWindowMinX();
        final double x2 = getWindowMaxX();
        if ( x1 <= x2 )
        {
            windowHandler.clear();
            strategy.setData(provider.getData( x1, x2 ));
            windowHandler.setData(strategy.getData());
        }
        
        if (toExcuteAfterSelection != null) {
            toExcuteAfterSelection.execute();
        }
    }
    
    void populateOverviewSeries( )
    {
        overviewHandler.clear();
        strategy.setData(provider.getData());
        overviewHandler.setData(strategy.getData());
    }
    
    public double getDataMinX( )
    {
        return dataMinX.doubleValue( );
    }

    public double getDataMaxX( )
    {
        return dataMaxX.doubleValue( );
    }

    public double getDataMinY( )
    {
        return dataMinY.doubleValue( );
    }

    public double getDataMaxY( )
    {
        return dataMaxY.doubleValue( );
    }

    private double getWindowMinX( )
    {
        double x = selection[0];
        SeriesData data = provider.getData();
        int size = data.length();
        if ( size > 0 && x == data.getX( 0 ) )
        {
            return firstDataPoint.getX();
        }
        return x;
    }

    private double getWindowMaxX( )
    {
        double x = selection[1];
        SeriesData data = provider.getData();
        int size = data.length();
        if ( size > 0 && x == data.getX( size - 1 ) )
        {
            return lastDataPoint.getX();
        }
        return x;
    }

    public SeriesData getData( double x1, double x2) {
        return provider.getData(x1, x2);
    }
    
    public SeriesData getData( double x1, double x2, boolean allowEmpty ) {
        return provider.getData(x1, x2, allowEmpty);
    }
    
    @Override
    @Deprecated
    public Series getSeries()
    {
        return getOverviewSeries();
    }

    public Series getOverviewSeries()
    {
        return overviewHandler.getSeries();
    }

    public SeriesHandler getOverviewSeriesHandler()
    {
        return overviewHandler;
    }
    
    public Series getWindowSeries()
    {
        return windowHandler.getSeries();
    }
    
    public SeriesHandler getWindowSeriesHandler()
    {
        return windowHandler;
    }

    @Override
    public boolean equals( Object obj )
    {
        if ( obj == this )
        {
            return true;
        }
        if ( obj instanceof PlotWithOverviewSeriesHandler )
        {
            return getSeries().equals( ( (PlotWithOverviewSeriesHandler) obj ).getSeries() );
        }
        return false;
    }

    @Override
    public int hashCode()
    {
        return getSeries().hashCode();
    }
}
