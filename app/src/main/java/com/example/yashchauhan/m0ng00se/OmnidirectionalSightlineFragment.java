/*
 * Copyright (c) 2017 United States Government as represented by the Administrator of the
 * National Aeronautics and Space Administration. All Rights Reserved.
 */

package com.example.yashchauhan.m0ng00se;
//
//import android.view.GestureDetector;
//import android.view.MotionEvent;

import android.view.GestureDetector;
import android.view.MotionEvent;

import gov.nasa.worldwind.Navigator;
import gov.nasa.worldwind.BasicWorldWindowController;
import gov.nasa.worldwind.PickedObject;
import gov.nasa.worldwind.PickedObjectList;
import gov.nasa.worldwind.WorldWind;
import gov.nasa.worldwind.WorldWindow;
import gov.nasa.worldwind.geom.LookAt;
import gov.nasa.worldwind.geom.Position;
import gov.nasa.worldwind.layer.RenderableLayer;
import gov.nasa.worldwind.render.Color;
import gov.nasa.worldwind.render.ImageSource;
import gov.nasa.worldwind.shape.Highlightable;
import gov.nasa.worldwind.shape.OmnidirectionalSightline;
import gov.nasa.worldwind.shape.Placemark;
import gov.nasa.worldwind.shape.PlacemarkAttributes;
import gov.nasa.worldwind.shape.ShapeAttributes;

public class OmnidirectionalSightlineFragment extends BasicGlobeFragment {

    public Placemark sightlinePlacemark;
  //  OmnidirectionalSightline sightline = new OmnidirectionalSightline(position, range);
  protected OmnidirectionalSightline sightline;



    /**
     * Creates a new WorldWindow (GLSurfaceView) object with an OmnidirectionalSightline
     *
     * @return The WorldWindow object containing the globe.
     */
    @Override
    public WorldWindow createWorldWindow() {
        // Let the super class (BasicGlobeFragment) do the creation
        WorldWindow wwd = super.createWorldWindow();

        // Specify the sightline position, which is the origin of the line of sight calculation
        Position position = new Position(46.230, -122.190, 2500.0);
        // Specify the range of the sightline (meters)
        double range = 10000.0;

        // Create the sightline
      //  OmnidirectionalSightline sightline = new OmnidirectionalSightline(position, range);




        // Create a layer for the sightline
//        RenderableLayer sightlineLayer = new RenderableLayer();
//        sightlineLayer.addRenderable(sightline);
//        wwd.getLayers().addLayer(sightlineLayer);

        // Create attributes for the visible terrain
//        ShapeAttributes visibleAttributes = new ShapeAttributes();
//        visibleAttributes.setInteriorColor(new Color(0f, 1f, 0f, 0.5f));
//        // Create attributes for the occluded terrain
//        ShapeAttributes occludedAttributes = new ShapeAttributes();
//        occludedAttributes.setInteriorColor(new Color(0.1f, 0.1f, 0.1f, 0.8f));

        ShapeAttributes viewableRegions = new ShapeAttributes();
        viewableRegions.setInteriorColor(new Color(0f, 0.5f, 0f, 0.5f));

        ShapeAttributes blockedRegions = new ShapeAttributes();
        blockedRegions.setInteriorColor(new Color(0.1f, 0.1f, 0.1f, 0.8f));

        Position pos = new Position(46.202, -122.190, 500.0);
        this.sightline = new OmnidirectionalSightline(pos, 10000.0);
        this.sightline.setAttributes(viewableRegions);
        this.sightline.setOccludeAttributes(blockedRegions);
        this.sightlinePlacemark = new Placemark(pos);
        this.sightlinePlacemark.setAltitudeMode(WorldWind.RELATIVE_TO_GROUND);
        this.sightlinePlacemark.getAttributes().setImageSource(ImageSource.fromResource(R.drawable.aircraft_fixwing));
        this.sightlinePlacemark.getAttributes().setImageScale(2);
        this.sightlinePlacemark.getAttributes().setDrawLeader(true);

//        // Create the sightline
//        OmnidirectionalSightline sightline = new OmnidirectionalSightline(position, range);
        // Set the attributes
//        sightline.setAttributes(visibleAttributes);
//        sightline.setOccludeAttributes(occludedAttributes);

//        // Create a layer for the sightline
        RenderableLayer sightlineLayer = new RenderableLayer();
        sightlineLayer.addRenderable(sightline);
        wwd.getLayers().addLayer(sightlineLayer);

        // Create a Placemark to visualize the position of the sightline
        this.createPlacemark(position, sightlineLayer);
        sightlineLayer.addRenderable(this.sightline);

        sightlineLayer.addRenderable(this.sightlinePlacemark);


        // Position the camera to look at the line of site terrain coverage
        this.positionView(wwd);

        return wwd;
    }

