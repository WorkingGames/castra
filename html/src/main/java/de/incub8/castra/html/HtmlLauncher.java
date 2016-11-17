package de.incub8.castra.html;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.backends.gwt.GwtApplication;
import com.badlogic.gdx.backends.gwt.GwtApplicationConfiguration;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NodeList;
import com.google.gwt.dom.client.Style;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.UIObject;
import com.google.gwt.user.client.ui.VerticalPanel;
import de.incub8.castra.core.Castra;

public class HtmlLauncher extends GwtApplication
{
    private static final String APPLICATION_CONTAINER_ID = "applicationContainer";

    private static final int WIDTH = 1366;
    private static final int HEIGHT = 768;

    private static HtmlLauncher instance;

    @Override
    public GwtApplicationConfiguration getConfig()
    {
        Panel rootPanel = createRootPanel();
        appendToApplicationContainer(rootPanel);
        return createConfiguration(rootPanel);
    }

    private Panel createRootPanel()
    {
        VerticalPanel result = new VerticalPanel();
        result.setWidth("100%");
        result.setHeight("100%");
        result.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
        result.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
        return result;
    }

    private void appendToApplicationContainer(UIObject uiObject)
    {
        getApplicationContainer().appendChild(uiObject.getElement());
    }

    private GwtApplicationConfiguration createConfiguration(Panel rootPanel)
    {
        GwtApplicationConfiguration config = new GwtApplicationConfiguration(WIDTH, HEIGHT);
        config.rootPanel = rootPanel;
        return config;
    }

    @Override
    public ApplicationListener createApplicationListener()
    {
        instance = this;
        setLoadingListener(
            new LoadingListener()
            {
                @Override
                public void beforeSetup()
                {

                }

                @Override
                public void afterSetup()
                {
                    scaleCanvas();
                    setupResizeHook();
                }
            });
        return new Castra();
    }

    void scaleCanvas()
    {
        int innerWidth = getWindowInnerWidth();
        int innerHeight = getWindowInnerHeight();
        int newWidth = innerWidth;
        int newHeight = innerHeight;
        float ratio = innerWidth / (float) innerHeight;
        float viewRatio = WIDTH / (float) HEIGHT;

        if (ratio > viewRatio)
        {
            newWidth = (int) (innerHeight * viewRatio);
        }
        else
        {
            newHeight = (int) (innerWidth / viewRatio);
        }

        NodeList<Element> canvasElements = getApplicationContainer().getElementsByTagName("canvas");

        if (canvasElements != null && canvasElements.getLength() > 0)
        {
            Element canvas = canvasElements.getItem(0);
            canvas.setAttribute("width", "" + newWidth + "px");
            canvas.setAttribute("height", "" + newHeight + "px");
            canvas.getStyle().setWidth(newWidth, Style.Unit.PX);
            canvas.getStyle().setHeight(newHeight, Style.Unit.PX);
            canvas.getStyle().setTop((int) ((innerHeight - newHeight) * 0.5f), Style.Unit.PX);
            canvas.getStyle().setLeft((int) ((innerWidth - newWidth) * 0.5f), Style.Unit.PX);
            canvas.getStyle().setPosition(Style.Position.ABSOLUTE);
        }
    }

    private Element getApplicationContainer()
    {
        return Document.get().getElementById(APPLICATION_CONTAINER_ID);
    }

    native int getWindowInnerWidth() /*-{
        return $wnd.innerWidth;
    }-*/;

    native int getWindowInnerHeight() /*-{
        return $wnd.innerHeight;
    }-*/;

    native void setupResizeHook() /*-{
        var htmlLauncher_onWindowResize = $entry(@de.incub8.castra.html.HtmlLauncher::handleResize());
        $wnd.addEventListener('resize', htmlLauncher_onWindowResize, false);
    }-*/;

    public static void handleResize()
    {
        instance.scaleCanvas();
    }
}