package io.lumpq126.elementapi;

import io.lumpq126.elementapi.utilities.Log;
import org.bukkit.plugin.java.JavaPlugin;

public class ElementAPIPlugin extends JavaPlugin {


    @Override
    public void onEnable() {
        Log.init(this);
    }
}
