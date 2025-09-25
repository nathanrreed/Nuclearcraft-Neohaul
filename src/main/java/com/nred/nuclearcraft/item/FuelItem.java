package com.nred.nuclearcraft.item;

import com.nred.nuclearcraft.NuclearcraftNeohaul;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.nred.nuclearcraft.config.Config.FUEL_CONFIG_MAP;
import static com.nred.nuclearcraft.helpers.SimpleHelper.getTimeString;

public class FuelItem extends TooltipItem {
    private final String type;
    private final Integer id;

    private static Map<String, Integer> createMap() {
        HashMap<String, Integer> rtn = new HashMap<>();
        rtn.putAll(Map.of("tbu_tr", 0, "tbu_ox", 1, "tbu_ni", 2, "tbu_za", 3));
        rtn.putAll(Map.of("leu_233_tr", 0, "leu_233_ox", 1, "leu_233_ni", 2, "leu_233_za", 3, "heu_233_tr", 4, "heu_233_ox", 5, "heu_233_ni", 6, "heu_233_za", 7, "leu_235_tr", 8));
        rtn.putAll(Map.of("leu_235_ox", 9, "leu_235_ni", 10, "leu_235_za", 11, "heu_235_tr", 12, "heu_235_ox", 13, "heu_235_ni", 14, "heu_235_za", 15));
        rtn.putAll(Map.of("len_236_tr", 0, "len_236_ox", 1, "len_236_ni", 2, "len_236_za", 3, "hen_236_tr", 4, "hen_236_ox", 5, "hen_236_ni", 6, "hen_236_za", 7));
        rtn.putAll(Map.of("lep_239_tr", 0, "lep_239_ox", 1, "lep_239_ni", 2, "lep_239_za", 3, "hep_239_tr", 4, "hep_239_ox", 5, "hep_239_ni", 6, "hep_239_za", 7, "lep_241_tr", 8));
        rtn.putAll(Map.of("lep_241_ox", 9, "lep_241_ni", 10, "lep_241_za", 11, "hep_241_tr", 12, "hep_241_ox", 13, "hep_241_ni", 14, "hep_241_za", 15));
        rtn.putAll(Map.of("mix_239_tr", 0, "mix_239_ox", 1, "mix_239_ni", 2, "mix_239_za", 3, "mix_241_tr", 4, "mix_241_ox", 5, "mix_241_ni", 6, "mix_241_za", 7));
        rtn.putAll(Map.of("lea_242_tr", 0, "lea_242_ox", 1, "lea_242_ni", 2, "lea_242_za", 3, "hea_242_tr", 4, "hea_242_ox", 5, "hea_242_ni", 6, "hea_242_za", 7));
        rtn.putAll(Map.of("lecm_243_tr", 0, "lecm_243_ox", 1, "lecm_243_ni", 2, "lecm_243_za", 3, "hecm_243_tr", 4, "hecm_243_ox", 5, "hecm_243_ni", 6, "hecm_243_za", 7, "lecm_245_tr", 8));
        rtn.putAll(Map.of("lecm_245_ox", 9, "lecm_245_ni", 10, "lecm_245_za", 11, "hecm_245_tr", 12, "hecm_245_ox", 13, "hecm_245_ni", 14, "hecm_245_za", 15, "lecm_247_tr", 16));
        rtn.putAll(Map.of("lecm_247_ox", 17, "lecm_247_ni", 18, "lecm_247_za", 19, "hecm_247_tr", 20, "hecm_247_ox", 21, "hecm_247_ni", 22, "hecm_247_za", 23));
        rtn.putAll(Map.of("leb_248_tr", 0, "leb_248_ox", 1, "leb_248_ni", 2, "leb_248_za", 3, "heb_248_tr", 4, "heb_248_ox", 5, "heb_248_ni", 6, "heb_248_za", 7));
        rtn.putAll(Map.of("lecf_249_tr", 0, "lecf_249_ox", 1, "lecf_249_ni", 2, "lecf_249_za", 3, "hecf_249_tr", 4, "hecf_249_ox", 5, "hecf_249_ni", 6, "hecf_249_za", 7, "lecf_251_tr", 8));
        rtn.putAll(Map.of("lecf_251_ox", 9, "lecf_251_ni", 10, "lecf_251_za", 11, "hecf_251_tr", 12, "hecf_251_ox", 13, "hecf_251_ni", 14, "hecf_251_za", 15));

        return rtn;
    }

    private static final Map<String, Integer> idMap = createMap();

    public FuelItem(Properties properties, String name, String type) {
        super(properties);
        this.type = type;
        this.id = idMap.get(name);
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        if (this.id == null) return;
        if (!Screen.hasShiftDown()) {
            tooltipComponents.add(shiftForDetails);
        } else {
            if (shiftTooltips == null) {
                shiftTooltips = List.of(Component.translatable(NuclearcraftNeohaul.MODID + ".tooltip.fission_0").withStyle(ChatFormatting.UNDERLINE, ChatFormatting.GRAY),
                        Component.translatable(NuclearcraftNeohaul.MODID + ".tooltip.fission_1", getTimeString(Double.valueOf(FUEL_CONFIG_MAP.get(type).fuel_time().get(id + id / 4)))).withStyle(ChatFormatting.GREEN),
                        Component.translatable(NuclearcraftNeohaul.MODID + ".tooltip.fission_2", FUEL_CONFIG_MAP.get(type).heat_generation().get(id + id / 4)).withStyle(ChatFormatting.YELLOW),
                        Component.translatable(NuclearcraftNeohaul.MODID + ".tooltip.fission_3", (int) (FUEL_CONFIG_MAP.get(type).efficiency().get(id + id / 4) * 100)).withStyle(ChatFormatting.LIGHT_PURPLE),
                        Component.translatable(NuclearcraftNeohaul.MODID + ".tooltip.fission_4", FUEL_CONFIG_MAP.get(type).criticality().get(id + id / 4)).withStyle(ChatFormatting.RED));
            }

            tooltipComponents.addAll(shiftTooltips);
        }
    }
}
