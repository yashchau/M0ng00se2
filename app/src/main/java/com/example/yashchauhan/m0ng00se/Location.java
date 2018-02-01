package com.example.yashchauhan.m0ng00se;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import gov.nasa.worldwind.WorldWind;
import gov.nasa.worldwind.WorldWindow;
import gov.nasa.worldwind.geom.LookAt;
import gov.nasa.worldwind.geom.Offset;
import gov.nasa.worldwind.geom.Position;
import gov.nasa.worldwind.layer.RenderableLayer;
import gov.nasa.worldwind.render.ImageSource;
import gov.nasa.worldwind.shape.Placemark;
import gov.nasa.worldwind.shape.PlacemarkAttributes;


public class Location extends BasicGlobeFragment {

    private static final double NORMAL_IMAGE_SCALE = 3.0;

    private static final double HIGHLIGHTED_IMAGE_SCALE = 4.0;
    String mParam1,mlon,mlat;
    double lat,lon;


    @Override
    public WorldWindow createWorldWindow(){
        WorldWindow wwd = super.createWorldWindow();

        RenderableLayer layer = new RenderableLayer("Location");
        wwd.getLayers().addLayer(layer);

//        layer.addRenderable(createAircraftPlacemark(Position.fromDegrees(12.9723385, 77.6853295, 2000)));


        if (getArguments() != null) {
            mParam1 = getArguments().getString("lat");
            lat = getArguments().getDouble("lat");
            lon = getArguments().getDouble("long");
            Log.d("checking",mParam1);
            mlat = String.valueOf(lat);
            mlon = String.valueOf(lon);
        }

        mlat = String.valueOf(lat);
        mlon = String.valueOf(lon);

        layer.addRenderable(createAircraftPlacemark(Position.fromDegrees(lat, lon, 2000)));
        Log.d("checking",mlat);
        Log.d("longitude", mlon);




        LookAt lookAt = new LookAt().set(lat, lon, 0, WorldWind.ABSOLUTE, 2e4 /*range*/, 0 /*heading*/, 45 /*tilt*/, 0 /*roll*/);
        wwd.getNavigator().setAsLookAt(wwd.getGlobe(), lookAt);

        return wwd;
    }

    private static Placemark createAircraftPlacemark(Position position) {
        Placemark placemark = Placemark.createWithImage(position, ImageSource.fromResource(R.drawable.aircraft_fighter));
        placemark.getAttributes().setImageOffset(Offset.bottomCenter()).setImageScale(NORMAL_IMAGE_SCALE).setDrawLeader(true);
        placemark.setHighlightAttributes(new PlacemarkAttributes(placemark.getAttributes()).setImageScale(HIGHLIGHTED_IMAGE_SCALE));
        return placemark;
    }




}
