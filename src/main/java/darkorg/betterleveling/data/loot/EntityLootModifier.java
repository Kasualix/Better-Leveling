package darkorg.betterleveling.data.loot;

import com.google.gson.JsonObject;
import darkorg.betterleveling.api.ISkill;
import darkorg.betterleveling.capability.PlayerCapabilityProvider;
import darkorg.betterleveling.util.CapabilityUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootContext;
import net.minecraft.loot.LootParameters;
import net.minecraft.loot.conditions.ILootCondition;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.loot.GlobalLootModifierSerializer;
import net.minecraftforge.common.loot.LootModifier;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Random;

public class EntityLootModifier extends LootModifier {
    private final Item addition;
    private final ISkill playerSkill;

    public EntityLootModifier(ILootCondition[] conditionsIn, Item pAddition, ISkill pPlayerSkill) {
        super(conditionsIn);
        this.addition = pAddition;
        this.playerSkill = pPlayerSkill;
    }

    @Nonnull
    @Override
    protected List<ItemStack> doApply(List<ItemStack> generatedLoot, LootContext context) {
        if (context.has(LootParameters.KILLER_ENTITY)) {
            Entity entity = context.get(LootParameters.KILLER_ENTITY);
            if (entity instanceof ServerPlayerEntity) {
                ServerPlayerEntity serverPlayerEntity = (ServerPlayerEntity) entity;
                serverPlayerEntity.getCapability(PlayerCapabilityProvider.PLAYER_CAP).ifPresent(capability -> {
                    if (capability.isUnlocked(serverPlayerEntity, this.playerSkill)) {
                        int level = capability.getLevel(serverPlayerEntity, this.playerSkill);
                        if (level > 0) {
                            Random random = new Random();
                            float chance = (float) level / ((float) level + 3.0F);
                            for (int i = 0; i < level; i++) {
                                if (random.nextFloat() > chance) {
                                    generatedLoot.add(new ItemStack(this.addition));
                                }
                            }
                        }
                    }
                });
            }
        }
        return generatedLoot;
    }

    public static class Serializer extends GlobalLootModifierSerializer<EntityLootModifier> {
        @Override
        public EntityLootModifier read(ResourceLocation pName, JsonObject pObject, ILootCondition[] pConditionsIn) {
            Item addition = ForgeRegistries.ITEMS.getValue(new ResourceLocation(JSONUtils.getString(pObject, "addition")));
            ISkill playerSkill = CapabilityUtil.getSkillFromName(JSONUtils.getString(pObject, "skillName"));
            return new EntityLootModifier(pConditionsIn, addition, playerSkill);
        }

        @Override
        public JsonObject write(EntityLootModifier instance) {
            JsonObject json = makeConditions(instance.conditions);
            json.addProperty("addition", ForgeRegistries.ITEMS.getKey(instance.addition).toString());
            json.addProperty("skillName", instance.playerSkill.getName());
            return json;
        }
    }
}
