package com.blueprinthell.view;

import com.blueprinthell.model.NetworkDevice;

/** کارخانهٔ ساخت View بر اساس نوع مدل */
public class DeviceViewFactory {
    public static AbstractDeviceView create(NetworkDevice model) {
        return switch (model.getType()) {
            case START      -> new StartSystemView(model);
            case PROCESSING -> new ProcessingSystemView(model);
            case END        -> new EndSystemView(model);
        };
    }
}
