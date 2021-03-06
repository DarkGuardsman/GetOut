package com.builtbroken.puntanimal;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(modid = PuntAnimal.MODID, name = PuntAnimal.MOD_NAME, version = PuntAnimal.VERSION, acceptableRemoteVersions = "*")
@Mod.EventBusSubscriber()
public class PuntAnimal
{
    public static final Logger LOGGER = LogManager.getLogger();

    public static final String MODID = "sbmpuntanimal";
    public static final String MOD_NAME = "SBM-Punt-Animal";

    //Version injection data
    public static final String MAJOR_VERSION = "@MAJOR@";
    public static final String MINOR_VERSION = "@MINOR@";
    public static final String REVISION_VERSION = "@REVIS@";
    public static final String BUILD_VERSION = "@BUILD@";
    public static final String MC_VERSION = "@MC@";
    public static final String VERSION = MC_VERSION + "-" + MAJOR_VERSION + "." + MINOR_VERSION + "." + REVISION_VERSION + "." + BUILD_VERSION;

    @SubscribeEvent
    public static void onDamage(LivingAttackEvent event)
    {
        try
        {
            final Entity source = event.getSource().getTrueSource();
            if (source instanceof EntityPlayer)
            {
                EntityPlayer attacker = (EntityPlayer) source;
                EntityLivingBase victim = event.getEntityLiving();

                if (attacker.isSneaking() && victim instanceof EntityAnimal) //TODO set allow/block list for victim
                {
                    event.setCanceled(true);

                    int xr = (int) -(victim.posX - attacker.posX);
                    int zr = (int) -(victim.posZ - attacker.posZ);

                    //When xr & zr == 0, both are on the same block, we don't know where to push
                    if (xr != 0 || zr != 0)
                    {
                        victim.knockBack(attacker, 0.5F, xr, zr);
                    }
                    //TODO push randomly into nearest free spot for else
                }
            }
        }
        catch (Exception e)
        {
            LOGGER.error("Unexpected error while pushing animal", e);
        }
    }
}
