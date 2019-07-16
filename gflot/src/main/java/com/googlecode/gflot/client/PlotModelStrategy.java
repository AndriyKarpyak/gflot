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

/**
 * @author Alexander De Leon
 */
public final class PlotModelStrategy
{

    /**
     * A plot model strategy that allows unlimited amount of datapoints.
     *
     * @return the new default {@link SeriesDataStrategy}
     */
    public static SeriesDataStrategy defaultStrategy()
    {
        return new DefaultSeriesDataStrategy();
    }

    public static SeriesDataStrategy lttbDownsamplingSeriesDataStrategy( final int threshold )
    {
    	return new LTTBDownsamplingSeriesDataStrategy( threshold );
    }

    public static SeriesDataStrategy downSamplingStrategy( final int capacity )
    {
        return downSamplingStrategy( capacity, 0 );
    }

    public static SeriesDataStrategy downSamplingStrategy( final int capacity, final long maximumXValueSpan )
    {
        if ( maximumXValueSpan <= 0 )
        {
            return new DownsamplingSeriesDataStrategy( capacity );
        }
        return new FixedSpanDownsamplingSeriesDataStrategy( capacity, maximumXValueSpan );
    }

    public static SeriesDataStrategy slidingWindowStrategy( final int capacity, final long maximumXValueSpan )
    {
        if ( maximumXValueSpan <= 0 )
        {
            return new FixedSizeSeriesDataStrategy( capacity );
        }
        else
        {
            return new FixedSpanFixedSizeSeriesDataStrategy( capacity, maximumXValueSpan );
        }
    }

    public static SeriesDataStrategy slidingWindowStrategy( final int capacity )
    {
        return slidingWindowStrategy( capacity, 0 );
    }

    public static SeriesDataStrategy copy(SeriesDataStrategy strategy) {
        if (strategy instanceof DownsamplingSeriesDataStrategy) {
            return new DownsamplingSeriesDataStrategy(((DownsamplingSeriesDataStrategy) strategy).getCapacity());
        } else if (strategy instanceof FixedSpanDownsamplingSeriesDataStrategy) {
            return new FixedSpanDownsamplingSeriesDataStrategy(((FixedSpanDownsamplingSeriesDataStrategy) strategy).getCapacity(), ((FixedSpanDownsamplingSeriesDataStrategy) strategy).getMaximumSpan());
        } else if (strategy instanceof FixedSizeSeriesDataStrategy) {
            return new FixedSizeSeriesDataStrategy(((FixedSizeSeriesDataStrategy) strategy).getCapacity());
        } else if (strategy instanceof FixedSpanFixedSizeSeriesDataStrategy) {
            return new FixedSpanFixedSizeSeriesDataStrategy(((FixedSpanFixedSizeSeriesDataStrategy) strategy).getCapacity(), ((FixedSpanFixedSizeSeriesDataStrategy) strategy).getMaximumSpan());
        } else if (strategy instanceof LTTBDownsamplingSeriesDataStrategy) {
            return new LTTBDownsamplingSeriesDataStrategy(((LTTBDownsamplingSeriesDataStrategy) strategy).getThreshold());
        } else {
            return new DefaultSeriesDataStrategy();
        }
    }

}
