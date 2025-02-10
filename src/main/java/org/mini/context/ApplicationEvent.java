package org.mini.context;

import org.mini.core.env.EnvironmentCapable;

import java.util.EventObject;

public class ApplicationEvent extends EventObject {

    private static final long serialVersionUID = 1L;
    protected String msg = null;


    public ApplicationEvent(Object arg0) {
        super(arg0);
        msg = arg0.toString();
    }
}
