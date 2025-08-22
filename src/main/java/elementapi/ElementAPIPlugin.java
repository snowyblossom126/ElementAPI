package elementapi;

import elementapi.utilities.Log;
import org.bukkit.plugin.java.JavaPlugin;

public final class ElementAPIPlugin extends JavaPlugin {


    @Override
    public void onEnable() {
        Log.init(this);
    }
}
