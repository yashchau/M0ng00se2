package com.example.yashchauhan.m0ng00se;

//import android.app.Fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;

import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;


import gov.nasa.worldwind.WorldWind;
import gov.nasa.worldwind.WorldWindow;
import gov.nasa.worldwind.geom.LookAt;
import gov.nasa.worldwind.geom.Offset;
import gov.nasa.worldwind.geom.Position;
import gov.nasa.worldwind.globe.BasicElevationCoverage;
import gov.nasa.worldwind.layer.BackgroundLayer;
import gov.nasa.worldwind.layer.BlueMarbleLandsatLayer;
import gov.nasa.worldwind.layer.RenderableLayer;
import gov.nasa.worldwind.render.ImageSource;
import gov.nasa.worldwind.shape.Placemark;
import gov.nasa.worldwind.shape.PlacemarkAttributes;

/**
 * Created by yashchauhan on 31/10/17.
 */

public class BasicGlobeFragment extends Fragment {

    private WorldWindow wwd;
    private static final double NORMAL_IMAGE_SCALE = 3.0;

    private static final double HIGHLIGHTED_IMAGE_SCALE = 4.0;

   // WorldWindow wwd = super.createWorldWindow();

    RenderableLayer layer = new RenderableLayer("Placemarks");



    public BasicGlobeFragment() {
    }

    /**
     * Creates a new WorldWindow (GLSurfaceView) object.
     */
    public WorldWindow createWorldWindow() {
        // Create the WorldWindow (a GLSurfaceView) which displays the globe.
        this.wwd = new WorldWindow(getContext());

        // Setup the WorldWindow's layers.
       this.wwd.getLayers().addLayer(new BackgroundLayer());
        this.wwd.getLayers().addLayer(new BlueMarbleLandsatLayer());
        // Setup the WorldWindow's elevation coverages.
        this.wwd.getGlobe().getElevationModel().addCoverage(new BasicElevationCoverage());
      //  RenderableLayer layer = new RenderableLayer("Placemarks");
      //  wwd.setWorldWindowController(new PlacemarksPickingFragment.PickNavigateController());

      //  wwd.getLayers().addLayer(layer);

        return this.wwd;
    }

    /**
     * Gets the WorldWindow (GLSurfaceView) object.
     */
    public WorldWindow getWorldWindow() {
        return this.wwd;
    }


    /**
     * Adds the WorldWindow to this Fragment's layout.
     */
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_globe, container, false);
        FrameLayout globeLayout = (FrameLayout) rootView.findViewById(R.id.globe);

        globeLayout.addView(this.createWorldWindow());


        return rootView;
    }


    /**
     * Resumes the WorldWindow's rendering thread
     */
    @Override
    public void onResume() {
        super.onResume();
        this.wwd.onResume(); // resumes a paused rendering thread
    }

    /**
     * Pauses the WorldWindow's rendering thread
     */
    @Override
    public void onPause() {
        super.onPause();
        this.wwd.onPause(); // pauses the rendering thread
    }
}
