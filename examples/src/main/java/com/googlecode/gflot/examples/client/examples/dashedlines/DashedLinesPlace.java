package com.googlecode.gflot.examples.client.examples.dashedlines;

import com.googlecode.gflot.examples.client.source.PlaceWithSources;

/**
 * @author Igor Baldachini
 */
public class DashedLinesPlace extends PlaceWithSources<DashedLinesPlace> {
    private static final String SOURCE_FILENAME;

    static final String UI_RAW_SOURCE_FILENAME = "DashedLinesExample.ui.xml";

    private static final String[] RAW_SOURCE_FILENAMES = new String[] { UI_RAW_SOURCE_FILENAME };

    static {
        SOURCE_FILENAME = extractSourceFilenameFromClassName(DashedLinesExample.class.getName());
    }

    @Override
    public String getSourceFilename() {
        return SOURCE_FILENAME;
    }

    @Override
    public String[] getRawSourceFilenames() {
        return RAW_SOURCE_FILENAMES;
    }

    /**
     * Default constructor. It will show the example
     */
    public DashedLinesPlace() {
        super();
    }

    public DashedLinesPlace(String filename, boolean rawSource) {
        super(filename, rawSource);
    }

    @Override
    public DashedLinesPlace createPlace() {
        return new DashedLinesPlace();
    }

}