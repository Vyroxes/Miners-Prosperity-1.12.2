package net.vyroxes.minersprosperity.client.sound;

import net.minecraft.client.audio.MovingSound;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.vyroxes.minersprosperity.block.machine.BlockMachine;
import net.vyroxes.minersprosperity.tile.TileEntityMachine;

public class MachineLoopSound extends MovingSound {
    private final TileEntityMachine tileEntity;

    public MachineLoopSound(TileEntityMachine tileEntity, SoundEvent sound)
    {
        super(sound, SoundCategory.BLOCKS);

        this.tileEntity = tileEntity;

        this.repeat = true;
        this.repeatDelay = 0;
        this.volume = tileEntity.getActiveSoundVolume();
        this.pitch = 1.0F;

        this.xPosF = tileEntity.getPos().getX() + 0.5F;
        this.yPosF = tileEntity.getPos().getY() + 0.5F;
        this.zPosF = tileEntity.getPos().getZ() + 0.5F;
    }

    @Override
    public void update() { if (this.tileEntity.isInvalid() || BlockMachine.getMachineState(this.tileEntity.getWorld(), this.tileEntity.getPos()) != BlockMachine.MachineState.ACTIVE) this.donePlaying = true; }
}