    protected void createPlacemark(Position position, RenderableLayer layer) {
        Placemark placemark = new Placemark(position);
        placemark.getAttributes().setImageSource(ImageSource.fromResource(R.drawable.aircraft_fixwing));
        placemark.getAttributes().setImageScale(2);
        placemark.getAttributes().setDrawLeader(true);
        placemark.setHighlightAttributes(new PlacemarkAttributes(placemark.getAttributes()).setImageScale(4));
        layer.addRenderable(placemark);
    }



    /**
     * This inner class is a custom WorldWindController that handles both picking and navigation via a combination of
     * the native WorldWind navigation gestures and Android gestures. This class' onTouchEvent method arbitrates
     * between pick events and globe navigation events.
     */
    public class PickNavigateController extends BasicWorldWindowController {

        protected Object pickedObject;          // last picked object from onDown events

        protected Object selectedObject;        // last "selected" object from single tap

        /**
         * Assign a subclassed SimpleOnGestureListener to a GestureDetector to handle the "pick" events.
         */
        protected GestureDetector pickGestureDetector = new GestureDetector(
                getContext().getApplicationContext(), new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onDown(MotionEvent event) {
                pick(event);    // Pick the object(s) at the tap location
                return false;   // By not consuming this event, we allow it to pass on to the navigation gesture handlers
            }

            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                toggleSelection();  // Highlight the picked object

                // By not consuming this event, we allow the "up" event to pass on to the navigation gestures,
                // which is required for proper zoom gestures.  Consuming this event will cause the first zoom
                // gesture to be ignored.  As an alternative, you can implement onSingleTapConfirmed and consume
                // event as you would expect, with the trade-off being a slight delay tap response.
                return false;
            }
        });

        /**
         * Delegates events to the pick handler or the native WorldWind navigation handlers.
         */
        @Override
        public boolean onTouchEvent(MotionEvent event) {
            // Allow pick listener to process the event first.
            boolean consumed = this.pickGestureDetector.onTouchEvent(event);

            // If event was not consumed by the pick operation, pass it on the globe navigation handlers
            if (!consumed) {

                // The super class performs the pan, tilt, rotate and zoom
                return super.onTouchEvent(event);
            }
            return consumed;
        }

        /**
         * Performs a pick at the tap location.
         */
        public void pick(MotionEvent event) {
            // Forget our last picked object
            this.pickedObject = null;

            // Perform a new pick at the screen x, y
            PickedObjectList pickList = getWorldWindow().pick(event.getX(), event.getY());

            // Get the top-most object for our new picked object
            PickedObject topPickedObject = pickList.topPickedObject();
            if (topPickedObject != null) {
                this.pickedObject = topPickedObject.getUserObject();
            }
        }

        /**
         * Toggles the selected state of a picked object.
         */
        public void toggleSelection() {

            // Display the highlight or normal attributes to indicate the
            // selected or unselected state respectively.
            if (pickedObject instanceof Highlightable) {

                // Determine if we've picked a "new" object so we know to deselect the previous selection
                boolean isNewSelection = pickedObject != this.selectedObject;

                // Only one object can be selected at time, deselect any previously selected object
                if (isNewSelection && this.selectedObject instanceof Highlightable) {
                    ((Highlightable) this.selectedObject).setHighlighted(false);
                }

                // Show the selection by showing its highlight attributes
                ((Highlightable) pickedObject).setHighlighted(isNewSelection);
                this.getWorldWindow().requestRedraw();

                // Track the selected object
                this.selectedObject = isNewSelection ? pickedObject : null;
            }
        }
    }



    protected void positionView(WorldWindow wwd) {
        LookAt lookAt = new LookAt().set(46.230, -122.190, 500, WorldWind.ABSOLUTE, 1.5e4 /*range*/, 45.0 /*heading*/, 70.0 /*tilt*/, 0 /*roll*/);
        wwd.getNavigator().setAsLookAt(this.getWorldWindow().getGlobe(), lookAt);
    }
}
