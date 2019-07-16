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
import com.google.gwt.core.client.JsArrayNumber;
import com.googlecode.gflot.client.options.AbstractLineSeriesOptions;

/**
 * @author Andriy 'kan' Karpyak
 */
public final class DashedLineSeriesOptions
    extends AbstractLineSeriesOptions<DashedLineSeriesOptions>
{
    /**
     * Creates a {@link DashedLineSeriesOptions}
     */
    public static final DashedLineSeriesOptions create()
    {
        DashedLineSeriesOptions options = JavaScriptObject.createObject().cast();
        options.put(TYPE_KEY, TYPE_NAME);
        return options;
    }

    public static final String TYPE_NAME = "type-dashed";

    private static final String DASH_LENGTH_KEY = "dashLength";

    protected DashedLineSeriesOptions()
    {
    }

    /**
     * Sets length of dashes, length of gaps will be equal to length of dashes
     * 
     * @param dashLength
     *            - length of dash
     */
    public final DashedLineSeriesOptions setDashLength( int dashLength )
    {
        put(DASH_LENGTH_KEY, dashLength);
        return this;
    }

    /**
     * Sets length of dashes and gaps
     * 
     * @param dashLength
     *            - length of dash
     * @param gap
     *            - length of gap between dashes
     */
    public final DashedLineSeriesOptions setDashLength( int dashLength, int gap )
    {
        JsArrayNumber array = JavaScriptObject.createArray().cast();
        array.set(0, dashLength);
        array.set(1, gap);
        put(DASH_LENGTH_KEY, array);
        return this;
    }

    /**
     * Sets length of dashes and gaps
     * 
     * @param JsArrayNumber
     *            - length of dash and gap
     */
    public final DashedLineSeriesOptions setDashLength( JsArrayNumber array )
    {
        put(DASH_LENGTH_KEY, array);
        return this;
    }

    /**
     * Sets length of dashes and gaps
     */
    public final JsArrayNumber getDashLength()
    {
        return getDoubleArray(DASH_LENGTH_KEY);
    }

    /**
     * Clear length of dashes and gaps
     */
    public final DashedLineSeriesOptions clearZero()
    {
        clear( DASH_LENGTH_KEY );
        return this;
    }
}
