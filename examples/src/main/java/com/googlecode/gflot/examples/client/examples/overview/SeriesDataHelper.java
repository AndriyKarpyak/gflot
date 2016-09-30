package com.googlecode.gflot.examples.client.examples.overview;

//import com.google.gwt.i18n.client.DateTimeFormat;
import com.googlecode.gflot.client.DataPoint;

public class SeriesDataHelper {

	private final SeriesData data;

	SeriesDataHelper(SeriesData data) {
		this.data = data;
	}
	
	public DataPoint getPoint(int index) {
		return DataPoint.of(/* DateTimeFormat.getFormat("yyyy-MM-dd hh:mm:ss.SSS").parse(data.getStamps()[index]).getTime()*/ index, Double.parseDouble(data.getValues()[index]) );
	}

	public int size() {
		return data.getValues().length;
	}
}
