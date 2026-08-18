package net.vyroxes.minersprosperity.client.sound;

import net.minecraft.client.Minecraft;
import net.minecraft.util.SoundEvent;
import net.vyroxes.minersprosperity.block.machine.BlockMachine;
import net.vyroxes.minersprosperity.tile.TileEntityMachine;
import java.util.Map;
import java.util.WeakHashMap;

public class MachineSoundManager {
    private static final Map<TileEntityMachine, MachineLoopSound> SOUNDS = new WeakHashMap<>();

    public static void update(TileEntityMachine tileEntity)
    {
        if (!tileEntity.getWorld().isRemote) return;

        BlockMachine.MachineState state = BlockMachine.getMachineState(tileEntity.getWorld(), tileEntity.getPos());

        MachineLoopSound currentSound = SOUNDS.get(tileEntity);

        if (state == BlockMachine.MachineState.ACTIVE)
        {
            if (currentSound == null || currentSound.isDonePlaying())
            {
                SoundEvent sound = tileEntity.getActiveSound();

                if (sound == null) return;

                MachineLoopSound machineSound = new MachineLoopSound(tileEntity, sound);

                SOUNDS.put(tileEntity, machineSound);
                Minecraft.getMinecraft().getSoundHandler().playSound(machineSound);
            }
        }
        else if (currentSound != null) SOUNDS.remove(tileEntity);
    }
}