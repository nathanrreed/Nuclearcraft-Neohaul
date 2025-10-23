package com.nred.nuclearcraft.registration;

import com.nred.nuclearcraft.advancements.AssembleTrigger;

import java.util.function.Supplier;

import static com.nred.nuclearcraft.registration.Registers.TRIGGER_TYPES;

public class TriggerTypeRegistration {
    public static final Supplier<AssembleTrigger> ELECTROLYZER_ASSEMBLE_TRIGGER = TRIGGER_TYPES.register("electrolyzer_assembled", AssembleTrigger::new);
    public static final Supplier<AssembleTrigger> DISTILLER_ASSEMBLE_TRIGGER = TRIGGER_TYPES.register("distiller_assembled", AssembleTrigger::new);
    public static final Supplier<AssembleTrigger> INFILTRATOR_ASSEMBLE_TRIGGER = TRIGGER_TYPES.register("infiltrator_assembled", AssembleTrigger::new);
    public static final Supplier<AssembleTrigger> SOLID_FISSION_ASSEMBLE_TRIGGER = TRIGGER_TYPES.register("solid_fission_reactor_assembled", AssembleTrigger::new);
    public static final Supplier<AssembleTrigger> SALT_FISSION_ASSEMBLE_TRIGGER = TRIGGER_TYPES.register("salt_fission_reactor_assembled", AssembleTrigger::new);
    public static final Supplier<AssembleTrigger> HEAT_EXCHANGER_ASSEMBLE_TRIGGER = TRIGGER_TYPES.register("heat_exchanger_assembled", AssembleTrigger::new);
    public static final Supplier<AssembleTrigger> CONDENSER_ASSEMBLE_TRIGGER = TRIGGER_TYPES.register("condenser_assembled", AssembleTrigger::new);
    public static final Supplier<AssembleTrigger> TURBINE_ASSEMBLE_TRIGGER = TRIGGER_TYPES.register("turbine_assembled", AssembleTrigger::new);

    public static void init() {
    }
}