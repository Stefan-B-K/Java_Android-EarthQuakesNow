package com.istef.earthquakesnow.data;

import com.istef.earthquakesnow.model.EarthQuake;


public interface AsyncJsonResponse {
    void handle(EarthQuake earthQuakeList);
}
