package com.example.hw1;

import android.content.Context;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.hw1.GamePlayFinals;
import com.example.hw1.GameUser;
import com.example.hw1.R;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class UserMapFragment extends Fragment implements OnMapReadyCallback {

    private View view;
    private GoogleMap map;
    private GameUser userPassed;
    private CameraPosition cameraPosition;
    private Button displayLocationButton;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_map, container, false);
        return view;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        if(getContext() != null) {

            MapsInitializer.initialize(getContext());
            map = googleMap;
            map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
            map.addMarker(new MarkerOptions().position(new LatLng(GamePlayFinals.AFEKA_LATITUDE, GamePlayFinals.AFEKA_LONGITUDE)).title(getString(R.string.location_title)).snippet(getString(R.string.location_subtitle)));
            cameraPosition = CameraPosition.builder().target(new LatLng(GamePlayFinals.AFEKA_LATITUDE, GamePlayFinals.AFEKA_LONGITUDE)).zoom(16).bearing(0).tilt(45).build();
            map.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
            displayLocationButton = view.findViewById(R.id.displayLocationButton);

            displayLocationButton.setOnClickListener(e -> {
                if (userPassed != null) {
                    map.addMarker(new MarkerOptions().position(new LatLng(userPassed.getLatitude(), userPassed.getLongitude())).snippet("I hope to go there"));
                    cameraPosition = CameraPosition.builder().target(new LatLng(userPassed.getLatitude(), userPassed.getLongitude())).zoom(16).bearing(0).tilt(45).build();
                   // map.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                    CameraUpdate location = CameraUpdateFactory.newLatLngZoom(new LatLng(userPassed.getLatitude(),userPassed.getLongitude()), 15);
                    map.animateCamera(location);
                }
            });
        }
    }

    public void getGameUserInfo(GameUser gameUser) {
        userPassed = gameUser;
    }

    public void setButtonText(String text){
        StringBuilder sb = new StringBuilder();
        sb.append(getString(R.string.show_player_location));
        displayLocationButton.setText(sb.append("-").append(text));

    }
    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        MapView mMapView = view.findViewById(R.id.map);
        if (mMapView != null) {
            mMapView.onCreate(null);
            mMapView.onResume();
            mMapView.getMapAsync(this);
        }

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        view = null; // now cleaning up!
    }


}

