package darkorg.betterleveling.data.client;

import darkorg.betterleveling.BetterLeveling;
import darkorg.betterleveling.registry.ModItems;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

public class ModItemModelProvider extends ItemModelProvider {
    public ModItemModelProvider(DataGenerator pOutput, ExistingFileHelper pExistingFileHelper) {
        super(pOutput, BetterLeveling.MOD_ID, pExistingFileHelper);
    }

    @Override
    protected void registerModels() {
        basicItem(ModItems.RAW_DEBRIS.get());
    }
}
