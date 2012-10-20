package factory;

import java.awt.Color;
import java.util.HashMap;
import java.util.Map;

import org.newdawn.slick.font.effects.ColorEffect;
import org.newdawn.slick.font.effects.GradientEffect;

public class EffectFactory {

    private static EffectFactory factory;
    private Map<Color, ColorEffect> colorEffect;
    private Map<Color, Map<Color, Map<Float, GradientEffect>>> gradientEffect;

    private EffectFactory() {
        this.colorEffect = new HashMap<Color, ColorEffect>();
        this.gradientEffect = new HashMap<Color, Map<Color, Map<Float, GradientEffect>>>();
    }

    public static EffectFactory getInstance() {
        if (EffectFactory.factory == null) {
            EffectFactory.factory = new EffectFactory();
        }
        return EffectFactory.factory;
    }

    public ColorEffect getColorEffect(Color c) {
        if (!this.colorEffect.containsKey(c)) {
            this.colorEffect.put(c, new ColorEffect(c));
        }
        return this.colorEffect.get(c);
    }

    public GradientEffect getGradientEffect(Color topColor, Color bottomColor, float scale) {
        if (!this.gradientEffect.containsKey(topColor)) {
            this.gradientEffect.put(topColor, new HashMap<Color, Map<Float, GradientEffect>>());
        }
        if (!this.gradientEffect.get(topColor).containsKey(bottomColor)) {
            this.gradientEffect.get(topColor)
                    .put(bottomColor, new HashMap<Float, GradientEffect>());
        }
        if (!this.gradientEffect.get(topColor).get(bottomColor).containsKey(scale)) {
            this.gradientEffect.get(topColor).get(bottomColor)
                    .put(scale, new GradientEffect(topColor, bottomColor, scale));
        }
        return this.gradientEffect.get(topColor).get(bottomColor).get(scale);
    }
}
