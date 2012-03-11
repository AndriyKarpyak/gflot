package ca.nanometrics.gflot.sample.client.resources;

import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;

public interface Resources
    extends ClientBundle
{
    @Source( "gflot.css" )
    Style style();

    public interface Style
        extends CssResource
    {
        String menuLink();

        String menuLinkSelected();
    }
}