package com.astrodir2.zombieapoc.client.renderer;

import com.astrodir2.zombieapoc.entity.TopHatZombieEntity;
import com.astrodir2.zombieapoc.client.model.TopHatZombieModel;
import net.minecraft.client.render.entity.EntityRendererFactory;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class TopHatZombieRenderer extends GeoEntityRenderer<TopHatZombieEntity> {

    public TopHatZombieRenderer(EntityRendererFactory.Context context) {
        super(context, new TopHatZombieModel());
    }
}
