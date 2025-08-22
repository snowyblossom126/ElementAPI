package io.snowyblossom126.elementapi;

import io.snowyblossom126.elementapi.api.ElementAPI;
import io.snowyblossom126.elementapi.utilities.Log;
import org.bukkit.plugin.java.JavaPlugin;

public final class ElementAPIPlugin extends JavaPlugin {


    @Override
    public void onEnable() {
        Log.init(this);
        ElementAPI.getInstance().init(this);
    }
}
