package life.steeze.hcfplus.FileUtils;

import life.steeze.hcfplus.Objects.Faction;
import life.steeze.hcfplus.HCFPlugin;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FactionsFile {



    private HCFPlugin plugin;

    public FactionsFile(final HCFPlugin plugin){
        this.plugin = plugin;
    }

    private File factionsFile;
    private FileConfiguration factionsData;
    public void loadFactions(){
        factionsFile = new File(plugin.getDataFolder(),"factions.yml");
        if(!factionsFile.exists()){
            factionsFile.getParentFile().mkdir();
            try{
                factionsFile.createNewFile();
            } catch(IOException e){
                e.printStackTrace();
            }
        }
        factionsData = YamlConfiguration.loadConfiguration(factionsFile);
        if(!factionsData.getKeys(false).isEmpty()){
            for(String key : factionsData.getKeys(false)){
                Faction loadedFac = Faction.deserialize(factionsData.getConfigurationSection(key).getValues(false), plugin);
                plugin.getData().addFaction(loadedFac);
            }
        }
    }

    public void saveFactions(){

        //Keep track of factions saved so we can delete any factions from the file that weren't saved (meaning they were deleted).
        List<String> savedFacs = new ArrayList<>();

        for(Faction f : plugin.getData().getFactions()){
            factionsData.set(f.getName(), f.serialize());
            savedFacs.add(f.getName());
        }
        try {
            factionsData.save(factionsFile);
        } catch (IOException e) {
            e.printStackTrace();
        }

        //Delete any disbanded factions
        for(String key : factionsData.getKeys(false)){
            if(!savedFacs.contains(key)){
                factionsData.set(key, null);
            }
        }

        try {
            factionsData.save(factionsFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
