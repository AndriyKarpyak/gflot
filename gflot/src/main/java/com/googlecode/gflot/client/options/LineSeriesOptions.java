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
package com.googlecode.gflot.client.options;

import com.google.gwt.core.client.JavaScriptObject;

/**
 * @author AlexanderDeleon
 */
public class LineSeriesOptions
    extends AbstractLineSeriesOptions<LineSeriesOptions>
{
    /**
     * Creates a {@link LineSeriesOptions}
     */
    public static final LineSeriesOptions create()
    {
        LineSeriesOptions options = JavaScriptObject.createObject().cast();
        options.put(TYPE_KEY, TYPE_NAME);
		return options;
    }

    public static final String TYPE_NAME = "type-solid";

    private static final String ZERO_KEY = "zero";

    protected LineSeriesOptions()
    {
    }

    /**
     * Set whether the y-axis minimum is scaled to fit the data or set to zero
     */
    public final LineSeriesOptions setZero( boolean zero )
    {
        put( ZERO_KEY, zero );
        return this;
    }

    /**
     * @return true if the y-axis minimum is set to zero
     */
    public final Boolean getZero()
    {
        return getBoolean( ZERO_KEY );
    }

    /**
     * Clear whether the y-axis minimum is scaled to fit the data or set to zero
     */
    public final LineSeriesOptions clearZero()
    {
        clear( ZERO_KEY );
        return this;
    }

}
