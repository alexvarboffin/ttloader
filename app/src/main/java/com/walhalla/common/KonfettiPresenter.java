package com.walhalla.common;

import android.graphics.drawable.Drawable;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.walhalla.ttvloader.R;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import nl.dionsegijn.konfetti.core.Angle;
import nl.dionsegijn.konfetti.core.Party;
import nl.dionsegijn.konfetti.core.PartyFactory;
import nl.dionsegijn.konfetti.core.Position;
import nl.dionsegijn.konfetti.core.Spread;
import nl.dionsegijn.konfetti.core.emitter.Emitter;
import nl.dionsegijn.konfetti.core.emitter.EmitterConfig;
import nl.dionsegijn.konfetti.core.models.Shape;
import nl.dionsegijn.konfetti.xml.KonfettiView;
import nl.dionsegijn.konfetti.xml.listeners.OnParticleSystemUpdateListener;


public class KonfettiPresenter {

    KonfettiView kv0;
    private final Shape.DrawableShape drawableShape;

    public KonfettiPresenter(KonfettiView kv0) {
        this.kv0 = kv0;
        final Drawable drawable = ContextCompat.getDrawable(
                kv0.getContext().getApplicationContext(), R.drawable.ic_konfetti_heart);
        if (drawable != null) {
            drawableShape = new Shape.DrawableShape(drawable, true, true);
        } else {
            drawableShape = null;
        }

//        EmitterConfig emitterConfig = new Emitter(5L, TimeUnit.SECONDS).perSecond(50);
//        party = new PartyFactory(emitterConfig)
//                .angle(270)
//                .spread(90)
//                .setSpeedBetween(1f, 5f)
//                .timeToLive(2000L)
//                .shapes(new Shape.Rectangle(0.2f), drawableShape)
//                .sizes(new Size(12, 5f, 0.2f))
//                .position(0.0, 0.0, 1.0, 0.0)
//                .build();
    }

    public void rain() {
        EmitterConfig emitterConfig = new Emitter(5, TimeUnit.SECONDS).perSecond(100);
        kv0.start(
                new PartyFactory(emitterConfig)
                        .angle(Angle.BOTTOM)
                        .spread(Spread.ROUND)
                        .shapes(Arrays.asList(Shape.Square.INSTANCE, Shape.Circle.INSTANCE, drawableShape))
                        .colors(Arrays.asList(0xfce18a, 0xff726d, 0xf4306d, 0xb48def))
                        .setSpeedBetween(0f, 15f)
                        .position(new Position.Relative(0.0, 0.0).between(new Position.Relative(1.0, 0.0)))
                        .build());
    }

    public void explode() {
        EmitterConfig emitterConfig = new Emitter(100L, TimeUnit.MILLISECONDS)
                .max(100);
        kv0.setOnParticleSystemUpdateListener(new OnParticleSystemUpdateListener() {
            @Override
            public void onParticleSystemStarted(@NonNull KonfettiView konfettiView, @NonNull Party party, int i) {

            }

            @Override
            public void onParticleSystemEnded(@NonNull KonfettiView konfettiView, @NonNull Party party, int i) {

            }
        });
        kv0.start(
                new PartyFactory(emitterConfig)
                        .spread(360)
                        .shapes(Arrays.asList(Shape.Square.INSTANCE, Shape.Circle.INSTANCE, drawableShape))
                        .colors(Arrays.asList(0xfce18a, 0xff726d, 0xf4306d, 0xb48def))
                        .setSpeedBetween(0f, 30f)
                        .position(new Position.Relative(0.5, 0.3))
                        .build());
    }
}
