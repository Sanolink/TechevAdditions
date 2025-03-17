package com.sanolink.techev_additions.event;

import com.sanolink.techev_additions.TechevAdditions;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.event.level.NoteBlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Mod.EventBusSubscriber(modid = TechevAdditions.MOD_ID)
public class TestNoteBlockEvent {
    private static final List<Task> tasks = new ArrayList<>();

    private static class Task {
        int delay;
        Runnable action;

        Task(int delay, Runnable action) {
            this.delay = delay;
            this.action = action;
        }
    }

    @SubscribeEvent
    public static void onServerTick(net.minecraftforge.event.TickEvent.ServerTickEvent event) {
        Iterator<Task> iterator = tasks.iterator();
        while (iterator.hasNext()) {
            Task task = iterator.next();
            task.delay--;

            if (task.delay <= 0) {
                task.action.run();
                iterator.remove();
            }
        }
    }

    @SubscribeEvent
    public static void SpawnCowAndSoundWave(NoteBlockEvent.Play event) {
        TechevAdditions.LOGGER.info("Event Call avant Spawn");

        LevelAccessor levelAccessor = event.getLevel();
        if (levelAccessor instanceof ServerLevel level) {
            BlockPos pos = event.getPos().above();

            EntityType.COW.spawn(level, null, null, pos, null, true, true);

            for (int radius = 1; radius <= 10; radius++) {
                int delay = radius * 5;
                int currentRadius = radius;

                tasks.add(new Task(delay, () -> {

                    for (double theta = 0; theta < Math.PI * 2; theta += Math.PI / 8) {
                        for (double phi = 0; phi < Math.PI; phi += Math.PI / 8) {
                            double x = pos.getX() + currentRadius * Math.sin(phi) * Math.cos(theta);
                            double y = pos.getY() + currentRadius * Math.sin(phi) * Math.sin(theta);
                            double z = pos.getZ() + currentRadius * Math.cos(phi);
                            level.sendParticles(
                                    net.minecraft.core.particles.ParticleTypes.NOTE,
                                    x, y, z,
                                    1,
                                    0, 0, 0, 0
                            );
                        }
                    }

                    AABB boundingBox = new AABB(
                            pos.getX() - currentRadius, pos.getY() - currentRadius, pos.getZ() - currentRadius,
                            pos.getX() + currentRadius, pos.getY() + currentRadius, pos.getZ() + currentRadius
                    );

                    List<Entity> entities = level.getEntitiesOfClass(Entity.class, boundingBox);

                    for (Entity entity : entities) {
                        if (entity instanceof Player player && player.distanceToSqr(pos.getX(), pos.getY(), pos.getZ()) <= currentRadius * currentRadius) {
                            player.hurt(DamageSource.MAGIC, 2.0F);
                        }
                    }
                }));
            }
        }
    }
}