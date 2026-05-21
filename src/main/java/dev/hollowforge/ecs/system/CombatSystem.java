package dev.hollowforge.ecs.system;

import dev.hollowforge.ecs.component.Damage;
import dev.hollowforge.ecs.component.Health;

public class CombatSystem {

    public void applyDamage(Health target, Damage damage) {
        if (target == null || damage == null) return;
        target.setCurrent(target.getCurrent() - damage.getAmount());
    }

    public boolean isTargetDead(Health health) {
        return health == null || !health.isAlive();
    }
}
