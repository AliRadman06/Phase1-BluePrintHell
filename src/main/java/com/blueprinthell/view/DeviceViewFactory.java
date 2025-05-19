package com.blueprinthell.view;

import com.blueprinthell.model.NetworkDevice;
import com.blueprinthell.model.StartSystem;

public class DeviceViewFactory {
    public static AbstractDeviceView create(NetworkDevice model) {
        return switch (model.getType()) {
            case START      -> new StartSystemView(model);
            case PROCESSING -> new ProcessingSystemView(model);
            case END        -> new EndSystemView(model);
        };
    }
}
